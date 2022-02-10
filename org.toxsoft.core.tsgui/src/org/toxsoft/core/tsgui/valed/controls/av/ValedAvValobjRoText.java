package org.toxsoft.core.tsgui.valed.controls.av;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.eclipse.swt.widgets.Text;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.valed.api.IValedControl;
import org.toxsoft.core.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link EAtomicType#VALOBJ} editor - just view textual representation part with {@link Text} widget.
 *
 * @author hazard157
 */
public class ValedAvValobjRoText
    extends AbstractAvWrapperValedControl<Object> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjRoText"; //$NON-NLS-1$

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
      return new ValedAvValobjRoText( aContext );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  private IAtomicValue value = IAtomicValue.NULL;

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedAvValobjRoText( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.VALOBJ, ValedAvAnytypeText.FACTORY );
  }

  // ------------------------------------------------------------------------------------
  // AbstractAvWrapperValedControl
  //

  @Override
  protected Object av2tv( IAtomicValue aAtomicValue ) {
    value = aAtomicValue; // remember value - this method av2tv() is called from setValue()
    return aAtomicValue;
  }

  @Override
  protected IAtomicValue tv2av( Object aValue ) {
    // this is read-only VALED - always returns the value set by setValue() method
    return value;
  }

}
