package org.toxsoft.core.tsgui.valed.api;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IValedControl} instance creation factory.
 * <p>
 * The only allowed implementation is {@link AbstractValedControlFactory}.
 *
 * @author hazard157
 */
public interface IValedControlFactory {

  /**
   * Returns the globally unique factory name.
   *
   * @return String - the globally unique factory name
   */
  String factoryName();

  /**
   * Creates the editor.
   *
   * @param <V> - edited value type
   * @param aContext {@link IEclipseContext} - the editor creation context
   * @return {@link IValedControl} - created editor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  <V> IValedControl<V> createEditor( ITsGuiContext aContext );

}
