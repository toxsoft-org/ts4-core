package org.toxsoft.core.jasperreports.gui.main;

import static org.toxsoft.core.jasperreports.gui.main.ITsResources.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The zoom factors available for viewer scaling.
 *
 * @author goga
 */
@SuppressWarnings( "javadoc" )
public enum EZoomType
    implements IStridable {

  ZOOM_10( 10 ),
  ZOOM_20( 20 ),
  ZOOM_30( 30 ),
  ZOOM_40( 40 ),
  ZOOM_50( 50 ),
  ZOOM_75( 75 ),
  ZOOM_100( 100 ),
  ZOOM_125( 125 ),
  ZOOM_150( 150 ),
  ZOOM_175( 175 ),
  ZOOM_200( 200 ),
  ZOOM_250( 250 ),
  ZOOM_300( 300 ),
  ZOOM_400( 400 ),
  ZOOM_500( 500 ),

  ;

  private static IStridablesListEdit<EZoomType> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final int    percents;
  private final float  zoomFactor;

  EZoomType( int aPercents ) {
    percents = aPercents;
    zoomFactor = (float)((percents) / 100.0);
    id = "ZOOM_" + percents; //$NON-NLS-1$
    name = String.format( FMT_ZOOM_NAME, Integer.valueOf( percents ) );
    description = String.format( FMT_ZOOM_DESCRITPION, Integer.valueOf( percents ) );
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

  public float zoomFactor() {
    return zoomFactor;
  }

  /**
   * Returns the enum constant nearest to the specified zoom factor.
   *
   * @param aFactorValue double - zoom factor (where 1.0 is 100% zoom)
   * @return
   */
  public static EZoomType findByFactorValue( double aFactorValue ) {
    EZoomType bestZoom = EZoomType.ZOOM_100;
    int diff = Integer.MAX_VALUE;
    int perc = (int)(aFactorValue * 100.0);
    for( EZoomType z : asList() ) {
      int d = Math.abs( z.percents - perc );
      if( d < diff ) {
        bestZoom = z;
      }
    }
    return bestZoom;
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EZoomType} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EZoomType> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EZoomType} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EZoomType getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EZoomType} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EZoomType findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EZoomType item : values() ) {
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
   * @return {@link EZoomType} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EZoomType getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
