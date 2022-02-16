package org.toxsoft.core.pas.http;

/**
 * Локализуемые ресурсы.
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link PasHttpMain}
   */
  String MSG_HELLO1          = "Мост реализация публичного API (http). Версия 1.0.1";
  String MSG_HELLO2          = "Компания \"ТоксСофт\", 2021.";
  String MSG_BYE             = "Программа штатно завершила работу.";
  String FMT_FAILED          = "ВНИМАНИЕ: программа вылетела по ошибке (подробнее смотрите лог)!\n  %s: %s";
  String FMT_ERR_NO_CFG_FILE = "Недоступен файл конфигурации %s";
  String FMT_HELP            = "\n" +                                                                       //
      "Аргументы командной строки:\n" +                                                                     //
      "  -h, --help\t\t- вывод этой подсказки\n" +                                                          //
      "  -%s FILENAME\t- задает имя файла конфигурации FILENAME\n" +                                        //
      "\n" +                                                                                                //
      "Используемые файлы (имена по умолчанию могут быть переопределены в командной строке):\n" +           //
      "  %s\t- INI-файл параметров подключения к HTTP, PAS, обслуживания запросов и т.п.\n" +               //
      "  %s\t- файл журнала (лог-файл)\n" +                                                                 //
      "\n" +                                                                                                //
      "\n" +                                                                                                //
      "\n";

}