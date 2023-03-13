package org.toxsoft.core.tsgui.bricks.quant;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Quant - any unit with initialization and finalization handling.
 * <p>
 * Quants (correctly - quantums) are small programmatic units in MWS applications, relaizing some funcionality.
 * Generally quants need to be initialized before usage and finalized after. This interface hepls to regulate quants
 * lifecylce.
 * <p>
 * Please not that two instances of the same quant can not be initialized in one MWS application.
 *
 * @author hazard157
 */
public interface IQuant
    extends ICloseable, IQuantRegistrator {

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
   *
   * @param aAppContext {@link IEclipseContext} - the application level context
   */
  void initApp( IEclipseContext aAppContext );

  /**
   * Implementation may perform initialization at when the specified window is open.
   * <p>
   * Called once per window, before window opened.
   * <p>
   * FIXME where to get reference to the MWindow?
   *
   * @param aWinContext {@link IEclipseContext} - the context of the window to be opened
   */
  void initWin( IEclipseContext aWinContext );

  /**
   * Реализация может и перехватить и если нужно, запретить попытку закрытия окна.
   *
   * @param aWinContext {@link IEclipseContext} - контекст уровня главного окна
   * @param aWindow {@link MWindow} - окно
   * @return boolean - признак разрешения закрытия окна<br>
   *         <b>true</b> - окно будет закрыто;<br>
   *         <b>false</b> - окно останется открытым.
   */
  boolean canCloseMainWindow( IEclipseContext aWinContext, MWindow aWindow );

}
