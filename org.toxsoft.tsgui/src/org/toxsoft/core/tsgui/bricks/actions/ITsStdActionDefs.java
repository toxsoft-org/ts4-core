package org.toxsoft.core.tsgui.bricks.actions;

import static org.toxsoft.core.tsgui.bricks.actions.ITsResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;

import org.toxsoft.core.tslib.ITsHardConstants;

/**
 * Стандартные действия.
 *
 * @author hazard157
 */
public interface ITsStdActionDefs {

  /**
   * Префикс идентификаторов стандартных действий.
   */
  String STD_ACTION_DEF_PREFIX = ITsHardConstants.TS_ID + ".stdact"; //$NON-NLS-1$

  /**
   * Идентификатор действия: разделитель и он вообще не действие :).
   */
  String ACTID_SEPARATOR = STD_ACTION_DEF_PREFIX + ".separator"; //$NON-NLS-1$

  /**
   * Идентификатор действия: добавить элемент.
   */
  String ACTID_ADD = STD_ACTION_DEF_PREFIX + ".add"; //$NON-NLS-1$

  /**
   * Идентификатор действия: добавить все доступные элементы.
   */
  String ACTID_ADD_ALL = STD_ACTION_DEF_PREFIX + ".add_all"; //$NON-NLS-1$

  /**
   * Идентификатор действия: редактировать элемент.
   */
  String ACTID_EDIT = STD_ACTION_DEF_PREFIX + ".edit"; //$NON-NLS-1$

  /**
   * Идентификатор действия: удалить элемент.
   */
  String ACTID_REMOVE = STD_ACTION_DEF_PREFIX + ".remove"; //$NON-NLS-1$

  /**
   * Идентификатор действия: очистить набор, коллекцию, документ, выделение и др.
   */
  String ACTID_CLEAR = STD_ACTION_DEF_PREFIX + ".clear"; //$NON-NLS-1$

  /**
   * Идентификатор действия: вывести диалог детальной информации об элементе.
   */
  String ACTID_INFO = STD_ACTION_DEF_PREFIX + ".info"; //$NON-NLS-1$

  /**
   * Идентификатор действия: скрыть / показать панель детальной информации.
   */
  String ACTID_HIDE_FILTER = STD_ACTION_DEF_PREFIX + ".hide_filter"; //$NON-NLS-1$

  /**
   * Идентификатор действия: скрыть / показать панель детальной информации.
   */
  String ACTID_HIDE_DETAILS = STD_ACTION_DEF_PREFIX + ".hide_details"; //$NON-NLS-1$

  /**
   * Идентификатор действия: скрыть / показать панель суммарной информации.
   */
  String ACTID_HIDE_SUMMARY = STD_ACTION_DEF_PREFIX + ".hide_summary"; //$NON-NLS-1$

  /**
   * Идентификатор действия: проверить корректность.
   */
  String ACTID_VALIDATE = STD_ACTION_DEF_PREFIX + ".validate"; //$NON-NLS-1$

  /**
   * Идентификатор действия: обновить перечень элементов.
   */
  String ACTID_REFRESH = STD_ACTION_DEF_PREFIX + ".refresh"; //$NON-NLS-1$

  /**
   * Идентификатор действия: раскрыть текущий элемент.
   */
  String ACTID_EXPAND = STD_ACTION_DEF_PREFIX + ".expand"; //$NON-NLS-1$

  /**
   * Идентификатор действия: раскрыть все элементы.
   */
  String ACTID_EXPAND_ALL = STD_ACTION_DEF_PREFIX + ".expand_all"; //$NON-NLS-1$

  /**
   * Идентификатор действия: схлопнуть текущий элемент.
   */
  String ACTID_COLLAPSE = STD_ACTION_DEF_PREFIX + ".collapse"; //$NON-NLS-1$

  /**
   * Идентификатор действия: схлопнуть все элементы.
   */
  String ACTID_COLLAPSE_ALL = STD_ACTION_DEF_PREFIX + ".collapse_all"; //$NON-NLS-1$

