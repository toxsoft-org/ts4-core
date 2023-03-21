package org.toxsoft.core.tsgui.bricks.quant;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Quant - any unit with initialization and finalization handling.
 * <p>
 * Quant (correctly - quantum) is small programmatic unit in MWS applications, realizing some functionality. Generally
 * quants need to be initialized before usage and finalized after. This interface helps to regulate quants lifecylce.
 * <p>
 * Please not that two instances of the same quant can not be initialized in one MWS application.
 *
 * @author hazard157
 */
public interface IQuant
    extends IQuantRegistrator, ICloseable {

  /**
   * Returns the name of the quant.
   * <p>
   * Quant name is any non-blank string. Name must be unique among all quants. Quants with duplicated names will be
   * ignored with appropriate warning message in log.
   *
   * @return String - non-blank quant name
   */
  String name();

  /**
   * Implementation may perform initialization at application start, before any window is open.
   * <p>
   * Called once, at application start.
   * <p>
   * Reference to the application is placed in the context as {@link MApplication}.
   *
   * @param aAppContext {@link IEclipseContext} - the application level context
   */
  void initApp( IEclipseContext aAppContext );

  /**
   * Implementation may perform initialization at when the specified window is open.
   * <p>
   * Called once per window, before window opened.
   * <p>
   * Reference to the window is placed in the context. FIXME {@link MTrimmedWindow} or {@link MWindow} ?
   *
   * @param aWinContext {@link IEclipseContext} - the context of the window to be opened
   */
  void initWin( IEclipseContext aWinContext );

  /**
   * The implementation may intercept and, if necessary, prevent the attempt to close the window.
   *
   * @param aWinContext {@link IEclipseContext} - the window context
   * @param aWindow {@link MWindow} - the window to be closed
   * @return boolean - permission to close the window<br>
   *         <b>true</b> - the window will be closed;<br>
   *         <b>false</b> - the window will remain open.
   */
  boolean canCloseMainWindow( IEclipseContext aWinContext, MWindow aWindow );

  /**
   * Called just before the window is closed.
   *
   * @param aWinContext {@link IEclipseContext} - the window context
   * @param aWindow {@link MWindow} - the window to be closed
   */
  void whenCloseMainWindow( IEclipseContext aWinContext, MWindow aWindow );

  /**
   * Called before application finishes.
   * <p>
   * Warning: at this point all windows are closed and obviously there is no main GUI threads. So this method is called
   * in some execution thread, not a GUI one.
   */
  @Override
  void close();

}
