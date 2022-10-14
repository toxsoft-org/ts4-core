package org.toxsoft.core.tsgui.chart.legaсy;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Интерфейс визуальных сущеностей, требующих освобождения ресурсов ОС.
 * <p>
 * Обратите внимание, что работающие с ресурсами ОС методы класса должны выбрасывать исключение
 * {@link TsIllegalStateRtException}, если ресурсы уже освобождены (или не были выделены). То есть, если
 * {@link #isDisposed()} = <code>true</code>.
 *
 * @author goga
 */
public interface IDisposable {

  /**
   * Определяет, освобождены ли уже выделенные ранее ресурсы.
   *
   * @return boolean - признак, что ресурсы ОС уже освобождены
   */
  boolean isDisposed();

  /**
   * Освобождает ресурсы ОС, ранее выделенные в классе.
   */
  void dispose();

}
