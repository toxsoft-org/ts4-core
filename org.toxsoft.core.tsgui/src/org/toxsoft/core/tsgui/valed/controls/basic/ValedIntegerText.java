package org.toxsoft.core.tsgui.valed.controls.basic;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link Integer} editor with support of an empty value.
 *
 * @author hazard157
 */
public class ValedIntegerText
    extends AbstractValedControl<Integer, Text> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_OPID_PREFIX + ".ValedAvIntText"; //$NON-NLS-1$

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
    protected IValedControl<Integer> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedIntegerText( aContext );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * введенное пользователм значение
   */
  Integer value = null;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException context does not contains mandatory information
   * @throws TsIllegalArgumentRtException <code>enum</code> does not contains any constant
   */
  public ValedIntegerText( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
  }

  private void displayValue() {
    if( value != null ) {
      getControl().setText( value.toString() );
    }
    else {
      getControl().setText( TsLibUtils.EMPTY_STRING );
    }
  }

  @Override
  protected Text doCreateControl( Composite aParent ) {
    Text text = new Text( aParent, SWT.BORDER );
    setControl( text );
    getControl().addVerifyListener( aEvent -> {
      // process non-empty input
      if( aEvent.text.trim().length() != 0 ) {
        // get old text and create new text by using the VerifyEvent.text
        final String oldS = getControl().getText();
        String newS = oldS.substring( 0, aEvent.start ) + aEvent.text + oldS.substring( aEvent.end );
        boolean isInteger = true;
        try {
          Integer.parseInt( newS );
        }
        catch( @SuppressWarnings( "unused" ) NumberFormatException ex ) {
          isInteger = false;
        }

        if( !isInteger ) {
          aEvent.doit = false;
        }
      }
      else {
        aEvent.doit = true;
      }
    } );
    getControl().setToolTipText( getTooltipText() );
    getControl().addModifyListener( aE -> {
      if( getControl().getText().trim().length() == 0 ) {
        value = null;
      }
      else {
        value = Integer.valueOf( getControl().getText() );
      }
      fireModifyEvent( true );
    } );

    return text;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    if( isWidget() ) {
      getControl().setEditable( isEditable() );
    }
  }

  @Override
  protected void doClearValue() {
    value = null;
    displayValue();
  }

  @Override
  protected void doSetUnvalidatedValue( Integer aValue ) {
    value = aValue;
    displayValue();
  }

  @Override
  protected Integer doGetUnvalidatedValue() {
    return value;
  }

}
