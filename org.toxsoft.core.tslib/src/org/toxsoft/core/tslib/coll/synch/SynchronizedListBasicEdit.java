package org.toxsoft.core.tslib.coll.synch;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.toxsoft.core.tslib.coll.IListBasicEdit;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Потоко-безопасная оболочка над базовым редактируемым списком {@link IListBasicEdit}.
 *
 * @author hazard157
 * @version $id$
 * @param <E> - тип элементов списка
 */
public class SynchronizedListBasicEdit<E>
    extends SynchronizedListBase<E, IListBasicEdit<E>> {

  private static final long serialVersionUID = 157157L;

  /**
   * Создает соболочку над aSource с потоко-безопасным доступом.
   *
   * @param aSource IListBasicEdit&lt;E&gt; - список - источник
   * @throws TsNullArgumentRtException аргумент = null
   */
  public SynchronizedListBasicEdit( IListBasicEdit<E> aSource ) {
    super( aSource );
  }

  /**
   * Создает оболочку над aSource с потоко-безопасным доступом с указанием блокировки.
   *
   * @param aSource IListBasicEdit&lt;E&gt; - список - источник
   * @param aLock {@link ReentrantReadWriteLock} - блокировка списка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public SynchronizedListBasicEdit( IListBasicEdit<E> aSource, ReentrantReadWriteLock aLock ) {
    super( aSource, aLock );
  }

}
