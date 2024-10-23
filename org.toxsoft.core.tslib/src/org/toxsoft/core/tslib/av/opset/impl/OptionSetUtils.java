package org.toxsoft.core.tslib.av.opset.impl;

import static org.toxsoft.core.tslib.av.opset.impl.ITsResources.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.bricks.validator.vrl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Utility methods and static constructors.
 *
 * @author hazard157
 */
public class OptionSetUtils {

  /**
   * Creates an editable option set from id / value pairs array.
   * <p>
   * Argument must be the array of even number of elements. For each entry in option set first element is {@link String}
   * or {@link IStridable} identifier. Second element must be an {@link IAtomicValue} or object converable to the atomic
   * value via {@link AvUtils#avFromObj(Object)}.
   *
   * @param aIdsAndValues Object[] - identifier / value pairs
   * @return {@link IOptionSet} - new instance of an editable option set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static IOptionSetEdit createOpSet( Object... aIdsAndValues ) {
    return internalCreateAdded( new OptionSet(), aIdsAndValues );
  }

  /**
   * Creates an editable option set from existing opset extended with optional id / value pairs array.
   * <p>
   * Values from <code>aIdsAndValues</code> override initial values specified in <code>aOps</code>.
   *
   * @param aOps {@link IOptionSet} - initial values for newly created option set
   * @param aIdsAndValues Object[] - identifier / value pairs as for {@link #createOpSet(Object...)}
   * @return {@link IOptionSet} - new instance of an editable option set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static final IOptionSetEdit createOpSetEx( IOptionSet aOps, Object... aIdsAndValues ) {
    IOptionSetEdit ops = new OptionSet();
    ops.addAll( aOps );
    return internalCreateAdded( ops, aIdsAndValues );
  }

  /**
   * Creates an editable option set from existing map.
   *
   * @param aOps {@link IMap}&lt;String,{@link IAtomicValue}&gt; - the values of the set
   * @return {@link IOptionSet} - new instance of an editable option set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any key in argument map is not an IDpath
   */
  public static final IOptionSetEdit createFromMap( IMap<String, ? extends IAtomicValue> aOps ) {
    TsNullArgumentRtException.checkNull( aOps );
    IOptionSetEdit ops = new OptionSet();
    for( String s : aOps.keys() ) {
      StridUtils.checkValidIdPath( s );
      ops.setValue( s, aOps.getByKey( s ) );
    }
    return ops;
  }

  /**
   * Makes the human readable multi-line string with option set values.
   *
   * @param aOpSet {@link IOptionSet} - option set to be shown
   * @return String - human readable representation
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  @SuppressWarnings( "nls" )
  public static String humanReadable( IOptionSet aOpSet ) {
    TsNullArgumentRtException.checkNull( aOpSet );
    StringBuilder sb = new StringBuilder();
    sb.append( "{" );
    IStringList keys = aOpSet.keys();
    if( !keys.isEmpty() ) {
      TsTestUtils.nl();
      for( int i = 0, n = keys.size(); i < n; i++ ) {
        String s = keys.get( i );
        IAtomicValue val = aOpSet.getValue( s );
        String line;
        if( val.atomicType() == EAtomicType.STRING ) {
          line = String.format( "  %s = \"%s\"\n", s, val.asString() );
        }
        else {
          line = String.format( "  %s = %s\n", s, val.asString() );
        }
        sb.append( line );
      }
    }
    sb.append( "}" );
    return sb.toString();
  }

  /**
   * Outputs {@link #humanReadable(IOptionSet)} using {@link TsTestUtils}.
   *
   * @param aOpSet {@link IOptionSet} - option set to be shown
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static void printOptionSet( IOptionSet aOpSet ) {
    TsNullArgumentRtException.checkNull( aOpSet );
    TsTestUtils.pl( " = " + humanReadable( aOpSet ) ); //$NON-NLS-1$
  }

  /**
   * Check option set against list of option definitions.
   * <p>
   * The checks are performed as in {@link #validateOptionSet(IOptionSet, IStridablesList, IVrListEdit)}.
   *
   * @param aOpValues {@link IOptionSet} - option values
   * @param aOpDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - options definitions
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ValidationResult validateOptionSet( IOptionSet aOpValues, IStridablesList<IDataDef> aOpDefs ) {
    TsNullArgumentRtException.checkNulls( aOpValues, aOpDefs );
    return validateOptionSet( aOpValues, aOpDefs, null ).getFirstWorst().vr();
  }

  /**
   * Check option set against list of option definitions and fills <code>aVrl</code> with results.
   * <p>
   * The check includes the following steps:
   * <ul>
   * <li>check all mandatory {@link IDataDef#isMandatory()} options from <code>aOpDefs</code> has corresponding values
   * in the option set <code>aOpValues</code>;</li>
   * <li>check that all values for options from <code>aOpDefs</code> have type compatible to the definition;</li>
   * <li>calls each data definition {@link IDataDef#validator()} for individual values check.</li>
   * </ul>
   * Options in <code>aOpValues</code> without corresponding definitions in <code>aOpDefs</code> are not checked.
   *
   * @param aOpValues {@link IOptionSet} - option values
   * @param aOpDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - options definitions
   * @param aVrl {@link IVrListEdit} - editable results list or <code>null</code> to create new instance
   * @return {@link IVrListEdit} - either non-<code>null</code> argument <code>aVrl</code> or new instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IVrListEdit validateOptionSet( IOptionSet aOpValues, IStridablesList<IDataDef> aOpDefs,
      IVrListEdit aVrl ) {
    TsNullArgumentRtException.checkNulls( aOpValues, aOpDefs );
    IVrListEdit vrl = aVrl != null ? aVrl : new VrList();
    // first check mandatory option present
    for( IDataDef dd : aOpDefs ) {
      if( dd.isMandatory() && !aOpValues.hasKey( dd.id() ) ) {
        vrl.error( FMT_ERR_NO_MANDATORY_OP, dd.id(), dd.nmName() );
      }
    }
    // check option values against defined types and with individual validators
    for( IDataDef dd : aOpDefs ) {
      if( aOpValues.hasKey( dd.id() ) ) {
        IAtomicValue opVal = aOpValues.getValue( dd.id() );
        if( !AvTypeCastRtException.canAssign( dd.atomicType(), opVal.atomicType() ) ) {
          vrl.error( FMT_ERR_OP_TYPE_MISMATCH, dd.id(), dd.atomicType().id(), opVal.atomicType().id() );
        }
        vrl.addNonOk( dd.validator().validate( opVal ) );
      }
    }
    return vrl;
  }

  /**
   * Check option set against list of option definitions and throws an exception on error.
   * <p>
   * The method {@link #validateOptionSet(IOptionSet, IStridablesList)} is used for checking.
   *
   * @param aOpValues {@link IOptionSet} - option values
   * @param aOpDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - option definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException on error
   */
  public static void checkOptionSet( IOptionSet aOpValues, IStridablesList<IDataDef> aOpDefs ) {
    TsValidationFailedRtException.checkError( validateOptionSet( aOpValues, aOpDefs ) );
  }

