package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedItemFactoryBase} base implementation.
 *
 * @author hazard157
 * @param <T> - exact type of the managed instances
 */
public abstract class VedAbstractItemFactory<T extends VedAbstractItem>
    extends StridableParameterized
    implements IVedItemFactoryBase<T> {

  private ITinTypeInfo                           tinTypeInfo    = null;
  private IStridablesList<IDataDef>              propDefs       = null;
  private IStridablesList<IVedItemsPaletteEntry> paletteEntries = null;

  /**
   * Constructor.
   *
   * @param aId String - the
   * @param aIdsAndValues Object[] - identifier / value pairs of the {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public VedAbstractItemFactory( String aId, Object... aIdsAndValues ) {
    super( aId, OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

  // ------------------------------------------------------------------------------------
  // IVedItemFactoryBase
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
  public IStridablesList<IVedItemsPaletteEntry> paletteEntries() {
    if( paletteEntries == null ) {
      IStridablesList<IVedItemsPaletteEntry> ll = doCreatePaletteEntries();
      // check the returned list
      TsInternalErrorRtException.checkNull( ll );
      TsInternalErrorRtException.checkTrue( ll.isEmpty() );
      for( IVedItemsPaletteEntry entry : ll ) {
        if( !entry.itemCfg().factoryId().equals( id() ) ) {
          throw new TsInternalErrorRtException();
        }
      }
      paletteEntries = ll;
    }
    return paletteEntries;
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
  public T create( IVedItemCfg aCfg, VedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNulls( aCfg, aVedScreen );
    TsIllegalArgumentRtException.checkFalse( aCfg.factoryId().equals( id() ) );
    OptionSetUtils.checkOptionSet( aCfg.propValues(), propDefs() );
    T item = doCreate( aCfg, aVedScreen );
    TsInternalErrorRtException.checkNull( item );
    item.params().setAll( aCfg.params() );
    item.props().setProps( aCfg.propValues() );
    item.doUpdateCachesAfterPropsChange( new OptionSet( item.props() ) );
    return item;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may create own set of the palette entries.
   * <p>
   * Method is called once, on first call to {@link #paletteEntries()}.
   * <p>
   * In the base class creates the list with one entry with the default {@link #propDefs()} item configuration. Subclass
   * may call base class method and add new entries or create own list without calling parent method.
   *
   * @return {@link StridablesList}&lt;{@link IVedItemsPaletteEntry}&gt; - an editable list
   */
  protected StridablesList<IVedItemsPaletteEntry> doCreatePaletteEntries() {
    VedItemCfg cfg = new VedItemCfg( id(), id(), IOptionSet.NULL );
    OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
    IVedItemsPaletteEntry entry = new VedItemPaletteEntry( id(), params(), cfg );
    return new StridablesList<>( entry );
  }

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
   * Subclass must create the VED item.
   *
   * @param aCfg {@link IVedItemCfg} - the configuration data
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @return &lt;T&gt; - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException config entity kind does not matches provided entity kind
   * @throws AvTypeCastRtException any property value is not compatible to the property definition
   */
  protected abstract T doCreate( IVedItemCfg aCfg, VedScreen aVedScreen );

}
