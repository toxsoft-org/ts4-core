package org.toxsoft.core.tslib.bricks.validator.impl;

import java.io.*;

import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.vrl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An {@link IValResList} implementation.
 *
 * @author hazard157
 * @deprecated use {@link IVrListEdit} or {@link VrList} instead
 */
@Deprecated
public class ValResList
    implements IValResList, ITsClearable, Serializable {

  private static final long serialVersionUID = 8370364400216345421L;

  private final IListEdit<ValidationResult> results = new ElemLinkedBundleList<>();

  /**
   * Constructor.
   */
  @Deprecated
  public ValResList() {
    // nop
  }

  /**
   * Constructor with initial content.
   *
   * @param aResList {@link IList}&lt;{@link ValidationResult}&gt; - the list to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @Deprecated
  public ValResList( IList<ValidationResult> aResList ) {
    results.addAll( aResList );
  }

  // ------------------------------------------------------------------------------------
  // IValResList
  //

  @Deprecated
  @Override
  public boolean isOk() {
    for( int i = 0, count = results.size(); i < count; i++ ) {
      ValidationResult vr = results.get( i );
      if( !vr.isOk() ) {
        return false;
      }
    }
    return true;
  }

  @Deprecated
  @Override
  public boolean isWarning() {
    boolean hasWarn = false;
    for( int i = 0, count = results.size(); i < count; i++ ) {
      ValidationResult vr = results.get( i );
      if( vr.isError() ) {
        return false;
      }
      if( vr.isWarning() ) {
        hasWarn = true;
      }
    }
    return hasWarn;

  }

  @Deprecated
  @Override
  public boolean isError() {
    for( int i = 0, count = results.size(); i < count; i++ ) {
      ValidationResult vr = results.get( i );
      if( vr.isError() ) {
        return true;
      }
    }
    return false;
  }

  @Deprecated
  @Override
  public IList<ValidationResult> results() {
    return results;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Adds the validation result to the {@link #results()}.
   * <p>
   * Note: better use {@link #addNonSuccess(ValidationResult)} instead.
   *
   * @param aResult {@link ValidationResult} - the result to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @see #addNonSuccess(ValidationResult)
   */
  @Deprecated
  public void add( ValidationResult aResult ) {
    results.add( aResult );
  }

  /**
   * Add other {@link IValResList} to this one.
   *
   * @param aValResList {@link IValResList} - the list to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @Deprecated
  public void addValResList( IValResList aValResList ) {
    TsNullArgumentRtException.checkNull( aValResList );
    results.addAll( aValResList.results() );
  }

  /**
   * Add other {@link IValResList} to this one.
   *
   * @param aResList {@link IList}&lt;{@link ValidationResult}&gt; - the list to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @Deprecated
  public void addResList( IList<ValidationResult> aResList ) {
    TsNullArgumentRtException.checkNull( aResList );
    results.addAll( aResList );
  }

  /**
   * Add the validation result to {@link #results()} only if argument is not {@link ValidationResult#SUCCESS}
   * <p>
   * This method is preferrable to {@link #add(ValidationResult)} beacause avoids useless
   * {@link ValidationResult#SUCCESS} items in {@link #results()} list.
   *
   * @param aResult {@link ValidationResult} - the validation result to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @Deprecated
  public void addNonSuccess( ValidationResult aResult ) {
    TsNullArgumentRtException.checkNull( aResult );
    if( aResult != ValidationResult.SUCCESS ) {
      results.add( aResult );
    }
  }

  /**
   * Creates and adds informational result.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @Deprecated
  public void info( String aMessageFormat, Object... aMsgArgs ) {
    add( ValidationResult.info( aMessageFormat, aMsgArgs ) );
  }

  /**
   * Creates and adds warning result.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @Deprecated
  public void warn( String aMessageFormat, Object... aMsgArgs ) {
    add( ValidationResult.warn( aMessageFormat, aMsgArgs ) );
  }

  /**
   * Creates and adds error result.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @Deprecated
  public void error( String aMessageFormat, Object... aMsgArgs ) {
    add( ValidationResult.error( aMessageFormat, aMsgArgs ) );
  }

  /**
   * Creates and adds error result.
   *
   * @param aError {@link Throwable} - exception
   */
  @Deprecated
  public void error( Throwable aError ) {
    add( ValidationResult.error( aError ) );
  }

  @Deprecated
  @Override
  public ValidationResult getFirstWorst() {
    ValidationResult vrWarn = null;
    for( ValidationResult vr : results ) {
      if( vr.isError() ) {
        return vr;
      }
      if( vr.isWarning() && vrWarn != null ) {
        vrWarn = vr;
      }
    }
    if( vrWarn != null ) {
      return vrWarn;
    }
    if( !results.isEmpty() ) {
      return results.first();
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // ITSClearable
  //

  @Deprecated
  @Override
  public void clear() {
    results.clear();
  }

}
