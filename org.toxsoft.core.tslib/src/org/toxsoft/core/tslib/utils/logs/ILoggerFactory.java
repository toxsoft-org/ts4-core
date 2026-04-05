package org.toxsoft.core.tslib.utils.logs;

import org.toxsoft.core.tslib.utils.errors.*;

// TODO: TRANSLATE!

/**
 * Фабрика журналов компонентов системы.
 *
 * @author mvk
 */
public interface ILoggerFactory {

  /**
   * Возвращает журнал для отдельного компонента.
   *
   * @param aName String имя компонента
   * @return {@link ILogger} журнал
   * @throws TsNullArgumentRtException аргумент = null
   */
  ILogger getLogger( String aName );

}
