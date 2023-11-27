package org.toxsoft.core.tsgui.ved.editor.palette;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

/**
 * Простая (без категорий) реализация палитры ved компонент в виде bar'a (выглядит аналогично {@link ToolBar}.
 * <p>
 * Каждый элемент палитры является "западающей" кнопкой. Кроме "западания" кнопок данная палитра поддерживает
 * {@link DragSource}. В любой момент времени "запавшей" может не более одной кнопки.
 *
 * @author vs
 */
public class VedItemsPaletteBar
    extends VedAbstractItemsPalette
    implements ITsSelectionChangeEventProducer<IVedItemsPaletteEntry> {

  private final TsPanel   paletteComp;
  private final EIconSize iconSize;

  private final TsSelectionChangeEventHelper<IVedItemsPaletteEntry> eventer;

  private final IListEdit<Button> buttons = new ElemArrayList<>();

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aSwtStyle int - стиль
   * @param aScreen {@link VedScreen} - экран
   * @param aVertical boolean - признак расположения патитры (вертикально/горизонтально)
   */
  public VedItemsPaletteBar( Composite aParent, int aSwtStyle, IVedScreen aScreen, boolean aVertical ) {
    this( aParent, aSwtStyle, aScreen, aVertical, null );
  }

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aSwtStyle int - стиль
   * @param aScreen {@link VedScreen} - экран
   * @param aVertical boolean - признак расположения патитры (вертикально/горизонтально)
   * @param aIconSize {@link EIconSize} - размер значка
   */
  public VedItemsPaletteBar( Composite aParent, int aSwtStyle, IVedScreen aScreen, boolean aVertical,
      EIconSize aIconSize ) {
    super( aScreen );
    eventer = new TsSelectionChangeEventHelper<>( this );
    if( aIconSize == null ) {
      iconSize = hdpiService().getIconsSize( VED_EDITOR_PALETTE_ICON_SIZE_CATEGORY );
    }
    else {
      iconSize = aIconSize;
    }
    paletteComp = new TsPanel( aParent, aScreen.tsContext(), aSwtStyle );
    if( !aVertical ) {
      paletteComp.setLayout( new RowLayout( SWT.HORIZONTAL ) );
    }
    else {
      paletteComp.setLayout( new RowLayout( SWT.VERTICAL ) );
    }

    paletteComp.addMouseListener( new MouseAdapter() {

      @Override
      public void mouseDown( MouseEvent e ) {
        deselectAllButtons();
        eventer.fireTsSelectionEvent( null );
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // IVedItemsPalette
  //

  @Override
  public Control getControl() {
    return paletteComp;
  }

  @Override
  protected void doOnEntryAdded( IVedItemsPaletteEntry aEntry, String aCategoryId ) {
    Button btn = new Button( paletteComp, SWT.FLAT | SWT.TOGGLE );
    btn.setData( aEntry );
    buttons.add( btn );

    Image image = iconManager().loadStdIcon( aEntry.iconId(), iconSize );

    btn.setImage( image );
    btn.setToolTipText( aEntry.nmName() + '\n' + aEntry.description() );
    btn.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        deselectOtherButtons( btn );
        if( selectedButton() != null ) {
          eventer.fireTsSelectionEvent( (IVedItemsPaletteEntry)selectedButton().getData() );
        }
        else {
          eventer.fireTsSelectionEvent( null );
        }
      }
    } );

    DragSource source = new DragSource( btn, DND.DROP_MOVE | DND.DROP_COPY );
    source.setData( aEntry );
    Transfer[] types = { TextTransfer.getInstance() };
    source.setTransfer( types );

    source.addDragListener( new DragSourceAdapter() {

      @Override
      public void dragSetData( DragSourceEvent aEvent ) {
        IVedItemCfg itemCfg = ((IVedItemsPaletteEntry)source.getData()).itemCfg();
        aEvent.data = VedItemCfg.KEEPER.ent2str( itemCfg );
      }
    } );

  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionChangeEventProducer
  //

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<IVedItemsPaletteEntry> aListener ) {
    eventer.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<IVedItemsPaletteEntry> aListener ) {
    eventer.removeTsSelectionListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает выделенный (текущий) элемент палитры или <b>null</b>.<br>
   *
   * @return {@link IVedItemsPaletteEntry} - выделенный (текущий) элемент палитры или <b>null</b>
   */
  public IVedItemsPaletteEntry selectedItem() {
    Button btn = selectedButton();
    if( btn != null ) {
      return (IVedItemsPaletteEntry)btn.getData();
    }
    return null;
  }

  /**
   * Возвращает курсор в зависимости от выделенного элемента.
   *
   * @return {@link Cursor} - курсор в зависимости от выделенного элемента
   */
  public Cursor cursor() {
    Button btn = selectedButton();
    if( btn != null ) {
      IVedItemsPaletteEntry pe = (IVedItemsPaletteEntry)btn.getData();
      pe.iconId();
      if( cursorManager().findCursor( pe.id() ) == null ) {
        cursorManager().putCursor( pe.id(), new Cursor( getDisplay(), btn.getImage().getImageData(), 0, 0 ) );
      }
      return cursorManager().findCursor( pe.id() );
    }
    return null;
  }

  /**
   * Делает все кнопки "не запавшими".
   */
  public void deselectAllButtons() {
    for( Button btn : buttons ) {
      btn.setSelection( false );
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void deselectOtherButtons( Button aButton ) {
    for( Button btn : buttons ) {
      if( !btn.equals( aButton ) ) {
        btn.setSelection( false );
      }
    }
  }

  Button selectedButton() {
    for( Button btn : buttons ) {
      if( btn.getSelection() ) {
        return btn;
      }
    }
    return null;
  }

}
