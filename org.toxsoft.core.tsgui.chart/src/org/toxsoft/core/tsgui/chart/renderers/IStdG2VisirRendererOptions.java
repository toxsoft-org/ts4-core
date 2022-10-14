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
 * Свойства сандартного отрисовщика "визира".
 *
 * @author vs
 */
public interface IStdG2VisirRendererOptions {

  /**
   * Название класса потребителя для {@link IG2Params#consumerName()}.
   */
  String CONSUMER_NAME = StdG2VisirRenderer.class.getName();

  /**
   * Параметры рисования вертикальной линии "визира".<br>
   * Тип ссылки: {@link TsLineInfo}<br>
   * Значение по умолчанию: {@link TsLineInfo#LineInfo#fromLineAttributes( LineAttributes(2) )}
   */
  @SuppressWarnings( "javadoc" )
  IDataDef VISIR_LINE_INFO = DataDef.create( "visirLineInfo", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_VISIR_LINE_INFO, //
      TSID_DESCRIPTION, STR_D_VISIR_LINE_INFO, //
      TSID_KEEPER_ID, TsLineInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsLineInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsLineInfo.fromLineAttributes( new LineAttributes( 2 ) ) ) //
  );
  // IFimbedOptionInfo<ILineInfo> VERTICAL_LINE_INFO = new FimbedOptionInfo<>( //
  // IStdG2VisirRendererOptions.class.getSimpleName() + ".VerticalLineInfo", //$NON-NLS-1$
  // STR_D_HOR_BIG_TICK_LINE_INFO, STR_N_BIG_TICK_LINE_INFO, ILineInfo.class, LineInfoKeeper.KEEPER, //
  // new LineInfo( 2, new RGB( 255, 0, 255 ), ELineType.SOLID ), //
  // false );

  /**
   * Цвет рисования вертикальной линии "визира".<br>
   * Тип ссылки: {@link RGBA}<br>
   * Значение по умолчанию: new RGBA( 255, 0, 255, 255 )
   */
  IDataDef VISIR_LINE_RGBA = DataDef.create( "visirLineRgba", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_VISIR_LINE_INFO, //
      TSID_DESCRIPTION, STR_D_VISIR_LINE_INFO, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, TSID_DEFAULT_VALUE,
      avValobj( new RGBA( 255, 0, 255, 255 ) ) //
  );

  /**
   * Признак того, нужно ли отображать название графика рядом со значением.<br>
   * Тип данных: примитивный {@link EAtomicType#BOOLEAN}<br>
   * Формат: булево значение - <b>true</b> - отображать название, <b>false</b> - не отображать название<br>
   * Значение по умолчанию: <b>true</b>
   */
  IDataDef SHOW_NAMES = DataDef.create( "showNames", BOOLEAN, // //$NON-NLS-1$
      TSID_NAME, STR_N_SHOW_NAMES, //
      TSID_DESCRIPTION, STR_D_SHOW_NAMES, //
      TSID_DEFAULT_VALUE, AvUtils.AV_TRUE //
  );
  // IAtomicOptionInfo SHOW_NAMES = new AtomicOptionInfo( IStdG2VisirRendererOptions.class.getSimpleName() +
  // ".ShowNames", //$NON-NLS-1$
  // STR_D_SHOW_NAMES, STR_N_SHOW_NAMES, EAtomicType.BOOLEAN, DvUtils.dvBool( true ), true );

}
