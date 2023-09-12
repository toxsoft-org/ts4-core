package org.toxsoft.core.tsgui.graphics.image;

import java.io.*;

import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages thumbnails of the image files.
 * <p>
 * TODO concepts, usage, need GraphicsMagic
 * <p>
 * TODO warning: old entries are disposed or dispose manually
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
 * href=http://www.graphicsmagick.org>graphicsmagick</a>.
 * <p>
 * TODO maybe use common (application level) cache for all instances of image manager?
 *
 * @author hazard157
 */
public interface ITsThumbManager {

  /**
   * Returns the directory where the thumbnail files are located.
   *
   * @return {@link File} - thumbnail files location directory
   */
  File getThumbsRoot();

  /**
   * Returns the maximum allowed number of the cached thumbnail images per thumb size.
   * <p>
   * Caching thumbs after this limit is reached will cause old images to be disposed.
   * <p>
   * TODO change to set individual limits for thumb size
   *
   * @return int - maximum number of the thumbnail cached images per thumb size
   */
  int getMaxThumbsInMemory();

  /**
   * Sets the manager configuration parameters.
   * <p>
   * Reducing number of cached thumbnails below the number of used cache entries disposes the old images.
   *
   * @param aThumbsRootDir {@link File} - the new root directory
   * @param aMaxThumbsInMemory int - maximum number of cached thumbnail images
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setup( File aThumbsRootDir, int aMaxThumbsInMemory );

  /**
   * Returns the the cached thumbnail image.
   * <p>
   * Method generates new thumbnail file of the specified size using GraphicsMagic external utility program, then loads
   * and caches the thumb image. If thumbnail image can't be created for any reason (invalid image file, no utility
   * program in systems, invalid thumbs cache directory, etc) method returns <code>null</code>.
   *
   * @param aImageFile {@link File} - the image file
   * @param aThumbSize {@link EThumbSize} - requested thumbnail size
   * @return {@link TsImage} - thumbnail image or <code>null</code> if image can't be created for any reason
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  TsImage findThumb( File aImageFile, EThumbSize aThumbSize );

  /**
   * Determines the path to the thumbnail file without creating it.
   *
   * @param aImageFile {@link File} - the image file
   * @param aThumbSize {@link EThumbSize} - requested thumbnail size
   * @return {@link File} - path to the generated thumbnail image file
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  File getThumbFile( File aImageFile, EThumbSize aThumbSize );

  /**
   * Ensures that the thumbnail image file exists.
   * <p>
   * If the thumbnail image file does not exist, creates one.
   *
   * @param aImageFile {@link File} - the image file
   * @param aThumbSize {@link EThumbSize} - requested thumbnail size
   * @param aForceCreate boolean - <code>true</code> = recreate the thumbnail file, regardless of its existence
   * @return boolean - an indication that the icon file was created or existed previously
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean ensureThumb( File aImageFile, EThumbSize aThumbSize, boolean aForceCreate );

  /**
   * Determines whether the thumbnail image is in cache.
   * <p>
   * If the image is in the cache, then the {@link #findThumb(File, EThumbSize)} method will return the result
   * immediately, otherwise it will run long caching process.
   *
   * @param aImageFile {@link File} - the image file
   * @param aThumbSize {@link EThumbSize} - requested thumbnail size
   * @return boolean - an indication that the image is in the cache
   */
  boolean isThumbCached( File aImageFile, EThumbSize aThumbSize );

  /**
   * Determines whether a thumbnail file of the specified image exists.
   *
   * @param aImageFile {@link File} - the image file
   * @param aThumbSize {@link EThumbSize} - requested thumbnail size
   * @return boolean - an indication that the thumbnail image file exists
   */
  boolean isThumbFile( File aImageFile, EThumbSize aThumbSize );

  /**
   * Checks existing thumbnail files and cached images and refreshes them.
   * <p>
   * For directories all files in directory will be processed non-recursively. If the specified file object does not
   * exist, it is treated as if it had just been removed from the file system.
   * <p>
   * The method clears the cached images from memory and deletes thumb files so next thumbnail request will recreate the
   * thumbnail file and reload the image.
   *
   * @param aFileOrDir {@link File} - file or directory of images whose cache will be updated
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void refreshCache( File aFileOrDir );

  /**
   * Clears the cache disposing all cached images.
   * <p>
   * Warning: all previously loaded thumbnail images became unusable.
   */
  void clearCache();

}