  /**
   * Идентификатор действия: отфильтровать список.
   */
  String ACTID_FILTER = STD_ACTION_DEF_PREFIX + ".filter"; //$NON-NLS-1$

  /**
   * Идентификатор действия: отсортировать и/или настроить параметры сортировки.
   */
  String ACTID_SORT = STD_ACTION_DEF_PREFIX + ".sort"; //$NON-NLS-1$

  /**
   * Идентификатор действия: отсортировать список по возрастанию.
   */
  String ACTID_SORT_ASCENDING = STD_ACTION_DEF_PREFIX + ".sort_asc"; //$NON-NLS-1$

  /**
   * Идентификатор действия: отсортировать список по убыванию.
   */
  String ACTID_SORT_DESCENDING = STD_ACTION_DEF_PREFIX + ".sort_desc"; //$NON-NLS-1$

  /**
   * Идентификатор действия: отметить все элементы набора.
   */
  String ACTID_CHECK_ALL = STD_ACTION_DEF_PREFIX + ".check_all"; //$NON-NLS-1$

  /**
   * Идентификатор действия: убрать отметки со всех элементов набора.
   */
  String ACTID_UNCHECK_ALL = STD_ACTION_DEF_PREFIX + ".uncheck_all"; //$NON-NLS-1$

  /**
   * Идентификатор действия: отметить чатсть, группу элементов набора.
   */
  String ACTID_CHECK_GROUP = STD_ACTION_DEF_PREFIX + ".check_group"; //$NON-NLS-1$

  /**
   * Идентификатор действия: убрать отметки с части, группы все элементов набора.
   */
  String ACTID_UNCHECK_GROUP = STD_ACTION_DEF_PREFIX + ".uncheck_group"; //$NON-NLS-1$

  /**
   * Идентификатор действия: инвертировать отметки элементов набора.
   */
  String ACTID_INVERT_CHECK = STD_ACTION_DEF_PREFIX + ".invert_check"; //$NON-NLS-1$

  /**
   * Идентификатор действия: создать новый документ.
   */
  String ACTID_CREATE_NEW = STD_ACTION_DEF_PREFIX + ".create_new"; //$NON-NLS-1$

  /**
   * Идентификатор действия: открыть файл.
   */
  String ACTID_OPEN = STD_ACTION_DEF_PREFIX + ".open"; //$NON-NLS-1$

  /**
   * Идентификатор действия: сохранить в файл.
   */
  String ACTID_SAVE = STD_ACTION_DEF_PREFIX + ".save"; //$NON-NLS-1$

  /**
   * Идентификатор действия: сохранить в новый файл.
   */
  String ACTID_SAVE_AS = STD_ACTION_DEF_PREFIX + ".save_as"; //$NON-NLS-1$

  /**
   * Идентификатор действия: завершение работы программы.
   */
  String ACTID_QUIT = STD_ACTION_DEF_PREFIX + ".quit"; //$NON-NLS-1$

  /**
   * Идентификатор действия: показ информации о программе.
   */
  String ACTID_ABOUT = STD_ACTION_DEF_PREFIX + ".about"; //$NON-NLS-1$

  /**
   * Идентификатор действия: изначальный масштаб (1:1).
   */
  String ACTID_ZOOM_ORIGINAL = STD_ACTION_DEF_PREFIX + ".zoom_orig"; //$NON-NLS-1$

  /**
   * Идентификатор действия: вместить все.
   */
  String ACTID_ZOOM_FIT_BEST = STD_ACTION_DEF_PREFIX + ".zoom_fit_best"; //$NON-NLS-1$

  /**
   * Идентификатор действия: вместить в высоту.
   */
  String ACTID_ZOOM_FIT_HEIGHT = STD_ACTION_DEF_PREFIX + ".zoom_fit_height"; //$NON-NLS-1$

  /**
   * Идентификатор действия: вместить в ширину.
   */
  String ACTID_ZOOM_FIT_WIDTH = STD_ACTION_DEF_PREFIX + ".zoom_fit_width"; //$NON-NLS-1$

