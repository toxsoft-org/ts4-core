package org.toxsoft.core.tsgui.graphics.image;

import java.io.*;

import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
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
 * href=http://www.graphicsmagick.org>graphicsmagick</a>.
 * <p>
 * TODO maybe use common (application level) cache for all instances of image manager?
 *
 * @author hazard157
 */
public interface ITsImageManager {

  // ------------------------------------------------------------------------------------
  // API with image descriptors
  //

  /**
   * Determines if image is already created and cached.
   *
   * @param aDescriptor {@link TsImageDescriptor} - the image descriptor
   * @return boolean - <code>true</code> if image is cached
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean isCached( TsImageDescriptor aDescriptor );

  /**
   * Returns the cached image.
   * <p>
   * If the image source does not exists then method returns the default "unknown" image.
   *
   * @param aDescriptor {@link TsImageDescriptor} - the image descriptor
   * @return {@link TsImage} - cached image or the unknown image
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException unknown kind of image source
   * @throws TsRuntimeException various exceptions specific to the image source kind
   */
  TsImage getImage( TsImageDescriptor aDescriptor );

  // ------------------------------------------------------------------------------------
  // API with File
  //

  /**
   * Determines if image is already created and cached.
   *
   * @param aImageFile {@link File} - the image file
   * @return boolean - <code>true</code> if image is cached
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean isCached( File aImageFile );

  /**
   * Returns the cached image.
   *
   * @param aImageFile {@link File} - the image file
   * @return {@link TsImage} - cached image or <code>null</code> if file can't be loaded or cached
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  TsImage findImage( File aImageFile );

  /**
   * Returns the cached image.
   *
   * @param aImageFile {@link File} - the image file
   * @return {@link TsImage} - cached image
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException file can't be loaded or cached
   */
  TsImage getImage( File aImageFile );

  /**
   * Updates the image cache for the specified file or directory.
   * <p>
   * For directories all files in directory will be processed non-recursively.
   * <p>
   * If the specified file object does not exist, it is treated as if it had just been removed from the file system.
   *
   * @param aFileOrDir {@link File} - file or directory of images whose cache will be updated
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void refreshCache( File aFileOrDir );

  // ------------------------------------------------------------------------------------
  // save images
  //

  /**
   * Saves the image to the file.
   * <p>
   * Existing file will be overwritten. Animated images {@link TsImage#isAnimated()} are saved as GIF files. Still
   * images are saved either as PNG or JPG format, depending on argument <code>aLoseless</code> value. Note that only
   * PNG format supports transparency.
   * <p>
   * The extension will be appended to the specified file name.
   *
   * @param aImage {@link TsImage} - the image to save
   * @param aLoseless boolean - <code>true</code> for PNG format
   * @param aFilePath String - the file path (without extension) to save the image to
   * @return {@link File} - absolute path to the created file (including an extension)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException image is not valid or is disposed
   * @throws TsIllegalArgumentRtException file path is a blank string
   * @throws TsIoRtException error while writing file
   */
  File saveToFile( TsImage aImage, boolean aLoseless, String aFilePath );

  // ------------------------------------------------------------------------------------
  // common API
  //

  /**
   * Sets the manager configuration.
   * <p>
   * Reducing the maximum number of images in memory will cause "old" images to be disposed.
   * <p>
   * The argument value is "fit" to the reasonable range.
   *
   * @param aMaxImagesInMemory int - maximum number of images in the cache
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setup( int aMaxImagesInMemory );

  /**
   * Resets (clears) the cache disposing all previously loaded images.
   */
  void clearCache();

  /**
   * Creates the new instance of the small image, to be used if any requested image can not be created.
   * <p>
   * Unknown image is a red square on the white background. Image is of specified size and red square edges will be half
   * of the image size. Too big or too small image size will be fit in the reasonable range.
   * <p>
   * Call is responsible for returned image disposal.
   *
   * @param aImageSize int - size in pixels of the creates square image
   * @return {@link TsImage} - new instance of the "unknown" image
   */
  TsImage createUnknownImage( int aImageSize );

}
