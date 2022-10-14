package org.toxsoft.core.tsgui.chart.renderers;

import static org.toxsoft.core.tsgui.chart.renderers.IG2Resources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.impl.*;
import org.toxsoft.core.tsgui.chart.legaсy.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Свойства стандартного отрисовщика холста для отображения графиков.
 *
 * @author vs
 */
public interface IStdG2CanvasRendererOptions {

  /**
   * Название класса потребителя для {@link IG2Params#consumerName()}.
   */
  String CONSUMER_NAME = StdG2CanvasRenderer.class.getName();

  /**
   * Цвет полей.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: число - первые 2 байта альфа канал, далее по два байта r,g и b составляющие цвета<br>
   * Значение по умолчанию: TsColorUtils.rgb2int( new RGB( 0x80, 0x80, 0x80 ))
   */
  IDataDef MARGINS_COLOR = DataDef.create( "marginsColor", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_MARGINS_COLOR, //
      TSID_DESCRIPTION, STR_D_MARGINS_COLOR, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( new RGBA( 0x80, 0x80, 0x80, 255 ) ) //
  );
  // IAtomicOptionInfo MARGINS_COLOR =
  // new AtomicOptionInfo( IStdG2CanvasRendererOptions.class.getSimpleName() + ".MarginsColor", //$NON-NLS-1$
  // STR_D_MARGINS_COLOR, STR_N_MARGINS_COLOR, EAtomicType.INTEGER,
  // DvUtils.dvInt( ITsColorManager.rgb2int( new RGB( 0x80, 0x80, 0x80 ) ) ), false );

  /**
   * Цвет фона.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: число - первые 2 байта альфа канал, далее по два байта r,g и b составляющие цвета<br>
   * Значение по умолчанию: TsColorUtils.rgb2int( new RGB( 0xff, 0xff, 0xff ))
   */
  IDataDef BACKGROUND_COLOR = DataDef.create( "marginsColor", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_BACKGROUND_COLOR, //
      TSID_DESCRIPTION, STR_D_BACKGROUND_COLOR, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( new RGBA( 0xff, 0xff, 0xff, 255 ) ) //
  );
  // IAtomicOptionInfo BACKGROUND_COLOR =
  // new AtomicOptionInfo( IStdG2CanvasRendererOptions.class.getSimpleName() + ".BackgoundColor", //$NON-NLS-1$
  // STR_D_BACKGROUND_COLOR, STR_N_BACKGROUND_COLOR, EAtomicType.INTEGER,
  // DvUtils.dvInt( ITsColorManager.rgb2int( new RGB( 0xff, 0xff, 0xff ) ) ), false );

  /**
   * Размеры полей.<br>
   * Тип ссылки: {@link Margins}<br>
   * Значение по умолчанию: {@link Margins#Margins(int, int, int, int) Margins(8,8,8,8)}
   */
  IDataDef MARGINS_INFO = DataDef.create( "marginsColor", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_MARGINS_INFO, //
      TSID_DESCRIPTION, STR_D_MARGINS_INFO, //
      TSID_KEEPER_ID, MarginsKeeper.KEEPER_ID, //
      // OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( new Margins( 8, 8, 8, 8 ) ) //
  );
  // IFimbedOptionInfo<Margins> MARGINS_INFO = new FimbedOptionInfo<>( //
  // IStdG2CanvasRendererOptions.class.getSimpleName() + ".Margins", //$NON-NLS-1$
  // STR_D_MARGINS_INFO, STR_N_MARGINS_INFO, Margins.class, MarginsKeeper.KEEPER, //
  // new Margins( 8, 8, 8, 8 ), //
  // false );

  // GOGA 2015-09-08 параметры должны задаваться IGrid-у
  // /**
  // * Имя класса - отрисовщика сетки.<br>
  // * Тип данных: примитивный {@link EAtomicType#STRING}<br>
  // * Формат: имя класса Значение по умолчанию: {@link StdG2GridRenderer}
  // */
  // IAtomicOptionInfo GRID_RENDERER_CLASS = new AtomicOptionInfo( IStdG2CanvasRendererOptions.class.getSimpleName()
  // + ".GridClass", //$NON-NLS-1$
  // STR_D_GRID_RENDERER_CLASS, STR_N_GRID_RENDERER_CLASS, EAtomicType.STRING, //
  // DvUtils.dvStr( StdG2GridRenderer.class.getName() ), false );
  //
  // /**
  // * Параметры отрисовщика сетки.<br>
  // * Тип ссылки: {@link IOptionSet}<br>
  // * Значение по умолчанию: {@link IOptionSet#NULL}
  // */
  // IFimbedOptionInfo<IOptionSet> GRID_RENDERER_OPS = new FimbedOptionInfo<>(
  // IStdG2CanvasRendererOptions.class.getSimpleName() + ".GridOps", //$NON-NLS-1$
  // STR_D_GRID_RENDERER_OPS, STR_N_GRID_RENDERER_OPS, IOptionSet.class, OptionSetKeeper.KEEPER, IOptionSet.NULL,
  // false );

}
