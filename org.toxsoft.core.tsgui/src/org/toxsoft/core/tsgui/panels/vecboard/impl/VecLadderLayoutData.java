package org.toxsoft.core.tsgui.panels.vecboard.impl;

import org.toxsoft.core.tsgui.graphics.EHorAlignment;
import org.toxsoft.core.tsgui.graphics.EVerAlignment;
import org.toxsoft.core.tsgui.panels.vecboard.IVecLadderLayoutData;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Неизменяемая реализация {@link IVecLadderLayoutData}.
 *
 * @author hazard157
 */
public class VecLadderLayoutData
    implements IVecLadderLayoutData {

  private final boolean       isLabelShown;
  private final boolean       isFullWidthControl;
  private final int           verticalSpan;
  private final String        labelText;
  private final String        tooltip;
  private final EHorAlignment horAlignment;
  private final EVerAlignment verAlignment;

  /**
   * Создает экземпляр со всеми инвариантами.
   *
   * @param aIsLabelShown boolean - принак показа подписи к элементу
   * @param aIsFullWidthControl boolean - признак того, что контроль занимает всю ширину панели
   * @param aVertSpan int - количество строк (ступенек лесенки), занимаемый элементом раскладки
   * @param aLabelText String - текст подписи к SWT-контролю
   * @param aHorAlignment {@link EHorAlignment} - выравнивание контролей по горизонтали внутри ячейки
   * @param aVerAlignment {@link EVerAlignment} - выравнивание контролей по вертикали внутри ячейки
   * @throws TsNullArgumentRtException аргумент = null
   */
  public VecLadderLayoutData( boolean aIsLabelShown, boolean aIsFullWidthControl, int aVertSpan, String aLabelText,
      EHorAlignment aHorAlignment, EVerAlignment aVerAlignment ) {
    this( aIsLabelShown, aIsFullWidthControl, aVertSpan, aLabelText, TsLibUtils.EMPTY_STRING, aHorAlignment,
        aVerAlignment );
  }

  /**
   * Создает экземпляр со всеми инвариантами.
   *
   * @param aIsLabelShown boolean - принак показа подписи к элементу
   * @param aIsFullWidthControl boolean - признак того, что контроль занимает всю ширину панели
   * @param aVertSpan int - количество строк (ступенек лесенки), занимаемый элементом раскладки
   * @param aLabelText String - текст подписи к SWT-контролю
   * @param aTooltip String - текст выплывающей подсказки
   * @param aHorAlignment {@link EHorAlignment} - выравнивание контролей по горизонтали внутри ячейки
   * @param aVerAlignment {@link EVerAlignment} - выравнивание контролей по вертикали внутри ячейки
   * @throws TsNullArgumentRtException аргумент = null
   */
  public VecLadderLayoutData( boolean aIsLabelShown, boolean aIsFullWidthControl, int aVertSpan, String aLabelText,
      String aTooltip, EHorAlignment aHorAlignment, EVerAlignment aVerAlignment ) {
    TsNullArgumentRtException.checkNulls( aLabelText, aTooltip, aHorAlignment, aVerAlignment );
    isLabelShown = aIsLabelShown;
    isFullWidthControl = aIsFullWidthControl;
    verticalSpan = aVertSpan;
    labelText = aLabelText;
    tooltip = aTooltip;
    horAlignment = aHorAlignment;
    verAlignment = aVerAlignment;
  }

  // ------------------------------------------------------------------------------------
  // ILadderLayoutData
  //

  @Override
  public boolean isLabelShown() {
    return isLabelShown;
  }

  @Override
  public boolean isFullWidthControl() {
    return isFullWidthControl;
  }

  @Override
  public int verticalSpan() {
    return verticalSpan;
  }

  @Override
  public String labelText() {
    return labelText;
  }

  @Override
  public String tooltip() {
    return tooltip;
  }

  @Override
  public EHorAlignment horAlignment() {
    return horAlignment;
  }

  @Override
  public EVerAlignment verAlignment() {
    return verAlignment;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return '\'' + labelText + '\'' + '\n' + //
        "  span=" + verticalSpan + '\n' + //$NON-NLS-1$
        "  fullWith=" + isFullWidthControl + '\n' + //$NON-NLS-1$
        "  labelShown=" + isLabelShown + '\n' + //$NON-NLS-1$
        "  horAlign=" + horAlignment + '\n' + //$NON-NLS-1$
        "  verAlign=" + verAlignment; //$NON-NLS-1$
  }

}
