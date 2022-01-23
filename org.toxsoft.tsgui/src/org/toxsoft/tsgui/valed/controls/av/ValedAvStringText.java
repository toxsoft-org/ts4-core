package org.toxsoft.tsgui.valed.controls.av;

import static org.toxsoft.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.valed.api.IValedControl;
import org.toxsoft.tsgui.valed.controls.basic.ValedStringText;
import org.toxsoft.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.impl.AvUtils;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link EAtomicType#STRING} editor using {@link ValedStringText}.
 *
 * @author hazard157
 */
public class ValedAvStringText
    extends AbstractAvWrapperValedControl<String> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvStringText"; //$NON-NLS-1$

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
      return new ValedAvStringText( aContext );
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
  public ValedAvStringText( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.STRING, ValedStringText.FACTORY );
  }

  @Override
  protected String av2tv( IAtomicValue aAtomicValue ) {
    return aAtomicValue.asString();
  }

  @Override
  protected IAtomicValue tv2av( String aValue ) {
    return AvUtils.avStr( aValue );
  }

}
