package org.toxsoft.core.tsgui.valed.controls.metainf;

import static org.toxsoft.core.tslib.bricks.strid.impl.StridUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.metainfo.constr.*;
import org.toxsoft.core.tslib.av.misc.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VALED to edit {@link IdValue} representation of the single {@link IDataType#params()} constraint.
 * <p>
 * Contains two controls:
 * <ul>
 * <li>constraint ID selector as a text line with button at right. Text line allows to enter arbitrary ID path as a
 * constraint ID, button allows to select constraint from {@link ConstraintUtils#linkConstraints()}. Note: we do not use
 * drop-down combo here because in the future IDs selection list will be replaces categorized tree in dialog;</li>
 * <li>constraint value editor as a FIXME ??? text line with button at right. .</li>
 * </ul>
 * <p>
 * TODO describe usage
 *
 * @author hazard157
 */
public class ValedIdValueConstraint
    extends AbstractValedControl<IdValue, Control> {

  /**
   * Constraint ID selector.
   *
   * @author hazard157
   */
  private class ValedConstraintId
      extends AbstractValedTextAndButton<String> {

    private ITsNameProvider<String> itemNameProvider = aItem -> {
      IConstraintInfo cinf = ConstraintUtils.linkConstraints().findByKey( aItem );
      return cinf != null ? printf( FORMAT_NAME_ID, cinf ) : aItem;
    };

    ValedConstraintId( ITsGuiContext aContext ) {
      super( aContext );
    }

    @Override
    protected boolean doProcessButtonPress() {
      ITsDialogInfo di = TsDialogInfo.forSelectEntity( tsContext() );
      IStringListEdit items = new StringLinkedBundleList();
      items.addAll( ConstraintUtils.linkConstraints().keys() );
      TsCollectionsUtils.subtract( items, usedConstrIds );
      String sel = getTextControl().getText();
      String constrId = DialogItemsList.select( di, items, sel, itemNameProvider );
      if( constrId != null && !constrId.equals( sel ) ) {
        getTextControl().setText( constrId );
        // TODO refresh for correct value editing
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
  private final IValedControlValueChangeListener listener = ( src, finished ) -> fireModifyEvent( finished );

  private EAtomicType     dataAtomicType = EAtomicType.NONE;
  private IStringListEdit usedConstrIds  = new StringArrayList();

  protected ValedIdValueConstraint( ITsGuiContext aContext ) {
    super( aContext );
    valedConstrId = new ValedConstraintId( tsContext() );
    // TODO Auto-generated constructor stub
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

    // TODO Auto-generated method stub

    // setup
    valedConstrId.eventer().addListener( listener );
    // TODO valedConstrValue.eventer().addListener( listener );
    return backplane;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // TODO Auto-generated method stub

  }

  @Override
  protected IdValue doGetUnvalidatedValue() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void doSetUnvalidatedValue( IdValue aValue ) {
    // TODO Auto-generated method stub

  }

  @Override
  protected void doClearValue() {
    // TODO Auto-generated method stub

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
