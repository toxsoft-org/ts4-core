package org.toxsoft.core.tslib.coll.synch;

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.basis.ITsSynchronizedCollectionWrapper;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Класс потоко-безопасной оболочки над НЕредактируемым списком строк.
 *
 * @author goga
 * @version $id$
 * @param <L> - тип НЕредактируемого списка-источника
 */
public class SynchronizedStringList<L extends IStringList>
    implements IStringList, ITsSynchronizedCollectionWrapper<String>, Serializable {

  private static final long serialVersionUID = 157157L;

  protected final ReentrantReadWriteLock lock;
  protected final L                      source;

  /**
   * Создает оболочку над aSource с потоко-безопасным доступом.
   *
   * @param aSource L - список - источник
   * @throws TsNullArgumentRtException аргумент = null
   */
  public SynchronizedStringList( L aSource ) {
    this( aSource, new ReentrantReadWriteLock() );
  }

  /**
   * Создает оболочку над aSource с потоко-безопасным доступом с указанием блокировки.
   *
   * @param aSource L - список - источник
   * @param aLock {@link ReentrantReadWriteLock} - блокировка списка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public SynchronizedStringList( L aSource, ReentrantReadWriteLock aLock ) {
    TsNullArgumentRtException.checkNulls( aSource, aLock );
    source = aSource;
    lock = aLock;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IStringList
  //

  @Override
  public String get( int aIndex ) {
    lock.readLock().lock();
    try {
      return source.get( aIndex );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean hasElem( String aString ) {
    lock.readLock().lock();
    try {
      return source.hasElem( aString );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public int indexOf( String aString ) {
    lock.readLock().lock();
    try {
      return source.indexOf( aString );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean isEmpty() {
    lock.readLock().lock();
    try {
      return source.isEmpty();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public int size() {
    lock.readLock().lock();
    try {
      return source.size();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public String[] toArray() {
    lock.readLock().lock();
    try {
      return source.toArray();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public String[] toArray( String[] aSrcArray ) {
    lock.readLock().lock();
    try {
      return source.toArray( aSrcArray );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Iterator<String> iterator() {
    lock.readLock().lock();
    try {
      return source.iterator();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsSynchronizedCollectionTag
  //

  @Override
  public ReentrantReadWriteLock getLockObject() {
    return lock;
  }

  @Override
  public ITsCollection<String> getSourceCollection() {
    return source;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    lock.readLock().lock();
    try {
      return source.toString();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this || aObj == source ) {
      return true;
    }
    lock.readLock().lock();
    try {
      return source.equals( aObj );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public int hashCode() {
    lock.readLock().lock();
    try {
      return source.hashCode();
    }
    finally {
      lock.readLock().unlock();
    }
  }

}
