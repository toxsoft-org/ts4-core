package org.toxsoft.tslib.gw;

import static org.toxsoft.tslib.gw.ITsResources.*;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.bricks.validator.ITsValidator;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.gw.gwid.Gwid;
import org.toxsoft.tslib.gw.gwid.GwidList;
import org.toxsoft.tslib.gw.skid.Skid;
import org.toxsoft.tslib.gw.skid.SkidListKeeper;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

/**
 * Green world utility methods.
 *
 * @author hazard157
 */
public final class GwUtils {

  // TODO TRANSLATE

  /**
   * OPTIMIZE в валидаторах надо делать прямое чтение строки и выдачу более вразумительных ошибок, учитывая, что
   * пользователь набирает Gwid по частям
   */

  /**
   * Валидатор формата канонической строки {@link Gwid}.
   * <p>
   * Валидатор не допускает <code>null</code> на входе - выбрасывает исключение.
   */
  public static final ITsValidator<String> GWID_STR_VALIDATOR = aValue -> {
    TsNullArgumentRtException.checkNull( aValue );
    try {
      if( aValue.isBlank() ) {
        return ValidationResult.error( MSG_ERR_GWID_STR_EMPTY );
      }
      Gwid.of( aValue );
      return ValidationResult.SUCCESS;
    }
    catch( @SuppressWarnings( "unused" ) Exception ex ) {
      return ValidationResult.error( MSG_ERR_INV_GWID_FORMAT );
    }
  };

  /**
   * Валидатор формата канонической строки {@link Gwid}, представленной в виде атомарного значения
   * {@link EAtomicType#STRING}.
   * <p>
   * Валидатор не допускает <code>null</code> на входе - выбрасывает исключение.
   * <p>
   * Рассматриваются только атомарные значения типа {@link EAtomicType#STRING}, остальные типы кроме
   * {@link EAtomicType#NONE} возвращают сообщение о неверном типе. {@link EAtomicType#NONE}, и что то же самое
   * {@link IAtomicValue#NULL} рассматриваются как пустая строка с соответствующим текстом сообщения.
   */
  public static final ITsValidator<IAtomicValue> GWID_STR_AV_VALIDATOR = aValue -> {
    TsNullArgumentRtException.checkNull( aValue );
    if( aValue == IAtomicValue.NULL || aValue.atomicType() == EAtomicType.NONE ) {
      return ValidationResult.error( MSG_ERR_GWID_STR_EMPTY );
    }
    if( aValue.atomicType() != EAtomicType.STRING ) {
      return ValidationResult.error( MSG_ERR_INV_GWID_AV );
    }
    return GWID_STR_VALIDATOR.validate( aValue.asString() );
  };

  /**
   * Регистрирует известные в библиотеке объекты-значения в {@link TsValobjUtils}.
   * <p>
   * Внимание: метод следуе вызывать в программе ровно один раз, как можно на ранней стадии работы.
   */
  public static void registerKnownValobjs() {
    TsValobjUtils.registerKeeperIfNone( Skid.KEEPER_ID, Skid.KEEPER );
    TsValobjUtils.registerKeeperIfNone( SkidListKeeper.KEEPER_ID, SkidListKeeper.KEEPER );
    TsValobjUtils.registerKeeperIfNone( Gwid.KEEPER_ID, Gwid.KEEPER );
    TsValobjUtils.registerKeeperIfNone( GwidList.KEEPER_ID, GwidList.KEEPER );
  }

  /**
   * Запрет на создание экземпляров.
   */
  private GwUtils() {
    // nop
  }

}
