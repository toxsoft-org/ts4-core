package org.toxsoft.core.tsgui.mws.e4.uiparts;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.mws.bases.*;

/**
 * Simple view for temporary use.
 * <p>
 * For MWS framework to work correctly, at least one view must be visible on application startup. This is the default
 * view visible in default MWS perspective.
 *
 * @author hazard157
 */
public class UipartFoo
    extends MwsAbstractPart {

  @Override
  protected void doInit( Composite aParent ) {

    Button b = new Button( aParent, SWT.PUSH );
    b.setText( getClass().getName() );
    b.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        TsDialogUtils.underDevelopment( getShell() );
      }

    } );

  }

}
