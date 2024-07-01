package org.toxsoft.core.tsgui.graphics.image;

import static org.toxsoft.core.tsgui.graphics.image.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.net.*;

import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsImageSourceKind} implementation - image from the URL.
 *
 * @author hazard157
 */
public class TsImageSourceKindUrl
    extends AbstractTsImageSourceKind {

  /**
   * Option: .
   */
  public static final IDataDef OPDEF_URL_STRING = DataDef.create( "url", STRING, //$NON-NLS-1$
      TSID_NAME, STR_FILE_URL_STRING, //
      TSID_DESCRIPTION, STR_FILE_URL_STRING_D, //
      TSID_IS_MANDATORY, AV_TRUE, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL //
  );

  /**
   * The kind ID.
   */
  public static final String KIND_ID = "url"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final ITsImageSourceKind INSTANCE = new TsImageSourceKindUrl();

  private TsImageSourceKindUrl() {
    super( KIND_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SRCKIND_URL, //
        TSID_DESCRIPTION, STR_SRCKIND_URL_D //
    ) );
    opDefs().add( OPDEF_URL_STRING );
  }

  /**
   * Creates the image descriptor of this kind.
   *
   * @param aUrl String - the URL string
   * @return {@link TsImageDescriptor} - created descriptor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument is a blank string
   */
  public static TsImageDescriptor createImageDescriptor( String aUrl ) {
    TsErrorUtils.checkNonBlank( aUrl );
    return TsImageDescriptor.create( KIND_ID, //
        OPDEF_URL_STRING, aUrl //
    );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsImageSourceKind
  //

  @Override
  protected TsImage doCreate( TsImageDescriptor aDescriptor, ITsGuiContext aContext ) {
    String urlStr = OPDEF_URL_STRING.getValue( aDescriptor.params() ).asString();
    try {
      URL url = new URI( urlStr ).toURL();
      ImageDescriptor imgDescr = ImageDescriptor.createFromURL( url );
      Display display = aContext.get( Display.class );
      Image image = imgDescr.createImage( display );
      return TsImage.create( image );
    }
    catch( URISyntaxException | MalformedURLException ex ) {
      throw new TsIllegalArgumentRtException( ex );
    }
  }

}
