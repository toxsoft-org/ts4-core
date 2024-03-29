package org.toxsoft.core.tslib.utils.plugins;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;

// TODO TRANSLATE

/**
 * Информация об плангинах, которые изменились с момента последней проверки директорий.
 * <p>
 * Содержит информацию о новых, удаленных и измененных модулях.
 *
 * @author hazard157
 */
public interface IChangedPluginsInfo {

  /**
   * Определяет, есть ли хоть один элемент в списках модулей.<br>
   * Если изменений нет, то не имеет смысла вызывать методы listXxxPlugins() - они все вернут пустые итераторы.
   *
   * @return true - существет хотя бы один новый/удаленный/измененны модуль,<br>
   *         false - в перечне модулей нет изменений.
   */
  boolean isChanges();

  /**
   * Возвращает список новых подключаемых модулей.
   *
   * @return Iterable&lt;IPluginInfo&gt; - поставщик итератора и возможности цикла for-each
   */
  IList<IPluginInfo> listAddedPlugins();

  /**
   * Возвращает список удаленных подключаемых модулей.
   *
   * @return Iterable&lt;IPluginInfo&gt; - поставщик итератора и возможности цикла for-each
   */
  IList<IPluginInfo> listRemovedPlugins();

  /**
   * Инофрмация о подключаемом модуле с изменением версии.
   *
   * @author hazard157
   */
  public interface IChangedPluginInfo {

    /**
     * Возвращает текущую информацию о модуле.
     *
     * @return акталтная информация о подключаемом модуле
     */
    IPluginInfo pluginInfo();

    /**
     * Получить информацию о предыдущей версии модуля.
     *
     * @return {@link TsVersion} - предыдущая версия, которая была до обновления файла модуля
     */
    TsVersion oldVersion();
  }

  /**
   * Возвращает список подключаемых модулей с изменением версии.
   *
   * @return Iterable&lt;IPluginInfo&gt; - поставщик итератора и возможности цикла for-each
   */
  IList<IChangedPluginInfo> listChangedPlugins();

}
