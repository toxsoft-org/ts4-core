package org.toxsoft.core.tsgui.graphics.image;

import java.io.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Image source kind determines how {@link TsImageDescriptor} of specified kind is handled in TsGUI library.
 * <p>
 * Kind must be registered by {@link TsImageDescriptor#registerImageSourceKind(ITsImageSourceKind)}.
 * <p>
 * The identifier {@link #id()} is used as kind identifier {@link TsImageDescriptor#kindId()}.
 *
 * @author hazard157
 */
public sealed interface ITsImageSourceKind
    extends IStridableParameterized
    permits AbstractTsImageSourceKind {

  /**
   * Returns the definitions of the {@link TsImageDescriptor#params()} options for this kind of image source.
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
   * @param aParams {@link IOptionSet} - the image descriptor parameters
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult validateParams( IOptionSet aParams );

  /**
   * Returns new instance of {@link TsImageDescriptor} of this kind.
   *
   * @param aParams {@link IOptionSet} - the image descriptor parameters
   * @return {@link TsImageDescriptor} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #validateParams(IOptionSet)}
   */
  TsImageDescriptor createDescriptor( IOptionSet aParams );

  /**
   * Creates the new instance of {@link TsImage} based on the {@link TsImageDescriptor#params()}.
   * <p>
   * Caller is responsible for created image dispose. Use {@link ITsImageManager} for images creation with automatic
   * dispose and cache management.
   * <p>
   * This method may be resource and time consuming and may throw other runtime exceptions, specific to the source kind.
   * <p>
   * Note: in case when image can not be created (for example, when {@link #validateParams(IOptionSet)} fails) method
   * does not throws an exception, rather logs an error message and returns the valid image created with
   * {@link ITsImageManager#createUnknownImage(int)}. The motivation for this behavior is to avoid the program crashing
   * when a resource is missing or corrupter.
   *
   * @param aDescriptor {@link TsImageDescriptor} - the image descriptor
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link TsImage} - created image, never is <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the descriptor has ID different with {@link #id()}
   */
  TsImage createImage( TsImageDescriptor aDescriptor, ITsGuiContext aContext );

  /**
   * Tries to represent the image as a file if possible.
   * <p>
   * The implementation may try to create the file with the image data in the local file system. For the descriptors of
   * the initially local file resources may return the image file itself. Implementation must be smart enough to create
   * file once and return existing file in subsequent calls.
   * <p>
   * Note that the returned file is guaranteed to exist only while application is running. Returned file may be used
   * both with {@link ITsImageManager} and {@link ITsThumbManager}.
   * <p>
   * File must have the image format, represented by the {@link ITsImageManager}.
   * <p>
   * If for any error file can not be created method returns <code>null</code>.
   *
   * @param aDescriptor {@link TsImageDescriptor} - the image descriptor
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link File} - the image file in a local file system or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the descriptor has ID different with {@link #id()}
   */
  File asFile( TsImageDescriptor aDescriptor, ITsGuiContext aContext );

  /**
   * Invokes GUI dialog to edit {@link TsImageDescriptor#params()}.
   * <p>
   * The dialog edits options as listed in {@link #opDefs()}. If edit dialog is cancelled by the user then the method
   * returns <code>null</code>.
   * <p>
   * Returned image description has the kind ID {@link TsImageDescriptor#kindId()} the same as this {@link #id()}.
   *
   * @param aParams {@link IOptionSet} - initial values of the edited options, may be an empty set
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link TsImageDescriptor} - the image description with the edited values of parameters or <code>null</code>
   */
  TsImageDescriptor editDescription( IOptionSet aParams, ITsGuiContext aContext );

  /**
   * Returns human readable text of the parameters.
   *
   * @param aParams {@link IOptionSet} - image descriptor parameters
   * @return String - the human readable text
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  String humanReadableString( IOptionSet aParams );

}
