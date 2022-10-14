package org.toxsoft.core.tsgui.chart.renderers;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static ru.toxsoft.tsgui.chart.renderers.IG2Resources.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.utils.*;

import ru.toxsoft.tsgui.chart.api.*;
import ru.toxsoft.tsgui.chart.impl.*;
import ru.toxsoft.tsgui.chart.legaсy.*;

/**
 * Свойства стандартного отрисовщика надписей на шкале.
 *
 * @author vs
 */
public interface IStdG2AxisAnnotationRendererOptions {

  /**
   * Название класса потребителя для {@link IG2Params#consumerName()}.
   */
  String CONSUMER_NAME = StdG2AxisAnnotationRenderer.class.getName();

  /**
   * Форматная строка для отображения значения.<br>
   * Тип данных: примитивный {@link EAtomicType#STRING}<br>
   * Формат: строка, формируемая по правилам для метода DvUtils.printDv<br>
   * Значение по умолчанию: StridUtils.EMPTY_STRING
   */
  IDataDef ANNOTATION_FORMAT = DataDef.create( "annotationFormat", STRING, // //$NON-NLS-1$
      TSID_NAME, STR_N_ANNOTATION_FORMAT, //
      TSID_DESCRIPTION, STR_D_ANNOTATION_FORMAT, //
      TSID_DEFAULT_VALUE, TsLibUtils.EMPTY_STRING //
  );
  // IAtomicOptionInfo ANNOTATION_FORMAT =
  // new AtomicOptionInfo( IStdG2AxisAnnotationRendererOptions.class.getSimpleName() + ".Format", //$NON-NLS-1$
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
  // new AtomicOptionInfo( IStdG2AxisAnnotationRendererOptions.class.getSimpleName() + ".AnnotationIndent",
  // // $NON-NLS-1$
  // STR_D_ANNOTATION_INDENT, STR_N_ANNOTATION_INDENT, EAtomicType.INTEGER, AvUtils.avInt( 2 ), false );

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
  // new AtomicOptionInfo( IStdG2AxisAnnotationRendererOptions.class.getSimpleName() + ".TextColor", //$NON-NLS-1$
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
  // IStdG2AxisAnnotationRendererOptions.class.getSimpleName() + ".FontInfo", //$NON-NLS-1$
  // STR_D_ANNOTATION_FONT_INFO, STR_N_ANNOTATION_FONT_INFO, IFontInfo.class, FontInfoKeeper.KEEPER, //
  // new FontInfo( "Arial", 10, false, false ), // //$NON-NLS-1$
  // false );

  /**
   * Текст заголовка.<br>
   * Тип данных: примитивный {@link EAtomicType#STRING}<br>
   * Формат: строка - текст заголовка<br>
   * Значение по умолчанию: StridUtils.EMPTY_STRING
   */
  IDataDef TITLE = DataDef.create( "annotationTitle", STRING, // //$NON-NLS-1$
      TSID_NAME, STR_N_TITLE, //
      TSID_DESCRIPTION, STR_D_TITLE, //
      TSID_DEFAULT_VALUE, TsLibUtils.EMPTY_STRING //
  );
  // IAtomicOptionInfo TITLE = new AtomicOptionInfo( IStdG2AxisAnnotationRendererOptions.class.getSimpleName() +
  // ".Title", //$NON-NLS-1$
  // STR_D_TITLE, STR_N_TITLE, EAtomicType.STRING, DvUtils.dvStr( StridUtils.EMPTY_STRING ), false );

  /**
   * Цвет заголовка.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: число - первые 2 байта альфа канал, далее по два байта r,g и b составляющие цвета<br>
   * Значение по умолчанию: TsColorUtils.rgb2int( new RGB( 0,0,0 ))
   */
  IDataDef TITLE_COLOR = DataDef.create( "annotationTitleColor", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_TITLE_COLOR, //
      TSID_DESCRIPTION, STR_D_TITLE_COLOR, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( new RGBA( 0, 0, 0, 255 ) ) //
  );
  // IAtomicOptionInfo TITLE_COLOR =
  // new AtomicOptionInfo( IStdG2AxisAnnotationRendererOptions.class.getSimpleName() + ".TitleColor", //$NON-NLS-1$
  // STR_D_TITLE_COLOR, STR_N_TITLE_COLOR, EAtomicType.INTEGER,
  // DvUtils.dvInt( ITsColorManager.rgb2int( new RGB( 0, 0, 0 ) ) ), false );

