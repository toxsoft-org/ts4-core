package org.toxsoft.core.tsgui.bricks.stdevents;

/**
 * Слушатель смены выделенного элемента в какой-либо визуальной компоненте.
 *
 * @author hazard157
 * @param <E> - конкретный тип элементов
 */
public interface ITsSelectionChangeListener<E> {

  /**
   * Вызывается при изменения текущего выбранного элемента в источнике.
   *
   * @param aSource Object - источни события, приводится к конкретному типу источника
   * @param aSelectedItem E - новый выбранный элемент, может быть null
   */
  void onTsSelectionChanged( Object aSource, E aSelectedItem );

}
