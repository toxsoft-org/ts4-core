package org.toxsoft.core.tsgui.ved.impl;

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
    implements IVedComponent {

  private final VedAbstractComponentProvider creator;

  private final IOptionSetEdit capabilities = new OptionSet();
  private final IOptionSetEdit extdata      = new OptionSet();

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
    if( propDefs().hasElem() ) {

    }
  }

  @Override
  public String description() {
    // TODO Auto-generated method stub
  }

  // ------------------------------------------------------------------------------------
  // IPropertable
  //

  @Override
  public IPropertiesSet props() {
    // TODO Auto-generated method stub
    return null;
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
