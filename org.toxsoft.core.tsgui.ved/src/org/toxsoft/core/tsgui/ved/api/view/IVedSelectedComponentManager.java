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
     * Nop component is selected.
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
   * Sets single component selection
   *
   * @param aComp {@link IVedComponent} - the only component to be set as selcted
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setSelectedComponent( IVedComponent aComp );

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

  /**
   * Clears selection.
   */
  void deselectAll();

}
