package org.toxsoft.core.tsgui.utils;

import static org.toxsoft.core.tslib.utils.files.EFsObjKind.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

/**
 * Constants, related to the media files processing.
 * <P>
 * Note: file extnsions, as a file names are case-sensitive. When comparing file extension to the constants of this
 * interface, convert extension to the lower-case.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
public interface IMediaFileConstants {

  /**
   * Image file extensions (without leading dot).
   */
  String[] IMAGE_FILE_EXTENSIONS = { "jpg", "jpeg", "png", "mng", "gif", "bmp" };

  /**
   * Array {@link #IMAGE_FILE_EXTENSIONS} as a list {@link IStringList}.
   */
  IStringList IMAGE_FILE_EXT_LIST = new StringArrayList( IMAGE_FILE_EXTENSIONS );

  /**
   * Image files (files with extension {@link #IMAGE_FILE_EXT_LIST}) filter.
   */
  TsFileFilter FF_IMAGES = new TsFileFilter( EFsObjKind.FILE, IMAGE_FILE_EXT_LIST );

  /**
   * Marks corresponding extension from {@link #IMAGE_FILE_EXTENSIONS} as possible animated image.
   */
  boolean[] IS_ANIM_IMAGE_EXT = { false, false, false, true, true, false };

  /**
   * Video file extensions (without leading dot).
   */
  String[] VIDEO_FILE_EXTENSIONS = { "webm", "avi", "mp4", "mpg", "mpeg", "vob", "wmv", "dv", "ogg", "vob" };

  /**
   * Array {@link #VIDEO_FILE_EXTENSIONS} as a list {@link IStringList}.
   */
  IStringList VIDEO_FILE_EXT_LIST = new StringArrayList( VIDEO_FILE_EXTENSIONS );

  /**
   * Video files (files with extension {@link #VIDEO_FILE_EXT_LIST}) filter.
   */
  TsFileFilter FF_VIDEOS = new TsFileFilter( FILE, VIDEO_FILE_EXT_LIST );

  /**
   * Audio file extensions (without leading dot).
   */
  String[] AUDIO_FILE_EXTENSIONS = { "mp3", "wav" };

  /**
   * Array {@link #AUDIO_FILE_EXTENSIONS} as a list {@link IStringList}.
   */
  IStringList AUDIO_FILE_EXT_LIST = new StringArrayList( AUDIO_FILE_EXTENSIONS );

  /**
   * Audio files (files with extension {@link #AUDIO_FILE_EXT_LIST}) filter.
   */
  TsFileFilter FF_AUDIO = new TsFileFilter( FILE, AUDIO_FILE_EXT_LIST );

  // TODO TRANSLATE

  /**
   * Возвращает все расширения медия-файлов, перечисленные ранее.
   * <p>
   * Возвращает список из {@link #VIDEO_FILE_EXTENSIONS}, {@link #IMAGE_FILE_EXTENSIONS} и
   * {@value #AUDIO_FILE_EXT_LIST}.
   *
   * @return {@link IStringList} - всписок всех медия-файлов
   */
  static IStringList mediaFileExtList() {
    IStringListEdit ll = new StringArrayList( 32 );
    ll.addAll( VIDEO_FILE_EXTENSIONS );
    ll.addAll( IMAGE_FILE_EXTENSIONS );
    ll.addAll( AUDIO_FILE_EXTENSIONS );
    return ll;
  }

  /**
   * Массив расширений {@link #mediaFileExtList()}.
   */
  String[] MEDIA_FILE_EXTENSIONS = mediaFileExtList().toArray();

  /**
   * Перечень {@link #MEDIA_FILE_EXTENSIONS} в виде {@link IStringList}.
   */
  IStringList MEDIA_FILE_EXT_LIST = new StringArrayList( MEDIA_FILE_EXTENSIONS );

  /**
   * Фильтр для отбора файлов изображений (файлов с расширениями {@link #MEDIA_FILE_EXTENSIONS}).
   */
  TsFileFilter MEDIA_FILES_FILTER = new TsFileFilter( FILE, MEDIA_FILE_EXT_LIST );

  /**
   * Возвращает массив расширений анимированных изображений (создает "на лету" из существующих массивов).
   *
   * @return String[] - массив расширений анимированных изображений
   */
  static String[] animatedImageFileExtensions() {
    int count = 0;
    for( int i = 0; i < IS_ANIM_IMAGE_EXT.length; i++ ) {
      if( IS_ANIM_IMAGE_EXT[i] ) {
        ++count;
      }
    }
    if( count == 0 ) {
      return TsLibUtils.EMPTY_ARRAY_OF_STRINGS;
    }
    String[] arr = new String[count];
    for( int i = 0, j = 0; i < IS_ANIM_IMAGE_EXT.length; i++ ) {
      if( IS_ANIM_IMAGE_EXT[i] ) {
        arr[j++] = IMAGE_FILE_EXTENSIONS[i];
      }
    }
    return arr;
  }

  /**
   * Возвращает список расширений анимированных изображений (создает "на лету" из существующих массивов).
   *
   * @return IStringList - список расширений анимированных изображений
   */
  static IStringList animatedImageFileExtList() {
    return new StringLinkedBundleList( animatedImageFileExtensions() );
  }

  /**
   * Возвращает фильтр для отбора файлов (потенциально) анимированных изображений.
   *
   * @return {@link TsFileFilter} - фильтр файлов
   */
  static TsFileFilter animatedImageFileFilter() {
    return new TsFileFilter( FILE, animatedImageFileExtList() );
  }

  /**
   * Возвращает массив расширений неанимируемых изображений (создает "на лету" из существующих массивов).
   *
   * @return String[] - массив расширений не-анимированных изображений
   */
  static String[] stillImageFileExtensions() {
    int count = 0;
    for( int i = 0; i < IS_ANIM_IMAGE_EXT.length; i++ ) {
      if( !IS_ANIM_IMAGE_EXT[i] ) {
        ++count;
      }
    }
    if( count == 0 ) {
      return TsLibUtils.EMPTY_ARRAY_OF_STRINGS;
    }
    String[] arr = new String[count];
    for( int i = 0, j = 0; i < IS_ANIM_IMAGE_EXT.length; i++ ) {
      if( !IS_ANIM_IMAGE_EXT[i] ) {
        arr[j++] = IMAGE_FILE_EXTENSIONS[i];
      }
    }
    return arr;
  }

  /**
   * Возвращает список расширений не-анимированных изображений (создает "на лету" из существующих массивов).
   *
   * @return IStringList - список не-расширений анимированных изображений
   */
  static IStringList stillImageFileExtList() {
    return new StringLinkedBundleList( stillImageFileExtensions() );
  }

  /**
   * Возвращает фильтр для отбора файлов не-анимированных изображений.
   *
   * @return {@link TsFileFilter} - фильтр файлов
   */
  static TsFileFilter stillImageFileFilter() {
    return new TsFileFilter( FILE, stillImageFileExtList() );
  }

  /**
   * Determines if the file has an image extension, one of {@link IMediaFileConstants#IMAGE_FILE_EXT_LIST}.
   *
   * @param aFileName String - the file name
   * @return boolean - <code>true</code> if file has known extension (case insensitive)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static boolean hasImageExtension( String aFileName ) {
    String ext = TsFileUtils.extractExtension( aFileName ).toLowerCase();
    return IMAGE_FILE_EXT_LIST.hasElem( ext );
  }

  /**
   * Determines if the file has an still image extension, one of {@link IMediaFileConstants#stillImageFileExtList()}.
   *
   * @param aFileName String - the file name
   * @return boolean - <code>true</code> if file has known extension (case insensitive)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static boolean hasStillImageExtension( String aFileName ) {
    String ext = TsFileUtils.extractExtension( aFileName ).toLowerCase();
    return stillImageFileExtList().hasElem( ext );
  }

  /**
   * Determines if the file has an animated image extension, one of
   * {@link IMediaFileConstants#animatedImageFileExtList()}.
   *
   * @param aFileName String - the file name
   * @return boolean - <code>true</code> if file has known extension (case insensitive)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static boolean hasAnimatedImageExtension( String aFileName ) {
    String ext = TsFileUtils.extractExtension( aFileName ).toLowerCase();
    return animatedImageFileExtList().hasElem( ext );
  }

  /**
   * Определяет, является ли расширение имени файла расширением анимированного формата.
   *
   * @param aFileName String - имя файла
   * @return boolean - признак расширения файла анимированного формата
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static boolean hasAnimatedExtension( String aFileName ) {
    String ext = TsFileUtils.extractExtension( aFileName ).toLowerCase();
    int index = IMAGE_FILE_EXT_LIST.indexOf( ext );
    if( index >= 0 ) {
      return IS_ANIM_IMAGE_EXT[index];
    }
    return false;
  }

  /**
   * Определяет, есть ли у файла расширение аудио, один из {@link IMediaFileConstants#AUDIO_FILE_EXT_LIST}.
   *
   * @param aFileName String - имя файла
   * @return boolean - признак расширения аудио-файла
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static boolean hasAudioExtension( String aFileName ) {
    String ext = TsFileUtils.extractExtension( aFileName ).toLowerCase();
    return AUDIO_FILE_EXT_LIST.hasElem( ext );
  }

  /**
   * Определяет, есть ли у файла расширение видео, один из {@link IMediaFileConstants#VIDEO_FILE_EXT_LIST}.
   *
   * @param aFileName String - имя файла
   * @return boolean - признак расширения видео-файла
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static boolean hasVideoExtension( String aFileName ) {
    String ext = TsFileUtils.extractExtension( aFileName ).toLowerCase();
    return VIDEO_FILE_EXT_LIST.hasElem( ext );
  }

  /**
   * Prepares string like "*&#46;ext1", "*&#46;ext2" from bare extensions "ext1", "ext2".
   * <p>
   * May be used to set argument of <code>FileDialog.setFilterExtensions(String[])</code>.
   *
   * @param aExtensions {@link IStringList} - file extensions without leading dot
   * @return {@link IStringList} - extensions with leading wildcards
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static IStringList appendWildcards( IStringList aExtensions ) {
    TsNullArgumentRtException.checkNull( aExtensions );
    IStringListEdit result = new StringArrayList( aExtensions.size() );
    for( String s : aExtensions ) {
      result.add( "*." + s );
    }
    return result;
  }

  /**
   * Prepares string for {@link FileDialog} from base extensions.
   *
   * @param aExtensions {@link IStringList} - file extensions without leading dot
   * @return String - string for {@link FileDialog}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static String prepareForFileDialog( IStringList aExtensions ) {
    TsNullArgumentRtException.checkNull( aExtensions );
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < aExtensions.size(); i++ ) {
      String s = aExtensions.get( i );
      sb.append( "*." );
      sb.append( s );
      if( i < aExtensions.size() - 1 ) {
        sb.append( ";" );
      }
    }
    return sb.toString();
  }

}
