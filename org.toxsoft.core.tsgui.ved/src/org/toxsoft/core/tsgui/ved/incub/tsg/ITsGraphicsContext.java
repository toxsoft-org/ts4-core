package org.toxsoft.core.tsgui.ved.incub.tsg;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * TS graphics context wraps over {@link GC} and adds some TsGUI specific functionality.
 * <p>
 * The additional functions are:
 * <ul>
 * <li>access to the TsGUI (and E4 application) specific context via {@link #tsContext()};</li>
 * <li>the concept of area to be drawn {@link #drawArea()}. When used in paint events drawing area is determined by
 * event boundaries. In other cases (eg drawing on image or printer) drawing area corresponds to the physical
 * dimensions. Anyway, there is no sense to draw outside drawing area;</li>
 * <li>TsGUI specific settings and drawing methods like <code>setXxxInfo()</code> and <code>draw/fillXxx()</code>.</li>
 * </ul>
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ITsGraphicsContext
    extends ITsGuiContextable {

  GC gc();

  ITsRectangle drawArea();

  void setLineInfo( TsLineInfo aLineInfo );

  void drawRect( int aX, int aY, int aWidth, int aHeight );

  void drawRoundRect( int aX, int aY, int aWidth, int aHeight, int aArcWdth, int aArcHeight );

  void setFillInfo( TsFillInfo aFillInfo );

  void fillRect( int aX, int aY, int aWidth, int aHeight );

  void fillRoundRect( int aX, int aY, int aWidth, int aHeight, int aArcWidth, int aArcHeight );

  void fillOval( int aX, int aY, int aWidth, int aHeight );

  void fillPath( Path aPath, int aX, int aY, int aWidth, int aHeight );

  void setBorderInfo( TsBorderInfo aBorderInfo );

  void drawRectBorder( int aX, int aY, int aWidth, int aHeight );

  void setBackgroundRgba( RGBA aRgba );

  void setBackgroundRgb( RGB aRgb );

  void setForegroundRgba( RGBA aRgba );

  void setForegroundRgb( RGB aRgb );

  // ------------------------------------------------------------------------------------
  // Inline methods for convenience
  //

  default void drawRect( ITsRectangle aRect ) {
    drawRect( aRect.x1(), aRect.y1(), aRect.width(), aRect.height() );
  }

  default void fillRect( ITsRectangle aRect ) {
    fillRect( aRect.x1(), aRect.y1(), aRect.width(), aRect.height() );
  }

  default void drawRectBorder( ITsRectangle aRect ) {
    drawRectBorder( aRect.x1(), aRect.y1(), aRect.width(), aRect.height() );
  }

  default void drawD2Rect( ID2Rectangle aRect ) {
    drawRect( (int)aRect.x1(), (int)aRect.y1(), (int)aRect.width(), (int)aRect.height() );
  }

  default void fillD2Rect( ID2Rectangle aRect ) {
    fillRect( (int)aRect.x1(), (int)aRect.y1(), (int)aRect.width(), (int)aRect.height() );
  }

  default void drawD2RectBorder( ID2Rectangle aRect ) {
    drawRectBorder( (int)aRect.x1(), (int)aRect.y1(), (int)aRect.width(), (int)aRect.height() );
  }

}
