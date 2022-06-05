package org.toxsoft.core.tsgui.panels.vecboard.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.graphics.ETsOrientation;
import org.toxsoft.core.tsgui.panels.vecboard.*;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация раскладки {@link IVecRowLayout}.
 *
 * @author hazard157
 */
public class VecRowLayout
    extends AbstractVecLayout<IVecRowLayoutData>
    implements IVecRowLayout {

  private final RowLayout rowLayout;

  /**
   * Создает раскладку с указанием ориентации ряда контролей.
   * <p>
   * Кроме ориентации, остальные параметры инициализируются значениями по умолчанию класса {@link RowLayout}.
   *
   * @param aOrientation {@link ETsOrientation} - ориентация ряда контролей
   * @throws TsNullArgumentRtException аргумент = null
   */
  public VecRowLayout( ETsOrientation aOrientation ) {
    TsNullArgumentRtException.checkNull( aOrientation );
    rowLayout = new RowLayout( aOrientation.swtStyle() );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private RowLayout createSwtLayout() {
    RowLayout l = new RowLayout( rowLayout.type );
    l.center = rowLayout.center;
    l.fill = rowLayout.fill;
    l.spacing = rowLayout.spacing;
    l.justify = rowLayout.justify;
    l.pack = rowLayout.pack;
    l.wrap = rowLayout.wrap;
    l.marginBottom = rowLayout.marginBottom;
    l.marginTop = rowLayout.marginTop;
    l.marginLeft = rowLayout.marginLeft;
    l.marginRight = rowLayout.marginRight;
    l.marginHeight = rowLayout.marginHeight;
    l.marginWidth = rowLayout.marginWidth;
    return l;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов AbstractLayout
  //

  @Override
  protected void fillComposite( Composite aParent ) {
    aParent.setLayout( createSwtLayout() );
    for( int i = 0, n = items().size(); i < n; i++ ) {
      Item<IVecRowLayoutData> item = items().get( i );
      item.cb().createControl( aParent );
      item.cb().getControl().setLayoutData( new RowData( item.layoutData().width(), item.layoutData().height() ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IAopRowLayout
  //

  @Override
  public ETsOrientation orientation() {
    return (rowLayout.type & SWT.HORIZONTAL) != 0 ? ETsOrientation.HORIZONTAL : ETsOrientation.VERTICAL;
  }

  @Override
  public int marginWidth() {
    return rowLayout.marginWidth;
  }

  @Override
  public int marginHeight() {
    return rowLayout.marginHeight;
  }

  @Override
  public int spacing() {
    return rowLayout.spacing;
  }

  @Override
  public boolean wrap() {
    return rowLayout.wrap;
  }

  @Override
  public boolean pack() {
    return rowLayout.pack;
  }

  @Override
  public boolean fill() {
    return rowLayout.fill;
  }

  @Override
  public boolean center() {
    return rowLayout.center;
  }

  @Override
  public boolean justify() {
    return rowLayout.justify;
  }

  @Override
  public int marginLeft() {
    return rowLayout.marginLeft;
  }

  @Override
  public int marginTop() {
    return rowLayout.marginTop;
  }

  @Override
  public int marginRight() {
    return rowLayout.marginRight;
  }

  @Override
  public int marginBottom() {
    return rowLayout.marginBottom;
  }

  @Override
  public EVecLayoutKind layoutKind() {
    return EVecLayoutKind.ROW;
  }

  // ------------------------------------------------------------------------------------
  // API класса для редактирования параметров
  //

  /**
   * Задает отсутп слева и справа от каждого контроля внутри ячейки контроля.
   *
   * @param aValue int - отступ в пикселях
   */
  public void setMarginWidth( int aValue ) {
    rowLayout.marginWidth = aValue;
  }

  /**
   * Задает отступ сверху и снизу от каждого контроля внутри ячейки контроля.
   *
   * @param aValue int - отступ в пикселях
   */
  public void setMarginHeight( int aValue ) {
    rowLayout.marginHeight = aValue;
  }

  /**
   * Задает расстояние между ячеуками с каонтролями.
   *
   * @param aValue int - значение в пикселях
   */
  public void setSpacing( int aValue ) {
    rowLayout.spacing = aValue;
  }

  /**
   * Задает, будет ли перенос на следующую линию, если контролям не хватает места в раскладке.
   *
   * @param aValue boolean - признак переноса контролей
   */
  public void setWrap( boolean aValue ) {
    rowLayout.wrap = aValue;
  }

  /**
   * Задает, будет ли каждый котроль упакован в свой размер, или все будут иметь размер наибольшего контроля.
   *
   * @param aValue boolean - признак упаковки каждого контроля в свой размер
   */
  public void setPack( boolean aValue ) {
    rowLayout.pack = aValue;
  }

  /**
   * Задает, будут ли все контроли одинаковой высоты (в горизонтальной) или ширини в вертикальной раскладке.
   *
   * @param aValue boolean - признак одинакового размера поперек расположения
   */
  public void setFill( boolean aValue ) {
    rowLayout.fill = aValue;
  }

  /**
   * Задает, будут ли контроли отцентрированы по вертикали в горизонтальной линии (или по горизонтали в вертикальной
   * линии).
   *
   * @param aValue boolean - признак центрирования поперек расположения
   */
  public void setCenter( boolean aValue ) {
    rowLayout.center = aValue;
  }

  /**
   * Задает, будут ли контроли в линии выровнены с добавлением расстояния между ними.
   *
   * @param aValue boolean - признак
   */
  public void setJustify( boolean aValue ) {
    rowLayout.justify = aValue;
  }

  /**
   * Задает отступ от левого внутреннего края раскладки.
   *
   * @param aValue int - отступ в пикселях
   */
  public void setMarginLeft( int aValue ) {
    rowLayout.marginLeft = aValue;
  }

  /**
   * Задает отступ от верхнего внутреннего края раскладки.
   *
   * @param aValue int - отступ в пикселях
   */
  public void setMarginTop( int aValue ) {
    rowLayout.marginTop = aValue;
  }

  /**
   * Задает отступ от правого внутреннего края раскладки.
   *
   * @param aValue int - отступ в пикселях
   */
  public void setMarginRight( int aValue ) {
    rowLayout.marginRight = aValue;
  }

  /**
   * Задает отступ от нижнего внутреннего края раскладки.
   *
   * @param aValue int - отступ в пикселях
   */
  public void setMarginBottom( int aValue ) {
    rowLayout.marginBottom = aValue;
  }
}
