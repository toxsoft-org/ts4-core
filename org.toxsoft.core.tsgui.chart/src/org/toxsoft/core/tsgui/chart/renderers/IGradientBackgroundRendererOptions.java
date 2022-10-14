package org.toxsoft.core.tsgui.chart.renderers;

import static org.toxsoft.core.tsgui.chart.renderers.IG2Resources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.impl.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Свойства отрисовщика фона с помощью градиентной заливки.
 * <p>
 * <b>На заметку:</b><br>
 * Поддерживается только два напрвления градиента
 * <ul>
 * <li><b>Горизонтальный</b> - цвет линейно менят свое значение в зависимости от координаты х от начального до конечного
 * значения слева направо</li>
 * <li><b>Вертикальный</b> - цвет линейно менят свое значение в зависимости от координаты y от начального до конечного
 * значения сверху вниз</li>
 * </ul>
 *
 * @author vs
 */
public interface IGradientBackgroundRendererOptions {

  /**
   * Название класса потребителя для {@link IG2Params#consumerName()}.
   */
  String CONSUMER_NAME = GradientBackgroundRenderer.class.getName();

  /**
   * Начальный цвет рисования градиентной заливки.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: число - первые 2 байта альфа канал, далее по два байта r,g и b составляющие цвета<br>
   * Значение по умолчанию: TsColorUtils.rgb2int( new RGB( 240,240, 240 ))
   */
  IDataDef START_COLOR = DataDef.create( "gradientStartColor", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_START_COLOR, //
      TSID_DESCRIPTION, STR_D_START_COLOR, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, TSID_DEFAULT_VALUE,
      avValobj( new RGBA( 240, 240, 240, 255 ) ) //
  );
  // IAtomicOptionInfo START_COLOR =
  // new AtomicOptionInfo( IGradientBackgroundRendererOptions.class.getSimpleName() + ".StartColor", //$NON-NLS-1$
  // STR_D_START_COLOR, STR_N_START_COLOR, EAtomicType.INTEGER,
  // DvUtils.dvInt( ITsColorManager.rgb2int( new RGB( 240, 240, 240 ) ) ), false );

  /**
   * Конечный цвет рисования градиентной заливки.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: число - первые 2 байта альфа канал, далее по два байта r,g и b составляющие цвета<br>
   * Значение по умолчанию: TsColorUtils.rgb2int( new RGB( 215,215, 215 ))
   */
  IDataDef END_COLOR = DataDef.create( "gradientEndColor", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_END_COLOR, //
      TSID_DESCRIPTION, STR_D_END_COLOR, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, TSID_DEFAULT_VALUE,
      avValobj( new RGBA( 215, 215, 215, 255 ) ) //
  );
  // IAtomicOptionInfo END_COLOR =
  // new AtomicOptionInfo( IGradientBackgroundRendererOptions.class.getSimpleName() + ".EndColor", //$NON-NLS-1$
  // STR_D_END_COLOR, STR_N_END_COLOR, EAtomicType.INTEGER,
  // DvUtils.dvInt( ITsColorManager.rgb2int( new RGB( 215, 215, 215 ) ) ), false );

  /**
   * Направление градиентной заливки.<br>
   * Тип данных: примитивный {@link EAtomicType#BOOLEAN}<br>
   * Формат: булевозначение - <b>true</b> - градиент в горизонтальном направлении, <b>false</b> - в вертикальном<br>
   * Значение по умолчанию: <b>false</b>
   */
  IDataDef HORIZONTAL = DataDef.create( "gradientHorizontal", BOOLEAN, // //$NON-NLS-1$
      TSID_NAME, STR_N_HORIZONTAL, //
      TSID_DESCRIPTION, STR_D_HORIZONTAL, //
      TSID_DEFAULT_VALUE, AvUtils.AV_FALSE //
  );
  // IAtomicOptionInfo HORIZONTAL =
  // new AtomicOptionInfo( IGradientBackgroundRendererOptions.class.getSimpleName() + ".Horizontal", //$NON-NLS-1$
  // STR_D_HORIZONTAL, STR_N_HORIZONTAL, EAtomicType.BOOLEAN, DvUtils.dvBool( false ), true );

}
