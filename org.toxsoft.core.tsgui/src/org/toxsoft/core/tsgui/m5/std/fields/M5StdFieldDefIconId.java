package org.toxsoft.core.tsgui.m5.std.fields;

import static org.toxsoft.core.tsgui.m5.std.fields.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Attribute {@link IIconIdable#iconId()}.
 *
 * @author hazard157
 * @param <T> - modelled {@link IIconIdable} entity type
 */
public class M5StdFieldDefIconId<T extends IIconIdable>
    extends M5AttributeFieldDef<T> {

  /**
   * Constructor.
   */
  public M5StdFieldDefIconId() {
    this( STR_N_ICON_ID, STR_D_ICON_ID );
  }

  /**
   * Constructor.
   *
   * @param aName String - field name
   * @param aDescription String - field description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5StdFieldDefIconId( String aName, String aDescription ) {
    super( TSID_ICON_ID, DDEF_ICON_ID );
    setNameAndDescription( aName, aDescription );
    setFlags( 0 );
  }

  // ------------------------------------------------------------------------------------
  // M5AttributeFieldDef
  //

  @Override
  protected IAtomicValue doGetFieldValue( T aEntity ) {
    return avStr( aEntity.iconId() );
  }

  @Override
  protected Image doGetFieldValueIcon( T aEntity, EIconSize aIconSize ) {
    return iconManager().loadStdIcon( aEntity.iconId(), aIconSize );
  }

}
