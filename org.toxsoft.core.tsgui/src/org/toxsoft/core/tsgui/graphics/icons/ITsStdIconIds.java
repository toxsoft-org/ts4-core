package org.toxsoft.core.tsgui.graphics.icons;

/**
 * Icons built in core TsGUI library.
 *
 * @author hazard157
 */
@SuppressWarnings( { "nls", "javadoc" } )
public interface ITsStdIconIds {

  String PREFIX_OF_ICON_IDS = "ICONID_";

  // ------------------------------------------------------------------------------------
  // TsGUI specific icons

  String ICONID_TSAPP_WINDOWS_ICON = "app-icon";         // the application icon
  String ICONID_TOXSOFT_LOGO       = "ts-logo";          // the ToxSoft logo
  String ICONID_UNKNOWN_ICON_ID    = "unknown-icon-id";  // image used for unknown icons
  String ICONID_TRANSPARENT        = "transparent-icon"; // tranparent image

  // ------------------------------------------------------------------------------------
  // General purpose icons

  String ICONID_APPLICATION_EXIT = "application-exit";

  String ICONID_FOLDER = "folder";
  String ICONID_FONTS  = "fonts";
  String ICONID_COLORS = "colors";

  // ------------------------------------------------------------------------------------
  // Working with documents as whole

  String ICONID_DOCUMENT_EDIT          = "document-edit";
  String ICONID_DOCUMENT_EXPORT        = "document-export";
  String ICONID_DOCUMENT_IMPORT        = "document-import";
  String ICONID_DOCUMENT_NEW           = "document-new";
  String ICONID_DOCUMENT_OPEN          = "document-open";
  String ICONID_DOCUMENT_PRINT         = "document-print";
  String ICONID_DOCUMENT_PRINT_PREVIEW = "document-print-preview";
  String ICONID_DOCUMENT_PROPERTIES    = "document-properties";
  String ICONID_DOCUMENT_REVERT        = "document-revert";
  String ICONID_DOCUMENT_SAVE          = "document-save";
  String ICONID_DOCUMENT_SAVE_ALL      = "document-save-all";
  String ICONID_DOCUMENT_SAVE_AS       = "document-save-as";

  // ------------------------------------------------------------------------------------
  // General editing actions

  String ICONID_EDIT_CLEAR            = "edit-clear";
  String ICONID_EDIT_COPY             = "edit-copy";
  String ICONID_EDIT_CUT              = "edit-cut";
  String ICONID_EDIT_DELETE           = "edit-delete";
  String ICONID_EDIT_FIND             = "edit-find";
  String ICONID_EDIT_FIND_REPLACE     = "edit-find-replace";
  String ICONID_EDIT_PASTE            = "edit-paste";
  String ICONID_EDIT_REDO             = "edit-redo";
  String ICONID_EDIT_SELECT_ALL       = "edit-select-all";
  String ICONID_EDIT_UNDO             = "edit-undo";
  String ICONID_LIST_ADD              = "list-add";
  String ICONID_LIST_ADD_COPY         = "list-add-copy";
  String ICONID_LIST_ADD_ALL          = "list-add-all";
  String ICONID_LIST_REMOVE           = "list-remove";
  String ICONID_ITEMS_CHECK_ALL       = "items_check_all";
  String ICONID_ITEMS_UNCHECK_ALL     = "items_uncheck_all";
  String ICONID_ITEMS_CHECK_GROUP     = "items_check";
  String ICONID_ITEMS_UNCHECK_GROUP   = "items_uncheck";
  String ICONID_ITEMS_INVERT_CHECK    = "items_invert_check";
  String ICONID_TREE_ADD_ROOT         = "tree-add-root";
  String ICONID_TREE_ADD_CHILD        = "tree-add-child";
  String ICONID_FORMAT_JUSTIFY_CENTER = "format-justify-center";
  String ICONID_FORMAT_JUSTIFY_FILL   = "format-justify-fill";
  String ICONID_FORMAT_JUSTIFY_LEFT   = "format-justify-left";
  String ICONID_FORMAT_JUSTIFY_RIGHT  = "format-justify-right";

  // ------------------------------------------------------------------------------------
  // Dialog window icons

  String ICONID_DIALOG_CANCEL      = "dialog-cancel";      // Cancel button icon
  String ICONID_DIALOG_CLOSE       = "dialog-close";       // Close button icon
  String ICONID_DIALOG_OK_APPLY    = "dialog-ok-apply";    // Apply button icon
  String ICONID_DIALOG_OK          = "dialog-ok";          // OK button icon
  String ICONID_DIALOG_ERROR       = "dialog-error";       // Dialog kind: error
  String ICONID_DIALOG_INFORMATION = "dialog-information"; // Dialog kind: informational message
  String ICONID_DIALOG_PASSWORD    = "dialog-password";    // Dialog kind: enter password
  String ICONID_DIALOG_WARNING     = "dialog-warning";     // Dialog kind: warning

