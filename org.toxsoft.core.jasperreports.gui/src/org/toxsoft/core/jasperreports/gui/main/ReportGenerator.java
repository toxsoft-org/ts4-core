package org.toxsoft.core.jasperreports.gui.main;

import static org.toxsoft.core.jasperreports.gui.main.ITsResources.*;

import java.awt.*;
import java.text.*;
import java.util.*;
import java.util.List;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.*;

/**
 * Генератор отчётов на лету по M5 модели, также формирующий готовые отчёты из поставщика данных M5
 *
 * @author max
 */
public class ReportGenerator {

  /**
   * формат для отображения метки времени
   */
  private static final String     timestampFormatString = "dd.MM.yy HH:mm:ss";                          //$NON-NLS-1$
  private static final DateFormat timestampFormat       = new SimpleDateFormat( timestampFormatString );

  /**
   * Ширина страницы типа пейзаж
   */
  private static final int LANDSCAPE_PAGE_WIDTH   = 842;
  /**
   * Ширина страницы типа портрет
   */
  private static final int PORTRAIT_PAGE_WIDTH    = 595;
  /**
   * Вертикальный отступ
   */
  private static final int VERTICAL_PAGE_MARGIN   = 20;
  /**
   * Горизонтальный отступ
   */
  private static final int HORIZONTAL_PAGE_MARGIN = 30;

  /**
   * Дата создания отчета
   */
  public static final String GENERATION_DATE = "generation_date";
  /**
   * Поле метки времени (для отчетов типа МП-ИВ)
   */
  public static final String TIMESTAMP       = "timestamp_field";
  /**
   * Поле названия объекта (для отчетов типа МП-МО-ТВ)
   */
  public static final String OBJ_NAME        = "obj_name_field";
  /**
   * Поле названия метода агрегации (для summary)
   */
  public static final String AGGR_FUNC_NAME  = "aggr_func_field";
  /**
   * Динамически создаваемая колонка с данными
   */
  public static final String DATA_COLUMN_    = "data_column_";
  /**
   * ФИО создаетля отчета
   */
  public static final String PERFORMER_NAME  = "performer_name";
  /**
   * Заголовок отчета
   */
  public static final String REPORT_TITLE    = "report_title";

  /**
   * Нижняя левая надпись
   */
  public static final String LEFT_BOTTOM_STR = "left_bottom_title";

  /**
   * Нижняя правая надпись
   */
  public static final String RIGHT_BOTTOM_STR = "right_bottom_title";

  /**
   * Левая граница интервала отчета
   */
  public static final String DATE_FROM = "date_from";
  /**
   * Правая граница интервала отчета
   */
  public static final String DATE_TO   = "date_to";

  private static final int REPORT_TITLE_HEIGHT                    = 32; // 70;
  private static final int REPORT_VERTICAL_COLUMN_HEADER_HEIGHT   = 64; // Высота при вертикальном расположении названия
                                                                        // колонок
  private static final int REPORT_HORIZONTAL_COLUMN_HEADER_HEIGHT = 15; // Высота при горизонтальном расположении
  // заголовка
  // таблицы
  private static final int PROTOCOL_PAGE_HEADER_HEIGHT = 0;
  /**
   * ширина первой колонки (метка времени данных)
   */
  private static final int TIMESTAMP_COLUMN_WIDTH      = 40;
  private static final int REPORT_DETAIL_HEIGHT        = 12;
  private static final int REPORT_SUMMARY_HEIGHT       = 45;
  private static final int PERFORMER_NAME_WIDTH        = 60;
  private static final int SUMMARY_ROW_HEIGHT          = 20;
  private static final int COLUMN_SPACE                = 10;

  static final String SUBREPORT_DS = "subreport_ds";
  static final String SUBREPORT_JR = "subreport_jr";

  /**
   * Создает шрифт стиля "курсив"
   *
   * @return объект типа JRDesignStyle
   */
  @SuppressWarnings( "boxing" )
  private static JRDesignStyle getItalicStyle() {
    JRDesignStyle italicStyle = new JRDesignStyle();
    italicStyle.setName( "Arial_Italic" );
    italicStyle.setFontName( "Serif" );
    italicStyle.setFontSize( 8f );
    italicStyle.setItalic( true );
    italicStyle.setPdfFontName( "arial.ttf" );
    italicStyle.setPdfEncoding( "Cp1251" );
    italicStyle.setPdfEmbedded( false );
    return italicStyle;
  }

  /**
   * Создает стиль для заголовка отчета (жирный и большой шрифт)
   *
   * @return объект типа JRDesignStyle
   */
  @SuppressWarnings( "boxing" )
  private static JRDesignStyle getTitleStyle() {
    JRDesignStyle boldStyle = new JRDesignStyle();
    boldStyle.setName( "Arial_Bold" );
    boldStyle.setFontName( "Serif" );
    boldStyle.setFontSize( 12f );
    boldStyle.setBold( true );
    boldStyle.setPdfFontName( "arial.ttf" );
    boldStyle.setPdfEncoding( "Cp1251" );
    boldStyle.setPdfEmbedded( false );
    return boldStyle;
  }

  /**
   * Создает шрифт стиля заголовка таблицы
   *
   * @return объект типа JRDesignStyle
   */
  @SuppressWarnings( "boxing" )
  private static JRDesignStyle getHeaderStyle() {
    JRDesignStyle boldStyle = new JRDesignStyle();
    boldStyle.setName( "Arial_Header" );
    boldStyle.setFontName( "Serif" );
    boldStyle.setFontSize( 10f );
    boldStyle.setBold( true );
    // boldStyle.setItalic( true );
    boldStyle.setPdfFontName( "arial.ttf" );
    boldStyle.setPdfEncoding( "Cp1251" );
    boldStyle.setPdfEmbedded( false );
    JRLineBox lineBox = boldStyle.getLineBox();
    lineBox.getTopPen().setLineWidth( 0.1f );
    lineBox.getRightPen().setLineWidth( 0.1f );
    lineBox.getBottomPen().setLineWidth( 0.1f );
    lineBox.getLeftPen().setLineWidth( 0.1f );

    // boldStyle.setBackcolor( Color.BLUE );

    // boldStyle.setForecolor( Color.RED );
    boldStyle.setMode( ModeEnum.OPAQUE );
    return boldStyle;
  }

  /**
   * Создает стиль для заголовка отчета
   *
   * @return объект типа JRDesignStyle
   */
  @SuppressWarnings( "boxing" )
  private static JRDesignStyle getSummaryStyle() {
    JRDesignStyle boldStyle = new JRDesignStyle();
    boldStyle.setName( "Arial_Bold" );
    boldStyle.setFontName( "Serif" );
    boldStyle.setFontSize( 10f );
    boldStyle.setBold( true );
    boldStyle.setPdfFontName( "arial.ttf" );
    boldStyle.setPdfEncoding( "Cp1251" );
    boldStyle.setPdfEmbedded( false );
    JRLineBox lineBox = boldStyle.getLineBox();
    lineBox.getTopPen().setLineWidth( 0.5f );
    lineBox.getRightPen().setLineWidth( 0.5f );
    lineBox.getBottomPen().setLineWidth( 0.5f );
    lineBox.getLeftPen().setLineWidth( 0.5f );
    return boldStyle;
  }

  /**
   * Создает шрифт нормального стиля
   *
   * @return объект типа JRDesignStyle
   */
  @SuppressWarnings( "boxing" )
  private static JRDesignStyle getNormalStyle() {
    JRDesignStyle normalStyle = new JRDesignStyle();
    normalStyle.setName( "Arial_Normal" );
    normalStyle.setDefault( true );
    normalStyle.setFontName( "Serif" );
    normalStyle.setFontSize( 10f );
    normalStyle.setPdfFontName( "arial.ttf" );
    normalStyle.setPdfEncoding( "Cp1251" );
    normalStyle.setPdfEmbedded( false );

    JRLineBox lineBox = normalStyle.getLineBox();
    lineBox.getTopPen().setLineWidth( 0.5f );
    lineBox.getRightPen().setLineWidth( 0.5f );
    lineBox.getBottomPen().setLineWidth( 0.5f );
    lineBox.getLeftPen().setLineWidth( 0.5f );

    lineBox.setLeftPadding( 5 );
    lineBox.setRightPadding( 5 );
    lineBox.setBottomPadding( 3 );
    lineBox.setTopPadding( 3 );
    return normalStyle;
  }

