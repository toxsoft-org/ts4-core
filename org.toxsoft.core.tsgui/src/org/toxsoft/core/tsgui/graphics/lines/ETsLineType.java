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
 * Разновидности прервистости линии - сплошной и разные пунктиры.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum ETsLineType
    implements IStridable {

  SOLID( "solid", STR_N_LT_SOLID, STR_D_LT_SOLID, SWT.LINE_SOLID ), //$NON-NLS-1$

  DASH( "dash", STR_N_LT_DASH, STR_D_LT_DASH, SWT.LINE_DASH ), //$NON-NLS-1$

  DOT( "dot", STR_N_LT_DOT, STR_D_LT_DOT, SWT.LINE_DOT ), //$NON-NLS-1$

  DASHDOT( "dashdot", STR_N_LT_DASHDOT, STR_D_LT_DASHDOT, SWT.LINE_DASHDOT ), //$NON-NLS-1$

  DASHDOTDOT( "dashdotdot", STR_N_LT_DASHDOTDOT, STR_D_LT_DASHDOTDOT, SWT.LINE_DASHDOTDOT ), //$NON-NLS-1$

  CUSTOM( "custom", STR_N_LT_CUSTOM, STR_D_LT_CUSTOM, SWT.LINE_CUSTOM ); //$NON-NLS-1$

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ETsLineType"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ETsLineType> KEEPER = new StridableEnumKeeper<>( ETsLineType.class );

  private static IStridablesListEdit<ETsLineType> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final int    swtStyle;

  ETsLineType( String aId, String aName, String aDescription, int aSwtStyle ) {
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
   * Returns one of the values {@link SWT#LINE_SOLID}, {@link SWT#LINE_DASH}, {@link SWT#LINE_DOT},
   * {@link SWT#LINE_DASHDOT}, {@link SWT#LINE_DASHDOTDOT} or {@link SWT#LINE_CUSTOM}.
   *
   * @return int - SWT line style
   */
  public int getSwtStyle() {
    return swtStyle;
  }

  /**
   * Returns the constant corresponding to the specified SWT style.
   *
   * @param aSwtStyle int - SWT style of constant
   * @return {@link ETsLineType} - found constant
   * @throws TsIllegalArgumentRtException invalid SWT style
   */
  public static ETsLineType getBySwtStyle( int aSwtStyle ) {
    return TsIllegalArgumentRtException.checkNull( findBySwtStyle( aSwtStyle ) );
  }

  /**
   * Finds the constant corresponding to the specified SWT style.
   *
   * @param aSwtStyle int - SWT style of constant
   * @return {@link ETsLineType} - found constant or <code>null</code>
   */
  public static ETsLineType findBySwtStyle( int aSwtStyle ) {
    switch( aSwtStyle ) {
      case SWT.LINE_SOLID: {
        return ETsLineType.SOLID;
      }
      case SWT.LINE_DASH: {
        return ETsLineType.DASH;
      }
      case SWT.LINE_DOT: {
        return ETsLineType.DOT;
      }
      case SWT.LINE_DASHDOT: {
        return ETsLineType.DASHDOT;
      }
      case SWT.LINE_DASHDOTDOT: {
        return ETsLineType.DASHDOTDOT;
      }
      case SWT.LINE_CUSTOM: {
        return ETsLineType.CUSTOM;
      }
      default:
        return null;
    }
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ETsLineType} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ETsLineType> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ETsLineType} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ETsLineType getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ETsLineType} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ETsLineType findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETsLineType item : values() ) {
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
   * @return {@link ETsLineType} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ETsLineType getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
