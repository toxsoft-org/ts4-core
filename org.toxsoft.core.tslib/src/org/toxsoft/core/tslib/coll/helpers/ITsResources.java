package org.toxsoft.core.tslib.coll.helpers;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link CollConstraint}
   */
  String FMT_MSG_NON_EXACT_SIZE = "Количество элементов %d должно быть равно %d";
  String FMT_MSG_TOO_BIG_SIZE   = "Количество элементов %d не должно первышать %d";
  String MSG_MSG_COLL_IS_EMPTY  = "Должен присутствовать хотя бы один элемент";
  String FMT_MSG_DUP_ELEMS      = "Повторяющейся элементы (например, ## %d, %d) не допустимы";

  /**
   * {@link ECrudOp}
   */
  String STR_N_CREATE = "Create";
  String STR_D_CREATE = "Add new element";
  String STR_N_EDIT   = "Edit";
  String STR_D_EDIT   = "Change an existing element";
  String STR_N_REMOVE = "Delete";
  String STR_D_REMOVE = "Remove an existing element";
  String STR_N_LIST   = "List";
  String STR_D_LIST   = "List elements, change two or more elements at once ";

  /**
   * {@link ETsCollMove}
   */
  String STR_N_TCM_NONE      = "На месте";
  String STR_D_TCM_NONE      = "Остаться на месте";
  String STR_N_TCM_FIRST     = "В начало";
  String STR_D_TCM_FIRST     = "Перейти в начало (наименьшее значение)";
  String STR_N_TCM_MIDDLE    = "В середину";
  String STR_D_TCM_MIDDLE    = "Перейти в середину (среднее значение)";
  String STR_N_TCM_LAST      = "В конец";
  String STR_D_TCM_LAST      = "Перейти в конец (наибольшее значение)";
  String STR_N_TCM_PREV      = "Предыдущий";
  String STR_D_TCM_PREV      = "Шаг назад на один элемент (уменьшить на малую величину)";
  String STR_N_TCM_NEXT      = "Следующий";
  String STR_D_TCM_NEXT      = "Шаг вперед на один элемент (увеличить на малую величину)";
  String STR_N_TCM_JUMP_PREV = "Скачок назад";
  String STR_D_TCM_JUMP_PREV = "Скачок (страница) назад (уменьшить на большую величину)";
  String STR_N_TCM_JUMP_NEXT = "Скачок вперед";
  String STR_D_TCM_JUMP_NEXT = "Скачок (страница) вперед (увеличить на большую величину)";

}
