package org.toxsoft.core.tslib.utils.valobj;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.gui.*;

/**
 * Value object class information used to register value-oebjcts.
 * <p>
 * This information is specified by the user during registration in {@link TsValobjUtils}.
 *
 * @author hazard157
 * @param id String - value-object ID (IDpath)
 * @param keeper {@link IEntityKeeper} - the valobj keeper (contains reference to the value-object {@link Class})
 * @param nmName String - short name, a non-blank string
 * @param description - full description
 * @param iconId String - {@link IIconIdable#iconId()} value, an IDpath or <code>null</code>
 */
public record TsValobjRegData ( String id, IEntityKeeper<?> keeper, String nmName, String description, String iconId )
    implements IStridable, IIconIdable {

  /**
   * Constructor.
   *
   * @param id String - value-object ID
   * @param nmName String - short name
   * @param keeper {@link IEntityKeeper} - the valobj keeper
   * @param description - full description
   * @param iconId String - {@link IIconIdable#iconId()} value, an IDpath or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any identifier is not an IDpath
   * @throws TsIllegalArgumentRtException <code>nmName</code> is a blank string
   */
  public TsValobjRegData( String id, IEntityKeeper<?> keeper, String nmName, String description, String iconId ) {
    this.id = StridUtils.checkValidIdPath( id );
    this.nmName = TsErrorUtils.checkNonBlank( nmName );
    TsNullArgumentRtException.checkNulls( keeper, description );
    this.keeper = keeper;
    this.description = description;
    if( iconId != null ) {
      StridUtils.checkValidIdPath( iconId );
    }
    this.iconId = iconId;
  }

  /**
   * Constructor without icon ID.
   *
   * @param id String - value-object class ID
   * @param nmName String - short name
   * @param keeper {@link IEntityKeeper} - the valobj keeper
   * @param description - full description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any identifier is not an IDpath
   */
  public TsValobjRegData( String id, IEntityKeeper<?> keeper, String nmName, String description ) {
    this( id, keeper, nmName, description, null );
  }

}
