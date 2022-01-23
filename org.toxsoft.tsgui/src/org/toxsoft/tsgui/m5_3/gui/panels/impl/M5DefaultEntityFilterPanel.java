package org.toxsoft.tsgui.m5_3.gui.panels.impl;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5_3.IM5Bunch;
import org.toxsoft.tsgui.m5_3.IM5Model;
import org.toxsoft.tsgui.m5_3.gui.panels.IM5FilterPanel;
import org.toxsoft.tsgui.panels.lazy.AbstractLazyPanel;
import org.toxsoft.tsgui.widgets.TsComposite;
import org.toxsoft.tslib.bricks.events.change.GenericChangeEventer;
import org.toxsoft.tslib.bricks.filter.ITsFilter;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Default implementation of {@link IM5FilterPanel}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public class M5DefaultEntityFilterPanel<T>
    extends AbstractLazyPanel<Control>
    implements IM5FilterPanel<T> {

  private final GenericChangeEventer eventer;
  private final IM5Model<T>          model;

  public M5DefaultEntityFilterPanel( ITsGuiContext aContext, IM5Model<T> aModel ) {
    super( aContext );
    TsNullArgumentRtException.checkNull( aModel );
    model = aModel;
    eventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected TsComposite doCreateControl( Composite aParent ) {
    TsComposite board = new TsComposite( aParent );
    // TODO Auto-generated method stub
    return board;
  }

  // ------------------------------------------------------------------------------------
  // IM5ModelRelated
  //

  @Override
  public IM5Model<T> model() {
    return model;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // IM5FilterPanel
  //

  @Override
  public ITsFilter<IM5Bunch<T>> getFilter() {
    // TODO Auto-generated method stub
    return null;
  }

}
