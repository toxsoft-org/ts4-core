package org.toxsoft.core.tsgui.m5.std.fields;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.utils.*;

/**
 * {@link IM5AttributeFieldDef} implementation for icon stored as {@link IAvMetaConstants#TSID_ICON_ID}.
 *
 * @author hazard157
 * @param <T> - concrete type of the {@link IParameterized} entity
 */
public class M5StdFieldDefParamIconId<T extends IParameterized>
    extends M5StdFieldDefParamAttr<T> {

  /**
   * Constructor.
   */
  public M5StdFieldDefParamIconId() {
    super( DDEF_ICON_ID );
  }

  // ------------------------------------------------------------------------------------
  // M5StdFieldDefParamAttr
  //

  @Override
  protected Image doGetFieldValueIcon( T aEntity, EIconSize aIconSize ) {
    IAtomicValue av = aEntity.params().getValue( TSID_ICON_ID, defaultValue() );
    if( av.isAssigned() && av.atomicType() == EAtomicType.STRING ) {
      String iconId = av.asString();
      if( !iconId.isBlank() ) {
        return iconManager().loadStdIcon( iconId, aIconSize );
      }
    }
    return null;
  }

}
