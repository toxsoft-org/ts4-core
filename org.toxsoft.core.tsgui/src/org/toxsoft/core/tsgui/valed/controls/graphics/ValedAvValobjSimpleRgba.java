package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Аналог {@link ValedSimpleRgba} работающий со значениями {@link IAtomicValue} типа {@link EAtomicType#VALOBJ}.
 * <p>
 * Редактор возвращает {@link IAtomicValue#NULL} если цвет не задан, рассматривая это как "цвет по умолчанию".
 *
 * @author hazard157
 * @author vs
 */
public class ValedAvValobjSimpleRgba
    extends AbstractValedSimpleRgba<IAtomicValue> {

  /**
   * The factory class.
   *
   * @author hazard157
   */
  public static class Factory
      extends AbstractValedControlFactory {

    /**
     * Constructor.
     */
    public Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvValobjSimpleRgba( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      if( aAtomicType == EAtomicType.VALOBJ && aKeeperId != null ) {
        return aKeeperId.equals( RGBAKeeper.KEEPER_ID );
      }
      return false;
    }

  }

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjSimpleRgba"; //$NON-NLS-1$

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Конструкторe.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ValedAvValobjSimpleRgba( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected IAtomicValue doGetUnvalidatedValue() {
    RGBA rgba = getRgba();
    if( rgba == null ) {
      return IAtomicValue.NULL;
    }
    return avValobj( rgba );
  }

  @Override
  protected void doDoSetUnvalidatedValue( IAtomicValue aValue ) {
    if( aValue == null || aValue == IAtomicValue.NULL ) {
      setRgba( null );
    }
    else {
      setRgba( aValue.asValobj() );
    }
  }

}
