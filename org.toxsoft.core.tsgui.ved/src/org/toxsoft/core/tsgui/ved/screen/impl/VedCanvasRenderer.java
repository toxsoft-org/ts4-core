package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Implementation of the VED canvas painting.
 * <p>
 * Notes:
 * <ul>
 * <li>this is a passive component, it does not generates any events when drawing parameters are changed;</li>
 * <li>single VED environment may be drawn on different canvas so several instances of theis class may exist.</li>
 * </ul>
 *
 * @author hazard157
 */
class VedCanvasRenderer
    implements PaintListener, ITsGuiContextable {

  /**
   * OPTIMIZE because {@link Transform} is OS managed resource, may be better not to create instances on each painting
   * operation, rather store cached instances (screen transform in this class, VISEL transforms in abstract VISELS) and
   * update cache on every conversion and/or other settings change.
   */

  private final VedScreenModel screenModel;
  private IVedCanvasCfg        canvasCfg           = new VedCanvasCfg();
  private boolean              isBackgroundFilling = true;

  /**
   * Current canvas drawing conversion.
   * <p>
   * Reset by {@link #setCanvasConfig(IVedCanvasCfg)} and changed by {@link #setCurrentConversion(ID2Conversion)}.
   */
  private ID2Conversion d2Conv = ID2Conversion.NONE;

  /**
   * Constructor.
   *
   * @param aScreenModel {@link VedScreenModel} - the VED screen content model
   */
  public VedCanvasRenderer( VedScreenModel aScreenModel ) {
    screenModel = aScreenModel;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return screenModel.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void setViselTransform( ITsGraphicsContext aGc, VedAbstractVisel aVisel, Transform aDefaultTransform ) {
    ID2Conversion viselConv = aVisel.getConversion();
    if( viselConv == ID2Conversion.NONE ) {
      aGc.gc().setTransform( aDefaultTransform );
      return;
    }
    Transform itemTransform = D2TransformUtils.d2ConversionToTransfrom( aGc.gc(), d2Conv );
    // D2TransformUtils.convertTransfrom( itemTransform, viselConv ); // old
    D2TransformUtils.convertItemTransfrom( itemTransform, viselConv, aVisel.rotationX(), aVisel.rotationY() );
    aGc.gc().setTransform( itemTransform );
    itemTransform.dispose();
  }

  private Transform viselTransform( ITsGraphicsContext aGc, VedAbstractVisel aVisel ) {
    ID2Conversion viselConv = aVisel.getConversion();
    Transform itemTransform = D2TransformUtils.d2ConversionToTransfrom( aGc.gc(), d2Conv );
    D2TransformUtils.convertItemTransfrom( itemTransform, viselConv, aVisel.rotationX(), aVisel.rotationY() );
    return itemTransform;
  }

  // ------------------------------------------------------------------------------------
  // PaintListener
  //

  @Override
  public void paintControl( PaintEvent aEvent ) {
    // prepare for paint
    aEvent.gc.setAdvanced( true );
    aEvent.gc.setAntialias( SWT.ON );
    aEvent.gc.setTextAntialias( SWT.ON );

    TsGraphicsContext tsg = new TsGraphicsContext( aEvent, tsContext() );
    Transform screenTransform = D2TransformUtils.d2ConversionToTransfrom( aEvent.gc, d2Conv );
    aEvent.gc.setTransform( screenTransform );
    IVedItemsManager<VedAbstractVisel> visels = screenModel.visels();

    // fill canvas - draw canvas background if needed
    if( isBackgroundFilling ) {
      tsg.setFillInfo( canvasCfg.fillInfo() );
      // tsg.fillRect( tsg.drawArea() ); // TODO maybe draw from the canvas (0,0) to (canvas_width,canvas_height) ?
      tsg.fillRect( 0, 0, (int)canvasCfg.size().x(), (int)canvasCfg.size().y() );
    }
    // // DEBUG -----------------------------------
    // aEvent.gc.setForeground( new Color( 0, 0, 0 ) );
    // aEvent.gc.drawLine( 100, 0, 100, 300 );
    // aEvent.gc.drawLine( 0, 100, 300, 100 );
    // // DEBUG -----------------------------------

    // draw screen decorators BEFORE
    for( VedAbstractDecorator d : screenModel.screenDecoratorsBefore().list() ) {
      if( d.isActive() ) {
        VedAbstractVisel visel = visels.list().findByKey( d.getViselIdOfDrawingTransform() );
        if( visel != null ) {
          setViselTransform( tsg, visel, screenTransform );
        }
        d.paint( tsg );
      }
    }

    // draw VISELs with decorators
    for( VedAbstractVisel visel : visels.list() ) {
      if( visel.isActive() ) {
        // prepare VISEL for drawing
        setViselTransform( tsg, visel, screenTransform );
        // decorators BEFORE
        for( VedAbstractDecorator d : screenModel.viselDecoratorsBefore( visel.id() ).list() ) {
          if( d.isActive() ) {
            d.paint( tsg );
          }
        }
        // draw VISEL
        visel.paint( tsg );
        // decorators AFTER
        for( VedAbstractDecorator d : screenModel.viselDecoratorsAfter( visel.id() ).list() ) {
          if( d.isActive() ) {
            d.paint( tsg );
          }
        }
        aEvent.gc.setTransform( null );
      }
    }
    // draw screen decorators AFTER
    for( VedAbstractDecorator d : screenModel.screenDecoratorsAfter().list() ) {
      if( d.isActive() ) {
        VedAbstractVisel visel = visels.list().findByKey( d.getViselIdOfDrawingTransform() );
        if( visel != null ) {
          setViselTransform( tsg, visel, screenTransform );
        }
        d.paint( tsg );
      }
    }
    // screenTransform.dispose();
    aEvent.gc.setTransform( null );

    // // DEBUG ------------------------------------------------
    // for( VedAbstractVisel visel : visels.list() ) {
    // if( visel.isActive() ) {
    // IVedCoorsConverter vedConv = visel.vedScreen().view().coorsConverter();
    // ITsPoint tsp = vedConv.visel2Swt( new D2Point( 0, 0 ), visel );
    // tsg.gc().setBackground( new Color( 255, 0, 0 ) );
    // tsg.gc().fillOval( tsp.x() - 4, tsp.y() - 4, 8, 8 );
    // }
    // }
    //
    // if( visels.list().size() > 0 ) {
    // IVedCoorsConverter vedConv = visels.list().first().vedScreen().view().coorsConverter();
    // ITsPoint tsp = vedConv.screen2Swt( 100, 100 );
    // tsg.gc().setBackground( new Color( 0, 0, 255 ) );
    // tsg.gc().fillOval( tsp.x() - 4, tsp.y() - 4, 8, 8 );
    // }
    // // DEBUG ------------------------------------------------
    screenTransform.dispose();

  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the canvas drawing conversion settings.
   *
   * @return {@link ID2Conversion} - conversion settings for canvas drawing
   */
  public ID2Conversion getCurrentConversion() {
    return d2Conv;
  }

  /**
   * Sets the current conversion used for screen.
   * <p>
   * Note: setting canvas config via {@link #setCanvasConfig(IVedCanvasCfg)} resets current conversion value to the
   * {@link IVedCanvasCfg#conversion()}.
   *
   * @param aConversion {@link ID2Conversion} - conversion settings for canvas drawing
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setCurrentConversion( ID2Conversion aConversion ) {
    TsNullArgumentRtException.checkNull( aConversion );
    d2Conv = aConversion;
  }

  /**
   * Determines if canvas background is drawn.
   * <p>
   * Canvas background filling parameters are defined by configuration parameter {@link IVedCanvasCfg#fillInfo()}.
   *
   * @return boolean - <code>true</code> if background fill parameters is applied
   */
  public boolean isBackgroundFill() {
    return isBackgroundFilling;
  }

  /**
   * Changes background filling {@link #isBackgroundFill()}.
   *
   * @param aFill boolean - <code>true</code> if background fill parameters is applied
   */
  public void setBackgroundFill( boolean aFill ) {
    isBackgroundFilling = aFill;
  }

  /**
   * Returns the canvas configuration set by {@link #setCanvasConfig(IVedCanvasCfg)}.
   *
   * @return {@link IVedCanvasCfg} - VED canvas configuration
   */
  public IVedCanvasCfg canvasConfig() {
    return canvasCfg;
  }

  /**
   * Sets the canvas configuration.
   * <p>
   * Note: setting canvas config resets current conversion value to the {@link IVedCanvasCfg#conversion()}.
   *
   * @param aCanvasConfig {@link IVedCanvasCfg} - VED canvas configuration
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setCanvasConfig( IVedCanvasCfg aCanvasConfig ) {
    TsNullArgumentRtException.checkNull( aCanvasConfig );
    canvasCfg = aCanvasConfig;
    d2Conv = canvasCfg.conversion();
  }

}
