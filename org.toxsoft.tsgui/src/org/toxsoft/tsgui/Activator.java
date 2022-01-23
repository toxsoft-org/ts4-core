package org.toxsoft.tsgui;

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
  public static final String PLUGIN_ID = "org.toxsoft.tsgui"; //$NON-NLS-1$

  // The shared instance
  private static Activator instance = null;

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
    instance = this;
  }

  @Override
  public void stop( BundleContext context )
      throws Exception {
    instance = null;
    super.stop( context );
  }

  /**
   * Returns the shared instance
   *
   * @return the shared instance
   */
  public static Activator getInstance() {
    return instance;
  }

}
