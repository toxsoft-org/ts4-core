package org.toxsoft.core.tsgui.panels.opsedit.impl;

import static org.toxsoft.core.tsgui.panels.opsedit.impl.ITsResources.*;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tsgui.bricks.tstree.impl.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.mws.services.hdpi.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IOpsetsKitPanel} implementation.
 *
 * @author hazard157
 */
public class OpsetsKitPanel
    extends AbstractLazyPanel<Control>
    implements IOpsetsKitPanel {

  private static final ITsNodeKind<IOpsetsKitItemDef> NK_ITEM = new TsNodeKind<>( "Item", //$NON-NLS-1$
      IOpsetsKitItemDef.class, false );

  /**
   * Class to create visuals provider for nodes with {@link IOpsetsKitItemDef} entities.
   *
   * @author hazard157
   */
  public class KitItemNodeVisualsProvider
      implements ITsVisualsProvider<ITsNode> {

    @Override
    public String getName( ITsNode aNode ) {
      if( aNode.kind() == NK_ITEM ) {
        return ((IOpsetsKitItemDef)aNode.entity()).nmName();
      }
      throw new TsInternalErrorRtException();
    }

    @Override
    public String getDescription( ITsNode aNode ) {
      if( aNode.kind() == NK_ITEM ) {
        return StridUtils.printf( StridUtils.FORMAT_DESCRIPTION_ID, (IOpsetsKitItemDef)aNode.entity() );
      }
      throw new TsInternalErrorRtException();
    }

    @Override
    public Image getIcon( ITsNode aNode, EIconSize aIconSize ) {
      if( aNode.kind() == NK_ITEM ) {
        String iconId = ((IOpsetsKitItemDef)aNode.entity()).iconId();
        if( iconId != null ) {
          return iconManager().loadStdIcon( iconId, aIconSize );
        }
      }
      return null;
    }

  }

  private final ITsSelectionChangeListener<ITsNode> itemSelectionChangeListener = new ITsSelectionChangeListener<>() {

    @Override
    public void onTsSelectionChanged( Object aSource, ITsNode aSelectedItem ) {
      String kid = null;
      if( aSelectedItem != null && aSelectedItem.kind() == NK_ITEM ) {
        kid = ((IOpsetsKitItemDef)aSelectedItem.entity()).id();
        IOpsetsKitItemDef kitItem = kitItemDefs.findByKey( kid );
        updateRightPaneForKitItem( kitItem );
      }
      else {
        updateRightPaneForKitItem( null );
      }
      eventer.fireKitItemSelectionEvent( kid );
    }
  };

  /**
   * React on user change value in VALED:<br>
   * 1. updates value in {@link #kitValues};<br>
   * 2. fire apprpriate event.
   */
  private final IOptionValueChangeListener valedValueChangeListener = new IOptionValueChangeListener() {

    @Override
    public void onOptionValueChange( Object aSource, String aOptionId, IAtomicValue aNewValue ) {
      IOpsetsKitItemDef selItemDef = selectedItemDef();
      TsInternalErrorRtException.checkNull( selItemDef );
      String kid = selItemDef.id();
      IOptionSetEdit selItemValues = kitValues.getByKey( kid );
      selItemValues.setValue( aOptionId, aNewValue );
      eventer.fireOptionValueChangeEvent( kid, aOptionId, aNewValue );
    }
  };

  private final OpsetsKitChangeEventer eventer;

  /**
   * Kit item definitions set in {@link #setKitItemDefs(IStridablesList)}.
   */
  private final IStridablesListEdit<IOpsetsKitItemDef> kitItemDefs = new StridablesList<>();

  /**
   * Kit items values are always in sync with valid values in editors.
   */
  private final IStringMapEdit<IOptionSetEdit> kitValues = new StringMap<>();

  /**
   * Backplane component on which {@link #itemsTree} and {@link #valedsPanel} are placed.
   */
  private SashForm backplane = null;

  /**
   * Left items table/tree.
   */
  private final ITsTreeViewer itemsTree;

  /**
   * Right value editors panel.
   */
  private final IOptionSetPanel valedsPanel;

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException аргумент = null
   */
  public OpsetsKitPanel( ITsGuiContext aContext ) {
    super( aContext );
    eventer = new OpsetsKitChangeEventer( this );
    // itemsTree
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    itemsTree = new TsTreeViewer( ctx );
    itemsTree.setIconSize( hdpiService().getIconsSize( ITsHdpiServiceConstants.ICON_CATEG_PREFS_KIT_ITEM ) );
    itemsTree.addTsSelectionListener( itemSelectionChangeListener );
    // valedsPanel
    ctx = new TsGuiContext( tsContext() );
    valedsPanel = new OptionSetPanel( ctx, false );
    valedsPanel.optionChangeEventer().addListener( valedValueChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private IOpsetsKitItemDef selectedItemDef() {
    ITsNode selNode = itemsTree.selectedItem();
    if( selNode != null && selNode.kind() == NK_ITEM ) {
      return IOpsetsKitItemDef.class.cast( selNode.entity() );
    }
    return null;
  }

  /**
   * Clears and resets {@link #kitValues} to defaults from {@link #kitItemDefs}.
   */
  private void resetOptionValuesToKitItemDefaults() {
    kitValues.clear();
    for( String itemId : kitItemDefs.keys() ) {
      IOpsetsKitItemDef itemDef = kitItemDefs.getByKey( itemId );
      IStridablesList<IDataDef> ddefs = itemDef.optionDefs();
      IOptionSetEdit ops = new OptionSet();
      for( String opId : ddefs.keys() ) {
        IAtomicValue av = ddefs.getByKey( opId ).defaultValue();
        ops.setValue( opId, av );
      }
      kitValues.put( itemId, ops );
    }
  }

  /**
   * Creates root nodes to be displayed in {@link #itemsTree}.
   *
   * @return {@link IList}&lt;{@link ITsNode}&gt; - nodes under parent node {@link #itemsTree}
   */
  private IList<ITsNode> createRootNodesForKitItems() {
    IListEdit<ITsNode> rootNodes = new ElemArrayList<>();
    for( IOpsetsKitItemDef kitItem : kitItemDefs ) {
      DefaultTsNode<IOpsetsKitItemDef> node = new DefaultTsNode<>( NK_ITEM, itemsTree, kitItem );
      rootNodes.add( node );
    }
    return rootNodes;
  }

  /**
   * Changes {@link #valedsPanel} content to accordin to the specified kit item.
   *
   * @param aKitItemDef {@link IOpsetsKitItemDef} - kit item or <code>null</code>
   */
  void updateRightPaneForKitItem( IOpsetsKitItemDef aKitItemDef ) {
    if( aKitItemDef == null ) {
      valedsPanel.setOptionDefs( IStridablesList.EMPTY );
      return;
    }
    valedsPanel.setOptionDefs( aKitItemDef.optionDefs() );
    valedsPanel.setEntity( kitValues.getByKey( aKitItemDef.id() ) );
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    backplane = new SashForm( aParent, SWT.HORIZONTAL );
    itemsTree.createControl( backplane );
    itemsTree.addColumn( STR_N_KIT_ITEM_COLUMN_HEADER, EHorAlignment.CENTER, new KitItemNodeVisualsProvider() );
    valedsPanel.createControl( backplane );
    backplane.setWeights( 3500, 6500 );
    setControl( backplane );
    if( !kitItemDefs.isEmpty() ) {
      setCurrentSelectedKitItemId( kitItemDefs.first().id() );
    }
    return backplane;
  }

  // ------------------------------------------------------------------------------------
  // IOpsetsKitPanel
  //

  @Override
  public void setKitItemDefs( IStridablesList<IOpsetsKitItemDef> aItemDefs ) {
    kitItemDefs.setAll( aItemDefs );
    resetOptionValuesToKitItemDefaults();
    // if no widget - return
    if( getControl() == null ) {
      return;
    }
    // clear right panel
    updateRightPaneForKitItem( null );
    // init left pane content
    IList<ITsNode> rootNodes = createRootNodesForKitItems();
    itemsTree.setRootNodes( rootNodes );
  }

  @Override
  public IStridablesListEdit<IOpsetsKitItemDef> listKitItemDefs() {
    return kitItemDefs;
  }

  @Override
  public String currentSelectedKitItemId() {
    IOpsetsKitItemDef sel = selectedItemDef();
    return (sel != null) ? sel.id() : null;
  }

  @Override
  public void setCurrentSelectedKitItemId( String aKitItemId ) {
    if( aKitItemId != null ) {
      IOpsetsKitItemDef itemDef = kitItemDefs.findByKey( aKitItemId );
      if( itemDef != null ) {
        ITsNode toSel = itemsTree.findByEntity( itemDef, true );
        itemsTree.setSelectedItem( toSel );
      }
    }
    else {
      itemsTree.setSelectedItem( null );
    }
  }

  @Override
  public void setKitOptionValues( String aKitItemId, IOptionSet aValues ) {
    TsNullArgumentRtException.checkNulls( aKitItemId, aValues );
    IOpsetsKitItemDef itemDef = kitItemDefs.findByKey( aKitItemId );
    if( itemDef == null ) {
      return;
    }
    // prepare item values: argument value and missed option default values
    IOptionSetEdit ops = new OptionSet( aValues );
    for( IDataDef dd : itemDef.optionDefs() ) {
      if( !ops.hasKey( dd.id() ) ) {
        ops.setValue( dd.id(), dd.defaultValue() );
      }
    }
    // update kitValues with ALL options from argument
    kitValues.put( aKitItemId, ops );
    // if item is selected then copy values to widgets
    if( Objects.equals( aKitItemId, currentSelectedKitItemId() ) ) {
      valedsPanel.setEntity( ops );
    }
  }

  @Override
  public IOptionSet getKitOptionValues( String aKitItemId ) {
    return kitValues.getByKey( aKitItemId );
  }

  @Override
  public void setAllKitOptionValues( IStringMap<IOptionSet> aAllValues ) {
    TsNullArgumentRtException.checkNulls( aAllValues );
    for( String kid : aAllValues.keys() ) {
      if( kitItemDefs.hasKey( kid ) ) {
        setKitOptionValues( kid, aAllValues.getByKey( kid ) );
      }
    }
  }

  @Override
  public IStringMap<IOptionSet> getAllKitOptionValues() {
    IStringMapEdit<IOptionSet> result = new StringMap<>();
    for( String kid : kitValues.keys() ) {
      IOptionSet opset = new OptionSet( kitValues.getByKey( kid ) );
      result.put( kid, opset );
    }
    return result;
  }

  @Override
  public ITsEventer<IOpsetsKitChangeListener> eventer() {
    return eventer;
  }

}
