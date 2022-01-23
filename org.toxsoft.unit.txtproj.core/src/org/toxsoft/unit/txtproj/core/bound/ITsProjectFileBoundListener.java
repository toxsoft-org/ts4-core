package org.toxsoft.unit.txtproj.core.bound;

import java.io.File;

import org.toxsoft.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.tslib.bricks.validator.EValidationResultType;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.unit.txtproj.core.IProjDataUnit;
import org.toxsoft.unit.txtproj.core.ITsProject;

/**
 * Извещения от {@link ITsProjectFileBound} о работе с привязкой проекта к файлу.
 * <p>
 * Обратите внимание, что {@link ITsProjectFileBound} не занимается извещением об изменении <b>содержимого проекта</b>,
 * только о работе связки "проект" <-> "файл". Конечно, некоторые действия {@link ITsProjectFileBound} (например,
 * открытие файла {@link ITsProjectFileBound#open(File)}) приводят к изменению содержимого проекта. Но если нужно
 * отслеживать изменения содержимого, то следует обратиться к извещениям {@link IGenericChangeListener} проекта
 * {@link ITsProject} или его составных компнент {@link IProjDataUnit} проекта. Ключевой момент заключается в том, что
 * большинство изменений происходит из-за редактирования пользователем компонент проекта, что находится вне рамок
 * комптенции {@link ITsProjectFileBound}.
 *
 * @author hazard157
 */
public interface ITsProjectFileBoundListener {

  /**
   * Вызывается, когда изменяется признак несохраненных правок {@link ITsProjectFileBound#isAltered()}.
   * <p>
   * Пример практического использование метода - обновить символ звездочки (*) в заголовке окна, означающее наличие
   * изменений в проекте, которые не сохранены в файл.
   *
   * @param aSource {@link ITsProjectFileBound} - источник извещения
   */
  void onAlteredStateChanged( ITsProjectFileBound aSource );

  /**
   * Вызывается при измененни привязанности проекта к файлу, то есть, когда менятся
   * {@link ITsProjectFileBound#getFile()}.
   * <p>
   * Метод вызывается и тогда, когда сменился файл (методами {@link ITsProjectFileBound#open(File)},
   * {@link ITsProjectFileBound#saveAs(File)}), а также при отмене привязки проекта к файлу методами
   * {@link ITsProjectFileBound#newContent()} и {@link ITsProjectFileBound#resetFileBound()}.
   * <p>
   * Пример практического использование метода - обновить имя открытого файла {@link ITsProjectFileBound#getFile()} в
   * заголовке окна, и если файл не определен, то есть {@link ITsProjectFileBound#hasFileBound()} = <code>false</code>,
   * писать что-то вроде "-- новый проект --".
   *
   * @param aSource {@link ITsProjectFileBound} - источник извещения
   */
  void onFileBindingChanged( ITsProjectFileBound aSource );

  /**
   * Извещение о намерении открыть файл (загрузить содержимое файла в проект).
   * <p>
   * Слушатель может запретить открытие файла вернув результат типа {@link EValidationResultType#ERROR}. Например,
   * проверив что открываемый файл имеет неверный формат.
   *
   * @param aSource {@link ITsProjectFileBound} - источник извещения
   * @param aFile {@link File} - открываемый файл
   * @return {@link ValidationResult} - результат проверки допустимости намерения
   */
  default ValidationResult beforeOpen( ITsProjectFileBound aSource, File aFile ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Вызывается после успешного открытия файла и загрузки содержимого в проект.
   *
   * @param aSource {@link ITsProjectFileBound} - источник извещения
   */
  default void afterOpen( ITsProjectFileBound aSource ) {
    // nop
  }

  /**
   * Извещение о намерении сохранить содержимое проекта в файл {@link ITsProjectFileBound#getFile()}.
   * <p>
   * Слушатель может запретить сохранение файла вернув результат типа {@link EValidationResultType#ERROR}. Например,
   * если проект находится в невалидном состоянии.
   * <p>
   * Основное предназначение извещения - подготовить данные проекта к сохранению на диск.
   *
   * @param aSource {@link ITsProjectFileBound} - источник извещения
   * @return {@link ValidationResult} - результат проверки допустимости намерения
   */
  default ValidationResult beforeSave( ITsProjectFileBound aSource ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Вызывается после успешного сохранения проекта в файл.
   *
   * @param aSource {@link ITsProjectFileBound} - источник извещения
   */
  default void afterSave( ITsProjectFileBound aSource ) {
    // nop
  }

  /**
   * Извещение о намерении сохранить содержимое проекта в другой файл.
   * <p>
   * Слушатель может запретить сохранение файла вернув результат типа {@link EValidationResultType#ERROR}. Например,
   * если проект находится в невалидном состоянии.
   * <p>
   * Основное предназначение извещения - подготовить данные проекта к сохранению на диск.
   *
   * @param aSource {@link ITsProjectFileBound} - источник извещения
   * @param aFile {@link File} - файл для сохранения проекта
   * @return {@link ValidationResult} - результат проверки допустимости намерения
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  default ValidationResult beforeSaveAs( ITsProjectFileBound aSource, File aFile ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Вызывается после успешного сохранения проекта в другой файл.
   *
   * @param aSource {@link ITsProjectFileBound} - источник извещения
   */
  default void afterSaveAs( ITsProjectFileBound aSource ) {
    // nop
  }

  /**
   * Извещение о намерении очистить содержимое проекта (действие "новый проект").
   * <p>
   * Слушатель может запретить очистку проекта вернув результат типа {@link EValidationResultType#ERROR}. Например, если
   * проект находится в невалидном состоянии.
   *
   * @param aSource {@link ITsProjectFileBound} - источник извещения
   * @return {@link ValidationResult} - результат проверки допустимости намерения
   */
  default ValidationResult beforeClear( ITsProjectFileBound aSource ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Вызывается после успешной очистки содержимого проекта.
   * <p>
   * Внимание: этот метод генерируется в том числе, при очисте проекта напрямую, методом {@link ITsProject#clear()}.
   *
   * @param aSource {@link ITsProjectFileBound} - источник извещения
   */
  default void afterClear( ITsProjectFileBound aSource ) {
    // nop
  }

}
