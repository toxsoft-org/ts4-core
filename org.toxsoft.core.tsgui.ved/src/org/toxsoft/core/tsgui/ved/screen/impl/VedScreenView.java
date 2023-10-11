package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedScreenView} implementation.
 *
 * @author hazard157
 */
public class VedScreenView
    implements IVedScreenView, ICloseable {

  private final VedScreen               vedScreen;
  private final VedCanvasRenderer       canvasRenderer;
  private final TsUserInputEventsBinder userInputBinder;
  private final VedCanvasHandler        canvasHandler;
  private final IVedCoorsConverter      coorsConverter;

  private Canvas theCanvas = null;

  /**
   * Constructor.
   *
   * @param aScreen {@link VedScreenCfg} - the owner screen
   */
  VedScreenView( VedScreen aScreen ) {
    vedScreen = aScreen;
    coorsConverter = new VedCoorsConverter( vedScreen );
    canvasRenderer = new VedCanvasRenderer( vedScreen.model() );
    canvasHandler = new VedCanvasHandler( vedScreen );
    userInputBinder = new TsUserInputEventsBinder( vedScreen );
    userInputBinder.addTsUserInputListener( canvasHandler );
    //
    // TODO do we need more precious event handling ?
    vedScreen.model().visels().eventer().addListener( ( src, op, id ) -> whenVedItemsChanged() );
    vedScreen.model().actors().eventer().addListener( ( src, op, id ) -> whenVedItemsChanged() );
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  void attachCanvas( Canvas aCanvas ) {
    TsNullArgumentRtException.checkNull( aCanvas );
    theCanvas = aCanvas;
    theCanvas.addPaintListener( canvasRenderer );
    userInputBinder.bindToControl( theCanvas, TsUserInputEventsBinder.BIND_ALL_INPUT_EVENTS );
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    userInputBinder.unbind();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void whenVedItemsChanged() {
    redraw();
  }

  // ------------------------------------------------------------------------------------
  // ID2Conversionable
  //

  @Override
  public ID2Conversion getConversion() {
    return canvasRenderer.getCurrentConversion();
  }

  @Override
  public void setConversion( ID2Conversion aConversion ) {
    canvasRenderer.setCurrentConversion( aConversion );
  }

  // ------------------------------------------------------------------------------------
  // IVedScreenView
  //

  @Override
  public boolean isBackgroundFill() {
    return canvasRenderer.isBackgroundFill();
  }

  @Override
  public void setBackgroundFill( boolean aFill ) {
    canvasRenderer.setBackgroundFill( aFill );
  }

  @Override
  public IVedCanvasCfg canvasConfig() {
    return canvasRenderer.canvasConfig();
  }

  @Override
  public void setCanvasConfig( IVedCanvasCfg aCanvasConfig ) {
    canvasRenderer.setCanvasConfig( aCanvasConfig );
  }

  @Override
  public void redraw() {
    if( theCanvas != null ) {
      theCanvas.redraw();
    }
  }

  @Override
  public void update() {
    if( theCanvas != null ) {
      theCanvas.update();
    }
  }

  @Override
  public IVedCoorsConverter coorsConverter() {
    return coorsConverter;
  }

  @Override
  public void redrawVisel( String aViselId ) {
    VedAbstractVisel visel = vedScreen.model().visels().list().getByKey( aViselId );
    ITsRectangle swtRect = coorsConverter.visel2Swt( visel.bounds(), visel );
    redrawSwtRect( swtRect );
  }

  @Override
  public void redrawSwtRect( ITsRectangle aSwtRect ) {
    if( theCanvas != null ) {
      theCanvas.redraw( aSwtRect.x1(), aSwtRect.y1(), aSwtRect.width(), aSwtRect.height(), true );
    }
  }

  @Override
  public void setCursor( Cursor aCursor ) {
    if( theCanvas != null ) {
      theCanvas.setCursor( aCursor );
    }
  }

  @Override
  public Control getControl() {
    return theCanvas;
  }

}
