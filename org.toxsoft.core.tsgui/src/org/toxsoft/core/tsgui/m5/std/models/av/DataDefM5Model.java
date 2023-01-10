package org.toxsoft.core.tsgui.m5.std.models.av;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.std.models.av.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.misc.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * M5-model of {@link IDataDef}.
 *
 * @author dima
 */
public class DataDefM5Model
    extends DataMetaInfoBasicM5Model<IDataDef> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TS_ID + "DataDef"; //$NON-NLS-1$

  /**
   * Attribute {@link IDataDef#id()}.
   */
  public final IM5AttributeFieldDef<IDataDef> ID = new M5AttributeFieldDef<>( FID_ID, DDEF_IDPATH ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_DATA_DEF_ID, STR_D_DATA_DEF_ID );
      setFlags( M5FF_READ_ONLY | M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( IDataDef aEntity ) {
      return avStr( aEntity.id() );
    }

  };

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

    private IDataDef makeDataDef( IM5Bunch<IDataDef> aValues ) {
      String id = aValues.getAsAv( FID_ID ).asString();
      EAtomicType atomicType = aValues.getAs( FID_ATOMIC_TYPE, EAtomicType.class );
      IList<IdValue> idvals = aValues.getAs( FID_CONSTRAINTS, IList.class );
      IOptionSetEdit params = new OptionSet();
      IdValue.fillOptionSetFromIdValuesColl( idvals, params );
      IDataDef dd = new DataDef( id, atomicType, params );
      return dd;

    }

    @Override
    protected ValidationResult doBeforeCreate( IM5Bunch<IDataDef> aValues ) {
      String id = aValues.getAsAv( FID_ID ).asString();
      if( !StridUtils.isValidIdPath( id ) ) {
        return ValidationResult.error( FMT_ERR_DD_ID_NOT_IDPATH, id );
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
        return ValidationResult.error( FMT_ERR_DD_ID_NOT_IDPATH, id );
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
    setNameAndDescription( STR_N_M5M_DATA_DEF, STR_D_M5M_DATA_DEF );
    addFieldDefs( ID, ATOMIC_TYPE, CONSTRAINTS );
    setVisualsProvider( aItem -> {
      StringBuilder s = new StringBuilder().append( aItem.atomicType().nmName() );
      if( !aItem.params().isEmpty() ) {
        s.append( '[' ).append( aItem.params().size() ).append( ']' );
      }
      return s.toString();
    } );
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