  /**
   * На основе описания модели и провайдера данных создает объект для печати из JasperReport
   *
   * @param <T> тип данных
   * @param aContext контекст
   * @param aModel модель данных
   * @param aItemsProvider поставщик данных
   * @return объект для печати
   * @throws JRException исключение при создании печатной формы
   */
  public static <T> JasperPrint generateJasperPrint( ITsGuiContext aContext, IM5Model<T> aModel,
      IM5ItemsProvider<T> aItemsProvider )
      throws JRException {
    return jasperPrint( aContext, aModel, aItemsProvider, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, 0, false );
  }

  /**
   * На основе описания модели и провайдера данных создает объект для печати из JasperReport
   *
   * @param <T> тип данных
   * @param aContext контекст
   * @param aModel модель данных
   * @param aItemsProvider поставщик данных
   * @return объект для печати
   * @throws JRException исключение при создании печатной формы
   */
  public static <T> JasperPrint generateJasperPrint2( ITsGuiContext aContext, IM5Model<T> aModel,
      IM5ItemsProvider<T> aItemsProvider )
      throws JRException {
    return jasperPrintDima( aContext, aModel, aItemsProvider );
  }

  /**
   * На основе описания модели и провайдера данных создает объект для экспорта в Excel из JasperReport
   *
   * @param <T> тип данных
   * @param aContext контекст
   * @param aModel модель данных
   * @param aItemsProvider поставщик данных
   * @return объект для печати
   * @throws JRException исключение при создании печатной формы
   */
  public static <T> JasperPrint jasperPrint4Xls( ITsGuiContext aContext, IM5Model<T> aModel,
      IM5ItemsProvider<T> aItemsProvider )
      throws JRException {
    return jasperPrint( aContext, aModel, aItemsProvider, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, 0, true );
  }

  /**
   * Создает объект готовый для печати/экспорта из Jasper
   *
   * @param <T>
   * @param aContext контекст
   * @param aModel модель данных
   * @param aItemsProvider поставщик данных
   * @param aNumberColumn если true, вставляет первую колонку № (номер строки)
   * @param aIgnorePagination если true, то не разделяет отчет на страницы
   * @param aColumnHeaderVertical если true, то загловки колонок пишгутся вертикально
   * @param aFirstColWeigth вес первой ширины колонки с данными, например, 0.1 означает 10% от всей доступной ширины.
   *          Значение 0 (ноль) означает что ширина первой колонки равна ширине остальных
   * @return объект для печати
   * @throws JRException исключение при создании печатной формы
   */
  private static <T> JasperPrint jasperPrint( ITsGuiContext aContext, IM5Model<T> aModel,
      IM5ItemsProvider<T> aItemsProvider, Boolean aNumberColumn, Boolean aIgnorePagination,
      Boolean aColumnHeaderVertical, float aFirstColWeigth, boolean aForExcelExport )
      throws JRException {
    JasperPrint retVal = null;
    // Генерируем дизайн отчета по переданному шаблону
    String reportTitle = IJasperReportConstants.REPORT_TITLE_M5_ID.getValue( aContext.params() ).asString();

    // IStringMap<EAtomicType> paramsAtomicTypes = getAtomicTypes( aReportTemplate, aReportAnswer );

    JasperDesign reportDesign = reportDesign( aContext, aModel, aNumberColumn.booleanValue(),
        aColumnHeaderVertical.booleanValue(), aFirstColWeigth, aForExcelExport );
    // Параметры отчета
    Map<String, Object> reportParameters = new HashMap<>();
    // Запрещаем разбиние на страницы
    reportParameters.put( JRParameter.IS_IGNORE_PAGINATION, aIgnorePagination );
    // Дата распечатки
    reportParameters.put( GENERATION_DATE, timestampFormat.format( new Date() ) );
    reportParameters.put( REPORT_TITLE, reportTitle );

    reportParameters.put( LEFT_BOTTOM_STR,
        IJasperReportConstants.LEFT_BOTTOM_STR_M5_ID.getValue( aContext.params() ).asString() );
    reportParameters.put( RIGHT_BOTTOM_STR,
        IJasperReportConstants.RIGHT_BOTTOM_STR_M5_ID.getValue( aContext.params() ).asString() );

    // строки заголовка страницы
    IStringList pageHeaderStrs = aContext.params().getValobj( IJasperReportConstants.PAGE_HEADER_STRINGS );

    for( int s = 0; s < pageHeaderStrs.size(); s++ ) {
      reportParameters.put( "page_header_" + s, pageHeaderStrs.get( s ) );
    }

    // reportParameters.put( DATE_FROM,
    // timestampFormat.format( Long.valueOf( aReportAnswer.reportQuestion().interval().startTime() ) ) );
    // reportParameters.put( DATE_TO,
    // timestampFormat.format( Long.valueOf( aReportAnswer.reportQuestion().interval().endTime() ) ) );
    // Название колонок (парамеров отчета)
    // Получаем список id колонок и названий
    for( IM5FieldDef<T, ?> fieldDef : aModel.fieldDefs() ) {
      reportParameters.put( DATA_COLUMN_ + fieldDef.id(), fieldDef.nmName() );
    }

    // Определим текущего пользователя
    // String user = aUserName;
    // reportParameters.put( ReportDesignGenerator.PERFORMER_NAME, user );
    // if( hasSummary ) {
    // // Генерим дизайн подотчета summary
    // JasperReport subreport = JasperCompileManager
    // .compileReport( ReportDesignGenerator.getSubreportDesign( aReportTemplate, paramsAtomicTypes ) );
    // String subreportJasperReportName = ReportDesignGenerator.SUBREPORT_JR;
    // reportParameters.put( subreportJasperReportName, subreport );
    // String subreportDataSetName = ReportDesignGenerator.SUBREPORT_DS;
    //
    // reportParameters.put( subreportDataSetName, summaryDataSource( aReportAnswer, aReportTemplate ) );
    // }
    Collection<JRValidationFault> faults = JasperCompileManager.verifyDesign( reportDesign );
    for( JRValidationFault fault : faults ) {
      System.err.println( fault );
    }
    JasperReport jasperReport = JasperCompileManager.compileReport( reportDesign );
    // detailDataSource4MO_MP_T( aReportAnswer, aReportTemplate, aMonitor, aConnection );
    retVal = JasperFillManager.fillReport( jasperReport, reportParameters, detailDataSource( aModel, aItemsProvider ) );

    setJasperProps( retVal );
    return retVal;
  }

  /**
   * Создает объект готовый для печати/экспорта из Jasper
   *
   * @param <T>
   * @param aContext контекст
   * @param aModel модель данных
   * @param aItemsProvider поставщик данных
   * @return объект для печати
   * @throws JRException исключение при создании печатной формы
   */
  private static <T> JasperPrint jasperPrintDima( ITsGuiContext aContext, IM5Model<T> aModel,
      IM5ItemsProvider<T> aItemsProvider )
      throws JRException {
    JasperPrint retVal = null;
    // Генерируем дизайн отчета по переданному шаблону
    String reportTitle = IJasperReportConstants.REPORT_TITLE_M5_ID.getValue( aContext.params() ).asString();

    JasperDesign reportDesign = reportDesignDima( aContext, aModel );
    // Параметры отчета
    Map<String, Object> reportParameters = new HashMap<>();
    // Запрещаем разбиние на страницы
    reportParameters.put( JRParameter.IS_IGNORE_PAGINATION, false );
    // Дата распечатки
    reportParameters.put( GENERATION_DATE, timestampFormat.format( new Date() ) );
    reportParameters.put( REPORT_TITLE, reportTitle );

    reportParameters.put( LEFT_BOTTOM_STR,
        IJasperReportConstants.LEFT_BOTTOM_STR_M5_ID.getValue( aContext.params() ).asString() );
    reportParameters.put( RIGHT_BOTTOM_STR,
        IJasperReportConstants.RIGHT_BOTTOM_STR_M5_ID.getValue( aContext.params() ).asString() );

    // Название колонок (парамеров отчета)
    // Получаем список id колонок и названий
    for( IM5FieldDef<T, ?> fieldDef : aModel.fieldDefs() ) {
      reportParameters.put( DATA_COLUMN_ + fieldDef.id(), fieldDef.nmName() );
    }

    Collection<JRValidationFault> faults = JasperCompileManager.verifyDesign( reportDesign );
    for( JRValidationFault fault : faults ) {
      System.err.println( fault );
    }
    JasperReport jasperReport = JasperCompileManager.compileReport( reportDesign );
    retVal = JasperFillManager.fillReport( jasperReport, reportParameters, detailDataSource( aModel, aItemsProvider ) );

    setJasperProps( retVal );
    return retVal;
  }

