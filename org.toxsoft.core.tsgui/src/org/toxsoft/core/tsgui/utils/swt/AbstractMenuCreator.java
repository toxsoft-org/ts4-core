package org.toxsoft.core.tsgui.utils.swt;

import org.eclipse.jface.action.*;
import org.eclipse.swt.widgets.*;

/**
 * Base class to ease implementation of {@link IMenuCreator}.
 * <p>
 * <p>
 * This class operates in one of two modes:
 * <ul>
 * <li>menu caching mode ({@link #isMenuChached()}=<code>true</code>) - the menu is created by the
 * {@link #fillMenu(Menu)} method the first time it is called and the created reference is remembered. The reference
 * will be returned on all subsequent calls of <code>getMenu()</code> methods;</li>
 * <li>dynamic menu ({@link #isMenuChached()}=<code>false</code>) - each time the getMenu() methods of the
 * {@link IMenuCreator} interface are called, a new empty {@link Menu} is created and filled with items by calling the
 * {@link #fillMenu(Menu)} method.</li>
 * </ul>
 * В режиме кеширования методы {@link IMenuCreator}.getMenu(), если {@link #fillMenu(Menu)} веренет <code>false</code>,
 * то меню считаеться не созданным, getMenu() возвращает null, и каждый раз будет вызываться {@link #fillMenu(Menu)},
 * пока он не верент <code>true</code>.
 *
 * @author hazard157
 */
public abstract class AbstractMenuCreator
    implements IMenuCreator {

  /**
   * No dorp-down menu creator,
   */
  public static final IMenuCreator NO_DROP_DOWN_MENU_CREATOR = new AbstractMenuCreator() {

    @Override
    protected boolean fillMenu( Menu aMenu ) {
      return false;
    }
  };

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
  // IMenuCreator
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
  // API
  //

  /**
   * Returns the sign of the cached menu.
   *
   * @return boolean - menu caching flag<br>
   *         <b>true</b> - the menu is created once, and <code>getMenu()</code> always returns the same link;<br>
   *         <b>false</b> - the menu is created every time <code>getMenu()</code> is called.
   */
  boolean isMenuChached() {
    return cacheOnceCreatedMenu;
  }

  // ------------------------------------------------------------------------------------
  // To override/implement
  //

  /**
   * The implementation must fill the passed menu with the required items.
   *
   * @param aMenu {@link Menu} - the empty menu to be filled
   * @return boolean - a sign that the menu should be shown<br>
   *         <b>true</b> - the menu was successfully created, it should be shown - <code>getMenu()</code> will return a
   *         non-<code>null</code> value;<br>
   *         <b>false</b> - the menu was not created, it should not be shown, <code>getMenu()</code> returns
   *         <code>null</code>.
   */
  protected abstract boolean fillMenu( Menu aMenu );

}
