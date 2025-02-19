package org.toxsoft.core.tsgui.bricks.tstree.tmm;

import java.util.*;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.gui.*;

/**
 * Description of the grouping items in tree view bound together with the the {@link ITsTreeMaker} strategy.
 * <p>
 * This is immutable class.
 *
 * @author hazard157
 * @param <T> - displayed M5-modeled entity type
 */
public final class TreeModeInfo<T>
    extends Stridable
    implements IIconIdable {

  private final String          iconId;
  private final ITsTreeMaker<T> treeMaker;

  /**
   * Constructor.
   *
   * @param aId String - the mode ID (IDpath)
   * @param aName String - readable mode name
   * @param aDescription String - mode description (is used as a tooltip)
   * @param aIconId String - the mode icon ID or <code>null</code>
   * @param aTreeMaker {@link ITsTreeMaker}&lt;T&gt; - the tree maker
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aId is not an IDpath
   */
  public TreeModeInfo( String aId, String aName, String aDescription, String aIconId, ITsTreeMaker<T> aTreeMaker ) {
    super( aId, aName, aDescription );
    iconId = aIconId;
    treeMaker = TsNullArgumentRtException.checkNull( aTreeMaker );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  @Override
  public String iconId() {
    return iconId;
  }

  /**
   * Returns the way a collection of elements is grouped into a tree.
   *
   * @return {@link ITsTreeMaker}&lt;T&gt; - the tree maker
   */
  public ITsTreeMaker<T> treeMaker() {
    return treeMaker;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return StridUtils.printf( StridUtils.FORMAT_ID_NAME, this );
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof TreeModeInfo<?> that ) {
      if( super.equals( that ) ) {
        return Objects.equals( iconId, that.iconId ) && treeMaker.equals( that.treeMaker );
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + super.hashCode();
    result = TsLibUtils.PRIME * result + ((iconId != null) ? iconId.hashCode() : 0);
    result = TsLibUtils.PRIME * result + treeMaker.hashCode();
    return result;
  }

}
