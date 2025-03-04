package org.toxsoft.core.tsgui.graphics.image.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.wrappers.*;
import org.toxsoft.core.tslib.coll.wrappers.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * An image that generally consists of several frames of animation.
 * <p>
 * There is no instance of this class without at least one frame.
 *
 * @author hazard157
 */
public final class TsImage {

  private final IList<Image> frames;
  private final ILongList    delays;
  private final long         cycleDuration;
  private final long         minDelay;
  private final long         maxDelay;
  private final int          imageIndex;

  private ITsPoint imageSize;
  private boolean  evenAnimation;

  private boolean disposed = false;

  // ------------------------------------------------------------------------------------
  // Creation
  //

  /**
   * Creates image from existing frames list.
   * <p>
   * For argument arrays defensive copys are created.
   *
   * @param aFrames {@link IList}&lt;{@link Image}&gt; - the frame images
   * @param aDelays {@link ILongList} - delayes for each frame
   * @param aImageIndex int - still image {@link #image()} selection index
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aFrames argument is empty
   * @throws TsIllegalArgumentRtException aDelays size is not equal to aFrames size
   * @throws TsIllegalArgumentRtException aImageIndex is out of range
   * @throws TsIllegalArgumentRtException any delay value < 0
   */
  public TsImage( IList<Image> aFrames, ILongList aDelays, int aImageIndex ) {
    TsNullArgumentRtException.checkNulls( aFrames, aDelays );
    TsIllegalArgumentRtException.checkTrue( aFrames.isEmpty() );
    TsIllegalArgumentRtException.checkTrue( aFrames.size() != aDelays.size() );
    TsIllegalArgumentRtException.checkTrue( aImageIndex < 0 || aImageIndex >= aFrames.size() );
    frames = new ElemArrayList<>( aFrames );
    delays = new LongArrayList( aDelays );
    imageIndex = aImageIndex;
    cycleDuration = calcSum( delays );
    minDelay = calcMinDelay();
    maxDelay = calcMaxDelay();
    internalInit();
  }

  /**
   * Creates image from existing frames array.
   * <p>
   * For argument arrays wrapper lists are created.
   *
   * @param aFrames &lt;{@link Image}&gt;[] - the frame images arrays
   * @param aDelays long[] - delayes for each frame
   * @param aImageIndex int - still image {@link #image()} selection index
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aFrames argument is empty
   * @throws TsIllegalArgumentRtException aDelays size is not equal to aFrames size
   * @throws TsIllegalArgumentRtException aImageIndex is out of range
   * @throws TsIllegalArgumentRtException any delay value < 0
   */
  public TsImage( Image[] aFrames, long[] aDelays, int aImageIndex ) {
    TsErrorUtils.checkArrayArg( aFrames, 1 );
    TsNullArgumentRtException.checkNull( aDelays );
    TsIllegalArgumentRtException.checkTrue( aFrames.length != aDelays.length );
    frames = new ElemArrayWrapper<>( aFrames );
    delays = new LongArrayWrapper( aDelays );
    imageIndex = aImageIndex;
    cycleDuration = calcSum( delays );
    minDelay = calcMinDelay();
    maxDelay = calcMaxDelay();
    internalInit();
  }

  private void internalInit() {
    // frame must the valid images
    for( int i = 0, n = frames.size(); i < n; i++ ) {
      Image img = frames.get( i );
      TsIllegalArgumentRtException.checkTrue( img.isDisposed() );
    }
    // check delays are not negative
    for( int i = 0; i < delays.size(); i++ ) {
      if( delays.getValue( i ) < 0 ) {
        throw new TsIllegalArgumentRtException();
      }
    }
    // initialize internals
    ImageData imgdata = frames.first().getImageData();
    imageSize = new TsPoint( imgdata.width, imgdata.height );
    boolean isEven = true;
    long delay = delays.getValue( 0 );
    for( int i = 0, n = delays.size(); i < n; i++ ) {
      long d = delays.getValue( i );
      if( d != delay ) {
        isEven = false;
        break;
      }
    }
    evenAnimation = isEven && frames.size() > 1;
  }