  /**
   * Идентификатор действия: увеличить масштаб.
   */
  String ACTID_ZOOM_IN = STD_ACTION_DEF_PREFIX + ".zoom_in"; //$NON-NLS-1$

  /**
   * Идентификатор действия: уменьшить масштаб.
   */
  String ACTID_ZOOM_OUT = STD_ACTION_DEF_PREFIX + ".zoom_out"; //$NON-NLS-1$

  /**
   * Идентификатор действия: переместить элемент вверх по списку.
   */
  String ACTID_LIST_ELEM_MOVE_UP = STD_ACTION_DEF_PREFIX + ".list_elem_move_up"; //$NON-NLS-1$

  /**
   * Идентификатор действия: переместить элемент вниз по списку.
   */
  String ACTID_LIST_ELEM_MOVE_DOWN = STD_ACTION_DEF_PREFIX + ".list_elem_move_down"; //$NON-NLS-1$

  /**
   * Идентификатор действия: переместить элемент в начало списка.
   */
  String ACTID_LIST_ELEM_MOVE_FIRST = STD_ACTION_DEF_PREFIX + ".list_elem_move_first"; //$NON-NLS-1$

  /**
   * Идентификатор действия: переместить элемент в конец списка.
   */
  String ACTID_LIST_ELEM_MOVE_LAST = STD_ACTION_DEF_PREFIX + ".list_elem_move_last"; //$NON-NLS-1$

  /**
   * Идентификатор действия: перейти в начало.
   */
  String ACTID_GO_FIRST = STD_ACTION_DEF_PREFIX + ".go_first"; //$NON-NLS-1$

  /**
   * Идентификатор действия: перейти на предыдущий элемент.
   */
  String ACTID_GO_PREV = STD_ACTION_DEF_PREFIX + ".go_prev"; //$NON-NLS-1$

  /**
   * Идентификатор действия: перейти на следующий элемент.
   */
  String ACTID_GO_NEXT = STD_ACTION_DEF_PREFIX + ".go_next"; //$NON-NLS-1$

  /**
   * Идентификатор действия: перейти в конец.
   */
  String ACTID_GO_LAST = STD_ACTION_DEF_PREFIX + ".go_last"; //$NON-NLS-1$

  /**
   * Идентификатор действия: перейти просмотр в виде списка.
   */
  String ACTID_VIEW_AS_LIST = STD_ACTION_DEF_PREFIX + ".view_as_list"; //$NON-NLS-1$

  /**
   * Идентификатор действия: перейти в просмотр в виде дерева.
   */
  String ACTID_VIEW_AS_TREE = STD_ACTION_DEF_PREFIX + ".view_as_tree"; //$NON-NLS-1$

  /**
   * Идентификатор действия: переключаетель просмотра список/дерево.
   */
  String ACTID_TOGGLE_VIEW_AS_TREE_OR_LIST = STD_ACTION_DEF_PREFIX + ".toggle_view_list_tree"; //$NON-NLS-1$

  /**
   * Идентификатор действия: Добавить корневой элемент в дерево.
   */
  String ACTID_TREE_ADD_ROOT = STD_ACTION_DEF_PREFIX + ".tree_add_root"; //$NON-NLS-1$

  /**
   * Идентификатор действия: добавить дочерный элемент к текущему узлу дерева.
   */
  String ACTID_TREE_ADD_CHILD = STD_ACTION_DEF_PREFIX + ".tree_add_child"; //$NON-NLS-1$

  /**
   * Идентификатор действия: отменить действие.
   */
  String ACTID_UNDO = STD_ACTION_DEF_PREFIX + ".undo"; //$NON-NLS-1$

  /**
   * Идентификатор действия: потворить отменённое действие.
   */
  String ACTID_REDO = STD_ACTION_DEF_PREFIX + ".redo"; //$NON-NLS-1$

  /**
   * Идентификатор действия: напечатать документ.
   */
  String ACTID_PRINT = STD_ACTION_DEF_PREFIX + ".print"; //$NON-NLS-1$

