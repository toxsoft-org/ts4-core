package org.toxsoft.core.tsgui.valed.controls.basic;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.basic.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link Boolean} value editor as check box.
 *
 * @author hazard157
 */
public class ValedBooleanCheck
    extends AbstractValedControl<Boolean, Button> {

  /**
   * ID of context reference {@link #OPDEF_TEXT}.
   */
  public static final String OPID_TEXT = VALED_OPID_PREFIX + ".CheckboxText"; //$NON-NLS-1$

  /**
   * The text to be shown on widget.<br>
   */
  public static final IDataDef OPDEF_TEXT = DataDef.create( OPID_TEXT, STRING, //
      TSID_NAME, STR_N_CHECKBOX_TEXT, //
      TSID_DESCRIPTION, STR_D_CHECKBOX_TEXT, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

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
      return new ValedBooleanCheck( aContext );
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
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Button doCreateControl( Composite aParent ) {
    checkBox = new Button( aParent, SWT.CHECK );
    checkBox.addSelectionListener( notificationSelectionListener );
    checkBox.setText( OPDEF_TEXT.getValue( params() ).asString() );
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
