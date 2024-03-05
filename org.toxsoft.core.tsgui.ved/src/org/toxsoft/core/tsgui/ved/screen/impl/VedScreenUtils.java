package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.asp.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Utility and helper methods.
 *
 * @author hazard157
 * @author vs
 */
public class VedScreenUtils {

  /**
   * Creates current configuration of the VED screen model.
   *
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return {@link VedScreenCfg} - created instance of the configuration
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static VedScreenCfg getVedScreenConfig( IVedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNull( aVedScreen );
    VedScreenCfg scrCfg = new VedScreenCfg();
    IVedScreenModel sm = aVedScreen.model();
    for( VedAbstractVisel item : sm.visels().list() ) {
      VedItemCfg cfg = VedItemCfg.ofVisel( item.id(), item.factoryId(), item.params(), item.props() );
      scrCfg.viselCfgs().add( cfg );
    }
    for( VedAbstractActor item : sm.actors().list() ) {
      VedItemCfg cfg = VedItemCfg.ofActor( item.id(), item.factoryId(), item.params(), item.props() );
      scrCfg.actorCfgs().add( cfg );
    }
    scrCfg.canvasCfg().copyFrom( aVedScreen.view().canvasConfig() );
    return scrCfg;
  }

  /**
   * Sets configuration to the VED screen.
   *
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @param aScreenCfg {@link IVedScreenCfg} - configuration to be applied to the screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void setVedScreenConfig( IVedScreen aVedScreen, IVedScreenCfg aScreenCfg ) {
    TsNullArgumentRtException.checkNulls( aVedScreen, aScreenCfg );
    IVedScreenModel sm = aVedScreen.model();
    try {
      sm.actors().eventer().pauseFiring();
      sm.visels().eventer().pauseFiring();
      sm.actors().clear();
      sm.visels().clear();
      for( IVedItemCfg cfg : aScreenCfg.viselCfgs() ) {
        sm.visels().create( cfg );
      }
      for( IVedItemCfg cfg : aScreenCfg.actorCfgs() ) {
        sm.actors().create( cfg );
      }
      aVedScreen.view().setCanvasConfig( aScreenCfg.canvasCfg() );
    }
    finally {
      sm.visels().eventer().resumeFiring( true );
      sm.actors().eventer().resumeFiring( true );
    }
  }

  /**
   * Returns list of VISEL IDs that have no assigned (bound) actors.<br>
   * <p>
   * "Bound actor" means the actor has the VISEL in the list {@link VedAbstractActor#listBoundViselIds()}.
   *
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return {@link IStringList} - list of visel ids that have no assigned actors
   */
  public static IStringList listOrphanViselIds( IVedScreen aVedScreen ) {
    IStringListEdit visels = new StringArrayList( aVedScreen.model().visels().list().ids() );
    for( IVedActor actor : aVedScreen.model().actors().list() ) {
      for( String viselId : actor.listBoundViselIds() ) {
        visels.remove( viselId );
      }
    }
    return visels;
  }

  /**
   * Returns actor ids, associated with this visel.<br>
   *
   * @param aViselId String - visel id
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return {@link IStringList} - actor ids, associated with this visel
   */
  public static IStringList viselActorIds( String aViselId, IVedScreen aVedScreen ) {
    IStringListEdit result = new StringArrayList();
    for( IVedActor actor : aVedScreen.model().actors().list() ) {
      if( actor.listBoundViselIds().hasElem( aViselId ) ) {
        result.add( actor.id() );
      }
    }
    return result;
  }

