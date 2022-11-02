package org.toxsoft.core.tsgui.ved.incub;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IKtorSectionsContainer} editable implementation.
 *
 * @author hazard157
 */
public class KtorSectionsContainer
    implements IKtorSectionsContainer, IKeepableEntity, ITsClearable {

  private final IStringMapEdit<String> sectionsMap = new StringMap<>();

  /**
   * Constructor creates an empty container.
   */
  public KtorSectionsContainer() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IKeepableEntity
  //

  @Override
  public void read( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    IStringMapEdit<String> sects = new StringMap<>();
    while( aSr.peekChar( EStrioSkipMode.SKIP_COMMENTS ) != CHAR_EOF ) {
      String sectId = aSr.readIdPath();
      aSr.ensureChar( CHAR_EQUAL );
      String sectContent = StrioUtils.readInterbaceContent( aSr );
      sects.put( sectId, sectContent );
    }
    sectionsMap.setAll( sects );
  }

  @Override
  public void write( IStrioWriter aSw ) {
    TsNullArgumentRtException.checkNull( aSw );
    for( String id : sectionsMap.keys() ) {
      aSw.writeAsIs( id );
      aSw.writeSpace();
      aSw.writeChar( CHAR_EQUAL );
      aSw.writeSpace();
      aSw.writeAsIs( sectionsMap.getByKey( id ) );
      aSw.writeEol();
    }
  }

  // ------------------------------------------------------------------------------------
  // IKtorSectionsContainer
  //

  @Override
  public IStringMap<String> sections() {
    return sectionsMap;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Adds the section.
   *
   * @param aSectionId String - the section ID (an IDpath)
   * @param aSectionContent String - the section content (KTOR interbrace content)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException section ID is not an IDpath
   * @throws TsValidationFailedRtException failed {@link StrioUtils#checkValidInterbraceContent(String)}
   * @throws TsItemAlreadyExistsRtException section with the same ID already exists
   */
  public void addSection( String aSectionId, String aSectionContent ) {
    StridUtils.checkValidIdPath( aSectionId );
    StrioUtils.checkValidInterbraceContent( aSectionContent );
    TsItemAlreadyExistsRtException.checkTrue( sectionsMap.hasKey( aSectionId ) );
    sectionsMap.put( aSectionId, aSectionContent );
  }

  /**
   * Removes the section if exists or does nothing.
   *
   * @param aSectionId String - the section ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void removeSection( String aSectionId ) {
    sectionsMap.removeByKey( aSectionId );
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    sectionsMap.clear();
  }

}
