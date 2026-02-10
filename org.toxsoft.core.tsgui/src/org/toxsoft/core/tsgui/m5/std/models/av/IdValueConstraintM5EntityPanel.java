package org.toxsoft.core.tsgui.m5.std.models.av;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.std.models.av.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.strid.impl.StridUtils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.constr.*;
import org.toxsoft.core.tslib.av.misc.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5EntityPanel} returned by model {@link IdValueConstraintM5Model#panelCreator()}.
 *
 * @author hazard157
 */
class IdValueConstraintM5EntityPanel
    extends M5AbstractEntityPanel<IdValue> {

  /**
   * Constraint ID selector VALED.
   *
   * @author hazard157
   */
  private class ValedConstraintId
      extends AbstractValedTextAndButton<String> {

    private ITsNameProvider<String> itemNameProvider = aItem -> {
      IConstraintInfo cinf = ConstraintUtils.listConstraints().findByKey( aItem );
      return cinf != null ? printf( FORMAT_NAME_ID, cinf ) : aItem;
    };

    ValedConstraintId( ITsGuiContext aContext ) {
      super( aContext, true );
    }

    private IStridablesList<IConstraintInfo> listApplicableConstraints() {
      IStridablesListEdit<IConstraintInfo> llConstrs = new StridablesList<>();
      for( IConstraintInfo cinf : ConstraintUtils.listConstraints() ) {
        if( lmMaster().constraints().hasKey( cinf.id() ) ) {
          continue; // do not include constraints already in the data type
        }
        if( !cinf.listApplicableDataTypes().hasElem( lmMaster().atomicType().get() ) ) {
          continue; // do not include constraints not applicable to the data type
        }
        llConstrs.add( cinf );
      }
      return llConstrs;
    }

    @Override
    protected void doAfterControlCreated() {
      getButtonControl().setToolTipText( STR_BTN_SELECT_CONSTRAINT );
    }

    @Override
    protected boolean doProcessButtonPress() {
      ITsDialogInfo di = TsDialogInfo.forSelectEntity( tsContext() );
      IStringListEdit items = new StringLinkedBundleList();
      items.addAll( listApplicableConstraints().ids() );
      String sel = getTextControl().getText();
      String constrId = DialogItemsList.select( di, items, sel, itemNameProvider );
      if( constrId != null && !constrId.equals( sel ) ) {
        getTextControl().setText( constrId );
        return true;
      }
      return false;
    }

    @Override
    protected void doDoSetUnvalidatedValue( String aValue ) {
      getTextControl().setText( aValue );
    }

    @Override
    protected String doGetUnvalidatedValue() {
      return getTextControl().getText();
    }

  }

  private final IValedControlValueChangeListener valedChangeListener = ( aSource, aEditFinished ) -> fireChangeEvent();

  private final ValedConstraintId valedId;

  private Composite            backplane             = null;
  private Text                 textKnownConstrInfo   = null;
  private IValedControlFactory lastValueValedFactory = null;

  private IValedControl<IAtomicValue> valedValue;     // value VALED recreated when constraint ID changes
  private Composite                   holdValedValue; // placeholder for #valedValue in the grid layout

  public IdValueConstraintM5EntityPanel( ITsGuiContext aContext, IM5Model<IdValue> aModel, boolean aViewer ) {
    super( aContext, aModel, aViewer );
    IdValueConstraintM5Model.class.cast( aModel );
    valedId = new ValedConstraintId( new TsGuiContext( tsContext() ) );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private IdValueConstraintM5LifecycleManager.Master lmMaster() {
    return ((IdValueConstraintM5LifecycleManager)lifecycleManager()).master();
  }

  private IdValueConstraintM5Model ivcModel() {
    return (IdValueConstraintM5Model)super.model();
  }

  /**
   * Updates text in {@link #textKnownConstrInfo} according to current value in {@link #valedId}.
   */
  private void updateKnownConstraintInfoLabel() {
    // determine if #valedId contains known constraint ID
    String id = EMPTY_STRING;
    if( !valedId.canGetValue().isError() ) {
      id = valedId.getValue();
    }
    IConstraintInfo cinf = ConstraintUtils.findConstraintInfo( id );
    // update text
    String msg = STR_UNKNOWN_CONSTRAINT_INFO_TEXT;
    if( cinf != null ) {
      msg = cinf.nmName() + '\n' + cinf.description();
    }
    textKnownConstrInfo.setText( msg );
    backplane.layout( true, true );
  }

  /**
   * Determines which {@link #valedValue} factory to use for known constraint.
   *
   * @param aInfo {@link IConstraintInfo} - known constraint information
   * @param aValedContext {@link ITsGuiContext} - VALED creation context, may be changed by this method
   * @return {@link IValedControlFactory} - found factory
   */
  private IValedControlFactory chooseValueValedFactory( IConstraintInfo aInfo, ITsGuiContext aValedContext ) {
    EAtomicType atomicType;
    if( aInfo.isConstraintTypeSameAsDataType() ) {
      atomicType = lmMaster().atomicType().get();
      if( atomicType == EAtomicType.VALOBJ ) {
        // TOSO set keeper ID from data type
        String keeperId = EMPTY_STRING;
        if( lmMaster().constraints().hasKey( TSID_KEEPER_ID ) ) {
          keeperId = lmMaster().constraints().getStr( TSID_KEEPER_ID );
          aValedContext.params().setStr( TSID_KEEPER_ID, keeperId );
        }
      }
    }
    else {
      atomicType = aInfo.constraintType();
      if( atomicType == EAtomicType.VALOBJ ) {
        if( !aInfo.valobjValueKeeperId().isEmpty() ) {
          aValedContext.params().setStr( TSID_KEEPER_ID, aInfo.valobjValueKeeperId() );
        }
      }
    }

    // TODO IdValueConstraintM5EntityPanel.chooseValueValedFactory() - add more logic:
    // process lookup value selection

    IValedControlFactoriesRegistry vcfReg = tsContext().get( IValedControlFactoriesRegistry.class );
    return vcfReg.getSuitableAvEditor( atomicType, aValedContext );
  }

  /**
   * Re-creates {@link #valedValue} according to current value in {@link #valedId}.
   * <p>
   * If {@link #valedId} contains known constraint ID, determines class of VALED to be created from
   * {@link IConstraintInfo}, for unknown constraints {@link ValedAvAnytypeText} is used.
   */
  private void recreateValueValedIfNeeded() {
    // determine if #valedId contains known constraint ID
    String id = EMPTY_STRING;
    if( !valedId.canGetValue().isError() ) {
      id = valedId.getValue();
    }
    IConstraintInfo cinf = ConstraintUtils.findConstraintInfo( id );
    // determine which factory to use
    IValedControlFactory factory;
    ITsGuiContext valedContext = new TsGuiContext( tsContext() );
    // for unknown constraint create ValedAvAnytypeText
    if( cinf != null ) {
      factory = chooseValueValedFactory( cinf, valedContext );
    }
    else {
      factory = ValedAvAnytypeText.FACTORY;
    }
    // recreate VALED if needed
    if( !Objects.equals( factory, lastValueValedFactory ) ) {
      backplane.setLayoutDeferred( false );
      try {
        if( valedValue != null ) {
          valedValue.eventer().removeListener( valedChangeListener );
          valedValue.getControl().dispose();
        }
        valedValue = factory.createEditor( valedContext );
        valedValue.createControl( holdValedValue );
        valedValue.getControl().setLayoutData( new BorderData( SWT.CENTER ) );
        valedValue.eventer().addListener( valedChangeListener );
      }
      finally {
        backplane.layout( true, true );
        backplane.setLayoutDeferred( false );
      }
      lastValueValedFactory = factory;
    }
  }

  private void whenOnConstraintIdChange() {
    updateKnownConstraintInfoLabel();
    recreateValueValedIfNeeded();
  }

  // ------------------------------------------------------------------------------------
  // M5AbstractEntityPanel
  //

  @Override
  public void setLifecycleManager( IM5LifecycleManager<IdValue> aLifecycleManager ) {
    IdValueConstraintM5LifecycleManager.class.cast( aLifecycleManager );
    super.setLifecycleManager( aLifecycleManager );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    backplane = new Composite( aParent, SWT.NONE );
    backplane.setLayout( new GridLayout( 2, false ) );
    // valedId
    CLabel l = new CLabel( backplane, SWT.LEFT );
    l.setText( STR_LABEL_ID );
    l.setToolTipText( STR_LABEL_ID_D );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false ) );
    valedId.createControl( backplane );
    valedId.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    // holdValedValue
    l = new CLabel( backplane, SWT.LEFT );
    l.setText( STR_LABEL_VALUE );
    l.setToolTipText( STR_LABEL_VALUE_D );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false ) );
    holdValedValue = new Composite( backplane, SWT.NONE );
    holdValedValue.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    holdValedValue.setLayout( new BorderLayout() );
    // labelKnownConstrInfo
    l = new CLabel( backplane, SWT.LEFT );
    l.setText( STR_LABEL_KNOWN_CONSTRAINT_INFO );
    l.setToolTipText( STR_LABEL_KNOWN_CONSTRAINT_INFO_D );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false ) );
    textKnownConstrInfo = new Text( backplane, SWT.LEFT | SWT.BORDER | SWT.WRAP );
    textKnownConstrInfo.setEditable( false );
    textKnownConstrInfo.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, true ) );
    // setup
    valedId.eventer().addListener( ( src, finished ) -> whenOnConstraintIdChange() );
    valedId.eventer().addListener( valedChangeListener );
    whenOnConstraintIdChange();
    return backplane;
  }

  @Override
  protected ValidationResult doCollectValues( IM5BunchEdit<IdValue> aBunch ) {
    // creak if ID and value can be read from VALEDs
    ValidationResult vr = valedId.canGetValue();
    if( vr.isError() ) {
      return vr;
    }
    if( valedValue == null ) {
      throw new TsInternalErrorRtException();
    }
    vr = ValidationResult.firstNonOk( vr, valedValue.canGetValue() );
    if( vr.isError() ) {
      return vr;
    }
    // update bunch
    String id = valedId.getValue();
    IAtomicValue av = valedValue.getValue();
    aBunch.set( ivcModel().ID, avStr( id ) );
    aBunch.set( ivcModel().VALUE, av );
    return vr;
  }

  @Override
  protected void doSetValues( IM5Bunch<IdValue> aBunch ) {
    String id = aBunch.getAsAv( FID_ID ).toString();
    valedId.setValue( id );
    whenOnConstraintIdChange();
    IAtomicValue av = aBunch.getAsAv( IdValueConstraintM5Model.FID_VALUE );
    valedValue.setValue( av );
  }

}
