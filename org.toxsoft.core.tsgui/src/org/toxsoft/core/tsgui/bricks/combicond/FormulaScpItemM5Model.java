package org.toxsoft.core.tsgui.bricks.combicond;

import static org.toxsoft.core.tsgui.bricks.combicond.ITsResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.combicond.valed.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.math.combicond.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Internal M5-model of {@link FormulaScpItem}.
 * <p>
 * Instances of this model are temporary initialized by the instances of {@link CombiCondParamsPanel}.
 *
 * @author hazard157
 */
class FormulaScpItemM5Model
    extends M5Model<FormulaScpItem> {

  static final String FID_KEYWORD = "keyword"; //$NON-NLS-1$

  static final String FID_SCP = "scp"; //$NON-NLS-1$

  final IM5AttributeFieldDef<FormulaScpItem> KEYWORD = new M5AttributeFieldDef<>( FID_KEYWORD, STRING, //
      TSID_NAME, STR_FSI_KEYWORD, //
      TSID_DESCRIPTION, STR_FSI_KEYWORD_D, //
      M5_OPDEF_FLAGS, avInt( M5FF_COLUMN ), //
      TSID_DEFAULT_VALUE, DEFAULT_ID_STR //
  ) {

    protected IAtomicValue doGetFieldValue( FormulaScpItem aEntity ) {
      return avStr( aEntity.keyword() );
    }

  };

  final IM5AttributeFieldDef<FormulaScpItem> SCP = new M5AttributeFieldDef<>( FID_SCP, VALOBJ, //
      TSID_NAME, STR_FSI_SCP, //
      TSID_DESCRIPTION, STR_FSI_SCP_D, //
      TSID_KEEPER_ID, SingleCondParams.KEEPER_ID, //
      M5_OPDEF_FLAGS, avInt( M5FF_COLUMN ), //
      IValedControlConstants.OPDEF_EDITOR_FACTORY_NAME, ValedAvSingleCondParams.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( ISingleCondParams.ALWAYS ) //
  ) {

    protected IAtomicValue doGetFieldValue( FormulaScpItem aEntity ) {
      return avValobj( aEntity.scp() );
    }

    protected String doGetFieldValueName( FormulaScpItem aEntity ) {
      String scpTypeId = aEntity.scp().typeId();
      ISingleCondType sct = scpTypesRegistry.find( scpTypeId );
      if( sct == null ) {
        return super.doGetFieldValueName( aEntity );
      }
      return sct.humanReadableDescription( aEntity.scp().params() );
    }

  };

  private final ISingleCondTypesRegistry<ISingleCondType> scpTypesRegistry;

  /**
   * Constructor.
   *
   * @param aModelId String - the model ID, must be generated for each instance
   * @param aScpTypesReg {@link ISingleCondTypesRegistry} - SCT registry to display correct text of SCP
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public FormulaScpItemM5Model( String aModelId, ISingleCondTypesRegistry<ISingleCondType> aScpTypesReg ) {
    super( aModelId, FormulaScpItem.class );
    scpTypesRegistry = TsNullArgumentRtException.checkNull( aScpTypesReg );
    setNameAndDescription( STR_M5M_FORMULA_SCP_ITEM, STR_M5M_FORMULA_SCP_ITEM_D );
    addFieldDefs( KEYWORD, SCP );
  }

  @Override
  protected IM5LifecycleManager<FormulaScpItem> doCreateLifecycleManager( Object aMaster ) {
    return new FormulaScpItemM5LifecycleManager( this, CombiCondParamsPanel.class.cast( aMaster ) );
  }

}
