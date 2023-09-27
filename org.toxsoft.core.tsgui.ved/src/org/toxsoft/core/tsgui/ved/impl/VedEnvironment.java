package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedEnvironment} implementation.
 *
 * @author hazard157
 */
public class VedEnvironment
    implements IVedEnvironment, ITsClearable {

  private final ITsGuiContext tsContext;

  private final IStridablesListEdit<VedAbstractVisel> viselsList = new StridablesList<>();
  private final IStridablesListEdit<VedAbstractActor> actorsList = new StridablesList<>();

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedEnvironment( ITsGuiContext aTsContext ) {
    tsContext = aTsContext;
  }

  // ------------------------------------------------------------------------------------
  // IVedEnvironment
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IVedEnvironment
  //

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  public IStridablesList<IVedVisel> visels() {
    return (IStridablesList)viselsList;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  public IStridablesList<IVedActor> actors() {
    return (IStridablesList)actorsList;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns an editable list of the VISELs.
   *
   * @return {@link IStridablesListEdit}&lt;{@link VedAbstractVisel}&gt; - the ordered list of VISELs
   */
  public IStridablesListEdit<VedAbstractVisel> viselsList() {
    return viselsList;
  }

  /**
   * Returns an editable list of the actors.
   *
   * @return {@link IStridablesListEdit}&lt;{@link VedAbstractActor}&gt; - the ordered list of actors
   */
  public IStridablesListEdit<VedAbstractActor> actorsList() {
    return actorsList;
  }

  public void createItems( IVedScreenCfg aScreenCfg ) {

  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    // remove and dispose actors
    while( !actorsList.isEmpty() ) {
      VedAbstractActor actor = actorsList.last();
      // actor.disable();
      // actor.dispose();
      actorsList.remove( actor );
    }
    // remove and dispose VISELs
    while( !viselsList.isEmpty() ) {
      VedAbstractVisel visel = viselsList.last();
      // visel.disable();
      // visel.dispose();
      viselsList.remove( visel );
    }

    // TODO Auto-generated method stub

  }

}
