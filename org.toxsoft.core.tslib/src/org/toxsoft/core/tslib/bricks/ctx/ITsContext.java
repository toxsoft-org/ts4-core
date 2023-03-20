package org.toxsoft.core.tslib.bricks.ctx;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An editable extension of the {@link ITsContextRo}.
 *
 * @author hazard157
 */
public interface ITsContext
    extends ITsContextRo {

  /**
   * Returns a context parameters ion the editable form.
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
   * Puts the reference to the map replacing existing one.
   * <p>
   * The reference is placed in this context, becoming available to child contexts, but not to the parent.
   *
   * @param <T> - the reference type
   * @param aClass {@link Class} - the key is the type of the reference
   * @param aRef Object - the reference
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  <T> void put( Class<T> aClass, T aRef );

  /**
   * Puts the reference to the map replacing existing one.
   * <p>
   * The reference is placed in this context, becoming available to child contexts, but not to the parent.
   *
   * @param aName String - the String key
   * @param aRef Object - the reference
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void put( String aName, Object aRef );

  /**
   * Removes the reference from this context.
   * <p>
   * If there is no such reference in this context (even if it exists in parents or children), the method does nothing.
   *
   * @param aClass {@link Class} - the key
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void remove( Class<?> aClass );

  /**
   * Removes the reference from this context.
   * <p>
   * If there is no such reference in this context (even if it exists in parents or children), the method does nothing.
   *
   * @param aName String - the String key
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void remove( String aName );

}
