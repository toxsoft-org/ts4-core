package org.toxsoft.core.jasperreports.gui.main;

import static org.toxsoft.core.jasperreports.gui.main.ITsResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;

import java.awt.image.*;
import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.singlesrc.rcp.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.jasperassistant.designer.viewer.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.export.ooxml.*;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.view.*;

/**
 * Панель отображения сформированного отчёта, готового для печати и экспорта.
 *
 * @author vs, max
 */
public class TsReportViewer
    extends Composite
    implements IReportViewer {

  private static final String CURR_PAGE_TEXT_FORMAT = "%d / %d"; //$NON-NLS-1$

  private final static String ACTID_PREFIX = TS_ID + ".jasper.report.viewer.action"; //$NON-NLS-1$

  private final static String ACTID_FIRST_PAGE_REPORT  = ACTID_PREFIX + ".ReportFirstPage";  //$NON-NLS-1$
  private final static String ACTID_LAST_PAGE_REPORT   = ACTID_PREFIX + ".ReportLastPage";   //$NON-NLS-1$
  private final static String ACTID_PREV_PAGE_REPORT   = ACTID_PREFIX + ".ReportPrevPage";   //$NON-NLS-1$
  private final static String ACTID_NEXT_PAGE_REPORT   = ACTID_PREFIX + ".ReportNextPage";   //$NON-NLS-1$
  private final static String ACTID_CHOOSE_PAGE_REPORT = ACTID_PREFIX + ".ReportChoosePage"; //$NON-NLS-1$
  private final static String ACTID_ZOOMFACTOR_REPORT  = ACTID_PREFIX + ".ReportZoomfactor"; //$NON-NLS-1$
  private final static String ACTID_PRINT_REPORT       = ACTID_PREFIX + ".ReportPrint";      //$NON-NLS-1$
  private final static String ACTID_EXPORT_PDF_REPORT  = ACTID_PREFIX + ".ReportExportPdf";  //$NON-NLS-1$
  private final static String ACTID_EXPORT_XLS_REPORT  = ACTID_PREFIX + ".ReportExportXls";  //$NON-NLS-1$

  private final static TsActionDef ACDEF_FIRST_PAGE_REPORT =
      TsActionDef.ofPush2( ACTID_FIRST_PAGE_REPORT, STR_T_FIRST_PAGE, STR_P_FIRST_PAGE, ICONID_GO_FIRST_VIEW_PAGE );

  private final static TsActionDef ACDEF_PREV_PAGE_REPORT =
      TsActionDef.ofPush2( ACTID_PREV_PAGE_REPORT, STR_T_PREV_PAGE, STR_P_PREV_PAGE, ICONID_GO_PREVIOUS_VIEW_PAGE );

  private final static TsActionDef ACDEF_CHOOSE_PAGE_REPORT =
      TsActionDef.ofMenu2( ACTID_CHOOSE_PAGE_REPORT, STR_T_DEFAULT_PAGE, STR_P_DEFAULT_PAGE, ICONID_EDIT_COPY );

  private final static TsActionDef ACDEF_NEXT_PAGE_REPORT =
      TsActionDef.ofPush2( ACTID_NEXT_PAGE_REPORT, STR_T_NEXT_PAGE, STR_P_NEXT_PAGE, ICONID_GO_NEXT_VIEW_PAGE );

  private final static TsActionDef ACDEF_LAST_PAGE_REPORT =
      TsActionDef.ofPush2( ACTID_LAST_PAGE_REPORT, STR_T_LAST_PAGE, STR_P_LAST_PAGE, ICONID_GO_LAST_VIEW_PAGE );

  private final static ITsActionDef ACDEF_ZOOMFACTOR_REPORT = TsActionDef.ofMenu2( ACTID_ZOOMFACTOR_REPORT,
      EZoomType.ZOOM_100.nmName(), EZoomType.ZOOM_100.description(), ICONID_ZOOM_ORIGINAL );

  private final static TsActionDef ACDEF_PRINT_REPORT =
      TsActionDef.ofPush2( ACTID_PRINT_REPORT, STR_PRINT, STR_PRINT, ICONID_DOCUMENT_PRINT );

  private final static TsActionDef ACDEF_EXPORT_PDF_REPORT =
      TsActionDef.ofPush2( ACTID_EXPORT_PDF_REPORT, STR_T_EXPORT_PDF, STR_P_EXPORT_PDF, ICONID_FILE_TYPE_PDF );

  private final static TsActionDef ACDEF_EXPORT_XLS_REPORT =
      TsActionDef.ofPush2( ACTID_EXPORT_XLS_REPORT, STR_T_EXPORT_XLS, STR_P_EXPORT_XLS, ICONID_FILE_TYPE_SPREADSHEET );

  private IListEdit<IReportViewerActionListener> listeners = new ElemArrayList<>();

  private TsToolbar toolBar;

  private Image swtImg;

  private final ITsGuiContext context;

  private final JasperPrint jasperPrint;

  private final ScrolledComposite scrollComp;

  private final Composite contentPanel;

  private final Composite imagePanel;

  private final int pageCount;

  private int pageNo = 0;

  private EZoomType zoomFactor;

  /**
   * Конструктор просмоторщика печатных форм jasper по контексту
   *
   * @param aContext - контекст
   * @param aParentComposite - родительский компонент
   * @param aJasperPrint - печатная форма
   */
  public TsReportViewer( ITsGuiContext aContext, Composite aParentComposite, JasperPrint aJasperPrint ) {
    super( aParentComposite, SWT.FILL );
    context = aContext;
    jasperPrint = aJasperPrint;

    setLayout( new BorderLayout() );
    pageCount = jasperPrint.getPages().size();

    toolBar = new TsToolbar( context );

    toolBar.setIconSize( EIconSize.IS_24X24 );

    toolBar.addActionDef( ACDEF_FIRST_PAGE_REPORT );
    toolBar.addActionDef( ACDEF_PREV_PAGE_REPORT );
    toolBar.addActionDef( ACDEF_CHOOSE_PAGE_REPORT );
    toolBar.addActionDef( ACDEF_NEXT_PAGE_REPORT );
    toolBar.addActionDef( ACDEF_LAST_PAGE_REPORT );

    toolBar.addSeparator();

    toolBar.addActionDef( ACDEF_ZOOM_OUT );
    toolBar.addActionDef( ACDEF_ZOOMFACTOR_REPORT );
    toolBar.addActionDef( ACDEF_ZOOM_IN );

    toolBar.addSeparator();

    toolBar.addActionDef( ACDEF_EXPORT_XLS_REPORT );
    toolBar.addActionDef( ACDEF_EXPORT_PDF_REPORT );

    toolBar.addSeparator();

    toolBar.addActionDef( ACDEF_PRINT_REPORT );

    toolBar.addListener( aActionId -> {
      if( aActionId.equals( ACDEF_EXPORT_XLS_REPORT.id() ) ) {

        FileDialog fd = new FileDialog( getShell(), SWT.SAVE | SWT.APPLICATION_MODAL );
        fd.setFilterExtensions( new String[] { "*.xls" } ); //$NON-NLS-1$
        String xlsFilePath = fd.open();

        if( xlsFilePath == null ) {
          return;
        }

        if( new File( xlsFilePath ).exists() ) {
          ETsDialogCode dialogResult =
              TsDialogUtils.askYesNoCancel( getShell(), STR_FILE_EXISTS_OVERWRITE_IT, xlsFilePath );
          if( dialogResult != ETsDialogCode.YES && dialogResult != ETsDialogCode.OK ) {
            return;
          }
        }

        JRXlsxExporter xlsExporter = new JRXlsxExporter();

        xlsExporter.setExporterInput( new SimpleExporterInput( jasperPrint ) );
        xlsExporter.setExporterOutput( new SimpleOutputStreamExporterOutput( xlsFilePath ) );
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        // TODO: set exporter configuration
        configuration.setOnePagePerSheet( Boolean.FALSE );
        configuration.setDetectCellType( Boolean.TRUE );
        xlsExporter.setConfiguration( configuration );

        try {
          xlsExporter.exportReport();
        }
        catch( JRException ex ) {
          LoggerUtils.errorLogger().error( ex );
          TsDialogUtils.error( getShell(), ex );
        }

        return;

      }
      if( aActionId.equals( ACDEF_EXPORT_PDF_REPORT.id() ) ) {

        FileDialog fd = new FileDialog( getShell(), SWT.SAVE | SWT.APPLICATION_MODAL );
        fd.setFilterExtensions( new String[] { "*.pdf" } ); //$NON-NLS-1$
        String pdfFilePath = fd.open();

        if( pdfFilePath == null ) {
          return;
        }

        if( new File( pdfFilePath ).exists() ) {
          ETsDialogCode dialogResult =
              TsDialogUtils.askYesNoCancel( getShell(), STR_FILE_EXISTS_OVERWRITE_IT, pdfFilePath );
          if( dialogResult != ETsDialogCode.YES && dialogResult != ETsDialogCode.OK ) {
            return;
          }
        }

        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput( new SimpleExporterInput( jasperPrint ) );
        exporter.setExporterOutput( new SimpleOutputStreamExporterOutput( pdfFilePath ) );
        exporter.setConfiguration( configuration );

        try {
          exporter.exportReport();
        }
        catch( JRException ex ) {
          LoggerUtils.errorLogger().error( ex );
          TsDialogUtils.error( getShell(), ex );
        }
        return;
      }
      if( aActionId.equals( ACDEF_PRINT_REPORT.id() ) ) {
        // new PrintAction( this ).run();
        try {
          JasperPrintManager.printReport( jasperPrint, true );
        }
        catch( JRException ex ) {
          LoggerUtils.errorLogger().error( ex );
          TsDialogUtils.error( getShell(), ex );
        }
        return;
      }

      if( aActionId.equals( ACDEF_FIRST_PAGE_REPORT.id() ) ) {
        // new FirstPageAction( this ).run();
        gotoFirstPage();
        return;
      }

      if( aActionId.equals( ACDEF_PREV_PAGE_REPORT.id() ) ) {
        // new PreviousPageAction( this ).run();
        gotoPreviousPage();
        return;
      }

      if( aActionId.equals( ACDEF_NEXT_PAGE_REPORT.id() ) ) {
        // new NextPageAction( this ).run();
        gotoNextPage();
        return;
      }

      if( aActionId.equals( ACDEF_LAST_PAGE_REPORT.id() ) ) {
        // new LastPageAction( this ).run();
        gotoLastPage();
        return;
      }

      if( aActionId.equals( ACDEF_CHOOSE_PAGE_REPORT.id() ) ) {
        setPageIndex( pageCount / 2 );
        return;
      }

      if( aActionId.equals( ACDEF_ZOOM_IN.id() ) ) {
        // new ZoomInAction( this ).run();
        zoomIn();
        return;
      }

      if( aActionId.equals( ACDEF_ZOOM_OUT.id() ) ) {
        // new ZoomOutAction( this ).run();
        zoomOut();
        return;
      }

      if( aActionId.equals( ACDEF_ZOOMFACTOR_REPORT.id() ) ) {
        setZoom( 1 );
        return;
      }

    } );

    zoomFactor = EZoomType.ZOOM_100;

    Control toolbarCtrl = toolBar.createControl( this );
    toolbarCtrl.setLayoutData( BorderLayout.NORTH );

    toolBar.setActionMenu( ACTID_ZOOMFACTOR_REPORT, new ZoomItemsMenuCreator() );
    toolBar.setActionMenu( ACTID_CHOOSE_PAGE_REPORT, new PageChooseMenuCreator( pageCount ) );

    scrollComp = new ScrolledComposite( this, SWT.V_SCROLL | SWT.H_SCROLL );
    scrollComp.setLayoutData( BorderLayout.CENTER );

    ITsColorManager colorMgr = aContext.eclipseContext().get( ITsColorManager.class );

    contentPanel = new Composite( scrollComp, SWT.NONE );
    contentPanel.setBackground( colorMgr.getColor( new RGB( 62, 62, 62 ) ) );
    contentPanel.setLayout( null );

    scrollComp.setContent( contentPanel );

    imagePanel = new Composite( contentPanel, SWT.NONE );

    setPageIndex( pageNo );

    addControlListener( new ControlListener() {

      @Override
      public void controlResized( ControlEvent e ) {
        layout();
        onResize();
        // TODO: workaround:
        scrollRestoreWorkaround();
      }

      @Override
      public void controlMoved( ControlEvent e ) {
        // Ничего не делаем
      }
    } );

    TsSinglesourcingUtils.Control_addMouseWheelListener( aParentComposite, aEvent -> {
      // Признак движения колеса ВНИЗ (НА СЕБЯ). В противном случае ВВЕРХ (ОТ СЕБЯ)
      boolean scrollDown = (aEvent.count < 0);
      // Отработка масштабирования через Ctrl+колесо мыши (mouse wheel scroll)
      if( (aEvent.stateMask & SWT.CONTROL) == SWT.CONTROL ) {
        if( scrollDown ) {
          zoomOut();
          return;
        }
        zoomIn();
        return;
      }
      // Передача управления компоненту сроллирования
      scrollComp.setFocus();
    } );

    TsSinglesourcingUtils.Control_addMouseWheelListener( scrollComp, aEvent -> {
      // Признак движения колеса ВНИЗ (НА СЕБЯ). В противном случае ВВЕРХ (ОТ СЕБЯ)
      boolean scrollDown = (aEvent.count < 0);
      int selectionMin = scrollComp.getVerticalBar().getMinimum();
      int selectionMax = scrollComp.getVerticalBar().getMaximum() - scrollComp.getVerticalBar().getThumb();
      int selection = scrollComp.getVerticalBar().getSelection();
      if( !scrollDown && selection == selectionMin && canGotoPreviousPage() ) {
        // Переход на предыдущую страницу
        gotoPreviousPage();
        scrollComp.getVerticalBar().setSelection( selectionMax );
        return;
      }
      if( scrollDown && selection == selectionMax && canGotoNextPage() ) {
        // Переход на следующую страницу
        gotoNextPage();
        scrollComp.getVerticalBar().setSelection( selectionMin );
      }
    } );
    // TODO: workaround:
    scrollRestoreWorkaround();
  }

  void scrollRestoreWorkaround() {
    final Display diplay = Display.getCurrent();
    Thread thread = new Thread( () -> {
      try {
        Thread.sleep( 500 );
      }
      catch( InterruptedException ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
      diplay.syncExec( () -> {
        scrollComp.setFocus();
        // pageSpinner.setFocus();
      } );

    } );
    thread.start();
  }

  @Override
  protected void checkSubclass() {
    // nop для того чтобы разрешить subclassing
  }

  // --------------------------------------------------------------------------
  // Реализация методов IReportViewer
  //

  @Override
  public void addHyperlinkListener( JRHyperlinkListener arg0 ) {
    // nop
  }

  @Override
  public void addReportViewerListener( IReportViewerListener arg0 ) {
    // nop
  }

  @Override
  public boolean canChangeZoom() {
    // nop
    return false;
  }

  @Override
  public boolean canGotoFirstPage() {
    return pageNo > 0;
  }

  @Override
  public boolean canGotoLastPage() {
    return pageNo < pageCount - 1;
  }

  @Override
  public boolean canGotoNextPage() {
    return canGotoLastPage();
  }

  @Override
  public boolean canGotoPreviousPage() {
    return canGotoFirstPage();
  }

  @Override
  public boolean canReload() {
    // nop
    return false;
  }

  @Override
  public boolean canZoomIn() {
    return zoomFactor.ordinal() < EZoomType.values().length - 1;
  }

  @Override
  public boolean canZoomOut() {
    return zoomFactor.ordinal() > 0;
  }

  @Override
  public JasperPrint getDocument() {

    return jasperPrint;
  }

  @Override
  public JRHyperlinkListener[] getHyperlinkListeners() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getPageIndex() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String getReason() {
    // nop
    return null;
  }

  @Override
  public double getZoom() {
    // nop
    return 0;
  }

  @Override
  public double[] getZoomLevels() {
    // nop
    return null;
  }

  @Override
  public int getZoomMode() {
    // nop
    return 0;
  }

  @Override
  public void gotoFirstPage() {
    if( canGotoFirstPage() ) {
      pageNo = 0;
      setPageIndex( pageNo );
    }
  }

  @Override
  public void gotoLastPage() {
    if( canGotoLastPage() ) {
      pageNo = pageCount - 1;
      setPageIndex( pageNo );
    }
  }

  @Override
  public void gotoNextPage() {
    if( canGotoNextPage() ) {
      pageNo++;
      setPageIndex( pageNo );
    }
  }

  @Override
  public void gotoPreviousPage() {
    if( canGotoPreviousPage() ) {
      pageNo--;
      setPageIndex( pageNo );
    }
  }

  @Override
  public boolean hasDocument() {
    // nop
    return false;
  }

  @Override
  public void loadDocument( String arg0, boolean arg1 ) {
    // nop
  }

  @Override
  public void reload() {
    // nop
  }

  @Override
  public void removeHyperlinkListener( JRHyperlinkListener arg0 ) {
    // nop
  }

  @Override
  public void removeReportViewerListener( IReportViewerListener arg0 ) {
    // nop
  }

  @Override
  public void setDocument( JasperPrint arg0 ) {
    // nop
  }

  @Override
  public void setPageIndex( int aPageNo ) {
    if( aPageNo >= 0 && aPageNo < pageCount ) {
      pageNo = aPageNo;
      showPage( pageNo, zoomFactor.zoomFactor() );

      String currPageText =
          String.format( CURR_PAGE_TEXT_FORMAT, Integer.valueOf( aPageNo + 1 ), Integer.valueOf( pageCount ) );

      toolBar.setActionText( ACTID_CHOOSE_PAGE_REPORT, currPageText );
      toolBar.setActionTooltipText( ACTID_CHOOSE_PAGE_REPORT, currPageText );

      toolBar.setActionEnabled( ACTID_LAST_PAGE_REPORT, canGotoNextPage() );
      toolBar.setActionEnabled( ACTID_NEXT_PAGE_REPORT, canGotoNextPage() );
      toolBar.setActionEnabled( ACTID_PREV_PAGE_REPORT, canGotoPreviousPage() );
      toolBar.setActionEnabled( ACTID_FIRST_PAGE_REPORT, canGotoPreviousPage() );

      // pageSpinner.setSelection( pageNo + 1 );

      // FIXME раскомментировать
      // firstPageAction.setEnabled( canGotoFirstPage() );
      // prevPageAction.setEnabled( canGotoPreviousPage() );
      // nextPageAction.setEnabled( canGotoNextPage() );
      // lastPageAction.setEnabled( canGotoLastPage() );
    }
  }

  @Override
  public void setZoom( double aZoomFactor ) {
    zoomFactor = EZoomType.findByFactorValue( aZoomFactor );
    // zoomInAction.setEnabled( canZoomIn() );
    // zoomOutAction.setEnabled( canZoomOut() );
    // zoomCombo.select( aValue );
    showPage( pageNo, zoomFactor.zoomFactor() );

    toolBar.setActionText( ACTID_ZOOMFACTOR_REPORT, zoomFactor.nmName() );
    toolBar.setActionTooltipText( ACTID_ZOOMFACTOR_REPORT, zoomFactor.nmName() );

    toolBar.setActionEnabled( ACTID_ZOOM_IN, canZoomIn() );
    toolBar.setActionEnabled( ACTID_ZOOM_OUT, canZoomOut() );

  }

  @Override
  public void setZoomLevels( double[] aZoomLevels ) {
    // nop
  }

  @Override
  public void setZoomMode( int aZoomMode ) {
    // nop
  }

  @Override
  public void unsetDocument( String aReason ) {
    // nop
  }

  @Override
  public void zoomIn() {
    if( canZoomIn() ) {
      EZoomType zt = zoomFactor;
      int idx = zt.ordinal() + 1;
      zt = EZoomType.values()[idx];

      setZoom( zt.zoomFactor() );
    }
  }

  @Override
  public void zoomOut() {
    if( canZoomOut() ) {
      EZoomType zt = zoomFactor;
      int idx = zt.ordinal() - 1;
      zt = EZoomType.values()[idx];

      setZoom( zt.zoomFactor() );
    }
  }

  // --------------------------------------------------------------------------
  // Внутренняя реализация
  //

  private void showPage( int aPageNo, float aZoomFactor ) {
    try {
      if( swtImg != null ) {
        swtImg.dispose();
      }
      BufferedImage img = (BufferedImage)JasperPrintManager.printPageToImage( jasperPrint, aPageNo, aZoomFactor );

      ImageData imageData = convertToSWT( img );
      swtImg = new Image( Display.getCurrent(), imageData );

      // ---------------------------------------------------------------------------------------------------------------
      imagePanel.setBackgroundImage( swtImg );
      imagePanel.setSize( swtImg.getBounds().width, swtImg.getBounds().height );

      onResize();
    }
    catch( Throwable e ) {
      e.printStackTrace();
    }
  }

  /**
   * Реакция на изменение размера - коррекция размера.
   */
  public void onResize() {
    Rectangle r = scrollComp.getClientArea();

    int width = imagePanel.getBounds().width + 4;
    if( r.width > width ) {
      width = r.width;
    }

    int height = imagePanel.getBounds().height + 4;
    if( r.height > height ) {
      height = r.height;
    }

    contentPanel.setSize( width, height );

    int x = 0;
    int y = 0;
    if( width > imagePanel.getSize().x ) {
      x = (width - imagePanel.getSize().x) / 2;
    }
    if( height > imagePanel.getSize().y ) {
      y = (height - imagePanel.getSize().y) / 2;
    }
    imagePanel.setLocation( x, y );
  }

  /**
   * Добавляет слушателя событий действий просмоторщика печатной формы.
   *
   * @param aListener IReportViewerActionListener - слушатель событий действий просмоторщика печатной формы.
   */
  public void addReportViewerActionListener( IReportViewerActionListener aListener ) {
    listeners.add( aListener );
  }

  /**
   * Удаляет слушателя событий действий просмоторщика печатной формы.
   *
   * @param aListener IReportViewerActionListener - слушатель событий действий просмоторщика печатной формы.
   */
  public void removeReportViewerActionListener( IReportViewerActionListener aListener ) {
    listeners.remove( aListener );
  }

  void fireActionDone( String aActionId ) {
    ReportViewerActionEvent event = new ReportViewerActionEvent( this, aActionId );
    for( IReportViewerActionListener l : listeners ) {
      l.actionDone( event );
    }
  }

  void fireActionCanceled( String aActionId ) {
    ReportViewerActionEvent event = new ReportViewerActionEvent( this, aActionId );
    for( IReportViewerActionListener l : listeners ) {
      l.actionCanceled( event );
    }
  }

  void fireActionCalled( String aActionId ) {
    ReportViewerActionEvent event = new ReportViewerActionEvent( this, aActionId );
    for( IReportViewerActionListener l : listeners ) {
      l.actionCalled( event );
    }
  }

  void fireActionError( String aActionId ) {
    ReportViewerActionEvent event = new ReportViewerActionEvent( this, aActionId );
    for( IReportViewerActionListener l : listeners ) {
      l.actionError( event );
    }
  }

  /**
   * Изображения в SWT и AWT представляются различными структурами данных.<br>
   * При работе в SWT при необхоимости использования сторонних библиотек, например JasperReports, возникает
   * необходимость обмениваться изображениями в этих "форматах" AWT.<br>
   * Данная функция предназначена для преобразования изображения из "AWT" преставления в "SWT" представление.
   * Преобразование не меняет параметров изображения: цвета, размеры, прозрачность <br>
   * <br>
   * В отличие от стандартных snippets данная функция учитывает все варианты представления палитры изображения.
   *
   * @param aBufferedImage - представление изображения AWT
   * @return ImageData - представление изображения в SWT
   * @throws TsNullArgumentRtException - aBufferedImage - null
   */
  public static ImageData convertToSWT( BufferedImage aBufferedImage ) {
    if( aBufferedImage.getColorModel() instanceof DirectColorModel ) {
      DirectColorModel colorModel = (DirectColorModel)aBufferedImage.getColorModel();
      PaletteData palette =
          new PaletteData( colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask() );
      ImageData data =
          new ImageData( aBufferedImage.getWidth(), aBufferedImage.getHeight(), colorModel.getPixelSize(), palette );
      WritableRaster raster = aBufferedImage.getRaster();
      int[] pixelArray = new int[4];
      for( int y = 0; y < data.height; y++ ) {
        for( int x = 0; x < data.width; x++ ) {
          raster.getPixel( x, y, pixelArray );
          int pixel = palette.getPixel( new RGB( pixelArray[0], pixelArray[1], pixelArray[2] ) );
          data.setPixel( x, y, pixel );
          if( colorModel.hasAlpha() ) {
            data.setAlpha( x, y, pixelArray[3] );
          }
        }
      }
      return data;
    }
    else
      if( aBufferedImage.getColorModel() instanceof IndexColorModel ) {
        IndexColorModel colorModel = (IndexColorModel)aBufferedImage.getColorModel();
        int size = colorModel.getMapSize();
        byte[] reds = new byte[size];
        byte[] greens = new byte[size];
        byte[] blues = new byte[size];
        colorModel.getReds( reds );
        colorModel.getGreens( greens );
        colorModel.getBlues( blues );
        RGB[] rgbs = new RGB[size];
        for( int i = 0; i < rgbs.length; i++ ) {
          rgbs[i] = new RGB( reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF );
        }
        PaletteData palette = new PaletteData( rgbs );
        ImageData data =
            new ImageData( aBufferedImage.getWidth(), aBufferedImage.getHeight(), colorModel.getPixelSize(), palette );
        data.transparentPixel = colorModel.getTransparentPixel();
        WritableRaster raster = aBufferedImage.getRaster();
        int[] pixelArray = new int[1];
        for( int y = 0; y < data.height; y++ ) {
          for( int x = 0; x < data.width; x++ ) {
            raster.getPixel( x, y, pixelArray );
            data.setPixel( x, y, pixelArray[0] );
          }
        }
        return data;
      }
    return null;
  }

  /**
   * Creator of zoom menu
   *
   * @author max
   */
  private class ZoomItemsMenuCreator
      extends AbstractMenuCreator {

    @Override
    protected boolean fillMenu( Menu aMenu ) {
      // separator
      MenuItem mItem = new MenuItem( aMenu, SWT.SEPARATOR );
      // one menu item per one EZoomType constant
      for( EZoomType sz : EZoomType.values() ) {
        mItem = new MenuItem( aMenu, SWT.CHECK );
        mItem.setText( sz.nmName() );
        mItem.setToolTipText( sz.description() );
        mItem.addSelectionListener( new SelectionAdapter() {

          @Override
          public void widgetSelected( SelectionEvent e ) {
            setZoom( sz.zoomFactor() );
          }
        } );
        mItem.setSelection( sz == zoomFactor );
      }
      return true;
    }

  }

  /**
   * Creator of page choose menu
   *
   * @author max
   */
  private class PageChooseMenuCreator
      extends AbstractMenuCreator {

    private int menuPageCount;

    public PageChooseMenuCreator( int aMenuPageCount ) {
      menuPageCount = aMenuPageCount;
    }

    @Override
    protected boolean fillMenu( Menu aMenu ) {
      // separator
      MenuItem mItem = new MenuItem( aMenu, SWT.SEPARATOR );
      // one menu item per report page
      for( int n = 0; n < menuPageCount; n++ ) {
        int number = n;
        mItem = new MenuItem( aMenu, SWT.CHECK );
        mItem.setText( String.valueOf( number + 1 ) );
        mItem.setToolTipText( String.valueOf( number + 1 ) );
        mItem.addSelectionListener( new SelectionAdapter() {

          @Override
          public void widgetSelected( SelectionEvent e ) {
            setPageIndex( number );
          }
        } );
        mItem.setSelection( number == pageNo );
      }
      return true;
    }
  }

}
