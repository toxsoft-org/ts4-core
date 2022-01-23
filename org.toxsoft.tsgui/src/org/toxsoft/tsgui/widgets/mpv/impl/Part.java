package org.toxsoft.tsgui.widgets.mpv.impl;

import static org.toxsoft.tsgui.widgets.mpv.impl.ITsResources.*;

import org.toxsoft.tsgui.widgets.mpv.IPart;
import org.toxsoft.tslib.bricks.strio.EStrioSkipMode;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.impl.StrioUtils;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.math.IntRange;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IPart} implementation.
 *
 * @author hazard157
 */
public final class Part
    implements IPart {

  private final String    name;
  private final int       digits;
  private final int       charsCount;
  private final Character beforeChar;
  private final Character afterChar;
  private final String    fmtStrValue;
  private final String    fmtStrPart;
  private IntRange        range = IntRange.FULL;
  private int             value = 0;

  /**
   * Constructor.
   *
   * @param aName String - part name
   * @param aDigits int - the number of digits of value text
   * @param aRange {@link IntRange} - allowed range of value
   * @param aCharBefore int - char before value digits or -1 for no leader char
   * @param aCharAfter int - char after value digits or -1 for no trailing char
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aDigits < 1
   * @throws TsIllegalArgumentRtException any char is an ASCII digit
   */
  public Part( String aName, int aDigits, IntRange aRange, int aCharBefore, int aCharAfter ) {
    TsIllegalArgumentRtException.checkTrue( aDigits < 1 );
    TsNullArgumentRtException.checkNulls( aName, aRange );
    name = aName;
    digits = aDigits;
    range = aRange;
    beforeChar = aCharBefore != -1 ? Character.valueOf( (char)aCharBefore ) : null;
    afterChar = aCharAfter != -1 ? Character.valueOf( (char)aCharAfter ) : null;
    int cc = digits;
    if( beforeChar != null ) {
      ++cc;
      TsIllegalArgumentRtException.checkTrue( StrioUtils.isAsciiChar( beforeChar.charValue() ) );
    }
    if( afterChar != null ) {
      ++cc;
      TsIllegalArgumentRtException.checkTrue( StrioUtils.isAsciiChar( afterChar.charValue() ) );
    }
    charsCount = cc;
    Integer digitsInt = Integer.valueOf( digits );
    fmtStrValue = String.format( "%%0%dd", digitsInt ); //$NON-NLS-1$
    if( afterChar != null || beforeChar != null ) {
      if( afterChar == null ) {
        fmtStrPart = String.format( "%c%%0%dd", beforeChar, digitsInt ); //$NON-NLS-1$
      }
      else {
        if( beforeChar == null ) {
          fmtStrPart = String.format( "%%0%dd%c", digitsInt, afterChar ); //$NON-NLS-1$
        }
        else {
          fmtStrPart = String.format( "%c%%0%dd%c", beforeChar, digitsInt, afterChar ); //$NON-NLS-1$
        }
      }
    }
    else {
      fmtStrPart = fmtStrValue;
    }
    value = range.minValue();
  }

  // ------------------------------------------------------------------------------------
  // IPart
  //

  @Override
  public String name() {
    return name;
  }

  @Override
  public int digitsCount() {
    return digits;
  }

  @Override
  public int charsCount() {
    return charsCount;
  }

  @Override
  public IntRange range() {
    return range;
  }

  @Override
  public int value() {
    return value;
  }

  @Override
  public String valueString() {
    return String.format( fmtStrValue, Integer.valueOf( value ) );
  }

  @Override
  public String partString() {
    return String.format( fmtStrPart, Integer.valueOf( value ) );
  }

  @Override
  public Character charBefore() {
    return beforeChar;
  }

  @Override
  public Character charAfter() {
    return afterChar;
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  /**
   * Simply sets the {@link #value()}.
   *
   * @param aValue int - new value
   */
  void setValue( int aValue ) {
    value = range.checkInRange( aValue );
  }

  @SuppressWarnings( "boxing" )
  ValidationResult canReadValue( IStrioReader aSr ) {
    char ch;
    // beforeChar - is mandatory if not null
    if( beforeChar != null ) {
      ch = aSr.nextChar( EStrioSkipMode.SKIP_NONE );
      if( ch != beforeChar.charValue() ) {
        return ValidationResult.error( FMT_ERR_READ_PART_BEFORE_CHAR, name, beforeChar );
      }
    }
    // at least one digit must present
    ch = aSr.nextChar( EStrioSkipMode.SKIP_NONE );
    if( !StrioUtils.isAsciiDigit( ch ) ) {
      return ValidationResult.error( FMT_ERR_READ_PART_VALUE, name, digits );
    }
    // up to digitsCount() ASCII digits are optional
    for( int i = 1; i < digitsCount(); i++ ) {
      ch = aSr.peekChar( EStrioSkipMode.SKIP_NONE );
      if( !StrioUtils.isAsciiDigit( ch ) ) {
        break;
      }
      ch = aSr.nextChar( EStrioSkipMode.SKIP_NONE );
    }
    // afterChar - is mandatory if not null
    if( afterChar != null ) {
      ch = aSr.nextChar( EStrioSkipMode.SKIP_NONE );
      if( ch != afterChar.charValue() ) {
        return ValidationResult.error( FMT_ERR_READ_PART_AFTER_CHAR, name, afterChar );
      }
    }
    return ValidationResult.SUCCESS;
  }

  // ALWAYS call after input is checked by canReadValue()!
  int readValue( IStrioReader aSr ) {
    if( beforeChar != null ) {
      aSr.nextChar( EStrioSkipMode.SKIP_NONE );
    }
    int v = aSr.readInt();
    if( afterChar != null ) {
      aSr.nextChar( EStrioSkipMode.SKIP_NONE );
    }
    return v;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return name + '=' + value;
  }

}
