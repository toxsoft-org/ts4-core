package org.toxsoft.unit.txtproj.mws;

import org.toxsoft.unit.txtproj.mws.e4.addons.AddonUnitTxtprojMws;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
public interface IUnitTxtprojMwsResources {

  /**
   * {@link IUnitTxtprojMwsConstants}
   */
  String STR_N_PROJECT_FILE_FORMAT_INFO = "Формат проекта";
  String STR_D_PROJECT_FILE_FORMAT_INFO = "Информация о формате файла проекта";
  String STR_N_ALWAYS_USE_FILE_MENU     = "В меню \"Файл\"";
  String STR_D_ALWAYS_USE_FILE_MENU     = "Всегда размещать команды работы с проектом в меню и панель \"Файл\"";

  String STR_N_SHOW_CMD_IN_MENU = "Отображать команды в меню";
  String STR_D_SHOW_CMD_IN_MENU =
      "Отображать команды управления проектом (создать, открыть, осхранить) в главном меню приложения";

  String STR_N_SHOW_CMD_IN_TOOLBAR = "Отображать команды в toolbar";
  String STR_D_SHOW_CMD_IN_TOOLBAR =
      "Отображать команды управления проектом (создать, открыть, осхранить) в главном toolbar приложения";

  String STR_N_IMMEDIATE_LOAD_PROJ = "Загрузить проект сразу";
  String STR_D_IMMEDIATE_LOAD_PROJ =
      "Загрузить проект сразу при инициализации, а не асинхронно, после запоска программы";

  String STR_N_IS_WINDOWS_TITLE_BOUND = "Заголовк окна?";
  String STR_D_IS_WINDOWS_TITLE_BOUND = "Признак привязки заголвка окна к имено загруженного проекта";

  /**
   * {@link AddonUnitTxtprojMws}
   */
  String MSG_ASK_SAVE_CHANGES_DLG_MESSAGE = "Имеются несохраненные изменения в проекте.\nСохранить изменения?";

}
