package org.toxsoft.core.tsgui.ved.screen.items;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.helpers.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.math.*;

/**
 * VISEL interceptor: guarantees that requested width and height is positive and greater than minimal values.
 * <p>
 * After interception width and height values in <code>aValuesToSet</code> argument are guaranteed to be grater or equal
 * to 1.0, and if specified {@link IAvMetaConstants#TSID_MIN_INCLUSIVE}, {@link IAvMetaConstants#TSID_MIN_EXCLUSIVE},
 * {@link IAvMetaConstants#TSID_MAX_INCLUSIVE} and {@link IAvMetaConstants#TSID_MAX_EXCLUSIVE} constraints of the width
 * and height properties are processed.
 * <p>
 * Requires following properties:
 * <ul>
 * <li>{@link IVedScreenConstants#PROP_WIDTH};</li>
 * <li>{@link IVedScreenConstants#PROP_HEIGHT}</li>
 * </ul>
 * <p>
 * Also checks that requested value is finite <code>double</code> (not any NaN or Infinity), otherwise throws an
 * exception.
 *
 * @author hazard157
 */
public class VedViselInterceptorMinWidthHeight
    implements IVedItemPropertyChangeInterceptor<VedAbstractVisel> {

  private static final double MIN_VALUE = 1.0;

  private final DoubleRange widthRange;
  private final DoubleRange heightRange;

  /**
   * Constructor.
   *
   * @param aVisel {@link VedAbstractVisel} - owner VISEL
   */
  public VedViselInterceptorMinWidthHeight( VedAbstractVisel aVisel ) {
    widthRange = makeRange( aVisel.props().propDefs().getByKey( PROPID_WIDTH ) );
    heightRange = makeRange( aVisel.props().propDefs().getByKey( PROPID_HEIGHT ) );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static DoubleRange makeRange( IDataDef aPropDef ) {
    DoubleRange dr = IAvMetaConstants.makeDoubleRangeFromConstraints( aPropDef.params() );
    double minVal = Math.max( MIN_VALUE, dr.minValue() );
    double maxVal = Math.max( MIN_VALUE, dr.maxValue() );
    return new DoubleRange( minVal, maxVal );
  }

  // ------------------------------------------------------------------------------------
  // IPropertyChangeInterceptor
  //

  @Override
  public void interceptPropsChange( VedAbstractVisel aSource, IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    if( aNewValues.hasKey( PROPID_WIDTH ) ) {
      double width = aNewValues.getDouble( PROP_WIDTH );
      width = widthRange.inRange( width );
      aValuesToSet.setDouble( PROP_WIDTH, width );
    }

    if( aNewValues.hasKey( PROPID_HEIGHT ) ) {
      double height = aNewValues.getDouble( PROP_HEIGHT );
      height = heightRange.inRange( height );
      aValuesToSet.setDouble( PROP_HEIGHT, height );
    }
  }

}
