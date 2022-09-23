package org.toxsoft.core.tsgui.bricks.qtree.impl;

import static org.toxsoft.core.tsgui.bricks.qtree.IQTreeViewerConstants.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.qtree.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IQTreeViewer} implementation.
 *
 * @author hazard157
 */
public class QTreeViewer
    implements ITsGuiContextable, IQTreeViewer, ITsContextListener {

  private static final IQNodeKind<Object> DEFAULT_ROOT_KIND = new QNodeKind<>( "defRookKind", Object.class, false ); //$NON-NLS-1$

  private final ISelectionChangedListener treeSelectionChangedListener = new ISelectionChangedListener() {

    @Override
    public void selectionChanged( SelectionChangedEvent aEvent ) {
      selectionChangeEventHelper.fireTsSelectionEvent( selectedItem() );
    }
  };

  private final IDoubleClickListener treeDoubleClickListener = new IDoubleClickListener() {

    @Override
    public void doubleClick( DoubleClickEvent aEvent ) {
      doubleClickEventHelper.fireTsDoublcClickEvent( selectedItem() );
    }
  };

  private final ITsGuiContext                        tsContext;
  private final TsSelectionChangeEventHelper<IQNode> selectionChangeEventHelper;
  private final TsDoubleClickEventHelper<IQNode>     doubleClickEventHelper;
  private final TsUserInputEventsBinder              userInputEventsBinder;
  private final GenericChangeEventer                 iconSizeEventer;
  private final GenericChangeEventer                 thumbSizeEventer;

  private final QTreeContentProvider contentProvider;
  private final QTreeLabelProvider   labelProvider;
  private final QTreeColumnManager   columnManager;
  private final IQRootNode           defaultRootNode;

  private TreeViewer    treeViewer = null;
  private IQTreeConsole console    = null;
  private IQRootNode    rootNode;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public QTreeViewer( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    tsContext = aContext;
    selectionChangeEventHelper = new TsSelectionChangeEventHelper<>( this );
    doubleClickEventHelper = new TsDoubleClickEventHelper<>( this );
    userInputEventsBinder = new TsUserInputEventsBinder( this );
    iconSizeEventer = new GenericChangeEventer( this );
    thumbSizeEventer = new GenericChangeEventer( this );
    labelProvider = new QTreeLabelProvider( this );
    columnManager = new QTreeColumnManager( this );
    contentProvider = new QTreeContentProvider();
    defaultRootNode = new AbstractQRootNode<>( "default_root", DEFAULT_ROOT_KIND, tsContext, this, IOptionSet.NULL ) { //$NON-NLS-1$

      @Override
      protected IStridablesList<IQNode> doGetNodes() {
        return IStridablesList.EMPTY;
      }
    };
    rootNode = defaultRootNode;
    tsContext.addContextListener( this );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void applyLineVisibility() {
    boolean isLinesVisible = OPDEF_IS_LINES_VISIBLE.getValue( tsContext.params() ).asBool();
    treeViewer.getTree().setLinesVisible( isLinesVisible );
  }

  private void applyNodeIconOrImageSizeChange() {
    // TODO QTreeViewer.applyNodeIconOrImageSizeChange()
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  TreeViewer treeViewer() {
    return treeViewer;
  }

  void applyLabelProviderChanges() {
    treeViewer.setLabelProvider( labelProvider ); // apply changes to SWT/JFace
  }

  void applyHeaderVisibility() {
    boolean isHeaderVisible = OPDEF_IS_HEADER_SHOWN.getValue( tsContext.params() ).asBool();
    treeViewer.getTree().setHeaderVisible( isHeaderVisible );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // ITsContextListener
  //

  @Override
  public <C extends ITsContextRo> void onContextRefChanged( C aSource, String aName, Object aRef ) {
    // nop
  }

  @Override
  public <C extends ITsContextRo> void onContextOpChanged( C aSource, String aId, IAtomicValue aValue ) {
    // header visibility
    if( aId == null || aId.equals( OPID_IS_HEADER_SHOWN ) ) {
      applyHeaderVisibility();
    }
    // lines visibility
    if( aId == null || aId.equals( OPID_IS_LINES_VISIBLE ) ) {
      applyLineVisibility();
    }
    // default icon/thumb size
    if( aId == null || aId.equals( OPID_DEFAULT_ICON_SIZE ) || aId.equals( OPID_DEFAULT_THUMB_SIZE ) ) {
      applyNodeIconOrImageSizeChange();
    }
  }

  // ------------------------------------------------------------------------------------
  // ILazyControl
  //

  @Override
  public Control createControl( Composite aParent ) {
    treeViewer = new TreeViewer( aParent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION );
    console = new QTreeConsole( treeViewer );
    treeViewer.setContentProvider( contentProvider );
    treeViewer.setLabelProvider( labelProvider );
    treeViewer.addSelectionChangedListener( treeSelectionChangedListener );
    treeViewer.addDoubleClickListener( treeDoubleClickListener );
    applyHeaderVisibility();
    applyLineVisibility();
    treeViewer.setInput( rootNode );

    userInputEventsBinder.bindToControl( treeViewer.getControl(), TsUserInputEventsBinder.BIND_ALL_INPUT_EVENTS );
    return treeViewer.getControl();
  }

  @Override
  public Control getControl() {
    if( treeViewer == null ) {
      return null;
    }
    return treeViewer.getControl();
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<IQNode> aListener ) {
    selectionChangeEventHelper.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<IQNode> aListener ) {
    selectionChangeEventHelper.removeTsSelectionListener( aListener );
  }

  @Override
  public IQNode selectedItem() {
    if( getControl() == null ) {
      return null;
    }
    IStructuredSelection ss = (IStructuredSelection)treeViewer.getSelection();
    if( ss.isEmpty() ) {
      return null;
    }
    return IQNode.class.cast( ss.getFirstElement() );
  }

  @Override
  public void setSelectedItem( IQNode aItem ) {
    if( getControl() != null ) {
      IStructuredSelection selection = StructuredSelection.EMPTY;
      if( aItem != null ) {
        selection = new StructuredSelection( aItem );
      }
      treeViewer.setSelection( selection, true );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsDoubleClickEventProducer
  //

  @Override
  public void addTsDoubleClickListener( ITsDoubleClickListener<IQNode> aListener ) {
    doubleClickEventHelper.addTsDoubleClickListener( aListener );
  }

  @Override
  public void removeTsDoubleClickListener( ITsDoubleClickListener<IQNode> aListener ) {
    doubleClickEventHelper.removeTsDoubleClickListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputProducer
  //

  @Override
  public void addTsUserInputListener( ITsUserInputListener aListener ) {
    userInputEventsBinder.addTsKeyInputListener( aListener );
  }

  @Override
  public void removeTsUserInputListener( ITsUserInputListener aListener ) {
    userInputEventsBinder.removeTsKeyInputListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // IIconSizeableEx
  //

  @Override
  public EIconSize iconSize() {
    return labelProvider.defaultIconSize();
  }

  @Override
  public void setIconSize( EIconSize aIconSize ) {
    TsNullArgumentRtException.checkNull( aIconSize );
    if( iconSize() != aIconSize ) {
      labelProvider.setIconSize( aIconSize );
      applyLabelProviderChanges();
      iconSizeEventer.fireChangeEvent();
    }
  }

  @Override
  public EIconSize defaultIconSize() {
    return labelProvider.defaultIconSize();
  }

  @Override
  public IGenericChangeEventer iconSizeChangeEventer() {
    return iconSizeEventer;
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeableEx
  //

  @Override
  public EThumbSize thumbSize() {
    return labelProvider.thumbSize();
  }

  @Override
  public EThumbSize defaultThumbSize() {
    return labelProvider.defaultThumbSize();
  }

  @Override
  public void setThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    if( thumbSize() != aThumbSize ) {
      labelProvider.setThumbSize( aThumbSize );
      applyLabelProviderChanges();
      thumbSizeEventer.fireChangeEvent();
    }
  }

  @Override
  public IGenericChangeEventer thumbSizeEventer() {
    return thumbSizeEventer;
  }

  // ------------------------------------------------------------------------------------
  // IQTreeViewer
  //

  @Override
  public IQRootNode rootNode() {
    return rootNode;
  }

  @Override
  public void setRoot( IQRootNode aRootNode ) {
    if( aRootNode != null ) {
      rootNode = aRootNode;
    }
    else {
      rootNode = defaultRootNode;
    }
    treeViewer.setInput( rootNode );
  }

  @Override
  public IQTreeColumnManager columnManager() {
    return columnManager;
  }

  @Override
  public void setCellFontAndColorProvider( IQTreeCellFontAndColorProvider aProvider ) {
    labelProvider.setFontAndColorProvider( aProvider );
  }

  @Override
  public IQTreeConsole console() {
    return console;
  }

}
