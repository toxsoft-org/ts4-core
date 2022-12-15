package org.toxsoft.core.jasperreports.gui.main;

import static org.toxsoft.core.jasperreports.gui.main.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;

import net.sf.jasperreports.engine.*;

/**
 * Диалог отображения предварительного просмотра и печати отчёта Jasper
 *
 * @author max
 */
public class JasperReportDialog
    extends AbstractTsDialogPanel<JasperPrint, ITsGuiContext> {

  private Composite tableComposite;

  protected JasperReportDialog( Composite aParent, TsDialog<JasperPrint, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    tableComposite = new Composite( aParent, SWT.NONE );
    tableComposite.setLayout( new BorderLayout() );
  }

  @Override
  protected void doSetDataRecord( JasperPrint aData ) {
    JasperReportViewer viewer = new JasperReportViewer( tableComposite, tsContext() );
    viewer.displeyJasperReportPrint( tsContext(), aData );

    viewer.setLayoutData( BorderLayout.CENTER );

    tableComposite.layout();
  }

  @Override
  protected JasperPrint doGetDataRecord() {
    // nop
    return null;
  }

  /**
   * Вызывает диалог отображения предварительного просмотра и печати отчёта Jasper
   *
   * @param aContext ITsGuiContext - контекст
   * @param aJasperPrint ITsGuiContext - готовый отчёт Jasper
   */
  public static void showPrint( ITsGuiContext aContext, final JasperPrint aJasperPrint ) {

    TsNullArgumentRtException.checkNulls( aContext, aJasperPrint );

    IDialogPanelCreator<JasperPrint, ITsGuiContext> creator = JasperReportDialog::new;

    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, STR_PRINT, STR_PRINT_PREVIEW );
    TsDialog<JasperPrint, ITsGuiContext> d = new TsDialog<>( dlgInfo, aJasperPrint, aContext, creator );
    d.execData();
  }

}
