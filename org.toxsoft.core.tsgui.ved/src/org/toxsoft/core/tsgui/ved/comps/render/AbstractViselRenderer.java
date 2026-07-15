package org.toxsoft.core.tsgui.ved.comps.render;

import org.eclipse.swt.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IViselRenderer} base implementation.
 *
 * @author vs
 */
public abstract class AbstractViselRenderer
    extends StridableParameterized
    implements IViselRenderer, ITsGuiContextable {

  private ITinTypeInfo              tinTypeInfo = null;
  private IStridablesList<IDataDef> propDefs    = null;

  private final IPropertiesSet<IViselRenderer> propSet;

  private final IVedVisel     visel;
  private final ITsGuiContext tsContext;

  /**
   * Constructor.
   *
   * @param aId String - the
   * @param aPropDefs IStridablesList&lt;IDataDef> - props definitions
   * @param aTsContext {@link ITsGuiContext} - corresponding context
   * @param aIdsAndValues Object[] - identifier / value pairs of the {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  protected AbstractViselRenderer( String aId, IStridablesList<IDataDef> aPropDefs, IVedVisel aVisel,
      ITsGuiContext aTsContext, Object... aIdsAndValues ) {
    super( aId, OptionSetUtils.createOpSet( aIdsAndValues ) );
    visel = aVisel;
    tsContext = aTsContext;
    propSet = new PropertiesSet<>( this, aPropDefs ) {

      @Override
      protected void doAfterPropValuesSet( IOptionSet aChangedValues ) {
        doUpdateCachesAfterPropsChange( aChangedValues );
      }
    };
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public final ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IPropertable
  //

  @Override
  final public IPropertiesSet<IViselRenderer> props() {
    return propSet;
  }

  // ------------------------------------------------------------------------------------
  // IViselRenderer
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
  public IVedVisel visel() {
    return visel;
  }

  @Override
  public final void paint( ITsGraphicsContext aPaintContext ) {
    aPaintContext.gc().setAntialias( SWT.ON );
    aPaintContext.gc().setTextAntialias( SWT.ON );
    doPaint( aPaintContext );
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

  protected abstract void doPaint( ITsGraphicsContext aPaintContext );

  protected void doUpdateCachesAfterPropsChange( @SuppressWarnings( "unused" ) IOptionSet aChangedValues ) {
    // nop
  }

}
