package org.toxsoft.tslib.av.errors;

import static org.toxsoft.tslib.av.errors.ITsResources.*;

import org.toxsoft.tslib.utils.errors.TsRuntimeException;

// TRANSLATE

/**
 * Потеря данных при преобразовании к типу с меньшей разрядностью.
 * <p>
 * Данная ошибка возникает при попытке получить значение какого нибуть типа как совместимый тип, но с меньшим диапазоном
 * допустимых значений, и при этом реальное значение выходит за допустимые пределы. В частности, при попытке получить
 * целое как int или long, если значение целого выходит за пределы 32-х или 64-х разрядных числе соответственно.
 * Аналогичные ситуации возможны с вещественными числами. Также возможна ситуация при попытке получить данные в буфер
 * (массив) меньшей длины, чем исходный.
 *
 * @author hazard157
 */
public class AvDataLossRtException
    extends TsRuntimeException {

  private static final long serialVersionUID = 157157L;

  /**
   * Создает трансилирующее исключение.
   *
   * @param aCause Throwable - ошибка, вызвавшее данное исключние
   * @param aMessageFormat String - форматирующее сообщение о сути исключения
   * @param aMsgArgs Object[] - аргументы форматированного сообщения
   */
  public AvDataLossRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
    super( aCause, aMessageFormat, aMsgArgs );
  }

  /**
   * Создает исключение ТоксСофт, происшедшее в коде.
   *
   * @param aMessageFormat String - форматирующее сообщение о сути исключения
   * @param aMsgArgs Object[] - аргументы форматированного сообщения
   */
  public AvDataLossRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Создает трансилирующее исключение ТоксСофт, с предопределенным текстом сообщения.
   *
   * @param aCause Throwable - ошибка, вызвавшее данное исключние
   */
  public AvDataLossRtException( Throwable aCause ) {
    super( ERR_MSG_STD_DATA_LOSS, aCause );
  }

  /**
   * Создает исключение ТоксСофт, с предопределенным текстом сообщения.
   */
  public AvDataLossRtException() {
    super( ERR_MSG_STD_DATA_LOSS );
  }

  /**
   * Проверяет выражение, и если оно не верно, выбрасывает исключение.
   *
   * @param aExpression boolean - проверяемое выражение
   * @param aMessageFormat String - форматирующее сообщение о сути исключения
   * @param aMsgArgs Object[] - аргументы форматированного сообщения
   * @throws AvDataLossRtException - если aExpression == false
   */
  public static void checkFalse( boolean aExpression, String aMessageFormat, Object... aMsgArgs )
      throws AvDataLossRtException {
    if( !aExpression ) {
      throw new AvDataLossRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Проверяет выражение, и если оно не верно, выбрасывает исключение с предопределенным текстом сообщения.
   *
   * @param aExpression boolean - проверяемое выражение
   * @throws AvDataLossRtException - если aExpression == false
   */
  public static void checkFalse( boolean aExpression )
      throws AvDataLossRtException {
    if( !aExpression ) {
      throw new AvDataLossRtException( ERR_MSG_STD_DATA_LOSS );
    }
  }

  /**
   * Проверяет выражение, и если оно верно, выбрасывает исключение.
   *
   * @param aExpression boolean - проверяемое выражение
   * @param aMessageFormat String - форматирующее сообщение о сути исключения
   * @param aMsgArgs Object[] - аргументы форматированного сообщения
   * @throws AvDataLossRtException - если aExpression == true
   */
  public static void checkTrue( boolean aExpression, String aMessageFormat, Object... aMsgArgs )
      throws AvDataLossRtException {
    if( aExpression ) {
      throw new AvDataLossRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Проверяет выражение, и если оно верно, выбрасывает исключение с заданным текстом сообщения.
   *
   * @param aExpression boolean - проверяемое выражение
   * @throws AvDataLossRtException - если aExpression == true
   */
  public static void checkTrue( boolean aExpression )
      throws AvDataLossRtException {
    if( aExpression ) {
      throw new AvDataLossRtException( ERR_MSG_STD_DATA_LOSS );
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
   * @throws AvDataLossRtException - проверяемая ссылка не равна null
   */
  public static <E> E checkNoNull( E aReference, String aMessageFormat, Object... aMsgArgs )
      throws AvDataLossRtException {
    if( aReference != null ) {
      throw new AvDataLossRtException( aMessageFormat, aMsgArgs );
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
   * @throws AvDataLossRtException - проверяемая ссылка не равна null
   */
  public static <E> E checkNoNull( E aReference )
      throws AvDataLossRtException {
    if( aReference != null ) {
      throw new AvDataLossRtException( ERR_MSG_STD_DATA_LOSS );
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
   * @throws AvDataLossRtException - проверяемая ссылка равна null
   */
  public static <E> E checkNull( E aReference, String aMessageFormat, Object... aMsgArgs )
      throws AvDataLossRtException {
    if( aReference == null ) {
      throw new AvDataLossRtException( aMessageFormat, aMsgArgs );
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
   * @throws AvDataLossRtException - проверяемая ссылка равна null
   */
  public static <E> E checkNull( E aReference )
      throws AvDataLossRtException {
    if( aReference == null ) {
      throw new AvDataLossRtException( ERR_MSG_STD_DATA_LOSS );
    }
    return aReference;
  }

}
