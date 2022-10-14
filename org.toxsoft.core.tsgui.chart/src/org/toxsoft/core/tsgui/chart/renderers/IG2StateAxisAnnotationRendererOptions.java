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
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Свойства стандартного отрисовщика надписей на шкале.
 *
 * @author vs
 */
public interface IG2StateAxisAnnotationRendererOptions {

  /**
   * Название класса потребителя для {@link IG2Params#consumerName()}.
   */
  String CONSUMER_NAME = G2StateAxisAnnotationRenderer.class.getName();

  /**
   * Форматная строка для отображения значения.<br>
   * Тип данных: примитивный {@link EAtomicType#STRING}<br>
   * Формат: строка, формируемая по правилам для метода AvUtils.printAv<br>
   * Значение по умолчанию: TsLibUtils.EMPTY_STRING
   */
  IDataDef ANNOTATION_FORMAT = DataDef.create( "annotationFormat", STRING, // //$NON-NLS-1$
      TSID_NAME, STR_N_ANNOTATION_FORMAT, //
      TSID_DESCRIPTION, STR_D_ANNOTATION_FORMAT, //
      TSID_DEFAULT_VALUE, TsLibUtils.EMPTY_STRING //
  );
  // IAtomicOptionInfo ANNOTATION_FORMAT =
  // new AtomicOptionInfo( IG2StateAxisAnnotationRendererOptions.class.getSimpleName() + ".Format", //$NON-NLS-1$
  // STR_D_ANNOTATION_FORMAT, STR_N_ANNOTATION_FORMAT, EAtomicType.STRING,
  // DvUtils.avStr( StridUtils.EMPTY_STRING ), false );

  /**
   * Отступ подписи от засечки.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: число - отступ подписи от засечки<br>
   * Значение по умолчанию: 2
   */
  IDataDef ANNOTATION_INDENT = DataDef.create( "annotationIndent", INTEGER, // //$NON-NLS-1$
      TSID_NAME, STR_N_ANNOTATION_INDENT, //
      TSID_DESCRIPTION, STR_D_ANNOTATION_INDENT, //
      TSID_DEFAULT_VALUE, AvUtils.avInt( 2 ) //
  );
  // IAtomicOptionInfo ANNOTATION_INDENT =
  // new AtomicOptionInfo( IG2StateAxisAnnotationRendererOptions.class.getSimpleName() + ".AnnotationIndent",
  // //$NON-NLS-1$
  // STR_D_ANNOTATION_INDENT, STR_N_ANNOTATION_INDENT, EAtomicType.INTEGER, DvUtils.dvInt( 2 ), false );

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
  // new AtomicOptionInfo( IG2StateAxisAnnotationRendererOptions.class.getSimpleName() + "annotationTextColor",
  // //$NON-NLS-1$
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
  // IG2StateAxisAnnotationRendererOptions.class.getSimpleName() + ".FontInfo", //$NON-NLS-1$
  // STR_D_ANNOTATION_FONT_INFO, STR_N_ANNOTATION_FONT_INFO, IFontInfo.class, FontInfoKeeper.KEEPER, //
  // new FontInfo( "Arial", 10, false, false ), // //$NON-NLS-1$
  // false );

  /**
   * Названия тиков.<br>
   * Тип ссылки: {@link IStringList}<br>
   * Значение по умолчанию: {@link IStringList#EMPTY}
   */
  IDataDef TICK_NAMES = DataDef.create( "tickNames", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_TICK_NAMES, //
      TSID_DESCRIPTION, STR_D_TICK_NAMES, //
      TSID_KEEPER_ID, StringListKeeper.KEEPER_ID, //
      // OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( IStringList.EMPTY ) //
  );
  // IFimbedOptionInfo<IStringList> NAMES = new FimbedOptionInfo<>( //
  // IG2StateAxisAnnotationRendererOptions.class.getSimpleName() + ".Names", //$NON-NLS-1$
  // STR_D_ANNOTATION_FONT_INFO, STR_N_ANNOTATION_FONT_INFO, IStringList.class, StringListKeeper.KEEPER, //
  // IStringList.EMPTY, //
  // false );

}
