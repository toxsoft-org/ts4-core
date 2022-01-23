package org.toxsoft.tslib.av.math;

import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.errors.AvTypeCastRtException;
import org.toxsoft.tslib.av.errors.AvUnassignedValueRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

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
   * @param aAv1 IAtomicValue - левый операнд сравнения
   * @param aOp EAvCompareOp - опреция сравнения
   * @param aAv2 IAtomicValue - правый операнд сравнения
   * @return boolean - результат сравнения<br>
   *         <b>true</b> - удвлетворяется условие сравнения aAv1 aOp aAv2;<br>
   *         <b>false</b> - условие сравнения aAv1 aOp aAv2 не воплняется.
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws AvTypeCastRtException одно из значении не является доспустимым
   * @throws AvUnassignedValueRtException одно из значении не содержит значения
   */
  boolean avCompare( IAtomicValue aAv1, EAvCompareOp aOp, IAtomicValue aAv2 );

}
