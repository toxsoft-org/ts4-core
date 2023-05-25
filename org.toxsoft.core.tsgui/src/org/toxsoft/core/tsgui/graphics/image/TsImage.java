package org.toxsoft.core.tsgui.graphics.image;

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

// TODO TRANSLATE

/**
 * Изображение, в общем случае состояшее из нескольких кадров наимации.
 * <p>
 * Не бывает экземпляра этого класса без хотя бы одного кадра.
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
    // предусловия
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
   * Создает {@link TsImage} с равномерной анимацией.
   *
   * @param aFrames {@link IList}&lt;{@link Image}&gt; - упорядовенный список кадров
   * @param aDelay long - межкадровая задержка в миллисекундах
   * @param aImageIndex int - индекс выделенного кдра {@link #image()}
   * @return {@link TsImage} - созданный экземпляр
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException аргумент содержит уничтноженное (disposed) изображение
   * @throws TsIllegalArgumentRtException список - пустой
   * @throws TsIllegalArgumentRtException индекс выделенного кадра выходит за заданные пределы
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
   * Создает {@link TsImage} с первым выделенным кадром.
   *
   * @param aFrames {@link IList}&lt;{@link Image}&gt; - упорядовенный список кадров
   * @param aDelays {@link ILongList} - список межкадровых задержек в миллисекундах
   * @return {@link TsImage} - созданный экземпляр
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException аргумент содержит уничтноженное (disposed) изображение
   * @throws TsIllegalArgumentRtException любой список - пустой
   * @throws TsIllegalArgumentRtException списки кадров и задержек имеют разнюю длину
   */
  public static TsImage create( IList<Image> aFrames, ILongList aDelays ) {
    return new TsImage( aFrames, aDelays, 0 );
  }

  /**
   * Создает {@link TsImage} с равномерной анимацией с первым выделенным кадром.
   *
   * @param aFrames {@link IList}&lt;{@link Image}&gt; - упорядовенный список кадров
   * @param aDelay long - межкадровая задержка в миллисекундах
   * @return {@link TsImage} - созданный экземпляр
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException аргумент содержит уничтноженное (disposed) изображение
   * @throws TsIllegalArgumentRtException список - пустой
   */
  public static TsImage create( IList<Image> aFrames, long aDelay ) {
    return create( aFrames, aDelay, 0 );
  }

  /**
   * Создает неанимированное {@link TsImage}.
   *
   * @param aImage {@link Image} - единственный кадр
   * @return {@link TsImage} - созданный экземпляр
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException аргумент содержит уничтноженное (disposed) изображение
   */
  public static TsImage create( Image aImage ) {
    return create( new SingleItemList<>( aImage ), 0L, 0 );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Определяет, состоит ли картинка из единственного кадра.
   * <p>
   * Равнозначно проверке <code>{@link #count()} == 1</code>.
   *
   * @return boolean - состоит ли картинка из единственного кадра<br>
   *         <b>true</b> - да, в картинке один кадр, и {@link #frames()}.size() = 1;<br>
   *         <b>false</b> - в картинке более одного кадра, это явно анимация.
   */
  public boolean isSingleFrame() {
    return frames.size() == 1;
  }

  /**
   * Определяет, является ли изображение анимируемым (то есть, количество кадров больше 1).
   * <p>
   * Равнозначно проверке <code>{@link #count()} > 1</code>.
   *
   * @return boolean - признак анимируемого изображения
   */
  public boolean isAnimated() {
    return frames.size() > 1;
  }

  /**
   * Возвращает количество кадров.
   *
   * @return int - количество кадров, всегда > 0
   */
  public int count() {
    return frames.size();
  }

  /**
   * Возвращаеть размер первого изображения.
   * <p>
   * Если список кадров пустой, возвращает {@link ITsPoint#ZERO}.
   *
   * @return {@link ITsPoint} - размер изображения в пикселях или {@link ITsPoint#ZERO} для пустого изображения
   */
  public ITsPoint imageSize() {
    return imageSize;
  }

  /**
   * Возвращает кадры анимированного изображения в порядке их отображения.
   *
   * @return Images[] - непустой массив изображении - кадров анимации
   * @throws TsIllegalStateRtException ресурсы уже были освобождены {@link #isDisposed()} = true
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
   * Возвращает заданный (или единственный) кадр набора кадров.
   * <p>
   * Для анимированных изображении имеет смысл как "характерное" изображение, которое отображается в режиме
   * не-анимированной визуализации.
   *
   * @return {@link Image} - заданный (или единственный) кадр набора кадров, для пустого изображения возвращает null
   * @throws TsIllegalStateRtException ресурсы уже были освобождены {@link #isDisposed()} = true
   */
  public Image image() {
    return frames().get( imageIndex );
  }

  /**
   * Возвращает индекс кадра, используемого как неподвижное изображение {@link #image()}.
   *
   * @return int - индекс кадра {@link #image()} или 0 для пустого изобрадения
   */
  public int imageIndex() {
    return imageIndex;
  }

  /**
   * Определяет, является ли изображение равномерной анимацией.
   * <p>
   * Равномерной считается анимация, все кадры {@link #frames()} которого имеют одинаковый размер (ширина x высота) и
   * все задержки между кадрами {@link #delays()}имеют одинаковое значение. Единичное и пустое изображение <b>не</b>
   * считается равномерно анимированным, посколько оно вообще не анимировано.
   *
   * @return boolean - признак равномерной анимации
   */
  public boolean isEvenAnimation() {
    return evenAnimation;
  }

  /**
   * Возвращает межкадровую задержку в миллисекундах.
   * <p>
   * Для неравномерной анимации возвращает первый элемент списка {@link #delays()}, для пустого или единичного
   * изображения возвращает 0;
   *
   * @return long - межкадровая задержка в миллисекундах или 0 для неанимированных изображений
   * @throws TsUnsupportedFeatureRtException {@link #isEvenAnimation()} = <code>false</code>
   */
  public long delay() {
    return delays().getValue( 0 );
  }

  /**
   * Освобождает все ресурсы, занятые изображениями.
   * <p>
   * Есди ресурсы уже были освобождены ({@link #isDisposed()} = true), то ничего не делает.
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
