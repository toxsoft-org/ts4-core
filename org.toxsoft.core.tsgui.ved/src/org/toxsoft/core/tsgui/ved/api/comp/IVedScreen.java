package org.toxsoft.core.tsgui.ved.api.comp;

import org.eclipse.swt.dnd.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.api.doc.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VED screen is interactive visualization of components {@link IVedDocument}.
 * <p>
 * The may be several screens displaying the same document with different zoom factor and/or vieweport.
 * <p>
 * Closing screen by {@link #close()} removes it from the {@link IVedScreenManager#listScreens()}.
 *
 * @author hazard157
 */
public interface IVedScreen
    extends ICloseable, ID2ConversionableEx, ITsUserInputProducer {

  /**
   * Returns component views owned by this screen.
   * <p>
   * List of views exactly corresponds to the components in data model {@link IVedDocument#components()}.
   *
   * @return {@link IStridablesList}&lt;{@link IVedComponentView}&fr; - list of views
   */
  IStridablesList<IVedComponentView> listViews();

  /**
   * Returns screen painters manager.
   *
   * @return {@link IVedScreenPaintingManager} - painting manager
   */
  IVedScreenPaintingManager paintingManager();

  /**
   * Returns the means to manage selected components on this screen.
   *
   * @return {@link IVedScreenSelectionManager} - selection manager
   */
  IVedScreenSelectionManager selectionManager();

  /**
   * returns normal to screen (and vice versa) coordinates convertor.
   *
   * @return {@link ID2Convertor} - the coordinates convertor
   */
  ID2Convertor coorsConvertor();

  /**
   * Creates drag-and-drop target for this screen.
   * <p>
   * Previously created {@link DropTarget} will be disposed. Cretaed instance will be disposed by screen.
   * <p>
   * Note: created {@link DropTarget} allows only {@link DND#DROP_COPY} operation.
   *
   * @param aAllowedTypes {@link IList}&lt;{@link Transfer}&gt; - allowed types of data accepted by screen
   * @return {@link DropTarget} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  DropTarget createDropTarget( IList<Transfer> aAllowedTypes );

}
