package org.toxsoft.core.tslib.utils.plugins;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Requirement to the plugin JAR file.
 * <p>
 * Used may be used by clients to determine if concrete plugin {@link IPluginBasicInfo} should be loaded.
 *
 * @author hazard157
 */
public final class PluginFileRequirement {

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "SkatletPluginRequirement"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<PluginFileRequirement> KEEPER =
      new AbstractEntityKeeper<>( PluginFileRequirement.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, PluginFileRequirement aEntity ) {
          aSw.writeAsIs( aEntity.pluginTypeId );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.thePluginId );
          aSw.writeSeparatorChar();
          TsVersion.KEEPER.write( aSw, aEntity.minVersion() );
          aSw.writeSeparatorChar();
          TsVersion.KEEPER.write( aSw, aEntity.maxVersion() );
        }

        @Override
        protected PluginFileRequirement doRead( IStrioReader aSr ) {
          String typeId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String pluginId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          TsVersion minVer = TsVersion.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          TsVersion maxVer = TsVersion.KEEPER.read( aSr );
          return new PluginFileRequirement( typeId, pluginId, minVer, maxVer );
        }
      };

  private final String    pluginTypeId;
  private final String    thePluginId;
  private final TsVersion minVersion;
  private final TsVersion maxVersion;

  /**
   * Constructor.
   *
   * @param aTypeId String - plugin type ID (an IDpath)
   * @param aPluginId String - returned plugin ID (an IDpath)
   * @param aMinVersion {@link TsVersion} - minimal allowed version or {@link TsVersion#HIGHEST} for no limit
   * @param aMaxVersion {@link TsVersion} - maximal allowed version or {@link TsVersion#HIGHEST} for no limit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   * @throws TsIllegalArgumentRtException max version is lower than minimal version
   */
  public PluginFileRequirement( String aTypeId, String aPluginId, TsVersion aMinVersion, TsVersion aMaxVersion ) {
    StridUtils.checkValidIdPath( aTypeId );
    StridUtils.checkValidIdPath( aPluginId );
    TsNullArgumentRtException.checkNulls( aMinVersion, aMaxVersion );
    TsIllegalArgumentRtException.checkTrue( aMaxVersion.compareTo( aMinVersion ) < 0 );
    pluginTypeId = aTypeId;
    thePluginId = aPluginId;
    minVersion = aMinVersion;
    maxVersion = aMaxVersion;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the required plugin type ID.
   *
   * @return String - plugin type ID, always an IDpath
   */
  public String pluginTypeId() {
    return pluginTypeId;
  }

  /**
   * Returns the ID of the plugin.
   *
   * @return String - returned plugin ID, always an IDpath
   */
  public String thePluginId() {
    return thePluginId;
  }

  /**
   * Returns minimal allowed version of the plugin.
   *
   * @return {@link TsVersion} - minimal allowed version or {@link TsVersion#HIGHEST} for no limit
   */
  public TsVersion minVersion() {
    return minVersion;
  }

  /**
   * Returns maximal allowed version of the plugin.
   *
   * @return {@link TsVersion} - maximal allowed version or {@link TsVersion#HIGHEST} for no limit
   */
  public TsVersion maxVersion() {
    return maxVersion;
  }

  /**
   * Checks if asked plugin meets this requirements.
   *
   * @param aPluginInfo {@link IPluginBasicInfo} - the plugin to check
   * @return boolean - <code>true</code> plugin can be used, <code>false</code> - requirements are not met
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public boolean isRequirementsMet( IPluginBasicInfo aPluginInfo ) {
    TsNullArgumentRtException.checkNull( aPluginInfo );
    if( !aPluginInfo.pluginType().equals( pluginTypeId ) ) {
      return false;
    }
    if( !aPluginInfo.pluginId().equals( thePluginId ) ) {
      return false;
    }
    if( minVersion.compareTo( aPluginInfo.pluginVersion() ) < 0 ) {
      return false;
    }
    if( maxVersion.compareTo( aPluginInfo.pluginVersion() ) > 0 ) {
      return false;
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return pluginTypeId + ", " + thePluginId + ", " + minVersion.toString() + " - " + maxVersion.toString(); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
  }

}
