package org.toxsoft.core.tsgui.ved.screen.items;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base interface of factories of VED items of all kinds.
 *
 * @author hazard157
 * @param <T> - exact type of the managed instances
 */
public interface IVedItemFactoryBase<T extends VedAbstractItem>
    extends IStridableParameterized {

  /**
   * Returns the type information for item to be viewed and edited in object inspector.
   *
   * @return {@link ITinTypeInfo} - the information for inspecting the VED item instance
   */
  ITinTypeInfo typeInfo();

  /**
   * Returns the information about VED item properties.
   * <p>
   * The list of properties is unambiguously compiled from the information provided by {@link #typeInfo()}.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   */
  IStridablesList<IDataDef> propDefs();

  /**
   * Creates the entity instance with default values of fields.
   *
   * @param aCfg {@link IVedItemCfg} - the configuration data
   * @param aVedScreen {@link IVedScreen} - the owner VED screen
   * @return &lt;T&gt; - created instance of the item
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException config entity kind does not matches provided entity kind
   * @throws AvTypeCastRtException any property value is not compatible to the property definition
   */
  T create( IVedItemCfg aCfg, IVedScreen aVedScreen );

}
