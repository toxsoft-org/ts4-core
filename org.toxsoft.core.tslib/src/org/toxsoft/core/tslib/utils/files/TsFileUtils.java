package org.toxsoft.core.tslib.utils.files;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;
import static org.toxsoft.core.tslib.utils.files.ITsResources.*;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * A set of methods for working with the files and directories.
 *
 * @author hazard157
 */
public class TsFileUtils {

  /**
   * The separator between file name and extension - dot character.
   * <p>
   * Here extension is the part of the file name after the last dot.
   */
  public static final char CHAR_EXT_SEPARATOR = '.';

  /**
   * {@link #CHAR_EXT_SEPARATOR} as string.
   */
  public static final String STR_EXT_SEPARATOR = "" + CHAR_EXT_SEPARATOR; //$NON-NLS-1$

  /**
   * Empty immutable array of {@link File} elements.
   */
  public static final File[] EMPTY_ARRAY_OF_FILES = {};

  /**
   * {@link File} comparator in ascending order by {@link File#getName()}.
   */
  public static final Comparator<File> FILE_CMP_ASC = Comparator.comparing( File::getName );

  /**
   * {@link File} comparator in descending order by {@link File#getName()}.
   */
  public static final Comparator<File> FILE_CMP_DESC = Comparator.comparing( File::getName ).reversed();

  /**
   * {@link File} comparator in ascending order by {@link File#getName()}, directories before files.
   */
  public static final Comparator<File> FILEDIR_CMP_ASC = ( f1, f2 ) -> {
    if( f1.isDirectory() != f2.isDirectory() ) {
      return f1.isDirectory() ? -1 : 1;
    }
    return f1.getName().compareTo( f2.getName() );
  };

  /**
   * {@link File} comparator in descending order by {@link File#getName()}, directories before files.
   */
  public static final Comparator<File> FILEDIR_CMP_DESC = ( f1, f2 ) -> {
    if( f1.isDirectory() != f2.isDirectory() ) {
      return f1.isDirectory() ? -1 : 1;
    }
    return f2.getName().compareTo( f1.getName() );
  };

  // ------------------------------------------------------------------------------------
  // File system access checks
  //

  /**
   * Validator checks if file exists and is readable.
   */
  public static final ITsValidator<File> VALIDATOR_FILE_READABLE = aFile -> {
    TsNullArgumentRtException.checkNull( aFile );
    if( !aFile.exists() ) {
      return ValidationResult.error( FMT_ERR_FILE_NOT_EXISTS, aFile.getAbsolutePath() );
    }
    if( !aFile.isFile() ) {
      return ValidationResult.error( FMT_ERR_PATH_IS_NOT_FILE, aFile.getAbsolutePath() );
    }
    if( !aFile.canRead() ) {
      return ValidationResult.error( FMT_ERR_FILE_NOT_READABLE, aFile.getAbsolutePath() );
    }
    return ValidationResult.SUCCESS;
  };

  /**
   * Validator checks if file exists and is writable.
   */
  public static final ITsValidator<File> VALIDATOR_FILE_WRITEABLE = aFile -> {
    ValidationResult r = VALIDATOR_FILE_READABLE.validate( aFile );
    if( !r.isOk() ) {
      return r;
    }
    if( !aFile.canWrite() ) {
      return ValidationResult.error( FMT_ERR_FILE_NOT_WRITEABLE, aFile.getAbsolutePath() );
    }
    return ValidationResult.SUCCESS;
  };

  /**
   * Validator checks if directory exists and is readable.
   */
  public static final ITsValidator<File> VALIDATOR_DIR_READABLE = aDir -> {
    TsNullArgumentRtException.checkNull( aDir );
    if( !aDir.exists() ) {
      return ValidationResult.error( FMT_ERR_DIR_NOT_EXISTS, aDir.getAbsolutePath() );
    }
    if( !aDir.isDirectory() ) {
      return ValidationResult.error( FMT_ERR_PATH_IS_NOT_DIRECTORY, aDir.getAbsolutePath() );
    }
    if( !aDir.canRead() ) {
      return ValidationResult.error( FMT_ERR_DIR_NOT_READABLE, aDir.getAbsolutePath() );
    }
    return ValidationResult.SUCCESS;
  };

