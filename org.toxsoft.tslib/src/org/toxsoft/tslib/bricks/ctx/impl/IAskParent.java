package org.toxsoft.tslib.bricks.ctx.impl;

import org.toxsoft.tslib.av.IAtomicValue;

/**
 * Internal interface to search parent references and options.
 *
 * @author hazard157
 */
public interface IAskParent {

  /**
   * Returns option from the parent by ID.
   *
   * @param aId String - the ID
   * @return {@link IAtomicValue} - found reference or <code>null</code>
   */

  IAtomicValue findOp( String aId );

  /**
   * Returns reference from parent by name.
   *
   * @param aName String - the name of the reference
   * @return {@link Object} - found reference of <code>null</code>
   */
  Object findRef( String aName );

}
