package org.toxsoft.core.tslib.bricks.ctx;

import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.utils.IParameterized;
import org.toxsoft.core.tslib.utils.ITsPausabeEventsProducer;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Readonly context contains arbitrary references and options set.
 *
 * @author hazard157
 */
public interface ITsContextRo
    extends ITsPausabeEventsProducer, IParameterized {

  /**
   * Returns the context options set.
   *
   * @return {@link IOptionSet} - the context options set
   */
  @Override
  IOptionSet params();

  /**
   * Returns the parent TS context.
   *
   * @return {@link ITsContextRo} - the parent context or <code>null</code> for the root context
   */
  ITsContextRo parent();

  /**
   * Находит произвольную ссылку, зарегистрированную в контексте по Java-классу.
   *
   * @param <T> - конкретный тип (класс) ссылки
   * @param aClass {@link Class} - Java-класс искомой ссылки
   * @return &lt;T&gt; - найденная ссылка, или null если нет такой ссылки
   * @throws TsNullArgumentRtException аргумент = null
   */
  <T> T find( Class<T> aClass );

  /**
   * Возвращает произвольную ссылку, зарегистрированную в контексте по Java-классу.
   *
   * @param <T> - конкретный тип (класс) ссылки
   * @param aClass {@link Class} - Java-класс искомой ссылки
   * @return &lt;T&gt; - найденная ссылка
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет запрошенной ссылки в контексте
   */
  <T> T get( Class<T> aClass );

  /**
   * Возвращает произвольную ссылку, зарегистрированную в контексте по текстовой строке (по имени).
   *
   * @param aName String - строковый ключ в карте ссылок
   * @return Object - найденная ссылка, или null если нет такой ссылки
   * @throws TsNullArgumentRtException аргумент = null
   */
  Object find( String aName );

  /**
   * Возвращает произвольную ссылку, зарегистрированную в контексте по текстовой строке (по имени).
   *
   * @param aName String - строковый ключ в карте ссылок
   * @return Object - найденная ссылка
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет запрошенной ссылки в контексте
   */
  Object get( String aName );

  /**
   * Adds the listener.
   * <p>
   * Method does nothing if the listener was already added before.
   *
   * @param aListener {@link ITsContextListener} - the listener
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  void addContextListener( ITsContextListener aListener );

  /**
   * Removes the listener.
   * <p>
   * Method does nothing if the listener was not added before.
   *
   * @param aListener {@link ITsContextListener} - the listener
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  void removeContextListener( ITsContextListener aListener );

  // ------------------------------------------------------------------------------------
  // Inline convinience methods
  //

  /**
   * Находит ссылку указанного типа по имени.
   *
   * @param <T> - тип ссылки
   * @param aName String - строковый ключ в карте ссылок
   * @param aClass {@link Class}&lt;T&gt; - класс сслыки
   * @return &lt;T&gt; - найденная ссылка или null если нет ссылки с таким именем или этого класса
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  default <T> T findRef( String aName, Class<T> aClass ) {
    TsNullArgumentRtException.checkNull( aClass );
    Object ref = find( aName );
    if( ref != null ) {
      if( aClass.isInstance( ref ) ) {
        return aClass.cast( ref );
      }
    }
    return null;
  }

  /**
   * Возвращает ссылку указанного типа по имени.
   *
   * @param <T> - тип ссылки
   * @param aName String - строковый ключ в карте ссылок
   * @param aClass {@link Class}&lt;T&gt; - класс сслыки
   * @return &lt;T&gt; - найденная ссылка
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemNotFoundRtException нет запрошенной ссылки в контексте
   * @throws ClassCastException ссылка не указанного типа
   */
  default <T> T getRef( String aName, Class<T> aClass ) {
    TsNullArgumentRtException.checkNull( aClass );
    Object ref = get( aName );
    return aClass.cast( ref );
  }

  /**
   * Определяет, содержит ли контекст ссылку с указанным ключом.
   *
   * @param aClass {@link Class} - Java-класс искомой ссылки
   * @return boolean - признак существования ссылки с указанным ключом
   * @throws TsNullArgumentRtException аргумент = null
   */
  default boolean hasKey( Class<?> aClass ) {
    TsNullArgumentRtException.checkNull( aClass );
    return find( aClass ) != null;
  }

  /**
   * Определяет, содержит ли контекст ссылку с указанным ключом.
   *
   * @param aName String - строковый ключ в карте ссылок
   * @return boolean - признак существования ссылки с указанным ключом
   * @throws TsNullArgumentRtException аргумент = null
   */
  default boolean hasKey( String aName ) {
    return find( aName ) != null;
  }

}
