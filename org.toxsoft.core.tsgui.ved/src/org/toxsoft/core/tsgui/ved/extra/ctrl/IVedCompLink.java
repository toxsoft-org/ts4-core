package org.toxsoft.core.tsgui.ved.extra.ctrl;

import org.toxsoft.core.tslib.av.props.*;

/**
 * The link to the component.
 *
 * @author hazard157
 */
public sealed interface IVedCompLink
    extends IPropertable permits VedAbstractCompLink {

  String componentId();

  String linkId();

}
