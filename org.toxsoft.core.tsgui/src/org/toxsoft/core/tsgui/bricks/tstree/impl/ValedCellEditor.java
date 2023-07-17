package org.toxsoft.core.tsgui.bricks.tstree.impl;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link CellEditor} implementation useing {@link IValedControl} as an actual editor.
 *
 * @author vs
 * @param <V> - type of the edited value
 */
public class ValedCellEditor<V>
    extends CellEditor
    implements ITsGuiContextable {

  IValedControl<V> valed;

  Control valedControl;

  TraverseListener keyListener = aEvent -> {
    switch( aEvent.detail ) {
      case SWT.TRAVERSE_ESCAPE:
        fireCancelEditor();
        aEvent.doit = false;
        break;
      case SWT.TRAVERSE_RETURN:
        fireApplyEditorValue();
        aEvent.doit = false;
        break;
      default:
        break;
    }
  };

  private final ITsGuiContext ctx;

  /**
   * Constructor.
   *
   * @param aValed {@link IValedControl}&lt;V&gt; - instance of the value editor
   * @param aParent {@link ColumnViewer} - the SWT viewer for which the cell editor is created
   * @param aTsContext ITsGuiContext - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedCellEditor( IValedControl<V> aValed, ColumnViewer aParent, ITsGuiContext aTsContext ) {
    TsNullArgumentRtException.checkNulls( aParent, aParent, aParent );
    valed = aValed;
    ctx = aTsContext;
    create( (Composite)aParent.getControl() );
  }

  // ------------------------------------------------------------------------------------
  // CellEditor
  //

  @Override
  protected Control createControl( Composite aParent ) {
    Composite bkPanel = new Composite( aParent, SWT.NONE );
    GridLayout gd = new GridLayout( 2, false );
    gd.horizontalSpacing = 0;
    gd.marginBottom = 0;
    gd.marginHeight = 0;
    gd.marginLeft = 0;
    gd.marginRight = 0;
    gd.marginTop = 0;
    gd.verticalSpacing = 0;
    bkPanel.setLayout( gd );

    valedControl = valed.createControl( bkPanel );
    valedControl.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

    Button okBtn = new Button( bkPanel, SWT.PUSH );
    okBtn.setImage( iconManager().loadStdIcon( ICONID_DIALOG_OK, EIconSize.IS_16X16 ) );
    okBtn.setLayoutData( new GridData( SWT.RIGHT, SWT.FILL, false, true ) );
    okBtn.addSelectionListener( new SelectionAdapter() {

      @SuppressWarnings( "synthetic-access" )
      @Override
      public void widgetSelected( SelectionEvent aE ) {
        fireApplyEditorValue();
      }
    } );

    okBtn.addControlListener( new ControlAdapter() {

      @Override
      public void controlResized( ControlEvent aE ) {
        Point p = okBtn.getSize();
        okBtn.setSize( p.y, p.y );
      }

    } );

    return bkPanel;
  }

  @Override
  protected V doGetValue() {
    return valed.getValue();
  }

  @Override
  protected void doSetFocus() {
    valedControl.setFocus();
    Control ctrl = getControl();
    ctrl.removeTraverseListener( keyListener );
    ctrl.addTraverseListener( keyListener );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  protected void doSetValue( Object aValue ) {
    valed.setValue( (V)aValue );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return ctx;
  }
}
