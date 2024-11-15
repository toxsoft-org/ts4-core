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
 * Horizontal alignment type.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EHorAlignment
    implements IStridable {

  LEFT( "Left", STR_HA_LEFT, STR_HA_LEFT_D, SWT.LEFT ), //$NON-NLS-1$

  CENTER( "Center", STR_HA_CENTER, STR_HA_CENTER_D, SWT.CENTER ), //$NON-NLS-1$

  RIGHT( "Right", STR_HA_RIGHT, STR_HA_RIGHT_D, SWT.RIGHT ), //$NON-NLS-1$

  FILL( "Fill", STR_HA_FILL, STR_HA_FILL_D, SWT.FILL ); //$NON-NLS-1$

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "HorAlignment"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static IEntityKeeper<EHorAlignment> KEEPER = new StridableEnumKeeper<>( EHorAlignment.class );

  private static IStridablesListEdit<EHorAlignment> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final int    swtStyle;

  EHorAlignment( String aId, String aName, String aDescr, int aSwtStyle ) {
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
   * Method returns respective style, one of the {@link SWT#LEFT} ({@link SWT#LEAD}), {@link SWT#CENTER},
   * {@link SWT#RIGHT} ({@link SWT#TRAIL}), {@link SWT#FILL}.
   *
   * @return int - the {@link SWT} alignment style
   */
  public int swtStyle() {
    return swtStyle;
  }

  /**
   * Calculates the X coordinate of the segment left point, located relative to the anchor point.
   *
   * @param aX int - anchor point X coordinate
   * @param aWidth int - the width of the segment to be placed according to the passed parameters
   * @return int - X coordinate of the segment left point
   */
  public int computeX( int aX, int aWidth ) {
    return switch( this ) {
      case LEFT -> aX;
      case CENTER -> aX - aWidth / 2;
      case RIGHT -> aX - aWidth;
      case FILL -> aX;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  /**
   * Calculates the X-coordinate of the foreground segment left point, above the background segment.
   *
   * @param aBackWidth int - the background segment width
   * @param aForeWidth int - the foreground segment width
   * @return int - X-coordinate of the foreground segment left point
   */
  public int relativeX( int aBackWidth, int aForeWidth ) {
    return switch( this ) {
      case LEFT -> 0;
      case CENTER -> (aBackWidth - aForeWidth) / 2;
      case RIGHT -> aBackWidth - aForeWidth;
      case FILL -> 0;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EHorAlignment} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EHorAlignment> asList() {
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
   * @return {@link EHorAlignment} - found constant or <code>null</code>
   */
  public static final EHorAlignment findBySwtStyle( int aSwtStyle ) {
    return switch( aSwtStyle & (SWT.LEFT | SWT.CENTER | SWT.RIGHT) ) {
      case SWT.LEFT -> /* case SWT.LEAD: */ LEFT;
      case SWT.CENTER -> CENTER;
      case SWT.RIGHT -> /* case SWT.TRAIL: */ RIGHT;
      default -> null;
    };
  }

  /**
   * Returns the constant by the SWT style value.
   * <p>
   * Simply returns constant found by {@link #findBySwtStyle1(int)} or throws an exception.
   *
   * @param aSwtStyle int - the SWT style word
   * @return {@link EHorAlignment} - found constant
   * @throws TsItemNotFoundRtException constant not found
   */
  public static final EHorAlignment getBySwtStyle( int aSwtStyle ) {
    return TsItemNotFoundRtException.checkNull( findBySwtStyle( aSwtStyle ) );
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EHorAlignment} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EHorAlignment getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EHorAlignment} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EHorAlignment findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EHorAlignment item : values() ) {
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
   * @return {@link EHorAlignment} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EHorAlignment getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
