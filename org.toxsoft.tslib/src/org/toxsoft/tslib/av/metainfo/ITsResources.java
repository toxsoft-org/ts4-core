package org.toxsoft.tslib.av.metainfo;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  // ------------------------------------------------------------------------------------
  // en.EN
  //

  String STR_DEF_NAME                = "<<enter the name>>";
  String STR_N_ID                    = "Identifier";
  String STR_D_ID                    = "Unique string identifier (IDpath)";
  String STR_N_NAME                  = "Name";
  String STR_D_NAME                  = "Short human-readable name";
  String STR_N_DESCRIPTION           = "Description";
  String STR_D_DESCRIPTION           = "Descriptiove human-readable text";
  String STR_N_DEFAULT_VALUE         = "Default";
  String STR_D_DEFAULT_VALUE         = "Default value";
  String STR_N_IS_MANDATORY          = "Mandatory?";
  String STR_D_IS_MANDATORY          = "Mandatory option flag";
  String STR_N_IDNAME                = "IDname";
  String STR_D_IDNAME                = "String indetifier of format IDname";
  String STR_N_IDPATH                = "IDpath";
  String STR_D_IDPATH                = "String indetifier of format IDpath";
  String STR_N_TS_BOOL               = "Логический";
  String STR_D_TS_BOOL               = "Логический тип с отображением галочкой";
  String STR_N_MAX_INCLUSIVE         = "MaxIncl";
  String STR_D_MAX_INCLUSIVE         = "Maximal allowed value (inclusive)";
  String STR_N_MIN_INCLUSIVE         = "MinIncl";
  String STR_D_MIN_INCLUSIVE         = "Minimal allowed value (inclusive)";
  String STR_N_MAX_EXCLUSIVE         = "MaxExcl";
  String STR_D_MAX_EXCLUSIVE         = "Maximal allowed value (exclusive)";
  String STR_N_MIN_EXCLUSIVE         = "MinExcl";
  String STR_D_MIN_EXCLUSIVE         = "Minimal allowed value (exclusive)";
  String FMT_ERR_NO_MANDATORY_OPTION = "Mandatory option %s is not specified";

  String FMT_ERR_NO_REGISTERED_DATA_DEF    = "Data defnition with ID '%s': not found";
  String FMT_ERR_PARENT_DATA_DEF_AT_CHANGE = "Data defnition with ID '%s': can not change type from parent's %s to %s";
  String FMT_ERR_DATA_DEF_AT_CHANGE        = "Data defnition with ID '%s': can not change type from %s to %s";
  String FMT_ERR_DATA_DEF_IS_ALREADY       = "Data defnition with ID '%s': is already registered";

  // ------------------------------------------------------------------------------------
  // ru.RU
  //

  // String STR_DEF_NAME = "<<введите название>>";
  // String STR_N_NAME = "Name";
  // String STR_D_NAME = "Short human-readable name";
  // String STR_N_DESCRIPTION = "Description";
  // String STR_D_DESCRIPTION = "Descriptiove human-readable text";
  // String STR_N_IDNAME = "ИД-имя";
  // String STR_D_IDNAME = "Идентификатор в формате ИД-имени";
  // String STR_N_IDPATH = "ИД-путь";
  // String STR_D_IDPATH = "Идентификатор в формате ИД-пути";
  // String FMT_ERR_NO_MANDATORY_OPTION = "Не задана объязательная опция %s";

}
