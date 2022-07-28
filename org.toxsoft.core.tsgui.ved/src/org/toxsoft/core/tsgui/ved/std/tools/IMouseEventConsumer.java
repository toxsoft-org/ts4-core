package org.toxsoft.core.tsgui.ved.std.tools;

import org.eclipse.swt.events.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;

public interface IMouseEventConsumer {

  void onClick( IScreenObject aHoveredObject, MouseEvent aEvent );
}
