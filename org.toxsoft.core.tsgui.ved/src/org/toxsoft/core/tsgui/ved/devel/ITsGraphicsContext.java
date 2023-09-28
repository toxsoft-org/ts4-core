package org.toxsoft.core.tsgui.ved.devel;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

public interface ITsGraphicsContext
    extends ITsGuiContextable {

  // PaintEvent event();

  GC gc();

  ITsRectangle drawArea();

  void setLineInfo( TsLineInfo aLineInfo );

  void drawRect( int aX, int aY, int aWidth, int aHeight );

  void setFillInfo( TsFillInfo aFillInfo );

  void fillRect( int aX, int aY, int aWidth, int aHeight );

  void setBorderInfo( TsBorderInfo aBorderInfo );

  void drawRectBorder( int aX, int aY, int aWidth, int aHeight );
}
