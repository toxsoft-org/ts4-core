package org.toxsoft.core.tslib.utils.txtmatch;

import static org.toxsoft.core.tslib.utils.txtmatch.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.std.StridableEnumKeeper;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Режим сравнения текстового исходного значения и текстовой константной строки.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
public enum ETextMatchMode
    implements IStridable {

  /**
   * Искомый текст содержит указанный текст.
   * <p>
   * Конкретная реализация может сравнивать как с учетом, так и без учета регистра.
   */
  CONTAINS( "Contains", STR_D_CONTAINS, STR_N_CONTAINS ),

  /**
   * Точное совпадение искомого текст с указанным.
   */
  EXACT( "Exact", STR_D_EXACT, STR_N_EXACT ),

  /**
   * Искомый текст начинается с указанного текста.
   * <p>
   * Конкретная реализация может сравнивать как с учетом, так и без учета регистра.
   */
  STARTS( "Starts", STR_D_STARTS, STR_N_STARTS ),

  /**
   * Искомый текст заканчивается на указанный текст.
   * <p>
   * Конкретная реализация может сравнивать как с учетом, так и без учета регистра.
   */
  ENDS( "Ends", STR_D_ENDS, STR_N_ENDS ),

  /**
   * Регулярное выражение ('.'=любой символ, '?'=0/1, '*'=0+, '+'=1+ раз и т.п.).
   */
  REGEXP( "Regexp", STR_D_REGEXP, STR_N_REGEXP ),

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "TextMatchMode"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ETextMatchMode> KEEPER = new StridableEnumKeeper<>( ETextMatchMode.class );

  private final String id;
  private final String description;
  private final String nmName;

  /**
   * Создать константу с заданием всех инвариантов.
   *
   * @param aId String - идентифицирующее название константы
   * @param aDescr String - отображаемое описание константы
   * @param aName String - краткое название
   */
  ETextMatchMode( String aId, String aDescr, String aName ) {
    id = aId;
    description = aDescr;
    nmName = aName;
  }

  // --------------------------------------------------------------------------
  // Реализация интерфейса IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public String nmName() {
    return nmName;
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
   * @return ETextMatchMode - найденная константа, или null если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ETextMatchMode findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( ETextMatchMode item : values() ) {
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
   * @return ETextMatchMode - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static ETextMatchMode findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Возвращает константу по описанию или null.
   *
   * @param aDescription String - описание искомой константы
   * @return ETextMatchMode - найденная константа, или null если нет константы с таким идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ETextMatchMode findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( ETextMatchMode item : values() ) {
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
   * @return ETextMatchMode - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static ETextMatchMode findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

}
