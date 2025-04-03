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

/**
 * Dialogs to work with files and directories.
 *
 * @author hazard157
 */
public final class TsRcpDialogUtils {

  private static final IStringList ALL_FILE_EXTS_LIST = new SingleStringList( "*.*" ); //$NON-NLS-1$

  /**
   * Displays file open dialog.
   * <p>
   * File extensions must be specified as described in {@link FileDialog#setFilterExtensions(String[])}.
   *
   * @param aShell {@link Shell} - parent window
   * @param aDefaultPath String - initial directory to open or an empty string for current directory
   * @param aExtensions {@link IStringList} - file extensions or an empty list for any extension
   * @return {@link File} - selected file or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static File askFileOpen( Shell aShell, String aDefaultPath, IStringList aExtensions ) {
    TsNullArgumentRtException.checkNulls( aShell, aDefaultPath, aExtensions );
    FileDialog fd = new FileDialog( aShell, SWT.OPEN );
    File initFile = new File( aDefaultPath );
    fd.setFileName( initFile.getName() );
    fd.setFilterPath( initFile.getParent() );
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
   * Displays file open dialog.
   *
   * @param aShell {@link Shell} - parent window
   * @param aDefaultPath String - initial directory to open or an empty string for current directory
   * @return {@link File} - selected file or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static File askFileOpen( Shell aShell, String aDefaultPath ) {
    return askFileOpen( aShell, aDefaultPath, IStringList.EMPTY );
  }

  /**
   * Displays file open dialog.
   * <p>
   * Is the same as calling {@link #askFileOpen(Shell, String)} with an empty string as a initial path.
   *
   * @param aShell {@link Shell} - parent window
   * @return {@link File} - selected file or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static File askFileOpen( Shell aShell ) {
    return askFileOpen( aShell, TsLibUtils.EMPTY_STRING, IStringList.EMPTY );
  }

  /**
   * Displays file dialog to specify file to be saved.
   * <p>
   * If the file already exists, then displays a request to overwrite. Returns the file name if the user has not
   * canceled and specified the file name, and also when choosing to overwrite an existing file. In all other cases,
   * returns <code>null</code>.
   * <p>
   * File extension <code>aDefaultExtension</code> will be added to the specified file name if it does not have the
   * specified extension.
   *
   * @param aShell Shell - parent window
   * @param aDefaultPath String - initial path to file or an empty string for current directory
   * @param aExtension String - file extension or an empty string to use extension specified in the dialog
   * @return File - the path to file or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static File askFileSave( Shell aShell, String aDefaultPath, String aExtension ) {
    TsNullArgumentRtException.checkNulls( aShell, aDefaultPath, aExtension );
    File initFile = new File( aDefaultPath );
    // asking while user selects valid file or cancels operation
    while( true ) {
      FileDialog fd = new FileDialog( aShell, SWT.SAVE );
      fd.setFileName( initFile.getName() );
      fd.setFilterPath( initFile.getParent() );
      fd.setText( STR_C_SAVE_DIALOG );
      String fName = fd.open();
      if( fName == null ) {
        return null; // user cancel
      }
      if( aExtension.length() > 0 ) {
        String extension = TsFileUtils.extractExtension( fName );
        if( extension.length() == 0 || !extension.equals( aExtension ) ) {
          fName += '.' + aExtension;
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
          initFile = f.getAbsoluteFile();
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

  // TODO TRANSLATE

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
