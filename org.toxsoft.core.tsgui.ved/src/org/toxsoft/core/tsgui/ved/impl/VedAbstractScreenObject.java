package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

/**
 * Базовы класс для создания экранных объектов.
 * <p>
 *
 * @author vs
 */
public abstract class VedAbstractScreenObject
    extends Stridable
    implements IScreenObject {

  /**
   * Признак видимости объекта
   */
  boolean visible = true;

  /**
   * Параметры преобразования координат
   */
  ID2Conversion d2Conv = ID2Conversion.NONE;

  /**
   * Коструктор.
   *
   * @param aId String - ИД объекта
   */
  public VedAbstractScreenObject( String aId ) {
    super( aId );
  }

  /**
   * Коструктор.
   *
   * @param aId String - ИД объекта
   * @param aName String - имя объекта
   * @param aDescription String - описние объекта
   */
  public VedAbstractScreenObject( String aId, String aName, String aDescription ) {
    super( aId, aName, aDescription );
  }

  @Override
  public ECursorType cursorType() {
    // TODO Auto-generated method stub
    return null;
  }

  // ------------------------------------------------------------------------------------
  // IScreenObject
  //

  @Override
  public final boolean visible() {
    return visible;
  }

  @Override
  public final void setVisible( boolean aVisible ) {
    visible = aVisible;
    onVisibiltyChanged();
  }

  @Override
  public final void setConversion( ID2Conversion aConversion ) {
    d2Conv = aConversion;
    onConversionChanged();
  }

  @Override
  public final ID2Conversion getConversion() {
    return d2Conv;
  }

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
