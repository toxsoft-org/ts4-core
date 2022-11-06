/**
 * 
 */
package org.toxsoft.core.tslib.utils.plugins.dirscan;

import java.io.*;
import java.util.*;

/**
 * Результат сканирования директория. Содержит флаг наличяия изменений и списки измененных файлов.
 * <p>
 * Все изменения в файловой системе делатся на три категории:
 * <ul>
 * <li><b>новые файлы</b> - те файлы, имена (с путями) которых не былы в момент последненго сканирования;</li>
 * <li><b>измененные файлы</b> - файлы с такими именами были, но хотя бы одно из следующих свойств файла изменилось:
 * длина файла, время модификации (не доступа), мета-информация;</li>
 * <li><b>удаленные файлы</b> - файлы с такими именами (пятями) были, но теперь их нет.</li>
 * </ul>
 * Например, переименование файла приведет появление информации в двух списках: в новых и удаленных.
 * 
 * @author goga
 */
public interface IDirScanResult {

  /**
   * Были ли какие либо изменения в файловой системе директория.<br>
   * Другими словами, содержится ли хотя бы один элемент в списках новых, измененных или удаленных файлов, возвращаемых
   * методами данного класса.<br>
   * Естественно, изменения отслеживаются только в тех файлах, которые включены в рассмотрение фильтром, заданным в
   * конструкторе {@link DirScanner#DirScanner(File, FileFilter, boolean, IDirScanFileMetaInfoParser) DirScanner()}.
   * 
   * @return true - были изменения с момента последнего сканирования, false - изменений нет, другие методы этого класса
   *         вернут пустые списки
   */
  boolean isChanged();

  /**
   * Получить списки <b>новых</b> файлов.
   * 
   * @return файлы, которые появились в директории после последнего сканирования
   */
  List<ScannedFileInfo> getNewFiles();

  /**
   * Получить списки <b>измененных</b> файлов.
   * 
   * @return файлы, длина, время модификации или мета-информация о которых озменилось после последнего сканирования
   */
  List<ScannedFileInfo> getChangedFiles();

  /**
   * Получить списки <b>удаленных</b> файлов.
   * 
   * @return файлы, которые пропали в директории после последнего сканирования
   */
  List<ScannedFileInfo> getRemovedFiles();
}
