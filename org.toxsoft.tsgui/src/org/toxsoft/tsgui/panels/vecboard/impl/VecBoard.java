package org.toxsoft.tsgui.panels.vecboard.impl;

import static org.toxsoft.tsgui.panels.vecboard.impl.ITsResources.*;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.toxsoft.tsgui.panels.vecboard.*;
import org.toxsoft.tsgui.utils.layout.BorderLayout;
import org.toxsoft.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация {@link IVecBoard}.
 *
 * @author goga
 */
public class VecBoard
    implements IVecBoard {

  private AbstractVecLayout<?> layout         = null;
  private IVecGroupBoxInfo     groupBoxInfo   = null;
  private Composite            boardComposite = null;

  /**
   * Создает пустую панель - не-группу и без заданной раскладки.
   */
  public VecBoard() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IBoard
  //

  @Override
  public void setLayout( IVecLayout<?> aLayout ) {
    TsNullArgumentRtException.checkNull( aLayout );
    TsIllegalStateRtException.checkNoNull( layout );
    layout = (AbstractVecLayout<?>)aLayout;
  }

  @Override
  public IVecLayout<? extends Object> getLayout() {
    return layout;
  }

  @Override
  public boolean isGroupBox() {
    return groupBoxInfo != null;
  }

  @Override
  public IVecGroupBoxInfo groupBoxInfo() {
    return groupBoxInfo;
  }

  @Override
  public void setGroupBoxInfo( IVecGroupBoxInfo aGroupBoxInfo ) {
    TsNullArgumentRtException.checkNull( aGroupBoxInfo );
    TsIllegalStateRtException.checkNoNull( layout );
    groupBoxInfo = aGroupBoxInfo;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IVecControlBuilder
  //

  @Override
  public Composite createControl( Composite aParent ) {
    TsNullArgumentRtException.checkNull( aParent );
    TsIllegalStateRtException.checkNull( layout, MSG_ERR_NO_BOADR_LAYOUT );
    // Если нужно, сделаем обрамление Groupbox
    if( isGroupBox() ) {
      Group g = new Group( aParent, groupBoxInfo.borderType().swtStyle() );
      g.setText( groupBoxInfo.title() );
      g.setToolTipText( groupBoxInfo.tooltipText() );
      g.setLayout( new BorderLayout() );
      layout.createWidget( g );
      boardComposite = g;
    }
    else {
      boardComposite = layout.createWidget( aParent );
    }
    return boardComposite;
  }

  @Override
  public Composite getControl() {
    return boardComposite;
  }

}
