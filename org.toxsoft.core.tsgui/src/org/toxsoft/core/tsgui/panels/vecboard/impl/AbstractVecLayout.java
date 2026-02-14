package org.toxsoft.core.tsgui.panels.vecboard.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.vecboard.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVecLayout} implementation base.
 *
 * @author hazard157
 * @param <D> - layout data type
 */
abstract class AbstractVecLayout<D>
    implements IVecLayout<D> {

  protected static class Item<D> {

    private final ILazyControl<?> cb;
    private final D               layoutData;

    Item( ILazyControl<?> aControlBuilder, D aLayoutData ) {
      cb = aControlBuilder;
      layoutData = aLayoutData;
    }

    public ILazyControl<?> cb() {
      return cb;
    }

    public D layoutData() {
      return layoutData;
    }

  }

  private final IListEdit<Item<D>> items = new ElemLinkedBundleList<>();

  // ------------------------------------------------------------------------------------
  // package API
  //

  final Composite createWidget( Composite aParent ) {
    Composite c = doCreateComposite( aParent );
    fillComposite( c );
    c.addDisposeListener( e -> onDispose() );
    return c;
  }

  final IList<Item<D>> items() {
    return items;
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  protected void addItem( ILazyControl<?> aControlBuilder, D aLayoutData ) {
    items.add( new Item<>( aControlBuilder, aLayoutData ) );
  }

  // ------------------------------------------------------------------------------------
  // ILayout
  //

  @Override
  public abstract EVecLayoutKind layoutKind();

  @Override
  public final void addControl( ILazyControl<?> aControlBuilder, D aLayoutData ) {
    TsNullArgumentRtException.checkNulls( aControlBuilder, aLayoutData );
    TsIllegalArgumentRtException.checkTrue( aControlBuilder.getControl() != null );
    doCheckAddControl( aControlBuilder, aLayoutData );
    addItem( aControlBuilder, aLayoutData );
  }

  // ------------------------------------------------------------------------------------
  // To override/implements
  //

  /**
   * Subclass may create own SWT control instead of default {@link Composite}..
   * <p>
   * For example, {@link EVecLayoutKind#SASH} create {@link Sash}, а {@link EVecLayoutKind#TABS} creates
   * {@link TabFolder}.
   *
   * @param aParent {@link Composite} - parent composite
   * @return {@link Composite} - created composite
   */
  protected Composite doCreateComposite( Composite aParent ) {
    return new Composite( aParent, SWT.NONE );
  }

  /**
   * Implementation may release resources allocated on SWT control creation.
   * <p>
   * In the base class does nothing.
   */
  protected void onDispose() {
    // nop
  }

  /**
   * Implementation must create child controls and initialize SWT layout.
   *
   * @param aParent {@link Composite} - parent composite created with {@link #createWidget(Composite)}
   */
  protected abstract void fillComposite( Composite aParent );

  /**
   * Implementation can check whether adding a control with the specified data is allowed.
   * <p>
   * If control can't be added to layout method must throw any exception based on {@link TsRuntimeException}.
   * <p>
   * In the base class does nothing.
   *
   * @param aControlBuilder {@link ILazyControl} - control creator, never is <code>null</code>
   * @param aLayoutData D - the layout data, never is <code>null</code>
   * @throws TsRuntimeException control can't be added to this layout
   */
  protected void doCheckAddControl( ILazyControl<?> aControlBuilder, D aLayoutData ) {
    // nop
  }

}
