package org.toxsoft.core.tsgui.ved.screen.helpers;

import java.util.*;

import org.toxsoft.core.tslib.coll.*;

/**
 * Вспомогательный класс для перебора элементов списка в обратном порядке.
 *
 * @author vs
 * @param <T> - тип элементов списка
 */
public class ListBackwardIterator<T>
    implements Iterator<T>, Iterable<T> {

  private final IList<T> source;

  int pointer;

  /**
   * Конструктор.
   *
   * @param aSource {@link IList} - список элементов
   */
  public ListBackwardIterator( IList<T> aSource ) {
    source = aSource;
    pointer = source.size();
  }

  // ------------------------------------------------------------------------------------
  // Iterator
  //

  @Override
  public boolean hasNext() {
    return pointer > 0;
  }

  @Override
  public T next() {
    pointer--;
    return source.get( pointer );
  }

  // ------------------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<T> iterator() {
    return this;
  }

}
