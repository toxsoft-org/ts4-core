package org.toxsoft.core.tsgui.m5.std.models.av;

import static org.toxsoft.core.tsgui.ITsGuiConstants.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.std.models.av.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;

/**
 * M5-model of {@link IDataDef}.
 *
 * @author hazard157
 */
public class DataDefM5Model
    extends DataTypeM5ModelBase<IDataDef> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TSGUI_M5_ID + ".DataDef"; //$NON-NLS-1$

  /**
   * Attribute {@link IDataDef#id()}.
   */
  public final IM5AttributeFieldDef<IDataDef> ID = new M5StdFieldDefId<>( //
      TSID_NAME, STR_DATADEF_ID, //
      TSID_DESCRIPTION, STR_DATADEF_ID_D, //
      TSID_DEFAULT_VALUE, DEFAULT_ID_AV //
  );

  /**
   * LM for this model.
   *
   * @author hazard157
   */
  class LifecycleManager
      extends M5LifecycleManager<IDataDef, Object> {

    public LifecycleManager( IM5Model<IDataDef> aModel ) {
      super( aModel, true, true, true, false, null );
    }

    // ------------------------------------------------------------------------------------
    // implementation
    //

    private IDataDef makeDataDef( IM5Bunch<IDataDef> aValues ) {
      String id = aValues.getAsAv( FID_ID ).asString();
      IDataType dataType = aValues.getAs( FID_DATA_TYPE, IDataType.class );
      return new DataDef( id, dataType.atomicType(), dataType.params() );
    }

    // ------------------------------------------------------------------------------------
    // M5LifecycleManager
    //

    @Override
    protected ValidationResult doBeforeCreate( IM5Bunch<IDataDef> aValues ) {
      String id = aValues.getAsAv( FID_ID ).asString();
      if( !StridUtils.isValidIdPath( id ) ) {
        return ValidationResult.error( FMT_ERR_INV_DATADEF_ID, id );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    protected IDataDef doCreate( IM5Bunch<IDataDef> aValues ) {
      return makeDataDef( aValues );
    }

    @Override
    protected ValidationResult doBeforeEdit( IM5Bunch<IDataDef> aValues ) {
      String id = aValues.getAsAv( FID_ID ).asString();
      if( !StridUtils.isValidIdPath( id ) ) {
        return ValidationResult.error( FMT_ERR_INV_DATADEF_ID, id );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    protected IDataDef doEdit( IM5Bunch<IDataDef> aValues ) {
      return makeDataDef( aValues );
    }

    @Override
    protected void doRemove( IDataDef aEntity ) {
      // nop
    }

  }

  /**
   * Constructor.
   */
  public DataDefM5Model() {
    super( MODEL_ID, IDataDef.class );
    setNameAndDescription( STR_M5M_DATA_DEF, STR_M5M_DATA_DEF_D );
    addFieldDefs( ID, DATA_TYPE );
  }

  @Override
  protected IM5LifecycleManager<IDataDef> doCreateDefaultLifecycleManager() {
    return new LifecycleManager( this );
  }

  @Override
  protected IM5LifecycleManager<IDataDef> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

}
