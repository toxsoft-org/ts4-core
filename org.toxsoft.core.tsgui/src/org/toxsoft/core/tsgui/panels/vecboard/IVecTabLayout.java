package org.toxsoft.core.tsgui.panels.vecboard;

/**
 * Each element of this layout is a separate tab in the tab folder.
 * <p>
 * The order of the tabs is determined by the order in which items are added to the tab folder.
 * <p>
 * When adding component to the folder the layout data {@link IVecTabLayoutData} determines text, icon and tooltip of
 * the tab.
 *
 * @author hazard157
 */
public interface IVecTabLayout
    extends IVecLayout<IVecTabLayoutData> {

  /**
   * Returns the flag for showing tab labels at the bottom of the panel.
   *
   * @return boolean - the flag for showing tab labels at the bottom of the panel<br>
   *         <b>true</b> - tab labels are at the bottom of the folder, below the components;<br>
   *         <b>false</b> - tab labels are at the top of the folder, above the components.
   */
  boolean isTabsAtBottom();

}