  /**
   * Идентификатор действия: предварительный просмотр печати.
   */
  String ACTID_PRINT_PREVIEW = STD_ACTION_DEF_PREFIX + ".print_preview"; //$NON-NLS-1$

  /**
   * Идентификатор действия: добавить элемент создав на основе существующего элемента.
   */
  String ACTID_ADD_COPY = STD_ACTION_DEF_PREFIX + ".add_copy"; //$NON-NLS-1$

  /**
   * Это "псевдо" действие, предназначенное для создания разделителя.
   */
  ITsActionDef ACDEF_SEPARATOR = ofUnspec1( ACTID_SEPARATOR );

  /**
   * Описание действия: завершить работу программы.
   */
  ITsActionDef ACDEF_QUIT = ofPush2( ACTID_QUIT, STR_T_QUIT, STR_P_QUIT, ICONID_APPLICATION_EXIT );

  /**
   * Описание действия: создать новый документ.
   */
  ITsActionDef ACDEF_CREATE_NEW = ofPush2( ACTID_CREATE_NEW, STR_T_CREATE_NEW, STR_P_CREATE_NEW, ICONID_DOCUMENT_NEW );

  /**
   * Описание действия: открыть файл.
   */
  ITsActionDef ACDEF_OPEN = ofPush2( ACTID_OPEN, STR_T_OPEN, STR_P_OPEN, ICONID_DOCUMENT_OPEN );

  /**
   * Описание действия: сохранить в файл.
   */
  ITsActionDef ACDEF_SAVE = ofPush2( ACTID_SAVE, STR_T_SAVE, STR_P_SAVE, ICONID_DOCUMENT_SAVE );

  /**
   * Описание действия: сохранить в новый файл.
   */
  ITsActionDef ACDEF_SAVE_AS = ofPush2( ACTID_SAVE_AS, STR_T_SAVE_AS, STR_P_SAVE_AS, ICONID_DOCUMENT_SAVE_AS );

  /**
   * Описание действия: добавить элемент.
   */
  ITsActionDef ACDEF_ADD = ofPush2( ACTID_ADD, STR_T_ADD, STR_P_ADD, ICONID_LIST_ADD );

  /**
   * Описание действия: добавить элемент создав на основе существующего элемента.
   */
  ITsActionDef ACDEF_ADD_COPY = ofPush2( ACTID_ADD_COPY, STR_T_ADD_COPY, STR_P_ADD_COPY, ICONID_LIST_ADD_COPY );

  /**
   * Описание действия: добавить все доступные элементы.
   */
  ITsActionDef ACDEF_ADD_ALL = ofPush2( ACTID_ADD_ALL, STR_T_ADD_ALL, STR_P_ADD_ALL, ICONID_LIST_ADD_ALL );

  /**
   * Описание действия: редактировать элемент.
   */
  ITsActionDef ACDEF_EDIT = ofPush2( ACTID_EDIT, STR_T_EDIT, STR_P_EDIT, ICONID_DOCUMENT_EDIT );

  /**
   * Описание действия: удалить элемент.
   */
  ITsActionDef ACDEF_REMOVE = ofPush2( ACTID_REMOVE, STR_T_REMOVE, STR_P_REMOVE, ICONID_LIST_REMOVE );

  /**
   * Описание действия: очистить набор, коллекцию.
   */
  ITsActionDef ACDEF_CLEAR = ofPush2( ACTID_CLEAR, STR_T_CLEAR, STR_P_CLEAR, ICONID_EDIT_CLEAR );

  /**
   * Описание действия: вывести диалог детальной информации об элементе.
   */
  ITsActionDef ACDEF_INFO = ofPush2( ACTID_INFO, STR_T_INFO, STR_P_INFO, ICONID_DIALOG_INFORMATION );

  /**
   * Описание действия: вывести диалог детальной информации об элементе (фиксируемая кнопка).
   */
  ITsActionDef ACDEF_INFO_CHECK = ofCheck2( ACTID_INFO, STR_T_INFO, STR_P_INFO, ICONID_DIALOG_INFORMATION );

