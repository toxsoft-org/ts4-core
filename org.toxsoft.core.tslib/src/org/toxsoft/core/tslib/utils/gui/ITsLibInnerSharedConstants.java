package org.toxsoft.core.tslib.utils.gui;

/**
 * GUI-related constants to be shared between GUI and LIB plugins.
 * <p>
 * TSLIB itself is not a GUI library. However some entities from library are assumed to have GUI representation when
 * used in GUI environment. This interface lists icon IDs for such entities. TSLIB does <b>not</b> contains icons
 * itself, just IDs.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ITsLibInnerSharedConstants {

  String TSLIB_VCC_EDITOR_FACTORY_NAME = "org.toxsoft.valed.option.EditorFactoryName"; //$NON-NLS-1$

  String TSLIB_VALED_AV_VALOBJ_ENUM_COMBO  = "ts.valed.AvValobjEnumCombo";             //$NON-NLS-1$
  String TSLIB_OPID_VALED_UI_OUTFIT        = "org.toxsoft.valed.option.ValedUiOutfit"; //$NON-NLS-1$
  String TSLIB_VALED_UI_OUTFIT_SINGLE_LINE = "SingleLine";                             //$NON-NLS-1$
  String TSLIB_VALED_UI_OUTFIT_EMBEDDABLE  = "Embeddable";                             //$NON-NLS-1$

  String TSLIB_ICONID_INFO    = "dialog-information"; //$NON-NLS-1$
  String TSLIB_ICONID_WARNING = "dialog-warning";     //$NON-NLS-1$
  String TSLIB_ICONID_ERROR   = "dialog-error";       //$NON-NLS-1$

}
