package org.toxsoft.core.txtproj.lib.bound;

import static org.toxsoft.core.txtproj.lib.bound.ITsProjectFileBoundParams.*;

import java.io.*;
import java.util.Calendar;

import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.bricks.strio.chario.ICharInputStreamCloseable;
import org.toxsoft.core.tslib.bricks.strio.chario.ICharOutputStream;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.CharInputStreamFile;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.CharOutputStreamWriter;
import org.toxsoft.core.tslib.bricks.strio.impl.StrioReader;
import org.toxsoft.core.tslib.bricks.strio.impl.StrioWriter;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.TsFileUtils;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.core.txtproj.lib.ITsProject;
import org.toxsoft.core.txtproj.lib.ITsProjectContentChangeListener;

/**
 * {@link ITsProjectFileBound} implementation.
 *
 * @author hazard157
 */
public class TsProjectFileBound
    implements ITsProjectFileBound {

  // TODO TRANSLATE

  /**
   * Слушаем изменения в содержимом проекта и вытсавим признак {@link #isAltered()}.
   */
  private final ITsProjectContentChangeListener projectContentsChangeListener = new ITsProjectContentChangeListener() {

    @Override
    public void onContentChanged( ITsProject aSource, boolean aCleared ) {
      if( aCleared ) {
        fireAfterClear();
      }
      setWasAltered( true );
    }
  };

  /**
   * Выставление признака расхождение проекта с файлом при правке проекта пользователем.
   */
  private final IGenericChangeListener projectContentChangeListener = aSource -> setWasAltered( true );

  /**
   * Календарь для формирования имен резервных копии.
   */
  private static final Calendar calendar = Calendar.getInstance();

  private final IListEdit<ITsProjectFileBoundListener> listeners = new ElemArrayList<>();

  private final IOptionSetEdit params = new OptionSet();
  private final ITsProject     project;

  /**
   * Файл, в котором храниться проект, или <code>null</code>, если привязка проекта к файлу не задана.
   */
  private File file = null;

  /**
   * Признак, что содержимое проекта изменилась и расходится с содержимым файла.
   */
  private boolean wasAltered = false;

  /**
   * Конструктор.
   *
   * @param aProject {@link ITsProject} - проект
   * @param aParams {@link IOptionSet} - опциональные параметры привязки из {@link ITsProjectFileBoundParams}
   */
  public TsProjectFileBound( ITsProject aProject, IOptionSet aParams ) {
    TsNullArgumentRtException.checkNulls( aProject, aParams );
    project = aProject;
    for( IDataDef opin : ALL_PARAMS ) {
      params.setValue( opin, opin.defaultValue() );
    }
    params.addAll( aParams );
    project.addProjectContentChangeListener( projectContentsChangeListener );
    project.genericChangeEventer().addListener( projectContentChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  /**
   * Создает абсолютный путь к файлу резервной копии для данного момента.
   *
   * @return {@link File} - абсолютный путь к файлу резервной копии
   */
  private File makeBackupFilePath() {
    TsInternalErrorRtException.checkNull( file );
    String dir = TsFileUtils.ensureEndingSeparator( file.getAbsoluteFile().getParent() );
    String backupsSubdir = params.getStr( OPDEF_BACKUP_FILES_SUBDIR_NAME );
    if( !backupsSubdir.isEmpty() ) {
      backupsSubdir = TsFileUtils.ensureEndingSeparator( backupsSubdir );
    }
    File backupDir = new File( dir + backupsSubdir );
    if( !backupDir.exists() ) {
      backupDir.mkdir();
    }
    String bareName = TsFileUtils.extractBareFileName( file.getAbsolutePath() );
    calendar.setTimeInMillis( System.currentTimeMillis() );
    String ext = OPDEF_BACKUP_FILE_EXT.getValue( params ).asString();
    String backupFileName = String.format( "%s.%04d-%02d-%02d_%02d-%02d-%02d.%s", bareName, //$NON-NLS-1$
        Integer.valueOf( calendar.get( Calendar.YEAR ) ), //
        Integer.valueOf( calendar.get( Calendar.MONTH ) + 1 ), //
        Integer.valueOf( calendar.get( Calendar.DAY_OF_MONTH ) ), //
        Integer.valueOf( calendar.get( Calendar.HOUR_OF_DAY ) ), //
        Integer.valueOf( calendar.get( Calendar.MINUTE ) ), //
        Integer.valueOf( calendar.get( Calendar.SECOND ) ), //
        ext //
    );
    return new File( backupDir, backupFileName );
  }

  /**
   * Creates backup file if needed.
   * <p>
   * If backups are enabled and {@link #file} is bound, copies {@link #file} to the {@link #makeBackupFilePath()}.
   */
  private void createBackupIfNeeded() {
    if( file != null && OPDEF_IS_AUTO_BACKUPS_ENABLED.getValue( params ).asBool() ) {
      File backupFilePath = makeBackupFilePath();
      backupFilePath.getParentFile().mkdirs();
      TsFileUtils.copyFile( file, backupFilePath );
    }
  }

  private boolean isListenerErrorsThrown() {
    return OPDEF_IS_LISTENER_ERRORS_THROWN.getValue( params ).asBool();
  }

  private ValidationResult askBeforeOpen( File aFile ) {
    ValidationResult vr = ValidationResult.SUCCESS;
    if( !listeners.isEmpty() ) {
      IList<ITsProjectFileBoundListener> ll = new ElemArrayList<>( listeners );
      for( ITsProjectFileBoundListener l : ll ) {
        try {
          vr = ValidationResult.firstNonOk( vr, l.beforeOpen( this, aFile ) );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
          if( isListenerErrorsThrown() ) {
            throw ex;
          }
        }
        if( vr.isError() ) {
          break;
        }
      }
    }
    return vr;
  }

  private void fireAfterOpen() {
    if( !listeners.isEmpty() ) {
      IList<ITsProjectFileBoundListener> ll = new ElemArrayList<>( listeners );
      for( ITsProjectFileBoundListener l : ll ) {
        try {
          l.afterOpen( this );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
          if( isListenerErrorsThrown() ) {
            throw ex;
          }
        }
      }
    }
  }

  private ValidationResult askBeforeSave() {
    ValidationResult vr = ValidationResult.SUCCESS;
    if( !listeners.isEmpty() ) {
      IList<ITsProjectFileBoundListener> ll = new ElemArrayList<>( listeners );
      for( ITsProjectFileBoundListener l : ll ) {
        try {
          vr = ValidationResult.firstNonOk( vr, l.beforeSave( this ) );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
          if( isListenerErrorsThrown() ) {
            throw ex;
          }
        }
        if( vr.isError() ) {
          break;
        }
      }
    }
    return vr;
  }

  private void fireAfterSave() {
    if( !listeners.isEmpty() ) {
      IList<ITsProjectFileBoundListener> ll = new ElemArrayList<>( listeners );
      for( ITsProjectFileBoundListener l : ll ) {
        try {
          l.afterSave( this );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
          if( isListenerErrorsThrown() ) {
            throw ex;
          }
        }
      }
    }
  }

  private ValidationResult askBeforeSaveAs( File aFile ) {
    ValidationResult vr = ValidationResult.SUCCESS;
    if( !listeners.isEmpty() ) {
      IList<ITsProjectFileBoundListener> ll = new ElemArrayList<>( listeners );
      for( ITsProjectFileBoundListener l : ll ) {
        try {
          vr = ValidationResult.firstNonOk( vr, l.beforeSaveAs( this, aFile ) );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
          if( isListenerErrorsThrown() ) {
            throw ex;
          }
        }
        if( vr.isError() ) {
          break;
        }
      }
    }
    return vr;
  }

  private void fireAfterSaveAs() {
    if( !listeners.isEmpty() ) {
      IList<ITsProjectFileBoundListener> ll = new ElemArrayList<>( listeners );
      for( ITsProjectFileBoundListener l : ll ) {
        try {
          l.afterSaveAs( this );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
          if( isListenerErrorsThrown() ) {
            throw ex;
          }
        }
      }
    }
  }

  private ValidationResult askBeforeClear() {
    ValidationResult vr = ValidationResult.SUCCESS;
    if( !listeners.isEmpty() ) {
      IList<ITsProjectFileBoundListener> ll = new ElemArrayList<>( listeners );
      for( ITsProjectFileBoundListener l : ll ) {
        try {
          vr = ValidationResult.firstNonOk( vr, l.beforeClear( this ) );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
          if( isListenerErrorsThrown() ) {
            throw ex;
          }
        }
        if( vr.isError() ) {
          break;
        }
      }
    }
    return vr;
  }

  void fireAfterClear() {
    if( !listeners.isEmpty() ) {
      IList<ITsProjectFileBoundListener> ll = new ElemArrayList<>( listeners );
      for( ITsProjectFileBoundListener l : ll ) {
        try {
          l.afterClear( this );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
          if( isListenerErrorsThrown() ) {
            throw ex;
          }
        }
      }
    }
  }

  private void fireAlteredStateChangeEvent() {
    if( !listeners.isEmpty() ) {
      IList<ITsProjectFileBoundListener> ll = new ElemArrayList<>( listeners );
      for( ITsProjectFileBoundListener l : ll ) {
        try {
          l.onAlteredStateChanged( this );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
          if( isListenerErrorsThrown() ) {
            throw ex;
          }
        }
      }
    }
  }

  private void fireFileStateChangeEvent() {
    if( !listeners.isEmpty() ) {
      IList<ITsProjectFileBoundListener> ll = new ElemArrayList<>( listeners );
      for( ITsProjectFileBoundListener l : ll ) {
        try {
          l.onFileBindingChanged( this );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
          if( isListenerErrorsThrown() ) {
            throw ex;
          }
        }
      }
    }
  }

  void setWasAltered( boolean aValue ) {
    if( wasAltered == aValue ) {
      return;
    }
    wasAltered = aValue;
    fireAlteredStateChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // ITsProjectFileBound
  //

  @Override
  public ITsProject project() {
    return project;
  }

  @Override
  public void open( File aFile ) {
    TsNullArgumentRtException.checkNull( aFile );
    TsValidationFailedRtException.checkError( askBeforeOpen( aFile ) );
    try( ICharInputStreamCloseable chIn = new CharInputStreamFile( aFile ) ) {
      IStrioReader dr = new StrioReader( chIn );
      file = aFile; // на всякий случай, ПЕРЕД чтением, чтобы знать из какого файла (по какому пути) читаем
      project.read( dr );
    }
    catch( Exception e ) {
      file = null;
      throw e;
    }
    finally {
      try {
        setWasAltered( false );
        fireFileStateChangeEvent();
      }
      catch( Exception e1 ) {
        e1.printStackTrace();
      }
    }
    fireAfterOpen();
  }

  @Override
  public void save() {
    TsIllegalStateRtException.checkNull( file );
    TsValidationFailedRtException.checkError( askBeforeSave() );
    createBackupIfNeeded();
    try( FileWriter fw = new FileWriter( file ) ) {
      ICharOutputStream chOut = new CharOutputStreamWriter( fw );
      IStrioWriter sw = new StrioWriter( chOut );
      project.write( sw );
      setWasAltered( false );
    }
    catch( IOException e ) {
      throw new TsIoRtException( e );
    }
    fireAfterSave();
  }

  @Override
  public void saveAs( File aFile ) {
    TsFileUtils.checkFileAppendable( aFile );
    TsValidationFailedRtException.checkError( askBeforeSaveAs( aFile ) );
    createBackupIfNeeded();
    try( FileWriter fw = new FileWriter( aFile ) ) {
      ICharOutputStream chOut = new CharOutputStreamWriter( fw );
      IStrioWriter sw = new StrioWriter( chOut );
      project.write( sw );
      file = aFile;
    }
    catch( IOException e ) {
      throw new TsIoRtException( e );
    }
    finally {
      try {
        setWasAltered( false );
        fireFileStateChangeEvent();
      }
      catch( Exception e1 ) {
        e1.printStackTrace();
      }
    }
    fireAfterSaveAs();
  }

  @Override
  public boolean isAltered() {
    return wasAltered;
  }

  @Override
  public void resetAlteredState() {
    setWasAltered( false );
  }

  @Override
  public boolean hasFileBound() {
    return file != null;
  }

  @Override
  public void resetFileBound() {
    if( file != null ) {
      file = null;
      fireFileStateChangeEvent();
    }
  }

  @Override
  public File getFile() {
    return file;
  }

  @Override
  public void newContent() {
    TsValidationFailedRtException.checkError( askBeforeClear() );
    resetFileBound();
    project.clear();
    // нижеприведенный код вызывается из слушателя projectContentsChangeListener
    // fireAfterClear();
    // setWasAltered( false );
  }

  @Override
  public void addTsProjectFileBoundListener( ITsProjectFileBoundListener aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeTsProjectFileBoundListener( ITsProjectFileBoundListener aListener ) {
    listeners.remove( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    project.genericChangeEventer().removeListener( projectContentChangeListener );
    project.removeProjectContentChangeListener( projectContentsChangeListener );
  }

}
