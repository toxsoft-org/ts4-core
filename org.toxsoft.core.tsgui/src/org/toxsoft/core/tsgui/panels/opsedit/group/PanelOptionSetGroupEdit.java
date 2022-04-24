package org.toxsoft.core.tsgui.panels.opsedit.group;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.panels.opsedit.set.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The panel to edit sections group at once.
 * <p>
 * Panel consists of:
 * <ul>
 * <li>list of sections at the left side;</li>
 * <li>options values editors at right side - displayes known options of the selected item in the sections list.</li>
 * </ul>
 *
 * @author hazard157
 */
public class PanelOptionSetGroupEdit
    extends TsPanel {

  private final IStridablesListEdit<ISectionDef> sections = new StridablesList<>();

  private IM5CollectionPanel<ISectionDef> sectsList;
  private IPanelOptionSetEdit             valsEdit;

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelOptionSetGroupEdit( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    SashForm sfMain = new SashForm( aParent, SWT.HORIZONTAL );
    sfMain.setLayoutData( BorderLayout.CENTER );

    // sectsList
    IM5Model<ISectionDef> model = m5().getModel( SectionDefM5Model.MODEL_ID, ISectionDef.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    sectsList = model.panelCreator().createCollViewerPanel( ctx, null );
    sectsList.createControl( sfMain );
    sectsList.addTsSelectionListener( ( src, sel ) -> whenSectionSelected( sel ) );

    // valsEdit
    ctx = new TsGuiContext( tsContext() );
    valsEdit = new PanelOptionSetEdit( ctx );
    valsEdit.createControl( sfMain );

    // TODO PanelOptionSetGroupEdit.PanelOptionSetGroupEdit()

    sfMain.setWeights( 3500, 6500 );

  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void whenSectionSelected( ISectionDef aSel ) {
    // TODO PanelOptionSetGroupEdit.whenSectionSelected()
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public IStridablesList<ISectionDef> getSections() {
    // TODO реализовать PanelOptionSetGroupEdit.getSections()
    throw new TsUnderDevelopmentRtException( "PanelOptionSetGroupEdit.getSections()" );
  }

  public void setSections( IStridablesList<ISectionDef> aSections ) {
    // TODO PanelOptionSetGroupEdit.setSections()
  }

  public void addSectionValuesChangeListener( ISectionValuesChangeListener aListener ) {
    // TODO PanelOptionSetGroupEdit.addSectionValuesChangeListener()
  }

  public void removeSectionValuesChangeListener( ISectionValuesChangeListener aListener ) {
    // TODO PanelOptionSetGroupEdit.addSectionValuesChangeListener()
  }

  public IStringMap<IOptionSet> listChangedValues() {
    // TODO реализовать PanelOptionSetGroupEdit.listChangedValues()
    throw new TsUnderDevelopmentRtException( "PanelOptionSetGroupEdit.listChangedValues()" );
  }

  public void resetChangedValues() {
    // TODO PanelOptionSetGroupEdit.resetChangedValues()
  }

}
