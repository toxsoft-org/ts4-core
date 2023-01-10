/**
 * 
 */
package org.toxsoft.core.tslib.utils.plugins.dirscan;

/**
 * Обратный вызов при выполнении шагов сканирования директория.<br>
 * Может использоваться для:
 * <ul>
 * <li>отображения степени выполнения сканирования;</li>
 * <li>прерывания сканирования;</li>
 * <li>любых других целей.</li>
 * </ul>
 * 
 * @author hazard157
 */
public interface IDirScanCallback {

  /**
   * Вызывается единственный раз, перед началом сканирования.<br>
   * Вызывается всегда, даже если директория сканирования отсутствует и сканирование прерывается.
   */
  void beforeStart();

  /**
   * Вызывается после каждого шага сканирования.<br>
   * Возвращаемое значение метода может указать на необходимость остановить процесс сканирования.<br>
   * Сканер выдает как процентное значение степени, так и словесное описание выполненяемой работы. Словесное описание
   * состоит из описания производимого действия (aMessage) и название объекта (aProcessedObjectName), над которым
   * производится действие. Эти две строки могут быть соединены через пробел для получения осмысленной фразы.
   * 
   * @param aDone double число в диапазоне 0.0-1.0, указывает на степень выполнения работы по сканированию (1.0 = 100%)
   * @param aMessage String сообщение о том, что делает сканер (нпример, "обрабатывается файл")
   * @param aProcessedObjectName String имя текущего обрабатываемого объекта (файла)
   * @return флаг продолжения работы: true - сканирование продолжается, false - сканирование следует прервать
   */
  boolean step( double aDone, String aMessage, String aProcessedObjectName );

  /**
   * Вызывается единственный раз послке окончания сканирования.<br>
   * Вызыавается всегда, даже при прерывании сканирования исключением или пользователем.
   */
  void afterFinish();
}
