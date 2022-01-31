package org.toxsoft.core.tsgui.rcp;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator
    extends Plugin {

  /**
   * The plugin ID for Java static import.
   */
  public static final String PLUGIN_ID = "org.toxsoft.tsgui.rcp"; //$NON-NLS-1$

  // The shared instance
  private static Activator plugin;

  /**
   * The constructor.
   */
  public Activator() {
    // nop
  }

  @Override
  public void start( BundleContext context )
      throws Exception {
    super.start( context );
    plugin = this;
  }

  @Override
  public void stop( BundleContext context )
      throws Exception {
    plugin = null;
    super.stop( context );
  }

  /**
   * Returns the shared instance
   *
   * @return the shared instance
   */
  public static Activator getDefault() {
    return plugin;
  }

}
