package org.toxsoft.tslib.gw.skid;

import static org.toxsoft.tslib.utils.TsLibUtils.*;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper.EEncloseMode;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link Skid} immutable implementation.
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
  private transient int hashCode = 0;

  /**
   * Constructor.
   *
   * @param aClassId String - class identirier of the object
   * @param aStrid String - string identirier of the object
   * @throws TsNullArgumentRtException any arguent = null
   * @throws TsIllegalArgumentRtException any argument is invalid IDpath
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
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return classId + '[' + strid + ']';
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof Skid ) {
      Skid that = (Skid)aThat;
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
   * Checks that this is {@link #NONE} identifier.
   *
   * @return boolean - <code>true</code> if {@link #classId()} and {@link #strid()} is equal to {@link #NONE} values
   */
  public boolean isNone() {
    return this.classId.equals( NONE.classId ) && this.strid.equals( NONE.strid );
  }

  /**
   * Returns the identirier of the class of the object.
   *
   * @return String - identirier (IDpath) or the empty string for {@link #NONE}
   */
  public String classId() {
    return classId;
  }

  /**
   * Returns the string identirier of the object.
   * <p>
   * String identifier is unique inside the class {@link #classId()}.
   *
   * @return String - string identirier of the object or the empty string for {@link #NONE}
   */
  public String strid() {
    return strid;
  }

}