  /**
   * Validator checks if directory exists and is writable.
   */
  public static final ITsValidator<File> VALIDATOR_DIR_WRITEABLE = aDir -> {
    ValidationResult r = VALIDATOR_DIR_READABLE.validate( aDir );
    if( !r.isOk() ) {
      return r;
    }
    if( !aDir.canWrite() ) {
      return ValidationResult.error( FMT_ERR_DIR_NOT_WRITEABLE, aDir.getAbsolutePath() );
    }
    return ValidationResult.SUCCESS;
  };

  /**
   * Validator checks if file exists and is writable.
   * <p>
   * Checks if existing file is writable or unexisting file may be created.
   */
  public static final ITsValidator<File> VALIDATOR_FILE_APPENDABLE = aFile -> {
    TsNullArgumentRtException.checkNull( aFile );
    if( aFile.exists() ) {
      if( !aFile.isFile() ) {
        return ValidationResult.error( FMT_ERR_PATH_IS_NOT_FILE, aFile.getAbsolutePath() );
      }
      if( !aFile.canRead() ) {
        return ValidationResult.error( FMT_ERR_FILE_NOT_READABLE, aFile.getAbsolutePath() );
      }
      if( !aFile.canWrite() ) {
        return ValidationResult.error( FMT_ERR_FILE_NOT_WRITEABLE, aFile.getAbsolutePath() );
      }
      return ValidationResult.SUCCESS;
    }
    File parentDir = aFile.getParentFile();
    if( parentDir != null ) {
      return VALIDATOR_DIR_WRITEABLE.validate( parentDir );
    }
    return ValidationResult.SUCCESS;
  };

  /**
   * Checks if file passes {@link #VALIDATOR_FILE_READABLE} validation.
   *
   * @param aFile File - file to be checked
   * @return boolean - validation returns not an error {@link ValidationResult#isError()} = <code>false</code>
   * @throws TsNullArgumentRtException aFile = null
   */
  public static boolean isFileReadable( File aFile ) {
    return !VALIDATOR_FILE_READABLE.validate( aFile ).isError();
  }

  /**
   * Throws an exception if {@link #VALIDATOR_FILE_READABLE} validation fails.
   *
   * @param aFile File - file to be checked
   * @return {@link File} - always return argument
   * @throws TsNullArgumentRtException aFile = null
   * @throws TsIoRtException validation failed
   */
  public static File checkFileReadable( File aFile ) {
    ValidationResult vr = VALIDATOR_FILE_READABLE.validate( aFile );
    if( vr.isError() ) {
      throw new TsIoRtException( vr.message() );
    }
    return aFile;
  }

  /**
   * Checks if file passes {@link #VALIDATOR_FILE_WRITEABLE} validation.
   *
   * @param aFile File - file to be checked
   * @return boolean - validation returns not an error {@link ValidationResult#isError()} = <code>false</code>
   * @throws TsNullArgumentRtException aFile = null
   */
  public static boolean isFileWriteable( File aFile ) {
    return !VALIDATOR_FILE_WRITEABLE.validate( aFile ).isError();
  }

  /**
   * Throws an exception if {@link #VALIDATOR_FILE_WRITEABLE} validation fails.
   *
   * @param aFile File - file to be checked
   * @return {@link File} - always return argument
   * @throws TsNullArgumentRtException aFile = null
   * @throws TsIoRtException validation failed
   */
  public static File checkFileWriteable( File aFile ) {
    ValidationResult vr = VALIDATOR_FILE_WRITEABLE.validate( aFile );
    if( vr.isError() ) {
      throw new TsIoRtException( vr.message() );
    }
    return aFile;
  }

  /**
   * Checks if file passes {@link #VALIDATOR_FILE_APPENDABLE} validation.
   *
   * @param aFile File - file to be checked
   * @return boolean - validation returns not an error {@link ValidationResult#isError()} = <code>false</code>
   * @throws TsNullArgumentRtException aFile = null
   */
  public static boolean isFileAppendable( File aFile ) {
    return !VALIDATOR_FILE_APPENDABLE.validate( aFile ).isError();
  }

