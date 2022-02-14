package org.toxsoft.core.txtproj.lib;

import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeEventer;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.core.tslib.bricks.keeper.IKeepableEntity;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.coll.basis.ITsClearable;

/**
 * Слушатель изменения содержимого проекта {@link ITsProject} или модулей {@link IProjDataUnit}.
 * <p>
 * Обратите внимание, что это сообщение <b>дополняет</b> ссообщения {@link IGenericChangeEventer}, которые
 * генерируются при <b>любом</b> изменении содержимого модулей. А это извещение генерируется <b>дополнительно</b> к
 * {@link IGenericChangeListener#onGenericChangeEvent(Object)}, по действиям над проектом в целом: "Загрузить
 * проект", "Создать новый проект" (что очищает содержимое проекта) и "Перед/после сохранением проекта".
 *
 * @author hazard157
 */
public interface ITsProjectContentChangeListener {

  /**
   * Вызывается перед началом процесса сохранения содержимого.
   * <p>
   * Слушатели могут подготовить содержимое для сохранения в файл вызывая методы модулей проекта, то есть штатные методы
   * редатирования наследников {@link IProjDataUnit}.
   *
   * @param aSource {@link ITsProject} - источник сообщения
   */
  default void beforeSave( ITsProject aSource ) {
    // nop
  }

  /**
   * Вызывается после успешного сохранения.
   *
   * @param aSource {@link ITsProject} - источник сообщения
   */
  default void afterSave( ITsProject aSource ) {
    // nop
  }

  /**
   * Вызывается при любом изменений содержимого проекта или модуля проекта.
   *
   * @param aSource {@link ITsProject} - источник сообщения
   * @param aCleared boolean - признак очистки содержимого <br>
   *          <b>true</b> - содержимое было очищено (сброшено) методом {@link ITsClearable#clear()};<br>
   *          <b>false</b> - содержимое было загружено с внешнего носителя методом
   *          {@link IKeepableEntity#read(IStrioReader)}.
   */
  default void onContentChanged( ITsProject aSource, boolean aCleared ) {
    // nop
  }

}
