package org.toxsoft.core.tsgui.m5.gui.mpc.impl;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.impl.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IMpcFilterPane} implementation that wraps over {@link IM5FilterPanel}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class MpcFilterPaneWrapper<T>
    extends MpcAbstractPane<T, Control>
    implements IMpcFilterPane<T> {

  private final GenericChangeEventer eventer;

  private final IM5FilterPanel<T> panel;

  boolean filterOn  = true; // filter on/off state flag, needed to work before widgets are created
  Button  btnFilter = null; // filter on/off switch button
  Button  btnClear  = null; // filter reset push button

  /**
   * Constructor.
   *
   * @param aOwner {@link MultiPaneComponent} - the owner component
   * @param aPanel {@link IM5FilterPanel} - panel to be wrapped
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException <code>aPanel</code> has already initialized {@link ILazyControl#getControl()}
   */
  public MpcFilterPaneWrapper( MultiPaneComponent<T> aOwner, IM5FilterPanel<T> aPanel ) {
    super( aOwner );
    TsNullArgumentRtException.checkNull( aPanel );
    TsIllegalArgumentRtException.checkTrue( aPanel.getControl() != null );
    eventer = new GenericChangeEventer( this );
    panel = aPanel;
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    TsComposite c = new TsComposite( aParent );
    GridLayout gridLayout = new GridLayout( 3, false );
    gridLayout.horizontalSpacing = 1;
    c.setLayout( gridLayout );
    // btnFilter
    btnFilter = new Button( c, SWT.CHECK );
    btnFilter.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false ) );
    btnFilter.setToolTipText( BTN_P_FILTER );
    ITsIconManager iconManager = owner().tsContext().get( ITsIconManager.class );
    Image icon = iconManager.loadStdIcon( ICONID_VIEW_FILTER, EIconSize.IS_16X16 ); // TODO what icon size to use?
    btnFilter.setImage( icon );
    btnFilter.setSelection( filterOn );
    btnFilter.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        eventer.fireChangeEvent();
      }
    } );
    // filter panel
    panel.createControl( c );
    panel.getControl().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
    panel.genericChangeEventer().addListener( aSource -> eventer.fireChangeEvent() );
    // btnClear
    btnClear = new Button( c, SWT.PUSH );
    btnClear.setLayoutData( new GridData( SWT.RIGHT, SWT.FILL, false, false ) );
    btnClear.setText( "<" ); //$NON-NLS-1$
    btnClear.setToolTipText( BTN_P_CLEAR );
    btnClear.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        panel.reset();
      }
    } );
    return c;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // IMpcFilterPane
  //

  @Override
  public boolean isFilterOn() {
    if( btnFilter != null ) {
      return btnFilter.getSelection();
    }
    return filterOn;
  }

  @Override
  public void setFilterOn( boolean aOn ) {
    if( btnFilter != null ) {
      btnFilter.setSelection( aOn );
    }
    else {
      filterOn = aOn;
    }
  }

  @Override
  public ITsFilter<T> getFilter() {
    ITsFilter<T> f = panel.getFilter();
    return (f != null) ? f : ITsFilter.ALL;
  }

}
