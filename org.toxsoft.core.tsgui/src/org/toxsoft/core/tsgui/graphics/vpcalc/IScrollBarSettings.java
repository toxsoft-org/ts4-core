package org.toxsoft.core.tsgui.graphics.vpcalc;

import org.eclipse.swt.widgets.*;

/**
 * The {@link ScrollBar} settings.
 * <p>
 * This is the value;</li>s as defined by the method {@link ScrollBar#setValues(int, int, int, int, int, int)}:
 * <ul>
 * <li>{@link #selection()}} - the new selection value;</li>
 * <li>{@link #minimum} - the new minimum value;</li>
 * <li>{@link #maximum} - the new maximum value;</li>
 * <li>{@link #thumb} - the new thumb value;</li>
 * <li>{@link #increment} - the new increment value;</li>
 * <li>{@link #pageIncrement} - the new pageIncrement value;</li>
 * </ul>
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IScrollBarSettings {

  int selection();

  int minimum();

  int maximum();

  int thumb();

  int increment();

  int pageIncrement();

}
