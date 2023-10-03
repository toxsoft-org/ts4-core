package org.toxsoft.core.tsgui.ved.runtime;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tslib.gw.time.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VED runtime panel is a SWT control capable of "playing" the specified {@link IVedScreenCfg}.
 * <p>
 * TODO D2 conversion ?
 * <p>
 * TODO Access to the internal VISELs and actors ?
 * <p>
 * TODO layer, theme, etc management ?
 * <p>
 * TODO how this panel allows to implement the master object of the mnemoscheme ?
 * <p>
 * TODO how to manage Green World time (how to call {@link IGwTimeFleetable#whenGwTimePassed(long)} of actors) ?
 *
 * @author hazard157
 */
public interface IVedRuntimePanel
    extends ILazyControl<Control>, IPausableAnimation, ICloseable {

  /**
   * Returns the configuration data of the VED screen played now.
   *
   * @return {@link IVedScreenCfg} - current VED screen configuration passed as argument to
   *         {@link #setVedScreenCfg(IVedScreenCfg)}
   */
  IVedScreenCfg getVedScreenCfg();

  /**
   * Sets the configuration data of the VED screen to be played.
   * <p>
   * Note: the implementation uses and holds reference to the argument, does not creates the copy.
   *
   * @param aCfg {@link IVedScreenCfg} - VED screen configuration
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setVedScreenCfg( IVedScreenCfg aCfg );

}
