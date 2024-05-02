package org.toxsoft.core.tsgui.bricks.combicond;

import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Single-line text widget for formula editing.
 * <p>
 * Visually looks like common {@link Text} widget. Additionally has ability to change background color of the text
 * substrings.
 *
 * @author hazard157
 */
public interface IFormulaTextWidget
    extends IGenericChangeEventCapable {

  /**
   * Returns the formula text.
   *
   * @return String - the formula text
   */
  String getFormulaText();

  /**
   * Sets the displayed formula text.
   * <p>
   * Clears all style ranges.
   *
   * @param aFormula String - the formula text
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setFormulaText( String aFormula );

  /**
   * Sets the widget tooltip.
   *
   * @param aTooltip String - the tooltip or <code>null</code>
   */
  void setToolTipText( String aTooltip );

  /**
   * Changes the visual display of the selected regions.
   * <p>
   * There are two kind of highlights:
   * <ul>
   * <li>error - starts at <code>aErrorPos</code> char in the formula string up to the end of the text. Error has
   * priority over <code>aHighlights</code>;</li>
   * <li>user specified highlight ranges specified as a list of the {@link StyleRange}.</li>
   * </ul>
   * Highlights must be set after any text changes.
   *
   * @param aErrorPos int - starting position of the error highlighting or -1 if no error
   * @param aHighlights {@link IList}&lt;{@link StyleRange}&gt; - color highlighting ranges
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException <code>aErrorPos</code> is out of range -1..(text length - 1)
   * @throws TsIllegalArgumentRtException any highlight position is out of range
   * @throws TsIllegalArgumentRtException there is an intersection in highlight ranges
   */
  void setHighlights( int aErrorPos, IList<StyleRange> aHighlights );

  /**
   * Returns the color of the error range background.
   *
   * @return {@link RGB} - the error background color
   */
  RGB getErrorHighlightColor();

  /**
   * Sets the color of the error range background.
   *
   * @param aColor {@link RGB} - the error background color
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setErrorHighlightColor( RGB aColor );

  /**
   * Determines if panel content editing is allowed right now.
   *
   * @return boolean - edit mode flag
   */
  boolean isEditable();

  /**
   * Toggles panel content edit mode.
   *
   * @param aEditable boolean - edit mode flag
   */
  void setEditable( boolean aEditable );

  /**
   * Returns implementing SWT control.
   *
   * @return {@link Control} - the SWT control
   */
  Control getControl();

}
