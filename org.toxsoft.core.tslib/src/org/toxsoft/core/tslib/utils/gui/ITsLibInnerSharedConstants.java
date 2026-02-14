package org.toxsoft.core.tslib.utils.gui;

/**
 * GUI-related constants to be shared between GUI and LIB plugins.
 * <p>
 * TSLIB itself is not a GUI library. However some entities from library are assumed to have GUI representation when
 * used in GUI environment. This interface lists icon and other graphical entity IDs for such entities. TSLIB does
 * <b>not</b> contains icons/entities itself, just IDs.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ITsLibInnerSharedConstants {

  /**
   * org.toxsoft.core.tsgui.valed.api.IValedControlConstants
   */
  String TSLIB_VCC_OPID_EDITOR_FACTORY_NAME = "org.toxsoft.valed.option.EditorFactoryName"; //$NON-NLS-1$
  String TSLIB_VCC_OPID_IS_SINGLE_LINE_UI   = "org.toxsoft.valed.op.isSingleLineUI";        //$NON-NLS-1$
  String TSLIB_VCC_OPID_VERTICAL_SPAN       = "org.toxsoft.valed.option.VerticalSpan";      //$NON-NLS-1$

  /**
   * org.toxsoft.core.tsgui.valed.controls.av.ValedAvValobjEnumCombo
   */
  String TSLIB_VALED_AV_VALOBJ_ENUM_COMBO = "ts.valed.AvValobjEnumCombo"; //$NON-NLS-1$

  /**
   * org.toxsoft.core.tsgui.valed.controls.av.ValedAvStringText
   */
  String TSLIB_VALED_AV_STRING_TEXT                 = "ts.valed.AvStringText";                                //$NON-NLS-1$
  String TSLIB_VALED_STRING_TEXT_OPID_IS_MULTI_LINE = "org.toxsoft.valed.option.ValedStringText.IsMultiLine"; //$NON-NLS-1$

  /**
   * org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds
   */
  String TSLIB_ICONID_INFO    = "dialog-information"; //$NON-NLS-1$
  String TSLIB_ICONID_WARNING = "dialog-warning";     //$NON-NLS-1$
  String TSLIB_ICONID_ERROR   = "dialog-error";       //$NON-NLS-1$

}
