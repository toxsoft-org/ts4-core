package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.opset.*;

/**
 * VED items (VISELs and actors) implementation helper methods.
 *
 * @author hazard157
 */
public class VedItemUtils {

  /**
   * Performs VISEL property change interception to process aspect ratio.
   * <p>
   * This interceptor is FIXME maybe <br>
   * interface IVedItemPropsChangeInterceptor {}<br>
   * VedAbstractItem.addInterseptor()
   *
   * @param aVisel
   * @param aNewValues
   * @param aValuesToSet
   */
  public static void interceptAspectRatio( VedAbstractVisel aVisel, IOptionSet aNewValues,
      IOptionSetEdit aValuesToSet ) {
    // get actual width and height
    double width;
    if( aValuesToSet.hasKey( PROPID_WIDTH ) ) {
      width = aValuesToSet.getDouble( PROPID_WIDTH );
    }
    else {
      width = aVisel.props().getDouble( PROPID_WIDTH );
    }
    double height;
    if( aValuesToSet.hasKey( PROPID_HEIGHT ) ) {
      height = aValuesToSet.getDouble( PROPID_HEIGHT );
    }
    else {
      height = aVisel.props().getDouble( PROPID_HEIGHT );
    }
    // update PROP_ASPECT_RATIO for non-fixed aspect
    if( !aVisel.props().getBool( PROP_IS_ASPECT_FIXED ) ) {
      double aspectRatio = width / height;
      aValuesToSet.setDouble( PROPID_ASPECT_RATIO, aspectRatio );
      return;
    }
    // keep current aspect ratio
    double currAspectRatio = aVisel.props().getDouble( PROPID_WIDTH ) / aVisel.props().getDouble( PROPID_HEIGHT );
    boolean wasWidthChangeRequested = aValuesToSet.hasKey( PROPID_WIDTH );
    boolean wasHeightChangeRequested = aValuesToSet.hasKey( PROPID_HEIGHT );
    // when changing both width and height, ignore height change request
    if( wasWidthChangeRequested && wasHeightChangeRequested ) {
      wasHeightChangeRequested = false;
    }
    if( wasWidthChangeRequested ) {
      aValuesToSet.setDouble( PROPID_HEIGHT, width / currAspectRatio );
      return;
    }
    if( wasHeightChangeRequested ) {
      aValuesToSet.setDouble( PROPID_WIDTH, height * currAspectRatio );
      return;
    }
  }

  /**
   * No subclass.
   */
  private VedItemUtils() {
    // nop
  }

}
