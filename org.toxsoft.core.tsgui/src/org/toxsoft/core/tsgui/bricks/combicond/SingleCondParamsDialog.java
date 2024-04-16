package org.toxsoft.core.tsgui.bricks.combicond;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Invokes dialog to edit {@link ISingleCondParams}.
 *
 * @author hazard157
 */
public class SingleCondParamsDialog {

  private static class Panel
      extends AbstractTsDialogPanel<ISingleCondParams, ISingleCondTypesRegistry<ISingleCondType>> {

    private final SingleCondParamsPanel panel;

    Panel( Composite aParent, TsDialog<ISingleCondParams, ISingleCondTypesRegistry<ISingleCondType>> aOwnerDialog ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      panel = new SingleCondParamsPanel( tsContext(), false );
      panel.createControl( this );
      panel.getControl().setLayoutData( BorderLayout.CENTER );
      panel.genericChangeEventer().addListener( notificationGenericChangeListener );
      panel.addRegistry( environ() );
    }

    @Override
    protected void doSetDataRecord( ISingleCondParams aData ) {
      panel.setEntity( aData );
    }

    @Override
    protected ValidationResult validateData() {
      return panel.canGetEntity();
    }

    @Override
    protected ISingleCondParams doGetDataRecord() {
      return panel.getEntity();
    }

  }

  /**
   * Invokes {@link ISingleCondParams} edit dialog.
   *
   * @param aDialogInfo {@link ITsDialogInfo} - the dialog window parameters
   * @param aInitVal {@link ISingleCondParams} - initially displayed value, may be <code>null</code>
   * @param aTypesRegistry {@link ISingleCondTypesRegistry} - the registry to use
   * @return {@link ISingleCondParams} edit value or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public static ISingleCondParams edit( ITsDialogInfo aDialogInfo, ISingleCondParams aInitVal,
      ISingleCondTypesRegistry<? extends ISingleCondType> aTypesRegistry ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aTypesRegistry );
    TsDialog<ISingleCondParams, ISingleCondTypesRegistry<ISingleCondType>> d =
        new TsDialog<>( aDialogInfo, aInitVal, (ISingleCondTypesRegistry)aTypesRegistry, Panel::new );
    return d.execData();
  }

}
