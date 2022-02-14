package org.toxsoft.core.pas.http.server;

/**
 * Локализуемые ресурсы.
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link PasHttpServer}
   */
  String FMT_ERR_INV_PASSWORD        = "Pubapi: Недопустимый пароль '%s'";
  String FMT_CONN_TO_SERVER          = "Pubapi: Подключение к серверу %s:%d ...";
  String FMT_ERR_NO_CONN             = "Pubapi: Не могу установить соединение с сервером (%s)";
  String FMT_INIT_SERV_SOCK          = "Pubapi: Инициализация прослушивания %s:%d ...";
  String FMT_ERR_CLIENT_ACCEPTED     = "Pubapi: Подключился клиент %s";
  String MSG_ADD_CHANNEL             = "Добавление канала: %s. channels = %d";
  String MSG_REMOVE_CHANNEL          = "Удаление канала: %s. removed = %b, channels = %d";
  String MSG_SERVER_DOJOB            = "doJob: обработанных http-запросов = %d, ошибок = %d";
  String ERR_CLOSE_INACTIVE_CHANNELS = "Сформирован запрос на удаление неактивного канала: %s";

}
