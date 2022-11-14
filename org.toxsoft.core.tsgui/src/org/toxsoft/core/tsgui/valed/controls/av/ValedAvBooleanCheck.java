package org.toxsoft.core.tsgui.valed.controls.av;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link EAtomicType#BOOLEAN} editor using {@link ValedBooleanCheck}.
 *
 * @author hazard157
 */
public class ValedAvBooleanCheck
    extends AbstractAvWrapperValedControl<Boolean> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvBooleanCheck"; //$NON-NLS-1$

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
      return new ValedAvBooleanCheck( aContext );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedAvBooleanCheck( ITsGuiContext aContext ) {
    super( aContext, EAtomicType.BOOLEAN, ValedBooleanCheck.FACTORY );
  }

  @Override
  protected Boolean av2tv( IAtomicValue aAtomicValue ) {
    return Boolean.valueOf( aAtomicValue.asBool() );
  }

  @Override
  protected IAtomicValue tv2av( Boolean aValue ) {
    return AvUtils.avBool( aValue.booleanValue() );
  }

}
