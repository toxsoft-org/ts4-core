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
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Свойства сандартного отрисовщика координатной сетки.
 *
 * @author vs
 */
public interface IStdG2GridRendererOptions {

  /**
   * Название класса потребителя для {@link IG2Params#consumerName()}.
   */
  String CONSUMER_NAME = StdG2GridRenderer.class.getName();

  /**
   * Параметры рисования линии для большой засечки по горизонтальной шкале.<br>
   * Тип ссылки: {@link TsLineInfo}<br>
   * Значение по умолчанию: {@link TsLineInfo#fromLineAttributes(new LineAttributes(1)}
   */
  @SuppressWarnings( "javadoc" )
  IDataDef HOR_BIG_TICK_LINE_INFO = DataDef.create( "horBigTickLineInfo", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_BIG_TICK_LINE_INFO, //
      TSID_DESCRIPTION, STR_BIG_TICK_LINE_INFO_D, //
      TSID_KEEPER_ID, TsLineInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsLineInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsLineInfo.fromLineAttributes( new LineAttributes( 1 ) ) ) //
  );
  // IFimbedOptionInfo<ILineInfo> HOR_BIG_TICK_LINE_INFO = new FimbedOptionInfo<>( //
  // IStdG2GridRendererOptions.class.getSimpleName() + ".HorBigTickLineInfo", //$NON-NLS-1$
  // STR_D_HOR_BIG_TICK_LINE_INFO, STR_N_BIG_TICK_LINE_INFO, ILineInfo.class, LineInfoKeeper.KEEPER, //
  // new LineInfo( 1, new RGB( 0, 0, 0 ), ELineType.SOLID ), //
  // false );

  /**
   * Параметры рисования линии для средней засечки по горизонтальной шкале.<br>
   * Тип ссылки: {@link TsLineInfo}<br>
   * Значение по умолчанию: {@link TsLineInfo#LineInfo(int, RGB, ELineType) LineInfo(1, 0, 0, 0}, ELineType.DOTTED1)}
   */
  @SuppressWarnings( "javadoc" )
  IDataDef HOR_MID_TICK_LINE_INFO = DataDef.create( "horMidTickLineInfo", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_MID_TICK_LINE_INFO, //
      TSID_DESCRIPTION, STR_MID_TICK_LINE_INFO_D, //
      TSID_KEEPER_ID, TsLineInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsLineInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE,
      avValobj( new TsLineInfo( 1, ETsLineType.DOT, ETsLineCapStyle.FLAT, ETsLineJoinStyle.MITER, IIntList.EMPTY ) ) //
  );
  // IFimbedOptionInfo<ILineInfo> HOR_MID_TICK_LINE_INFO = new FimbedOptionInfo<>( //
  // IStdG2GridRendererOptions.class.getSimpleName() + ".VerMidTickLineInfo", //$NON-NLS-1$
  // STR_D_HOR_MID_TICK_LINE_INFO, STR_N_MID_TICK_LINE_INFO, ILineInfo.class, LineInfoKeeper.KEEPER, //
  // new LineInfo( 1, new RGB( 0, 0, 0 ), ELineType.DOTTED1 ), //
  // false );

  /**
   * Параметры рисования линии для большой засечки по вертикальной шкале.<br>
   * Тип ссылки: {@link TsLineInfo}<br>
   * Значение по умолчанию: {@link TsLineInfo#fromLineAttributes(LineAttributes(1)}
   */
  @SuppressWarnings( "javadoc" )
  IDataDef VER_BIG_TICK_LINE_INFO = DataDef.create( "verBigTickLineInfo", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_BIG_TICK_LINE_INFO, //
      TSID_DESCRIPTION, STR_BIG_TICK_LINE_INFO_D, //
      TSID_KEEPER_ID, TsLineInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsLineInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsLineInfo.fromLineAttributes( new LineAttributes( 1 ) ) ) //
  );
  // IFimbedOptionInfo<ILineInfo> VER_BIG_TICK_LINE_INFO = new FimbedOptionInfo<>( //
  // IStdG2GridRendererOptions.class.getSimpleName() + ".HorBigTickLineInfo", //$NON-NLS-1$
  // STR_D_VER_BIG_TICK_LINE_INFO, STR_N_BIG_TICK_LINE_INFO, ILineInfo.class, LineInfoKeeper.KEEPER, //
  // new LineInfo( 1, new RGB( 0, 0, 0 ), ELineType.SOLID ), //
  // false );

  /**
   * Параметры рисования линии для средней засечки по вертикальной шкале.<br>
   * Тип ссылки: {@link TsLineInfo}<br>
   * Значение по умолчанию: {@link TsLineInfo#LineInfo(int, RGB, ELineType) LineInfo(1, 0, 0, 0}, ELineType.DOTTED1)}
   */
  @SuppressWarnings( "javadoc" )
  IDataDef VER_MID_TICK_LINE_INFO = DataDef.create( "verMidTickLineInfo", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_MID_TICK_LINE_INFO, //
      TSID_DESCRIPTION, STR_MID_TICK_LINE_INFO_D, //
      TSID_KEEPER_ID, TsLineInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsLineInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE,
      avValobj( new TsLineInfo( 1, ETsLineType.DOT, ETsLineCapStyle.FLAT, ETsLineJoinStyle.MITER, IIntList.EMPTY ) ) //
  );
  // IFimbedOptionInfo<ILineInfo> VER_MID_TICK_LINE_INFO = new FimbedOptionInfo<>( //
  // IStdG2GridRendererOptions.class.getSimpleName() + ".VerMidTickLineInfo", //$NON-NLS-1$
  // STR_D_VER_MID_TICK_LINE_INFO, STR_N_MID_TICK_LINE_INFO, ILineInfo.class, LineInfoKeeper.KEEPER, //
  // new LineInfo( 1, new RGB( 0, 0, 0 ), ELineType.DOTTED1 ), //
  // false );

  /**
   * Цвет горизонтальных линий.<br>
   * Тип ссылки: {@link Color}<br>
   * Значение по умолчанию: Color.BLACK}
   */
  IDataDef HOR_LINE_RGBA = DataDef.create( "horLineRgba", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_HOR_LINE_RGBA, //
      TSID_DESCRIPTION, STR_HOR_LINE_RGBA_D, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, TSID_DEFAULT_VALUE,
      avValobj( new RGBA( 0, 0, 0, 255 ) ) //
  );

  /**
   * Цвет вертикальных линий.<br>
   * Тип ссылки: {@link Color}<br>
   * Значение по умолчанию: Color.BLACK}
   */
  IDataDef VER_LINE_RGBA = DataDef.create( "verLineRgba", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_VER_LINE_RGBA, //
      TSID_DESCRIPTION, STR_VER_LINE_RGBA_D, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, TSID_DEFAULT_VALUE,
      avValobj( new RGBA( 0, 0, 0, 255 ) ) //
  );

}
