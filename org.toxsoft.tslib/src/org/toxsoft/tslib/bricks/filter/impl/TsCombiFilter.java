package org.toxsoft.tslib.bricks.filter.impl;

import java.io.Serializable;

import org.toxsoft.tslib.bricks.filter.*;
import org.toxsoft.tslib.utils.errors.*;

/**
 * {@link ITsFilter} implemented as single filters combined by logical operaions.
 *
 * @author hazard157
 * @param <T> - type of filtered objects
 */
public class TsCombiFilter<T>
    implements ITsFilter<T>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final ITsCombiFilterParams params;
  private final ITsFilter<T>         singleFilter;
  private final TsCombiFilter<T>     left;
  private final TsCombiFilter<T>     right;
  private final boolean              completeEvaluation;

  // ------------------------------------------------------------------------------------
  // Статические конструкторы
  //

  /**
   * Создает фильтра с указанием всех параметров и настроек.
   *
   * @param <T> - type of filtered objects
   * @param aParams {@link ITsCombiFilterParams} - параметры создаваемого фильтра
   * @param aFacReg ITsFilterFactoriesRegistry&lt;T&gt; - реестр фабрик единичных фильтров
   * @param aCompleteEvaluation boolean - признак режима полных вычислении
   * @return {@link ITsFilter}&lt;T&gt; - созданный фильтр
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemNotFoundRtException фабрика единичного фильтра не зарегистрирована
   */
  public static <T> ITsFilter<T> create( ITsCombiFilterParams aParams, ITsFilterFactoriesRegistry<T> aFacReg,
      boolean aCompleteEvaluation ) {
    if( aParams == ITsCombiFilterParams.NONE ) {
      return ITsFilter.NONE;
    }
    if( aParams == ITsCombiFilterParams.ALL ) {
      return ITsFilter.ALL;
    }
    return new TsCombiFilter<>( aParams, aFacReg, aCompleteEvaluation );
  }

  /**
   * Создает фильтр с указанием фабрик в режиме неполных вычислении.
   *
   * @param <T> - type of filtered objects
   * @param aParams {@link ITsCombiFilterParams} - параметры создаваемого фильтра
   * @param aFacReg ITsFilterFactoriesRegistry&lt;T&gt; - реестр фабрик единичных фильтров
   * @return {@link ITsFilter}&lt;T&gt; - созданный фильтр
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemNotFoundRtException фабрика единичного фильтра не зарегистрирована
   */
  public static <T> ITsFilter<T> create( ITsCombiFilterParams aParams, ITsFilterFactoriesRegistry<T> aFacReg ) {
    return create( aParams, aFacReg, false );
  }

  /**
   * Скрытый реальный конструктор, создает все внутренные структуры.
   *
   * @param aParams {@link ITsCombiFilterParams} - параметры создаваемого фильтра
   * @param aFacReg ITsFilterFactoriesRegistry&lt;T&gt; - реестр фабрик единичных фильтров
   * @param aCompleteEvaluation boolean - признак режима полных вычислении
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  private TsCombiFilter( ITsCombiFilterParams aParams, ITsFilterFactoriesRegistry<T> aFacReg,
      boolean aCompleteEvaluation ) {
    TsNullArgumentRtException.checkNulls( aParams, aFacReg );
    completeEvaluation = aCompleteEvaluation;
    params = aParams;
    // создание фильтров
    if( aParams.isSingle() ) { // создание единичного фильтра
      // если
      ITsSingleFilterFactory<T> f = aFacReg.get( aParams.single().typeId() );
      singleFilter = f.create( aParams.single() );
      left = null;
      right = null;
    }
    else { // создание поли-фильтра
      singleFilter = null;
      left = new TsCombiFilter<>( aParams.left(), aFacReg, aCompleteEvaluation );
      right = new TsCombiFilter<>( aParams.right(), aFacReg, aCompleteEvaluation );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsCombiFilter
  //

  @Override
  public boolean accept( T aElement ) {
    if( params.isSingle() ) {
      boolean b = singleFilter.accept( aElement );
      if( params.isInverted() ) {
        return !b;
      }
      return b;
    }
    boolean bLeft = left.accept( aElement );
    switch( params.op() ) {
      case AND:
        if( !bLeft && !completeEvaluation ) {
          return false;
        }
        break;
      case OR:
        if( bLeft && !completeEvaluation ) {
          return true;
        }
        break;
      case XOR:
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    boolean bRight = right.accept( aElement );
    boolean b = switch( params.op() ) {
      case AND -> bLeft && bRight;
      case OR -> bLeft || bRight;
      case XOR -> bLeft ^ bRight;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
    if( params.isInverted() ) {
      return !b;
    }
    return b;
  }

}
