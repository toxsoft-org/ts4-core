package org.toxsoft.core.tslib.utils.plugins.impl;

import static org.toxsoft.core.tslib.utils.plugins.impl.ITsResources.*;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;

import org.toxsoft.core.tslib.bricks.events.AbstractTsEventer;
import org.toxsoft.core.tslib.bricks.events.ITsEventer;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.utils.ICloseable;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.core.tslib.utils.plugins.IPluginInfo;
import org.toxsoft.core.tslib.utils.plugins.IPluginListener;

/**
 * Единственная реализация {@link IPlugin}
 *
 * @author mvk
 */
final class Plugin
    implements IPlugin, ICloseable {

  private final IPluginInfo    info;
  private final URLClassLoader classLoader;
  private final Object         instance;
  private final Eventer        eventer = new Eventer();

  /**
   * Конструктор
   *
   * @param aClassPath URL[] classpath загружаемого плагина
   * @param aPluginInfo {@link IPluginInfo} информация о плагине
   * @param aListener {@link IPluginListener} слушатель плагина
   * @throws TsNullArgumentRtException аргумент = null
   */
  Plugin( URL[] aClassPath, IPluginInfo aPluginInfo, IPluginListener aListener ) {
    TsNullArgumentRtException.checkNulls( aClassPath, aPluginInfo, aListener );
    try {
      info = aPluginInfo;
      classLoader = new URLClassLoader( aPluginInfo.pluginId(), aClassPath, getClass().getClassLoader() );
      Class<?> cls = classLoader.loadClass( aPluginInfo.pluginClassName() );
      Constructor<?> defaultConstructor = cls.getConstructor();
      instance = defaultConstructor.newInstance();
      eventer.addListener( aListener );
    }
    catch( Exception e ) {
      String msg = String.format( MSG_ERR_CANT_CREATE_PLUGIN_OBJECT, aPluginInfo.pluginId(), aPluginInfo.pluginType() );
      throw new TsInternalErrorRtException( msg, e );
    }
  }

  // --------------------------------------------------------------------------
  // ICloseable
  //
  @Override
  public void close() {
    try {
      // classpath загрузчика классов
      URL[] classpath = classLoader.getURLs();
      // Завершение работы загрузчика классов
      classLoader.close();
      // Удаление временных файлов
      for( URL fileURL : classpath ) {
        File file = new File( fileURL.toURI() );
        file.delete();
      }
    }
    catch( Throwable e ) {
      LoggerUtils.errorLogger().error( e );
    }
    eventer.fireClosed();
  }

  // --------------------------------------------------------------------------
  // IPlugin
  //
  @Override
  public IPluginInfo info() {
    return info;
  }

  @Override
  public ClassLoader classLoader() {
    return classLoader;
  }

  @Override
  public <T> T instance( Class<T> aInstanceClass ) {
    TsNullArgumentRtException.checkNull( aInstanceClass );
    try {
      return aInstanceClass.cast( instance );
    }
    catch( ClassCastException e ) {
      throw new TsIllegalArgumentRtException( e );
    }
  }

  @Override
  public ITsEventer<IPluginListener> eventer() {
    return eventer;
  }

  /**
   * {@link IPlugin#eventer()} implementation.
   */
  class Eventer
      extends AbstractTsEventer<IPluginListener> {

    private boolean isPending = false;

    @Override
    protected void doClearPendingEvents() {
      isPending = false;
    }

    @Override
    protected void doFirePendingEvents() {
      isPending = false;
      fireClosed();
    }

    @Override
    protected boolean doIsPendingEvents() {
      return isPending;
    }

    void fireClosed() {
      if( isFiringPaused() ) {
        isPending = true;
        return;
      }
      IList<IPluginListener> listeners = new ElemArrayList<>( listeners() );
      // // Слушатели получают сообщение о выгрузке только один раз
      // eventer.clearListenersList();
      // Оповещение
      for( IPluginListener l : listeners ) {
        try {
          l.onClose( Plugin.this );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
    }

  }
}
