package org.toxsoft.core.tslib.bricks.strio.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.util.Calendar;

import org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants;
import org.toxsoft.core.tslib.bricks.strio.IStrioStreamBase;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Base class for strio readr/writer implementations.
 *
 * @author hazard157
 */
public class StrioStreamBase
    implements IStrioStreamBase {

  private String charsSpace     = DEFAULT_SPACE_CHARS;
  private String charsBypassed  = DEFAULT_BYPASSED_CHARS;
  private String charsDelimiter = DEFAULT_DELIMITER_CHARS;

  /**
   * Calendar to convert <code>long</code> timestamps to strings and vice versa.
   */
  private Calendar calendar = null; // lazy initializaion in

  /**
   * Constructor for descendants.
   */
  protected StrioStreamBase() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Methods for descendants
  //

  protected Calendar calendar() {
    if( calendar == null ) {
      calendar = Calendar.getInstance();
    }
    return calendar;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns <code>true</code> if argument is space chanacter.
   * <p>
   * Returns <code>false</code> for {@link IStrioHardConstants#CHAR_EOF}.
   *
   * @param aCh char - character to be checked
   * @return <b>true</b> - this is the space character (one of the {@link #getSpaceChars()};<br>
   *         <b>false</b> - not a space character
   * @see #getSpaceChars()
   * @see IStrioHardConstants#DEFAULT_SPACE_CHARS
   */
  @Override
  public boolean isSpaceChar( char aCh ) {
    if( aCh == CHAR_EOF ) {
      return false;
    }
    return charsSpace.indexOf( aCh ) >= 0;
  }

  /**
   * Returns <code>true</code> if argument is bypassed chanacter.
   * <p>
   * Returns <code>false</code> for {@link IStrioHardConstants#CHAR_EOF}.
   *
   * @param aCh char - character to be checked
   * @return <b>true</b> - this is the bypassed character (one of the {@link #getBypassedChars()};<br>
   *         <b>false</b> - not a bypassed character
   * @see #getSpaceChars()
   * @see IStrioHardConstants#DEFAULT_BYPASSED_CHARS
   */
  @Override
  public boolean isBypassedChar( char aCh ) {
    if( aCh == CHAR_EOF ) {
      return false;
    }
    return charsBypassed.indexOf( aCh ) >= 0;
  }

  /**
   * Returns <code>true</code> if argument is delimiter chanacter.
   * <p>
   * Returns <code>false</code> for {@link IStrioHardConstants#CHAR_EOF}.
   *
   * @param aCh char - character to be checked
   * @return <b>true</b> - this is the delimiter character (one of the {@link #getDelimiterChars()};<br>
   *         <b>false</b> - not a delimiter character
   * @see #getSpaceChars()
   * @see IStrioHardConstants#DEFAULT_DELIMITER_CHARS
   */
  @Override
  public boolean isDelimiterChar( char aCh ) {
    if( aCh == CHAR_EOF ) {
      return false;
    }
    return charsDelimiter.indexOf( aCh ) >= 0;
  }

  /**
   * Returns the space characters as string.
   * <p>
   * Initally returns {@link IStrioHardConstants#DEFAULT_SPACE_CHARS}.
   *
   * @return String - the space characters as single string
   */
  @Override
  public String getSpaceChars() {
    return charsSpace;
  }

  /**
   * Returns the bypassed characters as string.
   * <p>
   * Initally returns {@link IStrioHardConstants#DEFAULT_BYPASSED_CHARS}.
   *
   * @return String - the bypassed characters as single string
   */
  @Override
  public String getBypassedChars() {
    return charsBypassed;
  }

  /**
   * Returns the delimiter characters as string.
   * <p>
   * Initally returns {@link IStrioHardConstants#DEFAULT_DELIMITER_CHARS}.
   *
   * @return String - the delimiter characters as single string
   */
  @Override
  public String getDelimiterChars() {
    return charsDelimiter;
  }

  /**
   * Changes the space characters.
   *
   * @see #getSpaceChars()
   * @see #DEFAULT_SPACE_CHARS
   * @param aSpaceChars String - the string consisting of the space characters
   * @return String - previuos string of the space characters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the argument is the empty string
   */
  @Override
  public String setSpaceChars( String aSpaceChars ) {
    TsNullArgumentRtException.checkNull( aSpaceChars );
    TsIllegalArgumentRtException.checkTrue( aSpaceChars.length() == 0 );
    String s = charsSpace;
    charsSpace = aSpaceChars;
    return s;
  }

  /**
   * Changes the space characters.
   *
   * @see #getBypassedChars()
   * @see IStrioHardConstants#DEFAULT_BYPASSED_CHARS
   * @param aBypassedChars String - the string consisting of the bypassed characters
   * @return String - previuos string of the bypassed characters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the argument is the empty string
   */
  @Override
  public String setBypassedChars( String aBypassedChars ) {
    TsNullArgumentRtException.checkNull( aBypassedChars );
    TsIllegalArgumentRtException.checkTrue( aBypassedChars.length() == 0 );
    String s = charsBypassed;
    charsBypassed = aBypassedChars;
    return s;
  }

  /**
   * Changes the delimiter characters.
   *
   * @see #getBypassedChars()
   * @see IStrioHardConstants#DEFAULT_DELIMITER_CHARS
   * @param aDelimiterChars String - the string consisting of the delimiter characters
   * @return String - previuos string of the delimiter characters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the argument is the empty string
   */
  @Override
  public String setDelimiterChars( String aDelimiterChars ) {
    TsNullArgumentRtException.checkNull( aDelimiterChars );
    TsIllegalArgumentRtException.checkTrue( aDelimiterChars.length() == 0 );
    String s = charsDelimiter;
    charsDelimiter = aDelimiterChars;
    return s;
  }

}
