package org.toxsoft.tsgui.m5_3.gui.mpc.impl;

import java.util.Objects;

import org.toxsoft.tsgui.bricks.tstree.ITsTreeMaker;
import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tsgui.graphics.icons.ITsIconManager;
import org.toxsoft.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.tslib.bricks.strid.impl.Stridable;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

// TODO TRANSLATE

/**
 * Описание группировки в режиме показа дерева.
 * <p>
 * Это неизменяемый класс.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public final class TreeModeInfo<T>
    extends Stridable {

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
    super( aId, aDescription, aName );
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
  // Реализация методов класса Object
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
