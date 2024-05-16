package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tsgui.ved.screen.snippets.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.txtproj.lib.storage.*;

/**
 * {@link IVedScreenModel} implementation.
 *
 * @author hazard157
 */
public class VedScreenModel
    implements IVedScreenModel, ITsGuiContextable, ICloseable {

  /**
   * TODO add change event generator and listen to the published collections
   */

  private final AbstractVedItemsManager<VedAbstractVisel> visels;
  private final AbstractVedItemsManager<VedAbstractActor> actors;
  private KeepablesStorageAsKeepable                      extraData = new KeepablesStorageAsKeepable();

  private final VedSnippetManager<VedAbstractDecorator>        screenDecoratorsBefore = new VedSnippetManager<>();
  private final VedSnippetManager<VedAbstractDecorator>        screenDecoratorsAfter  = new VedSnippetManager<>();
  private final VedSnippetManager<VedAbstractUserInputHandler> screenHandlersBefore   = new VedSnippetManager<>();
  private final VedSnippetManager<VedAbstractUserInputHandler> screenHandlersAfter    = new VedSnippetManager<>();

  private final IStringMapEdit<VedSnippetManager<VedAbstractDecorator>> viselDecoratorsBefore = new StringMap<>();
  private final IStringMapEdit<VedSnippetManager<VedAbstractDecorator>> viselDecoratorsAfter  = new StringMap<>();

  private final VedScreen screen;

  VedScreenModel( VedScreen aScreen ) {
    screen = aScreen;
    IVedViselFactoriesRegistry viselFactoriesRegistry = tsContext().get( IVedViselFactoriesRegistry.class );
    visels = new AbstractVedItemsManager<>( screen ) {

      @Override
      protected IVedItemFactoryBase<VedAbstractVisel> doFindFactory( IVedItemCfg aCfg ) {
        return viselFactoriesRegistry.find( aCfg.factoryId() );
      }
    };
    IVedActorFactoriesRegistry actorFactoriesRegistry = tsContext().get( IVedActorFactoriesRegistry.class );
    actors = new AbstractVedItemsManager<>( screen ) {

      @Override
      protected IVedItemFactoryBase<VedAbstractActor> doFindFactory( IVedItemCfg aCfg ) {
        return actorFactoriesRegistry.find( aCfg.factoryId() );
      }
    };
    // setup
    visels.eventer().addListener( ( src, op, id ) -> {
      refreshMapOfViselDecoratorsManagers( viselDecoratorsBefore );
      refreshMapOfViselDecoratorsManagers( viselDecoratorsAfter );
    } );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Updates VISEL decorator managers map to contain only managers for VISELs listed in {@link #visels}.
   *
   * @param aMap {@link IStringMapEdit}&lt;{@link VedSnippetManager}&gt; - the map to update
   */
  private void refreshMapOfViselDecoratorsManagers( IStringMapEdit<VedSnippetManager<VedAbstractDecorator>> aMap ) {
    // new map with actual managers
    IStringMapEdit<VedSnippetManager<VedAbstractDecorator>> map = new StringMap<>();
    for( VedAbstractVisel v : visels.list() ) {
      VedSnippetManager<VedAbstractDecorator> sm = aMap.removeByKey( v.id() );
      if( sm == null ) {
        sm = new VedSnippetManager<>();
      }
      map.put( v.id(), sm );
    }
    // dispose unneeded managers
    while( !aMap.isEmpty() ) {
      VedSnippetManager<VedAbstractDecorator> sm = aMap.removeByKey( aMap.keys().first() );
      sm.close();
    }
    //
    aMap.putAll( map );
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
    return visels;
  }

  @Override
  public IVedItemsManager<VedAbstractActor> actors() {
    return actors;
  }

  @Override
  public KeepablesStorageAsKeepable extraData() {
    return extraData;
  }

  @Override
  public IVedSnippetManager<VedAbstractDecorator> screenDecoratorsBefore() {
    return screenDecoratorsBefore;
  }

  @Override
  public IVedSnippetManager<VedAbstractDecorator> viselDecoratorsBefore( String aViselId ) {
    return viselDecoratorsBefore.getByKey( aViselId );
  }

  @Override
  public IVedSnippetManager<VedAbstractDecorator> viselDecoratorsAfter( String aViselId ) {
    return viselDecoratorsAfter.getByKey( aViselId );
  }

  @Override
  public IVedSnippetManager<VedAbstractDecorator> screenDecoratorsAfter() {
    return screenDecoratorsAfter;
  }

  @Override
  public IVedSnippetManager<VedAbstractUserInputHandler> screenHandlersBefore() {
    return screenHandlersBefore;
  }

  @Override
  public IVedSnippetManager<VedAbstractUserInputHandler> screenHandlersAfter() {
    return screenHandlersAfter;
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    screenHandlersAfter.close();
    screenHandlersBefore.close();
    screenDecoratorsAfter.close();
    screenDecoratorsBefore.close();
    while( !viselDecoratorsAfter.isEmpty() ) {
      VedSnippetManager<VedAbstractDecorator> sm =
          viselDecoratorsAfter.removeByKey( viselDecoratorsAfter.keys().first() );
      sm.close();
    }
    while( !viselDecoratorsBefore.isEmpty() ) {
      VedSnippetManager<VedAbstractDecorator> sm =
          viselDecoratorsBefore.removeByKey( viselDecoratorsBefore.keys().first() );
      sm.close();
    }
    actors.close();
    visels.close();
  }

}
