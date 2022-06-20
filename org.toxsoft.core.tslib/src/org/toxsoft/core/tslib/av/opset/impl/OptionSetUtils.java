package org.toxsoft.core.tslib.av.opset.impl;

import static org.toxsoft.core.tslib.av.opset.impl.ITsResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
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
   * Argumnet must be the array of even number of elements. For each entry in option set first element is {@link String}
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
   * Creates an editable option set from exsiting opset extended with optional id / value pairs array.
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

  // ------------------------------------------------------------------------------------
  // internal staff
  //

  private static final IOptionSetEdit internalCreateAdded( IOptionSetEdit aOps, Object... aIdsAndValues ) {
    TsErrorUtils.checkArrayArg( aIdsAndValues );
    TsIllegalArgumentRtException.checkTrue( aIdsAndValues.length % 2 != 0 ); // нечетное количество аргументов?
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
