package org.toxsoft.core.tsgui.ved.api.screen;

import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tsgui.ved.impl.*;

/**
 * An editable model of the VED screen content.
 * <p>
 * TODO comment: VISELs, decorators, actors, handlers
 * <p>
 * TODO comment: what IS included in {@link IVedScreenCfg}, and what is NOT included
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" ) // TODO comments
public interface IVedScreenModel {

  IVedItemsManager<VedAbstractVisel> visels();

  IVedItemsManager<VedAbstractActor> actors();

  IVedHelpersModel<IVedDecorator> viselDecoratorsBefore( String aViselId );

  IVedHelpersModel<IVedDecorator> viselDecoratorsAfter( String aViselId );

  IVedHelpersModel<IVedDecorator> screenDecoratorsBefore();

  IVedHelpersModel<IVedDecorator> screenDecoratorsAfter();

  IVedHelpersModel<ITsUserInputListener> screenHandlersBefore();

  IVedHelpersModel<ITsUserInputListener> screenHandlersAfter();

}
