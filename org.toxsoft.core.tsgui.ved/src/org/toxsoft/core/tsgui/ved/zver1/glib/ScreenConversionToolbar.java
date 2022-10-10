package org.toxsoft.core.tsgui.ved.zver1.glib;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.ved.zver1.core.*;
import org.toxsoft.core.tsgui.ved.zver1.core.view.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Contains control to manage {@link IVedScreen#getConversion()} parameters.
 * <p>
 * Contains control to change zoom, shift and set location of screen, rotate screen.
 * <p>
 * Listens to the {@link IVedScreen#conversionChangeEventer()} and updates controls.
 *
 * @author hazard157
 */
public class ScreenConversionToolbar
    extends AbstractLazyPanel<Control> {

  // TODO move to application preferences ??? or make the same size as other icons in VED?
  private final EIconSize TOOLBAR_ICON_SIZE     = EIconSize.IS_32X32;
  private final double    ZOOM_STEP_FACTOR      = Math.pow( 2.0, 0.25 ); // 4 steps = 2x times
  private final double    ROTATION_STEP_DEGREES = 15.0;

  private final IGenericChangeListener screenConversionChangeListener = aSource -> updateActionsState();

  private final TsToolbar toolbar;

  private IVedScreen attachedScreen = null;

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aVedEnv {@link IVedEnvironment} - the VED environment
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ScreenConversionToolbar( Composite aParent, IVedEnvironment aVedEnv ) {
    super( aVedEnv.tsContext() );
    toolbar = new TsToolbar( tsContext() );
    toolbar.addActionDefs( //
        ACDEF_ZOOM_IN, ACDEF_ZOOM_ORIGINAL, ACDEF_ZOOM_OUT, ACDEF_SEPARATOR, //
        ACDEF_OBJECT_ROTATE_LEFT, ACDEF_OBJECT_ROTATE_RIGHT, //
        ACDEF_SEPARATOR //
    );
    toolbar.setIconSize( TOOLBAR_ICON_SIZE );
    toolbar.addListener( this::processControl );
    // TODO set mouse handler to VedScreen for Ctrl+Wheel zoom in/out
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void processControl( String aActionId ) {
    if( attachedScreen == null || toolbar.hasAction( aActionId ) && !toolbar.isActionEnabled( aActionId ) ) {
      return;
    }
    D2ConversionEdit conv = new D2ConversionEdit( attachedScreen.getConversion() );
    switch( aActionId ) {
      case ACTID_ZOOM_IN: {
        conv.setZoomFactor( conv.zoomFactor() * ZOOM_STEP_FACTOR );
        break;
      }
      case ACTID_ZOOM_ORIGINAL: {
        conv.setConversion( ID2Conversion.NONE );
        break;
      }
      case ACTID_ZOOM_OUT: {
        conv.setZoomFactor( conv.zoomFactor() / ZOOM_STEP_FACTOR );
        break;
      }
      case ACTID_OBJECT_ROTATE_LEFT: {
        ID2Angle angle = D2Angle.ofDegrees( conv.rotation().degrees() - ROTATION_STEP_DEGREES );
        conv.rotation().setAngle( angle );
        break;
      }
      case ACTID_OBJECT_ROTATE_RIGHT: {
        ID2Angle angle = D2Angle.ofDegrees( conv.rotation().degrees() + ROTATION_STEP_DEGREES );
        conv.rotation().setAngle( angle );
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( aActionId );
    }
    attachedScreen.setConversion( conv );
    attachedScreen.paintingManager().redraw();
  }

  private void updateActionsState() {
    if( attachedScreen == null ) {
      for( ITsActionDef actDef : toolbar.listButtonItems() ) {
        toolbar.setActionEnabled( actDef.id(), false );
      }
      return;
    }
    ID2Conversion conv = attachedScreen.getConversion();
    toolbar.setActionEnabled( ACTID_ZOOM_IN, conv.zoomFactor() < D2Utils.MAX_ZOOM_FACTOR );
    // toolbar.setActionEnabled( ACTID_ZOOM_ORIGINAL, conv.zoomFactor() != 1.0 );
    toolbar.setActionEnabled( ACTID_ZOOM_OUT, conv.zoomFactor() > D2Utils.MIN_ZOOM_FACTOR );

    // TODO ScreenConversionToolbar.internalUpdateControlsFromScreen()
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    return toolbar.createControl( aParent );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Establishes link between this toolbar and VED screen.
   *
   * @param aScreen {@link IVedScreen} - controlled screen
   */
  public void attachToScreen( IVedScreen aScreen ) {
    if( attachedScreen != null ) {
      attachedScreen.conversionChangeEventer().removeListener( screenConversionChangeListener );
    }
    attachedScreen = aScreen;
    if( attachedScreen != null ) {
      attachedScreen.conversionChangeEventer().addListener( screenConversionChangeListener );
    }
    updateActionsState();
  }

}
