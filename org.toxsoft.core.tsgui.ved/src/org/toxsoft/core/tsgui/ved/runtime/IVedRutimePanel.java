package org.toxsoft.core.tsgui.ved.runtime;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VED runtime panel is a SWT control capable of "playing" the specified {@link IVedScreenCfg}.
 *
 * @author hazard157
 */
public interface IVedRutimePanel
    extends ILazyControl<Control>, IPausableAnimation {

  /**
   * Returns the configuration data of the VED screen played now.
   *
   * @return {@link IVedScreenCfg} - current VED screen configuration
   */
  IVedScreenCfg getVedScreenCfg();

  /**
   * Sets the configuration data of the VED screen to be played.
   *
   * @param aCfg {@link IVedScreenCfg} - VED screen configuration
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setVedScreenCfg( IVedScreenCfg aCfg );

  // TODO do we need access to the IVedEnvironment ?

}
