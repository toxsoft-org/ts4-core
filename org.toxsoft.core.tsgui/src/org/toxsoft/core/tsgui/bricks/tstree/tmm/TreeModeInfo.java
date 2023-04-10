package org.toxsoft.core.tsgui.bricks.tstree.tmm;

import java.util.*;

import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.icons.*;

// TODO TRANSLATE

/**
 * Description of the grouping items in tree view bound together with the the {@link ITsTreeMaker} strategy.
 * <p>
 * This is immutable class.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public final class TreeModeInfo<T>
    extends Stridable
    implements IIconIdable {

  private final String          iconId;
  private final ITsTreeMaker<T> treeMaker;

  /**
   * Конструктор.
   * <p>
   * Идентификатор значка используется для загрузки значка методом
   * {@link ITsIconManager#loadStdIcon(String, EIconSize)}.
   *
   * @param aId String - идентификатор сущности (ИД-путь)
   * @param aName String - удобочитаемое имя сущности
   * @param aDescription String - описание сущности
   * @param aIconId String - идентификатор значка или <code>null</code>
   * @param aTreeMaker {@link ITsTreeMaker}&lt;T&gt; - способ группировки коллекции элементов в дерево
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aId не ИД-путь
   */
  public TreeModeInfo( String aId, String aName, String aDescription, String aIconId, ITsTreeMaker<T> aTreeMaker ) {
    super( aId, aName, aDescription );
    iconId = aIconId;
    treeMaker = TsNullArgumentRtException.checkNull( aTreeMaker );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает идентификатор значка.
   *
   * @return String - идентификатор значка или <code>null</code
   */
  @Override
  public String iconId() {
    return iconId;
  }

  /**
   * Возвращает способ группировки коллекции элементов в дерево.
   *
   * @return {@link ITsTreeMaker}&lt;T&gt; - способ группировки коллекции элементов в дерево
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
