package org.toxsoft.core.tsgui.valed.impl;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.impl.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Wraps VALED as a text-and-button single line control.
 * <p>
 * Note: created text is always read-only. Only way to edit value is to invoke dialog with the right button.
 *
 * @author hazard157
 * @param <V> - the edited value type
 */
public class ValedControlSingleLineWrapper<V>
    extends AbstractValedTextAndButton<V> {

  private final ITsVisualsProvider<V> visualsProvider;
  private final IValedControlFactory  vcFactory;

  private V value = null;

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - the VALED context
   * @param aVcFactory {@link IValedControlFactory} - factory of VALED to be used in dialog
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected ValedControlSingleLineWrapper( ITsGuiContext aContext, IValedControlFactory aVcFactory ) {
    super( aContext, true );
    TsNullArgumentRtException.checkNull( aVcFactory );
    vcFactory = aVcFactory;
    visualsProvider = REFDEF_VALUE_VISUALS_PROVIDER.getRef( tsContext().eclipseContext(), ITsVisualsProvider.DEFAULT );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void updateWidgetFromValue() {
    getTextControl().setText( visualsProvider.getName( value ) );
    getTextControl().setToolTipText( visualsProvider.getDescription( value ) );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedTextAndButton
  //

  @Override
  protected boolean doProcessButtonPress() {
    ITsDialogInfo di = new TsDialogInfo( tsContext(), getShell(), DLG_VCSLW_DIALOG, DLG_VCSLW_DIALOG_D, 0 );
    Pair<V, Boolean> p = ValedControlUtils.invokeAsModalDialog( value, vcFactory, di );
    if( p.right().booleanValue() ) {
      value = p.left();
      updateWidgetFromValue();
      return true;
    }
    return false;
  }

  @Override
  protected void doDoSetUnvalidatedValue( V aValue ) {
    value = aValue;
    updateWidgetFromValue();
  }

  @Override
  protected V doGetUnvalidatedValue() {
    return value;
  }

}
