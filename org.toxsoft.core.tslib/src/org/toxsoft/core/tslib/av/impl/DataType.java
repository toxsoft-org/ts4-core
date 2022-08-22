package org.toxsoft.core.tslib.av.impl;

import java.io.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Mutable implemetation of {@link IDataType}.
 *
 * @author hazard157
 */
public class DataType
    implements IDataType, IParameterizedEdit, Serializable {

  private static final long serialVersionUID = -2236302618890319389L;

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "DataType"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<IDataType> KEEPER =
      new AbstractEntityKeeper<>( IDataType.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IDataType aEntity ) {
          EAtomicType.KEEPER.write( aSw, aEntity.atomicType() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
        }

        @Override
        protected IDataType doRead( IStrioReader aSr ) {
          EAtomicType atomicType = EAtomicType.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          return new DataType( atomicType, params );
        }
      };

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
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public DataType( EAtomicType aAtomicType ) {
    this( aAtomicType, IOptionSet.NULL );
  }

  /**
   * Static onstructor.
   *
   * @param aAtomicType {@link EAtomicType} - atomic type
   * @param aIdsAndValues {@link IOptionSet} - initial params values as in {@link OptionSetUtils#createOpSet(Object...)}
   * @return {@link DataType} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static DataType create( EAtomicType aAtomicType, Object... aIdsAndValues ) {
    TsNullArgumentRtException.checkNull( aAtomicType );
    IOptionSet params = OptionSetUtils.createOpSet( aIdsAndValues );
    return new DataType( aAtomicType, params );
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

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ": " + atomicType.id(); //$NON-NLS-1$
  }

}
