package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tsgui.ved.screen.snippets.*;

/**
 * {@link IVedScreenModel} implementation.
 *
 * @author hazard157
 */
class VedScreenModel
    implements IVedScreenModel, ITsGuiContextable {

  private final VedScreen screen;

  VedScreenModel( VedScreen aScreen ) {
    screen = aScreen;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return screen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IVedScreenModel
  //

  @Override
  public IVedItemsManager<VedAbstractVisel> visels() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IVedItemsManager<VedAbstractActor> actors() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IVedSnippetManager<VedAbstractDecorator> screenDecoratorsBefore() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IVedSnippetManager<VedAbstractDecorator> viselDecoratorsBefore( String aViselId ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IVedSnippetManager<VedAbstractDecorator> viselDecoratorsAfter( String aViselId ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IVedSnippetManager<VedAbstractDecorator> screenDecoratorsAfter() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IVedSnippetManager<VedAbstractUserInputHandler> screenHandlersBefore() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IVedSnippetManager<VedAbstractUserInputHandler> screenHandlersAfter() {
    // TODO Auto-generated method stub
    return null;
  }

}
