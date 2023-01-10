package org.toxsoft.core.tsgui.graphics.lines;

import static org.toxsoft.core.tsgui.graphics.lines.ITsResources.*;

import org.eclipse.swt.*;
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
public enum ETsLineCapStyle
    implements IStridable {

  FLAT( "Flat", STR_D_LCS_FLAT, STR_N_LCS_FLAT, SWT.CAP_FLAT ), //$NON-NLS-1$

  ROUND( "Round", STR_D_LCS_ROUND, STR_N_LCS_ROUND, SWT.CAP_ROUND ), //$NON-NLS-1$

  SQUARE( "Square", STR_D_LCS_SQUARE, STR_N_LCS_SQUARE, SWT.CAP_SQUARE ); //$NON-NLS-1$

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ETsLineCapStyle"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ETsLineCapStyle> KEEPER = new StridableEnumKeeper<>( ETsLineCapStyle.class );

  private static IStridablesListEdit<ETsLineCapStyle> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final int    swtStyle;

  ETsLineCapStyle( String aId, String aName, String aDescription, int aSwtStyle ) {
    id = aId;
    name = aName;
    description = aDescription;
    swtStyle = aSwtStyle;
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

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns an SWT style value, corrsponding to this constant.
   * <p>
   * Returns one of the values {@link SWT#CAP_FLAT}, {@link SWT#CAP_ROUND} or {@link SWT#CAP_SQUARE}
   *
   * @return int - SWT line cap style
   */
  public int getSwtStyle() {
    return swtStyle;
  }

  /**
   * Returns the constant corresponding to the specified SWT style.
   *
   * @param aSwtStyle int - SWT style of constant
   * @return {@link ETsLineCapStyle} - found constant
   * @throws TsIllegalArgumentRtException invalid SWT style
   */
  public static ETsLineCapStyle getBySwtStyle( int aSwtStyle ) {
    return TsIllegalArgumentRtException.checkNull( findBySwtStyle( aSwtStyle ) );
  }

  /**
   * Finds the constant corresponding to the specified SWT style.
   *
   * @param aSwtStyle int - SWT style of constant
   * @return {@link ETsLineCapStyle} - found constant or <code>null</code>
   */
  public static ETsLineCapStyle findBySwtStyle( int aSwtStyle ) {
    switch( aSwtStyle ) {
      case SWT.CAP_FLAT: {
        return ETsLineCapStyle.FLAT;
      }
      case SWT.CAP_ROUND: {
        return ETsLineCapStyle.ROUND;
      }
      case SWT.CAP_SQUARE: {
        return ETsLineCapStyle.SQUARE;
      }
      default:
        return null;
    }
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ETsLineCapStyle} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ETsLineCapStyle> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ETsLineCapStyle} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ETsLineCapStyle getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ETsLineCapStyle} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ETsLineCapStyle findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETsLineCapStyle item : values() ) {
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
   * @return {@link ETsLineCapStyle} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ETsLineCapStyle getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
