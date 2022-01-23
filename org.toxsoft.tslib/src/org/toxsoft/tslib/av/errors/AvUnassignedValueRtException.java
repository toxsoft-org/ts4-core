package org.toxsoft.tslib.av.errors;

import static org.toxsoft.tslib.av.errors.ITsResources.*;

import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.utils.errors.TsRuntimeException;

// TRANSLATE

/**
 * Ошибка доступа к значению в неинициализированном данном.
 * <p>
 * Данное исключение выбрасывается при попытке прочитать занчение из, которому не присвоено значение, то есть, для
 * которого {@link IAtomicValue#isAssigned()}=false.
 * <p>
 * Следует иметь в виду, что допустимо сущствование таких реализации интерфейса {@link IAtomicValue}, которые при вызове
 * методов asXxx() вместо выбрасываения исключения могут возвращать предопределенные значения.
 *
 * @author hazard157
 */
public class AvUnassignedValueRtException
    extends TsRuntimeException {

  private static final long serialVersionUID = 157157L;

  /**
   * Создает трансилирующее исключение.
   *
   * @param aCause Throwable - ошибка, вызвавшее данное исключние
   * @param aMessageFormat String - форматирующее сообщение о сути исключения
   * @param aMsgArgs Object[] - аргументы форматированного сообщения
   */
  public AvUnassignedValueRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
    super( aCause, aMessageFormat, aMsgArgs );
  }

  /**
   * Создает исключение ТоксСофт, происшедшее в коде.
   *
   * @param aMessageFormat String - форматирующее сообщение о сути исключения
   * @param aMsgArgs Object[] - аргументы форматированного сообщения
   */
  public AvUnassignedValueRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Создает трансилирующее исключение ТоксСофт, с предопределенным текстом сообщения.
   *
   * @param aCause Throwable - ошибка, вызвавшее данное исключние
   */
  public AvUnassignedValueRtException( Throwable aCause ) {
    super( ERR_MSG_STD_UNASSIGNED_VALUE, aCause );
  }

  /**
   * Создает исключение ТоксСофт, с предопределенным текстом сообщения.
   */
  public AvUnassignedValueRtException() {
    super( ERR_MSG_STD_UNASSIGNED_VALUE );
  }

  /**
   * Проверяет выражение, и если оно не верно, выбрасывает исключение.
   *
   * @param aExpression boolean - проверяемое выражение
   * @param aMessageFormat String - форматирующее сообщение о сути исключения
   * @param aMsgArgs Object[] - аргументы форматированного сообщения
   * @throws AvUnassignedValueRtException - если aExpression == false
   */
  public static void checkFalse( boolean aExpression, String aMessageFormat, Object... aMsgArgs )
      throws AvUnassignedValueRtException {
    if( !aExpression ) {
      throw new AvUnassignedValueRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Проверяет выражение, и если оно не верно, выбрасывает исключение с предопределенным текстом сообщения.
   *
   * @param aExpression boolean - проверяемое выражение
   * @throws AvUnassignedValueRtException - если aExpression == false
   */
  public static void checkFalse( boolean aExpression )
      throws AvUnassignedValueRtException {
    if( !aExpression ) {
      throw new AvUnassignedValueRtException( ERR_MSG_STD_UNASSIGNED_VALUE );
    }
  }

  /**
   * Проверяет выражение, и если оно верно, выбрасывает исключение.
   *
   * @param aExpression boolean - проверяемое выражение
   * @param aMessageFormat String - форматирующее сообщение о сути исключения
   * @param aMsgArgs Object[] - аргументы форматированного сообщения
   * @throws AvUnassignedValueRtException - если aExpression == true
   */
  public static void checkTrue( boolean aExpression, String aMessageFormat, Object... aMsgArgs )
      throws AvUnassignedValueRtException {
    if( aExpression ) {
      throw new AvUnassignedValueRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Проверяет выражение, и если оно верно, выбрасывает исключение с заданным текстом сообщения.
   *
   * @param aExpression boolean - проверяемое выражение
   * @throws AvUnassignedValueRtException - если aExpression == true
   */
  public static void checkTrue( boolean aExpression )
      throws AvUnassignedValueRtException {
    if( aExpression ) {
      throw new AvUnassignedValueRtException( ERR_MSG_STD_UNASSIGNED_VALUE );
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
   * @throws AvUnassignedValueRtException - проверяемая ссылка не равна null
   */
  public static <E> E checkNoNull( E aReference, String aMessageFormat, Object... aMsgArgs )
      throws AvUnassignedValueRtException {
    if( aReference != null ) {
      throw new AvUnassignedValueRtException( aMessageFormat, aMsgArgs );
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
   * @throws AvUnassignedValueRtException - проверяемая ссылка не равна null
   */
  public static <E> E checkNoNull( E aReference )
      throws AvUnassignedValueRtException {
    if( aReference != null ) {
      throw new AvUnassignedValueRtException( ERR_MSG_STD_UNASSIGNED_VALUE );
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
   * @throws AvUnassignedValueRtException - проверяемая ссылка равна null
   */
  public static <E> E checkNull( E aReference, String aMessageFormat, Object... aMsgArgs )
      throws AvUnassignedValueRtException {
    if( aReference == null ) {
      throw new AvUnassignedValueRtException( aMessageFormat, aMsgArgs );
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
   * @throws AvUnassignedValueRtException - проверяемая ссылка равна null
   */
  public static <E> E checkNull( E aReference )
      throws AvUnassignedValueRtException {
    if( aReference == null ) {
      throw new AvUnassignedValueRtException( ERR_MSG_STD_UNASSIGNED_VALUE );
    }
    return aReference;
  }

}
