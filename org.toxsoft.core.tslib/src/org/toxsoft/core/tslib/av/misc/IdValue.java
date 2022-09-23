package org.toxsoft.core.tslib.av.misc;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An ID/Value pair - the atomic value identified by the IDpath.
 *
 * @author hazard157
 */
public final class IdValue
    implements Comparable<IdValue>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "IdValue"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<IdValue> KEEPER =
      new AbstractEntityKeeper<>( IdValue.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IdValue aEntity ) {
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          AtomicValueKeeper.KEEPER.write( aSw, aEntity.value() );
        }

        @Override
        protected IdValue doRead( IStrioReader aSr ) {
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          IAtomicValue av = AtomicValueKeeper.KEEPER.read( aSr );
          return new IdValue( id, av );
        }
      };

  private final String       id;
  private final IAtomicValue value;

  /**
   * Constructor.
   *
   * @param aId String - the ID (an IDpath)
   * @param aValue {@link IAtomicValue} - the value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aId not an IDpath
   */
  public IdValue( String aId, IAtomicValue aValue ) {
    id = StridUtils.checkValidIdPath( aId );
    value = TsNullArgumentRtException.checkNull( aValue );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the ID.
   *
   * @return String - the ID (always an IDpath
   */
  public String id() {
    return id;
  }

  /**
   * Returns the value.
   *
   * @return {@link IAtomicValue} - the value, never is <code>null</code>
   */
  public IAtomicValue value() {
    return value;
  }

  // ------------------------------------------------------------------------------------
  // Static API
  //

  /**
   * Extracts content of {@link IOptionSet} as collection od {@link IdValue}.
   *
   * @param aOps {@link IOptionSet} - the options set
   * @return {@link IStringMapEdit}&lt;{@link IdValue}&gt; - the map "option ID" - "option as IdValue"
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IStringMapEdit<IdValue> makeIdValuesCollFromOptionSet( IOptionSet aOps ) {
    TsNullArgumentRtException.checkNull( aOps );
    IStringMapEdit<IdValue> map = new StringMap<>();
    for( String key : aOps.keys() ) {
      IAtomicValue value = aOps.getByKey( key );
      IdValue idv = new IdValue( key, value );
      map.put( key, idv );
    }
    return map;
  }

  /**
   * Copies c ontent of {@link IdValue} collection to the {@link IOptionSetEdit}.
   * <p>
   * Existing options will be overwritten. Options not listed in <code>aIdvals</code> will remain intact.
   *
   * @param aIdvals {@link ITsCollection}&lt;{@link IdValue}&gt; - named values collection
   * @param aOps
   */
  public static void fillOptionSetFromIdValuesColl( ITsCollection<IdValue> aIdvals, IOptionSetEdit aOps ) {

  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return id + '=' + value.asString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof IdValue that ) {
      return id.equals( that.id ) && value.equals( that.value );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + id.hashCode();
    result = PRIME * result + value.hashCode();
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Comparable
  //

  @Override
  public int compareTo( IdValue aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    return id.compareToIgnoreCase( aThat.id );
  }

}
