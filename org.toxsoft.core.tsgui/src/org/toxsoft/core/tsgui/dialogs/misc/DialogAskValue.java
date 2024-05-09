package org.toxsoft.core.tsgui.dialogs.misc;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Invokes dialog to ask single atomic value and some common values.
 *
 * @author hazard157
 */
public class DialogAskValue
    extends AbstractTsDialogPanel<IAtomicValue, IDataType> {

  private final IValedControl<IAtomicValue> valed;
  private final ITsValidator<IAtomicValue>  validator;

  protected DialogAskValue( Composite aParent, TsDialog<IAtomicValue, IDataType> aOwnerDialog,
      AskValueDialogInfo aInfo ) {
    super( aParent, aOwnerDialog );
    boolean hasLabel = !aInfo.label().isEmpty();
    this.setLayout( new GridLayout( hasLabel ? 2 : 1, false ) );
    // find the VALED factory
    ITsGuiContext ctxValed = new TsGuiContext( tsContext() );
    ctxValed.params().addAll( aInfo.params() );
    ValedControlFactoriesRegistry registry = tsContext().get( ValedControlFactoriesRegistry.class );
    if( registry == null ) {
      registry = new ValedControlFactoriesRegistry();
    }
    IValedControlFactory f = registry.getSuitableAvEditor( environ().atomicType(), ctxValed );
    // label (if needed)
    if( hasLabel ) {
      Label l = new Label( this, SWT.CENTER );
      l.setText( aInfo.label() );
    }
    // VALED
    valed = f.createEditor( ctxValed );
    valed.createControl( this );
    valed.getControl().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
    valed.eventer().addListener( notificationValedControlChangeListener );
    validator = aInfo.validator();
  }

  @Override
  protected ValidationResult validateData() {
    ValidationResult vr = valed.canGetValue();
    if( vr.isError() ) {
      return vr;
    }
    IAtomicValue value = valed.getValue();
    return ValidationResult.firstNonOk( vr, validator.validate( value ) );
  }

  @Override
  protected void doSetDataRecord( IAtomicValue aData ) {
    if( aData != null ) {
      valed.setValue( aData );
    }
    else {
      valed.clearValue();
    }
  }

  @Override
  protected IAtomicValue doGetDataRecord() {
    return valed.getValue();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Invokes atomic value edit dialog.
   *
   * @param aDialogInfo {@link AskValueDialogInfo} - the dialog window parameters
   * @param aValue {@link IAtomicValue} - initial value or <code>null</code>
   * @return {@link IAtomicValue} - edited value or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IAtomicValue askValue( AskValueDialogInfo aDialogInfo, IAtomicValue aValue ) {
    TsNullArgumentRtException.checkNull( aDialogInfo );
    IDialogPanelCreator<IAtomicValue, IDataType> creator = ( p, d ) -> new DialogAskValue( p, d, aDialogInfo );
    TsDialog<IAtomicValue, IDataType> d = new TsDialog<>( aDialogInfo, aValue, aDialogInfo.dataType(), creator );
    return d.execData();
  }

  /**
   * Invokes dialog to ask IDpath {@link String}.
   *
   * @param aDialogInfo {@link ITsDialogInfo} - the dialog window parameters
   * @param aLabelText String - label text or an empty string
   * @param aValue {@link IAtomicValue} - initial value or <code>null</code>
   * @return {@link IAtomicValue} - edited value or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static String askIdPath( ITsDialogInfo aDialogInfo, String aLabelText, String aValue ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aLabelText );
    AskValueDialogInfo di =
        new AskValueDialogInfo( aDialogInfo.tsContext(), aDialogInfo.caption(), aDialogInfo.title(), DDEF_IDPATH );
    di.setLabel( aLabelText );
    di.setValidator( DDEF_IDPATH.validator() );
    IAtomicValue av = askValue( di, aValue != null ? avStr( aValue ) : null );
    if( av != null ) {
      return av.asString();
    }
    return null;
  }

}
