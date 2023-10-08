package org.toxsoft.core.tsgui.ved.screen;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tsgui.ved.screen.snippets.*;
import org.toxsoft.core.tslib.utils.errors.*;

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

  /**
   * Returns the VISELs.
   *
   * @return {@link IVedItemsManager}&lt;{@link VedAbstractActor}&gt; - the VISELs manager
   */
  IVedItemsManager<VedAbstractVisel> visels();

  /**
   * Returns the actors.
   *
   * @return {@link IVedItemsManager}&lt;{@link VedAbstractActor}&gt; - the actors manager
   */
  IVedItemsManager<VedAbstractActor> actors();

  /**
   * Returns the decorators drawn before all VISELs.
   *
   * @return {@link IVedSnippetManager}&lt;{@link VedAbstractUserInputHandler}&gt; - decorators manager
   */
  IVedSnippetManager<VedAbstractDecorator> screenDecoratorsBefore();

  /**
   * Returns the decorators drawn before the specified VISEL.
   *
   * @param aViselId String - the VISEL ID
   * @return {@link IVedSnippetManager}&lt;{@link VedAbstractUserInputHandler}&gt; - decorators manager
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException a VISEL with the specified ID does not exists
   */
  IVedSnippetManager<VedAbstractDecorator> viselDecoratorsBefore( String aViselId );

  /**
   * Returns the decorators drawn after the specified VISEL.
   *
   * @param aViselId String - the VISEL ID
   * @return {@link IVedSnippetManager}&lt;{@link VedAbstractUserInputHandler}&gt; - decorators manager
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException a VISEL with the specified ID does not exists
   */
  IVedSnippetManager<VedAbstractDecorator> viselDecoratorsAfter( String aViselId );

  /**
   * Returns the decorators drawn after all VISELs.
   *
   * @return {@link IVedSnippetManager}&lt;{@link VedAbstractUserInputHandler}&gt; - decorators manager
   */
  IVedSnippetManager<VedAbstractDecorator> screenDecoratorsAfter();

  /**
   * Returns the user input handlers processed before actors.
   *
   * @return {@link IVedSnippetManager}&lt;{@link VedAbstractUserInputHandler}&gt; - handles manager
   */
  IVedSnippetManager<VedAbstractUserInputHandler> screenHandlersBefore();

  /**
   * Returns the user input handlers processed after actors.
   *
   * @return {@link IVedSnippetManager}&lt;{@link VedAbstractUserInputHandler}&gt; - handles manager
   */
  IVedSnippetManager<VedAbstractUserInputHandler> screenHandlersAfter();

}
