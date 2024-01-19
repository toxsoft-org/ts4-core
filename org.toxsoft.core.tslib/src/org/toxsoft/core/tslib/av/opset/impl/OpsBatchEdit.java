package org.toxsoft.core.tslib.av.opset.impl;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IOpsBatchEdit} implementation wraps over {@link IOptionSetEdit} with editing validation support.
 * <p>
 * All editing will be applied to wrapped option set by the method {@link IOptionSetEdit#setAll(IOptionSet)}.
 * <p>
 * All methods of interface {@link IOpsBatchEdit} additionally may throw {@link TsValidationFailedRtException} error if
 * {@link #doValidateSetParams(IOptionSet, IOptionSet)} validation fails.
 * <p>
 * Note: interceptor is called before the validator. Еherefore I
 *
 * @author hazard157
 */
public class OpsBatchEdit
    implements IOpsBatchEdit {

  private final IOptionSetEdit opset;

  private IOptionsBatchEditInterceptor changeInterceptor = IOptionsBatchEditInterceptor.NONE;

  /**
   * Constructor.
   *
   * @param aOpset {@link IOptionSetEdit} - wrapped options set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public OpsBatchEdit( IOptionSetEdit aOpset ) {
    TsNullArgumentRtException.checkNull( aOpset );
    opset = aOpset;
  }

  // ------------------------------------------------------------------------------------
  // OpsBatchEdit
  //

  @Override
  public void addAll( IOptionSet aOps ) {
    IOptionSetEdit valuesToSet = new OptionSet( aOps );
    if( changeInterceptor.interceptPropsChange( opset, valuesToSet ) ) {
      TsValidationFailedRtException.checkError( doValidateSetParams( valuesToSet, opset ) );
      opset.addAll( valuesToSet );
    }
  }

  @Override
  public boolean extendSet( IOptionSet aOps ) {
    IOptionSetEdit valuesToSet = null; // #valuesToSet will contain options not existing in #opset
    for( String opId : aOps.keys() ) {
      if( !opset.hasKey( opId ) ) {
        if( valuesToSet == null ) { // lazy initialization of #valuesToSet
          valuesToSet = new OptionSet();
        }
        valuesToSet.setValue( opId, aOps.getByKey( opId ) );
      }
    }
    if( valuesToSet == null ) { // nothing to do
      return false;
    }
    if( changeInterceptor.interceptPropsChange( opset, valuesToSet ) ) {
      TsValidationFailedRtException.checkError( doValidateSetParams( valuesToSet, opset ) );
      return opset.extendSet( valuesToSet );
    }
    return false;
  }

  @Override
  public boolean refreshSet( IOptionSet aOps ) {
    IOptionSetEdit valuesToSet = null; // #valuesToSet will contain options already existing in #opset
    for( String opId : aOps.keys() ) {
      if( opset.hasKey( opId ) ) {
        if( valuesToSet == null ) { // lazy initialization of #valuesToSet
          valuesToSet = new OptionSet();
        }
        valuesToSet.setValue( opId, aOps.getByKey( opId ) );
      }
    }
    if( valuesToSet == null ) { // nothing to do
      return false;
    }
    if( changeInterceptor.interceptPropsChange( opset, valuesToSet ) ) {
      TsValidationFailedRtException.checkError( doValidateSetParams( valuesToSet, opset ) );
      return opset.refreshSet( valuesToSet );
    }
    return false;
  }

  @Override
  public void setAll( IOptionSet aOps ) {
    IOptionSetEdit valuesToSet = new OptionSet( aOps );
    if( changeInterceptor.interceptPropsChange( null, valuesToSet ) ) {
      TsValidationFailedRtException.checkError( doValidateSetParams( valuesToSet, opset ) );
      opset.setAll( aOps );
    }
  }

  @Override
  public void addAll( IMap<String, ? extends IAtomicValue> aOps ) {
    addAll( OptionSetUtils.createFromMap( aOps ) );
  }

  @Override
  public boolean extendSet( IMap<String, ? extends IAtomicValue> aOps ) {
    return extendSet( OptionSetUtils.createFromMap( aOps ) );
  }

  @Override
  public boolean refreshSet( IMap<String, ? extends IAtomicValue> aOps ) {
    return refreshSet( OptionSetUtils.createFromMap( aOps ) );
  }

  @Override
  public void setAll( IMap<String, ? extends IAtomicValue> aOps ) {
    setAll( OptionSetUtils.createFromMap( aOps ) );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the interceptor.
   *
   * @param aInterceptor {@link IOptionsBatchEditInterceptor} - the interceptor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setInterceptor( IOptionsBatchEditInterceptor aInterceptor ) {
    TsNullArgumentRtException.checkNull( aInterceptor );
    changeInterceptor = aInterceptor;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Implementation may check if editing can be applied to options.
   * <p>
   * Argument <code>aNewValues</code> contains option set content as it will be if validation succeed.
   * <p>
   * In base class returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when overriding.
   *
   * @param aNewValues {@link IOptionSet} - values to be changed
   * @param aOldValues {@link IOptionSet} - values before editing starts
   * @return {@link ValidationResult} - check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected ValidationResult doValidateSetParams( IOptionSet aNewValues, IOptionSet aOldValues ) {
    return ValidationResult.SUCCESS;
  }

}
