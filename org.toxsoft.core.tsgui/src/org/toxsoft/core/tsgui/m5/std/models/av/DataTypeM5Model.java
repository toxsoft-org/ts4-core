package org.toxsoft.core.tsgui.m5.std.models.av;

import static org.toxsoft.core.tsgui.ITsGuiConstants.*;
import static org.toxsoft.core.tsgui.m5.std.models.av.ITsResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * M5-model of {@link IDataType} with support of constraints editing.
 * <p>
 * Model is designed with one editable field - the {@link #DATA_TYPE}. Other fields are read-only to be displayed in
 * columns, details, etc.
 *
 * @author hazard157
 */
public class DataTypeM5Model
    extends DataTypeM5ModelBase<IDataType> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TSGUI_M5_ID + ".DataType"; //$NON-NLS-1$

  /**
   * LM for this model.
   *
   * @author hazard157
   */
  private class LifecycleManager
      extends M5LifecycleManager<IDataType, Object> {

    public LifecycleManager() {
      super( DataTypeM5Model.this, true, true, true, false, null );
    }

    @Override
    protected IDataType doCreate( IM5Bunch<IDataType> aValues ) {
      return aValues.getAs( FID_DATA_TYPE, IDataType.class );
    }

    @Override
    protected IDataType doEdit( IM5Bunch<IDataType> aValues ) {
      return aValues.getAs( FID_DATA_TYPE, IDataType.class );
    }

    @Override
    protected void doRemove( IDataType aEntity ) {
      // nop addFlags( M5FF_COLUMN | M5FF_INVARIANT );

    }

  }

  /**
   * Constructor.
   */
  public DataTypeM5Model() {
    super( MODEL_ID, IDataType.class );
    setNameAndDescription( STR_M5M_DATA_TYPE, STR_M5M_DATA_TYPE_D );
    addFieldDefs( DATA_TYPE );
  }

  @Override
  protected IM5LifecycleManager<IDataType> doCreateDefaultLifecycleManager() {
    return new LifecycleManager();
  }

  @Override
  protected IM5LifecycleManager<IDataType> doCreateLifecycleManager( Object aMaster ) {
    return new LifecycleManager();
  }

}
