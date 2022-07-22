package org.toxsoft.core.tsgui.ved.api.view;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * VED screen is interactive visualization of {@link IVedDataModel}.
 * <p>
 * The may be several screens displaying the same data model with different zoom factor and/or vieweport.
 * <p>
 * Closing screen by {@link #close()} removes it from the {@link IVedScreenManager#listScreens()}.
 *
 * @author hazard157
 */
public interface IVedScreen
    extends ICloseable {

  /**
   * Returns component views owned by this screen.
   * <p>
   * List of views exactly corresponds to the components in data model {@link IVedDataModel#listComponents()}.
   *
   * @return {@link IStridablesList}&lt;{@link IVedComponentView}&fr; - list of views
   */
  IStridablesList<IVedComponentView> listViews();

  /**
   * Returns current conversion of normal coordinate space of component to painting coordinates space.
   *
   * @return {@link ID2Conversion} - current conversion parameters
   */
  ID2Conversion getConversion();

  /**
   * Sets the conversion parameters to be used in further paintings.
   *
   * @param aConversion {@link ID2Conversion} - conversion parameters
   */
  void setConversion( ID2Conversion aConversion );

  /**
   * Returns the manager of the tools owned by this screen.
   *
   * @return {@link IVedScreenToolsManager} - screen tools manager
   */
  IVedScreenToolsManager toolsManager();

  /**
   * Returns the means to manage selected components on this screen.
   *
   * @return {@link IVedSelectedComponentManager} - selection manager
   */
  IVedSelectedComponentManager selectionManager();

}
