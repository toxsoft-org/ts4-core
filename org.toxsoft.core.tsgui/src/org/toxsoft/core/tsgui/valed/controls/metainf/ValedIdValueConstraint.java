package org.toxsoft.core.tsgui.valed.controls.metainf;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.strid.impl.StridUtils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.metainfo.constr.*;
import org.toxsoft.core.tslib.av.misc.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VALED to edit {@link IdValue} as the single {@link IDataType#params()} known constraint.
 * <p>
 * "<i>Known</i>" constraint means one of the constraints from {@link ConstraintUtils#listConstraints()}. The VALED does
 * <b>not</b> allows to select any other constraint ID.
 * <p>
 * FIXME Contains following controls:
 * <ul>
 * <li>constraint ID selector as a text line with button at right. Text line allows to enter arbitrary ID path as a
 * constraint ID, button allows to select constraint from {@link ConstraintUtils#listConstraints()}. Note: we do not use
 * drop-down combo here because in the future IDs selection list will be replaces categorized tree in dialog;</li>
 * <li>constraint value editor as a FIXME ??? text line with button at right;</li>
 * <li>.</li>
 * </ul>
 * <p>
 * TODO describe usage
 *
 * @author hazard157
 */
public class ValedIdValueConstraint
    extends AbstractValedControl<IdValue, Control> {

  /**
   * The registered factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".ValedIdValueConstraint"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IdValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedIdValueConstraint( aContext );
    }

  };

  /**
   * Constraint ID selector.
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
        if( usedConstrIds.hasElem( cinf.id() ) ) {
          continue; // do not include constraints already in the data type
        }
        if( !cinf.listApplicableDataTypes().hasElem( dataAtomicType ) ) {
          continue; // do not include constraints not applicable to the data type
        }
        llConstrs.add( cinf );
      }
      return llConstrs;
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

  private final ValedConstraintId                valedConstrId;
  private final IValedControlValueChangeListener childValedListener = ( src, finished ) -> fireModifyEvent( finished );
  private final IStringListEdit                  usedConstrIds      = new StringArrayList();

  private EAtomicType dataAtomicType = EAtomicType.NONE;
// TODO  private IOptionSetEdit constraintsList = new OptionSet();

  private IValedControl<IAtomicValue> valedConstrValue = null; // FIXME null

  /**
   * {@link Composite} holds VALED to edit constraint value, recreated each time when atomic type changes.
   */
  private Composite valueValedHolder;

  protected ValedIdValueConstraint( ITsGuiContext aContext ) {
    super( aContext );
    valedConstrId = new ValedConstraintId( tsContext() );
    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Recreates {@link #valedConstrValue} in the {@link #valueValedHolder}.
   * <p>
   * Concrete class of the created VALED depends on the constraint atomic type and {@link ConstraintInfo} if any.
   */
  private void reinitValueValed() {
    // remove previous value VALED if any
    if( valedConstrValue != null ) {
      valedConstrValue.eventer().removeListener( childValedListener );
      valedConstrValue.getControl().dispose();
      valedConstrValue = null;
    }
    // find constraint info (this code must always succeed to get non-null #constrInfo)
    String constrId = EMPTY_STRING;
    if( !valedConstrId.canGetValue().isError() ) {
      constrId = valedConstrId.getValue();
    }
    IConstraintInfo constrInfo = ConstraintUtils.findConstraintInfo( constrId );
    if( constrInfo == null ) { // this must NOT happen, just catching programmin errors
      TsDialogUtils.warn( getShell(), "Unknown constraint ID '%s'", constrId );
      return;
    }
    ITsGuiContext valedContext = new TsGuiContext( tsContext() );
    // if only lookup values may be selected use enumeration combo selector
    if( constrInfo.isOnlyLookupValuesAllowed() ) {
      ITsVisualsProvider<IAtomicValue> vp = TsVisualsProviderWrapper.wrap( constrInfo.lookupValuesNameProvider() );
      valedConstrValue = new ValedComboSelector<>( valedContext, constrInfo.listLookupValues(), vp );
      valedConstrValue.createControl( valueValedHolder );
      valedConstrValue.getControl().setLayoutData( new BorderData( SWT.CENTER ) );
      valedConstrValue.eventer().addListener( childValedListener );
      return;
    }
    // determine constraint atomic type
    EAtomicType constrAtomicType;
    if( constrInfo.isConstraintTypeSameAsDataType() ) {
      constrAtomicType = dataAtomicType;
    }
    else {
      constrAtomicType = constrInfo.constraintType();
    }
    // create VALED
    if( constrAtomicType == EAtomicType.VALOBJ ) {
      valedContext.params().setStr( TSID_KEEPER_ID, constrInfo.valobjValueKeeperId() );
    }
    valedConstrValue = ValedControlUtils.createAvValedControl( constrAtomicType, valedContext );
    valedConstrValue.createControl( valueValedHolder );
    valedConstrValue.getControl().setLayoutData( new BorderData( SWT.CENTER ) );
    valedConstrValue.eventer().addListener( childValedListener );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    Composite backplane = new Composite( aParent, SWT.BORDER );
    backplane.setLayout( new GridLayout( 2, false ) );
    // valedConstrId
    CLabel l = new CLabel( backplane, SWT.CENTER );
    l.setLayoutData( new GridData() );
    l.setText( "ID: " );
    l.setText( "Enter manually or select from list the ID of the constraint to specify" );
    valedConstrId.createControl( backplane );
    valedConstrId.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    // valedConstrValue
    l.setLayoutData( new GridData() );
    l.setText( "Value: " );
    l.setText( "Enter manually or select from lookup list the the constraint value" );
    valueValedHolder = new Composite( backplane, SWT.NONE );
    valueValedHolder.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    valueValedHolder.setLayout( new BorderLayout() );

    // setup
    valedConstrId.eventer().addListener( childValedListener );
    // TODO valedConstrValue.eventer().addListener( listener );

    reinitValueValed();
    return backplane;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    valedConstrId.setEditable( aEditable );
    if( valedConstrValue != null ) {
      valedConstrValue.setEditable( aEditable );
    }
  }

  @Override
  protected ValidationResult doCanGetValue() {

    // TODO ValedIdValueConstraint.doCanGetValue()

    return super.doCanGetValue();
  }

  @Override
  protected IdValue doGetUnvalidatedValue() {
    String id = valedConstrId.getValue();
    IAtomicValue av = valedConstrValue.getValue();
    return new IdValue( id, av );
  }

  @Override
  protected void doSetUnvalidatedValue( IdValue aValue ) {
    valedConstrId.setValue( aValue.id() );
    valedConstrValue.setValue( aValue.value() );
    // TODO refresh view
  }

  @Override
  protected void doClearValue() {
    valedConstrId.setValue( EMPTY_STRING );
    if( valedConstrValue != null ) {
      valedConstrValue.setValue( IAtomicValue.NULL );
    }

  }

  // ------------------------------------------------------------------------------------
  // VALED specific API
  //

  /**
   * Sets the atomic type of the {@link IDataType} for which the constraint is to be created.
   *
   * @param aAtomicType {@link EAtomicType} - data atomic type {@link IDataType#atomicType()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setDataAtomicType( EAtomicType aAtomicType ) {
    TsNullArgumentRtException.checkNull( aAtomicType );
    if( dataAtomicType != aAtomicType ) {
      dataAtomicType = aAtomicType;
      // TODO refresh view
    }
  }

  /**
   * Informs VALED which constraints already exists in {@link IDataType#params()}.
   * <p>
   * Constrains with used IDs are excluded from the available constraint IDs .
   *
   * @param aUsedIds {@link IStringList} - constraint IDs already existing in {@link IDataType#params()}
   */
  public void setExistingConstraintIds( IStringList aUsedIds ) {
    TsNullArgumentRtException.checkNull( aUsedIds );
    usedConstrIds.setAll( aUsedIds );
    // TODO refresh view
  }

}
