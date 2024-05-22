package org.toxsoft.core.tsgui.bricks.cond.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.math.cond.*;

/**
 * {@link EAtomicType#VALOBJ} of type {@link ITsCombiCondInfo} editor.
 * <p>
 * Wraps over {@link ValedCombiCondInfo}.
 *
 * @author hazard157
 */
public class ValedAvValobjCombiCondInfo
    extends AbstractAvValobjWrapperValedControl<ITsCombiCondInfo> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjCombiCondInfo"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvValobjCombiCondInfo( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      if( aAtomicType == EAtomicType.VALOBJ && aKeeperId != null ) {
        return aKeeperId.equals( D2Angle.KEEPER_ID );
      }
      return false;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  protected ValedAvValobjCombiCondInfo( ITsGuiContext aTsContext ) {
    super( aTsContext, ValedCombiCondInfo.FACTORY );
  }

}
