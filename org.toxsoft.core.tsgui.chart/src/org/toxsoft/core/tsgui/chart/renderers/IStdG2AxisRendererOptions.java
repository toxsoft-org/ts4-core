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
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;

/**
 * Свойства отрисовщика шкалы по умолчанию.
 * <p>
 * Предполагается что процесс отрисовки шкалы осуществляется тремя отрисовщиками:
 * <ul>
 * <li>Отрисовщик фона</li>
 * <li>Отрисовщик разметки</li>
 * <li>Отрисовщик подписей</li>
 * </ul>
 * Таким образом, параметры настройки разделены на три группы, соотвествующие, указанным выше отрисовщикам.
 * <b>Мотивация:</b><br>
 * Разделение процесса рисования на три группы было сделано для облегчения реализации вариативности отображения шкалы.
 * Например для того чтобы сделать наклонные или вертикальные надписи не надо переписывать весь отрисовщик шкалы (или
 * создавать сомнительную иерархию классов), а достаточно просто написать соотвествующий аннотатор, имеющий ограниченную
 * и понятную функциональность.
 *
 * @author vs
 */
public interface IStdG2AxisRendererOptions {

  /**
   * Название класса потребителя для {@link IG2Params#consumerName()}.
   */
  String CONSUMER_NAME = StdG2AxisRenderer.class.getName();

  /**
   * Имя класса - отрисовщика фона.<br>
   * Тип данных: примитивный {@link EAtomicType#STRING}<br>
   * Формат: имя класса Значение по умолчанию: {@link IGradientBackgroundRendererOptions}
   */
  IDataDef BACKGROUND_RENDERER_CLASS = DataDef.create( "backgroundClass", STRING, //$NON-NLS-1$
      TSID_NAME, STR_BACKGROUND_RENDERER_CLASS, //
      TSID_DESCRIPTION, STR_BACKGROUND_RENDERER_CLASS_D, //
      TSID_DEFAULT_VALUE, AvUtils.avStr( IGradientBackgroundRendererOptions.CONSUMER_NAME ) //
  );
  // IAtomicOptionInfo BACKGROUND_RENDERER_CLASS =
  // new AtomicOptionInfo( IStdG2AxisRendererOptions.class.getSimpleName() + ".BackgroundClass", //$NON-NLS-1$
  // STR_D_BACKGROUND_RENDERER_CLASS, STR_N_BACKGROUND_RENDERER_CLASS, EAtomicType.STRING,
  // DvUtils.dvStr( IGradientBackgroundRendererOptions.CONSUMER_NAME ), false );

  /**
   * Параметры отрисовщика фона.<br>
   * Тип ссылки: {@link IOptionSet}<br>
   * Значение по умолчанию: {@link IOptionSet#NULL}
   */
  IDataDef BACKGROUND_RENDERER_OPS = DataDef.create( "backgroundOps", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_BACKGROUND_RENDERER_OPS, //
      TSID_DESCRIPTION, STR_BACKGROUND_RENDERER_OPS_D, //
      TSID_KEEPER_ID, OptionSetKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( new OptionSet() ) //
  );
  // IFimbedOptionInfo<IOptionSet> BACKGROUND_RENDERER_OPS =
  // new FimbedOptionInfo<>( IStdG2AxisRendererOptions.class.getSimpleName() + ".BackgroundOps", //$NON-NLS-1$
  // STR_D_BACKGROUND_RENDERER_OPS, STR_N_BACKGROUND_RENDERER_OPS, IOptionSet.class, OptionSetKeeper.KEEPER,
  // IOptionSet.NULL, false );

