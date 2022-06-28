package org.toxsoft.core.tsgui.ved.incub.secdata;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Arbitrary data sections to be stored as textual data using KTOR {@link IEntityKeeper} format.
 * <p>
 * It is simple identified Strings of sections content as defined by
 * {@link StrioUtils#readInterbaceContent(IStrioReader)}.
 * <p>
 * Note: contaner may hold several sections with the same ID.
 *
 * @author hazard157
 */
public class RawSectionedDataContainer
    implements IKeepableEntity, ITsClearable {

  private final IListEdit<RawSectionData>      sectionsList = new ElemLinkedBundleList<>();
  private final IListReorderer<RawSectionData> reorderer    = new ListReorderer<>( sectionsList );

  /**
   * Constructor creates an empty container.
   */
  public RawSectionedDataContainer() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IKeepableEntity
  //

  @Override
  public void read( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    IListEdit<RawSectionData> sects = new ElemLinkedBundleList<>();
    while( aSr.peekChar( EStrioSkipMode.SKIP_COMMENTS ) != CHAR_EOF ) {
      String sectId = aSr.readIdPath();
      aSr.ensureChar( CHAR_EQUAL );
      String content = StrioUtils.readInterbaceContent( aSr );
      RawSectionData ss = new RawSectionData( sectId, content );
      sects.add( ss );
    }
    sectionsList.setAll( sects );
  }

  @Override
  public void write( IStrioWriter aSw ) {
    TsNullArgumentRtException.checkNull( aSw );
    for( RawSectionData ss : sectionsList ) {
      aSw.writeAsIs( ss.sectionId() );
      aSw.writeSpace();
      aSw.writeChar( CHAR_EQUAL );
      aSw.writeSpace();
      aSw.writeAsIs( ss.sectionContent() );
      aSw.writeEol();
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the contained sections.
   *
   * @return {@link IListEdit}&lt;{@link RawSectionData}&gt; - an editable list of sections
   */
  public IListEdit<RawSectionData> sections() {
    return sectionsList;
  }

  /**
   * Returns content of the section with the specified ID.
   * <p>
   * If container has several sections with the same ID then the content of the first one will be returned.
   *
   * @param aSectionId String - the section ID
   * @param aDefaultContent String - returned value if no section with specified ID found, may be <code>null</code>
   * @return String - section content or <i>aDefaultContent</i>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public String getContent( String aSectionId, String aDefaultContent ) {
    TsNullArgumentRtException.checkNull( aSectionId );
    for( RawSectionData sd : sectionsList ) {
      if( sd.sectionId().equals( aSectionId ) ) {
        return sd.sectionContent();
      }
    }
    return aDefaultContent;
  }

  /**
   * Finds all sections with duplicated {@link RawSectionData#sectionId() IDs}.
   * <p>
   * Returns map where keys are IDs which have at least 2 sections in container.
   *
   * @return {@link IStringMap}&lt;{@link IIntList}&gt; - map "section ID" - "indexes of sections in list"
   */
  public IStringMap<IIntList> findDuplicateIds() {
    // all sections
    IStringMapEdit<IIntListEdit> map = new StringMap<>();
    for( int i = 0; i < sectionsList.size(); i++ ) {
      RawSectionData sd = sectionsList.get( i );
      IIntListEdit indexes = map.findByKey( sd.sectionId() );
      if( indexes == null ) {
        indexes = new IntArrayList();
        map.put( sd.sectionId(), indexes );
      }
      indexes.add( i );
    }
    // only duplicates
    IStringMapEdit<IIntList> result = new StringMap<>();
    for( String sid : map.keys() ) {
      IIntList indexes = map.getByKey( sid );
      if( indexes.size() > 1 ) {
        result.put( sid, indexes );
      }
    }
    return result;
  }

  /**
   * Returns all sections with the specified ID.
   *
   * @param aSectionId String - the ID of sections to find
   * @return {@link IList}&lt;{@link RawSectionData}&gt; -
   */
  public IList<RawSectionData> listSections( String aSectionId ) {
    TsNullArgumentRtException.checkNull( aSectionId );
    IListEdit<RawSectionData> ll = new ElemArrayList<>();
    for( RawSectionData sd : sectionsList ) {
      if( sd.sectionId().equals( aSectionId ) ) {
        ll.add( sd );
      }
    }
    return ll;
  }

  /**
   * Returns the section list reorderer.
   *
   * @return {@link IListReorderer} - the sections list reorderer
   */
  public IListReorderer<RawSectionData> reoderer() {
    return reorderer;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    sectionsList.clear();
  }

}
