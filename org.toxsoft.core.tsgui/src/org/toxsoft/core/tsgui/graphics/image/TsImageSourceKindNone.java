package org.toxsoft.core.tsgui.graphics.image;

import static org.toxsoft.core.tsgui.graphics.image.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.io.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsImageSourceKind} implementation - for special case {@link TsImageDescriptor#NONE}.
 * <p>
 * Nothing is specified as an option.
 *
 * @author hazard157
 */
public class TsImageSourceKindNone
    extends AbstractTsImageSourceKind {

  /**
   * The kind ID.
   */
  public static final String KIND_ID = "none"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final ITsImageSourceKind INSTANCE = new TsImageSourceKindNone();

  private static final int SIZE_OF_THE_IMAGE = 32;

  private TsImageSourceKindNone() {
    super( KIND_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SRCKIND_NONE, //
        TSID_DESCRIPTION, STR_SRCKIND_NONE_D //
    ) );
  }

  /**
   * Creates the image descriptor of this kind.
   *
   * @param aPluginId String - the plugin ID
   * @param aResourcePath String - path of the image resource in the plugin
   * @return {@link TsImageDescriptor} - created descriptor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument is a blank string
   */
  public static TsImageDescriptor createImageDescriptor( String aPluginId, String aResourcePath ) {
    TsErrorUtils.checkNonBlank( aPluginId );
    TsErrorUtils.checkNonBlank( aResourcePath );
    return TsImageDescriptor.create( KIND_ID, IOptionSet.NULL );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsImageSourceKind
  //

  @Override
  protected TsImage doCreate( TsImageDescriptor aDescriptor, ITsGuiContext aContext ) {
    ITsImageManager imageManager = aContext.get( ITsImageManager.class );
    return imageManager.createUnknownImage( SIZE_OF_THE_IMAGE );
  }

  @Override
  protected IOptionSet doEdit( IOptionSet aParams, ITsGuiContext aContext ) {
    return IOptionSet.NULL;
  }

  @Override
  protected File doReturnIfFile( IOptionSet aParams, ITsGuiContext aContext ) {
    return null;
  }

  @Override
  protected String doHumanReadableString( IOptionSet aParams ) {
    return KIND_ID;
  }

  @Override
  public String uniqueImageNameString( IOptionSet aParams ) {
    return KIND_ID;
  }

}
