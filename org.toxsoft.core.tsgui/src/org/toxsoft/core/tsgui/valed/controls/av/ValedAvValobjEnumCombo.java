package org.toxsoft.core.tsgui.valed.controls.av;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.valed.api.IValedControl;
import org.toxsoft.core.tsgui.valed.controls.enums.ValedEnumCombo;
import org.toxsoft.core.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link EAtomicType#VALOBJ} Java <code>enum</code> editor using {@link ValedEnumCombo}.
 *
 * @author hazard157
 * @param <V> - enum class
 */
public class ValedAvValobjEnumCombo<V extends Enum<V>>
    extends AbstractAvWrapperValedControl<V> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjEnumCombo"; //$NON-NLS-1$

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
      return new ValedAvValobjEnumCombo<>( aContext );
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
  public ValedAvValobjEnumCombo( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.VALOBJ, ValedEnumCombo.FACTORY );

  }

  @Override
  protected IAtomicValue tv2av( V aTypedValue ) {
    return avValobj( aTypedValue );
  }

  @Override
  protected V av2tv( IAtomicValue aAtomicValue ) {
    return aAtomicValue.asValobj();
  }

}
