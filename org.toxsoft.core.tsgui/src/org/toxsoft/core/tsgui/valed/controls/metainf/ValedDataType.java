package org.toxsoft.core.tsgui.valed.controls.metainf;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.metainf.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.std.models.av.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.misc.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;

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
      return new ValedDataType( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      if( aValueClass.equals( IDataType.class ) ) {
        return true;
      }
      return false;
    }

  }

  /**
   * The factory singleton.
   */
  public static final IValedControlFactory FACTORY = new Factory();

  private IOptionSetEdit constraintsList = new OptionSet();

  private final ValedEnumCombo<EAtomicType> valedAtomicType;
  private final IM5CollectionPanel<IdValue> panelConstraints;

  protected ValedDataType( ITsGuiContext aContext ) {
    super( aContext );
    valedAtomicType = new ValedEnumCombo<>( aContext, EAtomicType.class,
        item -> StridUtils.printf( StridUtils.FORMAT_NAME_ID, item ) );
    IM5Model<IdValue> model = m5().getModel( IdValueConstraintM5Model.MODEL_ID, IdValue.class );
    IM5LifecycleManager<IdValue> lm = model.getLifecycleManager(
        new IdValueConstraintM5LifecycleManager.Master( () -> valedAtomicType.getValue(), constraintsList ) );
    panelConstraints = model.panelCreator().createCollEditPanel( aContext, lm.itemsProvider(), lm );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void whenPanelConstraintsChanged() {
    constraintsList.clear();
    for( IdValue idval : panelConstraints.items() ) {
      constraintsList.put( idval.id(), idval.value() );
    }
  }

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
    // panelConstraints
    panelConstraints.createControl( backplane );
    panelConstraints.getControl().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 2, 15 ) );
    // setup
    valedAtomicType.eventer().addListener( childValedsListener );
    panelConstraints.genericChangeEventer().addListener( s -> whenPanelConstraintsChanged() );
    panelConstraints.genericChangeEventer().addListener( widgetValueChangeListener );
    return backplane;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    valedAtomicType.setEditable( aEditable );
    panelConstraints.setEditable( aEditable );
  }

  @Override
  protected ValidationResult doCanGetValue() {
    // TODO check constraints is valid for selected atomic type
    return super.doCanGetValue();
  }

  @Override
  protected IDataType doGetUnvalidatedValue() {
    IDataType dt = new DataType( valedAtomicType.getValue(), constraintsList );
    return dt;
  }

  @Override
  protected void doSetUnvalidatedValue( IDataType aValue ) {
    valedAtomicType.setValue( aValue.atomicType() );
    constraintsList.setAll( aValue.params() );
    panelConstraints.refresh();
  }

  @Override
  protected void doClearValue() {
    valedAtomicType.setValue( EAtomicType.NONE );
    constraintsList.clear();
    panelConstraints.refresh();
  }

}
