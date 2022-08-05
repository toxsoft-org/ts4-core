package org.toxsoft.core.tsgui.ved.core.view;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages component view's selection (inclugin multi-selections) on the screen.
 * <p>
 * All methods fire an {@link IGenericChangeListener#onGenericChangeEvent(Object)} ebent only if selection really
 * changes.
 *
 * @author hazard157
 */
public interface IVedScreenSelectionManager
    extends IGenericChangeEventCapable {

  /**
   * Determines the selection kind.
   *
   * @author hazard157
   */
  enum ESelectionKind {

    /**
     * No component is selected.
     */
    NONE,

    /**
     * Only one component is selected.
     */
    ONE,

    /**
     * Several components are selected at once.
     */
    MULTI
  }

  /**
   * Determines selection kind.
   * <p>
   * It is simple: empty {@link #selectedViews()} means NONE selection kind, 1 component means ONE selection, 2 or more
   * selected components - MULTI.
   *
   * @return {@link ESelectionKind} - selection kind
   */
  ESelectionKind selectionKind();

  // ------------------------------------------------------------------------------------
  //
  //

  /**
   * Returns selected component views.
   *
   * @return {@link IStridablesList}&lt;{@link IVedComponentView}&gt; - selected component views
   */
  IStridablesList<IVedComponentView> selectedViews();

  /**
   * Returns one selected component view.
   * <p>
   * For there is no selection (kind = ONE) returns <code>null</code>. For multi selection returns first item of the
   * list {@link #selectedViews()}.
   *
   * @return {@link IVedComponentView} - selected component view of <code>null</code>
   */
  IVedComponentView selectedView();

  /**
   * Sets single component view selection.
   * <p>
   * <code>null</code> has same effect as {@link #deselectAll()}.
   *
   * @param aView {@link IVedComponentView} - the only component view to be set as selected or <code>null</code>
   */
  void setSelectedView( IVedComponentView aView );

  /**
   * Toogle component view selection.<br>
   * If selection is true and component is already selected do nothing, if selection is false and component is
   * deselected do nothing otherwise toggle componetnt's selection.
   *
   * @param aView {@link IVedComponentView} - component whose selection should be toggled
   * @param aSelection <b>true</b> - component will be selected<br>
   *          <b>false</b> - component will be deselected
   */
  void setViewSelection( IVedComponentView aView, boolean aSelection );

  /**
   * Sets selected component views list.
   *
   * @param aViews {@link IStridablesList}&lt;{@link IVedComponentView}&gt; - component views to be selected
   */
  void setSelectedViews( IStridablesList<IVedComponentView> aViews );

  /**
   * Toggles component view selection state.
   *
   * @param aView {@link IVedComponentView} - the component
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void toggleSelection( IVedComponentView aView );

  /**
   * Clears selection.
   */
  void deselectAll();

}
