package org.toxsoft.core.tsgui.ved.std.comps;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.std.IVedStdProperties.*;
import static org.toxsoft.core.tsgui.ved.std.comps.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.impl.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.std.library.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;

/**
 * VED staandard component: filled rectangle.
 *
 * @author hazard157, vs
 */
public class VedStdCompRoundRectangle
    extends VedAbstractComponent {

  /**
   * Component kind ID.
   */
  public static final String KIND_ID = "roundRectangle"; //$NON-NLS-1$

  /**
   * Property: component arc width.
   */
  public static final IDataDef OPDEF_ARC_WIDTH = DataDef.create( "arcWidth", FLOATING, // //$NON-NLS-1$
      TSID_NAME, STR_N_ARC_WIDTH, //
      TSID_DESCRIPTION, STR_D_ARC_WIDTH, //
      TSID_DEFAULT_VALUE, avFloat( 16.0 ) //
  );

  /**
   * Property: component arc height.
   */
  public static final IDataDef OPDEF_ARC_HEIGHT = DataDef.create( "arcHeight", FLOATING, // //$NON-NLS-1$
      TSID_NAME, STR_N_ARC_HEIGHT, //
      TSID_DESCRIPTION, STR_D_ARC_HEIGHT, //
      TSID_DEFAULT_VALUE, avFloat( 16.0 ) //
  );

  /**
   * Component provider singleton.
   */
  public static final VedAbstractComponentProvider PROVIDER =
      new VedAbstractComponentProvider( VedStdLibraryShapes.LIBRARY_ID, KIND_ID, OptionSetUtils.createOpSet( //
          TSID_NAME, STR_N_VSC_ROUND_RECT, //
          TSID_DESCRIPTION, STR_D_VSC_ROUND_RECT, //
          TSID_ICON_ID, ICONID_COMP_ROUNDRECT //
      ), //
          PDEF_X, //
          PDEF_Y, //
          PDEF_WIDTH, //
          PDEF_HEIGHT, //
          PDEF_FG_COLOR, //
          PDEF_FILL_INFO, //
          PDEF_ROTATION_ANGLE, //
          OPDEF_ARC_WIDTH, //
          OPDEF_ARC_HEIGHT ) {

        @Override
        protected IVedComponent doCreateComponent( String aCompId, IVedEnvironment aEnvironment, IOptionSet aProps,
            IOptionSet aExtdata ) {
          IVedComponent c = new VedStdCompRoundRectangle( aCompId, aEnvironment );
          c.props().setProps( aProps );
          c.extdata().setAll( aExtdata );
          return c;
        }
      };

  /**
   * Constructor.
   *
   * @param aId String - component ID
   * @param aVedEnv {@link IVedEnvironment} - the VED envoronment
   */
  public VedStdCompRoundRectangle( String aId, IVedEnvironment aVedEnv ) {
    super( PROVIDER, aVedEnv, aId );
  }

  @Override
  protected VedAbstractComponentView doCreateView( IVedScreen aScreen ) {
    return new VedStdCompRoundRectangleView( this, aScreen );
  }

}
