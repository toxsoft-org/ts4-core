package org.toxsoft.core.tsgui.mws.services.hdpi;

import static org.toxsoft.core.tsgui.mws.services.hdpi.ITsHdpiServiceConstants.*;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tslib.bricks.events.AbstractTsEventer;
import org.toxsoft.core.tslib.bricks.events.ITsEventer;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

/**
 * An {@link ITsHdpiService} implementation.
 *
 * @author hazard157
 */
public final class TsHdpiService
    implements ITsHdpiService {

  class Eventer
      extends AbstractTsEventer<ITsHdpiServiceListener> {

    private final IStringMapEdit<TsHdpiIconSizeEvent> evMapIconSizes = new StringMap<>();

    @Override
    protected boolean doIsPendingEvents() {
      return !evMapIconSizes.isEmpty();
    }

    @Override
    protected void doClearPendingEvents() {
      evMapIconSizes.clear();
    }

    @Override
    protected void doFirePendingEvents() {
      while( !evMapIconSizes.isEmpty() ) {
        TsHdpiIconSizeEvent event = evMapIconSizes.removeByKey( evMapIconSizes.keys().first() );
        reallyFireIconSizeEvent( event );
      }
    }

    private void reallyFireIconSizeEvent( TsHdpiIconSizeEvent aEvent ) {
      for( ITsHdpiServiceListener l : listeners() ) {
        try {
          l.onAppGuiIconSizeChanged( TsHdpiService.this, aEvent );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
    }

    private void collectIconSizeEvent( TsHdpiIconSizeEvent aEvent ) {
      TsHdpiIconSizeEvent oldEvent = evMapIconSizes.findByKey( aEvent.categoryId() );
      // first event for specified category ID: jaust save it
      if( oldEvent == null ) {
        evMapIconSizes.put( aEvent.categoryId(), aEvent );
        return;
      }
      // change events does NOT change initial icon size, remove event as unneeded
      if( oldEvent.oldSize() == aEvent.newSize() ) {
        evMapIconSizes.removeByKey( aEvent.categoryId() );
        return;
      }
      // merge events with very old size and very new size
      TsHdpiIconSizeEvent event = new TsHdpiIconSizeEvent( aEvent.categoryId(), aEvent.newSize(), aEvent.oldSize() );
      evMapIconSizes.put( event.categoryId(), event );
    }

    void fireIconSizeEvent( TsHdpiIconSizeEvent aEvent ) {
      if( isFiringPaused() ) {
        collectIconSizeEvent( aEvent );
      }
      else {
        reallyFireIconSizeEvent( aEvent );
      }
    }

  }

  private final IEclipseContext           appContext;
  private final IStringMapEdit<Integer>   iconScalesMap = new StringMap<>();
  private final IStringMapEdit<EIconSize> iconSizesMap  = new StringMap<>();
  private final Eventer                   eventer       = new Eventer();

  private EIconSize defaultIconSize;

  /**
   * Constructor.
   *
   * @param aAppContext {@link IEclipseContext} - application level context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsHdpiService( IEclipseContext aAppContext ) {
    appContext = TsNullArgumentRtException.checkNull( aAppContext );
    defaultIconSize = determineDefaultIconSize( getScreenWidth() );
    // register builtin categories
    defineIconCategory( ICON_CATEG_ID_TOOLBAR, 0 );
    defineIconCategory( ICON_CATEG_ID_MENU, 0 );
    defineIconCategory( ICON_CATEG_ID_JFACE_CELL, 0 );
    defineIconCategory( ICON_CATEG_ID_UIPART_TAB, 1 );
    // calc initial icon sizes
    recalcIconSizesAfterDefaultSizeDetermined();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private int getScreenWidth() {
    Display display = Display.getCurrent();
    IEclipseContext ctx = appContext.getActiveLeaf();
    Shell shell = ctx.get( Shell.class );
    if( shell != null ) {
      display = shell.getDisplay();
    }
    TsInternalErrorRtException.checkNull( display );
    int screenWidth = display.getPrimaryMonitor().getBounds().width;
    return screenWidth;
  }

  private static EIconSize determineDefaultIconSize( int aScreenWidthInPixels ) {
    TsIllegalArgumentRtException.checkTrue( aScreenWidthInPixels <= 0 );
    if( aScreenWidthInPixels <= 1370 ) {
      return EIconSize.IS_16X16;
    }
    if( aScreenWidthInPixels <= 2560 ) {
      return EIconSize.IS_24X24;
    }
    return EIconSize.IS_32X32;
  }

  private EIconSize calcIconSize( int aScale ) {
    if( aScale == 0 ) {
      return defaultIconSize;
    }
    EIconSize iconSize = defaultIconSize;
    if( aScale > 0 ) {
      for( int i = 0; i < aScale; i++ ) {
        iconSize = iconSize.nextSize();
      }
    }
    else {
      for( int i = 0; i < -aScale; i++ ) {
        iconSize = iconSize.prevSize();
      }
    }
    return iconSize;
  }

  private void recalcIconSizesAfterDefaultSizeDetermined() {
    for( String categId : iconScalesMap.keys() ) {
      int scale = iconScalesMap.getByKey( categId ).intValue();
      EIconSize iconSize = calcIconSize( scale );
      setIconsSize( categId, iconSize );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsHdpiService
  //

  @Override
  public EIconSize getDefaultIconSize() {
    return defaultIconSize;
  }

  @Override
  public EIconSize getIconsSize( String aCategoryId ) {
    EIconSize isz = iconSizesMap.getByKey( aCategoryId );
    if( isz != null ) {
      return isz;
    }
    return getDefaultIconSize();
  }

  @Override
  public void defineIconCategory( String aCategoryId, int aIconScale ) {
    StridUtils.checkValidIdPath( aCategoryId );
    TsItemAlreadyExistsRtException.checkTrue( iconScalesMap.hasKey( aCategoryId ) );
    int scale = aIconScale;
    if( aIconScale > EIconSize.asList().size() ) {
      scale = EIconSize.asList().size();
    }
    if( aIconScale < -EIconSize.asList().size() ) {
      scale = -EIconSize.asList().size();
    }
    iconScalesMap.put( aCategoryId, Integer.valueOf( scale ) );
    EIconSize iconSize = calcIconSize( scale );
    iconSizesMap.put( aCategoryId, iconSize );
  }

  @Override
  public ITsEventer<ITsHdpiServiceListener> eventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Class API
  //

  /**
   * Changes the {@link #getDefaultIconSize()} value.
   * <p>
   * Call to this method is meaningfull immediately after service creation. Otherwise many GUI categorys may not respond
   * to the new value of the default icon size.
   *
   * @param aSize {@link EIconSize} - default icon size
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setDefaultIconSize( EIconSize aSize ) {
    TsNullArgumentRtException.checkNull( aSize );
    defaultIconSize = aSize;
    boolean wasPaused = eventer.isFiringPaused();
    if( !wasPaused ) {
      eventer.pauseFiring();
    }
    recalcIconSizesAfterDefaultSizeDetermined();
    if( !wasPaused ) {
      eventer.resumeFiring( true );
    }
  }

  /**
   * Changes the category icons size.
   * <p>
   * If new size differs from old size, generates
   * {@link ITsHdpiServiceListener#onAppGuiIconSizeChanged(ITsHdpiService, TsHdpiIconSizeEvent)}.
   *
   * @param aCategoryId String - the GUI category ID
   * @param aNewIconSize {@link EIconSize} - the size of category icons
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException unknown category
   */
  public void setIconsSize( String aCategoryId, EIconSize aNewIconSize ) {
    TsNullArgumentRtException.checkNull( aNewIconSize );
    EIconSize oldSize = getIconsSize( aCategoryId );
    if( aNewIconSize != oldSize ) {
      iconSizesMap.put( aCategoryId, aNewIconSize );
      TsHdpiIconSizeEvent event = new TsHdpiIconSizeEvent( aCategoryId, aNewIconSize, oldSize );
      eventer.fireIconSizeEvent( event );
    }
  }

}
