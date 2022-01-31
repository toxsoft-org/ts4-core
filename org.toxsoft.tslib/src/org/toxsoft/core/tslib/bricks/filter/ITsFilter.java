package org.toxsoft.core.tslib.bricks.filter;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullObjectErrorRtException;

/**
 * General purpose filer - accepts or declines objects.
 *
 * @author hazard157
 * @param <T> - type of filtered objects
 */
@SuppressWarnings( "rawtypes" )
public interface ITsFilter<T> {

  /**
   * "null" filter singleton, {@link #accept(Object)} always throws {@link TsNullObjectErrorRtException} exception.
   */
  ITsFilter NULL = new InternalNullTsFilter();

  /**
   * None objects accepted filter singleton, {@link #accept(Object)} always returns <code>false</code>.
   */
  ITsFilter NONE = new InternalNoneTsFilter();

  /**
   * All objects accepted filter singleton, {@link #accept(Object)} always returns <code>true</code>.
   */
  ITsFilter ALL = new InternalAllTsFilter();

  /**
   * Checks if specified object is accepted by the filter.
   * <p>
   * Concrete implementations must declare if <code>null</code> argument throws {@link TsNullArgumentRtException}.
   *
   * @param aObj &lt;T&gt; - object to be accepted or declined
   * @return boolean - accpetance check state<br>
   *         <b>true</b> - object is accepted by the filter;<br>
   *         <b>false</b> - object does not passes filter, it is declined.
   * @throws TsNullArgumentRtException may be thrown by concrete implementation
   */
  boolean accept( T aObj );

}

/**
 * Internal class for {@link ITsFilter#NULL} singleton implementation.
 *
 * @author hazard157
 * @param <T> - type of filtered objects
 */
class InternalNullTsFilter<T>
    implements ITsFilter<T>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ITsFilter#NULL} will be read correctly.
   *
   * @return Object - always {@link ITsFilter#NULL}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsFilter.NULL;
  }

  @Override
  public boolean accept( T aObj ) {
    throw new TsNullObjectErrorRtException();
  }

}

/**
 * Internal class for {@link ITsFilter#NONE} singleton implementation.
 *
 * @author hazard157
 * @param <T> - type of filtered objects
 */
class InternalNoneTsFilter<T>
    implements ITsFilter<T>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ITsFilter#NONE} will be read correctly.
   *
   * @return Object - always {@link ITsFilter#NONE}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsFilter.NONE;
  }

  @Override
  public boolean accept( T aObj ) {
    return false;
  }

}

/**
 * Internal class for {@link ITsFilter#ALL} singleton implementation.
 *
 * @author hazard157
 * @param <T> - type of filtered objects
 */
class InternalAllTsFilter<T>
    implements ITsFilter<T>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ITsFilter#ALL} will be read correctly.
   *
   * @return Object - always {@link ITsFilter#ALL}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsFilter.ALL;
  }

  @Override
  public boolean accept( T aObj ) {
    return true;
  }

}
