package org.toxsoft.core.tslib.bricks.validator.vrl;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Extends {@link IVrList} with methods to add items.
 *
 * @author hazard157
 */
public interface IVrListEdit
    extends IVrList {

  /**
   * Adds item to the end of the {@link #items()} iist.
   *
   * @param aItem {@link VrlItem} - item to add
   * @return {@link VrlItem} - the added item
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  VrlItem add( VrlItem aItem );

  /**
   * Adds all items from the argument to this list.
   *
   * @param aVrl {@link IVrList} - other list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addAll( IVrList aVrl );

  // ------------------------------------------------------------------------------------
  // inline methods for convenience
  //

  /**
   * Add non-OK validation result to the list.
   * <p>
   * If {@link ValidationResult#type() aVr.type()} = {@link EValidationResultType#OK OK}, then method does nothing and
   * returns <code>null</code>.
   *
   * @param aVr {@link ValidationResult} - the single validation result
   * @param aInfo {@link IOptionSet} - application specific additional information or <code>null</code>
   * @return {@link VrlItem} - created item or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  default VrlItem addNonOk( ValidationResult aVr, IOptionSet aInfo ) {
    TsNullArgumentRtException.checkNull( aVr );
    if( aVr.isOk() ) {
      return null;
    }
    return add( new VrlItem( aVr, aInfo ) );
  }

  /**
   * Add non-OK validation result to the list.
   * <p>
   * If {@link ValidationResult#type() aVr.type()} = {@link EValidationResultType#OK OK}, then method does nothing and
   * returns <code>null</code>.
   *
   * @param aVr {@link ValidationResult} - the single validation result
   * @return {@link VrlItem} - created item or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  default VrlItem addNonOk( ValidationResult aVr ) {
    TsNullArgumentRtException.checkNull( aVr );
    if( aVr.isOk() ) {
      return null;
    }
    return add( new VrlItem( aVr ) );
  }

  @SuppressWarnings( "javadoc" )
  default VrlItem add( ValidationResult aVr, IOptionSet aInfo ) {
    return add( new VrlItem( aVr, aInfo ) );
  }

  @SuppressWarnings( "javadoc" )
  default VrlItem add2( ValidationResult aVr, Object... aIdsAndValues ) {
    if( aIdsAndValues == null || aIdsAndValues.length == 0 ) {
      return add( aVr );
    }
    return add( new VrlItem( aVr, OptionSetUtils.createOpSet( aIdsAndValues ) ) );
  }

  @SuppressWarnings( "javadoc" )
  default VrlItem add( ValidationResult aVr ) {
    return add( new VrlItem( aVr ) );
  }

  @SuppressWarnings( "javadoc" )
  default VrlItem info( IOptionSet aInfo, String aMessageFormat, Object... aMsgArgs ) {
    return add( ValidationResult.info( aMessageFormat, aMsgArgs ), aInfo );
  }

  @SuppressWarnings( "javadoc" )
  default VrlItem info( String aMessageFormat, Object... aMsgArgs ) {
    return add( ValidationResult.info( aMessageFormat, aMsgArgs ) );
  }

  @SuppressWarnings( "javadoc" )
  default VrlItem warn( IOptionSet aInfo, String aMessageFormat, Object... aMsgArgs ) {
    return add( ValidationResult.warn( aMessageFormat, aMsgArgs ), aInfo );
  }

  @SuppressWarnings( "javadoc" )
  default VrlItem warn( String aMessageFormat, Object... aMsgArgs ) {
    return add( ValidationResult.warn( aMessageFormat, aMsgArgs ) );
  }

  @SuppressWarnings( "javadoc" )
  default VrlItem error( IOptionSet aInfo, Throwable aEx ) {
    return add( ValidationResult.error( aEx ), aInfo );
  }

  @SuppressWarnings( "javadoc" )
  default VrlItem error( Throwable aEx ) {
    return add( ValidationResult.error( aEx ) );
  }

  @SuppressWarnings( "javadoc" )
  default VrlItem error( IOptionSet aInfo, String aMessageFormat, Object... aMsgArgs ) {
    return add( ValidationResult.error( aMessageFormat, aMsgArgs ), aInfo );
  }

  @SuppressWarnings( "javadoc" )
  default VrlItem error( String aMessageFormat, Object... aMsgArgs ) {
    return add( ValidationResult.error( aMessageFormat, aMsgArgs ) );
  }

}
