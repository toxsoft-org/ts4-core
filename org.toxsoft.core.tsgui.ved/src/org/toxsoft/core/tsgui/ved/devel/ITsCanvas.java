package org.toxsoft.core.tsgui.ved.devel;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;

public interface ITsCanvas
    extends ITsGuiContextable {

  // Canvas canvas();

  // GC gc();

  void setLineInfo( TsLineInfo aLineInfo );

  void drawRect( int aX, int aY, int aWidth, int aHeight );

  void setFillInfo( TsFillInfo aFillInfo );

  void fillRect( int aX, int aY, int aWidth, int aHeight );

}
