package org.toxsoft.core.tsgui.graphics.image;

import static org.toxsoft.core.tsgui.graphics.image.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.io.*;
import java.net.*;

import org.eclipse.core.runtime.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

/**
 * {@link ITsImageSourceKind} implementation - image from the plugin (bundle) resource.
 * <p>
 * The image resource is specified as pair of plugin ID and path of the resource in the plugin.
 *
 * @author hazard157
 */
public class TsImageSourceKindPlugin
    extends AbstractTsImageSourceKind {

  /**
   * Option: the plugin ID.
   */
  public static final IDataDef OPDEF_PLUGIN_ID = DataDef.create( "pluginId", STRING, //$NON-NLS-1$
      TSID_NAME, STR_PLUGIN_PLUGIN_ID, //
      TSID_DESCRIPTION, STR_PLUGIN_PLUGIN_ID_D, //
      TSID_IS_MANDATORY, AV_TRUE, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * Option: the plugin ID.
   */
  public static final IDataDef OPDEF_RESOURCE_PATH = DataDef.create( "resourcePath", STRING, //$NON-NLS-1$
      TSID_NAME, STR_PLUGIN_RESOURCE_PATH, //
      TSID_DESCRIPTION, STR_PLUGIN_RESOURCE_PATH_D, //
      TSID_IS_MANDATORY, AV_TRUE, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * The kind ID.
   */
  public static final String KIND_ID = "plugin"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final ITsImageSourceKind INSTANCE = new TsImageSourceKindPlugin();

  private TsImageSourceKindPlugin() {
    super( KIND_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SRCKIND_PLUGIN, //
        TSID_DESCRIPTION, STR_SRCKIND_PLUGIN_D //
    ) );
    opDefs().add( OPDEF_PLUGIN_ID );
    opDefs().add( OPDEF_RESOURCE_PATH );
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
    return TsImageDescriptor.create( KIND_ID, //
        OPDEF_PLUGIN_ID, aPluginId, //
        OPDEF_RESOURCE_PATH, aResourcePath //
    );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static String trimSeparators( String aStr ) {
    String s = aStr;
    while( s.startsWith( "/" ) ) { //$NON-NLS-1$
      s = s.substring( 1 );
    }
    while( s.endsWith( "/" ) ) { //$NON-NLS-1$
      s = s.substring( 0, s.length() - 1 );
    }
    return s;
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsImageSourceKind
  //

  @Override
  protected ValidationResult doValidateParams( IOptionSet aParams ) {
    String pluginId = trimSeparators( OPDEF_PLUGIN_ID.getValue( aParams ).asString() );
    if( pluginId.isBlank() ) {
      return ValidationResult.error( MSG_ERR_NO_PLUGIN_ID );
    }
    String resourcePath = trimSeparators( OPDEF_RESOURCE_PATH.getValue( aParams ).asString() );
    if( resourcePath.isBlank() ) {
      return ValidationResult.error( MSG_ERR_NO_RESOURCE_PATH );
    }
    String uriStr = "platform:/plugin/" + pluginId + '/' + resourcePath; //$NON-NLS-1$
    URL platformURL = null;
    try {
      URL url = new URL( uriStr );
      platformURL = FileLocator.find( url );
    }
    catch( @SuppressWarnings( "unused" ) MalformedURLException ex ) {
      return ValidationResult.error( FMT_ERR_INV_RESOURCE_URL, uriStr );
    }
    if( platformURL == null ) {
      return ValidationResult.error( FMT_ERR_INV_RESOURCE_URL, uriStr );
    }
    try( InputStream ins = new BufferedInputStream( platformURL.openStream() ) ) {
      return ValidationResult.SUCCESS;
    }
    catch( @SuppressWarnings( "unused" ) IOException ex ) {
      return ValidationResult.error( FMT_ERR_NO_RESOURCE_BY_URL, uriStr );
    }
  }

  @Override
  protected TsImage doCreate( TsImageDescriptor aDescriptor, ITsGuiContext aContext ) {
    String pluginId = trimSeparators( OPDEF_PLUGIN_ID.getValue( aDescriptor.params() ).asString() );
    String resourcePath = trimSeparators( OPDEF_RESOURCE_PATH.getValue( aDescriptor.params() ).asString() );
    String uriStr = "platform:/plugin/" + pluginId + '/' + resourcePath; //$NON-NLS-1$
    try {
      URL url = new URL( uriStr );
      URL platformURL = FileLocator.find( url );
      try( InputStream ins = new BufferedInputStream( platformURL.openStream() ) ) {
        Display display = aContext.get( Display.class );
        return TsImageUtils.loadTsImage( ins, display );
      }
      finally {
        // TODO this method needs rewrite
      }
    }
    catch( MalformedURLException ex ) {
      throw new TsIllegalArgumentRtException( ex );
    }
    catch( IOException ex ) {
      throw new TsIoRtException( ex );
    }
  }

  @Override
  protected String doHumanReadableString( IOptionSet aParams ) {
    String resourcePath = trimSeparators( OPDEF_RESOURCE_PATH.getValue( aParams ).asString() );
    return TsFileUtils.extractFileName( resourcePath );
  }

  @Override
  protected IOptionSet doEdit( IOptionSet aParams, ITsGuiContext aContext ) {
    /**
     * TODO change to the dialog where plugin ID is selected from the list of available plugins at design time and the
     * resource path is selected as a resource from the specified plugin.
     */
    return super.doEdit( aParams, aContext );
  }

}
