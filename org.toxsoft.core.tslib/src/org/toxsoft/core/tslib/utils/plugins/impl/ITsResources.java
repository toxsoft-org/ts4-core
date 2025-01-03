package org.toxsoft.core.tslib.utils.plugins.impl;

import org.toxsoft.core.tslib.utils.plugins.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ITsResources {

  String FMT_PARENT_CLASSLOADER_NAME = "%s parent"; //$NON-NLS-1$

  /**
   * {@link ChangedPluginsInfo}
   */
  String ERR_STATE_TABLE_UNSOLVED = Messages.getString( "ERR_STATE_TABLE_UNSOLVED" ); //$NON-NLS-1$

  /**
   * {@link PluginUtils}
   */
  String ERR_NO_MANIFEST = Messages.getString( "ERR_NO_MANIFEST" ); //$NON-NLS-1$

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
  String MSG_CREATE_DIR                = "Created dir: %s";                                                 //$NON-NLS-1$
  String MSG_DEREGISTER_PLUGIN         = "deregisterPluginInfo(...): pluginId = %s";                        //$NON-NLS-1$
  String MSG_DEREGISTER_DEPENDENCY     = "deregisterPluginInfo(...): %s. deregister plugin dependency: %s"; //$NON-NLS-1$
  String ERR_CANT_CREATE_PLUGIN_OBJECT = Messages.getString( "ERR_CANT_CREATE_PLUGIN_OBJECT" );             //$NON-NLS-1$

  String ERR_DEPENDENCY_CANT_RESOLVE     = Messages.getString( "ERR_DEPENDENCY_CANT_RESOLVE" );     //$NON-NLS-1$
  String ERR_DEPENDENCY_INACCURATE_MATCH = Messages.getString( "ERR_DEPENDENCY_INACCURATE_MATCH" ); //$NON-NLS-1$
  String ERR_DEPENDENCY_VERSION_MISMATCH = Messages.getString( "ERR_DEPENDENCY_VERSION_MISMATCH" ); //$NON-NLS-1$

  String ERR_CANT_CREATE_DIR               = "Can't create directory %s.";                                     //$NON-NLS-1$
  String ERR_CANT_CREATE_DIR_NAME_CONFLICT = "Can't create directory %s. There is a file with the same name!"; //$NON-NLS-1$
  String ERR_NOT_FOUND_TEMPORARY_FILE      = "Plugin temporary file is not found: %s.";                        //$NON-NLS-1$
  String ERR_CANT_REMOVE_TEMPORARY_FILE    = "Can't remove plugin temporary file %s.";                         //$NON-NLS-1$
}
