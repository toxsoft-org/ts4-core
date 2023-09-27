package org.toxsoft.core.tsgui.ved.impl;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedItem} base implementation.
 *
 * @author hazard157
 */
public class VedAbstractItem
    implements IVedItem, IParameterizedEdit {

  /**
   * FIXME add dispose()
   */

  private final String         id;
  private final IOptionSetEdit params;
  private final IPropertiesSet propSet;

  /**
   * Constructor for subclasses.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  protected VedAbstractItem( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs ) {
    TsNullArgumentRtException.checkNulls( aConfig, aPropDefs );
    id = aConfig.id();
    params = new OptionSet( aConfig.params() );
    propSet = new PropertiesSet( aPropDefs );
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  final public String id() {
    return id;
  }

  @Override
  final public String nmName() {
    return DDEF_NAME.getValue( propSet ).asString();
  }

  @Override
  final public String description() {
    return DDEF_DESCRIPTION.getValue( propSet ).asString();
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  final public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IPropertable
  //

  @Override
  final public IPropertiesSet props() {
    return propSet;
  }

}
