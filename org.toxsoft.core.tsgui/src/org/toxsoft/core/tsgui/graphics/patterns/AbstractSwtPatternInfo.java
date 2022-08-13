package org.toxsoft.core.tsgui.graphics.patterns;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Базовый класс для параметров заливок.
 * <p>
 *
 * @author vs
 */
public abstract class AbstractSwtPatternInfo
    implements ISwtPatternInfo {

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "SwtPatternInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ISwtPatternInfo> KEEPER =
      new AbstractEntityKeeper<>( ISwtPatternInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ISwtPatternInfo aEntity ) {
          ESwtPatternType type = aEntity.type();
          ESwtPatternType.KEEPER.write( aSw, type );
          aSw.writeSeparatorChar();
          EGradientType gType = aEntity.gradientType();
          EGradientType.KEEPER.write( aSw, gType );
          aSw.writeSeparatorChar();
          if( type == ESwtPatternType.GRADIENT ) {
            switch( gType ) {
              case NONE:
                break;
              case CYLINDER:
                break;
              case LINEAR:
                LinearGradientInfo.keeper().write( aSw, (LinearGradientInfo)aEntity );
                break;
              case RADIAL:
                break;
              default:
                throw new TsNotAllEnumsUsedRtException();
            }
          }
        }

        @Override
        protected ISwtPatternInfo doRead( IStrioReader aSr ) {
          ESwtPatternType type = ESwtPatternType.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          EGradientType gType = EGradientType.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          if( type == ESwtPatternType.GRADIENT ) {
            switch( gType ) {
              case NONE:
                break;
              case CYLINDER:
                return CylinderGradientInfo.keeper().read( aSr );
              case LINEAR:
                return LinearGradientInfo.keeper().read( aSr );
              case RADIAL:
                break;
              default:
                throw new TsNotAllEnumsUsedRtException();
            }
          }
          return null;
        }
      };

  // ------------------------------------------------------------------------------------
  // ISwtPatternInfo
  //

  @Override
  public ISwtPattern createSwtPattern( ISwtPatternInfo aInfo, ITsGuiContext aContext ) {
    switch( aInfo.gradientType() ) {
      case NONE:
        break;
      case CYLINDER:
        return new CylinderGradientPattern( (CylinderGradientInfo)aInfo, aContext );
      case LINEAR:
        return new LinearGradientPattern( (LinearGradientInfo)aInfo, aContext );
      case RADIAL:
        return new RadialGradientPattern( (RadialGradientInfo)aInfo, aContext );
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    return null;
  }

}
