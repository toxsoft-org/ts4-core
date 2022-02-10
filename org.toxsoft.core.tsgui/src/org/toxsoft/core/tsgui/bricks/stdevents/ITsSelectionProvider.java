package org.toxsoft.core.tsgui.bricks.stdevents;

// TODO TRANSLATE

/**
 * Интерфейс классов имеющих понятие "текущий выбранный элемент".
 *
 * @author goga
 * @param <E> - тип элементов
 */
public interface ITsSelectionProvider<E>
    extends ITsSelectionChangeEventProducer<E> {

  /**
   * Вызвращает текущий выбранный элемент.
   *
   * @return &lt;E&gt; - текущий выбранный элемент или null, если нет выбранного элемента
   */
  E selectedItem();

  /**
   * Задает текущий выбранный элемент.
   * <p>
   * Некторые класс могут не позволять отсутствие выбранного элемента, и задание null может игнорироваться.
   * <p>
   * Внимание: реализация <b>не</b> должна генерировать сообщение
   * {@link ITsSelectionChangeListener#onTsSelectionChanged(Object, Object)} при измненении выюбеления этим методом.
   *
   * @param aItem &lt;E&gt; - текущий выбранный элемент или null для отказа от выбора элементов
   */
  void setSelectedItem( E aItem );

}
