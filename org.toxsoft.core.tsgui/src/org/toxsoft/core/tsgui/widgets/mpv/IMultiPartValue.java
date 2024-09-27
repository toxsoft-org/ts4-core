package org.toxsoft.core.tsgui.widgets.mpv;

import java.time.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The value that consists of several <code>int</code> parts with fixed width textual representation.
 * <p>
 * This interface is designed to visually represent values such as dates "YYYY-MM-DD", time of day "HH:MM:SS.UUU",
 * duration "HHH:MM:SS", foot-inches value "FF'II\"" etc.
 * <p>
 * Note: multi part value consists of several integers but does <b>not</b> defines the way to calculate summary value.
 * Even more, summary value may be calculated as <code>int</code>, <code>long</code>, {@link LocalDateTime} etc.
 *
 * @author hazard157
 */
public interface IMultiPartValue {

  /**
   * Returns the parts of the value.
   *
   * @return {@link IList}&lt;{@link IPart}&gt; - the parts list
   */
  IList<IPart> parts();

  /**
   * Returns the number of <code>char</code> symbols in value text {@link #getValueString()}.
   *
   * @return int - number of characters in text representation of the value
   */
  int getCharLength();

  /**
   * Return the String representation of the value.
   *
   * @return String - visual text
   */
  String getValueString();

  /**
   * Determines if argument may be interpreted as visual text representation of value.
   * <p>
   * If thei method returns {@link EValidationResultType#ERROR} then method {@link #setValueString(String)} will throw
   * an exception.
   *
   * @param aValueString String - text to be converted to value
   * @return {@link ValidationResult} - the validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canSetValueString( String aValueString );

  /**
   * Sets the value parts to the textual representation.
   * <p>
   * This is reverse method to {@link #getValueString()}.
   *
   * @param aValueString String - text to be converted to value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException validation {@link #canSetValueString(String)} failed
   */
  void setValueString( String aValueString );

  /**
   * Sets the part value.
   * <p>
   * Argument <code>aValue</code> must be in allowed range. First of all it must be in range {@link IPart#range()}. Also
   * some instances of this interface may apply additional checks for some parts. For example, editing date as
   * "YYYY-MM-DD" applies additional check on month value. While {@link IPart#range()} is in 1..31, depending on year
   * and month value additionaly may be disabled values 29, 30, 31.
   *
   * @param aPartIndex int - index of the part in the list {@link #parts()}
   * @param aValue int - the value
   * @throws TsIllegalArgumentRtException index is out of range
   * @throws TsValidationFailedRtException value is out of {@link IPart#range()}
   * @throws TsValidationFailedRtException value does not passes additional implemenation-specific checks
   */
  void setPartValue( int aPartIndex, int aValue );

  /**
   * Changes the part value on specified amount.
   * <p>
   * This method does not throws an value out of range exception.
   * <p>
   * Argument <code>aProcessOverflow</code> determines behaviour when changed value exceeds the allowed range. If
   * ovwerflow/underflow processing is off, the part value will have it max/min value and nothing more happens. When
   * <code>aProcessOverflow</code> = <code>true</code>, previous/next parts will also change value depengin on
   * overflow/undeflow amount. <br>
   * For exmple, when increasing minutes in time value "14:45:20" at +20 minutes causes overflow of 5 minutes. With
   * overflow processing off new value will be "14:59:20", while with overflow processing on new value will be
   * "15:05:20". Overflow processing strategy depends on implementation.
   *
   * @param aPartIndex int - index of the part in the list {@link #parts()}
   * @param aDelta int - an amount to change the value
   * @param aProcessOverflow boolean - if <code>true</code> overflow and underflow will be processed
   * @throws TsIllegalArgumentRtException index is out of range
   */
  void changePartValue( int aPartIndex, int aDelta, boolean aProcessOverflow );

  /**
   * Returns the index of the part under caret position.
   * <p>
   * Caret position is defined as in {@link Text#getCaretPosition()} for the text {@link #getValueString()}.
   *
   * @param aCaretPos int - caret position
   * @return int - part index or -1 if caret position is out of range 0..{@link #getCharLength()}
   */
  int indexOfPartByCaretPos( int aCaretPos );

  /**
   * Returns the parts values.
   *
   * @return {@link IIntList} - the part values with the {@link #parts()} order
   */
  IIntList getPartsValues();

  /**
   * Returns the value change eventer.
   *
   * @return {@link IGenericChangeEventer} - the eventer
   */
  IGenericChangeEventer eventer();

}
