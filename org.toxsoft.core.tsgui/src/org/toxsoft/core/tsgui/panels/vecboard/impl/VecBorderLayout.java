package org.toxsoft.core.tsgui.panels.vecboard.impl;

import static org.toxsoft.core.tsgui.panels.vecboard.impl.ITsResources.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.vecboard.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVecBorderLayout} implementation.
 *
 * @author hazard157
 */
public class VecBorderLayout
    extends AbstractVecLayout<EBorderLayoutPlacement>
    implements IVecBorderLayout {

  /**
   * Constructor.
   */
  public VecBorderLayout() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // AbstractLayout
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

  @Override
  protected void doCheckAddControl( ILazyControl<?> aControlBuilder, EBorderLayoutPlacement aLayoutData ) {
    for( Item<EBorderLayoutPlacement> item : items() ) {
      if( item.layoutData() == aLayoutData ) {
        throw new TsItemAlreadyExistsRtException( FMT_ERR_CONTROL_ALREADY_EXIST_AT_BORDER_PLACEMENT, aLayoutData.id() );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // IVecBorderLayout
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

}
