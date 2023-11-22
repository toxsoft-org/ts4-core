package org.toxsoft.core.tsgui.ved.incub.undoman;

import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

public class VedUndoManager
    extends UndoManager {

  private final IVedScreen vedScreen;

  IVedItemsManagerListener<VedAbstractVisel> viselsListener = new IVedItemsManagerListener<>() {

    @Override
    public void onVedItemsListChange( IVedItemsManager<VedAbstractVisel> aSource, ECrudOp aOp, String aId ) {
      IVedScreenCfg cfg = VedEditorUtils.getVedScreenConfig( vedScreen );
      String cfgStr4Redo = VedScreenCfg.KEEPER.ent2str( cfg );
      if( !cfgStr4Undo.isBlank() ) {
        AbstractUndoRedoItem urItem = new FullInfoUndoRedoItem( vedScreen, cfgStr4Undo, cfgStr4Redo );
        urItem.setVisualParams( aOp.nmName(), aOp.description(), TsLibUtils.EMPTY_STRING );
        addUndoredoItem( urItem );
        LoggerUtils.defaultLogger().info( "Visels undo info: " + aOp.nmName() );
      }
      cfgStr4Undo = cfgStr4Redo;
    }

  };

  IVedItemsManagerListener<VedAbstractActor> actorsListener = new IVedItemsManagerListener<>() {

    @Override
    public void onVedItemsListChange( IVedItemsManager<VedAbstractActor> aSource, ECrudOp aOp, String aId ) {
      IVedScreenCfg cfg = VedEditorUtils.getVedScreenConfig( vedScreen );
      String cfgStr4Redo = VedScreenCfg.KEEPER.ent2str( cfg );
      addUndoredoItem( new FullInfoUndoRedoItem( vedScreen, cfgStr4Undo, cfgStr4Redo ) );
      cfgStr4Undo = cfgStr4Redo;
      LoggerUtils.defaultLogger().info( "Actors undo info: " + aOp.nmName() );
    }

  };

  String cfgStr4Undo = TsLibUtils.EMPTY_STRING;

  public VedUndoManager( IVedScreen aVedScreen ) {
    vedScreen = aVedScreen;
    vedScreen.model().visels().eventer().addListener( viselsListener );
    vedScreen.model().actors().eventer().addListener( actorsListener );
    IVedScreenCfg cfg = VedEditorUtils.getVedScreenConfig( vedScreen );
    cfgStr4Undo = VedScreenCfg.KEEPER.ent2str( cfg );
  }

}
