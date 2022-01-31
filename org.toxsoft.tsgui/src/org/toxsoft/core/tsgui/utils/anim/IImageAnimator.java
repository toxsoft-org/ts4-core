package org.toxsoft.core.tsgui.utils.anim;

import org.toxsoft.core.tsgui.graphics.image.TsImage;

/**
 * Зарегистрованный в {@link AnimationSupport} анимированное (или обычное) изображение.
 *
 * @author goga
 */
public interface IImageAnimator
    extends IPausableAnimation {

  /**
   * Возвращает набор кадров для отображения.
   *
   * @return {@link TsImage} - набор кадров для анимированного изображения
   */
  TsImage multiImage();

  /**
   * Возвращает интерфейс для вызова пользовательской функции перерисовки при смене кадра.
   *
   * @return {@link IImageAnimationCallback} - интерфейс метода обратного вызова, не null
   */
  IImageAnimationCallback callback();

  /**
   * Возвращает ссылку, переданную в методе
   * {@link IAnimationSupport#registerImage(TsImage, IImageAnimationCallback, Object)}
   *
   * @return Object - пользовательсякая произвольная ссылка
   */
  Object userData();

}
