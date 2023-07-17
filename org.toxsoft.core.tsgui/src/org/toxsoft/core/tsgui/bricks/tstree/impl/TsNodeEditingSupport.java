package org.toxsoft.core.tsgui.bricks.tstree.impl;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;

/**
 * Cell editing support for {@link ITsNode} base tree viewers with {@link IValedControl} editors.
 *
 * @author vs
 * @author hazard157
 */
public class TsNodeEditingSupport
    extends EditingSupport {

  private final TreeViewer           viewer;
  private final ITsGuiContext        tsContext;
  private final ITsNodeValedProvider nodeValedProvider;

  /**
   * Constructor.
   *
   * @param aViewer {@link TreeViewer} - the underlying viewer
   * @param aTsContext {@link ITsGuiContext} - the context
   * @param aProvider {@link ITsNodeValedProvider} - VALED provider
   */
  public TsNodeEditingSupport( TreeViewer aViewer, ITsGuiContext aTsContext, ITsNodeValedProvider aProvider ) {
    super( aViewer );
    viewer = aViewer;
    tsContext = aTsContext;
    nodeValedProvider = aProvider;
  }

  // ------------------------------------------------------------------------------------
  // EditingSupport
  //

  @Override
  protected CellEditor getCellEditor( Object aElement ) {
    ITsNode node = ITsNode.class.cast( aElement );
    ITsGuiContext ctx = new TsGuiContext( tsContext );
    IValedControl<?> valed = nodeValedProvider.createValed( node, ctx );
    ValedCellEditor<?> cellEditor = new ValedCellEditor<>( valed, viewer, ctx );
    return cellEditor;
  }

  @Override
  protected boolean canEdit( Object aElement ) {
    ITsNode node = ITsNode.class.cast( aElement );
    return nodeValedProvider.canEdit( node );
  }

  @Override
  protected Object getValue( Object aElement ) {
    ITsNode node = ITsNode.class.cast( aElement );
    return nodeValedProvider.getValueForValed( node );
  }

  @Override
  protected void setValue( Object aElement, Object aValue ) {
    ITsNode node = ITsNode.class.cast( aElement );
    IAtomicValue value = IAtomicValue.class.cast( aValue );
    nodeValedProvider.setValueFromValed( node, value );
  }

}
