package org.toxsoft.core.tsgui.ved.editor;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.m5.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * This is a workpiece of the VED editor objects tree.
 *
 * @author hazard157
 */
public class VedObjectsTree
    extends TsPanel
    implements ITsSelectionProvider<IVedItem> {

  /**
   * TODO
   * <p>
   * TODO toolbar with items: create VISEL, create actor, edit selected, remove selected, move up/down/first/last,
   * hide/show filter pane
   * <p>
   * TODO filter pane: filter by searching in IDs and nmNames
   * <p>
   * TODO do NOT show tree/list switch, always in tree mode VISELS and ACTORs nodes
   * <p>
   * TODO maybe additional tree mode with VISELS/viselXxx/actors_bind_to_visel. This will need actor API enhancement
   * with IStringList boundViselsIds()
   * <p>
   * TODO reverse tree actorXxx/bound_VISELs
   */

  private final TsSelectionChangeEventHelper<IVedItem> selectionHelper;

  private final IVedScreen                   vedScreen;
  private final IM5CollectionPanel<IVedItem> panel;

  /**
   * Constructor.
   *
   * @param aParent {@link Composite} - parent component
   * @param aVedScreen {@link IVedScreen} - the VED screen to display it's model's content
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedObjectsTree( Composite aParent, IVedScreen aVedScreen, ITsGuiContext aContext ) {
    super( aParent, aContext );
    selectionHelper = new TsSelectionChangeEventHelper<>( this );
    this.setLayout( new BorderLayout() );
    TsNullArgumentRtException.checkNull( aVedScreen );
    vedScreen = aVedScreen;
    IM5Model<IVedItem> model = m5().getModel( VedItemM5Model.MODEL_ID, IVedItem.class );
    IM5LifecycleManager<IVedItem> lm = model.getLifecycleManager( vedScreen );
    panel = model.panelCreator().createCollEditPanel( tsContext(), lm.itemsProvider(), lm );
    panel.createControl( this );
    panel.getControl().setLayoutData( BorderLayout.CENTER );
    panel.addTsSelectionListener( selectionHelper );
    vedScreen.model().visels().eventer().addListener( ( src, op, id ) -> panel.refresh() );
    vedScreen.model().actors().eventer().addListener( ( src, op, id ) -> panel.refresh() );
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<IVedItem> aListener ) {
    selectionHelper.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<IVedItem> aListener ) {
    selectionHelper.removeTsSelectionListener( aListener );
  }

  @Override
  public IVedItem selectedItem() {
    return panel.selectedItem();
  }

  @Override
  public void setSelectedItem( IVedItem aItem ) {
    panel.setSelectedItem( aItem );
  }

}
