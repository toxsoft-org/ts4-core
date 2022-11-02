package org.toxsoft.core.tsgui.ved.zver1.core;

import org.toxsoft.core.tsgui.ved.zver1.core.base.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Defines link between {@link IVedController} and one {@link IVedComponent}.
 *
 * @author hazard157
 */
public interface IVedCompBindDef
    extends IStridableParameterized {

  /**
   * Determines if components from the specified providers are accepted by this link.
   *
   * @param aComponentProvider {@link IVedComponentProvider} - the component provied
   * @return boolean - <code>true</code> any component from this provider may be linked
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean acceptsComponentsOfProvider( IVedComponentProvider aComponentProvider );

  /**
   * Links component to the owner controller.
   *
   * @param aComponent {@link IVedComponent} - the component
   * @return {@link IVedBinding} - created link
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException this component is not accepter by the link
   */
  IVedCompBind createBind( IVedComponent aComponent, String aCompPropId );

}