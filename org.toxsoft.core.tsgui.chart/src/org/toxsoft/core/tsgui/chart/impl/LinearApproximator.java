package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.temporal.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Линейный аппроксиматор.
 *
 * @author vs
 */
public class LinearApproximator
    implements IApproximator {

  @Override
  public IAtomicValue approximate( ITemporalAtomicValue aLeftValue, ITemporalAtomicValue aRightValue,
      long aTimeStamp ) {

    if( !aLeftValue.value().isAssigned() && !aRightValue.value().isAssigned() ) {
      return IAtomicValue.NULL;
    }

    if( !aRightValue.value().isAssigned() ) {
      return aLeftValue.value();
    }

    if( !aLeftValue.value().isAssigned() ) {
      return aRightValue.value();
    }

    double x1 = aLeftValue.timestamp();
    double x2 = aRightValue.timestamp();
    double y1 = aLeftValue.value().asDouble();
    double y2 = aRightValue.value().asDouble();

    TsIllegalArgumentRtException.checkTrue( x2 == x1 );

    double x = aTimeStamp;

    double y = ((x - x1) / (x2 - x1)) * (y2 - y1) + y1;

    return AvUtils.avFloat( y );
  }

}
