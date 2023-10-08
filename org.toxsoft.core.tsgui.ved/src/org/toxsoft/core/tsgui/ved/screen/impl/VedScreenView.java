package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * {@link IVedScreenView} implementation.
 *
 * @author hazard157
 */
public class VedScreenView
    implements IVedScreenView, ICloseable {

  private final VedScreen               vedScreen;
  private final Canvas                  theCanvas;
  private final VedCanvasRenderer       canvasRenderer;
  private final TsUserInputEventsBinder userInputBinder;
  private final VedCanvasHandler        canvasHandler;
  private final IVedCoorsConverter      coorsConverter;

  /**
   * Constructor.
   *
   * @param aParent {@link Composite} - parent composite
   * @param aScreen {@link VedScreenCfg} - the owner screen
   */
  VedScreenView( Composite aParent, VedScreen aScreen ) {
    vedScreen = aScreen;
    coorsConverter = new VedCoorsConverter( vedScreen );
    theCanvas = new Canvas( aParent, SWT.NONE );
    canvasRenderer = new VedCanvasRenderer( vedScreen.model() );
    theCanvas.addPaintListener( canvasRenderer );
    canvasHandler = new VedCanvasHandler( vedScreen );
    userInputBinder = new TsUserInputEventsBinder( vedScreen );
    userInputBinder.bindToControl( theCanvas, TsUserInputEventsBinder.BIND_ALL_INPUT_EVENTS );
    userInputBinder.addTsUserInputListener( canvasHandler );
    //
    // TODO need more precious event handling
    vedScreen.model().visels().eventer().addListener( ( src, op, id ) -> whenVedItemsChanged() );
    vedScreen.model().actors().eventer().addListener( ( src, op, id ) -> whenVedItemsChanged() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void whenVedItemsChanged() {
    redraw();
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    userInputBinder.unbind();
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
    theCanvas.redraw( aSwtRect.x1(), aSwtRect.y1(), aSwtRect.width(), aSwtRect.height(), true );
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
