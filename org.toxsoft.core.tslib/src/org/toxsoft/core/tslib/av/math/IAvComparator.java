package org.toxsoft.core.tslib.av.math;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Интерфейс с методами сравнения атомарных значений.
 * <p>
 * Возможны различные реализации данного интерфеса, отличающейся аспектами повеления:
 * <ul>
 * <li>неприсвоенные значения - если операнд имеет неприсовенное значение ( {@link IAtomicValue#isAssigned()}=false, то
 * методы могут выбрасывать исключение {@link AvUnassignedValueRtException} или возвращать значение;</li>
 * <li>приведение типов - операции могут требовать жесткое соответствие типов операндов, или реализовывать тот или иной
 * алгоритм автоматичесого приведения типов.</li>
 * </ul>
 * Все реализации интерфейса должны быть потоко-безопасными.
 *
 * @author hazard157
 */
public interface IAvComparator {

  /**
   * Сравнивает атомарные значения.
   *
   * @param aAv1 IAtomicValue - the left operand
   * @param aOp EAvCompareOp - the comparison operation
   * @param aAv2 IAtomicValue - the right operand
   * @return boolean - comparison result<br>
   *         <b>true</b> - the comparison condition is satisfied;<br>
   *         <b>false</b> - comparison condition is not met.
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException operands have different atomic types
   * @throws AvUnassignedValueRtException operand has not assigned value
   */
  boolean avCompare( IAtomicValue aAv1, EAvCompareOp aOp, IAtomicValue aAv2 );

  /**
   * Checks if comparison operation may be performed with the given arguments.
   * <p>
   * This is optional operation. If operation is not implemented it simple returns {@link ValidationResult#SUCCESS}.
   * <p>
   * Method does not throws an exception even if any argument is <code>null</code>, rather returns the error.
   *
   * @param aAv1 IAtomicValue - the left operand
   * @param aOp EAvCompareOp - the comparison operation
   * @param aAv2 IAtomicValue - the right operand
   * @return {@link ValidationResult} - the check result
   */
  default ValidationResult canCompare( IAtomicValue aAv1, EAvCompareOp aOp, IAtomicValue aAv2 ) {
    return ValidationResult.SUCCESS;
  }

}
