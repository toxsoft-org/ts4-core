package org.toxsoft.core.tsgui.mws.bases;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.toxsoft.core.tsgui.bricks.quant.*;

/**
 * The application-wide quant manager.
 * <p>
 * The only instance of this interface is created and placed in {@link MApplication#getContext()} by the
 * <code>AddonMwsMain</code> from the <code>org.toxsoft.core.tsgui.mws</code> plugin. The singleton instance contains
 * all the quants created and registered in the application.
 * <p>
 * Application level initialization {@link IQuant#initApp(IEclipseContext)} must be performed by the quant creator
 * <b>before</b> the quant is registered in this manager. However windows level methods
 * ({@link IQuant#initWin(IEclipseContext) initWin()}, {@link IQuant#canCloseMainWindow(IEclipseContext, MWindow)
 * canCloseMainWindow()}, {@link IQuant#whenCloseMainWindow(IEclipseContext, MWindow) whenCloseMainWindow()}) will be
 * called for each application {@link MTrimmedWindow} from the {@link MwsAbstractPart}.
 *
 * @author hazard157
 */
public interface IApplicationWideQuantManager
    extends IQuant {

  // no additional API, interface is declared to access singleton by the class reference in context

}
