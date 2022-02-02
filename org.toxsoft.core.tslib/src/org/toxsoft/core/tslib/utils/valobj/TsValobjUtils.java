package org.toxsoft.core.tslib.utils.valobj;

import static org.toxsoft.core.tslib.utils.valobj.ITsResources.*;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.toxsoft.core.tslib.av.impl.AtomicValueKeeper;
import org.toxsoft.core.tslib.av.list.AvList;
import org.toxsoft.core.tslib.av.opset.impl.OptionSetKeeper;
import org.toxsoft.core.tslib.av.temporal.TemporalAtomicValueKeeper;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.coll.IMapEdit;
import org.toxsoft.core.tslib.coll.impl.ElemMap;
import org.toxsoft.core.tslib.coll.primtypes.IStringMap;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Value objects support in tslib.
 *
 * @author hazard157
 */
public class TsValobjUtils {

  /**
   * Lock object to syncronize access to keepers registry {@link #keepersMap}.
   */
  private static ReentrantReadWriteLock mainLock = new ReentrantReadWriteLock();

  /**
   * Keepers registry as map "keeper ID" - "keeper".
   */
  private static final IStringMapEdit<IEntityKeeper<?>> keepersMap = new StringMap<>();

  /**
   * The same keepers by class keys.
   */
  private static final IMapEdit<Class<?>, IEntityKeeper<?>> keepersMapByClass = new ElemMap<>();

  /**
   * The same keeper ids by class keys.
   */
  private static final IMapEdit<Class<?>, String> idsMapByClass = new ElemMap<>();

  static {
    registerKeeper( OptionSetKeeper.KEEPER_ID, OptionSetKeeper.KEEPER );
    registerKeeper( FileKeeper.KEEPER_ID, FileKeeper.KEEPER );
    registerKeeper( StringKeeper.KEEPER_ID, StringKeeper.KEEPER );
    // registerKeeper( LegacyStringListKeeper.KEEPER_ID, LegacyStringListKeeper.KEEPER );
    registerKeeper( StringListKeeper.KEEPER_ID, StringListKeeper.KEEPER );
    registerKeeper( AtomicValueKeeper.KEEPER_ID, AtomicValueKeeper.KEEPER );
    registerKeeper( AvList.KEEPER_ID, AvList.KEEPER );
    registerKeeper( TemporalAtomicValueKeeper.KEEPER_ID, TemporalAtomicValueKeeper.KEEPER );
    registerKeeper( TsVersion.KEEPER_ID, TsVersion.KEEPER );
    registerKeeper( ESortOrder.KEEPER_ID, ESortOrder.KEEPER );
    registerKeeper( LocaleKeeper.KEEPER_ID, LocaleKeeper.KEEPER );
    registerKeeper( LocalDateKeeper.KEEPER_ID, LocalDateKeeper.KEEPER );
    registerKeeper( LocalTimeKeeper.KEEPER_ID, LocalTimeKeeper.KEEPER );
    registerKeeper( LocalDateTimeKeeper.KEEPER_ID, LocalDateTimeKeeper.KEEPER );
    registerKeeper( ELockState.KEEPER_ID, ELockState.KEEPER );
  }

  // ------------------------------------------------------------------------------------
  // static thread-safe API for keepers management
  //

  /**
   * Return the copy of the registered keepers.
   *
   * @return {@link IStringMap}&lt;{@link IEntityKeeper}&gt; - the map "keeper ID" - "the keeper"
   */
  public static IStringMap<IEntityKeeper<?>> getRegisteredKeepers() {
    IStringMapEdit<IEntityKeeper<?>> map = new StringMap<>();
    mainLock.readLock().lock();
    try {
      map.putAll( keepersMap );
    }
    finally {
      mainLock.readLock().unlock();
    }
    return map;
  }

  /**
   * Registers the keeper.
   *
   * @param aKeeperId String - the key, IDPath identifier
   * @param aKeeper {@link IEntityKeeper} - the keeper to be registered
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException keeper with specified key was already registered
   * @throws TsItemAlreadyExistsRtException keeper for class was already registered
   * @throws TsIllegalArgumentRtException identifier is not an IDPath
   */
  public static void registerKeeper( String aKeeperId, IEntityKeeper<?> aKeeper ) {
    TsNullArgumentRtException.checkNulls( aKeeperId, aKeeper );
    mainLock.writeLock().lock();
    try {
      TsItemAlreadyExistsRtException.checkTrue( keepersMap.hasKey( aKeeperId ) );
      if( aKeeper.entityClass() != null ) {
        TsItemAlreadyExistsRtException.checkTrue( keepersMapByClass.hasKey( aKeeper.entityClass() ) );
        keepersMapByClass.put( aKeeper.entityClass(), aKeeper );
        idsMapByClass.put( aKeeper.entityClass(), aKeeperId );
      }
      keepersMap.put( aKeeperId, aKeeper );
    }
    finally {
      mainLock.writeLock().unlock();
    }
  }

