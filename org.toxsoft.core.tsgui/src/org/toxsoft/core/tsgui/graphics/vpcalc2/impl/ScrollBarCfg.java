package org.toxsoft.core.tsgui.graphics.vpcalc2.impl;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.vpcalc2.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IScrollBarCfg} implementation.
 *
 * @author hazard157
 */
public class ScrollBarCfg
    implements IScrollBarCfg {

  private static final int PAGE_INCR_STEPS_FOR_FULL_RANGE = 10;
  private static final int INCR_STEPS_FOR_FULL_RANGE      = 100;

  private boolean visible = true;

  private int selection;
  private int minimum;
  private int maximum;
  private int thumb;
  private int increment;
  private int pageIncrement;

  /**
   * Constructor.
   */
  public ScrollBarCfg() {
    this( 0, 0, 110, 10, 1, 10 );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link IScrollBarCfg} - the source
   */
  public ScrollBarCfg( IScrollBarCfg aSource ) {
    this( aSource.selection(), aSource.minimum(), aSource.maximum(), aSource.thumb(), aSource.increment(),
        aSource.pageIncrement() );
  }

  /**
   * Constructor.
   *
   * @param aSelection int - the new selection value
   * @param aMinimum int - the new minimum value
   * @param aMaximum int - the new maximum value
   * @param aThumb int - the new thumb value
   * @param aIncrement int - the new increment value
   * @param aPageIncr int - the new pageIncrement value
   */
  public ScrollBarCfg( int aSelection, int aMinimum, int aMaximum, int aThumb, int aIncrement, int aPageIncr ) {
    selection = aSelection;
    minimum = aMinimum;
    maximum = aMaximum;
    thumb = aThumb;
    increment = aIncrement;
    pageIncrement = aPageIncr;
  }

  /**
   * Creates instance with values of the specified scroll bar.
   *
   * @param aScrollBar {@link ScrollBar} - the scroll bar
   * @return {@link ScrollBarCfg} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ScrollBarCfg of( ScrollBar aScrollBar ) {
    TsNullArgumentRtException.checkNull( aScrollBar );
    return new ScrollBarCfg( aScrollBar.getSelection(), aScrollBar.getMinimum(), aScrollBar.getMaximum(),
        aScrollBar.getThumb(), aScrollBar.getIncrement(), aScrollBar.getPageIncrement() );
  }

  /**
   * Creates instance with tuned values as if {@link #tuneScrollBar(int, int, int, int)} was called.
   *
   * @param aVp1 int - viewport left/top coordinate in {@link GC} coordinates space
   * @param aVp2 int - viewport right/bottom coordinate in {@link GC} coordinates space
   * @param aContentSize int - the content width/height
   * @param aOrigin int - origin X/Y coordinate
   * @return {@link ScrollBarCfg} - created instance
   * @throws TsIllegalArgumentRtException content size < 1
   */
  public static ScrollBarCfg ofTuned( int aVp1, int aVp2, int aContentSize, int aOrigin ) {
    ScrollBarCfg sbs = new ScrollBarCfg();
    sbs.tuneScrollBar( aVp1, aVp2, aContentSize, aOrigin );
    return sbs;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Applies configto the specified scroll bar.
   *
   * @param aCfg {@link IScrollBarCfg} - the config
   * @param aScrollBar {@link ScrollBar} - the scroll bar
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void applyTo( IScrollBarCfg aCfg, ScrollBar aScrollBar ) {
    TsNullArgumentRtException.checkNulls( aCfg, aScrollBar );
    aScrollBar.setValues( aCfg.selection(), aCfg.minimum(), aCfg.maximum(), aCfg.thumb(), aCfg.increment(),
        aCfg.pageIncrement() );
    aScrollBar.setVisible( aCfg.isVisible() );
  }

  /**
   * Applies settings to the specified scroll bar.
   *
   * @param aScrollBar {@link ScrollBar} - the scroll bar
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void applyTo( ScrollBar aScrollBar ) {
    TsNullArgumentRtException.checkNull( aScrollBar );
    aScrollBar.setValues( selection, minimum, maximum, thumb, increment, pageIncrement );
    aScrollBar.setVisible( visible );
  }

  /**
   * Copies parameters from the source
   *
   * @param aSource {@link IScrollBarCfg} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void copyFrom( IScrollBarCfg aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    selection = aSource.selection();
    minimum = aSource.minimum();
    maximum = aSource.maximum();
    thumb = aSource.thumb();
    increment = aSource.increment();
    pageIncrement = aSource.pageIncrement();
    visible = aSource.isVisible();
  }

  @SuppressWarnings( "javadoc" )
  public void setVisible( boolean aVisible ) {
    visible = aVisible;
  }

  @SuppressWarnings( "javadoc" )
  public void setSelection( int aValue ) {
    selection = aValue;
  }

  @SuppressWarnings( "javadoc" )
  public void setMinimum( int aValue ) {
    minimum = aValue;
  }

  @SuppressWarnings( "javadoc" )
  public void setMaximum( int aValue ) {
    maximum = aValue;
  }

  @SuppressWarnings( "javadoc" )
  public void setThumb( int aValue ) {
    thumb = aValue;
  }

  @SuppressWarnings( "javadoc" )
  public void setIncrement( int aValue ) {
    increment = aValue;
  }

  @SuppressWarnings( "javadoc" )
  public void setPageIncrement( int aValue ) {
    pageIncrement = aValue;
  }

  /**
   * Sets up scroll bar according to the specified parameters.
   *
   * @param aVp1 int - viewport left/top coordinate in {@link GC} coordinates space
   * @param aVp2 int - viewport right/bottom coordinate in {@link GC} coordinates space
   * @param aContentSize int - the content width/height
   * @param aOrigin int - origin X/Y coordinate
   * @throws TsIllegalArgumentRtException content size < 1
   */
  public void tuneScrollBar( int aVp1, int aVp2, int aContentSize, int aOrigin ) {
    TsIllegalArgumentRtException.checkTrue( aContentSize < 1 );
    minimum = Math.min( aOrigin, aVp1 );
    thumb = Math.min( aContentSize, aVp2 - aVp1 + 1 );
    maximum = Math.max( aOrigin + aContentSize, aVp2 );
    selection = aOrigin;
    increment = aContentSize / INCR_STEPS_FOR_FULL_RANGE;
    if( increment < 1 ) {
      increment = 1;
    }
    pageIncrement = aContentSize / PAGE_INCR_STEPS_FOR_FULL_RANGE;
    if( pageIncrement < 2 ) {
      pageIncrement = 2;
    }
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return "(sel=" + selection + ", min=" + minimum + ", max=" + maximum + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        ", thumb=" + thumb + ", incr=" + increment + ", page=" + pageIncrement + ')'; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + selection;
    result = PRIME * result + minimum;
    result = PRIME * result + maximum;
    result = PRIME * result + thumb;
    result = PRIME * result + increment;
    result = PRIME * result + pageIncrement;
    return result;
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof IScrollBarCfg that ) {
      return this.selection == that.selection() && this.minimum == that.minimum() && this.maximum == that.maximum()
          && this.thumb == that.thumb() && this.increment == that.increment()
          && this.pageIncrement == that.pageIncrement();
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // IScrollBarCfg
  //

  @Override
  public boolean isVisible() {
    return visible;
  }

  @Override
  public int selection() {
    return selection;
  }

  @Override
  public int minimum() {
    return minimum;
  }

  @Override
  public int maximum() {
    return maximum;
  }

  @Override
  public int thumb() {
    return thumb;
  }

  @Override
  public int increment() {
    return increment;
  }

  @Override
  public int pageIncrement() {
    return pageIncrement;
  }

}
