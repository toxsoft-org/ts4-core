package org.toxsoft.core.tslib.utils.progargs;

import static org.toxsoft.core.tslib.utils.progargs.ITsResources.*;

import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Class parses and encapsulates program simple command line arguments.
 * <p>
 * Simple command line must contains pairs of argument names followed with optional value: <br>
 * <code><b>progarm.exe -arg1name arg1val -arg2name arg2val -arg3name -arg4name -arg5name arg5val</code></b> <br>
 * Note that arguments 3 and 4 has no values. Argument name is prepended with dash '-' character.
 * <p>
 * Command line arguments are represented as the map {@link #argValues()}. Keys are argument names (without dash). This
 * class interprets absent value as an empty string.
 * <p>
 * Please note that it is impossible to distinguish between absent value and empty string ("") specified as value in
 * command line.
 *
 * @author hazard157
 */
public class ProgramArgs {

  private static final String ARG_NAME_PREFIX = "-"; //$NON-NLS-1$

  final IStringMapEdit<String> argVals = new StringMap<>();

  /**
   * Constructor parses the command line.
   *
   * @param aArgs String[] - command line arguments
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid command line
   */
  public ProgramArgs( String[] aArgs ) {
    // the map "argument name (without dash)" - "argument value"
    for( int i = 0, n = aArgs.length; i < n; ) {
      String argName = aArgs[i++];
      if( !argName.startsWith( ARG_NAME_PREFIX ) ) {
        throw new TsIllegalArgumentRtException( MSG_ERR_INVALID_COMMAND_LINE );
      }
      String argValue = TsLibUtils.EMPTY_STRING;
      if( i < aArgs.length ) {
        String s = aArgs[i];
        if( !s.startsWith( ARG_NAME_PREFIX ) ) {
          ++i;
          argValue = s;
        }
      }
      argVals.put( argName.substring( 1 ), argValue );
    }
  }

  /**
   * Returns the command line arguments.
   * <p>
   * Absent values are represented as an empty string.
   *
   * @return {@link IStringMap}&lt;String&gt; - the map "arg name (without dash)" - "arg value"
   */
  public IStringMap<String> argValues() {
    return argVals;
  }

  /**
   * Determines if argument value was specified in command line.
   * <p>
   * Note: it is impossible to distinguish between absent value and an empty string ("") specified as value in command
   * line.
   *
   * @param aArgName String - argument name (without dash)
   * @return boolean - <code>true</code> if argument value was specified in command line
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public boolean hasArg( String aArgName ) {
    return argVals.hasKey( aArgName );
  }

  /**
   * Returns the argument value or if no such argument was specified returns an empty string.
   *
   * @param aArgName String - command line argument
   * @return String - argument value or an empty string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public String getArgValue( String aArgName ) {
    String s = argVals.findByKey( aArgName );
    if( s != null ) {
      return s;
    }
    return TsLibUtils.EMPTY_STRING;
  }

  /**
   * Returns the argument value or if no such argument was specified returns ste specified string.
   *
   * @param aArgName String - command line argument
   * @param aDefaultValue String - value in absence of the argument, may be <code>null</code>
   * @return String - argument value or aDefaultValue
   * @throws TsNullArgumentRtException aArgName = null
   */
  public String getArgValue( String aArgName, String aDefaultValue ) {
    String s = argVals.findByKey( aArgName );
    if( s != null ) {
      return s;
    }
    return aDefaultValue;
  }

}