  /**
   * Throws an exception if {@link #VALIDATOR_FILE_APPENDABLE} validation fails.
   *
   * @param aFile File - file to be checked
   * @return {@link File} - always return argument
   * @throws TsNullArgumentRtException aFile = null
   * @throws TsIoRtException validation failed
   */
  public static File checkFileAppendable( File aFile ) {
    ValidationResult vr = VALIDATOR_FILE_APPENDABLE.validate( aFile );
    if( vr.isError() ) {
      throw new TsIoRtException( vr.message() );
    }
    return aFile;
  }

  /**
   * Checks if directory passes {@link #VALIDATOR_DIR_READABLE} validation.
   *
   * @param aDir {@link File} - directory to be checked
   * @return boolean - validation returns not an error {@link ValidationResult#isError()} = <code>false</code>
   * @throws TsNullArgumentRtException aDir = null
   */
  public static boolean isDirReadable( File aDir ) {
    return !VALIDATOR_DIR_READABLE.validate( aDir ).isError();
  }

  /**
   * Throws an exception if {@link #VALIDATOR_DIR_READABLE} validation fails.
   *
   * @param aDir File - directory to be checked
   * @return {@link File} - always return argument
   * @throws TsNullArgumentRtException aFile = null
   * @throws TsIoRtException validation failed
   */
  public static File checkDirReadable( File aDir ) {
    ValidationResult vr = VALIDATOR_DIR_READABLE.validate( aDir );
    if( vr.isError() ) {
      throw new TsIoRtException( vr.message() );
    }
    return aDir;
  }

  /**
   * Checks if directory passes {@link #VALIDATOR_DIR_WRITEABLE} validation.
   *
   * @param aDir {@link File} - directory to be checked
   * @return boolean - validation returns not an error {@link ValidationResult#isError()} = <code>false</code>
   * @throws TsNullArgumentRtException aDir = null
   */
  public static boolean isDirWriteable( File aDir ) {
    return !VALIDATOR_DIR_WRITEABLE.validate( aDir ).isError();
  }

  /**
   * Throws an exception if {@link #VALIDATOR_DIR_WRITEABLE} validation fails.
   *
   * @param aDir File - directory to be checked
   * @return {@link File} - always return argument
   * @throws TsNullArgumentRtException aFile = null
   * @throws TsIoRtException validation failed
   */
  public static File checkDirWriteable( File aDir ) {
    ValidationResult vr = VALIDATOR_DIR_WRITEABLE.validate( aDir );
    if( vr.isError() ) {
      throw new TsIoRtException( vr.message() );
    }
    return aDir;
  }

  /**
   * Throws an exception if filesystem object does not extsis.
   *
   * @param aFsObj {@link File} - file system object
   * @return {@link File} - always return argument
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException no such object exists
   */
  public static File checkFsObjExists( File aFsObj ) {
    TsNullArgumentRtException.checkNull( aFsObj );
    if( !aFsObj.exists() ) {
      throw new TsIoRtException( ERR_FMT_FSOBJ_NOT_EXISTS, aFsObj.getPath() );
    }
    return aFsObj;
  }

  // ------------------------------------------------------------------------------------
  // File name manipulation methods
  //

  /**
   * Extracts file name (without path and extension).
   * <p>
   * If argument is considered as directory name (argument ends with {@link File#separatorChar}), no extension is
   * extracted.
   * <p>
   * Thgis methods works with strings and does not calls any filesystem access methods.
   *
   * @param aFileName String - specified file name, may include path
   * @return String - file name (without path and extension)
   * @throws TsNullArgumentRtException argument = null
   */
  public static String extractBareFileName( String aFileName ) {
    TsNullArgumentRtException.checkNull( aFileName );
    int pathIdx = aFileName.lastIndexOf( File.separatorChar );
    if( pathIdx >= 0 && pathIdx == aFileName.length() - 1 ) { // ?????? ?????? ??????????????????
      if( aFileName.length() == 1 ) {
        return aFileName;
      }
      pathIdx = aFileName.lastIndexOf( File.separatorChar, pathIdx - 1 );
      return aFileName.substring( pathIdx + 1, aFileName.length() - 1 );
    }
    int extIdx = aFileName.lastIndexOf( CHAR_EXT_SEPARATOR );
    int startIdx = 0;
    int endIdx = aFileName.length();
    if( pathIdx >= 0 ) {
      startIdx = pathIdx + 1;
    }
    if( extIdx >= 0 && extIdx > pathIdx ) {
      endIdx = extIdx;
    }
    return aFileName.substring( startIdx, endIdx );
  }

