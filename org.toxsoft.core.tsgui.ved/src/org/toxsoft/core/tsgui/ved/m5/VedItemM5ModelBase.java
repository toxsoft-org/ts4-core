package org.toxsoft.core.tsgui.ved.m5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;
import static org.toxsoft.core.tsgui.ved.m5.IVedM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Basic M5-model of all {@link IVedItem} and sub-interfaces.
 *
 * @author hazard157
 * @param <T> - concrete item type
 */
public class VedItemM5ModelBase<T extends IVedItem>
    extends M5Model<T> {

  /**
   * Field {@link IVedItem#id()}
   */
  public final IM5AttributeFieldDef<IVedItem> ID = new M5StdFieldDefId<>( //
      TSID_NAME, STR_ITEM_ID, //
      TSID_DESCRIPTION, STR_ITEM_ID_D, //
      M5_OPID_FLAGS, avInt( M5FF_COLUMN | M5FF_INVARIANT ) //
  ) {

    protected Image doGetFieldValueIcon( IVedItem aEntity, EIconSize aIconSize ) {
      String iconId = aEntity.iconId();
      if( iconId != null ) {
        return iconManager().loadStdIcon( iconId, aIconSize );
      }
      return null;
    }
  };

  /**
   * Field {@link IVedItem#factoryId()}
   */
  public final IM5AttributeFieldDef<IVedItem> FACTORY_ID = new M5AttributeFieldDef<>( FID_FACTORY_ID, DDEF_IDPATH, //
      TSID_NAME, STR_ITEM_FACTORY_ID, //
      TSID_DESCRIPTION, STR_ITEM_FACTORY_ID_D, //
      M5_OPID_FLAGS, avInt( M5FF_DETAIL | M5FF_INVARIANT ) //
  ) {

    protected IAtomicValue doGetFieldValue( IVedItem aEntity ) {
      return avStr( aEntity.factoryId() );
    }

  };

  /**
   * Field displays the name of the factory by {@link IVedItem#factoryId()}.
   */
  public final IM5AttributeFieldDef<IVedItem> FACTORY_NAME = new M5AttributeFieldDef<>( FID_FACTORY_NAME, DDEF_STRING, //
      TSID_NAME, STR_ITEM_FACTORY_NAME, //
      TSID_DESCRIPTION, STR_ITEM_FACTORY_NAME_D, //
      M5_OPID_FLAGS, avInt( M5FF_DETAIL | M5FF_INVARIANT ) //
  ) {

    protected IAtomicValue doGetFieldValue( IVedItem aEntity ) {
      IVedItemFactoryBase<?> factory = switch( aEntity.kind() ) {
        case VISEL -> tsContext().get( IVedViselFactoriesRegistry.class ).find( aEntity.factoryId() );
        case ACTOR -> tsContext().get( IVedActorFactoriesRegistry.class ).find( aEntity.factoryId() );
        default -> throw new TsNotAllEnumsUsedRtException();
      };
      if( factory == null ) {
        return AV_STR_EMPTY;
      }
      return avStr( factory.nmName() );
    }

  };

  /**
   * Field {@link IVedItem#nmName()}
   */
  public final IM5AttributeFieldDef<IVedItem> NAME = new M5StdFieldDefName<>( //
      TSID_NAME, STR_ITEM_NAME, //
      TSID_DESCRIPTION, STR_ITEM_NAME_D, //
      M5_OPID_FLAGS, avInt( M5FF_COLUMN ) //
  );

  /**
   * Field {@link IVedItem#description()}
   */
  public final IM5AttributeFieldDef<IVedItem> DESCRIPTION = new M5StdFieldDefDescription<>( //
      TSID_NAME, STR_ITEM_DESCRIPTION, //
      TSID_DESCRIPTION, STR_ITEM_DESCRIPTION_D, //
      M5_OPID_FLAGS, avInt( M5FF_DETAIL ) //
  );

  /**
   * Constructor for subclasses.
   *
   * @param aId String - model ID
   * @param aItemClass {@link Class}&lt;T&gt; - modeled entity type
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   */
  protected VedItemM5ModelBase( String aId, Class<T> aItemClass ) {
    super( aId, aItemClass );
    addFieldDefs( ID, FACTORY_ID, FACTORY_NAME, NAME, DESCRIPTION );
  }

}
