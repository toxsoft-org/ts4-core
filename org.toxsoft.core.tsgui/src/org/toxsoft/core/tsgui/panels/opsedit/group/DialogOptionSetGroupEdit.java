package org.toxsoft.core.tsgui.panels.opsedit.group;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * The dialog to edit sections group at once using {@link PanelOptionSetGroupEdit}.
 *
 * @author hazard157
 */
public class DialogOptionSetGroupEdit {

  static class DialogContent
      extends AbstractTsDialogPanel<IStridablesList<ISectionDef>, Object> {

    private final PanelOptionSetGroupEdit panel;

    protected DialogContent( Composite aParent, TsDialog<IStridablesList<ISectionDef>, Object> aOwnerDialog ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      panel = new PanelOptionSetGroupEdit( aParent, tsContext() );
      panel.setLayoutData( BorderLayout.CENTER );
    }

    @Override
    protected void doSetDataRecord( IStridablesList<ISectionDef> aData ) {
      panel.setSections( aData );
    }

    @Override
    protected IStridablesList<ISectionDef> doGetDataRecord() {
      return panel.getSections();
    }

    public void addSectionValuesChangeListener( ISectionValuesChangeListener aListener ) {
      panel.addSectionValuesChangeListener( aListener );
    }

    public void removeSectionValuesChangeListener( ISectionValuesChangeListener aListener ) {
      panel.removeSectionValuesChangeListener( aListener );
    }

  }

  /**
   * Invokes sections group editing dialog.
   * <p>
   * Dialog may be invoked either as common OK/Cancel editing dialog (with no flags) or with live editor with only Close
   * button (flag {@link ITsDialogConstants#DF_NO_APPROVE}). In both cases <code>aListener</code> is called on any
   * option change. In case when OK button was pressed method returns only changed values in the sections. Returned map
   * has section ID {@link ISectionDef#id()} as keys and {@link IOptionSet} containing <b>only</b> changed values. For
   * unchanges ections keys may be absent in the map.
   *
   * @param aDialogDef {@link ITsDialogInfo} - dialog window properties
   * @param aSections {@link IStridablesListEdit}&lt;{@link ISectionDef}&gt; - sections to edit
   * @param aListener {@link ISectionValuesChangeListener} - values change listener
   * @return {@link IStringMap}&lt;{@link IOptionSet}&gt; - changed values or <code>null</code>
   */
  // public static final IStringMap<IOptionSet> edit( ITsDialogInfo aDialogDef, IStridablesList<ISectionDef> aSections,
  // ISectionValuesChangeListener aListener ) {
  // TsNullArgumentRtException.checkNulls( aDialogDef, aSections, aListener );
  // IDialogPanelCreator<IStridablesList<ISectionDef>, Object> creator = ( aParent, aOwnerDialog ) -> {
  // DialogContent dc = new DialogContent( aParent, aOwnerDialog );
  // dc.addSectionValuesChangeListener( aListener );
  // return dc;
  // };
  // TsDialog<IStridablesList<ISectionDef>, Object> d = new TsDialog<>( aDialogDef, aSections, new Object(), creator );
  // d.execData();
  // }

}
