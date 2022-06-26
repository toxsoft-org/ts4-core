package org.toxsoft.core.tsgui.ved.api.view;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Manages several screens displaing the same data model.
 *
 * @author hazard157
 */
public interface IVedScreenManager {

  // TODO API ???

  IVedScreen activeScreen();

  void activateScreen( IVedScreen aScreen );

  IList<IVedScreen> screensList();

  IGenericChangeEventer activeScreenChangeEventer();

}
