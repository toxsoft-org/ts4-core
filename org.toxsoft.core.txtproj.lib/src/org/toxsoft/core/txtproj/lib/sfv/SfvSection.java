package org.toxsoft.core.txtproj.lib.sfv;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ISfvSection} implementation.
 * <p>
 * This is an immutable class.
 *
 * @author hazard157
 */
public final class SfvSection
    implements ISfvSection {

  private final String id;
  private final String content;

  private int hashCode = 0;

  /**
   * Constructor.
   *
   * @param aId String - section ID (an IDpath)
   * @param aContent String - section content
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDPath
   */
  public SfvSection( String aId, String aContent ) {
    id = StridUtils.checkValidIdPath( aId );
    content = TsNullArgumentRtException.checkNull( aContent );
  }

  // ------------------------------------------------------------------------------------
  // ISfvSection
  //

  @Override
  public String sectionId() {
    return id;
  }

  @Override
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
    if( aThat instanceof ISfvSection that ) {
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
  public int compareTo( ISfvSection aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    if( aThat == this ) {
      return 0;
    }
    return this.sectionId().compareToIgnoreCase( aThat.sectionId() );
  }

}
