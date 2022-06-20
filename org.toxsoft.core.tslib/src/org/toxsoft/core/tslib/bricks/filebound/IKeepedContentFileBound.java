package org.toxsoft.core.tslib.bricks.filebound;

import java.io.*;

import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The "holder" of the link of the {@link #content()} with the file {@link #file()}.
 * <p>
 * Designed to make easy GUI commands "New", "Open...", "Save", "Save as..." implementation. All this actions may be
 * performed by corresponding methods.
 * <p>
 * Optionally backup file creation is supported.
 * <p>
 * Note: class generates {@link IGenericChangeListener#onGenericChangeEvent(Object)} event when bound state changes not
 * when content changes. Bond state change includes changes in vaues of {@link #file()} or {@link #isAltered()}.
 *
 * @author hazard157
 */
public interface IKeepedContentFileBound
    extends IParameterizedEdit, ICloseable, IGenericChangeEventCapable {

  /**
   * The content in the memory linked with the {@link #file()}.
   *
   * @return {@link IKeepedContent} - the content of the file
   */
  IKeepedContent content();

  /**
   * Returns the file linked with the content in the memory {@link #content()}.
   *
   * @return {@link File} - файл проекта или <code>null</code>
   */
  File file();

  /**
   * Clears (resets) content to an empty content defaults.
   * <p>
   * This is "New" commmand implementation.
   * <p>
   * Unbounds content from the file, sets {@link #file()} to <code>null</code>. Resets {@link #isAltered()} and
   * {@link #hasFileBound()}.
   * <p>
   * <b>Warning:</b> previous content (if not saved) will be lost!
   *
   * @throws TsValidationFailedRtException check failed in {@link IKeepedContentFileBoundValidator}
   */
  void clear();

  /**
   * Read content from the file and updates {@link #file()}.
   * <p>
   * This is "Open" command implementation.
   *
   * @param aFile {@link File} - the file to read from
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException check failed in {@link IKeepedContentFileBoundValidator}
   * @throws TsIoRtException file I/O error
   * @throws StrioRtException unexpected file format
   */
  void open( File aFile );

  /**
   * Saves content to the file {@link #file()}.
   *
   * @throws TsIllegalStateRtException content is not bounded to the file, {@link #hasFileBound()} = <code>false</code>
   * @throws TsValidationFailedRtException check failed in {@link IKeepedContentFileBoundValidator}
   * @throws TsIoRtException ошибка доступа/записи в файл
   */
  void save();

  /**
   * Saves content to the new file and updates {@link #file()}.
   *
   * @param aFile {@link File} - файл для сохранения проекта
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException check failed in {@link IKeepedContentFileBoundValidator}
   * @throws TsIoRtException ошибка доступа/записи в файл
   */
  void saveAs( File aFile );

  // TODO TRANSLATE

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
   * Detemines if there is a bound between {@link #content()} and {@link #file()}.
   * <p>
   * Изначально, при создании "держателя" привязка <b>не</b> установлена. Привязка устанавливается методами
   * {@link #open(File)} и {@link #saveAs(File)}. Привязку пожно сбросить методом {@link #resetFileBound()}.
   * <p>
   * Метод равнозначен проверке {@link #file()} == <code>null</code>.
   *
   * @return boolean - признак, установленной привязки проекта к файлу
   */
  boolean hasFileBound();

  /**
   * Unbounds content from the file.
   * <p>
   * After this method call {@link #hasFileBound()} в <code>false</code> and {@link #file()} = <code>null</code>.
   */
  void resetFileBound();

  /**
   * Returns the service validator.
   *
   * @return {@link ITsValidationSupport}&lt;{@link IKeepedContentFileBoundValidator}&gt; - the service validator
   */
  ITsValidationSupport<IKeepedContentFileBoundValidator> svs();

}
