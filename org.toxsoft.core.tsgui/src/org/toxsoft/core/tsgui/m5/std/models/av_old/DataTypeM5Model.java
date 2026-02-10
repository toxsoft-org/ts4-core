package org.toxsoft.core.tsgui.m5.std.models.av_old;

import static org.toxsoft.core.tsgui.m5.std.models.av_old.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.misc.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * M5-model of {@link IDataType}.
 *
 * @author hazard157
 */
public class DataTypeM5Model
    extends DataMetaInfoBasicM5Model<IDataType> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TS_ID + "DataType"; //$NON-NLS-1$

  /**
   * LM for this model.
   *
   * @author hazard157
   */
  class LifecycleManager
      extends M5LifecycleManager<IDataType, Object> {

    public LifecycleManager( IM5Model<IDataType> aModel ) {
      super( aModel, true, true, true, false, null );
    }

    @Override
    protected IDataType doCreate( IM5Bunch<IDataType> aValues ) {
      EAtomicType atomicType = aValues.getAs( FID_ATOMIC_TYPE, EAtomicType.class );
      IList<IdValue> idvals = aValues.getAs( FID_CONSTRAINTS, IList.class );
      DataType dt = new DataType( atomicType );
      IdValue.fillOptionSetFromIdValuesColl( idvals, dt.params() );
      return dt;
    }

    @Override
    protected IDataType doEdit( IM5Bunch<IDataType> aValues ) {
      EAtomicType atomicType = aValues.getAs( FID_ATOMIC_TYPE, EAtomicType.class );
      IList<IdValue> idvals = aValues.getAs( FID_CONSTRAINTS, IList.class );
      DataType dt = new DataType( atomicType );
      IdValue.fillOptionSetFromIdValuesColl( idvals, dt.params() );
      return dt;
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
    setNameAndDescription( STR_N_M5M_DATA_TYPE, STR_D_M5M_DATA_TYPE );
    addFieldDefs( ATOMIC_TYPE, CONSTRAINTS );
    setVisualsProvider( aItem -> {
      StringBuilder s = new StringBuilder().append( aItem.atomicType().nmName() );
      if( !aItem.params().isEmpty() ) {
        s.append( '[' ).append( aItem.params().size() ).append( ']' );
      }
      return s.toString();
    } );
  }

  @Override
  protected IM5LifecycleManager<IDataType> doCreateDefaultLifecycleManager() {
    return new LifecycleManager( this );
  }

  @Override
  protected IM5LifecycleManager<IDataType> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

}
