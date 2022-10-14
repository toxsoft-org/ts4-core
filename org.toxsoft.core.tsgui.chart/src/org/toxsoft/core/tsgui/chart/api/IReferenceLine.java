package org.toxsoft.core.tsgui.chart.api;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.*;

public interface IReferenceLine
    extends IStridable {

  IAtomicValue refValue();

  IG2Params rendererInfo();

}
