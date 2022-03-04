package org.toxsoft.core.tslib.utils.diff;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String STR_N_DN_NONE  = "Отсутствует";
  String STR_D_DN_NONE  = "Запрошенный элемент вообще отсутствует в сравнении";
  String STR_N_DN_LEFT  = "Слева";
  String STR_D_DN_LEFT  = "Сравниваемый элемент существует только в левой части";
  String STR_N_DN_RIGHT = "Справа";
  String STR_D_DN_RIGHT = "Сравниваемый элемент существует только в правой части";
  String STR_N_DN_EQUAL = "Одинаковы";
  String STR_D_DN_EQUAL = "Сравниваемые элементы одинаковые, нет различии";
  String STR_N_DN_DIFF  = "Отличются";
  String STR_D_DN_DIFF  = "Элементы с разных сторон отличаются друг от друга";

}
