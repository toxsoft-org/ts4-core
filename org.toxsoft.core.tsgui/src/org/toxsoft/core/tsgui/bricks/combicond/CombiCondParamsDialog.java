package org.toxsoft.core.tsgui.bricks.combicond;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Invokes dialog with {@link CombiCondParamsPanel} to edit {@link ICombiCondParams}.
 *
 * @author hazard157
 */
public class CombiCondParamsDialog {

  /**
   * {@link AbstractTsDialogPanel} implementation wrapper over {@link CombiCondParamsPanel}.
   *
   * @author hazard157
   */
  private static class Panel
      extends AbstractTsDialogPanel<ICombiCondParams, ISingleCondTypesRegistry<ISingleCondType>> {

    private final CombiCondParamsPanel ccpp;

    Panel( Composite aParent, TsDialog<ICombiCondParams, ISingleCondTypesRegistry<ISingleCondType>> aOwnerDialog ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      ccpp = new CombiCondParamsPanel( tsContext(), false );
      ccpp.addSctRegistry( environ() );
      ccpp.createControl( this );
      ccpp.getControl().setLayoutData( BorderLayout.CENTER );
      ccpp.genericChangeEventer().addListener( notificationGenericChangeListener );
    }

    @Override
    protected void doSetDataRecord( ICombiCondParams aData ) {
      ccpp.setEntity( aData );
    }

    @Override
    protected ValidationResult validateData() {
      return ccpp.canGetEntity();
    }

    @Override
    protected ICombiCondParams doGetDataRecord() {
      return ccpp.getEntity();
    }

  }

  /**
   * No instances.
   */
  private CombiCondParamsDialog() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Dialog invocation
  //

  /**
   * Invokes {@link ICombiCondParams} edit dialog.
   *
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window parameters
   * @param aInitVal {@link ICombiCondParams} - initial value to display or <code>null</code>
   * @param aSctRegistry {@link ISingleCondTypesRegistry} - single types registry to use
   * @return {@link ICombiCondParams} - edited CCP or <code>null</code> if user cancels editing
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public static ICombiCondParams edit( ITsDialogInfo aDialogInfo, ICombiCondParams aInitVal,
      ISingleCondTypesRegistry<? extends ISingleCondType> aSctRegistry ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aSctRegistry );
    TsDialog<ICombiCondParams, ISingleCondTypesRegistry<ISingleCondType>> d =
        new TsDialog<>( aDialogInfo, aInitVal, (ISingleCondTypesRegistry)aSctRegistry, Panel::new );
    return d.execData();
  }

}
