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
 * {@link EAtomicType#INTEGER} editor using {@link ValedIntegerSpinner}.
 *
 * @author hazard157
 */
public class ValedAvIntegerSpinner
    extends AbstractAvWrapperValedControl<Integer> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvIntegerSpinner"; //$NON-NLS-1$

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
      return new ValedAvIntegerSpinner( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      return aAtomicType == EAtomicType.INTEGER;
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
  public ValedAvIntegerSpinner( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.INTEGER, ValedIntegerSpinner.FACTORY );
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
