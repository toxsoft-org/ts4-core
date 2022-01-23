package org.toxsoft.tsgui.utils.anim;

import java.io.File;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Display;
import org.toxsoft.tsgui.graphics.image.TsImage;
import org.toxsoft.tsgui.graphics.image.TsImageUtils;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

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
 * @author goga
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
   * Регистрирует аниматор общего назначения.
   * <p>
   * Анимация создается в приостановленном состоянии. Для включения анимации, следует вызвать
   * {@link IGeneralAnimator#resume()} от возвращаемого значения.
   *
   * @param <T> - тип пользовательских данных
   * @param aInterval long - интервал анимации (миллисекунды между вызовами aCallback)
   * @param aCallback {@link IGeneralAnimationCallback} - периодический вызваемый метод анимации
   * @param aUserData &lt;T&gt; - пользовательские данные, может быть null
   * @return {@link IGeneralAnimator} - зарегистрированный аниматор
   * @throws TsIllegalArgumentRtException aInterval выходит за допустимые пределы
   * @throws TsNullArgumentRtException aCallback = null
   */
  <T> IGeneralAnimator<T> registerGeneral( long aInterval, IGeneralAnimationCallback<T> aCallback, T aUserData );

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
}
