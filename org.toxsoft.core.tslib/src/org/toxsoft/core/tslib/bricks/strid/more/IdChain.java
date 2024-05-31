package org.toxsoft.core.tslib.bricks.strid.more;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * A chain of the IDpaths.
 * <p>
 * This is an immutable class.
 * <p>
 * {@link IdChain} instances are compared case-insensitive.
 *
 * @author hazard157
 */
public final class IdChain
    implements Serializable, Comparable<IdChain> {

  private static final long serialVersionUID = 157157L;

  /**
   * Empty {@link IdChain} singleton.
   */
  public static final IdChain NULL = new IdChain();

  /**
   * Branches separator symbol.
   */
  public static final char CHAR_BRANCH_SEPARATOR = '/';

  /**
   * Branches separator string (made of single character {@link #CHAR_BRANCH_SEPARATOR}.
   */
  public static final String STR_BRANCH_SEPARATOR = "/"; //$NON-NLS-1$

  /**
   * Value-object keeper identifier.
   */
  public static final String KEEPER_ID = "IdChain"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public final static IEntityKeeper<IdChain> KEEPER =
      new AbstractEntityKeeper<>( IdChain.class, EEncloseMode.ENCLOSES_BASE_CLASS, NULL ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IdChain aEntity ) {
          aSw.writeAsIs( aEntity.canonicalString() );
        }

        @Override
        protected IdChain doRead( IStrioReader aSr ) {
          IStringListEdit ll = new StringArrayList();
          while( true ) {
            ll.add( aSr.readIdPath() );
            char ch = aSr.peekChar( EStrioSkipMode.SKIP_NONE );
            if( ch != CHAR_BRANCH_SEPARATOR ) {
              break;
            }
            aSr.nextChar( EStrioSkipMode.SKIP_NONE ); // bypass separator char
          }
          return new IdChain( 0, ll );
        }
      };

  private final IStringList branches;
  private transient String  canonicalString = null;

  // ------------------------------------------------------------------------------------
  // Initialization
  //

  /**
   * Constructor.
   *
   * @param aBranches {@link IStringList} - ordered list of the chain branches
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is an empty list
   * @throws TsIllegalArgumentRtException any element of the list is not an IDpath
   */
  public IdChain( IStringList aBranches ) {
    TsNullArgumentRtException.checkNull( aBranches );
    TsIllegalArgumentRtException.checkTrue( aBranches.isEmpty() );
    IStringListEdit ll = new StringArrayList( aBranches.size() );
    for( String s : aBranches ) {
      StridUtils.checkValidIdPath( s );
      ll.add( s );
    }
    branches = ll;
  }

  /**
   * Constructor.
   *
   * @param aBranches String[] - an array of the chain branches
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is an empty array
   * @throws TsIllegalArgumentRtException any element of the array is not an IDpath
   */
  public IdChain( String... aBranches ) {
    TsNullArgumentRtException.checkNull( aBranches );
    TsIllegalArgumentRtException.checkTrue( aBranches.length == 0 );
    IStringListEdit ll = new StringArrayList( aBranches.length );
    for( String s : aBranches ) {
      StridUtils.checkValidIdPath( s );
      ll.add( s );
    }
    branches = ll;
  }

  /**
   * Creates an instance of {@link IdChain} from canonical string representation.
   *
   * @param aCanonicalString String - canonical string representation
   * @return {@link IdChain} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not canonical string representation
   */
  public static IdChain of( String aCanonicalString ) {
    TsErrorUtils.checkNonEmpty( aCanonicalString );
    int lastIndex = aCanonicalString.length();
    if( lastIndex == 0 ) { // empty string -> IdChain.NULL
      return NULL;
    }
    IStringListEdit sl = new StringArrayList();
    int prevIndex = 0;
    while( true ) {
      int index = aCanonicalString.indexOf( CHAR_BRANCH_SEPARATOR, prevIndex );
      if( index == prevIndex || index == lastIndex ) {
        throw new TsIllegalArgumentRtException();
      }
      if( index >= 0 ) {
        sl.add( aCanonicalString.substring( prevIndex, index ) );
        prevIndex = index + 1;
      }
      else {
        sl.add( aCanonicalString.substring( prevIndex ) );
        break;
      }
    }
    return new IdChain( sl );
  }

  /**
   * Internal constructor for {@link #NULL}.
   */
  private IdChain() {
    branches = IStringList.EMPTY;
  }

  /**
   * Internal constructor for some kind of optimization.
   *
   * @param aFoo int - unused argument to have method signature different from {@link #IdChain(IStringList)}
   * @param aBranches {@link IStringList} - reference to this argument will became field {@link #branches}
   */
  IdChain( int aFoo, IStringList aBranches ) {
    branches = aBranches;
  }

  /**
   * Method correctly deserializes {@link #NULL} value.
   *
   * @return {@link ObjectStreamException} - {@link #NULL}
   * @throws ObjectStreamException is declared but newer throw by this method
   */
  private Object readResolve()
      throws ObjectStreamException {
    if( branches.equals( IStringList.EMPTY ) ) {
      return NULL;
    }
    return this;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the ordered list of the chain branches.
   *
   * @return {@link IStringList} - the ordered list of the chain branches
   */
  public IStringList branches() {
    return branches;
  }

  /**
   * Shorthand for {@link IList#get(int) branches().get( aIndex )}.
   *
   * @param aIndex int - the index of the branch
   * @return String - the branch value
   * @throws TsIllegalArgumentRtException index is out of range
   */
  public String get( int aIndex ) {
    return branches.get( aIndex );
  }

  /**
   * Determines if chain is empty.
   *
   * @return boolean - <code>true</code> - an empty chain, normally only {@link #NULL} is an empty chain
   */
  public boolean isEmpty() {
    return branches.isEmpty();
  }

  /**
   * Returns the canonical representation of the {@link IdChain}.
   * <p>
   * Canonical representation is branches separated by the {@link #CHAR_BRANCH_SEPARATOR}, eg
   * "<code>branch1.id.path/branch2/branch3</code>". For the chain with no branches (that is for singleton
   * {@link #NULL}) returns an empty string.
   *
   * @return String - string
   */
  public String canonicalString() {
    if( canonicalString == null ) {
      StringBuilder sb = new StringBuilder();
      for( String s : branches ) {
        sb.append( s );
        if( s != branches.last() ) {
          sb.append( CHAR_BRANCH_SEPARATOR );
        }
      }
      canonicalString = sb.toString();
    }
    return canonicalString;
  }

  /**
   * Returns the first branch of chain.
   *
   * @return String - last branch (an IDpath)
   */
  public String first() {
    return branches.first();
  }

  /**
   * Returns the first of chain.
   *
   * @return String - last branch (an IDpath)
   */
  public String last() {
    return branches.last();
  }

  /**
   * Returns new chain made of <code>aCount</code> number of subsequent branches starting with specified branch index.
   *
   * @param aStartBranchIndex int - index of first branch to include in subchain
   * @param aCount int - number of subsequent branches to include
   * @return {@link IdChain} - created instance of subchain
   * @throws TsIllegalArgumentRtException index out of {@link #branches()} range
   * @throws TsIllegalArgumentRtException <code>aCount</code> <= 0
   * @throws TsIllegalArgumentRtException <code>aCount</code> specifies branches out of {@link #branches()} range
   */
  public IdChain subchain( int aStartBranchIndex, int aCount ) {
    TsIllegalArgumentRtException.checkFalse( branches.isInRange( aStartBranchIndex ) );
    TsIllegalArgumentRtException.checkTrue( aCount <= 0 );
    int endIndex = aStartBranchIndex + aCount - 1; // index of last branch to include
    TsIllegalArgumentRtException.checkFalse( branches.isInRange( endIndex ) );
    IStringListEdit ss = new StringArrayList();
    for( int i = aStartBranchIndex; i <= endIndex; i++ ) {
      ss.add( branches.get( i ) );
    }
    return new IdChain( 0, ss );
  }

  /**
   * Checks if argument is a valid canoncal string.
   * <p>
   * Method {@link #of(String)} will not throw an exception if and only if this method returns true.
   * <p>
   * An empty string (string with 0 length) is considered valid as it produces {@link IdChain#NULL}.
   *
   * @param aCanonicalString String - the string to test
   * @return boolean - <code>true</code> if argument is valid canonical string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean isValidCanonicalString( String aCanonicalString ) {
    TsNullArgumentRtException.checkNull( aCanonicalString );
    if( aCanonicalString.isEmpty() ) {
      return true;
    }
    char ch = aCanonicalString.charAt( 0 );
    if( !StridUtils.isIdStart( ch ) ) {
      return false;
    }
    boolean compFirstChar = true;
    for( int i = 0, n = aCanonicalString.length(); i < n; i++ ) {
      ch = aCanonicalString.charAt( n );
      if( compFirstChar ) { // previous char was '/' or this is the string start
        if( !StridUtils.isIdStart( ch ) ) { // here may be IDpath starting char
          return false;
        }
      }
      else {
        if( ch == CHAR_BRANCH_SEPARATOR ) {
          compFirstChar = true;
        }
        else {
          if( !StridUtils.isIdPathPart( ch ) ) { // here may be IDpath body char
            return false;
          }
        }
      }
    }
    if( compFirstChar ) { // last char was '/'
      return false;
    }
    return true;
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
    if( aThat instanceof IdChain that ) {
      return this.branches.equals( that.branches );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + branches.hashCode();
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Comparable
  //

  @Override
  public int compareTo( IdChain aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    if( aThat == this ) {
      return 0;
    }
    return canonicalString().compareToIgnoreCase( aThat.canonicalString() );
  }

}