  /**
   * Initializes the option set from the options definitions.
   * <p>
   * After this method <code>aOpValues</code> will contain exactly the same keys as {@link IStridablesList#keys()
   * aOpDefs.keys()}. The values will be corresponding definition's {@link IDataDef#defaultValue() defaultValue()}.
   *
   * @param aOpValues {@link IOptionSetEdit} - option set to initialize
   * @param aOpDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - option definitions
   * @return {@link IOptionSetEdit} - the argument <code>aOpValues</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IOptionSetEdit initOptionSet( IOptionSetEdit aOpValues, IStridablesList<IDataDef> aOpDefs ) {
    TsNullArgumentRtException.checkNulls( aOpValues, aOpDefs );
    aOpValues.clear();
    for( String opId : aOpDefs.keys() ) {
      aOpValues.setValue( opId, aOpDefs.getByKey( opId ).defaultValue() );
    }
    return aOpValues;
  }

  /**
   * Formats output string with the arguments from an option set.
   * <p>
   * Method requires a <i>format string</i> <code>aFmtStr</code> and a <i>named argument values</i> <code>aArgs</code>.
   * The format string is a {@link String} which may contain <i>fixed text</i> and one or more embedded <i>format
   * specifiers</i>.
   * <p>
   * The format specified has a general full form <code><b>{argument.id,avFmtStr}</b></code> or reduced form
   * <code><b>{argument.id}</b></code> where curly braces <code><b>'{' '}'</b></code> and comma
   * <code><b>','</b></code>are a literal characters. <code><b>argument.id</b></code> is an IDpath used as option
   * identifier in <code>aArgs</code>. <code><b>avFmtStr</b></code> is a single atomic value format string without
   * starting percent '%' character. Single atomic value formatting is performed by the method
   * {@link AvUtils#printAv(String, IAtomicValue)}.
   * <p>
   * To use curly bracket characters '{' or '}' as a fixed text, bracket character should be preceded with escape
   * character '\' (reverse slash, reverse solidus).
   * <p>
   * Examples:<br>
   * "<code>Count = <b>{cnt}</b></code>" formats as "<code>Count = 157</code>".
   * <p>
   * "<code>Voltage: raw= <b>{voltage}</b>, pretty= <b>{voltage,0.2f}</b></code>" formats as
   * "<code>Voltage: raw= 234.76294712, pretty= 234.76</code>".
   * <p>
   * "<code>The person is \{ '<b>{firstName}</b>', '<b>{lastName)</b>' \}</code>" formats as
   * "<code>The person is { 'John', 'Smith' }</code>".
   * <p>
   * If <code>aDefs</code> contains definition for the option specified in the <i>reduced form</i>, than
   * {@link IDataDef#formatString()} will be used for value formatting.
   * <p>
   * Note: both {@link IStridablesList}&lt;{@link IDataDef}&gt; and {@link IStringMap}&lt;{@link IDataType}&gt; may be
   * supplied as <code>aDefs</code> argument.
   *
   * @param aFmtStr String - the format string
   * @param aArgs {@link IOptionSet} - argument options, may be <code>null</code>
   * @param aDefs {@link IMap}&lt;String,{@link IDataType}&gt; - optional argument options definitions
   * @return String - formatted string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid format string
   * @throws TsItemNotFoundRtException argument ID mentioned in <code>aFmtStr</code> not found in <code>aArgs</code>
   * @throws TsIllegalArgumentRtException value type from <code>aArgs</code> does not matches format specifier type
   */
  public static String format( String aFmtStr, IOptionSet aArgs, IMap<String, IDataType> aDefs ) {
    TsNullArgumentRtException.checkNulls( aFmtStr, aDefs );
    IOptionSet args = aArgs != null ? aArgs : IOptionSet.NULL;
    IStrioReader sr = new StrioReader( new CharInputStreamString( aFmtStr ) );
    sr.setSkipMode( EStrioSkipMode.SKIP_NONE );
    StringBuilder sb = new StringBuilder();
    char ch;
    while( (ch = sr.nextChar()) != CHAR_EOF ) {
      // process escaped formatted argument starter char
      if( ch == CHAR_ESCAPE && sr.peekChar() == CH_FMT_BEGIN ) {
        ch = sr.nextChar();
      }
      else {
        if( ch == CH_FMT_BEGIN ) {
          // read argument option ID and get it's value
          String opId = sr.readIdPath();
          IAtomicValue av = args.getValue( opId );
          // expect either formatter end or format string, any other input is an error
          ch = sr.nextChar();
          TsIllegalArgumentRtException.checkTrue( ch != CH_FMT_SEPARATOR && ch != CH_FMT_END );
          String fmtStr = null;
          // read value format string if specified
          if( ch == CH_FMT_SEPARATOR ) {
            StringBuilder sbValFmtStr = new StringBuilder();
            while( (ch = sr.nextChar()) != CH_FMT_END ) {
              TsIllegalArgumentRtException.checkTrue( ch == CHAR_EOF );
              if( ch == CHAR_ESCAPE && sr.peekChar() == CH_FMT_END ) { // use escaped ender as common char
                ch = sr.nextChar();
              }
              sbValFmtStr.append( ch );
            }
            String s = sbValFmtStr.toString();
            if( !s.isBlank() ) {
              fmtStr = "%" + s; //$NON-NLS-1$
            }
          }
          // if not specified, try to use value format string from option definition
          else {
            IDataType dd = aDefs.findByKey( opId );
            if( dd != null ) {
              fmtStr = dd.formatString();
            }
          }
          String formattedValue = AvUtils.printAv( fmtStr, av );
          sb.append( formattedValue );
          continue;
        }
      }
      sb.append( ch );
    }
    return sb.toString();
  }

