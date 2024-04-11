package org.toxsoft.core.tslib.math.combicond.impl;

import java.io.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ISingleCondParams} immutable implementation.
 *
 * @author hazard157
 */
public final class SingleCondParams
    implements ISingleCondParams, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "SingleCondParams"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ISingleCondParams> KEEPER =
      new AbstractEntityKeeper<>( ISingleCondParams.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ISingleCondParams aEntity ) {
          aSw.writeAsIs( aEntity.typeId() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
        }

        @Override
        protected ISingleCondParams doRead( IStrioReader aSr ) {
          String typeId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          return new SingleCondParams( typeId, params );
        }

      };

  private final String         typeId;
  private final IOptionSetEdit params = new OptionSet();

  /**
   * Constructor.
   *
   * @param aTypeId String - condition type ID (IDpath) must match corresponding {@link ISingleCondType#id()}
   * @param aParams {@link IOptionSet} - condition parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   */
  public SingleCondParams( String aTypeId, IOptionSet aParams ) {
    typeId = StridUtils.checkValidIdPath( aTypeId );
    params.setAll( aParams );
  }

  /**
   * Static constructor.
   *
   * @param aTypeId String - condition type ID (IDpath) must match corresponding {@link ISingleCondType#id()}
   * @param aIdsAndValues Object[] - identifier / value pairs
   * @return {@link SingleCondParams} - the instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static SingleCondParams create( String aTypeId, Object... aIdsAndValues ) {
    return new SingleCondParams( aTypeId, OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

  // ------------------------------------------------------------------------------------
  // ISingleCondParams
  //

  @Override
  public String typeId() {
    return typeId;
  }

  @Override
  public IOptionSet params() {
    return params;
  }

}
