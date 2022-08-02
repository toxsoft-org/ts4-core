package org.toxsoft.core.tsgui.ved.api.view;

import org.toxsoft.core.tsgui.ved.api.*;
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
public interface IVedSelectedComponentManager
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
   * It is simple: empty {@link #selectedComponent()} means NONE selection kind, 1 component means ONE selection, 2 or
   * more selected components - MULTI.
   *
   * @return {@link ESelectionKind} - selection kind
   */
  ESelectionKind selectionKind();

  /**
   * Returns selected components.
   *
   * @return {@link IStridablesList}&lt;{@link IVedComponent}&gt; - selected components
   */
  IStridablesList<IVedComponent> selectedComponents();

  /**
   * Returns one selected component.
   * <p>
   * For there is no selection (kind = ONE) returns <code>null</code>. For multi selection returns first item of the
   * list {@link #selectedComponents()}.
   *
   * @return {@link IVedComponent} - selected component of <code>null</code>
   */
  IVedComponent selectedComponent();

  /**
   * Sets single component selection.
   * <p>
   * <code>null</code> has same effect as {@link #deselectAll()}.
   *
   * @param aComp {@link IVedComponent} - the only component to be set as selected or <code>null</code>
   */
  void setSelectedComponent( IVedComponent aComp );

  /**
   * Toogle component selection.<br>
   * If selection is true and component is already selected do nothing, if selection is false and component is
   * deselected do nothing otherwise toggle componetnt's selection.
   *
   * @param aComp {@link IVedComponent} - component whose selection should be toggled
   * @param aSelection <b>true</b> - component will be selected<br>
   *          <b>false</b> - component will be deselected
   */
  void setComponentSelection( IVedComponent aComp, boolean aSelection );

  /**
   * Sets selected components list.
   *
   * @param aComps {@link IStridablesList}&lt;{@link IVedComponent}&gt; - components to be selected
   */
  void setSelectedComponents( IStridablesList<IVedComponent> aComps );

  /**
   * Toggles components selection state.
   *
   * @param aComp {@link IVedComponent} - the component
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void toggleSelection( IVedComponent aComp );

  // ------------------------------------------------------------------------------------
  //
  //

  /**
   * Returns selected component views.
   *
   * @return {@link IStridablesList}&lt;{@link IVedComponentView}&gt; - selected component views
   */
  IStridablesList<IVedComponentView> selectedComponentViews();

  /**
   * Returns one selected component view.
   * <p>
   * For there is no selection (kind = ONE) returns <code>null</code>. For multi selection returns first item of the
   * list {@link #selectedComponents()}.
   *
   * @return {@link IVedComponentView} - selected component view of <code>null</code>
   */
  IVedComponentView selectedComponentView();

  /**
   * Sets single component view selection.
   * <p>
   * <code>null</code> has same effect as {@link #deselectAll()}.
   *
   * @param aView {@link IVedComponentView} - the only component view to be set as selected or <code>null</code>
   */
  void setSelectedComponentView( IVedComponentView aView );

  /**
   * Toogle component view selection.<br>
   * If selection is true and component is already selected do nothing, if selection is false and component is
   * deselected do nothing otherwise toggle componetnt's selection.
   *
   * @param aView {@link IVedComponentView} - component whose selection should be toggled
   * @param aSelection <b>true</b> - component will be selected<br>
   *          <b>false</b> - component will be deselected
   */
  void setComponentViewSelection( IVedComponentView aView, boolean aSelection );

  /**
   * Sets selected component views list.
   *
   * @param aViews {@link IStridablesList}&lt;{@link IVedComponentView}&gt; - component views to be selected
   */
  void setSelectedComponentViews( IStridablesList<IVedComponentView> aViews );

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
