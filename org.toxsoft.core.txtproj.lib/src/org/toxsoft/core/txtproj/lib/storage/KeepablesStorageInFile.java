package org.toxsoft.core.txtproj.lib.storage;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

/**
 * Хранит все раздели хранилища в одном файле.
 *
 * @author hazard157
 */
public class KeepablesStorageInFile
    implements IKeepablesStorage {

  private final File                   file;
  private final IStringMapEdit<String> sectionsMap = new StringMap<>();

  /**
   * Создает хранилище.
   *
   * @param aFile {@link File} - файл для хранения
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public KeepablesStorageInFile( File aFile ) {
    file = TsFileUtils.checkFileAppendable( aFile );
    if( file.exists() ) {
      load();
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void load() {
    try( ICharInputStreamCloseable chIn = new CharInputStreamFile( file ) ) {
      IStrioReader sr = new StrioReader( chIn );
      IStringMapEdit<String> map = new StringMap<>();
      while( sr.peekChar( EStrioSkipMode.SKIP_COMMENTS ) != CHAR_EOF ) {
        String keywrod = sr.readIdPath();
        sr.ensureChar( CHAR_EQUAL );
        String content = StrioUtils.readInterbaceContent( sr );
        map.put( keywrod, content );
      }
      sectionsMap.putAll( map );
    }
  }

  private void save() {
    try( FileWriter fw = new FileWriter( file ) ) {
      ICharOutputStream chOut = new CharOutputStreamWriter( fw );
      IStrioWriter sw = new StrioWriter( chOut );
      for( String keyword : sectionsMap.keys() ) {
        sw.writeAsIs( keyword );
        sw.writeSpace();
        sw.writeChar( CHAR_EQUAL );
        sw.incNewLine();
        sw.writeAsIs( sectionsMap.getByKey( keyword ) );
        sw.decNewLine();
      }
    }
    catch( IOException ex ) {
      throw new TsIoRtException( ex );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  public void clear() {
    try( FileWriter fw = new FileWriter( file ) ) {
      // просто откроем и закроем
    }
    catch( IOException ex ) {
      throw new TsIoRtException( ex );
    }
  }

  // ------------------------------------------------------------------------------------
  // IStorage
  //

  @Override
  public boolean hasSection( String aId ) {
    return sectionsMap.hasKey( aId );
  }

  @Override
  public <T> T readItem( String aId, IEntityKeeper<T> aKeeper, T aDefault ) {
    TsNullArgumentRtException.checkNulls( aId, aKeeper );
    if( !sectionsMap.hasKey( aId ) ) {
      return aDefault;
    }
    return aKeeper.str2ent( sectionsMap.getByKey( aId ) );
  }

  @Override
  public <T> void writeItem( String aId, T aItem, IEntityKeeper<T> aKeeper ) {
    StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNulls( aItem, aKeeper );
    String content = aKeeper.ent2str( aItem );
    sectionsMap.put( aId, content );
    save();
  }

  @Override
  public <T> IList<T> readColl( String aId, IEntityKeeper<T> aKeeper ) {
    TsNullArgumentRtException.checkNulls( aId, aKeeper );
    if( !sectionsMap.hasKey( aId ) ) {
      return IList.EMPTY;
    }
    return aKeeper.str2coll( sectionsMap.getByKey( aId ) );
  }

  @Override
  public <T> void writeColl( String aId, ITsCollection<T> aColl, IEntityKeeper<T> aKeeper ) {
    StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNulls( aColl, aKeeper );
    String content = aKeeper.coll2str( aColl );
    sectionsMap.put( aId, content );
    save();
  }

  @Override
  public void removeSection( String aId ) {
    if( sectionsMap.removeByKey( aId ) != null ) {
      save();
    }
  }

}
