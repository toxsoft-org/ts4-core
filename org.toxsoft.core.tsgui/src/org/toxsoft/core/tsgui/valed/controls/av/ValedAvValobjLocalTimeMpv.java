package org.toxsoft.core.tsgui.valed.controls.av;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import java.time.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.time.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Edit {@link EAtomicType#VALOBJ} values containing {@link LocalTime}.
 * <p>
 * Wraps over {@link ValedLocalTimeMpv}.
 *
 * @author hazard157
 */
public class ValedAvValobjLocalTimeMpv
    extends AbstractAvValobjWrapperValedControl<LocalTime> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvLocalTimeMpv"; //$NON-NLS-1$

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
      return new ValedAvValobjLocalTimeMpv( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      if( aAtomicType == EAtomicType.VALOBJ && aKeeperId != null ) {
        return aKeeperId.equals( LocalTimeKeeper.KEEPER_ID );
      }
      return false;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedAvValobjLocalTimeMpv( ITsGuiContext aTsContext ) {
    super( aTsContext, ValedLocalTimeMpv.FACTORY );
  }

}