  // ------------------------------------------------------------------------------------
  // Arrows and navigation indcators

  /**
   * ИД значка: двойная стрелка вниз.
   */
  String ICONID_ARROW_DOWN_DOUBLE     = "arrow-down-double";
  String ICONID_ARROW_DOWN            = "arrow-down";
  String ICONID_ARROW_LEFT_DOUBLE     = "arrow-left-double";
  String ICONID_ARROW_LEFT            = "arrow-left";
  String ICONID_ARROW_RIGHT_DOUBLE    = "arrow-right-double";
  String ICONID_ARROW_RIGHT           = "arrow-right";
  String ICONID_ARROW_UP_DOUBLE       = "arrow-up-double";
  String ICONID_ARROW_UP              = "arrow-up";
  String ICONID_GO_FIRST              = "go-first";
  String ICONID_GO_FIRST_VIEW_PAGE    = "go-first-view-page";
  String ICONID_GO_FIRST_VIEW         = "go-first-view";
  String ICONID_GO_LAST               = "go-last";
  String ICONID_GO_LAST_VIEW_PAGE     = "go-last-view-page";
  String ICONID_GO_LAST_VIEW          = "go-last-view";
  String ICONID_GO_NEXT               = "go-next";
  String ICONID_GO_NEXT_VIEW_PAGE     = "go-next-view-page";
  String ICONID_GO_NEXT_VIEW          = "go-next-view";
  String ICONID_GO_PREVIOUS           = "go-previous";
  String ICONID_GO_PREVIOUS_VIEW_PAGE = "go-previous-view-page";
  String ICONID_GO_PREVIOUS_VIEW      = "go-previous-view";
  String ICONID_NAVIGATE_UP_LAST      = "navigate_up_last";
  String ICONID_NAVIGATE_UP           = "navigate_up";
  String ICONID_NAVIGATE_UP2          = "navigate_up2";
  String ICONID_NAVIGATE_DOWN_LAST    = "navigate_down_last";
  String ICONID_NAVIGATE_DOWN         = "navigate_down";
  String ICONID_NAVIGATE_DOWN2        = "navigate_down2";
  String ICONID_NAVIGATE_LEFT_LAST    = "navigate_left_last";
  String ICONID_NAVIGATE_LEFT         = "navigate_left";
  String ICONID_NAVIGATE_LEFT2        = "navigate_left2";
  String ICONID_NAVIGATE_RIGHT_LAST   = "navigate_right_last";
  String ICONID_NAVIGATE_RIGHT        = "navigate_right";
  String ICONID_NAVIGATE_RIGHT2       = "navigate_right2";

  // ------------------------------------------------------------------------------------
  // Manage views

  String ICONID_VIEW_AS_LIST         = "view-as-list";
  String ICONID_VIEW_AS_TREE         = "view-as-tree";
  String ICONID_VIEW_COLLAPSE        = "collapse";
  String ICONID_VIEW_COLLAPSE_ALL    = "collapse-all";
  String ICONID_VIEW_EXPAND          = "expand";
  String ICONID_VIEW_EXPAND_ALL      = "expand-all";
  String ICONID_VIEW_FILTER          = "view-filter";
  String ICONID_VIEW_FULLSCREEN      = "view-fullscreen";
  String ICONID_VIEW_REFRESH         = "view-refresh";
  String ICONID_VIEW_RESTORE         = "view-restore";
  String ICONID_VIEW_SORT            = "view-sort";
  String ICONID_VIEW_SORT_ASCENDING  = "view-sort-ascending";
  String ICONID_VIEW_SORT_DESCENDING = "view-sort-descending";
  String ICONID_HIDE_BOTTOM_PANE     = "hide-bottom-pane";
  String ICONID_HIDE_DETAILS_PANE    = "hide-details-pane";
  String ICONID_HIDE_SUMMARY_PANE    = "hide-summary-pane";
  String ICONID_HIDE_FILTER_PANE     = "hide-filter-pane";
  String ICONID_ZOOM_FIT_BEST        = "zoom-fit-best";
  String ICONID_ZOOM_FIT_HEIGHT      = "zoom-fit-height";
  String ICONID_ZOOM_FIT_WIDTH       = "zoom-fit-width";
  String ICONID_ZOOM_IN              = "zoom-in";
  String ICONID_ZOOM_ORIGINAL        = "zoom-original";
  String ICONID_ZOOM_OUT             = "zoom-out";

  // ------------------------------------------------------------------------------------
  // Content (MIME) types

  // String ICONID_MIMETYPE_PDF = "mimetypes/application-pdf";
  // String ICONID_MIMETYPE_VND_MS_EXCEL = "mimetypes/application-vnd.ms-excel";
  // String ICONID_MIMETYPE_TEXT_HTML = "mimetypes/text-html";
  // String ICONID_MIMETYPE_X_OFFICE_CALENDAR = "mimetypes/x-office-calendar";

}
