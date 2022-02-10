package org.toxsoft.core.tsgui.panels.vecboard.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.core.tsgui.panels.vecboard.EVecLayoutKind;
import org.toxsoft.core.tsgui.panels.vecboard.IVecSashLayout;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;

/**
 * Реализация раскладки {@link IVecSashLayout}.
 *
 * @author goga
 */
public class VecSashLayout
    extends AbstractVecLayout<Integer>
    implements IVecSashLayout {

  private final boolean isHorizontal;
  private final int     sashWidth;

  /**
   * Создает раскладку со всеми инвариантами.
   *
   * @param aIsHorizontal boolean - признак горизонтального ряда контролей
   * @param aSashWidth int - положительная ширина перемещаемой планки в пределах или <=0 для ширины по умолчанию
   */
  public VecSashLayout( boolean aIsHorizontal, int aSashWidth ) {
    isHorizontal = aIsHorizontal;
    sashWidth = aSashWidth;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AopLayoutBase
  //

  @Override
  protected Composite doCreateComposite( Composite aParent ) {
    SashForm sf = new SashForm( aParent, isHorizontal ? SWT.HORIZONTAL : SWT.VERTICAL );
    if( sashWidth > 0 ) {
      sf.setSashWidth( sashWidth );
    }
    return sf;
  }

  @Override
  protected void fillComposite( Composite aParent ) {
    int[] weights = new int[items().size()];
    for( int i = 0, n = items().size(); i < n; i++ ) {
      Item<Integer> item = items().get( i );
      item.cb().createControl( aParent );
      weights[i] = item.layoutData().intValue();
    }
    ((SashForm)aParent).setWeights( weights );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IAopSashLayout
  //

  @Override
  public EVecLayoutKind layoutKind() {
    return EVecLayoutKind.SASH;
  }

  @Override
  public boolean isHorizontal() {
    return isHorizontal;
  }

  @Override
  public int sashWidth() {
    return sashWidth;
  }

  @Override
  protected void doCheckAddControl( ILazyControl<?> aControlBuilder, Integer aLayoutData ) {
    TsIllegalArgumentRtException.checkTrue( aLayoutData.intValue() <= 0 );
  }

}
