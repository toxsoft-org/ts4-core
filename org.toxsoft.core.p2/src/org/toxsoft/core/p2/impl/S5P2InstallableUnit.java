package org.toxsoft.core.p2.impl;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * s5-идентифицируемая оболочка над {@link IInstallableUnit}
 *
 * @author mvk
 */
class S5P2InstallableUnit {

  private final IInstallableUnit unit;

  /**
   * Конструктор
   *
   * @param aUnit {@link IInstallableUnit} модуль установки
   * @throws TsNullArgumentRtException аргумент = null
   */
  S5P2InstallableUnit( IInstallableUnit aUnit ) {
    unit = TsNullArgumentRtException.checkNull( aUnit );
  }

  /**
   * Возвращает идентификатор обновляемого модуля
   *
   * @return String идентификатор модуля
   */
  String id() {
    return unit.getId();
  }

  /**
   * Возвращает модуль установки
   *
   * @return {@link IInstallableUnit} модуль установки
   */
  IInstallableUnit unit() {
    return unit;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return id();
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof S5P2InstallableUnit obj ) {
      return id().equals( obj.id() );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + id().hashCode();
    return result;
  }
}
