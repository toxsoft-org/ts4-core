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
public interface IJasperReportConstants {

  IDataDef REPORT_TITLE_M5_ID =
      create( "jasper.report.title.m5.id", STRING, TSID_NAME, "Jasper Report Title M5 Identifier", //
          TSID_DEFAULT_VALUE, AvUtils.avStr( TsLibUtils.EMPTY_STRING ) );

  IDataDef LEFT_BOTTOM_STR_M5_ID =
      create( "left.bottom.str.m5.id", STRING, TSID_NAME, "Left bottom string (Author) m5 identifier",
          TSID_DEFAULT_VALUE, AvUtils.avStr( TsLibUtils.EMPTY_STRING ), TSID_IS_MANDATORY, Boolean.FALSE );

  IDataDef RIGHT_BOTTOM_STR_M5_ID =
      create( "right.bottom.str.m5.id", STRING, TSID_NAME, "Right bottom string (Author) m5 identifier",
          TSID_DEFAULT_VALUE, AvUtils.avStr( TsLibUtils.EMPTY_STRING ), TSID_IS_MANDATORY, Boolean.FALSE );

  IDataDef REPORT_DATA_HORIZONTAL_TEXT_ALIGN_ID =
      create( "report.data.horizontal.text.align.id", STRING, TSID_NAME, "Text horizontal align id", TSID_DEFAULT_VALUE,
          AvUtils.avStr( HorizontalTextAlignEnum.CENTER.getName() ), TSID_IS_MANDATORY, Boolean.FALSE );
  // Dima, 14.08.19
  // Настройки дизайна отчета
  // ориентация листа
  IDataDef LANDSCAPE_ORIENTATION_M5_ID =
      create( "landscape.orientation.m5.id", BOOLEAN, TSID_NAME, "Landscape/Portrait orientaition m5 identifier",
          TSID_DEFAULT_VALUE, AvUtils.avBool( true ), TSID_IS_MANDATORY, Boolean.FALSE );

  // вертикальные названия в заголовке таблицы
  IDataDef COLUM_HEADER_VERTICAL_M5_ID =
      create( "colum.header.vertical.m5.id", BOOLEAN, TSID_NAME, "Column header vertical orientaition m5 identifier",
          TSID_DEFAULT_VALUE, AvUtils.avBool( false ), TSID_IS_MANDATORY, Boolean.FALSE );

  // показывать нулевую колонку с номерами строк
  IDataDef HAS_NUMBER_COLUMN_M5_ID =
      create( "has.number.column.m5.id", BOOLEAN, TSID_NAME, "Create zero column with numbers m5 identifier",
          TSID_DEFAULT_VALUE, AvUtils.avBool( true ), TSID_IS_MANDATORY, Boolean.FALSE );

  /**
   * Параметр - список содержащий значения весов колонок отчета.
   */
  IDataDef COL_WEIGTH = create( "column.weigth.m5.id", VALOBJ, TSID_NAME, "Array of column width weigths",
      TSID_DEFAULT_VALUE, avValobj( new IntArrayList() ), TSID_IS_MANDATORY, Boolean.FALSE );

  /**
   * Параметр - список содержащий значения строк верхнего колонтитула.
   */
  IDataDef PAGE_HEADER_STRINGS = create( "page.header.strings.id", VALOBJ, TSID_NAME, "Page header strings",
      TSID_DEFAULT_VALUE, avValobj( new StringArrayList() ), TSID_IS_MANDATORY, Boolean.FALSE );

}