  private static void setJasperProps( JasperPrint retVal ) {
    // Remove the pageHeader from pages except starting page
    retVal.setProperty( "net.sf.jasperreports.export.xlsx.exclude.origin.keep.first.band.1", "pageHeader" );
    retVal.setProperty( "net.sf.jasperreports.export.xls.exclude.origin.keep.first.band.1", "pageHeader" );

    // Remove the column headers except the first one
    retVal.setProperty( "net.sf.jasperreports.export.xlsx.exclude.origin.keep.first.band.2", "columnHeader" );
    retVal.setProperty( "net.sf.jasperreports.export.xls.exclude.origin.keep.first.band.2", "columnHeader" );

    retVal.setProperty( "net.sf.jasperreports.export.xlsx.exclude.origin.band.3", "pageFooter" );
    retVal.setProperty( "net.sf.jasperreports.export.xls.exclude.origin.band.3", "pageFooter" );

    retVal.setProperty( "net.sf.jasperreports.export.xlsx.remove.empty.space.between.rows", "true" );// для единообразия
                                                                                                     // (на случай если
                                                                                                     // в обновлённой
                                                                                                     // библиотеке что
                                                                                                     // то изменится
    retVal.setProperty( "net.sf.jasperreports.export.xls.remove.empty.space.between.rows", "true" );// вот эта строка
                                                                                                    // обязательна -
                                                                                                    // именно "xls"
                                                                                                    // (xlsx - не
                                                                                                    // работает)
  }

