package org.toxsoft.unit.txtproj.core;

import org.toxsoft.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.tslib.bricks.events.change.IGenericChangeEventer;
import org.toxsoft.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.tslib.bricks.keeper.IKeepableEntity;
import org.toxsoft.tslib.bricks.validator.ITsValidator;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.coll.basis.ITsClearable;
import org.toxsoft.tslib.coll.primtypes.IStringMap;
import org.toxsoft.tslib.utils.errors.*;
import org.toxsoft.unit.txtproj.core.bound.ITsProjectFileBound;
import org.toxsoft.unit.txtproj.core.impl.TsProjectFileFormatInfo;
import org.toxsoft.unit.txtproj.core.tdfile.ITdFile;

// TODO TRANSLATE

/**
 * Файл проекта для различных приложений.
 * <p>
 * Подразумеватеся, что приложение работает с одним текстовым файлом проекта. Проект состоит из разных компонент
 * {@link IProjDataUnit}, каждый со своим именем-идентификатором. Назначение проекта {@link ITsProject} - собрать вместе
 * компоненты проекта для сохранения / загрузки в виде текстовго файла с использованием возможностей
 * {@link IKeepableEntity} и {@link ITsProjectFileBound}.
 * <p>
 * Для хранения проекта используются возможности {@link ITdFile}, то есть, если в файле есть конмоненты, которые
 * неизвестны проекту (их нет среди {@link #units()}), они не загружаютися при чтении, но сохраняются при сохранении
 * проекта.
 * <p>
 * Следует отметить, что регистрировать (и дерегистрировать) компоненты можно по ходу работы.
 *
 * @author hazard157
 */
public interface ITsProject
    extends //
    IKeepableEntity, // для сохранения/загрузки в текстовом формате
    ITsProjectContentChangeProducer, // генерирует сообщения, специфичные для проекта
    ITsClearable // поддержка понятия "создать новый проект"
{

  // FIXME определится, как делать проверку загружаемого проекта на соответствие описанию формата
  /**
   * Варианты:
   * <ul>
   * <li>1. Ввести TsProjectFileFormatInfo.NONE. Если задан не-NONE, то ITsProject.fileFormatValidator() проверяет на
   * однозначное соответсиве формата файла заданному формату. Если задан NONE, то грузится ЛЮБОЙ файл проекта, у
   * которого вресия формата подходящая (приложение и версия приложения - пофиг).</li>
   * <li>2.</li>
   * <li></li>
   * </ul>
   */

  /**
   * Возвращает информацию о формате файла, для работы с которым предназначен этот проект.
   * <p>
   * Эта информация не меняется с момента создания проекта. Валидатор файла проекта {@link #fileFormatValidator()} по
   * умолчанию проверяет загружаемый файл на строгое соответствие этому формату.
   *
   * @return {@link TsProjectFileFormatInfo} - информация о формате файла проекта
   */
  TsProjectFileFormatInfo initialFormatInfo();

  /**
   * Возвращает информацию о текущем формате файла проекта.
   * <p>
   * Эта информация может меняться при каждой успешной загрузке файла проекта.
   * <p>
   * При сохранении файла используется этот формат, а не {@link #initialFormatInfo()}.
   *
   * @return {@link TsProjectFileFormatInfo} - информация о формате файла проекта
   */
  TsProjectFileFormatInfo formatInfo();

  /**
   * Возвращает произвольные параметры проекта.
   * <p>
   * Параметры не используются самим проектом - они полностью зависят от приложения.
   *
   * @return {@link IOptionSetEdit} - редактируемые параметры проекта
   */
  IOptionSetEdit params();

  /**
   * Возвращает зарегистрированные компоненты.
   *
   * @return {@link IStringMap}&lt;{@link IProjDataUnit}&gt; - карта "идентификатор" - "компонента"
   */
  IStringMap<IProjDataUnit> units();

  /**
   * Регистрирует компоненту проекта.
   * <p>
   * Обратите внимание, что регистрация (и дерегистривация) компоненты не приводит к извещению
   * {@link IGenericChangeListener#onGenericChangeEvent(Object)}. Извещение генерируется только при изменении
   * <b>содержимого</b> проекта (то есть, при извещений об изменений хотя бы от одного из компонент проекта).
   * <p>
   * Если аргумент aReadContent = <code>true</code>, то после регистрации проект попытается занести ранее считанное
   * содержимое соответствующего раздела (если такой раздел был) файла в компоненту. Результат (успех или сообщение о
   * возникшем исключении) возвращается.
   *
   * @param aId String - идентификатор (ИД-путь) компоненты
   * @param aUnit {@link IProjDataUnit} - регистроируемая компонента
   * @param aReadContent boolean - признак занесения ранее считанного из файла содержимого в компоненту
   * @return {@link ValidationResult} - результат чтения содержимого из файла
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   * @throws TsItemAlreadyExistsRtException компонента с таким идентификатором уже зарегистрирована
   */
  ValidationResult registerUnit( String aId, IProjDataUnit aUnit, boolean aReadContent );

  /**
   * Удалет компоненту из списка зарегистрированных.
   * <p>
   * Если компонента с таким идентификатором не заругистрирована, метод ничего не делает.
   * <p>
   * Обратите внимание, что регистрация (и дерегистривация) компоненты не приводит к извежению
   * {@link IGenericChangeListener#onGenericChangeEvent(Object)}. Извещение генер ируется только при изменении
   * <b>содержимого</b> проекта (то есть, при извещений об изменений хотя бы от одного из компонент проекта).
   * <p>
   * Обратите внимание, что удаление компненты не удалет соответствующее содержимое из файла. То есть, если содержимое
   * компоненты с таким идентификатором было хоть раз считано/записно в файл, то оно останется и после дерегистрации
   * компоненты.
   *
   * @param aId String - идентификатор удаляемой компоненты
   * @throws TsNullArgumentRtException аргумент = null
   */
  void unregisterUnit( String aId );

  /**
   * Возвращает валидатор, используемый для определения допустимости загрузки файла проекта.
   * <p>
   * Валидатор файла проекта {@link #fileFormatValidator()} по умолчанию проверяет загружаемый файл на строгое
   * соответствие этому формату. Возможно заменить валидатор с тем, чтобы иметь возможность загрузить более новые
   * форматы файлов.
   *
   * @return {@link ITsValidator} - валидатор формата файла проекта
   */
  ITsValidator<TsProjectFileFormatInfo> fileFormatValidator();

  /**
   * Задает валидатор, используемый для определения допустимости загрузки файла проекта.
   *
   * @param aValidator {@link ITsValidator} - валидатор формата файла проекта или <code>null</code> - по умолчанию
   */
  void setFileFormatValidator( ITsValidator<TsProjectFileFormatInfo> aValidator );

  /**
   * Returns the change eventer.
   *
   * @return {@link IGenericChangeEventer} - the change eventer
   */
  IGenericChangeEventer genericChangeEventer();

}
