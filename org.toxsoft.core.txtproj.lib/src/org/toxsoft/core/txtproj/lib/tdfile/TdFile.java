package org.toxsoft.core.txtproj.lib.tdfile;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITdFile} implemetation.
 *
 * @author hazard157
 */
public class TdFile
    implements ITdFile {

  private final GenericChangeEventer       eventer;
  private final IStringMapEdit<TdfSection> sectionsMap = new StringMap<>();

  /**
   * Пустой конструктор.
   */
  public TdFile() {
    eventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // IKeepableEntity
  //

  @Override
  public void write( IStrioWriter aSw ) {
    TsNullArgumentRtException.checkNull( aSw );
    for( TdfSection s : sectionsMap ) {
      aSw.writeAsIs( s.keyword() );
      aSw.writeSpace();
      aSw.writeChar( CHAR_EQUAL );
      aSw.writeSpace();
      aSw.writeAsIs( s.getContent() );
      aSw.writeEol();
    }
  }

  @Override
  public void read( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    IListEdit<TdfSection> list = new ElemLinkedBundleList<>();
    while( aSr.peekChar( EStrioSkipMode.SKIP_COMMENTS ) != CHAR_EOF ) {
      String keywrod = aSr.readIdPath();
      aSr.ensureChar( CHAR_EQUAL );
      String content = StrioUtils.readInterbaceContent( aSr );
      list.add( new TdfSection( keywrod, content ) );
    }
    sectionsMap.clear();
    for( TdfSection s : list ) {
      s.eventer().addListener( eventer );
      sectionsMap.put( s.keyword(), s );
    }
    eventer.fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // ITdFile
  //

  @Override
  public IStringMap<TdfSection> sections() {
    return sectionsMap;
  }

  @Override
  public void add( TdfSection aSection ) {
    TsItemAlreadyExistsRtException.checkTrue( sectionsMap.values().hasElem( aSection ) );
    TsItemAlreadyExistsRtException.checkTrue( sectionsMap.hasKey( aSection.keyword() ) );
    aSection.eventer().addListener( eventer );
    sectionsMap.put( aSection.keyword(), aSection );
    eventer.fireChangeEvent();
  }

  @Override
  public void setAll( ITsCollection<TdfSection> aSections ) {
    TsNullArgumentRtException.checkNull( aSections );
    sectionsMap.clear();
    for( TdfSection s : aSections ) {
      sectionsMap.put( s.keyword(), s );
    }
    eventer.fireChangeEvent();
  }

  @Override
  public void remove( String aKeyword ) {
    TdfSection s = sectionsMap.removeByKey( aKeyword );
    if( s != null ) {
      s.eventer().removeListener( eventer );
      eventer.fireChangeEvent();
    }
  }

  @Override
  public IGenericChangeEventer eventer() {
    return eventer;
  }

  @Override
  public void clear() {
    if( !sectionsMap.isEmpty() ) {
      sectionsMap.clear();
      eventer.fireChangeEvent();
    }
  }

}
