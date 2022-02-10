package org.toxsoft.core.tsgui.bricks.ctx;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRefDef;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Extends {@link ITsContextRefDef} with methos to access {@link IEclipseContext}.
 *
 * @author hazard157
 * @param <T> - the reference type
 */
public interface ITsGuiContextRefDef<T>
    extends ITsContextRefDef<T> {

  /**
   * Retrieves the reference from the context.
   *
   * @param aContext {@link IEclipseContext} - the context
   * @return &lt;T&gt; - reference from the context or <code>null</code> if non-mandatory reference does not extsts
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException mandatory reference not found in the context
   * @throws ClassCastException found reference is not of expected type {@link #refClass()}
   */
  T getRef( IEclipseContext aContext );

  /**
   * Retrieves the reference from the context.
   *
   * @param aContext {@link IEclipseContext} - the context
   * @param aDefaultRef &lt;T&gt; - references to be returned if not found in context, may be <code>null</code>
   * @return &lt;T&gt; - reference from the context or <code>aDefaultRef</code> if reference does not extsts
   * @throws TsNullArgumentRtException <code>aContext</code> = <code>null</code>
   * @throws ClassCastException found reference is not of expected type {@link #refClass()}
   */
  T getRef( IEclipseContext aContext, T aDefaultRef );

  /**
   * Puts the reference in the context.
   *
   * @param aContext {@link IEclipseContext} - the context
   * @param aRef &lt;T&gt; - the reference, must not be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws ClassCastException <code>aRef</code> reference is not of expected type {@link #refClass()}
   */
  void setRef( IEclipseContext aContext, T aRef );

}
