package org.toxsoft.core.tslib.bricks.filter.impl;

import java.io.*;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsCombiFilterParams} implementation.
 * <p>
 * Instances of the class (actually, hidden descendants) are created by static constructors <code>createXxx()</code>.
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

    static ITsCombiFilterParams create( ITsSingleFilterParams aParams, boolean aIsInverted ) {
      if( !aIsInverted ) {
        if( aParams == ITsSingleFilterParams.ALL ) {
          return ITsCombiFilterParams.ALL;
        }
        if( aParams == ITsSingleFilterParams.NONE ) {
          return ITsCombiFilterParams.NONE;
        }
      }
      return new FilterParamsSingle( aParams, aIsInverted );
    }

    private FilterParamsSingle( ITsSingleFilterParams aParams, boolean aIsInverted ) {
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
  // Instance creation
  //

  /**
   * Creates {@link ITsCombiFilterParams} instance as a single filter.
   * <p>
   * In the corresponding case, returns one of two constants {@link ITsCombiFilterParams#NONE} or
   * {@link ITsCombiFilterParams#ALL}.
   *
   * @param aParams {@link ITsSingleFilterParams} - single filter parameters
   * @param aIsResultInverted boolean - the flag of the NOT unary operation
   * @return {@link ITsCombiFilterParams} - created instance or constant ALL or NONE
   */
  public static ITsCombiFilterParams createSingle( ITsSingleFilterParams aParams, boolean aIsResultInverted ) {
    TsNullArgumentRtException.checkNull( aParams );
    return FilterParamsSingle.create( aParams, aIsResultInverted );
  }

  /**
   * Creates {@link ITsCombiFilterParams} instance as a single filter.
   * <p>
   * Is the same as call to {@link #createSingle(ITsSingleFilterParams, boolean) createSingle(aParams, <b>false</b>)}.
   *
   * @param aParams {@link ITsSingleFilterParams} - single filter parameters
   * @return {@link ITsCombiFilterParams} - created instance or constant ALL or NONE
   */
  public static ITsCombiFilterParams createSingle( ITsSingleFilterParams aParams ) {
    TsNullArgumentRtException.checkNull( aParams );
    return FilterParamsSingle.create( aParams, false );
  }

  // TODO TRANSLATE

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
    ITsCombiFilterParams sf1 = FilterParamsSingle.create( aLeft, false );
    ITsCombiFilterParams sf2 = FilterParamsSingle.create( aRight, false );
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
    ITsCombiFilterParams sf1 = FilterParamsSingle.create( aLeft, false );
    ITsCombiFilterParams sf2 = FilterParamsSingle.create( aRight, false );
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
    ITsCombiFilterParams sf = FilterParamsSingle.create( aLeft, false );
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
    ITsCombiFilterParams sf = FilterParamsSingle.create( aRight, false );
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
