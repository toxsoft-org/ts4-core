package org.toxsoft.core.tsgui.panels.vecboard.impl;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.vecboard.*;
import org.toxsoft.core.tsgui.utils.margins.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

/**
 * Неизменяемая реализация раскладки контролей по колонкам.
 * <p>
 *
 * @author vs
 */
public class VecColumnLayout
    extends AbstractVecLayout<IVecColumnLayoutData>
    implements IVecColumnLayout {

  private final int horSpace;
  private final int vertSpace;

  private final boolean equalWidth;

  private final boolean fixedHeight;

  private final ITsMargins margins;

  private final IList<IVecColumnLayoutData> columnDefs;

  /**
   * Создает раскладку с нулевыми расстояними по вертикали и горизонтали и без полей.
   *
   * @param aColumnDefs IList&lt;IVecColumnLayoutData> - список описаний колонок
   * @param aEqualWidth boolean - признак одинаковой ширины колонок
   * @param aFixedHeight boolean - признак фиксированной высоты строк
   * @return IVecColumnLayout - раскладка с мнимальными отступами
   */
  public static VecColumnLayout createNoTrims( IList<IVecColumnLayoutData> aColumnDefs, boolean aEqualWidth,
      boolean aFixedHeight ) {
    return new VecColumnLayout( aColumnDefs, aEqualWidth, aFixedHeight, 0, 0, new TsMargins() );
  }

  /**
   * Сокращенный конструктор с нулевыми полями и 4 пикселя по горизонтали и 2 по вертикали.
   *
   * @param aColumnDefs IList&lt;IVecColumnLayoutData> - список описаний колонок
   * @param aEqualWidth boolean - признак одинаковой ширины колонок
   * @param aFixedHeight boolean - признак фиксированной высоты строк
   */
  public VecColumnLayout( IList<IVecColumnLayoutData> aColumnDefs, boolean aEqualWidth, boolean aFixedHeight ) {
    columnDefs = new ElemArrayList<>( aColumnDefs );
    equalWidth = aEqualWidth;
    fixedHeight = aFixedHeight;
    horSpace = 4;
    vertSpace = 2;
    margins = new TsMargins();
  }

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aColumnDefs IList&lt;IVecColumnLayoutData> - список описаний колонок
   * @param aEqualWidth boolean - признак одинаковой ширины колонок
   * @param aFixedHeight boolean - признак фиксированной высоты строк
   * @param aHorSpace int - расстояние между колонками по горизонтали
   * @param aVertSpace int - расстояние между контролями по вертикали
   * @param aMargins ITsMargins - поля
   */
  public VecColumnLayout( IList<IVecColumnLayoutData> aColumnDefs, boolean aEqualWidth, boolean aFixedHeight,
      int aHorSpace, int aVertSpace, ITsMargins aMargins ) {
    columnDefs = new ElemArrayList<>( aColumnDefs );
    equalWidth = aEqualWidth;
    fixedHeight = aFixedHeight;
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
    GridLayout gl = new GridLayout( items().size(), equalWidth );
    gl.horizontalSpacing = horSpace;
    gl.verticalSpacing = vertSpace;

    gl.marginLeft = margins.left();
    gl.marginTop = margins.top();
    gl.marginRight = margins.right();
    gl.marginBottom = margins.bottom();

    gl.marginHeight = 0;
    gl.marginWidth = 0;

    aParent.setLayout( gl );

    int idx = 0;
    for( Item<IVecColumnLayoutData> item : items() ) {
      Control c = item.cb().createControl( aParent );
      IVecColumnLayoutData ld = columnDefs.get( idx % columnDefs.size() );
      c.setLayoutData( createGridData( ld ) );
      idx++;
    }

  }

  // ------------------------------------------------------------------------------------
  // IVecColumnLayout
  //

  @Override
  public int columnsCount() {
    return columnDefs.size();
  }

  @Override
  public boolean areEqualWidth() {
    return equalWidth;
  }

  @Override
  public boolean isHeightFixed() {
    return fixedHeight;
  }

  @Override
  public ITsMargins margins() {
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

  private GridData createGridData( IVecColumnLayoutData aData ) {
    GridData gd = new GridData();

    gd.horizontalIndent = 0;
    gd.verticalIndent = 0;

    gd.verticalAlignment = aData.horAlignment().swtStyle();
    gd.horizontalAlignment = aData.verAlignment().swtStyle();
    gd.grabExcessHorizontalSpace = !aData.isWidthFixed();
    gd.grabExcessVerticalSpace = !fixedHeight;

    return gd;
  }

}