  /**
   * Описание действия: скрыть / показать панель фильтрации.
   */
  ITsActionDef ACDEF_HIDE_FILTERS =
      ofCheck2( ACTID_HIDE_FILTER, STR_T_HIDE_FILTER, STR_P_HIDE_FILTER, ICONID_HIDE_FILTER_PANE );

  /**
   * Описание действия: скрыть / показать панель детальной информации.
   */
  ITsActionDef ACDEF_HIDE_DETAILS =
      ofCheck2( ACTID_HIDE_DETAILS, STR_T_HIDE_DETAILS, STR_P_HIDE_DETAILS, ICONID_HIDE_DETAILS_PANE );

  /**
   * Описание действия: скрыть / показать панель детальной информации.
   */
  ITsActionDef ACDEF_HIDE_SUMMARY =
      ofCheck2( ACTID_HIDE_SUMMARY, STR_T_HIDE_SUMMARY, STR_P_HIDE_SUMMARY, ICONID_HIDE_SUMMARY_PANE );

  /**
   * Описание действия: проверить корректность.
   */
  ITsActionDef ACDEF_VALIDATE = ofPush2( ACTID_VALIDATE, STR_T_VALIDATE, STR_P_VALIDATE, ICONID_DIALOG_OK );

  /**
   * Описание действия: обновить перечень элементов.
   */
  ITsActionDef ACDEF_REFRESH = ofPush2( ACTID_REFRESH, STR_T_REFRESH, STR_P_REFRESH, ICONID_VIEW_REFRESH );

  /**
   * Описание действия: развернуть текущий элемент.
   */
  ITsActionDef ACDEF_EXPAND = ofPush2( ACTID_EXPAND, STR_T_EXPAND, STR_P_EXPAND, ICONID_VIEW_EXPAND );

  /**
   * Описание действия: развернуть все элементы.
   */
  ITsActionDef ACDEF_EXPAND_ALL =
      ofPush2( ACTID_EXPAND_ALL, STR_T_EXPAND_ALL, STR_P_EXPAND_ALL, ICONID_VIEW_EXPAND_ALL );

  /**
   * Описание действия: свернуть текущий элемент.
   */
  ITsActionDef ACDEF_COLLAPSE = ofPush2( ACTID_COLLAPSE, STR_T_COLLAPSE, STR_P_COLLAPSE, ICONID_VIEW_COLLAPSE );

  /**
   * Описание действия: свернуть все элементы.
   */
  ITsActionDef ACDEF_COLLAPSE_ALL =
      ofPush2( ACTID_COLLAPSE_ALL, STR_T_COLLAPSE_ALL, STR_P_COLLAPSE_ALL, ICONID_VIEW_COLLAPSE_ALL );

  /**
   * Описание действия: отфильтровать список.
   */
  ITsActionDef ACDEF_FILTER = ofPush2( ACTID_FILTER, STR_T_FILTER, STR_P_FILTER, ICONID_VIEW_FILTER );

  /**
   * Описание действия: отсортировать и/или настроить параметры сортировки.
   */
  ITsActionDef ACDEF_SORT = ofPush2( ACTID_SORT, STR_T_SORT, STR_P_SORT, ICONID_VIEW_SORT );

  /**
   * Описание действия: отсортировать список по возрастанию.
   */
  ITsActionDef ACDEF_SORT_ASCENDING =
      ofPush2( ACTID_SORT_ASCENDING, STR_T_SORT_ASCENDING, STR_P_SORT_ASCENDING, ICONID_VIEW_SORT_ASCENDING );

  /**
   * Описание действия: отсортировать список по убыванию.
   */
  ITsActionDef ACDEF_SORT_DESCENDING =
      ofPush2( ACTID_SORT_DESCENDING, STR_T_SORT_DESCENDING, STR_P_SORT_DESCENDING, ICONID_VIEW_SORT_DESCENDING );

  /**
   * Описание действия: отметить все элементы набора.
   */
  ITsActionDef ACDEF_CHECK_ALL = ofPush2( ACTID_CHECK_ALL, STR_T_CHECK_ALL, STR_P_CHECK_ALL, ICONID_ITEMS_CHECK_ALL );

