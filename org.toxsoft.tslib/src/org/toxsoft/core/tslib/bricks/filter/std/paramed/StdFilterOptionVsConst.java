package org.toxsoft.core.tslib.bricks.filter.std.paramed;

import static org.toxsoft.core.tslib.bricks.filter.std.IStdTsFiltersConstants.*;

import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.math.*;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.utils.IParameterized;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Фильтр сравнения одного из опции {@link IParameterized#params()} с константой.
 * <p>
 * Метод проверки {@link #accept(IParameterized)} не выбрасывает исключений - если аргумент на самом деле не
 * {@link IParameterized}, просто возвращает <code>false</code>.
 * <p>
 * Отсутствие в {@link IParameterized#params()} опции {@link #optionId()} рассматривается как значение
 * {@link IAtomicValue#NULL}.
 *
 * @author hazard157
 */
public final class StdFilterOptionVsConst
    implements ITsFilter<IParameterized> {

  /**
   * Идентификатор типа фильтра {@link ITsSingleFilterFactory#id()},
   */
  public static final String TYPE_ID = STD_FILTERID_ID_PREFIX + ".OptionVsConst"; //$NON-NLS-1$

  /**
   * Фабрика создания фильтра из значений параметров.
   */
  public static final ITsSingleFilterFactory<IParameterized> FACTORY =
      new AbstractTsSingleFilterFactory<>( TYPE_ID, IParameterized.class ) {

        @Override
        protected ITsFilter<IParameterized> doCreateFilter( IOptionSet aParams ) {
          String optionId = aParams.getStr( PID_OPTION_ID );
          String opId = aParams.getStr( PID_OP );
          EAvCompareOp op = EAvCompareOp.findById( opId );
          IAtomicValue constant = aParams.getValue( PID_CONSTANT );
          return new StdFilterOptionVsConst( optionId, op, constant );
        }
      };

  private static final String PID_OPTION_ID = "optionId"; //$NON-NLS-1$
  private static final String PID_OP        = "op";       //$NON-NLS-1$
  private static final String PID_CONSTANT  = "constant"; //$NON-NLS-1$

  private final String       optionId;
  private final EAvCompareOp op;
  private final IAtomicValue constant;

  /**
   * Конструктор.
   *
   * @param aOptionId String - идентификатор (ИД-путь) проверяемой опции
   * @param aOp {@link EAvCompareOp} - способ сравнения
   * @param aConst {@link IAtomicValue} - константа для сравнения
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   */
  public StdFilterOptionVsConst( String aOptionId, EAvCompareOp aOp, IAtomicValue aConst ) {
    TsNullArgumentRtException.checkNulls( aOp, aConst );
    optionId = StridUtils.checkValidIdPath( aOptionId );
    op = aOp;
    constant = aConst;
  }

  /**
   * Создает набор параметров {@link ITsCombiFilterParams} для создания фильтра фабрикой {@link #FACTORY}.
   *
   * @param aOptionId String - идентификатор (ИД-путь) проверяемой опции
   * @param aOp {@link EAvCompareOp} - способ сравнения
   * @param aConst {@link IAtomicValue} - константа для сравнения
   * @return {@link ITsCombiFilterParams} - параметры для создания фильтра фабрикой
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   */
  public static ITsCombiFilterParams makeFilterParams( String aOptionId, EAvCompareOp aOp, IAtomicValue aConst ) {
    StridUtils.checkValidIdPath( aOptionId );
    TsNullArgumentRtException.checkNulls( aOp, aConst );
    ITsSingleFilterParams sp = TsSingleFilterParams.create( TYPE_ID, //
        PID_OPTION_ID, aOptionId, //
        PID_OP, aOp.id(), //
        PID_CONSTANT, aConst //
    );
    ITsCombiFilterParams p = TsCombiFilterParams.createSingle( sp );
    return p;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает идентификатор проверяемой опции.
   *
   * @return String - идентификатор (ИД-путь) проверяемой опции
   */
  public String optionId() {
    return optionId;
  }

  /**
   * Возвращает способ сравнения.
   *
   * @return {@link EAvCompareOp} - способ сравнения
   */
  public EAvCompareOp op() {
    return op;
  }

  /**
   * Возвращает константа для сравнения.
   *
   * @return {@link IAtomicValue} - константа для сравнения
   */
  public IAtomicValue constant() {
    return constant;
  }

  // ------------------------------------------------------------------------------------
  // ITsFilter
  //

  @Override
  public boolean accept( IParameterized aObj ) {
    IAvComparator c = AvComparatorStrict.INSTANCE;
    IAtomicValue opVal = aObj.params().getValue( optionId, IAtomicValue.NULL );
    return c.avCompare( opVal, op, constant );
  }

}
