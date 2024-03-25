package org.toxsoft.core.tslib.math.mathops;

import static org.toxsoft.core.tslib.math.mathops.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The enumeration of XXX.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EMathBinaryOp
    implements IStridable {

  PLUS( "plus", '+', STR_MBO_PLUS, STR_MBO_PLUS_D ), //$NON-NLS-1$

  MINUS( "minus", '+', STR_MBO_MINUS, STR_MBO_MINUS_D ), //$NON-NLS-1$

  MUL( "mul", '+', STR_MBO_MUL, STR_MBO_MUL_D ), //$NON-NLS-1$

  DIV( "div", '+', STR_MBO_DIV, STR_MBO_DIV_D ), //$NON-NLS-1$

  ;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "EMathBinaryOp"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<EMathBinaryOp> KEEPER = new StridableEnumKeeper<>( EMathBinaryOp.class );

  private static IStridablesListEdit<EMathBinaryOp> list = null;

  private final String id;
  private final char   opChar;
  private final String name;
  private final String description;

  EMathBinaryOp( String aId, char aOpChar, String aName, String aDescription ) {
    id = aId;
    name = aName;
    description = aDescription;
    opChar = aOpChar;
  }

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the character representation of the operation.
   *
   * @return char - the character representation
   */
  public char opChar() {
    return opChar;
  }

  /**
   * Determines if argument is an operation char.
   *
   * @param aChar char - the symbol to check
   * @return {@link EMathBinaryOp} - the operation or <code>null</code>
   */
  public static EMathBinaryOp findByChar( char aChar ) {
    for( EMathBinaryOp op : asList() ) {
      if( op.opChar() == aChar ) {
        return op;
      }
    }
    return null;
  }

  // ----------------------------------------------------------------------------------
  // Stridable enum common API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EMathBinaryOp} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EMathBinaryOp> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EMathBinaryOp} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EMathBinaryOp getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EMathBinaryOp} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EMathBinaryOp findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EMathBinaryOp item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EMathBinaryOp} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EMathBinaryOp getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
