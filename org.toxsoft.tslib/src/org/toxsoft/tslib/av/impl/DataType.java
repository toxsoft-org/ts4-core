package org.toxsoft.tslib.av.impl;

import java.io.Serializable;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.metainfo.IDataType;
import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.tslib.av.opset.impl.OptionSet;
import org.toxsoft.tslib.av.opset.impl.OptionSetUtils;
import org.toxsoft.tslib.av.utils.IParameterizedEdit;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Mutable implemetation of {@link IDataType}.
 *
 * @author hazard157
 */
public class DataType
    implements IDataType, IParameterizedEdit, Serializable {

  private static final long serialVersionUID = -2236302618890319389L;

  private EAtomicType          atomicType = EAtomicType.NONE;
  private final IOptionSetEdit params     = new OptionSet();

  /**
   * Constructor.
   *
   * @param aAtomicType {@link EAtomicType} - atomic type
   * @param aParams {@link IOptionSet} - initial values of {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public DataType( EAtomicType aAtomicType, IOptionSet aParams ) {
    atomicType = TsNullArgumentRtException.checkNull( aAtomicType );
    params.addAll( aParams );
  }

  /**
   * Constructor.
   *
   * @param aAtomicType {@link EAtomicType} - atomic type
   * @param aIdsAndValues {@link IOptionSet} - initial params values as in {@link OptionSetUtils#createOpSet(Object...)}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public DataType( EAtomicType aAtomicType, Object... aIdsAndValues ) {
    atomicType = TsNullArgumentRtException.checkNull( aAtomicType );
    params.addAll( OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

  /**
   * Constructor.
   *
   * @param aAtomicType {@link EAtomicType} - atomic type
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public DataType( EAtomicType aAtomicType ) {
    this( aAtomicType, IOptionSet.NULL );
  }

  // ------------------------------------------------------------------------------------
  // IDataType
  //

  @Override
  public EAtomicType atomicType() {
    return atomicType;
  }

  @Override
  public IOptionSetEdit params() {
    return params;
  }

}
