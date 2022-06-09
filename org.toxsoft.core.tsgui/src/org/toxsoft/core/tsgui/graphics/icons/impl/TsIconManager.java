package org.toxsoft.core.tsgui.graphics.icons.impl;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.graphics.icons.impl.ITsResources.*;
import static org.toxsoft.core.tslib.utils.errors.TsErrorUtils.*;

import java.lang.reflect.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link ITsIconManager}.
 *
 * @author hazard157
 */
public class TsIconManager
    implements ITsIconManager {

  private final IEclipseContext       appContext;
  private final Display               display;
  /**
   * Карта загруженных изображений "символьное имя значка" - "изображение значка".
   */
  private final IStringMapEdit<Image> imagesMap = new StringMap<>();

  /**
   * Карта зарегистрированных значков "символьное имя" - "дескриптор значка".
   */
  private final IStringMapEdit<ImageDescriptor> idescrMap = new StringMap<>();

  /**
   * Карта строк URI ресурсов значков в плагинах "символьное имя" - "строка URI".
   * <p>
   * Содержит те ключи тех дескрипторов значков из {@link #idescrMap}, которые были зарегистрированы как ресрусы в
   * плагинах.
   */
  private final IStringMapEdit<String> uriMap = new StringMap<>( 157 );

  /**
   * Конструктор.
   *
   * @param aAppContext {@link IEclipseContext} - контекст приложения
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsIconManager( IEclipseContext aAppContext ) {
    TsNullArgumentRtException.checkNull( aAppContext );
    appContext = aAppContext;
    display = appContext.get( Display.class );
    TsItemNotFoundRtException.checkNull( display );
    registerStdIconByIds( Activator.PLUGIN_ID, ITsStdIconIds.class, ITsStdIconIds.PREFIX_OF_ICON_IDS );
    // запланируем освобождение ресурсов
    display.disposeExec( () -> releaseResources() );
  }
  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  void releaseResources() {
    while( !imagesMap.isEmpty() ) {
      Image img = imagesMap.removeByKey( imagesMap.keys().first() );
      img.dispose();
    }
    idescrMap.clear();
    uriMap.clear();
    System.gc();
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private Image internalGetUnknownIconImage( EIconSize aIconSize ) {
    String symName = makeSymbolicName( ICONID_UNKNOWN_ICON_ID, aIconSize );
    Image img = imagesMap.findByKey( symName );
    if( img == null ) {
      ImageDescriptor id = TsIconManagerUtils.imageDescriptorFromPlugin( Activator.PLUGIN_ID, symName );
      img = id.createImage( display );
      imagesMap.put( symName, img );
    }
    return img;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsIconManager
  //

  @Override
  public String makeSymbolicName( String aStdIconId, EIconSize aSize ) {
    return TsIconManagerUtils.makeStdPathInPlugin( aStdIconId, aSize );
  }

  @Override
  public Image loadStdIcon( String aStdIconId, EIconSize aSize ) {
    String symName = makeSymbolicName( aStdIconId, aSize );
    Image img = imagesMap.findByKey( symName );
    if( img != null ) {
      return img;
    }
    ImageDescriptor id = idescrMap.findByKey( symName );
    if( id != null ) {
      img = id.createImage( display );
      if( img != null ) {
        imagesMap.put( symName, img );
        return img;
      }
    }
    return internalGetUnknownIconImage( aSize );
  }

  @Override
  public ImageDescriptor loadStdDescriptor( String aStdIconId, EIconSize aSize ) {
    String symName = makeSymbolicName( aStdIconId, aSize );
    ImageDescriptor imgDes = idescrMap.findByKey( symName );
    if( imgDes == null ) {
      symName = makeSymbolicName( ICONID_UNKNOWN_ICON_ID, aSize );
      imgDes = idescrMap.findByKey( symName );
      TsInternalErrorRtException.checkNull( imgDes );
    }
    return imgDes;
  }

  @Override
  public Image loadFreeIcon( String aSymbolicName ) {
    checkNonBlank( aSymbolicName );
    Image img = imagesMap.findByKey( aSymbolicName );
    if( img != null ) {
      return img;
    }
    ImageDescriptor id = idescrMap.findByKey( aSymbolicName );
    if( id != null ) {
      img = id.createImage( display );
      if( img != null ) {
        imagesMap.put( aSymbolicName, img );
        return img;
      }
    }
    return internalGetUnknownIconImage( EIconSize.maxSize() );
  }

  @Override
  public ImageDescriptor loadFreeDescriptor( String aSymbolicName ) {
    checkNonBlank( aSymbolicName );
    return idescrMap.getByKey( aSymbolicName );
  }

  @Override
  public String findStdIconBundleUri( String aStdIconId, EIconSize aSize ) {
    String symName = makeSymbolicName( aStdIconId, aSize );
    return uriMap.findByKey( symName );
  }

  @Override
  public boolean isRegistered( String aSymbolicName ) {
    return idescrMap.hasKey( aSymbolicName );
  }

  @Override
  public void registerStdIcon( String aPluginId, String aStdIconId ) {
    checkNonBlank( aPluginId );
    checkNonBlank( aStdIconId );
    for( EIconSize sz : EIconSize.values() ) {
      String symName = makeSymbolicName( aStdIconId, sz );
      Image img = imagesMap.removeByKey( symName );
      if( img != null ) {
        img.dispose();
      }
      ImageDescriptor id = TsIconManagerUtils.imageDescriptorFromPlugin( aPluginId, symName );
      if( id != null ) {
        idescrMap.put( symName, id );
        uriMap.put( symName, TsIconManagerUtils.imageUriFromPlugin( aPluginId, symName ) );
      }
    }
  }

  @Override
  public boolean registerFreeIcon( String aPluginId, String aPathInPlugin, String aSymbolicName ) {
    checkNonBlank( aPluginId );
    checkNonBlank( aPathInPlugin );
    checkNonBlank( aSymbolicName );
    Image img = imagesMap.removeByKey( aSymbolicName );
    if( img != null ) {
      img.dispose();
    }
    ImageDescriptor id = TsIconManagerUtils.imageDescriptorFromPlugin( aPluginId, aPathInPlugin );
    if( id == null ) {
      return false;
    }
    idescrMap.put( aSymbolicName, id );
    uriMap.put( aSymbolicName, TsIconManagerUtils.imageUriFromPlugin( aPluginId, aPathInPlugin ) );
    return true;
  }

  @Override
  public void registerStdIconByIds( String aPluginId, Class<?> aInterfaceClass, String aIconNamePrefix ) {
    checkNonBlank( aPluginId );
    TsNullArgumentRtException.checkNulls( aInterfaceClass, aIconNamePrefix );
    for( Field f : aInterfaceClass.getDeclaredFields() ) {
      String fldName = f.getName();
      if( fldName.startsWith( aIconNamePrefix ) ) {
        if( f.getType() != String.class ) {
          throw new TsIllegalStateRtException( FMT_ERR_ICON_NAME_FIELD_MUST_BE_STRING_CONST,
              aInterfaceClass.getSimpleName(), f.getName() );
        }
        int mods = f.getModifiers();
        if( !Modifier.isStatic( mods ) || !Modifier.isFinal( mods ) ) {
          throw new TsIllegalStateRtException( FMT_ERR_ICON_NAME_FIELD_MUST_BE_STRING_CONST,
              aInterfaceClass.getSimpleName(), f.getName() );
        }
        String iconId;
        try {
          iconId = (String)f.get( null );
        }
        catch( Exception ex ) {
          throw new TsIllegalStateRtException( ex, FMT_ERR_ICON_NAME_FIELD_MUST_BE_STRING_CONST,
              aInterfaceClass.getSimpleName(), f.getName() );
        }
        registerStdIcon( aPluginId, iconId );
      }
    }
  }

}
