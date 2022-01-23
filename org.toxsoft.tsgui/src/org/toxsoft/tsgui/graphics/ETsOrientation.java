package org.toxsoft.tsgui.graphics;

import static org.toxsoft.tsgui.graphics.ITsResources.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.std.StridableEnumKeeper;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.utils.errors.*;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

/**
 * Ориентация визуальных компонент - горизонтаьная или вертикальная.
 *
 * @author goga
 */
public enum ETsOrientation
    implements IStridable {

  /**
   * Расположение по горизонтали.
   */
  HORIZONTAL( "Horizontal", STR_N_TSO_HORIZONTAL, STR_D_TSO_HORIZONTAL, SWT.HORIZONTAL ), //$NON-NLS-1$

  /**
   * Расположение по вертикали.
   */
  VERTICAL( "Vertical", STR_N_TSO_VERTICAL, STR_D_TSO_VERTICAL, SWT.VERTICAL ); //$NON-NLS-1$

  /**
   * Keeper ID for registration in {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "TsOrientation"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static IEntityKeeper<ETsOrientation> KEEPER = new StridableEnumKeeper<>( ETsOrientation.class );

  private final String id;
  private final String description;
  private final String nmName;
  private final int    swtStyle;

  /**
   * Создать константу с заданием всех инвариантов.
   *
   * @param aId String - идентифицирующее название константы
   * @param aDescr String - отображаемое описание константы
   * @param aName String - отображаемое название константы
   * @param aSwtStyle int - SWT стиль расположения, одна из констант {@link SWT#HORIZONTAL} или {@link SWT#VERTICAL}.
   */
  ETsOrientation( String aId, String aDescr, String aName, int aSwtStyle ) {
    id = aId;
    description = aDescr;
    nmName = aName;
    swtStyle = aSwtStyle;
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

  // ------------------------------------------------------------------------------------
  // Публичное API
  //

  /**
   * Возвращает другую ориентацию.
   *
   * @return {@link ETsOrientation} - другая ориентация
   */
  public ETsOrientation otherOrientation() {
    if( this == HORIZONTAL ) {
      return VERTICAL;
    }
    return HORIZONTAL;
  }

  /**
   * Возвращает признак горизонтальной ориентации.
   *
   * @return boolean - признак, что это константа {@link #HORIZONTAL}
   */
  public boolean isHorisontal() {
    return this == HORIZONTAL;
  }

  /**
   * Возвращает признак вертикальной ориентации.
   *
   * @return boolean - признак, что это константа {@link #VERTICAL}
   */
  public boolean isVertical() {
    return this == VERTICAL;
  }

  /**
   * Возвращает SWT-стиль, соответствующий данному расположению.
   * <p>
   * Метод возвращает одну из констант {@link SWT#HORIZONTAL} или {@link SWT#VERTICAL}.
   *
   * @return int - SWT стиль расположения
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

  /**
   * Определяет, существует ли константа с заданным именем.
   *
   * @param aName String - имя {@link #nmName()} константы
   * @return boolean - <b>true</b> - да, есть константа с таким именем;<br>
   *         <b>false</b> - нет такой константы.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByName( String aName ) {
    return findByNameOrNull( aName ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Методы поиска
  //

  /**
   * Находит константу с заданным идентификатором, а если нет такой константы, возвращает null.
   *
   * @param aId String - идентификатор {@link #id()} константы
   * @return EAnchorVerPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ETsOrientation findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( ETsOrientation item : values() ) {
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
   * @return EAnchorVerPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static ETsOrientation findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Находит константу с заданным описанием, а если нет такой константы, возвращает null.
   *
   * @param aDescription String - описание {@link #description()} константы
   * @return EAnchorVerPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ETsOrientation findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( ETsOrientation item : values() ) {
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
   * @return EAnchorVerPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static ETsOrientation findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

  /**
   * Находит константу с заданным именем, а если нет такой константы, возвращает null.
   *
   * @param aName String - имя {@link #nmName()} константы
   * @return ETsOrientation - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ETsOrientation findByNameOrNull( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETsOrientation item : values() ) {
      if( item.nmName.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Находит константу с заданным именем, а если нет такой константы, выбрасывает исключение.
   *
   * @param aName String - имя {@link #nmName()} константы
   * @return ETsOrientation - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static ETsOrientation findByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByNameOrNull( aName ) );
  }

  /**
   * Находит константу по SWT-стилю выравнивания, а если аргумент неверный, возвращает null.
   * <p>
   * Из аргумента все биты кроме {@link SWT#TOP}, {@link SWT#BOTTOM} и {@link SWT#CENTER} игнорируются. То есть, можно
   * прямо передавать слово SWT-стиля так, как он испольуется например в конструкторе {@link Composite}.
   * <p>
   * <b>Внимание:</b>аргумент должен содержать <b>ровно один</b> выставленный бит из {@link SWT#TOP}, {@link SWT#BOTTOM}
   * или {@link SWT#CENTER}, иначе метод вернет null.
   *
   * @param aSwtStyle int - слово с одним высталенным битом из трех {@link SWT#TOP}, {@link SWT#CENTER} или
   *          {@link SWT#BOTTOM}
   * @return {@link ETsOrientation} - найденная константа или null
   */
  public static final ETsOrientation findBySwtStyleOrNull( int aSwtStyle ) {
    switch( aSwtStyle & (SWT.HORIZONTAL | SWT.VERTICAL) ) {
      case SWT.HORIZONTAL:
        return HORIZONTAL;
      case SWT.VERTICAL:
        return VERTICAL;
      default:
        return null;
    }
  }

  /**
   * Находит константу по SWT-стилю выравнивания, а если аргумент неверный, выбрасывает исключения.
   * <p>
   * Из аргумента все биты кроме {@link SWT#TOP}, {@link SWT#BOTTOM} и {@link SWT#CENTER} игнорируются. То есть, можно
   * прямо передавать слово SWT-стиля так, как он испольуется например в конструкторе {@link Composite}.
   * <p>
   * <b>Внимание:</b>аргумент должен содержать <b>ровно один</b> выставленный бит из {@link SWT#TOP}, {@link SWT#BOTTOM}
   * или {@link SWT#CENTER}, иначе метод вернет null.
   *
   * @param aSwtStyle int - слово с одним высталенным битом из трех {@link SWT#TOP}, {@link SWT#CENTER} или
   *          {@link SWT#BOTTOM}
   * @return {@link ETsOrientation} - найденная константа
   * @throws TsIllegalArgumentRtException аргумент неверный
   */
  public static final ETsOrientation findBySwtStyle( int aSwtStyle ) {
    ETsOrientation result = findBySwtStyleOrNull( aSwtStyle );
    TsIllegalArgumentRtException.checkTrue( result == null );
    return result;
  }

}
