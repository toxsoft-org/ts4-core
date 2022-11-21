package org.toxsoft.core.tsgui.utils.checkcoll;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Collection viewers ability to set check marks on individual elemenets.
 * <p>
 * This interface may be mixin or agregated member. Anyway if check states is not supported by viewer
 * {@link #isChecksSupported()} returns <code>false</code> and other method of this interface throws an
 * {@link TsUnsupportedFeatureRtException}.
 *
 * @author hazard157
 * @param <T> - type of elements in collection viewer
 */
public interface ITsCheckSupport<T> {

  /**
   * No check supporting singleton.
   */
  @SuppressWarnings( "rawtypes" )
  ITsCheckSupport NONE = new InternalNoneCheckSupport();

  /**
   * Определяет, поддерживается ли состояние отмеченности компонентой.
   * <p>
   * If this method returns <code>false</code> then all other methods throws an {@link TsUnsupportedFeatureRtException}.
   *
   * @return boolean - <code>true</code> if viewer supports check stetes of elements
   */
  boolean isChecksSupported();

  /**
   * Determines the check state of the specified element.
   *
   * @param aItem &lt;T&gt; - element to determine chech state
   * @return boolean - the check state of the element
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException specified element is not present in collection
   * @throws TsUnsupportedFeatureRtException check state is not supported in viewer
   */
  boolean getItemCheckState( T aItem );

  /**
   * Returns the items with specified check state.
   *
   * @param aCheckState boolean - ths check state of the elements to return
   * @return {@link IList}&lt;T&gt; - list of all elements which have the specified check state
   * @throws TsUnsupportedFeatureRtException check state is not supported in viewer
   */
  IList<T> listCheckedItems( boolean aCheckState );

  /**
   * Sets the check state of the specified element.
   * <p>
   * If specified element is not present in displayed collection then method doesa nothing.
   * <p>
   * For some implementations, at least for SWT tree, the check state change does not changes parent nodess visual
   * representation. Client must refresh all parents nodes (that is whole tree) to visualize parent nodes state (eg node
   * is grayed because some childs are checked and some not).
   *
   * @param aItem &lt;T&gt; - element to set chech state
   * @param aCheckState boolean - the check state
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsUnsupportedFeatureRtException check state is not supported in viewer
   */
  void setItemCheckState( T aItem, boolean aCheckState );

  /**
   * Sets the check state for the specified elements.
   * <p>
   * Check state will be set for elements listed in argument <code>aItems</code>. Elements of <code>aItems</code> that
   * are not present in displayed collection are ignored.
   *
   * @param aItems IList&lt;T&gt; - list of elements to chenge the check state
   * @param aCheckState boolean - the check state to be set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsUnsupportedFeatureRtException check state is not supported in viewer
   */
  void setItemsCheckState( IList<T> aItems, boolean aCheckState );

  /**
   * Sets check state for all elemets of the collection.
   *
   * @param aCheckState boolean - the check state
   * @throws TsUnsupportedFeatureRtException check state is not supported in viewer
   */
  void setAllItemsCheckState( boolean aCheckState );

  /**
   * Return the eventer of check state chenged notification.
   * <p>
   * Note: check state chenge events are generated both for programmatic and GUI user changes.
   * <p>
   * This method does not throws any exception.
   *
   * @return {@link IGenericChangeEventer} - the eventer
   */
  IGenericChangeEventer checksChangeEventer();

}

class InternalNoneCheckSupport
    implements ITsCheckSupport<Object> {

  @Override
  public boolean isChecksSupported() {
    return false;
  }

  @Override
  public boolean getItemCheckState( Object aItem ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public IList<Object> listCheckedItems( boolean aCheckState ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setItemCheckState( Object aItem, boolean aCheckState ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setItemsCheckState( IList<Object> aItems, boolean aCheckState ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setAllItemsCheckState( boolean aCheckState ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public IGenericChangeEventer checksChangeEventer() {
    throw new TsNullObjectErrorRtException();
  }

}
