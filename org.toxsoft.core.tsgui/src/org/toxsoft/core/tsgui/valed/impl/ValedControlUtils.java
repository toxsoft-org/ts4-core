package org.toxsoft.core.tsgui.valed.impl;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Helper methods to work with VALEDs.
 *
 * @author hazard157
 */
public class ValedControlUtils {

  /**
   * Helper method to crate atomic value VALED.
   * <p>
   * <b>Important:</b> argument <code>aContext</code> must be the new instance created for the VALED. Content of the
   * context may be changed by the method.
   *
   * @param aDataType {@link IDataType} - the data type to edit
   * @param aContext {@link ITsGuiContext} - the editor context
   * @return {@link IValedControl} - atomic value editor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsRuntimeException VALED can ot be created
   */
  public static IValedControl<IAtomicValue> createAvValedControl( IDataType aDataType, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aDataType, aContext );
    aContext.params().addAll( aDataType.params() );
    IValedControlFactoriesRegistry vcfReg = aContext.get( IValedControlFactoriesRegistry.class );
    IValedControlFactory f = vcfReg.getSuitableAvEditor( aDataType.atomicType(), aContext );
    return f.createEditor( aContext );
  }

  /**
   * Returns the default factory name for the specified atomic type.
   *
   * @param aAtomicType {@link EAtomicType} - the atomic type
   * @return String - name of the editor factory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static String getDefaultFactoryName( EAtomicType aAtomicType ) {
    TsNullArgumentRtException.checkNull( aAtomicType );
    return getDefaultFactory( aAtomicType ).factoryName();
  }

  /**
   * Returns the default factory for the specified atomic type.
   *
   * @param aAtomicType {@link EAtomicType} - the atomic type
   * @return {@link AbstractValedControlFactory} - the factory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static AbstractValedControlFactory getDefaultFactory( EAtomicType aAtomicType ) {
    TsNullArgumentRtException.checkNull( aAtomicType );
    return switch( aAtomicType ) {
      case NONE -> ValedAvAnytypeText.FACTORY;
      case BOOLEAN -> ValedAvBooleanCheck.FACTORY;
      case INTEGER -> ValedAvIntegerSpinner.FACTORY;
      case FLOATING -> ValedAvFloatSpinner.FACTORY;
      case TIMESTAMP -> ValedAvTimestampMpv.FACTORY;
      case STRING -> ValedAvStringText.FACTORY;
      case VALOBJ -> ValedAvValobjRoText.FACTORY;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  /**
   * Wraps embeddable instance of the VALED into the modal dialog.
   * <p>
   * Invokes modal dialog with CANCEL and OK buttons. Content of dialog is the VALED control created by the the
   * specified factory with {@link IValedControlConstants#OPDEF_IS_SINGLE_LINE_UI} set to <code>false</code>. OK button
   * will be disabled if {@link IValedControl#canGetValue()} returns an error.
   * <p>
   * Returns pair where {@link Pair#left()} is edited value and {@link Pair#right()} indicates if edit was finished or
   * cancelled. As the VALED may return <code>null</code> as a valid edited value, we need a way to determine if edit
   * was canceled (eg. user pressed CANCEL button in dialog box). {@link Pair#right()} is {@link Boolean#TRUE} only when
   * user closes dialog box with OK button. When dialog was cancelled {@link Pair#left()} contains the initial
   * <code>aValue</code>.
   * <p>
   * As a VALED creation context {@link ITsDialogInfo#tsContext()} is used.
   *
   * @param <V> - the edited value type
   * @param aValue &lt;V&gt; - initial value, may be <code>null</code>
   * @param aVcFactory {@link IValedControlFactory} - factory for VALED creation
   * @param aDialogInfo {@link ITsDialogInfo} - dialog box settings
   * @return {@link Pair}&lt;V,{@link Boolean}&gt; - pair "edited value" - "edit OK flag"
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <V> Pair<V, Boolean> invokeAsModalDialog( V aValue, IValedControlFactory aVcFactory,
      ITsDialogInfo aDialogInfo ) {
    TsNullArgumentRtException.checkNulls( aVcFactory, aDialogInfo );
    final Object NULL_VALUE = new Object();

    /**
     * Implements content of the dialog invoked by
     * {@link ValedControlUtils#invokeAsModalDialog(Object, IValedControlFactory, ITsDialogInfo)}.
     *
     * @author hazard157 IValedControlFactory
     */
    class ValedWrapperDialogContent
        extends AbstractTsDialogPanel<Object, IValedControlFactory> {

      private final IValedControl<Object> valed;

      protected ValedWrapperDialogContent( Composite aParent, TsDialog<Object, IValedControlFactory> aOwnerDialog ) {
        super( aParent, aOwnerDialog );
        this.setLayout( new BorderLayout() );
        ITsGuiContext ctx = new TsGuiContext( aDialogInfo.tsContext() );
        OPDEF_IS_SINGLE_LINE_UI.setValue( ctx.params(), AV_TRUE );
        valed = aVcFactory.createEditor( ctx );
        valed.createControl( this );
        valed.getControl().setLayoutData( new BorderData( SWT.CENTER ) );
        valed.eventer().addListener( notificationValedControlChangeListener );
      }

      @Override
      protected ValidationResult doValidate() {
        return valed.canGetValue();
      }

      @Override
      protected void doSetDataRecord( Object aData ) {
        valed.setValue( aData );
      }

      @Override
      protected Object doGetDataRecord() {
        Object value = valed.getValue();
        return value != null ? value : NULL_VALUE;
      }

    }

    IDialogPanelCreator<Object, IValedControlFactory> creator = ValedWrapperDialogContent::new;
    TsDialog<Object, IValedControlFactory> d = new TsDialog<>( aDialogInfo, aValue, null, creator );
    Object rawValue = d.execData();
    @SuppressWarnings( "unchecked" )
    V value = rawValue != NULL_VALUE ? (V)rawValue : null;
    Boolean ok = Boolean.valueOf( rawValue != null );
    return new Pair<>( value, ok );
  }

  /**
   * No subclasses.
   */
  private ValedControlUtils() {
    // nop
  }

}