  /**
   * Returns actor configuration {@link IVedItemCfg}, associated with the specified VISEL.<br>
   * <p>
   * FIXME this method must be redesigned due to the problems noted in {@link VedAspCopyPaste#fooJustForCommant()}.
   *
   * @param aViselId String - VISEL ID
   * @param aActorsCfg IStridablesList&lt;VedItemCfg> - list of actors configurations
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return {@link IStringList} - actor ids, associated with this visel
   */
  public static IStridablesList<IVedItemCfg> viselActorsConfigs( String aViselId,
      IStridablesList<IVedItemCfg> aActorsCfg, IVedScreen aVedScreen ) {
    IStridablesListEdit<IVedItemCfg> result = new StridablesList<>();
    for( IVedItemCfg actorCfg : aActorsCfg ) {
      if( actorCfg.propValues().hasKey( PROPID_VISEL_ID ) ) {
        if( actorCfg.propValues().getStr( PROPID_VISEL_ID ).equals( aViselId ) ) {
          result.add( actorCfg );
        }
      }
    }
    return result;
  }

  /**
   * Returns copy of visel configuration.<br>
   *
   * @param aViselId String - visel id
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return VedItemCfg - copy of visel configuration
   */
  public static VedItemCfg createCopyOfViselConfig( String aViselId, IVedScreen aVedScreen ) {
    IVedVisel visel = aVedScreen.model().visels().list().getByKey( aViselId );
    VedItemCfg cfg = VedItemCfg.ofVisel( visel.id(), visel.factoryId(), visel.params(), visel.props() );
    return aVedScreen.model().visels().prepareFromTemplate( cfg );
  }

  /**
   * Returns copy of actor configuration.<br>
   *
   * @param aActorId String - actor id
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return VedItemCfg - copy of visel configuration
   */
  public static VedItemCfg createCopyOfActorConfig( String aActorId, IVedScreen aVedScreen ) {
    IVedActor actor = aVedScreen.model().actors().list().getByKey( aActorId );
    VedItemCfg cfg = VedItemCfg.ofVisel( actor.id(), actor.factoryId(), actor.params(), actor.props() );
    return aVedScreen.model().actors().prepareFromTemplate( cfg );
  }

  /**
   * Returns visel configuration list.<br>
   *
   * @param aViselIds {@link IStringList} - list of visel ids
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return IStridablesList&lt;IVedItemCfg> - visel configuration list
   */
  public static IStridablesList<IVedItemCfg> listViselConfigs( IStringList aViselIds, IVedScreen aVedScreen ) {
    IStridablesListEdit<IVedItemCfg> result = new StridablesList<>();
    for( String id : aViselIds ) {
      IVedVisel visel = aVedScreen.model().visels().list().getByKey( id );
      VedItemCfg cfg = VedItemCfg.ofVisel( visel.id(), visel.factoryId(), visel.params(), visel.props() );
      result.add( cfg );
    }
    return result;
  }

  /**
   * Returns actor configuration list.<br>
   *
   * @param aActorIds {@link IStringList} - list of actor ids
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return IStridablesList&lt;IVedItemCfg> - actor configuration list
   */
  public static IStridablesList<IVedItemCfg> listActorConfigs( IStringList aActorIds, IVedScreen aVedScreen ) {
    IStridablesListEdit<IVedItemCfg> result = new StridablesList<>();
    for( String id : aActorIds ) {
      IVedActor actor = aVedScreen.model().actors().list().getByKey( id );
      VedItemCfg cfg = VedItemCfg.ofActor( actor.id(), actor.factoryId(), actor.params(), actor.props() );
      result.add( cfg );
    }
    return result;
  }

  /**
   * Returns copy of visel configuration list.<br>
   *
   * @param aViselIds {@link IStringList} - list of visel ids
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return IStridablesList&lt;VedItemCfg> - copy of visel configuration list
   */
  public static IStridablesList<VedItemCfg> listCopyOfViselConfigs( IStringList aViselIds, IVedScreen aVedScreen ) {
    IStridablesListEdit<VedItemCfg> result = new StridablesList<>();
    for( String id : aViselIds ) {
      IVedVisel visel = aVedScreen.model().visels().list().getByKey( id );
      VedItemCfg cfg = VedItemCfg.ofVisel( visel.id(), visel.factoryId(), visel.params(), visel.props() );
      VedItemCfg newCfg = aVedScreen.model().visels().prepareFromTemplate( cfg );
      result.add( newCfg );
    }
    return result;
  }