  private static <T> JasperDesign reportDesign( ITsGuiContext aContext, IM5Model<T> aModel, boolean hasNumberColumn,
      boolean aColumHeaderVertical, float aFirstColWeigth, boolean aForExcelExport )
      throws JRException {

    // Сразу запомним кол-во параметров
    int paramQtty = aModel.fieldDefs().size();
    // Настройки отчета в целом
    JasperDesign jasperDesign = new JasperDesign();
    jasperDesign.setPrintOrder( PrintOrderEnum.VERTICAL );
    boolean isLandscape = true;
    jasperDesign.setOrientation( !isLandscape ? OrientationEnum.PORTRAIT : OrientationEnum.LANDSCAPE );

    jasperDesign.setName( "M5ModelDynamicDesignReport" ); //$NON-NLS-1$
    if( !isLandscape ) {
      jasperDesign.setPageWidth( PORTRAIT_PAGE_WIDTH );
      jasperDesign.setPageHeight( LANDSCAPE_PAGE_WIDTH );
    }
    else {
      jasperDesign.setPageWidth( LANDSCAPE_PAGE_WIDTH );
      jasperDesign.setPageHeight( PORTRAIT_PAGE_WIDTH );
    }
    jasperDesign.setColumnWidth( jasperDesign.getPageWidth() - 2 * HORIZONTAL_PAGE_MARGIN );
    jasperDesign.setColumnSpacing( 0 );
    jasperDesign.setLeftMargin( HORIZONTAL_PAGE_MARGIN );
    jasperDesign.setRightMargin( HORIZONTAL_PAGE_MARGIN );
    jasperDesign.setTopMargin( VERTICAL_PAGE_MARGIN );
    jasperDesign.setBottomMargin( VERTICAL_PAGE_MARGIN );

    JRDesignStyle titleStyle = getTitleStyle();
    jasperDesign.addStyle( titleStyle );

    JRDesignStyle headerStyle = getHeaderStyle();
    jasperDesign.addStyle( headerStyle );

    JRDesignStyle normalStyle = getNormalStyle();
    jasperDesign.addStyle( normalStyle );

    JRDesignStyle italicStyle = getItalicStyle();
    jasperDesign.addStyle( italicStyle );

    // Parameters
    // Дата генерации отчета
    JRDesignParameter parameter = new JRDesignParameter();
    parameter.setName( GENERATION_DATE );
    parameter.setValueClass( java.lang.String.class );
    jasperDesign.addParameter( parameter );

    // Заголовок отчета
    parameter = new JRDesignParameter();
    parameter.setName( REPORT_TITLE );
    parameter.setValueClass( java.lang.String.class );
    jasperDesign.addParameter( parameter );

    // левая нижняя надпись
    parameter = new JRDesignParameter();
    parameter.setName( LEFT_BOTTOM_STR );
    parameter.setValueClass( java.lang.String.class );
    jasperDesign.addParameter( parameter );

    // правая нижняя надпись
    parameter = new JRDesignParameter();
    parameter.setName( RIGHT_BOTTOM_STR );
    parameter.setValueClass( java.lang.String.class );
    jasperDesign.addParameter( parameter );

    // TODO
    // не нужно для отчета МП-МО-ТВ
    parameter = new JRDesignParameter();
    parameter.setName( DATE_FROM );
    parameter.setValueClass( java.lang.String.class );
    jasperDesign.addParameter( parameter );

    parameter = new JRDesignParameter();
    parameter.setName( DATE_TO );
    parameter.setValueClass( java.lang.String.class );
    jasperDesign.addParameter( parameter );

    // Фамилия исполнителя
    parameter = new JRDesignParameter();
    parameter.setName( PERFORMER_NAME );
    parameter.setValueClass( java.lang.String.class );
    jasperDesign.addParameter( parameter );

    // Создаем динамические названия колонок
    for( IM5FieldDef<T, ?> fieldDef : aModel.fieldDefs() ) {
      parameter = new JRDesignParameter();
      parameter.setName( DATA_COLUMN_ + fieldDef.id() );
      // TODO: mvk
      // parameter.setValueClass( java.lang.String.class );
      parameter.setValueClass( Object.class );
      jasperDesign.addParameter( parameter );
    }

    // fields
    // Метка времени
    JRDesignField field = new JRDesignField();
    // TODO
    // для отчета МП-МО-ТВ здесь нужно OBJ_NAME (название объекта)
    // field.setName( OBJ_NAME );
    field.setName( TIMESTAMP );
    // TODO: mvk
    // field.setValueClass( java.lang.String.class );
    field.setValueClass( Object.class );
    jasperDesign.addField( field );

    // Создаем динамические поля колонок
    for( IM5FieldDef<T, ?> fieldDef : aModel.fieldDefs() ) {
      field = new JRDesignField();
      field.setName( DATA_COLUMN_ + fieldDef.id() );
      // TODO: mvk
      // field.setValueClass( java.lang.String.class );
      field.setValueClass( Object.class );
      jasperDesign.addField( field );
    }

    JRDesignStaticText staticText;
    JRDesignTextField textField = new JRDesignTextField();
    JRDesignExpression expression;

    // int tempHeight = 0;
    // textField = new JRDesignTextField();
    // textField.setX( 0 );
    // textField.setY( tempHeight );
    // textField.setWidth( jasperDesign.getColumnWidth() );
    // textField.setHeight( 16 );
    // textField.setStyle( italicStyle );
    // textField.getLineBox().getPen().setLineWidth( 0 );
    // textField.setHorizontalTextAlign( HorizontalTextAlignEnum.LEFT );
    // textField.setVerticalTextAlign( VerticalTextAlignEnum.MIDDLE );

    // Заголовок отчета.
    JRDesignBand band = new JRDesignBand();

    int sizeOfTitleStr =
        IJasperReportConstants.REPORT_TITLE_M5_ID.getValue( aContext.params() ).asString().trim().length();

    band.setHeight( sizeOfTitleStr > 0 ? 25 : 0 );// REPORT_TITLE_HEIGHT / 2 );

    if( sizeOfTitleStr > 0 ) {
      textField = new JRDesignTextField();
      textField.setX( 0 );
      textField.setY( 0 );
      textField.setWidth( jasperDesign.getColumnWidth() );
      textField.setHeight( 25 );
      textField.setStyle( titleStyle );
      textField.getLineBox().getPen().setLineWidth( 0 );
      textField.setHorizontalTextAlign( HorizontalTextAlignEnum.CENTER );
      textField.setVerticalTextAlign( VerticalTextAlignEnum.MIDDLE );
      textField.setStretchWithOverflow( false );
      expression = new JRDesignExpression();
      expression.setText( "$P{" + REPORT_TITLE + "}" ); //$NON-NLS-1$ //$NON-NLS-2$
      textField.setExpression( expression );
      band.addElement( textField );
    }

    // JRDesignTextField titleTextField = textField;

    jasperDesign.setTitle( band );

    // Заголовок страницы
    // Page header
    // Оставляем пустым
    band = new JRDesignBand();
    // количество строк
    IStringList pageHeaderStrs = aContext.params().getValobj( IJasperReportConstants.PAGE_HEADER_STRINGS );
    int pageHeaderStrsCount = pageHeaderStrs.size();
    // IJasperReportConstants.PAGE_HEADER_STRINGS.getValue( aContext.params() ).size();
    int pageHeaderHeight = 16 * pageHeaderStrsCount;
    band.setHeight( pageHeaderHeight );

    for( int s = 0; s < pageHeaderStrsCount; s++ ) {
      // регистрация параметра
      parameter = new JRDesignParameter();
      parameter.setName( "page_header_" + s );
      parameter.setValueClass( java.lang.String.class );
      jasperDesign.addParameter( parameter );

      // регистрация поля
      textField = new JRDesignTextField();
      textField.setX( 0 );
      textField.setY( s * 16 );
      textField.setWidth( jasperDesign.getColumnWidth() );
      textField.setHeight( 16 );
      textField.setStyle( titleStyle );
      textField.getLineBox().getPen().setLineWidth( 0 );
      textField.setHorizontalTextAlign( HorizontalTextAlignEnum.CENTER );
      textField.setVerticalTextAlign( VerticalTextAlignEnum.MIDDLE );
      textField.setStretchWithOverflow( true );
      expression = new JRDesignExpression();
      expression.setText( "$P{page_header_" + s + "}" ); //$NON-NLS-1$ //$NON-NLS-2$
      textField.setExpression( expression );
      band.addElement( textField );
    }

    jasperDesign.setPageHeader( band );

    // Создаем дизайн динамической таблицы
    // Заголовок таблицы
    band = new JRDesignBand();
    band.setHeight( REPORT_HORIZONTAL_COLUMN_HEADER_HEIGHT );
    if( aColumHeaderVertical ) {
      band.setHeight( REPORT_VERTICAL_COLUMN_HEADER_HEIGHT );
    }
    // Растягиваем ячейку, чтобы уместить полное название
    // band.setSplitType( SplitTypeEnum.IMMEDIATE );
    band.setSplitType( SplitTypeEnum.STRETCH );

    // Создаем стандартную колонку отчета ("Время")
    staticText = new JRDesignStaticText();
    staticText.setX( 0 );
    staticText.setY( 0 );
    staticText.setWidth( TIMESTAMP_COLUMN_WIDTH );
    staticText.setHeight( REPORT_HORIZONTAL_COLUMN_HEADER_HEIGHT );
    if( aColumHeaderVertical ) {
      staticText.setHeight( REPORT_VERTICAL_COLUMN_HEADER_HEIGHT );
    }

    staticText.setHorizontalTextAlign( HorizontalTextAlignEnum.CENTER );
    staticText.setVerticalTextAlign( VerticalTextAlignEnum.MIDDLE );
    staticText.setStretchType( StretchTypeEnum.CONTAINER_HEIGHT );
    // TODO
    // в отчете типа MP_MO_T
    // staticText.setText( OBJECTS );
    // staticText.setText( "TIME" );
    staticText.setText( STR_NUMBER_SYMBOL );
    staticText.setStyle( headerStyle );

    staticText.setBackcolor( Color.GRAY );
    staticText.setForecolor( Color.WHITE );

    // Чтобы работал HTML
    // band.setSplitType( SplitTypeEnum.PREVENT );

    if( hasNumberColumn ) {
      band.addElement( staticText );
    }
    // Рассчет ширины динамических колонок
    int dynamicColumnWidth = (jasperDesign.getColumnWidth() - (hasNumberColumn ? TIMESTAMP_COLUMN_WIDTH : 0))
        / (paramQtty > 0 ? paramQtty : 1);
    // Dima, 12.08.19
    // Реализуем возможность регулировать длину первой колонки
    int firstColumnWidth = dynamicColumnWidth;
    if( aFirstColWeigth > 0 ) {
      firstColumnWidth = (int)(jasperDesign.getColumnWidth() * aFirstColWeigth);
      dynamicColumnWidth =
          (jasperDesign.getColumnWidth() - (hasNumberColumn ? TIMESTAMP_COLUMN_WIDTH : 0) - firstColumnWidth)
              / (paramQtty > 0 ? paramQtty - 1 : 1);
    }

    // titleTextField.setWidth( hasNumberColumn ? TIMESTAMP_COLUMN_WIDTH : firstColumnWidth );

    // Текст "Выбранные параметры"
    staticText = new JRDesignStaticText();
    staticText.setX( 0 );// TIMESTAMP_COLUMN_WIDTH );
    staticText.setY( 0 );
    staticText.setWidth( dynamicColumnWidth * paramQtty );
    staticText.setHeight( REPORT_HORIZONTAL_COLUMN_HEADER_HEIGHT / 2 );
    if( aColumHeaderVertical ) {
      staticText.setHeight( REPORT_VERTICAL_COLUMN_HEADER_HEIGHT / 2 );
    }
    staticText.setHorizontalTextAlign( HorizontalTextAlignEnum.CENTER );
    staticText.setVerticalTextAlign( VerticalTextAlignEnum.MIDDLE );
    staticText.setText( "SELECTED_PARAMS" );
    staticText.setStyle( headerStyle );
    // band.addElement( staticText );

    // JRElementGroup headerGroup = new JRDesignElementGroup();

    // Теперь динамические колонки
    int start_x = (hasNumberColumn ? TIMESTAMP_COLUMN_WIDTH : 0);
    for( IM5FieldDef<T, ?> fieldDef : aModel.fieldDefs() ) {
      textField = new JRDesignTextField();
      textField.setX( start_x );
      textField.setY( 0 );// REPORT_COLUMN_HEADER_HEIGHT / 2 );
      if( fieldDef.id().equals( aModel.fieldDefs().first().id() ) ) {
        textField.setWidth( firstColumnWidth );
      }
      else {
        textField.setWidth( dynamicColumnWidth );
      }
      textField.setHeight( REPORT_HORIZONTAL_COLUMN_HEADER_HEIGHT );
      if( aColumHeaderVertical ) {
        textField.setHeight( REPORT_VERTICAL_COLUMN_HEADER_HEIGHT );
      }
      textField.setHorizontalTextAlign( HorizontalTextAlignEnum.CENTER );
      textField.setVerticalTextAlign( VerticalTextAlignEnum.MIDDLE );
      textField.setStretchWithOverflow( true );
      textField.setStretchType( StretchTypeEnum.CONTAINER_BOTTOM );
      textField.setStyle( headerStyle );

      textField.setBackcolor( Color.GRAY );
      textField.setForecolor( Color.WHITE );

      expression = new JRDesignExpression();
      expression.setText( "$P{" + DATA_COLUMN_ + String.valueOf( fieldDef.id() ) + "}" );
      textField.setExpression( expression );
      if( aColumHeaderVertical && !fieldDef.id().equals( aModel.fieldDefs().first().id() ) ) {
        textField.setRotation( RotationEnum.LEFT );
      }
      // band.setSplitType( SplitTypeEnum.IMMEDIATE );
      band.addElement( textField );
      // old version
      // start_x += dynamicColumnWidth;
      // new version
      if( fieldDef.id().equals( aModel.fieldDefs().first().id() ) ) {
        start_x += firstColumnWidth;
      }
      else {
        start_x += dynamicColumnWidth;
      }

      // textField.setElementGroup( headerGroup );
    }

    // последнюю колонку добить до края
    int lastColumnWidth = jasperDesign.getColumnWidth() - (start_x - dynamicColumnWidth);
    textField.setWidth( lastColumnWidth );

    // Устанавливаем область заголовок таблицы
    jasperDesign.setColumnHeader( band );

    // Тело таблицы
    band = new JRDesignBand();
    band.setHeight( REPORT_DETAIL_HEIGHT );
    // band.setSplitType( SplitTypeEnum.IMMEDIATE );
    band.setSplitType( SplitTypeEnum.PREVENT );

    // Создаем стандартное поле отчета (timestamp)
    textField = new JRDesignTextField();
    textField.setX( 0 );
    textField.setY( 0 );
    textField.setWidth( TIMESTAMP_COLUMN_WIDTH );
    textField.setHeight( REPORT_DETAIL_HEIGHT );
    textField.setStyle( normalStyle );
    textField.setHorizontalTextAlign( HorizontalTextAlignEnum.CENTER );
    textField.setVerticalTextAlign( VerticalTextAlignEnum.MIDDLE );
    textField.setStretchWithOverflow( true );
    textField.setStretchType( StretchTypeEnum.CONTAINER_BOTTOM );
    expression = new JRDesignExpression();
    // TODO
    // в отчете типа MP_MO_T здесь название объекта
    // expression.setText( "$F{" + OBJ_NAME + "}" ); //$NON-NLS-1$ //$NON-NLS-2$
    // длинные имена переносим, а не отрезаем
    textField.setStretchWithOverflow( true );
    expression.setText( "$F{" + TIMESTAMP + "}" ); //$NON-NLS-1$ //$NON-NLS-2$
    textField.setExpression( expression );
    // textField.setPattern( timestampFormatString );

    if( hasNumberColumn ) {
      band.addElement( textField );
    }

    // HorizontalTextAlignEnum dataHorizontalTextAlign =
    // aContext.params().hasKey( IJasperReportConstants.REPORT_DATA_HORIZONTAL_TEXT_ALIGN_ID )
    // ? HorizontalTextAlignEnum
    // .getByName( aContext.params().getStr( IJasperReportConstants.REPORT_DATA_HORIZONTAL_TEXT_ALIGN_ID ) )
    // : HorizontalTextAlignEnum.CENTER;

    HorizontalTextAlignEnum dataHorizontalTextAlign = HorizontalTextAlignEnum.getByName(
        IJasperReportConstants.REPORT_DATA_HORIZONTAL_TEXT_ALIGN_ID.getValue( aContext.params() ).asString() );

    // Теперь динамические колонки
    start_x = (hasNumberColumn ? TIMESTAMP_COLUMN_WIDTH : 0);
    // TODO: mvk
    // IStringList detailColFormats = IReportParamsDefinitions.MAIN_COL_FORMATS.getValue( aReportTemplate.params() );
    for( int index = 0; index < paramQtty; index++ ) {
      IM5FieldDef<T, ?> fieldDef = aModel.fieldDefs().get( index );
      String paramId = fieldDef.id();
      // String formatter = detailColFormats.get( index );
      textField = new JRDesignTextField();
      textField.setX( start_x );
      textField.setY( 0 );
      if( fieldDef.id().equals( aModel.fieldDefs().first().id() ) ) {
        textField.setWidth( firstColumnWidth );
      }
      else {
        textField.setWidth( dynamicColumnWidth );
      }
      textField.setHeight( REPORT_DETAIL_HEIGHT );
      textField.setStyle( normalStyle );
      textField.setHorizontalTextAlign( dataHorizontalTextAlign );
      textField.setVerticalTextAlign( VerticalTextAlignEnum.MIDDLE );
      textField.setStretchWithOverflow( true );
      textField.setStretchType( StretchTypeEnum.CONTAINER_BOTTOM );

      // by Max - тип колонки
      // EAtomicType type = EAtomicType.STRING;// aParamsAtomicTypes.get( paramId );
      // String typeStr = convertToJavaClass( type ).getSimpleName();
      // TODO: mvk ???
      String rawValue = "$F{" + DATA_COLUMN_ + String.valueOf( paramId ) + "}";
      expression = new JRDesignExpression();
      // expression.setText( "(" + rawValue + "!= null )?(new " + typeStr + "(" + rawValue + ")):( null )" );
      expression.setText( "(" + rawValue + ")" );
      textField.setExpression( expression );
      // TODO: mvk ???
      // if( type == EAtomicType.FLOATING ) {
      // TODO - заплатка, пока на UI не будет убран формат для всех кроме FLOATING
      // textField.setPattern( formatter );
      // }
      band.addElement( textField );
      // old version
      // start_x += dynamicColumnWidth;
      // new version
      if( fieldDef.id().equals( aModel.fieldDefs().first().id() ) ) {
        start_x += firstColumnWidth;
      }
      else {
        start_x += dynamicColumnWidth;
      }
    }

    // последнюю колонку добить до края
    textField.setWidth( lastColumnWidth );

    // Устанавливаем область detail
    ((JRDesignSection)jasperDesign.getDetailSection()).addBand( band );

    // Page footer
    // Оставляем пустым
    band = new JRDesignBand();
    band.setHeight( SUMMARY_ROW_HEIGHT + 5 );
    textField = new JRDesignTextField();
    textField.setX( 0 );
    textField.setY( 5 );
    textField.setWidth( jasperDesign.getColumnWidth() );
    textField.setHeight( SUMMARY_ROW_HEIGHT );
    textField.setStyle( headerStyle );
    textField.getLineBox().getPen().setLineWidth( 0 );
    textField.setHorizontalTextAlign( HorizontalTextAlignEnum.CENTER );
    expression = new JRDesignExpression();
    expression.setText( "$V{PAGE_NUMBER}" );
    textField.setExpression( expression );
    if( !aForExcelExport ) {
      band.addElement( textField );
    }
    jasperDesign.setPageFooter( band );

    // Column footer
    band = new JRDesignBand();
    int offsetCounter = 3;
    band.setHeight( SUMMARY_ROW_HEIGHT + (2 * offsetCounter) );// REPORT_SUMMARY_HEIGHT );

    // Текст "Исполнитель"
    staticText = new JRDesignStaticText();
    staticText.setX( 10 );
    staticText.setY( offsetCounter );
    staticText.setWidth( PERFORMER_NAME_WIDTH );
    staticText.setHeight( SUMMARY_ROW_HEIGHT );
    staticText.setStyle( italicStyle );
    staticText.getLineBox().getPen().setLineWidth( 0 );
    staticText.setText( "PERFORMER" );
    staticText.getLineBox().getPen().setLineWidth( 0 );
    staticText.setHorizontalTextAlign( HorizontalTextAlignEnum.LEFT );
    band.setHeight( offsetCounter + REPORT_DETAIL_HEIGHT );
    // band.addElement( staticText );
    // ФИО исполнителя
    textField = new JRDesignTextField();
    textField.setX( start_x - jasperDesign.getColumnWidth() / 2 );
    textField.setY( offsetCounter );
    textField.setWidth( jasperDesign.getColumnWidth() / 2 );
    textField.setHeight( SUMMARY_ROW_HEIGHT );
    textField.setStyle( italicStyle );
    textField.getLineBox().getPen().setLineWidth( 0 );
    textField.setHorizontalTextAlign( HorizontalTextAlignEnum.RIGHT );
    expression = new JRDesignExpression();
    expression.setText( "\"( \" + String.valueOf($P{" + PERFORMER_NAME + "}) + \" ) \"" ); //$NON-NLS-1$ //$NON-NLS-2$
    textField.setExpression( expression );
    // band.addElement( textField );
    // Текст "Сгенерирован: "
    staticText = new JRDesignStaticText();
    staticText.setX( 10 );
    staticText.setY( offsetCounter + SUMMARY_ROW_HEIGHT );
    staticText.setWidth( PERFORMER_NAME_WIDTH );
    staticText.setHeight( SUMMARY_ROW_HEIGHT );
    staticText.setStyle( italicStyle );
    staticText.getLineBox().getPen().setLineWidth( 0 );
    staticText.setText( "REPORT_GENERATED" );
    staticText.setHorizontalTextAlign( HorizontalTextAlignEnum.LEFT );
    band.setHeight( 2 * offsetCounter + 2 * REPORT_DETAIL_HEIGHT );
    // band.addElement( staticText );
    // время генерации отчета
    textField = new JRDesignTextField();
    textField.setX( start_x - jasperDesign.getColumnWidth() / 2 );
    textField.setY( offsetCounter + SUMMARY_ROW_HEIGHT );
    textField.setWidth( jasperDesign.getColumnWidth() / 2 );
    textField.setHeight( SUMMARY_ROW_HEIGHT );
    textField.setStyle( italicStyle );
    textField.getLineBox().getPen().setLineWidth( 0 );
    textField.setHorizontalTextAlign( HorizontalTextAlignEnum.RIGHT );
    expression = new JRDesignExpression();
    expression.setText( "\"( \" + String.valueOf($P{" + GENERATION_DATE + "}) + \" ) \"" );
    textField.setExpression( expression );
    // band.addElement( textField );

    textField = new JRDesignTextField();
    textField.setX( 10 );
    textField.setY( offsetCounter );
    textField.setWidth( (jasperDesign.getColumnWidth() / 2) - 10 );
    textField.setHeight( SUMMARY_ROW_HEIGHT );
    textField.setStyle( headerStyle );
    textField.getLineBox().getPen().setLineWidth( 0 );
    textField.setHorizontalTextAlign( HorizontalTextAlignEnum.LEFT );
    expression = new JRDesignExpression();
    expression.setText( "$P{" + LEFT_BOTTOM_STR + "}" );
    textField.setExpression( expression );
    if( !aForExcelExport ) {
      band.addElement( textField );
    }

    textField = new JRDesignTextField();
    textField.setX( jasperDesign.getColumnWidth() / 2 );
    textField.setY( offsetCounter );
    textField.setWidth( (jasperDesign.getColumnWidth() / 2) - 10 );
    textField.setHeight( SUMMARY_ROW_HEIGHT );
    textField.setStyle( headerStyle );
    textField.getLineBox().getPen().setLineWidth( 0 );
    textField.setHorizontalTextAlign( HorizontalTextAlignEnum.RIGHT );
    expression = new JRDesignExpression();
    expression.setText( "$P{" + RIGHT_BOTTOM_STR + "}" );
    textField.setExpression( expression );
    if( !aForExcelExport ) {
      band.addElement( textField );
    }

    JRDesignLine line = new JRDesignLine();
    line.setX( 0 );
    line.setY( band.getHeight() - 1 );
    line.setWidth( start_x );
    line.setHeight( 0 );

    if( !aForExcelExport ) {
      band.addElement( line );
    }

    jasperDesign.setLastPageFooter( band );

    return jasperDesign;
  }

