package org.toxsoft.core.tsgui.chart.api;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.time.*;

public interface IZone
    extends IGenericChangeEventCapable {

  boolean isVisible();

  void setVisible( boolean aVisible );

  IG2Params rendererParams();

  ITimeInterval timeInterval();

  void setTimeInterval( ITimeInterval aInterval );

}
