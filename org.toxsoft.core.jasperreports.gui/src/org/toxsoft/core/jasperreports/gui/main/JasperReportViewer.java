package org.toxsoft.core.jasperreports.gui.main;

import static org.toxsoft.core.jasperreports.gui.main.ITsResources.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.operation.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;

import net.sf.jasperreports.engine.*;

/**
 * Панель для отображения конкретного отчета в виде печатной формы.
 *
 * @author max
 */
public class JasperReportViewer
    extends TsPanel {

  /**
   * Панель отображения табличного отчёта - изначально пустой
   */
  private Composite tableComposite;

  /**
   * Отображатель табличного отчёта - формируется по запросу и вставляется в панель отображения табличного отчёта.
   */
  private TsReportViewer viewer;

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public JasperReportViewer( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    doDoInit( aParent );
  }

  protected void doDoInit( Composite aParent ) {
    tableComposite = new Composite( aParent, SWT.NONE );
    tableComposite.setLayout( new BorderLayout() );
  }

  //
  // -----------------------------------------------
  // Апишные методы

  /**
   * Устанавливает отчёт в виде модели и набора данных M5
   *
   * @param <T> - класс сущности модели
   * @param aContext ITsGuiContext - контекст
   * @param aModel IM5Model - модель данных M5
   * @param aProvider IM5ItemsProvider - поставщик данных M5
   */
  public <T> void setJasperReportPrint( ITsGuiContext aContext, IM5Model<T> aModel, IM5ItemsProvider<T> aProvider ) {

    runInWaitingDialog( STR_REPORT_FORMING,
        aMonitor -> formAndShowJasperReport( aContext, aMonitor, aModel, aProvider ) );
  }

  /**
   * Устанавливает отчёт в виде указания пути к нескомпилированному jasper отчёту, источника данных и набора параметров
   * отчёта jasper.
   *
   * @param aContext ITsGuiContext - контекст
   * @param aJasperReportPath String - путь к нескомпилированному jasper отчёту
   * @param aDataSource JRDataSource - источник данных jasper
   * @param aReportParameters Map - набор параметров отчёта jasper
   */
  public void setJasperReportPrint( ITsGuiContext aContext, String aJasperReportPath, JRDataSource aDataSource,
      Map<String, Object> aReportParameters ) {

    runInWaitingDialog( STR_REPORT_FORMING,
        aMonitor -> formAndShowJasperReport( aContext, aMonitor, aJasperReportPath, aDataSource, aReportParameters ) );

  }

  /**
   * Отображает готовый jasper отчёт.
   *
   * @param aContext ITsGuiContext - контекст
   * @param aJasperPrint JasperPrint - готовый jasper отчёт
   */
  public void displeyJasperReportPrint( ITsGuiContext aContext, JasperPrint aJasperPrint ) {
    Display.getDefault().syncExec( () -> {
      if( viewer != null ) {
        viewer.dispose();
        System.out.println( "DISPOSED" ); //$NON-NLS-1$
      }
      viewer = new TsReportViewer( aContext, tableComposite, aJasperPrint );
      viewer.setLayoutData( BorderLayout.CENTER );

      tableComposite.layout();
    } );
  }

  //
  // --------------------------------------------
  // Вспомогательные методы

  /**
   * Формирует отчёт по данным и модели M5 и открывает его отображение.
   *
   * @param <T> - класс сущности модели
   * @param aContext ITsGuiContext - контекст
   * @param aMonitor IProgressMonitor - монитор диалога длительного процесса.
   * @param aModel IM5Model - модель данных M5
   * @param aProvider IM5ItemsProvider - поставщик данных M5
   * @throws InvocationTargetException - в случае ошибки формирования диалога.
   */
  private <T> void formAndShowJasperReport( ITsGuiContext aContext, IProgressMonitor aMonitor, IM5Model<T> aModel,
      IM5ItemsProvider<T> aProvider )
      throws InvocationTargetException {
    try {
      aMonitor.subTask( STR_REPORT_FORMING_STATE_STR );
      final JasperPrint jasperPrint = ReportGenerator.generateJasperPrint( aContext, aModel, aProvider );

      aMonitor.subTask( STR_REPORT_VIEW_OPEN );

      displeyJasperReportPrint( aContext, jasperPrint );
    }
    catch( JRException ee ) {

      ee.printStackTrace();
      throw new InvocationTargetException( ee );
    }
    aMonitor.done();
  }

  /**
   * Формирует отчёт по статическому отчёту и данным для него.
   *
   * @param aContext ITsGuiContext - контекст
   * @param aMonitor IProgressMonitor - монитор диалога длительного процесса.
   * @param aJasperReportPath String - путь к нескомпилированному jasper отчёту
   * @param aDataSource JRDataSource - источник данных jasper
   * @param aReportParameters Map - набор параметров отчёта jasper
   * @throws InvocationTargetException - в случае ошибки формирования диалога.
   */
  private void formAndShowJasperReport( ITsGuiContext aContext, IProgressMonitor aMonitor, String aJasperReportPath,
      JRDataSource aDataSource, Map<String, Object> aReportParameters )
      throws InvocationTargetException {

    try {

      aMonitor.subTask( STR_REPORT_FORMING_STATE_STR );
      // TODO try-with-resource
      InputStream stream = getClass().getClassLoader().getResourceAsStream( aJasperReportPath );
      JasperReport jasperReport = JasperCompileManager.compileReport( stream );
      final JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, aReportParameters, aDataSource );
      stream.close();

      aMonitor.subTask( STR_REPORT_VIEW_OPEN );

      displeyJasperReportPrint( aContext, jasperPrint );

    }
    catch( Exception ee ) {
      throw new InvocationTargetException( ee );
    }
    aMonitor.done();
  }

  /**
   * Выполняет работы в отдельном потоке при открытом диалоге ожидания.
   *
   * @param aDialogName String - имя диалога ожидания.
   * @param aRunnable IRunnableWithProgress - реализация потока выполнения работы.
   */
  private void runInWaitingDialog( final String aDialogName, final IRunnableWithProgress aRunnable ) {

    final ProgressMonitorDialog dialog = new ProgressMonitorDialog( getShell() ) {

      @Override
      protected Control createDialogArea( Composite aParent ) {
        Control c = super.createDialogArea( aParent );
        c.getShell().setText( aDialogName );
        return c;
      }
    };

    try {
      dialog.run( true, true, aRunnable );
    }
    catch( InvocationTargetException | InterruptedException e ) {
      Display.getDefault()
          .asyncExec( () -> TsDialogUtils.error( getShell(), e.getCause() != null ? e.getCause() : e ) );

    }

  }
}
