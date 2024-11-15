package org.toxsoft.core.tsgui.graphics;

import static org.toxsoft.core.tsgui.graphics.ITsResources.*;

import org.eclipse.swt.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * A type of rectangular border that frames a certain area on the screen.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EBorderType
    implements IStridable {

  NONE( "BorderNone", STR_BT_NONE, STR_BT_NONE_D, SWT.NONE ), //$NON-NLS-1$

  LINE( "BorderLine", STR_BT_LINE, STR_BT_LINE_D, SWT.SHADOW_NONE ), //$NON-NLS-1$

  ETCHED( "BorderEtched", STR_BT_ECTHED, STR_BT_ECTHED_D, SWT.SHADOW_ETCHED_IN ), //$NON-NLS-1$

  CONVEX( "BorderConvex", STR_BT_CONVEX, STR_BT_CONVEX_D, SWT.SHADOW_ETCHED_OUT ), //$NON-NLS-1$

  BEVEL_INNER( "BorderBevelInner", STR_BT_BEVEL_INNER, STR_BT_BEVEL_INNER_D, SWT.SHADOW_IN ), //$NON-NLS-1$

  BEVEL_OUTER( "BorderBevelOuter", STR_BT_BEVEL_OUTER, STR_BT_BEVEL_OUTER_D, SWT.SHADOW_OUT ); //$NON-NLS-1$

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "EBorderType"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EBorderType> KEEPER = new StridableEnumKeeper<>( EBorderType.class );

  private static IStridablesListEdit<EBorderType> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final int    swtStyle;

  EBorderType( String aId, String aName, String aDescr, int aSwtStyle ) {
    id = aId;
    name = aName;
    description = aDescr;
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

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the {@link SWT} style corresponding to this constant.
   * <p>
   * For {@link #NONE} returns {@link SWT#NONE}, for other constants returns respective style, one of the
   * {@link SWT#SHADOW_NONE}, {@link SWT#SHADOW_IN}, {@link SWT#SHADOW_OUT}, {@link SWT#SHADOW_ETCHED_IN},
   * {@link SWT#SHADOW_ETCHED_OUT}.
   *
   * @return int - the {@link SWT} border style
   */
  public int swtStyle() {
    return swtStyle;
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EBorderType} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EBorderType> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EBorderType} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EBorderType getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EBorderType} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EBorderType findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EBorderType item : values() ) {
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
   * @return {@link EBorderType} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EBorderType getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
