package org.toxsoft.core.tslib.utils.icons;

/**
 * Icon IDs for entities defined in TSLIB.
 * <p>
 * TSLIB itself is not a GUI library. However some entities from library are assumed to have GUI representation when
 * used in GUI environment. This interface lists icon IDs for such entities. TSLIB does <b>not</b> contains icons
 * itself, just IDs.
 * <p>
 * Note: TSGUI library must have matching icons!
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ITsLibIconIds {

  String TSLIB_ICONID_INFO    = "dialog-information"; //$NON-NLS-1$
  String TSLIB_ICONID_WARNING = "dialog-warning";     //$NON-NLS-1$
  String TSLIB_ICONID_ERROR   = "dialog-error";       //$NON-NLS-1$

}
