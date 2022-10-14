package org.toxsoft.core.tsgui.chart.renderers;

import org.toxsoft.core.tslib.av.opset.*;

import ru.toxsoft.tsgui.chart.api.*;

/**
 * Внутренный интерфейс рендереров, создаваемых из {@link IG2Params}.
 * 
 * @author goga
 */
interface IRenderer {

  void init( IOptionSet aParams );

}
