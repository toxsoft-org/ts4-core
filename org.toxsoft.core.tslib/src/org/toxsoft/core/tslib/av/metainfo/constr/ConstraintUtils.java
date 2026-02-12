package org.toxsoft.core.tslib.av.metainfo.constr;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Helper methods to work with {@link IConstraintInfo}.
 *
 * @author hazard157
 */
public class ConstraintUtils {

  /**
   * Predefined category ID: uncategorized {@link IConstraintInfo}.
   */
  public static final String CATEGID_UNCATEGORIZED = TS_ID + ".av.constr.categ.none"; //$NON-NLS-1$

  /**
   * Predefined category ID: TSLIB builtin {@link IConstraintInfo}.
   */
  public static final String CATEGID_TSLIB_BUILTIN = TS_ID + ".av.constr.categ.tslib"; //$NON-NLS-1$

  private static final IStridablesListEdit<IConstraintInfo> knownConstraintInfos = new StridablesList<>();

  static {
    for( IConstraintInfo cinf : BUILTIN_CONSTRAINT_INFOS ) {
      knownConstraintInfos.add( cinf );
    }
  }

  /**
   * Adds {@link IConstraintInfo} to the known constraints.
   *
   * @param aConstraintInfo {@link IConstraintInfo} - constraint info to register
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException constraint with the same ID already exists
   */
  public static void registerConstraint( IConstraintInfo aConstraintInfo ) {
    TsNullArgumentRtException.checkNull( aConstraintInfo );
    knownConstraintInfos.add( aConstraintInfo );
  }

  /**
   * Adds several {@link IConstraintInfo} to the known constraints at once.
   * <p>
   * If any new constraint has same ID as a registered one then methods throws an exception and does not registers any
   * constraint.
   *
   * @param aConstraintInfoes ITsCollection&lt;{@link IConstraintInfo}&gt; - constraints to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException constraint with the same ID already exists
   */
  public static void registerConstraints( ITsCollection<IConstraintInfo> aConstraintInfoes ) {
    knownConstraintInfos.addAll( aConstraintInfoes );
  }

  /**
   * Returns registered constraint informations.
   *
   * @return {@link IStridablesListEdit}&lt;{@link IConstraintInfo}&gt; - lsit of all {@link IConstraintInfo}
   */
  public static IStridablesList<IConstraintInfo> listConstraints() {
    return knownConstraintInfos;
  }

  /**
   * Finds the constraint info by constraint ID.
   *
   * @param aConstraintId String - the constraint ID
   * @return {@link IConstraintInfo} - found info or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IConstraintInfo findConstraintInfo( String aConstraintId ) {
    return knownConstraintInfos.findByKey( aConstraintId );
  }

  /**
   * No subclasses.
   */
  private ConstraintUtils() {
    // nop
  }

}
