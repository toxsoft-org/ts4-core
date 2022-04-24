package org.toxsoft.core.tslib.utils.diff;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Utility methods to compare and calculate differences between containers/collections.
 *
 * @author hazard157
 */
public class DiffUtils {

  /**
   * Compares two lists and returns difference.
   * <p>
   * <code>null</code> argument of left/right collection is considered as an empty collection.
   *
   * @param <E> - elents type in lists
   * @param aLeft {@link IList} - the left list of comparison, may be <code>null</code>
   * @param aRight {@link IList} - the right list of comparison, may be <code>null</code>
   * @return {@link IMap}&lt;{@link EDiffNature},{@link IList}&gt; - result of comparison
   */
  public static <E> IMap<EDiffNature, IList<E>> compareLists( IList<E> aLeft, IList<E> aRight ) {
    IList<E> left = aLeft != null ? aLeft : IList.EMPTY;
    IList<E> right = aRight != null ? aRight : IList.EMPTY;

    // TODO реализовать DiffUtils.compareLists()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareLists()" );
  }

  public static <E extends IStridable> IMap<EDiffNature, IStridablesList<E>> compareStridablesLists(
      IStridablesList<E> aLeft, IStridablesList<E> aRight ) {
    // TODO реализовать DiffUtils.compareStridablesLists()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareStridablesLists()" );
  }

  public static <K, V> IMap<EDiffNature, IList<K>> compareMaps( IMap<K, V> aLeft, IMap<K, V> aRight ) {
    // TODO реализовать DiffUtils.compareMaps()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareMaps()" );
  }

  public static <V> IMap<EDiffNature, IIntList> compareIntMaps( IIntMap<V> aLeft, IIntMap<V> aRight ) {
    // TODO реализовать DiffUtils.compareMaps()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareMaps()" );
  }

  public static <V> IMap<EDiffNature, ILongList> compareLongMaps( ILongMap<V> aLeft, ILongMap<V> aRight ) {
    // TODO реализовать DiffUtils.compareMaps()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareMaps()" );
  }

  public static <V> IMap<EDiffNature, IStringList> compareStringMaps( IStringMap<V> aLeft, IStringMap<V> aRight ) {
    // TODO реализовать DiffUtils.compareMaps()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareMaps()" );
  }

  public static <K, V> IMap<EDiffNature, IList<K>> compareMapsEx( IList<K> aExKeys, IMap<K, V> aLeft,
      IMap<K, V> aRight ) {
    // TODO реализовать DiffUtils.compareMapsEx()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareMapsEx()" );
  }

  public static <V> IMap<EDiffNature, IIntList> compareIntMapsEx( IIntList aExKeys, IIntMap<V> aLeft,
      IIntMap<V> aRight ) {
    // TODO реализовать DiffUtils.compareMaps()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareMaps()" );
  }

  public static <V> IMap<EDiffNature, ILongList> compareLongMapsEx( ILongList aExKeys, ILongMap<V> aLeft,
      ILongMap<V> aRight ) {
    // TODO реализовать DiffUtils.compareMaps()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareMaps()" );
  }

  public static <V> IMap<EDiffNature, IStringList> compareStringMapsEx( IStringList aExKeys, IStringMap<V> aLeft,
      IStringMap<V> aRight ) {
    // TODO реализовать DiffUtils.compareMaps()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareMaps()" );
  }

  /**
   * No subclassing.
   */
  private DiffUtils() {
    // nop
  }

}
