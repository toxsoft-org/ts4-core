package org.toxsoft.core.tsgui.valed.controls.metainf;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.metainf.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VALED edits {@link IDataType} as a whole.
 * <p>
 * Contains {@link IDataType#atomicType()} selection combo box and table of the constraints {@link IDataType#params()}.
 *
 * @author hazard157
 */
public class ValedDataType
    extends AbstractValedControl<IDataType, Control> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".DataType"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IDataType> doCreateEditor( ITsGuiContext aContext ) {

      // TODO реализовать ValedDataType.Factory.doCreateEditor()
      throw new TsUnderDevelopmentRtException( "ValedDataType.Factory.doCreateEditor()" );

      // IValedControl<IDataType> vc;
      // switch( OPDEF_VALED_UI_OUTFIT.getValue( aContext.params() ).asString() ) {
      // case VALED_UI_OUTFIT_EMBEDDABLE: {
      // // TODO ValedDataType.Factory.doCreateEditor()
      // vc = new ValedDataTypeEmbeddable( aContext );
      // TsTestUtils.pl( "Embed" );
      // }
      // //$FALL-THROUGH$
      // case VALED_UI_OUTFIT_SINGLE_LINE:
      // default: {
      // // TODO ValedDataType.Factory.doCreateEditor()
      // vc = new ValedDataTypeEmbeddable( aContext );
      // TsTestUtils.pl( "Line" );
      // }
      // }
      // return vc;
    }

  }

  private EAtomicType    atomicType  = EAtomicType.NONE;
  private IOptionSetEdit constraints = new OptionSet();

  private final ValedEnumCombo<EAtomicType> valedAtomicType;

  protected ValedDataType( ITsGuiContext aContext ) {
    super( aContext );
    valedAtomicType = new ValedEnumCombo<>( aContext, EAtomicType.class,
        item -> StridUtils.printf( StridUtils.FORMAT_NAME_ID, item ) );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    Composite backplane = new Composite( aParent, SWT.NONE );
    backplane.setLayout( new GridLayout( 2, false ) );
    // valedAtomicType
    CLabel label = new CLabel( backplane, SWT.CENTER );
    label.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    label.setText( STR_LBL_ATOMIC_TYPE );
    label.setToolTipText( STR_LBL_ATOMIC_TYPE_D );
    valedAtomicType.createControl( backplane );
    valedAtomicType.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    // TODO Auto-generated method stub

    // setup
    valedAtomicType.eventer().addListener( childValedsListener );

    return backplane;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    valedAtomicType.setEditable( aEditable );

    // TODO Auto-generated method stub

  }

  @Override
  protected ValidationResult doCanGetValue() {
    // TODO check constraints is valid for selected atomic type
    return super.doCanGetValue();
  }

  @Override
  protected IDataType doGetUnvalidatedValue() {
    IDataType dt = new DataType( atomicType, constraints );
    return dt;
  }

  @Override
  protected void doSetUnvalidatedValue( IDataType aValue ) {
    atomicType = aValue.atomicType();
    constraints.setAll( aValue.params() );
  }

  @Override
  protected void doClearValue() {
    atomicType = EAtomicType.NONE;
    constraints.clear();
  }

}
