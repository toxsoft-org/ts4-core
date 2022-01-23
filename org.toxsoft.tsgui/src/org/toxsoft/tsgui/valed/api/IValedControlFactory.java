package org.toxsoft.tsgui.valed.api;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IValedControl} instance creation factory.
 *
 * @author hazard157
 */
public interface IValedControlFactory {

  /**
   * Returns the globally uinque factory name.
   * <p>
   * Factory may be named as of created control name, eg. "ts.core.ValedAvIntSpinner".
   *
   * @return String - the globally uinque factory name
   */
  String factoryName();

  /**
   * Создает редактор.
   *
   * @param <V> - конкретный тип редактируемого значения
   * @param aContext {@link IEclipseContext} - контекст редактора с параметрами
   * @return {@link IValedControl} - созданный редактор
   * @throws TsNullArgumentRtException aContext или aParams аргумент = null
   */
  <V> IValedControl<V> createEditor( ITsGuiContext aContext );

}
