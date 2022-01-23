package org.toxsoft.tsgui.m5_2.impl;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5.gui.panels.IM5PanelBase;
import org.toxsoft.tsgui.m5_2.IM5Model;
import org.toxsoft.tsgui.panels.lazy.AbstractLazyPanel;
import org.toxsoft.tslib.bricks.events.change.GenericChangeEventer;
import org.toxsoft.tslib.bricks.events.change.IGenericChangeEventer;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IM5PanelBase} implementation.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public abstract class M5PanelBase<T>
    extends AbstractLazyPanel<Control>
    implements IM5PanelBase<T> {

  private final GenericChangeEventer eventer;
  private final boolean              isViewer;
  private boolean                    editable;

  private final IM5Model<T> model;

  /**
   * Конструктор для наследников.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @param aModel {@link IM5Model} - модель
   * @param aViewer boolean - признак просмотрщика (панели только для просмотра)
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  protected M5PanelBase( ITsGuiContext aContext, IM5Model<T> aModel, boolean aViewer ) {
    super( aContext );
    model = TsNullArgumentRtException.checkNull( aModel );
    eventer = new GenericChangeEventer( this );
    isViewer = aViewer;
    editable = !isViewer;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IM5PanelBase
  //

  @Override
  final public boolean isViewer() {
    return isViewer;
  }

  @Override
  final public IM5Model<T> model() {
    return model;
  }

  @Override
  final public boolean isEditable() {
    return editable;
  }

  @Override
  public void setEditable( boolean aEditable ) {
    if( !isViewer ) {
      if( editable != aEditable ) {
        editable = aEditable;
        doEditableStateChanged();
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass mat change panel look and behaviour when {@link #isEditable()} changes.
   * <p>
   * Does nothing in base class, there is no need to call parent method in subclass.
   */
  protected void doEditableStateChanged() {
    // nop
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected abstract Control doCreateControl( Composite aParent );

}
