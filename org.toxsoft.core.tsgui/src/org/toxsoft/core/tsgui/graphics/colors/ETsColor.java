package org.toxsoft.core.tsgui.graphics.colors;

import static org.toxsoft.core.tsgui.graphics.colors.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Constants of predefined colors.
 * <p>
 * Attention: these are colors that have <b>specified</b> values of the RGB components. That is, there can be no system
 * "default background" colors, etc., but there are "dark red" and other predefined colors.
 * <p>
 * Includes 16 colors defined by the SWT.COLOR_XXX constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum ETsColor
    implements IStridable {

  BLACK( "Black", STR_BLACK, STR_BLACK_D, //$NON-NLS-1$
      new RGB( 0x00, 0x00, 0x00 ), SWT.COLOR_BLACK ),

  DARK_RED( "DarkRed", STR_DARK_RED, STR_DARK_RED_D, //$NON-NLS-1$
      new RGB( 0x80, 0x00, 0x00 ), SWT.COLOR_DARK_RED ),

  DARK_GREEN( "DarkGreen", STR_DARK_GREEN, STR_DARK_GREEN_D, //$NON-NLS-1$
      new RGB( 0x00, 0x80, 0x00 ), SWT.COLOR_DARK_GREEN ),

  DARK_YELLOW( "DarkYellow", STR_DARK_YELLOW, STR_DARK_YELLOW_D, //$NON-NLS-1$
      new RGB( 0x80, 0x80, 0x00 ), SWT.COLOR_DARK_YELLOW ),

  DARK_BLUE( "DarkBlue", STR_DARK_BLUE, STR_DARK_BLUE_D, //$NON-NLS-1$
      new RGB( 0x00, 0x00, 0x80 ), SWT.COLOR_DARK_BLUE ),

  DARK_MAGENTA( "DarkMagenta", STR_DARK_MAGENTA, STR_DARK_MAGENTA_D, //$NON-NLS-1$
      new RGB( 0x80, 0x00, 0x80 ), SWT.COLOR_DARK_MAGENTA ),

  DARK_CYAN( "DarkCyan", STR_DARK_CYAN, STR_DARK_CYAN_D, //$NON-NLS-1$
      new RGB( 0x00, 0x80, 0x80 ), SWT.COLOR_DARK_CYAN ),

  GRAY( "Gray", STR_GRAY, STR_GRAY_D, //$NON-NLS-1$
      new RGB( 0xC0, 0xC0, 0xC0 ), SWT.COLOR_GRAY ),

  DARK_GRAY( "DarkGray", STR_DARK_GRAY, STR_DARK_GRAY_D, //$NON-NLS-1$
      new RGB( 0x80, 0x80, 0x80 ), SWT.COLOR_DARK_GRAY ),

  RED( "Red", STR_RED, STR_RED_D, //$NON-NLS-1$
      new RGB( 0xFF, 0x00, 0x00 ), SWT.COLOR_RED ),

  GREEN( "Green", STR_GREEN, STR_GREEN_D, //$NON-NLS-1$
      new RGB( 0x00, 0xFF, 0x00 ), SWT.COLOR_GREEN ),

  YELLOW( "Yellow", STR_YELLOW, STR_YELLOW_D, //$NON-NLS-1$
      new RGB( 0xFF, 0xFF, 0x00 ), SWT.COLOR_YELLOW ),

  BLUE( "Blue", STR_BLUE, STR_BLUE_D, //$NON-NLS-1$
      new RGB( 0x00, 0x00, 0xFF ), SWT.COLOR_BLUE ),

  MAGENTA( "Magenta", STR_MAGENTA, STR_MAGENTA_D, //$NON-NLS-1$
      new RGB( 0xFF, 0x00, 0xFF ), SWT.COLOR_MAGENTA ),

  CYAN( "Cyan", STR_CYAN, STR_CYAN_D, //$NON-NLS-1$
      new RGB( 0x00, 0xFF, 0xFF ), SWT.COLOR_CYAN ),

  WHITE( "White", STR_WHITE, STR_WHITE_D, //$NON-NLS-1$
      new RGB( 0xFF, 0xFF, 0xFF ), SWT.COLOR_WHITE );

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "TsColor"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ETsColor> KEEPER = new StridableEnumKeeper<>( ETsColor.class );

  private static IStridablesListEdit<ETsColor> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final RGB    rgb;
  private final RGBA   rgba;
  private final int    swtColorCode;

  ETsColor( String aId, String aName, String aDescr, RGB aRgb, int aSwtColorCode ) {
    id = aId;
    name = aName;
    description = aDescr;
    rgb = aRgb;
    rgba = new RGBA( aRgb.red, aRgb.green, aRgb.blue, 255 );
    swtColorCode = aSwtColorCode;
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
   * Returns the SWT color code (one of the <code>SWT.COLOR_XXX</code> constants).
   *
   * @return int - SWT color code (one of <code>SWT.COLOR_XXX</code> constants)
   */
  public int swtColorCode() {
    return swtColorCode;
  }

  /**
   * Returns color as {@link RGB} - Red, Green and Blue components values.
   *
   * @return {@link RGB} - Red, Green and Blue components values
   */
  public RGB rgb() {
    return rgb;
  }

  /**
   * Returns color as opaque {@link RGBA} - Red, Green and Blue components values.
   *
   * @return {@link RGB} - opaque Red, Green and Blue components values
   */
  public RGBA rgba() {
    return rgba;
  }

  /**
   * Finds the constant by the specified {@link RGB}.
   *
   * @param aRgb RGB - {@link RGB} to find the {@link ETsColor}
   * @return {@link ETsColor} -found color or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ETsColor findByRgb( RGB aRgb ) {
    TsNullArgumentRtException.checkNull( aRgb );
    for( ETsColor item : values() ) {
      if( item.rgb.equals( aRgb ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the constant by the specified {@link RGB}.
   *
   * @param aRgb RGB - {@link RGB} to find the {@link ETsColor}
   * @return {@link ETsColor} - found color
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no {@link ETsColor} exists with the specified RGB
   */
  public static ETsColor getByRgb( RGB aRgb ) {
    return TsItemNotFoundRtException.checkNull( findByRgb( aRgb ) );
  }

  // ----------------------------------------------------------------------------------
  // Stridable enum common API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ETsColor} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ETsColor> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ETsColor} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ETsColor getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ETsColor} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ETsColor findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETsColor item : values() ) {
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
   * @return {@link ETsColor} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ETsColor getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
