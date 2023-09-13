package org.toxsoft.core.tsgui.rcp.graphics.images;

import static org.toxsoft.core.tsgui.rcp.graphics.images.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.io.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.rcp.valed.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsImageSourceKind} implementation - image from the file.
 *
 * @author hazard157
 */
public class TsImageSourceKindFile
    extends AbstractTsImageSourceKind {

  /**
   * Option: path to the image file.
   */
  public static final IDataDef OPDEF_FILE_PATH = DataDef.create( "path", STRING, //$NON-NLS-1$
      TSID_NAME, STR_FILE_FILE_PATH, //
      TSID_DESCRIPTION, STR_FILE_FILE_PATH_D, //
      IValedControlConstants.OPDEF_EDITOR_FACTORY_NAME, ValedAvStringFile.FACTORY_NAME, //
      IValedFileConstants.OPDEF_IS_OPEN_DIALOG, AV_TRUE, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * The kind ID.
   */
  public static final String KIND_ID = "file"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final ITsImageSourceKind INSTANCE = new TsImageSourceKindFile();

  private static final int SIZE_OF_THE_MISSING_FILE_IMAGE = 32;

  private TsImageSourceKindFile() {
    super( KIND_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SRCKIND_FILE, //
        TSID_DESCRIPTION, STR_SRCKIND_FILE_D //
    ) );
    opDefs().add( OPDEF_FILE_PATH );
  }

  /**
   * Creates the image descriptor of this kind.
   *
   * @param aFilePath String - the path to the file
   * @return {@link TsImageDescriptor} - created descriptor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException an argument is a blank string
   */
  public static TsImageDescriptor createImageDescriptor( String aFilePath ) {
    TsErrorUtils.checkNonBlank( aFilePath );
    return TsImageDescriptor.create( KIND_ID, //
        OPDEF_FILE_PATH, aFilePath //
    );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsImageSourceKind
  //

  @Override
  protected TsImage doCreate( TsImageDescriptor aDescriptor, ITsGuiContext aContext ) {
    String pathStr = OPDEF_FILE_PATH.getValue( aDescriptor.params() ).asString();
    File path = new File( pathStr );
    if( !path.exists() ) {
      ITsImageManager imageManager = aContext.get( ITsImageManager.class );
      return imageManager.createUnknownImage( SIZE_OF_THE_MISSING_FILE_IMAGE );
    }
    Display display = aContext.get( Display.class );
    return TsImageUtils.loadTsImage( path, display );
  }

}