  /**
   * Описание действия: убрать отметки со всех элементов набора.
   */
  ITsActionDef ACDEF_UNCHECK_ALL =
      ofPush2( ACTID_UNCHECK_ALL, STR_T_UNCHECK_ALL, STR_P_UNCHECK_ALL, ICONID_ITEMS_UNCHECK_ALL );

  /**
   * Описание действия: отметить чатсть, группу элементов набора.
   */
  ITsActionDef ACDEF_CHECK_GROUP =
      ofPush2( ACTID_CHECK_GROUP, STR_T_CHECK_GROUP, STR_P_CHECK_GROUP, ICONID_ITEMS_CHECK_GROUP );

  /**
   * Описание действия: убрать отметки с части, группы все элементов набора.
   */
  ITsActionDef ACDEF_UNCHECK_GROUP =
      ofPush2( ACTID_UNCHECK_GROUP, STR_T_UNCHECK_GROUP, STR_P_UNCHECK_GROUP, ICONID_ITEMS_UNCHECK_GROUP );

  /**
   * Описание действия: инвертировать отметки элементов набора.
   */
  ITsActionDef ACDEF_INVERT_CHECK =
      ofPush2( ACTID_INVERT_CHECK, STR_T_INVERT_CHECK, STR_P_INVERT_CHECK, ICONID_ITEMS_INVERT_CHECK );

  /**
   * Описание действия: показ информации о программе.
   */
  ITsActionDef ACDEF_ABOUT = ofPush2( ACTID_ABOUT, STR_T_ABOUT, STR_P_ABOUT, null );

  /**
   * Описание действия: вместить все.
   */
  ITsActionDef ACDEF_ZOOM_FIT_BEST =
      ofCheck2( ACTID_ZOOM_FIT_BEST, STR_T_ZOOM_FIT_BEST, STR_P_ZOOM_FIT_BEST, ICONID_ZOOM_FIT_BEST );

  /**
   * Описание действия: вместить все, действие в виде кнопки, переключателя как {@link #ACTID_ZOOM_FIT_BEST}.
   */
  ITsActionDef ACDEF_ZOOM_FIT_BEST_PUSHBUTTON =
      ofPush2( ACTID_ZOOM_FIT_BEST, STR_T_ZOOM_FIT_BEST, STR_P_ZOOM_FIT_BEST, ICONID_ZOOM_FIT_BEST );

  /**
   * Описание действия: вместить в ширину.
   */
  ITsActionDef ACDEF_ZOOM_FIT_WIDTH =
      ofCheck2( ACTID_ZOOM_FIT_WIDTH, STR_T_ZOOM_FIT_WIDTH, STR_P_ZOOM_FIT_WIDTH, ICONID_ZOOM_FIT_WIDTH );

  /**
   * Описание действия: вместить в ширину, действие в виде кнопки, переключателя как {@link #ACTID_ZOOM_FIT_WIDTH}.
   */
  ITsActionDef ACDEF_ZOOM_FIT_WIDTH_PUSHBUTTON =
      ofPush2( ACTID_ZOOM_FIT_WIDTH, STR_T_ZOOM_FIT_WIDTH, STR_P_ZOOM_FIT_WIDTH, ICONID_ZOOM_FIT_WIDTH );

  /**
   * Описание действия: вместить в высоту.
   */
  ITsActionDef ACDEF_ZOOM_FIT_HEIGHT =
      ofCheck2( ACTID_ZOOM_FIT_HEIGHT, STR_T_ZOOM_FIT_HEIGHT, STR_P_ZOOM_FIT_HEIGHT, ICONID_ZOOM_FIT_HEIGHT );

  /**
   * Описание действия: вместить в высоту, действие в виде кнопки, переключателя как {@link #ACTID_ZOOM_FIT_HEIGHT}.
   */
  ITsActionDef ACDEF_ZOOM_FIT_HEIGHT_PUSHBUTTON =
      ofPush2( ACTID_ZOOM_FIT_HEIGHT, STR_T_ZOOM_FIT_HEIGHT, STR_P_ZOOM_FIT_HEIGHT, ICONID_ZOOM_FIT_HEIGHT );

