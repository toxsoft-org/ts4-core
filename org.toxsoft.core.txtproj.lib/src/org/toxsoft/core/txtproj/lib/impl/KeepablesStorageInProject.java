package org.toxsoft.core.txtproj.lib.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.util.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.*;
import org.toxsoft.core.txtproj.lib.storage.*;
import org.toxsoft.core.txtproj.lib.tdfile.*;

/**
 * Реализация хранилище {@link IKeepablesStorage} как компоненты проекта {@link IProjDataUnit}.
 *
 * @author hazard157
 */
public class KeepablesStorageInProject
    extends AbstractProjDataUnit
    implements IKeepablesStorage {

  // OPTIMIZE здесь текстовое содержимое хранится в памяти, но оно же хранится в TdFile, как-то избежать этого?

  private final IStringMapEdit<String> sectionsMap = new StringMap<>();

  /**
   * Конструктор.
   */
  public KeepablesStorageInProject() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // AbstractProjDataUnit
  //

  @Override
  protected void doWrite( IStrioWriter aSw ) {
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
      aSw.writeSpace();
      aSw.writeAsIs( sectionsMap.getByKey( keyword ) );
      aSw.writeEol();
    }
    aSw.decNewLine();
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  protected void doRead( IStrioReader aSr ) {
    aSr.ensureChar( CHAR_SET_BEGIN );
    IStringMapEdit<String> map = new StringMap<>();
    while( aSr.peekChar( EStrioSkipMode.SKIP_COMMENTS ) != CHAR_SET_END ) {
      String keywrod = aSr.readIdPath();
      aSr.ensureChar( CHAR_EQUAL );
      String content = StrioUtils.readInterbaceContent( aSr );
      map.put( keywrod, content );
    }
    sectionsMap.putAll( map );
    aSr.ensureChar( CHAR_SET_END );
  }

  @Override
  protected void doClear() {
    if( !sectionsMap.isEmpty() ) {
      sectionsMap.clear();
      genericChangeEventer().fireChangeEvent();
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
    return aKeeper.str2ent( sectionsMap.getByKey( aId ) );
  }

  @Override
  public <T> void writeItem( String aId, T aItem, IEntityKeeper<T> aKeeper ) {
    StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNulls( aItem, aKeeper );
    String content = aKeeper.ent2str( aItem );
    if( !Objects.equals( sectionsMap.findByKey( aId ), content ) ) {
      sectionsMap.put( aId, content );
      genericChangeEventer().fireChangeEvent();
    }
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
    if( !Objects.equals( sectionsMap.findByKey( aId ), content ) ) {
      sectionsMap.put( aId, content );
      genericChangeEventer().fireChangeEvent();
    }
  }

  @Override
  public void writeSection( TdfSection aSection ) {
    TsNullArgumentRtException.checkNull( aSection );
    String oldContent = sectionsMap.findByKey( aSection.keyword() );
    if( !Objects.equals( aSection.getContent(), oldContent ) ) {
      sectionsMap.put( aSection.keyword(), aSection.getContent() );
      genericChangeEventer().fireChangeEvent();
    }
  }

  @Override
  public void removeSection( String aId ) {
    if( sectionsMap.removeByKey( aId ) != null ) {
      genericChangeEventer().fireChangeEvent();
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
      genericChangeEventer().fireChangeEvent();
    }
  }

}
