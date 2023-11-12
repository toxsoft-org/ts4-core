package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2Angle} editor with {@link Spinner} and {@link Combo} widgets.
 *
 * @author vs
 */
public class ValedD2Angle
    extends AbstractValedControl<ID2Angle, Composite> {

  private static final String STR_DEGREES = "(180, 360)°"; //$NON-NLS-1$
  private static final String STR_RADIANS = "(π, 2π) rad"; //$NON-NLS-1$

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".D2AngleEditor"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author vs
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<ID2Angle> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedD2Angle( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( D2Angle.class ) //
          || aValueClass.equals( D2AngleEdit.class ) //
          || aValueClass.equals( ID2Angle.class ) //
          || aValueClass.equals( ID2AngleEdit.class );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  private Composite bkPanel = null;

  private ValedAvFloatSpinner valueSpinner;

  private ValedComboSelector<String> unitCombo;

  private D2AngleEdit angle = new D2AngleEdit();

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ValedD2Angle( ITsGuiContext aContext ) {
    super( aContext );

  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Composite doCreateControl( Composite aParent ) {
    bkPanel = new Composite( aParent, SWT.NONE );
    GridLayout gd = new GridLayout( 2, false );
    gd.marginLeft = 0;
    gd.marginTop = 0;
    gd.marginRight = 0;
    gd.marginBottom = 0;
    gd.marginHeight = 0;
    gd.marginWidth = 0;
    bkPanel.setLayout( gd );

    valueSpinner = new ValedAvFloatSpinner( tsContext() );
    Control ctrlSpinner = valueSpinner.createControl( bkPanel );
    ctrlSpinner.setToolTipText( DLG_T_ANGLE_VALUE );
    ctrlSpinner.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    valueSpinner.setValue( avFloat( angle.degrees() ) );

    IList<String> items = new ElemArrayList<>( STR_DEGREES, STR_RADIANS );
    unitCombo = new ValedComboSelector<String>( tsContext(), items, ITsVisualsProvider.DEFAULT );
    Combo ctrlCombo = (Combo)unitCombo.createControl( bkPanel );
    ctrlCombo.setToolTipText( DLG_T_ANGLE_UNIT );
    unitCombo.setSelectedItem( items.first() );
    ctrlCombo.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        updateValue();
      }
    } );

    return bkPanel;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    valueSpinner.getControl().setEnabled( aEditable );
    unitCombo.getControl().setEnabled( aEditable );
  }

  @Override
  protected ID2Angle doGetUnvalidatedValue() {
    if( unitCombo.selectedItem().equals( STR_DEGREES ) ) {
      D2Angle.ofDegrees( valueSpinner.getValue().asDouble() );
    }
    return D2Angle.ofRadians( valueSpinner.getValue().asDouble() );
  }

  @Override
  protected void doSetUnvalidatedValue( ID2Angle aValue ) {
    angle.setDeg( aValue.degrees() );
    updateValue();
  }

  @Override
  protected void doClearValue() {
    angle.setDeg( 0 );
    updateValue();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void updateValue() {
    if( unitCombo.selectedItem().equals( STR_DEGREES ) ) {
      valueSpinner.setValue( avFloat( angle.degrees() ) );
    }
    else {
      valueSpinner.setValue( avFloat( angle.radians() ) );
    }
  }

}
