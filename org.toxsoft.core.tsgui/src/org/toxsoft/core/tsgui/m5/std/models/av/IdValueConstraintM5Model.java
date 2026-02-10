package org.toxsoft.core.tsgui.m5.std.models.av;

import static org.toxsoft.core.tsgui.ITsGuiConstants.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.std.models.av.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.constr.*;
import org.toxsoft.core.tslib.av.misc.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

/**
 * M5-model of {@link IdValue}.
 * <p>
 * This model uses {@link IdValueConstraintM5LifecycleManager} as a lifecycle manager. See LM comments for usage.
 *
 * @author hazard157
 */
public class IdValueConstraintM5Model
    extends M5Model<IdValue> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TSGUI_M5_ID + ".IdValueConstraint"; //$NON-NLS-1$

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
      setNameAndDescription( STR_IDVAL_ID, STR_IDVAL_ID_D );
      setDefaultValue( AV_STR_EMPTY );
      setFlags( M5FF_DETAIL );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IdValue aEntity ) {
      return avStr( aEntity.id() );
    }

    protected String doGetFieldValueDescription( IdValue aEntity ) {
      String constId = aEntity.id();
      IConstraintInfo cinf = ConstraintUtils.findConstraintInfo( constId );
      if( cinf != null ) {
        return StridUtils.printf( StridUtils.FORMAT_NAME_DESCRIPTION, cinf );
      }
      return super.doGetFieldValueDescription( aEntity );
    }

  };

  /**
   * Field {@link IConstraintInfo#nmName()} if constraint is known by ID {@link IdValue#id()}
   */
  public final M5AttributeFieldDef<IdValue> CONSTR_NAME = new M5AttributeFieldDef<>( FID_NAME, DT_STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_IDVAL_CONSTR_NAME, STR_IDVAL_CONSTR_NAME_D );
      setDefaultValue( AV_STR_EMPTY );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY | M5FF_HIDDEN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IdValue aEntity ) {
      String constId = aEntity.id();
      IConstraintInfo cinf = ConstraintUtils.findConstraintInfo( constId );
      return avStr( cinf != null ? cinf.nmName() : "ID: " + constId ); //$NON-NLS-1$
    }

  };

  /**
   * Field {@link IConstraintInfo#description()} if constraint is known by ID {@link IdValue#id()}
   */
  public final M5AttributeFieldDef<IdValue> CONSTR_DESCRIPTION =
      new M5AttributeFieldDef<>( FID_DESCRIPTION, DT_STRING ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_IDVAL_CONSTR_DESCRIPTION, STR_IDVAL_CONSTR_DESCRIPTION_D );
          setDefaultValue( AV_STR_EMPTY );
          setFlags( M5FF_DETAIL | M5FF_READ_ONLY | M5FF_HIDDEN );
        }

        @Override
        protected IAtomicValue doGetFieldValue( IdValue aEntity ) {
          String constId = aEntity.id();
          IConstraintInfo cinf = ConstraintUtils.findConstraintInfo( constId );
          return avStr( cinf != null ? cinf.description() : EMPTY_STRING );
        }

      };

  /**
   * Attribute {@link IdValue#value()}
   */
  public final M5AttributeFieldDef<IdValue> VALUE = new M5AttributeFieldDef<>( FID_VALUE, DT_NONE ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_IDVAL_VALUE, STR_IDVAL_VALUE_D );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IdValue aEntity ) {
      return aEntity.value();
    }

  };

  /**
   * Constructor.
   */
  public IdValueConstraintM5Model() {
    super( MODEL_ID, IdValue.class );
    setNameAndDescription( STR_M5M_IDVAL, STR_M5M_IDVAL_D );
    addFieldDefs( ID, CONSTR_NAME, CONSTR_DESCRIPTION, VALUE );
    setPanelCreator( new M5DefaultPanelCreator<>() {

      @Override
      protected IM5EntityPanel<IdValue> doCreateEntityEditorPanel( ITsGuiContext aContext,
          IM5LifecycleManager<IdValue> aLifecycleManager ) {
        IdValueConstraintM5EntityPanel p = new IdValueConstraintM5EntityPanel( aContext, model(), false );
        p.setLifecycleManager( aLifecycleManager );
        return p;
      }

      protected IM5EntityPanel<IdValue> doCreateEntityViewerPanel( ITsGuiContext aContext ) {
        IdValueConstraintM5EntityPanel p = new IdValueConstraintM5EntityPanel( aContext, model(), true );
        return p;
      }

    } );
  }

  @Override
  protected IM5LifecycleManager<IdValue> doCreateLifecycleManager( Object aMaster ) {
    return new IdValueConstraintM5LifecycleManager( this,
        IdValueConstraintM5LifecycleManager.Master.class.cast( aMaster ) );

  }

}
