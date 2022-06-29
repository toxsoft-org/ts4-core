package org.toxsoft.core.tsgui.ved.utils.drag;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.av.utils.*;

public interface IVedVertex
    extends IScreenObject, IVedPainter, IParameterized {

  ECursorType cursorType();

  void setForeground( Color aColor );

  void setBackground( Color aColor );

  Color foregroundColor();

  Color backgroundColor();
}
