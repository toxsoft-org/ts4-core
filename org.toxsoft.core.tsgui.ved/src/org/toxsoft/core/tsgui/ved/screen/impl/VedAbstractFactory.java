package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedViselFactory} base implementation.
 * <p>
 * As the {@link #nmName()} and {@link #description()} the {@link #params()} options {@link IAvMetaConstants#DDEF_NAME}
 * and {@link IAvMetaConstants#DDEF_DESCRIPTION} are used respectively.
 *
 * @author hazard157
 */
public abstract class VedAbstractFactory
    extends StridableParameterized
    implements IVedViselFactory {

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
  public VedAbstractFactory( String aId, Object... aIdsAndValues ) {
    super( aId, OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

  // ------------------------------------------------------------------------------------
  // IViselFactoryBase
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
  public VedAbstractVisel create( IVedItemCfg aCfg, IVedScreen aEnv ) {
    TsNullArgumentRtException.checkNulls( aCfg, aEnv );
    TsIllegalArgumentRtException.checkFalse( aCfg.factoryId().equals( id() ) );
    OptionSetUtils.checkOptionSet( aCfg.propValues(), propDefs() );
    VedAbstractVisel visel = doCreate( aCfg, aEnv );
    TsInternalErrorRtException.checkNull( visel );
    return visel;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass must create (once in a lifetime) the type info used as {@link #typeInfo()}.
   * <p>
   * Warning: there are following restrictions on created type info:
   * <ul>
   * <li>type must have at least one child for the created item to have at least one property;</li>
   * <li>it is not allowed for any child to be group {@link ETinTypeKind#GROUP} because such field can not be directly
   * converted to the property of atomic type.</li>
   * </ul>
   *
   * @return {@link ITinTypeInfo} - the type information for inspector
   */
  protected abstract ITinTypeInfo doCreateTypeInfo();

  /**
   * Subclass must create the VISEL.
   * <p>
   * Note: method returns {@link VedAbstractVisel} implementation of a VISEL.
   *
   * @param aCfg {@link IVedItemCfg} - the VISEL configuration data
   * @param aScreen {@link IVedScreen} the environment
   * @return {@link VedAbstractVisel} - created entity
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException config entity kind does not matches provided entity kind
   * @throws AvTypeCastRtException any property value is not compatible to the property definition
   */
  protected abstract VedAbstractVisel doCreate( IVedItemCfg aCfg, IVedScreen aScreen );

}
