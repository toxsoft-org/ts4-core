package org.toxsoft.tsgui.panels.vecboard;

import org.toxsoft.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.tsgui.utils.layout.EBorderLayoutPlacement;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Раскладка контролей по четырем сторонам и центру панели.
 * <p>
 * В данной раскладке можно добавлять только другие панели IBoard и SWT-контроли.
 * <p>
 * Раскладка может иметь до пяти элементов, расположенных вдоль границ или по центру (согласно перечислению
 * {@link EBorderLayoutPlacement}).
 *
 * @author goga
 */
public interface IVecBorderLayout
    extends IVecLayout<EBorderLayoutPlacement> {

  /**
   * Возвращает элемент в указанном месте раскладки.
   *
   * @param aPlacement {@link EBorderLayoutPlacement} - место в раскладке
   * @return {@link ILazyControl} - элемент раскладки или null, если нет элемента в этом месте
   * @throws TsNullArgumentRtException аргумент = null
   */
  ILazyControl<?> findItem( EBorderLayoutPlacement aPlacement );

}
