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
import org.toxsoft.core.tslib.av.misc.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;

/**
 * Модель объектов типа {@link IdValue}.
 *
 * @author goga
 */
public class IdValueM5Model
    extends M5Model<IdValue> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TS_ID + ".IdValue"; //$NON-NLS-1$

  /**
   * ID of the field {@link #VALUE}.
   */
  public static final String FID_VALUE = "Value"; //$NON-NLS-1$

  /**
   * Attribute {@link IdValue#id()}
   */
  public final M5AttributeFieldDef<IdValue> ID = new M5AttributeFieldDef<>( FID_ID, DDEF_IDPATH ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_IDVAL_ID, STR_D_IDVAL_ID );
      setDefaultValue( DEFAULT_ID_AV );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IdValue aEntity ) {
      return avStr( aEntity.id() );
    }

  };

  /**
   * Attribute {@link IdValue#value()}
   */
  public final M5AttributeFieldDef<IdValue> VALUE = new M5AttributeFieldDef<>( FID_VALUE, DDEF_NONE ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_IDVAL_VALUE, STR_D_IDVAL_VALUE );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IdValue aEntity ) {
      return aEntity.value();
    }

  };

  class LifecycleManager
      extends M5LifecycleManager<IdValue, Object> {

    public LifecycleManager( IM5Model<IdValue> aModel ) {
      super( aModel, true, true, true, false, null );
    }

    @Override
    protected ValidationResult doBeforeCreate( IM5Bunch<IdValue> aValues ) {
      String id = ID.getFieldValue( aValues ).asString();
      return StridUtils.validateIdPath( id );
    }

    @Override
    protected IdValue doCreate( IM5Bunch<IdValue> aValues ) {
      String id = ID.getFieldValue( aValues ).asString();
      IAtomicValue value = VALUE.getFieldValue( aValues );
      return new IdValue( id, value );
    }

    @Override
    protected ValidationResult doBeforeEdit( IM5Bunch<IdValue> aValues ) {
      String id = ID.getFieldValue( aValues ).asString();
      return StridUtils.validateIdPath( id );
    }

    @Override
    protected IdValue doEdit( IM5Bunch<IdValue> aValues ) {
      return doCreate( aValues );
    }

    @Override
    protected void doRemove( IdValue aEntity ) {
      // nop
    }

  }

  /**
   * Constructor.
   */
  public IdValueM5Model() {
    super( MODEL_ID, IdValue.class );
    setNameAndDescription( STR_N_M5M_IDVAL, STR_D_M5M_IDVAL );
    addFieldDefs( ID, VALUE );
  }

  @Override
  protected IM5LifecycleManager<IdValue> doCreateDefaultLifecycleManager() {
    return new LifecycleManager( this );
  }

  @Override
  protected IM5LifecycleManager<IdValue> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

}
