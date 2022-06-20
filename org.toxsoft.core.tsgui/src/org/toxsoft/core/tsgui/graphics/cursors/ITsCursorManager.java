package org.toxsoft.core.tsgui.graphics.cursors;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Менеджер работы с курсорами {@link Cursor}.
 * <p>
 * У этого менеджера две функции:
 * <ul>
 * <li>содержать в себе кеш курсоров {@link Cursor} и автоматиечски удалять закешированные ресурсы курсоров при
 * завершении программы (точнее, при уничтожении дисплея {@link Display}, ассоциированной с программой);</li>
 * <li>поддерживать понятие предопределенных курсоров {@link ECursorType};</li>
 * <li>поддерживать понятие пользовательского курсора.</li>
 * </ul>
 * <p>
 * Ссылка на экземпляр этого класса должна находится в контексте приложения.
 *
 * @author vs
 */
public interface ITsCursorManager {

  /**
   * Возвращает стандартный курсор.
   *
   * @param aCursorType {@link ECursorType} - тип предопрделенного курсора
   * @return {@link Cursor} - курсор
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  Cursor getCursor( ECursorType aCursorType );

  /**
   * Находит курсор по имени.
   * <p>
   * Именем курсора может быть любое, заданное пользователем в {@link #putCursor(String, Cursor)} название курсора. <br>
   * Если курсор с таким именем отсутствует, то возвращает null. Null является допустимым аргументом для метода
   * {@link Control#setCursor(Cursor)}, поэтому исключение не порождается.
   * <p>
   * В качестве имени может использоваться идентификатор предопределенного курсора {@link ECursorType#id()}.
   *
   * @param aCursorId String - идентификатор курсора
   * @return {@link Cursor} - кешированный курсор или <code>null</code>
   * @throws TsNullArgumentRtException аргумент = null
   */
  Cursor findCursor( String aCursorId );

  /**
   * Определяет, существует ли в кеше курсор с заданным именем.
   * <p>
   * Считается, что все предопределенные курсоры {@link ECursorType} всегда есть в кеше и их можно переопределить
   * методом {@link #putCursor(String, Cursor)}.
   *
   * @param aCursorId String - идентификатор курсора
   * @return boolean - признак существования курсора с заданным именем в кеше
   * @throws TsNullArgumentRtException аргумент = null
   */
  boolean hasCursor( String aCursorId );

  /**
   * Размещает в кеше курсор с заданным именем.
   * <p>
   * В случае, если курсор с таким именем уже закширован, заменяет его, в том числе, предопределенные курсоры.
   * <p>
   * Внимание: предыдущий курсор уничтожается менеджером, и вообще, после вызова этого метода экземпляр и нового курсора
   * будет уничтожен менеджером.
   *
   * @param aCursorId String - идентификатор (ИД-путь) курсора
   * @param aCursor {@link Cursor} - курсор
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   * @throws TsIllegalArgumentRtException попытка положить в кеш уже удаленный курсор
   */
  void putCursor( String aCursorId, Cursor aCursor );

}
