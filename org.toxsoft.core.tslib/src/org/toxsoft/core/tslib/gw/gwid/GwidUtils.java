package org.toxsoft.core.tslib.gw.gwid;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Helper methods to work with GWIDs.
 *
 * @author hazard157
 */
public class GwidUtils {

  /**
   * Determines if <code>aGeneral</code> GWID "covers" <code>aSpecific</code> one.
   * <p>
   * More general GWID covers more specific one. More general means that expanding GWIDs in any system resulting
   * <code>aGeneral</code> GWIDs set will contain all of the GWIDs of <code>aSpecific</code> GWID expanded.
   * <p>
   * Notes:
   * <ul>
   * <li>class inheritance is not known here hence GWIDs with different class IDs always returns
   * <code>false</code>;</li>
   * <li>GWID of level (that is of kind {@link EGwidKind#GW_CLASS}) is considered to cover any properties and
   * subproperties, while GWID of levels 2 and 3 covers only GWIDs of respective kind.</li>
   * <li>abstract GWIDs are considered to be multi-object GWID, like {@link Gwid#isStridMulti()}=<code>true</code>.</li>
   * </ul>
   *
   * @param aGeneral {@link Gwid} - probably more general GWID
   * @param aSpecific {@link Gwid} - probably more specific GWID
   * @return boolean - true if this GWID is more general than <code>aThat</code> GWID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean covers( Gwid aGeneral, Gwid aSpecific ) {
    TsNullArgumentRtException.checkNull( aSpecific );
    // we don't know about class inheritance so check class IDs are equal
    if( !aGeneral.classId().equals( aSpecific.classId() ) ) {
      return false;
    }
    // check for objects
    if( !internalCoversObjects( aGeneral, aSpecific ) ) {
      return false;
    }
    // GWID without specified properties covers any other GWID of the same class
    if( aGeneral.level() == 1 ) {
      return true;
    }
    // for GWIDs with props/subprops only same kind may cover each other
    if( aGeneral.kind() != aSpecific.kind() ) { // also means the same levels
      return false;
    }
    // check for props
    if( !internalCoversProps( aGeneral, aSpecific ) ) {
      return false;
    }
    // for level 3 also check for subprops
    if( aGeneral.level() == 3 ) {
      if( !internalCoversSubProps( aGeneral, aSpecific ) ) {
        return false;
      }
    }
    // all possibilities checked
    return true;
  }

  private static boolean internalCoversProps( Gwid aGeneral, Gwid aSpecific ) {
    // here we assume that GWIDs are of same kind and of level 2
    if( aGeneral.isPropMulti() ) {
      return true;
    }
    if( aSpecific.isPropMulti() ) {
      return false;
    }
    return aGeneral.propId().equals( aSpecific.propId() );
  }

  private static boolean internalCoversSubProps( Gwid aGeneral, Gwid aSpecific ) {
    // here we assume that GWIDs are of same kind and of level 3
    if( aGeneral.isSubPropMulti() ) {
      return true;
    }
    if( aSpecific.isSubPropMulti() ) {
      return false;
    }
    return aGeneral.subPropId().equals( aSpecific.subPropId() );
  }

  private static boolean internalCoversObjects( Gwid aGeneral, Gwid aSpecific ) {
    // here we assume that class IDs of GWIDs are equal
    boolean isGenMulti = aGeneral.isAbstract() || aGeneral.isStridMulti();
    if( isGenMulti ) {
      return true;
    }
    boolean isSpecMulti = aSpecific.isAbstract() || aSpecific.isStridMulti();
    if( isSpecMulti ) {
      return false;
    }
    return aGeneral.strid().equals( aSpecific.strid() );
  }

  /**
   * No sublcassing.
   */
  private GwidUtils() {
    // nop
  }

}
