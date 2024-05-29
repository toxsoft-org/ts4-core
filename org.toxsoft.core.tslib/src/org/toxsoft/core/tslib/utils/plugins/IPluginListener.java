package org.toxsoft.core.tslib.utils.plugins;

import org.toxsoft.core.tslib.utils.plugins.impl.IPlugin;

/**
 * Plugin listener.
 *
 * @author mvk
 */
public interface IPluginListener {

  /**
   * Called when the plugin is closed.
   *
   * @param aPlugin {@link IPlugin} plugin.
   */
  void onClose( IPlugin aPlugin );
}
