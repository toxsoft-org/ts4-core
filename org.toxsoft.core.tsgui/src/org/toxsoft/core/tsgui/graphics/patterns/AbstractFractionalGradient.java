package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The base class for implementing the description of gradients when filling shapes.
 *
 * @author vs
 */
public abstract class AbstractFractionalGradient
    extends AbstractGradient {

  Pattern     pattern  = null;
  boolean     disposed = false;
  private int width    = 0;
  private int height   = 0;

  private IListEdit<IGradientFraction> fractions = new ElemArrayList<>();

  /**
   * Constructor.
   *
   * @param aFractions IList&lt;Pair&lt;Double, RGBA>> - the list of fractions as pairs value-color
   * @param aContext ITsGuiContext - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException - number of fractions is less than 2 2
   */
  public AbstractFractionalGradient( IList<Pair<Double, RGBA>> aFractions, ITsGuiContext aContext ) {
    super( aContext );
    TsIllegalArgumentRtException.checkTrue( aFractions.size() < 2 );
    Pair<Double, RGBA> prevPair = null;
    for( Pair<Double, RGBA> p : aFractions ) {
      if( prevPair == null ) {
        prevPair = p;
        continue;
      }
      fractions.add( createFraction( prevPair, p ) );
      prevPair = p;
    }
  }

  // ------------------------------------------------------------------------------------
  // IDisposable
  //

  @Override
  public boolean isDisposed() {
    return disposed;
  }

  @Override
  public void dispose() {
    if( !disposed ) {
      disposed = true;
    }
  }

  // ------------------------------------------------------------------------------------
  // IGradientPattern
  //

  @Override
  public Pattern pattern( GC aGc, int aWidth, int aHeight ) {
    if( shouldRecreate( aWidth, aHeight ) ) {
      recreate( aGc, aWidth, aHeight );
    }
    return pattern;
  }

  // ------------------------------------------------------------------------------------
  // for subclasses
  //

  protected IListEdit<IGradientFraction> fractions() {
    return fractions;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private boolean shouldRecreate( int aWidth, int aHeight ) {
    if( pattern == null || pattern.isDisposed() ) {
      return true;
    }
    if( width != aWidth || height != aHeight ) {
      return true;
    }
    return false;
  }

  private void recreate( GC aGc, int aWidth, int aHeight ) {
    width = aWidth;
    height = aHeight;
    if( pattern != null && !pattern.isDisposed() ) {
      pattern.dispose();
    }
    if( width <= 0 || aHeight <= 0 ) {
      pattern = null;
      return;
    }
    pattern = null;
    Image img = createImage( aGc, aWidth, aHeight );
    if( img != null ) {
      pattern = new Pattern( aGc.getDevice(), img );
      img.dispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // to override
  //

  abstract IGradientFraction createFraction( Pair<Double, RGBA> aStart, Pair<Double, RGBA> aEnd );

  abstract Image createImage( GC aGc, int aWidth, int aHeight );

}
