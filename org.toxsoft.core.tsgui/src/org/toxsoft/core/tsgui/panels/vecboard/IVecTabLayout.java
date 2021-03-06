package org.toxsoft.core.tsgui.panels.vecboard;

/**
 * Раскладка, в которой каждый элемент является вклюадкой (Tab).
 * <p>
 * Порядок вкладок определяется порядком добавления элементов во вкладке.
 * <p>
 * При создании раскладки, в конструкторе указываются параметры раслакдки:
 * <ul>
 * <li>{@link #isTabsAtBottom()} - true=показывать ярлыки вкладки внизу, false= вверху панели.</li>
 * </ul>
 * Каждый контроль в раскладку добавляется в виде отдельной вкладки.
 *
 * @author hazard157
 */
public interface IVecTabLayout
    extends IVecLayout<IVecTabLayoutData> {

  /**
   * Возвращает признак показа ярлыков вкладок внизу панели.
   *
   * @return boolean - признак показа ярлыков вкладок внизу (а не вверху) панели<br>
   *         <b>true</b> - ярлыки вкладок будут внизу панели;<br>
   *         <b>false</b> - ярлыки будут сверху панели.
   */
  boolean isTabsAtBottom();

}
