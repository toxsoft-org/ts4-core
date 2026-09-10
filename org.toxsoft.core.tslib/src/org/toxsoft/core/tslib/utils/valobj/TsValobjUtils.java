package org.toxsoft.core.tslib.utils.valobj;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.util.concurrent.locks.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.list.*;
import org.toxsoft.core.tslib.av.misc.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.temporal.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.math.cond.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.diff.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.login.*;
import org.toxsoft.core.tslib.utils.logs.*;
import org.toxsoft.core.tslib.utils.plugins.*;
import org.toxsoft.core.tslib.utils.txtmatch.*;

/**
 * Value objects support in tslib.
 *
 * @author hazard157
 */
public class TsValobjUtils {

  /**
   * Lock object to synchronize access to keepers registry {@link #dataMap}.
   */
  private static ReentrantReadWriteLock mainLock = new ReentrantReadWriteLock();

  /**
   * Registered value-oebjcts.
   */
  private static final IStridablesListBasicEdit<TsValobjRegData> dataMap = new SortedStridablesList<>();

  static {
    registerKeeper( EAtomicType.KEEPER_ID, EAtomicType.KEEPER );
    registerKeeper( OptionSetKeeper.KEEPER_ID, OptionSetKeeper.KEEPER );
    registerKeeper( FileKeeper.KEEPER_ID, FileKeeper.KEEPER, FileKeeper.INFO );
    registerKeeper( StringKeeper.KEEPER_ID, StringKeeper.KEEPER );
    registerKeeper( IntegerKeeper.KEEPER_ID, IntegerKeeper.KEEPER );
    // registerKeeper( LegacyStringListKeeper.KEEPER_ID, LegacyStringListKeeper.KEEPER );
    registerKeeper( StringListKeeper.KEEPER_ID, StringListKeeper.KEEPER );
    registerKeeper( AtomicValueKeeper.KEEPER_ID, AtomicValueKeeper.KEEPER );
    registerKeeper( DataType.KEEPER_ID, DataType.KEEPER );
    registerKeeper( AvList.KEEPER_ID, AvList.KEEPER );
    registerKeeper( TemporalAtomicValueKeeper.KEEPER_ID, TemporalAtomicValueKeeper.KEEPER );
    registerKeeper( TsVersion.KEEPER_ID, TsVersion.KEEPER );
    registerKeeper( ESortOrder.KEEPER_ID, ESortOrder.KEEPER );
    registerKeeper( LocaleKeeper.KEEPER_ID, LocaleKeeper.KEEPER );
    registerKeeper( LocalDateKeeper.KEEPER_ID, LocalDateKeeper.KEEPER );
    registerKeeper( LocalTimeKeeper.KEEPER_ID, LocalTimeKeeper.KEEPER );
    registerKeeper( LocalDateTimeKeeper.KEEPER_ID, LocalDateTimeKeeper.KEEPER );
    registerKeeper( ELockState.KEEPER_ID, ELockState.KEEPER );
    registerKeeper( DataDefKeeper.KEEPER_ID, DataDefKeeper.KEEPER );
    registerKeeper( Skid.KEEPER_ID, Skid.KEEPER );
    registerKeeper( SkidListKeeper.KEEPER_ID, SkidListKeeper.KEEPER );
    registerKeeper( EGwidKind.KEEPER_ID, EGwidKind.KEEPER );
    registerKeeper( Gwid.KEEPER_ID, Gwid.KEEPER );
    registerKeeper( GwidList.KEEPER_ID, GwidList.KEEPER );
    registerKeeper( MappedSkids.KEEPER_ID, MappedSkids.KEEPER );
    registerKeeper( ELogSeverity.KEEPER_ID, ELogSeverity.KEEPER );
    registerKeeper( CollConstraint.KEEPER_ID, CollConstraint.KEEPER );
    registerKeeper( ECrudOp.KEEPER_ID, ECrudOp.KEEPER );
    registerKeeper( ETextMatchMode.KEEPER_ID, ETextMatchMode.KEEPER );
    registerKeeper( TsRectangleKeeper.KEEPER_ID, TsRectangleKeeper.KEEPER );
    registerKeeper( TsPointKeeper.KEEPER_ID, TsPointKeeper.KEEPER );
    registerKeeper( TsDims.KEEPER_ID, TsDims.KEEPER );
    registerKeeper( IdValue.KEEPER_ID, IdValue.KEEPER );
    registerKeeper( IdChain.KEEPER_ID, IdChain.KEEPER );
    registerKeeper( D2Point.KEEPER_ID, D2Point.KEEPER );
    registerKeeper( D2Vector.KEEPER_ID, D2Vector.KEEPER );
    registerKeeper( D2Size.KEEPER_ID, D2Size.KEEPER );
    registerKeeper( D2Angle.KEEPER_ID, D2Angle.KEEPER );
    registerKeeper( D2Rotation.KEEPER_ID, D2Rotation.KEEPER );
    registerKeeper( D2Conversion.KEEPER_ID, D2Conversion.KEEPER );
    registerKeeper( D2GeomData.KEEPER_ID, D2GeomData.KEEPER );
    registerKeeper( IntListKeeper.KEEPER_ID, IntListKeeper.KEEPER );
    registerKeeper( LongListKeeper.KEEPER_ID, LongListKeeper.KEEPER );
    registerKeeper( EValidationResultType.KEEPER_ID, EValidationResultType.KEEPER );
    registerKeeper( ValidationResult.KEEPER_ID, ValidationResult.KEEPER );
    registerKeeper( IntRange.KEEPER_ID, IntRange.KEEPER );
    registerKeeper( LongRange.KEEPER_ID, LongRange.KEEPER );
    registerKeeper( DoubleRange.KEEPER_ID, DoubleRange.KEEPER );
    registerKeeper( LoginInfo.KEEPER_ID, LoginInfo.KEEPER );
    registerKeeper( EDiffNature.KEEPER_ID, EDiffNature.KEEPER );
    registerKeeper( TsCombiCondInfo.KEEPER_ID, TsCombiCondInfo.KEEPER );
    registerKeeper( PluginFileRequirement.KEEPER_ID, PluginFileRequirement.KEEPER );
    registerKeeper( Ugwi.KEEPER_ID, Ugwi.KEEPER );
    registerKeeper( ED2Quadrant.KEEPER_ID, ED2Quadrant.KEEPER );
    registerKeeper( EQueryParamUsage.KEEPER_ID, EQueryParamUsage.KEEPER );
    registerKeeper( EGwidSelectionOption.KEEPER_ID, EGwidSelectionOption.KEEPER );
    registerKeeper( TsCombiFilterParamsKeeper.KEEPER_ID, TsCombiFilterParamsKeeper.KEEPER );

    print();

  }

