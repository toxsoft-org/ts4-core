package org.toxsoft.tsgui.valed.controls.av;

import static org.toxsoft.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.valed.api.IValedControl;
import org.toxsoft.tsgui.valed.controls.basic.ValedDoubleSpinner;
import org.toxsoft.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.impl.AvUtils;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link EAtomicType#FLOATING} editor wraps over {@link ValedDoubleSpinner}.
 *
 * @author hazard157
 */
public class ValedAvFloatSpinner
    extends AbstractAvWrapperValedControl<Double> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvFloatSpinner"; //$NON-NLS-1$

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
      return new ValedAvFloatSpinner( aContext );
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
  public ValedAvFloatSpinner( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.INTEGER, ValedDoubleSpinner.FACTORY );
  }

  @Override
  protected Double av2tv( IAtomicValue aAtomicValue ) {
    return Double.valueOf( aAtomicValue.asDouble() );
  }

  @Override
  protected IAtomicValue tv2av( Double aValue ) {
    return AvUtils.avFloat( aValue.doubleValue() );
  }

}
