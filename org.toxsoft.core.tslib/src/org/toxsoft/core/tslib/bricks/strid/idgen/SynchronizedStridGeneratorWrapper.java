package org.toxsoft.core.tslib.bricks.strid.idgen;

import java.util.concurrent.locks.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Thread-safe wrapper over {@link IStridGenerator} implementation.
 *
 * @author hazard157
 */
public class SynchronizedStridGeneratorWrapper
    implements IStridGenerator, ITsSynchronizedWrapper {

  /**
   * Provides thread safety.
   */
  private final ReentrantReadWriteLock lock;

  private final IStridGenerator source;

  /**
   * Constructor.
   *
   * @param aSource {@link IStridGenerator} - the wrapped generator
   * @param aLock {@link ReentrantReadWriteLock} - lock object used for thread-safe access
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedStridGeneratorWrapper( IStridGenerator aSource, ReentrantReadWriteLock aLock ) {
    TsNullArgumentRtException.checkNulls( aSource, aLock );
    source = aSource;
    lock = aLock;
  }

  /**
   * Constructor.
   * <p>
   * Internally creates the new instance of {@link ReentrantReadWriteLock}.
   *
   * @param aSource {@link IStridGenerator} - the wrapped generator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedStridGeneratorWrapper( IStridGenerator aSource ) {
    this( aSource, new ReentrantReadWriteLock() );
  }

  // ------------------------------------------------------------------------------------
  // ITsSynchronizedWrapper
  //

  @Override
  public ReentrantReadWriteLock getLockObject() {
    return lock;
  }

  // ------------------------------------------------------------------------------------
  // IStridGenerator
  //

  @Override
  public String nextId() {
    lock.writeLock().lock();
    try {
      return source.nextId();
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public IOptionSet getInitialState() {
    lock.readLock().lock();
    try {
      return source.getInitialState();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public IOptionSet getState() {
    lock.readLock().lock();
    try {
      return source.getState();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public void setState( IOptionSet aState ) {
    lock.writeLock().lock();
    try {
      source.setState( aState );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public ValidationResult validateState( IOptionSet aState ) {
    lock.readLock().lock();
    try {
      return source.validateState( aState );
    }
    finally {
      lock.readLock().unlock();
    }
  }

}
