package org.toxsoft.core.tsgui.valed.controls.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;

/**
 * Панель редактирования параметров заливки изображением.
 * <p>
 *
 * @author vs
 */
public class PanelImageFillInfo
    extends TsPanel {

  PanelImageFillInfo( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    setLayout( new BorderLayout() );
    CLabel l = new CLabel( this, SWT.CENTER );
    l.setText( "Панель находится в стадии under development" ); //$NON-NLS-1$
    l.setLayoutData( BorderLayout.CENTER );
  }

}
