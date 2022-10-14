package org.toxsoft.core.tsgui.chart.renderers;

import static org.toxsoft.core.tsgui.chart.renderers.IG2Resources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.impl.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Свойства стандартного отрисовщика надписей на шкале.
 *
 * @author vs
 */
public interface IStdG2TimeAxisAnnotationRendererOptions {

  /**
   * Название класса потребителя для {@link IG2Params#consumerName()}.
   */
  String CONSUMER_NAME = StdG2TimeAxisAnnotationRenderer.class.getName();

  /**
   * Форматная строка для отображения даты.<br>
   * Если строка пустая, то дата не отображается.<br>
   * Тип данных: примитивный {@link EAtomicType#STRING}<br>
   * Формат: строка, формируемая по правилам для метода DvUtils.printDv<br>
   * Значение по умолчанию: %1$td-%1$tm-%1$tY
   */
  IDataDef DATE_FORMAT = DataDef.create( "dateFormat", STRING, // //$NON-NLS-1$
      TSID_NAME, STR_N_DATE_FORMAT, //
      TSID_DESCRIPTION, STR_D_DATE_FORMAT, //
      TSID_DEFAULT_VALUE, AvUtils.avStr( "%1$td-%1$tm-%1$tY" ) // //$NON-NLS-1$
  );
  // IAtomicOptionInfo DATE_FORMAT =
  // new AtomicOptionInfo( IStdG2TimeAxisAnnotationRendererOptions.class.getSimpleName() + ".DateFormat", //$NON-NLS-1$
  // STR_D_DATE_FORMAT, STR_N_DATE_FORMAT, EAtomicType.STRING, DvUtils.avStr( "%1$td-%1$tm-%1$tY" ), false );
  // //$NON-NLS-1$

  /**
   * Форматная строка для отображения времени.<br>
   * Если строка пустая, то время не отображается.<br>
   * Тип данных: примитивный {@link EAtomicType#STRING}<br>
   * Формат: строка, формируемая по правилам для метода DvUtils.printDv<br>
   * Значение по умолчанию: %1$tH:%1$tM
   */
  IDataDef TIME_FORMAT = DataDef.create( "timeFormat", STRING, // //$NON-NLS-1$
      TSID_NAME, STR_N_TIME_FORMAT, //
      TSID_DESCRIPTION, STR_D_TIME_FORMAT, //
      TSID_DEFAULT_VALUE, AvUtils.avStr( "%1$tH:%1$tM" ) // //$NON-NLS-1$
  );
  // IAtomicOptionInfo TIME_FORMAT =
  // new AtomicOptionInfo( IStdG2TimeAxisAnnotationRendererOptions.class.getSimpleName() + ".TimeFormat", //$NON-NLS-1$
  // STR_D_TIME_FORMAT, STR_N_TIME_FORMAT, EAtomicType.STRING, DvUtils.avStr( "%1$tH:%1$tM" ), false ); //$NON-NLS-1$

  /**
   * Признак того, нужно ли переносить дату на другую строчку если отображается и дата и время.<br>
   * Тип данных: примитивный {@link EAtomicType#BOOLEAN}<br>
   * Формат: булевое значение - перенос даты на другу строчку<br>
   * Значение по умолчанию: true
   */
  IDataDef DATE_WRAP = DataDef.create( "dateWrap", BOOLEAN, // //$NON-NLS-1$
      TSID_NAME, STR_N_DATE_WRAP, //
      TSID_DESCRIPTION, STR_D_DATE_WRAP, //
      TSID_DEFAULT_VALUE, AvUtils.AV_TRUE //
  );
  // IAtomicOptionInfo DATE_WRAP =
  // new AtomicOptionInfo( IStdG2TimeAxisAnnotationRendererOptions.class.getSimpleName() + ".DateWrap", //$NON-NLS-1$
  // STR_D_DATE_WRAP, STR_N_DATE_WRAP, EAtomicType.BOOLEAN, DvUtils.dvBool( true ), false );

  /**
   * Отступ подписи от засечки.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: число - отступ подписи от засечки<br>
   * Значение по умолчанию: 4
   */
  IDataDef ANNOTATION_INDENT = DataDef.create( "annotationIndent", INTEGER, // //$NON-NLS-1$
      TSID_NAME, STR_N_ANNOTATION_INDENT, //
      TSID_DESCRIPTION, STR_D_ANNOTATION_INDENT, //
      TSID_DEFAULT_VALUE, AvUtils.avInt( 4 ) //
  );
  // IAtomicOptionInfo ANNOTATION_INDENT =
  // new AtomicOptionInfo( IStdG2TimeAxisAnnotationRendererOptions.class.getSimpleName() + ".AnnotationIndent",
  // //$NON-NLS-1$
  // STR_D_ANNOTATION_INDENT, STR_N_ANNOTATION_INDENT, EAtomicType.INTEGER, DvUtils.dvInt( 4 ), false );

  /**
   * Цвет подписей.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: число - первые 2 байта альфа канал, далее по два байта r,g и b составляющие цвета<br>
   * Значение по умолчанию: TsColorUtils.rgb2int( new RGB( 0,0, 0 ))
   */
  IDataDef TEXT_COLOR = DataDef.create( "annotationTextColor", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_TEXT_COLOR, //
      TSID_DESCRIPTION, STR_D_TEXT_COLOR, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( new RGBA( 0, 0, 0, 255 ) ) //
  );
  // IAtomicOptionInfo TEXT_COLOR =
  // new AtomicOptionInfo( IStdG2TimeAxisAnnotationRendererOptions.class.getSimpleName() + ".TextColor", //$NON-NLS-1$
  // STR_D_TEXT_COLOR, STR_N_TEXT_COLOR, EAtomicType.INTEGER,
  // DvUtils.dvInt( ITsColorManager.rgb2int( new RGB( 0, 0, 0 ) ) ), false );

  /**
   * Параметры шрифта.<br>
   * Тип ссылки: {@link IFontInfo}<br>
   * Значение по умолчанию: {@link FontInfo#FontInfo(String, int, boolean, boolean) FontInfo("Arial", 10, false,false)}
   */
  IDataDef FONT_INFO = DataDef.create( "annotationFontInfo", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_ANNOTATION_FONT_INFO, //
      TSID_DESCRIPTION, STR_D_ANNOTATION_FONT_INFO, //
      TSID_KEEPER_ID, FontInfo.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( new FontInfo( "Arial", 10, 0 ) ) // //$NON-NLS-1$
  );
  // IFimbedOptionInfo<IFontInfo> FONT_INFO = new FimbedOptionInfo<>( //
  // IStdG2TimeAxisAnnotationRendererOptions.class.getSimpleName() + ".FontInfo", //$NON-NLS-1$
  // STR_D_ANNOTATION_FONT_INFO, STR_N_ANNOTATION_FONT_INFO, IFontInfo.class, FontInfoKeeper.KEEPER, //
  // new FontInfo( "Arial", 10, false, false ), // //$NON-NLS-1$
  // false );

}
