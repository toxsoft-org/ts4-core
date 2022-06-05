package org.toxsoft.core.tsgui.utils;

import static org.toxsoft.core.tslib.utils.files.EFsObjKind.*;

import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

/**
 * Константы, связанные с обработкой медия-файлов.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
public interface IMediaFileConstants {

  /**
   * Перечень расширении в нижнем регистре (без первой точки) файлов изображений, распознаваемых программой.
   * <p>
   * Расширения в общем случае (например, в Linux) чувствительны к регистру, поэтому, сравнение надо делать либо
   * регистро-независимым, либо предвариетльно приведя расширение к нижнему регистру.
   * <p>
   * Для добавления формата, просто добавьте расширение (например, "jpeg").
   * <p>
   * Внимание: порядок важен при поиске файла кадра эпизода.
   */
  String[] IMAGE_FILE_EXTENSIONS = { "jpg", "jpeg", "png", "mng", "gif", "bmp" };

  /**
   * Перечень {@link #IMAGE_FILE_EXTENSIONS} в виде {@link IStringList}.
   */
  IStringList IMAGE_FILE_EXT_LIST = new StringArrayList( IMAGE_FILE_EXTENSIONS );

  /**
   * Image files (files with extension {@link #IMAGE_FILE_EXT_LIST}) filter.
   */
  TsFileFilter FF_IMAGES = new TsFileFilter( EFsObjKind.FILE, IMAGE_FILE_EXT_LIST );

  /**
   * Признак того, что соответствующее {@link #IMAGE_FILE_EXTENSIONS} является расширением анимрованного формата.
   */
  boolean[] IS_ANIM_IMAGE_EXT = { false, false, false, true, true, false };

  /**
   * Фильтр для отбора файлов изображений (файлов с расширениями {@link #IMAGE_FILE_EXTENSIONS}).
   */
  TsFileFilter IMAGE_FILES_FILTER = new TsFileFilter( FILE, IMAGE_FILE_EXT_LIST );

  /**
   * Перечень расширении в нижнем регистре (без первой точки) видео-файлов, распознаваемых программой.
   * <p>
   * Расширения в общем случае (например, в Linux) чувствительны к регистру.
   * <p>
   * Для добавления формата, просто добавьте расширение (например, "wmv").
   */
  String[] VIDEO_FILE_EXTENSIONS = { "webm", "avi", "mp4", "mpg", "mpeg", "vob", "wmv", "dv" };

  /**
   * Перечень {@link #VIDEO_FILE_EXTENSIONS} в виде {@link IStringList}.
   */
  IStringList VIDEO_FILE_EXT_LIST = new StringArrayList( VIDEO_FILE_EXTENSIONS );

  /**
   * Фильтр для отбора файлов видео (файлов с расширениями {@link #VIDEO_FILE_EXTENSIONS}).
   */
  TsFileFilter VIDEO_FILES_FILTER = new TsFileFilter( FILE, VIDEO_FILE_EXT_LIST );

  /**
   * Перечень расширении в нижнем регистре (без первой точки) видео-файлов, распознаваемых программой.
   * <p>
   * Расширения в общем случае (например, в Linux) чувствительны к регистру.
   * <p>
   * Для добавления формата, просто добавьте расширение (например, "wmv").
   */
  String[] AUDIO_FILE_EXTENSIONS = { "mp3", "wav" };

  /**
   * Перечень {@link #AUDIO_FILE_EXTENSIONS} в виде {@link IStringList}.
   */
  IStringList AUDIO_FILE_EXT_LIST = new StringArrayList( AUDIO_FILE_EXTENSIONS );

  /**
   * Фильтр для отбора файлов изображений (файлов с расширениями {@link #AUDIO_FILE_EXTENSIONS}).
   */
  TsFileFilter AUDIO_FILES_FILTER = new TsFileFilter( FILE, AUDIO_FILE_EXT_LIST );

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
   * Определяет, есть ли у файла расширение изображения, один из {@link IMediaFileConstants#IMAGE_FILE_EXT_LIST}.
   *
   * @param aFileName String - имя файла
   * @return boolean - признак расширения файла - изображения
   * @throws TsNullArgumentRtException аргумент = null
   */
  static boolean hasImageExtension( String aFileName ) {
    String ext = TsFileUtils.extractExtension( aFileName ).toLowerCase();
    return IMAGE_FILE_EXT_LIST.hasElem( ext );
  }

  /**
   * Определяет, является ли расширение имени файла расширением анимированного формата.
   *
   * @param aFileName String - имя файла
   * @return boolean - признак расширения файла анимированного формата
   * @throws TsNullArgumentRtException аргумент = null
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
   * @throws TsNullArgumentRtException аргумент = null
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
   * @throws TsNullArgumentRtException аргумент = null
   */
  static boolean hasVideoExtension( String aFileName ) {
    String ext = TsFileUtils.extractExtension( aFileName ).toLowerCase();
    return VIDEO_FILE_EXT_LIST.hasElem( ext );
  }

  /**
   * Из расширений "ext1", "ext2" без точек создает расширения со звездочками "*&#46;ext1", "*&#46;ext2".
   * <p>
   * Полезно для преобразования выше приведенных раширений в форму, пригодную для
   * <code>FileDialog.setFilterExtensions(String[])</code>.
   *
   * @param aExtensions {@link IStringList} - список расширений без точек
   * @return {@link IStringList} - список расширений со звездочками и точками
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
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
   * Из расширений "ext1", "ext2" без точек создает строку для <code>FileDialog</code>.
   * <p>
   * Этот метод имеет смысл только для RCP, ведь в RAP нет <code>FileDialog</code>.
   *
   * @param aExtensions {@link IStringList} - раширения без точек
   * @return String - строка для <code>FileDialog</code>
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
