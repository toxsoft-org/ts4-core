package org.toxsoft.core.tslib.bricks.ctx;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Readonly context contains arbitrary references and options set.
 * <p>
 * Note: depending on implementation and creation of instance, context may be @child" of the "parent" context. For child
 * copntexts methods <code>isSelfXxx()</code> determines if reference or option is hold by this instance, or supplied by
 * the parent.
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
   * Determines if the option is hold by this instance, or supplied by the parent hierarchy.
   * <p>
   * Returning <code>false</code> may mean either that option is supplied by parent hierarhy or not found al all.
   *
   * @param aClass {@link Class} - the class of the reference
   * @return boolean - <code>true</code> option value is in this context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean isSelfRef( Class<?> aClass );

  /**
   * Determines if the option is hold by this instance, or supplied by the parent hierarchy.
   * <p>
   * Returning <code>false</code> may mean either that option is supplied by parent hierarhy or not found al all.
   *
   * @param aName String - the referennce name
   * @return boolean - <code>true</code> option value is in this context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean isSelfRef( String aName );

  /**
   * Determines if the option is hold by this instance, or supplied by the parent hierarchy.
   * <p>
   * Returning <code>false</code> may mean either that option is supplied by parent hierarhy or not found al all.
   *
   * @param aOptionId String - the option ID
   * @return boolean - <code>true</code> option value is in this context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean isSelfOption( String aOptionId );

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
