package org.toxsoft.core.tsgui.ved.api.screen;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;

/**
 * Manages the VED screen helpers in the {@link IVedScreenModel}.
 *
 * @author hazard157
 */
public interface IVedHelpersModel<T> {

  IList<T> list();

  IList<T> listAllHelpers();

  IListReorderer<T> reorderer();

  void insert( int aIndex, T aHelper );

  void remove( T aHelper );

  @SuppressWarnings( "javadoc" )
  default void add( T aHelper ) {
    insert( listAllHelpers().size(), aHelper );
  }

}
