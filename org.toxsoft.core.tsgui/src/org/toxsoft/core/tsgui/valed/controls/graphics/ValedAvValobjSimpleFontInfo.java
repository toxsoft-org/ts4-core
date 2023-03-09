package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Аналог {@link ValedSimpleFontInfo} работающий со значениями {@link IAtomicValue} типа {@link EAtomicType#VALOBJ}.
 *
 * @author hazard157
 */
public class ValedAvValobjSimpleFontInfo
    extends AbstractValedSimpleFontInfo<IAtomicValue> {

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
      return new ValedAvValobjSimpleFontInfo( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      if( aAtomicType == EAtomicType.VALOBJ && aKeeperId != null ) {
        return aKeeperId.equals( FontInfo.KEEPER_ID );
      }
      return false;
    }

  }

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjSimpleFontInfo"; //$NON-NLS-1$

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
  ValedAvValobjSimpleFontInfo( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected IAtomicValue doGetUnvalidatedValue() {
    return avValobj( getFontInfo() );
  }

  @Override
  protected void doDoSetUnvalidatedValue( IAtomicValue aValue ) {
    IFontInfo finf = IFontInfo.NULL;
    if( aValue != null ) {
      finf = aValue.asValobj();
    }
    setFontInfo( finf );
  }

}
