package org.toxsoft.tslib.av.validators.defav;

import static org.toxsoft.tslib.av.validators.defav.ITsResources.*;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.impl.DataType;
import org.toxsoft.tslib.av.metainfo.IAvMetaConstants;
import org.toxsoft.tslib.av.metainfo.IDataType;
import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.bricks.validator.*;
import org.toxsoft.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

// TODO TRANSLATE

/**
 * Валидатор, проверяющий значение по известным из {@link IAvMetaConstants} ограничениям типа.
 * <p>
 * Данный валидатор проверяет атомарное значение на соответствие атомарному типу и на прохождение всех известныхв tslib
 * ограничений типа, перечисленных в {@link IAvMetaConstants}.
 * <p>
 * Обратите внимание, что если тип данных {@link EAtomicType#NONE}, то считается, что любой атомарный тип значения
 * допустим. В любом случае, к проверямому методом {@link #validate(IAtomicValue)} значению применяются те ограничения,
 * которые соотвтетсвуют атомарному типу значения.
 * <p>
 * Проверка значения атомарного типа {@link EAtomicType#NONE} происходит следующим образом: если значение равно
 * {@link IAtomicValue#NULL}, то проверяется только допустимость пустого значения (ограничение
 * {@link IAvMetaConstants#TSID_IS_NULL_ALLOWED}), а все остальные значения считаются допустимыми только если тип данных
 * {@link #atomicType()} тоже {@link EAtomicType#NONE}. В остальных случаях возвращается ошибка.
 * <p>
 * Данный валидатор не возвращает предупреждения {@link EValidationResultType#WARNING}, только ошибки или сообщение об
 * успехе.
 *
 * @author hazard157
 */
public class DefaultAvValidator
    implements ITsValidator<IAtomicValue> {

  /*
   * Этот класс реализован на основе паттерна программирования "состояние". В зависимости от атомарного типа
   * проверяемого значения (задается в конструкторе или setDataType()), ссылка currentValidator указывает на экземпляр
   * валидатора соответствующего типа XxxValidator.
   */

  /**
   * Тип проверямых данных, задается в конструкторе, меняется в setXxx().
   */
  private IDataType dataType;

  /**
   * Текущий валидатор, одна из XxxValidator, в зависимости от атомарного типа dataType.atomicType().
   */
  private ITsValidator<IAtomicValue> currentValidator = ITsValidator.PASS;

  /**
   * Constructor.
   *
   * @param aDataType {@link IDataType} - atomic data type
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public DefaultAvValidator( IDataType aDataType ) {
    setType( aDataType );
  }

  /**
   * Создает валидатор.
   * <p>
   * <b>Внимание:</b> Конструктор не копирует ограничения типа, только запоминает ссылку. Поэтому, если органичения типа
   * изменятся "снаружи", то валидатор при каждом вызове {@link #validate(IAtomicValue)} будет использовать текущие
   * значения органицений типа.
   *
   * @param aAtomicType {@link EAtomicType} - атомарный тип данных
   * @param aTypeContsraints {@link IOptionSet} - ограничения типа
   */
  public DefaultAvValidator( EAtomicType aAtomicType, IOptionSet aTypeContsraints ) {
    setType( new DataType( aAtomicType, aTypeContsraints ) );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void setType( IDataType aDataType ) {
    TsNullArgumentRtException.checkNulls( aDataType );
    dataType = aDataType;
    // sets the state - switch reference currentValidator
    currentValidator = switch( aDataType.atomicType() ) {
      case NONE -> new NoneValidator( aDataType.params() );
      case BOOLEAN -> new BooleanValidator( aDataType.params() );
      case INTEGER -> new IntegerValidator( aDataType.params() );
      case FLOATING -> new FloatingValidator( aDataType.params() );
      case STRING -> new StringValidator( aDataType.params() );
      case TIMESTAMP -> new TimestampValidator( aDataType.params() );
      case VALOBJ -> new ValobjValidator( aDataType.params() );
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает атомарный тип данных по отношению к которому проверяется значение.
   *
   * @return {@link EAtomicType} - тип данных по отношению к которому проверяется значение
   */
  public EAtomicType atomicType() {
    return dataType.atomicType();
  }

  /**
   * Возвращает используемые ограничения типа.
   *
   * @return {@link IOptionSet} - используемые ограничения типа
   */
  public IOptionSet constraints() {
    return dataType.params();
  }

  /**
   * Returns the datomic data type.
   *
   * @return {@link IDataType} - value vill be vaildated agains this type
   */
  public IDataType dataType() {
    return dataType;
  }

  // ------------------------------------------------------------------------------------
  // ITsValidator<IAtomicValue>
  //

  @Override
  public ValidationResult validate( IAtomicValue aValue ) {
    // нельзя проверять null ссылку
    if( aValue == null ) {
      return ValidationResult.error( MSG_ERR_NULL_REF_NOT_ALLOWED );
    }
    // IAtomicValue.NULL допустимо для всех типов, если разрешено "отсутствие значения"
    if( aValue == IAtomicValue.NULL ) {
      if( !dataType.params().getBool( IAvMetaConstants.TSID_IS_NULL_ALLOWED, true ) ) {
        return ValidationResult.error( MSG_ERR_NULL_NOT_ALLOWED );
      }
      return ValidationResult.SUCCESS;
    }
    // проверим на соответствие типа (кроме типа NONE - с ним совместимы все типы)
    if( dataType.atomicType() != EAtomicType.NONE && aValue.atomicType() != dataType.atomicType() ) {
      return ValidationResult.error( FMT_ERR_INCOMPATIBLE_TYPES, aValue.atomicType().description(),
          dataType.atomicType().description() );
    }
    // проверим не-null и не-NULL значение того атомарного типа, которго ожидает currentValidator
    return currentValidator.validate( aValue );
  }

}
