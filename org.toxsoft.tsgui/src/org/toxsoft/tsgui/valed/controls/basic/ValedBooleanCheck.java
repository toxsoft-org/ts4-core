package org.toxsoft.tsgui.valed.controls.basic;

import static org.toxsoft.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.valed.api.IValedControl;
import org.toxsoft.tsgui.valed.impl.AbstractValedControl;
import org.toxsoft.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link Boolean} value editor as check box.
 *
 * @author hazard157
 */
public class ValedBooleanCheck
    extends AbstractValedControl<Boolean, Button> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".BooleanCheck"; //$NON-NLS-1$

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
    protected IValedControl<Boolean> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<Boolean, ?> e = new ValedBooleanCheck( aContext );
      e.setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_TRUE );
      e.setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
      return e;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  private Button checkBox = null;

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - th editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException <code>enum</code> does not contains any constant
   */
  public ValedBooleanCheck( ITsGuiContext aTsContext ) {
    super( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Button doCreateControl( Composite aParent ) {
    checkBox = new Button( aParent, SWT.CHECK );
    checkBox.addSelectionListener( notificationSelectionListener );
    checkBox.setEnabled( isEditable() );
    return checkBox;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    if( isWidget() ) {
      getControl().setEnabled( aEditable );
    }
  }

  @Override
  protected Boolean doGetUnvalidatedValue() {
    return Boolean.valueOf( checkBox.getSelection() );
  }

  @Override
  protected void doSetUnvalidatedValue( Boolean aValue ) {
    checkBox.setSelection( aValue.booleanValue() );
  }

  @Override
  protected void doClearValue() {
    checkBox.setSelection( false );
  }

}