  /**
   * Описание действия: увеличить масштаб.
   */
  ITsActionDef ACDEF_ZOOM_IN = ofPush2( ACTID_ZOOM_IN, STR_T_ZOOM_IN, STR_P_ZOOM_IN, ICONID_ZOOM_IN );

  /**
   * Описание действия: уменьшить масштаб.
   */
  ITsActionDef ACDEF_ZOOM_OUT = ofPush2( ACTID_ZOOM_OUT, STR_T_ZOOM_OUT, STR_P_ZOOM_OUT, ICONID_ZOOM_OUT );

  /**
   * Описание действия: Переместить элемент вверх по списку.
   */
  ITsActionDef ACDEF_LIST_ELEM_MOVE_UP =
      ofPush2( ACTID_LIST_ELEM_MOVE_UP, STR_T_LIST_ELEM_MOVE_UP, STR_P_LIST_ELEM_MOVE_UP, ICONID_ARROW_UP );

  /**
   * Описание действия: Переместить элемент вниз по списку.
   */
  ITsActionDef ACDEF_LIST_ELEM_MOVE_DOWN =
      ofPush2( ACTID_LIST_ELEM_MOVE_DOWN, STR_T_LIST_ELEM_MOVE_DOWN, STR_P_LIST_ELEM_MOVE_DOWN, ICONID_ARROW_DOWN );

  /**
   * Описание действия: Переместить элемент в начало списка.
   */
  ITsActionDef ACDEF_LIST_ELEM_MOVE_FIRST = ofPush2( ACTID_LIST_ELEM_MOVE_FIRST, STR_T_LIST_ELEM_MOVE_FIRST,
      STR_P_LIST_ELEM_MOVE_FIRST, ICONID_ARROW_UP_DOUBLE );

  /**
   * Описание действия: Переместить элемент в конец списка.
   */
  ITsActionDef ACDEF_LIST_ELEM_MOVE_LAST = ofPush2( ACTID_LIST_ELEM_MOVE_LAST, STR_T_LIST_ELEM_MOVE_LAST,
      STR_P_LIST_ELEM_MOVE_LAST, ICONID_ARROW_DOWN_DOUBLE );

  /**
   * Описание действия: изначальный размер (1:1).
   */
  ITsActionDef ACDEF_ZOOM_ORIGINAL =
      ofCheck2( ACTID_ZOOM_ORIGINAL, STR_T_ZOOM_ORIGINAL, STR_P_ZOOM_ORIGINAL, ICONID_ZOOM_ORIGINAL );

  /**
   * Описание действия: изначальный размер (1:1) с выпадающим меню изменения размера.
   */
  ITsActionDef ACDEF_ZOOM_ORIGINAL_MENU =
      ofMenu2( ACTID_ZOOM_ORIGINAL, STR_T_ZOOM_ORIGINAL, STR_P_ZOOM_ORIGINAL, ICONID_ZOOM_ORIGINAL );

  /**
   * Описание действия: изначальный размер (1:1).
   */
  ITsActionDef ACDEF_ZOOM_ORIGINAL_PUSHBUTTON =
      ofPush2( ACTID_ZOOM_ORIGINAL, STR_T_ZOOM_ORIGINAL, STR_P_ZOOM_ORIGINAL, ICONID_ZOOM_ORIGINAL );

  /**
   * Описание действия: перейти в начало.
   */
  ITsActionDef ACDEF_GO_FIRST = ofPush2( ACTID_GO_FIRST, STR_T_GO_FIRST, STR_P_GO_FIRST, ICONID_GO_FIRST );

  /**
   * Описание действия: перейти на предыдущий элемент.
   */
  ITsActionDef ACDEF_GO_PREV = ofPush2( ACTID_GO_PREV, STR_T_GO_PREV, STR_P_GO_PREV, ICONID_GO_PREVIOUS );