  /**
   * Formats output string with the arguments from an option set.
   * <p>
   * The same formatting rules as for {@link #format(String, IOptionSet, IMap)} applies.
   *
   * @param aFmtStr String - the format string
   * @param aArgs {@link IOptionSet} - argument options, may be <code>null</code>
   * @return String - formatted string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid format string
   * @throws TsItemNotFoundRtException no argument with ID mentioned in <code>aFmtStr</code>
   * @throws TsIllegalArgumentRtException actual value does not matches expected type
   */
  public static String format( String aFmtStr, IOptionSet aArgs ) {
    return format( aFmtStr, aArgs, IStridablesList.EMPTY );
  }

  // ------------------------------------------------------------------------------------
  // internal staff
  //

  private static final char CH_FMT_BEGIN     = '{';
  private static final char CH_FMT_SEPARATOR = ',';
  private static final char CH_FMT_END       = '}';

  private static final IOptionSetEdit internalCreateAdded( IOptionSetEdit aOps, Object... aIdsAndValues ) {
    TsErrorUtils.checkArrayArg( aIdsAndValues );
    TsIllegalArgumentRtException.checkTrue( aIdsAndValues.length % 2 != 0 ); // must be even number of elements
    for( int i = 0; i < aIdsAndValues.length; i += 2 ) {
      Object nameObj = aIdsAndValues[i];
      String name;
      if( nameObj instanceof IStridable ) {
        name = ((IStridable)nameObj).id();
      }
      else {
        if( nameObj instanceof String ) {
          name = (String)nameObj;
        }
        else {
          throw new ClassCastException(
              String.format( FMT_ERR_INV_OPSET_CREATION_NAME_VARARG, Integer.valueOf( i + 1 ) ) );
        }
      }
      IAtomicValue av = AvUtils.avFromObj( aIdsAndValues[i + 1] );
      if( av == null ) {
        throw new ClassCastException(
            String.format( FMT_ERR_INV_OPSET_CREATION_VALUE_VARARG, Integer.valueOf( i + 2 ) ) );
      }
      aOps.setValue( name, av );
    }
    return aOps;
  }

  /**
   * Prohibition of descendants creation.
   */
  private OptionSetUtils() {
    // nop
  }

}
