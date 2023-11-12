package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2Point} editor with widgets {@link Spinner} for x and {@link Spinner} for y.
 *
 * @author vs
 */
public class ValedD2Point
    extends AbstractValedControl<ID2Point, Composite> {

  /**
   * ID of the {@link #OPDEF_ORIENTATION}.
   */
  public static final String OPID_ORIENTATION = VALED_OPID_PREFIX + ".D2PointEditor.Orientation"; //$NON-NLS-1$

  /**
   * ID of the {@link #OPDEF_WIDTH_HINT}.
   */
  public static final String OPID_WIDTH_HINT = VALED_OPID_PREFIX + ".D2PointEditor.WidthHint"; //$NON-NLS-1$

  /**
   * Orientation of controls (place it in row or column).
   */
  public static final IDataDef OPDEF_ORIENTATION = DataDef.create( OPID_ORIENTATION, VALOBJ, //
      TSID_NAME, STR_N_D2POINT_ORIENTATION, //
      TSID_DESCRIPTION, STR_D_D2POINT_ORIENTATION, //
      TSID_KEEPER_ID, ETsOrientation.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETsOrientation.HORIZONTAL ) //
  );

  /**
   * Preferred width for spinner width.
   */
  public static final IDataDef OPDEF_WIDTH_HINT = DataDef.create( OPID_WIDTH_HINT, INTEGER, //
      TSID_NAME, STR_N_WIDTH_HINT, //
      TSID_DESCRIPTION, STR_D_WIDTH_HINT, //
      TSID_DEFAULT_VALUE, avInt( 60 ) //
  );

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".D2PointEditor"; //$NON-NLS-1$

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
    protected IValedControl<ID2Point> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedD2Point( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( D2Point.class ) //
          || aValueClass.equals( D2PointEdit.class ) //
          || aValueClass.equals( ID2Point.class ) //
          || aValueClass.equals( ID2PointEdit.class );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  private Composite bkPanel = null;

  private ValedAvFloatSpinner xSpinner;

  private ValedAvFloatSpinner ySpinner;

  private ID2Point point = null;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ValedD2Point( ITsGuiContext aContext ) {
    super( aContext );

  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Composite doCreateControl( Composite aParent ) {
    bkPanel = new Composite( aParent, SWT.NONE );
    GridLayout gl;
    if( params().getValobj( OPDEF_ORIENTATION ) == ETsOrientation.VERTICAL ) {
      gl = new GridLayout( 2, false );
    }
    else {
      gl = new GridLayout( 4, false );
    }
    gl.marginLeft = 0;
    gl.marginTop = 0;
    gl.marginRight = 0;
    gl.marginBottom = 0;
    gl.marginHeight = 0;
    gl.marginWidth = 0;
    bkPanel.setLayout( gl );

    CLabel l = new CLabel( bkPanel, SWT.CENTER );
    l.setText( "x:" ); //$NON-NLS-1$
    xSpinner = new ValedAvFloatSpinner( tsContext() );
    Control ctrl = xSpinner.createControl( bkPanel );
    GridData gd = new GridData( SWT.FILL, SWT.TOP, true, false );
    gd.widthHint = params().getInt( OPDEF_WIDTH_HINT );
    ctrl.setLayoutData( gd );

    l = new CLabel( bkPanel, SWT.CENTER );
    l.setText( "y:" ); //$NON-NLS-1$
    ySpinner = new ValedAvFloatSpinner( tsContext() );
    ctrl = ySpinner.createControl( bkPanel );
    gd = new GridData( SWT.FILL, SWT.TOP, true, false );
    gd.widthHint = params().getInt( OPDEF_WIDTH_HINT );
    ctrl.setLayoutData( gd );

    return bkPanel;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    xSpinner.getControl().setEnabled( aEditable );
    ySpinner.getControl().setEnabled( aEditable );
  }

  @Override
  protected ID2Point doGetUnvalidatedValue() {
    return new D2Point( xSpinner.getValue().asDouble(), ySpinner.getValue().asDouble() );
  }

  @Override
  protected void doSetUnvalidatedValue( ID2Point aValue ) {
    point = new D2Point( aValue.x(), aValue.y() );
    updateSpinners();
  }

  @Override
  protected void doClearValue() {
    point = null;
    updateSpinners();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void updateSpinners() {
    if( point != null ) {
      xSpinner.setValue( avFloat( point.x() ) );
      ySpinner.setValue( avFloat( point.y() ) );
    }
    else {
      xSpinner.clearValue();
      xSpinner.clearValue();
    }
  }

}