  /**
   * Extracts file name (with extension, without path) from the specified file name.
   * <p>
   * If argument is considered as directory name (argument ends with {@link File#separatorChar}), returns empty string.
   * <p>
   * Thgis methods works with strings and does not calls any filesystem access methods.
   *
   * @param aFileName String - specified file name, may include path
   * @return String - file name (with extension, without path)
   * @throws TsNullArgumentRtException argument = null
   */
  public static String extractFileName( String aFileName ) {
    TsNullArgumentRtException.checkNull( aFileName );
    if( aFileName.isEmpty() ) {
      return EMPTY_STRING;
    }
    if( aFileName.charAt( aFileName.length() - 1 ) == File.separatorChar ) {
      return EMPTY_STRING;
    }
    File f = new File( aFileName );
    return f.getName();
  }

  /**
   * Extracts extension from the specified file name.
   * <p>
   * The ending part (after last dot) is considered as extension.
   * <p>
   * If argument is considered as directory name (argument ends with {@link File#separatorChar}), returns empty string.
   * <p>
   * Thgis methods works with strings and does not calls any filesystem access methods.
   *
   * @param aFileName String - specified file name, may include path
   * @return String - file extension
   * @throws TsNullArgumentRtException argument = null
   */
  public static String extractExtension( String aFileName ) {
    TsNullArgumentRtException.checkNull( aFileName );
    if( aFileName.length() == 0 ) {
      return TsLibUtils.EMPTY_STRING;
    }
    int pathIdx = aFileName.lastIndexOf( File.separatorChar );
    if( pathIdx == aFileName.length() - 1 ) { // ?????? ?????? ??????????????????
      return TsLibUtils.EMPTY_STRING;
    }
    int extIdx = aFileName.lastIndexOf( CHAR_EXT_SEPARATOR );
    if( extIdx < pathIdx ) { // ???????? ?????? ????????????????????, ?? ?????????? ???????? ?????????? ???????????? ???? ???????????????????????? ????????????????????
      return TsLibUtils.EMPTY_STRING;
    }
    return aFileName.substring( extIdx + 1 );
  }

  /**
   * Removes all (if any) {@link File#separatorChar} from the start of the string.
   *
   * @param aPath String - source string
   * @return String - source string without starting separator chars
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static String removeStartingSeparator( String aPath ) {
    TsNullArgumentRtException.checkNull( aPath );
    int pathLen = aPath.length();
    if( pathLen == 0 ) {
      return aPath;
    }
    int count = 0;
    while( aPath.charAt( count ) == File.separatorChar ) {
      ++count;
    }
    return aPath.substring( count );
  }

  /**
   * Returns an argument with the addition of a {@link File#separatorChar} at the end, if there is none.
   * <p>
   * Empty string is returned as is.
   *
   * @param aPath String - source string
   * @return String - source string with {@link File#separatorChar} at end
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static String ensureEndingSeparator( String aPath ) {
    TsNullArgumentRtException.checkNull( aPath );
    int pathLen = aPath.length();
    if( pathLen == 0 ) {
      return aPath;
    }
    if( aPath.charAt( pathLen - 1 ) == File.separatorChar ) {
      return aPath;
    }
    return aPath + File.separatorChar;
  }

  // ------------------------------------------------------------------------------------
  // Working with filesystem hierarchy
  //

  /**
   * Determines if filesystem object aChild is under directory aParentDir.
   * <p>
   * If arguments points to the same objects then method returns <code>true</code>.
   *
   * @param aParentDir {@link File} - probably parent fs object
   * @param aChild {@link File} - probably child fs object
   * @return boolean - <code>true</code> if aChild is in subdiectory under aParentDir
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aParentDir does not passes {@link #checkDirReadable(File)}
   */
  public static boolean isChild( File aParentDir, File aChild ) {
    checkDirReadable( aParentDir );
    TsNullArgumentRtException.checkNull( aChild );
    String p1 = aParentDir.getAbsolutePath();
    String p2 = aChild.getAbsolutePath();
    int len1 = p1.length();
    int len2 = p2.length();
    if( len1 > len2 ) {
      return false;
    }
    if( len1 == len2 ) {
      return p1.equals( p2 );
    }
    String p2sub = p2.substring( 0, len1 );
    // start of the paths is the same
    if( p1.equals( p2sub ) ) {
      // ????????????????, ?????? ???????????????? p2sub ???? ?????????????????? ?????????? ???????????????? ????????????????????
      if( p2.charAt( len1 ) == File.separatorChar ) {
        return true;
      }
    }
    return false;
  }

