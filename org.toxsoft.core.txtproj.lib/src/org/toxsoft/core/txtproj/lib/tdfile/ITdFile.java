package org.toxsoft.core.txtproj.lib.tdfile;

import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeEventer;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.core.tslib.bricks.keeper.IKeepableEntity;
import org.toxsoft.core.tslib.coll.basis.ITsClearable;
import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.primtypes.IStringMap;
import org.toxsoft.core.tslib.utils.errors.TsItemAlreadyExistsRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Файл с текстовыми данными (text data file).
 * <p>
 * Содержит набор разделов {@link TdfSection} так, чтобы ключевые слова каждого раздела уникальны внутри файла.
 * <p>
 * Хоть интерфейс и имеет в названии слово "файл", напрямую реализация не привязана именно к файлу, просто
 * предполагается, что содержимое будет записываться в текстовый файл и читаться из него.
 * <p>
 * Обратите внимание, что если разделы созданы путем чтения из файла, и не были отредактированы, то они записываются без
 * изменений. Это позволяет работать с файлом программе, которая знает только о части разделов, игнорируя неизвестные
 * разделы.
 * <p>
 * Сообщение об изменениях {@link IGenericChangeListener#onGenericChangeEvent(Object)} генерируется как при изменении
 * состава (карты) разделов, так и при изменении содержимого раздела.
 *
 * @author hazard157
 */
public interface ITdFile
    extends IKeepableEntity, ITsClearable {

  /**
   * Возвращает все разделы в файле.
   *
   * @return {@link IStringMap}&lt;{@link TdfSection}&gt; - карта "ключевое слово" - "раздел"
   */
  IStringMap<TdfSection> sections();

  /**
   * Добавляет раздел в карту.
   *
   * @param aSection {@link TdfSection} - добавляемый раздел
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemAlreadyExistsRtException раздел уже есть
   */
  void add( TdfSection aSection );

  /**
   * Заменяет существующие разделы новыми.
   *
   * @param aSections {@link ITsCollection}&lt;{@link TdfSection}&gt; - новый набор разделов
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void setAll( ITsCollection<TdfSection> aSections );

  /**
   * Удаляет раздел по ключевому слову.
   *
   * @param aKeyword String - ключевое слово
   */
  void remove( String aKeyword );

  /**
   * Returns the change eventer.
   *
   * @return {@link IGenericChangeEventer} - the change eventer
   */
  IGenericChangeEventer eventer();

}
