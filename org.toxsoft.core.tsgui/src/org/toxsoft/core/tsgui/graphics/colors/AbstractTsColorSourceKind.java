package org.toxsoft.core.tsgui.graphics.colors;

import static org.toxsoft.core.tsgui.graphics.colors.ITsResources.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsColorSourceKind} implementation base.
 *
 * @author hazard157
 */
public abstract non-sealed class AbstractTsColorSourceKind
    extends StridableParameterized
    implements ITsColorSourceKind {

  private final IStridablesListEdit<IDataDef> opDefs = new StridablesList<>();

  /**
   * Constructor.
   *
   * @param aId String - the color source kind ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public AbstractTsColorSourceKind( String aId, IOptionSet aParams ) {
    super( aId, aParams );
  }

  // ------------------------------------------------------------------------------------
  // ITsColorSourceKind
  //

  @Override
  final public IStridablesListEdit<IDataDef> opDefs() {
    return opDefs;
  }

  @Override
  public ValidationResult validateParams( IOptionSet aParams ) {
    ValidationResult vr = OptionSetUtils.validateOptionSet( aParams, opDefs );
    if( vr.isError() ) {
      return vr;
    }
    return doValidateParams( aParams );
  }

  @Override
  final public TsColorDescriptor createDescriptor( IOptionSet aParams ) {
    TsValidationFailedRtException.checkError( validateParams( aParams ) );
    return new TsColorDescriptor( id(), aParams );
  }

  @Override
  final public TsColorDescriptor editDescription( IOptionSet aParams, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aParams, aContext );
    IOptionSetEdit params = new OptionSet();
    for( IDataDef dd : opDefs ) {
      IAtomicValue av = aParams.findValue( dd.id() );
      if( av != null ) {
        if( AvTypeCastRtException.canAssign( dd.atomicType(), av.atomicType() ) ) {
          params.setValue( dd, av );
        }
      }
    }
    IOptionSet p = doEdit( params, aContext );
    if( p == null ) {
      return null;
    }
    IOptionSetEdit newParams = new OptionSet();
    for( IDataDef dd : opDefs ) {
      IAtomicValue av = p.findValue( dd.id() );
      if( av == null ) {
        av = dd.defaultValue();
      }
      newParams.setValue( dd, av );
    }
    return new TsColorDescriptor( id(), newParams );
  }

  @Override
  public final String humanReadableString( IOptionSet aParams ) {
    TsNullArgumentRtException.checkNull( aParams );
    return doGetHumanReadableString( aParams );
  }

  @Override
  public final Color createColor( TsColorDescriptor aDescriptor, Display aDisplay ) {
    return doCreate( aDescriptor, aDisplay );
  }

  // ------------------------------------------------------------------------------------
  // To implements
  //

  /**
   * Subclass must create color based on it's description.
   * <p>
   * Arguments are checked for validity: non-<code>null</code>, description is of this kind, and parameters passed
   * validity check against {@link #opDefs}.
   * <p>
   * If Color can not be created the method must throw an exception and never return <code>null</code>.
   *
   * @param aDescriptor {@link TsColorDescriptor} - the color descriptor
   * @param aDisplay {@link Display} - the device to create color for
   * @return {@link Color} - created instance
   */
  protected abstract Color doCreate( TsColorDescriptor aDescriptor, Display aDisplay );

  /**
   * Subclass may override and create own implementation of the parameters editing dialog.
   * <p>
   * Default implementation simply calls {@link DialogOptionsEdit#editOpset(ITsDialogInfo, IStridablesList, IOptionSet)}
   * <p>
   * Arguments are checked for validity: non-<code>null</code>, and parameters contains only options listed in
   * {@link #opDefs} with the values of the compatible type.
   * <p>
   * Note <code>aParams</code> may not contain any particular option, even the mandatory one.
   *
   * @param aParams {@link IOptionSet} - initial values of the edited options, may be an empty set
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link IOptionSet} - edited parameters or <code>null</code>
   */
  protected IOptionSet doEdit( IOptionSet aParams, ITsGuiContext aContext ) {
    String title = String.format( FMT_DLG_T_COLOR_DESCR_EDIT, nmName() );
    ITsDialogInfo dlgInf = new TsDialogInfo( aContext, STR_DLG_C_COLOR_DESCR_EDIT, title );
    return DialogOptionsEdit.editOpset( dlgInf, opDefs, aParams, this::validateParams );
  }

  /**
   * Subclass may perform additional arguments check.
   * <p>
   * <code>aParams</code> are already checked by {@link OptionSetUtils#validateOptionSet(IOptionSet, IStridablesList)}
   * against definitions {@link #opDefs()}.
   * <p>
   * In base class returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when overriding.
   *
   * @param aParams {@link IOptionSet} - the command arguments to check
   * @return {@link ValidationResult} - the check result
   */
  protected ValidationResult doValidateParams( IOptionSet aParams ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Subclass may return human readable text of the parameters.
   * <p>
   * In base class returns {@link IOptionSet#toString()}, there is no need to call superclass method when overriding.
   *
   * @param aParams {@link IOptionSet} - color descriptor parameters, never is <code>null</code>
   * @return String - the human readable text
   */
  protected String doGetHumanReadableString( IOptionSet aParams ) {
    return aParams.toString();
  }

  /**
   * The implementation must return a string that uniquely identifies the color.
   * <p>
   * Uniqueness should be ensured only within this kind.
   *
   * @param aParams {@link IOptionSet} - validated color descriptor values
   * @return String - kind-unique color non-blank idenftifer string
   */
  public abstract String uniqueColorNameString( IOptionSet aParams );

}
