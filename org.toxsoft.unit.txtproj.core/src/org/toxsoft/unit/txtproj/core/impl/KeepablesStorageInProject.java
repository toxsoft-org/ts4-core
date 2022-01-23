package org.toxsoft.unit.txtproj.core.impl;

import static org.toxsoft.tslib.bricks.strio.IStrioHardConstants.*;

import java.util.Objects;

import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.tslib.bricks.strio.*;
import org.toxsoft.tslib.bricks.strio.impl.StrioUtils;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.basis.ITsCollection;
import org.toxsoft.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.unit.txtproj.core.IProjDataUnit;
import org.toxsoft.unit.txtproj.core.storage.IKeepablesStorage;

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
  // Реализация методов AbstractProjDataUnit
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
      genericChangeEventer.fireChangeEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса
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
      genericChangeEventer.fireChangeEvent();
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
      genericChangeEventer.fireChangeEvent();
    }
  }

  @Override
  public void removeSection( String aId ) {
    if( sectionsMap.removeByKey( aId ) != null ) {
      genericChangeEventer.fireChangeEvent();
    }
  }

}