  private static final void print() {
    TsTestUtils.pl( "Data map content:" );
    for( int i = 0; i < dataMap.size(); i++ ) {
      String key = dataMap.keys().get( i );
      TsValobjRegData value = dataMap.values().get( i );
      TsTestUtils.pl( "  %20s = %s", key, value.id() );
    }
    TsTestUtils.nl();
    TsTestUtils.nl();
    TsTestUtils.nl();
  }

  // ------------------------------------------------------------------------------------
  // NEW STUFF
  //

  /**
   * Return the copy of the registered {@link TsValobjRegData} descriptions.
   *
   * @return {@link IStringMap}&lt;{@link TsValobjRegData}&gt; - the map "keeper ID" - "the info"
   */
  public static IStridablesList<TsValobjRegData> listRegistered() {
    mainLock.readLock().lock();
    try {
      return new StridablesList<>( dataMap );
    }
    finally {
      mainLock.readLock().unlock();
    }
  }

  /**
   * Registers the the value-object with full meta information.
   *
   * @param aRegData {@link TsValobjRegData} - registration information
   * @return {@link TsValobjRegData} - the argument is returned
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException keeper with specified key was already registered
   * @throws TsItemAlreadyExistsRtException keeper for class was already registered
   */
  public static TsValobjRegData registerValobj( TsValobjRegData aRegData ) {
    TsNullArgumentRtException.checkNulls( aRegData );
    mainLock.writeLock().lock();
    try {
      TsItemAlreadyExistsRtException.checkTrue( dataMap.hasKey( aRegData.id() ) );
      dataMap.put( aRegData.id(), aRegData );
      return aRegData;
    }
    finally {
      mainLock.writeLock().unlock();
    }
  }

