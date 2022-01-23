package org.toxsoft.unit.txtproj.core.bound;

import java.io.File;

import org.toxsoft.tslib.av.utils.IParameterizedEdit;
import org.toxsoft.tslib.bricks.strio.StrioRtException;
import org.toxsoft.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.tslib.utils.ICloseable;
import org.toxsoft.tslib.utils.errors.*;
import org.toxsoft.unit.txtproj.core.ITsProject;

/**
 * "Держатель" связки проекта {@link #project()} с файлом {@link #getFile()}.
 *
 * @author hazard157
 */
public interface ITsProjectFileBound
    extends IParameterizedEdit, ICloseable {

  /**
   * Возвращает проект, который связывается с файлом.
   *
   * @return {@link ITsProject} - проект
   */
  ITsProject project();

  /**
   * Возвращает связанный с проектом файл или <code>null</code>, если привязка не установлена.
   *
   * @return {@link File} - файл проекта или <code>null</code>
   */
  File getFile();

  /**
   * Считывает содержимого проекта из файла и устанавливает привязку.
   * <p>
   * Сбрасывает признак {@link #isAltered()}.
   *
   * @param aFile {@link File} - открываемый файл
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException из {@link ITsProjectFileBoundListener#beforeOpen(ITsProjectFileBound, File)}
   * @throws TsIoRtException ошибка доступа/чтения файла
   * @throws StrioRtException неверный формат файла
   */
  void open( File aFile );

  /**
   * Сохраняет проект в файл {@link #getFile()}.
   * <p>
   * В соответствии с параметрами конфигурации создает файл резервной копии. Сбрасывает признак изменений
   * {@link #isAltered()} в <code>false</code>. В случае любой ошибки записи в файл, признак изменений не сбрасывается.
   *
   * @throws TsIllegalStateRtException привязка к файлу не установлена, то есть {@link #hasFileBound()}
   * @throws TsValidationFailedRtException не из {@link ITsProjectFileBoundListener#beforeSave(ITsProjectFileBound)}
   * @throws TsIoRtException ошибка доступа/записи в файл
   */
  void save();

  /**
   * Сохраняет проект в указанный файл и устанавливает новую привязку.
   *
   * @param aFile {@link File} - файл для сохранения проекта
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException не из {@link ITsProjectFileBoundListener#beforeSave(ITsProjectFileBound)}
   * @throws TsIoRtException ошибка доступа/записи в файл
   */
  void saveAs( File aFile );

  /**
   * Возвращает признак, что содержимое проекта изменилась и расходится с содержимым файла.
   * <p>
   * Признак устанавливается при любой правке проекта. Сбрасывается при загрузке из и сохранении в файл, а также извне,
   * с помощью метода {@link #resetAlteredState()}.
   *
   * @return boolean - признак, что содержимое проекта расходится с файлом
   */
  boolean isAltered();

  /**
   * Сбрасывает признак {@link #isAltered()} в <code>false</code> .
   */
  void resetAlteredState();

  /**
   * Определяет, установлено ли соответствие между проктом {@link #project()} и файлом {@link #getFile()}.
   * <p>
   * Изначально, при создании "держателя" привязка <b>не</b> установлена. Привязка устанавливается методами
   * {@link #open(File)} и {@link #saveAs(File)}. Привязку пожно сбросить методом {@link #resetFileBound()}.
   * <p>
   * Метод равнозначен проверке {@link #getFile()} == <code>null</code>.
   *
   * @return boolean - признак, установленной привязки проекта к файлу
   */
  boolean hasFileBound();

  /**
   * Сбрасывает привязку {@link #hasFileBound()} в <code>false</code> .
   * <p>
   * После этого метода {@link #getFile()} = <code>null</code>.
   */
  void resetFileBound();

  /**
   * Осуществляет действие "создать новый проект" путем очистки проекта методом {@link ITsProject#clear()}.
   * <p>
   * Надо понимать, что Java-объект проекта {@link #project()} не пересоздается, а очищается содержимое документа.
   * <p>
   * <b>Внимание:</b> предыдущее содержимое проекта теряется безвозвратно!
   */
  void newContent();

  /**
   * Добавляет слушатель.
   * <p>
   * Если слушатель уже зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsProjectFileBoundListener} - слушатель
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void addTsProjectFileBoundListener( ITsProjectFileBoundListener aListener );

  /**
   * Удаляет слушатель.
   * <p>
   * Если слушатель не зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsProjectFileBoundListener} - слушатель
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void removeTsProjectFileBoundListener( ITsProjectFileBoundListener aListener );

}
