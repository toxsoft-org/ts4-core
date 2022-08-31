package org.toxsoft.core.tsgui.utils.checkstate;

import static org.toxsoft.core.tsgui.utils.checkstate.ITsResources.*;

import org.eclipse.jface.viewers.ICheckStateProvider;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Состояние отмеченности элементов в списках, деревях и др.
 * <p>
 * Любой элемент может быть в непомеченном {@link #UNCHECKED} состоянии. Помеченное же состояние бывает двух типов:
 * просто отметка {@link #CHECKED} - малый квадрат с галочкой или крестиком), либо состояние непределенной пометки
 * {@link #GRAYED} (визуально - малый квадрат серого цвета). Обычно, непределенное состояние задается родительским
 * элементам, часть детей которого отмечены, а часть в непомеченном состоянии.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
public enum ECheckState
    implements IStridable {

  /**
   * Элемент никак не помечен.
   */
  UNCHECKED( "Unchecked", STR_N_UNCHECKED, STR_D_UNCHECKED, false, false ),

  /**
   * Элемент отмечен квадратом с галочкой.
   */
  CHECKED( "Checked", STR_N_CHECKED, STR_D_CHECKED, true, false ),

  /**
   * Элемент отмечен непоределенным состоянием (серым квадратом).
   */
  GRAYED( "Grayed", STR_N_GRAYED, STR_D_GRAYED, true, true );

  private final String  id;
  private final String  name;
  private final String  description;
  private final boolean isAnyCheck;
  private final boolean isGrayCheck;

  ECheckState( String aId, String aName, String aDescr, boolean aIsAnyCheck, boolean aIsGrayCheck ) {
    id = aId;
    name = aName;
    description = aDescr;
    isAnyCheck = aIsAnyCheck;
    isGrayCheck = aIsGrayCheck;
  }

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ------------------------------------------------------------------------------------
  // Дополнительное API
  //

  /**
   * Определяет, соответствут ли данной константе какое либо состояние отмеченности.
   * <p>
   * Возвращает <code>true</code> для констант {@link #GRAYED} и {@link #CHECKED}.
   *
   * @return boolean - признак, что есть пометка галочкой {@link #CHECKED} или серым квадратом {@link #GRAYED}
   */
  public boolean isAnyCheck() {
    return isAnyCheck;
  }

  /**
   * Определяет, что есть пометка серым квадратом.
   * <p>
   * Возвращает <code>true</code> только для константы {@link #GRAYED}.
   *
   * @return boolean - признак пометки серым квадратом (т.е. это константа {@link #GRAYED})
   */
  public boolean isGrayCheck() {
    return isGrayCheck;
  }

  /**
   * Определяет, что есть четко определенная пометка.
   * <p>
   * Возвращает <code>true</code> только для константы {@link #CHECKED}.
   *
   * @return boolean - признак пометки
   */
  public boolean isCheck() {
    return isAnyCheck && !isGrayCheck;
  }

  /**
   * Создает константу состояния из булевых переменных состояния отметки и непределенного состояния.
   * <p>
   * Правила соответствия состоянии описано в комментарии к SWT интефейсу {@link ICheckStateProvider}.
   *
   * @param aIsChecked boolean - признак отметки галочкой
   * @param aIsGrayed boolean - признак отметки серым квадратом
   * @return {@link ECheckState} - константа, соответстввющая заданным состояниям
   */
  public static ECheckState checkState( boolean aIsChecked, boolean aIsGrayed ) {
    if( aIsChecked ) {
      if( aIsGrayed ) {
        return GRAYED;
      }
      return CHECKED;
    }
    return UNCHECKED;
  }

  // ----------------------------------------------------------------------------------
  // Методы проверки
  //

  /**
   * Определяет, существует ли константа перечисления с заданным идентификатором.
   *
   * @param aId String - идентификатор искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным идентификатором существует;<br>
   *         <b>false</b> - неет константы с таким идентификатором.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemById( String aId ) {
    return findByIdOrNull( aId ) != null;
  }

  /**
   * Определяет, существует ли константа перечисления с заданным описанием.
   *
   * @param aDescription String - описание искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным описанием существует;<br>
   *         <b>false</b> - неет константы с таким описанием.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByDescription( String aDescription ) {
    return findByDescriptionOrNull( aDescription ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Методы поиска
  //

  /**
   * Возвращает константу по идентификатору или null.
   *
   * @param aId String - идентификатор искомой константы
   * @return ECheckState - найденная константа, или null если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ECheckState findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( ECheckState item : values() ) {
      if( item.id.equals( aId ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по идентификатору или выбрасывает исключение.
   *
   * @param aId String - идентификатор искомой константы
   * @return ECheckState - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static ECheckState findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Возвращает константу по описанию или null.
   *
   * @param aDescription String - описание искомой константы
   * @return ECheckState - найденная константа, или null если нет константы с таким идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ECheckState findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( ECheckState item : values() ) {
      if( item.description.equals( aDescription ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по описанию или выбрасывает исключение.
   *
   * @param aDescription String - описание искомой константы
   * @return ECheckState - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static ECheckState findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

}