  /**
   * Имя класса - отрисовщика разметки.<br>
   * Тип данных: примитивный {@link EAtomicType#STRING}<br>
   * Формат: имя класса Значение по умолчанию: {@link IStdG2AxisMarkingRendererOptions}
   */
  IDataDef MARKING_RENDERER_CLASS = DataDef.create( "markingClass", STRING, //$NON-NLS-1$
      TSID_NAME, STR_MARKING_RENDERER_CLASS, //
      TSID_DESCRIPTION, STR_MARKING_RENDERER_CLASS_D, //
      TSID_DEFAULT_VALUE, AvUtils.avStr( IStdG2AxisMarkingRendererOptions.CONSUMER_NAME ) //
  );
  // IAtomicOptionInfo MARKING_RENDERER_CLASS =
  // new AtomicOptionInfo( IStdG2AxisRendererOptions.class.getSimpleName() + ".MarkingClass", //$NON-NLS-1$
  // STR_D_MARKING_RENDERER_CLASS, STR_N_MARKING_RENDERER_CLASS, EAtomicType.STRING,
  // DvUtils.dvStr( IStdG2AxisMarkingRendererOptions.CONSUMER_NAME ), false );

  /**
   * Параметры отрисовщика разметки.<br>
   * Тип ссылки: {@link IOptionSet}<br>
   * Значение по умолчанию: {@link IOptionSet#NULL}
   */
  IDataDef MARKING_RENDERER_OPS = DataDef.create( "markingOps", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_MARKING_RENDERER_OPS, //
      TSID_DESCRIPTION, STR_MARKING_RENDERER_OPS_D, //
      TSID_KEEPER_ID, OptionSetKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( new OptionSet() ) //
  );
  // IFimbedOptionInfo<IOptionSet> MARKING_RENDERER_OPS =
  // new FimbedOptionInfo<>( IStdG2AxisRendererOptions.class.getSimpleName() + ".MarkingOps", //$NON-NLS-1$
  // STR_D_MARKING_RENDERER_OPS, STR_N_MARKING_RENDERER_OPS, IOptionSet.class, OptionSetKeeper.KEEPER,
  // IOptionSet.NULL, false );

  /**
   * Имя класса - отрисовщика подписей.<br>
   * Тип данных: примитивный {@link EAtomicType#STRING}<br>
   * Формат: имя класса Значение по умолчанию: {@link IStdG2AxisAnnotationRendererOptions}
   */
  IDataDef ANNOTATION_RENDERER_CLASS = DataDef.create( "annotationClass", STRING, //$NON-NLS-1$
      TSID_NAME, STR_ANNOTATION_RENDERER_CLASS, //
      TSID_DESCRIPTION, STR_ANNOTATION_RENDERER_CLASS_D, //
      TSID_DEFAULT_VALUE, AvUtils.avStr( IGradientBackgroundRendererOptions.CONSUMER_NAME ) //
  );
  // IAtomicOptionInfo ANNOTATION_RENDERER_CLASS =
  // new AtomicOptionInfo( IStdG2AxisRendererOptions.class.getSimpleName() + ".AnnotationClass", //$NON-NLS-1$
  // STR_D_ANNOTATION_RENDERER_CLASS, STR_N_ANNOTATION_RENDERER_CLASS, EAtomicType.STRING,
  // DvUtils.dvStr( IStdG2AxisAnnotationRendererOptions.CONSUMER_NAME ), false );

  /**
   * Параметры отрисовщика подписей.<br>
   * Тип ссылки: {@link IOptionSet}<br>
   * Значение по умолчанию: {@link IOptionSet#NULL}
   */
  IDataDef ANNOTATION_RENDERER_OPS = DataDef.create( "annotationOps", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_ANNOTATION_RENDERER_OPS, //
      TSID_DESCRIPTION, STR_ANNOTATION_RENDERER_OPS_D, //
      TSID_KEEPER_ID, OptionSetKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( new OptionSet() ) //
  );
  // IFimbedOptionInfo<IOptionSet> ANNOTATION_RENDERER_OPS =
  // new FimbedOptionInfo<>( IStdG2AxisRendererOptions.class.getSimpleName() + ".AnnotationOps", //$NON-NLS-1$
  // STR_D_ANNOTATION_RENDERER_OPS, STR_N_ANNOTATION_RENDERER_OPS, IOptionSet.class, OptionSetKeeper.KEEPER,
  // IOptionSet.NULL, false );

}
