package org.toxsoft.core.tslib.utils.plugins.impl;

import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.wub.AbstractWubUnit;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация компонента контейнера {@link PluginBox} представляющего плагин.
 *
 * @author mvk
 */
public class PluginUnit
    extends AbstractWubUnit {

  private final Plugin plugin;

  /**
   * Конструктор.
   *
   * @param aId String идентификатор компонента
   * @param aParams {@link IOptionSet} параметры компонента
   * @param aPlugin {@link IPlugin} плагин компонента
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PluginUnit( String aId, IOptionSet aParams, IPlugin aPlugin ) {
    super( aId, aParams );
    plugin = (Plugin)TsNullArgumentRtException.checkNull( aPlugin );
  }

  // ------------------------------------------------------------------------------------
  // API наследников
  //
  /**
   * Возвращает плагин компонента
   *
   * @return {@link IPlugin} плагин
   */
  protected final IPlugin plugin() {
    return plugin;
  }

  // ------------------------------------------------------------------------------------
  // AbstractWubUnit
  //
  @Override
  protected ValidationResult doInit( ITsContextRo aEnviron ) {
    return ValidationResult.SUCCESS;
  }

  @Override
  protected void doDoJob() {
    // nop
  }

  @Override
  protected boolean doQueryStop() {
    plugin.close();
    return true;
  }
}
