package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;

/**
 * Аналог {@link ValedSimpleFontInfo} работающий со значениями {@link IAtomicValue} типа {@link EAtomicType#VALOBJ}.
 *
 * @author hazard157
 */
public class ValedAvValobjSimpleFontInfo
    extends AbstractAvValobjWrapperValedControl<IFontInfo> {

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

  ValedAvValobjSimpleFontInfo( ITsGuiContext aTsContext ) {
    super( aTsContext, ValedSimpleFontInfo.FACTORY );
  }

}