  private static long calcSum( ILongList aLongs ) {
    long sum = 0L;
    for( int i = 0; i < aLongs.size(); i++ ) {
      sum += aLongs.getValue( i );
    }
    return sum;
  }

  private long calcMinDelay() {
    long s = Long.MAX_VALUE;
    for( int i = 0; i < delays.size(); i++ ) {
      s = Math.min( s, delays.getValue( i ) );
    }
    return s;
  }

  private long calcMaxDelay() {
    long s = 0L;
    for( int i = 0; i < delays.size(); i++ ) {
      s = Math.max( s, delays.getValue( i ) );
    }
    return s;
  }

  /**
   * Creates a {@link TsImage} with even animation.
   *
   * @param aFrames {@link IList}&lt;{@link Image}&gt; - ordered list of frames
   * @param aDelay long - interframe delay in milliseconds
   * @param aImageIndex int - index of the selected frame {@link #image()}
   * @return {@link TsImage} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the argument contains the disposed image
   * @throws TsIllegalArgumentRtException the frames list is empty
   * @throws TsIllegalArgumentRtException selected frame index is out of range
   */
  public static TsImage create( IList<Image> aFrames, long aDelay, int aImageIndex ) {
    TsNullArgumentRtException.checkNull( aFrames );
    TsIllegalArgumentRtException.checkTrue( aFrames.isEmpty() );
    LongArrayList delays = new LongArrayList();
    for( int i = 0; i < aFrames.size(); i++ ) {
      delays.add( aDelay );
    }
    return new TsImage( aFrames, delays, aImageIndex );
  }

  /**
   * Creates a {@link TsImage} with the first selected frame.
   *
   * @param aFrames {@link IList}&lt;{@link Image}&gt; - ordered list of frames
   * @param aDelays {@link ILongList} - list of interframe delays in milliseconds
   * @return {@link TsImage} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the argument contains the disposed image
   * @throws TsIllegalArgumentRtException the frames list is empty
   * @throws TsIllegalArgumentRtException frame and delay lists have different lengths
   */
  public static TsImage create( IList<Image> aFrames, ILongList aDelays ) {
    return new TsImage( aFrames, aDelays, 0 );
  }

  /**
   * Creates a {@link TsImage} with even animation with the first selected frame.
   *
   * @param aFrames {@link IList}&lt;{@link Image}&gt; - ordered list of frames
   * @param aDelay long - interframe delay in milliseconds
   * @return {@link TsImage} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the argument contains the disposed image
   * @throws TsIllegalArgumentRtException the frames list is empty
   */
  public static TsImage create( IList<Image> aFrames, long aDelay ) {
    return create( aFrames, aDelay, 0 );
  }

