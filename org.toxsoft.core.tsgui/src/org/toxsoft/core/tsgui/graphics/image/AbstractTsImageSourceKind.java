package org.toxsoft.core.tsgui.graphics.image;

import static org.toxsoft.core.tsgui.graphics.image.ITsResources.*;

import java.io.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
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
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link ITsImageSourceKind} implementation base.
 *
 * @author hazard157
 */
public abstract non-sealed class AbstractTsImageSourceKind
    extends StridableParameterized
    implements ITsImageSourceKind {

  private static final int SIZE_OF_THE_MISSING_FILE_IMAGE = 32;

  private final IStridablesListEdit<IDataDef> opDefs = new StridablesList<>();

  /**
   * Constructor.
   *
   * @param aId String - the image source kind ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public AbstractTsImageSourceKind( String aId, IOptionSet aParams ) {
    super( aId, aParams );
  }

  // ------------------------------------------------------------------------------------
  // ITsImageSourceKind
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
  final public TsImageDescriptor createDescriptor( IOptionSet aParams ) {
    TsValidationFailedRtException.checkError( validateParams( aParams ) );
    return new TsImageDescriptor( id(), aParams );
  }

  @Override
  final public TsImage createImage( TsImageDescriptor aDescriptor, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aDescriptor, aContext );
    if( !aDescriptor.kindId().equals( id() ) ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INV_KIND, aDescriptor.kindId(), id() );
    }
    ValidationResult vr = validateParams( aDescriptor.params() );
    if( !vr.isError() ) {
      try {
        TsImage tsim = doCreate( aDescriptor, aContext );
        if( tsim != null ) {
          return tsim;
        }
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
    else {
      LoggerUtils.errorLogger().warning( vr.message() );
    }
    ITsImageManager imageManager = aContext.get( ITsImageManager.class );
    return imageManager.createUnknownImage( SIZE_OF_THE_MISSING_FILE_IMAGE );
  }

  @Override
  final public File asFile( TsImageDescriptor aDescriptor, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    if( !aDescriptor.kindId().equals( id() ) ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INV_KIND, aDescriptor.kindId(), id() );
    }
    if( validateParams( aDescriptor.params() ).isError() ) {
      return null;
    }
    // if descriptor denotes a persistent file, return it
    File f = doReturnIfFile( aDescriptor.params(), aContext );
    if( f != null ) {
      return f;
    }
    //
    String filePath = TsImageManagementUtils.makeTsImgDescrTempFileAbsulutePath( this, aDescriptor );
    // if file already exists, simply return it
    f = ESaveImageFileFormat.findExistingFileByBarePath( filePath );
    if( f != null ) {
      return f;
    }
    // now we'll create the image and save it to the disk
    TsImage image = createImage( aDescriptor, aContext );
    try {
      ITsImageManager imageManager = aContext.get( ITsImageManager.class );
      return imageManager.saveToFile( image, isLoselessPreferred(), filePath );
    }
    finally {
      image.dispose();
    }
  }

  @Override
  final public TsImageDescriptor editDescription( IOptionSet aParams, ITsGuiContext aContext ) {
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
    return new TsImageDescriptor( id(), newParams );
  }

  @Override
  public String humanReadableString( IOptionSet aParams ) {
    TsNullArgumentRtException.checkNull( aParams );
    return doHumanReadableString( aParams );
  }

  // ------------------------------------------------------------------------------------
  // To implements
  //

  /**
   * Subclass must create image based on it's description.
   * <p>
   * Arguments are checked for validity: non-<code>null</code>, description is of this kind, and parameters passed
   * validity check against {@link #opDefs}.
   * <p>
   * If image can not be created the method must throw an exception and never return <code>null</code>.
   *
   * @param aDescriptor {@link TsImageDescriptor} - the image descriptor
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link TsImage} - created image
   */
  protected abstract TsImage doCreate( TsImageDescriptor aDescriptor, ITsGuiContext aContext );

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
    String title = String.format( FMT_DLG_T_IMAGE_DESCR_EDIT, nmName() );
    ITsDialogInfo dlgInf = new TsDialogInfo( aContext, STR_DLG_C_IMAGE_DESCR_EDIT, title );
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
   * @param aParams {@link IOptionSet} - image descriptor parameters, never is <code>null</code>
   * @return String - the human readable text
   */
  protected String doHumanReadableString( IOptionSet aParams ) {
    return aParams.toString();
  }

  /**
   * Implementation must return the {@link File} if this descriptor denotes the file already in the local file system.
   * <p>
   * Implementation may also return non-<code>null</code> value if the descriptor denotes image data that may be
   * persistently (between application runs) represented in the local file system. For such case subclass must ensure
   * that returned file exists and contains valid image data.
   * <p>
   * File must have the image format, recognized by the {@link ITsImageManager}.
   * <p>
   * In any other cases (the file is remote, image data is packed in archive or downloaded from server, etc) method must
   * return <code>null</code>.
   *
   * @param aParams {@link IOptionSet} - validated image descriptor values
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link File} - the image file in a local file system or <code>null</code>
   */
  protected abstract File doReturnIfFile( IOptionSet aParams, ITsGuiContext aContext );

  /**
   * The implementation must return a string that uniquely identifies the image.
   * <p>
   * Uniqueness should be ensured only within this kind.
   *
   * @param aParams {@link IOptionSet} - validated image descriptor values
   * @return String - kind-unique image non-blank idenftifer string
   */
  public abstract String uniqueImageNameString( IOptionSet aParams );

  /**
   * Implementation may override default preference when saving image.
   * <p>
   * Returned value is used by internal call of the method {@link ITsImageManager#saveToFile(TsImage,boolean,String)}.
   * <p>
   * By default returns <code>true</code> for PNG format, override it to return <code>false</code>.
   *
   * @return boolean - <code>true</code> for PNG format
   */
  protected boolean isLoselessPreferred() {
    return true;
  }

}
