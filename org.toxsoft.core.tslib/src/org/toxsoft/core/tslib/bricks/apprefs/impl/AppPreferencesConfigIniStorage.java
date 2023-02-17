package org.toxsoft.core.tslib.bricks.apprefs.impl;

import static org.toxsoft.core.tslib.bricks.apprefs.impl.ITsResources.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * App preferences storage to the INI file.
 *
 * @author hazard157
 */
public class AppPreferencesConfigIniStorage
    extends AbstractAppPreferencesStorage {

  private static final char   LINE_COMMENT_CHAR         = ';';
  private static final String BAKUP_FILE_ADDITIONAL_EXT = ".bak"; //$NON-NLS-1$

  private final File    iniFile;
  private final boolean backups;

  private final IStringMapEdit<IOptionSet> prefsMap = new StringMap<>();

  /**
   * Constructor.
   *
   * @param aIniFile {@link File} - INI file
   * @param aCreateBackups boolean - the flag to create backup copies of INI-file
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIoRtException failed {@link TsFileUtils#checkFileWriteable(File)}
   * @throws TsIoRtException error reading INI file
   * @throws StrioRtException invalid file format
   */
  public AppPreferencesConfigIniStorage( File aIniFile, boolean aCreateBackups ) {
    TsFileUtils.checkFileAppendable( aIniFile );
    iniFile = aIniFile;
    backups = aCreateBackups;
    if( aIniFile.exists() ) {
      load();
    }
  }

  /**
   * Constructor with bakups turned on.
   *
   * @param aIniFile {@link File} - INI file
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIoRtException failed {@link TsFileUtils#checkFileWriteable(File)}
   * @throws TsIoRtException error reading INI file
   * @throws StrioRtException invalid file format
   */
  public AppPreferencesConfigIniStorage( File aIniFile ) {
    this( aIniFile, true );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static String readSectionName( IStrioReader aSr ) {
    if( aSr.peekChar( EStrioSkipMode.SKIP_COMMENTS ) == CHAR_EOF ) {
      return null;
    }
    aSr.ensureChar( '[' );
    String id = aSr.readIdPath();
    aSr.ensureChar( ']' );
    if( !aSr.readLine().trim().isEmpty() ) {
      throw new StrioRtException( FMT_ERR_NONEMPTY_AFTER_SECTNAME, id );
    }
    return id;
  }

  private static IOptionSet readSectionContent( IStrioReader aSr ) {
    if( aSr.peekChar( EStrioSkipMode.SKIP_COMMENTS ) == CHAR_EOF ) {
      return IOptionSet.NULL;
    }
    IOptionSetEdit ops = new OptionSet();
    while( true ) {
      char ch = aSr.peekChar( EStrioSkipMode.SKIP_COMMENTS );
      if( ch == '[' || ch == CHAR_EOF ) {
        break;
      }
      String id = aSr.readIdPath();
      aSr.ensureChar( CHAR_EQUAL );
      IAtomicValue val = AtomicValueKeeper.KEEPER.read( aSr );
      ops.setValue( id, val );
      if( !aSr.readLine().trim().isEmpty() ) {
        throw new StrioRtException( FMT_ERR_NONEMPTY_AFTER_VALUE, id );
      }
    }
    return ops;
  }

  private void load() {
    try( ICharInputStreamCloseable chIn = new CharInputStreamFile( iniFile ) ) {
      IStrioReader sr = new StrioReader( chIn );
      sr.setLineCommentChar( LINE_COMMENT_CHAR );
      String sectionName = null;
      IStringMapEdit<IOptionSet> tmpMap = new StringMap<>();
      while( (sectionName = readSectionName( sr )) != null ) {
        IOptionSet params = readSectionContent( sr );
        tmpMap.put( sectionName, params );
      }
      prefsMap.setAll( tmpMap );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      throw ex;
    }
  }

  private void save() {
    // creating backup copy
    if( backups && iniFile.exists() ) {
      File backupFile = new File( iniFile.getParentFile(), iniFile.getName() + BAKUP_FILE_ADDITIONAL_EXT );
      TsFileUtils.copyFile( iniFile, backupFile );
    }
    // writing content
    try( FileWriter fw = new FileWriter( iniFile ) ) {
      ICharOutputStream chOut = new CharOutputStreamAppendable( fw );
      IStrioWriter sw = new StrioWriter( chOut );
      for( String sectionName : prefsMap.keys() ) {
        // [sectionName]
        sw.writeChar( '[' );
        sw.writeAsIs( sectionName );
        sw.writeChar( ']' );
        sw.writeEol();
        // id = value pairs...
        IOptionSet params = prefsMap.getByKey( sectionName );
        for( String id : params.keys() ) {
          IAtomicValue val = params.getValue( id );
          sw.writeAsIs( id );
          sw.writeSpace();
          sw.writeChar( CHAR_EQUAL );
          sw.writeSpace();
          AtomicValueKeeper.KEEPER.write( sw, val );
          sw.writeEol();
        }
        sw.writeEol();
      }
    }
    catch( IOException ex ) {
      LoggerUtils.errorLogger().error( ex );
      throw new TsIoRtException( ex );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      throw ex;
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractAppPreferencesStorage
  //

  @Override
  protected IStringList listBundleIds() {
    return prefsMap.keys();
  }

  @Override
  protected void saveBundle( String aBundleId, IOptionSet aParams ) {
    StridUtils.checkValidIdPath( aBundleId );
    TsNullArgumentRtException.checkNull( aParams );
    prefsMap.put( aBundleId, aParams );
    save();
  }

  @Override
  protected IOptionSet loadBundle( String aBundleId ) {
    return prefsMap.findByKey( aBundleId );
  }

  @Override
  protected void removeBundle( String aAbundleId ) {
    if( prefsMap.removeByKey( aAbundleId ) != null ) {
      save();
    }
  }

}
