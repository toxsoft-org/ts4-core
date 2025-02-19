package org.toxsoft.core.tslib.utils.plugins.impl;

import static java.lang.String.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.plugins.*;

/**
 * неизменяемый класс - реализация интерфейса IPluginInfo.
 *
 * @author Дима
 * @author hazard157
 */
class PluginInfo
    implements IPluginInfo {

  private final String                 jarFileName;
  private final String                 className;
  private final IList<IDependencyInfo> dependencyInfoList;
  private final IStringMap<String>     userProps;
  private final TsVersion              version;
  private final String                 type;
  private final String                 id;

  /**
   * Создает описание плагина со всеми инвариантами.
   * <p>
   * Внимание: все ссылки запоминаются без создания защитной копии!
   *
   * @param aJarFilePath String - путь к JAR-файлу плагина
   * @param aType String - тип плагина
   * @param aId String - идентификатор плагина
   * @param aClassName String - полное имя Java-класса плагина
   * @param aVer TsVersion - версия плагина
   * @param aDepInfoes IList&lt;IDependencyInfo&gt; - требуемые зависимости
   * @param aUserProps IStringMap&lt;String&gt; - другие свойства из раздела описания плагина
   * @throws TsNullArgumentRtException любой аргумент = null плагина
   * @throws TsIllegalArgumentRtException aType или aId не валидные ИД-пути
   */
  public PluginInfo( String aJarFilePath, String aType, String aId, String aClassName, TsVersion aVer,
      IList<IDependencyInfo> aDepInfoes, IStringMap<String> aUserProps ) {
    id = aId;
    type = aType;
    jarFileName = aJarFilePath;
    className = aClassName;
    userProps = aUserProps;
    version = aVer;
    dependencyInfoList = aDepInfoes;
  }

  /**
   * Конструктор копирования.
   *
   * @param aPluginInfo PluginInfo - источник
   * @throws TsNullArgumentRtException аргумент = null
   */
  public PluginInfo( PluginInfo aPluginInfo ) {
    TsNullArgumentRtException.checkNull( aPluginInfo );
    id = aPluginInfo.id;
    type = aPluginInfo.type;
    className = aPluginInfo.className;
    jarFileName = aPluginInfo.jarFileName;
    userProps = aPluginInfo.userProps;
    version = aPluginInfo.version;
    dependencyInfoList = new ElemArrayList<>( aPluginInfo.dependencyInfoList );
  }

  /**
   * Конструктор копирования с замещением версии.
   *
   * @param aPluginInfo PluginInfo - источник
   * @param aChangedVersion TsVersion - версия, замещающая версию источника
   * @throws TsNullArgumentRtException аргумент = null
   */
  public PluginInfo( PluginInfo aPluginInfo, TsVersion aChangedVersion ) {
    TsNullArgumentRtException.checkNulls( aPluginInfo, aChangedVersion );
    id = aPluginInfo.id;
    type = aPluginInfo.type;
    className = aPluginInfo.className;
    jarFileName = aPluginInfo.jarFileName;
    version = aChangedVersion;
    dependencyInfoList = aPluginInfo.dependencyInfoList;
    userProps = aPluginInfo.userProps;
  }

  public IList<IDependencyInfo> getDependencyInfoList() {
    return dependencyInfoList;
  }

  // ------------------------------------------------------------------------------------
  // IPluginInfo
  //

  @Override
  public String pluginJarFileName() {
    return jarFileName;
  }

  @Override
  public String pluginClassName() {
    return className;
  }

  @Override
  public TsVersion pluginVersion() {
    return version;
  }

  @Override
  public String pluginType() {
    return type;
  }

  @Override
  public String pluginId() {
    return id;
  }

  @Override
  public IList<IDependencyInfo> listDependencies() {
    return dependencyInfoList;
  }

  @Override
  public IStringMap<String> userProperties() {
    return userProps;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //
  @Override
  public String toString() {
    return format( "%s[%s,%d.%d]", type, id, Integer.valueOf( version.verMajor() ), //$NON-NLS-1$
        Integer.valueOf( version.verMinor() ) );
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + id.hashCode();
    result = TsLibUtils.PRIME * result + type.hashCode();
    result = TsLibUtils.PRIME * result + version.hashCode();
    result = TsLibUtils.PRIME * result + className.hashCode();
    result = TsLibUtils.PRIME * result + jarFileName.hashCode();
    result = TsLibUtils.PRIME * result + dependencyInfoList.hashCode();
    result = TsLibUtils.PRIME * result + userProps.hashCode();
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
    IPluginInfo other = (IPluginInfo)aObject;
    if( !id.equals( other.pluginId() ) ) {
      return false;
    }
    if( !type.equals( other.pluginType() ) ) {
      return false;
    }
    if( !version.equals( other.pluginVersion() ) ) {
      return false;
    }
    if( !className.equals( other.pluginClassName() ) ) {
      return false;
    }
    if( !jarFileName.equals( other.pluginJarFileName() ) ) {
      return false;
    }
    if( !dependencyInfoList.equals( other.listDependencies() ) ) {
      return false;
    }
    if( !userProps.equals( other.userProperties() ) ) {
      return false;
    }
    return true;
  }

}
