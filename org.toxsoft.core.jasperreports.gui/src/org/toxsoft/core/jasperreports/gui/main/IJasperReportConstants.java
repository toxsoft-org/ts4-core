package org.toxsoft.core.jasperreports.gui.main;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.impl.DataDef.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;

import net.sf.jasperreports.engine.type.*;

/**
 * Константы, использующиеся для формирования и отображения отчёта.
 *
 * @author max
 */
@SuppressWarnings( { "javadoc", "nls" } )
public interface IJasperReportConstants {

  String STR_N_PAR_PAGE_HEADER_STRINGS = "Page header strings";

  String PAR_ID_PAGE_HEADER_STRINGS = "page.header.strings.id";

  String STR_N_PAR_COLUMNS_WEIGTHS = "Array of column width weigths";

  String PAR_ID_COLUMNS_WEIGTHS = "column.weigth.m5.id";

  String STR_N_PAR_HAS_NUM_COLUMN = "Create zero column with numbers m5 identifier";

  String PAR_ID_HAS_NUM_COLUMN = "has.number.column.m5.id";

  String STR_N_PAR_COLUM_HEADER_VERTICAL = "Column header vertical orientaition m5 identifier";

  String PAR_ID_COLUM_HEADER_VERTICAL = "colum.header.vertical.m5.id";

  String STR_N_PAR_LANDSCAPE_ORIENTATION = "Landscape/Portrait orientaition m5 identifier";

  String PAR_ID_LANDSCAPE_ORIENTATION = "landscape.orientation.m5.id";

  String STR_N_PAR_DATA_HORIZONTAL_TEXT_ALIGN = "Text horizontal align id";

  String PAR_ID_DATA_HORIZONTAL_TEXT_ALIGN = "report.data.horizontal.text.align.id";

  String STR_N_PAR_RIGHT_BOTTOM_TEXT = "Right bottom string (Author) m5 identifier";

  String PAR_ID_RIGHT_BOTTOM_TEXT = "right.bottom.str.m5.id";

  String STR_N_PAR_LEFT_BOTTOM_TEXT = "Left bottom string (Author) m5 identifier";

  String PAR_ID_LEFT_BOTTOM_TEXT = "left.bottom.str.m5.id";

  String STR_N_PAR_REPORT_TITLE = "Jasper Report Title M5 Identifier";

  String PAR_ID_REPORT_TITLE = "jasper.report.title.m5.id";

  /**
   * Параметр - текст в заголовке
   */
  IDataDef REPORT_TITLE_M5_ID = create( PAR_ID_REPORT_TITLE, STRING, TSID_NAME, STR_N_PAR_REPORT_TITLE,
      TSID_DEFAULT_VALUE, AvUtils.avStr( TsLibUtils.EMPTY_STRING ) );

  /**
   * Параметр - текст в нижнем левом углу (подпись)
   */
  IDataDef LEFT_BOTTOM_STR_M5_ID = create( PAR_ID_LEFT_BOTTOM_TEXT, STRING, TSID_NAME, STR_N_PAR_LEFT_BOTTOM_TEXT,
      TSID_DEFAULT_VALUE, AvUtils.avStr( TsLibUtils.EMPTY_STRING ), TSID_IS_MANDATORY, Boolean.FALSE );

  /**
   * Параметр - текст в нижнем правом углу (подпись)
   */
  IDataDef RIGHT_BOTTOM_STR_M5_ID = create( PAR_ID_RIGHT_BOTTOM_TEXT, STRING, TSID_NAME, STR_N_PAR_RIGHT_BOTTOM_TEXT,
      TSID_DEFAULT_VALUE, AvUtils.avStr( TsLibUtils.EMPTY_STRING ), TSID_IS_MANDATORY, Boolean.FALSE );

  /**
   * Параметр - выравниевание :портретная ( false) или пейзажная (true)
   */
  IDataDef REPORT_DATA_HORIZONTAL_TEXT_ALIGN_ID = create( PAR_ID_DATA_HORIZONTAL_TEXT_ALIGN, STRING, TSID_NAME,
      STR_N_PAR_DATA_HORIZONTAL_TEXT_ALIGN, TSID_DEFAULT_VALUE,
      AvUtils.avStr( HorizontalTextAlignEnum.CENTER.getName() ), TSID_IS_MANDATORY, Boolean.FALSE );

  /**
   * Параметр - ориентация листа
   */
  IDataDef LANDSCAPE_ORIENTATION_M5_ID = create( PAR_ID_LANDSCAPE_ORIENTATION, BOOLEAN, TSID_NAME,
      STR_N_PAR_LANDSCAPE_ORIENTATION, TSID_DEFAULT_VALUE, AvUtils.avBool( true ), TSID_IS_MANDATORY, Boolean.FALSE );

  /**
   * Параметр, определяющий вертикальные названия в заголовке таблицы или горизонтальные.
   */
  IDataDef COLUM_HEADER_VERTICAL_M5_ID = create( PAR_ID_COLUM_HEADER_VERTICAL, BOOLEAN, TSID_NAME,
      STR_N_PAR_COLUM_HEADER_VERTICAL, TSID_DEFAULT_VALUE, AvUtils.avBool( false ), TSID_IS_MANDATORY, Boolean.FALSE );

  /**
   * Параметр, определяющий показывать нулевую колонку с номерами строк или нет.
   */
  IDataDef HAS_NUMBER_COLUMN_M5_ID = create( PAR_ID_HAS_NUM_COLUMN, BOOLEAN, TSID_NAME, STR_N_PAR_HAS_NUM_COLUMN,
      TSID_DEFAULT_VALUE, AvUtils.avBool( true ), TSID_IS_MANDATORY, Boolean.FALSE );

  /**
   * Параметр - список содержащий значения весов колонок отчета, желательно указывать веса в процентах, длина списка
   * может быть меньше количества столбцов (например, только ширину первого столбца) - тогда значения автоматически
   * интерпретируются как проценты, а оставшиеся столбцы равномерно заполняют оставленное для них пространство.
   */
  IDataDef COLUMNS_WEIGTHS = create( PAR_ID_COLUMNS_WEIGTHS, VALOBJ, TSID_NAME, STR_N_PAR_COLUMNS_WEIGTHS,
      TSID_DEFAULT_VALUE, avValobj( new IntArrayList() ), TSID_IS_MANDATORY, Boolean.FALSE );

  /**
   * Параметр - список содержащий значения строк верхнего колонтитула (Многострочный заголовок страницы (на каждой
   * странице)).
   */
  IDataDef PAGE_HEADER_STRINGS = create( PAR_ID_PAGE_HEADER_STRINGS, VALOBJ, TSID_NAME, STR_N_PAR_PAGE_HEADER_STRINGS,
      TSID_DEFAULT_VALUE, avValobj( new StringArrayList() ), TSID_IS_MANDATORY, Boolean.FALSE );

}