  /**
   * Registers the value-object if was not registered already.
   *
   * @param aRegData {@link TsValobjRegData} - registration information
   * @return {@link TsValobjRegData} - registration data existing or an argument
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDPath
   */
  public static TsValobjRegData registerValobjIfNone( TsValobjRegData aRegData ) {
    TsNullArgumentRtException.checkNull( aRegData );
    mainLock.writeLock().lock();
    try {
      if( !dataMap.hasKey( aRegData.id() ) ) {
        dataMap.put( aRegData.id(), aRegData );
        return aRegData;
      }
      return dataMap.getByKey( aRegData.id() );
    }
    finally {
      mainLock.writeLock().unlock();
    }
  }

  /**
   * Registers the value-object with minimum mandatory data.
   * <p>
   * Creates the registration data with {@link TsValobjRegData#nmName()} = <code>aValobjId</code>, no description and no
   * iconID.
   *
   * @param aValobjId String - the key, IDPath identifier
   * @param aKeeper {@link IEntityKeeper} - the keeper to be registered
   * @return {@link TsValobjRegData} - created registration data
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException keeper with specified key was already registered
   * @throws TsItemAlreadyExistsRtException keeper for class was already registered
   * @throws TsIllegalArgumentRtException identifier is not an IDPath
   */
  public static TsValobjRegData registerValobj( String aValobjId, IEntityKeeper<?> aKeeper ) {
    TsValobjRegData regData = new TsValobjRegData( aValobjId, aKeeper, aValobjId, EMPTY_STRING );
    return registerValobj( regData );
  }

  /**
   * Registers the value-object if was not registered already.
   * <p>
   * Creates the registration data with {@link TsValobjRegData#nmName()} = <code>aValobjId</code>, no description and no
   * iconID.
   *
   * @param aValobjId String - the key, IDPath identifier
   * @param aKeeper {@link IEntityKeeper} - the keeper to be registered
   * @return {@link TsValobjRegData} - created registration data
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDPath
   */
  public static TsValobjRegData registerValobjIfNone( String aValobjId, IEntityKeeper<?> aKeeper ) {
    TsValobjRegData regData = new TsValobjRegData( aValobjId, aKeeper, aValobjId, EMPTY_STRING );
    return registerValobjIfNone( regData );
  }

