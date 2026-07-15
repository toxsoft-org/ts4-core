package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.render.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

public abstract class VedAbstractRendererFactory
    extends StridableParameterized
    implements IViselRendererFactory {

  public static final String PROPID_RENDERER = "viselRenderer"; //$NON-NLS-1$

  /**
   * Data type: {@link ViselRendererCfg} as {@link EAtomicType#VALOBJ VALOBJ}.
   */
  public static final IDataType DT_TS_RENDERER_CFG = DataType.create( VALOBJ, //
      TSID_NAME, STR_TS_RENDERER_CFG, //
      TSID_DESCRIPTION, STR_TS_RENDERER_CFG_D, //
      TSID_KEEPER_ID, ViselRendererCfg.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjViselRendererCfg.FACTORY.factoryName() //
  );

  public static final IDataDef PROP_RENDERER = DataDef.create3( PROPID_RENDERER, DT_TS_RENDERER_CFG, //
      TSID_NAME, STR_RENDERER, //
      TSID_DESCRIPTION, STR_RENDERER_D //
  );

  public static final ITinTypeInfo TTI_TS_RENDERER_CFG =
      new TinAtomicTypeInfo.TtiValobj<>( DT_TS_RENDERER_CFG, ViselRendererCfg.class );

  private ITinTypeInfo              tinTypeInfo = null;
  private IStridablesList<IDataDef> propDefs    = null;

  /**
   * Constructor.
   *
   * @param aId String - the
   * @param aIdsAndValues Object[] - identifier / value pairs of the {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public VedAbstractRendererFactory( String aId, Object... aIdsAndValues ) {
    super( aId, OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

  // ------------------------------------------------------------------------------------
  // IViselRendererFactory
  //

  @Override
  final public IStridablesList<IDataDef> propDefs() {
    if( propDefs == null ) {
      IStridablesListEdit<IDataDef> pdefs = new StridablesList<>();
      // convert child fields of type info to the properties definitions
      for( ITinFieldInfo finf : typeInfo().fieldInfos() ) {
        TsInternalErrorRtException.checkFalse( finf.typeInfo().kind().hasAtomic() );
        DataDef dd = DataDef.create4( finf.id(), finf.typeInfo().dataType(), finf.params() );
        pdefs.add( dd );
      }
      propDefs = pdefs;
    }
    return propDefs;
  }

  @Override
  public ITinTypeInfo typeInfo() {
    if( tinTypeInfo == null ) {
      tinTypeInfo = doCreateTypeInfo();
      TsInternalErrorRtException.checkNull( tinTypeInfo );
      TsInternalErrorRtException.checkFalse( tinTypeInfo.kind().hasChildren() );
      for( ITinFieldInfo finf : tinTypeInfo.fieldInfos() ) {
        TsInternalErrorRtException.checkFalse( finf.typeInfo().kind().hasAtomic() );
      }
    }
    return tinTypeInfo;
  }

  @Override
  public AbstractViselRenderer create( ViselRendererCfg aCfg, IVedVisel aVisel, VedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNulls( aCfg, aVedScreen );
    TsIllegalArgumentRtException.checkFalse( aCfg.factoryId().equals( id() ) );
    OptionSetUtils.checkOptionSet( aCfg.propValues(), propDefs() );
    AbstractViselRenderer item = doCreate( aCfg, aVisel, aVedScreen );
    TsInternalErrorRtException.checkNull( item );
    item.params().setAll( aCfg.params() );
    item.props().setProps( aCfg.propValues() );
    item.doUpdateCachesAfterPropsChange( new OptionSet( item.props() ) );
    return item;
  }

  protected final ITinTypeInfo doCreateTypeInfo() {
    IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();

    addTinTypeInfoes( fields );

    // ----------------------------------------------------------------------------
    // Скрытые поля, значения которых устанавливаются извне
    //
    fields.add( TtiUtils.createHidden( TFI_X ) );
    fields.add( TtiUtils.createHidden( TFI_Y ) );
    fields.add( TtiUtils.createHidden( TFI_WIDTH ) );
    fields.add( TtiUtils.createHidden( TFI_HEIGHT ) );

    return new PropertableEntitiesTinTypeInfo<>( fields, AbstractViselRenderer.class );
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Наследник должен добавить описания специфичных полей.
   *
   * @param fields IStridablesListEdit&lt;ITinFieldInfo> - редактируемый список описаний полей
   */
  protected abstract void addTinTypeInfoes( IStridablesListEdit<ITinFieldInfo> fields );

  /**
   * Subclass must create the renderer.
   *
   * @param aCfg {@link ViselRendererCfg} - the configuration data
   * @param aVisel {@link IVedVisel} - the corresponding VISEL
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @return AbstractViselRenderer - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException config entity kind does not matches provided entity kind
   * @throws AvTypeCastRtException any property value is not compatible to the property definition
   */
  protected abstract AbstractViselRenderer doCreate( ViselRendererCfg aCfg, IVedVisel aVisel, VedScreen aVedScreen );

}
