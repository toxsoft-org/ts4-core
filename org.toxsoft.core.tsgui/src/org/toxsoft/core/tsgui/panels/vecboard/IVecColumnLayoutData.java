package org.toxsoft.core.tsgui.panels.vecboard;

/**
 * Неизменяемый класс параметров размещения элементов для раскладки {@link IVecColumnLayout}.
 *
 * @author vs
 */
public interface IVecColumnLayoutData {

  /**
   * Единстрвенный экземпляр класса
   */
  IVecColumnLayoutData INSTANCE = new VecColumnLayoutData();
}

class VecColumnLayoutData
    implements IVecColumnLayoutData {

  protected VecColumnLayoutData() {
    // nop
  }
}
