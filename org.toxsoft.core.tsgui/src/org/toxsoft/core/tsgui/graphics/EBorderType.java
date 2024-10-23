package org.toxsoft.core.tsgui.graphics;

import static org.toxsoft.core.tsgui.graphics.ITsResources.*;

import org.eclipse.swt.SWT;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.std.StridableEnumKeeper;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * Вид прямоугольной границы, обрамляющий некоторую облесть на экране.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
public enum EBorderType
    implements IStridable {

  /**
   * Тип границы области: нет границы.
   */
  NONE( "BorderNone", STR_BT_NONE, STR_BT_NONE_D, SWT.NONE ),

  /**
   * Тип границы области: обведение вдавленной линией.
   */
  LINE( "BorderLine", STR_BT_LINE, STR_BT_LINE_D, SWT.SHADOW_NONE ),

  /**
   * Тип границы области: обведение вдавленной линией.
   */
  ETCHED( "BorderEtched", STR_BT_ECTHED, STR_BT_ECTHED_D, SWT.SHADOW_ETCHED_IN ),

  /**
   * Тип границы области: обведение выпуклой линией.
   */
  CONVEX( "BorderConvex", STR_BT_CONVEX, STR_BT_CONVEX_D, SWT.SHADOW_ETCHED_OUT ),

  /**
   * Тип границы области: имитация вдавленной облести.
   */
  BEVEL_INNER( "BorderBevelInner", STR_BT_BEVEL_INNER, STR_BT_BEVEL_INNER_D, SWT.SHADOW_IN ),

  /**
   * Тип границы области: имитация выпуклой области.
   */
  BEVEL_OUTER( "BorderBevelOuter", STR_BT_BEVEL_OUTER, STR_BT_BEVEL_OUTER_D, SWT.SHADOW_OUT );

  /**
   * Keeper ID for registration in {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "EBorderType"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static IEntityKeeper<EBorderType> KEEPER = new StridableEnumKeeper<>( EBorderType.class );

  private final String id;
  private final String name;
  private final String description;
  private final int    swtStyle;

  /**
   * Создать константу с заданием всех инвариантов.
   *
   * @param aId String - идентифицирующее название константы
   * @param aName String - краткое название константы
   * @param aDescr String - отображаемое описание константы
   * @param aSwtStyle int - соответствующие границе стиль SWT
   */
  EBorderType( String aId, String aName, String aDescr, int aSwtStyle ) {
    id = aId;
    name = aName;
    description = aDescr;
    swtStyle = aSwtStyle;
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
  // API
  //

  /**
   * Возвращает бит стиля {@link SWT}, соответствующий типу границы.
   * <p>
   * Для {@link #NONE} возвращаеться {@link SWT#NONE}, для остальных констант возвращаеться один из стилей
   * {@link SWT#SHADOW_NONE}, {@link SWT#SHADOW_IN}, {@link SWT#SHADOW_OUT}, {@link SWT#SHADOW_ETCHED_IN},
   * {@link SWT#SHADOW_ETCHED_OUT}.
   *
   * @return int - соответствующий бит стиля {@link SWT}
   */
  public int swtStyle() {
    return swtStyle;
  }

  // ----------------------------------------------------------------------------------
  // Методы проверки
  //

  /**
   * Определяет, существует ли константа с заданным идентификатором.
   *
   * @param aId String - идентификатор {@link #id()} константы
   * @return boolean - <b>true</b> - да, есть константа с таким идентификатором;<br>
   *         <b>false</b> - нет такой константы.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemById( String aId ) {
    return findByIdOrNull( aId ) != null;
  }

  /**
   * Определяет, существует ли константа с заданным описанием.
   *
   * @param aDescription String - описание {@link #id()} константы
   * @return boolean - <b>true</b> - да, есть константа с таким описанием;<br>
   *         <b>false</b> - нет такой константы.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByDescription( String aDescription ) {
    return findByDescriptionOrNull( aDescription ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Методы поиска
  //

  /**
   * Находит константу с заданным идентификатором, а если нет такой константы, возвращает null.
   *
   * @param aId String - идентификатор {@link #id()} константы
   * @return EBorderType - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EBorderType findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( EBorderType item : values() ) {
      if( item.id.equals( aId ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Находит константу с заданным идентификатором, а если нет такой константы, выбрасывает исключение.
   *
   * @param aId String - идентификатор {@link #id()} константы
   * @return EBorderType - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EBorderType findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Находит константу с заданным описанием, а если нет такой константы, возвращает null.
   *
   * @param aDescription String - описание {@link #description()} константы
   * @return EBorderType - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EBorderType findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( EBorderType item : values() ) {
      if( item.description.equals( aDescription ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Находит константу с заданным описанием, а если нет такой константы, выбрасывает исключение.
   *
   * @param aDescription String - описание {@link #description()} константы
   * @return EBorderType - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static EBorderType findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

}
