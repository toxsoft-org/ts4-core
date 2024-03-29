package org.toxsoft.core.tsgui.panels.vecboard;

import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Раскладка контролей по четырем сторонам и центру панели.
 * <p>
 * В данной раскладке можно добавлять только другие панели IBoard и SWT-контроли.
 * <p>
 * Раскладка может иметь до пяти элементов, расположенных вдоль границ или по центру (согласно перечислению
 * {@link EBorderLayoutPlacement}).
 *
 * @author hazard157
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
