package org.toxsoft.core.tsgui.bricks.qtree.tmm;

import java.util.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IQTreeModeManager} base implementation.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class QTreeModeManager<T>
    implements IQTreeModeManager<T> {

  private final GenericChangeEventer eventer;

  private final INotifierStridablesListEdit<QTreeModeInfo<T>> treeModeInfoes =
      new NotifierStridablesListEditWrapper<>( new StridablesList<QTreeModeInfo<T>>() );

  private boolean hasTreeMode;

  private String modeId     = null;
  private String lastModeId = null;

  /**
   * Constructor.
   *
   * @param aHasTreeMode boolean - tree modes existance flag
   */
  public QTreeModeManager( boolean aHasTreeMode ) {
    hasTreeMode = aHasTreeMode;
    eventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // IQTreeModeManager
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
  public INotifierStridablesList<QTreeModeInfo<T>> treeModeInfoes() {
    return treeModeInfoes;
  }

  @Override
  public void addTreeMode( QTreeModeInfo<T> aModeInfo ) {
    TsNullArgumentRtException.checkNull( aModeInfo );
    TsItemAlreadyExistsRtException.checkTrue( treeModeInfoes.hasKey( aModeInfo.id() ) );
    treeModeInfoes.add( aModeInfo );
    onModeInfoesChanged();
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
