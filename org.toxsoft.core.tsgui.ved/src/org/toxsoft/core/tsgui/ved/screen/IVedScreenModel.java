package org.toxsoft.core.tsgui.ved.screen;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tsgui.ved.screen.snippets.*;

/**
 * An editable model of the VED screen content.
 * <p>
 * TODO comment: VISELs, decorators, actors, handlers
 * <p>
 * TODO comment: what IS included in {@link IVedScreenCfg}, and what is NOT included
 *
 * @author hazard157
 */
public interface IVedScreenModel {

  IVedItemsManager<VedAbstractVisel> visels();

  IVedItemsManager<VedAbstractActor> actors();

  IVedSnippetManager<VedAbstractDecorator> screenDecoratorsBefore();

  IVedSnippetManager<VedAbstractDecorator> viselDecoratorsBefore( String aViselId );

  IVedSnippetManager<VedAbstractDecorator> viselDecoratorsAfter( String aViselId );

  IVedSnippetManager<VedAbstractDecorator> screenDecoratorsAfter();

  IVedSnippetManager<VedAbstractUserInputHandler> screenHandlersBefore();

  IVedSnippetManager<VedAbstractUserInputHandler> screenHandlersAfter();

}
