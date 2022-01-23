package org.toxsoft.tslib.bricks.validator.impl;

import org.toxsoft.tslib.bricks.validator.*;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.utils.errors.TsInternalErrorRtException;

/**
 * Default implementation of {@link ITsCompoundValidator}.
 *
 * @author hazard157
 * @param <V> - checked entity (value) class
 */
public class TsCompoundValidator<V>
    extends AbstractCompoundTsValidator<V> {

  static class FirstWarningFirstErrorValidator<V>
      extends AbstractCompoundTsValidator<V> {

    @Override
    public ValidationResult validate( V aValue ) {
      ValidationResult result = ValidationResult.SUCCESS;
      IList<ITsValidator<V>> ll = enabledValidators();
      for( int i = 0, n = ll.size(); i < n; i++ ) {
        ITsValidator<V> v = ll.get( i );
        ValidationResult r = v.validate( aValue );
        if( r.isError() ) {
          return r;
        }
        if( r.isWarning() && r == ValidationResult.SUCCESS ) {
          result = r;
        }
      }
      return result;
    }

  }

  static class FirstWarningLastErrorValidator<V>
      extends AbstractCompoundTsValidator<V> {

    @Override
    public ValidationResult validate( V aValue ) {
      ValidationResult result = ValidationResult.SUCCESS;
      IList<ITsValidator<V>> ll = enabledValidators();
      for( int i = 0, n = ll.size(); i < n; i++ ) {
        ITsValidator<V> v = ll.get( i );
        ValidationResult r = v.validate( aValue );
        if( r.isError() ) {
          result = r;
        }
        else {
          if( r.isWarning() && r == ValidationResult.SUCCESS ) {
            result = r;
          }
        }
      }
      return result;
    }

  }

  static class LastWarningFirstErrorValidator<V>
      extends AbstractCompoundTsValidator<V> {

    @Override
    public ValidationResult validate( V aValue ) {
      ValidationResult result = ValidationResult.SUCCESS;
      IList<ITsValidator<V>> ll = enabledValidators();
      for( int i = 0, n = ll.size(); i < n; i++ ) {
        ITsValidator<V> v = ll.get( i );
        ValidationResult r = v.validate( aValue );
        if( r.isError() ) {
          return r;
        }
        if( r.isWarning() ) {
          result = r;
        }
      }
      return result;
    }

  }

  static class LastWarningLastErrorValidator<V>
      extends AbstractCompoundTsValidator<V> {

    @Override
    public ValidationResult validate( V aValue ) {
      ValidationResult result = ValidationResult.SUCCESS;
      IList<ITsValidator<V>> ll = enabledValidators();
      for( int i = 0, n = ll.size(); i < n; i++ ) {
        ITsValidator<V> v = ll.get( i );
        ValidationResult r = v.validate( aValue );
        if( r.isError() ) {
          result = r;
        }
        else {
          if( r.isWarning() && r == ValidationResult.SUCCESS ) {
            result = r;
          }
        }
      }
      return result;
    }

  }

  private TsCompoundValidator() {
    // nop
  }

  /**
   * Creates validator with specified warning / error returning strategy.
   *
   * @param <T> - checked entity (value) class
   * @param aFirstWarning boolean - a sign to return first warning rather the last one<br>
   *          <b>true</b> - returns first warning encountered during validation;<br>
   *          <b>false</b> - returns last warning encountered during validation.
   * @param aFirstError boolean - - a sign to return first error rather the last one<br>
   *          <b>true</b> - returns first error encountered during validation;<br>
   *          <b>false</b> - returns last error encountered during validation.
   * @return {@link ITsCompoundValidator} - created validator
   */
  public static <T> ITsCompoundValidator<T> create( boolean aFirstWarning, boolean aFirstError ) {
    if( aFirstWarning ) {
      if( aFirstError ) {
        return new FirstWarningFirstErrorValidator<>();
      }
      return new FirstWarningLastErrorValidator<>();
    }
    if( aFirstError ) {
      return new LastWarningFirstErrorValidator<>();
    }
    return new LastWarningLastErrorValidator<>();
  }

  @Override
  public ValidationResult validate( V aValue ) {
    // this method is never called - instance of this class will never be created
    throw new TsInternalErrorRtException();
  }

}
