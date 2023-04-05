package org.toxsoft.core.tslib.bricks.ctx;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The meta-information about reference in the context {@link ITsContext}.
 *
 * @author hazard157
 * @param <T> - the reference type
 */
public interface ITsContextRefDef<T>
    extends IParameterized {

  /**
   * Returns the key of the reference in the context.
   *
   * @return String - the key, never is an empty string or <code>null</code>
   */
  String refKey();

  /**
   * Returns the reference type.
   *
   * @return {@link Class}&lt;T&gt; - the reference type, never is <code>null</code>
   */
  Class<T> refClass();

  /**
   * Returns the flag that this reference must present in context.
   *
   * @return boolean - <code>true</code> if reference must present in context
   */
  default boolean isMandatory() {
    return params().getBool( TSID_IS_MANDATORY, false );
  }

  /**
   * Retrieves the reference from the context.
   *
   * @param aContext {@link ITsContextRo} - the context
   * @return &lt;T&gt; - reference from the context or <code>null</code> if non-mandatory reference does not extsts
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException mandatory reference not found in the context
   * @throws ClassCastException found reference is not of expected type {@link #refClass()}
   */
  T getRef( ITsContextRo aContext );

  /**
   * Retrieves the reference from the context.
   *
   * @param aContext {@link ITsContextRo} - the context
   * @param aDefaultRef &lt;T&gt; - references to be returned if not found in context, may be <code>null</code>
   * @return &lt;T&gt; - reference from the context or <code>aDefaultRef</code> if reference does not extsts
   * @throws TsNullArgumentRtException <code>aContext</code> = <code>null</code>
   * @throws ClassCastException found reference is not of expected type {@link #refClass()}
   */
  T getRef( ITsContextRo aContext, T aDefaultRef );

  /**
   * Puts the reference in the context.
   *
   * @param aContext {@link ITsContext} - the context
   * @param aRef &lt;T&gt; - the reference, must not be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws ClassCastException <code>aRef</code> reference is not of expected type {@link #refClass()}
   */
  void setRef( ITsContext aContext, T aRef );

}
