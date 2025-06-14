package org.toxsoft.core.tslib.utils.plugins.impl;

import static org.toxsoft.core.tslib.utils.plugins.IPluginsHardConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.wub.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.tslib.utils.plugins.*;
import org.toxsoft.core.tslib.utils.plugins.IChangedPluginsInfo.*;

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

  /**
   * Возвращает список загруженных плагинов.
   *
   * @return {@link IList}&lt;{@link IPlugin}&gt; список загруженных плагинов.
   * @throws TsIllegalStateRtException контейнер не инициализирован
   */
  public synchronized IList<IPlugin> listPlugins() {
    IListEdit<IPlugin> retValue = new ElemArrayList<>();
    TsIllegalStateRtException.checkFalse( wubBox != null && !wubBox.isStopped() );
    for( IWubUnit unit : wubBox.unitsList() ) {
      retValue.add( ((PluginUnit)unit).plugin() );
    }
    return retValue;
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
    for( String path : (IStringList)PLUGINS_DIR.getValue( params ).asValobj() ) {
      storage.addPluginJarPath( new File( path ), false ); // aIncludeSubDirs = false
    }
    // Конфигурация каталога для хранения временных файлов
    storage.setTemporaryDir( TMP_DIR.getValue( params ).asString(), CLEAN_TMP_DIR.getValue( params ).asBool() );
    // Инициализация контейнера
    wubBox.init( aEnviron );

    return ValidationResult.SUCCESS;
  }

  @Override
  protected synchronized void doStart() {
    wubBox.start();
    // Загрузка компонентов контейнера
    loadUnits( storage.listPlugins() );
  }

  @Override
  protected synchronized void doDoJob() {
    if( storage.checkChanges() ) {
      // Изменение состояния хранилища. Запрос изменений
      IChangedPluginsInfo changes = storage.getChanges();
      // Список описаний загружаемых плагинов
      IListEdit<IPluginInfo> pluginInfos = new ElemArrayList<>( changes.listAddedPlugins() );
      // Загрузка компонентов контейнера измененных плагинов
      for( IChangedPluginInfo changedInfo : changes.listChangedPlugins() ) {
        pluginInfos.add( changedInfo.pluginInfo() );
      }
      // Загрузка компонентов контейнера добавленных плагинов
      loadUnits( pluginInfos );
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
  // methods for inheritance
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

  /**
   * Вызывается перед загрузкой указанных плагинов.
   *
   * @param aPluginInfos {@link IList}&lt;{@link IPluginInfo}&gt; список описаний загружаемых плагинов
   * @return {@link IList}&lt;{@link IPluginInfo} упорядочный список описаний загружаемых плагинов. В начале списка
   *         плагины загружаемые первыми, в конце - последними.
   */
  protected IList<IPluginInfo> doBeforeLoadUnits( IList<IPluginInfo> aPluginInfos ) {
    return aPluginInfos;
  }

  /**
   * Вызывается перед запуском (размещением в контейнере) указанных плагинов.
   *
   * @param aPlugins {@link IList}&lt;{@link PluginUnit}&gt; упорядочный список загруженных плагинов.
   */
  protected IList<T> doBeforeRunUnits( IList<T> aPlugins ) {
    return aPlugins;
  }

  /**
   * Вызывается после запуска (размещением в контейнере) указанных плагинов.
   *
   * @param aPlugins {@link IList}&lt;{@link PluginUnit}&gt; упорядочный список загруженных плагинов.
   */
  protected void doAfterRunUnits( IList<T> aPlugins ) {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // private methods
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
    // Упорядочный список описаний согласно зависимостей
    IList<IPluginInfo> pluginInfoes = sortInfoesByDependencies( aPluginInfos, new ElemArrayList<>() );
    // Упорядочный список описаний определямый наследниками
    IList<IPluginInfo> sortedPluginInfos = doBeforeLoadUnits( pluginInfoes );
    // Список загруженных плагинов
    IListEdit<T> units = new ElemLinkedList<>();
    for( IPluginInfo pluginInfo : new ElemArrayList<>( sortedPluginInfos ) ) {
      units.add( loadUnit( pluginInfo ) );
    }
    // Оповещение наследника о готовности разместить плагины в контейнере
    IList<T> runnableUnits = doBeforeRunUnits( units );
    for( T unit : runnableUnits ) {
      runUnit( unit );
    }
  }

  /**
   * Загрузка компонента контейнера
   *
   * @param aPluginInfo {@link IPluginInfo} описание плагина для которого требуется загрузить компоненту контейнера
   * @throws TsNullArgumentRtException аргумент = null
   */
  private T loadUnit( IPluginInfo aPluginInfo ) {
    TsNullArgumentRtException.checkNull( aPluginInfo );
    T unit = null;
    try {
      // Идентификатор плагина
      String pluginId = aPluginInfo.pluginId();
      // Загрузка плагина
      IPlugin plugin = storage.loadPlugin( pluginId );
      // Создание компонента контейнера
      unit = doCreateUnit( pluginId, plugin );
      // "Защита от дурака"
      TsIllegalStateRtException.checkFalse( pluginId.equals( unit.id() ) );
      // Регистрация слушателя выгрузки плагина
      plugin.eventer().addListener( aPlugin -> wubBox.removeUnit( pluginId ) );
    }
    catch( ClassNotFoundException ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
    return unit;
  }

  /**
   * Запуск компонента контейнера
   *
   * @param aUnit T компонент контейнера (плагин) {@link TsNullArgumentRtException} аргумент = null
   */
  private void runUnit( T aUnit ) {
    TsNullArgumentRtException.checkNull( aUnit );
    // Регистрация в контейнере
    wubBox.addUnit( aUnit );
  }

  private static IListEdit<IPluginInfo> sortInfoesByDependencies( IList<IPluginInfo> aPluginInfoes,
      IListEdit<IPluginInfo> aSortedInfoes ) {
    TsNullArgumentRtException.checkNulls( aPluginInfoes, aSortedInfoes );
    IListEdit<IPluginInfo> pluginInfoes = new ElemArrayList<>( aPluginInfoes );
    while( pluginInfoes.size() > 0 ) {
      IPluginInfo info = pluginInfoes.removeByIndex( 0 );
      if( aSortedInfoes.hasElem( info ) ) {
        // Плагин уже в списке
        continue;
      }
      aSortedInfoes.addAll( sortInfoesByDependencies( pluginInfoes, aSortedInfoes ) );
      aSortedInfoes.add( info );
    }
    return aSortedInfoes;
  }
}
