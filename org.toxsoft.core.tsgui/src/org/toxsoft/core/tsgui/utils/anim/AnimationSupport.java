package org.toxsoft.core.tsgui.utils.anim;

import org.eclipse.swt.widgets.Display;
import org.toxsoft.core.tsgui.graphics.image.TsImage;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

/**
 * Поддержка использования анимированных изображения (GIF, MNG).
 *
 * @author goga
 */
public class AnimationSupport
    implements IAnimationSupport, Runnable {

  /**
   * Минимальная дискретность расчета задержек между кадрами (в миллисекундах).
   */
  public static final long MIN_GRANULARITY = 5;

  /**
   * Мксимальная дискретность расчета задержек между кадрами (в миллисекундах).
   */
  public static final long MAX_GRANULARITY = 750;

  /**
   * Дискретность расчета задержек между кадрами (в миллисекундах) по умолчанию.
   */
  public static final long DEFAULT_GRANULARITY = 10;

  static class CallbackCaller
      implements Runnable {

    private final IList<ImageAnimator>      imageAnimators;
    private final IList<BlinkerAnimator>    blinkerAnimators;
    private final IList<GeneralAnimator<?>> generalAnimators;

    CallbackCaller( IList<ImageAnimator> aImageAnimators, IList<BlinkerAnimator> aBlinkerAnimators,
        IList<GeneralAnimator<?>> aGeneralAnimators ) {
      imageAnimators = aImageAnimators;
      blinkerAnimators = aBlinkerAnimators;
      generalAnimators = aGeneralAnimators;
    }

    @SuppressWarnings( { "rawtypes" } )
    @Override
    public void run() {
      long currTimestamp = System.currentTimeMillis();
      // отрисуем активные аниматоры мигающих объектов
      for( int i = 0, count = blinkerAnimators.size(); i < count; i++ ) {
        BlinkerAnimator item = blinkerAnimators.get( i );
        try {
          item.callback().onDrawBlink( item, item.nextState(), item.userData() );
        }
        catch( Exception e ) {
          e.printStackTrace();
          LoggerUtils.errorLogger().error( e );
        }
        item.setLastDrawTimespamp( currTimestamp );
      }
      // отрисуем активные аниматоры изображений
      for( int i = 0, count = imageAnimators.size(); i < count; i++ ) {
        ImageAnimator item = imageAnimators.get( i );
        if( !item.multiImage().isDisposed() ) {
          try {
            item.callback().onDrawFrame( item, item.nextIndex(), item.userData() );
          }
          catch( Exception e ) {
            e.printStackTrace();
            LoggerUtils.errorLogger().error( e );
          }
          item.setLastDrawTimespamp( currTimestamp );
        }
      }
      // отрисуем активные аниматоры общего назначения
      for( int i = 0, count = generalAnimators.size(); i < count; i++ ) {
        GeneralAnimator item = generalAnimators.get( i );
        try {
          if( item.callback().onNextStep( item, item.nextCounter(), item.userData() ) ) {
            item.resetCounter();
          }
        }
        catch( Exception e ) {
          e.printStackTrace();
          LoggerUtils.errorLogger().error( e );
        }
        item.setLastCallTimestamp( currTimestamp );
      }
    }
  }

  private final long PAUSE_WAITING_CYCLE_SLEEP_MSEC = 10;
  private final int  pauseWaitingCyclesCount;

  private final Display display;
  private final long    granularity;

  final IListEdit<ImageAnimator>      imageAnimators   = new ElemArrayList<>();
  final IListEdit<BlinkerAnimator>    blinkerAnimators = new ElemArrayList<>();
  final IListEdit<GeneralAnimator<?>> generalAnimators = new ElemArrayList<>();

  private volatile boolean queryPause    = false;
  private volatile Thread  drawingThread = null;

  /**
   * Создает аниматор с заданной дискретностью расчета задержек в приостановленном состоянии.
   *
   * @param aDisplay {@link Display} - дисплей, на которой будет происходить отрисовка
   * @param aGranularity long - дискретность расчета задержек между кадрами (в миллисекундах)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException аргумент выходит за допустимые пределы
   */
  public AnimationSupport( Display aDisplay, long aGranularity ) {
    display = TsNullArgumentRtException.checkNull( aDisplay );
    TsIllegalArgumentRtException.checkTrue( aGranularity < MIN_GRANULARITY || aGranularity > MAX_GRANULARITY );
    granularity = aGranularity;
    pauseWaitingCyclesCount = (int)(granularity / PAUSE_WAITING_CYCLE_SLEEP_MSEC) + 2;
    display.disposeExec( new Runnable() {

      @Override
      public void run() {
        dispose();
      }
    } );
  }

  /**
   * Создает аниматор с дискретностью по умолчанию {@link #DEFAULT_GRANULARITY}.
   *
   * @param aDisplay {@link Display} - дисплей, на которой будет происходить отрисовка
   * @throws TsIllegalArgumentRtException аргумент выходит за допустимые пределы
   */
  public AnimationSupport( Display aDisplay ) {
    this( aDisplay, DEFAULT_GRANULARITY );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  /**
   * Выбирает из {@link #imageAnimators} и возвращает новый список активных аниматоров изображений.
   * <p>
   * Активными (то есть, требущими перерисовки), считаются аниматоры, которые:
   * <ul>
   * <li>количество кадров больше одного - иначе не имеет смысла перерысовывать;</li>
   * <li>не приостановлены (то есть, {@link IImageAnimator#isPaused()} = false;</li>
   * <li>с предыдущей перерисовки прошло больше времени, чем задеркжа отображения предыдущего кадра.</li>
   * </ul>
   *
   * @param aCurrTimestamp long - текущее время (нужно чтобы не вызывать {@link System#currentTimeMillis()})
   * @return ILis&lt;{@link ImageAnimator}&gt; - вновь созданный список активных аниматоров изображений
   */
  private IList<ImageAnimator> listAcitiveImageAnimators( long aCurrTimestamp ) {
    IListEdit<ImageAnimator> animators = new ElemArrayList<>( 32 );
    synchronized (imageAnimators) {
      for( int i = 0, n = imageAnimators.size(); i < n; i++ ) {
        ImageAnimator ia = imageAnimators.get( i );
        if( !ia.isPaused() && !ia.multiImage().isDisposed() ) {
          long delay = ia.multiImage().delays().getValue( ia.getIndex() );
          if( aCurrTimestamp - ia.lastDrawTimespamp() >= delay ) {
            animators.add( ia );
          }
        }
      }
    }
    return animators;
  }

  /**
   * Выбирает из {@link #blinkerAnimators} и возвращает новый список активных аниматоров мигания.
   * <p>
   * Активными (то есть, требущими перерисовки), считаются аниматоры, которые:
   * <ul>
   * <li>не приостановлены (то есть, {@link IBlinkerAnimator#isPaused()} = false;</li>
   * <li>с предыдущей перерисовки прошло больше времени, чем интервали смены состояния мигания.</li>
   * </ul>
   *
   * @param aCurrTimestamp long - текущее время (нужно чтобы не вызывать {@link System#currentTimeMillis()})
   * @return ILis&lt;{@link ImageAnimator}&gt; - вновь созданный список активных аниматоров мигания
   */
  private IList<BlinkerAnimator> listActiveBlinkAnimators( long aCurrTimestamp ) {
    IListEdit<BlinkerAnimator> animators = new ElemArrayList<>( 32 );
    synchronized (blinkerAnimators) {
      for( int i = 0, n = blinkerAnimators.size(); i < n; i++ ) {
        BlinkerAnimator ia = blinkerAnimators.get( i );
        if( !ia.isPaused() ) {
          if( aCurrTimestamp - ia.lastDrawTimespamp() >= ia.interval() ) {
            animators.add( ia );
          }
        }
      }
    }
    return animators;
  }

  /**
   * Выбирает из {@link #generalAnimators} и возвращает новый список активных аниматоров.
   * <p>
   * Активными (то есть, требущими перерисовки), считаются аниматоры, которые:
   * <ul>
   * <li>не приостановлены (то есть, {@link IPausableAnimation#isPaused()} = false;</li>
   * <li>с предыдущей перерисовки прошло больше времени, чем интервали смены состояния мигания.</li>
   * </ul>
   *
   * @param aCurrTimestamp long - текущее время (нужно чтобы не вызывать {@link System#currentTimeMillis()})
   * @return ILis&lt;{@link GeneralAnimator}&gt; - вновь созданный список активных аниматоров
   */
  private IList<GeneralAnimator<?>> listActiveGaneralAnimators( long aCurrTimestamp ) {
    IListEdit<GeneralAnimator<?>> animators = new ElemArrayList<>( 32 );
    synchronized (generalAnimators) {
      for( int i = 0, n = generalAnimators.size(); i < n; i++ ) {
        GeneralAnimator<?> ia = generalAnimators.get( i );
        if( !ia.isPaused() ) {
          if( aCurrTimestamp - ia.lastCallTimestamp() >= ia.interval() ) {
            animators.add( ia );
          }
        }
      }
    }
    return animators;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Runnable
  //

  @Override
  public void run() {
    while( !queryPause ) {
      long currTime = System.currentTimeMillis();
      final IList<ImageAnimator> iaList = listAcitiveImageAnimators( currTime );
      final IList<BlinkerAnimator> baList = listActiveBlinkAnimators( currTime );
      final IList<GeneralAnimator<?>> gaList = listActiveGaneralAnimators( currTime );
      if( !display.isDisposed() ) {
        display.asyncExec( new CallbackCaller( iaList, baList, gaList ) );
      }
      try {
        Thread.sleep( granularity );
      }
      catch( InterruptedException e ) {
        e.printStackTrace();
        break; // немедленно переходим в состоянии паузы
      }
    }
    drawingThread = null;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IAnimationSupport
  //

  @Override
  public Display display() {
    return display;
  }

  @Override
  public IImageAnimator registerImage( TsImage aTsImage, IImageAnimationCallback aCallback, Object aUserData ) {
    ImageAnimator ia = new ImageAnimator( aTsImage, aCallback, aUserData );
    synchronized (imageAnimators) {
      imageAnimators.add( ia );
    }
    return ia;
  }

  @Override
  public void unregister( IImageAnimator aImageAnimator ) {
    TsNullArgumentRtException.checkNull( aImageAnimator );
    TsIllegalArgumentRtException.checkFalse( aImageAnimator instanceof ImageAnimator );
    synchronized (imageAnimators) {
      imageAnimators.remove( (ImageAnimator)aImageAnimator );
    }
  }

  @Override
  public IBlinkerAnimator registerBlinker( long aInterval, IBlinkerAnimationCallback aCallback, Object aUserData ) {
    BlinkerAnimator ba = new BlinkerAnimator( aInterval, aCallback, aUserData );
    synchronized (blinkerAnimators) {
      blinkerAnimators.add( ba );
    }
    return ba;
  }

  @Override
  public void unregister( IBlinkerAnimator aBlinkerAnimator ) {
    TsNullArgumentRtException.checkNull( aBlinkerAnimator );
    TsIllegalArgumentRtException.checkFalse( aBlinkerAnimator instanceof BlinkerAnimator );
    synchronized (blinkerAnimators) {
      blinkerAnimators.remove( (BlinkerAnimator)aBlinkerAnimator );
    }
  }

  @Override
  public <T> IGeneralAnimator<T> registerGeneral( long aInterval, IGeneralAnimationCallback<T> aCallback,
      T aUserData ) {
    GeneralAnimator<T> ga = new GeneralAnimator<>( aInterval, aCallback, aUserData );
    synchronized (generalAnimators) {
      generalAnimators.add( ga );
    }
    return ga;
  }

  @Override
  public void unregister( IGeneralAnimator<?> aGeneralAnimator ) {
    TsNullArgumentRtException.checkNull( aGeneralAnimator );
    TsIllegalArgumentRtException.checkFalse( aGeneralAnimator instanceof GeneralAnimator );
    synchronized (generalAnimators) {
      generalAnimators.remove( (GeneralAnimator<?>)aGeneralAnimator );
    }
  }

  @Override
  public void clear() {
    synchronized (imageAnimators) {
      imageAnimators.clear();
    }
    synchronized (blinkerAnimators) {
      blinkerAnimators.clear();
    }
    synchronized (generalAnimators) {
      generalAnimators.clear();
    }
  }

  @Override
  public void dispose() {
    if( !isPaused() ) {
      pause();
    }
    clear();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IPausable
  //

  @Override
  public void pause() {
    if( drawingThread == null ) {
      return;
    }
    int count = 0;
    // запросим остановку потока
    queryPause = true;
    // и подождем немного...
    while( drawingThread != null ) {
      try {
        Thread.sleep( PAUSE_WAITING_CYCLE_SLEEP_MSEC );
      }
      catch( InterruptedException ex ) {
        LoggerUtils.errorLogger().warning( ex, TsLibUtils.EMPTY_STRING );
        break; // сразу выходим
      }
      if( ++count > pauseWaitingCyclesCount ) {
        break;
      }
    }
  }

  @Override
  public void resume() {
    if( drawingThread != null ) {
      return;
    }
    drawingThread = new Thread( this );
    drawingThread.start();
  }

  @Override
  public boolean isPaused() {
    return drawingThread == null;
  }

}