  /**
   * Returns copy of visel configurations list.<br>
   *
   * @param aViselCfgs IStridablesList&lt;VedItemCfg> - list of visel configurations
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return IStridablesList&lt;VedItemCfg> - copy of visel configuration list
   */
  public static IStridablesList<VedItemCfg> createCopyOfViselConfigs( IStridablesList<VedItemCfg> aViselCfgs,
      IVedScreen aVedScreen ) {
    IStridablesListEdit<VedItemCfg> result = new StridablesList<>();
    for( VedItemCfg cfg : aViselCfgs ) {
      VedItemCfg config = VedItemCfg.ofVisel( cfg.id(), cfg.factoryId(), cfg.params(), cfg.propValues() );
      VedItemCfg newCfg = aVedScreen.model().visels().prepareFromTemplate( config );
      result.add( newCfg );
    }
    return result;
  }

  /**
   * Возвращает визуальный элемент по ИДу или <code>null</code>.
   *
   * @param aViselId String - ИД визуального элемента
   * @param aVedScreen {@link IVedScreen} - экран редактирования
   * @return VedAbstractVisel - визуальный элемент или <code>null</code> если таковой отсутствует
   */
  public static VedAbstractVisel findVisel( String aViselId, IVedScreen aVedScreen ) {
    return aVedScreen.model().visels().list().findByKey( aViselId );
  }

  /**
   * Вычисляет описывающий визель прямоугольник в экранных координатах.<br>
   * Например, если визель повернут, а экран нет, то описывающий прямоугольник не будет совпадать с
   * {@link IVedVisel#bounds()}.
   *
   * @param aVisel {@link VedAbstractVisel} - визуальный элемент
   * @param aVedScreen {@link IVedScreen} - экран редактирования
   * @return {@link ID2Rectangle} - описывающий визель прямоугольник в экранных координатах
   */
  public static ID2Rectangle calcViselScreenRect( VedAbstractVisel aVisel, IVedScreen aVedScreen ) {
    ID2Rectangle vr = aVisel.bounds();
    IVedCoorsConverter converter = aVedScreen.view().coorsConverter();
    ID2Point[] points = new ID2Point[4];
    points[0] = converter.visel2Screen( 0, 0, aVisel );
    points[1] = converter.visel2Screen( vr.width(), 0, aVisel );
    points[2] = converter.visel2Screen( vr.width(), vr.height(), aVisel );
    points[3] = converter.visel2Screen( 0, vr.height(), aVisel );

    double minX = points[0].x();
    double minY = points[0].y();
    double maxX = points[0].x();
    double maxY = points[0].y();

    for( int i = 0; i < 4; i++ ) {
      if( points[i].x() < minX ) {
        minX = points[i].x();
      }
      if( points[i].y() < minY ) {
        minY = points[i].y();
      }
      if( points[i].x() > maxX ) {
        maxX = points[i].x();
      }
      if( points[i].y() > maxY ) {
        maxY = points[i].y();
      }
    }
    return new D2Rectangle( minX, minY, maxX - minX, maxY - minY );
  }

  /**
   * Вычисляет описывающий группу визелей прямоугольник в экранных координатах.<br>
   *
   * @param aViselIds {@link IStringList} - список идентификаторов визуальных элементов
   * @param aVedScreen {@link IVedScreen} - экран редактирования
   * @return {@link ID2Rectangle} - описывающий группу визелей прямоугольник в экранных координатах
   */
  public static ID2Rectangle calcGroupScreenRect( IStringList aViselIds, IVedScreen aVedScreen ) {
    ID2Rectangle result = null;
    IStridablesList<VedAbstractVisel> visels = aVedScreen.model().visels().list();
    for( String id : aViselIds ) {
      VedAbstractVisel visel = visels.getByKey( id );
      ID2Rectangle vr = calcViselScreenRect( visel, aVedScreen );
      if( result == null ) {
        result = vr;
      }
      else {
        result = D2GeometryUtils.union( vr, result );
      }
    }
    return result;
  }

  /**
   * No subclasses.
   */
  private VedScreenUtils() {
    // nop
  }

}
