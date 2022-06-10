package org.toxsoft.core.tsgui.panels.toolbar;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.panels.toolbar.ITsResources.*;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsToolBar} implementation.
 *
 * @author hazard157
 */
public class TsToolbar
    extends AbstractLazyPanel<Control>
    implements ITsToolBar {

  private final IListEdit<ITsToolbarListener> listeners = new ElemArrayList<>();

  private final IListEdit<ITsActionDef>  allActionDefs = new ElemArrayList<>();
  private final IListEdit<ITsActionDef>  buttonActDefs = new ElemArrayList<>();
  private final IStringMapEdit<TsAction> actionsMap    = new StringMap<>();

  private TsComposite    panel     = null;
  private ToolBar        tbControl = null;
  private ToolBarManager tbManager = null;
  private Label          nameLabel = null;

  private String nameLabelText = TsLibUtils.EMPTY_STRING;
  private String tooltipText   = TsLibUtils.EMPTY_STRING;

  private EIconSize iconSize = EIconSize.IS_24X24;
  private boolean   vertical = false;

  // ------------------------------------------------------------------------------------
  // Creation
  //

  /**
   * Constructor.
   * <p>
   * Constructor hods reference to the argument.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsToolbar( ITsGuiContext aContext ) {
    super( aContext );
  }

  /**
   * Creates named toolbar.
   *
   * @param aParent {@link Composite} - parent composite
   * @param aContext {@link ITsGuiContext} - the context
   * @param aName String - toolbar name, may be <code>null</code>
   * @param aIconSize {@link EIconSize} - icons size or <code>null</code> for default size
   * @param aActionDefs {@link ITsActionDef} - action buttons on toolbar
   * @return {@link TsToolbar} - created instance
   */
  public static TsToolbar create( Composite aParent, ITsGuiContext aContext, String aName, EIconSize aIconSize,
      ITsActionDef... aActionDefs ) {
    TsNullArgumentRtException.checkNulls( aParent, aContext, aIconSize );
    TsErrorUtils.checkArrayArg( aActionDefs );
    TsToolbar toolBar = new TsToolbar( aContext );
    for( ITsActionDef actDef : aActionDefs ) {
      toolBar.addActionDef( actDef );
    }
    if( aName != null ) {
      toolBar.setNameLabelText( aName );
    }
    if( aIconSize == null ) {
      toolBar.setIconSize( toolBar.hdpiService().getToolbarIconsSize() );
    }
    else {
      toolBar.setIconSize( aIconSize );
    }
    toolBar.createControl( aParent );
    return toolBar;
  }

  /**
   * Creates unnamed toolbar.
   *
   * @param aParent {@link Composite} - parent composite
   * @param aContext {@link ITsGuiContext} - the context
   * @param aIconSize {@link EIconSize} - icons size or <code>null</code> for default size
   * @param aActionDefs {@link ITsActionDef}[] - action buttons on toolbar
   * @return {@link TsToolbar} - created instance
   */
  public static TsToolbar create( Composite aParent, ITsGuiContext aContext, EIconSize aIconSize,
      ITsActionDef... aActionDefs ) {
    return create( aParent, aContext, null, aIconSize, aActionDefs );
  }

  /**
   * Creates unnamed toolbar with icons of default size.
   *
   * @param aParent {@link Composite} - parent composite
   * @param aContext {@link ITsGuiContext} - the context
   * @param aActionDefs {@link ITsActionDef}[] - action buttons on toolbar
   * @return {@link TsToolbar} - created instance
   */
  public static TsToolbar create( Composite aParent, ITsGuiContext aContext, ITsActionDef... aActionDefs ) {
    return create( aParent, aContext, null, null, aActionDefs );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void fireToolButtonPressed( String aActionId ) {
    if( !listeners.isEmpty() ) {
      for( ITsToolbarListener l : listeners ) {
        l.onToolButtonPressed( aActionId );
      }
    }
  }

  private void internalAddToToolbarManager( ITsActionDef aActionDef ) {
    if( aActionDef.id().equals( ACTID_SEPARATOR ) ) {
      tbManager.add( new Separator() );
      return;
    }
    TsAction action = new TsAction( aActionDef, iconSize, tsContext() ) {

      @Override
      public void run() {
        fireToolButtonPressed( aActionDef.id() );
      }
    };
    tbManager.add( action );
    actionsMap.put( aActionDef.id(), action );
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    // panel
    panel = new TsComposite( aParent, SWT.NONE );
    BorderLayout layout = new BorderLayout();
    panel.setLayout( layout );
    // nameLabel
    nameLabel = new Label( panel, SWT.LEFT );
    if( vertical ) {
      nameLabel.setLayoutData( BorderLayout.NORTH );
    }
    else {
      nameLabel.setLayoutData( BorderLayout.WEST );
    }
    nameLabel.setText( nameLabelText );
    nameLabel.setToolTipText( tooltipText );
    // tbManager
    int style = SWT.FLAT;
    if( vertical ) {
      style |= SWT.VERTICAL;
    }
    tbManager = new ToolBarManager( style );
    for( ITsActionDef d : allActionDefs ) {
      internalAddToToolbarManager( d );
    }
    // tbControl
    tbControl = tbManager.createControl( panel );
    tbControl.setLayoutData( BorderLayout.CENTER );
    return panel;
  }

  // ------------------------------------------------------------------------------------
  // ITsToolBar
  //

  @Override
  public boolean isVertical() {
    return vertical;
  }

  @Override
  public void setVertical( boolean aVartical ) {
    if( !isControlValid() ) {
      vertical = aVartical;
    }
  }

  @Override
  public String getNameLabelText() {
    return nameLabelText;
  }

  @Override
  public void setNameLabelText( String aText ) {
    TsNullArgumentRtException.checkNull( aText );
    nameLabelText = aText;
    if( isControlValid() ) {
      nameLabel.setText( nameLabelText );
    }
  }

  @Override
  public String getTooltipText() {
    return tooltipText;
  }

  @Override
  public void setTooltipText( String aTooltip ) {
    TsNullArgumentRtException.checkNull( aTooltip );
    tooltipText = aTooltip;
    if( isControlValid() ) {
      nameLabel.setText( tooltipText );
      panel.setToolTipText( tooltipText );
      tbControl.setToolTipText( tooltipText );
    }
  }

  @Override
  public TsAction findAction( String aActionId ) {
    TsNullArgumentRtException.checkNull( aActionId );
    TsIllegalStateRtException.checkFalse( isControlValid() );
    return actionsMap.findByKey( aActionId );
  }

  @Override
  public TsAction getAction( String aActionId ) {
    TsNullArgumentRtException.checkNull( aActionId );
    TsIllegalStateRtException.checkFalse( isControlValid() );
    return actionsMap.getByKey( aActionId );
  }

  @Override
  public IList<ITsActionDef> listButtonItems() {
    return buttonActDefs;
  }

  @Override
  public IList<ITsActionDef> listAllItems() {
    return allActionDefs;
  }

  @Override
  public boolean isActionEnabled( String aActionId ) {
    TsAction a = findAction( aActionId );
    if( a != null ) {
      return a.isEnabled();
    }
    return false;
  }

  @Override
  public void setActionEnabled( String aActionId, boolean aEnabled ) {
    TsAction a = findAction( aActionId );
    if( a != null ) {
      a.setEnabled( aEnabled );
    }
  }

  @Override
  public boolean isActionChecked( String aActionId ) {
    TsAction a = getAction( aActionId );
    if( a != null ) {
      return a.isChecked();
    }
    return false;
  }

  @Override
  public void setActionChecked( String aActionId, boolean aChecked ) {
    TsAction a = findAction( aActionId );
    if( a != null ) {
      a.setChecked( aChecked );
    }
  }

  @Override
  public void setActionText( String aActionId, String aText ) {
    TsAction a = findAction( aActionId );
    if( a != null ) {
      a.setText( aText );
    }
  }

  @Override
  public void setActionImage( String aActionId, ImageDescriptor aImageDescriptior ) {
    TsAction a = findAction( aActionId );
    if( a != null ) {
      a.setImageDescriptor( aImageDescriptior );
    }
  }

  @Override
  public void setActionTooltipText( String aActionId, String aText ) {
    TsAction a = findAction( aActionId );
    if( a != null ) {
      a.setToolTipText( aText );
    }
  }

  @Override
  public void setActionMenu( String aActionId, IMenuCreator aMenuCreator ) {
    TsAction a = findAction( aActionId );
    if( a != null ) {
      TsIllegalStateRtException.checkFalse( a.getStyle() == IAction.AS_DROP_DOWN_MENU );
      a.setMenuCreator( aMenuCreator );
    }
  }

  @Override
  public void addAction( TsAction aAction ) {
    TsNullArgumentRtException.checkNull( aAction );
    tbManager.add( aAction );
    tbManager.update( true );
  }

  @Override
  public void addActionDef( ITsActionDef aActionDef ) {
    TsNullArgumentRtException.checkNull( aActionDef );
    if( isControlValid() ) {
      internalAddToToolbarManager( aActionDef );
    }
    // can not duplicate an action
    for( ITsActionDef d : buttonActDefs ) {
      if( d.id().equals( aActionDef.id() ) ) {
        throw new TsItemAlreadyExistsRtException( FMT_ERR_DUP_ACT_ID, aActionDef.id() );
      }
    }
    allActionDefs.add( aActionDef );
    // update buttonActDefs
    if( !aActionDef.id().equals( ACTID_SEPARATOR ) ) {
      buttonActDefs.add( aActionDef );
    }
  }

  @Override
  public void addActionDefs( IList<ITsActionDef> aActionDefs ) {
    TsNullArgumentRtException.checkNull( aActionDefs );
    for( ITsActionDef d : aActionDefs ) {
      addActionDef( d );
    }
  }

  @Override
  public void addActionDefs( ITsActionDef... aActionDefs ) {
    TsNullArgumentRtException.checkNull( aActionDefs );
    for( ITsActionDef d : aActionDefs ) {
      addActionDef( d );
    }
  }

  @Override
  public void addSeparator() {
    addActionDef( ACDEF_SEPARATOR );
  }

  @Override
  public void addContributionItem( IContributionItem aItem ) {
    TsNullArgumentRtException.checkNull( aItem );
    TsIllegalStateRtException.checkFalse( isControlValid() );
    tbManager.add( aItem );
    tbManager.update( true );
  }

  @Override
  public void addListener( ITsToolbarListener aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeListener( ITsToolbarListener aListener ) {
    listeners.remove( aListener );
  }

  // ------------------------------------------------------------------------------------
  // IIconSizeable
  //

  @Override
  public EIconSize iconSize() {
    return iconSize;
  }

  @Override
  public void setIconSize( EIconSize aIconSize ) {
    TsNullArgumentRtException.checkNull( aIconSize );
    if( !isControlValid() ) {
      if( iconSize != aIconSize ) {
        iconSize = aIconSize;
      }
    }
  }

  @Override
  public EIconSize defaultIconSize() {
    return hdpiService().getToolbarIconsSize();
  }

}
