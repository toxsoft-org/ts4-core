package org.toxsoft.core.tsgui.m5.model.helpers;

/**
 * Смешиваемый интерфейс полей, содержащих ключи, а не сами моделированные сущности.
 *
 * @author goga
 * @param <V> - тип моделированной сущности, содержащейся в поле
 */
public interface IM5MixinKeyLookupField<V>
    extends IM5MixinLookupField<V>, IM5MixinModelledField<V> {

  /**
   * Возвращает идентификатор поля элементов справочника, который используется как ключ.
   *
   * @return String - идентификатор ключевого поля в модели справочных элементов
   */
  String keyFieldId();

  /**
   * Возвращает тип элемента коллекции - то есть, ключа.
   *
   * @return {@link Class} - тип (класс) ключа
   */
  Class<?> keyType();

}
