package org.toxsoft.core.tsgui.ved.core.view;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages several screens displaing the same data model.
 *
 * @author hazard157
 */
public interface IVedScreenManager {

  /**
   * Creates the screen based on supplied SWT canvas.
   *
   * @param aCanvas {@link Canvas} - the canvas
   * @return {@link IVedScreen} - created screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException there already is the screen bound to this canvas
   */
  IVedScreen createScreen( Canvas aCanvas );

  /**
   * Returns the active screen.
   *
   * @return {@link IVedScreen} - active screen or <code>null</code>
   */
  IVedScreen activeScreen();

  /**
   * Setes screen active.
   * <p>
   * This method must be called by GUI when screen canvas gets the user input focus.
   *
   * @param aScreen {@link IVedScreen} - screen to activate or <code>null</code>
   */
  void activateScreen( IVedScreen aScreen );

  /**
   * Returns all screens created so far.
   *
   * @return {@link IList}&lt;{@link IVedScreen}&gt; - list of existing screens
   */
  IList<IVedScreen> listScreens();

  /**
   * Returns {@link #activeScreen()} change eventer.
   *
   * @return {@link IGenericChangeEventer} - active screen change eventer
   */
  IGenericChangeEventer activeScreenChangeEventer();

}
