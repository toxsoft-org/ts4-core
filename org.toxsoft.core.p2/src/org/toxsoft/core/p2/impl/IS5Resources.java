package org.toxsoft.core.p2.impl;

/**
 * Локализуемы ресурсы.
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
interface IS5Resources {

  String MSG_CHECK_UPDATES = "Проверка обновлений программы";
  String MSG_CHECK_REPOSITORY = "%s";
  String MSG_REPO_PING_REPOSITORY = "%s. Установка связи с репозиторием";
  String MSG_PING_REPOSITORY = "Установка связи с репозиторием";
  String MSG_REPO_FIND_UPDATES = "%s. Поиск обновлений на репозитории";
  String MSG_FIND_UPDATES = "Поиск обновлений на репозитории";
  String MSG_CHECK_REPOSITORY_UPDATE =
      "Проверка репозитория %s\n\n.Обновления не найдены.\n\nВы используете последнюю версию программы";
  String MSG_REPO_UPDATES_NOT_FOUND = "%s. Обновления не найдены.";
  String MSG_UPDATES_NOT_FOUND = "Обновления не найдены.\n\nВы используете последнюю версию программы";
  String MSG_INSTALL_UPDATES = "Установка обновлений из репозитория";
  String MSG_UPDATES = "Было загружено и установлено обновление программы.\n\nОбновленные модули:\n%s";
  String MSG_NEED_RESTART = "Программа будет перезапущена";
  String MSG_INSTALL_COMPLETED = "Завершение обновления";
  String MSG_ADDED_MODULE = "%s (%s) добавлен";
  String MSG_REMOVED_MODULE = "%s (%s) удален";
  String MSG_RESTART_CANCEL = "Перезапуск отменен. OK - перезапустить. Cancel - закрыть диалог";

  String MSG_ERR_REPO_NOT_FOUND = "%s. Репозиторий не найден";
  String MSG_ERR_REPOSITORY_NOT_FOUND =
      "Нет доступных репозиториев:\n%s.\n\nПопробуйте повторить запрос через некоторое время";
  String MSG_ERR_IDE_NOT_SUPPORT =
      "Обновление из репозитория %s.\n\nФункция не поддерживается.\n\nПопытка запустить обновление из под Eclipse IDE?";
  String MSG_ERR_PROVIDER_NOT_FOUND = "В контексте плагина не найдена провайдер агента службы обновления %s";
  String MSG_ERR_CREATE_AGENT = "Ошибка создания агента обновления. Причина: %s";
  String MSG_ERR_UNEXPECTED = "Неожиданная ошибка задачи обновления. Код ошибки: %s. Задача: %s";
}
