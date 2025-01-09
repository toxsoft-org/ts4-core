package org.toxsoft.core.tslib.utils.plugins.impl;

import static java.lang.String.*;
import static org.toxsoft.core.tslib.utils.plugins.impl.ITsResources.*;
import static org.toxsoft.core.tslib.utils.plugins.impl.PluginUtils.*;

import java.io.*;
import java.net.*;
import java.util.*;

import org.toxsoft.core.tslib.bricks.strid.idgen.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.tslib.utils.plugins.*;
import org.toxsoft.core.tslib.utils.plugins.IChangedPluginsInfo.*;
import org.toxsoft.core.tslib.utils.plugins.IPluginInfo.*;
import org.toxsoft.core.tslib.utils.plugins.dirscan.*;

/**
 * Реализация {@link IPluginsStorage}.
 *
 * @author Дима
 */
class PluginStorage
    implements IPluginsStorage {

  /**
   * Каталог размещения временных файлов по умолчанию.
   */
  public static final String TEMPORARY_DIR_DEFAULT = "ts_temp"; //$NON-NLS-1$

  /**
   * Хранит и накапливает изменения плагинов между вызовами getChanges().
   */
  private ChangedPluginsInfo changedModulesInfo = new ChangedPluginsInfo();

  /**
   * Карта для хранения описаний плагинов.
   * <p>
   * Ключ: идентификатор плагина;<br>
   * Значение: описание плагина {@link IPluginInfo}.
   */
  private IStringMapEdit<IPluginInfo> pluginInfos = new StringMap<>();

  /**
   * Карта загруженных плагинов.
   * <p>
   * Ключ: идентификатор плагина;<br>
   * Значение: список созданных экземпляров.
   */
  private IStringMapEdit<IListEdit<Plugin>> plugins = new StringMap<>();

  /**
   * Тип обрабатываемых плагинов. Пустая строка - любой тип.
   */
  private String plugInType;

  /**
   * Хранилище зарегистрированных директорий поиска плагинов.
   */
  private List<PluginDir> pluginDirs = new ArrayList<>();

  /**
   * Контейнер для хранения информации по директории для поиска плагинов.
   */
  private static class PluginDir {

    /**
     * Путь поиска плагинов
     */
    private File       path;
    /**
     * Сканер директории
     */
    private DirScanner jarScanner;

    public PluginDir( File aPath, boolean aIsIncludeSubDirs ) {
      this.path = aPath;
      jarScanner = new DirScanner( aPath, DirScanner.JAR_FILTER, aIsIncludeSubDirs, null );
    }

    public File getPath() {
      return path;
    }

    public DirScanner getJarScanner() {
      return jarScanner;
    }
  }

  /**
   * Каталог временных файлов по умолчанию.
   */
  private String temporaryDir = TEMPORARY_DIR_DEFAULT;

  /**
   * Генератор идентификаторов временных файлов.
   */
  private static final IStridGenerator filenameGenerator =
      new UuidStridGenerator( UuidStridGenerator.createState( "content" ) ); //$NON-NLS-1$

  /**
   * Конструктор
   *
   * @param aPlugInType String тип обрабатываемых плагинов. Пустая строка - любой тип
   * @throws TsNullArgumentRtException аргумент = null
   */
  public PluginStorage( String aPlugInType ) {
    TsNullArgumentRtException.checkNull( aPlugInType );
    plugInType = aPlugInType;
    // Установка каталога для временных файлов по умолчанию. aNeedClean = true.
    String td = System.getProperty( "java.io.tmpdir" ) + File.separator + TEMPORARY_DIR_DEFAULT; //$NON-NLS-1$
    setTemporaryDir( td, true );
  }

  // --------------------------------------------------------------------------
  // IPluginsStorage
  //

  @Override
  public String pluginTypeId() {
    return plugInType;
  }

  @Override
  public void addPluginJarPath( File aPath, boolean aIncludeSubDirs ) {
    TsFileUtils.checkDirReadable( aPath );
    PluginDir addedPluginDir = new PluginDir( aPath, aIncludeSubDirs );
    pluginDirs.add( addedPluginDir );
    // Сканируем выбранный путь на предмет обнаружения jar-файлов с манифестами
    // Сделать обработку обратных вызовов сканера (если она нужна в данном контексте)
    IDirScanResult dirScanResult = addedPluginDir.getJarScanner().scan( null );
    if( !dirScanResult.isChanged() ) {
      // Ничего не изменилось, уходим ...
      return;
    }
    // Исследуем новые jar-файлы
    List<ScannedFileInfo> jarFileList = dirScanResult.getNewFiles();
    Iterator<ScannedFileInfo> iterator = jarFileList.iterator();
    while( iterator.hasNext() ) {
      ScannedFileInfo jarFileInfo = iterator.next();
      String jarPathString = createPathStringToJar( aPath, jarFileInfo );
      File jarFile = new File( jarPathString );
      IList<IPluginInfo> pList = PluginUtils.readPluginInfoesFromJarFile( jarFile );
      registerPlugins( pList );
    }
  }

  @Override
  public void setTemporaryDir( String aDir, boolean aNeedClean ) {
    TsNullArgumentRtException.checkNull( aDir );
    File dir = new File( aDir );
    if( aNeedClean && dir.exists() ) {
      // Очистка каталога
      TsFileUtils.deleteDirectory( dir, ILongOpProgressCallback.CONSOLE );
    }
    // Проверка и если треубется создание каталога
    createDirIfNotExist( aDir );
    temporaryDir = aDir;
  }

  @Override
  public IList<IPluginInfo> listPlugins() {
    return pluginInfos.values();
  }

  @Override
  public IPlugin loadPlugin( String aPluginId )
      throws ClassNotFoundException {
    TsNullArgumentRtException.checkNull( aPluginId );
    // Проверка возможности загрузки плагина
    checkIfPluginCanBeLoaded( pluginInfos.getByKey( aPluginId ) );
    // Загрузка плагина
    return loadPlugin( getClass().getClassLoader(), aPluginId );
  }

  @Override
  public boolean checkChanges() {
    boolean retVal = false;
    // Сканируем все директории поиска на предмет изменений
    Iterator<PluginDir> iterator = pluginDirs.iterator();
    while( iterator.hasNext() ) {
      PluginDir currPluginDir = iterator.next();
      //
      IDirScanResult dirScanResult = currPluginDir.getJarScanner().scan( null );
      if( dirScanResult.isChanged() ) {
        // Что-то изменилось, выясняем подробности
        if( hasAddedPlugins( currPluginDir.getPath(), dirScanResult.getNewFiles() ) ) {
          // Появились новые подгружаемые модули
          retVal = true;
        }
        if( hasChangedPlugins( currPluginDir.getPath(), dirScanResult.getChangedFiles() ) ) {
          // Изменились подгружаемые модули
          retVal = true;
        }
        if( hasRemovedPlugins( currPluginDir.getPath(), dirScanResult.getRemovedFiles() ) ) {
          // удалились подгружаемые модули
          retVal = true;
        }
      }
    }
    return retVal;
  }

  @Override
  public IChangedPluginsInfo getChanges() {
    IChangedPluginsInfo retVal = changedModulesInfo;
    // Сбрасываем накопленные изменения
    changedModulesInfo = new ChangedPluginsInfo();
    return retVal;
  }

  // --------------------------------------------------------------------------
  // private methods
  //
  /**
   * Загружает плагин и создает экземпляр класса подключаемого модуля (смотри {@link IPlugin#instance(Class)}).
   * <p>
   * Создает класс вызовом {@link ClassLoader#loadClass(String)} и использованием корректного загрузчика классов из
   * JAR-файла модуля. Производит проверку зависимостей, и если они не разрешимы, выбрасывает исключение.
   *
   * @param aClassLoader {@link ClassLoader} - используемый загручик классов
   * @param aPluginId String - идентификатор плагина
   * @return {@link IPlugin} - загруженный плагин
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет плагина с таким идентификатором
   * @throws TsIoRtException ошибка работы с файлом плагина
   * @throws ClassNotFoundException нельзя разрешить зависимости или отсутствет файл класса в JAR-файле модуля
   */
  private IPlugin loadPlugin( ClassLoader aClassLoader, String aPluginId )
      throws ClassNotFoundException {
    TsNullArgumentRtException.checkNulls( aPluginId, aClassLoader );
    IPluginInfo pluginInfo = pluginInfos.getByKey( aPluginId );
    // Загрузка плагинов от которых зависит целевой плагин
    ClassLoader classLoader = loadDependencies( aClassLoader, pluginInfo );
    // Файл представляющий плагин
    File pluginJarFile = new File( pluginInfo.pluginJarFileName() );
    TsFileUtils.checkFileReadable( pluginJarFile );
    // Плагин загружается из его копии во временном каталоге, чтобы его можно
    File temporaryFile = new File( temporaryDir + File.separator + filenameGenerator.nextId() );
    try {
      // Подготовка временного файла
      TsFileUtils.copyFile( pluginJarFile, temporaryFile );
      // classpath для загрузки плагина
      URL[] classpath = { temporaryFile.toURI().toURL() };
      Plugin plugin = new Plugin( classLoader, classpath, pluginInfo, aPlugin -> {
        // Обработка выгрузки плагина - удаление всех созданных экземпляров
        IListEdit<Plugin> instances = plugins.findByKey( aPluginId );
        if( instances != null ) {
          instances.remove( (Plugin)aPlugin );
          if( instances.size() == 0 ) {
            plugins.removeByKey( aPluginId );
          }
        }
      } );
      IListEdit<Plugin> instances = plugins.findByKey( aPluginId );
      if( instances == null ) {
        // aAllowDuplicates = false
        instances = new ElemArrayList<>( false );
        plugins.put( aPluginId, instances );
      }
      instances.add( plugin );
      return plugin;
    }
    catch( Exception e ) {
      temporaryFile.delete();
      String msg = format( ERR_CANT_CREATE_PLUGIN_OBJECT, pluginInfo.pluginId(), pluginInfo.pluginType() );
      throw new ClassNotFoundException( msg, e );
    }
  }

  /**
   * Проходит по всему списку зависимостей плагина и пытается загрузить
   *
   * @param aClassLoader {@link ClassLoader} загрузчик классов для загрузки плагинов
   * @param aPluginInfo описан плагина
   * @throws ClassNotFoundException - не найден плагин, от которого зависит aPluginInfo
   */
  private ClassLoader loadDependencies( ClassLoader aClassLoader, IPluginInfo aPluginInfo )
      throws ClassNotFoundException {
    TsNullArgumentRtException.checkNulls( aClassLoader, aPluginInfo );
    IList<IDependencyInfo> dependencies = aPluginInfo.listDependencies();
    if( dependencies.size() == 0 ) {
      return aClassLoader;
    }
    // Создание родительского контекста для загрузки в него классов требуемых целевому плагину
    MultiClassLoader retValue = new MultiClassLoader( aPluginInfo.pluginId(), aClassLoader );
    for( int index = 0, n = dependencies.size(); index < n; index++ ) {
      IDependencyInfo dependencyInfo = dependencies.get( index );
      String pluginId = dependencyInfo.pluginId();
      IPlugin plugin = null;
      IList<Plugin> instances = plugins.findByKey( pluginId );
      if( instances != null && instances.size() > 0 ) {
        // Плагин уже был загружен
        plugin = instances.first();
      }
      if( plugin == null ) {
        // Загрузка плагина
        plugin = loadPlugin( aClassLoader, pluginId );
      }
      retValue.addParent( plugin.classLoader() );
    }
    return retValue;
  }

  /**
   * Проверяет возмножность загрузки указанного плагина
   *
   * @param aPluginInfo {@link IPluginInfo} описание плагина
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException не найден плагин или одна из его зависемостей
   * @throws ClassNotFoundException - не найден плагин, от которого зависит aPluginInfo
   */
  private void checkIfPluginCanBeLoaded( IPluginInfo aPluginInfo )
      throws ClassNotFoundException {
    TsNullArgumentRtException.checkNull( aPluginInfo );
    TsItemNotFoundRtException.checkFalse( pluginInfos.hasKey( aPluginInfo.pluginId() ) );
    IList<IDependencyInfo> dependencies = aPluginInfo.listDependencies();
    for( int index = 0, n = dependencies.size(); index < n; index++ ) {
      IDependencyInfo dependencyInfo = dependencies.get( index );
      // Проверяем существование зависимого плагина и его номер версии (сравнение требуемой с имеющейся).
      checkDependenceVersion( aPluginInfo, dependencyInfo );
      checkIfPluginCanBeLoaded( pluginInfos.getByKey( dependencyInfo.pluginId() ) );
    }
  }

  /**
   * Создает строковое предстваление полного пути к jar-файлу
   *
   * @param aJarDir путь к директории jar'a
   * @param jarFileInfo описание файла сделанное сканером
   * @return объект типа String
   */
  private static String createPathStringToJar( File aJarDir, ScannedFileInfo jarFileInfo ) {
    return aJarDir.getAbsolutePath() + File.separatorChar + jarFileInfo.getRelativePath() + jarFileInfo.getFileName();
  }

  /**
   * Региструем плагины. Механизм регистрации следующий: <br>
   * - если плагин новый (плагина с таким идентификатором нет в системе), то регистрируется безусловно <br>
   * - если плагин уже зарегистрирован, то проверятся версия нового плагина, если она более свежая, то плагин
   * регистрируется (старое описание удаляется), иначе он игнорируется.
   *
   * @param aPluginsInfoList IList&lt;IPluginInfo&gt; - ???
   */
  private void registerPlugins( IList<IPluginInfo> aPluginsInfoList ) {
    // Пробегаемся по всему списку
    for( IPluginInfo pluginInfo : aPluginsInfoList ) {
      // 2021-02-06 mvk
      // 2024-05-28 mvk +++ !plugInType.equals( TsLibUtils.EMPTY_STRING ) &&
      if( !plugInType.equals( TsLibUtils.EMPTY_STRING ) && !plugInType.equals( pluginInfo.pluginType() ) ) {
        // Необрабатываемый тип плагина
        continue;
      }
      // Проверяем плагин на новизну
      if( !isPluginRegistered( pluginInfo ) ) {
        // Новый плагин просто регистрируем
        registerPluginInfo( pluginInfo );
      }
      else {
        // Плагин уже зарегистрирован, проверяем версии
        IPluginInfo registeredPlugin = pluginInfos.getByKey( pluginInfo.pluginId() );
        if( isNeedToUpdate( registeredPlugin, pluginInfo ) ) {
          updatePluginInfo( registeredPlugin, pluginInfo );
        }
      }
    }
  }

  /**
   * Отменяет регистрацию плагина.
   *
   * @param aPluginInfo {@link IPluginInfo} информация о плагине
   */
  private void deregisterPluginInfo( IPluginInfo aPluginInfo ) {
    String pluginId = aPluginInfo.pluginId();
    LoggerUtils.defaultLogger().debug( MSG_DEREGISTER_PLUGIN, pluginId );
    // Удаление всех зависимостей от целевого плагина
    // TODO: 2025-01-09 mvk: выгрузка зависимостей требует тестирования/отладки!
    for( IPluginInfo pluginInfo : new ElemLinkedList<>( pluginInfos.values() ) ) {
      for( IDependencyInfo dependencyInfo : pluginInfo.listDependencies() ) {
        String dependencyPluginId = dependencyInfo.pluginId();
        if( dependencyPluginId.equals( pluginId ) ) {
          LoggerUtils.defaultLogger().debug( MSG_DEREGISTER_DEPENDENCY, pluginId, dependencyPluginId );
          deregisterPluginInfo( pluginInfo );
        }
      }
    }
    pluginInfos.removeByKey( pluginId );
    // Удаление всех созданных экземпляров
    IList<Plugin> instances = plugins.findByKey( pluginId );
    if( instances != null ) {
      for( Plugin instance : new ElemArrayList<>( instances ) ) {
        instance.close();
      }
    }
  }

  /**
   * Регистрация описания плагина.
   *
   * @param pluginInfo объект типа IPluginInfo
   */
  private void registerPluginInfo( IPluginInfo pluginInfo ) {
    pluginInfos.put( pluginInfo.pluginId(), pluginInfo );
  }

  /**
   * Проверяет плагин на регистрацию в системе
   *
   * @param aPluginInfo описание плагина
   * @return true, если плагин с таким идентификатором уже зарегистрирован, иначе false
   */
  private boolean isPluginRegistered( IPluginInfo aPluginInfo ) {
    return (pluginInfos.findByKey( aPluginInfo.pluginId() ) != null);
  }

  /**
   * Проверяет пригодность версии зависимого модуля
   *
   * @param aPluginInfo {@link IPluginInfo} описание модуля
   * @param aDependPluginInfo {@link IPluginInfo} описание модуля зависимости
   * @throws TsItemNotFoundRtException плагин, от которого зависит aPluginInfo, либо не найден, либо версия не та
   */
  private void checkDependenceVersion( IPluginInfo aPluginInfo, IDependencyInfo aDependPluginInfo ) {
    TsNullArgumentRtException.checkNulls( aPluginInfo, aDependPluginInfo );
    // Получаем описание зависимого плагина
    IPluginInfo pluginInfo = pluginInfos.findByKey( aDependPluginInfo.pluginId() );
    if( pluginInfo == null ) {
      // Не найдена зависимость плагина
      throw new TsItemNotFoundRtException( ERR_DEPENDENCY_CANT_RESOLVE, aPluginInfo, aDependPluginInfo );
    }
    TsVersion version = aDependPluginInfo.pluginVersion();
    if( aDependPluginInfo.isExactVersionNeeded() ) {
      // Требуется точная версия
      if( version.compareTo( pluginInfo.pluginVersion() ) == 0 ) {
        return;
      }
      // Неточное соответствие версии найденной зависимости
      throw new TsItemNotFoundRtException( ERR_DEPENDENCY_INACCURATE_MATCH, aPluginInfo, aDependPluginInfo );
    }
    // Достаточно просто более новой версии
    if( pluginInfo.pluginVersion().compareTo( version ) >= 0 ) {
      return;
    }
    // Несоответствие версии найденной зависимости
    throw new TsItemNotFoundRtException( ERR_DEPENDENCY_VERSION_MISMATCH, aPluginInfo, aDependPluginInfo );
  }

  /**
   * Проверяет удаленные файлы в зарегистрованной директории сканирования на предмет содержания в них подгружаемых
   * модулей. Все плагины из удаленных файлов разрегистрируются
   *
   * @param aJarDir директория сканирования
   * @param aRemovedFiles объект типа List<ScannedFileInfo>
   * @return true, если в списке удаленных файлов есть jar-файлы с зарегистрированными плагинами, иначе false
   */
  private boolean hasRemovedPlugins( File aJarDir, List<ScannedFileInfo> aRemovedFiles ) {
    boolean retVal = false;
    // Проходим по списку всех файлов
    for( ScannedFileInfo scannedFileInfo : aRemovedFiles ) {
      // Получаем список описаний плагинов
      File removedJarFile = new File( createPathStringToJar( aJarDir, scannedFileInfo ) );
      // MVK: bug: нельзя запрашивать описание у файла которого может уже не быть :)
      // List<IPluginInfo> readedPluginsInfoList = readPluginsInfoFromJarManifest(
      // removedJarFile );
      List<IPluginInfo> readedPluginsInfoList = getJarPlugins( removedJarFile.getAbsolutePath() );
      // Идем по списку плагинов и проверяем их регистрацию
      for( IPluginInfo removedPluginInfo : readedPluginsInfoList ) {
        if( isPluginRegistered( removedPluginInfo ) ) {
          // Зарегистрированный плагин, отменяем регистрацию и заносим в список удаленных
          // плагинов объекта хранящего изменения плагинов
          deregisterPluginInfo( removedPluginInfo );
          // Заносим в изменения
          getChangedModulesInfo().addRemovedPlugin( removedPluginInfo );
          retVal = true;
        }
      }
    }
    return retVal;
  }

  /**
   * Возвращает список плагинов которые определяется в указанном jar-файле
   *
   * @param aJarFileName String - имя jar-файла
   * @return List&lt;{@link IPluginInfo}&gt; - список описаний плагинов
   * @throws TsNullArgumentRtException - aJarFileName = null
   */
  private List<IPluginInfo> getJarPlugins( String aJarFileName ) {
    TsNullArgumentRtException.checkNull( aJarFileName );
    List<IPluginInfo> retValue = new LinkedList<>();
    for( IPluginInfo pluginInfo : pluginInfos ) {
      if( aJarFileName.equals( pluginInfo.pluginJarFileName() ) ) {
        retValue.add( pluginInfo );
      }
    }
    return retValue;
  }

  /**
   * Проверяет изменившиеся файлы в зарегистрованной директории сканирования на предмет содержания в них подгружаемых
   * модулей. Все плагины в изменившихся jar-файлах анализируются на предмет новизны в системе или на предмет появления
   * более новой версии зарегистрированных в системе. В первом случае плагин просто регистрируется, во втором старый
   * плагин заменяется на плагин с более новой версией. В случае изменения типа и/или идентификатора подключаемого
   * модуля - считается, что предыдущий модуль был удален, и появился новый. Т.е. указанный модуль окажется в списках
   * удаленных (со старым типом/идентификатором) и в списке новых, с текущими свойствами.
   *
   * @param aJarDir директория сканирования
   * @param aChangedFiles объект типа List<ScannedFileInfo>
   * @return true, если в списке файлов есть jar-файлы с новыми плагинами и/или с существующими плагинами другой версии,
   *         иначе false
   * @throws TsIoRtException невозможно открыть aJarDir как JAR-файл
   * @throws TsRuntimeException различные ошибки ???
   */
  private boolean hasChangedPlugins( File aJarDir, List<ScannedFileInfo> aChangedFiles ) {
    boolean retVal = false;
    // Проходим по списку всех файлов
    for( ScannedFileInfo scannedFileInfo : aChangedFiles ) {
      // Получаем список описаний плагинов
      File changedJarFile = new File( createPathStringToJar( aJarDir, scannedFileInfo ) );
      // Получить список всех плагинов бывших в данном jar-файле (контрольный список)
      List<IPluginInfo> oldPluginInfoList = getRegisteredPluginsFromThisJar( changedJarFile );
      IList<IPluginInfo> piList = PluginUtils.readPluginInfoesFromJarFile( changedJarFile );
      // Идем по списку плагинов и проверяем их новизну (незарегистрированные плагины
      // и новые версии зарегистрированных)
      for( IPluginInfo addedPluginInfo : piList ) {
        if( !isPluginRegistered( addedPluginInfo ) ) {
          // Незарегистрированный плагин, регистрируем и заносим в список добавленных
          // плагинов объекта хранящего измения плагинов
          registerPluginInfo( addedPluginInfo );
          // Заносим в изменения
          getChangedModulesInfo().addAddedPlugin( addedPluginInfo );
        }
        else {
          // Есть такой плагин, смотрим его версию и если она свежее, то обновляем
          IPluginInfo pluginInfo = pluginInfos.getByKey( addedPluginInfo.pluginId() );
          updatePluginInfo( pluginInfo, addedPluginInfo );
          // Заносим изменения
          IChangedPluginInfo changedPluginInfo =
              getChangedModulesInfo().createChangedPluginInfo( addedPluginInfo, pluginInfo.pluginVersion() );
          getChangedModulesInfo().addChangedPlugin( changedPluginInfo );
          // Удаляем из контрольного списка
          for( IPluginInfo info : oldPluginInfoList ) {
            if( (info.pluginId().compareTo( pluginInfo.pluginId() ) == 0) ) {
              oldPluginInfoList.remove( info );
              break;
            }
          }
        }
        retVal = true;
      }
      // Оставшиеся в контрольном списке плагины разрегистрируем и добавляем в список
      // удаленных
      for( IPluginInfo info : oldPluginInfoList ) {
        deregisterPluginInfo( info );
        getChangedModulesInfo().addRemovedPlugin( info );
        retVal = true;
      }
    }
    return retVal;
  }

  /**
   * Получить список всех плагинов бывших в данном jar-файле (контрольный список)
   *
   * @param aChangedJarFile - файл с плагинами
   * @return List&lt;IPluginInfo&gt; - список плагинов в файле
   */
  private List<IPluginInfo> getRegisteredPluginsFromThisJar( File aChangedJarFile ) {
    List<IPluginInfo> retVal = new ArrayList<>();
    String jarPathString;
    try {
      jarPathString = aChangedJarFile.getCanonicalPath();
    }
    catch( IOException ex ) {
      throw new TsIoRtException( ex, aChangedJarFile.getAbsolutePath() );
    }
    for( IPluginInfo pluginInfo : pluginInfos ) {
      if( jarPathString.compareTo( pluginInfo.pluginJarFileName() ) == 0 ) {
        retVal.add( pluginInfo );
      }
    }
    return retVal;
  }

  /**
   * Проверяет новые файлы появивишиеся в зарегистрованной директории сканирования на предмет содержания в них
   * подгружаемых модулей. Все плагины в новых jar-файлах анализируются на предмет новизны в системе или на предмет
   * появления более новой версии зарегистрированных в системе. В первом случае плагин просто регистрируется, во втором
   * старый плагин заменяется на плагин с более новой версией.
   *
   * @param aJarDir объект типа File - директория сканирования
   * @param aNewFiles объект типа List<ScannedFileInfo>
   * @return true, если в списке файлов есть jar-файлы с новыми плагинами и/или с существующими плагинами другой версии,
   *         иначе false
   */
  private boolean hasAddedPlugins( File aJarDir, List<ScannedFileInfo> aNewFiles ) {
    boolean retVal = false;
    // Проходим по списку всех файлов
    for( ScannedFileInfo scannedFileInfo : aNewFiles ) {
      // Получаем список описаний плагинов
      File addedJarFile = new File( createPathStringToJar( aJarDir, scannedFileInfo ) );
      IList<IPluginInfo> piList = PluginUtils.readPluginInfoesFromJarFile( addedJarFile );
      // Идем по списку плагинов и проверяем их новизну (незарегистрированные плагины
      // и новые версии зарегистрированных)
      for( IPluginInfo pluginInfo : piList ) {
        if( !isPluginRegistered( pluginInfo ) ) {
          // Незарегистрированный плагин, регистрируем и заносим в список добавленных
          // плагинов объекта
          // хранящего измения плагинов
          registerPluginInfo( pluginInfo );
          // Заносим в изменения
          getChangedModulesInfo().addAddedPlugin( pluginInfo );
          retVal = true;
        }
        else {
          // Есть такой плагин, смотрим его версию и если она свежее, то обновляем
          IPluginInfo registeredPluginInfo = pluginInfos.getByKey( pluginInfo.pluginId() );
          if( isNeedToUpdate( registeredPluginInfo, pluginInfo ) ) {
            updatePluginInfo( registeredPluginInfo, pluginInfo );
            // Заносим изменения
            IChangedPluginInfo changedPluginInfo =
                getChangedModulesInfo().createChangedPluginInfo( pluginInfo, registeredPluginInfo.pluginVersion() );
            getChangedModulesInfo().addChangedPlugin( changedPluginInfo );
            retVal = true;
          }
        }
      }
    }
    return retVal;
  }

  /**
   * Заменяет описание плагина aRegisteredPluginInfo на описание более свежей версии aAddedPluginInfo
   *
   * @param aRegisteredPluginInfo объект типа IPluginInfo
   * @param aAddedPluginInfo объект типа IPluginInfo
   */
  private void updatePluginInfo( IPluginInfo aRegisteredPluginInfo, IPluginInfo aAddedPluginInfo ) {
    deregisterPluginInfo( aRegisteredPluginInfo );
    registerPluginInfo( aAddedPluginInfo );
  }

  /**
   * Оценить необходимость обновления плагина
   *
   * @param aRegisteredPluginInfo описание зарегистрированного плагина
   * @param aAddedPluginInfo описание его добавленной версии
   * @return true, обновление необходимо, иначе false
   */
  private static boolean isNeedToUpdate( IPluginInfo aRegisteredPluginInfo, IPluginInfo aAddedPluginInfo ) {
    TsVersion registeredVersion = aRegisteredPluginInfo.pluginVersion();
    TsVersion addedVersion = aAddedPluginInfo.pluginVersion();
    if( addedVersion.compareTo( registeredVersion ) > 0 ) {
      return true;
    }
    return false;
  }

  private ChangedPluginsInfo getChangedModulesInfo() {
    return changedModulesInfo;
  }

  /**
   * Загрузчик классов имеющих несколько родительских контекстов.
   *
   * @author mvk
   */
  private static class MultiClassLoader
      extends ClassLoader {

    private final IStringMapEdit<ClassLoader> parents = new StringMap<>();

    MultiClassLoader( String aPluginId, ClassLoader aParent ) {
      super( format( FMT_PARENT_CLASSLOADER_NAME, aPluginId ), aParent );
    }

    /**
     * Add parent classloader.
     *
     * @param aClassLoader {@link ClassLoader} загрузчик классов {@link TsNullArgumentRtException} аргумент = nuul
     *          {@link TsNullArgumentRtException} аргумент = null
     */
    void addParent( ClassLoader aClassLoader ) {
      TsNullArgumentRtException.checkNull( aClassLoader );
      if( this.equals( aClassLoader ) ) {
        return;
      }
      if( parents.hasKey( aClassLoader.getName() ) ) {
        return;
      }
      parents.put( aClassLoader.getName(), aClassLoader );
    }

    @Override
    protected Class<?> findClass( String name )
        throws ClassNotFoundException {
      for( ClassLoader classLoader : parents ) {
        try {
          Class<?> retValue = classLoader.loadClass( name );
          return retValue;
        }
        catch( @SuppressWarnings( "unused" ) ClassNotFoundException e ) {
          // .. try next one
        }
      }
      // not found in any parent
      throw new ClassNotFoundException( name );
    }

    // ------------------------------------------------------------------------------------
    // Object
    //
    @Override
    public String toString() {
      return String.format( "%s[%s]", getClass().getSimpleName(), getName() ); //$NON-NLS-1$
    }

    @Override
    public int hashCode() {
      int result = TsLibUtils.INITIAL_HASH_CODE;
      result = TsLibUtils.PRIME * result + super.hashCode();
      result = TsLibUtils.PRIME * result + parents.hashCode();
      return result;
    }

    @Override
    public boolean equals( Object aObject ) {
      if( this == aObject ) {
        return true;
      }
      if( aObject == null ) {
        return false;
      }
      if( getClass() != aObject.getClass() ) {
        return false;
      }
      MultiClassLoader other = (MultiClassLoader)aObject;
      if( !super.equals( other ) ) {
        return false;
      }
      if( !super.equals( other ) ) {
        return false;
      }
      if( !parents.equals( other.parents ) ) {
        return false;
      }
      return true;
    }
  }
}