  /**
   * Параметры шрифта заголовка.<br>
   * Тип ссылки: {@link IFontInfo}<br>
   * Значение по умолчанию: {@link FontInfo#FontInfo(String, int, boolean, boolean) FontInfo("Arial", 10, false,false)}
   */
  IDataDef TITLE_FONT_INFO = DataDef.create( "titleFontInfo", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_TITLE_FONT_INFO, //
      TSID_DESCRIPTION, STR_D_TITLE_FONT_INFO, //
      TSID_KEEPER_ID, FontInfo.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( new FontInfo( "Arial", 10, 0 ) ) // //$NON-NLS-1$
  );
  // IFimbedOptionInfo<IFontInfo> TITLE_FONT_INFO = new FimbedOptionInfo<>( //
  // IStdG2AxisAnnotationRendererOptions.class.getSimpleName() + ".TitleFontInfo", //$NON-NLS-1$
  // STR_D_TITLE_FONT_INFO, STR_N_TITLE_FONT_INFO, IFontInfo.class, FontInfoKeeper.KEEPER, //
  // new FontInfo( "Arial", 10, false, false ), // //$NON-NLS-1$
  // false );

  /**
   * Ориентация заголовка.<br>
   * Тип данных: примитивный {@link EAtomicType#STRING}<br>
   * Формат: строка - идентификатор элемента перечисления {@link ETsOrientation}<br>
   * Значение по умолчанию: ETsOrientation.VERTICAL.id())
   */
  IDataDef TITLE_ORIENTATION = DataDef.create( "titleOrientation", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_TITLE_ORIENTATION, //
      TSID_DESCRIPTION, STR_D_TITLE_ORIENTATION, //
      TSID_KEEPER_ID, ETsOrientation.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETsOrientation.VERTICAL ) //
  );
  // IAtomicOptionInfo TITLE_ORIENTATION =
  // new AtomicOptionInfo( IStdG2AxisAnnotationRendererOptions.class.getSimpleName() + ".TitleOrientation", //
  // $NON-NLS-1$
  // STR_D_TITLE_ORIENTATION, STR_N_TITLE_ORIENTATION, EAtomicType.STRING,
  // DvUtils.dvStr( ETsOrientation.HORIZONTAL.id() ), false );

  /**
   * Поля заголовка.<br>
   * Тип ссылки: {@link Margins}<br>
   * Значение по умолчанию: Margins( 2, 2, 2, 2 )
   */
  IDataDef TITLE_MARGINS = DataDef.create( "titleMargins", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_TITLE_MARGINS, //
      TSID_DESCRIPTION, STR_D_TITLE_MARGINS, //
      TSID_KEEPER_ID, MarginsKeeper.KEEPER_ID, //
      // OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( new Margins( 2, 2, 2, 2 ) ) //
  );
  // IFimbedOptionInfo<Margins> TITLE_MARGINS = new FimbedOptionInfo<>( //
  // IStdG2AxisMarkingRendererOptions.class.getSimpleName() + ".TitleMargins", //$NON-NLS-1$
  // STR_D_TITLE_MARGINS, STR_N_TITLE_MARGINS, Margins.class, MarginsKeeper.KEEPER, new Margins( 2, 2, 2, 2 ), false );

  /**
   * Выравнивание заголовка по вертикали.<br>
   * Тип данных: примитивный {@link EAtomicType#STRING}<br>
   * Формат: строка - идентификатор элемента перечисления {@link EVerAlignment}<br>
   * Значение по умолчанию: {@link EVerAlignment#CENTER})
   */
  IDataDef TITLE_VER_ALIGNMENT = DataDef.create( "titleVerAlign", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_TITLE_VER_ALIGNMENT, //
      TSID_DESCRIPTION, STR_D_TITLE_VER_ALIGNMENT, //
      TSID_KEEPER_ID, EVerAlignment.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EVerAlignment.CENTER ) //
  );
  // IAtomicOptionInfo TITLE_VER_ALIGNMENT =
  // new AtomicOptionInfo( IStdG2AxisAnnotationRendererOptions.class.getSimpleName() + ".TitleVerAlignment",
  // // $NON-NLS-1$
  // STR_D_TITLE_VER_ALIGNMENT, STR_N_TITLE_VER_ALIGNMENT, EAtomicType.STRING,
  // DvUtils.dvStr( EVerAlignment.CENTER.id() ), false );

  /**
   * Выравнивание заголовка по горизонтали.<br>
   * Тип данных: примитивный {@link EAtomicType#STRING}<br>
   * Формат: строка - идентификатор элемента перечисления {@link EHorAlignment}<br>
   * Значение по умолчанию: {@link EHorAlignment#CENTER})
   */
  IDataDef TITLE_HOR_ALIGNMENT = DataDef.create( "titleHorAlign", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_TITLE_HOR_ALIGNMENT, //
      TSID_DESCRIPTION, STR_D_TITLE_HOR_ALIGNMENT, //
      TSID_KEEPER_ID, EHorAlignment.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EHorAlignment.CENTER ) //
  );
  // IAtomicOptionInfo TITLE_HOR_ALIGNMENT =
  // new AtomicOptionInfo( IStdG2AxisAnnotationRendererOptions.class.getSimpleName() + ".TitleHorAlignment",
  // // $NON-NLS-1$
  // STR_D_TITLE_VER_ALIGNMENT, STR_N_TITLE_VER_ALIGNMENT, EAtomicType.STRING,
  // DvUtils.dvStr( EHorAlignment.CENTER.id() ), false );

}
