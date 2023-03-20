package org.toxsoft.core.tslib.bricks.ctx;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Read-only context contains arbitrary references and options set.
 * <p>
 * Note: depending on implementation and creation of instance, context may be @child" of the "parent" context. For child
 * contexts methods <code>isSelfXxx()</code> determines if reference or option is hold by this instance, or supplied by
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
   * Finds a reference registered in the context by Java class.
   *
   * @param <T> - expected type of the reference
   * @param aClass {@link Class} - expected type of the reference
   * @return &lt;T&gt; - the found reference or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  <T> T find( Class<T> aClass );

  /**
   * Returns a reference registered in the context by Java class.
   *
   * @param <T> - expected type of the reference
   * @param aClass {@link Class} - expected type of the reference
   * @return &lt;T&gt; - the found reference
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException asked reference not found in the context
   */
  <T> T get( Class<T> aClass );

  /**
   * Finds a reference registered in the context by the String key.
   *
   * @param aName String - the key
   * @return &lt;T&gt; - the found reference or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  Object find( String aName );

  /**
   * Finds a reference registered in the context by the String key.
   *
   * @param aName String - the key
   * @return Object - the found reference
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException asked reference not found in the context
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
  // convenience in-line methods
  //

  /**
   * Finds a reference of expected type registered in the context by the String key.
   * <p>
   * Returns <code>null</code> if either no reference found b key or if found reference is not of specified type.
   *
   * @param <T> - expected type of the reference
   * @param aName String - the key
   * @param aClass {@link Class} - expected type of the reference
   * @return &lt;T&gt; - the found reference or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
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
   * Returns a reference of expected type registered in the context by the String key.
   *
   * @param <T> - expected type of the reference
   * @param aName String - the key
   * @param aClass {@link Class} - expected type of the reference
   * @return &lt;T&gt; - the found reference
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException asked reference not found in the context
   * @throws ClassCastException found reference is not of specified type
   */
  default <T> T getRef( String aName, Class<T> aClass ) {
    TsNullArgumentRtException.checkNull( aClass );
    Object ref = find( aName );
    if( ref != null ) {
      return aClass.cast( ref );
    }
    return null;
  }

  /**
   * Determines a reference exists in the context by Java class.
   *
   * @param aClass {@link Class} - expected type of the reference
   * @return boolean - <code>true</code> if there is a reference in the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  default boolean hasKey( Class<?> aClass ) {
    TsNullArgumentRtException.checkNull( aClass );
    return find( aClass ) != null;
  }

  /**
   * Determines a reference exists in the context by the String key.
   *
   * @param aName String - the key
   * @return boolean - <code>true</code> if there is a reference in the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  default boolean hasKey( String aName ) {
    return find( aName ) != null;
  }

}
