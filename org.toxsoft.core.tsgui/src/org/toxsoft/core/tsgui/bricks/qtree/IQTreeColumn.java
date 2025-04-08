package org.toxsoft.core.tsgui.bricks.qtree;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Colemn of the {@link IQTreeViewer}.
 *
 * @author hazard157
 */
public interface IQTreeColumn {

  /**
   * Returns column header text.
   *
   * @return String - text on the header bar
   */
  String title();

  /**
   * Sets column header text.
   * <p>
   * Changes are immediately shown in tree viewer.
   *
   * @param aTitle String - text on the header bar
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setTitle( String aTitle );

  /**
   * Returns the cell visuals (text, icon) provider.
   *
   * @return {@link ITsVisualsProvider}&lt;{@link IQNode}&gt; - cell visuals provider
   */
  ITsVisualsProvider<IQNode> visProvider();

  /**
   * Returns the text horizontal alignment in the cells of this column.
   * <p>
   * Alignments {@link EHorAlignment#FILL} for cell text is treated as {@link EHorAlignment#CENTER}.
   *
   * @return {@link EHorAlignment} - horizontal alignment in cell
   */
  EHorAlignment alignment();

  /**
   * Sets the text horizontal alignment in the cells of this column.
   * <p>
   * Changes are immediately shown in tree viewer.
   *
   * @param aAlignment {@link EHorAlignment} - horizontal alignment in cell
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setAlignment( EHorAlignment aAlignment );

  /**
   * Returns tooltip text for the column header.
   *
   * @return String - tooltip text for the column header
   */
  String tooltip();

  /**
   * Sets tooltip text for the column header.
   * <p>
   * Changes are immediately shown in tree viewer.
   *
   * @param aTooltip String - tooltip text for the column header
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setTooltip( String aTooltip );

  /**
   * Returns the icon image show in the column header.
   *
   * @return {@link Image} - column header icon image or <code>null</code> for no icon
   */
  Image headerImage();

  /**
   * Sets the icon image show in the column header.
   * <p>
   * Changes are immediately shown in tree viewer.
   *
   * @param aImage {@link Image} - column header icon image or <code>null</code> for no icon
   */
  void setHeaderImage( Image aImage );

  /**
   * Returns the column width in pixels.
   * <p>
   * For a hidden columnt ({@link #isHidden()} = <code>true</code>) return width of column before it was hidden, that is
   * the width after column becames visible.
   *
   * @return int - column with in pixels
   */
  int width();

  /**
   * Sets the column width in pixels.
   * <p>
   * Negative values of argument is ignored.
   * <p>
   * Changes are immediately shown in tree viewer.
   * <p>
   * Setting width for the hidden column does nt makes it visible.
   *
   * @param aPixelWidth int - column with in pixels
   */
  void setWidth( int aPixelWidth );

  /**
   * Sets column width to the default value.
   * <p>
   * This method calls {@link TreeColumn#pack()} to set the width.
   */
  void pack();

  /**
   * Adjusts column width so the given text will fit with small extra space.
   * <p>
   * Changes are immediately shown in tree viewer.
   *
   * @param aSampleString String - sample string to adjust column width to fit it
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void adjustWidth( String aSampleString );

  /**
   * Determines if column is hidden.
   * <p>
   * Hidden column remains in tree viewer but has width of 0.
   *
   * @return boolean - <code>true</code> column is hidden, <code>false</code> - visible
   */
  boolean isHidden();

  /**
   * Hides or shows the column.
   * <p>
   * Changes are immediately shown in tree viewer.
   *
   * @param aHidden boolean - <code>true</code> to hide column, <code>false</code> - make visible
   */
  void setHidden( boolean aHidden );

  /**
   * Determines if in column cells the thumb (not icon) images will be drawn.
   * <p>
   *
   * @return boolean - <code>true</code> if thumbs will drawn, <code>false</code> - icons
   */
  boolean isUseThumb();

  /**
   * Sets if in column cells the thumb (not icon) images will be drawn.
   *
   * @param aUseThumb boolean - thibs instead of icons flag
   */
  void setUseThumb( boolean aUseThumb );

}
