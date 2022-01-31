package org.toxsoft.core.tsgui.mws.e4.uiparts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tsgui.mws.bases.MwsAbstractPart;

/**
 * Simple view for temporary use.
 * <p>
 * For MWS framework to work correctly, at least one view must be visible on application startup. This is the default
 * view visiblke in default MWS perspective.
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
