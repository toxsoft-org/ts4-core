package org.toxsoft.core.tsgui.bricks.actions;

import static org.toxsoft.core.tsgui.ITsGuiConstants.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.l10n.ITsGuiSharedResources.*;

/**
 * Standatrd actions.
 *
 * @author hazard157
 */
public interface ITsStdActionDefs {

  // ------------------------------------------------------------------------------------
  // Action IDs

  // GOGA --- 2025-02-26 moving to ITsGuiConstants.TSGUI_ACT_ID
  // /**
  // * Prefix of the standard action IDs.
  // */
  // String TSGUI_ACT_ID = TS_FULL_ID + ".tsgui.stdact"; //$NON-NLS-1$
  // ---

  /**
   * Action ID: the separator in menus, toolbars, etc and this is not an action!
   */
  String ACTID_SEPARATOR = TSGUI_ACT_ID + ".separator"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_ADD}.
   */
  String ACTID_ADD = TSGUI_ACT_ID + ".add"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_ADD_ALL}.
   */
  String ACTID_ADD_ALL = TSGUI_ACT_ID + ".add_all"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_EDIT}.
   */
  String ACTID_EDIT = TSGUI_ACT_ID + ".edit"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_REMOVE}.
   */
  String ACTID_REMOVE = TSGUI_ACT_ID + ".remove"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_CLEAR}.
   */
  String ACTID_CLEAR = TSGUI_ACT_ID + ".clear"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_INFO}.
   */
  String ACTID_INFO = TSGUI_ACT_ID + ".info"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_FILTER}.
   */
  String ACTID_HIDE_FILTER = TSGUI_ACT_ID + ".hide_filter"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_HIDE_DETAILS}.
   */
  String ACTID_HIDE_DETAILS = TSGUI_ACT_ID + ".hide_details"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_HIDE_SUMMARY}.
   */
  String ACTID_HIDE_SUMMARY = TSGUI_ACT_ID + ".hide_summary"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_VALIDATE}.
   */
  String ACTID_VALIDATE = TSGUI_ACT_ID + ".validate"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_REFRESH}.
   */
  String ACTID_REFRESH = TSGUI_ACT_ID + ".refresh"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_EXPAND}.
   */
  String ACTID_EXPAND = TSGUI_ACT_ID + ".expand"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_EXPAND_ALL}.
   */
  String ACTID_EXPAND_ALL = TSGUI_ACT_ID + ".expand_all"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_COLLAPSE}.
   */
  String ACTID_COLLAPSE = TSGUI_ACT_ID + ".collapse"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_COLLAPSE_ALL}.
   */
  String ACTID_COLLAPSE_ALL = TSGUI_ACT_ID + ".collapse_all"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_FILTER}.
   */
  String ACTID_FILTER = TSGUI_ACT_ID + ".filter"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_SORT}.
   */
  String ACTID_SORT = TSGUI_ACT_ID + ".sort"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_SORT_ASCENDING}.
   */
  String ACTID_SORT_ASCENDING = TSGUI_ACT_ID + ".sort_asc"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_SORT_DESCENDING}.
   */
  String ACTID_SORT_DESCENDING = TSGUI_ACT_ID + ".sort_desc"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_REVERT_CHANGES}.
   */
  String ACTID_REVERT_CHANGES = TSGUI_ACT_ID + ".revert_changes"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_OK_CHANGES}.
   */
  String ACTID_OK_CHANGES = TSGUI_ACT_ID + ".ok_changes"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_CANCEL_CHANGES}.
   */
  String ACTID_CANCEL_CHANGES = TSGUI_ACT_ID + ".cancel_changes"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_APPLY_CHANGES}.
   */
  String ACTID_APPLY_CHANGES = TSGUI_ACT_ID + ".apply_changes"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_RESTORE_DEFAULTS}.
   */
  String ACTID_RESTORE_DEFAULTS = TSGUI_ACT_ID + ".restore_defaults"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_CHECK_ALL}.
   */
  String ACTID_CHECK_ALL = TSGUI_ACT_ID + ".check_all"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_UNCHECK_ALL}.
   */
  String ACTID_UNCHECK_ALL = TSGUI_ACT_ID + ".uncheck_all"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_CHECK_GROUP}.
   */
  String ACTID_CHECK_GROUP = TSGUI_ACT_ID + ".check_group"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_UNCHECK_GROUP}.
   */
  String ACTID_UNCHECK_GROUP = TSGUI_ACT_ID + ".uncheck_group"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_INVERT_CHECK}.
   */
  String ACTID_INVERT_CHECK = TSGUI_ACT_ID + ".invert_check"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_CREATE_NEW}.
   */
  String ACTID_CREATE_NEW = TSGUI_ACT_ID + ".create_new"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_OPEN}.
   */
  String ACTID_OPEN = TSGUI_ACT_ID + ".open"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_SAVE}.
   */
  String ACTID_SAVE = TSGUI_ACT_ID + ".save"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_SAVE_AS}.
   */
  String ACTID_SAVE_AS = TSGUI_ACT_ID + ".save_as"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_FILE_EXPORT}.
   */
  String ACTID_FILE_EXPORT = TSGUI_ACT_ID + ".file_export"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_FILE_IMPORT}.
   */
  String ACTID_FILE_IMPORT = TSGUI_ACT_ID + ".file_import"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_QUIT}.
   */
  String ACTID_QUIT = TSGUI_ACT_ID + ".quit"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_ABOUT}.
   */
  String ACTID_ABOUT = TSGUI_ACT_ID + ".about"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_ZOOM_ORIGINAL}, {@link #ACDEF_ZOOM_ORIGINAL_MENU},
   * {@link #ACDEF_ZOOM_ORIGINAL_PUSHBUTTON}.
   */
  String ACTID_ZOOM_ORIGINAL = TSGUI_ACT_ID + ".zoom_orig"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_ZOOM_FIT_NONE}, {@link #ACDEF_ZOOM_FIT_NONE_PUSHBUTTON}.
   */
  String ACTID_ZOOM_FIT_NONE = TSGUI_ACT_ID + ".zoom_fit_none"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_ZOOM_FIT_BEST}, {@link #ACDEF_ZOOM_FIT_BEST_PUSHBUTTON}.
   */
  String ACTID_ZOOM_FIT_BEST = TSGUI_ACT_ID + ".zoom_fit_best"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_ZOOM_FIT_FILL}, {@link #ACDEF_ZOOM_FIT_FILL_PUSHBUTTON}.
   */
  String ACTID_ZOOM_FIT_FILL = TSGUI_ACT_ID + ".zoom_fit_fill"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_ZOOM_FIT_HEIGHT}, {@link #ACDEF_ZOOM_FIT_HEIGHT_PUSHBUTTON}.
   */
  String ACTID_ZOOM_FIT_HEIGHT = TSGUI_ACT_ID + ".zoom_fit_height"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_ZOOM_FIT_WIDTH}, {@link #ACDEF_ZOOM_FIT_WIDTH_PUSHBUTTON}.
   */
  String ACTID_ZOOM_FIT_WIDTH = TSGUI_ACT_ID + ".zoom_fit_width"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_ZOOM_IN}.
   */
  String ACTID_ZOOM_IN = TSGUI_ACT_ID + ".zoom_in"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_ZOOM_OUT}.
   */
  String ACTID_ZOOM_OUT = TSGUI_ACT_ID + ".zoom_out"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_ZOOM_EXPAND_TO_FIT}.
   */
  String ACTID_ZOOM_EXPAND_TO_FIT = TSGUI_ACT_ID + ".zoom_expand_to_fit"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_LIST_ELEM_MOVE_UP}.
   */
  String ACTID_LIST_ELEM_MOVE_UP = TSGUI_ACT_ID + ".list_elem_move_up"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_LIST_ELEM_MOVE_DOWN}.
   */
  String ACTID_LIST_ELEM_MOVE_DOWN = TSGUI_ACT_ID + ".list_elem_move_down"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_LIST_ELEM_MOVE_FIRST}.
   */
  String ACTID_LIST_ELEM_MOVE_FIRST = TSGUI_ACT_ID + ".list_elem_move_first"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_LIST_ELEM_MOVE_LAST}.
   */
  String ACTID_LIST_ELEM_MOVE_LAST = TSGUI_ACT_ID + ".list_elem_move_last"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_GO_FIRST}.
   */
  String ACTID_GO_FIRST = TSGUI_ACT_ID + ".go_first"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_GO_PREV}.
   */
  String ACTID_GO_PREV = TSGUI_ACT_ID + ".go_prev"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_GO_NEXT}.
   */
  String ACTID_GO_NEXT = TSGUI_ACT_ID + ".go_next"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_GO_LAST}.
   */
  String ACTID_GO_LAST = TSGUI_ACT_ID + ".go_last"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_VIEW_AS_LIST}.
   */
  String ACTID_VIEW_AS_LIST = TSGUI_ACT_ID + ".view_as_list"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_VIEW_AS_TREE}.
   */
  String ACTID_VIEW_AS_TREE = TSGUI_ACT_ID + ".view_as_tree"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_TREE_ADD_ROOT}.
   */
  String ACTID_TREE_ADD_ROOT = TSGUI_ACT_ID + ".tree_add_root"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_TREE_ADD_CHILD}.
   */
  String ACTID_TREE_ADD_CHILD = TSGUI_ACT_ID + ".tree_add_child"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_UNDO}.
   */
  String ACTID_UNDO = TSGUI_ACT_ID + ".undo"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_REDO}.
   */
  String ACTID_REDO = TSGUI_ACT_ID + ".redo"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_PRINT}.
   */
  String ACTID_PRINT = TSGUI_ACT_ID + ".print"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_PRINT_PREVIEW}.
   */
  String ACTID_PRINT_PREVIEW = TSGUI_ACT_ID + ".print_preview"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_ADD_COPY}.
   */
  String ACTID_ADD_COPY = TSGUI_ACT_ID + ".add_copy"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_RUN_TEST}.
   */
  String ACTID_RUN_TEST = TSGUI_ACT_ID + ".run_test"; //$NON-NLS-1$

  /**
   * THis is "pseudo" action to insert separators in menu, toolbar, etc.
   */
  ITsActionDef ACDEF_SEPARATOR = ofUnspec1( ACTID_SEPARATOR );

  // ------------------------------------------------------------------------------------
  // Action definitions

  /**
   * Action: quit the application.
   */
  ITsActionDef ACDEF_QUIT = ofPush2( ACTID_QUIT, //
      STR_T_QUIT, STR_P_QUIT, ICONID_APPLICATION_EXIT );

  /**
   * Action: create new document.
   */
  ITsActionDef ACDEF_CREATE_NEW = ofPush2( ACTID_CREATE_NEW, //
      STR_T_CREATE_NEW, STR_P_CREATE_NEW, ICONID_DOCUMENT_NEW );

  /**
   * Action: open file.
   */
  ITsActionDef ACDEF_OPEN = ofPush2( ACTID_OPEN, //
      STR_T_OPEN, STR_P_OPEN, ICONID_DOCUMENT_OPEN );

  /**
   * Action: save to file.
   */
  ITsActionDef ACDEF_SAVE = ofPush2( ACTID_SAVE, //
      STR_T_SAVE, STR_P_SAVE, ICONID_DOCUMENT_SAVE );

  /**
   * Action: save to the specified file.
   */
  ITsActionDef ACDEF_SAVE_AS = ofPush2( ACTID_SAVE_AS, //
      STR_T_SAVE_AS, STR_P_SAVE_AS, ICONID_DOCUMENT_SAVE_AS );

  /**
   * Action: export content to the specified file.
   */
  ITsActionDef ACDEF_FILE_EXPORT = ofPush2( ACTID_FILE_EXPORT, //
      STR_FILE_EXPORT, STR_FILE_EXPORT_D, ICONID_DOCUMENT_EXPORT );

  /**
   * Action: import content from the specified file.
   */
  ITsActionDef ACDEF_FILE_IMPORT = ofPush2( ACTID_FILE_IMPORT, //
      STR_FILE_IMPORT, STR_FILE_IMPORT_D, ICONID_DOCUMENT_IMPORT );

  /**
   * Action: add an element.
   */
  ITsActionDef ACDEF_ADD = ofPush2( ACTID_ADD, //
      STR_T_ADD, STR_P_ADD, ICONID_LIST_ADD );

  /**
   * Action: add an element created from current element.
   */
  ITsActionDef ACDEF_ADD_COPY = ofPush2( ACTID_ADD_COPY, //
      STR_T_ADD_COPY, STR_P_ADD_COPY, ICONID_LIST_ADD_COPY );

  /**
   * Action: add all available elements.
   */
  ITsActionDef ACDEF_ADD_ALL = ofPush2( ACTID_ADD_ALL, //
      STR_T_ADD_ALL, STR_P_ADD_ALL, ICONID_LIST_ADD_ALL );

  /**
   * Action: edit an element.
   */
  ITsActionDef ACDEF_EDIT = ofPush2( ACTID_EDIT, //
      STR_T_EDIT, STR_P_EDIT, ICONID_DOCUMENT_EDIT );

  /**
   * Action: remove an element.
   */
  ITsActionDef ACDEF_REMOVE = ofPush2( ACTID_REMOVE, //
      STR_T_REMOVE, STR_P_REMOVE, ICONID_LIST_REMOVE );

  /**
   * Action: clear the selection, clear the collection.
   */
  ITsActionDef ACDEF_CLEAR = ofPush2( ACTID_CLEAR, //
      STR_T_CLEAR, STR_P_CLEAR, ICONID_EDIT_CLEAR );

  /**
   * Action: вывести диалог детальной информации об элементе.
   */
  ITsActionDef ACDEF_INFO = ofPush2( ACTID_INFO, //
      STR_T_INFO, STR_P_INFO, ICONID_DIALOG_INFORMATION );

  /**
   * Action: checkbutton to show info about an element.
   */
  ITsActionDef ACDEF_INFO_CHECK = ofCheck2( ACTID_INFO, //
      STR_T_INFO, STR_P_INFO, ICONID_DIALOG_INFORMATION );

  /**
   * Action: checkbutton to hide filters pane.
   */
  ITsActionDef ACDEF_HIDE_FILTERS = ofCheck2( ACTID_HIDE_FILTER, //
      STR_T_HIDE_FILTER, STR_P_HIDE_FILTER, ICONID_HIDE_FILTER_PANE );

  /**
   * Action: checkbutton to hide details pane.
   */
  ITsActionDef ACDEF_HIDE_DETAILS = ofCheck2( ACTID_HIDE_DETAILS, //
      STR_T_HIDE_DETAILS, STR_P_HIDE_DETAILS, ICONID_HIDE_DETAILS_PANE );

  /**
   * Action: checkbutton to hide summary pane.
   */
  ITsActionDef ACDEF_HIDE_SUMMARY = ofCheck2( ACTID_HIDE_SUMMARY, //
      STR_T_HIDE_SUMMARY, STR_P_HIDE_SUMMARY, ICONID_HIDE_SUMMARY_PANE );

  /**
   * Action: validate selected (focused) element.
   */
  ITsActionDef ACDEF_VALIDATE = ofPush2( ACTID_VALIDATE, //
      STR_T_VALIDATE, STR_P_VALIDATE, ICONID_DIALOG_OK );

  /**
   * Action: refresh the view.
   */
  ITsActionDef ACDEF_REFRESH = ofPush2( ACTID_REFRESH, //
      STR_T_REFRESH, STR_P_REFRESH, ICONID_VIEW_REFRESH );

  /**
   * Action: expand selected node.
   */
  ITsActionDef ACDEF_EXPAND = ofPush2( ACTID_EXPAND, //
      STR_T_EXPAND, STR_P_EXPAND, ICONID_VIEW_EXPAND );

  /**
   * Action: expand all nodes.
   */
  ITsActionDef ACDEF_EXPAND_ALL = ofPush2( ACTID_EXPAND_ALL, //
      STR_T_EXPAND_ALL, STR_P_EXPAND_ALL, ICONID_VIEW_EXPAND_ALL );

  /**
   * Action: collapse selected node.
   */
  ITsActionDef ACDEF_COLLAPSE = ofPush2( ACTID_COLLAPSE, //
      STR_T_COLLAPSE, STR_P_COLLAPSE, ICONID_VIEW_COLLAPSE );

  /**
   * Action: collapse all nodes.
   */
  ITsActionDef ACDEF_COLLAPSE_ALL = ofPush2( ACTID_COLLAPSE_ALL, //
      STR_T_COLLAPSE_ALL, STR_P_COLLAPSE_ALL, ICONID_VIEW_COLLAPSE_ALL );

  /**
   * Action: invoke collection filtering parameters dialog.<br>
   * Actions {@link #ACDEF_FILTER} and {@link #ACDEF_FILTER} has the same ID, so they can not be used together.
   */
  ITsActionDef ACDEF_FILTER = ofPush2( ACTID_FILTER, //
      STR_T_FILTER, STR_P_FILTER, ICONID_VIEW_FILTER );

  /**
   * Action: set filter on/off check.<br>
   * Actions {@link #ACDEF_FILTER} and {@link #ACDEF_FILTER} has the same ID, so they can not be used together.
   */
  ITsActionDef ACDEF_FILTER_CHECK = ofCheck2( ACTID_FILTER, //
      STR_T_FILTER, STR_P_FILTER, ICONID_VIEW_FILTER );

  /**
   * Action: sort collection (or invoke sort parameters dialog).
   */
  ITsActionDef ACDEF_SORT = ofPush2( ACTID_SORT, //
      STR_T_SORT, STR_P_SORT, ICONID_VIEW_SORT );

  /**
   * Action: sort collection in ascending order.
   */
  ITsActionDef ACDEF_SORT_ASCENDING = ofPush2( ACTID_SORT_ASCENDING, //
      STR_T_SORT_ASCENDING, STR_P_SORT_ASCENDING, ICONID_VIEW_SORT_ASCENDING );

  /**
   * Action: sort collection in descending order.
   */
  ITsActionDef ACDEF_SORT_DESCENDING = ofPush2( ACTID_SORT_DESCENDING, //
      STR_T_SORT_DESCENDING, STR_P_SORT_DESCENDING, ICONID_VIEW_SORT_DESCENDING );

  /**
   * Action: revert changes to the previous values and finish editing.
   */
  ITsActionDef ACDEF_OK_CHANGES = ofPush2( ACTID_OK_CHANGES, //
      STR_T_OK_CHANGES, STR_P_OK_CHANGES, ICONID_DIALOG_OK );

  /**
   * Action: revert changes to the previous values and finish editing.
   */
  ITsActionDef ACDEF_CANCEL_CHANGES = ofPush2( ACTID_CANCEL_CHANGES, //
      STR_T_CANCEL_CHANGES, STR_P_CANCEL_CHANGES, ICONID_DIALOG_CANCEL );

  /**
   * Action: revert changes to the previous values and finish editing.
   */
  ITsActionDef ACDEF_APPLY_CHANGES = ofPush2( ACTID_APPLY_CHANGES, //
      STR_T_APPLY_CHANGES, STR_P_APPLY_CHANGES, ICONID_DIALOG_OK_APPLY );

  /**
   * Action: revert changes to the previous values and continue editing.
   */
  ITsActionDef ACDEF_REVERT_CHANGES = ofPush2( ACTID_REVERT_CHANGES, //
      STR_T_REVERT_CHANGES, STR_P_REVERT_CHANGES, ICONID_DOCUMENT_REVERT );

  /**
   * Action: restore default values.
   */
  ITsActionDef ACDEF_RESTORE_DEFAULTS = ofPush2( ACTID_RESTORE_DEFAULTS, //
      STR_T_RESTORE_DEFAULTS, STR_P_RESTORE_DEFAULTS, ICONID_RESTORE_DEFAULTS );

  /**
   * Action: set check mark on all elements of the collection.
   */
  ITsActionDef ACDEF_CHECK_ALL = ofPush2( ACTID_CHECK_ALL, //
      STR_T_CHECK_ALL, STR_P_CHECK_ALL, ICONID_ITEMS_CHECK_ALL );

  /**
   * Action: remove check mark from all elements of the collection.
   */
  ITsActionDef ACDEF_UNCHECK_ALL = ofPush2( ACTID_UNCHECK_ALL, //
      STR_T_UNCHECK_ALL, STR_P_UNCHECK_ALL, ICONID_ITEMS_UNCHECK_ALL );

  /**
   * Action: set check mark on the some group of elements of the collection.
   */
  ITsActionDef ACDEF_CHECK_GROUP = ofPush2( ACTID_CHECK_GROUP, //
      STR_T_CHECK_GROUP, STR_P_CHECK_GROUP, ICONID_ITEMS_CHECK_GROUP );

  /**
   * Action: remove check mark from the some group of elements of the collection.
   */
  ITsActionDef ACDEF_UNCHECK_GROUP = ofPush2( ACTID_UNCHECK_GROUP, //
      STR_T_UNCHECK_GROUP, STR_P_UNCHECK_GROUP, ICONID_ITEMS_UNCHECK_GROUP );

  /**
   * Action: invert all check marks state of the collection elements.
   */
  ITsActionDef ACDEF_INVERT_CHECK = ofPush2( ACTID_INVERT_CHECK, //
      STR_T_INVERT_CHECK, STR_P_INVERT_CHECK, ICONID_ITEMS_INVERT_CHECK );

  /**
   * Action: show information about application.
   */
  ITsActionDef ACDEF_ABOUT = ofPush2( ACTID_ABOUT, //
      STR_T_ABOUT, STR_P_ABOUT, null );

  /**
   * Action: set no adaptive fit mode (check button).
   */
  ITsActionDef ACDEF_ZOOM_FIT_NONE = ofCheck2( ACTID_ZOOM_FIT_NONE, //
      STR_ZOOM_FIT_NONE, STR_ZOOM_FIT_NONE_D, ICONID_ZOOM_FIT_NONE );

  /**
   * Action: set no adaptive fit mode (push button)
   */
  ITsActionDef ACDEF_ZOOM_FIT_NONE_PUSHBUTTON = ofPush2( ACTID_ZOOM_FIT_NONE, //
      STR_ZOOM_FIT_NONE, STR_ZOOM_FIT_NONE_D, ICONID_ZOOM_FIT_NONE );

  /**
   * Action: zoom to fit best (check button).
   */
  ITsActionDef ACDEF_ZOOM_FIT_BEST = ofCheck2( ACTID_ZOOM_FIT_BEST, //
      STR_ZOOM_FIT_BEST, STR_ZOOM_FIT_BEST_D, ICONID_ZOOM_FIT_BEST );

  /**
   * Action: zoom to fit best (push button)
   */
  ITsActionDef ACDEF_ZOOM_FIT_BEST_PUSHBUTTON = ofPush2( ACTID_ZOOM_FIT_BEST, //
      STR_ZOOM_FIT_BEST, STR_ZOOM_FIT_BEST_D, ICONID_ZOOM_FIT_BEST );

  /**
   * Action: zoom to fit fill (check button).
   */
  ITsActionDef ACDEF_ZOOM_FIT_FILL = ofCheck2( ACTID_ZOOM_FIT_FILL, //
      STR_ZOOM_FIT_FILL, STR_ZOOM_FIT_FILL_D, ICONID_ZOOM_FIT_FILL );

  /**
   * Action: zoom to fit fill (push button)
   */
  ITsActionDef ACDEF_ZOOM_FIT_FILL_PUSHBUTTON = ofPush2( ACTID_ZOOM_FIT_FILL, //
      STR_ZOOM_FIT_FILL, STR_ZOOM_FIT_FILL_D, ICONID_ZOOM_FIT_FILL );

  /**
   * Action: zoom to fit width (check button).
   */
  ITsActionDef ACDEF_ZOOM_FIT_WIDTH = ofCheck2( ACTID_ZOOM_FIT_WIDTH, //
      STR_ZOOM_FIT_WIDTH, STR_ZOOM_FIT_WIDTH_D, ICONID_ZOOM_FIT_WIDTH );

  /**
   * Action: zoom to fit width (push button).
   */
  ITsActionDef ACDEF_ZOOM_FIT_WIDTH_PUSHBUTTON = ofPush2( ACTID_ZOOM_FIT_WIDTH, //
      STR_ZOOM_FIT_WIDTH, STR_ZOOM_FIT_WIDTH_D, ICONID_ZOOM_FIT_WIDTH );

  /**
   * Action: zoom to fit height (check button).
   */
  ITsActionDef ACDEF_ZOOM_FIT_HEIGHT = ofCheck2( ACTID_ZOOM_FIT_HEIGHT, //
      STR_ZOOM_FIT_HEIGHT, STR_ZOOM_FIT_HEIGHT_D, ICONID_ZOOM_FIT_HEIGHT );

  /**
   * Action: zoom to fit height (pushbutton).
   */
  ITsActionDef ACDEF_ZOOM_FIT_HEIGHT_PUSHBUTTON = ofPush2( ACTID_ZOOM_FIT_HEIGHT, //
      STR_ZOOM_FIT_HEIGHT, STR_ZOOM_FIT_HEIGHT_D, ICONID_ZOOM_FIT_HEIGHT );

  /**
   * Action: zoom in.
   */
  ITsActionDef ACDEF_ZOOM_IN = ofPush2( ACTID_ZOOM_IN, //
      STR_ZOOM_IN, STR_ZOOM_IN_D, ICONID_ZOOM_IN );

  /**
   * Action: zoom out.
   */
  ITsActionDef ACDEF_ZOOM_OUT = ofPush2( ACTID_ZOOM_OUT, //
      STR_ZOOM_OUT, STR_ZOOM_OUT_D, ICONID_ZOOM_OUT );

  /**
   * Action: toggle "zoom small image to fit" mode.
   */
  ITsActionDef ACDEF_ZOOM_EXPAND_TO_FIT = ofCheck2( ACTID_ZOOM_EXPAND_TO_FIT, //
      STR_ZOOM_EXPAND_TO_FIT, STR_ZOOM_EXPAND_TO_FIT_D, ICONID_VIEW_FULLSCREEN );

  /**
   * Action: move element up in list.
   */
  ITsActionDef ACDEF_LIST_ELEM_MOVE_UP = ofPush2( ACTID_LIST_ELEM_MOVE_UP, //
      STR_T_LIST_ELEM_MOVE_UP, STR_P_LIST_ELEM_MOVE_UP, ICONID_ARROW_UP );

  /**
   * Action: Move selected element down in list.
   */
  ITsActionDef ACDEF_LIST_ELEM_MOVE_DOWN = ofPush2( ACTID_LIST_ELEM_MOVE_DOWN, //
      STR_T_LIST_ELEM_MOVE_DOWN, STR_P_LIST_ELEM_MOVE_DOWN, ICONID_ARROW_DOWN );

  /**
   * Action: Move selected element to the list start.
   */
  ITsActionDef ACDEF_LIST_ELEM_MOVE_FIRST = ofPush2( ACTID_LIST_ELEM_MOVE_FIRST, //
      STR_T_LIST_ELEM_MOVE_FIRST, STR_P_LIST_ELEM_MOVE_FIRST, ICONID_ARROW_UP_DOUBLE );

  /**
   * Action: Move selected element to the list end.
   */
  ITsActionDef ACDEF_LIST_ELEM_MOVE_LAST = ofPush2( ACTID_LIST_ELEM_MOVE_LAST, //
      STR_T_LIST_ELEM_MOVE_LAST, STR_P_LIST_ELEM_MOVE_LAST, ICONID_ARROW_DOWN_DOUBLE );

  /**
   * Action: original size (1:1) check button.
   */
  ITsActionDef ACDEF_ZOOM_ORIGINAL = ofCheck2( ACTID_ZOOM_ORIGINAL, //
      STR_ZOOM_ORIGINAL, STR_ZOOM_ORIGINAL_D, ICONID_ZOOM_ORIGINAL );

  /**
   * Action: original size (1:1) drop-down menu..
   */
  ITsActionDef ACDEF_ZOOM_ORIGINAL_MENU = ofMenu2( ACTID_ZOOM_ORIGINAL, //
      STR_ZOOM_ORIGINAL, STR_ZOOM_ORIGINAL_D, ICONID_ZOOM_ORIGINAL );

  /**
   * Action: original size (1:1) push button.
   */
  ITsActionDef ACDEF_ZOOM_ORIGINAL_PUSHBUTTON = ofPush2( ACTID_ZOOM_ORIGINAL, //
      STR_ZOOM_ORIGINAL, STR_ZOOM_ORIGINAL_D, ICONID_ZOOM_ORIGINAL );

  /**
   * Action: Go to first element.
   */
  ITsActionDef ACDEF_GO_FIRST = ofPush2( ACTID_GO_FIRST, //
      STR_T_GO_FIRST, STR_P_GO_FIRST, ICONID_GO_FIRST );

  /**
   * Action: Go to previous element.
   */
  ITsActionDef ACDEF_GO_PREV = ofPush2( ACTID_GO_PREV, //
      STR_T_GO_PREV, STR_P_GO_PREV, ICONID_GO_PREVIOUS );

  /**
   * Action: Go to next element.
   */
  ITsActionDef ACDEF_GO_NEXT = ofPush2( ACTID_GO_NEXT, //
      STR_T_GO_NEXT, STR_P_GO_NEXT, ICONID_GO_NEXT );

  /**
   * Action: Go to last element.
   */
  ITsActionDef ACDEF_GO_LAST = ofPush2( ACTID_GO_LAST, //
      STR_T_GO_LAST, STR_P_GO_LAST, ICONID_GO_LAST );

  /**
   * Action: Show as list of elements.
   */
  ITsActionDef ACDEF_VIEW_AS_LIST = ofCheck2( ACTID_VIEW_AS_LIST, //
      STR_T_VIEW_AS_LIST, STR_P_VIEW_AS_LIST, ICONID_VIEW_AS_LIST );

  /**
   * Action: Show as element tree.
   */
  ITsActionDef ACDEF_VIEW_AS_TREE = ofCheck2( ACTID_VIEW_AS_TREE, //
      STR_T_VIEW_AS_TREE, STR_P_VIEW_AS_TREE, ICONID_VIEW_AS_TREE );

  /**
   * Action: Show as element tree drop-down menu.
   */
  ITsActionDef ACDEF_VIEW_AS_TREE_MENU = ofMenu2( ACTID_VIEW_AS_TREE, //
      STR_T_VIEW_AS_TREE, STR_P_VIEW_AS_TREE, ICONID_VIEW_AS_TREE );

  /**
   * Action: Create new root node in the tree.
   */
  ITsActionDef ACDEF_TREE_ADD_ROOT = ofPush2( ACTID_TREE_ADD_ROOT, //
      STR_T_TREE_ADD_ROOT, STR_P_TREE_ADD_ROOT, ICONID_TREE_ADD_ROOT );

  /**
   * Action: Create child node of the selected node.
   */
  ITsActionDef ACDEF_TREE_ADD_CHILD = ofPush2( ACTID_TREE_ADD_CHILD, //
      STR_T_TREE_ADD_CHILD, STR_P_TREE_ADD_CHILD, ICONID_TREE_ADD_CHILD );

  /**
   * Action: undo the last editing (action).
   */
  ITsActionDef ACDEF_UNDO = ofPush2( ACTID_UNDO, //
      STR_T_UNDO, STR_P_UNDO, ICONID_EDIT_UNDO );

  /**
   * Action: redo (repeat) the last undone editing (action).
   */
  ITsActionDef ACDEF_REDO = ofPush2( ACTID_REDO, //
      STR_T_REDO, STR_P_REDO, ICONID_EDIT_REDO );

  /**
   * Action: print the document.
   */
  ITsActionDef ACDEF_PRINT = ofPush2( ACTID_PRINT, //
      STR_T_PRINT, STR_P_PRINT, ICONID_DOCUMENT_PRINT );

  /**
   * Action: show the print preview.
   */
  ITsActionDef ACDEF_PRINT_PREVIEW = ofPush2( ACTID_PRINT_PREVIEW, //
      STR_T_PRINT_PREVIEW, STR_P_PRINT_PREVIEW, ICONID_DOCUMENT_PRINT_PREVIEW );

  /**
   * Action: Run test action for development purposes.
   */
  ITsActionDef ACDEF_RUN_TEST = ofPush2( ACTID_RUN_TEST, //
      STR_RUN_TEST, STR_RUN_TEST_D, ICONID_RUN_TEST );

}