  private static <T> JRDataSource detailDataSource( IM5Model<T> aModel, IM5ItemsProvider<T> aItemsProvider ) {
    JRDataSource retValue = null;
    // IStringList paramIds = IReportParamsDefinitions.MAIN_COL_IDS.getValue( aReportTemplate.params() );

    List<Map<String, IAtomicValue>> mapList = new ArrayList<>();

    IList<T> items = aItemsProvider.listItems();

    int number = 1;
    for( T item : items ) {
      Map<String, IAtomicValue> rowValues = new HashMap<>();
      mapList.add( rowValues );
      for( IM5FieldDef<T, ?> fieldDef : aModel.fieldDefs() ) {
        // Object value = fieldDef.getValue( item );
        // if( fieldDef.valueClass() == IAtomicValue.class ) {
        // rowValues.put( DATA_COLUMN_ + fieldDef.id(), (IAtomicValue)value );
        // }
        // else
        {
          rowValues.put( DATA_COLUMN_ + fieldDef.id(), AvUtils.avStr( fieldDef.getFieldValue( item ).toString() ) );
        }
      }

      rowValues.put( TIMESTAMP, AvUtils.avInt( number ) );
      number++;
    }

    // Формируем dataset
    retValue = new ReportDetailDataSource( mapList );

    return retValue;
  }

