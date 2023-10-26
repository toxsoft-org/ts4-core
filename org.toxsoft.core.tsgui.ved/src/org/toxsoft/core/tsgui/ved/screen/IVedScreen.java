package org.toxsoft.core.tsgui.ved.screen;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.gw.time.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VED screen is interactive panel displaying VED entities.
 * <p>
 * Content of the screen is determined by an editable {@link #model()}, while {@link #view()} is GUI facing the user.
 * <p>
 * TODO comments: VED screen
 * <p>
 * Notes:
 * <ul>
 * <li>{@link #attachTo(Canvas)} must be called before actual use the VED screen;</li>
 * <li>implements {@link IPausableAnimation} allowing to temporary deactivate (pause) actors time handling (both RT and
 * GW times). However, pausing actors does not affects VISELs animated drawing.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IVedScreen
    extends IGwTimeFleetable, IRealTimeSensitive, IPausableAnimation, ITsGuiContextable {

  /**
   * Returns the content of the screen - the displayable and interactive entities.
   *
   * @return {@link IVedScreenModel} - the editable data model of the screen
   */
  IVedScreenModel model();

  /**
   * Returns the visual representation of the screen,
   *
   * @return {@link IVedScreenView} - the GUI panel of the screen
   */
  IVedScreenView view();

  /**
   * Attaches created VED screen to the SWT canvas.
   *
   * @param aCanvas {@link Canvas} - SWT canvas to draw on and handle user input
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void attachTo( Canvas aCanvas );

}
