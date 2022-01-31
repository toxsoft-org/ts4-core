package org.toxsoft.core.tslib.bricks.ctx;

import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * An edtable extention of the {@link ITsContextRo}.
 *
 * @author hazard157
 */
public interface ITsContext
    extends ITsContextRo {

  /**
   * Returns an edtable context parameters.
   *
   * @return {@link IOptionSetEdit} - editable parameters
   */
  @Override
  IOptionSetEdit params();

  /**
   * Returns the parent TS context.
   *
   * @return {@link ITsContextRo} - the parent context or <code>null</code> for the root context
   */
  @Override
  ITsContextRo parent();

  /**
   * Добавляет элемент в карту, заменяя существующий с тем же ключом.
   * <p>
   * Ссылка кладется в этот контекст, становясь доступным наследникам-контекстам, но не родителю.
   *
   * @param <T> - конкретный тип (класс) добавляемой ссылки
   * @param aClass {@link Class} - ключ добавляемого элемента
   * @param aRef Object - добавляемая ссылка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  <T> void put( Class<T> aClass, T aRef );

  /**
   * Добавляет элемент в карту, заменяя существующий с тем же ключом.
   * <p>
   * Ссылка кладется в этот контекст, становясь доступным наследникам-контекстам, но не родителю.
   *
   * @param aName String - ключ добавляемого элемента
   * @param aRef Object - добавляемая ссылка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void put( String aName, Object aRef );

  /**
   * Удаляет ссылку по ключу - имени из текущего контекста.
   * <p>
   * Если такого элемента нет в этом контексте (даже если есть в родителях или наследниках), метод ничего не делает.
   *
   * @param aClass {@link Class} - ключ удаляемого элемента
   * @throws TsNullArgumentRtException аргумент = null
   */
  void remove( Class<?> aClass );

  /**
   * Удаляет ссылку по ключу - имени из текущего контекста.
   * <p>
   * Если такого элемента нет в этом контексте (даже если есть в родителях или наследниках), метод ничего не делает.
   *
   * @param aName String - ключ удаляемого элемента
   * @throws TsNullArgumentRtException аргумент = null
   */
  void remove( String aName );

}
