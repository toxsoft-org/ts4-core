package org.toxsoft.core.tsgui.ved.editor;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.editor.IVedViselSelectionManager.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Обработчик пользовательского ввода обеспечивающий множественное выделение визелей мышью.
 * <p>
 *
 * @author vs
 */
public class VedViselMultiselectionManager
    extends VedAbstractUserInputHandler {

  private boolean multiSelection = false;

  class MultiSelectionDecorator
      extends VedAbstractDecorator {

    class SelectionListener
        implements IGenericChangeListener {

      @Override
      public void onGenericChangeEvent( Object aSource ) {
        if( multiSelection ) {
          vedScreen().view().redraw();
          vedScreen().view().update();
        }
        if( selectionManager.selectedViselIds().isEmpty() ) {
          multiSelection = false;
        }
      }
    }

    private static final Color              colorWhite = new Color( 255, 0, 255 );
    private final IVedViselSelectionManager msManager;
    private final SelectionListener         selectionListener;

    // ------------------------------------------------------------------------------------
    // VedAbstractDecorator
    //

    public MultiSelectionDecorator( IVedScreen aScreen, IVedViselSelectionManager aSelectionManager ) {
      super( aScreen );
      msManager = TsNullArgumentRtException.checkNull( aSelectionManager );
      selectionListener = new SelectionListener();
      msManager.genericChangeEventer().addListener( selectionListener );
    }

    @Override
    public void paint( ITsGraphicsContext aPaintContext ) {
      if( selectionManager.selectionKind() == ESelectionKind.MULTI ) {
        aPaintContext.gc().setAdvanced( true );
        aPaintContext.gc().setLineWidth( 3 );
        aPaintContext.gc().setForeground( colorWhite );
        aPaintContext.gc().setXORMode( true );
        ID2Conversion d2Conv = vedScreen().view().getConversion();
        Transform screenTransform = D2TransformUtils.d2ConversionToTransfrom( aPaintContext.gc(), d2Conv );
        for( String id : selectionManager.selectedViselIds() ) {
          VedAbstractVisel visel = vedScreen().model().visels().list().getByKey( id );
          setViselTransform( aPaintContext, visel, screenTransform );
          paintViselSelection( aPaintContext, visel );
        }
        aPaintContext.gc().setXORMode( false );
        screenTransform.dispose();
      }
    }

    @Override
    public boolean isYours( double aX, double aY ) {
      return false;
    }

    @Override
    public ID2Rectangle bounds() {
      return new D2Rectangle( 0, 0, 1, 1 );
    }

    // ------------------------------------------------------------------------------------
    // Implementation
    //

    private void setViselTransform( ITsGraphicsContext aGc, VedAbstractVisel aVisel, Transform aDefaultTransform ) {
      ID2Conversion viselConv = aVisel.getConversion();
      if( viselConv == ID2Conversion.NONE ) {
        aGc.gc().setTransform( aDefaultTransform );
        return;
      }
      ID2Conversion d2Conv = vedScreen().view().getConversion();
      Transform itemTransform = D2TransformUtils.d2ConversionToTransfrom( aGc.gc(), d2Conv );
      D2TransformUtils.convertItemTransfrom( itemTransform, viselConv, aVisel.rotationX(), aVisel.rotationY() );
      aGc.gc().setTransform( itemTransform );
      itemTransform.dispose();
    }

    private void paintViselSelection( ITsGraphicsContext aPaintContext, VedAbstractVisel aVisel ) {
      ID2Rectangle d2r = aVisel.bounds();
      int x = -3;
      int y = -3;
      int w = (int)(d2r.width() + 5);
      int h = (int)(d2r.height() + 5);
      aPaintContext.gc().drawRectangle( x, y, w, h );
    }

  }

  private final IVedViselSelectionManager selectionManager;
  private final MultiSelectionDecorator   selectionDecorator;

  /**
   * Constructor.
   *
   * @param aScreen {@link VedScreen} - the owner VED screen
   * @param aSelectionManager {@link IVedViselSelectionManager} - selection manager for VISELs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedViselMultiselectionManager( IVedScreen aScreen, IVedViselSelectionManager aSelectionManager ) {
    super( aScreen );
    selectionManager = TsNullArgumentRtException.checkNull( aSelectionManager );
    selectionDecorator = new MultiSelectionDecorator( aScreen, aSelectionManager );
    aScreen.model().screenDecoratorsAfter().add( selectionDecorator );
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputListener
  //

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    if( aButton == ETsMouseButton.LEFT && aState == SWT.CTRL ) {
      VedAbstractVisel visel = itemByPoint( aCoors.x(), aCoors.y() );
      if( visel != null ) {
        selectionManager.toggleSelection( visel.id() );
        multiSelection = true;
      }
      return true;
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private VedAbstractVisel itemByPoint( int aSwtX, int aSwtY ) {
    IVedCoorsConverter converter = vedScreen().view().coorsConverter();
    for( VedAbstractVisel item : vedScreen().model().visels().list() ) {
      ID2Point d2p = converter.swt2Visel( aSwtX, aSwtY, item );
      if( item.isYours( d2p.x(), d2p.y() ) ) {
        return item;
      }
    }
    return null;
  }

}
