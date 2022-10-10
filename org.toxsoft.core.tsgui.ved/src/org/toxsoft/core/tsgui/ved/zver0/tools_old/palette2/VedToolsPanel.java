package org.toxsoft.core.tsgui.ved.zver0.tools_old.palette2;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.ved.zver0.tools_old.tools2.*;
import org.toxsoft.core.tsgui.ved.zver1.core.view.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель инструментов редактора.
 * <p>
 *
 * @author vs
 */
public class VedToolsPanel
    extends TsPanel
    implements IVedToolsPanel {

  class ToolAction
      extends Action {

    private final IVedEditorTool tool;

    ToolAction( IVedEditorTool aTool ) {
      super( aTool.id(), IAction.AS_RADIO_BUTTON );
      tool = aTool;
      setText( tool.nmName() );
      setToolTipText( tool.description() );
      ImageDescriptor imd = tsContext().get( ITsIconManager.class ).loadFreeDescriptor( tool.iconId() );
      setImageDescriptor( imd );
    }

    @Override
    public void run() {
      PanelItem item = null;
      if( activeTool != null ) {
        item = findPanelItem( activeTool.id() );
        item.setSelected( false );
      }
      setChecked( true );
      item = findPanelItem( tool.id() );
      item.setSelected( true );
    }
  }

  class GroupAction
      extends Action {

    private final IVedToolsGroup group;

    private Control control = null;

    private Color bkColor = null;

    GroupAction( IVedToolsGroup aGroup ) {
      super( aGroup.id(), IAction.AS_DROP_DOWN_MENU | SWT.CHECK );
      group = aGroup;

      IVedEditorTool tool = aGroup.selectedTool();
      setText( tool.nmName() );
      setToolTipText( tool.description() );
      ImageDescriptor imd = tsContext().get( ITsIconManager.class ).loadFreeDescriptor( tool.iconId() );
      setImageDescriptor( imd );

      setMenuCreator( new AbstractMenuCreator( true ) {

        @Override
        protected boolean fillMenu( Menu aMenu ) {
          initMenuFromGroup( aMenu, GroupAction.this );
          return true;
        }
      } );
    }

    @Override
    public void run() {
      PanelItem item;
      if( activeTool != null ) {
        item = findPanelItem( activeTool.id() );
        item.setSelected( false );
      }

      item = findPanelItem( group.selectedTool().id() );
      item.setSelected( true );
      // selected = true;
      // clearSelection();
      // control.setBackground( Display.getCurrent().getSystemColor( SWT.COLOR_TITLE_BACKGROUND_GRADIENT ) );
    }

    @Override
    public void setChecked( boolean aChecked ) {
      boolean selected = aChecked;
      Color c = bkColor;
      if( aChecked ) {
        c = Display.getCurrent().getSystemColor( SWT.COLOR_TITLE_BACKGROUND_GRADIENT );
      }
      control.setBackground( c );
    }

    void setControl( Control aControl ) {
      control = aControl;
      bkColor = control.getBackground();
    }
  }

  class PanelItem
      implements IStridable {

    private final IVedEditorTool tool;

    private final VedToolsGroup group;

    private boolean selected = false;

    private Action action = null;

    PanelItem( IVedEditorTool aTool ) {
      tool = aTool;
      group = null;
    }

    PanelItem( VedToolsGroup aGroup ) {
      tool = null;
      group = aGroup;
    }

    @Override
    public String id() {
      if( group != null ) {
        return group.id();
      }
      return tool.id();
    }

    @Override
    public String description() {
      if( group != null ) {
        group.description();
      }
      return tool.description();
    }

    @Override
    public String nmName() {
      return id();
    }

    boolean isGroup() {
      return group != null;
    }

    IVedEditorTool asTool() {
      return tool;
    }

    VedToolsGroup asGroup() {
      return group;
    }

    void setSelected( boolean aSelected ) {
      IVedEditorTool itemTool;
      if( isGroup() ) {
        itemTool = group.selectedTool();
      }
      else {
        itemTool = tool;
      }

      if( !aSelected ) {
        if( selected ) {
          fireToolDeactivated( activeTool );
          activeTool = null;
        }
      }
      else {
        if( activeTool == null || !activeTool.equals( itemTool ) ) {
          activeTool = itemTool;
          fireToolActivated( itemTool );
        }
      }

      selected = aSelected;
      action.setChecked( aSelected );
    }

    Action action() {
      return action;
    }

    void setAction( Action aAction ) {
      action = aAction;
    }
  }

  class GroupToolSelectionListener
      extends SelectionAdapter {

    private final GroupAction action;

    GroupToolSelectionListener( GroupAction aAction ) {
      action = aAction;
    }

    @Override
    public void widgetSelected( SelectionEvent aE ) {
      IVedEditorTool tool = (IVedEditorTool)aE.widget.getData();

      PanelItem item = null;
      if( activeTool != null ) {
        item = findPanelItem( activeTool.id() );
      }
      PanelItem item1 = findPanelItem( tool.id() );

      if( item1.equals( item ) ) {
        fireToolDeactivated( activeTool );
        activeTool = tool;
        fireToolActivated( tool );
      }

      action.group.selectTool( tool );
      ImageDescriptor imd = tsContext().get( ITsIconManager.class ).loadFreeDescriptor( tool.iconId() );
      action.setImageDescriptor( imd );
      action.setToolTipText( tool.description() );
    }
  }

  private final IListEdit<IVedToolSelectionListener> listeners = new ElemLinkedBundleList<>();

  private final IStridablesListEdit<PanelItem> items = new StridablesList<>();

  private IVedEditorTool activeTool = null;

  private final Composite tbHolder;

  /**
   * Конструктор.<br>
   * Запоминает ссылку на контекст. Не копирует его.
   *
   * @param aParent Composite - родительская компонента
   * @param aContext ITsGuiContext - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public VedToolsPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    tbHolder = new Composite( this, SWT.NONE );
    aParent.setBackground( tsContext().get( ITsColorManager.class ).getColor( ETsColor.GRAY ) );
    tbHolder.setBackground( tsContext().get( ITsColorManager.class ).getColor( ETsColor.GRAY ) );
    RowLayout rowLayout = new RowLayout( SWT.VERTICAL );
    rowLayout.center = false;
    rowLayout.fill = true;
    rowLayout.pack = false;
    tbHolder.setLayout( rowLayout );
  }

  @Override
  public void createContent() {
    for( PanelItem item : items ) {
      if( !item.isGroup() ) {
        ToolBarManager tbManager = new ToolBarManager( SWT.FLAT | SWT.VERTICAL );
        ToolAction action = new ToolAction( item.asTool() );
        tbManager.add( action );
        tbManager.createControl( tbHolder );
        item.setAction( action );
      }
      else {
        ToolBarManager tbManager = new ToolBarManager( SWT.FLAT | SWT.VERTICAL );
        GroupAction action = new GroupAction( item.asGroup() );
        tbManager.add( action );
        Control ctrl = tbManager.createControl( tbHolder );
        action.setControl( ctrl );
        item.setAction( action );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IVedToolSelectionEventProducer}
  //

  @Override
  public void addToolSelectionListener( IVedToolSelectionListener aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeToolSelectionListener( IVedToolSelectionListener aListener ) {
    listeners.remove( aListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IToolsPanel}
  //

  @Override
  public IVedEditorTool activeTool() {
    return activeTool;
  }

  @Override
  public IStridablesList<IVedEditorTool> listTools() {
    IStridablesListEdit<IVedEditorTool> result = new StridablesList<>();
    for( PanelItem item : items ) {
      if( item.isGroup() ) {
        result.addAll( item.asGroup().listTools() );
      }
      else {
        result.add( item.asTool() );
      }
    }
    return result;
  }

  @Override
  public String findGroupId( String aToolId ) {
    for( PanelItem item : items ) {
      if( item.isGroup() ) {
        if( item.asGroup().listTools().hasKey( aToolId ) ) {
          return item.asGroup().id();
        }
      }
    }
    return null;
  }

  @Override
  public void addTool( IVedEditorTool aTool, String aGroupId ) {
    TsNullArgumentRtException.checkNull( aTool );
    if( aGroupId != null && !aGroupId.isBlank() ) {
      VedToolsGroup tg = findGroup( aGroupId );
      tg.addTool( aTool );
    }
    else {
      items.add( new PanelItem( aTool ) );
    }
  }

  @Override
  public VedToolsGroup createGroup( String aId ) {
    TsItemAlreadyExistsRtException.checkTrue( findGroup( aId ) != null );
    VedToolsGroup tg = new VedToolsGroup( aId );
    items.add( new PanelItem( tg ) );
    return tg;
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  VedToolsGroup findGroup( String aGroupId ) {
    for( PanelItem item : items ) {
      if( item.isGroup() ) {
        if( item.asGroup().id().equals( aGroupId ) ) {
          return item.asGroup();
        }
      }
    }
    return null;
  }

  PanelItem findPanelItem( String aToolId ) {
    for( PanelItem item : items ) {
      if( item.isGroup() ) {
        if( item.group.listTools().hasKey( aToolId ) ) {
          return item;
        }
      }
      else {
        if( item.tool.id().equals( aToolId ) ) {
          return item;
        }
      }
    }
    return null;
  }

  void initMenuFromGroup( Menu aMenu, GroupAction aAction ) {

    GroupToolSelectionListener listener = new GroupToolSelectionListener( aAction );

    for( IVedEditorTool tool : aAction.group.listTools() ) {
      MenuItem item = new MenuItem( aMenu, SWT.PUSH );
      item.setText( tool.nmName() );
      item.setData( tool );
      Image img = tsContext().get( ITsIconManager.class ).loadFreeIcon( tool.iconId() );
      item.setImage( img );
      item.addSelectionListener( listener );
    }

  }

  void clearSelection() {
    for( PanelItem item : items ) {
      item.setSelected( false );
    }
  }

  private void fireToolActivated( IVedEditorTool aTool ) {
    // System.out.println( "ToolActivated: " + aTool.id() );
    for( IVedToolSelectionListener l : listeners ) {
      l.onToolActivated( aTool );
    }
  }

  private void fireToolDeactivated( IVedEditorTool aTool ) {
    // System.out.println( "ToolDeactivated: " + aTool.id() );
    for( IVedToolSelectionListener l : listeners ) {
      l.onToolDeactivated( aTool );
    }
  }

}
