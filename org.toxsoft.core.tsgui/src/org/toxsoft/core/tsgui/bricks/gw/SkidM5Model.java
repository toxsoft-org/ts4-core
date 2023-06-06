package org.toxsoft.core.tsgui.bricks.gw;

import static org.toxsoft.core.tsgui.bricks.gw.IGwM5Constants.*;
import static org.toxsoft.core.tsgui.bricks.gw.l10n.ITsguiGwSharedResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * M5-model of the {@link Skid} entities.
 * <p>
 * This model may be used either "as is" or may be subclassed. Basic model considers SKIDs as simple value-object
 * classes.
 * <p>
 * Note:
 *
 * @author hazard157
 */
public class SkidM5Model
    extends M5Model<Skid> {

  /**
   * Attribute field {@link Skid#classId()}
   */
  public final M5AttributeFieldDef<Skid> CLASS_ID = new M5AttributeFieldDef<>( FID_CLASS_ID, STRING, //
      TSID_NAME, STR_SKID_CLASS_ID, //
      TSID_DESCRIPTION, STR_SKID_CLASS_ID_D, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      M5_OPDEF_FLAGS, avInt( M5FF_DETAIL ), //
      TSID_DEFAULT_VALUE, avStr( Skid.NONE.classId() ) //
  ) {

    @Override
    protected IAtomicValue doGetFieldValue( Skid aEntity ) {
      return avStr( aEntity.classId() );
    }

  };

  /**
   * Attribute field {@link Skid#strid()}
   */
  public final M5AttributeFieldDef<Skid> OBJ_STRID = new M5AttributeFieldDef<>( FID_OBJ_STRID, STRING, //
      TSID_NAME, STR_SKID_OBJ_STRID, //
      TSID_DESCRIPTION, STR_SKID_OBJ_STRID_D, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      M5_OPDEF_FLAGS, avInt( M5FF_COLUMN ), //
      TSID_DEFAULT_VALUE, avStr( Skid.NONE.strid() ) //
  ) {

    @Override
    protected IAtomicValue doGetFieldValue( Skid aEntity ) {
      return avStr( aEntity.strid() );
    }

  };

  /**
   * M5-field of type {@link Skid}, returns the entity itself.
   */
  public final M5FieldDef<Skid, Skid> SKID = new M5FieldDef<>( FID_SKID, Skid.class ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_SKID_SKID, STR_SKID_SKID_D );
      setFlags( M5FF_READ_ONLY | M5FF_HIDDEN );
      setDefaultValue( Skid.NONE );
    }

    protected Skid doGetFieldValue( Skid aEntity ) {
      return aEntity;
    }

    protected String doGetFieldValueName( Skid aEntity ) {
      return aEntity.toString();
    }

  };

  /**
   * Constructor.
   */
  public SkidM5Model() {
    super( MID_SKID, Skid.class );
    addFieldDefs( CLASS_ID, OBJ_STRID, SKID );
  }

  /**
   * Constructor for subclass.
   *
   * @param aId String - the model ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   */
  protected SkidM5Model( String aId ) {
    super( aId, Skid.class );
    addFieldDefs( CLASS_ID, OBJ_STRID, SKID );
  }

  @Override
  protected IM5LifecycleManager<Skid> doCreateDefaultLifecycleManager() {
    return new DefaultSkidM5LifecycleManager( this );
  }

}
