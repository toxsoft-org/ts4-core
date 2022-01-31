package org.toxsoft.core.unit.txtproj.lib;

import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Смешиваемый интерфейс сущности, генерирующий сообщение
 * {@link ITsProjectContentChangeListener#onContentChanged(ITsProject, boolean)}.
 *
 * @author hazard157
 */
public interface ITsProjectContentChangeProducer {

  /**
   * Добавляет слушатель изменения.
   * <p>
   * Если слушатель уже зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsProjectContentChangeListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  void addProjectContentChangeListener( ITsProjectContentChangeListener aListener );

  /**
   * Удаляет слушатель изменения.
   * <p>
   * Если слушатель не зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsProjectContentChangeListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  void removeProjectContentChangeListener( ITsProjectContentChangeListener aListener );

}
