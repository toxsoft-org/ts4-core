package org.toxsoft.core.tslib.utils.plugins.impl;

import org.toxsoft.core.tslib.utils.plugins.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ITsResources {

  /**
   * {@link ChangedPluginsInfo}
   */
  String ERR_STATE_TABLE_UNSOLVED = Messages.getString( "ERR_STATE_TABLE_UNSOLVED" ); //$NON-NLS-1$

  /**
   * {@link PluginUtils}
   */
  String ERR_NO_MANIFEST = Messages.getString( "ERR_NO_MANIFEST" ); //$NON-NLS-1$

  String ERR_NOT_PLUGIN_CONTAINER = Messages.getString( "ERR_NOT_PLUGIN_CONTAINER" ) //$NON-NLS-1$
      + IPluginsHardConstants.MF_MAIN_ATTR_PLUGIN_CONTAINER_VERSION
      + Messages.getString( "ERR_NOT_PLUGIN_CONTAINER___1" ); //$NON-NLS-1$

  String ERR_INV_CONTAINER_VERSION_FORMAT = Messages.getString( "ERR_INV_CONTAINER_VERSION_FORMAT" ) //$NON-NLS-1$
      + IPluginsHardConstants.MF_MAIN_ATTR_PLUGIN_CONTAINER_VERSION
      + Messages.getString( "ERR_INV_CONTAINER_VERSION_FORMAT___1" ); //$NON-NLS-1$

  String ERR_INV_CONTAINER_VERSION = Messages.getString( "ERR_INV_CONTAINER_VERSION" ); //$NON-NLS-1$

  String ERR_INCOPLETE_PLUGIN_INFO_SECTION = Messages.getString( "ERR_INCOPLETE_PLUGIN_INFO_SECTION" ); //$NON-NLS-1$

  String ERR_PLUGIN_ID_NOT_ID_PATH   = Messages.getString( "ERR_PLUGIN_ID_NOT_ID_PATH" );   //$NON-NLS-1$
  String ERR_PLUGIN_TYPE_NOT_ID_PATH = Messages.getString( "ERR_PLUGIN_TYPE_NOT_ID_PATH" ); //$NON-NLS-1$
  String ERR_INV_PLUGIN_VERSION      = Messages.getString( "ERR_INV_PLUGIN_VERSION" );      //$NON-NLS-1$

  /**
   * {@link PluginStorage}
   */
  String MSG_CREATE_DIR                = "Created dir: %s";                                     //$NON-NLS-1$
  String ERR_CANT_CREATE_PLUGIN_OBJECT = Messages.getString( "ERR_CANT_CREATE_PLUGIN_OBJECT" ); //$NON-NLS-1$

  String ERR_CANT_RESOLVE_DEPENDENCE_TYPE = Messages.getString( "ERR_CANT_RESOLVE_DEPENDENCE_TYPE" ); //$NON-NLS-1$

  String ERR_FOR_DEPENDENCE                = Messages.getString( "ERR_FOR_DEPENDENCE" );                       //$NON-NLS-1$
  String ERR_EXACT_VERSION_NUMBER          = Messages.getString( "ERR_EXACT_VERSION_NUMBER" );                 //$NON-NLS-1$
  String ERR_NEED_NEWER_VERSION_NUMBER     = Messages.getString( "ERR_NEED_NEWER_VERSION_NUMBER" );            //$NON-NLS-1$
  String ERR_AVAILABLE_VERSION_NUMBER      = Messages.getString( "ERR_AVAILABLE_VERSION_NUMBER" );             //$NON-NLS-1$
  String ERR_CANT_RESOLVE_DEPENDENCE_ID    = Messages.getString( "ERR_CANT_RESOLVE_DEPENDENCE_ID" );           //$NON-NLS-1$
  String ERR_CANT_CREATE_DIR               = "Can't create directory %s.";                                     //$NON-NLS-1$
  String ERR_CANT_CREATE_DIR_NAME_CONFLICT = "Can't create directory %s. There is a file with the same name!"; //$NON-NLS-1$
  String ERR_CANT_REMOVE_TEMPORARY_FILE    = "Can't remove plugin temporary file %s.";                         //$NON-NLS-1$
}
