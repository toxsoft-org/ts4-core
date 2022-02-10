package org.toxsoft.core.tsgui.mws.services.currentity;

import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

// TODO TRANSLATE

/**
 * Introduces concept of something currently selected in application.
 * <p>
 * <p>
 * Использование этой службы:
 * <ul>
 * <li>Унаследовать интерфейс ICurrentXxxService от этого интерфейса;</li>
 * <li>Создать пустой класс, наследник {@link CurrentEntityService} и расширяющий интерфейс ICurrentXxxService;</li>
 * <li>Создать и зарегистрировать экземпляр наследника в контексте приложени, после чего это тэкземпляр доступен для
 * инъекции.</li>
 * </ul>
 *
 * @author hazard157
 * @param <E> - type of "something"
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
