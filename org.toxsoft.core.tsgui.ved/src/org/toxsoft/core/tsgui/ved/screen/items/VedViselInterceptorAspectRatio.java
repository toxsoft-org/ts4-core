package org.toxsoft.core.tsgui.ved.screen.items;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.helpers.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.opset.*;

/**
 * VISEL interceptor: processes aspect ratio.
 * <p>
 * If aspect ration is fixed, {@link IVedScreenConstants#PROP_IS_ASPECT_FIXED} = <code>true</code>, then keeps VISEL's
 * width/height relation to the value specified in the property {@link IVedScreenConstants#PROP_ASPECT_RATIO}. If aspect
 * ration is <b>not</b> fixed (the default) then simply updates value of the aspect ratio property.
 * <p>
 * Requires following properties:
 * <ul>
 * <li>{@link IVedScreenConstants#PROP_IS_ASPECT_FIXED};</li>
 * <li>{@link IVedScreenConstants#PROP_ASPECT_RATIO}</li>
 * </ul>
 * <p>
 * This is a stateless class so only singleton instance exists.
 *
 * @author hazard157
 */
public class VedViselInterceptorAspectRatio
    implements IVedItemPropertyChangeInterceptor<VedAbstractVisel> {

  /**
   * The singleton instance.
   */
  public static final IVedItemPropertyChangeInterceptor<VedAbstractVisel> INSTANCE =
      new VedViselInterceptorAspectRatio();

  private VedViselInterceptorAspectRatio() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IVedItemPropertyChangeInterceptor
  //

  @Override
  public void interceptPropsChange( VedAbstractVisel aSource, IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    // get actual width and height
    double width;
    if( aValuesToSet.hasKey( PROPID_WIDTH ) ) {
      width = aValuesToSet.getDouble( PROPID_WIDTH );
    }
    else {
      width = aSource.props().getDouble( PROPID_WIDTH );
    }
    double height;
    if( aValuesToSet.hasKey( PROPID_HEIGHT ) ) {
      height = aValuesToSet.getDouble( PROPID_HEIGHT );
    }
    else {
      height = aSource.props().getDouble( PROPID_HEIGHT );
    }
    // update PROP_ASPECT_RATIO for non-fixed aspect
    if( !aSource.props().getBool( PROP_IS_ASPECT_FIXED ) ) {
      double aspectRatio = width / height;
      aValuesToSet.setDouble( PROPID_ASPECT_RATIO, aspectRatio );
      return;
    }
    // keep current aspect ratio
    double currAspectRatio = aSource.props().getDouble( PROPID_WIDTH ) / aSource.props().getDouble( PROPID_HEIGHT );
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

}
