package org.toxsoft.tslib.coll.helpers;

import static org.toxsoft.tslib.coll.helpers.ITsResources.*;

import java.io.Serializable;

import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper.EEncloseMode;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.bricks.validator.EValidationResultType;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.basis.ITsCollection;
import org.toxsoft.tslib.coll.basis.ITsFastIndexListTag;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

/**
 * Constraints on any collection content.
 * <p>
 * This is an immutable class.
 *
 * @author hazard157
 */
public final class CollConstraint
    implements Serializable {

  /**
   * Constant for uncontrainted collections constraint.
   */
  public static final CollConstraint NONE = new CollConstraint( 0, 0 );

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "CollConstraint"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static IEntityKeeper<CollConstraint> KEEPER =
      new AbstractEntityKeeper<>( CollConstraint.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, CollConstraint aEntity ) {
          aSw.writeInt( aEntity.maxCount() );
          aSw.writeSeparatorChar();
          aSw.writeIntHex( aEntity.flags() );
        }

        @Override
        protected CollConstraint doRead( IStrioReader aSr ) {
          int maxCount = aSr.readInt();
          aSr.ensureSeparatorChar();
          int flags = aSr.readInt();
          return new CollConstraint( maxCount, flags );
        }

      };

  private static final long serialVersionUID = 157157L;

  private static final int FLAG_IS_EXACT_COUNT      = 0x0001;
  private static final int FLAG_IS_EMPTY_PROHIBITED = 0x0002;
  private static final int FLAG_IS_DUPS_PROHIBITED  = 0x0004;

  private final int maxCount;
  private final int flags;

  /**
   * Constructor.
   *
   * @param aMaxCount int - maximum allowen number of elements in collection
   * @param aIsExactCount boolean - the flag of the empty collection is prohibited
   * @param aIsEmptyProhibited boolean - the flag of the empty collection is prohibited
   * @param aIsDuplicatesProhibited boolean - the flag of the dulicates prohibited
   * @throws TsIllegalArgumentRtException aMaxCount < 0
   */
  public CollConstraint( int aMaxCount, boolean aIsExactCount, boolean aIsEmptyProhibited,
      boolean aIsDuplicatesProhibited ) {
    TsIllegalArgumentRtException.checkTrue( aMaxCount < 0 );
    maxCount = aMaxCount;
    int f = 0;
    if( aIsExactCount ) {
      f |= FLAG_IS_EXACT_COUNT;
    }
    if( aIsEmptyProhibited ) {
      f |= FLAG_IS_EMPTY_PROHIBITED;
    }
    if( aIsDuplicatesProhibited ) {
      f |= FLAG_IS_DUPS_PROHIBITED;
    }
    flags = f;
  }

  CollConstraint( int aMaxCount, int aFlags ) {
    TsIllegalArgumentRtException.checkTrue( aMaxCount < 0 );
    maxCount = aMaxCount;
    flags = aFlags;
  }

  // ------------------------------------------------------------------------------------
  // internal implementation
  //

  @SuppressWarnings( "boxing" )
  private String internalCheckSize( ITsCollection<?> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    if( maxCount != 0 ) {
      int sz = aColl.size();
      if( isExactCount() ) {
        if( sz != maxCount ) {
          return String.format( FMT_MSG_NON_EXACT_SIZE, sz, maxCount );
        }
      }
      else {
        if( sz > maxCount ) {
          return String.format( FMT_MSG_TOO_BIG_SIZE, sz, maxCount );
        }
      }
    }
    return TsLibUtils.EMPTY_STRING;
  }

  private String internalCheckEmpty( ITsCollection<?> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    if( isEmptyProhibited() ) {
      if( aColl.isEmpty() ) {
        return MSG_MSG_COLL_IS_EMPTY;
      }
    }
    return TsLibUtils.EMPTY_STRING;
  }

  @SuppressWarnings( { "rawtypes", "unchecked", "boxing" } )
  private String internalCheckDups( ITsCollection<?> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    if( isDuplicatesProhibited() ) {
      IList ll;
      if( aColl instanceof ITsFastIndexListTag ) {
        ll = (IList)aColl;
      }
      else {
        ll = new ElemArrayList( aColl );
      }
      for( int i = 0, sz = ll.size(); i < sz - 1; i++ ) {
        Object e1 = ll.get( i );
        for( int j = i + 1; j < sz; j++ ) {
          Object e2 = ll.get( j );
          if( e1.equals( e2 ) ) {
            return String.format( FMT_MSG_DUP_ELEMS, i, j );
          }
        }
      }
    }
    return TsLibUtils.EMPTY_STRING;
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  int flags() {
    return flags;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append( '{' );
    sb.append( Integer.valueOf( maxCount ) );
    if( (flags & FLAG_IS_EXACT_COUNT) != 0 ) {
      sb.append( " EXACT_COUNT" ); //$NON-NLS-1$
    }
    if( (flags & FLAG_IS_EMPTY_PROHIBITED) != 0 ) {
      sb.append( " EMPTY_PROHIBITED" ); //$NON-NLS-1$
    }
    if( (flags & FLAG_IS_DUPS_PROHIBITED) != 0 ) {
      sb.append( " DUPS_PROHIBITED" ); //$NON-NLS-1$
    }
    sb.append( '}' );
    return sb.toString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof CollConstraint ) {
      CollConstraint that = (CollConstraint)aThat;
      return maxCount == that.maxCount && flags == that.flags;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + maxCount;
    result = TsLibUtils.PRIME * result + flags;
    return result;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the size limit on the collection.
   * <p>
   * If {@link #isExactCount()} flag is <code>true</code> then collection must contain exactly his number of elements.
   * Otherwise collection is prohibited to contain up to this number of elements.
   * <p>
   * Returned value 0 means that there is no size resriction on the collection.
   * <p>
   * Never returns negative values.
   *
   * @return int - maxmum or exact number of elements prohibited in collection or 0 for no size limit
   */
  public int maxCount() {
    return maxCount;
  }

  /**
   * Determines if there is size restriction on the collection.
   * <p>
   * Returns the result of the comparison <code>{@link #maxCount()} != 0</code>.
   *
   * @return blooean - the flag of the size limit exsitance on the collection
   */
  public boolean isSizeResticted() {
    return maxCount != 0;
  }

  /**
   * Determines if the collection must contain single element (or amybe no elements at all).
   * <p>
   * Returns the result of the comparison <code>{@link #maxCount()} == 1</code>.
   *
   * @return blooean - the flag of the single element collecion
   */
  public boolean isSingle() {
    return maxCount == 1;
  }

  /**
   * Determines if {@link #maxCount()} specifies exact rather than maximum number of elements in collection.
   *
   * @return boolean - the flag of the empty collection is prohibited
   */
  public boolean isExactCount() {
    return (flags & FLAG_IS_EXACT_COUNT) != 0;
  }

  /**
   * Determines if empty collection is prohibited.
   *
   * @return boolean - the flag of the empty collection is prohibited
   */
  public boolean isEmptyProhibited() {
    return (flags & FLAG_IS_EMPTY_PROHIBITED) != 0;
  }

  /**
   * Determines if duplicated elements is prohibited in collection.
   *
   * @return boolean - the flag of the dulicates prohibited
   */
  public boolean isDuplicatesProhibited() {
    return (flags & FLAG_IS_DUPS_PROHIBITED) != 0;
  }

  // ------------------------------------------------------------------------------------
  // Collection check API
  //

  /**
   * Checks collection size against {@link #maxCount()} and {@link #isExactCount()} constraints.
   * <p>
   * If the collection size violates contsraints method returns warning {@link EValidationResultType#WARNING} with
   * appropriate message.
   *
   * @param aColl {@link ITsCollection} - collection to be checked
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public ValidationResult checkWarnSize( ITsCollection<?> aColl ) {
    String msg = internalCheckSize( aColl );
    if( msg.isEmpty() ) {
      return ValidationResult.SUCCESS;
    }
    return ValidationResult.warn( msg );
  }

  /**
   * Checks collection size against {@link #maxCount()} and {@link #isExactCount()} constraints.
   * <p>
   * If the collection size violates any contsraint then method returns error {@link EValidationResultType#ERROR} with
   * appropriate message.
   *
   * @param aColl {@link ITsCollection} - collection to be checked
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public ValidationResult checkErrorSize( ITsCollection<?> aColl ) {
    String msg = internalCheckSize( aColl );
    if( msg.isEmpty() ) {
      return ValidationResult.SUCCESS;
    }
    return ValidationResult.error( msg );
  }

  /**
   * Checks the collection against {@link #isEmptyProhibited()} constraint.
   * <p>
   * If the collection violates contsraint then method returns warning {@link EValidationResultType#WARNING} with
   * appropriate message.
   *
   * @param aColl {@link ITsCollection} - collection to be checked
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public ValidationResult checkWarnEmpty( ITsCollection<?> aColl ) {
    String msg = internalCheckEmpty( aColl );
    if( msg.isEmpty() ) {
      return ValidationResult.SUCCESS;
    }
    return ValidationResult.warn( msg );
  }

  /**
   * Checks the collection against {@link #isEmptyProhibited()} constraint.
   * <p>
   * If the collection violates contsraint then method returns error warning {@link EValidationResultType#ERROR} with
   * appropriate message.
   *
   * @param aColl {@link ITsCollection} - collection to be checked
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public ValidationResult checkErrorEmpty( ITsCollection<?> aColl ) {
    String msg = internalCheckEmpty( aColl );
    if( msg.isEmpty() ) {
      return ValidationResult.SUCCESS;
    }
    return ValidationResult.error( msg );
  }

  /**
   * Checks the collection against {@link #isDuplicatesProhibited()} constraint.
   * <p>
   * If the collection violates contsraint then method returns warning {@link EValidationResultType#WARNING} with
   * appropriate message.
   *
   * @param aColl {@link ITsCollection} - collection to be checked
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public ValidationResult checkWarnDups( ITsCollection<?> aColl ) {
    String msg = internalCheckDups( aColl );
    if( msg.isEmpty() ) {
      return ValidationResult.SUCCESS;
    }
    return ValidationResult.warn( msg );
  }

  /**
   * Checks the collection against {@link #isDuplicatesProhibited()} constraint.
   * <p>
   * If the collection violates contsraint then method returns error {@link EValidationResultType#ERROR} with
   * appropriate message.
   *
   * @param aColl {@link ITsCollection} - collection to be checked
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public ValidationResult checkErrorDups( ITsCollection<?> aColl ) {
    String msg = internalCheckDups( aColl );
    if( msg.isEmpty() ) {
      return ValidationResult.SUCCESS;
    }
    return ValidationResult.error( msg );
  }

}
