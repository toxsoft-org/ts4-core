package org.toxsoft.core.tsgui.ved.std.comps;

import static org.toxsoft.core.tsgui.ved.std.IVedStdProperties.*;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
import org.toxsoft.core.tsgui.ved.api.entity.*;
import org.toxsoft.core.tsgui.ved.api.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VED builtin componen "Rectangle shape" provider.
 *
 * @author hazard157
 */
public class VedStdShapeRectangleProvider
    extends VedAbstractEntityProvider {

  /**
   * The provider ID.
   */
  public static final String PROVIDER_ID = VED_COMP_ID + "RectangleShape"; //$NON-NLS-1$

  private static final IStridablesList<IDataDef> PROP_DEFS = new StridablesList<>( //
      PDEF_NAME, //
      PDEF_DESCRIPTION, //
      PDEF_X, //
      PDEF_Y, //
      PDEF_WIDTH, //
      PDEF_HEIGHT, //
      PDEF_FILL_INFO, //
      PDEF_BORDER_INFO, //

      PDEF_ROTATION, //
      PDEF_FLIP_HOR, //
      PDEF_FLIP_VER, //
      PDEF_FULCRUM, //
      PDEF_ZOOM_X, //
      PDEF_ZOOM_Y //
  );

  /**
   * Constructor.
   */
  public VedStdShapeRectangleProvider() {
    super( EVedEntityKind.COMPONENT, PROVIDER_ID, IOptionSet.NULL, PROP_DEFS );
  }

  @Override
  protected IVedEntity doCreateEntity( IVedEntityConfig aCfg, IVedEnvironment aVedEnv ) {
    // TODO реализовать VedStdShapeRectangleProvider.doCreateEntity()
    throw new TsUnderDevelopmentRtException( "VedStdShapeRectangleProvider.doCreateEntity()" );
  }

}
