package org.toxsoft.core.tsgui.ved.api.impl;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
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
  private final IOptionSet props;
  private final IOptionSet extdata;

  /**
   * Constructor.
   *
   * @param aId String - the ID
   * @param aProps {@link IOptionSet} - properties values
   * @param aExtData {@link IOptionSet} - external data
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public VedConfigBase( String aId, IOptionSet aProps, IOptionSet aExtData ) {
    id = StridUtils.checkValidIdPath( aId );
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
