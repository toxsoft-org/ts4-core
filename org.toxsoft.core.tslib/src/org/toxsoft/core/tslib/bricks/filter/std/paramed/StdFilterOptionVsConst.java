package org.toxsoft.core.tslib.bricks.filter.std.paramed;

import static org.toxsoft.core.tslib.bricks.filter.std.IStdTsFiltersConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.math.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

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
   * The filter type ID {@link ITsSingleFilterFactory#id()},
   */
  public static final String TYPE_ID = STD_FILTERID_ID_PREFIX + ".OptionVsConst"; //$NON-NLS-1$

  /**
   * Filter factory, an {@link ITsSingleFilterFactory} implementation.
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
   * Constructor.
   *
   * @param aOptionId String - the checked option ID (an IDpath)
   * @param aOp {@link EAvCompareOp} - the comparison method
   * @param aConst {@link IAtomicValue} - the constant to compare to
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public StdFilterOptionVsConst( String aOptionId, EAvCompareOp aOp, IAtomicValue aConst ) {
    TsNullArgumentRtException.checkNulls( aOp, aConst );
    optionId = StridUtils.checkValidIdPath( aOptionId );
    op = aOp;
    constant = aConst;
  }

  /**
   * Makes the parameters {@link ITsCombiFilterParams} to create filter using {@link #FACTORY}.
   *
   * @param aOptionId String - the checked option ID (an IDpath)
   * @param aOp {@link EAvCompareOp} - the comparison method
   * @param aConst {@link IAtomicValue} - the constant to compare to
   * @return {@link ITsCombiFilterParams} - parameters to create the filter
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public static ITsCombiFilterParams makeFilterParams( String aOptionId, EAvCompareOp aOp, IAtomicValue aConst ) {
    StridUtils.checkValidIdPath( aOptionId );
    TsNullArgumentRtException.checkNulls( aOp, aConst );
    ITsSingleFilterParams spf = TsSingleFilterParams.create( TYPE_ID, //
        PID_OPTION_ID, aOptionId, //
        PID_OP, aOp.id(), //
        PID_CONSTANT, aConst //
    );
    return TsCombiFilterParams.createSingle( spf );
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
