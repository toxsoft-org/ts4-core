package org.toxsoft.tsgui.mws.services.currentity;

import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Базовый инетфейс поддержи понятия "текущее что-то" в приложении.
 * <p>
 * Использование этой службы:
 * <ul>
 * <li>Унаследовать интерфейс ICurrentXxxService от этого интерфейса;</li>
 * <li>Создать пустой класс, наследник {@link CurrentEntityService} и расширяющий интерфейс ICurrentXxxService;</li>
 * <li>Создать и зарегистрировать экземпляр наследника в контексте приложени, после чего это тэкземпляр доступен для
 * инъекции.</li>
 * </ul>
 *
 * @author goga
 * @param <E> - тип этого "текущего чего-то"
 */
public interface ICurrentEntityService<E> {

  /**
   * Возвращает текущий элемент.
   *
   * @return <b>E</b> - текущий элемент, может быть null
   */
  E current();

  /**
   * Изменяет текущий элемент.
   *
   * @param aCurrent <b>E</b> - новый текущий элемент или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  void setCurrent( E aCurrent );

  /**
   * Вызывает извещение {@link ICurrentEntityChangeListener#onCurrentContentChanged(Object)}.
   */
  void informOnContentChange();

  /**
   * Добавялет слушатель.
   *
   * @param aListener {@link ICurrentEntityChangeListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void addCurrentEntityChangeListener( ICurrentEntityChangeListener<E> aListener );

  /**
   * Удаляет слушатель.
   *
   * @param aListener {@link ICurrentEntityChangeListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void removeCurrentEntityChangeListener( ICurrentEntityChangeListener<E> aListener );

}
