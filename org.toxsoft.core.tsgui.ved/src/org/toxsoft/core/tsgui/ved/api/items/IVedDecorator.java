package org.toxsoft.core.tsgui.ved.api.items;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.helpers.*;
import org.toxsoft.core.tsgui.ved.devel.*;
import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * The decorator is an entity to be painted on the VED screen.
 * <p>
 * Besides the painting algorithm the decorator has two main characteristics:
 * <ul>
 * <li>Z-order of drawing - is determined by the place where decorator is placed in {@link IVedEnvironment}:
 * <ul>
 * <li>{@link IVedEnvironment#screenDecoratorsBefore() screenDecoratorsBefore()};</li>
 * <li>{@link IVedEnvironment#viselDecoratorsBefore(String) viselDecoratorsBefore(aViselId)};</li>
 * <li>{@link IVedEnvironment#viselDecoratorsAfter(String) viselDecoratorsAfter(aViselId)};</li>
 * <li>{@link IVedEnvironment#screenDecoratorsAfter() screenDecoratorsAfter()};</li>
 * </ul>
 * ;</li>
 * <li>canvas transform selection - VED canvas and each VISEL have their specific {@link ID2Conversion} (rotation, zoom,
 * displacement) settings. When painting on SWT {@link Canvas} specific to screen and VISEL affine transforms are
 * applied before the paint methods are called. The {@link #viselId()} method determines which transform to be set
 * before the {@link #paint(ITsGraphicsContext)} method is called. Returning an existing VISEL ID will set the
 * respective VISEL transform. Returning an empty string or any other string not matching existing VISEL ID will use the
 * screen transform.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IVedDecorator
    extends ITsPaintable, IPointsHost {

  /**
   * Determines screen or VISEL transform to use.
   * <p>
   * In other words, determines if it is specific VISEL's decorator or independently drawn on the VED canvas.
   *
   * @return String - the VISEL ID or any other non-<code>null</code> string for screen transform
   */
  String viselId();

}
