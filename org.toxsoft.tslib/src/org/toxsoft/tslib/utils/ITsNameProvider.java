package org.toxsoft.tslib.utils;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.toxsoft.tslib.bricks.strid.IStridable;

/**
 * Any items name and description provider.
 *
 * @author hazard157
 * @param <T> - type of the item
 */
public interface ITsNameProvider<T> {

  /**
   * The default provider.
   */
  @SuppressWarnings( "rawtypes" )
  ITsNameProvider DEFAULT = new InternalDefaultItemNameProvider();

  /**
   * Returns the short name of the atem.
   * <p>
   * This method may return <code>null</code> indicating that no name is provided for specific item. Other way is needed
   * to be used to get the name of the item.
   *
   * @param aItem &lt;&gt; - the item, may be <code>null</code>
   * @return String - the short name, may be <code>null</code>
   */
  String getName( T aItem );

  /**
   * Returns the description of the atem.
   * <p>
   * This method may return <code>null</code> indicating that no description is provided for specific item. Other way is
   * needed to be used to get the description of the item.
   *
   * @param aItem &lt;&gt; - the item, may be <code>null</code>
   * @return String - the description, may be <code>null</code>
   */
  default String getDescription( T aItem ) {
    return null;
  }

}

class InternalDefaultItemNameProvider
    implements ITsNameProvider<Object>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Correctly deserializes {@link ITsNameProvider#DEFAULT}.
   *
   * @return Object - always {@link ITsNameProvider#DEFAULT}
   * @throws ObjectStreamException just declaration is never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsNameProvider.DEFAULT;
  }

  @Override
  public String getName( Object aItem ) {
    if( aItem == null ) {
      return TsLibUtils.EMPTY_STRING;
    }
    if( aItem instanceof IStridable ) {
      return ((IStridable)aItem).nmName();
    }
    return aItem.toString();
  }

  @Override
  public String getDescription( Object aItem ) {
    if( aItem instanceof IStridable ) {
      return ((IStridable)aItem).description();
    }
    return null;
  }

}
