package org.toxsoft.core.tsgui.panels.lazy;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link AbstractLazyPanel} extension to work with a list of objects of the same type.
 * <p>
 * Implements {@link ITsSelectionChangeEventProducer} and {@link ITsDoubleClickEventProducer}.
 *
 * @author hazard157
 * @param <E> - type of the objects
 * @param <C> - type of the implementing SWT control
 */
public abstract class AbstractTsStdEventsProducerLazyPanel<E, C extends Control>
    extends AbstractLazyPanel<C>
    implements ITsSelectionProvider<E>, ITsDoubleClickEventProducer<E>, IGenericChangeEventCapable {

  protected final GenericChangeEventer            genericChangeEventer;
  protected final TsSelectionChangeEventHelper<E> selectionChangeEventHelper;
  protected final TsDoubleClickEventHelper<E>     doubleClickEventHelper;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractTsStdEventsProducerLazyPanel( ITsGuiContext aContext ) {
    super( aContext );
    genericChangeEventer = new GenericChangeEventer( this );
    selectionChangeEventHelper = new TsSelectionChangeEventHelper<>( this );
    doubleClickEventHelper = new TsDoubleClickEventHelper<>( this );
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Generates an event {@link ITsSelectionChangeListener#onTsSelectionChanged(Object, Object)}.
   *
   * @param aItem &lt;E&gt; - selected element, may be <code>null</code>
   */
  public void fireTsSelectionEvent( E aItem ) {
    selectionChangeEventHelper.fireTsSelectionEvent( aItem );
  }

  /**
   * Generates an event {@link ITsSelectionChangeListener#onTsSelectionChanged(Object, Object)}.
   *
   * @param aItem &lt;E&gt; - selected element, may be <code>null</code>
   */
  public void fireTsDoubleClickEvent( E aItem ) {
    doubleClickEventHelper.fireTsDoublcClickEvent( aItem );
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionChangeEventProducer
  //

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<E> aListener ) {
    selectionChangeEventHelper.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<E> aListener ) {
    selectionChangeEventHelper.removeTsSelectionListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsDoubleClickEventProducer
  //

  @Override
  public void addTsDoubleClickListener( ITsDoubleClickListener<E> aListener ) {
    doubleClickEventHelper.addTsDoubleClickListener( aListener );
  }

  @Override
  public void removeTsDoubleClickListener( ITsDoubleClickListener<E> aListener ) {
    doubleClickEventHelper.removeTsDoubleClickListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

}
