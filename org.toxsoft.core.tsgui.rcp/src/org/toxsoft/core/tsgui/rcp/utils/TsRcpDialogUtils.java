package org.toxsoft.core.tsgui.rcp.utils;

import static org.toxsoft.core.tsgui.rcp.utils.ITsResources.*;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

// TODO TRANSLATE

/**
 * Утилитные методы работы со стандартными диалогами, специфичными для среды RCP.
 * <p>
 * Содержит следующую функциональность:
 * <ul>
 * <li><b>askFileXxx()</b> - запрос имени файла для открытия или сохранения.</li>
 * </ul>
 * Остальные методы являются общими для сред RCP и RAP и находятся в файле TsDialogUtils проекта
 * ru.toxsoft.tsgui.common.
 *
 * @author hazard157
 */
public final class TsRcpDialogUtils {

  private static final IStringList ALL_FILE_EXTS_LIST = new SingleStringList( "*.*" ); //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Диалоги работы с файлами
  //

  /**
   * Выводит диалог выбора открываемого файла.
   * <p>
   * Для того, чтобы в начале диалог показал текущую директриию, следует в качестве aDefaultPath задать пустую строку.
   *
   * @param aShell Shell - родительское окно
   * @param aDefaultPath String - путь, которую покажет диалог в начале
   * @param aExtensions {@link IStringList} - расширения файлов в формате
   *          {@link FileDialog#setFilterExtensions(String[])} или пустой список для всех файлов
   * @return File - путь к открываемому файлу или null если пользователь отказался
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static File askFileOpen( Shell aShell, String aDefaultPath, IStringList aExtensions ) {
    TsNullArgumentRtException.checkNulls( aShell, aDefaultPath );
    FileDialog fd = new FileDialog( aShell, SWT.OPEN );
    fd.setFileName( aDefaultPath );
    fd.setText( STR_C_OPEN_DIALOG );
    if( !aExtensions.isEmpty() ) {
      fd.setFilterExtensions( aExtensions.toArray() );
    }
    String fName = fd.open();
    if( fName != null ) {
      return new File( fName );
    }
    return null;
  }

  /**
   * Выводит диалог выбора открываемого файла.
   * <p>
   * Для того, чтобы в начале диалог показал текущую директриию, следует в качестве aDefaultPath задать пустую строку.
   *
   * @param aShell Shell - родительское окно
   * @param aDefaultPath String - путь, которую покажет диалог в начале
   * @return File - путь к открываемому файлу или null если пользователь отказался
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static File askFileOpen( Shell aShell, String aDefaultPath ) {
    return askFileOpen( aShell, aDefaultPath, IStringList.EMPTY );
  }

  /**
   * Выводит диалог выбора открываемого файла.
   * <p>
   * Эквивалентно вызову метода {@link #askFileOpen(Shell, String)} с пустой строкой в качестве aDefaultPath.
   *
   * @param aShell Shell - родительское окно
   * @return File - путь к открываемому файлу или null если пользователь отказался
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static File askFileOpen( Shell aShell ) {
    return askFileOpen( aShell, TsLibUtils.EMPTY_STRING, IStringList.EMPTY );
  }

  /**
   * Выводит диалог выбора/ввода имени файла для сохранения документа.
   * <p>
   * Если файл уже существует, то выводит запрос на перезапись. Возвращает имя файла если пользователь не отказался и
   * задал имя файла, а также, при выборе перезаписи существующего файла. В всех других случаях возвращает null.
   * <p>
   * Для того, чтобы в начале диалог показал текущую директриию, следует в качестве aDefaultPath задать пустую строку.
   * <p>
   * Можно указать расширение aDefaultExtension (без начальной точки), которое будет добавляться к указанному в диалоге
   * имени файла. Расширение добавляется, если указанное в диалоге имя не содержит точки (т.е. нет расширения), или
   * указано расширение, отличающейся от aDefaultExtension. Если aDefaultExtension пустая строка, имя файла не меняется.
   *
   * @param aShell Shell - родительское окно
   * @param aDefaultPath String - путь, которую покажет диалог в начале
   * @param aDefaultExtension String - добавляемое по умолчанию расширение имеи файла или пустая строка
   * @return File - путь к файлу для сохранения или null если пользователь отказался
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static File askFileSave( Shell aShell, String aDefaultPath, String aDefaultExtension ) {
    TsNullArgumentRtException.checkNulls( aShell, aDefaultPath, aDefaultExtension );
    String initialPath = aDefaultPath;
    // asking while user selects valid file or cancels operation
    while( true ) {
      FileDialog fd = new FileDialog( aShell, SWT.SAVE );
      fd.setFileName( initialPath );
      fd.setText( STR_C_SAVE_DIALOG );
      String fName = fd.open();
      if( fName == null ) {
        return null; // user cancel
      }
      if( aDefaultExtension.length() > 0 ) {
        String extension = TsFileUtils.extractExtension( fName );
        if( extension.length() == 0 || !extension.equals( aDefaultExtension ) ) {
          fName += '.' + aDefaultExtension;
        }
      }
      File f = new File( fName );
      if( !f.exists() ) {
        return f; // no such file - just return entered file name
      }
      // file exists, allow user to cancel, overwrite or specify new name
      switch( TsDialogUtils.askYesNoCancel( aShell, FMT_MSG_OVERWRITE_FILE, f.getAbsoluteFile() ) ) {
        case YES:
          return f; // overwrite existing file
        case NO:
          initialPath = f.getAbsolutePath();
          break; // return to file selection
        case APPLY:
        case CANCEL:
        case CLOSE:
        case OK:
        default:
          return null; // cancel operation
      }
    }
  }

  /**
   * Выводит диалог выбора/ввода имени файла для сохранения документа.
   * <p>
   * Если файл уже существует, то выводит запрос на перезапись. Возвращает имя файла если пользователь не отказался и
   * задал имя файла, а также, при выборе перезаписи существующего файла. В всех других случаях возвращает null.
   * <p>
   * Для того, чтобы в начале диалог показал текущую директриию, следует в качестве aDefaultPath задать пустую строку.
   *
   * @param aShell Shell - родительское окно
   * @param aDefaultPath String - путь, которую покажет диалог в начале
   * @return File - путь к файлу для сохранения или null если пользователь отказался
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static File askFileSave( Shell aShell, String aDefaultPath ) {
    return askFileSave( aShell, aDefaultPath, TsLibUtils.EMPTY_STRING );
  }

  /**
   * Выводит диалог выбора/ввода имени файла для сохранения документа.
   * <p>
   * Эквивалентно вызову метода {@link #askFileSave(Shell, String)} с пустой строкой в качестве aDefaultPath.
   *
   * @param aShell Shell - родительское окно
   * @return File - путь к файлу для сохранения или null если пользователь отказался
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static File askFileSave( Shell aShell ) {
    return askFileSave( aShell, TsLibUtils.EMPTY_STRING );
  }

  /**
   * Выводит диалог выбора существующего диретория.
   * <p>
   * Для того, чтобы в начале диалог показал текущую директриию, следует в качестве aDefaultPath задать пустую строку.
   *
   * @param aShell Shell - родительское окно
   * @param aDefaultPath String - путь, которую покажет диалог в начале
   * @return File - путь к выбранной директории или null если пользователь отказался
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static File askDirOpen( Shell aShell, String aDefaultPath ) {
    TsNullArgumentRtException.checkNulls( aShell, aDefaultPath );
    DirectoryDialog dd = new DirectoryDialog( aShell, 0 );
    dd.setFilterPath( aDefaultPath );
    dd.setText( STR_SELECT_DIRECTORY_CAPTION );
    String dirName = dd.open();
    if( dirName != null ) {
      return new File( dirName );
    }
    return null;
  }

  /**
   * Prepares multi-extension string for {@link FileDialog#setFilterExtensions(String[])}.
   * <p>
   * For empty list returns "*.*" extension filter.
   *
   * @param aExtensions {@link IStringList} - extensions without dots
   * @return {@link IStringList} - listwith string of multi-extensions and strings for each extension
   */
  public static IStringList makeMultiExtensions( IStringList aExtensions ) {
    TsNullArgumentRtException.checkNull( aExtensions );
    if( aExtensions.isEmpty() ) {
      return ALL_FILE_EXTS_LIST;
    }
    IStringListEdit ss = new StringArrayList();
    // all extension as one multi-extension
    StringBuilder sb = new StringBuilder();
    for( String s : aExtensions ) {
      sb.append( "*." ); //$NON-NLS-1$
      sb.append( s );
      if( s != aExtensions.last() ) {
        sb.append( ';' );
      }
    }
    ss.add( sb.toString() );
    // all extensions one-by-one
    for( String s : aExtensions ) {
      ss.add( "*." + s ); //$NON-NLS-1$
    }
    return ss;
  }

  private TsRcpDialogUtils() {
    // nop
  }

}
