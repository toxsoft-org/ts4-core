package org.toxsoft.core.tsgui.valed.controls.enums;

import org.toxsoft.core.tsgui.utils.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link AbstractValedEnum}
   */
  String FMT_ERR_CLASS_NOT_VIS_PROVIDER = "%s: класс %s не является " + ITsVisualsProvider.class.getSimpleName();

  /**
   * {@link IValedEnumConstants}
   */
  String FMT_ERR_CLASS_NOT_ENUM           = "Класс %s не является enum-перечислением";
  String MSG_ERR_ENUM_CLASS_NOT_SPECIFIED = "Не задан класс enum-константы";
  String FMT_ERR_ENUM_CLASS_NOT_FOUND     = "Невозможно найти класс enum-константы %s";
  String FMT_ERR_EMPTY_ENUM               = "enum-перечисление %s не содержит ни одной константы";

}