  /**
   * Creates a still (not animated) {@link TsImage}.
   *
   * @param aImage {@link Image} - the single frame
   * @return {@link TsImage} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the argument contains the disposed image
   */
  public static TsImage create( Image aImage ) {
    return create( new SingleItemList<>( aImage ), 0L, 0 );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Determines if the image consists of a single frame.
   * <p>
   * Equivalent to checking <code>{@link #count()} == 1</code>.
   *
   * @return boolean - whether the picture image of a single frame<br>
   *         <b>true</b> - yes, there is one frame in the image, and {@link #frames()}.size() = 1;<br>
   *         <b>false</b> - there is more than one frame in the image, this is clearly an animation.
   */
  public boolean isSingleFrame() {
    return frames.size() == 1;
  }

  /**
   * Determines if the image is animated (that is, the number of frames is greater than 1).
   * <p>
   * Equivalent to checking <code>{@link #count()} > 1</code>.
   *
   * @return boolean - a sign of the animated image
   */
  public boolean isAnimated() {
    return frames.size() > 1;
  }

  /**
   * Returns the number of frames.
   *
   * @return int - number of frames, always > 0
   */
  public int count() {
    return frames.size();
  }

  /**
   * Return the size of the first frame.
   *
   * @return {@link ITsPoint} - first frame size in pixels
   */
  public ITsPoint imageSize() {
    return imageSize;
  }

  /**
   * Returns the frames of the image in the order in which they are displayed.
   *
   * @return {@link IList}&lt;{@link Image}&gt; - non-empty list of frames
   * @throws TsIllegalStateRtException resources have already been disposed {@link #isDisposed()} = true
   */
  public IList<Image> frames() {
    TsIllegalStateRtException.checkTrue( disposed );
    return frames;
  }

  /**
   * Returns delays to display Nth frame.
   * <p>
   * The Nth index in this list determines how many milliseconds th Nth frame from {@link #frames()} must be displayed
   * for animation.
   * <p>
   * Note: any value in the list is non-negative integer, that is >= 0.
   *
   * @return {@link ILongList} - delays to display Nth frame
   */
  public ILongList delays() {
    return delays;
  }

  /**
   * Returns the selected (or single) frame of the frameset.
   * <p>
   * For animated images, it makes sense as a "characteristic" image that is displayed in the mode non-animated
   * renderings.
   *
   * @return {@link Image} - the specified (or only) frame of the frameset
   * @throws TsIllegalStateRtException resources have already been disposed {@link #isDisposed()} = true
   */
  public Image image() {
    return frames().get( imageIndex );
  }

  /**
   * Returns the index of the frame used as the still (selected) image {@link #image()}.
   *
   * @return int - frame index {@link #image()} or 0 for an empty image
   */
  public int imageIndex() {
    return imageIndex;
  }

  /**
   * Determines if the image is an even animation.
   * <p>
   * An animation is considered even if all {@link #frames()} have the same size (width x height) and all delays between
   * frames {@link #delays()} have the same value. Single image is <b>not</b> considered evenly animated because it is
   * not animated at all.
   *
   * @return boolean - sign of uniform animation
   */
  public boolean isEvenAnimation() {
    return evenAnimation;
  }

  /**
   * Returns the interframe delay in milliseconds.
   * <p>
   * For non-even animation, returns the first element of the list {@link #delays()}, for single images returns 0;
   *
   * @return long - frame delay in milliseconds or 0 for non-animated images
   * @throws TsUnsupportedFeatureRtException {@link #isEvenAnimation()} = <code>false</code>
   */
  public long delay() {
    return delays().getValue( 0 );
  }

  /**
   * Releases all resources occupied by images.
   * <p>
   * If the resources have already been released ({@link #isDisposed()} = true), it does nothing.
   */
  public void dispose() {
    if( !disposed ) {
      for( int i = 0, n = frames.size(); i < n; i++ ) {
        Image img = frames.get( i );
        try {
          img.dispose();
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
      disposed = true;
    }
  }

  /**
   * Определает, были ли освобождены ресурсы многокадрового изображения.
   *
   * @return boolean - признак, что ресрусы изображения были освобождены
   */
  public boolean isDisposed() {
    return disposed;
  }

  /**
   * Returns the duration of the animation cycle.
   * <p>
   * The cycle duration is sum of all delays from {@link #delays()} list.
   *
   * @return long - animation cycle duration in milliseconds
   */
  public long cycleDuration() {
    return cycleDuration;
  }

  /**
   * Finds the minimal value in {@link #delays()}.
   *
   * @return long - minimal delay
   */
  public long minDelay() {
    return minDelay;
  }

  /**
   * Finds the maximal value in {@link #delays()}.
   *
   * @return long - maximal delay
   */
  public long maxDelay() {
    return maxDelay;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return "TsImage[" + frames.size() + "](" + imageSize.x() + "," + imageSize.y() + ")";
  }

}
