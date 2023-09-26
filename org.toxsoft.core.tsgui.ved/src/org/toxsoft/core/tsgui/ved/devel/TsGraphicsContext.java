package org.toxsoft.core.tsgui.ved.devel;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class TsGraphicsContext
    implements ITsGraphicsContext {

  private final GC gc;

  private final ITsGuiContext tsContext;

  private final ITsRectangle drawingArea;

  private TsLineInfo lineInfo = null;

  private TsFillInfo fillInfo = null;

  public TsGraphicsContext( PaintEvent aEvent, ITsGuiContext aTsContext ) {
    tsContext = aTsContext;
    gc = aEvent.gc;
    drawingArea = new TsRectangle( aEvent.x, aEvent.y, aEvent.width, aEvent.height );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // TsGraphicsContext
  //

  @Override
  public GC gc() {
    return gc;
  }

  @Override
  public ITsRectangle drawArea() {
    return drawingArea;
  }

  @Override
  public void setLineInfo( TsLineInfo aLineInfo ) {
    lineInfo = aLineInfo;
  }

  @Override
  public void drawRect( int aX, int aY, int aWidth, int aHeight ) {
    if( lineInfo != null ) {
      lineInfo.setToGc( gc );
    }
    gc.drawRectangle( aX, aY, aWidth, aHeight );
  }

  @Override
  public void setFillInfo( TsFillInfo aFillInfo ) {
    fillInfo = aFillInfo;
  }

  @Override
  public void fillRect( int aX, int aY, int aWidth, int aHeight ) {
    if( fillInfo != null ) {
      switch( fillInfo.kind() ) {
        case SOLID:
          RGBA rgba = fillInfo.fillColor();
          gc.setBackground( colorManager().getColor( rgba.rgb ) );
          gc.setAlpha( rgba.alpha );
          break;
        case NONE:
          return;
        case GRADIENT:
          throw new TsUnderDevelopmentRtException();
        case IMAGE:
          throw new TsUnderDevelopmentRtException();
        default:
          throw new IllegalArgumentException( "Unexpected value: " + fillInfo.kind() ); //$NON-NLS-1$
      }
    }
    gc.fillRectangle( aX, aY, aWidth, aHeight );
  }

}
