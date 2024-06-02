package org.toxsoft.core.tslib.utils.plugins.impl;

import org.toxsoft.core.tslib.bricks.events.ITsEventer;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.plugins.IPluginInfo;
import org.toxsoft.core.tslib.utils.plugins.IPluginListener;

/**
 * Загруженный экземпляр плагина
 *
 * @author mvk
 */
public sealed interface IPlugin permits Plugin {

  /**
   * Возвращает информацию о плагине.
   *
   * @return {@link IPluginInfo} информация о плагине.
   */
  IPluginInfo info();

  /**
   * Возвращает загрузчик классов плагина.
   *
   * @return {@link ClassLoader} загрузчик классов.
   */
  ClassLoader classLoader();

  /**
   * Возвращает экземпляр плагина, то есть созданный объект загруженного класса.
   *
   * @param aInstanceClass java-класс объекта.
   * @param <T> тип объекта.
   * @return {@link Object} экземпляр плагина.
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException недопустимый тип объекта.
   */
  <T> T instance( Class<T> aInstanceClass );

  /**
   * Returns the plugin eventer.
   *
   * @return {@link ITsEventer}&lt;{@link IPluginListener}&gt; - the plugin eventer
   */
  ITsEventer<IPluginListener> eventer();
}
