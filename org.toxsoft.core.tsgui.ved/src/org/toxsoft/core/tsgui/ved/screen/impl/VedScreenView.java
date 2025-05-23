package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedScreenView} implementation.
 *
 * @author hazard157
 */
public final class VedScreenView
    implements IVedScreenView, ICloseable {

  private final GenericChangeEventer configChangeEventer;

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
    configChangeEventer = new GenericChangeEventer( this );
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
    theCanvas.setBackground( new Color( 96, 96, 96 ) );
    theCanvas.addPaintListener( canvasRenderer );
    userInputBinder.bindToControl( theCanvas, TsUserInputEventsBinder.BIND_ALL_INPUT_EVENTS );
  }

  void paint( GC aGc ) {
    Event e = new Event();
    e.widget = theCanvas;
    e.display = vedScreen.getDisplay();

    PaintEvent pe = new PaintEvent( e );
    pe.gc = aGc;
    pe.x = 0;
    pe.y = 0;
    pe.width = canvasConfig().size().intX();
    pe.height = canvasConfig().size().intX();
    canvasRenderer.paintControl( pe );
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
    configChangeEventer.fireChangeEvent(); // Sol++ м.б. нужно другое событие
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
    TsNullArgumentRtException.checkNull( aCanvasConfig );
    if( !canvasRenderer.canvasConfig().equals( aCanvasConfig ) ) {
      canvasRenderer.setCanvasConfig( aCanvasConfig );
      configChangeEventer.fireChangeEvent();
    }
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
  public IStringList listViselIdsAtPoint( ITsPoint aSwtCoors ) {
    TsNullArgumentRtException.checkNull( aSwtCoors );
    IStridablesList<VedAbstractVisel> visels = vedScreen.model().visels().list();
    IStringListEdit result = IStringList.EMPTY;
    if( visels.size() > 0 ) {
      result = new StringArrayList();
      IVedCoorsConverter conv = vedScreen.view().coorsConverter();
      for( int i = visels.size() - 1; i >= 0; i-- ) {
        VedAbstractVisel visel = visels.get( i );
        ID2Point p = conv.swt2Visel( aSwtCoors, visel );
        if( visel.isYours( p.x(), p.y() ) ) {
          result.add( visel.id() );
        }
      }
    }
    return result;
  }

  @Override
  public void redrawVisel( String aViselId ) {
    VedAbstractVisel visel = vedScreen.model().visels().list().findByKey( aViselId );
    if( visel != null ) { // visel may be null at initialization time
      ITsRectangle swtRect = coorsConverter.visel2Swt( visel.bounds(), visel );
      redrawSwtRect( swtRect );
    }
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

  @Override
  public IGenericChangeEventer configChangeEventer() {
    return configChangeEventer;
  }

}
