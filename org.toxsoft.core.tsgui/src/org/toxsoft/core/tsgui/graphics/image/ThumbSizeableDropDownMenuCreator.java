package org.toxsoft.core.tsgui.graphics.image;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.graphics.image.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;

import java.util.*;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.singlesrc.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.mws.services.hdpi.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Creates thumb size selection drop-down menu for the action {@link #AI_THUMB_SIZEABLE_ZOOM_MENU}.
 * <p>
 * Usage:
 * <ul>
 * <li>add the action {@link #AI_THUMB_SIZEABLE_ZOOM_MENU} to the toolbar;</li>
 * <li>create instance of this class
 * <code>IMenuCreator thumbSizeMenuCreator = new {@link ThumbSizeableDropDownMenuCreator}(...)</code>;</li>
 * <li>add menu to the action by the method {@link TsToolbar#setActionMenu(String, IMenuCreator) toolbar.setActionMenu(
 * AID_THUMB_SIZEABLE_ZOOM_MENU, thumbSizeMenuCreator )};</li>
 * <li>in the toolbar handler {@link ITsActionHandler#handleAction(String)} handle action with ID
 * #AID_THUMB_SIZEABLE_ZOOM_MENU} by setting default thumb size {@link IThumbSizeable#setThumbSize(EThumbSize)
 * src.setThumbSize( src.defaultThumbSize() )}.</li>
 * </ul>
 * <p>
 * Note: default size {@link IThumbSizeable#defaultThumbSize()} should be in the lsit {@link #getAvailableThumbSizes()}
 * otherwise behavior will be undefined.
 *
 * @author hazrard157
 */
public class ThumbSizeableDropDownMenuCreator
    extends AbstractMenuCreator {

  /**
   * ID of action {@link #AI_THUMB_SIZEABLE_ZOOM_MENU}.
   */
  public static final String AID_THUMB_SIZEABLE_ZOOM_MENU = TS_ID + ".act.ThumbSizeDropDownMenu"; //$NON-NLS-1$

  /**
   * ID of action {@link #AI_THUMB_SIZEABLE_ZOOM_IN}.
   */
  public static final String AID_THUMB_SIZEABLE_ZOOM_IN = AID_THUMB_SIZEABLE_ZOOM_MENU + ".ZoomIn"; //$NON-NLS-1$

  /**
   * ID of action {@link #AI_THUMB_SIZEABLE_ZOOM_OUT}.
   */
  public static final String AID_THUMB_SIZEABLE_ZOOM_OUT = AID_THUMB_SIZEABLE_ZOOM_MENU + ".ZoomOut"; //$NON-NLS-1$

  /**
   * Drop-down action for controlling the icon size {@link IThumbSizeable}.
   * <p>
   * The button calls the {@link ITsStdActionDefs#ACTID_ZOOM_ORIGINAL} action, and the two drop-down menu items call the
   * actions {@link ITsStdActionDefs#ACTID_ZOOM_OUT} and {@link ITsStdActionDefs#ACTID_ZOOM_IN}.
   * <p>
   * <b>Warning:</b> This action cannot be set directly to {@link IMenuCreator}. You must set the menu on the toolbar
   * using the {@link TsToolbar#setActionMenu(String, IMenuCreator)} method.
   */
  public static final ITsActionDef AI_THUMB_SIZEABLE_ZOOM_MENU = TsActionDef.ofMenu2( AID_THUMB_SIZEABLE_ZOOM_MENU, //
      STR_N_THUMB_SIZEABLE_ZOOM_MENU, STR_D_THUMB_SIZEABLE_ZOOM_MENU, ICONID_ZOOM_ORIGINAL );

  /**
   * Action to increase thumbnail size.
   */
  public static final ITsActionDef AI_THUMB_SIZEABLE_ZOOM_IN = TsActionDef.ofPush2( AID_THUMB_SIZEABLE_ZOOM_IN, //
      STR_N_THUMB_SIZEABLE_ZOOM_IN, STR_D_THUMB_SIZEABLE_ZOOM_IN, ICONID_ZOOM_IN );

  /**
   * Action to reduce thumbnail size.
   */
  public static final ITsActionDef AI_THUMB_SIZEABLE_ZOOM_OUT = TsActionDef.ofPush2( AID_THUMB_SIZEABLE_ZOOM_OUT, //
      STR_N_THUMB_SIZEABLE_ZOOM_OUT, STR_D_THUMB_SIZEABLE_ZOOM_OUT, ICONID_ZOOM_OUT );

  final IThumbSizeable subject;
  final ITsIconManager iconManager;

  final IListEdit<EThumbSize> availableThumbSizes         = new ElemArrayList<>( EThumbSize.values() );
  private boolean             hasIndividualSizesMenuItems = true;
  EIconSize                   menuIconSize                = EIconSize.IS_16X16;

  /**
   * Constructor.
   *
   * @param aSubject {@link IThumbSizeable} - managed entity
   * @param aContext {@link ITsGuiContext} - the context
   * @param aMenuIconSize {@link EIconSize} - menu icons size or <code>null</code> for ts default size
   * @param aMinThumbSize {@link EThumbSize} - minimum acceptable thumbnail size
   * @param aMaxThumbSize {@link EThumbSize} - minimum acceptable thumbnail size
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ThumbSizeableDropDownMenuCreator( IThumbSizeable aSubject, ITsGuiContext aContext, EIconSize aMenuIconSize,
      EThumbSize aMinThumbSize, EThumbSize aMaxThumbSize ) {
    TsNullArgumentRtException.checkNulls( aSubject, aContext, aMinThumbSize, aMaxThumbSize );
    subject = aSubject;
    iconManager = aContext.get( ITsIconManager.class );
    if( aMenuIconSize != null ) {
      menuIconSize = aMenuIconSize;
    }
    else {
      ITsHdpiService hdpiService = aContext.get( ITsHdpiService.class );
      menuIconSize = hdpiService.getMenuIconsSize();
    }
    setAvalaiableThumbSizesRange( aMinThumbSize, aMaxThumbSize );
  }

  /**
   * Constructor.
   *
   * @param aSubject {@link IThumbSizeable} - managed entity
   * @param aContext {@link ITsGuiContext} - the context
   * @param aMinThumbSize {@link EThumbSize} - minimum acceptable thumbnail size
   * @param aMaxThumbSize {@link EThumbSize} - minimum acceptable thumbnail size
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ThumbSizeableDropDownMenuCreator( IThumbSizeable aSubject, ITsGuiContext aContext, EThumbSize aMinThumbSize,
      EThumbSize aMaxThumbSize ) {
    this( aSubject, aContext, null, aMinThumbSize, aMaxThumbSize );
  }

  /**
   * Constructor with icon sizes in the drop-down menu {@link EIconSize#IS_16X16}.
   *
   * @param aSubject {@link IThumbSizeable} - managed entity
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ThumbSizeableDropDownMenuCreator( IThumbSizeable aSubject, ITsGuiContext aContext ) {
    this( aSubject, aContext, null, EThumbSize.minSize(), EThumbSize.maxSize() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractMenuCreator
  //

  @SuppressWarnings( "unused" )
  @Override
  protected boolean fillMenu( Menu aMenu ) {
    // zoom original
    MenuItem mItem = new MenuItem( aMenu, SWT.PUSH );
    mItem.setText( String.format( FMT_N_ORIGINAL_SIZE, subject.defaultThumbSize().nmName() ) );
    TsSinglesourcingUtils.MenuItem_setToolTipText( mItem,
        String.format( FMT_D_ORIGINAL_SIZE, subject.defaultThumbSize().nmName() ) );
    mItem.setImage( iconManager.loadStdIcon( AI_THUMB_SIZEABLE_ZOOM_MENU.iconId(), menuIconSize ) );
    mItem.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        doSetOriginalSize( subject );
      }
    } );
    mItem.setEnabled( subject.thumbSize() != subject.defaultThumbSize() );
    // separator
    new MenuItem( aMenu, SWT.SEPARATOR );
    // zoom out
    mItem = new MenuItem( aMenu, SWT.PUSH );
    mItem.setText( AI_THUMB_SIZEABLE_ZOOM_OUT.nmName() );
    TsSinglesourcingUtils.MenuItem_setToolTipText( mItem, AI_THUMB_SIZEABLE_ZOOM_OUT.description() );

    mItem.setImage( iconManager.loadStdIcon( AI_THUMB_SIZEABLE_ZOOM_OUT.iconId(), menuIconSize ) );
    mItem.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        doZoomOut( subject );
      }
    } );
    mItem.setEnabled( subject.thumbSize() != getAvailableThumbSizes().first() );
    // zoom in
    mItem = new MenuItem( aMenu, SWT.PUSH );
    mItem.setText( AI_THUMB_SIZEABLE_ZOOM_IN.nmName() );
    TsSinglesourcingUtils.MenuItem_setToolTipText( mItem, AI_THUMB_SIZEABLE_ZOOM_IN.description() );

    mItem.setImage( iconManager.loadStdIcon( AI_THUMB_SIZEABLE_ZOOM_IN.iconId(), menuIconSize ) );
    mItem.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        doZoomIn( subject );
      }
    } );
    mItem.setEnabled( subject.thumbSize() != getAvailableThumbSizes().last() );
    if( hasIndividualSizesMenuItems ) {
      // separator
      new MenuItem( aMenu, SWT.SEPARATOR );
      // availableThumbSizes
      for( EThumbSize sz : availableThumbSizes ) {
        mItem = new MenuItem( aMenu, SWT.CHECK );
        mItem.setText( sz.nmName() );
        TsSinglesourcingUtils.MenuItem_setToolTipText( mItem, sz.description() );
        mItem.addSelectionListener( new SelectionAdapter() {

          @Override
          public void widgetSelected( SelectionEvent e ) {
            doSetThumbSize( subject, sz );
          }
        } );
        mItem.setSelection( sz == subject.thumbSize() );
      }
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the size of the icons in the created menu.
   *
   * @return {@link EIconSize} - размер значков в меню
   */
  public EIconSize getMenuIconSize() {
    return menuIconSize;
  }

  /**
   * Sets the size of icons in the created menu.
   *
   * @param aMenuIconSize {@link EIconSize} - size of icons in the menu
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setMenuIconSize( EIconSize aMenuIconSize ) {
    menuIconSize = TsNullArgumentRtException.checkNull( aMenuIconSize );
  }

  /**
   * Determines if the menu item will have individual size settings for each of the {@link #getAvailableThumbSizes()}.
   *
   * @return boolean - an indication of the presence of individual menu items of acceptable sizes
   */
  public boolean isThumbSizesMenuItems() {
    return hasIndividualSizesMenuItems;
  }

  /**
   * Specifies if the menu item will have individual size settings for each of the {@link #getAvailableThumbSizes()}.
   *
   * @param aValue boolean - an indication of the presence of individual menu items of acceptable sizes
   */
  public void setThumbSizesMenuItems( boolean aValue ) {
    hasIndividualSizesMenuItems = aValue;
  }

  /**
   * Returns the thumbnail dimensions that will be used for resizing.
   * <p>
   * The returned list always contains at least one element and is always sorted by size. *
   *
   * @return {@link IList}&lt;{@link EThumbSize}&gt; - list of acceptable sizes
   */
  public IList<EThumbSize> getAvailableThumbSizes() {
    return availableThumbSizes;
  }

  /**
   * Sets the list {@link #getAvailableThumbSizes()}.
   * <p>
   * You can specify any dimensions; the range will be generated automatically, even if aMinSize < aMaxSize. You can
   * specify the same minimum and maximum, but this doesn't make sense.
   * <p>
   * Note: The default size {@link IThumbSizeable#defaultThumbSize()} must be among the sizes specified.
   *
   * @param aMinSize {@link EThumbSize} - lower limit of the range
   * @param aMaxSize {@link EThumbSize} - upper limit of the range
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setAvalaiableThumbSizesRange( EThumbSize aMinSize, EThumbSize aMaxSize ) {
    TsNullArgumentRtException.checkNulls( aMinSize, aMaxSize );
    availableThumbSizes.clear();
    EThumbSize minSize = aMinSize;
    EThumbSize maxSize = aMaxSize;
    if( minSize.size() > maxSize.size() ) {
      minSize = aMaxSize;
      maxSize = aMinSize;
    }
    EThumbSize sz = minSize;
    while( true ) {
      availableThumbSizes.add( sz );
      if( sz == maxSize ) {
        break;
      }
      sz = sz.nextSize();
    }
  }

  /**
   * Sets the list {@link #getAvailableThumbSizes()}.
   * <p>
   * At least one size must be specified. Sizes can be specified in any order; they will be sorted.
   * <p>
   * Note: Among the specified sizes, there must be a default size {@link IThumbSizeable#defaultThumbSize()}. *
   *
   * @param aSizes {@link EThumbSize}[] - array of sizes
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is empty array
   */
  public void setAvalaiableThumbSizes( EThumbSize... aSizes ) {
    TsErrorUtils.checkArrayArg( aSizes, 1 );
    Arrays.sort( aSizes );
    availableThumbSizes.setAll( aSizes );
  }

  /**
   * Returns the next (bigger) valid thumbnail size.
   * <p>
   * The point of this method is that it returns only sizes from the valid {@link #getAvailableThumbSizes()}. If the
   * argument is already the largest valid size, it returns the argument.
   * <p>
   * If the argument is larger than the largest valid size, it returns the largest valid size that is <b>smaller</b>
   * than the argument! *
   *
   * @param aThumbSize {@link EThumbSize} - the thumbnail size
   * @return {@link EThumbSize} - bigger allowed size, may be <b>smaller</b> than the argument!
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public EThumbSize getNextThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    if( availableThumbSizes.size() == 1 ) {
      return availableThumbSizes.first();
    }
    if( aThumbSize.size() < availableThumbSizes.first().size() ) {
      return availableThumbSizes.first();
    }
    if( aThumbSize.size() >= availableThumbSizes.last().size() ) {
      return availableThumbSizes.last();
    }
    for( EThumbSize sz = aThumbSize; sz.size() < availableThumbSizes.last().size(); sz = sz.nextSize() ) {
      if( availableThumbSizes.hasElem( sz.nextSize() ) ) {
        return sz.nextSize();
      }
    }
    throw new TsInternalErrorRtException();
  }

  /**
   * Returns the previous (smaller) acceptable thumbnail size.
   * <p>
   * The point of this method is that it returns only sizes from the allowed {@link #getAvailableThumbSizes()}. If the
   * argument is already the smallest acceptable size, it returns the argument.
   * <p>
   * If the argument is less than the smallest acceptable size, it returns the smallest acceptable size that is
   * <b>greater</b> than the argument! *
   *
   * @param aThumbSize {@link EThumbSize} - the thumbnail size
   * @return {@link EThumbSize} - smaller allowed size, may be <b>larger</b> than the argument!
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public EThumbSize getPrevThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    if( availableThumbSizes.size() == 1 ) {
      return availableThumbSizes.first();
    }
    if( aThumbSize.size() <= availableThumbSizes.first().size() ) {
      return availableThumbSizes.first();
    }
    if( aThumbSize.size() > availableThumbSizes.last().size() ) {
      return availableThumbSizes.last();
    }
    for( EThumbSize sz = aThumbSize; sz.size() > availableThumbSizes.first().size(); sz = sz.prevSize() ) {
      if( availableThumbSizes.hasElem( sz.prevSize() ) ) {
        return sz.prevSize();
      }
    }
    throw new TsInternalErrorRtException();
  }

  // ------------------------------------------------------------------------------------
  // May be overridden
  //

  /**
   * Resets the size to {@link IThumbSizeable#defaultThumbSize()}.
   * <p>
   * The method is not called if the current size {@link IThumbSizeable#thumbSize()} is
   * {@link IThumbSizeable#defaultThumbSize()}.
   * <p>
   * In the base class, sets the initial size {@link IThumbSizeable#defaultThumbSize()} of the managed entity. Whether
   * to call the superclass method when overriding depends on the usage logic.
   * <p>
   * Note: to actually change the size, call the {@link #doSetThumbSize(IThumbSizeable, EThumbSize)} method. *
   *
   * @param aSubject {@link IThumbSizeable} - managed entity, never is <code>null</code>
   */
  public void doSetOriginalSize( IThumbSizeable aSubject ) {
    doSetThumbSize( aSubject, aSubject.defaultThumbSize() );
  }

  /**
   * Performs zoom-out - implementation may override default behaviour.
   * <p>
   * The method is not called if the current size {@link IThumbSizeable#thumbSize()} is {@link EThumbSize#maxSize()}.
   * <p>
   * In the base class, sets the next size {@link #getNextThumbSize(EThumbSize)}. Whether to call the superclass method
   * when overriding depends on the usage logic.
   * <p>
   * Note: to actually change the size, call the {@link #doSetThumbSize(IThumbSizeable, EThumbSize)} method.
   *
   * @param aSubject {@link IThumbSizeable} - managed entity, never is <code>null</code>
   */
  public void doZoomOut( IThumbSizeable aSubject ) {
    doSetThumbSize( aSubject, getPrevThumbSize( subject.thumbSize() ) );
  }

  /**
   * Performs zoom-in - implementation may override default behaviour.
   * <p>
   * The method is not called if the current size {@link IThumbSizeable#thumbSize()} is {@link EThumbSize#maxSize()}.
   * <p>
   * In the base class, sets the next size {@link #getNextThumbSize(EThumbSize)}. Whether to call the superclass method
   * when overriding depends on the usage logic.
   * <p>
   * Note: to actually change the size, call the {@link #doSetThumbSize(IThumbSizeable, EThumbSize)} method.
   *
   * @param aSubject {@link IThumbSizeable} - managed entity, never is <code>null</code>
   */
  public void doZoomIn( IThumbSizeable aSubject ) {
    doSetThumbSize( aSubject, getNextThumbSize( subject.thumbSize() ) );
  }

  /**
   * Implementation must change the thumbnail size to the specified size.
   * <p>
   * In the base class, this simply sets the aThumbSize to the specified size. Whether to call the parent method when
   * overriding it depends on the usage logic.
   * <p>
   * Note: This method is implicitly/explicitly called by all other <code>doXxx()</code> resizing methods. *
   *
   * @param aSubject {@link IThumbSizeable} - managed entity, never is <code>null</code>
   * @param aSize {@link EThumbSize} - new size, never is <code>null</code>
   */
  public void doSetThumbSize( IThumbSizeable aSubject, EThumbSize aSize ) {
    aSubject.setThumbSize( aSize );
  }

}
