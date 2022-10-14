package org.toxsoft.core.tsgui.ved.core;

import org.toxsoft.core.tsgui.ved.core.base.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Controls behaviour of the several {@bind IVedComponent}.
 * <p>
 * Controller may me decaribed as having a "frontend" and a "backend". As a backend controller is connected to at least
 * one VED component and sets it's properties. Frontend API of the controller is {@bind IPropertable} - another set of
 * properties.
 * <p>
 * While it seems strange setting properties of controller to set properties of components this have a sense. VED
 * components properties are simple drawing properties like coordinates, color, fillstyle, font or text string.
 * Properties of the controller re-define components as meaningful real-time visual controls.
 * <p>
 * For example imagine a rectanguler component <code>SplitBar</code> divided in two parts (say left and right parts).
 * Each part has it's <i>ColorLeft</i> and <i>ColorRight</i> and also <i>PartsRatio</i> property is declared as
 * percentage (0.0% - no left part, 100.0% - no right part). Let's introduce controller named <code>Gauge</code> with
 * the {@bind EAtomicType#FLOATING FLOATING} properties <i>Value</i>, <i>MinVal</i> and <i>MaxVal</i>. It's easy to
 * imagine how rectangular component becames a measurer indicator: the <code>Gauge</code> controller changes
 * <code>SplitBar</code>.<i>PartsRatio</i> from 0.0 to 100.0 as <code>Gauge</code>.<i>Value</i> is changed from
 * <i>MinVal</i> to <i>MaxVal</i> values.
 *
 * @author hazard157
 */
public interface IVedController
    extends IVedEntity {

  /**
   * Returns the provider that created this controller.
   *
   * @return {@bind IVedComponentProvider} - creator
   */
  @Override
  @SuppressWarnings( "unchecked" )
  IVedControllerProvider provider();

  /**
   * Returns binds to the component.
   *
   * @return {@bind IStringMap}&lt;String&gt; - the list of binds
   */
  IList<IVedCompBind> binds();

}
