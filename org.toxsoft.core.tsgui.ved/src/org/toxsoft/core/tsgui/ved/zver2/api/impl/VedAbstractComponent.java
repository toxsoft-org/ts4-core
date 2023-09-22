package org.toxsoft.core.tsgui.ved.zver2.api.impl;

import org.toxsoft.core.tsgui.ved.zver2.api.*;
import org.toxsoft.core.tsgui.ved.zver2.api.comp.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedComponent} base implementation.
 *
 * @author hazard157
 */
public abstract class VedAbstractComponent
    extends VedEntityBase
    implements IVedComponent {

  /**
   * Contsructor.
   *
   * @param aProvider {@link VedAbstractEntityProvider} - the creator
   * @param aVedEnv {@link IVedEnvironment} - environment for component creation
   * @param aId String - conmonent ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public VedAbstractComponent( VedAbstractEntityProvider aProvider, IVedEnvironment aVedEnv, String aId ) {
    super( aProvider, aVedEnv, aId );
  }

  // ------------------------------------------------------------------------------------
  // IVedComponent
  //

  @Override
  final public VedAbstractComponentView createView( IVedScreen aScreen ) {
    TsNullArgumentRtException.checkNull( aScreen );
    Object rawView = doCreateView( aScreen );
    if( rawView instanceof VedAbstractComponentView v ) {
      TsInternalErrorRtException.checkTrue( v.ownerScreen() != aScreen );
      TsInternalErrorRtException.checkTrue( v.component() != this );
      v.papiInitialUpdate();
      return v;
    }
    throw new TsInternalErrorRtException();
  }

  // ------------------------------------------------------------------------------------
  // To implements
  //

  /**
   * Subclass must create the view instance.
   *
   * @param aScreen {@link IVedScreen} - the screen
   * @return {@link VedAbstractComponentView} - created instance
   */
  protected abstract VedAbstractComponentView doCreateView( IVedScreen aScreen );

}
