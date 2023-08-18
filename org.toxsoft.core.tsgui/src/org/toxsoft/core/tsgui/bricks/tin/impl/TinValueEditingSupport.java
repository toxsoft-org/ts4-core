package org.toxsoft.core.tsgui.bricks.tin.impl;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tstree.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;

/**
 * Поддержка редактирования значений свойств в инспекторе "по месту".
 * <p>
 *
 * @author vs
 */
public class TinValueEditingSupport
    extends EditingSupport {

  private final TreeViewer viewer;

  private final ITsGuiContext tsContext;

  /**
   * Конструктор.
   *
   * @param aViewer {@link TreeViewer} - просмотрщик в виде дерева
   * @param aTsContext {@link ITsGuiContext} - соответствующий контекст
   */
  public TinValueEditingSupport( TreeViewer aViewer, ITsGuiContext aTsContext ) {
    super( aViewer );
    viewer = aViewer;
    tsContext = aTsContext;
  }

  @Override
  protected CellEditor getCellEditor( Object aElement ) {
    ITinRow tinNode = ITinRow.class.cast( aElement );
    IValedControl<IAtomicValue> valed = tinNode.createValed( tsContext );
    ValedCellEditor<IAtomicValue> cellEditor = new ValedCellEditor<>( valed, viewer, tsContext );
    return cellEditor;
  }

  @Override
  protected boolean canEdit( Object aElement ) {
    ITinRow tinNode = ITinRow.class.cast( aElement );
    return tinNode.canEdit();
  }

  @Override
  protected Object getValue( Object aElement ) {
    ITinRow tinNode = ITinRow.class.cast( aElement );
    return tinNode.getAtomicValueForValed();
  }

  @Override
  protected void setValue( Object aElement, Object aValue ) {
    ITinRow tinNode = ITinRow.class.cast( aElement );
    IAtomicValue value = IAtomicValue.class.cast( aValue );
    tinNode.setAtomicValueFromValed( value );
  }

}
