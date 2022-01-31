package org.toxsoft.core.tsgui.panels.vecboard.impl;

import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.panels.vecboard.IVecTabLayoutData;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Неизменяемая реализация {@link IVecTabLayoutData}.
 *
 * @author goga
 */
public class VecTabLayoutData
    implements IVecTabLayoutData {

  private final String    name;
  private final String    tooltipText;
  private final String    iconId;
  private final EIconSize iconSize;

  /**
   * Создает объект со всеми инвариантами.
   *
   * @param aName String - название, отображаемое на ярлыке вкладки
   * @param aTooltipText String - всплывающая подсказка вкладки
   * @param aIconId String - имя значка или пустая строка, при отсутствии значка у вкладки
   * @param aIconSize {@link EIconSize} - размер значка
   * @throws TsNullArgumentRtException aName или aTooltipText = null
   */
  public VecTabLayoutData( String aName, String aTooltipText, String aIconId, EIconSize aIconSize ) {
    TsNullArgumentRtException.checkNulls( aName, aTooltipText, aIconId, aIconSize );
    name = aName;
    tooltipText = aTooltipText;
    iconId = aIconId;
    iconSize = aIconSize;
  }

  /**
   * Создает описание вкладки без значка.
   * <p>
   * Равнозначно вызову {@link VecTabLayoutData#VecTabLayoutData(String, String, String, EIconSize)
   * VecTabLayoutData(aName, aTooltipText, <b>""</b>, <b>EIconSize.IS_16X16</b>)}
   *
   * @param aName String - название, отображаемое на ярлыке вкладки
   * @param aTooltipText String - всплывающая подсказка вкладки
   * @throws TsNullArgumentRtException aName или aTooltipText = null
   */
  public VecTabLayoutData( String aName, String aTooltipText ) {
    this( aName, aTooltipText, TsLibUtils.EMPTY_STRING, EIconSize.IS_16X16 );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IAopTabLayoutData
  //

  @Override
  public String name() {
    return name;
  }

  @Override
  public String tooltipText() {
    return tooltipText;
  }

  @Override
  public String iconId() {
    return iconId;
  }

  @Override
  public EIconSize iconSize() {
    return iconSize;
  }

}
