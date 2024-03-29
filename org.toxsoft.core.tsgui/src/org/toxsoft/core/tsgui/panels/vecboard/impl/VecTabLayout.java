package org.toxsoft.core.tsgui.panels.vecboard.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.vecboard.*;

/**
 * Реализация раскладки {@link IVecTabLayout}.
 *
 * @author hazard157
 */
public class VecTabLayout
    extends AbstractVecLayout<IVecTabLayoutData>
    implements IVecTabLayout {

  private final boolean isTabsAtBottom;

  /**
   * Создает раскладку с указанеим параметров.
   *
   * @param aIsTabsAtBottom boolean - признак показа ярлыков вкладок внизу (а не вверху) панели
   */
  public VecTabLayout( boolean aIsTabsAtBottom ) {
    isTabsAtBottom = aIsTabsAtBottom;
  }

  // ------------------------------------------------------------------------------------
  // Реализация мтодов класса AopLayoutBase
  //

  @Override
  protected Composite doCreateComposite( Composite aParent ) {
    int style;
    if( isTabsAtBottom ) {
      style = SWT.BOTTOM;
    }
    else {
      style = SWT.TOP;
    }
    return new TabFolder( aParent, style );
  }

  @Override
  protected void fillComposite( Composite aParent ) {
    for( Item<IVecTabLayoutData> item : items() ) {
      Control c = item.cb().createControl( aParent );
      TabItem tabItem = new TabItem( (TabFolder)aParent, SWT.NONE );
      IVecTabLayoutData ld = item.layoutData();
      tabItem.setText( ld.name() );
      tabItem.setToolTipText( ld.tooltipText() );
      if( !ld.iconId().isEmpty() ) {
        // FIXME GOGA MWA
        // IEclipseContext winContext = TsGuiUtils.getGuiThreadWinContext();
        // ITsIconManager iconManager = winContext.get( ITsIconManager.class );
        // tabItem.setImage( iconManager.loadStdIcon( ld.iconId(), ld.iconSize() ) );
      }
      tabItem.setControl( c );
    }
  }

  // ------------------------------------------------------------------------------------
  // IAopTabLayout
  //

  @Override
  public boolean isTabsAtBottom() {
    return isTabsAtBottom;
  }

  @Override
  public EVecLayoutKind layoutKind() {
    return EVecLayoutKind.TABS;
  }

}
