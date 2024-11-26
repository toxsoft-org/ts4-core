package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import java.util.*;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.asp.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.helpers.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
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
      VedItemCfg cfg = VedItemCfg.ofItem( item );
      scrCfg.viselCfgs().add( cfg );
    }
    for( VedAbstractActor item : sm.actors().list() ) {
      VedItemCfg cfg = VedItemCfg.ofItem( item );
      scrCfg.actorCfgs().add( cfg );
    }
    scrCfg.canvasCfg().copyFrom( aVedScreen.view().canvasConfig() );
    scrCfg.extraData().copyFrom( aVedScreen.model().extraData() );
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
      aVedScreen.model().extraData().copyFrom( aScreenCfg.extraData() );
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
   * Returns list of ACTOR IDs that may be, but not bounded to visel.<br>
   * <p>
   *
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return {@link IStringList} - list of actor ids that not bounded to visel
   */
  public static IStringList listUnboundActorIds( IVedScreen aVedScreen ) {
    IStringListEdit actorIds = new StringArrayList();
    for( IVedActor actor : aVedScreen.model().actors().list() ) {
      if( actor.isBoudable() && actor.listBoundViselIds().isEmpty() ) {
        actorIds.add( actor.id() );
      }
    }
    return actorIds;
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
   * @return {@link VedItemCfg} - copy of visel configuration
   */
  public static VedItemCfg createCopyOfViselConfig( String aViselId, IVedScreen aVedScreen ) {
    VedAbstractVisel visel = aVedScreen.model().visels().list().getByKey( aViselId );
    VedItemCfg cfg = VedItemCfg.ofItem( visel );
    return aVedScreen.model().visels().prepareFromTemplate( cfg );
  }

  /**
   * Returns copy of actor configuration.<br>
   *
   * @param aActorId String - actor id
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return VedItemCfg - copy of actor configuration
   */
  public static VedItemCfg createCopyOfActorConfig( String aActorId, IVedScreen aVedScreen ) {
    VedAbstractActor actor = aVedScreen.model().actors().list().getByKey( aActorId );
    VedItemCfg cfg = VedItemCfg.ofItem( actor );
    return aVedScreen.model().actors().prepareFromTemplate( cfg );
  }

  /**
   * Returns VISEL configuration list.<br>
   *
   * @param aViselIds {@link IStringList} - list of VISEL IDs
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return IStridablesList&lt;IVedItemCfg> - VISEL configuration list
   */
  public static IStridablesList<IVedItemCfg> listViselConfigs( IStringList aViselIds, IVedScreen aVedScreen ) {
    IStridablesListEdit<IVedItemCfg> result = new StridablesList<>();
    for( String id : aViselIds ) {
      VedAbstractVisel visel = aVedScreen.model().visels().list().getByKey( id );
      VedItemCfg cfg = VedItemCfg.ofItem( visel );
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
      VedAbstractActor actor = aVedScreen.model().actors().list().getByKey( id );
      VedItemCfg cfg = VedItemCfg.ofItem( actor );
      result.add( cfg );
    }
    return result;
  }

  /**
   * Returns copy of VISEL configuration list.<br>
   *
   * @param aViselIds {@link IStringList} - list of VISEL ids
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return IStridablesList&lt;VedItemCfg> - copy of VISEL configuration list
   */
  public static IStridablesList<VedItemCfg> listCopyOfViselConfigs( IStringList aViselIds, IVedScreen aVedScreen ) {
    IStridablesListEdit<VedItemCfg> result = new StridablesList<>();
    for( String id : aViselIds ) {
      VedAbstractVisel visel = aVedScreen.model().visels().list().getByKey( id );
      VedItemCfg cfg = VedItemCfg.ofItem( visel );
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
      VedItemCfg config = new VedItemCfg( cfg );
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
   * Вычисляет описывающий визель прямоугольник в экранных координатах.<br>
   * Например, если визель повернут, а экран нет, то описывающий прямоугольник не будет совпадать с
   * {@link IVedVisel#bounds()}.
   *
   * @param aViselCfg {@link IVedItemCfg} - конфигурация визуального элемента
   * @param aVedScreen {@link IVedScreen} - экран редактирования
   * @return {@link ID2Rectangle} - описывающий визель прямоугольник в экранных координатах
   */
  public static ID2Rectangle calcViselScreenRect( IVedItemCfg aViselCfg, IVedScreen aVedScreen ) {
    ID2Rectangle vr = VedTransformUtils.viselBounds( aViselCfg.propValues() );
    // IVedCoorsConverter converter = aVedScreen.view().coorsConverter();
    ID2Point[] points = new ID2Point[4];
    points[0] = VedTransformUtils.visel2Screen( 0, 0, aViselCfg.propValues() );
    points[1] = VedTransformUtils.visel2Screen( vr.width(), 0, aViselCfg.propValues() );
    points[2] = VedTransformUtils.visel2Screen( vr.width(), vr.height(), aViselCfg.propValues() );
    points[3] = VedTransformUtils.visel2Screen( 0, vr.height(), aViselCfg.propValues() );

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
      if( !visels.hasKey( id ) ) { // если копируется элементы с другого экрана
        return ID2Rectangle.ZERO;
      }
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
   * Вычисляет описывающий группу визелей прямоугольник в экранных координатах.<br>
   *
   * @param aViselConfs {@link IStridablesList} - список идентификаторов визуальных элементов
   * @param aVedScreen {@link IVedScreen} - экран редактирования
   * @return {@link ID2Rectangle} - описывающий группу визелей прямоугольник в экранных координатах
   */
  public static ID2Rectangle calcGroupScreenRect( IStridablesList<IVedItemCfg> aViselConfs, IVedScreen aVedScreen ) {
    ID2Rectangle result = null;
    for( IVedItemCfg cfg : aViselConfs ) {
      ID2Rectangle vr = calcViselScreenRect( cfg, aVedScreen );
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
   * Генерирует новый ИД для переданной конфигурации элемента.
   *
   * @param aItemCfg IVedItemCfg - конфигурация элемента
   * @param aCfgList IStridablesList&lt;IVedItemCfg> - список существющих конфигураций
   * @param aFactory IVedItemFactoryBase&lt;VedAbstractItem> - фабрика элемента
   * @return String - новый ИД элемента
   */
  public static Pair<String, String> generateIdForItemConfig( IVedItemCfg aItemCfg,
      IStridablesList<IVedItemCfg> aCfgList, IVedItemFactoryBase<? extends VedAbstractItem> aFactory ) {
    TsNullArgumentRtException.checkNull( aItemCfg );
    TsItemNotFoundRtException.checkNull( aFactory );
    // generate ID
    String id;
    int counter = 0;
    String prefix = StridUtils.getLast( aItemCfg.factoryId() );
    prefix = prefix.toLowerCase().substring( 0, 1 ) + prefix.substring( 1 ); // convert first char to lower case
    String name;
    do {
      id = prefix + Integer.toString( ++counter ); // "prefixNN"
      name = aFactory.nmName() + ' ' + Integer.toString( counter ); // "Factory name NN"
    } while( aCfgList.hasKey( id ) || hasItemWithName( name, aCfgList ) );
    return new Pair<>( id, name );
  }

  /**
   * Возвращает признак того, существует ли в наборе элемент с указанным именем.
   *
   * @param aName String - имя элемента
   * @param aCfgList IList&lt;IVedItemCfg> - список конфигураций
   * @return <b>true</b> - элемент с таким именем существует<br>
   *         <b>false</b> - нет такого элемента
   */
  public static boolean hasItemWithName( String aName, IList<IVedItemCfg> aCfgList ) {
    for( IVedItemCfg item : aCfgList ) {
      if( item.propValues().hasKey( PROPID_NAME ) ) {
        if( item.propValues().getStr( PROPID_NAME ).equals( aName ) ) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Возвращает визуальный элемент, содержащий точку с указанными SWT координатами или <code>null</code>.<br>
   * Перебор визуальных элементов осуществляется в соответствии с z-order в прямом или обратном порядке, в зависисмости
   * от значения аргумента aForward.
   *
   * @param aSwtX int - x координата точки
   * @param aSwtY int - y координата точки
   * @param aVedScreen {@link IVedScreen} - экран редактирования
   * @param aForward boolean - признак перебора списка в прямом направ
   * @return VedAbstractVisel - визуальный элемент, содержащий точку с указанными SWT координатами или <code>null</code>
   */
  public static VedAbstractVisel itemByPoint( int aSwtX, int aSwtY, IVedScreen aVedScreen, boolean aForward ) {
    IVedCoorsConverter converter = aVedScreen.view().coorsConverter();
    IStridablesList<VedAbstractVisel> visels = aVedScreen.model().visels().list();
    Iterable<VedAbstractVisel> it = visels;
    if( !aForward ) {
      it = new ListBackwardIterator<>( visels );
    }
    for( VedAbstractVisel item : it ) {
      ID2Point d2p = converter.swt2Visel( aSwtX, aSwtY, item );
      if( item.isYours( d2p.x(), d2p.y() ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает список ИДов визуальных элементов, отсортированных в z-порядке.
   *
   * @param aIds {@link IStringList} - список ИДов, которые необходимо отсортировать
   * @param aVedScreen {@link IVedScreen} - экран редактора
   * @return {@link IStringList} - список ИДов визуальных элементов, отсортированных в z-порядке
   */
  public static IStringList sortViselIdsByZorder( IStringList aIds, IVedScreen aVedScreen ) {
    IStringListEdit result = new StringArrayList();
    Comparator<String> comparator = ( aId1, aId2 ) -> {
      int idx1 = aVedScreen.model().visels().list().ids().indexOf( aId1 );
      int idx2 = aVedScreen.model().visels().list().ids().indexOf( aId2 );
      return Integer.compare( idx1, idx2 );
    };
    ElemArrayList<String> ll = new ElemArrayList<>();
    ll.addAll( aIds );
    ListReorderer<String, IListEdit<String>> lr = new ListReorderer<>( ll );
    lr.sort( comparator );
    for( String str : lr.list() ) {
      result.add( str );
    }
    return result;
  }

  /**
   * Возвращает список визуальных элементов по их идентификаторам.
   *
   * @param aViselIds {@link IStringList} - список ИДов визуальных элементов
   * @param aVedScreen {@link IVedScreen} -экран редактирования
   * @return IStridablesList&lt;IVedVisel> - список визуальных элементов
   */
  public static IStridablesList<IVedVisel> listVisels( IStringList aViselIds, IVedScreen aVedScreen ) {
    IStridablesListEdit<IVedVisel> visels = new StridablesList<>();
    for( String id : aViselIds ) {
      VedAbstractVisel v = findVisel( id, aVedScreen );
      if( v != null ) {
        visels.add( findVisel( id, aVedScreen ) );
      }
      else {
        System.out.println( "Visel not found: " + id ); //$NON-NLS-1$
      }
    }
    return visels;
  }

  /**
   * Возвращает список "висячих" акторов.<br>
   * "Висячим" - считается актор, который не привязан ни к одному из существующих визелей.
   *
   * @param aVedScreen {@link IVedScreen} - экран мнемосхемы
   * @return IStridablesList&lt;IVedItem> - список акторов не привязанных к визелям.
   */
  public static IStridablesList<IVedItem> listHangedActors( IVedScreen aVedScreen ) {
    IStridablesListEdit<IVedItem> result = new StridablesList<>();
    for( IVedActor actor : aVedScreen.model().actors().list() ) {
      if( actor.props().propDefs().hasElem( PROP_VISEL_ID ) ) {
        if( !actor.props().hasValue( PROP_VISEL_ID.id() ) ) {
          result.add( actor );
          continue;
        }
        IAtomicValue av = actor.props().getValue( PROP_VISEL_ID.id() );
        if( !av.isAssigned() ) {
          result.add( actor );
          continue;
        }
        IVedVisel visel = findVisel( av.asString(), aVedScreen );
        if( visel == null ) {
          result.add( actor );
          continue;
        }
        if( actor.props().propDefs().hasElem( PROP_VISEL_PROP_ID ) ) {
          if( !actor.props().hasValue( PROP_VISEL_PROP_ID.id() ) ) {
            result.add( actor );
            continue;
          }
          av = actor.props().getValue( PROP_VISEL_PROP_ID.id() );
          if( !av.isAssigned() ) {
            result.add( actor );
            continue;
          }
          if( !visel.props().hasKey( av.asString() ) ) {
            result.add( actor );
            continue;
          }
        }
      }

    }
    return result;
  }

  /**
   * Возвращает список визуальных элементов, к которым не привязан ни один актор (а дожен).
   *
   * @param aVedScreen {@link IVedScreen} - экран мнемосхемы
   * @return IStridablesList&lt;IVedItem> - список визуальных элементов, к которым не привязан ни один актор
   */
  public static IStridablesList<IVedVisel> listNonlinkedVisels( IVedScreen aVedScreen ) {
    IStridablesListEdit<IVedVisel> result = new StridablesList<>();
    for( IVedVisel visel : aVedScreen.model().visels().list() ) {
      if( visel.props().hasKey( PROPID_IS_ACTOR_MANDATORY ) ) {
        IAtomicValue av = visel.props().getValue( PROPID_IS_ACTOR_MANDATORY );
        if( av.isAssigned() && !av.asBool() ) {
          continue;
        }
      }
      if( VedScreenUtils.viselActorIds( visel.id(), aVedScreen ).size() <= 0 ) {
        System.out.println( "Hanging visel: " + visel.id() ); //$NON-NLS-1$
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