  /**
   * Lists the content of the directory.
   *
   * @param aDir {@link File} - the directory
   * @param aKind {@link EFsObjKind} - the kind of objects to list
   * @return {@link IListEdit}&lt;{@link File}&gt; - new editable instance of the child file objects
   * @throws TsNullArgumentRtException aDir = <code>null</code>
   * @throws TsIoRtException directory access validation failed
   */
  public static IListEdit<File> listChilds( File aDir, EFsObjKind aKind ) {
    TsFileUtils.checkDirReadable( aDir );
    TsNullArgumentRtException.checkNull( aKind );
    File[] ll = aDir.listFiles();
    IListEdit<File> result = new ElemLinkedBundleList<>();
    if( ll != null ) {
      for( File f : ll ) {
        if( aKind.isAccepted( f ) ) {
          result.add( f );
        }
      }
    }
    return result;
  }

  /**
   * Lists the content of the directory.
   *
   * @param aDir {@link File} - the directory
   * @param aFilter {@link FileFilter} - the filter or <code>null</code> for all childs
   * @return {@link IListEdit}&lt;{@link File}&gt; - new editable instance of the child file objects
   * @throws TsNullArgumentRtException aDir = <code>null</code>
   * @throws TsIoRtException directory access validation failed
   */
  public static IListEdit<File> listChilds( File aDir, FileFilter aFilter ) {
    TsFileUtils.checkDirReadable( aDir );
    File[] ll;
    if( aFilter != null ) {
      ll = aDir.listFiles( aFilter );
    }
    else {
      ll = aDir.listFiles();
    }
    IListEdit<File> result;
    if( ll != null ) {
      result = new ElemArrayList<>( ll );
    }
    else {
      result = new ElemArrayList<>();
    }
    return result;
  }

