package org.toxsoft.core.tslib.bricks.strid.impl;

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

  String ERR_MSG_EMPTY_ID_STRING        = "Strid must not be empty";
  String ERR_MSG_INV_ID_STRING_START    = "Strid starts from invalid character '%c'";
  String ERR_MSG_INV_ID_NAME_PART       = "IDname contains invalid character '%c'";
  String ERR_MSG_ILLEGAL_LAST_DOT       = "IDpath must not end with dot (.)";
  String ERR_MSG_DUPLICATED_DOTS        = "IDpath must not contain two consecutive dots (..)";
  String ERR_MSG_INV_ID_PATH_PART       = "IDpath contains invalid character '%c'";
  String ERR_MSG_INV_ID_PATH_PART_START = "IDpath component starts with invalid character '%c'";
  String MSG_ERR_INVALID_ARG_FOR_ID2STR = "Argument is not valid string encoded in IDpath";

  // ------------------------------------------------------------------------------------
  // ru.RU
  //

  // String ERR_MSG_EMPTY_ID_STRING = "Строковый идентификатор не может быть пустой строкой";
  // String ERR_MSG_INV_ID_STRING_START = "Строковый идентификатор начинается с недопустимого символа '%c'";
  // String ERR_MSG_INV_ID_NAME_PART = "ИД-имя содержит недопустимый символ '%c'";
  // String ERR_MSG_ILLEGAL_LAST_DOT = "Ид-путь не может заканчиваться точкой";
  // String ERR_MSG_DUPLICATED_DOTS = "Ид-путь не может содержать две точки подряд";
  // String ERR_MSG_INV_ID_PATH_PART = "ИД-путь содержит недопустимый символ '%c'";
  // String ERR_MSG_INV_ID_PATH_PART_START = "Часть ИД-пути не может начинаться с символа '%c'";
  // String MSG_ERR_INVALID_ARG_FOR_ID2STR = "Аргумент не является закодированной в ИД-пути текстовой строкой";

}
