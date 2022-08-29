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
 * All methods of interface {@link IOpsBatchEdit} additionally may throw {@link TsValidationFailedRtException} erro if
 * {@link #doValidateSetParams(IOptionSet, IOptionSet)} validation fails.
 *
 * @author hazard157
 */
public class OpsBatchEdit
    implements IOpsBatchEdit {

  private final IOptionSetEdit opset;

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
    IOptionSetEdit newValues = new OptionSet( opset );
    newValues.addAll( aOps );
    ValidationResult vr = doValidateSetParams( newValues, opset );
    TsValidationFailedRtException.checkError( vr );
    opset.setAll( newValues );
  }

  @Override
  public void addAll( IMap<String, ? extends IAtomicValue> aOps ) {
    IOptionSetEdit newValues = new OptionSet( opset );
    newValues.addAll( aOps );
    ValidationResult vr = doValidateSetParams( newValues, opset );
    TsValidationFailedRtException.checkError( vr );
    opset.setAll( newValues );
  }

  @Override
  public boolean extendSet( IOptionSet aOps ) {
    IOptionSetEdit newValues = new OptionSet( opset );
    newValues.extendSet( aOps );
    ValidationResult vr = doValidateSetParams( newValues, opset );
    TsValidationFailedRtException.checkError( vr );
    return opset.extendSet( newValues );
  }

  @Override
  public boolean extendSet( IMap<String, ? extends IAtomicValue> aOps ) {
    IOptionSetEdit newValues = new OptionSet( opset );
    newValues.extendSet( aOps );
    ValidationResult vr = doValidateSetParams( newValues, opset );
    TsValidationFailedRtException.checkError( vr );
    return opset.extendSet( newValues );
  }

  @Override
  public boolean refreshSet( IOptionSet aOps ) {
    IOptionSetEdit newValues = new OptionSet( opset );
    newValues.refreshSet( aOps );
    ValidationResult vr = doValidateSetParams( newValues, opset );
    TsValidationFailedRtException.checkError( vr );
    return opset.refreshSet( newValues );
  }

  @Override
  public boolean refreshSet( IMap<String, ? extends IAtomicValue> aOps ) {
    IOptionSetEdit newValues = new OptionSet( opset );
    newValues.refreshSet( aOps );
    ValidationResult vr = doValidateSetParams( newValues, opset );
    TsValidationFailedRtException.checkError( vr );
    return opset.refreshSet( newValues );
  }

  @Override
  public void setAll( IOptionSet aOps ) {
    ValidationResult vr = doValidateSetParams( aOps, opset );
    TsValidationFailedRtException.checkError( vr );
    opset.refreshSet( aOps );
  }

  @Override
  public void setAll( IMap<String, ? extends IAtomicValue> aOps ) {
    IOptionSetEdit newValues = new OptionSet();
    newValues.setAll( aOps );
    ValidationResult vr = doValidateSetParams( newValues, opset );
    TsValidationFailedRtException.checkError( vr );
    opset.refreshSet( newValues );
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Implementation may check if editing can be applied to options.
   * <p>
   * Any kind of options editing, any editing API, leads to the change of {@link IOptionSet} from old values to new one.
   * This method creates temporary new values option set and allows to check if editing is possible.
   * <p>
   * In base class returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when overriding.
   *
   * @param aNewValues {@link IOptionSet} - values that will be after editing
   * @param aOldValues {@link IOptionSet} - values before editing starts
   * @return {@link ValidationResult} - check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected ValidationResult doValidateSetParams( IOptionSet aNewValues, IOptionSet aOldValues ) {
    return ValidationResult.SUCCESS;
  }

}
