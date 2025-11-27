package org.toxsoft.core.tsgui.bricks.tstree.tmm;

import java.util.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITreeModeManager} base implementation.
 *
 * @author hazard157
 * @param <T> - displayed M5-modeled entity type
 */
public class TreeModeManager<T>
    implements ITreeModeManager<T> {

  private final GenericChangeEventer eventer;

  private final INotifierStridablesListEdit<TreeModeInfo<T>> treeModeInfoes =
      new NotifierStridablesListEditWrapper<>( new StridablesList<>() );

  private boolean hasTreeMode;

  private String modeId     = null;
  private String lastModeId = null;

  /**
   * Constructor.
   *
   * @param aHasTreeMode boolean - tree modes existance flag
   */
  public TreeModeManager( boolean aHasTreeMode ) {
    hasTreeMode = aHasTreeMode;
    eventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // ITreeModeManager
  //

  @Override
  public boolean hasTreeMode() {
    return hasTreeMode;
  }

  @Override
  public void setHasTreeMode( boolean aValue ) {
    if( hasTreeMode != aValue ) {
      hasTreeMode = aValue;
      if( !hasTreeMode ) {
        modeId = null;
      }
      eventer.fireChangeEvent();
    }
  }

  @Override
  public INotifierStridablesList<TreeModeInfo<T>> treeModeInfoes() {
    return treeModeInfoes;
  }

  @Override
  public void addTreeMode( TreeModeInfo<T> aModeInfo ) {
    TsNullArgumentRtException.checkNull( aModeInfo );
    TsItemAlreadyExistsRtException.checkTrue( treeModeInfoes.hasKey( aModeInfo.id() ) );
    treeModeInfoes.add( aModeInfo );
    onModeInfoesChanged();
    eventer.fireChangeEvent();
  }

  @Override
  public void clearTreeModes() {
    if( !treeModeInfoes.isEmpty() ) {
      treeModeInfoes.clear();
      if( modeId != null ) {
        modeId = null;
        onCurrentModeIdChanged();
      }
      onModeInfoesChanged();
      eventer.fireChangeEvent();
      lastModeId = null;
    }
  }

  @Override
  public boolean isCurrentTreeMode() {
    return modeId != null;
  }

  @Override
  public String currModeId() {
    return modeId;
  }

  @Override
  public String lastModeId() {
    if( lastModeId == null && !treeModeInfoes.isEmpty() ) {
      lastModeId = treeModeInfoes.keys().get( 0 );
    }
    return lastModeId;
  }

  @Override
  public void setCurrentMode( String aModeId ) {
    if( aModeId != null ) {
      TsItemNotFoundRtException.checkFalse( treeModeInfoes.hasKey( aModeId ) );
    }
    if( !Objects.equals( modeId, aModeId ) ) {
      modeId = aModeId;
      lastModeId = aModeId;
      onCurrentModeIdChanged();
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void setNextMode() {
    if( treeModeInfoes.isEmpty() || !hasTreeMode ) {
      setCurrentMode( null );
      return;
    }
    String nextId = ETsCollMove.NEXT.findElemAtWni( modeId, treeModeInfoes.keys(), 1, true );
    setCurrentMode( nextId );
  }

  protected void onModeInfoesChanged() {
    // nop
  }

  protected void onCurrentModeIdChanged() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

}
