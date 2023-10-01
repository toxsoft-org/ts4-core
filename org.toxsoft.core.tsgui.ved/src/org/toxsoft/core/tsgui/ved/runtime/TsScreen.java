package org.toxsoft.core.tsgui.ved.runtime;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class TsScreen {

  /**
   * TODO do we need size of the screen ?
   */

  private final Canvas canvas;

  /**
   * Constructor.
   *
   * @param aParent {@link Composite} - a composite control which will be the parent of the new instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsScreen( Composite aParent ) {
    TsNullArgumentRtException.checkNull( aParent );
    canvas = new Canvas( aParent, SWT.NONE );
  }

}
