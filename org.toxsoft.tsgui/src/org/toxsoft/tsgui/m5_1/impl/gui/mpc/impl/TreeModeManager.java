package org.toxsoft.tsgui.m5_1.impl.gui.mpc.impl;

import java.util.Objects;

import org.toxsoft.tsgui.m5.gui.mpc.ITreeModeManager;
import org.toxsoft.tsgui.m5.gui.mpc.impl.TreeModeInfo;
import org.toxsoft.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.tslib.utils.errors.*;

import ru.toxsoft.tslib.strids.stridable.INotifyerStridablesList;
import ru.toxsoft.tslib.strids.stridable.INotifyerStridablesListEdit;
import ru.toxsoft.tslib.strids.stridable.impl.NotifyerStridablesListEditWrapper;
import ru.toxsoft.tslib.utils.misc.GenericChangeEventHelper;

/**
 * Базовая реализация {@link ITreeModeManager}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class TreeModeManager<T>
    implements ITreeModeManager<T> {

  private final GenericChangeEventHelper GenericChangeEventHelper;

  private final INotifyerStridablesListEdit<TreeModeInfo<T>> treeModeInfoes =
      new NotifyerStridablesListEditWrapper<>( new StridablesList<TreeModeInfo<T>>() );

  private boolean hasTreeMode;

  private String modeId     = null;
  private String lastModeId = null;

  /**
   * Конструктор.
   *
   * @param aHasTreeMode boolean - признак наличия режимов дерева
   */
  public TreeModeManager( boolean aHasTreeMode ) {
    hasTreeMode = aHasTreeMode;
    GenericChangeEventHelper = new GenericChangeEventHelper( this );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITreeModeManager
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
      GenericChangeEventHelper.fireChangeEvent();
    }
  }

  @Override
  public INotifyerStridablesList<TreeModeInfo<T>> treeModeInfoes() {
    return treeModeInfoes;
  }

  @Override
  public void addTreeMode( TreeModeInfo<T> aModeInfo ) {
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
  public String getCurrentModeId() {
    return modeId;
  }

  @Override
  public String getLastModeId() {
    if( lastModeId == null && !treeModeInfoes.isEmpty() ) {
      lastModeId = treeModeInfoes.idList().get( 0 );
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
      GenericChangeEventHelper.fireChangeEvent();
    }
  }

  protected void onModeInfoesChanged() {
    // nop
  }

  protected void onCurrentModeIdChanged() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGenericChangeEventProducer
  //

  @Override
  public void addGenericChangeListener( IGenericChangeListener aListener ) {
    GenericChangeEventHelper.addGenericChangeListener( aListener );
  }

  @Override
  public void removeGenericChangeListener( IGenericChangeListener aListener ) {
    GenericChangeEventHelper.removeGenericChangeListener( aListener );
  }

}
