package org.toxsoft.core.tsgui.ved.screen.cfg;

import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedCanvasCfg} implementation.
 * <p>
 * Warning: this implementation uses {@link #params()} to store all data so be careful when changing the options listed
 * below.
 *
 * @author hazard157
 */
public final class VedCanvasCfg
    implements IVedCanvasCfg, IParameterizedEdit {

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<IVedCanvasCfg> KEEPER =
      new AbstractEntityKeeper<>( IVedCanvasCfg.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IVedCanvasCfg aEntity ) {
          OptionSetKeeper.KEEPER_INDENTED.write( aSw, ((VedCanvasCfg)aEntity).params );
        }

        @Override
        protected IVedCanvasCfg doRead( IStrioReader aSr ) {
          IOptionSet p = OptionSetKeeper.KEEPER.read( aSr );
          return new VedCanvasCfg( p );
        }
      };

  static final String OPID_FILL_INFO  = "FillInfo";   //$NON-NLS-1$
  static final String OPID_SIZE       = "Size";       //$NON-NLS-1$
  static final String OPID_CONVERSION = "Conversion"; //$NON-NLS-1$

  static final IStringList ALL_OP_IDS = new StringArrayList( //
      OPID_FILL_INFO, //
      OPID_SIZE, //
      OPID_CONVERSION //
  );

  static final ID2Point DEFAULT_SIZE = new D2Point( 800.0, 600.0 );

  private final IOptionSetEdit params = new OptionSet();

  /**
   * Constructor with defaults.
   */
  public VedCanvasCfg() {
    this( TsFillInfo.NONE, new D2Point( 640.0, 480.0 ) );
  }

  /**
   * Constructor.
   *
   * @param aFillInfo {@link TsFillInfo} - background drawing parameters
   * @param aSize {@link ID2Point} - screen size in the normalized units
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedCanvasCfg( TsFillInfo aFillInfo, ID2Point aSize ) {
    this( aFillInfo, aSize, ID2Conversion.NONE );
  }

  /**
   * Constructor.
   *
   * @param aFillInfo {@link TsFillInfo} - background drawing parameters
   * @param aSize {@link ID2Point} - screen size in the normalized units
   * @param aConversion {@link ID2Conversion} - conversion settings
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedCanvasCfg( TsFillInfo aFillInfo, ID2Point aSize, ID2Conversion aConversion ) {
    TsNullArgumentRtException.checkNulls( aFillInfo, aSize, aConversion );
    params.setValobj( OPID_FILL_INFO, aFillInfo );
    params.setValobj( OPID_SIZE, aSize );
    params.setValobj( OPID_CONVERSION, aConversion );
  }

  private VedCanvasCfg( IOptionSet aParams ) {
    TsNullArgumentRtException.checkNull( aParams );
    for( String opId : ALL_OP_IDS ) {
      IAtomicValue av = aParams.findValue( opId );
      if( av != null ) {
        params.setValue( opId, av );
      }
    }
  }

  /**
   * Makes this instance copy of the source.
   *
   * @param aSource {@link IVedCanvasCfg} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void copyFrom( IVedCanvasCfg aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    VedCanvasCfg src = VedCanvasCfg.class.cast( aSource );
    params.setAll( src.params );
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IViCanvasCfg
  //

  @Override
  public TsFillInfo fillInfo() {
    return params.getValobj( OPID_FILL_INFO, TsFillInfo.NONE );
  }

  @Override
  public ID2Point size() {
    return params.getValobj( OPID_SIZE, DEFAULT_SIZE );
  }

  @Override
  public ID2Conversion conversion() {
    return params.getValobj( OPID_CONVERSION, ID2Conversion.NONE );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the value of {@link #fillInfo()}.
   *
   * @param aFillInfo {@link TsFillInfo} - background drawing parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setFillInfo( TsFillInfo aFillInfo ) {
    TsNullArgumentRtException.checkNull( aFillInfo );
    params.setValobj( OPID_FILL_INFO, aFillInfo );
  }

  /**
   * Sets the value of {@link #size()}.
   *
   * @param aSize {@link ID2Point} - screen size in the normalized units
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setSize( ID2Point aSize ) {
    TsNullArgumentRtException.checkNull( aSize );
    params.setValobj( OPID_SIZE, aSize );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return String.format( "%s - %s", size().toString(), fillInfo().toString() ); //$NON-NLS-1$
  }

  @Override
  public int hashCode() {
    return params.hashCode();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof VedCanvasCfg that ) {
      return this.params.equals( that.params );
    }
    return false;
  }

}
