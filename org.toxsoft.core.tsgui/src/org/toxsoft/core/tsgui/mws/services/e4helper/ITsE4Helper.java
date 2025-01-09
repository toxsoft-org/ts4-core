package org.toxsoft.core.tsgui.mws.services.e4helper;

import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.e4.ui.model.application.ui.*;
import org.eclipse.e4.ui.model.application.ui.advanced.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Service provides methods to perform common tasks in E4-based GUI.
 * <p>
 * Reference to the instance of this interface is provided in windows level context.
 *
 * @author hazard157
 */
public interface ITsE4Helper {

  /**
   * Updates all E4 commands enabled state.
   * <p>
   * For all E4 command handlers calls method annotated with {@link CanExecute}.
   */
  void updateHandlersCanExecuteState();

  /**
   * Switches active perspective and optionally activates the specified UIpart.
   *
   * @param aPerspectiveId String - the perspective ID
   * @param aActivatePartId String - UIpart ID or <code>null</code> for default
   * @return {@link MPart} - activated UIpart ID or <code>null</code> if no UIpart was specified
   * @throws TsNullArgumentRtException aPerspectiveId = null
   */
  MPart switchToPerspective( String aPerspectiveId, String aActivatePartId );

  /**
   * Finds the perspective by ID.
   *
   * @param aPerspectiveId String - the perspective ID
   * @return {@link MPerspective} - found perspective or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  MPerspective findPerspective( String aPerspectiveId );

  /**
   * Finds the part by ID.
   *
   * @param aPartId String - the part ID
   * @return {@link MPart} - found part or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  MPart findPart( String aPartId );

  /**
   * Returns current perspective ID.
   *
   * @return String - current perspective ID or <code>null</code> if no perspective is active
   */
  String currentPerspId();

  /**
   * Returns the ID of the currently active (focused) UIpart.
   *
   * @return String - active UIpart ID or <code>null</code> if no UIpart is active
   */
  String currentPartId();

  /**
   * Quits the application.
   */
  void quitApplication();

  /**
   * Runs E4 command without parameters.
   *
   * @param aCmdId String - E4 command ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void execCmd( String aCmdId );

  /**
   * Runs E4 command with parameters.
   *
   * @param aCmdId String - E4 command ID
   * @param aArgValues {@link IStringMap}&lt;String&gt; - copmmand params as map "param ID" - "value"
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException aArgValues contains unknown parameter
   */
  void execCmd( String aCmdId, IStringMap<String> aArgValues );

  /**
   * Searches for the specified element in e4 model of application.
   *
   * @param <T> - the type of element being searched for
   * @param aRoot {@link MElementContainer} - search root in e4 model of application
   * @param aId String - the ID of element being searched for
   * @param aClass {@link Class}&lt;T&gt; - the type of element being searched for
   * @param aFlags int - search flags {@link EModelService}<code>.XXX</code>
   * @return &lt;T&gt; - found element or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  <T> T findElement( MElementContainer<?> aRoot, String aId, Class<T> aClass, int aFlags );

  /**
   * Sets perspective visibility state.
   * <p>
   * Does nothing if no such element exists in application E4 model.
   *
   * @param aPerspectiveId String - the perspective ID
   * @param aVisible boolean - visibility state to set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setPrerspectiveVisible( String aPerspectiveId, boolean aVisible );

  /**
   * Sets UIpart visibility state.
   * <p>
   * Does nothing if no such element exists in application E4 model.
   *
   * @param aUipartId String - the UIpart ID
   * @param aVisible boolean - visibility state to set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setUipartVisible( String aUipartId, boolean aVisible );

  /**
   * Sets menu element (item or menu) visibility state.
   * <p>
   * Does nothing if no such element exists in application E4 model.
   *
   * @param aMenuElementId String - the menu element ID
   * @param aVisible boolean - visibility state to set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setMenuItemVisible( String aMenuElementId, boolean aVisible );

  /**
   * Sets tool item visibility state.
   * <p>
   * Does nothing if no such element exists in application E4 model.
   *
   * @param aToolItemId String - the tool item ID
   * @param aVisible boolean - visibility state to set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setToolItemVisible( String aToolItemId, boolean aVisible );

  /**
   * Perspective change eventer.
   * <p>
   * <b>Note:</b>: as of 17/06/2022 eventer {@link #perspectiveEventer()} fire events only when perspectives are changed
   * through {@link #switchToPerspective(String, String)} method (including switching with MWS command
   * {@link IMwsCoreConstants#MWSID_CMD_SWITCH_PERSP}). Switching perspectives via E4 builtin means <b>does not
   * fires</b> {@link ITsE4PerspectiveSwitchListener#onPerspectiveChanged(ITsE4Helper, String)} events!
   *
   * @return {@link ITsE4Helper}&lt;{@link ITsE4PerspectiveSwitchListener}&gt; - the eventer
   */
  ITsEventer<ITsE4PerspectiveSwitchListener> perspectiveEventer();

  // ------------------------------------------------------------------------------------
  // inline methods for convenience

  /**
   * Searches for the specified element in e4 model of application.
   * <p>
   * Searches everywhere inclusive under the specified root element.
   *
   * @param <T> - the type of element being searched for
   * @param aRoot {@link MElementContainer} - search root in e4 model of application
   * @param aId String - the ID of element being searched for
   * @param aClass {@link Class}&lt;T&gt; - the type of element being searched for
   * @return &lt;T&gt; - found element or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  default <T> T findElement( MElementContainer<?> aRoot, String aId, Class<T> aClass ) {
    int flags = EModelService.ANYWHERE | EModelService.IN_SHARED_ELEMENTS | EModelService.IN_MAIN_MENU
        | EModelService.IN_SHARED_ELEMENTS;
    return findElement( aRoot, aId, aClass, flags );
  }

}
