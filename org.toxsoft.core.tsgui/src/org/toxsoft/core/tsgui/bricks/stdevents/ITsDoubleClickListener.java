package org.toxsoft.core.tsgui.bricks.stdevents;

/**
 * Слушатель двойного щелчка мышью в какой-либо визуальной компоненте, содержащей однотипные элементы.
 *
 * @author goga
 * @param <E> - конкретный тип элементов
 */
public interface ITsDoubleClickListener<E> {

  /**
   * Вызывается при двоном щелчке в источнике.
   *
   * @param aSource Object - источни события (приводится к конкретному типу источника)
   * @param aSelectedItem E - новый выбранный элемент, может быть null
   */
  void onTsDoubleClick( Object aSource, E aSelectedItem );

}
