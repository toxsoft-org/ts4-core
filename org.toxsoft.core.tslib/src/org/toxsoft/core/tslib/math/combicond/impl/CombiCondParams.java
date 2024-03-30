package org.toxsoft.core.tslib.math.combicond.impl;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ICombiCondParams} implementation.
 * <p>
 * Static constructors used to create combined condition parameters. Different <code>createXxx()</code> methods have all
 * possible combinations of the single and combined filter parameters the arguments.
 * <p>
 * Note: {@link #name()} may be changed at any time with method {@link #setName(String)}.
 *
 * @author hazard157
 */
public abstract class CombiCondParams
    implements ICombiCondParams, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ICombiCondParams> KEEPER =
      new AbstractEntityKeeper<>( ICombiCondParams.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ICombiCondParams aEntity ) {
          // name
          aSw.writeQuotedString( aEntity.name() );
          aSw.writeSeparatorChar();
          // isSingle
          aSw.writeBoolean( aEntity.isSingle() );
          aSw.writeSeparatorChar();
          // isResultInverted
          aSw.writeBoolean( aEntity.isInverted() );
          aSw.writeSeparatorChar();
          if( aEntity.isSingle() ) {
            SingleCondParams.KEEPER.write( aSw, aEntity.single() );
            return;
          }
          aSw.incNewLine();
          // left ICombiCondParams
          KEEPER.write( aSw, aEntity.left() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // op
          ELogicalOp.KEEPER.write( aSw, aEntity.op() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // right ICombiCondParams
          KEEPER.write( aSw, aEntity.right() );
          aSw.writeSeparatorChar();
          aSw.decNewLine();
        }

        @Override
        protected ICombiCondParams doRead( IStrioReader aSr ) {
          // name
          String name = aSr.readQuotedString();
          aSr.ensureSeparatorChar();
          // isSingle
          boolean isSingle = aSr.readBoolean();
          aSr.ensureSeparatorChar();
          // isResultInverted
          boolean isResultInverted = aSr.readBoolean();
          aSr.ensureSeparatorChar();
          if( isSingle ) {
            ISingleCondParams sfp = SingleCondParams.KEEPER.read( aSr );
            return CombiCondParams.createSingle( sfp, isResultInverted );
          }
          // left ICombiCondParams
          ICombiCondParams pfp1 = KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          // op
          ELogicalOp op = ELogicalOp.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          // right ICombiCondParams
          ICombiCondParams pfp2 = KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          CombiCondParams ccp = CombiCondParams.createCombi( pfp1, op, pfp2, isResultInverted );
          ccp.setName( name );
          return ccp;
        }

      };

  private final boolean isResultInverted;

  private String name = TsLibUtils.EMPTY_STRING;

  protected CombiCondParams( boolean aIsInverted ) {
    isResultInverted = aIsInverted;
  }

  // ------------------------------------------------------------------------------------
  // Static constructors
  //

  /**
   * Static constructor.
   *
   * @param aParams {@link ISingleCondParams} - single condition parameters
   * @param aIsResultInverted boolean - the value of the flag {@link ICombiCondParams#isInverted()}
   * @return {@link ICombiCondParams} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static CombiCondParams createSingle( ISingleCondParams aParams, boolean aIsResultInverted ) {
    TsNullArgumentRtException.checkNull( aParams );
    return new InternalCondParamsSingle( aParams, aIsResultInverted );
  }

  /**
   * Static constructor.
   * <p>
   * The same as {@link #createSingle(ISingleCondParams, boolean) createSingle(aParams, <b>false</b>)}.
   *
   * @param aParams {@link ISingleCondParams} - single condition parameters
   * @return {@link ICombiCondParams} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static CombiCondParams createSingle( ISingleCondParams aParams ) {
    TsNullArgumentRtException.checkNull( aParams );
    return new InternalCondParamsSingle( aParams, false );
  }

  /**
   * Static constructor.
   *
   * @param aParams {@link ICombiCondParams} - combi condition parameters
   * @param aIsResultInverted boolean - the value of the flag {@link ICombiCondParams#isInverted()}
   * @return {@link ICombiCondParams} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static CombiCondParams createCombi( ICombiCondParams aParams, boolean aIsResultInverted ) {
    TsNullArgumentRtException.checkNull( aParams );
    boolean inverted = aIsResultInverted ? !aParams.isInverted() : aParams.isInverted();
    if( aParams.isSingle() ) {
      return new InternalCondParamsSingle( aParams.single(), inverted );
    }
    return new InternalCondParamsCombi( aParams.left(), aParams.op(), aParams.right(), inverted );
  }

  /**
   * Static constructor.
   *
   * @param aLeft {@link ICombiCondParams} - left condition parameters
   * @param aOp {@link ELogicalOp} - logical operation between left and right conditions
   * @param aRight {@link ICombiCondParams} - right condition parameters
   * @param aIsResultInverted boolean - the value of the flag {@link ICombiCondParams#isInverted()}
   * @return {@link ICombiCondParams} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static CombiCondParams createCombi( ICombiCondParams aLeft, ELogicalOp aOp, ICombiCondParams aRight,
      boolean aIsResultInverted ) {
    TsNullArgumentRtException.checkNulls( aLeft, aOp, aRight );
    return new InternalCondParamsCombi( aLeft, aOp, aRight, aIsResultInverted );
  }

  /**
   * Static constructor.
   * <p>
   * The same as {@link #createCombi(ICombiCondParams, ELogicalOp, ICombiCondParams, boolean) createCombi(aLeft, aOp,
   * aRight,` <b>false</b>)}.
   *
   * @param aLeft {@link ICombiCondParams} - left condition parameters
   * @param aOp {@link ELogicalOp} - logical operation between left and right conditions
   * @param aRight {@link ICombiCondParams} - right condition parameters
   * @return {@link ICombiCondParams} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static CombiCondParams createCombi( ICombiCondParams aLeft, ELogicalOp aOp, ICombiCondParams aRight ) {
    return createCombi( aLeft, aOp, aRight, false );
  }

  /**
   * Static constructor.
   *
   * @param aLeft {@link ISingleCondParams} - left condition parameters
   * @param aOp {@link ELogicalOp} - logical operation between left and right conditions
   * @param aRight {@link ISingleCondParams} - right condition parameters
   * @param aIsResultInverted boolean - the value of the flag {@link ICombiCondParams#isInverted()}
   * @return {@link ICombiCondParams} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static CombiCondParams createCombi( ISingleCondParams aLeft, ELogicalOp aOp, ISingleCondParams aRight,
      boolean aIsResultInverted ) {
    TsNullArgumentRtException.checkNulls( aLeft, aOp, aRight );
    ICombiCondParams sf1 = new InternalCondParamsSingle( aLeft, false );
    ICombiCondParams sf2 = new InternalCondParamsSingle( aRight, false );
    return new InternalCondParamsCombi( sf1, aOp, sf2, aIsResultInverted );
  }

  /**
   * Static constructor.
   *
   * @param aLeft {@link ISingleCondParams} - left condition parameters
   * @param aOp {@link ELogicalOp} - logical operation between left and right conditions
   * @param aRight {@link ISingleCondParams} - right condition parameters
   * @return {@link ICombiCondParams} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static CombiCondParams createCombi( ISingleCondParams aLeft, ELogicalOp aOp, ISingleCondParams aRight ) {
    TsNullArgumentRtException.checkNulls( aLeft, aOp, aRight );
    ICombiCondParams sf1 = new InternalCondParamsSingle( aLeft, false );
    ICombiCondParams sf2 = new InternalCondParamsSingle( aRight, false );
    return new InternalCondParamsCombi( sf1, aOp, sf2, false );
  }

  /**
   * Static constructor.
   *
   * @param aLeft {@link ISingleCondParams} - left condition parameters
   * @param aOp {@link ELogicalOp} - logical operation between left and right conditions
   * @param aRight {@link ICombiCondParams} - right condition parameters
   * @param aIsResultInverted boolean - the value of the flag {@link ICombiCondParams#isInverted()}
   * @return {@link ICombiCondParams} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static CombiCondParams createCombi( ISingleCondParams aLeft, ELogicalOp aOp, ICombiCondParams aRight,
      boolean aIsResultInverted ) {
    TsNullArgumentRtException.checkNulls( aLeft, aOp, aRight );
    ICombiCondParams sf = new InternalCondParamsSingle( aLeft, false );
    return new InternalCondParamsCombi( sf, aOp, aRight, aIsResultInverted );
  }

  /**
   * Static constructor.
   * <p>
   * The same as {@link #createCombi(ISingleCondParams, ELogicalOp, ICombiCondParams, boolean) createCombi(aLeft, aOp,
   * aRight,` <b>false</b>)}.
   *
   * @param aLeft {@link ISingleCondParams} - left condition parameters
   * @param aOp {@link ELogicalOp} - logical operation between left and right conditions
   * @param aRight {@link ICombiCondParams} - right condition parameters
   * @return {@link ICombiCondParams} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static CombiCondParams createCombi( ISingleCondParams aLeft, ELogicalOp aOp, ICombiCondParams aRight ) {
    return createCombi( aLeft, aOp, aRight, false );
  }

  /**
   * Static constructor.
   *
   * @param aLeft {@link ICombiCondParams} - left condition parameters
   * @param aOp {@link ELogicalOp} - logical operation between left and right conditions
   * @param aRight {@link ISingleCondParams} - right condition parameters
   * @param aIsResultInverted boolean - the value of the flag {@link ICombiCondParams#isInverted()}
   * @return {@link ICombiCondParams} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static CombiCondParams createCombi( ICombiCondParams aLeft, ELogicalOp aOp, ISingleCondParams aRight,
      boolean aIsResultInverted ) {
    TsNullArgumentRtException.checkNulls( aLeft, aOp, aRight );
    ICombiCondParams sf = new InternalCondParamsSingle( aRight, false );
    return new InternalCondParamsCombi( aLeft, aOp, sf, aIsResultInverted );
  }

  /**
   * Static constructor.
   * <p>
   * The same as {@link #createCombi(ICombiCondParams, ELogicalOp, ISingleCondParams, boolean) createCombi(aLeft, aOp,
   * aRight,` <b>false</b>)}.
   *
   * @param aLeft {@link ICombiCondParams} - left condition parameters
   * @param aOp {@link ELogicalOp} - logical operation between left and right conditions
   * @param aRight {@link ISingleCondParams} - right condition parameters
   * @return {@link ICombiCondParams} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static CombiCondParams createCombi( ICombiCondParams aLeft, ELogicalOp aOp, ISingleCondParams aRight ) {
    return createCombi( aLeft, aOp, aRight, false );
  }

  // ------------------------------------------------------------------------------------
  // ICombiCondParams
  //

  @Override
  public String name() {
    return name;
  }

  @Override
  public boolean isInverted() {
    return isResultInverted;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the {@link #name()}.
   *
   * @param aName String - human-readable name
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    name = aName;
  }

}
