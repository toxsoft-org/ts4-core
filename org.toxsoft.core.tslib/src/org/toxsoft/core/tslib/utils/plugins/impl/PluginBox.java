package org.toxsoft.core.tslib.utils.plugins.impl;

import static org.toxsoft.core.tslib.utils.plugins.IPluginsHardConstants.*;

import java.io.File;

import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.wub.AbstractWubUnit;
import org.toxsoft.core.tslib.bricks.wub.WubBox;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.core.tslib.utils.plugins.IChangedPluginsInfo;
import org.toxsoft.core.tslib.utils.plugins.IChangedPluginsInfo.IChangedPluginInfo;
import org.toxsoft.core.tslib.utils.plugins.IPluginInfo;

/**
 * Контейнер плагинов
 * <p>
 * Реализация контейнера плагинов является потокобезопасной. Это обеспечивается синхронизацией (спецификатор
 * synchronized) шаблонных методов {@link AbstractWubUnit}.
 *
 * @author mvk
 * @param <T> тип плагинов
 */
public class PluginBox<T extends PluginUnit>
    extends AbstractWubUnit {

  private WubBox        wubBox;
  private PluginStorage storage;

  /**
   * Конструктор
   *
   * @param aId String идентификатор контейнера
   * @param aParams {@link IOptionSet} параметры контейнера
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PluginBox( String aId, IOptionSet aParams ) {
    super( aId, aParams );
  }

  // ------------------------------------------------------------------------------------
  // AbstractWubUnit
  //
  @Override
  protected synchronized ValidationResult doInit( ITsContextRo aEnviron ) {
    IOptionSet params = aEnviron.params();
    // Создание контейнера
    wubBox = new WubBox( id(), params );
    // Создание хранилища плагинов
    storage = new PluginStorage( PLUGIN_TYPE_ID.getValue( params ).asString() );
    for( String path : (IStringList)PLUGIN_JAR_PATHS.getValue( params ).asValobj() ) {
      storage.addPluginJarPath( new File( path ), false ); // aIncludeSubDirs = false
    }
    // Загрузка компонентов контейнера
    loadUnits( storage.listPlugins() );
    // Инициализация контейнера
    wubBox.init( aEnviron );

    return ValidationResult.SUCCESS;
  }

  @Override
  protected synchronized void doStart() {
    wubBox.start();
  }

  @Override
  protected synchronized void doDoJob() {
    IChangedPluginsInfo changes = storage.getChanges();
    if( changes.isChanges() ) {
      // Загрузка компонентов контейнера добавленных плагинов
      loadUnits( changes.listAddedPlugins() );
      // Загрузка компонентов контейнера измененных плагинов
      for( IChangedPluginInfo changedInfo : changes.listChangedPlugins() ) {
        loadUnit( changedInfo.pluginInfo() );
      }
    }
    wubBox.doJob();
  }

  @Override
  protected synchronized boolean doStopping() {
    return wubBox.queryStop();
  }

  @Override
  protected synchronized boolean doQueryStop() {
    return wubBox.queryStop();
  }

  @Override
  protected synchronized void doDestroy() {
    wubBox.destroy();
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследников
  //
  /**
   * Создает компонент контейнера для загруженного плагина.
   *
   * @param aPluginId String идентификатор который ДОЛЖЕН быть назначен компоненту.
   * @param aPlugin {@link IPlugin} загруженный плагин
   * @return {@link PluginUnit} компонент контейнера.
   */
  @SuppressWarnings( "unchecked" )
  protected T doCreateUnit( String aPluginId, IPlugin aPlugin ) {
    return (T)new PluginUnit( aPluginId, IOptionSet.NULL, aPlugin );
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследников
  //
  /**
   * Загрузка компонентов контейнера
   *
   * @param aPluginInfos {@link IList}&lt;{@link IPluginInfo}&gt; список описаний плагинов для которых требуется
   *          загрузить компоненты контейнера
   * @throws TsNullArgumentRtException аргумент = null
   */
  private void loadUnits( IList<IPluginInfo> aPluginInfos ) {
    TsNullArgumentRtException.checkNull( aPluginInfos );
    for( IPluginInfo pluginInfo : new ElemArrayList<>( aPluginInfos ) ) {
      loadUnit( pluginInfo );
    }
  }

  /**
   * Загрузка компонента контейнера
   *
   * @param aPluginInfo {@link IPluginInfo} описание плагина для которого требуется загрузить компоненту контейнера
   * @throws TsNullArgumentRtException аргумент = null
   */
  private void loadUnit( IPluginInfo aPluginInfo ) {
    TsNullArgumentRtException.checkNull( aPluginInfo );
    try {
      // Идентификатор плагина
      String pluginId = aPluginInfo.pluginId();
      // Загрузка плагина
      IPlugin plugin = storage.createPlugin( pluginId );
      // Создание компонента контейнера
      T unit = doCreateUnit( pluginId, plugin );
      // "Защита от дурака"
      TsIllegalStateRtException.checkFalse( pluginId.equals( unit.id() ) );
      // Регистрация слушателя выгрузки плагина
      plugin.eventer().addListener( aPlugin -> wubBox.removeUnit( pluginId ) );
      // Регистрация в контейнере
      wubBox.addUnit( unit );
    }
    catch( ClassNotFoundException ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }
}
