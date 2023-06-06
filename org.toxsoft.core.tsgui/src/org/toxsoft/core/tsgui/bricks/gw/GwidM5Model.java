package org.toxsoft.core.tsgui.bricks.gw;

import static org.toxsoft.core.tsgui.bricks.gw.IGwM5Constants.*;
import static org.toxsoft.core.tsgui.bricks.gw.l10n.ITsguiGwSharedResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * M5-model of the {@link Gwid} entities.
 * <p>
 * This model may be used either "as is" or may be subclassed. Basic model considers GWIDs as simple value-object
 * classes.
 * <p>
 * Model has separate M5-fields for {@link Gwid#classId()}, {@link Gwid#strid()}, {@link Gwid#propId()}, etc. It is
 * assumed that user enters class ID, object STRID (to make a the GWID a concrete GWID), selects {@link EGwidKind} and
 * fills kind-dependent fields {@link Gwid#propId()} and {@link Gwid#subPropId()}.
 *
 * @author hazard157
 */
public class GwidM5Model
    extends M5Model<Gwid> {

  /**
   * Attribute field {@link Gwid#classId()}
   */
  public final M5AttributeFieldDef<Gwid> CLASS_ID = new M5AttributeFieldDef<>( FID_CLASS_ID, DDEF_IDPATH, //
      TSID_NAME, STR_GWID_CLASS_ID, //
      TSID_DESCRIPTION, STR_GWID_CLASS_ID_D, //
      M5_OPDEF_FLAGS, avInt( M5FF_DETAIL ), //
      TSID_DEFAULT_VALUE, avStr( Skid.NONE.classId() ) //
  ) {

    @Override
    protected IAtomicValue doGetFieldValue( Gwid aEntity ) {
      return avStr( aEntity.classId() );
    }

  };

  /**
   * Attribute field {@link Gwid#strid()}
   */
  public final M5AttributeFieldDef<Gwid> OBJ_STRID = new M5AttributeFieldDef<>( FID_OBJ_STRID, DDEF_IDPATH, //
      TSID_NAME, STR_GWID_OBJ_STRID, //
      TSID_DESCRIPTION, STR_GWID_OBJ_STRID_D, //
      M5_OPDEF_FLAGS, avInt( M5FF_COLUMN ), //
      TSID_DEFAULT_VALUE, avStr( Skid.NONE.strid() ) //
  ) {

    @Override
    protected IAtomicValue doGetFieldValue( Gwid aEntity ) {
      return avStr( aEntity.strid() );
    }

  };

  /**
   * Attribute field {@link Gwid#kind()}
   */
  public final M5AttributeFieldDef<Gwid> GWID_KIND = new M5AttributeFieldDef<>( FID_GWID_KIND, VALOBJ, //
      TSID_NAME, STR_GWID_KIND, //
      TSID_DESCRIPTION, STR_GWID_KIND_D, //
      M5_OPDEF_FLAGS, avInt( M5FF_COLUMN ), //
      TSID_DEFAULT_VALUE, EGwidKind.GW_CLASS.atomicValue() //
  ) {

    @Override
    protected IAtomicValue doGetFieldValue( Gwid aEntity ) {
      return aEntity.kind().atomicValue();
    }

  };

  // FIXME field propId()

  /**
   * Attribute field {@link Gwid#propId()}
   */
  public final M5AttributeFieldDef<Gwid> PROP_ID = new M5AttributeFieldDef<>( FID_PROP_ID, DDEF_IDPATH, //
      TSID_NAME, STR_GWID_PROP_ID, //
      TSID_DESCRIPTION, STR_GWID_PROP_ID_D, //
      M5_OPDEF_FLAGS, avInt( M5FF_COLUMN ) //
  ) {

    @Override
    protected IAtomicValue doGetFieldValue( Gwid aEntity ) {
      return aEntity.kind().atomicValue();
    }

  };

  /**
   * Attribute field {@link Gwid#subPropId()}
   */
  public final M5AttributeFieldDef<Gwid> SUB_PROP_ID = new M5AttributeFieldDef<>( FID_SUB_PROP_ID, DDEF_IDPATH, //
      TSID_NAME, STR_GWID_SUB_PROP_ID, //
      TSID_DESCRIPTION, STR_GWID_SUB_PROP_ID_D, //
      M5_OPDEF_FLAGS, avInt( M5FF_COLUMN ) //
  ) {

    @Override
    protected IAtomicValue doGetFieldValue( Gwid aEntity ) {
      return aEntity.kind().atomicValue();
    }

  };

  /**
   * M5-field of type {@link Gwid}, returns the entity itself.
   */
  public final M5FieldDef<Gwid, Gwid> GWID = new M5FieldDef<>( FID_GWID, Gwid.class ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_GWID_GWID, STR_GWID_GWID_D );
      setFlags( M5FF_READ_ONLY | M5FF_HIDDEN );
      setDefaultValue( Gwid.createClass( Skid.NONE.classId() ) );
    }

    protected Gwid doGetFieldValue( Gwid aEntity ) {
      return aEntity;
    }

    protected String doGetFieldValueName( Gwid aEntity ) {
      return aEntity.asString();
    }

  };

  /**
   * Constructor.
   */
  public GwidM5Model() {
    super( MID_GWID, Gwid.class );
    addFieldDefs( CLASS_ID, OBJ_STRID, GWID );
  }

  /**
   * Constructor for subclass.
   *
   * @param aId String - the model ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   */
  protected GwidM5Model( String aId ) {
    super( aId, Gwid.class );
    addFieldDefs( CLASS_ID, OBJ_STRID, GWID_KIND, PROP_ID, SUB_PROP_ID, GWID );
    setPanelCreator( new M5DefaultPanelCreator<>() {

      @Override
      protected IM5EntityPanel<Gwid> doCreateEntityEditorPanel( ITsGuiContext aContext,
          IM5LifecycleManager<Gwid> aLifecycleManager ) {

        /**
         * TODO create specific panel (or, maybe controller) for GWID editing. In particular we need to change
         * PROP_ID/SUB_PROP_ID visible status or at least, editable status. Also labels for PROP_ID/SUB_PROP_ID must
         * change when GWID_KIND changes.
         */

        return super.doCreateEntityEditorPanel( aContext, aLifecycleManager );
      }
    } );
  }

  @Override
  protected IM5LifecycleManager<Gwid> doCreateDefaultLifecycleManager() {
    return new DefaultGwidM5LifecycleManager( this );
  }

}
