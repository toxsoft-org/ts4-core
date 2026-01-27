package org.toxsoft.core.tsgui.graphics.image;

import static org.toxsoft.core.tsgui.graphics.image.IThumbSizeConstants.*;
import static org.toxsoft.core.tsgui.graphics.image.ITsResources.*;

import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Image thumbnail size.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EThumbSize
    implements IStridable {

  // constants must be arranged strictly according to the size increase

  SZ16( THUMB_SIZE_ID_016, 16 ),
  SZ24( THUMB_SIZE_ID_024, 24 ),
  SZ32( THUMB_SIZE_ID_032, 32 ),
  SZ48( THUMB_SIZE_ID_048, 48 ),
  SZ64( THUMB_SIZE_ID_064, 64 ),
  SZ96( THUMB_SIZE_ID_096, 96 ),
  SZ128( THUMB_SIZE_ID_128, 128 ),
  SZ180( THUMB_SIZE_ID_180, 180 ),
  SZ256( THUMB_SIZE_ID_256, 256 ),
  SZ360( THUMB_SIZE_ID_360, 360 ),
  SZ512( THUMB_SIZE_ID_512, 512 ),
  SZ724( THUMB_SIZE_ID_724, 724 ),
  SZ1024( THUMB_SIZE_ID_1024, 1024 ),

  ;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "EThumbSize"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<EThumbSize> KEEPER = new StridableEnumKeeper<>( EThumbSize.class );

  private static IStridablesListEdit<EThumbSize> list = null;

  private final String   id;
  private final String   name;
  private final String   description;
  private final int      size;
  private final ITsPoint pointSize;

  EThumbSize( String aId, int aSize ) {
    id = aId;
    size = aSize;
    Integer sz = Integer.valueOf( size );
    name = String.format( FMT_THUMB_SIZE, sz, sz );
    description = String.format( FMT_THUMB_SIZE_D, sz, sz );
    pointSize = new TsPoint( aSize, aSize );
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
   * Returns the thumb size in pixels.
   *
   * @return int - Returns the thumb size in pixels
   */
  public int size() {
    return size;
  }

  /**
   * Returns thumbnail size as {@link ITsPoint}.
   *
   * @return int - thumbnail size as {@link ITsPoint}
   */
  public ITsPoint pointSize() {
    return pointSize;
  }

  /**
   * Returns next (bigger) thumbnail size, up to maximal possible size.
   *
   * @return {@link EThumbSize} - next (bigger) size
   */
  public EThumbSize nextSize() {
    if( isMaxSize() ) {
      return this;
    }
    return values()[ordinal() + 1];
  }

  /**
   * Returns next (bigger) thumbnail size, wrapping to the minimal size after maximal one..
   *
   * @return {@link EThumbSize} - next (bigger) size
   */
  public EThumbSize nextSizeW() {
    if( isMaxSize() ) {
      return minSize();
    }
    return values()[ordinal() + 1];
  }

  /**
   * Returns previous (smaller) size down to the minimal size.
   *
   * @return {@link EThumbSize} - previous (smaller) size
   */
  public EThumbSize prevSize() {
    if( isMinSize() ) {
      return this;
    }
    return values()[ordinal() - 1];
  }

  /**
   * Returns previous (smaller) size, wrapping to the maximal size after minimal one.
   *
   * @return {@link EThumbSize} - previous (smaller) size
   */
  public EThumbSize prevSizeW() {
    if( isMinSize() ) {
      return maxSize();
    }
    return values()[ordinal() - 1];
  }

  /**
   * Determines if this is the minimal thumbnail size.
   *
   * @return boolean - <code>true</code> only for {@link #minSize()}
   */
  public boolean isMinSize() {
    return ordinal() == 0;
  }

  /**
   * Determines if this is the maximal thumbnail size.
   *
   * @return boolean - <code>true</code> only for {@link #maxSize()}
   */
  public boolean isMaxSize() {
    return ordinal() == values().length - 1;
  }

  /**
   * Returns minimal thumbnail size constant.
   *
   * @return {@link EThumbSize} - minimal size
   */
  public static EThumbSize minSize() {
    return values()[0];
  }

  /**
   * Returns maximal thumbnail size constant.
   *
   * @return {@link EThumbSize} - maximal size
   */
  public static EThumbSize maxSize() {
    return values()[values().length - 1];
  }

  /**
   * Finds the smallest size that encloses a rectangle of the specified size.
   * <p>
   * If the dimensions are greater than {@link #maxSize()}, returns {@link #maxSize()}.
   *
   * @param aWidth int - width in pizels
   * @param aHeight int - height in pixels
   * @return {@link EThumbSize} - minimal size that encloses the rectangle of size <code>aWidth x aHeight</code>
   * @throws TsIllegalArgumentRtException any argument < 0
   */
  public static EThumbSize findIncluding( int aWidth, int aHeight ) {
    TsIllegalArgumentRtException.checkTrue( aWidth < 0 || aHeight < 0 );
    int dim = Math.max( aWidth, aHeight );
    for( EThumbSize sz : values() ) {
      if( sz.size >= dim ) {
        return sz;
      }
    }
    return maxSize();
  }

  /**
   * Finds the smallest size that encloses an icon of the specified size.
   *
   * @param aIconSize {@link EIconSize} - the icon size
   * @return {@link EThumbSize} - minimal size that encloses the icon size
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EThumbSize findIncluding( EIconSize aIconSize ) {
    TsNullArgumentRtException.checkNull( aIconSize );
    return findIncluding( aIconSize.size(), aIconSize.size() );
  }

  // ----------------------------------------------------------------------------------
  // Stridable enum common API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EThumbSize} &gt; - list of constants in order of declaration
   */
  public static IStridablesList<EThumbSize> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EThumbSize} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EThumbSize getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EThumbSize} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EThumbSize findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EThumbSize item : values() ) {
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
   * @return {@link EThumbSize} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EThumbSize getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
