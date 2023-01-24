package org.toxsoft.core.tsgui.panels.inpled;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Button bar for {@link InplaceEditorPanel}.
 *
 * @author hazard157
 */
class ButtonBar
    extends Composite
    implements ITsGuiContextable {

  private final SelectionAdapter buttonListener = new SelectionAdapter() {

    @Override
    public void widgetSelected( SelectionEvent aEvent ) {
      Button btn = (Button)aEvent.widget;
      actionHandler.handleAction( (String)btn.getData() );
    }

  };

  private final ITsGuiContext    tsContext;
  private final ITsActionHandler actionHandler;

  private final EIconSize              iconSize;
  private final IStringMapEdit<Button> btnsMap = new StringMap<>();

  public ButtonBar( Composite aParent, ITsGuiContext aContext, ITsActionHandler aButtonPressHandler ) {
    super( aParent, SWT.BORDER );
    tsContext = aContext;
    actionHandler = aButtonPressHandler;
    iconSize = hdpiService().getDefaultIconSize(); // TODO how to specify icon size ?
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void removeButtons() {
    while( !btnsMap.isEmpty() ) {
      Button b = btnsMap.removeByKey( btnsMap.keys().first() );
      b.dispose();
    }
  }

  public void createButton( ITsActionDef aActionDef ) {
    Button btn = new Button( this, SWT.PUSH );
    btn.setData( aActionDef.id() );
    btn.setText( aActionDef.nmName() );
    btn.setToolTipText( aActionDef.description() );
    btn.setImage( iconManager().loadStdIcon( aActionDef.iconId(), iconSize ) );
    btn.addSelectionListener( buttonListener );
    btnsMap.put( aActionDef.id(), btn );
  }

  public void setActionEnabled( String aActionId, boolean aEnabled ) {
    Button b = btnsMap.findByKey( aActionId );
    if( b != null ) {
      b.setEnabled( aEnabled );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

}
