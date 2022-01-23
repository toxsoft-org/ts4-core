package org.toxsoft.tsgui.valed.controls.av;

import static org.toxsoft.tsgui.valed.api.IValedControlConstants.*;

import java.time.LocalTime;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.valed.api.IValedControl;
import org.toxsoft.tsgui.valed.controls.time.ValedLocalTimeMpv;
import org.toxsoft.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

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
