package org.toxsoft.core.tsgui.graphics.colors;

import static org.toxsoft.core.tsgui.graphics.colors.ITsResources.*;

import org.eclipse.swt.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The SWT system color corresponding to the <code>SWT.COLOR_XXX</code> constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum ESwtSysColor
    implements IStridable {

  // SWT.COLOR_WIDGET_DARK_SHADOW
  // SWT.COLOR_WIDGET_DISABLED_FOREGROUND
  // SWT.COLOR_WIDGET_NORMAL_SHADOW
  // SWT.COLOR_WIDGET_LIGHT_SHADOW
  // SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW
  // SWT.COLOR_TEXT_DISABLED_BACKGROUND
  // SWT.COLOR_WIDGET_BACKGROUND
  // SWT.COLOR_WIDGET_BORDER
  // SWT.COLOR_WIDGET_FOREGROUND
  // SWT.COLOR_LIST_FOREGROUND
  // SWT.COLOR_LIST_BACKGROUND
  // SWT.COLOR_LIST_SELECTION
  // SWT.COLOR_LIST_SELECTION_TEXT
  // SWT.COLOR_LINK_FOREGROUND
  // SWT.COLOR_INFO_FOREGROUND
  // SWT.COLOR_INFO_BACKGROUND
  // SWT.COLOR_TITLE_FOREGROUND
  // SWT.COLOR_TITLE_BACKGROUND
  // SWT.COLOR_TITLE_BACKGROUND_GRADIENT
  // SWT.COLOR_TITLE_INACTIVE_FOREGROUND
  // SWT.COLOR_TITLE_INACTIVE_BACKGROUND
  // SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT

  SYSCOL_LINK_FOREGROUND( "syscol_link_foreground", SWT.COLOR_LINK_FOREGROUND, // //$NON-NLS-1$
      STR_SYSCOL_LINK_FOREGROUND, STR_SYSCOL_LINK_FOREGROUND_D ),

  // FIXME add colors

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ESwtSysColor"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ESwtSysColor> KEEPER = new StridableEnumKeeper<>( ESwtSysColor.class );

  private static IStridablesListEdit<ESwtSysColor> list = null;

  private final String id;
  private final int    swtColorId;
  private final String name;
  private final String description;

  ESwtSysColor( String aId, int aSwtColorId, String aName, String aDescription ) {
    id = aId;
    swtColorId = aSwtColorId;
    name = aName;
    description = aDescription;
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
   * Returns SWT color id.
   *
   * @return int SWT color number
   */
  public int swtColorId() {
    return swtColorId;
  }

  /**
   * Returns enum element by its SWT color id
   *
   * @param aSwtColorId
   * @return {@link ESwtSysColor} - enum element
   */
  public static ESwtSysColor findBySwtColorId( int aSwtColorId ) {
    for( ESwtSysColor eColor : values() ) {
      if( eColor.getSwtColorId() == aSwtColorId ) {
        return eColor;
      }
    }
    return null;
  }

  /**
   * Returns SWT color id for enum lement
   *
   * @return int SWT color id
   */
  public int getSwtColorId() {
    return swtColorId;
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ESwtSysColor} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ESwtSysColor> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ESwtSysColor} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ESwtSysColor getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ESwtSysColor} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ESwtSysColor findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ESwtSysColor item : values() ) {
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
   * @return {@link ESwtSysColor} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ESwtSysColor getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
