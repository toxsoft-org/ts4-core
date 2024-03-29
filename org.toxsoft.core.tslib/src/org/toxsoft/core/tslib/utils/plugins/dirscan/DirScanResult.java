package org.toxsoft.core.tslib.utils.plugins.dirscan;

import java.util.*;

/**
 * Простая реализация интерфейса возвращаемых данных сканирования директория.
 * 
 * @author hazard157
 */
final class DirScanResult
    implements IDirScanResult {

  List<ScannedFileInfo> newFiles = new ArrayList<>();
  List<ScannedFileInfo> changedFiles = new ArrayList<>();
  List<ScannedFileInfo> removedFiles = new ArrayList<>();
  boolean changed = false;

  /**
   * Пустой конструктор.
   */
  public DirScanResult() {
    // Пустой конструктор.
  }

  /**
   * Установить флаг того, что обнаружены изменения в сканируемых файлах.
   */
  void setChangedState() {
    changed = true;
  }

  // --------------------------------------------------------------------------
  // Руализация интерфейса IDirScanResult
  //

  @Override
  public boolean isChanged() {
    return changed;
  }

  @Override
  public List<ScannedFileInfo> getNewFiles() {
    return newFiles;
  }

  @Override
  public List<ScannedFileInfo> getChangedFiles() {
    return changedFiles;
  }

  @Override
  public List<ScannedFileInfo> getRemovedFiles() {
    return removedFiles;
  }

}
