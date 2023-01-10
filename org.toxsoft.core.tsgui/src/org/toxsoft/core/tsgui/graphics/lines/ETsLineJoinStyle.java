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
public enum ETsLineJoinStyle
    implements IStridable {

  ROUND( "Round", STR_D_LJS_ROUND, STR_N_LJS_ROUND, SWT.JOIN_ROUND ), //$NON-NLS-1$

  BEVEL( "Bevel", STR_D_LJS_BEVEL, STR_N_LJS_BEVEL, SWT.JOIN_BEVEL ), //$NON-NLS-1$

  MITER( "Miter", STR_D_LJS_MITER, STR_N_LJS_MITER, SWT.JOIN_MITER ); //$NON-NLS-1$

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ETsLineJoinStyle"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ETsLineJoinStyle> KEEPER = new StridableEnumKeeper<>( ETsLineJoinStyle.class );

  private static IStridablesListEdit<ETsLineJoinStyle> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final int    swtStyle;

  ETsLineJoinStyle( String aId, String aName, String aDescription, int aSwtStyle ) {
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
   * Returns one of the values {@link SWT#JOIN_BEVEL}, {@link SWT#JOIN_ROUND} or{@link SWT#JOIN_MITER}.
   *
   * @return int - SWT line join style
   */
  public int getSwtStyle() {
    return swtStyle;
  }

  /**
   * Returns the constant corresponding to the specified SWT style.
   *
   * @param aSwtStyle int - SWT style of constant
   * @return {@link ETsLineJoinStyle} - found constant
   * @throws TsIllegalArgumentRtException invalid SWT style
   */
  public static ETsLineJoinStyle getBySwtStyle( int aSwtStyle ) {
    return TsIllegalArgumentRtException.checkNull( findBySwtStyle( aSwtStyle ) );
  }

  /**
   * Finds the constant corresponding to the specified SWT style.
   *
   * @param aSwtStyle int - SWT style of constant
   * @return {@link ETsLineJoinStyle} - found constant or <code>null</code>
   */
  public static ETsLineJoinStyle findBySwtStyle( int aSwtStyle ) {
    switch( aSwtStyle ) {
      case SWT.JOIN_MITER: {
        return ETsLineJoinStyle.MITER;
      }
      case SWT.JOIN_ROUND: {
        return ETsLineJoinStyle.ROUND;
      }
      case SWT.JOIN_BEVEL: {
        return ETsLineJoinStyle.BEVEL;
      }
      default:
        return null;
    }
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ETsLineJoinStyle} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ETsLineJoinStyle> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ETsLineJoinStyle} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ETsLineJoinStyle getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ETsLineJoinStyle} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ETsLineJoinStyle findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETsLineJoinStyle item : values() ) {
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
   * @return {@link ETsLineJoinStyle} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ETsLineJoinStyle getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
