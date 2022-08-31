package org.toxsoft.core.tsgui.dialogs;

import static org.toxsoft.core.tsgui.dialogs.ITsGuiDialogResources.*;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.std.StridableEnumKeeper;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * Локализуемые ресурсы работы с различными диалогами.
 *
 * @author hazard157
 */
public enum ETsDialogCode
    implements IStridable {

  /**
   * Кнопка "Закрыть" и код возврата при закрытии окна диалога.
   * <p>
   * Обратите внимание, что этот код возвращается при закрытии окна диалога (любым способом кроме нажатия на одну из
   * кнопок диалога) вне зависимости от того, есть ли такая кнопка в диалоге.
   */
  CLOSE( "Close", STR_N_CLOSE, STR_D_CLOSE, IDialogConstants.CLOSE_ID ), //$NON-NLS-1$

  /**
   * Кпонка и код возврата "ДА".
   */
  YES( "Yes", STR_N_YES, STR_D_YES, IDialogConstants.YES_ID ), //$NON-NLS-1$

  /**
   * Кпонка и код возврата "НЕТ".
   */
  NO( "No", STR_N_NO, STR_D_NO, IDialogConstants.NO_ID ), //$NON-NLS-1$

  /**
   * Кпонка и код возврата "ПРИНЯТЬ".
   */
  OK( "OK", STR_N_OK, STR_D_OK, IDialogConstants.OK_ID ), //$NON-NLS-1$

  /**
   * Кпонка и код возврата "ОТМЕНИТЬ".
   */
  CANCEL( "Cancel", STR_N_CANCEL, STR_D_CANCEL, IDialogConstants.CANCEL_ID ), //$NON-NLS-1$

  /**
   * Кпонка и код возврата "ОТМЕНИТЬ".
   */
  APPLY( "Apply", STR_N_APPLY, STR_D_APPLY, IDialogConstants.CLIENT_ID + 1 ); //$NON-NLS-1$

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "TsDialogCode"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<ETsDialogCode> KEEPER = new StridableEnumKeeper<>( ETsDialogCode.class );

  private static IStridablesListEdit<ETsDialogCode> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final int    jfaceButtonId;

  /**
   * Создает константу со всеми инвариантами.
   *
   * @param aId String - идентификатор (ИД-путь) константы
   * @param aName String - краткое удобочитаемое название константы
   * @param aDescription String - отображаемое описание константы
   * @param aJFaceId int - код (если есть) кнопки из {@link IDialogConstants}.XXX_ID
   */
  ETsDialogCode( String aId, String aName, String aDescription, int aJFaceId ) {
    id = aId;
    name = aName;
    description = aDescription;
    jfaceButtonId = aJFaceId;
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

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает код кпоки согласно {@link IDialogConstants}.XXX_ID.
   *
   * @return int - целочисленный код (идентификатор) кнопки
   */
  public int jfaceButtonId() {
    return jfaceButtonId;
  }

  /**
   * Находит константу с заданным JFace-идентификатором кнопки, а если нет такой константы, возвращает null.
   *
   * @param aButtonId int - JFace-идентификатор кнопки
   * @return ETsDialogCode - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ETsDialogCode finaByJFaceId( int aButtonId ) {
    for( ETsDialogCode item : values() ) {
      if( item.jfaceButtonId == aButtonId ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Находит константу с заданным описанием, а если нет такой константы, выбрасывает исключение.
   *
   * @param aButtonId int - JFace-идентификатор кнопки
   * @return ETsDialogCode - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static ETsDialogCode getByJFaceId( int aButtonId ) {
    return TsItemNotFoundRtException.checkNull( finaByJFaceId( aButtonId ) );
  }

  /**
   * Возвращает все константы в виде списка.
   *
   * @return {@link IStridablesList}&lt; {@link ETsDialogCode} &gt; - список всех констант
   */
  public static IStridablesList<ETsDialogCode> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Определяет, существует ли константа перечисления с заданным идентификатором.
   *
   * @param aId String - идентификатор искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным идентификатором существует;<br>
   *         <b>false</b> - нет константы с таким идентификатором.
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static boolean isItemById( String aId ) {
    return findById( aId ) != null;
  }

  /**
   * Находит константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return ETsDialogCode - найденная константа, или <code>null</code> если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static ETsDialogCode findById( String aId ) {
    return asList().findByKey( aId );
  }

  /**
   * Возвращает константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return ETsDialogCode - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static ETsDialogCode getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Определяет, существует ли константа перечисления с заданным именем.
   *
   * @param aName String - имя (название) искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным именем существует;<br>
   *         <b>false</b> - нет константы с таким именем.
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static boolean isItemByName( String aName ) {
    return findByName( aName ) != null;
  }

  /**
   * Находит константу по имени.
   *
   * @param aName String - имя искомой константы
   * @return ETsDialogCode - найденная константа, или <code>null</code> если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static ETsDialogCode findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETsDialogCode item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по имени.
   *
   * @param aName String - имя искомой константы
   * @return ETsDialogCode - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static ETsDialogCode getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
