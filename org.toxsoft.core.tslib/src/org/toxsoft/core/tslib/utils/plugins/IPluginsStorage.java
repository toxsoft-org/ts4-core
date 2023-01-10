package org.toxsoft.core.tslib.utils.plugins;

import java.io.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Управление поключаемыми модулями одного типа.
 *
 * @author hazard157
 */
public interface IPluginsStorage {

  /**
   * Возвращает идентификатор типа подключаемого модуля (плагина).
   * <p>
   * Пустая строка (то есть, строка нулевой длины) означает, что обрабатываюся все типы плагинов в заданных директориях.
   *
   * @return String - идентификатор типа плагинов или пустая строка
   */
  String pluginTypeId();

  /**
   * Добавить директорию (возможно, с поддиректориями) в путь поиска загружаемых модулей.
   * <p>
   * Если путь уже есть в пути поиска, то ничего не делает. Добавление нового пути приводит к автоматическому обновлению
   * списка найденных модулей, включая информирование об изменении в списке модулей.
   *
   * @param aPath File - директория, в которой будет осуществляться поиск JAR-файлов подключаемых модулей
   * @param aIncludeSubDirs boolean - включать ли поддиретории в путь поиска
   * @throws TsNullArgumentRtException aPath = null
   */
  void addPluginJarPath( File aPath, boolean aIncludeSubDirs );

  /**
   * Возвращает текущий перечень модулей, находящихся в пути поиска.
   * <p>
   * Внимание! Этот метод НЕ производит повторное сканирование директории, а использует список модулей, сформированный
   * во время последнего вызова {@link #checkChanges()}.
   *
   * @return IList&lt;IPluginInfo&gt; - список всех плагинов
   */
  IList<IPluginInfo> listPlugins();

  /**
   * Создает экземпляр класса подключаемого модуля.
   * <p>
   * Создает класс вызовом class.forName() и использованием корректного загрузчика классов из JAR-файла модуля.
   * Производит проверку зависимостей, и если они не разрешимы, выбрасывает исключение.
   *
   * @param aPluginId - идентификатор плагина
   * @return Object - созданный класс (экземпляр) плагина
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет плагина с таким идентификатором
   * @throws TsIoRtException ошибка работы с файлом плагина
   * @throws ClassNotFoundException нельзя разрешить зависимости или отсутствет файл класса в JAR-файле модуля
   */
  Object createPluginInstance( String aPluginId )
      throws ClassNotFoundException;

  /**
   * Производит сканирование директории для обнаружения изменений с перечне подключаемых модулей.<br>
   * По результатам проверки определяются новые/удаленные/измененные модули, перечень которых можно подлучить методом
   * {@link #getChanges()}. Несколько подряд проверок без чтения перечня изменений приводят к накоплению изменений.
   * Например, если сначала добавить, а потом удалить новый молуль, то результатом второй проверки будет информация об
   * отсутствии изменений!
   * <p>
   * Очевидно, что добавление нового и удаление существующего модулей приводит к соответствующим изменениям в списке.
   * Такая же ситуация в случае изменения типа и/или идентификатора подключаемого модуля - считается что предыдущий
   * модуль был удален, и появился новый. Т.е. указанный модуль окажется в списках удаленных (со старым
   * типом/идентификатором) и в списке новых, с текущими свойствами.
   * <p>
   * Понятие "измененного" модуля следует уточнить. Модуль считается измененным только при изменении версии. Изменение
   * имени, описания и т.п. не считается изменением и не приводит к обновлению списка!
   *
   * @return boolean - наличие изменений (с последнего вызова {@link #getChanges()}) в перечне найденных модулей:
   *         <ul>
   *         <li>true - именения есть, хотя бы один модуль был изменен, удален или добавлен;</li>
   *         <li>false - все по старому, если изменения и были, то они уже были отменены.</li>
   *         </ul>
   */
  boolean checkChanges();

  /**
   * Получить список новых, удаленных и измененых модулей.<br>
   * Список изменений накапливается между вызовами getChanges() и сбрасывается этим вызовом.
   *
   * @return IChangedModulesInfo - списки новых, удаленных и измененных модулей
   */
  IChangedPluginsInfo getChanges();

}
