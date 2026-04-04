package org.toxsoft.core.tsgui.widgets.tooltip;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Provides data to be displayed by {@link TsTooltipWindow}.
 * <p>
 * May be used a a mix-in interface for controls with tooltips.
 *
 * @author hazard157
 */
public interface ITsTooltipDataProvider {

  /**
   * Data to be displayed by the tooltip window.
   * <p>
   * The {@link #image} is owned and must be disposed by provider.
   *
   * @author hazard157
   * @param image {@link Image} - the image to displayed, may be <code>null</code>
   * @param tooltip {@link String} - tooltip text to display, may be <code>null</code>
   */
  public record Data( Image image, String tooltip ) {}

  /**
   * Implementation must return the data to by displayed as a tooltip.
   *
   * @param aControl {@link Control} - the control causing the tooltip to appear
   * @param aMouseX int - mouse X coordinate relative to control
   * @param aMouseY int - mouse Y coordinate relative to control
   * @return {@link Data} - the data to display, may be <code>null</code>
   */
  Data getTsTooltipData( Control aControl, int aMouseX, int aMouseY );

}
