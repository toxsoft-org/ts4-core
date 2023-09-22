package org.toxsoft.core.tsgui.ved.zver2.api.impl;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tsgui.ved.zver2.api.cfgdata.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedConfigBase} immutable implementation.
 *
 * @author hazard157
 */
class VedConfigBase
    implements IVedConfigBase {

  private final String     id;
  private final String     providerId;
  private final IOptionSet props;
  private final IOptionSet extdata;

  /**
   * Constructor.
   *
   * @param aId String - the ID
   * @param aProviderId String - ID of the provider (factory) that creates the entity
   * @param aProps {@link IOptionSet} - properties values
   * @param aExtData {@link IOptionSet} - external data
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public VedConfigBase( String aId, String aProviderId, IOptionSet aProps, IOptionSet aExtData ) {
    id = StridUtils.checkValidIdPath( aId );
    providerId = StridUtils.checkValidIdPath( aProviderId );
    props = new OptionSet( aProps );
    extdata = new OptionSet( aExtData );
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String providerId() {
    return providerId;
  }

  @Override
  public String nmName() {
    return props.getStr( TSID_NAME, EMPTY_STRING );
  }

  @Override
  public String description() {
    return props.getStr( TSID_DESCRIPTION, EMPTY_STRING );
  }

  // ------------------------------------------------------------------------------------
  // IVedConfigBase
  //

  @Override
  public IOptionSet propValues() {
    return props;
  }

  @Override
  public IOptionSet extdata() {
    return extdata;
  }

}