  /**
   * Registers the keeper if keeper with such ID is not registered already.
   *
   * @param aKeeperId String - the key, IDPath identifier
   * @param aKeeper {@link IEntityKeeper} - the keeper to be registered
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDPath
   */
  public static void registerKeeperIfNone( String aKeeperId, IEntityKeeper<?> aKeeper ) {
    TsNullArgumentRtException.checkNulls( aKeeperId, aKeeper );
    mainLock.writeLock().lock();
    try {
      if( !keepersMap.hasKey( aKeeperId ) ) {
        if( aKeeper.entityClass() != null ) {
          keepersMapByClass.put( aKeeper.entityClass(), aKeeper );
          idsMapByClass.put( aKeeper.entityClass(), aKeeperId );
        }
        keepersMap.put( aKeeperId, aKeeper );
      }
    }
    finally {
      mainLock.writeLock().unlock();
    }
  }

  /**
   * Returns the keeper by identifier.
   *
   * @param aKeeperId String - keeper identifier
   * @return {@link IEntityKeeper} - found keeper
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no keeper was registered with specified identifier
   */
  public static IEntityKeeper<?> getKeeperById( String aKeeperId ) {
    TsNullArgumentRtException.checkNull( aKeeperId );
    IEntityKeeper<?> keeper = null;
    mainLock.readLock().lock();
    try {
      keeper = keepersMap.findByKey( aKeeperId );
    }
    finally {
      mainLock.readLock().unlock();
    }
    TsItemNotFoundRtException.checkNull( keeper, FMT_ERR_NO_KEEPER_BY_ID, aKeeperId );
    return keeper;
  }

  /**
   * Returns the registered keepers identifier or <code>null</code> if keeper is not registered.
   *
   * @param aKeeper {@link IEntityKeeper} - specified keeper
   * @return String - keeper identifier (also it is registration key) or <code>null</code>
   */
  public static String findIdByKeeper( IEntityKeeper<?> aKeeper ) {
    TsNullArgumentRtException.checkNull( aKeeper );
    mainLock.readLock().lock();
    try {
      for( int i = 0, count = keepersMap.size(); i < count; i++ ) {
        if( keepersMap.values().get( i ).equals( aKeeper ) ) {
          return keepersMap.keys().get( i );
        }
      }
      return null;
    }
    finally {
      mainLock.readLock().unlock();
    }
  }

  /**
   * Finds the keeper by identifier.
   *
   * @param aKeeperId String - keeper identifier
   * @return {@link IEntityKeeper} - found keeper or <code>null</code>
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static IEntityKeeper<?> findKeeperById( String aKeeperId ) {
    TsNullArgumentRtException.checkNull( aKeeperId );
    mainLock.readLock().lock();
    try {
      return keepersMap.findByKey( aKeeperId );
    }
    finally {
      mainLock.readLock().unlock();
    }
  }

  /**
   * Returns the keeper identifier by entity class.
   *
   * @param aEntityClass {@link Class} - the specified class
   * @return String - found keeper identifier
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no keeper was registered for the specified class
   */
  public static String getKeeperIdByClass( Class<?> aEntityClass ) {
    TsNullArgumentRtException.checkNull( aEntityClass );
    String keeperId = null;
    mainLock.readLock().lock();
    try {
      keeperId = idsMapByClass.findByKey( aEntityClass );
      if( keeperId == null ) {
        for( Class<?> clz : idsMapByClass.keys() ) {
          if( clz.isAssignableFrom( aEntityClass ) ) {
            return idsMapByClass.getByKey( clz );
          }
        }
      }
    }
    finally {
      mainLock.readLock().unlock();
    }
    TsItemNotFoundRtException.checkNull( keeperId, FMT_ERR_NO_KEEPER_BY_CLASS, aEntityClass.getName() );
    return keeperId;
  }

  /**
   * Finds the keeper identifier by entity class.
   *
   * @param aEntityClass {@link Class} - the specified class
   * @return String - found keeper identifier
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static String findKeeperIdByClass( Class<?> aEntityClass ) {
    TsNullArgumentRtException.checkNull( aEntityClass );
    mainLock.readLock().lock();
    try {
      return idsMapByClass.findByKey( aEntityClass );
    }
    finally {
      mainLock.readLock().unlock();
    }
  }

  /**
   * Returns the keeper by entity class.
   *
   * @param aEntityClass {@link Class} - the specified class
   * @return {@link IEntityKeeper} - found keeper
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no keeper was registered for the specified class
   */
  public static IEntityKeeper<?> getKeeperByClass( Class<?> aEntityClass ) {
    TsNullArgumentRtException.checkNull( aEntityClass );
    IEntityKeeper<?> keeper = null;
    mainLock.readLock().lock();
    try {
      keeper = keepersMapByClass.findByKey( aEntityClass );
    }
    finally {
      mainLock.readLock().unlock();
    }
    TsItemNotFoundRtException.checkNull( keeper, FMT_ERR_NO_KEEPER_BY_CLASS, aEntityClass.getName() );
    return keeper;
  }

  /**
   * Finds the keeper by entity class.
   *
   * @param aEntityClass {@link Class} - the specified class
   * @return {@link IEntityKeeper} - found keeper or <code>null</code>
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static IEntityKeeper<?> findKeeperByClass( Class<?> aEntityClass ) {
    TsNullArgumentRtException.checkNull( aEntityClass );
    mainLock.readLock().lock();
    try {
      return keepersMapByClass.findByKey( aEntityClass );
    }
    finally {
      mainLock.readLock().unlock();
    }
  }

  private TsValobjUtils() {
    // nop
  }

}
