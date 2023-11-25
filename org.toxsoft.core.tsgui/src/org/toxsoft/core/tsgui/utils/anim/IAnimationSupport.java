package org.toxsoft.core.tsgui.utils.anim;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Класс для осуществления поддержки анимированных изображении и мигающих объектов.
 * <p>
 * Суть поддержки заключается в том, что регистрируется объект для анимации, а потом у зарегистрированного объекта из
 * основного (GUI) потока выполнения вызывается метод для осуществления перерисовки объекта.
 * <p>
 * Можно зарегистрировать два типа анимированных объектов:
 * <ul>
 * <li>анимированное растровое изображение {@link TsImage} методом
 * {@link #registerImage(TsImage, IImageAnimationCallback, Object)}. Например, можно загрузить анимированное GIF
 * изображение {@link TsImageUtils#loadTsImage(File, Device)} и потом анимировать его;</li>
 * <li>объет, который должен мигать (то есть, менять свое булево состояние при каждой перерисовке) методом
 * {@link #registerBlinker(long, IBlinkerAnimationCallback, Object)}. При вызове
 * {@link IBlinkerAnimationCallback#onDrawBlink(IBlinkerAnimator, boolean, Object)} надо рисовать объект в соответствии
 * с переданным в аргументе aState состоянии.</li>
 * </ul>
 * А что делать, если нужно перебирать не два булевых состояния, а больше? В таком случае, все равно надо пользоваться
 * аниматором миганния, только аргумент aState можно игнорировать, а перебор состояния огранизовать в самом рисуемом
 * объекте.
 *
 * @author hazard157
 */
public interface IAnimationSupport
    extends IPausableAnimation {

  /**
   * Returns display for which the animation is supported.
   *
   * @return {@link Display} - the display
   */
  Display display();

  /**
   * Регистрирует многокадровое изображение для анимации.
   * <p>
   * Анимация создается в работающем состоянии. Для выключения анимации, следует вызвать {@link IImageAnimator#pause()}
   * от возвращаемого значения.
   * <p>
   * Если в мульти-изображении только один кадр, то аниматор <b>не будет</b> вызывать методы перерисовки, считая, что
   * это не надо.
   *
   * @param aTsImage {@link TsImage} - набор кадров анимированного изображения
   * @param aCallback {@link IImageAnimationCallback} - пользовательский метод отрисовки очередного изрображения
   * @param aUserData Object - произвольная ссылка, которая обратно возвращается в методе
   *          {@link IImageAnimationCallback#onDrawFrame(IImageAnimator, int, Object)}
   * @return {@link IImageAnimator} - зарегистрированный аниматор изображений
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  IImageAnimator registerImage( TsImage aTsImage, IImageAnimationCallback aCallback, Object aUserData );

  /**
   * Удаляет ранее зарегистрированный аниматор изображения.
   * <p>
   * Если такой аниматор не зарегистрирован, то ничего не делает.
   *
   * @param aImageAnimator {@link IImageAnimator} - удалемый аниматор
   * @throws TsNullArgumentRtException аргумент = null
   */
  void unregister( IImageAnimator aImageAnimator );

  /**
   * Регистрирует мигающий объект для анимации.
   * <p>
   * Анимация создается в приостановленном состоянии. Для включения анимации, следует вызвать
   * {@link IBlinkerAnimator#resume()} от возвращаемого значения.
   *
   * @param aInterval long - интервал между сменами состояния мигания в миллисекундах
   * @param aCallback {@link IImageAnimationCallback} - пользовательский метод отрисовки очередного изрображения
   * @param aUserData Object - произвольная ссылка, которая обратно возвращается в методе
   *          {@link IBlinkerAnimationCallback#onDrawBlink(IBlinkerAnimator, boolean, Object)}
   * @return {@link IImageAnimator} - зарегистрированный аниматор изображений
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  IBlinkerAnimator registerBlinker( long aInterval, IBlinkerAnimationCallback aCallback, Object aUserData );

  /**
   * Удаляет ранее зарегистрированный аниматор мигающего объекта.
   * <p>
   * Если такой аниматор не зарегистрирован, то ничего не делает.
   *
   * @param aBlinkerAnimator {@link IBlinkerAnimator} - удалемый аниматор
   * @throws TsNullArgumentRtException аргумент = null
   */
  void unregister( IBlinkerAnimator aBlinkerAnimator );

  /**
   * Register the general purpose animator.
   * <p>
   * Animation is created in the suspended state, call {@link IGeneralAnimator#resume()} to turn on animation.
   *
   * @param <T> - user data type
   * @param aInterval long - animation callback call period in milliseconds
   * @param aInitialCounter long - initial value of the internal counter
   * @param aCallback {@link IGeneralAnimationCallback} - callback interface
   * @param aUserData &lt;T&gt; - arbitrary user-specified data or <code>null</code>
   * @return {@link IGeneralAnimator} - create instance of the animator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  <T> IGeneralAnimator<T> registerGeneral( long aInterval, long aInitialCounter, IGeneralAnimationCallback<T> aCallback,
      T aUserData );

  /**
   * Удаляет ранее зарегистрированный аниматор.
   * <p>
   * Если такой аниматор не зарегистрирован, то ничего не делает.
   *
   * @param aGeneralAnimator {@link IGeneralAnimator} - удалемый аниматор
   * @throws TsNullArgumentRtException аргумент = null
   */
  void unregister( IGeneralAnimator<?> aGeneralAnimator );

  /**
   * Удалеят все зарегистрированные аниматоры - как изображений, так и мигающих объектов.
   */
  void clear();

  /**
   * Завершает работу - дерегистрирует все аниматоры, останавливает анимацию, осовобождает все ресурсы.
   */
  void dispose();

  // ------------------------------------------------------------------------------------
  // Inline methods for convenience
  //

  @SuppressWarnings( "javadoc" )
  default <T> IGeneralAnimator<T> registerGeneral( long aInterval, IGeneralAnimationCallback<T> aCallback,
      T aUserData ) {
    return registerGeneral( aInterval, 0L, aCallback, aUserData );
  }

}
