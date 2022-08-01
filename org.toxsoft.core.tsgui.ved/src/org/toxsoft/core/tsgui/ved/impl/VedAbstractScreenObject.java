package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * Базовы класс для создания экранных объектов.
 * <p>
 *
 * @author vs
 * @param <T> - "суть" экранного объекта
 */
public abstract class VedAbstractScreenObject<T>
    implements IScreenObject {

  /**
   * Признак видимости объекта
   */
  boolean visible = true;
//
//  /**
//   * Параметры преобразования координат
//   */
//  ID2Conversion d2Conv = ID2Conversion.NONE;

  /**
   * Тип экранного объекта, зависящий от {@link #entity()}
   */
  private final EScreenObjectKind kind;

  /**
   * "суть" экранного объекта
   */
  private final T entity;

  /**
   * Коструктор.
   *
   * @param aKind EScreenObjectKind - тип экранного объекта, зависящий от {@link #entity()}
   * @param aEntity T - "суть" экранного объекта
   */
  public VedAbstractScreenObject( EScreenObjectKind aKind, T aEntity ) {
    entity = aEntity;
    kind = aKind;
  }

  // ------------------------------------------------------------------------------------
  // IScreenObject
  //

  @Override
  public EScreenObjectKind kind() {
    return kind;
  }

  @Override
  public T entity() {
    return entity;
  }

  @Override
  public ECursorType cursorType() {
    return null;
  }

  @Override
  public final boolean visible() {
    return visible;
  }

  @Override
  public final void setVisible( boolean aVisible ) {
    visible = aVisible;
    onVisibiltyChanged();
  }

//  @Override
//  public final void setConversion( ID2Conversion aConversion ) {
//    d2Conv = aConversion;
//    onConversionChanged();
//  }

  // ------------------------------------------------------------------------------------
  // Методы для возможного переопределения в наследниках
  //

  protected void onConversionChanged() {
    // nop
  }

  protected void onVisibiltyChanged() {
    // nop
  }
}
