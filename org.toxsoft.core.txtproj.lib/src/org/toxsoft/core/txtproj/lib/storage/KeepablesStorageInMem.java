package org.toxsoft.core.txtproj.lib.storage;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.util.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.tdfile.*;

/**
 * {@link IKeepablesStorage} is implemented as a storage in the memory (RAM).
 * <p>
 * However in-memory data may be saved and loaded by yje {@link #KEEPER}.
 *
 * @author hazard157
 */
public class KeepablesStorageInMem
    implements IKeepablesStorage {

  /**
   * Keeper singleton.
   * <p>
   * Read value may be safely cast to {@link KeepablesStorageInMem}.
   */
  public static final IEntityKeeper<IKeepablesStorageRo> KEEPER =
      new AbstractEntityKeeper<>( IKeepablesStorageRo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IKeepablesStorageRo aEntity ) {
          for( TdfSection s : aEntity ) {
            aSw.writeAsIs( s.keyword() );
            aSw.writeSpace();
            aSw.writeChar( CHAR_EQUAL );
            aSw.incNewLine();
            aSw.writeAsIs( s.getContent() );
            aSw.decNewLine();
          }
        }

        @Override
        protected IKeepablesStorageRo doRead( IStrioReader aSr ) {
          KeepablesStorageInMem ks = new KeepablesStorageInMem();
          while( aSr.peekChar( EStrioSkipMode.SKIP_COMMENTS ) != CHAR_EOF ) {
            String keywrod = aSr.readIdPath();
            aSr.ensureChar( CHAR_EQUAL );
            String content = StrioUtils.readInterbaceContent( aSr );
            ks.writeSection( new TdfSection( keywrod, content ) );
          }
          return ks;
        }
      };

  private final IStringMapEdit<String> sectionsMap = new StringMap<>();

  /**
   * Constructor.
   */
  public KeepablesStorageInMem() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  public void clear() {
    sectionsMap.clear();
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
  public <T> void writeColl( String aId, ITsCollection<T> aColl, IEntityKeeper<T> aKeeper, boolean aIndented ) {
    StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNulls( aColl, aKeeper );
    String content = aKeeper.coll2str( aColl, aIndented );
    sectionsMap.put( aId, content );
  }

  @Override
  public <T> IStringMap<T> readStridMap( String aId, IEntityKeeper<T> aKeeper ) {
    TsNullArgumentRtException.checkNulls( aId, aKeeper );
    if( !sectionsMap.hasKey( aId ) ) {
      return IStringMap.EMPTY;
    }
    return aKeeper.str2idmap( sectionsMap.getByKey( aId ) );
  }

  @Override
  public <T> IStringMap<T> readStringMap( String aId, IEntityKeeper<T> aKeeper ) {
    TsNullArgumentRtException.checkNulls( aId, aKeeper );
    if( !sectionsMap.hasKey( aId ) ) {
      return IStringMap.EMPTY;
    }
    return aKeeper.str2strmap( sectionsMap.getByKey( aId ) );
  }

  @Override
  public <T> void writeStridMap( String aId, IStringMap<T> aMap, IEntityKeeper<T> aKeeper, boolean aIndented ) {
    StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNulls( aMap, aKeeper );
    String content = aKeeper.idmap2str( aMap, aIndented );
    sectionsMap.put( aId, content );
  }

  @Override
  public <T> void writeStringMap( String aId, IStringMap<T> aMap, IEntityKeeper<T> aKeeper, boolean aIndented ) {
    StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNulls( aMap, aKeeper );
    String content = aKeeper.strmap2str( aMap, aIndented );
    sectionsMap.put( aId, content );
  }

  @Override
  public void writeSection( TdfSection aSection ) {
    TsNullArgumentRtException.checkNull( aSection );
    sectionsMap.put( aSection.keyword(), aSection.getContent() );
  }

  @Override
  public void removeSection( String aId ) {
    sectionsMap.removeByKey( aId );
  }

  @Override
  public void copyFrom( IKeepablesStorageRo aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    for( TdfSection s : aSource ) {
      String oldContent = sectionsMap.findByKey( s.keyword() );
      if( !Objects.equals( s.getContent(), oldContent ) ) {
        sectionsMap.put( s.keyword(), s.getContent() );
      }
    }
  }

}
