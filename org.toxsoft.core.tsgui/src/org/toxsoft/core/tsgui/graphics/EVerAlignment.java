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
 * Vertical alignment type.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EVerAlignment
    implements IStridable {

  TOP( "Top", STR_VA_TOP, STR_VA_TOP_D, SWT.TOP ), //$NON-NLS-1$

  CENTER( "Center", STR_VA_CENTER, STR_VA_CENTER_D, SWT.CENTER ), //$NON-NLS-1$

  BOTTOM( "Bottom", STR_VA_BOTTOM, STR_VA_BOTTOM_D, SWT.BOTTOM ), //$NON-NLS-1$

  FILL( "Fill", STR_VA_FILL, STR_VA_FILL_D, SWT.FILL ); //$NON-NLS-1$

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "VerAlignment"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static IEntityKeeper<EVerAlignment> KEEPER = new StridableEnumKeeper<>( EVerAlignment.class );

  private static IStridablesListEdit<EVerAlignment> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final int    swtStyle;

  EVerAlignment( String aId, String aName, String aDescr, int aSwtStyle ) {
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
   * Method returns respective style, one of the {@link SWT#TOP} ({@link SWT#UP}), {@link SWT#CENTER},
   * {@link SWT#BOTTOM} ({@link SWT#DOWN}), {@link SWT#FILL}.
   *
   * @return int - the {@link SWT} alignment style
   */
  public int swtStyle() {
    return swtStyle;
  }

  /**
   * Calculates the Y coordinate of the segment left point, located relative to the anchor point.
   *
   * @param aY int - anchor point Y coordinate
   * @param aHeight int - the height of the segment to be placed according to the passed parameters
   * @return int - Y coordinate of the segment left point
   */
  public int computeY( int aY, int aHeight ) {
    return switch( this ) {
      case TOP -> aY;
      case CENTER -> aY - aHeight / 2;
      case BOTTOM -> aY - aHeight;
      case FILL -> aY;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  /**
   * Calculates the Y-coordinate of the foreground segment left point, above the background segment.
   *
   * @param aBackgroundHeight int - the background segment height
   * @param aForegroundHeight int - the foreground segment height
   * @return int - Y-coordinate of the foreground segment left point
   */
  public int relativeY( int aBackgroundHeight, int aForegroundHeight ) {
    return switch( this ) {
      case TOP -> 0;
      case CENTER -> (aBackgroundHeight - aForegroundHeight) / 2;
      case BOTTOM -> aBackgroundHeight - aForegroundHeight;
      case FILL -> 0;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EVerAlignment} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EVerAlignment> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Finds the constant by the SWT style value.
   * <p>
   * Argument must contain only one of the bits listed in {@link #swtStyle()}, otherwise <code>null</code> will be
   * returned. Other bits are ignored.
   *
   * @param aSwtStyle int - the SWT style word
   * @return {@link EVerAlignment} - found constant or <code>null</code>
   */
  public static final EVerAlignment findBySwtStyle( int aSwtStyle ) {
    return switch( aSwtStyle & (SWT.TOP | SWT.CENTER | SWT.BOTTOM) ) {
      case SWT.TOP -> /* case SWT.UP: */ TOP;
      case SWT.CENTER -> CENTER;
      case SWT.BOTTOM -> /* case SWT.DOWN: */ BOTTOM;
      default -> null;
    };
  }

  /**
   * Returns the constant by the SWT style value.
   * <p>
   * Simply returns constant found by {@link #findBySwtStyle(int)} or throws an exception.
   *
   * @param aSwtStyle int - the SWT style word
   * @return {@link EVerAlignment} - found constant
   * @throws TsItemNotFoundRtException constant not found
   */
  public static final EVerAlignment getBySwtStyle( int aSwtStyle ) {
    return TsItemNotFoundRtException.checkNull( findBySwtStyle( aSwtStyle ) );
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EVerAlignment} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EVerAlignment getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EVerAlignment} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EVerAlignment findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EVerAlignment item : values() ) {
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
   * @return {@link EVerAlignment} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EVerAlignment getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
