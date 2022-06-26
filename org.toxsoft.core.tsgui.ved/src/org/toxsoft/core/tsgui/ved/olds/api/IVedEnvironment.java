package org.toxsoft.core.tsgui.ved.olds.api;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;

public interface IVedEnvironment {

  IVedLibraryManager libraryManager();

  IVedDataModel dataModel();

  IVedScreen activeScreen();

  void activateScreen( IVedScreen aScreen );

  IList<IVedScreen> screensList();

  IGenericChangeEventer activeScreenChangeEventer();

}
