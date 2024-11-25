package org.toxsoft.core.tsgui.graphics.colors;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Color source kind determines how {@link TsColorDescriptor} of specified kind is handled in TsGUI library.
 * <p>
 * Kind must be registered by {@link TsColorDescriptor#registerColorSourceKind(ITsColorSourceKind)}.
 * <p>
 * The identifier {@link #id()} is used as kind identifier {@link TsColorDescriptor#kindId()}.
 *
 * @author hazard157
 */
public sealed interface ITsColorSourceKind
    extends IStridableParameterized
    permits AbstractTsColorSourceKind {

  /**
   * Returns the definitions of the {@link TsColorDescriptor#params()} options for this kind of color source.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - list of option definitions
   */
  IStridablesList<IDataDef> opDefs();

  /**
   * Checks descriptor parameters values validity against definitions {@link #opDefs()}.
   * <p>
   * Check includes:
   * <ul>
   * <li>atomic type of the value is compatible with the option definition;</li>
   * <li>mandatory options are present;</li>
   * <li>any additional checks performed by the implementation.</li>
   * </ul>
   * <p>
   * Method {@link #createDescriptor(IOptionSet)} succeeds if and only if this method does not returns an error.
   *
   * @param aParams {@link IOptionSet} - the color descriptor parameters
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult validateParams( IOptionSet aParams );

  /**
   * Returns new instance of {@link TsColorDescriptor} of this kind.
   *
   * @param aParams {@link IOptionSet} - the color descriptor parameters
   * @return {@link TsColorDescriptor} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #validateParams(IOptionSet)}
   */
  TsColorDescriptor createDescriptor( IOptionSet aParams );

  /**
   * Creates the new instance of {@link Color} based on the {@link TsColorDescriptor#params()}.
   * <p>
   *
   * @param aDescriptor {@link TsColorDescriptor} - the color descriptor
   * @return {@link Color} - created color, never is <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the descriptor has ID different with {@link #id()}
   */
  Color createColor( TsColorDescriptor aDescriptor );

  /**
   * Invokes GUI dialog to edit {@link TsColorDescriptor#params()}.
   * <p>
   * The dialog edits options as listed in {@link #opDefs()}. If edit dialog is cancelled by the user then the method
   * returns <code>null</code>.
   * <p>
   * Returned color description has the kind ID {@link TsColorDescriptor#kindId()} the same as this {@link #id()}.
   *
   * @param aParams {@link IOptionSet} - initial values of the edited options, may be an empty set
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link TsColorDescriptor} - the color description with the edited values of parameters or <code>null</code>
   */
  TsColorDescriptor editDescription( IOptionSet aParams, ITsGuiContext aContext );

  /**
   * Returns human readable text of the parameters.
   *
   * @param aParams {@link IOptionSet} - color descriptor parameters
   * @return String - the human readable text
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  String humanReadableString( IOptionSet aParams );

}
