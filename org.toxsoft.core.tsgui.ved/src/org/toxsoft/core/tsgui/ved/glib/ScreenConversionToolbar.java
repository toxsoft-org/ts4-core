package org.toxsoft.core.tsgui.ved.glib;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
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
  private final EIconSize TOOLBAR_ICON_SIZE = EIconSize.IS_32X32;
  private final double    ZOOM_STEP_FACTOR  = Math.pow( 2.0, 0.25 ); // 4 steps = 2x times

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
        ACDEF_ZOOM_IN, ACDEF_ZOOM_ORIGINAL, ACDEF_ZOOM_OUT, //
        ACDEF_SEPARATOR //
    );
    toolbar.setIconSize( TOOLBAR_ICON_SIZE );
    toolbar.addListener( this::processControl );
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
        // conv.setZoomFactor( conv.zoomFactor() * ZOOM_STEP_FACTOR );
        conv.setZoomFactor( 2 );
        D2AngleEdit angle = new D2AngleEdit();
        // angle.setDeg( conv.zoomFactor() * ZOOM_STEP_FACTOR * 5 );
        angle.setDeg( -15 );
        conv.rotation().setAngle( angle );
        conv.origin().setX( 100 );
        conv.origin().setY( 0 );
        break;
      }
      case ACTID_ZOOM_ORIGINAL: {
        conv.setZoomFactor( 2.0 );
        conv.setZoomFactor( 1.0 );
        // conv.rotation().setRotation( ID2Rotation.NONE );
        D2AngleEdit angle = new D2AngleEdit();
        angle.setDeg( 0 );
        conv.rotation().setAngle( angle );
        conv.origin().setX( 0 );
        conv.origin().setY( 0 );
        break;
      }
      case ACTID_ZOOM_OUT: {
        // conv.setZoomFactor( conv.zoomFactor() / ZOOM_STEP_FACTOR );
        conv.setZoomFactor( 0.5 );
        D2AngleEdit angle = new D2AngleEdit();
        // angle.setDeg( -conv.zoomFactor() * ZOOM_STEP_FACTOR * 5 );
        angle.setDeg( 15 );
        conv.rotation().setAngle( angle );
        conv.origin().setX( 0 );
        conv.origin().setY( 100 );
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
