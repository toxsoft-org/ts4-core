package org.toxsoft.core.tslib.utils.plugins.impl;

import static java.lang.String.*;
import static org.toxsoft.core.tslib.utils.plugins.IPluginsHardConstants.*;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.plugins.*;
import org.toxsoft.core.tslib.utils.plugins.IPluginInfo.*;

/**
 * Неизменяемый класс - реализация интерфейса {@link IDependencyInfo}.
 *
 * @author Dima
 * @author hazard157
 */
class DepedencyInfo
    implements IDependencyInfo {

  /**
   * Требование точной версии.
   */
  private final boolean exactVersionNeeded;

  /**
   * Номер требуемой версии.
   */
  private final TsVersion version;

  /**
   * Тип модуля плагина.
   */
  private final String pluginType;

  /**
   * Id модуля плагина.
   */
  private final String pluginId;

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aPluginType String - тип требуемого плагина
   * @param aPluginId String - идентификатор требуемого плагина
   * @param aNeededVersion TsVersion - версия требуемого плагина
   * @param aIsExactVersionneeded boolean - признак того, что требуется точная, а не более высокая вер
   */
  private DepedencyInfo( String aPluginType, String aPluginId, TsVersion aNeededVersion,
      boolean aIsExactVersionneeded ) {
    TsNullArgumentRtException.checkNulls( aPluginType, aPluginId, aNeededVersion );
    pluginType = aPluginType;
    pluginId = aPluginId;
    version = aNeededVersion;
    exactVersionNeeded = aIsExactVersionneeded;
  }

  /**
   * Создает описание зависимости из строки, заданной в аттрибуте с префиксом
   * {@link IPluginsHardConstants#MF_ATTR_PREFIX_DEPENDENCY} в файле манифеста.
   *
   * @param aDependencyString String - строка описания зависимости, согласно описанию
   *          {@link IPluginsHardConstants#MF_DEPENDENCY_LINE_PARTS_DELIMITER}
   * @return IDependencyInfo - информация, полученная разбором строки
   */
  public static IDependencyInfo createFromString( String aDependencyString ) {
    String[] ss = aDependencyString.split( MF_DEPENDENCY_LINE_PARTS_DELIMITER );
    String pType = ss[0].trim();
    String pId = ss[1].trim();
    TsVersion pVer = TsVersion.KEEPER.str2ent( ss[2].trim() );
    boolean needExactVersion = Boolean.parseBoolean( ss[3] );
    return new DepedencyInfo( pType, pId, pVer, needExactVersion );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IPluginBasicInfo
  //

  @Override
  public TsVersion pluginVersion() {
    return version;
  }

  @Override
  public String pluginType() {
    return pluginType;
  }

  @Override
  public String pluginId() {
    return pluginId;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IDependencyInfo
  //

  @Override
  public boolean isExactVersionNeeded() {
    return exactVersionNeeded;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //
  @Override
  public String toString() {
    return format( "%s[%s,%d.%d]", pluginType, pluginId, Integer.valueOf( version.verMajor() ), //$NON-NLS-1$
        Integer.valueOf( version.verMinor() ) );
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + pluginId.hashCode();
    result = TsLibUtils.PRIME * result + pluginType.hashCode();
    result = TsLibUtils.PRIME * result + version.hashCode();
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
    DepedencyInfo other = (DepedencyInfo)aObject;
    if( !pluginId.equals( other.pluginId ) ) {
      return false;
    }
    if( !pluginType.equals( other.pluginType ) ) {
      return false;
    }
    if( !version.equals( other.version ) ) {
      return false;
    }
    return true;
  }
}
