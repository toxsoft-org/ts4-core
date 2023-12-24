package org.toxsoft.core.tsgui.m5.std.models.misc;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.std.models.misc.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * M5-model of {@link IGenericTaskInfo}.
 * <p>
 * Model does not allows to edit the tasks list, only to enumerate tasks specifying {@link ITsItemsProvider} as a master
 * object to the {@link #getLifecycleManager(Object)}.
 *
 * @author hazard157
 */
public class GenericTaskInfoM5Model
    extends M5Model<IGenericTaskInfo> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = M5_ID + ".IGenericTaskInfo"; //$NON-NLS-1$

  /**
   * {@link IGenericTaskInfo#nmName()}
   */
  public final IM5AttributeFieldDef<IGenericTaskInfo> NAME = new M5StdFieldDefName<>( //
      TSID_NAME, STR_GTI_NAME, //
      TSID_DESCRIPTION, STR_GTI_NAME_D, //
      M5_OPDEF_FLAGS, avInt( M5FF_COLUMN ) //
  ) {

    protected Image doGetFieldValueIcon( IGenericTaskInfo aEntity, EIconSize aIconSize ) {
      String iconId = aEntity.iconId();
      if( iconId != null ) {
        return iconManager().loadStdIcon( iconId, aIconSize );
      }
      return null;
    }
  };

  /**
   * {@link IGenericTaskInfo#id()}
   */
  public final IM5AttributeFieldDef<IGenericTaskInfo> ID = new M5StdFieldDefId<>( //
      TSID_NAME, STR_GTI_ID, //
      TSID_DESCRIPTION, STR_GTI_ID_D, //
      M5_OPDEF_FLAGS, avInt( M5FF_DETAIL ) //
  );

  /**
   * {@link IGenericTaskInfo#description()}
   */
  public final IM5AttributeFieldDef<IGenericTaskInfo> DESCRIPTION = new M5StdFieldDefDescription<>( //
      TSID_NAME, STR_GTI_DESCRIPTION, //
      TSID_DESCRIPTION, STR_GTI_DESCRIPTION_D, //
      M5_OPDEF_FLAGS, avInt( M5FF_DETAIL ) //
  );

  private static class LifecylceManager
      extends M5LifecycleManager<IGenericTaskInfo, ITsItemsProvider<IGenericTaskInfo>> {

    public LifecylceManager( IM5Model<IGenericTaskInfo> aModel, ITsItemsProvider<IGenericTaskInfo> aMaster ) {
      super( aModel, false, false, false, true, aMaster );
    }

    @Override
    protected IList<IGenericTaskInfo> doListEntities() {
      return master().listItems();
    }

  }

  /**
   * Constructor.
   */
  public GenericTaskInfoM5Model() {
    super( MODEL_ID, IGenericTaskInfo.class );
    setNameAndDescription( STR_M5M_GENERIC_TASK_INFO, STR_M5M_GENERIC_TASK_INFO_D );
    addFieldDefs( NAME, ID, DESCRIPTION );
  }

  @Override
  protected IM5LifecycleManager<IGenericTaskInfo> doCreateLifecycleManager( Object aMaster ) {
    ITsItemsProvider<IGenericTaskInfo> itemsProvider = ITsItemsProvider.class.cast( aMaster );
    return new LifecylceManager( this, itemsProvider );
  }

}
