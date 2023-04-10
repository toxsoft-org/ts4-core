package org.toxsoft.core.tsgui.panels.generic;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IGenericCollPanel} abstract implementation.
 *
 * @author hazard157
 * @param <T> - the entity type
 */
public abstract class AbstractGenericCollPanel<T>
    extends AbstractLazyPanel<Control>
    implements IGenericCollPanel<T> {

  protected final GenericChangeEventer            genericChangeEventer;
  protected final TsSelectionChangeEventHelper<T> selectionChangeEventHelper;
  protected final TsDoubleClickEventHelper<T>     doubleClickEventHelper;

  private final boolean viewerFlag;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException аргумент = null
   */
  public AbstractGenericCollPanel( ITsGuiContext aContext ) {
    this( aContext, false );
  }

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @param aIsViewer boolean - the viewer (read-only) panel flag
   * @throws TsNullArgumentRtException аргумент = null
   */
  public AbstractGenericCollPanel( ITsGuiContext aContext, boolean aIsViewer ) {
    super( aContext );
    genericChangeEventer = new GenericChangeEventer( this );
    selectionChangeEventHelper = new TsSelectionChangeEventHelper<>( this );
    doubleClickEventHelper = new TsDoubleClickEventHelper<>( this );
    viewerFlag = aIsViewer;
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected abstract Control doCreateControl( Composite aParent );

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  public abstract T selectedItem();

  @Override
  public abstract void setSelectedItem( T aItem );

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<T> aListener ) {
    selectionChangeEventHelper.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<T> aListener ) {
    selectionChangeEventHelper.removeTsSelectionListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsDoubleClickEventProducer
  //

  @Override
  public void addTsDoubleClickListener( ITsDoubleClickListener<T> aListener ) {
    doubleClickEventHelper.addTsDoubleClickListener( aListener );
  }

  @Override
  public void removeTsDoubleClickListener( ITsDoubleClickListener<T> aListener ) {
    doubleClickEventHelper.removeTsDoubleClickListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // IGenericCollPanel
  //

  @Override
  public boolean isViewer() {
    return viewerFlag;
  }

  @Override
  public abstract IList<T> items();

  @Override
  public abstract void refresh();

  @Override
  public ITsCheckSupport<T> checkSupport() {
    return ITsCheckSupport.NONE;
  }

}
