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

  SYSCOL_WIDGET_DARK_SHADOW( "syscol_WIDGET_DARK_SHADOW", SWT.COLOR_WIDGET_DARK_SHADOW, // //$NON-NLS-1$
      STR_SYSCOL_WIDGET_DARK_SHADOW, STR_SYSCOL_WIDGET_DARK_SHADOW_D ),

  SYSCOL_WIDGET_DISABLED_FOREGROUND( "syscol_WIDGET_DISABLED_FOREGROUND", SWT.COLOR_WIDGET_DISABLED_FOREGROUND, // //$NON-NLS-1$
      STR_SYSCOL_WIDGET_DISABLED_FOREGROUND, STR_SYSCOL_WIDGET_DISABLED_FOREGROUND_D ),

  SYSCOL_WIDGET_NORMAL_SHADOW( "syscol_WIDGET_NORMAL_SHADOW", SWT.COLOR_WIDGET_NORMAL_SHADOW, // //$NON-NLS-1$
      STR_SYSCOL_WIDGET_NORMAL_SHADOW, STR_SYSCOL_WIDGET_NORMAL_SHADOW_D ),

  SYSCOL_WIDGET_LIGHT_SHADOW( "syscol_WIDGET_LIGHT_SHADOW", SWT.COLOR_WIDGET_LIGHT_SHADOW, // //$NON-NLS-1$
      STR_SYSCOL_WIDGET_LIGHT_SHADOW, STR_SYSCOL_WIDGET_LIGHT_SHADOW_D ),

  SYSCOL_WIDGET_HIGHLIGHT_SHADOW( "syscol_WIDGET_HIGHLIGHT_SHADOW", SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW, // //$NON-NLS-1$
      STR_SYSCOL_WIDGET_HIGHLIGHT_SHADOW, STR_SYSCOL_WIDGET_HIGHLIGHT_SHADOW_D ),

  SYSCOL_TEXT_DISABLED_BACKGROUND( "syscol_TEXT_DISABLED_BACKGROUND", SWT.COLOR_TEXT_DISABLED_BACKGROUND, // //$NON-NLS-1$
      STR_SYSCOL_TEXT_DISABLED_BACKGROUND, STR_SYSCOL_TEXT_DISABLED_BACKGROUND_D ),

  SYSCOL_WIDGET_BACKGROUND( "syscol_WIDGET_BACKGROUND", SWT.COLOR_WIDGET_BACKGROUND, // //$NON-NLS-1$
      STR_SYSCOL_WIDGET_BACKGROUND, STR_SYSCOL_WIDGET_BACKGROUND_D ),

  SYSCOL_WIDGET_BORDER( "syscol_WIDGET_BORDER", SWT.COLOR_WIDGET_BORDER, // //$NON-NLS-1$
      STR_SYSCOL_WIDGET_BORDER, STR_SYSCOL_WIDGET_BORDER_D ),

  // SYSCOL_WIDGET_FOREGROUND ( "syscol_WIDGET_FOREGROUND" , SWT.COLOR_WIDGET_FOREGROUND, // //$NON-NLS-1$

  // SYSCOL_LIST_FOREGROUND ( "syscol_LIST_FOREGROUND" , SWT.COLOR_LIST_FOREGROUND, // //$NON-NLS-1$

  // SYSCOL_LIST_BACKGROUND ( "syscol_LIST_BACKGROUND" , SWT.COLOR_LIST_BACKGROUND, // //$NON-NLS-1$

  // SYSCOL_LIST_SELECTION ( "syscol_LIST_SELECTION" , SWT.COLOR_LIST_SELECTION, // //$NON-NLS-1$

  // SYSCOL_LIST_SELECTION_TEXT ( "syscol_LIST_SELECTION_TEXT" , SWT.COLOR_LIST_SELECTION_TEXT, // //$NON-NLS-1$

  SYSCOL_LINK_FOREGROUND( "syscol_link_foreground", SWT.COLOR_LINK_FOREGROUND, // //$NON-NLS-1$
      STR_SYSCOL_LINK_FOREGROUND, STR_SYSCOL_LINK_FOREGROUND_D ),

  // SYSCOL_INFO_FOREGROUND ( "syscol_INFO_FOREGROUND" , SWT.COLOR_INFO_FOREGROUND, // //$NON-NLS-1$

  // SYSCOL_INFO_BACKGROUND ( "syscol_INFO_BACKGROUND" , SWT.COLOR_INFO_BACKGROUND, // //$NON-NLS-1$

  // SYSCOL_TITLE_FOREGROUND ( "syscol_TITLE_FOREGROUND" , SWT.COLOR_TITLE_FOREGROUND, // //$NON-NLS-1$

  // SYSCOL_TITLE_BACKGROUND ( "syscol_TITLE_BACKGROUND" , SWT.COLOR_TITLE_BACKGROUND, // //$NON-NLS-1$

  // SYSCOL_TITLE_BACKGROUND_GRADIENT ( "syscol_TITLE_BACKGROUND_GRADIENT" , SWT.COLOR_TITLE_BACKGROUND_GRADIENT, //
  // //$NON-NLS-1$

  // SYSCOL_TITLE_INACTIVE_FOREGROUND ( "syscol_TITLE_INACTIVE_FOREGROUND" , SWT.COLOR_TITLE_INACTIVE_FOREGROUND, //
  // //$NON-NLS-1$

  // SYSCOL_TITLE_INACTIVE_BACKGROUND ( "syscol_TITLE_INACTIVE_BACKGROUND" , SWT.COLOR_TITLE_INACTIVE_BACKGROUND, //
  // //$NON-NLS-1$

  // SYSCOL_TITLE_INACTIVE_BACKGROUND_GRADIENT ( "syscol_TITLE_INACTIVE_BACKGROUND_GRADIENT" ,
  // SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT, // //$NON-NLS-1$

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