  /**
   * Описание действия: перейти на следующий элемент.
   */
  ITsActionDef ACDEF_GO_NEXT = ofPush2( ACTID_GO_NEXT, STR_T_GO_NEXT, STR_P_GO_NEXT, ICONID_GO_NEXT );

  /**
   * Описание действия: перейти в конец.
   */
  ITsActionDef ACDEF_GO_LAST = ofPush2( ACTID_GO_LAST, STR_T_GO_LAST, STR_P_GO_LAST, ICONID_GO_LAST );

  /**
   * Описание действия: просмотр в виде списка.
   */
  ITsActionDef ACDEF_VIEW_AS_LIST =
      ofCheck2( ACTID_VIEW_AS_LIST, STR_T_VIEW_AS_LIST, STR_P_VIEW_AS_LIST, ICONID_VIEW_AS_LIST );

  /**
   * Описание действия: просмотр в виде дерева.
   */
  ITsActionDef ACDEF_VIEW_AS_TREE =
      ofCheck2( ACTID_VIEW_AS_TREE, STR_T_VIEW_AS_TREE, STR_P_VIEW_AS_TREE, ICONID_VIEW_AS_TREE );

  /**
   * Описание действия: меню просмотра в виде дерева.
   */
  ITsActionDef ACDEF_VIEW_AS_TREE_MENU =
      ofMenu2( ACTID_VIEW_AS_TREE, STR_T_VIEW_AS_TREE, STR_P_VIEW_AS_TREE, ICONID_VIEW_AS_TREE );

  /**
   * Описание действия: Добавить корневой элемент в дерево.
   */
  ITsActionDef ACDEF_TREE_ADD_ROOT =
      ofPush2( ACTID_TREE_ADD_ROOT, STR_T_TREE_ADD_ROOT, STR_P_TREE_ADD_ROOT, ICONID_TREE_ADD_ROOT );

  /**
   * Описание действия: добавить дочерный элемент к текущему узлу дерева.
   */
  ITsActionDef ACDEF_TREE_ADD_CHILD =
      ofPush2( ACTID_TREE_ADD_CHILD, STR_T_TREE_ADD_CHILD, STR_P_TREE_ADD_CHILD, ICONID_TREE_ADD_CHILD );

  /**
   * Описание действия: просмотр в виде дерева.
   * <p>
   * В отжатом состоянии показывает значок списка но пишет о переключении в дерево, в нажатом - показывает дерево и
   * пишет о переключении в список.
   */
  ITsActionDef ACDEF_TOGGLE_VIEW_AS_TREE_OR_LIST = ofCheck2( ACTID_TOGGLE_VIEW_AS_TREE_OR_LIST, //
      STR_T_TOGGLE_VIEW_TREE_LIST, STR_P_TOGGLE_VIEW_TREE_LIST,
      // TODO надо иметь специальный значок переключателя список/дерево
      ICONID_VIEW_AS_TREE );

  /**
   * Описание действия: отменить действие.
   */
  ITsActionDef ACDEF_UNDO = ofPush2( ACTID_UNDO, STR_T_UNDO, STR_P_UNDO, ICONID_EDIT_UNDO );

  /**
   * Описание действия: вернуть действие.
   */
  ITsActionDef ACDEF_REDO = ofPush2( ACTID_REDO, STR_T_REDO, STR_P_REDO, ICONID_EDIT_REDO );

  /**
   * Описание действия: напечатать документ.
   */
  ITsActionDef ACDEF_PRINT = ofPush2( ACTID_PRINT, STR_T_PRINT, STR_P_PRINT, ICONID_DOCUMENT_PRINT );

  /**
   * Описание действия: Предварительный просмотр печатаемого документа.
   */
  ITsActionDef ACDEF_PRINT_PREVIEW =
      ofPush2( ACTID_PRINT_PREVIEW, STR_T_PRINT_PREVIEW, STR_P_PRINT_PREVIEW, ICONID_DOCUMENT_PRINT_PREVIEW );

}
