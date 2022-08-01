package org.toxsoft.core.tsgui.ved.impl;

import static org.toxsoft.core.tsgui.ved.std.IVedStdProperties.*;
import static org.toxsoft.core.tslib.bricks.d2.D2Utils.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.incub.props.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Базовый класс для реализации всех "представлений" компонент.
 * <p>
 *
 * @author vs
 */
public abstract class VedAbstractComponentView
    implements IVedComponentView, IVedContextable, IVedDisposable {

  /**
   * {@link IVedComponentView#painter()} implementation.
   *
   * @author hazard157
   */
  private class Painter
      implements IVedViewPainter {

    @Override
    public void paint( GC aGc, ITsRectangle aPaintBounds ) {
      doPaint( aGc, aPaintBounds );
    }

  }

  /**
   * {@link IVedComponentView#porter()} implementation.
   *
   * @author hazard157
   */
  private class Porter
      implements IVedViewPorter {

    @Override
    public void locate( double aX, double aY ) {
      double x = duck( checkCoor( aX ) );
      double y = duck( checkCoor( aY ) );
      doSetLocation( x, y );
    }

    @Override
    public void shiftOn( double aDx, double aDy ) {
      double dx = duck( checkCoor( aDx ) );
      double dy = duck( checkCoor( aDy ) );
      doSetLocation( doGetX() + dx, doGetY() + dy );
    }

    @Override
    public void setSize( double aWidth, double aHeight ) {
      double w = duck( checkCoor( aWidth ) );
      double h = duck( checkCoor( aHeight ) );
      doSetBoundsSize( w, h );
    }

    @Override
    public void setBounds( double aX, double aY, double aWidth, double aHeight ) {
      double x = duck( checkCoor( aX ) );
      double y = duck( checkCoor( aY ) );
      double w = duck( checkCoor( aWidth ) );
      double h = duck( checkCoor( aHeight ) );
      doSetLocation( x, y );
      doSetBoundsSize( w, h );
    }

  }

  private final IOptionSetEdit       extdata;
  private final VedAbstractComponent ownerComp;
  private final IVedScreen           ownerScreen;
  private final IVedViewPainter      painter = new Painter();
  private final IVedViewPorter       porter  = new Porter();

  private final ID2ConversionEdit d2Conv = new D2ConversionEdit();

  private boolean disposed = false;

  /**
   * Constructor.
   *
   * @param aComponent {@link VedAbstractComponent} - owner component
   * @param aScreen {@link IVedScreen} - owner screen
   */
  public VedAbstractComponentView( VedAbstractComponent aComponent, IVedScreen aScreen ) {
    TsNullArgumentRtException.checkNulls( aComponent, aScreen );
    ownerComp = aComponent;
    extdata = new OptionSet() {

      @Override
      protected IAtomicValue doInternalFind( String aId ) {
        IAtomicValue val = super.doInternalFind( aId );
        if( val != null ) {
          return val;
        }
        return ownerComp.extdata().findValue( aId );
      }

    };
    ownerScreen = aScreen;
    ownerComp.props().propsEventer().addListener( ( s, id, ov, nv ) -> updateOnPropertiesChange( id, ov, nv ) );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  final private void updateOnPropertiesChange( String aPropId, IAtomicValue aOldValue, IAtomicValue aNewValue ) {
    ITsRectangle r1 = ownerScreen.coorsConvertor().rectBounds( outline().bounds() );
    ownerScreen.paintingManager().redrawRect( r1 );
    doUpdateOnPropertiesChange( aPropId, aOldValue, aNewValue );
    ITsRectangle r2 = ownerScreen.coorsConvertor().rectBounds( outline().bounds() );
    if( !r1.equals( r2 ) ) {
      ownerScreen.paintingManager().redrawRect( r2 );
    }
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  /**
   * Called once by creator to update internals immediately after creation.
   */
  void papiInitialUpdate() {
    doUpdateOnPropertiesChange( null, null, null );
  }

  // ------------------------------------------------------------------------------------
  // IStridable}
  //

  @Override
  final public String id() {
    return ownerComp.id();
  }

  @Override
  final public String nmName() {
    return ownerComp.nmName();
  }

  @Override
  final public String description() {
    return ownerComp.description();
  }

  // ------------------------------------------------------------------------------------
  // ID2Conversionable
  //

  @Override
  final public ID2Conversion getConversion() {
    return d2Conv;
  }

  @Override
  final public void setConversion( ID2Conversion aConversion ) {
    TsNullArgumentRtException.checkNull( aConversion );
    if( !d2Conv.equals( aConversion ) ) {
      d2Conv.setConversion( aConversion );
      doUpdateOnConversionChange();
    }
  }

  // ------------------------------------------------------------------------------------
  // IVedDisposable
  //

  @Override
  public final boolean isDisposed() {
    return disposed;
  }

  @Override
  public final void dispose() {
    if( !disposed ) {
      disposed = true;
      doDispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // IVedComponentView
  //

  @Override
  final public IVedViewPainter painter() {
    return painter;
  }

  @Override
  final public IVedViewPorter porter() {
    return porter;
  }

  @Override
  public abstract IVedOutline outline();

  @Override
  final public IOptionSetEdit extdata() {
    return extdata;
  }

  @Override
  final public IVedComponent component() {
    return ownerComp;
  }

  @Override
  final public IVedScreen ownerScreen() {
    return ownerScreen;
  }

  @Override
  public void redraw() {
    ownerScreen.paintingManager().redrawView( id() );
  }

  // ------------------------------------------------------------------------------------
  // IVedContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return ownerComp.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Sublass may perform additional resource cleaning when view not needed any more.
   */
  protected void doDispose() {
    // nop
  }

  protected double doGetX() {
    return ownerComp.props().getDouble( PDEF_X );
  }

  protected double doGetY() {
    return ownerComp.props().getDouble( PDEF_Y );
  }

  protected double doGetWidth() {
    return ownerComp.props().getDouble( PDEF_WIDTH );
  }

  protected double doGetHeight() {
    return ownerComp.props().getDouble( PDEF_HEIGHT );
  }

  protected void doSetLocation( double aX, double aY ) {
    ownerComp.props().propsEventer().pauseFiring();
    ownerComp.props().setDouble( PDEF_X, aX );
    ownerComp.props().setDouble( PDEF_Y, aY );
    ownerComp.props().propsEventer().resumeFiring( true );
  }

  protected void doSetBoundsSize( double aWidth, double aHeight ) {
    ownerComp.props().propsEventer().pauseFiring();
    ownerComp.props().setDouble( PDEF_WIDTH, aWidth );
    ownerComp.props().setDouble( PDEF_HEIGHT, aHeight );
    ownerComp.props().propsEventer().resumeFiring( true );
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Returns the owner component properties.
   *
   * @return {@link IOptionSet} - component properties
   */
  public IPropertiesSet props() {
    return ownerComp.props();
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass must prepare painting when {@link #getConversion()} changes.
   * <p>
   * This method is called from {@link #setConversion(ID2Conversion)} when conversion parameters really changes.
   */
  protected abstract void doUpdateOnConversionChange();

  /**
   * Subclass must prepare painting when any of the owner component property changes.
   *
   * @param aPropId String changed property ID or <code>null</code> for batch changes
   * @param aOldValue {@link IAtomicValue} - property value before change or <code>null</code> for batch changes
   * @param aNewValue {@link IAtomicValue} - property value after change or <code>null</code> for batch changes
   */
  protected abstract void doUpdateOnPropertiesChange( String aPropId, IAtomicValue aOldValue, IAtomicValue aNewValue );

  /**
   * Subclass must paint the view.
   *
   * @param aGc {@link GC} - the graphics context
   * @param aPaintBounds {@link ITsRectangle} - rectangle region in pixels that need to be painted
   */
  protected abstract void doPaint( GC aGc, ITsRectangle aPaintBounds );

}
