package org.toxsoft.core.tsgui.panels.opsedit;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Utility methods to invoke {@link IOptionSet} editing panels as dialogs.
 *
 * @author hazard157
 */
public class DialogOptionsEdit {

  static class KitEditPanel
      extends AbstractTsDialogPanel<IStringMap<IOptionSet>, IStridablesList<IOpsetsKitItemDef>> {

    final IOpsetsKitPanel panel;

    protected KitEditPanel( Composite aParent, String aInitialSecionId,
        TsDialog<IStringMap<IOptionSet>, IStridablesList<IOpsetsKitItemDef>> aOwnerDialog ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      panel = new OpsetsKitPanel( tsContext() );
      panel.createControl( this );
      panel.getControl().setLayoutData( BorderLayout.CENTER );
      panel.setKitItemDefs( environ() );
      panel.setCurrentSelectedKitItemId( aInitialSecionId );
    }

    @Override
    protected void doSetDataRecord( IStringMap<IOptionSet> aData ) {
      if( aData != null ) {
        panel.setAllKitOptionValues( aData );
      }
      else {
        panel.setAllKitOptionValues( IStringMap.EMPTY );
      }
    }

    @Override
    protected IStringMap<IOptionSet> doGetDataRecord() {
      return panel.getAllKitOptionValues();
    }

  }

  /**
   * Invokes {@link IOpsetsKitPanel} in dialog window.
   *
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window properties
   * @param aSectionDefs {@link IStridablesListEdit}&lt;{@link IOpsetsKitItemDef}&gt; - list of item definitions
   * @param aInitialValues {@link IStringMap}&lt;{@link IOptionSet}&gt; - initial values may be <code>null</code>
   * @param aInitialSecionId String - initially selected kit item ID or <code>null</code> for first one
   * @return {@link IStringMap}&lt;{@link IOptionSet}&gt; - values or <code>null</code> on cancel
   */
  public static final IStringMap<IOptionSet> editKit( ITsDialogInfo aDialogInfo,
      IStridablesList<IOpsetsKitItemDef> aSectionDefs, IStringMap<IOptionSet> aInitialValues,
      String aInitialSecionId ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aSectionDefs );
    IDialogPanelCreator<IStringMap<IOptionSet>, IStridablesList<IOpsetsKitItemDef>> creator =
        ( aParent, aOwnerDialog ) -> new KitEditPanel( aParent, aInitialSecionId, aOwnerDialog );
    TsDialog<IStringMap<IOptionSet>, IStridablesList<IOpsetsKitItemDef>> d =
        new TsDialog<>( aDialogInfo, aInitialValues, aSectionDefs, creator );
    return d.execData();
  }

  // TODO other dialogs invoke methods

  /**
   * No sublcassing.
   */
  private DialogOptionsEdit() {
    // nop
  }

}
