package org.toxsoft.core.txtproj.lib.impl;

import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.core.txtproj.lib.*;

/**
 * Класс для облегчения реализации смешиваемого интерфейса {@link ITsProjectContentChangeProducer}.
 *
 * @author hazard157
 */
public class TsProjectContentChangeProducerHelper
    implements ITsProjectContentChangeProducer {

  private final IListEdit<ITsProjectContentChangeListener> listeners = new ElemLinkedBundleList<>();

  private final ITsProject source;

  /**
   * Конструктор.
   *
   * @param aSource {@link ITsProject} - источник сообщения
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public TsProjectContentChangeProducerHelper( ITsProject aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Вызывает всех зарегистрированных слушателей {@link ITsProjectContentChangeListener#beforeSave(ITsProject)}.
   */
  public void fireBeforeSave() {
    if( listeners.isEmpty() ) {
      return;
    }
    IList<ITsProjectContentChangeListener> ll = new ElemArrayList<>( listeners );
    for( int i = 0, n = ll.size(); i < n; i++ ) {
      try {
        ITsProjectContentChangeListener l = ll.get( i );
        l.beforeSave( source );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  /**
   * Вызывает всех зарегистрированных слушателей {@link ITsProjectContentChangeListener#afterSave(ITsProject)}.
   */
  public void fireAfterSave() {
    if( listeners.isEmpty() ) {
      return;
    }
    IList<ITsProjectContentChangeListener> ll = new ElemArrayList<>( listeners );
    for( int i = 0, n = ll.size(); i < n; i++ ) {
      try {
        ITsProjectContentChangeListener l = ll.get( i );
        l.afterSave( source );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  /**
   * Вызывает всех зарегистрированных слушателей.
   *
   * @param aCleared boolean - признак очистки содержимого
   */
  public void fireChangeEvent( boolean aCleared ) {
    if( listeners.isEmpty() ) {
      return;
    }
    IList<ITsProjectContentChangeListener> ll = new ElemArrayList<>( listeners );
    for( int i = 0, n = ll.size(); i < n; i++ ) {
      try {
        ITsProjectContentChangeListener l = ll.get( i );
        l.onContentChanged( source, aCleared );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsProjectContentChangeProducer
  //

  @Override
  public void addProjectContentChangeListener( ITsProjectContentChangeListener aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeProjectContentChangeListener( ITsProjectContentChangeListener aListener ) {
    listeners.remove( aListener );
  }

}
