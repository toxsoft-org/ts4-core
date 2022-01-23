package org.toxsoft.tsgui.valed.api;

import static org.toxsoft.tsgui.valed.api.ITsResources.*;
import static org.toxsoft.tslib.ITsHardConstants.*;
import static org.toxsoft.tslib.av.EAtomicType.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;
import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContextRefDef;
import org.toxsoft.tsgui.bricks.ctx.impl.TsGuiContextRefDef;
import org.toxsoft.tsgui.panels.vecboard.IVecLadderLayout;
import org.toxsoft.tsgui.utils.ITsVisualsProvider;
import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.impl.DataDef;
import org.toxsoft.tslib.av.metainfo.IDataDef;

/**
 * {@link IValedControl} related constants.
 *
 * @author hazard157
 */
public interface IValedControlConstants {

  /**
   * Common prefix of all editor names.
   */
  String VALED_EDNAME_PREFIX = TS_ID + ".valed"; //$NON-NLS-1$

  /**
   * Common prefix of all option IDs in this interface and for other Valed controls.
   */
  String VALED_OPID_PREFIX = TS_FULL_ID + ".valed.option"; //$NON-NLS-1$

  /**
   * Common prefix of all references IDs in this interface and for other Valed controls.
   */
  String VALED_REFID_PREFIX = TS_FULL_ID + ".valed.ref"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_EDITOR_FACTORY_NAME}.
   */
  String OPID_EDITOR_FACTORY_NAME = VALED_OPID_PREFIX + ".EditorFactoryName"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_CREATE_UNEDITABLE}.
   */
  String OPID_CREATE_UNEDITABLE = VALED_OPID_PREFIX + ".CreateUneditable"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_TOOLTIP_TEXT}.
   */
  String OPID_TOOLTIP_TEXT = VALED_OPID_PREFIX + ".TooltipText"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_IS_WIDTH_FIXED}.
   */
  String OPID_IS_WIDTH_FIXED = VALED_OPID_PREFIX + ".IsWidthFixed"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_IS_HEIGHT_FIXED}.
   */
  String OPID_IS_HEIGHT_FIXED = VALED_OPID_PREFIX + ".IsHeightFixed"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_VERTICAL_SPAN}.
   */
  String OPID_VERTICAL_SPAN = VALED_OPID_PREFIX + ".VerticalSpan"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_NO_FIELD_LABEL}.
   */
  String OPID_NO_FIELD_LABEL = VALED_OPID_PREFIX + ".NoFieldLabel"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #REFDEF_VALUE_VISUALS_PROVIDER}.
   */
  String REFID_VALUE_VISUALS_PROVIDER = VALED_REFID_PREFIX + ".ValueNameProvider"; //$NON-NLS-1$

  /**
   * Регистрационная строка или имя класса фабрики для использования без регистрации.<br>
   * Тип данных: примитивный {@link EAtomicType#STRING}<br>
   * Формат: Непустая строка. Этот параметр непосредственно не используется редактором {@link IValedControl}, из
   * параметров редактора его извлекает любой код, находящий по идентификатору фабрику редактора в списке
   * зарегистрированных фабрик. С помощью фабрики создается редактор, а дальше этот значение не используется редактором.
   * Интерпретация данного параметра находится вне компетенции редактора, хотя можно рекомендовать использовать полное
   * имя класса фабрики, для создания экземпляра фабрики методом {@link Class#forName(String)}. Например, именно так
   * работает реестр фабрик {@link IValedControlFactoriesRegistry}.<br>
   * Значение по умолчанию: {@link IAtomicValue#NULL}
   */
  IDataDef OPDEF_EDITOR_FACTORY_NAME = DataDef.create( OPID_EDITOR_FACTORY_NAME, STRING, //
      TSID_NAME, STR_N_EDITOR_FACTORY_NAME, //
      TSID_DESCRIPTION, STR_D_EDITOR_FACTORY_NAME, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * Созданный контроль редактора будет показывать значение без возможности правки.<br>
   * Тип данных: {@link EAtomicType#BOOLEAN}<br>
   * Формат: true - контроль не позволяет редактировать значение, false - обычный, редактируемый контроль. Запрет
   * редактирования этим параметром имеет приоритет над {@link IValedControl#setEditable(boolean)}.<br>
   * Значение по умолчанию: false - можно редактировать
   */
  IDataDef OPDEF_CREATE_UNEDITABLE = DataDef.create( OPID_CREATE_UNEDITABLE, BOOLEAN, //
      TSID_NAME, STR_N_CREATE_UNEDITABLE, //
      TSID_DESCRIPTION, STR_D_CREATE_UNEDITABLE, //
      TSID_DEFAULT_VALUE, AV_FALSE, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * Текст всплывающей подсказки.<br>
   * Тип данных: {@link EAtomicType#STRING}<br>
   * Формат: удобочитаемый текст, который показывается во всплывающей подсказке. Пустая строка или
   * {@link IAtomicValue#NULL} для отсутствия подсказки;<br>
   * Значение по умолчанию: пустая строка
   */
  IDataDef OPDEF_TOOLTIP_TEXT = DataDef.create( OPID_TOOLTIP_TEXT, STRING, //
      TSID_NAME, STR_N_TOOLTIP_TEXT, //
      TSID_DESCRIPTION, STR_D_TOOLTIP_TEXT, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * Признак не желания контроля редактора занимать всю возможную ширину.<br>
   * Тип данных: {@link EAtomicType#BOOLEAN}<br>
   * Формат: true - контроль говорит, что предпочтительно оставить его ширину так, как он посчитал, false - по
   * возможности занять всю возможную ширину в родительской панели<br>
   * Значение по умолчанию: true (если иное не задано в самом редакторе)
   */
  IDataDef OPDEF_IS_WIDTH_FIXED = DataDef.create( OPID_IS_WIDTH_FIXED, BOOLEAN, //
      TSID_NAME, STR_N_IS_WIDTH_FIXED, //
      TSID_DESCRIPTION, STR_D_IS_WIDTH_FIXED, //
      TSID_DEFAULT_VALUE, AV_TRUE, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * Признак не желания контроля редактора занимать всю возможную высоту.<br>
   * Тип данных: {@link EAtomicType#BOOLEAN}<br>
   * Формат: true - контроль говорит, что предпочтительно оставить его высоту так, как он посчитал, false - по
   * возможности занять всю возможную высоту в родительской панели<br>
   * Значение по умолчанию: true (если иное не задано в самом редакторе)
   */
  IDataDef OPDEF_IS_HEIGHT_FIXED = DataDef.create( OPID_IS_HEIGHT_FIXED, BOOLEAN, //
      TSID_NAME, STR_N_IS_HEIGHT_FIXED, //
      TSID_DESCRIPTION, STR_D_IS_HEIGHT_FIXED, //
      TSID_DEFAULT_VALUE, AV_TRUE, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * Высота редактора (в единицах высоты однострочного поля ввода текста).<br>
   * Тип данных: {@link EAtomicType#INTEGER}<br>
   * Формат: чаще всего все виджеты редактора имеют единичную высоту (в единицах высоты однострочного {@link Text}). Это
   * естественно, поскольку в них содержится именно строка текста (например, {@link Button}, {@link Text},
   * {@link Spinner} и т.п.). Когда пользователь сам создает раскладку, он может сам управлять размерами контролей, но
   * при автоматическом создании GUI требуется подсказка, если виджет должен быть выше, чем одна единица. Особенно это
   * актуально для раскладки {@link IVecLadderLayout}. Это параметр является подсказкой, сколько "единиц" высоты должно
   * быть в контроле. Особенно полезно для многострочного {@link Text} и редакторов связей, представляющих данные в виде
   * списка;<br>
   * Значение по умолчанию: 1
   */
  IDataDef OPDEF_VERTICAL_SPAN = DataDef.create( OPID_VERTICAL_SPAN, INTEGER, //
      TSID_NAME, STR_N_VERTICAL_SPAN, //
      TSID_DESCRIPTION, STR_D_VERTICAL_SPAN, //
      TSID_DEFAULT_VALUE, avInt( 1 ), //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * Признак скрытия подписи к редактору.<br>
   * Тип данных: {@link EAtomicType#BOOLEAN}<br>
   * Формат: true - контроль говорит, он сам содержит в себе визуальное пояснение что редактирует, не нужен генерируемый
   * по умолчанию {@link Label}, false - следует использовать (если доступно) автоматические создание меток для
   * идентификации поля.<br>
   * Значение по умолчанию: false
   */
  IDataDef OPDEF_NO_FIELD_LABEL = DataDef.create( OPID_NO_FIELD_LABEL, BOOLEAN, //
      TSID_NAME, STR_N_NO_FIELD_LABEL, //
      TSID_DESCRIPTION, STR_D_NO_FIELD_LABEL, //
      TSID_DEFAULT_VALUE, AV_FALSE, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * The context reference to the {@link ITsVisualsProvider} or {@link ITsVisualsProvider}.<br>
   * Reference type: {@link ITsVisualsProvider}<br>
   * Usage: visuals provider must accept value type as argument of {@link ITsVisualsProvider}. Returned values are used
   * to display value as text, icon or thumbnail.<br>
   * Default value: none
   */
  @SuppressWarnings( "rawtypes" )
  ITsGuiContextRefDef<ITsVisualsProvider> REFDEF_VALUE_VISUALS_PROVIDER = //
      TsGuiContextRefDef.create( REFID_VALUE_VISUALS_PROVIDER, ITsVisualsProvider.class, //
          TSID_NAME, STR_N_VALUE_VISUALS_PROVIDER, //
          TSID_DESCRIPTION, STR_D_VALUE_VISUALS_PROVIDER, //
          TSID_IS_MANDATORY, AV_FALSE //
      );

}
