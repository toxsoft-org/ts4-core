package org.toxsoft.core.tsgui.valed.api;

import static org.toxsoft.core.tsgui.valed.api.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.gui.ITsLibInnerSharedConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.vecboard.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.validators.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

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
  String OPID_EDITOR_FACTORY_NAME = TSLIB_VCC_EDITOR_FACTORY_NAME;

  /**
   * ID of option {@link #OPDEF_CREATE_UNEDITABLE}.
   */
  String OPID_CREATE_UNEDITABLE = "org.toxsoft.valed.option.CreateUneditable"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_TOOLTIP_TEXT}.
   */
  String OPID_TOOLTIP_TEXT = "org.toxsoft.valed.option.TooltipText"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_IS_WIDTH_FIXED}.
   */
  String OPID_IS_WIDTH_FIXED = "org.toxsoft.valed.option.IsWidthFixed"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_IS_HEIGHT_FIXED}.
   */
  String OPID_IS_HEIGHT_FIXED = "org.toxsoft.valed.option.IsHeightFixed"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_VERTICAL_SPAN}.
   */
  String OPID_VERTICAL_SPAN = "org.toxsoft.valed.option.VerticalSpan"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_NO_FIELD_LABEL}.
   */
  String OPID_NO_FIELD_LABEL = "org.toxsoft.valed.option.NoFieldLabel"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_VALED_UI_OUTFIT}.
   */
  String OPID_VALED_UI_OUTFIT = TSLIB_OPID_VALED_UI_OUTFIT;

  /**
   * ID of context reference {@link #REFDEF_VALUE_VISUALS_PROVIDER}.
   */
  String REFID_VALUE_VISUALS_PROVIDER = VALED_REFID_PREFIX + ".ValueVisualsProvider"; //$NON-NLS-1$

  // TODO TRANSLATE

  /**
   * Регистрационная строка или имя класса фабрики для использования без регистрации.<br>
   * <i>Type:</i> {@link EAtomicType#STRING}<br>
   * <i>Usage:</i> Непустая строка. Этот параметр непосредственно не используется редактором {@link IValedControl}, из
   * параметров редактора его извлекает любой код, находящий по идентификатору фабрику редактора в списке
   * зарегистрированных фабрик. С помощью фабрики создается редактор, а дальше этот значение не используется редактором.
   * Интерпретация данного параметра находится вне компетенции редактора, хотя можно рекомендовать использовать полное
   * имя класса фабрики, для создания экземпляра фабрики методом {@link Class#forName(String)}. Например, именно так
   * работает реестр фабрик {@link IValedControlFactoriesRegistry}.<br>
   * <i>Default value:</i> {@link IAtomicValue#NULL}
   */
  IDataDef OPDEF_EDITOR_FACTORY_NAME = DataDef.create( OPID_EDITOR_FACTORY_NAME, STRING, //
      TSID_NAME, STR_EDITOR_FACTORY_NAME, //
      TSID_DESCRIPTION, STR_EDITOR_FACTORY_NAME_D, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * Созданный контроль редактора будет показывать значение без возможности правки.<br>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN}<br>
   * <i>Usage:</i> true - контроль не позволяет редактировать значение, false - обычный, редактируемый контроль. Запрет
   * редактирования этим параметром имеет приоритет над {@link IValedControl#setEditable(boolean)}.<br>
   * <i>Default value:</i> false - можно редактировать
   */
  IDataDef OPDEF_CREATE_UNEDITABLE = DataDef.create( OPID_CREATE_UNEDITABLE, BOOLEAN, //
      TSID_NAME, STR_CREATE_UNEDITABLE, //
      TSID_DESCRIPTION, STR_CREATE_UNEDITABLE_D, //
      TSID_DEFAULT_VALUE, AV_FALSE, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * Текст всплывающей подсказки.<br>
   * <i>Type:</i> {@link EAtomicType#STRING}<br>
   * <i>Usage:</i> удобочитаемый текст, который показывается во всплывающей подсказке. Пустая строка или
   * {@link IAtomicValue#NULL} для отсутствия подсказки;<br>
   * <i>Default value:</i> пустая строка
   */
  IDataDef OPDEF_TOOLTIP_TEXT = DataDef.create( OPID_TOOLTIP_TEXT, STRING, //
      TSID_NAME, STR_TOOLTIP_TEXT, //
      TSID_DESCRIPTION, STR_TOOLTIP_TEXT_D, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * Признак не желания контроля редактора занимать всю возможную ширину.<br>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN}<br>
   * <i>Usage:</i> true - контроль говорит, что предпочтительно оставить его ширину так, как он посчитал, false - по
   * возможности занять всю возможную ширину в родительской панели<br>
   * <i>Default value:</i> <code>false</code>
   */
  IDataDef OPDEF_IS_WIDTH_FIXED = DataDef.create( OPID_IS_WIDTH_FIXED, BOOLEAN, //
      TSID_NAME, STR_IS_WIDTH_FIXED, //
      TSID_DESCRIPTION, STR_IS_WIDTH_FIXED_D, //
      TSID_DEFAULT_VALUE, AV_FALSE, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * Признак не желания контроля редактора занимать всю возможную высоту.<br>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN}<br>
   * <i>Usage:</i> true - контроль говорит, что предпочтительно оставить его высоту так, как он посчитал, false - по
   * возможности занять всю возможную высоту в родительской панели<br>
   * <i>Default value:</i> true (если иное не задано в самом редакторе)
   */
  IDataDef OPDEF_IS_HEIGHT_FIXED = DataDef.create( OPID_IS_HEIGHT_FIXED, BOOLEAN, //
      TSID_NAME, STR_IS_HEIGHT_FIXED, //
      TSID_DESCRIPTION, STR_IS_HEIGHT_FIXED_D, //
      TSID_DEFAULT_VALUE, AV_TRUE, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * Высота редактора (в единицах высоты однострочного поля ввода текста).<br>
   * <i>Type:</i> {@link EAtomicType#INTEGER}<br>
   * <i>Usage:</i> чаще всего все виджеты редактора имеют единичную высоту (в единицах высоты однострочного
   * {@link Text}). Это естественно, поскольку в них содержится именно строка текста (например, {@link Button},
   * {@link Text}, {@link Spinner} и т.п.). Когда пользователь сам создает раскладку, он может сам управлять размерами
   * контролей, но при автоматическом создании GUI требуется подсказка, если виджет должен быть выше, чем одна единица.
   * Особенно это актуально для раскладки {@link IVecLadderLayout}. Это параметр является подсказкой, сколько "единиц"
   * высоты должно быть в контроле. Особенно полезно для многострочного {@link Text} и редакторов связей, представляющих
   * данные в виде списка;<br>
   * <i>Default value:</i> 1
   */
  IDataDef OPDEF_VERTICAL_SPAN = DataDef.create( OPID_VERTICAL_SPAN, INTEGER, //
      TSID_NAME, STR_VERTICAL_SPAN, //
      TSID_DESCRIPTION, STR_VERTICAL_SPAN_D, //
      TSID_DEFAULT_VALUE, avInt( 1 ), //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * Признак скрытия подписи к редактору.<br>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN}<br>
   * <i>Usage:</i> true - контроль говорит, он сам содержит в себе визуальное пояснение что редактирует, не нужен
   * генерируемый по умолчанию {@link Label}, false - следует использовать (если доступно) автоматические создание меток
   * для идентификации поля.<br>
   * <i>Default value:</i> <code>false</code>
   */
  IDataDef OPDEF_NO_FIELD_LABEL = DataDef.create( OPID_NO_FIELD_LABEL, BOOLEAN, //
      TSID_NAME, STR_NO_FIELD_LABEL, //
      TSID_DESCRIPTION, STR_NO_FIELD_LABEL_D, //
      TSID_DEFAULT_VALUE, AV_FALSE, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * Option {@link #OPDEF_VALED_UI_OUTFIT} predefined value: choose single line UI (like SWT {@link Text} control).
   * <p>
   * Usually it means to choose the VALED UI with the smallest possible height.
   *
   * @see IValedControlConstants#VALED_UI_OUTFIT_EMBEDDABLE
   */
  String VALED_UI_OUTFIT_SINGLE_LINE = TSLIB_VALED_UI_OUTFIT_SINGLE_LINE;

  /**
   * Option {@link #OPDEF_VALED_UI_OUTFIT} predefined value: choose full size panel (like to embed in dialog window).
   * <p>
   * Usually it means the panel (maybe with multiple controls) allowing to specify all the properties of the edited
   * value, as if the VALED should be directly used as the content of the value edit dialog.
   *
   * @see IValedControlConstants#VALED_UI_OUTFIT_SINGLE_LINE
   */
  String VALED_UI_OUTFIT_EMBEDDABLE = TSLIB_VALED_UI_OUTFIT_EMBEDDABLE;

  /**
   * Specifies the appearance of the VALED UI if it has multiple user interfaces to choose from..<br>
   * <i>Type:</i> {@link EAtomicType#STRING}<br>
   * <i>Usage:</i> some factories may produce different VALEDs with different appearance. For example, the person editor
   * may have two UIs: as a text line with edit button (invoking the edit dialog) and the full size panel with "First
   * name", "Second name", "Family name" and "Birth date" editors. This option is designed for such multi-UI editors to
   * specify which appearance to use when created.<br>
   * Option may have one of the predefined values <code><b>VALED_UI_OUTFIT_</b>XXX</code> or VALED-specific value. <br>
   * <i>Default value:</i> {@link #VALED_UI_OUTFIT_SINGLE_LINE} - VALED of the smallest height
   *
   * @see IValedControlConstants#VALED_UI_OUTFIT_SINGLE_LINE
   * @see IValedControlConstants#VALED_UI_OUTFIT_EMBEDDABLE
   */
  IDataDef OPDEF_VALED_UI_OUTFIT = DataDef.create2( OPID_VALED_UI_OUTFIT, STRING, //
      IdPathStringAvValidator.IDPATH_EMPTY_VALIDATOR, AvUtils.DEFAULT_AV_COMPARATOR, //
      TSID_NAME, STR_NO_FIELD_LABEL, //
      TSID_DESCRIPTION, STR_NO_FIELD_LABEL_D, //
      TSID_DEFAULT_VALUE, avStr( VALED_UI_OUTFIT_SINGLE_LINE ), //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * The context reference to the {@link ITsVisualsProvider}.<br>
   * <i>Reference type:</i> {@link ITsVisualsProvider}<br>
   * <i>Usage:</i> visuals provider must accept VALED value type objects as an argument of the
   * {@link ITsVisualsProvider} methods. Returned values are used to display value as text, icon or thumbnail.<br>
   * <i>Default value:</i> none
   */
  @SuppressWarnings( "rawtypes" )
  ITsGuiContextRefDef<ITsVisualsProvider> REFDEF_VALUE_VISUALS_PROVIDER = //
      TsGuiContextRefDef.create( REFID_VALUE_VISUALS_PROVIDER, ITsVisualsProvider.class, //
          TSID_NAME, STR_VALUE_VISUALS_PROVIDER, //
          TSID_DESCRIPTION, STR_VALUE_VISUALS_PROVIDER_D, //
          TSID_IS_MANDATORY, AV_FALSE //
      );

  /**
   * Options to be inhibited when creating the new VALED.
   */
  IStridablesList<IDataDef> ALL_INHIBITABLE_VALED_OPDEFS = new StridablesList<>( //
      OPDEF_EDITOR_FACTORY_NAME, //
      OPDEF_CREATE_UNEDITABLE, //
      OPDEF_TOOLTIP_TEXT, //
      OPDEF_IS_WIDTH_FIXED, //
      OPDEF_IS_HEIGHT_FIXED, //
      OPDEF_VERTICAL_SPAN, //
      OPDEF_NO_FIELD_LABEL //
  );

  /**
   * Removes all options {@link #ALL_INHIBITABLE_VALED_OPDEFS} from the specified context.
   * <p>
   * This method is to be used when preparing context for VALED creation. Recall that for each instance of the VALED,
   * you need to create its own instance of the context. Creating context with constructor
   * {@link TsGuiContext#TsGuiContext(ITsGuiContext)} inherits options from the parent context. VALED options listed
   * below <b>must</b> be specified for each VALED individually. The problem is with the option not specified
   * explicitly. For such options parent context values are returned. However most VALEDs assume that unspecified
   * options have default values. This method sets value of each option from {@link #ALL_INHIBITABLE_VALED_OPDEFS} to
   * {@link IAtomicValue#NULL} marking their values as not specified.
   * <p>
   * Use this method immediately after creating {@link ITsGuiContext} instance for each VALED.
   *
   * @param aValedContext {@link ITsGuiContext} - context for individual VALED creation
   */
  static void inhibitParamsOfParentContext( ITsGuiContext aValedContext ) {
    for( IDataDef opdef : ALL_INHIBITABLE_VALED_OPDEFS ) {
      aValedContext.params().setValue( opdef, IAtomicValue.NULL );
    }
  }

}
