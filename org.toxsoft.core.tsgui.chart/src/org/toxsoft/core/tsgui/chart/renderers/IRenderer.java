package org.toxsoft.core.tsgui.chart.renderers;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tslib.av.opset.*;

/**
 * Внутренный интерфейс рендереров, создаваемых из {@link IG2Params}.
 *
 * @author goga
 */
interface IRenderer {

  void init( IOptionSet aParams );

}
