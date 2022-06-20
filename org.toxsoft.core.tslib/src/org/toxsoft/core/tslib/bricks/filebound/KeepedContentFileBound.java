package org.toxsoft.core.tslib.bricks.filebound;

import static org.toxsoft.core.tslib.bricks.filebound.IKeepedContentFileBoundConstants.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

/**
 * {@link IKeepedContentFileBound} implementation.
 *
 * @author hazard157
 */
public class KeepedContentFileBound
    implements IKeepedContentFileBound {

  /**
   * {@link KeepedContentFileBound#svs()} implemenation class.
   *
   * @author hazard157
   */
  static class ValidationSupport
      extends AbstractTsValidationSupport<IKeepedContentFileBoundValidator>
      implements IKeepedContentFileBoundValidator {

    @Override
    public IKeepedContentFileBoundValidator validator() {
      return this;
    }

    @Override
    public ValidationResult canClear( IKeepedContentFileBound aSource ) {
      ValidationResult vr = ValidationResult.SUCCESS;
      for( IKeepedContentFileBoundValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canClear( aSource ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canOpen( IKeepedContentFileBound aSource, File aFile ) {
      ValidationResult vr = ValidationResult.SUCCESS;
      for( IKeepedContentFileBoundValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canOpen( aSource, aFile ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canSave( IKeepedContentFileBound aSource ) {
      ValidationResult vr = ValidationResult.SUCCESS;
      for( IKeepedContentFileBoundValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canSave( aSource ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canSaveAs( IKeepedContentFileBound aSource, File aFile ) {
      ValidationResult vr = ValidationResult.SUCCESS;
      for( IKeepedContentFileBoundValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canSaveAs( aSource, aFile ) );
      }
      return vr;
    }

  }

  /**
   * Content change ia user editing listener only updates altered state.
   */
  private final IGenericChangeListener contentsChangeListener = aSource -> setWasAltered( true );

  /**
   * Used to make backup file names.
   */
  private static final Calendar calendar = Calendar.getInstance();

  /**
   * Bound configuration parameters from {@link IKeepedContentFileBoundConstants}.
   */
  private final IOptionSetEdit params = new OptionSet();

  private final ValidationSupport    validationSupport = new ValidationSupport();
  private final GenericChangeEventer eventer;

  /**
   * The bound content.
   */
  private final IKeepedContent content;

  /**
   * The bound file or <code>null</code> if content inot bound to file.
   */
  private File file = null;

  /**
   * Признак, что содержимое проекта изменилась и расходится с содержимым файла.
   */
  private boolean wasAltered = false;

  /**
   * Constructor.
   *
   * @param aContent {@link IKeepedContent} - the content
   * @param aParams {@link IOptionSet} - optional paremetrs from {@link IKeepedContentFileBoundConstants}
   */
  public KeepedContentFileBound( IKeepedContent aContent, IOptionSet aParams ) {
    TsNullArgumentRtException.checkNulls( aContent, aParams );
    eventer = new GenericChangeEventer( this );
    content = aContent;
    for( IDataDef opin : ALL_PARAMS ) {
      params.setValue( opin, opin.defaultValue() );
    }
    params.addAll( aParams );
    content.genericChangeEventer().addListener( contentsChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Creates absolute path to the back file for current time.
   *
   * @return {@link File} - an absolute path to backup file
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

  void setWasAltered( boolean aValue ) {
    if( wasAltered != aValue ) {
      wasAltered = aValue;
      eventer.fireChangeEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    content.genericChangeEventer().removeListener( contentsChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsContentFileBound
  //

  @Override
  public IKeepedContent content() {
    return content;
  }

  @Override
  public void clear() {
    TsValidationFailedRtException.checkError( validationSupport.canClear( this ) );
    try {
      content.genericChangeEventer().muteListener( contentsChangeListener );
      doBeforeClear();
      content.clear();
      doAfterClear();
      file = null;
      wasAltered = false;
      eventer.fireChangeEvent();
    }
    finally {
      content.genericChangeEventer().unmuteListener( contentsChangeListener );
    }
  }

  @Override
  public void open( File aFile ) {
    TsNullArgumentRtException.checkNull( aFile );
    TsValidationFailedRtException.checkError( validationSupport.canOpen( this, aFile ) );
    try( ICharInputStreamCloseable chIn = new CharInputStreamFile( aFile ) ) {
      IStrioReader dr = new StrioReader( chIn );
      content.genericChangeEventer().muteListener( contentsChangeListener );
      doBeforeOpen( aFile );
      content.read( dr );
      doAfterOpen( aFile );
      file = aFile;
      wasAltered = false;
      eventer.fireChangeEvent();
    }
    finally {
      content.genericChangeEventer().unmuteListener( contentsChangeListener );
    }
  }

  @Override
  public void save() {
    TsIllegalStateRtException.checkNull( file );
    TsValidationFailedRtException.checkError( validationSupport.canSave( this ) );
    createBackupIfNeeded();
    try( ICharOutputStreamCloseable chOut = new CharOutputStreamFile( file ) ) {
      IStrioWriter sw = new StrioWriter( chOut );
      content.genericChangeEventer().muteListener( contentsChangeListener );
      doBeforeSave();
      content.write( sw );
      doAfterSave();
      eventer.fireChangeEvent();
    }
    finally {
      content.genericChangeEventer().unmuteListener( contentsChangeListener );
    }
  }

  @Override
  public void saveAs( File aFile ) {
    TsFileUtils.checkFileAppendable( aFile );
    TsValidationFailedRtException.checkError( validationSupport.canSaveAs( this, aFile ) );
    createBackupIfNeeded();
    try( ICharOutputStreamCloseable chOut = new CharOutputStreamFile( file ) ) {
      IStrioWriter sw = new StrioWriter( chOut );
      content.genericChangeEventer().muteListener( contentsChangeListener );
      doBeforeSaveAs( file );
      content.write( sw );
      doAfterSaveAs( file );
      file = aFile;
      eventer.fireChangeEvent();
    }
    finally {
      content.genericChangeEventer().unmuteListener( contentsChangeListener );
    }
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
      eventer.fireChangeEvent();
    }
  }

  @Override
  public File file() {
    return file;
  }

  @Override
  public ITsValidationSupport<IKeepedContentFileBoundValidator> svs() {
    return validationSupport;
  }

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Called just before content will be cleared (reset) in {@link #clear()}.
   */
  protected void doBeforeClear() {
    // nop
  }

  /**
   * Called immediately after content was cleared (reset) in {@link #clear()}.
   */
  protected void doAfterClear() {
    // nop
  }

  /**
   * Called just before content will be loaded from file in {@link #open(File)}.
   *
   * @param aFile {@link File} - the file
   */
  protected void doBeforeOpen( File aFile ) {
    // nop
  }

  /**
   * Called immediately after content was loaded from file in {@link #open(File)}.
   *
   * @param aFile {@link File} - the file
   */
  protected void doAfterOpen( File aFile ) {
    // nop
  }

  /**
   * Called just before content will be written to file in {@link #save()}.
   */
  protected void doBeforeSave() {
    // nop
  }

  /**
   * Called immediately after content was written to file in {@link #save()}.
   */
  protected void doAfterSave() {
    // nop
  }

  /**
   * Called just before content will be written to file in {@link #saveAs(File)}.
   *
   * @param aFile {@link File} - the file
   */
  protected void doBeforeSaveAs( File aFile ) {
    // nop
  }

  /**
   * Called immediately after content was written to file in {@link #saveAs(File)}.
   *
   * @param aFile {@link File} - the file
   */
  protected void doAfterSaveAs( File aFile ) {
    // nop
  }

}
