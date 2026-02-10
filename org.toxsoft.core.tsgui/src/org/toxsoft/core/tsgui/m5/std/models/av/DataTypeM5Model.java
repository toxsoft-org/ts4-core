package org.toxsoft.core.tsgui.m5.std.models.av;

import static org.toxsoft.core.tsgui.ITsGuiConstants.*;
import static org.toxsoft.core.tsgui.m5.std.models.av.ITsResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.metainf.*;
import org.toxsoft.core.tslib.av.*;
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
    extends M5Model<IDataType> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TSGUI_M5_ID + ".DataType"; //$NON-NLS-1$

  /**
   * ID of field {@link #DATA_TYPE}.
   */
  public static final String FID_DATA_TYPE = "dataType"; //$NON-NLS-1$

  /**
   * ID of field {@link #ATOMIC_TYPE}.
   */
  public static final String FID_ATOMIC_TYPE = "atomicType"; //$NON-NLS-1$

  /**
   * Field returns modeled entity as is to be edited with {@link ValedDataType}.
   */
  public final IM5FieldDef<IDataType, IDataType> DATA_TYPE = new M5FieldDef<>( FID_DATA_TYPE, IDataType.class ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_FIELD_DATA_TYPE, STR_FIELD_DATA_TYPE_D );
      params().setBool( IValedControlConstants.OPDEF_NO_FIELD_LABEL, true );
      params().setInt( IValedControlConstants.OPDEF_VERTICAL_SPAN, 10 );
      params().setBool( IValedControlConstants.OPDEF_IS_HEIGHT_FIXED, false );
      setValedEditor( ValedDataType.FACTORY_NAME );
    }

    protected IDataType doGetFieldValue( IDataType aEntity ) {
      return aEntity;
    }

    protected String doGetFieldValueName( IDataType aEntity ) {
      return aEntity.toString();
    }

  };

  /**
   * Field {@link IDataType#atomicType()}
   */
  public final IM5SingleLookupFieldDef<IDataType, EAtomicType> ATOMIC_TYPE =
      new M5SingleLookupFieldDef<>( FID_ATOMIC_TYPE, AtomicTypeM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_ATOMIC_TYPE, STR_ATOMIC_TYPE_D );
        }

        protected EAtomicType doGetFieldValue( IDataType aEntity ) {
          return aEntity.atomicType();
        }

      };

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
      // nop
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
