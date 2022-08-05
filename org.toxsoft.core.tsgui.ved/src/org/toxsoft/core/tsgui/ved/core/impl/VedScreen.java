package org.toxsoft.core.tsgui.ved.core.impl;

import org.eclipse.swt.dnd.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.swtevents.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedScreen} implementation.
 *
 * @author hazard157
 */
class VedScreen
    implements IVedScreen, IVedContextable, IVedScreenPaintingManager {

  private final ITsCollectionChangeListener componentsListChangeListener =
      ( s, o, i ) -> refreshViewsListFromCompnentsList();

  private final IGenericChangeListener componentSelectionListener = aSource -> {
    paintingManager().redraw();
  };

  private final IStridablesListEdit<VedAbstractComponentView> compViews = new StridablesList<>();
  private final VedSelectedComponentManager                   selectionManager;

  private final IListEdit<IVedViewDecorator>   viewDecorators   = new ElemArrayList<>();
  private final IListEdit<IVedScreenDecorator> screenDecorators = new ElemArrayList<>();

  private final GenericChangeEventer    conversionChangeEventer;
  private final TsUserInputEventsBinder userInputBinder;
  private final D2Convertor             d2Conv = new D2Convertor();

  private final Canvas         theCanvas;
  private final VedEnvironment vedEnv;

  private DropTarget dropTarget = null;

  /**
   * Constructor.
   *
   * @param aCanvas {@link Canvas} - canvas to attach screen to
   * @param aVedEnv {@link VedEnvironment} - the VED environment
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedScreen( Canvas aCanvas, VedEnvironment aVedEnv ) {
    TsNullArgumentRtException.checkNulls( aCanvas, aCanvas );
    theCanvas = aCanvas;
    vedEnv = aVedEnv;
    vedEnv.dataModel().listComponents().addCollectionChangeListener( componentsListChangeListener );
    userInputBinder = new TsUserInputEventsBinder( this );
    userInputBinder.bindToControl( theCanvas, SwtUserInputEventsBinder.BIND_ALL_INPUT_EVENTS );
    conversionChangeEventer = new GenericChangeEventer( this );
    selectionManager = new VedSelectedComponentManager( componentSelectionListener );
    selectionManager.genericChangeEventer().addListener( componentSelectionListener );
    theCanvas.addPaintListener( aE -> paint( aE.gc, new TsRectangle( aE.x, aE.y, aE.width, aE.height ) ) );
    theCanvas.addDisposeListener( aE -> close() );
    refreshViewsListFromCompnentsList();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  final void paint( GC aGc, ITsRectangle aPaintBounds ) {
    // decorate screen before views
    for( IVedScreenDecorator d : screenDecorators ) {
      d.paintBefore( aGc, aPaintBounds );
    }
    // paint views and decorators
    for( VedAbstractComponentView v : compViews ) {
      for( IVedViewDecorator d : viewDecorators ) {
        d.paintBefore( v, aGc, aPaintBounds );
      }
      v.painter().paint( aGc, aPaintBounds );
      for( IVedViewDecorator d : viewDecorators ) {
        d.paintAfter( v, aGc, aPaintBounds );
      }
    }
    // decorate screen after views
    for( IVedScreenDecorator d : screenDecorators ) {
      d.paintAfter( aGc, aPaintBounds );
    }
  }

  /**
   * Synchronizes list of views {@link #compViews} to the components {@link IVedDataModel#listComponents()}.
   * <p>
   * Existing views remain, views for new components are created while for removed components views are removed and
   * disposed.
   * <p>
   * FIXME что-то тут не так... ведь если удалили компоненту с ИД "somеId, потом создали другую компоненту с "someId"
   * другого типа, то мы про это не знаем, и останеться вью с "высящей" компонентой, а новая компонента не будет иметь
   * вью... Скорее всего, вообще надо по другому наладить коммуникацию между моделью данных и экраном
   */
  void refreshViewsListFromCompnentsList() {
    IStridablesListEdit<IVedComponent> compsList = new StridablesList<>( vedEnv.dataModel().listComponents() );
    IStridablesListEdit<VedAbstractComponentView> viewsList = new StridablesList<>( compViews );
    // remove views which components has been removed
    IStringListEdit removedCompIds = new StringArrayList();
    for( IVedComponentView v : viewsList ) {
      if( !compsList.hasKey( v.id() ) ) {
        removedCompIds.add( v.id() );
      }
    }
    for( String vid : removedCompIds ) {
      viewsList.removeByKey( vid );
      VedAbstractComponentView v = compViews.removeById( vid );
      v.dispose();
    }
    // create and add views for new components
    IStringListEdit newCompIds = new StringArrayList();
    for( IVedComponent c : compsList ) {
      if( !viewsList.hasKey( c.id() ) ) {
        newCompIds.add( c.id() );
      }
    }
    for( String cid : newCompIds ) {
      IVedComponent c = compsList.getByKey( cid );
      VedAbstractComponentView v = (VedAbstractComponentView)c.createView( this );
      viewsList.add( v );
    }
    // add views in #compsViews in the order of components
    compViews.clear();
    for( String cid : compsList.keys() ) {
      VedAbstractComponentView v = viewsList.getByKey( cid );
      compViews.add( v );
    }
    redraw();
  }

  // ------------------------------------------------------------------------------------
  // ITsContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedEnv.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // ID2ConversionableEx
  //

  @Override
  public ID2Conversion getConversion() {
    return d2Conv.getConversion();
  }

  @Override
  public void setConversion( ID2Conversion aConversion ) {
    TsNullArgumentRtException.checkNull( aConversion );
    if( d2Conv.getConversion().equals( aConversion ) ) {
      return;
    }
    d2Conv.setConversion( aConversion );
    // inform all painters about conversion change
    for( IVedScreenDecorator d : screenDecorators ) {
      d.updateOnScreenConversionChange();
    }
    for( IVedViewDecorator d : viewDecorators ) {
      d.updateOnScreenConversionChange();
    }
    for( IVedComponentView v : compViews ) {
      v.setConversion( d2Conv.getConversion() );
    }
    conversionChangeEventer.fireChangeEvent();
  }

  @Override
  public IGenericChangeEventer conversionChangeEventer() {
    return conversionChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    vedEnv.dataModel().listComponents().removeCollectionChangeListener( componentsListChangeListener );
    // remove screen from screen manager's list
    vedEnv.screenManager().papiWhenScreenClosed( this );
    // remove and dispose painters
    while( !screenDecorators.isEmpty() ) {
      screenDecorators.removeByIndex( 0 ).dispose();
    }
    while( !viewDecorators.isEmpty() ) {
      viewDecorators.removeByIndex( 0 ).dispose();
    }
    while( !compViews.isEmpty() ) {
      compViews.removeByIndex( 0 ).dispose();
    }
    if( dropTarget != null ) {
      dropTarget.dispose();
      dropTarget = null;
    }
  }

  // ------------------------------------------------------------------------------------
  // IVedScreenPaintingManager
  //

  @Override
  public void addViewsDecorator( IVedViewDecorator aDecorator ) {
    if( !viewDecorators.hasElem( aDecorator ) ) {
      viewDecorators.add( aDecorator );
    }
  }

  @Override
  public void removeViewsDecorator( IVedViewDecorator aDecorator ) {
    viewDecorators.remove( aDecorator );
  }

  @Override
  public void addScreensDecorator( IVedScreenDecorator aDecorator ) {
    if( !screenDecorators.hasElem( aDecorator ) ) {
      screenDecorators.add( aDecorator );
    }
  }

  @Override
  public void removeScreensDecorator( IVedScreenDecorator aDecorator ) {
    screenDecorators.remove( aDecorator );
  }

  @Override
  public void redraw() {
    theCanvas.redraw();
  }

  @Override
  public void update() {
    theCanvas.update();
  }

  @Override
  public void redrawView( String aViewId ) {
    IVedComponentView v = compViews.getByKey( aViewId );
    ITsRectangle r = coorsConvertor().rectBounds( v.outline().bounds() );
    theCanvas.redraw( r.a().x() - 1, r.a().y() - 1, r.width() + 2, r.height() + 2, true );
  }

  @Override
  public void redrawRect( ITsRectangle aScreenRect ) {
    TsNullArgumentRtException.checkNull( aScreenRect );
    theCanvas.redraw( aScreenRect.a().x() - 1, aScreenRect.a().y() - 1, //
        aScreenRect.width() + 2, aScreenRect.height() + 2, true );
  }

  @Override
  public void setCursor( Cursor aCursor ) {
    theCanvas.setCursor( aCursor );
  }

  // ------------------------------------------------------------------------------------
  // IVedScreen
  //

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IStridablesList<IVedComponentView> listViews() {
    return (IStridablesList)compViews;
  }

  @Override
  public IVedScreenPaintingManager paintingManager() {
    return this;
  }

  @Override
  public IVedScreenSelectionManager selectionManager() {
    return selectionManager;
  }

  @Override
  public ID2Convertor coorsConvertor() {
    return d2Conv;
  }

  @Override
  public DropTarget createDropTarget( IList<Transfer> aAllowedTypes ) {
    TsNullArgumentRtException.checkNull( aAllowedTypes );
    if( dropTarget != null ) {
      dropTarget.dispose();
      dropTarget = null;
    }
    dropTarget = new DropTarget( theCanvas, DND.DROP_COPY );
    dropTarget.setTransfer( aAllowedTypes.toArray( new Transfer[aAllowedTypes.size()] ) );
    return dropTarget;
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputProducer
  //

  @Override
  public void addTsUserInputListener( ITsUserInputListener aListener ) {
    userInputBinder.addTsUserInputListener( aListener );
  }

  @Override
  public void removeTsUserInputListener( ITsUserInputListener aListener ) {
    userInputBinder.removeTsUserInputListener( aListener );
  }

}
