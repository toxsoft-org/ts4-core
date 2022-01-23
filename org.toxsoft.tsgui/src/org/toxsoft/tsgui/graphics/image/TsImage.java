package org.toxsoft.tsgui.graphics.image;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.toxsoft.tslib.bricks.geometry.ITsPoint;
import org.toxsoft.tslib.bricks.geometry.impl.TsPoint;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.coll.primtypes.ILongList;
import org.toxsoft.tslib.coll.primtypes.impl.LongArrayList;
import org.toxsoft.tslib.coll.primtypes.wrappers.LongArrayWrapper;
import org.toxsoft.tslib.coll.wrappers.ElemArrayWrapper;
import org.toxsoft.tslib.utils.errors.*;
import org.toxsoft.tslib.utils.logs.impl.LoggerUtils;

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
  private final int          imageIndex;

  private ITsPoint imageSize;
  private boolean  evenAnimation;

  private boolean disposed = false;

  // ------------------------------------------------------------------------------------
  // Создание экземпляров
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
   */
  public TsImage( Image[] aFrames, long[] aDelays, int aImageIndex ) {
    TsErrorUtils.checkArrayArg( aFrames, 1 );
    TsNullArgumentRtException.checkNull( aDelays );
    TsIllegalArgumentRtException.checkTrue( aFrames.length != aDelays.length );
    frames = new ElemArrayWrapper<>( aFrames );
    delays = new LongArrayWrapper( aDelays );
    imageIndex = aImageIndex;
    internalInit();
  }

  private void internalInit() {
    // изображения должны быть валидными
    for( int i = 0, n = frames.size(); i < n; i++ ) {
      Image img = frames.get( i );
      TsIllegalArgumentRtException.checkTrue( img.isDisposed() );
    }
    // инициализация внутренностей
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

  // /**
  // * Создает {@link TsImage}.
  // *
  // * @param aFrames {@link IList}&lt;{@link Image}&gt; - упорядовенный список кадров
  // * @param aDelays {@link ILongList} - список межкадровых задержек в миллисекундах
  // * @param aImageIndex int - индекс выделенного кадра {@link #image()}
  // * @return {@link TsImage} - созданный экземпляр
  // * @throws TsNullArgumentRtException любой аргумент = null
  // * @throws TsIllegalArgumentRtException аргумент содержит уничтноженное (disposed) изображение
  // * @throws TsIllegalArgumentRtException любой список - пустой
  // * @throws TsIllegalArgumentRtException списки кадров и задержек имеют разнюю длину
  // * @throws TsIllegalArgumentRtException индекс выделенного кадра выходит за заданные пределы
  // */
  // public static TsImage create( IList<Image> aFrames, ILongList aDelays, int aImageIndex ) {
  // return new TsImage( aFrames, aDelays, aImageIndex );
  // }
  //
  // /**
  // * Создает {@link TsImage} с равномерной анимацией.
  // *
  // * @param aFrames {@link IList}&lt;{@link Image}&gt; - упорядовенный список кадров
  // * @param aDelay long - межкадровая задержка в миллисекундах
  // * @param aImageIndex int - индекс выделенного кдра {@link #image()}
  // * @return {@link TsImage} - созданный экземпляр
  // * @throws TsNullArgumentRtException любой аргумент = null
  // * @throws TsIllegalArgumentRtException аргумент содержит уничтноженное (disposed) изображение
  // * @throws TsIllegalArgumentRtException список - пустой
  // * @throws TsIllegalArgumentRtException индекс выделенного кадра выходит за заданные пределы
  // */
  // public static TsImage create( IList<Image> aFrames, long aDelay, int aImageIndex ) {
  // TsNullArgumentRtException.checkNull( aFrames );
  // TsIllegalArgumentRtException.checkTrue( aFrames.isEmpty() );
  // LongArrayList delays = new LongArrayList();
  // for( int i = 0; i < aFrames.size(); i++ ) {
  // delays.add( aDelay );
  // }
  // return new TsImage( aFrames, delays, aImageIndex );
  // }
  //
  // /**
  // * Создает {@link TsImage} с первым выделенным кадром.
  // *
  // * @param aFrames {@link IList}&lt;{@link Image}&gt; - упорядовенный список кадров
  // * @param aDelays {@link ILongList} - список межкадровых задержек в миллисекундах
  // * @return {@link TsImage} - созданный экземпляр
  // * @throws TsNullArgumentRtException любой аргумент = null
  // * @throws TsIllegalArgumentRtException аргумент содержит уничтноженное (disposed) изображение
  // * @throws TsIllegalArgumentRtException любой список - пустой
  // * @throws TsIllegalArgumentRtException списки кадров и задержек имеют разнюю длину
  // */
  // public static TsImage create( IList<Image> aFrames, ILongList aDelays ) {
  // return create( aFrames, aDelays, 0 );
  // }
  //
  // /**
  // * Создает {@link TsImage} с равномерной анимацией с первым выделенным кадром.
  // *
  // * @param aFrames {@link IList}&lt;{@link Image}&gt; - упорядовенный список кадров
  // * @param aDelay long - межкадровая задержка в миллисекундах
  // * @return {@link TsImage} - созданный экземпляр
  // * @throws TsNullArgumentRtException любой аргумент = null
  // * @throws TsIllegalArgumentRtException аргумент содержит уничтноженное (disposed) изображение
  // * @throws TsIllegalArgumentRtException список - пустой
  // */
  // public static TsImage create( IList<Image> aFrames, long aDelay ) {
  // return create( aFrames, aDelay, 0 );
  // }
  //
  // /**
  // * Создает неанимированное {@link TsImage}.
  // *
  // * @param aImage {@link Image} - единственный кадр
  // * @return {@link TsImage} - созданный экземпляр
  // * @throws TsNullArgumentRtException любой аргумент = null
  // * @throws TsIllegalArgumentRtException аргумент содержит уничтноженное (disposed) изображение
  // */
  // public static TsImage create( Image aImage ) {
  // return create( new SingleItemList<>( aImage ), 0L, 0 );
  // }

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
   * Возвращает значение задержки после отображания очередного кадра и до отображения следующего кадра.
   * <p>
   * Элменты этого массива соответствуют задержке после отображения кадра из {@link #frames()} с соответствующим
   * индексом.
   *
   * @return long[] - значение задержек омежду кадрами в милисекундах
   * @throws TsIllegalStateRtException ресурсы уже были освобождены {@link #isDisposed()} = true
   */
  public ILongList delays() {
    TsIllegalStateRtException.checkTrue( disposed );
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

  // ------------------------------------------------------------------------------------
  // Реализация методов Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return "TsImage[" + frames.size() + "](" + imageSize.x() + "," + imageSize.y() + ")";
  }

}
