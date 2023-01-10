package org.toxsoft.core.tsgui.m5.model.helpers;

import org.toxsoft.core.tsgui.m5.*;

/**
 * Смешиваемый инетрфейс полей, содержащие моделированные сущности.у
 *
 * @author hazard157
 * @param <V> - тип моделированной сущности, содержащейся в поле
 */
public interface IM5MixinModelledField<V> {

  /**
   * Возвращает модель элементов коллекции - значения поля.
   *
   * @return {@link IM5Model}&lt;V&gt; - модель элементов коллекции - значения поля
   */
  IM5Model<V> itemModel();

}
