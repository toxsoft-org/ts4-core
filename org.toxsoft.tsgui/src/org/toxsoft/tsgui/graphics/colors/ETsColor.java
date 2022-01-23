package org.toxsoft.tsgui.graphics.colors;

import static org.toxsoft.tsgui.graphics.colors.ITsResources.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Константы предопределенных цветов.
 * <p>
 * Внимание: это цвета, которые имеют <b>заданные</b> значения компонент К/З/С. То есть, тут не может быть системного
 * цвета "фон по умолчанию" и т.п., но есть "темно-красный" и другие предопределенные цвета.
 * <p>
 * Включает в себя цвета, определенные константами SWT.COLOR_XXX, а также некоторые цвета, частоиспользуемые в
 * приложениях.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
public enum ETsColor
    implements IStridable {

  /**
   * Цвет: черный.
   */
  BLACK( "Black", STR_N_BLACK, STR_D_BLACK, new RGB( 0x00, 0x00, 0x00 ), SWT.COLOR_BLACK ),

  /**
   * Цвет: темно-красный.
   */
  DARK_RED( "DarkRed", STR_N_DARK_RED, STR_D_DARK_RED, new RGB( 0x80, 0x00, 0x00 ), SWT.COLOR_DARK_RED ),

  /**
   * Цвет: темно-зеленый.
   */
  DARK_GREEN( "DarkGreen", STR_N_DARK_GREEN, STR_D_DARK_GREEN, new RGB( 0x00, 0x80, 0x00 ), SWT.COLOR_DARK_GREEN ),

  /**
   * Цвет: темно-желтый.
   */
  DARK_YELLOW( "DarkYellow", STR_N_DARK_YELLOW, STR_D_DARK_YELLOW, new RGB( 0x80, 0x80, 0x00 ), SWT.COLOR_DARK_YELLOW ),

  /**
   * Цвет: темно-синий.
   */
  DARK_BLUE( "DarkBlue", STR_N_DARK_BLUE, STR_D_DARK_BLUE, new RGB( 0x00, 0x00, 0x80 ), SWT.COLOR_DARK_BLUE ),

  /**
   * Цвет: темно-пурпурный.
   */
  DARK_MAGENTA( "DarkMagenta", STR_N_DARK_MAGENTA, STR_D_DARK_MAGENTA, new RGB( 0x80, 0x00, 0x80 ),
      SWT.COLOR_DARK_MAGENTA ),

  /**
   * Цвет: темный циан.
   */
  DARK_CYAN( "DarkCyan", STR_N_DARK_CYAN, STR_D_DARK_CYAN, new RGB( 0x00, 0x80, 0x80 ), SWT.COLOR_DARK_CYAN ),

  /**
   * Цвет: серый.
   */
  GRAY( "Gray", STR_N_GRAY, STR_D_GRAY, new RGB( 0xC0, 0xC0, 0xC0 ), SWT.COLOR_GRAY ),

  /**
   * Цвет: темно-серый.
   */
  DARK_GRAY( "DarkGray", STR_N_DARK_GRAY, STR_D_DARK_GRAY, new RGB( 0x80, 0x80, 0x80 ), SWT.COLOR_DARK_GRAY ),

  /**
   * Цвет: красный.
   */
  RED( "Red", STR_N_RED, STR_D_RED, new RGB( 0xFF, 0x00, 0x00 ), SWT.COLOR_RED ),

  /**
   * Цвет: зеленый.
   */
  GREEN( "Green", STR_N_GREEN, STR_D_GREEN, new RGB( 0x00, 0xFF, 0x00 ), SWT.COLOR_GREEN ),

  /**
   * Цвет: желтый.
   */
  YELLOW( "Yellow", STR_N_YELLOW, STR_D_YELLOW, new RGB( 0xFF, 0xFF, 0x00 ), SWT.COLOR_YELLOW ),

  /**
   * Цвет: синий.
   */
  BLUE( "Blue", STR_N_BLUE, STR_D_BLUE, new RGB( 0x00, 0x00, 0xFF ), SWT.COLOR_BLUE ),

  /**
   * Цвет: пурпурный.
   */
  MAGENTA( "Magenta", STR_N_MAGENTA, STR_D_MAGENTA, new RGB( 0xFF, 0x00, 0xFF ), SWT.COLOR_MAGENTA ),

  /**
   * Цвет: циан.
   */
  CYAN( "Cyan", STR_N_CYAN, STR_D_CYAN, new RGB( 0x00, 0xFF, 0xFF ), SWT.COLOR_CYAN ),

  /**
   * Цвет: Белый.
   */
  WHITE( "White", STR_N_WHITE, STR_D_WHITE, new RGB( 0xFF, 0xFF, 0xFF ), SWT.COLOR_WHITE ),

  ;

  private final String id;
  private final String description;
  private final String nmName;
  private final RGB    rgb;
  private final int    swtColorCode;

  /**
   * Создать константу с заданием всех инвариантов.
   *
   * @param aId String - идентифицирующее название константы
   * @param aDescr String - отображаемое описание константы
   * @param aName String - отображаемое имя константы
   * @param aRgb {@link RGB} - значения красного, зеленого и синего компонент цвета
   * @param aSwtColorCode int - код цвета SWT (одна из констант SWT.COLOR_XXX) или -1 для не-SWT цветов
   */
  ETsColor( String aId, String aName, String aDescr, RGB aRgb, int aSwtColorCode ) {
    id = aId;
    nmName = aName;
    description = aDescr;
    rgb = aRgb;
    swtColorCode = aSwtColorCode;
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
   * Возврашает код цвета SWT (одна из констант SWT.COLOR_XXX).
   * <p>
   * Если у этого цвета нет соответствующей SWT константы, то возвращает -1.
   *
   * @return int - код цвета SWT (одна из констант SWT.COLOR_XXX или -1)
   */
  public int swtColorCode() {
    return swtColorCode;
  }

  /**
   * Возвращает цвет в виде {@link RGB} - набор значений компонент красного, синего и зеленого.
   *
   * @return {@link RGB} - значения компонент цвета
   */
  public RGB rgb() {
    return rgb;
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
    return findById( aId ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Методы поиска
  //

  /**
   * Находит константу с заданным кодом цвета SWT, а если нет такой константы, возвращает null.
   *
   * @param aSwtColorCode int - код цвета SWT (одна из констант SWT.COLOR_XXX)
   * @return ETsColor - найденная константа или null
   */
  public static ETsColor findBySwtColorCode( int aSwtColorCode ) {
    for( ETsColor item : values() ) {
      if( item.swtColorCode == aSwtColorCode ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Находит константу с заданным кодом цвета SWT, а если нет такой константы, выбрасывает исключение.
   *
   * @param aSwtColorCode int - код цвета SWT (одна из констант SWT.COLOR_XXX)
   * @return ETsColor - найденная константа
   * @throws TsItemNotFoundRtException нет константы с таким кодом цвета SWT
   */
  public static ETsColor getBySwtColorCode( int aSwtColorCode ) {
    return TsItemNotFoundRtException.checkNull( findBySwtColorCode( aSwtColorCode ) );
  }

  /**
   * Находит константу с заданным идентификатором, а если нет такой константы, возвращает null.
   *
   * @param aId String - идентификатор {@link #id()} константы
   * @return ETsColor - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ETsColor findById( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( ETsColor item : values() ) {
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
   * @return ETsColor - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static ETsColor getById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findById( aId ) );
  }

  /**
   * Находит константу с заданными значениями красного, зеленого и синего компонент цвета {@link #rgb()}, а если нет
   * такой константы, возвращает null.
   *
   * @param aRgb RGB - значения красного, зеленого и синего компонент цвета {@link #rgb()} константы
   * @return ETsColor - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ETsColor findByRgb( RGB aRgb ) {
    TsNullArgumentRtException.checkNull( aRgb );
    for( ETsColor item : values() ) {
      if( item.rgb.equals( aRgb ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Находит константу с заданными значениями красного, зеленого и синего компонент цвета {@link #rgb()}, а если нет
   * такой константы, выбрасывает исключение.
   *
   * @param aRgb RGB - значения красного, зеленого и синего компонент цвета {@link #rgb()} константы
   * @return ETsColor - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static ETsColor getByRgb( RGB aRgb ) {
    return TsItemNotFoundRtException.checkNull( findByRgb( aRgb ) );
  }

}
