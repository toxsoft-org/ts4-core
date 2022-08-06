package org.toxsoft.core.tsgui.ved.tools_old;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.toxsoft.core.tsgui.bricks.swtevents.*;

public class VedPointerToolKeyListener
    implements ISwtKeyListener {

  // ------------------------------------------------------------------------------------
  // ISwtKeyListener
  //

  @Override
  public void keyPressed( KeyEvent aE ) {
    switch( aE.keyCode ) {
      case SWT.ARROW_LEFT:
      case SWT.ARROW_RIGHT:
      case SWT.ARROW_UP:
      case SWT.ARROW_DOWN:
        System.out.println( "Key code = " + aE.keyCode );
        break;
      default:
        break;
    }
  }

}
