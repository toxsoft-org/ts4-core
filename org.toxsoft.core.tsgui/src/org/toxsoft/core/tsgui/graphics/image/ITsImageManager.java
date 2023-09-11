package org.toxsoft.core.tsgui.graphics.image;

import java.io.*;

import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages images and files and caches.
 * <p>
 * FIXME update comments
 * <p>
 * О терминологии: "значок", "картинка", "изображение" - смотри комментарии к {@link ITsIconManager}. Дополнительно
 * менеджер картинок вводит поянтие "<b><i>миниатюра</i></b>" (thumbnail). Миниатюра - уменьшенное изображение
 * оргинальной картинки и имеет размеры из ряда {@link EThumbSize}.
 * <p>
 * Изображения значков представлены в виде виде {@link TsImage}, что позволяет работать анимированными изображениями.
 * <p>
 * Менеджер имеет слудующую функциональность:
 * <ul>
 * <li>управление ресурсами - созданные с помощью менеджера изображения уничтожаются самим менеджером;</li>
 * <li>кеширование - изображения кешируются в памяти, и при необходимости удаляются из памяти. Из-за келирования,
 * <b>никогда не запоминайте ссылки на {@link TsImage}</b>, созданные с кешированием.</li>
 * <li>загрузка - можно загрузить изображения методами loadXxx()</li>
 * <li>файлы миниатюр - TODO ???</li>
 * <li></li>
 * </ul>
 * <p>
 * Ссылка на экземпляр этого класса должен находится в контексте приложения.
 * <p>
 * Внимание: для работы функционала создания файлового кеша миниатюр в системе должна быть установлена программа <a
 * href=http://www.graphicsmagick.org>graphicsmagic</a>.
 * <p>
 * TODO maybe use common (application level) cache for all instances of image manager?
 *
 * @author hazard157
 */
public interface ITsImageManager {

  /**
   * Задает параметры кеширования.
   * <p>
   * Изменение параметров приводит к сбросу кеша.
   * <p>
   * Если задать слишком большое или маленькое количество элементов кеша в памяти, они будут сброшены во внутренные
   * ограничители.
   *
   * @param aMaxImagesInMemory int - максимальное количество изображений в памяти
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void setup( int aMaxImagesInMemory );

  /**
   * Определяет, есть ли изображение в кеше.
   *
   * @param aImageFile {@link File} - файл изображения
   * @return boolean - признак находждения изображения в кеше
   * @throws TsNullArgumentRtException аргумент = null
   */
  boolean isCached( File aImageFile );

  /**
   * Возвращает кешированное изображение файла.
   *
   * @param aImageFile {@link File} - файл изображения
   * @return {@link TsImage} - кешированное изображение или null, если нет такого файла или его нельзя загрузить
   * @throws TsNullArgumentRtException аргумент = null
   */
  TsImage findImage( File aImageFile );

  /**
   * Возвращает кешированное изображение файла.
   *
   * @param aImageFile {@link File} - файл изображения
   * @return {@link TsImage} - кешированное изображение
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsNotAllEnumsUsedRtException нет такого файла или его нельзя загрузить
   */
  TsImage getImage( File aImageFile );

  /**
   * Обновляет кеш значков и изображений для указанного файла или директория.
   * <p>
   * Если указанный файловый объект не существует, он отрабатывается как только что удаленный из фйловой системы.
   *
   * @param aFileOrDir {@link File} - файл или директория изображений, чей кеш будет обновлен
   * @throws TsNullArgumentRtException аргумент = null
   */
  void refreshCache( File aFileOrDir );

  /**
   * Сбрасывает кеш.
   * <p>
   * TODO Что c дисковым кешем?
   */
  void clearCache();

}
