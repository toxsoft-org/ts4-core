package org.toxsoft.core.tsgui.ved.api.doc;

import org.toxsoft.core.tsgui.ved.api.comp.*;
import org.toxsoft.core.tsgui.ved.api.entity.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Defines {@link IVedTailor} binding to the {@link IVedComponent} property.
 *
 * @author hazard157
 */
public interface IVedBindingProvider
    extends IStridableParameterized {

  /**
   * Determines if components from the specified providers are accepted by this link.
   *
   * @param aComponentProvider {@link IVedEntityProvider} - the component provied
   * @return boolean - <code>true</code> any component from this provider may be linked
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException provider kind is not {@link EVedEntityKind#COMPONENT}
   */
  boolean acceptsComponentsOfProvider( IVedEntityProvider aComponentProvider );

  /**
   * Links component to the owner controller.
   *
   * @param aComponent {@link IVedComponent} - the component
   * @param aProperyId String - the bind property ID
   * @return {@link IVedBinding} - created link
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException this component is not accepter by the link
   * @throws TsItemNotFoundRtException no such property
   */
  IVedBinding createBind( IVedComponent aComponent, String aProperyId );

}
