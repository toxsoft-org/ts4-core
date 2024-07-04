package org.toxsoft.core.tsgui.utils.anim;

import org.toxsoft.core.tsgui.graphics.image.impl.*;

/**
 * Обратный вызов аниматором {@link AnimationSupport} для перерисовки очередного кадра анимации.
 *
 * @author hazard157
 */
public interface IImageAnimationCallback {

  /**
   * Вызывается когда настала пора нарисовать очередной кадр анимации.
   * <p>
   * Изображение, которе должно быть нарисовано получается по идексу aIndex из массива изображении
   * {@link TsImage#frames()}.
   * <p>
   * Этот метод вызывается из UI-потока выполнения, что позволяет рисовать прямо из тела метода.
   *
   * @param aImageAnimator {@link IImageAnimator} - вся информация об анимированном изображении
   * @param aIndex int - индекс изображения кадра, который должен быть нарисован
   * @param aUserData Object - произвольная ссылка, ранее переданная в
   *          {@link IAnimationSupport#registerImage(TsImage, IImageAnimationCallback, Object)}
   */
  void onDrawFrame( IImageAnimator aImageAnimator, int aIndex, Object aUserData );

}
