package org.toxsoft.tsgui.m5_3.gui.mpc.impl;

import static org.toxsoft.tsgui.m5_3.IM5Constants.*;
import static org.toxsoft.tsgui.m5_3.gui.mpc.IMultiPaneComponentConstants.*;

import org.eclipse.swt.widgets.Composite;
import org.toxsoft.tsgui.bricks.actions.ITsActionDef;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tsgui.m5_3.IM5Constants;
import org.toxsoft.tsgui.m5_3.IM5FieldDef;
import org.toxsoft.tsgui.m5_3.gui.mpc.IMultiPaneComponent;
import org.toxsoft.tsgui.m5_3.gui.panels.IM5EntityPanel;
import org.toxsoft.tsgui.m5_3.gui.panels.IM5PanelCreator;
import org.toxsoft.tsgui.m5_3.gui.viewers.IM5TreeViewer;
import org.toxsoft.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.tsgui.panels.toolbar.ITsToolBar;
import org.toxsoft.tsgui.panels.toolbar.TsToolBar;
import org.toxsoft.tsgui.widgets.TsComposite;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IMultiPaneComponent} base implementation.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class MultiPaneComponent<T>
    implements //
    ITsGuiContextable, //
    ILazyControl<TsComposite> //
{

  private final IM5TreeViewer<T> tree;

  private TsComposite        board       = null;
  private ITsToolBar         toolbar     = null;
  private IMpcDetailsPane<T> detailsPane = null;
  private IMpcSummaryPane<T> summaryPane = null;
  private IMpcFilterPane<T>  filterPane  = null;

  MultiPaneComponent( IM5TreeViewer<T> aViewer ) {
    TsNullArgumentRtException.checkNull( aViewer );
    tree = aViewer;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ITsToolBar createToolBar() {
    // TODO MultiPaneComponent.createToolBar()
    return null;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tree.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // ILazyControl
  //

  @Override
  public TsComposite createControl( Composite aParent ) {
    TsIllegalStateRtException.checkNoNull( board );
    if( OPDEF_IS_TOOLBAR.getValue( tsContext().params() ).asBool() ) {
      toolbar = createToolBar();
    }
    if( OPDEF_IS_DETAILS_PANE.getValue( tsContext().params() ).asBool() ) {

    }
    if( OPDEF_IS_DETAILS_PANE.getValue( tsContext().params() ).asBool() ) {

    }
    if( OPDEF_IS_SUMMARY_PANE.getValue( tsContext().params() ).asBool() ) {

    }

    // TODO Auto-generated method stub

    return null;
  }

  @Override
  public TsComposite getControl() {
    return board;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass may adjust created toolbar.
   * <p>
   * In base class simply creates toolbar, sets icons size, name labes and actions.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aName String - name of the toolbar
   * @param aIconSize {@link EIconSize} - toolbar icons size
   * @param aActs IList&lt;{@link ITsActionDef}&gt; - actions for buttons creation
   * @return {@link ITsToolBar} - created toolbar, must not be <code>null</code>
   */
  protected ITsToolBar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
      IListEdit<ITsActionDef> aActs ) {
    ITsToolBar tb = new TsToolBar( aContext );
    tb.setIconSize( aIconSize );
    tb.setNameLabelText( aName );
    tb.addActionDefs( aActs );
    return tb;
  }

  /**
   * Subclass may create its own implemenation of details pane {@link IMpcDetailsPane}.
   * <p>
   * In the base class, if any M5-field has {@link IM5Constants#M5FF_DETAIL} flag, TODO TRANSLATE
   * <p>
   * В базовом классе, если есть хотя бы одно поле с флагом {@link IM5Constants#M5FF_DETAIL}, создает экземпляр
   * {@link DefaultDetailsPane} с экземпляровм панели {@link IM5PanelCreator#createEntityDetailsPanel(ITsGuiContext)}.
   * Если такого поля нет, возвращает <code>null</code>.
   * <p>
   * Метод вызывается из {@link #createControl(Composite)}, только если задан параметр
   * {@link IMultiPaneComponentParams#USE_DETAILS_PANE}.
   * <p>
   * Если наследник решил, что панель не нужна, можно вернуть <code>null</code>.
   *
   * @return {@link IMpcDetailsPane} - созданная панель детальной информации или <code>null</code>
   */
  protected IMpcDetailsPane<T> doCreateDetailsPane() {
    boolean needDetailsPane = false;
    for( IM5FieldDef<T, ?> fdef : model().fieldDefs() ) {
      if( (fdef.flags() & M5FF_DETAIL) != 0 ) {
        needDetailsPane = true;
      }
    }
    if( needDetailsPane ) {
      IM5EntityPanel<T> panel = model().panelCreator().createEntityDetailsPanel( tsContext() );
      return new DefaultDetailsPane<>( this, panel );
    }
    return null;
  }

  /**
   * Наследник может создать собственную панель просмотра суммарной информации.
   * <p>
   * В базовом классе создает экземпляр {@link DefaultSummaryPane}. При переопределении вызывать родительский метод не
   * нужно.
   * <p>
   * Метод вызывается из {@link #createControl(Composite)}, только если задан параметр
   * {@link IMultiPaneComponentParams#USE_SUMMARY_PANE}.
   * <p>
   * Если наследник решил, что панель не нужна, можно вернуть <code>null</code>.
   *
   * @return {@link ISummaryPane} - созданная панель суммарной информации или <code>null</code>
   */
  protected ISummaryPane<T> doCreateSummaryPane() {
    return new DefaultSummaryPane<>( this );
  }

}
