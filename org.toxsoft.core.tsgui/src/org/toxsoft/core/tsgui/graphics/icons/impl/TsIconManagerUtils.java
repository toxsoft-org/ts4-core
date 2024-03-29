package org.toxsoft.core.tsgui.graphics.icons.impl;

import static org.toxsoft.core.tsgui.graphics.icons.ITsIconManager.*;
import static org.toxsoft.core.tslib.utils.errors.TsErrorUtils.*;

import java.net.*;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.model.application.ui.menu.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.ui.plugin.*;
import org.toxsoft.core.tsgui.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.utils.errors.*;

// TODO TRANSLATE

/**
 * Вспомгательные методы работы со значками.
 * <p>
 * Эти методы могут понадобится только тогда, когда нет возможности исполльзовать {@link ITsIconManager}, например, при
 * запуске программы, до создания главного окна и соответствующей инициализации контекста приложения.
 *
 * @author hazard157
 */
public class TsIconManagerUtils {

  /**
   * Constructs icon URI string.
   * <p>
   * Method assumes that icon files are arranges as specified in
   * {@link ITsIconManager#registerStdIconByIds(String, Class, String)}
   * <p>
   * Returns string may be used to set icons for e4 model entities like {@link MHandledMenuItem#setIconURI(String)}.
   *
   * @param aPluginId String - the plugin ID
   * @param aStdIconId String - icon ID
   * @param aIconSize {@link EIconSize} - icon size
   * @return String - URI to access icon resource in plugin
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument is a blank string
   */
  public static String makeStdIconUriString( String aPluginId, String aStdIconId, EIconSize aIconSize ) {
    if( aPluginId == null || aStdIconId == null || aIconSize == null ) {
      throw new TsNullArgumentRtException();
    }
    if( aPluginId.isBlank() || aStdIconId.isBlank() ) {
      throw new TsIllegalArgumentRtException();
    }
    return "platform:/plugin/" + aPluginId + //$NON-NLS-1$
        "/icons/" + aIconSize.id() + '/' + aStdIconId + ICON_FILE_EXTENSION; //$NON-NLS-1$
  }

  /**
   * Создает стандартный путь к файлу изображения в плагине из идентификатора значка и запрошенного размера.
   *
   * @param aStdIconId String - идентификатор стандартного значка
   * @param aSize {@link EIconSize} - размер значка
   * @return String - стандартный путь к файлу значка в плагие
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException идентификатор значка пустая строка
   */
  public static String makeStdPathInPlugin( String aStdIconId, EIconSize aSize ) {
    TsNullArgumentRtException.checkNull( aSize );
    checkNonBlank( aStdIconId );
    StringBuilder sb = new StringBuilder();
    sb.append( PATH_TO_ICONS );
    sb.append( aSize.id() );
    sb.append( '/' );
    sb.append( aStdIconId );
    sb.append( ICON_FILE_EXTENSION );
    return sb.toString();
  }

  /**
   * Загружает стандартный значок перечня {@link ITsStdIconIds} из плагина tsgui.
   * <p>
   * <b>Внимание:</b> созданный ресурс изобажения требует явного уничтожения методом {@link Image#dispose()}.
   *
   * @param aStdIconId String - идентификатор значка
   * @param aSize {@link EIconSize} - размер значка
   * @param aDevice {@link Device} - графическое устройство, для которого создается изображение
   * @return {@link Image} - изображение значка указанного размера
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemNotFoundRtException не найдено изображение для запрошенного значка/размера
   */
  public static Image loadStdIconFromTsGuiPlugin( String aStdIconId, EIconSize aSize, Device aDevice ) {
    TsNullArgumentRtException.checkNull( aDevice );
    String pathInPlugin = makeStdPathInPlugin( aStdIconId, aSize );
    ImageDescriptor id = AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, pathInPlugin );
    TsItemNotFoundRtException.checkNull( id );
    return id.createImage( aDevice );
  }

  /**
   * Создаение дескриптор изображения, находящегося в плагине.
   * <p>
   * Каждый вызов создает новый дескриптор, кеширование не используется.
   * <p>
   * Метод использует код из <code>AbstractUIPlugin.imageDescriptorFromPlugin()</code>.
   *
   * @param aPluginId String - идентификатор плагина
   * @param aImageFilePath String - путь изображения относительно корня плагина
   * @return {@link ImageDescriptor} - дескриптор изображения или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException любой аргумент - пустая строка
   */
  public static ImageDescriptor imageDescriptorFromPlugin( String aPluginId, String aImageFilePath ) {
    checkNonBlank( aPluginId );
    checkNonBlank( aImageFilePath );
    // Don't resolve the URL here, but create a URL using the
    // "platform:/plugin" protocol, which also supports fragments.
    // Caveat: The resulting URL may contain $nl$ etc., which is not
    // directly supported by PlatformURLConnection and needs to go through
    // FileLocator#find(URL), see bug 250432.
    IPath uriPath = new Path( "/plugin" ).append( aPluginId ).append( aImageFilePath ); //$NON-NLS-1$
    URL url;
    try {
      URI uri = new URI( "platform", null, uriPath.toString(), null ); //$NON-NLS-1$
      url = uri.toURL();
    }
    catch( @SuppressWarnings( "unused" ) MalformedURLException | URISyntaxException ex ) {
      return null;
    }
    // look for the image
    URL fullPathString = FileLocator.find( url );
    if( fullPathString == null ) {
      // If not found, reinterpret imageFilePath as full URL.
      // This is unspecified, but apparently widely-used, see bug 395126.
      try {
        url = new URL( aImageFilePath );
      }
      catch( @SuppressWarnings( "unused" ) MalformedURLException ex ) {
        return null;
      }
    }
    // create image descriptor with the platform:/ URL
    return ImageDescriptor.createFromURL( url );
  }

  /**
   * Создает строку URI ресурса изображения в плагине.
   *
   * @param aPluginId String - идентификатор плагина
   * @param aImageFilePath String - путь изображения относительно корня плагина
   * @return String - строка URI ресурса изображения в плагине
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException любой аргумент - пустая строка
   */
  public static String imageUriFromPlugin( String aPluginId, String aImageFilePath ) {
    checkNonBlank( aPluginId );
    checkNonBlank( aImageFilePath );
    // Don't resolve the URL here, but create a URL using the
    // "platform:/plugin" protocol, which also supports fragments.
    // Caveat: The resulting URL may contain $nl$ etc., which is not
    // directly supported by PlatformURLConnection and needs to go through
    // FileLocator#find(URL), see bug 250432.
    IPath uriPath = new Path( "/plugin" ).append( aPluginId ).append( aImageFilePath ); //$NON-NLS-1$
    URL url;
    try {
      URI uri = new URI( "platform", null, uriPath.toString(), null ); //$NON-NLS-1$
      url = uri.toURL();
    }
    catch( @SuppressWarnings( "unused" ) MalformedURLException | URISyntaxException ex ) {
      return null;
    }
    // look for the image
    URL fullPathString = FileLocator.find( url );
    if( fullPathString == null ) {
      // If not found, reinterpret imageFilePath as full URL.
      // This is unspecified, but apparently widely-used, see bug 395126.
      try {
        url = new URL( aImageFilePath );
      }
      catch( @SuppressWarnings( "unused" ) MalformedURLException ex ) {
        return null;
      }
    }
    try {
      return url.toURI().toString();
    }
    catch( URISyntaxException ex ) {
      // этого тут не должно случится никогда...
      throw new TsInternalErrorRtException( ex );
    }
  }

  /**
   * NO subclasses.
   */
  private TsIconManagerUtils() {
    // nop
  }

}
