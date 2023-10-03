package org.toxsoft.core.tsgui.ved.api.screen;

import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.*;

/**
 * An editable model of the VED screen content.
 * <p>
 * TODO comment: VISELs, decorators, actors, handlers
 * <p>
 * TODO comment: what IS included in {@link IVedScreenCfg}, and what is NOT included
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" ) // TODO comment methods
public interface IVedScreenModel {

  // ------------------------------------------------------------------------------------
  // VISELs

  INotifierStridablesList<VedAbstractVisel> activeVisels();

  INotifierStridablesListEdit<VedAbstractVisel> allVisels();

  // ------------------------------------------------------------------------------------
  // Decorators

  INotifierListEdit<IVedDecorator> viselDecoratorsBefore( String aViselId );

  INotifierListEdit<IVedDecorator> viselDecoratorsAfter( String aViselId );

  INotifierListEdit<IVedDecorator> screenDecoratorsBefore();

  INotifierListEdit<IVedDecorator> screenDecoratorsAfter();

  // ------------------------------------------------------------------------------------
  // Actors

  INotifierStridablesList<VedAbstractActor> activeActors();

  INotifierStridablesListEdit<VedAbstractActor> allActors();

  // ------------------------------------------------------------------------------------
  // Input handlers

  INotifierListEdit<ITsUserInputListener> screenHandlersBefore();

  INotifierListEdit<ITsUserInputListener> screenHandlersAfter();

}
