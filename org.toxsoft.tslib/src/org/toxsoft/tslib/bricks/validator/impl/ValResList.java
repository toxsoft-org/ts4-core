package org.toxsoft.tslib.bricks.validator.impl;

import java.io.Serializable;

import org.toxsoft.tslib.bricks.validator.IValResList;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.basis.ITsClearable;
import org.toxsoft.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * An {@link IValResList} implementation.
 *
 * @author hazard157
 */
public class ValResList
    implements IValResList, ITsClearable, Serializable {

  private static final long serialVersionUID = 8370364400216345421L;

  private final IListEdit<ValidationResult> results = new ElemLinkedBundleList<>();

  /**
   * Constructor.
   */
  public ValResList() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IValResList
  //

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

  @Override
  public boolean isError() {
    for( int i = 0, count = results.size(); i < count; i++ ) {
      ValidationResult vr = results.get( i );
      if( !vr.isError() ) {
        return true;
      }
    }
    return false;
  }

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
  public void add( ValidationResult aResult ) {
    results.add( aResult );
  }

  /**
   * Add other {@link IValResList} to this one.
   *
   * @param aValResList {@link IValResList} - the list to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void addValResList( IValResList aValResList ) {
    TsNullArgumentRtException.checkNull( aValResList );
    results.addAll( aValResList.results() );
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
  public void addNonSuccess( ValidationResult aResult ) {
    TsNullArgumentRtException.checkNull( aResult );
    if( aResult != ValidationResult.SUCCESS ) {
      results.add( aResult );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITSClearable
  //

  @Override
  public void clear() {
    results.clear();
  }

}