  /**
   * Lists the content of the directory sorted in ascending order by file name.
   *
   * @param aDir {@link File} - the directory
   * @param aFilter {@link FileFilter} - the filter or <code>null</code> for all childs
   * @return {@link IListEdit}&lt;{@link File}&gt; - new editable sorted list of the child file objects
   * @throws TsNullArgumentRtException aDir = <code>null</code>
   * @throws TsIoRtException directory access validation failed
   */
  public static IListEdit<File> listChildsSorted( File aDir, FileFilter aFilter ) {
    TsFileUtils.checkDirReadable( aDir );
    File[] ll;
    if( aFilter != null ) {
      ll = aDir.listFiles( aFilter );
    }
    else {
      ll = aDir.listFiles();
    }
    IListEdit<File> result;
    if( ll != null ) {
      Arrays.parallelSort( ll );
      result = ElemArrayList.createDirect( ll );
    }
    else {
      result = new ElemArrayList<>();
    }
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Copy files
  //

  /**
   * Temporary buffer size for {@link #copyFile(File, File, IFileOperationProgressCallback)}.
   */
  private static final int FILE_BUF_SIZE = 32 * 1024;

  /**
   * Copies the file aSrc to the file aDest.
   * <p>
   * Existing destination file will be overwritten.
   *
   * @param aSrc {@link File} - source file
   * @param aDest {@link File} - destination file
   * @param aProgressCounter {@link IFileOperationProgressCallback} - progress monitoring callback
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error reading from the input file
   * @throws TsIoRtException error writing to the output file
   */
  public static void copyFile( File aSrc, File aDest, IFileOperationProgressCallback aProgressCounter ) {
    TsNullArgumentRtException.checkNull( aProgressCounter );
    checkFileReadable( aSrc );
    checkFileAppendable( aDest );
    try {
      long totalSteps = aSrc.length() / FILE_BUF_SIZE + 1;
      long currentStep = 0;
      if( aProgressCounter.onFileCopyProgress( totalSteps, currentStep ) ) {
        return;
      }
      try( InputStream in = new FileInputStream( aSrc ); OutputStream out = new FileOutputStream( aDest ) ) {
        byte[] buf = new byte[FILE_BUF_SIZE];
        int len;
        while( (len = in.read( buf )) > 0 ) {
          out.write( buf, 0, len );
          if( aProgressCounter.onFileCopyProgress( totalSteps, currentStep ) ) {
            break;
          }
        }
      }
    }
    catch( IOException e ) {
      throw new TsIoRtException( e );
    }
  }

  /**
   * Copies the file aSrc to the file aDest.
   * <p>
   * Simply calls {@link #copyFile(File, File, IFileOperationProgressCallback)
   * copyFile(aSrc,aDest,IFileCopyProgressCallback.NULL}.
   *
   * @param aSrc {@link File} - source file
   * @param aDest {@link File} - destination file
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error reading from the input file
   * @throws TsIoRtException error writing to the output file
   */
  public static void copyFile( File aSrc, File aDest ) {
    copyFile( aSrc, aDest, IFileOperationProgressCallback.NULL );
  }

  // ------------------------------------------------------------------------------------
  // Miscallenous methods
  //

  /**
   * ???????????????????? ???? ?????????????????? ?????????? ????????, ?????????????? ?????????????? ???????????????????? ?? ???????????????? ??????????????.
   * <p>
   * ????????????????, aPath = "/home/user/temp/cache/info.txt", ???? ???? ???????? ???? ???????????????????? ???????????????????? temp, ???? ?????????? ????????????
   * "/home/user", ?? ???????? ?????????? ???????? ????????????????????, ???? ???????????? ???????????????????? ????????????????. ???????? ???????????????? - ???????????????????? ????????, ???? ??
   * UNIX ???????????????? ?? ???????????? ???????????? ?????????? ???????????? "/", ?? ?? Windows, ?????? ???????????????????? ?????????????????? ?????????? (????????????????, "f:"),
   * ???????????? ???????????? ????????????. ???????? ???????????????? ?????????????????????????? ????????, ?? ???? ???? ???????????????????? - ?????????? ???????????? ???????????? ????????????.
   * <p>
   * ???????? ????????????, ?????? ???????????? ???????????? ???????????????? ?????????????? ????????????????????. ??????????????, ?? UNIX ???? ???????? ?????????????? ???????????????????????? ????????????????
   * ???????????????????? - ???????? ???????????????? ?????????????? "/", ???????? ???????????? ???????????? "", ?????????????? ?? ???????????? ???????????????? ?????????????? ????????????????????, ??????????
   * ???????????????? ?????? ?????????????????????????? ??????????. ?? Windows ???? ???????????????? ???????????????????? (???????????? ?????????? ????????????) - ???????????????????????? ????????????
   * ???????????? ?????????? ???????????????? ?????? ?????????????? ???????????????????? (?????????????? ???? ?????????????????????? ????????????????????, ?????? ?? ???????????????????????? ???????? ??????
   * ???????????????? ?? ?????????????????? ???????????????????????????? ???????????????????? ????????. ?????? ??????, ?????? ?????????? - ?????????????????????? UNIX ?? Linux :)
   *
   * @param aPath String - ???????? ?? ???????????????? ?????? ??????????
   * @return String - ???????????????????????? ?????????? ???????? ?????? ???????????? ????????????, ???????? ???????????????? ???????????????????? ???????? ?? ?????????????????? ????
   *         ???????????????????????????? ???????? ?? Windows
   * @throws TsNullArgumentRtException ???????????????? = null
   */
  public static String determineExistingPartOfPath( String aPath ) {
    TsNullArgumentRtException.checkNull( aPath );
    String s = aPath;
    int index = aPath.length();
    // ?????????????? ?????????????????????? ???????? ?? ??????????, ???????? ???? ?????????????????? ???? ?????????????????????? ????????
    while( (index = s.lastIndexOf( File.separator, index - 1 )) >= 0 ) {
      File f = new File( s );
      if( f.exists() ) {
        return s;
      }
      s = s.substring( 0, index + 1 );
    }
    // ???????????? ?????? ?????????????????????????? - ?????????????????? ??????????????: ?? Windows ?????? ????????, ?? ?? UNIX ???? ?????? ???? ???????????????? :)
    File f = new File( s );
    if( f.exists() ) {
      return s;
    }
    return TsLibUtils.EMPTY_STRING;
  }

  /**
   * Maximum number of tries to create unique file name in {@link #uniqueDestFile(File)}.
   */
  static final int MAX_UNIQUE_FILE_NAME_PREFIXES = 100500;

  /**
   * Returns unique name of the unexistent file, based on specified file.
   * <p>
   * Main purpose of this method is to avoid destination file to overwritten during file copy operations. Returns the
   * file name in the same directory as specified file and with the same extension. New name is created by adding some
   * postfix to file name string.
   * <p>
   * For unexistent file returns argument.
   *
   * @param aDestFile {@link File} - specified file
   * @return {@link File} - unexistent file name like specified one
   * @throws TsNullArgumentRtException argument = null
   * @throws TsIllegalStateRtException files with all available names already exist
   */
  public static File uniqueDestFile( File aDestFile ) {
    TsNullArgumentRtException.checkNull( aDestFile );
    if( !aDestFile.exists() ) {
      return aDestFile;
    }
    File dir = aDestFile.getParentFile();
    String bare = TsFileUtils.extractBareFileName( aDestFile.getName() );
    String ext = TsFileUtils.extractExtension( aDestFile.getName() );
    for( int i = 1; i <= 100500; i++ ) {
      String name = bare + ' ' + i;
      File f = new File( dir, name + CHAR_EXT_SEPARATOR + ext );
      if( !f.exists() ) {
        return f;
      }
    }
    throw new TsIllegalStateRtException( FMT_ERR_CANT_UNIQUE_FILE, aDestFile.getName() );
  }

  /**
   * Permanently deletes directory and it's content from the file system.
   *
   * @param aDirectory {@link File} - the directory to be deleted
   * @param aProgressCallback {@link IFileOperationProgressCallback} - progress monitoring callback
   * @return boolean - <code>true</code> if directory was succesfully deleted
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException can not access directory, insufficient rights or any other filesystem error
   */
  public static boolean deleteDirectory( File aDirectory, IFileOperationProgressCallback aProgressCallback ) {
    TsFileUtils.checkDirReadable( aDirectory );
    TsNullArgumentRtException.checkNull( aProgressCallback );
    File[] files = aDirectory.listFiles();
    if( null != files ) {
      for( int i = 0; i < files.length; i++ ) {
        aProgressCallback.onFileCopyProgress( files.length, 0 );
        if( files[i].isDirectory() ) {
          deleteDirectory( files[i], IFileOperationProgressCallback.NULL );
        }
        else {
          files[i].delete();
        }
      }
      aProgressCallback.onFileCopyProgress( files.length, files.length );
    }
    return aDirectory.delete();
  }

  /**
   * Creates the sorted list of the roots directories with subtrees which does not inresects.
   * <p>
   * Non-directory entries, as well as unexisting directories in argument are ignored.
   *
   * @param aDirs {@link IList}&lt;{@link File}&gt; - list of dirs
   * @return {@link IList}&lt;File&gt; - list of the exsting dirs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IList<File> ensureUniqueDirTrees( IList<File> aDirs ) {
    TsNullArgumentRtException.checkNull( aDirs );
    if( aDirs.isEmpty() ) {
      return IList.EMPTY;
    }
    IListBasicEdit<File> result = new SortedElemLinkedBundleList<>();
    // process all elements of the argument
    for( File d : aDirs ) {
      // consider only existing readable dircetories
      if( d.exists() && d.isDirectory() && d.canRead() ) {
        // bypass curr dir if it is child of any resulting dir
        if( internalIsChildOfAnyDir( d, result ) ) {
          continue;
        }
        // if result has childs of curr dir, remove them
        internalRemoveChildsFromList( d, result );
        result.add( d );
      }
    }
    return result;
  }

  private static boolean internalIsChildOfAnyDir( File aChild, IList<File> aDirs ) {
    for( File d : aDirs ) {
      if( isChild( d, aChild ) ) {
        return true;
      }
    }
    return false;
  }

  private static void internalRemoveChildsFromList( File aParentDir, IListBasicEdit<File> aDirs ) {
    for( int i = 0; i < aDirs.size(); i++ ) {
      File d = aDirs.get( i );
      if( isChild( aParentDir, d ) ) {
        aDirs.removeByIndex( i );
      }
    }
  }

  /**
   * No descendants allowed.
   */
  private TsFileUtils() {
    // nop
  }

}
