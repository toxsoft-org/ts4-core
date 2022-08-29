package org.toxsoft.core.tsgui.graphics.patterns;

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
public abstract class AbstractGradientInfo
    implements IGradientInfo {

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "SwtPatternInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<IGradientInfo> KEEPER =
      new AbstractEntityKeeper<>( IGradientInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IGradientInfo aEntity ) {
          EGradientType gType = aEntity.gradientType();
          EGradientType.KEEPER.write( aSw, gType );
          aSw.writeSeparatorChar();
          switch( gType ) {
            case NONE:
              break;
            case CYLINDER:
              CylinderGradientInfo.keeper().write( aSw, (CylinderGradientInfo)aEntity );
              break;
            case LINEAR:
              LinearGradientInfo.keeper().write( aSw, (LinearGradientInfo)aEntity );
              break;
            case RADIAL:
              RadialGradientInfo.keeper().write( aSw, (RadialGradientInfo)aEntity );
              break;
            default:
              throw new TsNotAllEnumsUsedRtException();
          }
        }

        @Override
        protected IGradientInfo doRead( IStrioReader aSr ) {
          EGradientType gType = EGradientType.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          switch( gType ) {
            case NONE:
              break;
            case CYLINDER:
              return CylinderGradientInfo.keeper().read( aSr );
            case LINEAR:
              return LinearGradientInfo.keeper().read( aSr );
            case RADIAL:
              return RadialGradientInfo.keeper().read( aSr );
            default:
              throw new TsNotAllEnumsUsedRtException();
          }
          return null;
        }
      };

  // ------------------------------------------------------------------------------------
  // IGradientInfo
  //

  // @Override
  // public IGradient createGradient( IGradientInfo aInfo, ITsGuiContext aContext ) {
  // switch( aInfo.gradientType() ) {
  // case NONE:
  // break;
  // case CYLINDER:
  // return new CylinderGradient( (CylinderGradientInfo)aInfo, aContext );
  // case LINEAR:
  // return new LinearGradient( (LinearGradientInfo)aInfo, aContext );
  // case RADIAL:
  // return new RadialGradient( (RadialGradientInfo)aInfo, aContext );
  // default:
  // throw new TsNotAllEnumsUsedRtException();
  // }
  // return null;
  // }

}