  /**
   * Finds the {@link TsValobjRegData} by entity class.
   *
   * @param aEntityClass {@link Class} - the specified class
   * @return {@link TsValobjRegData} - found info or <code>null</code>
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static TsValobjRegData findValobjByClass( Class<?> aEntityClass ) {
    TsNullArgumentRtException.checkNull( aEntityClass );
    mainLock.readLock().lock();
    try {
      for( TsValobjRegData d : dataMap ) {
        if( d.keeper().entityClass().equals( aEntityClass ) ) {
          return d;
        }
      }
      return null;
    }
    finally {
      mainLock.readLock().unlock();
    }
  }

  /**
   * Returns the {@link TsValobjRegData} by entity class or throws an exception.
   *
   * @param aEntityClass {@link Class} - the specified class
   * @return {@link TsValobjRegData} - found info or <code>null</code>
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static TsValobjRegData getValobjByClass( Class<?> aEntityClass ) {
    return TsItemNotFoundRtException.checkNull( findValobjByClass( aEntityClass ) );
  }

  /**
   * Finds the {@link TsValobjRegData} by keeper identifier.
   *
   * @param aValobjId String - valobj identifier
   * @return {@link TsValobjRegData} - found info or <code>null</code>
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static TsValobjRegData findValobjById( String aValobjId ) {
    TsNullArgumentRtException.checkNull( aValobjId );
    mainLock.readLock().lock();
    try {
      return dataMap.findByKey( aValobjId );
    }
    finally {
      mainLock.readLock().unlock();
    }
  }

  /**
   * Returns the {@link TsValobjRegData} by identifier or throws an exception.
   *
   * @param aValobjId String - valobj identifier
   * @return {@link TsValobjRegData} - found info or <code>null</code>
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException valobj data not found
   */
  public static TsValobjRegData getValobjById( String aValobjId ) {
    return TsItemNotFoundRtException.checkNull( findValobjById( aValobjId ) );
  }

