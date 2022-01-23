package org.toxsoft.tsgui.valed.controls.av;

import static org.toxsoft.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.valed.api.IValedControl;
import org.toxsoft.tsgui.valed.controls.basic.ValedIntegerText;
import org.toxsoft.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.impl.AvUtils;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link EAtomicType#INTEGER} editor using {@link ValedIntegerText}.
 *
 * @author hazard157
 */
public class ValedAvIntegerText
    extends AbstractAvWrapperValedControl<Integer> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvIntegerText"; //$NON-NLS-1$

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
      return new ValedAvIntegerText( aContext );
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
  public ValedAvIntegerText( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.INTEGER, ValedIntegerText.FACTORY );
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