  private static <T> IStringMap<EAtomicType> getAtomicTypes( IM5Model<T> aModel, IM5ItemsProvider<T> aItemsProvider ) {

    IStringMapEdit<EAtomicType> typesMap = new StringMap<>();

    IList<T> items = aItemsProvider.listItems();

    for( IM5FieldDef<T, ?> fieldDef : aModel.fieldDefs() ) {
      if( fieldDef.valueClass() != IAtomicValue.class ) {
        typesMap.put( DATA_COLUMN_ + fieldDef.id(), EAtomicType.NONE );
        continue;
      }

      EAtomicType currType = null;

      for( T item : items ) {
        Object value = fieldDef.getFieldValue( item );
        if( value != null ) {
          if( currType != null && currType != ((IAtomicValue)value).atomicType() ) {
            typesMap.put( DATA_COLUMN_ + fieldDef.id(), EAtomicType.NONE );
            break;
          }
          currType = ((IAtomicValue)value).atomicType();
        }
      }
      typesMap.put( DATA_COLUMN_ + fieldDef.id(), currType == null ? EAtomicType.NONE : currType );
    }

    return typesMap;
  }

  /**
   * @param <T>
   * @param aContext контекст
   * @param aModel описание модели данных
   * @return дизайн отчета
   * @throws JRException
   */
  private static <T> JasperDesign reportDesignDima( ITsGuiContext aContext, IM5Model<T> aModel )
      throws JRException {
    // Сразу запомним кол-во параметров
    int paramQtty = aModel.fieldDefs().size();
    // Настройки отчета в целом
    JasperDesign jasperDesign = new JasperDesign();
    jasperDesign.setPrintOrder( PrintOrderEnum.VERTICAL );
    IIntList integerColumnsWeigths = aContext.params().getValobj( IJasperReportConstants.COLUMNS_WEIGTHS );
    // В случае если массив пустой, то просто все колонки делаем одинаковой ширины
    IListEdit<Float> columnsWeigths = new ElemArrayList<>();

    if( integerColumnsWeigths.size() == 0 ) {
      for( int i = 0; i < paramQtty; i++ ) {
        columnsWeigths.add( Float.valueOf( 1.0f / paramQtty ) );
      }
    }
    else {
      // Проверяем что кол-во колонок равно кол-ву параметров
      TsIllegalArgumentRtException.checkFalse( integerColumnsWeigths.size() == paramQtty,
          "Size of column weigth array size must be equal parameters qtty" );

      int intTotalWeight = 0;
      for( int i = 0; i < paramQtty; i++ ) {
        intTotalWeight += integerColumnsWeigths.getValue( i );
      }

      for( int i = 0; i < paramQtty; i++ ) {
        columnsWeigths.add( Float.valueOf( (float)integerColumnsWeigths.getValue( i ) / (float)intTotalWeight ) );
      }

      float totalWeigth = 0;
      for( float colWeigth : columnsWeigths ) {
        totalWeigth += colWeigth;
      }
      TsIllegalArgumentRtException.checkFalse( (0.95 < totalWeigth) && (totalWeigth < 1.05),
          "Total column size weigth must be in 0.95 < totalWeigth < 1.05" );
    }
    // Настройки отчета в целом
    jasperDesign.setPrintOrder( PrintOrderEnum.VERTICAL );
    // Получаем настройки дизайна отчета
    boolean isLandscape = IJasperReportConstants.LANDSCAPE_ORIENTATION_M5_ID.getValue( aContext.params() ).asBool();
    jasperDesign.setOrientation( !isLandscape ? OrientationEnum.PORTRAIT : OrientationEnum.LANDSCAPE );
    boolean columnHeaderVertical =
        IJasperReportConstants.COLUM_HEADER_VERTICAL_M5_ID.getValue( aContext.params() ).asBool();
    boolean hasNumberColumn = IJasperReportConstants.HAS_NUMBER_COLUMN_M5_ID.getValue( aContext.params() ).asBool();

    jasperDesign.setName( "M5ModelDynamicDesignReport" ); //$NON-NLS-1$
    if( !isLandscape ) {
      jasperDesign.setPageWidth( PORTRAIT_PAGE_WIDTH );
      jasperDesign.setPageHeight( LANDSCAPE_PAGE_WIDTH );
    }
    else {
      jasperDesign.setPageWidth( LANDSCAPE_PAGE_WIDTH );
      jasperDesign.setPageHeight( PORTRAIT_PAGE_WIDTH );
    }
    jasperDesign.setColumnWidth( jasperDesign.getPageWidth() - 2 * HORIZONTAL_PAGE_MARGIN );
    jasperDesign.setColumnSpacing( 0 );
    jasperDesign.setLeftMargin( HORIZONTAL_PAGE_MARGIN );
    jasperDesign.setRightMargin( HORIZONTAL_PAGE_MARGIN );
    jasperDesign.setTopMargin( VERTICAL_PAGE_MARGIN );
    jasperDesign.setBottomMargin( VERTICAL_PAGE_MARGIN );

    JRDesignStyle titleStyle = getTitleStyle();
    jasperDesign.addStyle( titleStyle );

    JRDesignStyle headerStyle = getHeaderStyle();
    jasperDesign.addStyle( headerStyle );

    JRDesignStyle normalStyle = getNormalStyle();
    jasperDesign.addStyle( normalStyle );

    JRDesignStyle italicStyle = getItalicStyle();
    jasperDesign.addStyle( italicStyle );

    // Parameters
    // Дата генерации отчета
    JRDesignParameter parameter = new JRDesignParameter();
    parameter.setName( GENERATION_DATE );
    parameter.setValueClass( java.lang.String.class );
    jasperDesign.addParameter( parameter );

    // Заголовок отчета
    parameter = new JRDesignParameter();
    parameter.setName( REPORT_TITLE );
    parameter.setValueClass( java.lang.String.class );
    jasperDesign.addParameter( parameter );

    // левая нижняя надпись
    parameter = new JRDesignParameter();
    parameter.setName( LEFT_BOTTOM_STR );
    parameter.setValueClass( java.lang.String.class );
    jasperDesign.addParameter( parameter );

    // правая нижняя надпись
    parameter = new JRDesignParameter();
    parameter.setName( RIGHT_BOTTOM_STR );
    parameter.setValueClass( java.lang.String.class );
    jasperDesign.addParameter( parameter );

    // Создаем динамические названия колонок
    for( IM5FieldDef<T, ?> fieldDef : aModel.fieldDefs() ) {
      parameter = new JRDesignParameter();
      parameter.setName( DATA_COLUMN_ + fieldDef.id() );
      parameter.setValueClass( Object.class );
      jasperDesign.addParameter( parameter );
    }

    // fields
    // Метка времени
    JRDesignField field = new JRDesignField();
    field.setName( TIMESTAMP );
    field.setValueClass( Object.class );
    jasperDesign.addField( field );

    // Создаем динамические поля колонок
    for( IM5FieldDef<T, ?> fieldDef : aModel.fieldDefs() ) {
      field = new JRDesignField();
      field.setName( DATA_COLUMN_ + fieldDef.id() );
      field.setValueClass( Object.class );
      jasperDesign.addField( field );
    }

    // Заголовок отчета.
    JRDesignBand band = new JRDesignBand();
    band.setHeight( REPORT_TITLE_HEIGHT );
    JRDesignStaticText staticText;
    JRDesignTextField textField;

    int tempHeight = 0;
    textField = new JRDesignTextField();
    textField.setX( 0 );
    textField.setY( tempHeight );
    textField.setWidth( jasperDesign.getColumnWidth() );
    textField.setHeight( 16 );
    textField.setStyle( italicStyle );
    textField.getLineBox().getPen().setLineWidth( 0 );
    textField.setHorizontalTextAlign( HorizontalTextAlignEnum.LEFT );
    textField.setVerticalTextAlign( VerticalTextAlignEnum.MIDDLE );
    JRDesignExpression expression = new JRDesignExpression();

    // Описание отчета
    textField = new JRDesignTextField();
    textField.setX( 0 );
    textField.setY( tempHeight );
    textField.setWidth( jasperDesign.getColumnWidth() );
    textField.setHeight( 16 );
    textField.setStyle( titleStyle );
    textField.getLineBox().getPen().setLineWidth( 0 );
    textField.setHorizontalTextAlign( HorizontalTextAlignEnum.CENTER );
    textField.setVerticalTextAlign( VerticalTextAlignEnum.MIDDLE );
    textField.setStretchWithOverflow( true );
    expression = new JRDesignExpression();
    expression.setText( "$P{" + REPORT_TITLE + "}" ); //$NON-NLS-1$ //$NON-NLS-2$
    textField.setExpression( expression );
    band.addElement( textField );
    tempHeight += 16;

    band.setHeight( REPORT_TITLE_HEIGHT - 16 );

    jasperDesign.setTitle( band );

    // Заголовок страницы
    // Page header
    // Оставляем пустым
    band = new JRDesignBand();
    band.setHeight( PROTOCOL_PAGE_HEADER_HEIGHT );
    jasperDesign.setPageHeader( band );

    // Создаем дизайн динамической таблицы
    // Заголовок таблицы
    band = new JRDesignBand();
    band.setHeight( REPORT_HORIZONTAL_COLUMN_HEADER_HEIGHT );
    if( columnHeaderVertical ) {
      band.setHeight( REPORT_VERTICAL_COLUMN_HEADER_HEIGHT );
    }
    // Растягиваем ячейку, чтобы уместить полное название
    // band.setSplitType( SplitTypeEnum.IMMEDIATE );
    band.setSplitType( SplitTypeEnum.STRETCH );

    // Создаем стандартную колонку отчета ("Время")
    staticText = new JRDesignStaticText();
    staticText.setX( 0 );
    staticText.setY( 0 );
    staticText.setWidth( TIMESTAMP_COLUMN_WIDTH );
    staticText.setHeight( REPORT_HORIZONTAL_COLUMN_HEADER_HEIGHT );
    if( columnHeaderVertical ) {
      staticText.setHeight( REPORT_VERTICAL_COLUMN_HEADER_HEIGHT );
    }

    staticText.setHorizontalTextAlign( HorizontalTextAlignEnum.CENTER );
    staticText.setVerticalTextAlign( VerticalTextAlignEnum.MIDDLE );
    staticText.setStretchType( StretchTypeEnum.CONTAINER_HEIGHT );
    staticText.setText( STR_NUMBER_SYMBOL );
    staticText.setStyle( headerStyle );

    staticText.setBackcolor( Color.GRAY );
    staticText.setForecolor( Color.WHITE );

    // Чтобы работал HTML
    // band.setSplitType( SplitTypeEnum.PREVENT );

    if( hasNumberColumn ) {
      band.addElement( staticText );
    }
    // Рассчет ширины динамических колонок
    int defaultColumnWidth = (jasperDesign.getColumnWidth() - (hasNumberColumn ? TIMESTAMP_COLUMN_WIDTH : 0))
        / (paramQtty > 0 ? paramQtty : 1);
    // Dima, 12.08.19
    // Реализуем возможность регулировать длину колонок
    int[] colWidth = new int[paramQtty];
    Arrays.fill( colWidth, defaultColumnWidth );
    // Массив весов
    for( int i = 0; i < colWidth.length; i++ ) {
      colWidth[i] = (int)((jasperDesign.getColumnWidth() - (hasNumberColumn ? TIMESTAMP_COLUMN_WIDTH : 0))
          * columnsWeigths.get( i ));
    }

    // Теперь динамические колонки
    int start_x = (hasNumberColumn ? TIMESTAMP_COLUMN_WIDTH : 0);
    for( IM5FieldDef<T, ?> fieldDef : aModel.fieldDefs() ) {
      textField = new JRDesignTextField();
      textField.setX( start_x );
      textField.setY( 0 );// REPORT_COLUMN_HEADER_HEIGHT / 2 );
      int currWidth = colWidth[aModel.fieldDefs().indexOf( fieldDef )];
      textField.setWidth( currWidth );

      textField.setHeight( REPORT_HORIZONTAL_COLUMN_HEADER_HEIGHT );
      if( columnHeaderVertical ) {
        textField.setHeight( REPORT_VERTICAL_COLUMN_HEADER_HEIGHT );
      }
      textField.setHorizontalTextAlign( HorizontalTextAlignEnum.CENTER );
      textField.setVerticalTextAlign( VerticalTextAlignEnum.MIDDLE );
      textField.setStretchWithOverflow( true );
      textField.setStretchType( StretchTypeEnum.CONTAINER_BOTTOM );
      textField.setStyle( headerStyle );

      textField.setBackcolor( Color.GRAY );
      textField.setForecolor( Color.WHITE );

      expression = new JRDesignExpression();
      expression.setText( "$P{" + DATA_COLUMN_ + String.valueOf( fieldDef.id() ) + "}" );
      textField.setExpression( expression );
      if( columnHeaderVertical && !fieldDef.id().equals( aModel.fieldDefs().first().id() ) ) {
        textField.setRotation( RotationEnum.LEFT );
      }
      // band.setSplitType( SplitTypeEnum.IMMEDIATE );
      band.addElement( textField );
      start_x += currWidth;
    }

    // Устанавливаем область заголовок таблицы
    jasperDesign.setColumnHeader( band );

    // Тело таблицы
    band = new JRDesignBand();
    band.setHeight( REPORT_DETAIL_HEIGHT );
    band.setSplitType( SplitTypeEnum.IMMEDIATE );

    // Создаем стандартное поле отчета (timestamp)
    textField = new JRDesignTextField();
    textField.setX( 0 );
    textField.setY( 0 );
    textField.setWidth( TIMESTAMP_COLUMN_WIDTH );
    textField.setHeight( REPORT_DETAIL_HEIGHT );
    textField.setStyle( normalStyle );
    textField.setHorizontalTextAlign( HorizontalTextAlignEnum.CENTER );
    textField.setVerticalTextAlign( VerticalTextAlignEnum.MIDDLE );
    textField.setStretchWithOverflow( true );
    textField.setStretchType( StretchTypeEnum.CONTAINER_BOTTOM );
    expression = new JRDesignExpression();
    // длинные имена переносим, а не отрезаем
    textField.setStretchWithOverflow( true );
    expression.setText( "$F{" + TIMESTAMP + "}" ); //$NON-NLS-1$ //$NON-NLS-2$
    textField.setExpression( expression );
    // textField.setPattern( timestampFormatString );

    if( hasNumberColumn ) {
      band.addElement( textField );
    }

    // HorizontalTextAlignEnum dataHorizontalTextAlign =
    // aContext.params().hasKey( IJasperReportConstants.REPORT_DATA_HORIZONTAL_TEXT_ALIGN_ID )
    // ? HorizontalTextAlignEnum
    // .getByName( aContext.params().getStr( IJasperReportConstants.REPORT_DATA_HORIZONTAL_TEXT_ALIGN_ID ) )
    // : HorizontalTextAlignEnum.CENTER;

    HorizontalTextAlignEnum dataHorizontalTextAlign = HorizontalTextAlignEnum.getByName(
        IJasperReportConstants.REPORT_DATA_HORIZONTAL_TEXT_ALIGN_ID.getValue( aContext.params() ).asString() );

    // Теперь динамические колонки
    start_x = (hasNumberColumn ? TIMESTAMP_COLUMN_WIDTH : 0);
    // TODO: mvk
    // IStringList detailColFormats = IReportParamsDefinitions.MAIN_COL_FORMATS.getValue( aReportTemplate.params() );
    for( int index = 0; index < paramQtty; index++ ) {
      IM5FieldDef<T, ?> fieldDef = aModel.fieldDefs().get( index );
      String paramId = fieldDef.id();
      // String formatter = detailColFormats.get( index );
      textField = new JRDesignTextField();
      textField.setX( start_x );
      textField.setY( 0 );

      int currWidth = colWidth[aModel.fieldDefs().indexOf( fieldDef )];
      textField.setWidth( currWidth );

      textField.setHeight( REPORT_DETAIL_HEIGHT );
      textField.setStyle( normalStyle );
      textField.setHorizontalTextAlign( dataHorizontalTextAlign );
      textField.setVerticalTextAlign( VerticalTextAlignEnum.MIDDLE );
      textField.setStretchWithOverflow( true );
      textField.setStretchType( StretchTypeEnum.CONTAINER_BOTTOM );

      // by Max - тип колонки
      // EAtomicType type = EAtomicType.STRING;// aParamsAtomicTypes.get( paramId );
      // String typeStr = convertToJavaClass( type ).getSimpleName();
      // TODO: mvk ???
      String rawValue = "$F{" + DATA_COLUMN_ + String.valueOf( paramId ) + "}";
      expression = new JRDesignExpression();
      // expression.setText( "(" + rawValue + "!= null )?(new " + typeStr + "(" + rawValue + ")):( null )" );
      expression.setText( "(" + rawValue + ")" );
      textField.setExpression( expression );
      band.addElement( textField );
      start_x += currWidth;
    }

    // Устанавливаем область detail
    ((JRDesignSection)jasperDesign.getDetailSection()).addBand( band );

    // Page footer
    // Оставляем пустым
    band = new JRDesignBand();
    band.setHeight( SUMMARY_ROW_HEIGHT );
    textField = new JRDesignTextField();
    textField.setX( 10 );
    textField.setY( 0 );
    textField.setWidth( jasperDesign.getColumnWidth() - 20 );
    textField.setHeight( SUMMARY_ROW_HEIGHT );
    textField.setStyle( headerStyle );
    textField.getLineBox().getPen().setLineWidth( 0 );
    textField.setHorizontalTextAlign( HorizontalTextAlignEnum.CENTER );
    expression = new JRDesignExpression();
    expression.setText( "$V{PAGE_NUMBER}" );
    textField.setExpression( expression );
    band.addElement( textField );
    jasperDesign.setPageFooter( band );

    // Column footer
    band = new JRDesignBand();
    int offsetCounter = 3;
    band.setHeight( SUMMARY_ROW_HEIGHT + (2 * offsetCounter) );// REPORT_SUMMARY_HEIGHT );

    textField = new JRDesignTextField();
    textField.setX( 10 );
    textField.setY( offsetCounter );
    textField.setWidth( (jasperDesign.getColumnWidth() / 2) - 10 );
    textField.setHeight( SUMMARY_ROW_HEIGHT );
    textField.setStyle( headerStyle );
    textField.getLineBox().getPen().setLineWidth( 0 );
    textField.setHorizontalTextAlign( HorizontalTextAlignEnum.LEFT );
    expression = new JRDesignExpression();
    expression.setText( "$P{" + LEFT_BOTTOM_STR + "}" );
    textField.setExpression( expression );
    band.addElement( textField );

    textField = new JRDesignTextField();
    textField.setX( jasperDesign.getColumnWidth() / 2 );
    textField.setY( offsetCounter );
    textField.setWidth( (jasperDesign.getColumnWidth() / 2) - 10 );
    textField.setHeight( SUMMARY_ROW_HEIGHT );
    textField.setStyle( headerStyle );
    textField.getLineBox().getPen().setLineWidth( 0 );
    textField.setHorizontalTextAlign( HorizontalTextAlignEnum.RIGHT );
    expression = new JRDesignExpression();
    expression.setText( "$P{" + RIGHT_BOTTOM_STR + "}" );
    textField.setExpression( expression );
    band.addElement( textField );

    JRDesignLine line = new JRDesignLine();
    line.setX( 0 );
    line.setY( band.getHeight() - 1 );
    line.setWidth( start_x );
    line.setHeight( 0 );
    band.addElement( line );

    jasperDesign.setLastPageFooter( band );

    return jasperDesign;
  }

}
