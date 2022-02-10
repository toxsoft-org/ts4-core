package org.toxsoft.core.tslib.av.errors;

import static org.toxsoft.core.tslib.av.errors.ITsResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

// TODO TRANSLATE

/**
 * Ошибка преобразования типизированного значения или нетипизированного хранилища к конкретному типу.
 * <p>
 * Реальные данные по сути имеют какой либо тип ({@link EAtomicType} - целое, вещественное, дата/время и т.п.) или
 * хранится в нетипизированном виде, и не всегда возмжно представить их в любом другом виде. Например, строку чаще всего
 * нельзя получить как целое число. В таких случаях и возникает эта ошибка.
 *
 * @author hazard157
 */
public class AvTypeCastRtException
    extends TsRuntimeException {

  private static final long serialVersionUID = 157157L;

  /**
   * Создает трансилирующее исключение.
   *
   * @param aCause Throwable - ошибка, вызвавшее данное исключние
   * @param aMessageFormat String - форматирующее сообщение о сути исключения
   * @param aMsgArgs Object[] - аргументы форматированного сообщения
   */
  public AvTypeCastRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
    super( aCause, aMessageFormat, aMsgArgs );
  }

  /**
   * Создает исключение ТоксСофт, происшедшее в коде.
   *
   * @param aMessageFormat String - форматирующее сообщение о сути исключения
   * @param aMsgArgs Object[] - аргументы форматированного сообщения
   */
  public AvTypeCastRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Создает трансилирующее исключение ТоксСофт, с предопределенным текстом сообщения.
   *
   * @param aCause Throwable - ошибка, вызвавшее данное исключние
   */
  public AvTypeCastRtException( Throwable aCause ) {
    super( ERR_MSG_STD_TYPE_CAST, aCause );
  }

  /**
   * Создает исключение ТоксСофт, с предопределенным текстом сообщения.
   */
  public AvTypeCastRtException() {
    super( ERR_MSG_STD_TYPE_CAST );
  }

  /**
   * Проверяет выражение, и если оно не верно, выбрасывает исключение.
   *
   * @param aExpression boolean - проверяемое выражение
   * @param aMessageFormat String - форматирующее сообщение о сути исключения
   * @param aMsgArgs Object[] - аргументы форматированного сообщения
   * @throws AvTypeCastRtException - если aExpression == false
   */
  public static void checkFalse( boolean aExpression, String aMessageFormat, Object... aMsgArgs )
      throws AvTypeCastRtException {
    if( !aExpression ) {
      throw new AvTypeCastRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Проверяет выражение, и если оно не верно, выбрасывает исключение с предопределенным текстом сообщения.
   *
   * @param aExpression boolean - проверяемое выражение
   * @throws AvTypeCastRtException - если aExpression == false
   */
  public static void checkFalse( boolean aExpression )
      throws AvTypeCastRtException {
    if( !aExpression ) {
      throw new AvTypeCastRtException( ERR_MSG_STD_TYPE_CAST );
    }
  }

  /**
   * Проверяет выражение, и если оно верно, выбрасывает исключение.
   *
   * @param aExpression boolean - проверяемое выражение
   * @param aMessageFormat String - форматирующее сообщение о сути исключения
   * @param aMsgArgs Object[] - аргументы форматированного сообщения
   * @throws AvTypeCastRtException - если aExpression == true
   */
  public static void checkTrue( boolean aExpression, String aMessageFormat, Object... aMsgArgs )
      throws AvTypeCastRtException {
    if( aExpression ) {
      throw new AvTypeCastRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Проверяет выражение, и если оно верно, выбрасывает исключение с заданным текстом сообщения.
   *
   * @param aExpression boolean - проверяемое выражение
   * @throws AvTypeCastRtException - если aExpression == true
   */
  public static void checkTrue( boolean aExpression )
      throws AvTypeCastRtException {
    if( aExpression ) {
      throw new AvTypeCastRtException( ERR_MSG_STD_TYPE_CAST );
    }
  }

  /**
   * Проверяет ссылку, и если она не нулевая, выбрасывает исключение.<br>
   * Для удобаства использования, возвращает переданную ссылку.
   *
   * @param <E> - необязательная типизация по переданной ссылке
   * @param aReference Object - проверяемая ссылка
   * @param aMessageFormat String - форматирующее сообщение о сути исключения
   * @param aMsgArgs Object[] - аргументы форматированного сообщения
   * @return E - переданная ссылка
   * @throws AvTypeCastRtException - проверяемая ссылка не равна null
   */
  public static <E> E checkNoNull( E aReference, String aMessageFormat, Object... aMsgArgs )
      throws AvTypeCastRtException {
    if( aReference != null ) {
      throw new AvTypeCastRtException( aMessageFormat, aMsgArgs );
    }
    return aReference;
  }

  /**
   * Проверяет ссылку, и если она не нулевая, выбрасывает исключение с заданным текстом сообщения.<br>
   * Для удобаства использования, возвращает переданную ссылку.
   *
   * @param <E> - необязательная типизация по переданной ссылке
   * @param aReference Object - проверяемая ссылка
   * @return E - переданная ссылка
   * @throws AvTypeCastRtException - проверяемая ссылка не равна null
   */
  public static <E> E checkNoNull( E aReference )
      throws AvTypeCastRtException {
    if( aReference != null ) {
      throw new AvTypeCastRtException( ERR_MSG_STD_TYPE_CAST );
    }
    return aReference;
  }

  /**
   * Проверяет ссылку, и если она нулевая, выбрасывает исключение.<br>
   * Для удобаства использования, возвращает переданную ссылку.
   *
   * @param <E> - необязательная типизация по переданной ссылке
   * @param aReference Object - проверяемая ссылка
   * @param aMessageFormat String - форматирующее сообщение о сути исключения
   * @param aMsgArgs Object[] - аргументы форматированного сообщения
   * @return E - переданная ссылка
   * @throws AvTypeCastRtException - проверяемая ссылка равна null
   */
  public static <E> E checkNull( E aReference, String aMessageFormat, Object... aMsgArgs )
      throws AvTypeCastRtException {
    if( aReference == null ) {
      throw new AvTypeCastRtException( aMessageFormat, aMsgArgs );
    }
    return aReference;
  }

  /**
   * Проверяет ссылку, и если она нулевая, выбрасывает исключение с заданным текстом сообщения.<br>
   * Для удобаства использования, возвращает переданную ссылку.
   *
   * @param <E> - необязательная типизация по переданной ссылке
   * @param aReference Object - проверяемая ссылка
   * @return E - переданная ссылка
   * @throws AvTypeCastRtException - проверяемая ссылка равна null
   */
  public static <E> E checkNull( E aReference )
      throws AvTypeCastRtException {
    if( aReference == null ) {
      throw new AvTypeCastRtException( ERR_MSG_STD_TYPE_CAST );
    }
    return aReference;
  }

  /**
   * Проверят, можно ли перемнной указанного типа присвоить значение указанного типа.
   *
   * @param aLvalType {@link EAtomicType} - тип переменной
   * @param aRvalType {@link EAtomicType} - тип значение
   * @return boolean - результат проверки
   * @throws AvTypeCastRtException типы не равны между собой и ни один из них не {@link EAtomicType#NONE}
   */
  public static boolean canAssign( EAtomicType aLvalType, EAtomicType aRvalType )
      throws AvTypeCastRtException {
    if( aLvalType != EAtomicType.NONE && aRvalType != EAtomicType.NONE ) {
      if( aLvalType != aRvalType ) {
        return false;
      }
    }
    return true;
  }

  /**
   * Проверят, можно ли перемнной указанного типа присвоить значение указанного типа.
   *
   * @param aLvalType {@link EAtomicType} - тип переменной
   * @param aRvalType {@link EAtomicType} - тип значение
   * @throws AvTypeCastRtException типы не равны между собой и ни один из них не {@link EAtomicType#NONE}
   */
  public static void checkCanAssign( EAtomicType aLvalType, EAtomicType aRvalType )
      throws AvTypeCastRtException {
    if( aLvalType != EAtomicType.NONE && aRvalType != EAtomicType.NONE ) {
      if( aLvalType != aRvalType ) {
        throw new AvTypeCastRtException( FMT_ERR_CANT_ASSIGN, aLvalType.id(), aRvalType.id() );
      }
    }
  }

  /**
   * Проверят, можно ли перемнной указанного типа присвоить значение указанного типа.
   *
   * @param aLvalType {@link EAtomicType} - тип переменной
   * @param aRvalType {@link EAtomicType} - тип значение
   * @param aMessageFormat String - форматирующее сообщение о сути исключения
   * @param aMsgArgs Object[] - аргументы форматированного сообщения
   * @throws AvTypeCastRtException типы не равны между собой и ни один из них не {@link EAtomicType#NONE}
   */
  public static void checkCanAssign( EAtomicType aLvalType, EAtomicType aRvalType, String aMessageFormat,
      Object... aMsgArgs )
      throws AvTypeCastRtException {
    if( aLvalType != EAtomicType.NONE && aRvalType != EAtomicType.NONE ) {
      if( aLvalType != aRvalType ) {
        throw new AvTypeCastRtException( aMessageFormat, aMsgArgs );
      }
    }
  }

}
