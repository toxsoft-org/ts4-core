package org.toxsoft.core.tsgui.ved.api.screen;

import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.gw.time.*;

/**
 * VED screen is interactive panel displaying VED entities.
 * <p>
 * Content of the screen is determined by an editable {@link #model()}, while {@link #view()} is GUI facing the user.
 *
 * @author hazard157
 */
public interface IVedScreen
    extends IGwTimeFleetable, IRealTimeSensitive {

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

}
