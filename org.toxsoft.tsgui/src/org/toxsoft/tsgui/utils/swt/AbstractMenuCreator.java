package org.toxsoft.tsgui.utils.swt;

import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

/**
 * Базовый класс для облегчения реализации {@link IMenuCreator}.
 * <p>
 * Данный класс работает в одном из двух режимов:
 * <ul>
 * <li>кеширование меню ({@link #isMenuChached()}=<code>true</code>) - меню создается методом {@link #fillMenu(Menu)}
 * при первом использовании, созданная ссылка запоминается. В дальнейшем, при обращении к методам getMenu() интерфейса
 * {@link IMenuCreator}, возвращается созданная ссылка;</li>
 * <li>динамическое меню ({@link #isMenuChached()}=<code>false</code>) - при каждом обращении к методам getMenu()
 * интерфейса {@link IMenuCreator}, создается новый пустой {@link Menu}, и заполняется пунктами вызовом метода
 * {@link #fillMenu(Menu)}.</li>
 * </ul>
 * В режиме кеширования методы {@link IMenuCreator}.getMenu(), если {@link #fillMenu(Menu)} веренет <code>false</code>,
 * то меню считаеться не созданным, getMenu() возвращает null, и каждый раз будет вызываться {@link #fillMenu(Menu)},
 * пока он не верент <code>true</code>.
 *
 * @author hazard157
 */
public abstract class AbstractMenuCreator
    implements IMenuCreator {

  private final boolean cacheOnceCreatedMenu;
  private Menu          fMenu = null;

  /**
   * Создает поставщик меню.
   *
   * @param aCacheOnceCreatedMenu boolean - признак, кешировани меню<br>
   *          <b>true</b> - меню создается один раз, а getMenu() возвращает всегда одну и ту же ссылку;<br>
   *          <b>false</b> - меню создается каждый рах при вызове getMenu().
   */
  public AbstractMenuCreator( boolean aCacheOnceCreatedMenu ) {
    cacheOnceCreatedMenu = aCacheOnceCreatedMenu;
  }

  /**
   * Конструктор без кеширования, равнозначен {@link #AbstractMenuCreator(boolean) AbstractMenuCreator(<b>false</b>)}.
   */
  public AbstractMenuCreator() {
    this( false );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IMenuCreator
  //

  @Override
  public void dispose() {
    if( fMenu != null ) {
      fMenu.dispose();
      fMenu = null;
    }
  }

  @Override
  final public Menu getMenu( Control aParent ) {
    if( fMenu != null ) {
      if( cacheOnceCreatedMenu ) {
        return fMenu;
      }
      fMenu.dispose();
      fMenu = null;
    }
    Menu tempMenu = new Menu( aParent );
    if( fillMenu( tempMenu ) ) {
      fMenu = tempMenu;
      return fMenu;
    }
    tempMenu.dispose();
    return null;
  }

  @Override
  final public Menu getMenu( Menu aParent ) {
    if( fMenu != null ) {
      if( cacheOnceCreatedMenu ) {
        return fMenu;
      }
      fMenu.dispose();
      fMenu = null;
    }
    Menu tempMenu = new Menu( aParent );
    if( fillMenu( tempMenu ) ) {
      fMenu = tempMenu;
      return fMenu;
    }
    tempMenu.dispose();
    return null;
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Возвращает признак кеширования меню.
   *
   * @return boolean - признак кеширования меню<br>
   *         <b>true</b> - меню создается один раз, а getMenu() возвращает всегда одну и ту же ссылку;<br>
   *         <b>false</b> - меню создается каждый рах при вызове getMenu().
   */
  boolean isMenuChached() {
    return cacheOnceCreatedMenu;
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения наследниками
  //

  /**
   * Реализация в наследние должна заполнить переданное меню нужными пунктами.
   *
   * @param aMenu {@link Menu} - пустое меню для заполнения нужными пунктами
   * @return boolean - признак, что меню надо показывать<br>
   *         <b>true</b> - меню успешно создано, оно должно быть показано - getMenu() вернут ненулевое значение;<br>
   *         <b>false</b> - меню не было создано, его не надо показывать, getMenu() возвращают null.
   */
  protected abstract boolean fillMenu( Menu aMenu );

}
