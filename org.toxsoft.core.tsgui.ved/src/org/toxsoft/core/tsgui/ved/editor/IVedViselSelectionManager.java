package org.toxsoft.core.tsgui.ved.editor;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages VISELs selection (including multi-selections) on the screen.
 * <p>
 * All methods fire an {@link IGenericChangeListener#onGenericChangeEvent(Object)} event only if selection really
 * changes.
 * <p>
 * Implementation listens to the changes in {@link IVedScreenModel#visels()} and appropriately updates selection list on
 * VISEL removal from model.
 *
 * @author hazard157
 */
public interface IVedViselSelectionManager
    extends IGenericChangeEventCapable {

  /**
   * Determines the selection kind.
   * <p>
   * Selection kind is simple shortcut to check number of the selected VISELs:
   * <ul>
   * <li>0 selected VISELs - {@link ESelectionKind#NONE};</li>
   * <li>1 selected VISEL - {@link ESelectionKind#SINGLE};</li>
   * <li>2 or more selected VISELs - {@link ESelectionKind#MULTI};</li>
   * </ul>
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
    SINGLE,

    /**
     * Several components are selected at once.
     */
    MULTI
  }

  /**
   * Determines selection kind.
   *
   * @return {@link ESelectionKind} - selection kind
   */
  ESelectionKind selectionKind();

  // ------------------------------------------------------------------------------------
  //
  //

  /**
   * Returns selected VISELs.
   * <p>
   * Note: order of the returned list does <b>not</b> matches the order of VISELs in the
   * {@link IVedScreenModel#visels()}.
   *
   * @return {@link IStridablesList} - selected VSEL IDs
   */
  IStringList selectedViselIds();

  /**
   * Returns one selected VISEL.
   * <p>
   * For selection kinds {@link ESelectionKind#NONE} and {@link ESelectionKind#MULTI} returns <code>null</code>.
   *
   * @return String - selected VISEL ID of <code>null</code>
   */
  String singleSelectedViselId();

  /**
   * Sets single VISEL selection.
   * <p>
   * Sets {@link #selectionKind()} to {@link ESelectionKind#SINGLE SINGLE}. Argument value <code>null</code> has same
   * effect as {@link #deselectAll()}.
   *
   * @param aViselId String - ID of the only VISEL to be set as selected or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such VISEL exists in the VED model
   */
  void setSingleSelectedViselId( String aViselId );

  /**
   * Sets the specified VISEL selection state.
   *
   * @param aViselId String - ID of the VISEL to change selection state
   * @param aSelection boolean - the selection state to set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such VISEL exists in the VED model
   */
  void setViselSelection( String aViselId, boolean aSelection );

  /**
   * Sets the specified VISEL selection state.
   * <p>
   * Other VISELs selection state remains unchanged.
   *
   * @param aViselIds {@link IStringList} - IDs of the VISEL to change selection state
   * @param aSelection boolean - the selection state to set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException one of the specified VISEL not exists in the VED model
   */
  void setViselsSelection( IStringList aViselIds, boolean aSelection );

  /**
   * Sets selected VISELs list to the specified VISEL.
   * <p>
   * All other VISELs became unselected.
   *
   * @param aSelectedViselIds {@link IStringList} - IDs of VISELs to be selected
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException one of the specified VISEL not exists in the VED model
   */
  void setSelectedVisels( IStringList aSelectedViselIds );

  /**
   * Toggles VISEL selection state.
   *
   * @param aViselId String - the VISEL ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such VISEL exists in the VED model
   */
  void toggleSelection( String aViselId );

  /**
   * Selects all VISELs in the model.
   */
  void selectAll();

  /**
   * Clears selection.
   */
  void deselectAll();

  /**
   * Returns the bound VED screen.
   *
   * @return {@link IVedScreen} - the bound VED screen
   */
  IVedScreen vedScreen();

  // ------------------------------------------------------------------------------------
  // Inline methods for convenience

  @SuppressWarnings( "javadoc" )
  default boolean isSelected( String aViselId ) {
    return selectedViselIds().hasElem( aViselId );
  }

}
