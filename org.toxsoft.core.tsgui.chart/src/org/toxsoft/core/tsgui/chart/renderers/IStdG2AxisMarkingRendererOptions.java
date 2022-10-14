package org.toxsoft.core.tsgui.chart.renderers;

import static org.toxsoft.core.tsgui.chart.renderers.IG2Resources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.impl.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Свойства стандартного отрисовщика шкалы.
 *
 * @author vs
 */
public interface IStdG2AxisMarkingRendererOptions {

  /**
   * Название класса потребителя для {@link IG2Params#consumerName()}.
   */
  String CONSUMER_NAME = StdG2AxisMarkingRenderer.class.getName();

  /**
   * Отступ засечек от края шкалы в пикселях.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: число - кол-во пикселей от границы шкалы<br>
   * Значение по умолчанию: 3
   */
  IDataDef INDENT_FROM_EDGE = DataDef.create( "markIndent", INTEGER, // //$NON-NLS-1$
      TSID_NAME, STR_N_INDENT, //
      TSID_DESCRIPTION, STR_D_INDENT, //
      TSID_DEFAULT_VALUE, AvUtils.avInt( 3 ) //
  );
  // IAtomicOptionInfo INDENT_FROM_EDGE =
  // new AtomicOptionInfo( IStdG2AxisMarkingRendererOptions.class.getSimpleName() + ".Indent", //$NON-NLS-1$
  // STR_D_INDENT, STR_N_INDENT, EAtomicType.INTEGER, DvUtils.avInt( 3 ), false );

  /**
   * Параметры рисования линии большой засечки.<br>
   * Тип ссылки: {@link TsLineInfo}<br>
   * Значение по умолчанию: {@link TsLineInfo#fromLineAttributes( new LineAttributes( 3 )}
   */
  @SuppressWarnings( "javadoc" )
  IDataDef BIG_TICK_LINE_INFO = DataDef.create( "bigTickLineInfo", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_BIG_TICK_LINE_INFO, //
      TSID_DESCRIPTION, STR_D_BIG_TICK_LINE_INFO, //
      TSID_KEEPER_ID, TsLineInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsLineInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsLineInfo.fromLineAttributes( new LineAttributes( 3 ) ) ) //
  );
  // IFimbedOptionInfo<ILineInfo> BIG_TICK_LINE_INFO = new FimbedOptionInfo<>( //
  // IStdG2AxisMarkingRendererOptions.class.getSimpleName() + ".BigTickLineInfo", //$NON-NLS-1$
  // STR_D_BIG_TICK_LINE_INFO, STR_N_BIG_TICK_LINE_INFO, ILineInfo.class, LineInfoKeeper.KEEPER, //
  // new LineInfo( 3, new RGB( 0, 0, 0 ), ELineType.SOLID ), //
  // false );

  /**
   * Параметры рисования линии средней засечки.<br>
   * Тип ссылки: {@link TsLineInfo}<br>
   * Значение по умолчанию: {@link TsLineInfo#fromLineAttributes( new LineAttributes( 2 )}
   */
  @SuppressWarnings( "javadoc" )
  IDataDef MID_TICK_LINE_INFO = DataDef.create( "midTickLineInfo", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_MID_TICK_LINE_INFO, //
      TSID_DESCRIPTION, STR_D_MID_TICK_LINE_INFO, //
      TSID_KEEPER_ID, TsLineInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsLineInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsLineInfo.fromLineAttributes( new LineAttributes( 2 ) ) ) //
  );
  // IFimbedOptionInfo<ILineInfo> MID_TICK_LINE_INFO = new FimbedOptionInfo<>( //
  // IStdG2AxisMarkingRendererOptions.class.getSimpleName() + ".MidTickLineInfo", //$NON-NLS-1$
  // STR_D_MID_TICK_LINE_INFO, STR_N_MID_TICK_LINE_INFO, ILineInfo.class, LineInfoKeeper.KEEPER, //
  // new LineInfo( 2, new RGB( 0, 0, 0 ), ELineType.SOLID ), //
  // false );

  /**
   * Параметры рисования линии маленькой засечки.<br>
   * Тип ссылки: {@link TsLineInfo}<br>
   * Значение по умолчанию: {@link TsLineInfo#fromLineAttributes( new LineAttributes( 1 )}
   */
  @SuppressWarnings( "javadoc" )
  IDataDef LIT_TICK_LINE_INFO = DataDef.create( "litTickLineInfo", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_LIT_TICK_LINE_INFO, //
      TSID_DESCRIPTION, STR_D_LIT_TICK_LINE_INFO, //
      TSID_KEEPER_ID, TsLineInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsLineInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsLineInfo.fromLineAttributes( new LineAttributes( 1 ) ) ) //
  );
  // IFimbedOptionInfo<ILineInfo> LIT_TICK_LINE_INFO = new FimbedOptionInfo<>( //
  // IStdG2AxisMarkingRendererOptions.class.getSimpleName() + ".LitTickLineInfo", //$NON-NLS-1$
  // STR_D_LIT_TICK_LINE_INFO, STR_N_LIT_TICK_LINE_INFO, ILineInfo.class, LineInfoKeeper.KEEPER, //
  // new LineInfo( 1, new RGB( 0, 0, 0 ), ELineType.SOLID ), //
  // false );

  /**
   * Property: big tick axis color.
   */
  IDataDef BIG_TICK_RGBA = DataDef.create( "bigTickRgba", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_BIG_TICK_COLOR, //
      TSID_DESCRIPTION, STR_D_BIG_TICK_COLOR, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, TSID_DEFAULT_VALUE,
      avValobj( new RGBA( 0, 0, 0, 255 ) ) //
  );

  /**
   * Property: mid tick axis color.
   */
  IDataDef MID_TICK_RGBA = DataDef.create( "midTickRgba", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_MID_TICK_COLOR, //
      TSID_DESCRIPTION, STR_D_MID_TICK_COLOR, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, TSID_DEFAULT_VALUE,
      avValobj( new RGBA( 0, 0, 0, 255 ) ) //
  );

  /**
   * Property: little tick axis color.
   */
  IDataDef LIT_TICK_RGBA = DataDef.create( "litTickRgba", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_LIT_TICK_COLOR, //
      TSID_DESCRIPTION, STR_D_LIT_TICK_COLOR, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, TSID_DEFAULT_VALUE,
      avValobj( new RGBA( 0, 0, 0, 255 ) ) //
  );

}
