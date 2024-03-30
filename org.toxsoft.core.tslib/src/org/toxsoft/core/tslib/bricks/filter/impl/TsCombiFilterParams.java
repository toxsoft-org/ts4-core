package org.toxsoft.core.tslib.bricks.filter.impl;

import java.io.Serializable;

import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.filter.ITsCombiFilterParams;
import org.toxsoft.core.tslib.bricks.filter.ITsSingleFilterParams;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsUnsupportedFeatureRtException;

// TRANSLATE

/**
 * Реализация параметров составного фильтра {@link ITsCombiFilterParams}.
 * <p>
 * Экземпляры класса (точнее, скрытых наследников) создаются статическими конструкторами <code>createXxx()</code>.
 *
 * @author hazard157
 */
public abstract class TsCombiFilterParams
    implements ITsCombiFilterParams, Serializable {

  private static final long serialVersionUID = 157157L;

  static class FilterParamsSingle
      extends TsCombiFilterParams {

    private static final long serialVersionUID = 157157L;

    private final ITsSingleFilterParams params;

    FilterParamsSingle( ITsSingleFilterParams aParams, boolean aIsInverted ) {
      super( aIsInverted );
      params = TsNullArgumentRtException.checkNull( aParams );
    }

    @Override
    public boolean isSingle() {
      return true;
    }

    @Override
    public ITsSingleFilterParams single() {
      return params;
    }

    @Override
    public ITsCombiFilterParams left() {
      throw new TsUnsupportedFeatureRtException();
    }

    @Override
    public ELogicalOp op() {
      throw new TsUnsupportedFeatureRtException();
    }

    @Override
    public ITsCombiFilterParams right() {
      throw new TsUnsupportedFeatureRtException();
    }

  }

  static class FilterParamsCombi
      extends TsCombiFilterParams {

    private static final long serialVersionUID = 157157L;

    private final ITsCombiFilterParams left;
    private final ELogicalOp           op;
    private final ITsCombiFilterParams right;

    protected FilterParamsCombi( ITsCombiFilterParams aLeft, ELogicalOp aOp, ITsCombiFilterParams aRight,
        boolean aIsInverted ) {
      super( aIsInverted );
      left = aLeft;
      op = aOp;
      right = aRight;
    }

    @Override
    public boolean isSingle() {
      return false;
    }

    @Override
    public ITsSingleFilterParams single() {
      throw new TsUnsupportedFeatureRtException();
    }

    @Override
    public ITsCombiFilterParams left() {
      return left;
    }

    @Override
    public ELogicalOp op() {
      return op;
    }

    @Override
    public ITsCombiFilterParams right() {
      return right;
    }
  }

  private final boolean isResultInverted;

  protected TsCombiFilterParams( boolean aIsInverted ) {
    isResultInverted = aIsInverted;
  }

  // ------------------------------------------------------------------------------------
  // Создание экземпляров класса
  //

  /**
   * Создает параметры поли-фильтра, состоящего из одного единичного фильтра.
   *
   * @param aParams {@link ITsSingleFilterParams} - параметры единичного фильтра
   * @param aIsResultInverted boolean - признак инвертирования результата
   * @return {@link ITsCombiFilterParams} - созданные параметры поли-фильтра
   */
  public static ITsCombiFilterParams createSingle( ITsSingleFilterParams aParams, boolean aIsResultInverted ) {
    TsNullArgumentRtException.checkNull( aParams );
    return new FilterParamsSingle( aParams, aIsResultInverted );
  }

  /**
   * Создает параметры поли-фильтра, состоящего из одного единичного фильтра.
   * <p>
   * Равнозначно вызову метода {@link #createSingle(ITsSingleFilterParams, boolean) createSingle(aParams,
   * <b>false</b>)}.
   *
   * @param aParams {@link IOptionSet} - параметры единичного фильтра
   * @return {@link ITsCombiFilterParams} - созданные параметры поли-фильтра
   */
  public static ITsCombiFilterParams createSingle( ITsSingleFilterParams aParams ) {
    TsNullArgumentRtException.checkNull( aParams );
    return new FilterParamsSingle( aParams, false );
  }

  /**
   * Создает параметры поли-фильтра из двух поли-фильтров и логической операции.
   *
   * @param aLeft {@link ITsCombiFilterParams} - параметры левого фильтра
   * @param aOp {@link ELogicalOp} - логическая операция между фильтрами
   * @param aRight {@link ITsCombiFilterParams} - параметры правого фильтра
   * @param aIsResultInverted boolean - признак инвертирования результата
   * @return {@link ITsCombiFilterParams} - созданные параметры поли-фильтра
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITsCombiFilterParams createCombi( ITsCombiFilterParams aLeft, ELogicalOp aOp,
      ITsCombiFilterParams aRight, boolean aIsResultInverted ) {
    TsNullArgumentRtException.checkNulls( aLeft, aOp, aRight );
    return new FilterParamsCombi( aLeft, aOp, aRight, aIsResultInverted );
  }

  /**
   * Создает параметры поли-фильтра из двух поли-фильтров и логической операции.
   * <p>
   * Равнозначно вызову метода {@link #createCombi(ITsCombiFilterParams, ELogicalOp, ITsCombiFilterParams, boolean)
   * createCombi(aLeft, aOp, aRight,` <b>false</b>)}.
   *
   * @param aLeft {@link ITsCombiFilterParams} - параметры левого фильтра
   * @param aOp {@link ELogicalOp} - логическая операция между фильтрами
   * @param aRight {@link ITsCombiFilterParams} - параметры правого фильтра
   * @return {@link ITsCombiFilterParams} - созданные параметры поли-фильтра
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITsCombiFilterParams createCombi( ITsCombiFilterParams aLeft, ELogicalOp aOp,
      ITsCombiFilterParams aRight ) {
    return createCombi( aLeft, aOp, aRight, false );
  }

  /**
   * Создает параметры поли-фильтра из двух единичных фильтров.
   *
   * @param aLeft {@link ITsSingleFilterParams} - параметры левого фильтра
   * @param aOp {@link ELogicalOp} - логическая операция между фильтрами
   * @param aRight {@link ITsSingleFilterParams} - параметры правого фильтра
   * @param aIsResultInverted boolean - признак инвертирования результата
   * @return {@link ITsCombiFilterParams} - созданные параметры поли-фильтра
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITsCombiFilterParams createCombi( ITsSingleFilterParams aLeft, ELogicalOp aOp,
      ITsSingleFilterParams aRight, boolean aIsResultInverted ) {
    TsNullArgumentRtException.checkNulls( aLeft, aOp, aRight );
    ITsCombiFilterParams sf1 = new FilterParamsSingle( aLeft, false );
    ITsCombiFilterParams sf2 = new FilterParamsSingle( aRight, false );
    return new FilterParamsCombi( sf1, aOp, sf2, aIsResultInverted );
  }

  /**
   * Создает параметры поли-фильтра из двух единичных фильтров.
   *
   * @param aLeft {@link ITsSingleFilterParams} - параметры левого фильтра
   * @param aOp {@link ELogicalOp} - логическая операция между фильтрами
   * @param aRight {@link ITsSingleFilterParams} - параметры правого фильтра
   * @return {@link ITsCombiFilterParams} - созданные параметры поли-фильтра
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITsCombiFilterParams createCombi( ITsSingleFilterParams aLeft, ELogicalOp aOp,
      ITsSingleFilterParams aRight ) {
    TsNullArgumentRtException.checkNulls( aLeft, aOp, aRight );
    ITsCombiFilterParams sf1 = new FilterParamsSingle( aLeft, false );
    ITsCombiFilterParams sf2 = new FilterParamsSingle( aRight, false );
    return new FilterParamsCombi( sf1, aOp, sf2, false );
  }

  /**
   * Создает параметры поли-фильтра из единичного и поли-фильтра.
   *
   * @param aLeft {@link ITsSingleFilterParams} - параметры левого фильтра
   * @param aOp {@link ELogicalOp} - логическая операция между фильтрами
   * @param aRight {@link ITsCombiFilterParams} - параметры правого фильтра
   * @param aIsResultInverted boolean - признак инвертирования результата
   * @return {@link ITsCombiFilterParams} - созданные параметры поли-фильтра
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITsCombiFilterParams createCombi( ITsSingleFilterParams aLeft, ELogicalOp aOp,
      ITsCombiFilterParams aRight, boolean aIsResultInverted ) {
    TsNullArgumentRtException.checkNulls( aLeft, aOp, aRight );
    ITsCombiFilterParams sf = new FilterParamsSingle( aLeft, false );
    return new FilterParamsCombi( sf, aOp, aRight, aIsResultInverted );
  }

  /**
   * Создает параметры поли-фильтра из единичного и поли-фильтра.
   * <p>
   * Равнозначно вызову метода {@link #createCombi(ITsSingleFilterParams, ELogicalOp, ITsCombiFilterParams, boolean)
   * createCombi(aLeft, aOp, aRight,` <b>false</b>)}.
   *
   * @param aLeft {@link ITsSingleFilterParams} - параметры левого фильтра
   * @param aOp {@link ELogicalOp} - логическая операция между фильтрами
   * @param aRight {@link ITsCombiFilterParams} - параметры правого фильтра
   * @return {@link ITsCombiFilterParams} - созданные параметры поли-фильтра
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITsCombiFilterParams createCombi( ITsSingleFilterParams aLeft, ELogicalOp aOp,
      ITsCombiFilterParams aRight ) {
    return createCombi( aLeft, aOp, aRight, false );
  }

  /**
   * Создает параметры поли-фильтра из единичного и поли-фильтра.
   *
   * @param aLeft {@link ITsCombiFilterParams} - параметры левого фильтра
   * @param aOp {@link ELogicalOp} - логическая операция между фильтрами
   * @param aRight {@link ITsSingleFilterParams} - параметры правого фильтра
   * @param aIsResultInverted boolean - признак инвертирования результата
   * @return {@link ITsCombiFilterParams} - созданные параметры поли-фильтра
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITsCombiFilterParams createCombi( ITsCombiFilterParams aLeft, ELogicalOp aOp,
      ITsSingleFilterParams aRight, boolean aIsResultInverted ) {
    TsNullArgumentRtException.checkNulls( aLeft, aOp, aRight );
    ITsCombiFilterParams sf = new FilterParamsSingle( aRight, false );
    return new FilterParamsCombi( aLeft, aOp, sf, aIsResultInverted );
  }

  /**
   * Создает параметры поли-фильтра из единичного и поли-фильтра.
   * <p>
   * Равнозначно вызову метода {@link #createCombi(ITsCombiFilterParams, ELogicalOp, ITsSingleFilterParams, boolean)
   * createCombi(aLeft, aOp, aRight,` <b>false</b>)}.
   *
   * @param aLeft {@link ITsCombiFilterParams} - параметры левого фильтра
   * @param aOp {@link ELogicalOp} - логическая операция между фильтрами
   * @param aRight {@link ITsSingleFilterParams} - параметры правого фильтра
   * @return {@link ITsCombiFilterParams} - созданные параметры поли-фильтра
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITsCombiFilterParams createCombi( ITsCombiFilterParams aLeft, ELogicalOp aOp,
      ITsSingleFilterParams aRight ) {
    return createCombi( aLeft, aOp, aRight, false );
  }

  // ------------------------------------------------------------------------------------
  // ITsCombiFilterParams
  //

  @Override
  public boolean isInverted() {
    return isResultInverted;
  }

}
