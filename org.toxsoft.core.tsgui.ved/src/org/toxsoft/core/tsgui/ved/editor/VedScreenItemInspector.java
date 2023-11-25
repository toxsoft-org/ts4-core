package org.toxsoft.core.tsgui.ved.editor;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.ved.editor.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The SWT panel to inspect the VED item, either VISEL or actor.
 * <p>
 * Panel content:
 * <ul>
 * <li>at {@link BorderLayout#NORTH} - read0only VED item ID field;</li>
 * <li>at {@link BorderLayout#CENTER} - inspector tree widget {@link ITinWidget}.</li>
 * </ul>
 * <p>
 * TODO Add item ID edit handler in {@link #whenBtnEditPressed()}
 *
 * @author hazard157
 */
public class VedScreenItemInspector
    extends TsPanel {

  private final IVedScreen vedScreen;
  private final CLabel     label;
  private final Text       idText;
  private final Button     btnCopy;  // copy item ID to clipboard
  private final Button     btnEdit;  // edit item ID
  private final ITinWidget tinWidget;
  private final EIconSize  iconSize; // label and buttons icons size

  private IVedItem vedItem = null;

  private boolean selfEditing = false;

  /**
   * Constructor.
   *
   * @param aParent {@link Composite} - SWT parent
   * @param aVedScreen {@link IVedScreen} - the screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedScreenItemInspector( Composite aParent, IVedScreen aVedScreen ) {
    super( aParent, TsNullArgumentRtException.checkNull( aVedScreen ).tsContext() );
    vedScreen = aVedScreen;
    this.setLayout( new BorderLayout() );
    iconSize = hdpiService().getToolbarIconsSize().prevSize();
    // create ID field
    Composite northBoard = new Composite( this, SWT.BORDER );
    northBoard.setLayoutData( BorderLayout.NORTH );
    GridLayout gl1 = new GridLayout( 4, false );
    gl1.marginHeight = 2;
    northBoard.setLayout( gl1 );
    // label
    label = new CLabel( northBoard, SWT.CENTER );
    label.setText( STR_LABEL_ID + ": " ); //$NON-NLS-1$
    label.setToolTipText( STR_LABEL_ID );
    label.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ) );
    // ID read-only text
    idText = new Text( northBoard, SWT.READ_ONLY | SWT.BORDER );
    idText.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
    // btnCopy
    btnCopy = new Button( northBoard, SWT.PUSH );
    // btnCopy.setText( STR_BTN_COPY );
    btnCopy.setToolTipText( STR_BTN_COPY_D );
    btnCopy.setImage( iconManager().loadStdIcon( ICONID_EDIT_COPY, iconSize ) );
    // btnEdit
    btnEdit = new Button( northBoard, SWT.PUSH );
    // btnEdit.setText( STR_BTN_EDIT );
    btnEdit.setToolTipText( STR_BTN_EDIT_D );
    btnEdit.setImage( iconManager().loadStdIcon( ICONID_DOCUMENT_EDIT, iconSize ) );
    // inspector tree
    tinWidget = new TinWidget( tsContext() );
    tinWidget.createControl( this );
    tinWidget.getControl().setLayoutData( BorderLayout.CENTER );
    // setup
    btnCopy.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        whenBtnCopyPressed();
      }
    } );
    btnEdit.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        whenBtnEditPressed();
      }
    } );
    tinWidget.addPropertyChangeListener( this::whenInspectorChanges );
    vedScreen.model().visels().eventer().addListener( this::whenViselsChanged );
    vedScreen.model().actors().eventer().addListener( this::whenActorsChanged );
    updateVedKindIcon();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void whenViselsChanged( @SuppressWarnings( "unused" ) IVedItemsManager<VedAbstractVisel> aSource, ECrudOp aOp,
      String aId ) {
    if( selfEditing || vedItem == null || vedItem.kind() != EVedItemKind.VISEL ) {
      return;
    }
    String currItemId = vedItem.id();
    if( aId == null || aId.equals( currItemId ) ) {
      switch( aOp ) {
        case CREATE: {
          // this must not happen, as I think now...
          throw new TsInternalErrorRtException();
        }
        case REMOVE: {
          clearInspector();
          break;
        }
        case EDIT:
        case LIST: {
          refreshInspectorValues();
          break;
        }
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
  }

  private void whenActorsChanged( @SuppressWarnings( "unused" ) IVedItemsManager<VedAbstractActor> aSource, ECrudOp aOp,
      String aId ) {
    if( selfEditing || vedItem == null || vedItem.kind() != EVedItemKind.ACTOR ) {
      return;
    }
    String currItemId = vedItem.id();
    if( aId == null || aId.equals( currItemId ) ) {
      switch( aOp ) {
        case CREATE: {
          // this must not happen, as I think now...
          throw new TsInternalErrorRtException();
        }
        case REMOVE: {
          clearInspector();
          break;
        }
        case EDIT:
        case LIST: {
          refreshInspectorValues();
          break;
        }
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
  }

  private void whenInspectorChanges( @SuppressWarnings( "unused" ) ITinWidget aSource, String aChangedPropId ) {
    if( selfEditing || vedItem == null ) {
      throw new TsInternalErrorRtException(); // this must not happen
    }
    try {
      selfEditing = true;
      ITinValue tv = tinWidget.getValue();
      IAtomicValue av = tv.childValues().getByKey( aChangedPropId ).atomicValue();
      /**
       * NOTE: due to IPropertiesSet implementation conceptual problem (see fix-me note in the
       * PropertiesSet.doBeforeSet() method body), here we MUST call batch update method setProps(), not a
       * setValue().<br>
       * Even when conceptual problem will be solved, this code will remain. Only this note need to be removed :)
       */
      IOptionSetEdit newValueAsSet = new OptionSet();
      newValueAsSet.setValue( aChangedPropId, av );
      vedItem.props().setProps( newValueAsSet );
    }
    finally {
      selfEditing = false;
    }
  }

  private void whenBtnCopyPressed() {
    if( vedItem != null ) {
      String s = vedItem.id();
      Clipboard clipboard = new Clipboard( getDisplay() );
      clipboard.setContents( new String[] { s }, new Transfer[] { TextTransfer.getInstance() } );
      clipboard.dispose();
    }
  }

  private void whenBtnEditPressed() {

    // TODO VedScreenItemInspector.whenBtnEditPressed()
    TsDialogUtils.underDevelopment( getShell() );
  }

  private void updateVedKindIcon() {
    String iconId = ICONID_TRANSPARENT;
    if( vedItem != null ) {
      IVedItemFactoryBase<?> factory = getFactory( vedItem );
      iconId = factory.iconId();
    }
    label.setImage( iconManager().loadStdIcon( iconId, iconSize ) );
  }

  private IVedItemFactoryBase<?> getFactory( IVedItem aVedItem ) {
    return switch( aVedItem.kind() ) {
      case VISEL -> {
        IVedViselFactoriesRegistry facReg = tsContext().get( IVedViselFactoriesRegistry.class );
        yield facReg.get( aVedItem.factoryId() );
      }
      case ACTOR -> {
        IVedActorFactoriesRegistry facReg = tsContext().get( IVedActorFactoriesRegistry.class );
        yield facReg.get( aVedItem.factoryId() );
      }
      default -> throw new TsNotAllEnumsUsedRtException( aVedItem.kind().id() );
    };
  }

  private void refreshInspectorValues() {
    idText.setText( vedItem.id() );
    IVedItemFactoryBase<?> factory = getFactory( vedItem );
    ITinValue tv = factory.typeInfo().makeValue( vedItem );
    tinWidget.setValue( tv );
  }

  private void clearInspector() {
    vedItem = null;
    idText.setText( TsLibUtils.EMPTY_STRING );
    tinWidget.setEntityInfo( null );
    tinWidget.setValue( null );
    updateVedKindIcon();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the inspected VED item,
   *
   * @return {@link IVedItem} - the inspected VED item or <code>null</code>
   */
  public IVedItem getVedItem() {
    return vedItem;
  }

  /**
   * Sets VED item to inspect.
   *
   * @param aItem {@link IVedItem} - the VED item to inspect or <code>null</code>
   * @throws TsItemNotFoundRtException the item is not in VED screen model
   */
  public void setVedItem( IVedItem aItem ) {
    if( aItem == null ) {
      clearInspector();
      return;
    }
    // only existing in owner VED screen items are allowed
    switch( aItem.kind() ) {
      case VISEL: {
        TsItemNotFoundRtException.checkFalse( vedScreen.model().visels().list().hasElem( (VedAbstractVisel)aItem ) );
        break;
      }
      case ACTOR: {
        TsItemNotFoundRtException.checkFalse( vedScreen.model().actors().list().hasElem( (VedAbstractActor)aItem ) );
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( aItem.kind().id() );
    }
    // update inspector
    vedItem = aItem;
    IVedItemFactoryBase<?> factory = getFactory( vedItem );
    tinWidget.setEntityInfo( factory.typeInfo() );
    refreshInspectorValues();
    updateVedKindIcon();
  }

}
