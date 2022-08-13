package org.toxsoft.core.tsgui.ved.std.comps;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.std.IVedStdProperties.*;
import static org.toxsoft.core.tsgui.ved.std.comps.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.impl.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.std.library.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;

/**
 * VED staandard component: filled rectangle.
 *
 * @author hazard157, vs
 */
public class VedStdCompRectangle
    extends VedAbstractComponent {

  /**
   * Component kind ID.
   */
  public static final String KIND_ID = "rectangle"; //$NON-NLS-1$

  /**
   * Component provider singleton.
   */
  public static final VedAbstractComponentProvider PROVIDER =
      new VedAbstractComponentProvider( VedStdLibraryShapes.LIBRARY_ID, KIND_ID, OptionSetUtils.createOpSet( //
          TSID_NAME, STR_N_VSC_RECT, //
          TSID_DESCRIPTION, STR_D_VSC_RECT, //
          TSID_ICON_ID, ICONID_COMP_RECTANGLE //
      ), //
          PDEF_X, //
          PDEF_Y, //
          PDEF_WIDTH, //
          PDEF_HEIGHT, //
          PDEF_FG_COLOR, //
          PDEF_BG_COLOR, //
          PDEF_BG_PATTERN, //
          PDEF_ROTATION_ANGLE //
      ) {

        @Override
        protected IVedComponent doCreateComponent( String aCompId, IVedEnvironment aEnvironment, IOptionSet aProps,
            IOptionSet aExtdata ) {
          IVedComponent c = new VedStdCompRectangle( aCompId, aEnvironment );
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
  public VedStdCompRectangle( String aId, IVedEnvironment aVedEnv ) {
    super( PROVIDER, aVedEnv, aId );
  }

  @Override
  protected VedAbstractComponentView doCreateView( IVedScreen aScreen ) {
    return new VedStdCompRectangleView( this, aScreen );
  }

}
