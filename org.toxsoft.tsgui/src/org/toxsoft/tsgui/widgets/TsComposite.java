package org.toxsoft.tsgui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.tslib.bricks.geometry.ITsPoint;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Extends {@link Composite} with min/max size constrains for some layouts.
 *
 * @author hazard157
 */
public class TsComposite
    extends Canvas {

  protected int minHeight = SWT.DEFAULT;
  protected int maxHeight = SWT.DEFAULT;
  protected int minWidth  = SWT.DEFAULT;
  protected int maxWidth  = SWT.DEFAULT;

  /**
   * Creates composite with style {@link SWT#NONE}.
   * <p>
   * Min and max constraints are set to {@link SWT#DEFAULT} that means no restrictions.
   *
   * @param aParent Composite - the parent composite
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsComposite( Composite aParent ) {
    super( TsNullArgumentRtException.checkNull( aParent ), SWT.NONE );
  }

  /**
   * Creates composite with the specified style.
   * <p>
   * Min and max constraints are set to {@link SWT#DEFAULT} that means no restrictions.
   *
   * @param aParent Composite - the parent composite
   * @param aStyle int - the cmposite style
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsComposite( Composite aParent, int aStyle ) {
    super( TsNullArgumentRtException.checkNull( aParent ), aStyle );
  }

  // --------------------------------------------------------------------------
  // Внутренняя реализация
  //

  private Point adjustSize( Point aSize ) {
    if( minHeight != SWT.DEFAULT && aSize.y < minHeight ) {
      aSize.y = minHeight;
    }
    if( maxHeight != SWT.DEFAULT && aSize.y > maxHeight ) {
      aSize.y = maxHeight;
    }
    if( minWidth != SWT.DEFAULT && aSize.x < minWidth ) {
      aSize.x = minWidth;
    }
    if( maxWidth != SWT.DEFAULT && aSize.x > maxWidth ) {
      aSize.x = maxWidth;
    }
    return aSize;
  }

  // --------------------------------------------------------------------------
  // Composite
  //

  @Override
  public Point computeSize( int wHint, int hHint, boolean changed ) {
    Point size = super.computeSize( wHint, hHint, changed );
    return adjustSize( size );
  }

  @Override
  public Point computeSize( int wHint, int hHint ) {
    Point size = super.computeSize( wHint, hHint );
    return adjustSize( size );
  }

  // --------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает минимальную ширину компоненты, число {@link SWT#DEFAULT} означает отсутствие ограничений.
   *
   * @return int - минимальная ширина компоненты или {@link SWT#DEFAULT}
   */
  public int minimumWidth() {
    return minWidth;
  }

  /**
   * Возвращает максимальную ширину компоненты, число {@link SWT#DEFAULT} означает отсутствие ограничений.
   *
   * @return int - максимальная ширина компоненты или {@link SWT#DEFAULT}
   */
  public int maximumWidth() {
    return maxHeight;
  }

  /**
   * Возвращает минимальную высоту компоненты, число {@link SWT#DEFAULT} означает отсутствие ограничений.
   *
   * @return int - минимальная высота компоненты или {@link SWT#DEFAULT}
   */
  public int minimumHeight() {
    return minHeight;
  }

  /**
   * Возвращает максимальную высоту компоненты, число {@link SWT#DEFAULT} означает отсутствие ограничений.
   *
   * @return int - масимальная высота компоненты или {@link SWT#DEFAULT}
   */
  public int maximumHeight() {
    return maxHeight;
  }

  /**
   * Задает минимальную ширину компоненты, число {@link SWT#DEFAULT} означает отсутствие ограничений.
   *
   * @param aWidth int - минимальная ширина компоненты или {@link SWT#DEFAULT}
   */
  public void setMinimumWidth( int aWidth ) {
    minWidth = aWidth;
  }

  /**
   * Задает максимальную ширину компоненты, число {@link SWT#DEFAULT} означает отсутствие ограничений.
   *
   * @param aWidth int - максимальная ширина компоненты или {@link SWT#DEFAULT}
   */
  public void setMaximumWidth( int aWidth ) {
    maxWidth = aWidth;
  }

  /**
   * Задает минимальную высоту компоненты, число {@link SWT#DEFAULT} означает отсутствие ограничений.
   *
   * @param aHeight int - минимальная высота компоненты или {@link SWT#DEFAULT}
   */
  public void setMinimumHeight( int aHeight ) {
    minHeight = aHeight;
  }

  /**
   * Задает максимальную высоту компоненты, число {@link SWT#DEFAULT} означает отсутствие ограничений.
   *
   * @param aHeight int - масимальная высота компоненты или {@link SWT#DEFAULT}
   */
  public void setMaximumHeight( int aHeight ) {
    maxHeight = aHeight;
  }

  /**
   * Задает минимальный размер компоненты.
   * <p>
   * Значения {@link SWT#DEFAULT} означает отсутствие ограничений по соответствующему измерению.
   *
   * @param aSize {@link Point} - минимальная ширина и высота компоненты
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setMinimumSize( Point aSize ) {
    TsNullArgumentRtException.checkNull( aSize );
    minWidth = aSize.x;
    minHeight = aSize.y;
  }

  /**
   * Задает минимальный размер компоненты.
   * <p>
   * Значения {@link SWT#DEFAULT} означает отсутствие ограничений по соответствующему измерению.
   *
   * @param aSize {@link ITsPoint} - минимальная ширина и высота компоненты
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setMinimumSize( ITsPoint aSize ) {
    TsNullArgumentRtException.checkNull( aSize );
    minWidth = aSize.x();
    minHeight = aSize.y();
  }

  /**
   * Задает максимальный размер компоненты.
   * <p>
   * Значения {@link SWT#DEFAULT} означает отсутствие ограничений по соответствующему измерению.
   *
   * @param aSize {@link Point} - максимальная ширина и высота компоненты
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setMaximumSize( Point aSize ) {
    TsNullArgumentRtException.checkNull( aSize );
    maxWidth = aSize.x;
    maxHeight = aSize.y;
  }

  /**
   * Задает максимальный размер компоненты.
   * <p>
   * Значения {@link SWT#DEFAULT} означает отсутствие ограничений по соответствующему измерению.
   *
   * @param aSize {@link ITsPoint} - максимальная ширина и высота компоненты
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setMaximumSize( ITsPoint aSize ) {
    TsNullArgumentRtException.checkNull( aSize );
    maxWidth = aSize.x();
    maxHeight = aSize.y();
  }

  /**
   * Задает минимальную высоту в процентах от высоты дисплея.
   *
   * @param aPercentage int - проценты в пределах 1..100
   * @throws TsIllegalArgumentRtException аргумент выходит за допустимые пределы
   */
  public void setMinHeightDisplayRelative( int aPercentage ) {
    TsIllegalArgumentRtException.checkTrue( aPercentage < 1 || aPercentage > 100 );
    int displayHeight = getDisplay().getBounds().height;
    minHeight = (displayHeight * aPercentage) / 100;
  }

  /**
   * Задает минимальную ширину в процентах от ширины дисплея.
   *
   * @param aPercentage int - проценты в пределах 1..100
   * @throws TsIllegalArgumentRtException аргумент выходит за допустимые пределы
   */
  public void setMinWidthDisplayRelative( int aPercentage ) {
    TsIllegalArgumentRtException.checkTrue( aPercentage < 1 || aPercentage > 100 );
    int displayWidth = getDisplay().getBounds().width;
    minWidth = (displayWidth * aPercentage) / 100;
  }

  /**
   * Задает маскимальную высоту в процентах от высоты дисплея.
   *
   * @param aPercentage int - проценты в пределах 1..100
   * @throws TsIllegalArgumentRtException аргумент выходит за допустимые пределы
   */
  public void setMaxHeightDisplayRelative( int aPercentage ) {
    TsIllegalArgumentRtException.checkTrue( aPercentage < 1 || aPercentage > 100 );
    int displayHeight = getDisplay().getBounds().height;
    maxHeight = (displayHeight * aPercentage) / 100;
  }

  /**
   * Задает маскимальную ширину в процентах от ширины дисплея.
   *
   * @param aPercentage int - проценты в пределах 1..100
   * @throws TsIllegalArgumentRtException аргумент выходит за допустимые пределы
   */
  public void setMaxWidthDisplayRelative( int aPercentage ) {
    TsIllegalArgumentRtException.checkTrue( aPercentage < 1 || aPercentage > 100 );
    int displayWidth = getDisplay().getBounds().width;
    maxWidth = (displayWidth * aPercentage) / 100;
  }

}
