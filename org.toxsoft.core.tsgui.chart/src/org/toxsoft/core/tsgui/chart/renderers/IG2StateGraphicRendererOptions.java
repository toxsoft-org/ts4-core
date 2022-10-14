package org.toxsoft.core.tsgui.chart.renderers;

import static org.toxsoft.core.tsgui.chart.renderers.IG2Resources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Свойства стандартного отрисовщика графика состояний.
 *
 * @author vs
 */
public interface IG2StateGraphicRendererOptions {

  /**
   * Название класса потребителя для {@link IG2Params#consumerName()}.
   */
  String CONSUMER_NAME = G2StateGraphicRenderer.class.getName();

  /**
   * Префикс ИДов определений данных
   */
  String ID_PREFIX = IG2StateGraphicRendererOptions.class.getSimpleName();

  /**
   * Признак того, нужно ли на канве рисовать название графика.<br>
   * Тип данных: примитивный {@link EAtomicType#BOOLEAN}<br>
   * Формат: логическое значение - <b>true</b> - нужно на канве рисовать название графика<br>
   * Значение по умолчанию: false
   */
  IDataDef DRAW_LABEL = DataDef.create( ID_PREFIX + ".DrawLabel", BOOLEAN, // //$NON-NLS-1$
      TSID_NAME, STR_N_DRAW_LABEL, //
      TSID_DESCRIPTION, STR_D_DRAW_LABEL, //
      TSID_DEFAULT_VALUE, AvUtils.AV_FALSE //
  );
  // IAtomicOptionInfo DRAW_LABEL = new AtomicOptionInfo( //
  // IG2StateGraphicRendererOptions.class.getSimpleName() + ".DrawLabel", //$NON-NLS-1$
  // STR_D_DRAW_LABEL, STR_N_DRAW_LABEL, EAtomicType.BOOLEAN, DvUtils.dvBool( false ), false );

  /**
   * Толщина линии графика.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: целое значение - толщина линии графика<br>
   * Значение по умолчанию: 8
   */
  IDataDef LINE_THICKNESS = DataDef.create( ID_PREFIX + ".LineThick", INTEGER, // //$NON-NLS-1$
      TSID_NAME, STR_N_LINE_THICKNESS, //
      TSID_DESCRIPTION, STR_D_LINE_THICKNESS, //
      TSID_DEFAULT_VALUE, AvUtils.avInt( 8 ) //
  );
  // IAtomicOptionInfo LINE_THICKNESS = new AtomicOptionInfo( //
  // IG2StateGraphicRendererOptions.class.getSimpleName() + ".LineThickness", //$NON-NLS-1$
  // STR_D_LINE_THICKNESS, STR_N_LINE_THICKNESS, EAtomicType.INTEGER, DvUtils.dvInt( 8 ), false );

  /**
   * Набор цветов для отображения состояний (каждый элемент - целое число, преобразованное в строку).<br>
   * Тип ссылки: {@link IStringList}<br>
   * Значение по умолчанию: {@link IStringList#EMPTY}
   */
  IDataDef COLORS = DataDef.create( "colors", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_COLORS, //
      TSID_DESCRIPTION, STR_D_COLORS, //
      TSID_KEEPER_ID, StringListKeeper.KEEPER_ID, //
      // OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( IStringList.EMPTY ) //
  );
  // IFimbedOptionInfo<IStringList> COLORS = new FimbedOptionInfo<>( //
  // IG2StateGraphicRendererOptions.class.getSimpleName() + ".Colors", //$NON-NLS-1$
  // STR_D_COLORS, STR_N_COLORS, IStringList.class, StringListKeeper.KEEPER, //
  // IStringList.EMPTY, //
  // false );

  /**
   * Набор названий для отображения состояний.<br>
   * Тип ссылки: {@link IStringList}<br>
   * Значение по умолчанию: {@link IStringList#EMPTY}
   */
  IDataDef STATE_NAMES = DataDef.create( "stateNames", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_STATE_NAMES, //
      TSID_DESCRIPTION, STR_D_STATE_NAMES, //
      TSID_KEEPER_ID, StringListKeeper.KEEPER_ID, //
      // OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( IStringList.EMPTY ) //
  );
  // IFimbedOptionInfo<IStringList> STATE_NAMES = new FimbedOptionInfo<>( //
  // IG2StateGraphicRendererOptions.class.getSimpleName() + ".StateNames", //$NON-NLS-1$
  // STR_D_STATE_NAMES, STR_N_STATE_NAMES, IStringList.class, StringListKeeper.KEEPER, //
  // IStringList.EMPTY, //
  // false );

}
