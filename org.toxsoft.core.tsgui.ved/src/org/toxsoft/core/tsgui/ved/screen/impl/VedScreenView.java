package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * {@link IVedScreenView} implementation.
 *
 * @author hazard157
 */
class VedScreenView
    implements IVedScreenView {

  private final VedScreen               vedScreen;
  private final Canvas                  theCanvas;
  private final VedCanvasRenderer       canvasRenderer;
  private final TsUserInputEventsBinder userInputBinder;
  private final VedCanvasHandler        canvasHandler;

  VedScreenView( Composite aParent, VedScreen aScreen ) {
    vedScreen = aScreen;
    theCanvas = new Canvas( aParent, SWT.NONE );
    canvasRenderer = new VedCanvasRenderer( vedScreen.model() );
    theCanvas.addPaintListener( canvasRenderer );
    canvasHandler = new VedCanvasHandler( vedScreen );
    userInputBinder = new TsUserInputEventsBinder( vedScreen );
    userInputBinder.bindToControl( theCanvas, TsUserInputEventsBinder.BIND_ALL_INPUT_EVENTS );
    userInputBinder.addTsUserInputListener( canvasHandler );
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
    theCanvas.redraw();
  }

  @Override
  public void update() {
    theCanvas.update();
  }

  @Override
  public void redrawVisel( String aViselId ) {
    VedAbstractVisel visel = vedScreen.model().visels().listAllItems().getByKey( aViselId );
    redrawRect( visel.bounds() );
  }

  @Override
  public void redrawRect( ITsRectangle aScreenRect ) {
    theCanvas.redraw( aScreenRect.x1(), aScreenRect.y1(), aScreenRect.width(), aScreenRect.height(), true );
  }

  @Override
  public void setCursor( Cursor aCursor ) {
    theCanvas.setCursor( aCursor );
  }

  @Override
  public Control getControl() {
    return theCanvas;
  }

}
