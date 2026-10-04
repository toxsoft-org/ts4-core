package org.toxsoft.core.tslib.utils.valobj;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Information used to register value-oebjcts.
 * <p>
 * This information is specified by the user during registration in {@link TsValobjUtils}.
 *
 * @author hazard157
 * @param id String - value-object ID (IDpath)
 * @param keeper {@link IEntityKeeper} - the valobj keeper (contains reference to the value-object {@link Class})
 * @param params {@link IOptionSet} - values for {@link #params}
 */
public record TsValobjInfo ( String id, IEntityKeeper<?> keeper, IOptionSet params )
    implements IStridableParameterized {

  /**
   * {@link #params()} option ID: Option contains value-object category ID.
   * <p>
   * Category is only used to group visually the registered Value-objects in GUI/UI.Categoy must be an IDpath.
   */
  public static final String OPID_CATEGORY_ID = "categoryId"; //$NON-NLS-1$

  /**
   * Category ID returned by {@link #categoryId()} for Value-object without explicitly specified category ID.
   */
  public static final String UNSPECIFIED_CATEGORY_ID = "uncpecified"; //$NON-NLS-1$

  /**
   * Constructor.
   *
   * @param id String - value-object ID
   * @param keeper {@link IEntityKeeper} - the valobj keeper
   * @param params {@link IOptionSet} - values for {@link #params}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any identifier is not an IDpath
   */
  public TsValobjInfo( String id, IEntityKeeper<?> keeper, IOptionSet params ) {
    StridUtils.checkValidIdPath( id );
    TsNullArgumentRtException.checkNulls( keeper, params );
    this.id = id;
    this.keeper = keeper;
    this.params = params;
  }

  /**
   * Constructor.
   *
   * @param id String - value-object class ID
   * @param keeper {@link IEntityKeeper} - the valobj keeper
   * @param nmName String - short name
   * @param description - full description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any identifier is not an IDpath
   */
  public TsValobjInfo( String id, IEntityKeeper<?> keeper, String nmName, String description ) {
    this( id, keeper, makeParams( nmName, description ) );
  }

  /**
   * Static constructor.
   *
   * @param aId String - value-object class ID
   * @param aKeeper {@link IEntityKeeper} - the valobj keeper
   * @param aCategoryId String - the category ID
   * @param aName String - short name
   * @param aDescription - full description
   * @return {@link TsValobjInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any identifier is not an IDpath
   */
  public static TsValobjInfo create1( String aId, IEntityKeeper<?> aKeeper, String aCategoryId, String aName,
      String aDescription ) {
    TsNullArgumentRtException.checkNulls( aId, aKeeper, aName, aDescription );
    StridUtils.checkValidIdPath( aCategoryId );
    IOptionSetEdit p = new OptionSet();
    p.setStr( TSID_NAME, aName );
    p.setStr( TSID_DESCRIPTION, aDescription );
    p.setStr( OPID_CATEGORY_ID, aCategoryId );
    return new TsValobjInfo( aId, aKeeper, p );
  }

  /**
   * Static constructor.
   *
   * @param aId String - value-object class ID
   * @param aKeeper {@link IEntityKeeper} - the valobj keeper
   * @param aIdsAndValues Object[] - identifier / value pairs
   * @return {@link TsValobjInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any identifier is not an IDpath
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static TsValobjInfo create1( String aId, IEntityKeeper<?> aKeeper, Object... aIdsAndValues ) {
    IOptionSet p = OptionSetUtils.createOpSet( aIdsAndValues );
    TsValobjInfo inf = new TsValobjInfo( aId, aKeeper, p );
    StridUtils.checkValidIdPath( inf.categoryId() );
    return inf;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static IOptionSet makeParams( String nmName, String description ) {
    TsNullArgumentRtException.checkNulls( nmName, description );
    IOptionSetEdit p = new OptionSet();
    p.setStr( TSID_NAME, nmName );
    p.setStr( TSID_DESCRIPTION, description );
    return p;
  }

  // ------------------------------------------------------------------------------------
  // IStridableParameterized
  //

  @Override
  public String nmName() {
    return params.getStr( TSID_NAME, id );
  }

  @Override
  public String description() {
    return params.getStr( DDEF_DESCRIPTION );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the category ID, that is the {@link String} value of the option {@link #OPID_CATEGORY_ID}.
   *
   * @return String - the category ID option value or {@link #UNSPECIFIED_CATEGORY_ID}
   */
  public String categoryId() {
    return params.getStr( OPID_CATEGORY_ID, UNSPECIFIED_CATEGORY_ID );
  }

}
