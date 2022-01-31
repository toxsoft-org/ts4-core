package org.toxsoft.core.tsgui.valed.controls.av;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.valed.api.IValedControl;
import org.toxsoft.core.tsgui.valed.controls.time.ValedSecsDurationMpv;
import org.toxsoft.core.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link EAtomicType#INTEGER} editor wraps over {@link ValedSecsDurationMpv}.
 *
 * @author hazard157
 */
public class ValedAvIntegerSecsDurationMpv
    extends AbstractAvWrapperValedControl<Integer> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvIntegerSecsDurationMpv"; //$NON-NLS-1$

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
      return new ValedAvIntegerSecsDurationMpv( aContext );
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
  public ValedAvIntegerSecsDurationMpv( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.INTEGER, ValedSecsDurationMpv.FACTORY );
  }

  @Override
  protected Integer av2tv( IAtomicValue aAtomicValue ) {
    return Integer.valueOf( aAtomicValue.asInt() );
  }

  @Override
  protected IAtomicValue tv2av( Integer aValue ) {
    return AvUtils.avInt( aValue.intValue() );
  }

}
