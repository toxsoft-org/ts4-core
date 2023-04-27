package org.toxsoft.core.tslib.bricks.strid.more;

import static org.toxsoft.core.tslib.bricks.strid.more.ITsResources.*;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Methods for {@link IdChain} manipulation.
 *
 * @author hazard157
 */
public class IdChainUtils {

  /**
   * Validates {@link String} value to be valid {@link IdChain} canonical representation.
   * <p>
   * Does not allows <code>null</code> value.
   */
  public static final ITsValidator<String> CANONICAL_STRING_VALIDATOR = aValue -> {
    TsNullArgumentRtException.checkNull( aValue );
    String[] ss = aValue.split( IdChain.STR_BRANCH_SEPARATOR, -1 );
    if( ss.length == 0 ) {
      return ValidationResult.error( FMT_ERR_EMPTY_CANOSTR, aValue );
    }
    for( int i = 0; i < ss.length; i++ ) {
      String s = ss[i];
      if( !StridUtils.isValidIdPath( s ) ) {
        return ValidationResult.error( FMT_ERR_INV_NTH_IDPATH_IN_CANOSTR, Integer.valueOf( i + 1 ), aValue );
      }
    }
    return ValidationResult.SUCCESS;
  };

  /**
   * TODO IdChain static API:<br>
   * IdChain add(IdChain1,IdChain2), IdChain getParent(IdChain), boolean isChild() <br>
   * ... etc
   */

  /**
   * Constructs new IdChain concatenating two arguments.
   * <p>
   * If ID is an empty string that the chain argument is returned. If chain argument is {@link IdChain#NULL} then either
   * chain of string ID or {@link IdChain#NULL} (iuf aId is an empty string) is returned.
   *
   * @param aId {@link IdChain} - first ID (an IDpath), to be the prefix of the created chain
   * @param aChain {@link IdChain} - second chain, to be the suffix of the created chain
   * @return {@link IdChain} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public static IdChain of( String aId, IdChain aChain ) {
    TsNullArgumentRtException.checkNulls( aId, aChain );
    if( aId.isEmpty() ) {
      return aChain;
    }
    StridUtils.checkValidIdPath( aId );
    if( aChain == IdChain.NULL ) {
      return new IdChain( 0, new SingleStringList( aId ) );
    }
    IStringListEdit branches = new StringArrayList( 1 + aChain.branches().size() );
    branches.addAll( aId );
    branches.addAll( aChain.branches() );
    return new IdChain( 0, branches );
  }

  /**
   * Constructs new IdChain concatenating tho arguments.
   * <p>
   * If ID is an empty string that the chain argument is returned. If chain argument is {@link IdChain#NULL} then either
   * chain of string ID or {@link IdChain#NULL} (iuf aId is an empty string) is returned.
   *
   * @param aChain {@link IdChain} - first chain, to be the prefix of the created chain
   * @param aId {@link IdChain} - second ID (an IDpath), to be the suffix of the created chain
   * @return {@link IdChain} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public static IdChain of( IdChain aChain, String aId ) {
    TsNullArgumentRtException.checkNulls( aChain, aId );
    if( aId.isEmpty() ) {
      return aChain;
    }
    StridUtils.checkValidIdPath( aId );
    if( aChain == IdChain.NULL ) {
      return new IdChain( 0, new SingleStringList( aId ) );
    }
    IStringListEdit branches = new StringArrayList( aChain.branches().size() + 1 );
    branches.addAll( aChain.branches() );
    branches.addAll( aId );
    return new IdChain( 0, branches );
  }

  /**
   * Constructs new IdChain concatenating tho arguments.
   * <p>
   * If any argument is {@link IdChain#NULL} then other argument is returned. If both arguemnts ar {@link IdChain#NULL}
   * the {@link IdChain#NULL} is returned;
   *
   * @param aChain1 {@link IdChain} - first chain, to be the prefix of the created chain
   * @param aChain2 {@link IdChain} - second chain, to be the suffix of the created chain
   * @return {@link IdChain} - concatenated chain
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IdChain of( IdChain aChain1, IdChain aChain2 ) {
    TsNullArgumentRtException.checkNulls( aChain1, aChain2 );
    if( aChain1 == IdChain.NULL && aChain2 == IdChain.NULL ) {
      return IdChain.NULL;
    }
    if( aChain1 == IdChain.NULL ) {
      return aChain2;
    }
    if( aChain2 == IdChain.NULL ) {
      return aChain1;
    }
    IStringListEdit branches = new StringArrayList( aChain1.branches().size() + aChain2.branches().size() );
    branches.addAll( aChain1.branches() );
    branches.addAll( aChain2.branches() );
    return new IdChain( 0, branches );
  }

  /**
   * Determines if <code>aChain</code> starts with the chain <code>aPrefix</code>.
   * <p>
   * Returns true if the arguments are the same.
   *
   * @param aChain {@link IdChain} - the IDchain
   * @param aPrefix {@link IdChain} - the probable prefix
   * @return boolean - <code>true</code> if <code>aChain</code> is the same or starts with <code>aPrefix</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean startsWith( IdChain aChain, IdChain aPrefix ) {
    TsNullArgumentRtException.checkNulls( aChain, aPrefix );
    if( aChain.branches().size() < aPrefix.branches().size() ) {
      return false;
    }
    for( int i = 0; i < aPrefix.branches().size(); i++ ) {
      String b1 = aChain.branches().get( i );
      String b2 = aPrefix.branches().get( i );
      if( !b1.equals( b2 ) ) {
        return false;
      }
    }
    return true;
  }

  /**
   * No subclassing.
   */
  private IdChainUtils() {
    // nop
  }

}
