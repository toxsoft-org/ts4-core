package org.toxsoft.core.tslib.utils.plugins.impl;

import org.toxsoft.core.tslib.utils.plugins.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String FMT_PARENT_CLASSLOADER_NAME = "%s parent";

  /**
   * {@link ChangedPluginsInfo}
   */
  String ERR_STATE_TABLE_UNSOLVED = Messages.getString( "ERR_STATE_TABLE_UNSOLVED" );

  /**
   * {@link PluginUtils}
   */
  String ERR_NO_MANIFEST = Messages.getString( "ERR_NO_MANIFEST" );

  String ERR_INV_CONTAINER_VERSION_FORMAT = Messages.getString( "ERR_INV_CONTAINER_VERSION_FORMAT" )
      + IPluginsHardConstants.MF_MAIN_ATTR_PLUGIN_CONTAINER_VERSION
      + Messages.getString( "ERR_INV_CONTAINER_VERSION_FORMAT___1" );

  String ERR_INV_CONTAINER_VERSION = Messages.getString( "ERR_INV_CONTAINER_VERSION" );

  String ERR_INCOPLETE_PLUGIN_INFO_SECTION = Messages.getString( "ERR_INCOPLETE_PLUGIN_INFO_SECTION" );

  String ERR_PLUGIN_ID_NOT_ID_PATH   = Messages.getString( "ERR_PLUGIN_ID_NOT_ID_PATH" );
  String ERR_PLUGIN_TYPE_NOT_ID_PATH = Messages.getString( "ERR_PLUGIN_TYPE_NOT_ID_PATH" );
  String ERR_INV_PLUGIN_VERSION      = Messages.getString( "ERR_INV_PLUGIN_VERSION" );

  /**
   * {@link PluginStorage}
   */
  String MSG_CREATE_DIR                = "Created dir: %s";
  String MSG_DEREGISTER_PLUGIN         = "deregisterPluginInfo(...): pluginId = %s";
  String MSG_DEREGISTER_DEPENDENCY     = "deregisterPluginInfo(...): %s. deregister plugin dependency: %s";
  String ERR_CANT_CREATE_PLUGIN_OBJECT = Messages.getString( "ERR_CANT_CREATE_PLUGIN_OBJECT" );

  String ERR_DEPENDENCY_CANT_RESOLVE     = Messages.getString( "ERR_DEPENDENCY_CANT_RESOLVE" );
  String ERR_DEPENDENCY_INACCURATE_MATCH = Messages.getString( "ERR_DEPENDENCY_INACCURATE_MATCH" );
  String ERR_DEPENDENCY_VERSION_MISMATCH = Messages.getString( "ERR_DEPENDENCY_VERSION_MISMATCH" );

  String ERR_CANT_CREATE_DIR               = "Can't create directory %s.";
  String ERR_CANT_CREATE_DIR_NAME_CONFLICT = "Can't create directory %s. There is a file with the same name!";
  String ERR_NOT_FOUND_TEMPORARY_FILE      = "Plugin temporary file is not found: %s.";
  String ERR_CANT_REMOVE_TEMPORARY_FILE    = "Can't remove plugin temporary file %s.";
  String ERR_PLUGIN_NOT_FOUND              = "plugin %s is not found";

  /**
   * {@link PluginBox}
   */
  String MSG_PLUGIN_LOAD_ORDER = "plugins(%d) will be loaded in the following order:\n%s";
}
