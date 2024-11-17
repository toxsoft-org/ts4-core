package org.toxsoft.core.tslib.utils.plugins.impl;

import static org.toxsoft.core.tslib.utils.plugins.impl.ITsResources.*;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;

import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.tslib.utils.plugins.*;

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
      Thread.currentThread().setContextClassLoader( classLoader );
      Class<?> cls = classLoader.loadClass( aPluginInfo.pluginClassName() );
      Constructor<?> defaultConstructor = cls.getConstructor();
      instance = defaultConstructor.newInstance();
      eventer.addListener( aListener );
    }
    catch( Exception e ) {
      String msg = String.format( ERR_CANT_CREATE_PLUGIN_OBJECT, aPluginInfo.pluginId(), aPluginInfo.pluginType() );
      throw new TsInternalErrorRtException( msg, e );
    }
  }

  // --------------------------------------------------------------------------
  // ICloseable
  //
  @Override
  public void close() {
    try {
      // TODO: FIXME: 2024-11-15 mvk классы не выгружаются!!! Решение может быть чем-то следующим:
      // source: https://stackoverflow.com/questions/148681/unloading-classes-in-java
      // classpath загрузчика классов
      // 2024-11-17 mvk update - классы выгружаются! Проверено на скатлетах виртуальных данных. Проблема со скатлетом
      // с AlarmProcessor в том, что он грузит классы на которые остаются ссылки. Например, для службы ISkAlarmService
      // необходимо зарегистрировать типы данных, ISkServiceCreator в синглетонах конфигурации соединения uskat и сейчас
      // нет возможности дерегистрировать все что было зарегистрировано службой. При этом нужно будет решить вопрос, что
      // делать если несколько скателтов регистрируют одни и те же типы данных/классы.
      // uskat-соединения
      URL[] classpath = classLoader.getURLs();
      // Завершение работы загрузчика классов
      classLoader.close();
      // Удаление временных файлов
      for( URL fileURL : classpath ) {
        File file = new File( fileURL.toURI() );
        if( !file.exists() ) {
          LoggerUtils.errorLogger().warning( "Plugin.close(): " + ERR_NOT_FOUND_TEMPORARY_FILE, file ); //$NON-NLS-1$
        }
        if( file.exists() && !file.delete() ) {
          LoggerUtils.errorLogger().error( "Plugin.close(): " + ERR_CANT_REMOVE_TEMPORARY_FILE, file ); //$NON-NLS-1$
        }
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
