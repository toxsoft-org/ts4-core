package org.toxsoft.core.txtproj.lib.storage;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.io.*;
import java.util.*;

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
import org.toxsoft.core.txtproj.lib.tdfile.*;

/**
 * {@link IKeepablesStorage} is implemented as the single file containing all data.
 *
 * @author hazard157
 */
public class KeepablesStorageInFile
    implements IKeepablesStorage {

  private final File                   file;
  private final IStringMapEdit<String> sectionsMap = new StringMap<>();

  /**
   * Constructor.
   *
   * @param aFile {@link File} - the storage file
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException file can be accessed for read-write
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
    sectionsMap.clear();
    try( FileWriter fw = new FileWriter( file ) ) {
      // just open writer and immediately close to clear the file
    }
    catch( IOException ex ) {
      throw new TsIoRtException( ex );
    }
  }

  // ------------------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<TdfSection> iterator() {
    return new Iterator<>() {

      Iterator<String> keyIterator = sectionsMap.keys().iterator();

      @Override
      public TdfSection next() {
        String key = keyIterator.next();
        String content = sectionsMap.getByKey( key );
        return new TdfSection( key, content );
      }

      @Override
      public boolean hasNext() {
        return keyIterator.hasNext();
      }
    };
  }

  // ------------------------------------------------------------------------------------
  // IKeepablesStorage
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
    IStrioReader sr = new StrioReader( new CharInputStreamString( sectionsMap.getByKey( aId ) ) );
    return aKeeper.readEnclosed( sr );
  }

  @Override
  public <T> void writeItem( String aId, T aItem, IEntityKeeper<T> aKeeper ) {
    StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNulls( aItem, aKeeper );
    StringBuilder sb = new StringBuilder();
    IStrioWriter sw = new StrioWriter( new CharOutputStreamAppendable( sb ) );
    aKeeper.writeEnclosed( sw, aItem );
    sectionsMap.put( aId, sb.toString() );
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
  public void writeSection( TdfSection aSection ) {
    TsNullArgumentRtException.checkNull( aSection );
    sectionsMap.put( aSection.keyword(), aSection.getContent() );
    save();
  }

  @Override
  public void removeSection( String aId ) {
    if( sectionsMap.removeByKey( aId ) != null ) {
      save();
    }
  }

  @Override
  public void copyFrom( IKeepablesStorageRo aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    boolean wasChange = false;
    for( TdfSection s : aSource ) {
      String oldContent = sectionsMap.findByKey( s.keyword() );
      if( !Objects.equals( s.getContent(), oldContent ) ) {
        sectionsMap.put( s.keyword(), s.getContent() );
        wasChange = true;
      }
    }
    if( wasChange ) {
      save();
    }
  }

}
