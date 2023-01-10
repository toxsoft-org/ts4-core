package org.toxsoft.core.tsgui.panels.vecboard.impl;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.vecboard.*;

/**
 * Неизменяемая реализация раскладки контролей по колонкам.
 * <p>
 *
 * @author vs
 */
public class VecColumnLayout
    extends AbstractVecLayout<IVecColumnLayoutData>
    implements IVecColumnLayout {

  private final int colCount;
  private final int horSpace;
  private final int vertSpace;

  private final boolean equalWidth;

  private final IMargins margins;

  /**
   * Создает раскладку с нулевыми расстояними по вертикали и горизонтали и без полей.
   *
   * @param aColumnCount int - количество колонок
   * @param aEqualWidth boolean - признак одинаковой ширины колонок
   * @return IVecColumnLayout - раскладка с мнимальными отступами
   */
  public static VecColumnLayout createNoTrims( int aColumnCount, boolean aEqualWidth ) {
    return new VecColumnLayout( aColumnCount, aEqualWidth, 0, 0, IMargins.NONE );
  }

  /**
   * Сокращенный конструктор с нулевыми полями и 4 пикселя по горизонтали и 2 по вертикали.
   *
   * @param aColumnCount int - количество колонок
   * @param aEqualWidth boolean - признак одинаковой ширины колонок
   */
  public VecColumnLayout( int aColumnCount, boolean aEqualWidth ) {
    colCount = aColumnCount;
    equalWidth = aEqualWidth;
    horSpace = 4;
    vertSpace = 2;
    margins = IMargins.NONE;
  }

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aColumnCount int - количество колонок
   * @param aEqualWidth boolean - признак одинаковой ширины колонок
   * @param aHorSpace int - расстояние между колонками по горизонтали
   * @param aVertSpace int - расстояние между контролями по вертикали
   * @param aMargins IMargins - поля
   */
  public VecColumnLayout( int aColumnCount, boolean aEqualWidth, int aHorSpace, int aVertSpace, IMargins aMargins ) {
    colCount = aColumnCount;
    equalWidth = aEqualWidth;
    horSpace = aHorSpace;
    vertSpace = aVertSpace;
    margins = aMargins;
  }

  // ------------------------------------------------------------------------------------
  // AbstractVecLayout
  //

  @Override
  public EVecLayoutKind layoutKind() {
    return EVecLayoutKind.COLUMN;
  }

  @Override
  protected void fillComposite( Composite aParent ) {
    GridLayout gl = new GridLayout( colCount, equalWidth );
    gl.horizontalSpacing = horSpace;
    gl.verticalSpacing = vertSpace;

    gl.marginLeft = margins.left();
    gl.marginTop = margins.top();
    gl.marginRight = margins.right();
    gl.marginBottom = margins.bottom();

    gl.marginHeight = 0;
    gl.marginWidth = 0;

    aParent.setLayout( gl );

    for( Item<IVecColumnLayoutData> item : items() ) {
      if( item.cb() == null ) { // нет контроля - идем дальше
        continue;
      }
      Control c = item.cb().createControl( aParent );
      IVecColumnLayoutData ld = item.layoutData();
      c.setLayoutData( createGridData( ld ) );
    }

  }

  // ------------------------------------------------------------------------------------
  // IVecColumnLayout
  //

  @Override
  public int columnsCount() {
    return colCount;
  }

  @Override
  public boolean areEqualWidth() {
    return equalWidth;
  }

  @Override
  public IMargins margins() {
    return margins;
  }

  @Override
  public int horSpace() {
    return horSpace;
  }

  @Override
  public int vertSpace() {
    return vertSpace;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private static GridData createGridData( IVecColumnLayoutData aData ) {
    GridData gd = new GridData();

    gd.horizontalIndent = 0;
    gd.verticalIndent = 0;

    gd.verticalAlignment = aData.horAlignment().swtStyle();
    gd.horizontalAlignment = aData.verAlignment().swtStyle();
    gd.grabExcessHorizontalSpace = !aData.isWidthFixed();
    gd.grabExcessVerticalSpace = !aData.isHeightFixed();

    return gd;
  }

}
