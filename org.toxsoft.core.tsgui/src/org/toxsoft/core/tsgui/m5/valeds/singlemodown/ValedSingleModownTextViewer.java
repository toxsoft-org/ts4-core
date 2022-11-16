package org.toxsoft.core.tsgui.m5.valeds.singlemodown;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.valeds.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link M5SingleModownFieldDef} field viewer as single-line text.
 *
 * @author hazard157
 * @param <V> - edited value type (also field value type)
 */
public class ValedSingleModownTextViewer<V>
    extends AbstractValedSingleModownEditor<V> {

  private Text text  = null;
  private V    value = null;

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - editor context (used as VALED creation argument)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF} not found in the context
   * @throws ClassCastException found reference is not of expected type {@link M5SingleModownFieldDef}
   */
  public ValedSingleModownTextViewer( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedSingleModownEditor
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    text = new Text( aParent, SWT.BORDER );
    text.setEditable( false );
    return text;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // nop
  }

  @Override
  public ValidationResult canGetValue() {
    return ValidationResult.SUCCESS;
  }

  @Override
  protected V doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( V aValue ) {
    value = aValue;
    if( value != null ) {
      text.setText( fieldDef().itemModel().visualsProvider().getName( aValue ) );
    }
    else {
      text.setText( TsLibUtils.EMPTY_STRING );
    }
  }

  @Override
  protected void doClearValue() {
    value = null;
    text.setText( TsLibUtils.EMPTY_STRING );
  }

}