  /**
   * Removes previously registered value-object.
   * <p>
   * If value-object with ID <code>aValobjId</code> is not registered then method does nothing.
   *
   * @param aValobjId String - the value-object ID
   * @return boolean - determines if keeper was really unregistered<br>
   *         <b>true</b> - there was registered keeper with the specified ID and is is not registered any more ;<br>
   *         <b>false</b> - nothing has been changed in registered keepers list.
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean unregisterValobj( String aValobjId ) {
    TsNullArgumentRtException.checkNull( aValobjId );
    mainLock.readLock().lock();
    try {
      return dataMap.removeById( aValobjId ) != null;
    }
    finally {
      mainLock.readLock().unlock();
    }
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
      IStringMapEdit<IEntityKeeper<?>> mmKeepers = new StringMap<>();
      for( TsValobjRegData d : dataMap ) {
        mmKeepers.put( d.id(), d.keeper() );
      }
    }
    finally {
      mainLock.readLock().unlock();
    }
    return map;
  }

  /**
   * Return the copy of the registered {@link ValobjInfo} descriptions.
   *
   * @return {@link IStringMap}&lt;{@link ValobjInfo}&gt; - the map "keeper ID" - "the info"
   */
  public static IStringMap<ValobjInfo> getRegisteredInfos() {
    IStringMapEdit<ValobjInfo> map = new StringMap<>();
    mainLock.readLock().lock();
    try {
      for( TsValobjRegData d : dataMap ) {
        map.put( d.id(), new ValobjInfo( d.nmName(), d.description() ) );
      }
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
   * @param aInfo {@link ValobjInfo} - optional information, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException keeper with specified key was already registered
   * @throws TsItemAlreadyExistsRtException keeper for class was already registered
   * @throws TsIllegalArgumentRtException identifier is not an IDPath
   */
  public static void registerKeeper( String aKeeperId, IEntityKeeper<?> aKeeper, ValobjInfo aInfo ) {
    String name = aKeeperId;
    String description = EMPTY_STRING;
    if( aInfo != null ) {
      name = aInfo.name();
      description = aInfo.description();
    }
    TsValobjRegData regData = new TsValobjRegData( aKeeperId, aKeeper, name, description );
    registerValobj( regData );
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
    registerKeeper( aKeeperId, aKeeper, null );
  }

  /**
   * Registers the keeper if keeper with such ID is not registered already.
   *
   * @param aKeeperId String - the key, IDPath identifier
   * @param aKeeper {@link IEntityKeeper} - the keeper to be registered
   * @param aInfo {@link ValobjInfo} - optional information, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDPath
   */
  public static void registerKeeperIfNone( String aKeeperId, IEntityKeeper<?> aKeeper, ValobjInfo aInfo ) {
    String name = aKeeperId;
    String description = EMPTY_STRING;
    if( aInfo != null ) {
      name = aInfo.name();
      description = aInfo.description();
    }
    TsValobjRegData regData = new TsValobjRegData( aKeeperId, aKeeper, name, description );
    registerValobjIfNone( regData );
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
    registerKeeperIfNone( aKeeperId, aKeeper, null );
  }

  /**
   * Removes previously registered keeper.
   * <p>
   * If keeper with ID <code>aKeeperId</code> is not registered then method does nothing.
   * <p>
   * Note: <code>aKeeper</code> must be exactly the same reference (checked by == operator) as registered one, itherwise
   * method does nothing.
   *
   * @param aKeeperId String - the key, IDPath identifier
   * @param aKeeper {@link IEntityKeeper} - the keeper
   * @return boolean - determines if keeper was really unregistered<br>
   *         <b>true</b> - there was registered keeper with the specified ID and is is not registered any more ;<br>
   *         <b>false</b> - nothing has been changed in registered keepers list.
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean unregisterKeeper( String aKeeperId, IEntityKeeper<?> aKeeper ) {
    TsNullArgumentRtException.checkNulls( aKeeperId, aKeeper );
    mainLock.writeLock().lock();
    try {
      TsValobjRegData found = dataMap.findByKey( aKeeperId );
      if( found != null && found.keeper() == aKeeper ) {
        dataMap.removeByKey( aKeeperId );
        return true;
      }
      return false;
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
    return getValobjById( aKeeperId ).keeper();
  }

  /**
   * Finds registered keeper identifier by reference to the keeper.
   *
   * @param aKeeper {@link IEntityKeeper} - specified keeper
   * @return String - keeper identifier (also it is registration key) or <code>null</code>
   */
  public static String findIdByKeeper( IEntityKeeper<?> aKeeper ) {
    TsNullArgumentRtException.checkNull( aKeeper );
    mainLock.readLock().lock();
    try {
      for( TsValobjRegData d : dataMap ) {
        if( d.keeper().equals( aKeeper ) ) {
          return d.id();
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
    TsValobjRegData d = findValobjById( aKeeperId );
    return d != null ? d.keeper() : null;
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
    return getValobjByClass( aEntityClass ).id();
  }

  /**
   * Finds the keeper identifier by entity class.
   *
   * @param aEntityClass {@link Class} - the specified class
   * @return String - found keeper identifier
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static String findKeeperIdByClass( Class<?> aEntityClass ) {
    TsValobjRegData d = findValobjByClass( aEntityClass );
    return d != null ? d.id() : null;
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
    return getValobjByClass( aEntityClass ).keeper();
  }

  /**
   * Finds the keeper by entity class.
   *
   * @param aEntityClass {@link Class} - the specified class
   * @return {@link IEntityKeeper} - found keeper or <code>null</code>
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static IEntityKeeper<?> findKeeperByClass( Class<?> aEntityClass ) {
    TsValobjRegData d = findValobjByClass( aEntityClass );
    return d != null ? d.keeper() : null;
  }

  /**
   * Finds the {@link ValobjInfo} by keeper identifier.
   *
   * @param aKeeperId String - keeper identifier
   * @return {@link ValobjInfo} - found info or <code>null</code>
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static ValobjInfo findInfoById( String aKeeperId ) {
    TsValobjRegData d = findValobjById( aKeeperId );
    if( d == null ) {
      return null;
    }
    return new ValobjInfo( d.nmName(), d.description() );
  }

  /**
   * Returns the {@link ValobjInfo} by keeper identifier or {@link ValobjInfo#EMPTY} if not found
   *
   * @param aKeeperId String - keeper identifier
   * @return {@link ValobjInfo} - found info or <code>null</code>
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static ValobjInfo getInfoById( String aKeeperId ) {
    return TsItemNotFoundRtException.checkNull( findInfoById( aKeeperId ) );
  }

  private TsValobjUtils() {
    // nop
  }

}
