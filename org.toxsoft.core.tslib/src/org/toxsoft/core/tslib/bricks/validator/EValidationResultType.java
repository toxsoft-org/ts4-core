package org.toxsoft.core.tslib.bricks.validator;

import static org.toxsoft.core.tslib.bricks.validator.ITsResources.*;
import static org.toxsoft.core.tslib.utils.icons.ITsLibIconIds.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.icons.*;

/**
 * The type of validaton result: success (ok), warning or error.
 *
 * @author hazard157
 */
public enum EValidationResultType
    implements IStridable, IIconIdable {

  /**
   * Validation succeeded without errors or warnings.
   * <p>
   * Please note: {@link ValidationResult#SUCCESS} is the singleton instance of the this type.
   */
  OK( "Ok", STR_N_OK, STR_D_OK, TSLIB_ICONID_INFO ), //$NON-NLS-1$

  /**
   * Validation succeeded but with some warning.
   */
  WARNING( "Warning", STR_N_WARNING, STR_D_WARNING, TSLIB_ICONID_WARNING ), //$NON-NLS-1$

  /**
   * Validation failure with error.
   */
  ERROR( "Error", STR_N_ERROR, STR_D_ERROR, TSLIB_ICONID_ERROR ); //$NON-NLS-1$

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "EValResType"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EValidationResultType> KEEPER =
      new StridableEnumKeeper<>( EValidationResultType.class );

  private static IStridablesList<EValidationResultType> list = null;

  private final String id;
  private final String nmName;
  private final String description;
  private final String iconId;

  /**
   * Constructor.
   *
   * @param aId String - identifier (IDPath)
   * @param aName String - short, human-readable name
   * @param aDescr String - description
   * @param aIconId String - the icon ID
   */
  EValidationResultType( String aId, String aName, String aDescr, String aIconId ) {
    id = aId;
    nmName = aName;
    description = aDescr;
    iconId = aIconId;
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
    return nmName;
  }

  @Override
  public String description() {
    return description;
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return iconId;
  }

  // ------------------------------------------------------------------------------------
  // static API
  //

  /**
   * Returns all constants as ordered {@link IStridablesList}.
   *
   * @return {@link IStridablesList}&lt; {@link EValidationResultType} &gt; - list of all constants in order of
   *         declaration
   */
  public static IStridablesList<EValidationResultType> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  // ------------------------------------------------------------------------------------
  // Search and check methods
  //

  /**
   * Determines if constant with specified identifier ({@link #id()}) exists.
   *
   * @param aId String - identifier of constant to search for
   * @return boolean - <code>true</code> if constant exists
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static boolean isItemById( String aId ) {
    return findById( aId ) != null;
  }

  /**
   * Determines if constant with specified name ({@link #nmName()}) exists.
   *
   * @param aName String - name of constant to search for
   * @return boolean - <code>true</code> if constant exists
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static boolean isItemByName( String aName ) {
    return findByName( aName ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Find & get
  //

  /**
   * Finds constant with specified identifier ({@link #id()}).
   *
   * @param aId String - identifier of constant to search for
   * @return EValidationResultType - found constant or <code>null</code> if no constant exists with specified identifier
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static EValidationResultType findById( String aId ) {
    return asList().findByKey( aId );
  }

  /**
   * Returns constant with specified identifier ({@link #id()}).
   *
   * @param aId String - identifier of constant to search for
   * @return EValidationResultType - found constant
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such constant
   */
  public static EValidationResultType getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds constant with specified name ({@link #nmName()}) exists.
   *
   * @param aName String - identifier of constant to search for
   * @return EValidationResultType - found constant or <code>null</code> if no constant exists with specified name
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static EValidationResultType findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EValidationResultType item : asList() ) {
      if( item.nmName.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns constant with specified name ({@link #nmName()}).
   *
   * @param aName String - identifier of constant to search for
   * @return EValidationResultType - found constant
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such constant
   */
  public static EValidationResultType getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
