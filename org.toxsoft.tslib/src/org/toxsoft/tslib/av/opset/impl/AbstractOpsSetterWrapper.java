package org.toxsoft.tslib.av.opset.impl;

import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.opset.IOpsSetter;
import org.toxsoft.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IOpsSetter} implementaion wrapped over existing {@link IOptionSetEdit} instance.
 *
 * @author hazard157
 */
public abstract class AbstractOpsSetterWrapper
    extends AbstractOptionsSetter {

  private final IOptionSetEdit source;

  /**
   * Constructor.
   *
   * @param aSource {@link IOptionSetEdit} - options set to be wrapped
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractOpsSetterWrapper( IOptionSetEdit aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // AbstractOptionsSetter
  //

  @Override
  protected abstract void doInternalSet( String aId, IAtomicValue aValue );

  @Override
  protected IAtomicValue doInternalFind( String aId ) {
    return source.findByKey( aId );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the wrapped set.
   *
   * @return {@link IOptionSetEdit} - the wrapped set
   */
  final public IOptionSetEdit source() {
    return source;
  }

}
