package org.toxsoft.tsgui.mws.appinf;

import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.utils.TsVersion;

/**
 * Описание приложения.
 *
 * @author hazard157
 */
public interface ITsApplicationInfo
    extends IStridable {

  /**
   * Возвращает алиас (псевдоним) - сокращенный идентификатор разворачиваемого приложения.
   * <p>
   * Недлинное (2-5) символов название проекта в формате ИД-имени. Используется при формировании разлиных имен и
   * идентификаторов. Примеры: tm (Tbilisi Metro), n1 (Nornikel 1 app).
   *
   * @return String - сокращенный идентификатор (ИД-имя длиной 2-5 символов) разворачиваемого приложения
   */
  String alias();

  /**
   * Возвращает версию приложения.
   *
   * @return {@link TsVersion} - версия приложения
   */
  TsVersion version();

}
