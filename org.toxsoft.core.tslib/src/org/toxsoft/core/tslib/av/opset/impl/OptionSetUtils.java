package org.toxsoft.core.tslib.av.opset.impl;

import static org.toxsoft.core.tslib.av.opset.impl.ITsResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
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
   * The check includes the following steps:
   * <ul>
   * <li>check all mandatory {@link IDataDef#isMandatory()} options from <code>aOpDefs</code> has corresponding values
   * in the option set <code>aOpValues</code>;</li>
   * <li>check that all values for options from <code>aOpDefs</code> have type compatible to the definition.</li>
   * </ul>
   * Options in <code>aOpValues</code> without corresponding definitions in <code>aOpDefs</code> are not checked.
   *
   * @param aOpValues {@link IOptionSet} - option values
   * @param aOpDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - optiondefinitions
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ValidationResult validateOptionSet( IOptionSet aOpValues, IStridablesList<IDataDef> aOpDefs ) {
    TsNullArgumentRtException.checkNulls( aOpValues, aOpDefs );
    // check mandatory option present
    for( IDataDef dd : aOpDefs ) {
      if( dd.isMandatory() && !aOpValues.hasKey( dd.id() ) ) {
        return ValidationResult.error( FMT_ERR_NO_MANDATORY_OP, dd.id(), dd.nmName() );
      }
    }
    // check option values against defined types
    for( IDataDef dd : aOpDefs ) {
      if( aOpValues.hasKey( dd.id() ) ) {
        IAtomicValue opVal = aOpValues.getValue( dd.id() );
        if( !AvTypeCastRtException.canAssign( dd.atomicType(), opVal.atomicType() ) ) {
          return ValidationResult.error( FMT_ERR_OP_TYPE_MISMATCH, dd.id(), dd.atomicType().id(),
              opVal.atomicType().id() );
        }
      }
    }
    return ValidationResult.SUCCESS;
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

  // ------------------------------------------------------------------------------------
  // internal staff
  //

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
