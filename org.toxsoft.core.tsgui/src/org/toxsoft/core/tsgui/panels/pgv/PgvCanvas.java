package org.toxsoft.core.tsgui.panels.pgv;

import static org.toxsoft.core.tsgui.panels.pgv.IPicsGridViewerConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.txtsplit.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tsgui.utils.margins.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.derivative.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Canvas in {@link ScrolledComposite}, which actually draws the grid of thumbnails.
 * <p>
 * More precisely, it draws rows of cells, with wrapping, in order from left to right, top to bottom.
 *
 * @author hazard157
 * @param <V> - entity type
 */
class PgvCanvas<V>
    extends TsAbstractCanvas
    implements ITsUserInputListener, IThumbSizeableEx {

  /**
   * An element that contains the entity and its thumbnail to render.
   *
   * @author hazard157
   */
  class PgvItem
      implements IImageAnimationCallback, ICloseable {

    final V   entity;
    final int cellIndex;

    private IList<TextLine> labelLines = IList.EMPTY;

    private IImageAnimator iman         = null;
    private TsImage        tsimg        = null;
    private ITsPoint       imgSize      = ITsPoint.ZERO;
    private int            currentIndex = 0;

    public PgvItem( V aEntity, int aCellIndex ) {
      entity = aEntity;
      cellIndex = aCellIndex;
    }

    void setItemLabel( IList<TextLine> aLabelLines ) {
      labelLines = aLabelLines;
    }

    void paintItem( GC aGc, ITsRectangle aRect ) {
      boolean isLabels = OPDEF_IS_LABELS_SHOWN.getValue( tsContext().params() ).asBool();
      // draw image centered
      Image image = getCurrFrame();
      if( image != null ) {
        int x = aRect.x1() + aRect.width() / 2;
        int y = aRect.y1() + aRect.width() / 2; // use Width() for square part
        aGc.drawImage( image, x - imgSize.x() / 2, y - imgSize.y() / 2 );
      }
      // draw labels
      if( isLabels ) {
        /**
         * Note about lineAreaY<br>
         * If text labels are shown then the cell is the vertically elongated rectangle. Top square part of rectangle is
         * for icon and the lower part is for text. So we'll start drawing text after the bottom line of the square
         * part.
         */
        int lineAreaY = aRect.y1() + aRect.width(); // yes, width! not a height
        for( TextLine t : labelLines ) {
          String s = t.text();
          ITsPoint p = t.bounds();
          // line area center coordinates
          int centerX = aRect.x1() + aRect.width() / 2;
          int centerY = lineAreaY + textLineHeight / 2;
          // draw center aligned text
          aGc.drawText( s, centerX - p.x() / 2, centerY - p.y() / 2 );
          lineAreaY += textLineHeight;
        }
      }
    }

    Image getCurrFrame() {
      if( tsimg != null && currentIndex >= 0 && currentIndex < tsimg.count() && !tsimg.isDisposed() ) {
        if( forceStill ) {
          return tsimg.image();
        }
        return tsimg.frames().get( currentIndex );
      }
      return null;
    }

    V getEntity() {
      return entity;
    }

    /**
     * Uploads images in memory from the disk files.
     * <p>
     * The method must be called from the main GUI thread. In order to avoid freezing the GUI, you should organize
     * sequential call of these methods for a sequence of {@link PgvItem} items via {@link Display#asyncExec(Runnable)}.
     * This is what is done in {@link PgvCanvas#loadNextImage()}. *
     */
    void reloadImage() {
      if( iman != null ) {
        animationSupport.unregister( iman );
        iman = null;
        currentIndex = 0;
      }
      if( OPDEF_IS_ICONS_INSTEAD_OF_THUMBS.getValue( tsContext().params() ).asBool() ) {
        EIconSize iconSize = EIconSize.findIncluding( thumbSize.size(), thumbSize.size() );
        Image swtImage = visualsProvider.getIcon( entity, iconSize );
        if( swtImage != null ) {
          tsimg = TsImage.create( swtImage );
        }
        imgSize = iconSize.pointSize();
      }
      else {
        tsimg = visualsProvider.getThumb( entity, thumbSize );
        if( tsimg != null ) {
          if( tsimg.isAnimated() ) {
            iman = animationSupport.registerImage( tsimg, PgvItem.this, entity );
            iman.resume();
          }
        }
        imgSize = thumbSize.pointSize();
      }
      if( tsimg != null ) {
        redrawCell( cellIndex );
      }
    }

    // ------------------------------------------------------------------------------------
    // IImageAnimationCallback
    //

    @Override
    public void onDrawFrame( IImageAnimator aImageAnimator, int aIndex, Object aUserData ) {
      currentIndex = aIndex;
      redrawCell( cellIndex );
    }

    @Override
    public void close() {
      if( iman != null ) {
        animationSupport.unregister( iman );
        iman = null;
      }
    }

  }

  /**
   * The pop-up tooltip implementation.
   *
   * @author hazard157
   */
  class EntityTooltip
      extends ToolTip {

    EntityTooltip( Control control ) {
      super( control );
    }

    @Override
    protected Composite createToolTipContentArea( Event aEvent, Composite aParent ) {
      CLabel label = new CLabel( aParent, SWT.SHADOW_NONE );
      Color bk = colorManager().getColor( ETsColor.WHITE );
      label.setBackground( bk );
      Color fg = colorManager().getColor( ETsColor.BLACK );
      label.setForeground( fg );
      int index = cellsGrid.getIndexAtCoors( aEvent.x, aEvent.y );
      label.setText( visualsProvider.getDescription( items.get( index ).getEntity() ) );
      return label;
    }

    @Override
    protected boolean shouldCreateToolTip( Event aEvent ) {
      if( super.shouldCreateToolTip( aEvent ) ) {
        return visualsProvider != ITsVisualsProvider.DEFAULT && cellsGrid.getIndexAtCoors( aEvent.x, aEvent.y ) >= 0;
      }
      return false;
    }

  }

  final IQueue<PgvItem> itemsWithImagesToLoadQueue = new Queue<>();

  final IAnimationSupport animationSupport;
  final ToolTip           tooltipControl;
  final PicsGridViewer<V> owner;

  ITsVisualsProvider<V> visualsProvider = ITsVisualsProvider.DEFAULT;

  /**
   * For owner to add/remove listeners.
   */
  public final TsUserInputEventsBinder userInputEventsBinder;

  // ------------------------------------------------------------------------------------
  // thumbSize
  private final GenericChangeEventer thumbSizeChangeEventer;
  private final EThumbSize           defaultThumbSize;
  private EThumbSize                 thumbSize = EThumbSize.SZ256;

  // ------------------------------------------------------------------------------------
  // cells
  final IListEdit<PgvItem> items     = new ElemArrayList<>( 256 );
  final CellsGrid          cellsGrid = new CellsGrid();

  int textLineHeight = 1; // calculated in getTextAreaHeight()

  // ------------------------------------------------------------------------------------
  // selection
  int selectedIndex = -1;

  private boolean selfResize = false;
  private boolean forceStill = false;

  /**
   * Constructor with no style bits.
   * <p>
   * Stores reference to context.
   *
   * @param aParent {@link Composite} - parent composite
   * @param aContext {@link ITsGuiContext} - the context
   * @param aOwner {@link PicsGridViewer} - the owner
   */
  public PgvCanvas( Composite aParent, ITsGuiContext aContext, PicsGridViewer<V> aOwner ) {
    super( aParent, aContext );
    owner = TsNullArgumentRtException.checkNull( aOwner );
    thumbSizeChangeEventer = new GenericChangeEventer( owner );
    userInputEventsBinder = new TsUserInputEventsBinder( aOwner );
    userInputEventsBinder.bindToControl( this, TsUserInputEventsBinder.BIND_ALL_INPUT_EVENTS );
    userInputEventsBinder.addTsUserInputListener( this );
    animationSupport = new AnimationSupport( getDisplay(), 10 );
    animationSupport.resume();
    defaultThumbSize = OPDEF_DEFAULT_THUMB_SIZE.getValue( tsContext().params() ).asValobj();
    thumbSize = defaultThumbSize;
    tooltipControl = new EntityTooltip( this );
    cellsGrid.setMargins( new TsGridMargins( 3 ) );
    cellsGrid.eventer().addListener( aSource -> adjustCanvasHeight() );
    adjustCellSize();
    adjustCanvasHeight();
  }

  @Override
  protected void doDispose() {
    animationSupport.dispose();
  }

  // ------------------------------------------------------------------------------------
  // i9mplementation
  //

  /**
   * Redraws the cell specified by the index.
   * <p>
   * For invalid index does nothing.
   *
   * @param aIndex int - the cell index, that is index in the {@link #items} list
   */
  void redrawCell( int aIndex ) {
    if( aIndex >= 0 && aIndex < cellsGrid.getCellsCount() ) {
      ITsRectangle r = cellsGrid.getCell( aIndex );
      int bw = cellsGrid.margins().borderWidth();
      redraw( r.x1() - bw - 1, r.y1() - bw - 1, r.width() + 2 * bw + 2, r.height() + 2 * bw + 2, false );
    }
  }

  /**
   * Adjusts the height of the canvas to the height computed in {@link #cellsGrid} to fit all grid rows.
   * <p>
   * When the user changes the size of the window (panel), the width of the canvas changes, with a given number of
   * cells, the height should be changed so that all the cells fit in the canvas in order from left to right and top to
   * bottom.
   *
   * @return boolean - a sign that the height has really been changed
   */
  boolean adjustCanvasHeight() {
    selfResize = true;
    try {
      int h = cellsGrid.getCanvasHeight();
      Point sz = getSize();
      if( sz.y != h ) {
        setSize( sz.x, h );
        Composite grandma = getParent().getParent();
        grandma.layout( true, true );
        return true;
      }
    }
    finally {
      selfResize = false;
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsE4Canvas
  //

  @Override
  public void paint( ITsGraphicsContext aTsGc, ITsRectangle aPaintBounds ) {
    TsBorderInfo borderInfo = OPDEF_SELECTION_BORDER_SETTINGS.getValue( tsContext().params() ).asValobj();
    // draw loop over all cells
    for( int i = 0, count = cellsGrid.getCellsCount(); i < count; i++ ) {
      PgvItem item = items.get( i );
      ITsRectangle r = cellsGrid.getCell( i );
      item.paintItem( aTsGc.gc(), r );
      // draw border on selected cell only
      if( i == selectedIndex ) {
        Color oldFg = aTsGc.gc().getForeground();
        TsGraphicsUtils.drawBorder( aTsGc.gc(), borderInfo, r, colorManager() );
        aTsGc.gc().setForeground( oldFg );
      }
    }
  }

  @Override
  protected void doControlResized( ControlEvent aEvent ) {
    if( !selfResize ) {
      Point p = getSize();
      cellsGrid.setCanvasWidth( p.x );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputListener
  //

  @Override
  public boolean onMouseDoubleClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors,
      Control aWidget ) {
    int index = cellsGrid.getIndexAtCoors( aCoors.x(), aCoors.y() );
    V entity = index >= 0 ? items.get( index ).getEntity() : null;
    owner.fireTsDoubleClickEvent( entity );
    return false;
  }

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    int index = cellsGrid.getIndexAtCoors( aCoors.x(), aCoors.y() );
    pgvSetSelectedIndex( index );
    return false;
  }

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    int index = selectedIndex;
    int col = -1, row = -1;
    if( index >= 0 ) {
      col = index % cellsGrid.getColsCount();
      row = index / cellsGrid.getColsCount();
    }
    switch( aCode ) {
      case SWT.ARROW_UP: {
        if( --row >= 0 ) {
          index = cellsGrid.getIndex( col, row );
        }
        break;
      }
      case SWT.ARROW_DOWN: {
        if( ++row < (cellsGrid.getRowsCount() - 1) || col < cellsGrid.getColsInRow( row ) ) {
          index = cellsGrid.getIndex( col, row );
        }
        break;
      }
      case SWT.ARROW_LEFT: {
        if( --index < -1 ) {
          index = cellsGrid.getCellsCount() - 1;
        }
        break;
      }
      case SWT.ARROW_RIGHT: {
        if( ++index >= cellsGrid.getCellsCount() ) {
          index = -1;
        }
        break;
      }
      case SWT.HOME:
        index = 0;
        break;
      case SWT.END:
        index = cellsGrid.getCellsCount() - 1;
        break;
      // TODO перелистывание на страницу
      // case SWT.PAGE_DOWN:
      // break;
      // case SWT.PAGE_UP:
      // break;
      default: {
        return false;
      }
    }
    pgvSetSelectedIndex( index );
    revealItem( index );
    return true;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Calculates the height of the rectangle for text output.
   * <p>
   * Height is calculated based on the height of the current font.
   *
   * @return int - the height of the rectangle for text output in pixels
   */
  private int getTextAreaHeight() {
    GC gc = new GC( this );
    int h;
    try {
      Point p1 = gc.textExtent( "ABCDEFGHIJKLMONOPQRSTUVWXYZabcdefghijklmonopqrstuvwxyz" ); //$NON-NLS-1$
      textLineHeight = p1.y + 2;
      boolean isLabels = OPDEF_IS_LABELS_SHOWN.getValue( tsContext().params() ).asBool();
      int labelLines = getLabelLinesCount();
      h = 0;
      if( isLabels ) {
        h = labelLines * textLineHeight;
      }
    }
    finally {
      gc.dispose();
    }
    return h;
  }

  private void initializeItems( IList<V> aEntities ) {
    TsNullArgumentRtException.checkNull( aEntities );
    // stop the current animations and clear the list of #registeredAnimators
    itemsWithImagesToLoadQueue.clear();
    while( !items.isEmpty() ) {
      items.removeByIndex( 0 ).close();
    }
    // create new items
    for( int i = 0; i < aEntities.size(); i++ ) {
      V e = aEntities.get( i );
      PgvItem item = new PgvItem( e, i );
      items.add( item );
      itemsWithImagesToLoadQueue.putTail( item );
    }
    reinititlizeSlittedTexts();
    loadNextImage();
  }

  private void loadNextImage() {
    getDisplay().asyncExec( () -> {
      // start processing the next element|, if there is one
      PgvItem next = itemsWithImagesToLoadQueue.getHeadOrNull();
      if( next == null ) {
        return;
      }
      // image upload and animation if needed
      next.reloadImage();
      // load next image
      loadNextImage();
    } );
  }

  private IList<V> getEntities() {
    IListEdit<V> ll = new ElemArrayList<>( items.size() );
    for( PgvItem i : items ) {
      ll.add( i.getEntity() );
    }
    return ll;
  }

  private void adjustCellSize() {
    int textAreaHeight = getTextAreaHeight();
    cellsGrid.setCellSize( thumbSize.size(), thumbSize.size() + textAreaHeight );
  }

  private int ensureRowInRange( int aRow ) {
    TsInternalErrorRtException.checkTrue( cellsGrid.getRowsCount() == 0 );
    if( aRow < 0 ) {
      return 0;
    }
    if( aRow >= cellsGrid.getRowsCount() ) {
      return cellsGrid.getRowsCount() - 1;
    }
    return aRow;
  }

  private void revealItem( int aIndex ) {
    if( items.isEmpty() ) {
      return;
    }
    // find the row to be shown (columns are always visible!)
    int rowToSel = 0;
    if( aIndex >= 0 ) {
      rowToSel = aIndex / cellsGrid.getColsCount();
    }
    // determine which lines are now FULLY visible
    ScrolledComposite parent = (ScrolledComposite)getParent();
    Point origin = parent.getOrigin();
    Rectangle viewport = parent.getClientArea();
    int startY = cellsGrid.margins().top();
    int rowH = cellsGrid.getCellHeight() + cellsGrid.margins().verGap();
    // index of first fully visible row
    int visibleRow1 = 0;
    if( origin.y > startY ) {
      visibleRow1 = ensureRowInRange( (origin.y - startY) / rowH + 1 );
    }
    // index of last fully visible row
    int visibleRow2 = ensureRowInRange( (origin.y + viewport.height - startY) / rowH - 1 );
    if( visibleRow2 < visibleRow1 ) {
      visibleRow2 = visibleRow1;
    }
    // if the desired line is visible, do nothing
    boolean isAbove = rowToSel < visibleRow1;
    boolean isBelow = rowToSel > visibleRow2;
    if( !isAbove && !isBelow ) {
      return;
    }
    // let's make the required line visible: at the very top, if it was hidden above the visible area, or at the very
    // bottom, if it was hidden below the area visible
    int newTopRow = visibleRow1; // which line should be the first visible
    if( isAbove ) {
      // the selected element will be in the first visible row
      newTopRow = ensureRowInRange( rowToSel );
    }
    else {
      if( isBelow ) {
        // the selected element will be in the last visible line
        int visibleRowsCount = visibleRow2 - visibleRow1;
        newTopRow = ensureRowInRange( rowToSel - visibleRowsCount );
      }
    }
    if( newTopRow == visibleRow1 ) {
      return;
    }
    // set the coordinates
    int newOriginY;
    if( newTopRow > 0 ) {
      newOriginY = startY + newTopRow * rowH - cellsGrid.margins().verGap() + 1;
    }
    else {
      newOriginY = startY;
    }
    parent.setOrigin( origin.x, newOriginY );
  }

  private int getLabelLinesCount() {
    int minCount = OPDEF_LABEL_LINES.params().getInt( TSID_MIN_INCLUSIVE, 1 );
    int maxCount = OPDEF_LABEL_LINES.params().getInt( TSID_MAX_INCLUSIVE, 4 );
    IntRange range = new IntRange( minCount, maxCount );
    return range.inRange( OPDEF_LABEL_LINES.getValue( tsContext().params() ).asInt() );
  }

  /**
   * Iterates over {@link #items} and calls {@link PgvItem#setItemLabel(IList)}.
   * <p>
   * Called when items are created and every time when cell width or texts may be changed.
   */
  private void reinititlizeSlittedTexts() {
    GC gc = new GC( this );
    try {
      ICanvasTextSplitter textSplitter = new SimpleCanvasTextSplitter( gc, gc.getFont() );
      int labelLinesCount = getLabelLinesCount();
      for( PgvItem p : items ) {
        V entity = p.getEntity();
        String text = visualsProvider.getName( entity );
        IList<TextLine> ll = textSplitter.splitText( text, cellsGrid.getCellWidth(), labelLinesCount );
        p.setItemLabel( ll );
      }
    }
    finally {
      gc.dispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the index of the selected element in the list given by {@link #pgvSetEntities(IList)}.
   *
   * @return int - the index of the visually selected element, or -1
   */
  public int pgvGetSelectionIndex() {
    return selectedIndex;
  }

  /**
   * Sets the selected entity by index in the {@link #pgvSetEntities(IList)} list.
   *
   * @param aIndex int - index in the list or -1 to deselect
   * @throws TsIllegalArgumentRtException index out of range
   */
  public void pgvSetSelectedIndex( int aIndex ) {
    if( selectedIndex != aIndex && aIndex >= -1 && aIndex < items.size() ) {
      int oldIndex = selectedIndex;
      selectedIndex = aIndex;
      redrawCell( oldIndex );
      redrawCell( selectedIndex );
      V selEntity = selectedIndex >= 0 ? items.get( selectedIndex ).getEntity() : null;
      revealItem( selectedIndex );
      owner.fireTsSelectionEvent( selEntity );
    }
  }

  /**
   * Sets the entities to by displayed.
   *
   * @param aEntities {@link IList}&lt;V&gt; - the list of entities
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void pgvSetEntities( IList<V> aEntities ) {
    initializeItems( aEntities );
    cellsGrid.setCellsCount( items.size() );
    selectedIndex = -1;
    redraw();
  }

  public void refresh() {
    // reload all images - fill again #itemsWithImagesToLoadQueue and start loading
    itemsWithImagesToLoadQueue.clear();
    for( PgvItem i : items ) {
      itemsWithImagesToLoadQueue.putTail( i );
    }
    reinititlizeSlittedTexts();
    loadNextImage();
    redraw();
  }

  public ITsGridMargins pgvGetMargins() {
    return cellsGrid.margins();
  }

  public void pgvSetMargins( ITsGridMargins aMrgins ) {
    cellsGrid.setMargins( aMrgins );
  }

  public ITsVisualsProvider<V> pgvGetVisualsProvider() {
    return visualsProvider;
  }

  public void pgvSetVisualsProvider( ITsVisualsProvider<V> aProvider ) {
    TsNullArgumentRtException.checkNull( aProvider );
    if( !visualsProvider.equals( aProvider ) ) {
      visualsProvider = aProvider;
      initializeItems( getEntities() );
    }
  }

  public boolean isForceStill() {
    return forceStill;
  }

  public void setFocreStill( boolean aForceStill ) {
    if( forceStill != aForceStill ) {
      forceStill = aForceStill;
      redraw();
    }
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeableEx
  //

  @Override
  public EThumbSize thumbSize() {
    return thumbSize;
  }

  @Override
  public void setThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    if( thumbSize != aThumbSize ) {
      thumbSize = aThumbSize;
      initializeItems( getEntities() );
      adjustCellSize();
      thumbSizeChangeEventer.fireChangeEvent();
    }
  }

  @Override
  public EThumbSize defaultThumbSize() {
    return defaultThumbSize;
  }

  @Override
  public IGenericChangeEventer thumbSizeEventer() {
    return thumbSizeChangeEventer;
  }

}
