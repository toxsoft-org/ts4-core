package org.toxsoft.core.tslib.av.math;

import static org.toxsoft.core.tslib.av.math.ITsResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

// TODO TRANSLATE

/**
 * Строгий сравниватель атомарных значений.
 * <p>
 * Строгий означает, что сравниватель:
 * <ul>
 * <li>использование неприсвоенных значении (в том числе {@link IAtomicValue#NULL}) приводит к исключениям
 * {@link AvUnassignedValueRtException};</li>
 * <li>не производит никакого приведения типов, любое несоответствие атомарных типов операндов приводит к выбрасыванию
 * исключения {@link AvTypeCastRtException}.</li>
 * </ul>
 * Все методы класса являются потоко-безопасными.
 *
 * @author hazard157
 */
public class AvComparatorStrict
    implements IAvComparator {

  /**
   * Singleton instance.
   */
  public static final IAvComparator INSTANCE = new AvComparatorStrict();

  /**
   * Пустой конструктор.
   */
  private AvComparatorStrict() {
    // пусто
  }

  // ------------------------------------------------------------------------------------
  // IAvComparator
  //

  @Override
  public boolean avCompare( IAtomicValue aAv1, EAvCompareOp aOp, IAtomicValue aAv2 ) {
    if( aAv1 == null || aAv2 == null || aOp == null ) {
      throw new TsNullArgumentRtException();
    }
    if( !aAv1.isAssigned() || !aAv2.isAssigned() ) {
      throw new AvUnassignedValueRtException();
    }
    if( aAv1.atomicType() != aAv2.atomicType() ) {
      throw new AvTypeCastRtException();
    }
    if( aOp == EAvCompareOp.MATCH ) {
      if( aAv1.atomicType() == EAtomicType.STRING ) {
        return aAv1.asString().matches( aAv2.asString() );
      }
      return false;
    }
    int cmpVal; // результат сравнения имеет три значения: <0, 0, >0
    // сравним однотипные значение
    switch( aAv1.atomicType() ) {
      case NONE:
        switch( aOp ) {
          case EQ:
          case GE:
          case LE:
            return true;
          case NE:
          case GT:
          case LT:
            return false;
          case MATCH:
            throw new TsInternalErrorRtException();
          default:
            throw new TsNotAllEnumsUsedRtException();
        }
      case BOOLEAN:
        switch( aOp ) {
          case EQ:
          case GE:
          case LE:
            return aAv1.asBool() == aAv2.asBool();
          case NE:
          case GT:
          case LT:
            return aAv1.asBool() != aAv2.asBool();
          case MATCH:
            throw new TsInternalErrorRtException();
          default:
            throw new TsNotAllEnumsUsedRtException();
        }
      case INTEGER:
      case TIMESTAMP:
        if( aAv1.asLong() > aAv2.asLong() ) {
          cmpVal = 1;
        }
        else {
          cmpVal = (aAv1.asLong() == aAv2.asLong()) ? 0 : -1;
        }
        break;
      case FLOATING:
        cmpVal = Double.compare( aAv1.asDouble(), aAv2.asDouble() );
        break;
      case STRING:
        cmpVal = aAv1.asString().compareTo( aAv2.asString() );
        break;
      case VALOBJ:
        cmpVal = aAv1.asString().compareTo( aAv2.asString() );
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    // вернем признак выполнения условия сравнения
    switch( aOp ) {
      case EQ:
        return cmpVal == 0;
      case NE:
        return cmpVal != 0;
      case GE:
        return cmpVal >= 0;
      case GT:
        return cmpVal > 0;
      case LE:
        return cmpVal <= 0;
      case LT:
        return cmpVal < 0;
      case MATCH:
        throw new TsInternalErrorRtException();
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  public ValidationResult canCompare( IAtomicValue aAv1, EAvCompareOp aOp, IAtomicValue aAv2 ) {
    if( aAv1 == null ) {
      return ValidationResult.error( FMT_ERR_ARG_NULL, "1" ); //$NON-NLS-1$
    }
    if( aOp == null ) {
      return ValidationResult.error( FMT_ERR_ARG_NULL, "OP" ); //$NON-NLS-1$
    }
    if( aAv2 == null ) {
      return ValidationResult.error( FMT_ERR_ARG_NULL, "2" ); //$NON-NLS-1$
    }
    if( !aAv1.isAssigned() ) {
      return ValidationResult.error( FMT_ERR_OPER_AV_NULL, "1" ); //$NON-NLS-1$
    }
    if( !aAv2.isAssigned() ) {
      return ValidationResult.error( FMT_ERR_OPER_AV_NULL, "2" ); //$NON-NLS-1$
    }
    if( aAv1.atomicType() != aAv2.atomicType() ) {
      return ValidationResult.error( FMT_ERR_OPER_DIFF_AT, aAv1.atomicType().id(), aAv2.atomicType().id() );
    }
    if( aOp == EAvCompareOp.MATCH ) {
      if( aAv1.atomicType() != EAtomicType.STRING ) {
        return ValidationResult.error( FMT_ERR_OPER_NOT_STR, "1" ); //$NON-NLS-1$
      }
      if( aAv2.atomicType() != EAtomicType.STRING ) {
        return ValidationResult.error( FMT_ERR_OPER_NOT_STR, "2" ); //$NON-NLS-1$
      }
    }
    return ValidationResult.SUCCESS;
  }

}
