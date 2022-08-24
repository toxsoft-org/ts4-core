package org.toxsoft.core.tsgui.valed.controls.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;

public class PanelColorFillInfo
    extends TsPanel {

  RgbaSelector rgbaSelector;

  public PanelColorFillInfo( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    setLayout( new BorderLayout() );

    Composite topPanel = new Composite( this, SWT.NONE );
    topPanel.setLayout( new RowLayout( SWT.HORIZONTAL ) );
    topPanel.setLayoutData( BorderLayout.SOUTH );

    Button btn = new Button( topPanel, SWT.PUSH );
    btn.setText( "Выбрать цвет..." );
    btn.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        ColorDialog dlg = new ColorDialog( getShell() );
        RGB rgb = dlg.open();
        if( rgb != null ) {
          rgbaSelector.setRgb( rgb );
        }
      }
    } );

    rgbaSelector = new RgbaSelector( this, SWT.BORDER, aContext.eclipseContext() );
    rgbaSelector.setLayoutData( BorderLayout.CENTER );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает выбранные параметры цвета.<br>
   *
   * @return RGBA - выбранные параметры цвета
   */
  public RGBA rgba() {
    return rgbaSelector.rgba();
  }

}
