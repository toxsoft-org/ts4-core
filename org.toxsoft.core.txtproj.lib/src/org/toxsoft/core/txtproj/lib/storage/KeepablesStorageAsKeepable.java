package org.toxsoft.core.txtproj.lib.storage;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IKeepablesStorage} implemented as the {@link IKeepableEntity}.
 * <p>
 * Note: {@link #write(IStrioWriter)} writes indented content enclosed in braces
 * {@link IStrioHardConstants#CHAR_SET_BEGIN} and {@link IStrioHardConstants#CHAR_SET_END}.
 *
 * @author hazard157
 */
public class KeepablesStorageAsKeepable
    implements IKeepablesStorage, IKeepableEntity {

  private final IStringMapEdit<String> sectionsMap = new StringMap<>();

  /**
   * Constructor.
   */
  public KeepablesStorageAsKeepable() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IKeepableEntity
  //

  @Override
  public void read( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    IStringMapEdit<String> map = new StringMap<>();
    aSr.ensureChar( CHAR_SET_BEGIN );
    while( aSr.peekChar( EStrioSkipMode.SKIP_COMMENTS ) != CHAR_EOF ) {
      String keywrod = aSr.readIdPath();
      aSr.ensureChar( CHAR_EQUAL );
      String content = StrioUtils.readInterbaceContent( aSr );
      map.put( keywrod, content );
    }
    aSr.ensureChar( CHAR_SET_END );
    sectionsMap.putAll( map );
  }

  @Override
  public void write( IStrioWriter aSw ) {
    TsNullArgumentRtException.checkNull( aSw );
    aSw.writeChar( CHAR_SET_BEGIN );
    if( sectionsMap.isEmpty() ) {
      aSw.writeChar( CHAR_SET_END );
      return;
    }
    aSw.incNewLine();
    for( String keyword : sectionsMap.keys() ) {
      aSw.writeAsIs( keyword );
      aSw.writeSpace();
      aSw.writeChar( CHAR_EQUAL );
      aSw.incNewLine();
      aSw.writeAsIs( sectionsMap.getByKey( keyword ) );
      aSw.decNewLine();
    }
    aSw.decNewLine();
    aSw.writeChar( CHAR_SET_END );
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  public void clear() {
    sectionsMap.clear();
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
  }

  @Override
  public void removeSection( String aId ) {
    sectionsMap.removeByKey( aId );
  }

}
