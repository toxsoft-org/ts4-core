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
 * Свойства стандартного отрисовщика графика.
 *
 * @author vs
 */
public interface IStdG2GraphicRendererOptions {

  /**
   * Название класса потребителя для {@link IG2Params#consumerName()}.
   */
  String CONSUMER_NAME = StdG2GraphicRenderer.class.getName();

  /**
   * Параметры рисования графика.<br>
   * Тип ссылки: {@link TsLineInfo}<br>
   * Значение по умолчанию: {@link TsLineInfo#fromLineAttributes( new LineAttributes(3)) }
   */
  @SuppressWarnings( "javadoc" )
  IDataDef GRAPHIC_LINE_INFO = DataDef.create( "graphLineInfo", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_GRAPHIC_LINE_INFO, //
      TSID_DESCRIPTION, STR_D_GRAPHIC_LINE_INFO, //
      TSID_KEEPER_ID, TsLineInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsLineInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsLineInfo.fromLineAttributes( new LineAttributes( 3 ) ) ) //
  );
  // IFimbedOptionInfo<ILineInfo> LINE_INFO = new FimbedOptionInfo<>( //
  // IStdG2GraphicRendererOptions.class.getSimpleName() + ".LineInfo", //$NON-NLS-1$
  // STR_D_GRAPH_LINE_INFO, STR_N_GRAPH_LINE_INFO, ILineInfo.class, LineInfoKeeper.KEEPER, //
  // new LineInfo( 3, new RGB( 0, 0, 0 ), ELineType.SOLID ), //
  // false );

  /**
   * Цвет графика.<br>
   * Тип ссылки: {@link Color}<br>
   * Значение по умолчанию: Color.BLACK}
   */
  IDataDef GRAPHIC_RGBA = DataDef.create( "graphicRgba", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_GRAPHIC_RGBA, //
      TSID_DESCRIPTION, STR_D_GRAPHIC_RGBA, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, TSID_DEFAULT_VALUE,
      avValobj( new RGBA( 0, 0, 0, 255 ) ) //
  );

  /**
   * Тип соединения точек графика.<br>
   * Тип данных: примитивный {@link EAtomicType#VALOBJ} <br>
   * Значение по умолчанию: {@link EGraphicRenderingKind#LINE}
   */
  IDataDef RENDERING_KIND = DataDef.create( "rederingKind", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_RENDERING_KIND, //
      TSID_DESCRIPTION, STR_D_RENDERING_KIND, //
      TSID_KEEPER_ID, EGraphicRenderingKind.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EGraphicRenderingKind.LINE ) //
  );
  // IAtomicOptionInfo RENDERING_KIND =
  // new AtomicOptionInfo( IStdG2GraphicRendererOptions.class.getSimpleName() + ".RederingKind", //$NON-NLS-1$
  // STR_D_RENDERING_KIND, STR_N_RENDERING_KIND, EAtomicType.STRING,
  // DvUtils.avStr( EGraphicRenderingKind.LINE.id() ), false );

}
