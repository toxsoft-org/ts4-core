package org.toxsoft.core.tsgui.ved.impl;

import static org.toxsoft.core.tsgui.ved.std.IVedStdProperties.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.incub.props.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedComponent} base implementation.
 *
 * @author hazard157
 */
public class VedAbstractComponent
    implements IVedComponent, ITsGuiContextable {

  private final VedAbstractComponentProvider creator;

  private final IOptionSetEdit capabilities = new OptionSet();
  private final IOptionSetEdit extdata      = new OptionSet();

  private final PropertiesSet props;

  private String id;

  /**
   * Contsructor.
   *
   * @param aProvider {@link VedAbstractComponentProvider} - the creator
   * @param aId String - conmonent ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public VedAbstractComponent( VedAbstractComponentProvider aProvider, String aId ) {
    creator = TsNullArgumentRtException.checkNull( aProvider );
    id = StridUtils.checkValidIdPath( aId );
    props = new PropertiesSet( creator.propDefs() );
  }

  // ------------------------------------------------------------------------------------
  // For subclasses
  //

  /**
   * Returns the information about properties.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   */
  public IStridablesList<IDataDef> propDefs() {
    return creator.propDefs();
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  final public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return props().getStr( PID_NAME, EMPTY_STRING );
  }

  @Override
  public String description() {
    return props().getStr( PID_DESCRIPTION, EMPTY_STRING );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    // TODO реализовать VedAbstractComponent.tsContext()
    throw new TsUnderDevelopmentRtException( "VedAbstractComponent.tsContext()" );
  }

  // ------------------------------------------------------------------------------------
  // IPropertable
  //

  @Override
  public IPropertiesSet props() {
    return props;
  }

  // ------------------------------------------------------------------------------------
  // IVedComponent
  //

  @Override
  final public String libraryId() {
    return creator.libraryId();
  }

  @Override
  final public String componentKindId() {
    return creator.id();
  }

  @Override
  public IOptionSet capabilities() {
    return capabilities;
  }

  @Override
  public IOptionSet extdata() {
    return extdata;
  }

  @Override
  public IVedComponentView createView( IVedScreen aScreen ) {
    // TODO реализовать VedAbstractComponent.createView()
    throw new TsUnderDevelopmentRtException( "VedAbstractComponent.createView()" );
  }

}
