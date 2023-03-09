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
 * Edit {@link EAtomicType#VALOBJ} values containing {@link LocalDateTime}.
 * <p>
 * Wraps over {@link ValedLocalDateTimeMpv}.
 *
 * @author hazard157
 */
public class ValedAvValobjLocalDateTimeMpv
    extends AbstractAvValobjWrapperValedControl<LocalDateTime> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvLocalDateTimeMpv"; //$NON-NLS-1$

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
      return new ValedAvValobjLocalDateTimeMpv( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      if( aAtomicType == EAtomicType.VALOBJ && aKeeperId != null ) {
        return aKeeperId.equals( LocalDateTimeKeeper.KEEPER_ID );
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
  public ValedAvValobjLocalDateTimeMpv( ITsGuiContext aTsContext ) {
    super( aTsContext, ValedLocalDateTimeMpv.FACTORY );
  }

}
