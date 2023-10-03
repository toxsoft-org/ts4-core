package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tsgui.ved.api.screen.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedEnvironment} implementation.
 *
 * @author hazard157
 */
public class VedScreenModel
    implements IVedScreenModel, ITsGuiContextable, ITsClearable {

  // static class ViselsValidator
  // implements ITsMapValidator<String, IStridable> {
  //
  // }

  private final ITsGuiContext tsContext;

  // VISELs
  private final INotifierStridablesListEdit<VedAbstractVisel> allVisels    =
      new NotifierStridablesListEditWrapper<>( new StridablesList<>() );
  private final INotifierStridablesListEdit<VedAbstractVisel> activeVisels =
      new NotifierStridablesListEditWrapper<>( new StridablesList<>() );

  // decorators
  private final IStringMap<INotifierListEdit<IVedDecorator>> viselDecoratorsBefore  = new StringMap<>();
  private final IStringMap<INotifierListEdit<IVedDecorator>> viselDecoratorsAfter   = new StringMap<>();
  private final INotifierListEdit<IVedDecorator>             screenDecoratorsBefore =
      new NotifierListEditWrapper<>( new ElemArrayList<>() );
  private final INotifierListEdit<IVedDecorator>             screenDecoratorsAfter  =
      new NotifierListEditWrapper<>( new ElemArrayList<>() );

  // actors
  private final INotifierStridablesListEdit<VedAbstractActor> allActors    =
      new NotifierStridablesListEditWrapper<>( new StridablesList<>() );
  private final INotifierStridablesListEdit<VedAbstractActor> activeActors =
      new NotifierStridablesListEditWrapper<>( new StridablesList<>() );

  // handlers
  private final INotifierListEdit<ITsUserInputListener> screenHandlersBefore =
      new NotifierListEditWrapper<>( new ElemArrayList<>() );
  private final INotifierListEdit<ITsUserInputListener> screenHandlersAfter  =
      new NotifierListEditWrapper<>( new ElemArrayList<>() );

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedScreenModel( ITsGuiContext aTsContext ) {
    tsContext = aTsContext;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IVedEnvironment
  //

  @Override
  public INotifierStridablesList<VedAbstractVisel> activeVisels() {
    return activeVisels;
  }

  @Override
  public INotifierStridablesListEdit<VedAbstractVisel> allVisels() {
    return allVisels;
  }

  @Override
  public INotifierListEdit<IVedDecorator> viselDecoratorsBefore( String aViselId ) {
    TsItemNotFoundRtException.checkFalse( allVisels.hasKey( aViselId ) );
    INotifierListEdit<IVedDecorator> ll = viselDecoratorsBefore.findByKey( aViselId );
    TsInternalErrorRtException.checkNull( ll );
    return ll;
  }

  @Override
  public INotifierListEdit<IVedDecorator> viselDecoratorsAfter( String aViselId ) {
    TsItemNotFoundRtException.checkFalse( allVisels.hasKey( aViselId ) );
    INotifierListEdit<IVedDecorator> ll = viselDecoratorsAfter.findByKey( aViselId );
    TsInternalErrorRtException.checkNull( ll );
    return ll;
  }

  @Override
  public INotifierListEdit<IVedDecorator> screenDecoratorsBefore() {
    return screenDecoratorsBefore;
  }

  @Override
  public INotifierListEdit<IVedDecorator> screenDecoratorsAfter() {
    return screenDecoratorsAfter;
  }

  @Override
  public INotifierStridablesList<VedAbstractActor> activeActors() {
    return activeActors;
  }

  @Override
  public INotifierStridablesListEdit<VedAbstractActor> allActors() {
    return allActors;
  }

  @Override
  public INotifierListEdit<ITsUserInputListener> screenHandlersBefore() {
    return screenHandlersBefore;
  }

  @Override
  public INotifierListEdit<ITsUserInputListener> screenHandlersAfter() {
    return screenHandlersAfter;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  /**
   * Removes and disposes all items.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    allActors.pauseFiring();
    activeActors.pauseFiring();
    allVisels.pauseFiring();
    activeVisels.pauseFiring();
    try {
      // remove and dispose actors
      activeActors.clear();
      while( !allActors.isEmpty() ) {
        VedAbstractActor actor = allActors.last();
        // actor.disable();
        actor.dispose();
        allActors.remove( actor );
      }
      // remove and dispose VISELs
      activeVisels.clear();
      while( !allVisels.isEmpty() ) {
        VedAbstractVisel visel = allVisels.last();
        // visel.hide();
        visel.dispose();
        allVisels.remove( visel );
      }
      // TODO remove and dispose decorators
      // TODO remove and dispose handlers
    }
    finally {
      allActors.resumeFiring( true );
      activeActors.resumeFiring( true );
      allVisels.resumeFiring( true );
      activeVisels.resumeFiring( true );
    }
  }

}
