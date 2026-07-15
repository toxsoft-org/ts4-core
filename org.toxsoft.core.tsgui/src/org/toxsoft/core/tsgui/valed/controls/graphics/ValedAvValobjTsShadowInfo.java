package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.shadow.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;

/**
 * {@link EAtomicType#VALOBJ} of type {@link TsShadowInfo} editor.
 * <p>
 * Wraps over {@link ValedTsShadowInfo}.
 *
 * @author vs
 */
public class ValedAvValobjTsShadowInfo
    extends AbstractAvValobjWrapperValedControl<TsShadowInfo> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjTsShadowInfo"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author vs
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvValobjTsShadowInfo( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      if( aAtomicType == EAtomicType.VALOBJ && aKeeperId != null ) {
        return aKeeperId.equals( TsShadowInfo.KEEPER_ID );
      }
      return false;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  protected ValedAvValobjTsShadowInfo( ITsGuiContext aTsContext ) {
    super( aTsContext, ValedTsShadowInfo.FACTORY );
  }

}
