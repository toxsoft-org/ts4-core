package org.toxsoft.core.tsgui.mws.services.currentity;

/**
 * Слушатель изменения текущего элемента.
 *
 * @author hazard157
 * @param <E> - тип этого "текущего чего-то"
 */
public interface ICurrentEntityChangeListener<E> {

  /**
   * Вызвается при изменении текущего элемента.
   *
   * @param aCurrent <b>E</b> - текущий элемент, может быть null
   */
  void onCurrentEntityChanged( E aCurrent );

  /**
   * Вызывается, когда изменилсь содержимое текущего элемента (без изменения ссылки на него).
   * <p>
   * Этот метод вызывается при извещении пользователем {@link ICurrentEntityService#informOnContentChange()}.
   *
   * @param aCurrent <b>E</b> - текущий элемент, может быть null
   */
  default void onCurrentContentChanged( E aCurrent ) {
    // nop
  }

}
