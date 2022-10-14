package org.toxsoft.core.tsgui.chart.api;

import org.toxsoft.core.tslib.av.opset.*;

/**
 * Набор настроечных параметров какой-либо части модуля графиков.
 *
 * @author goga
 */
public interface IG2Params {

  /**
   * Возвращает имя потребителя настроечных параметров.
   * <p>
   * Возвращаемое значение используется либо как просто идентификатор, либо как имя класса для создания экземпляра
   * методом {@link Class#forName(String)}. Посе создания экземпляра ему "скармливаются" параметры {@link #params()}.
   *
   * @return String - имя класса-потребителя настроечных параметров
   */
  String consumerName();

  /**
   * Возвращает настроечные параметры.
   * <p>
   * Предполагается, что параметры "населяются" значениями с использованием паттернов {@link IAtomicOptionInfo} и
   * {@link IFimbedOptionInfo}.
   *
   * @return {@link IOptionSet} - настроечные параметры
   */
  IOptionSet params();

}
