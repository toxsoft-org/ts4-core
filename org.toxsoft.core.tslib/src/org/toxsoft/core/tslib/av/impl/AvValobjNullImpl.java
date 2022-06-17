package org.toxsoft.core.tslib.av.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.av.*;

/**
 * Atomic value of type {@link EAtomicType#VALOBJ} implemented as <code>null</code> reference holder.
 * <p>
 * There
 * <p>
 * Like the Java <code>null</code> which does not contains type information, NULL value-object holder does not contains
 * keeper identifier.
 *
 * @author hazard157
 */
public class AvValobjNullImpl
    extends AbstractAtomicValue {

  private static final long serialVersionUID = 157157L;

  /**
   * Textual representation of the constant {@link #VALOBJ_NULL} is the string "@{}".
   */
  static final String KTOR = "" + CHAR_VALOBJ_PREFIX + CHAR_SET_BEGIN + CHAR_SET_END; //$NON-NLS-1$

  /**
   * Singltone instance of the <code>null</code> reference holder.
   */
  static final AvValobjNullImpl VALOBJ_NULL = new AvValobjNullImpl();

  /**
   * Methor correctly deserializes {@link AvValobjNullImpl#NULL} value.
   *
   * @return {@link ObjectStreamException} - {@link AvValobjNullImpl#NULL}
   * @throws ObjectStreamException is declared but newer throw by this method
   */
  @SuppressWarnings( "static-method" )
  private Object readResolve()
      throws ObjectStreamException {
    return AvValobjNullImpl.VALOBJ_NULL;
  }

  private AvValobjNullImpl() {
    // nop
  }

  @Override
  public EAtomicType atomicType() {
    return EAtomicType.VALOBJ;
  }

  @Override
  public String asString() {
    return KTOR;
  }

  @Override
  public <T> T asValobj() {
    return null;
  }

  @Override
  protected boolean internalEqualsValue( IAtomicValue aThat ) {
    return aThat.asValobj() == null;
  }

  @Override
  protected int internalCompareValue( IAtomicValue aThat ) {
    return -1; // null is less than any other valobj
  }

  @Override
  protected int internalValueHashCode() {
    return 0;
  }

}
