package org.toxsoft.core.tsgui.panels.vecboard.impl;

import static org.toxsoft.core.tsgui.panels.vecboard.impl.ITsResources.*;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.core.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.core.tsgui.panels.vecboard.EVecLayoutKind;
import org.toxsoft.core.tsgui.panels.vecboard.IVecBorderLayout;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.utils.layout.EBorderLayoutPlacement;
import org.toxsoft.core.tslib.utils.errors.TsItemAlreadyExistsRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация раскладки {@link IVecBorderLayout}.
 *
 * @author goga
 */
public class VecBorderLayout
    extends AbstractVecLayout<EBorderLayoutPlacement>
    implements IVecBorderLayout {

  /**
   * Создает пустую раскладку.
   */
  public VecBorderLayout() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов AbstractLayout
  //

  @Override
  public EVecLayoutKind layoutKind() {
    return EVecLayoutKind.BORDER;
  }

  @Override
  protected void fillComposite( Composite aParent ) {
    aParent.setLayout( new BorderLayout() );
    for( Item<EBorderLayoutPlacement> item : items() ) {
      Control c = item.cb().createControl( aParent );
      c.setLayoutData( item.layoutData() );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IVecBorderLayout
  //

  @Override
  public ILazyControl<?> findItem( EBorderLayoutPlacement aPlacement ) {
    TsNullArgumentRtException.checkNull( aPlacement );
    for( Item<EBorderLayoutPlacement> item : items() ) {
      if( item.layoutData() == aPlacement ) {
        return item.cb();
      }
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractVecLayout
  //

  @Override
  protected void doCheckAddControl( ILazyControl<?> aControlBuilder, EBorderLayoutPlacement aLayoutData ) {
    for( Item<EBorderLayoutPlacement> item : items() ) {
      if( item.layoutData() == aLayoutData ) {
        throw new TsItemAlreadyExistsRtException( FMT_ERR_CONTROL_ALREADY_EXIST_AT_BORDER_PLACEMENT, aLayoutData.id() );
      }
    }
  }

}
