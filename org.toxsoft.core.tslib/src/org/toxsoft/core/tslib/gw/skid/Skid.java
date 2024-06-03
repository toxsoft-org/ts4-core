package org.toxsoft.core.tslib.gw.skid;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The Green World object identifier, is a part of the {@link Gwid}.
 * <p>
 * Canonical string representation of the SKID {@link #canonicalString()} is the same as concrete GWID representation
 * {@link Gwid#canonicalString()} of kind {@link EGwidKind#GW_CLASS} in the form of "<code>classId[strid]</code>". There
 * is one exception - the singleton {@link #NONE} is represented by the {@link #CANONICAL_STRING_NONE} IDname.
 *
 * @author hazard157
 */
public final class Skid
    implements Serializable, Comparable<Skid> {

  /**
   * Not existing object identifier - there is no real world object with this ID.
   */
  public static final Skid NONE = new Skid();

  /**
   * Canonical string representation of {@link #NONE} instance.
   */
  public static final String CANONICAL_STRING_NONE = "NONE"; //$NON-NLS-1$

  /**
   * Value-object keeper identifier.
   */
  public static final String KEEPER_ID = "Skid"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public final static IEntityKeeper<Skid> KEEPER =
      new AbstractEntityKeeper<>( Skid.class, EEncloseMode.NOT_IN_PARENTHESES, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, Skid aEntity ) {
          aSw.writeAsIs( aEntity.classId() );
          aSw.writeChar( '[' );
          aSw.writeAsIs( aEntity.strid() );
          aSw.writeChar( ']' );
        }

        @Override
        protected Skid doRead( IStrioReader aSr ) {
          String classId = aSr.readIdPath();
          aSr.ensureChar( '[' );
          String strid = aSr.readIdPath();
          aSr.ensureChar( ']' );
          if( classId.equals( IStridable.NONE_ID ) && strid.equals( IStridable.NONE_ID ) ) {
            return NONE;
          }
          return new Skid( classId, strid );
        }
      };

  private static final long serialVersionUID = 157157L;

  private final String  classId;
  private final String  strid;
  private String        canonicalString = null; // lazy initialization in #canonicalString()
  private transient int hashCode        = 0;

  /**
   * Constructor.
   *
   * @param aClassId String - class identifier of the object
   * @param aStrid String - string identifier of the object
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument is not an IDpath
   */
  public Skid( String aClassId, String aStrid ) {
    classId = StridUtils.checkValidIdPath( aClassId );
    strid = StridUtils.checkValidIdPath( aStrid );
  }

  /**
   * Constructor for {@link #NONE} instance.
   */
  protected Skid() {
    classId = IStridable.NONE_ID;
    strid = IStridable.NONE_ID;
  }

  /**
   * Creates {@link Skid} instance from the canonical textual representation.
   *
   * @param aCanonicalString String - the canonical textual representation or {@link #CANONICAL_STRING_NONE}
   * @return {@link Skid} - created instance of {@link #NONE}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid textual format
   */
  public static Skid of( String aCanonicalString ) {
    TsNullArgumentRtException.checkNull( aCanonicalString );
    int len = aCanonicalString.length();
    TsIllegalArgumentRtException.checkTrue( len < 4 ); // min length for "a[b]"
    if( aCanonicalString.equals( CANONICAL_STRING_NONE ) ) {
      return NONE;
    }
    StringBuilder sb = new StringBuilder( 2 * len );
    int currIndex = 0;
    // read class ID
    char ch = aCanonicalString.charAt( currIndex++ );
    TsIllegalArgumentRtException.checkFalse( StridUtils.isIdStart( ch ) );
    sb.append( ch );
    while( StridUtils.isIdPathPart( ch = aCanonicalString.charAt( currIndex++ ) ) ) {
      sb.append( ch );
    }
    String classId = sb.toString();
    sb.setLength( 0 );
    // read object STRID
    TsIllegalArgumentRtException.checkTrue( ch != Gwid.KEYCH_STRID_LEFT );
    ch = aCanonicalString.charAt( currIndex++ );
    TsIllegalArgumentRtException.checkFalse( StridUtils.isIdStart( ch ) );
    sb.append( ch );
    while( StridUtils.isIdPathPart( ch = aCanonicalString.charAt( currIndex++ ) ) ) {
      sb.append( ch );
    }
    TsIllegalArgumentRtException.checkTrue( ch != Gwid.KEYCH_STRID_RIGHT );
    TsIllegalArgumentRtException.checkTrue( currIndex != len );
    String strid = sb.toString();
    return new Skid( classId, strid );
  }

  /**
   * Correctly restores singleton {@link Skid#NONE} from serialized form.
   *
   * @return Object always {@link Skid#NONE}
   * @throws ObjectStreamException never thrown
   */
  private Object readResolve()
      throws ObjectStreamException {
    if( classId.equals( IStridable.NONE_ID ) && strid.equals( IStridable.NONE_ID ) ) {
      return Skid.NONE;
    }
    return this;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return canonicalString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof Skid that ) {
      return this.classId.equals( that.classId ) && this.strid.equals( that.strid );
    }
    return false;
  }

  @Override
  public int hashCode() {
    if( hashCode == 0 ) {
      int result = INITIAL_HASH_CODE;
      result = PRIME * result + classId.hashCode();
      result = PRIME * result + strid.hashCode();
      hashCode = result;
    }
    return hashCode;
  }

  // ------------------------------------------------------------------------------------
  // Comparable
  //

  @Override
  public int compareTo( Skid aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    if( this == aThat ) {
      return 0;
    }
    if( aThat == Skid.NONE ) {
      return +1;
    }
    int c = classId.compareTo( aThat.classId() );
    if( c != 0 ) {
      return c;
    }
    return strid.compareTo( aThat.strid() );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the canonical string representation of the GWID.
   *
   * @return String - GWID canonical string
   */
  public String canonicalString() {
    if( canonicalString == null ) {
      if( this == NONE ) {
        canonicalString = CANONICAL_STRING_NONE;
      }
      else {
        canonicalString = classId + Gwid.KEYCH_STRID_LEFT + strid + Gwid.KEYCH_STRID_RIGHT;
      }
    }
    return canonicalString;
  }

  /**
   * Checks that this is {@link #NONE} identifier.
   *
   * @return boolean - <code>true</code> if {@link #classId()} and {@link #strid()} is equal to {@link #NONE} values
   */
  public boolean isNone() {
    return this.classId.equals( NONE.classId ) && this.strid.equals( NONE.strid );
  }

  /**
   * Returns the identifier of the class of the object.
   *
   * @return String - identifier (IDpath) or the empty string for {@link #NONE}
   */
  public String classId() {
    return classId;
  }

  /**
   * Returns the string identifier of the object.
   * <p>
   * String identifier is unique inside the class {@link #classId()}.
   *
   * @return String - string identifier of the object or the empty string for {@link #NONE}
   */
  public String strid() {
    return strid;
  }

  /**
   * Determines if argument is a valid canonical string.
   *
   * @param aCanonicalString String - the canonical textual representation or {@link #CANONICAL_STRING_NONE}
   * @return boolean - <code>true</code> if argument is syntactic valid canonical string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean isValidCanonicalString( String aCanonicalString ) {
    // TODO temporary code, to be rewritten without using exceptions in logic
    try {
      of( aCanonicalString );
      return true;
    }
    catch( Exception ex ) {
      return false;
    }
  }

}
