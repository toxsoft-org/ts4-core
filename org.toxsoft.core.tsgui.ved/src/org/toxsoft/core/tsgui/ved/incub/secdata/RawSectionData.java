package org.toxsoft.core.tsgui.ved.incub.secdata;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Section data for {@link RawSectionedDataContainer}.
 * <p>
 * This an immutable class.
 *
 * @author hazard157
 */
public final class RawSectionData
    implements Comparable<RawSectionData>, Serializable {

  private static final long serialVersionUID = 2552577993577497502L;

  private final String id;
  private final String content;

  private transient int hashCode = 0;

  /**
   * Constructor.
   * <p>
   * Note: section content will be trimmed by {@link String#trim()}.
   * <p>
   * First character intreiimed String must be {@link IStrioHardConstants#CHAR_SET_BEGIN} or
   * {@link IStrioHardConstants#CHAR_ARRAY_BEGIN} and last one must be a matching bracket.
   * <p>
   * Obiously, content sting must have at least teo characters - opening and closing brackets.
   *
   * @param aId String - section ID (an IDpath)
   * @param aContent String - section content
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDPath
   * @throws TsIllegalArgumentRtException content is not surrounded with valid brackets
   */
  public RawSectionData( String aId, String aContent ) {
    id = StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNull( aContent );
    String s = aContent.trim();
    TsIllegalArgumentRtException.checkTrue( s.length() < 2 );
    switch( s.charAt( 0 ) ) {
      case CHAR_SET_BEGIN: {
        TsIllegalArgumentRtException.checkTrue( s.charAt( s.length() - 1 ) != CHAR_SET_END );
        break;
      }
      case CHAR_ARRAY_BEGIN: {
        TsIllegalArgumentRtException.checkTrue( s.charAt( s.length() - 1 ) != CHAR_ARRAY_END );
        break;
      }
      default:
        throw new TsIllegalArgumentRtException();
    }
    content = s;
  }

  // ------------------------------------------------------------------------------------
  // RawSectionData
  //

  /**
   * Returns the section ID.
   *
   * @return String - the section ID (an IDpath)
   */
  public String sectionId() {
    return id;
  }

  /**
   * Returns the section content.
   * <p>
   * Section content starts by opening bracket and finshed with ending bracket.
   *
   * @return String - the section content may be really huge string
   */
  public String sectionContent() {
    return content;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return id;
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof RawSectionData that ) {
      return id.equals( that.sectionId() ) && content.equals( that.sectionContent() );
    }
    return false;
  }

  @Override
  public int hashCode() {
    if( hashCode == 0 ) {
      hashCode = TsLibUtils.INITIAL_HASH_CODE;
      hashCode = TsLibUtils.PRIME * hashCode + id.hashCode();
      hashCode = TsLibUtils.PRIME * hashCode + content.hashCode();
    }
    return hashCode;
  }

  // ------------------------------------------------------------------------------------
  // Comparable
  //

  @Override
  public int compareTo( RawSectionData aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    if( aThat == this ) {
      return 0;
    }
    return this.sectionId().compareToIgnoreCase( aThat.sectionId() );
  }

}
