package org.toxsoft.tsgui.m5_1.api.helpers;

import org.toxsoft.tsgui.m5_1.impl.M5FieldDef;
import org.toxsoft.tsgui.utils.ITsVisualsProvider;

/**
 * Интерфейс извлечения значения (и отображаемой строки) из моделированной сущности.
 * <p>
 * Служит для настройки экземпляра описания поля {@link M5FieldDef}.
 *
 * @author goga
 * @param <T> - тип моделированной сущности
 * @param <V> - тип значения поля
 */
public interface IM5Getter<T, V> {

  /**
   * Returns the field value of the specified entity.
   * <p>
   * If <code>aEntity</code> is <code>null</code>, depending on implementation method may return some value or even
   * throw an exception.
   *
   * @param aEntity &lt;T&gt; - the entity, may be <code>null</code>
   * @return &lt;&gt; - the field value, may be <code>null</code>
   */
  V getFieldValue( T aEntity );

  /**
   * Returns the field value representation means.
   *
   * @return {@link ITsVisualsProvider}&lt;T&gt; - the field value representation means
   */
  ITsVisualsProvider<T> visualsProvider();

}
