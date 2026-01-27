package org.toxsoft.core.tslib.bricks.strid.more;

import java.io.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * A paired identifier consisting of a left and a right ID-path identifier.
 * <p>
 * An {@link IdPair} has a text representation {@link #pairId()}, which is <b>not</b> an IDpath, but is guaranteed not
 * to contain spaces.
 * <p>
 * A pair identifier {@link #pairId()} consists of a left {@link #leftId()} and a right {@link #rightId()} ID path,
 * joined using the {@link #makePairId(String, String)} method. Missing left or right parts are not allowed.
 * <p>
 * {@link IdPair} is a special case of a more general {@link IdChain} introduced for convenience when exactly 2 IDpaths
 * are bound together.
 *
 * @author hazard157
 */
public final class IdPair
    implements Comparable<IdPair>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * A singleton of an identifier indicating the absence of something.
   * <p>
   * This constant has the same usage as {@link IStridable#NONE_ID} for IDpaths/IDnames.
   */
  public static final IdPair NONE = new IdPair( IStridable.NONE_ID, IStridable.NONE_ID );

  /**
   * Separator between left and right IDs when creating {@link #pairId()}.
   */
  public static final char CHAR_SEPARATOR = '$';

  /**
   * Separator to create the return value of {@link #asNonUniqueIdPath(IdPair)}.
   */
  private static final String IDPATH_SEPARATOR = "_____"; //$NON-NLS-1$

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "IdPair"; //$NON-NLS-1$

  /**
   * * The keeper singleton.
   */
  public static final IEntityKeeper<IdPair> KEEPER =
      new AbstractEntityKeeper<>( IdPair.class, EEncloseMode.NOT_IN_PARENTHESES, NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IdPair aEntity ) {
          aSw.writeAsIs( aEntity.pairId() );
        }

        @Override
        protected IdPair doRead( IStrioReader aSr ) {
          String leftId = aSr.readIdPath();
          EStrioSkipMode oldMode = aSr.skipMode();
          try {
            aSr.setSkipMode( EStrioSkipMode.SKIP_NONE );
            aSr.ensureChar( CHAR_SEPARATOR );
            String rightId = aSr.readIdPath();
            return new IdPair( leftId, rightId );
          }
          finally {
            aSr.setSkipMode( oldMode );
          }
        }
      };

  private transient String leftId  = null;
  private transient String rightId = null;
  private final String     pairId;

  /**
   * Constructor.
   *
   * @param aLeftId String - left IDpath
   * @param aRightId String - right IDpath
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public IdPair( String aLeftId, String aRightId ) {
    pairId = makePairId( aLeftId, aRightId );
    leftId = aLeftId;
    rightId = aRightId;
  }

  /**
   * Constructor from the pair ID created by {@link #pairId()}.
   *
   * @param aPairId String - the pair ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not a pair ID
   */
  public IdPair( String aPairId ) {
    TsIllegalArgumentRtException.checkFalse( isValidPairId( aPairId ) );
    pairId = aPairId;
    initParts();
  }

  // the method for recovery after de-serialization
  private void initParts() {
    if( leftId == null || rightId == null ) {
      int index = pairId.indexOf( CHAR_SEPARATOR );
      leftId = pairId.substring( 0, index );
      rightId = pairId.substring( index + 1 );
    }
  }

  /**
   * Method correctly deserializes {@link IAtomicValue#NULL} value and initializes transient fields.
   *
   * @return {@link Object} - an {@link IdPair} instance (including {@link IdPair#NONE})
   * @throws ObjectStreamException is declared but newer thrown by this method
   */
  private Object readResolve()
      throws ObjectStreamException {
    initParts();
    if( pairId.equals( NONE.pairId ) ) {
      return NONE;
    }
    return this;
  }

  // ------------------------------------------------------------------------------------
  // static API
  //

  // TODO TRANSLATE

  /**
   * Создает текстовое представление парного идентификатора.
   *
   * @param aLeftId String - левый идентификатор
   * @param aRightId String - правый идентификатор
   * @return String - текст парного идентификатор
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException любой идентификатор не ИД-путь
   */
  public static String makePairId( String aLeftId, String aRightId ) {
    StridUtils.checkValidIdPath( aLeftId );
    StridUtils.checkValidIdPath( aRightId );
    return aLeftId + CHAR_SEPARATOR + aRightId;
  }

  /**
   * Определяет, имеет ли текстовое представление парного идентификатора допустимый формат.
   *
   * @param aPairId String - текст парного идентификатор
   * @return boolean - признак правилного аргумента
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException неверный формат текстового представления
   */
  public static boolean isValidPairId( String aPairId ) {
    TsNullArgumentRtException.checkNull( aPairId );
    int len = aPairId.length();
    if( len < 3 ) {
      return false;
    }
    int index = aPairId.indexOf( CHAR_SEPARATOR );
    if( index <= 0 || index >= len - 1 ) {
      return false;
    }
    String groupId = aPairId.substring( 0, index );
    String entryId = aPairId.substring( index + 1 );
    if( !StridUtils.isValidIdPath( groupId ) ) {
      return false;
    }
    if( !StridUtils.isValidIdPath( entryId ) ) {
      return false;
    }
    return true;
  }

  /**
   * Creates probably non-restorable IDPath from an {@link IdPair}.
   * <p>
   * If arguments does <b>not</b> contains string {@link #IDPATH_SEPARATOR} then {@link IdPair} may be restored from the
   * resulting IDpath.
   *
   * @param aIdPair {@link IdPair} - the pair
   * @return String - created IDpath
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static String asNonUniqueIdPath( IdPair aIdPair ) {
    TsNullArgumentRtException.checkNull( aIdPair );
    return aIdPair.leftId + IDPATH_SEPARATOR + aIdPair.rightId;
  }

  /**
   * Argument is converted to an IDpath allowing to restore {@link IdPair} from it.
   * <p>
   * If any part of the argument contains string {@link #IDPATH_SEPARATOR} then exception is thrown because result can
   * not guarantee {@link IdPair} restoration.
   * <p>
   * {@link IdPair} may be restored by the method {@link #fromUniqueIdPath(String)}.
   *
   * @param aIdPair {@link IdPair} - the pair
   * @return String - created IDpath
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any part of the argument contains {@link #IDPATH_SEPARATOR}
   */
  public static String toUniqueIdPath( IdPair aIdPair ) {
    TsNullArgumentRtException.checkNull( aIdPair );
    TsIllegalArgumentRtException.checkTrue( aIdPair.pairId().contains( IDPATH_SEPARATOR ) );
    return aIdPair.leftId + IDPATH_SEPARATOR + aIdPair.rightId;
  }

  /**
   * Restores an {@link IdPair} from from the string created by {@link #toUniqueIdPath(IdPair)}.
   *
   * @param aIdPath String - result of {@link #asNonUniqueIdPath(IdPair)}
   * @return {@link IdPair} - restored instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument was not created by {@link #toString()}
   */
  public static IdPair fromUniqueIdPath( String aIdPath ) {
    StridUtils.checkValidIdPath( aIdPath );
    String[] rrPair = aIdPath.split( IDPATH_SEPARATOR );
    TsIllegalArgumentRtException.checkTrue( rrPair.length != 2 );
    return new IdPair( rrPair[0], rrPair[1] );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Определяет, явлется ли этот экземпляр константой {@link #NONE}.
   *
   * @return boolean - признак конатанты {@link #NONE}
   */
  public boolean isNone() {
    return pairId.equals( NONE.pairId );
  }

  /**
   * Возвращает левый идентификатор.
   *
   * @return String - левый идентификатор
   */
  public String leftId() {
    return leftId;
  }

  /**
   * Возвращает правый идентификатор.
   *
   * @return aRightId String - правый идентификатор
   */
  public String rightId() {
    return rightId;
  }

  /**
   * Возвращает текст парного идентификатора.
   *
   * @return String - текст парного идентификатора
   */
  public String pairId() {
    return pairId;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return pairId;
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof IdPair that ) {
      return pairId.equals( that.pairId );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + pairId.hashCode();
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Comparable
  //

  @Override
  public int compareTo( IdPair aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    return pairId.compareTo( aThat.pairId() );
  }

}
