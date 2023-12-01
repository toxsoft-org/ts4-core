package org.toxsoft.core.tsgui.ved.screen.asp;

import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.asp.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.editor.IVedViselSelectionManager.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Edit cut/copy/paste operations.
 * <p>
 * Handles the actions:
 * <ul>
 * <li>{@link #ACTID_CUT};</li>
 * <li>{@link #ACTID_COPY}.</li>
 * <li>{@link #ACTID_PASTE}.</li>
 * </ul>
 *
 * @author vs
 */
public class VedAspCopyPaste
    extends MethodPerActionTsActionSetProvider
    implements ITsGuiContextable {

  /**
   * FIXME There's a conceptual problem here: when searching for the associated actors of the VISELs (selected to be
   * copied) right now we have only one method {@link VedAbstractActor#listBoundViselIds()}. But after copied VED items
   * as a configuration, we must change IDs and restore VISEL-Actor associations (binding). Allas, we have no API means
   * for this task!
   */
  public void fooJustForCommant() {
    // see method comment
  }

  /**
   * ID of action {@link #ACDEF_CUT}.
   */
  public static final String ACTID_CUT = "ved.cut"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_COPY}.
   */
  public static final String ACTID_COPY = "ved.copy"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_PASTE}.
   */
  public static final String ACTID_PASTE = "ved.paste"; //$NON-NLS-1$

  /**
   * Action: copy selected visels and associated actors to the internal buffer and delete it from model.
   */
  public static final ITsActionDef ACDEF_CUT = ofPush2( ACTID_CUT, //
      STR_EDIT_CUT, STR_EDIT_CUT_D, ICONID_EDIT_CUT );

  /**
   * Action: copy selected visels and associated actors to the internal buffer.
   */
  public static final ITsActionDef ACDEF_COPY = ofPush2( ACTID_COPY, //
      STR_EDIT_COPY, STR_EDIT_COPY_D, ICONID_EDIT_COPY );

  /**
   * Action: copy selected visels and associated actors to the internal buffer.
   */
  public static final ITsActionDef ACDEF_PASTE = ofPush2( ACTID_PASTE, //
      STR_EDIT_PASTE, STR_EDIT_PASTE_D, ICONID_EDIT_PASTE );

  private final IVedScreen vedScreen;

  private final IVedViselSelectionManager selectionManager;

  private VedAbstractVisel activeVisel = null;

  private final IStridablesListEdit<VedItemCfg> visels2paste = new StridablesList<>();

  private final IStridablesListEdit<VedItemCfg> actors2paste = new StridablesList<>();

  ITsPoint mouseCoords = null;

  /**
   * Constructor.
   *
   * @param aVedScreen {@link IVedScreen} - the screen to handle
   * @param aSelectionManager IVedViselSelectionManager - manager of the selected visels
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAspCopyPaste( IVedScreen aVedScreen, IVedViselSelectionManager aSelectionManager ) {
    vedScreen = TsNullArgumentRtException.checkNull( aVedScreen );
    selectionManager = aSelectionManager;
    selectionManager.genericChangeEventer().addListener( this::onSelectionChanged );
    defineAction( ACDEF_CUT, this::doCut );
    defineAction( ACDEF_COPY, this::doCopy );
    defineAction( ACDEF_PASTE, this::doPaste );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // MethodPerActionTsActionSetProvider
  //

  @Override
  public boolean isActionEnabled( String aActionId ) {
    if( aActionId.equals( ACDEF_CUT.id() ) ) {
      if( activeVisel == null ) {
        return selectionManager.selectionKind() != ESelectionKind.NONE;
      }
    }
    if( aActionId.equals( ACDEF_COPY.id() ) ) {
      if( activeVisel == null ) {
        return selectionManager.selectionKind() != ESelectionKind.NONE;
      }
    }
    if( aActionId.equals( ACDEF_PASTE.id() ) ) {
      return visels2paste.size() > 0;
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает активный визуальный элемент.<br>
   * От этого зависит доступность некоторых действий набора.
   *
   * @param aVisel {@link VedAbstractVisel} - визуальный элемент, м.б. <b>null</b>
   */
  public void setActiveVisel( VedAbstractVisel aVisel ) {
    activeVisel = aVisel;
    actionsStateEventer().fireChangeEvent();
  }

  /**
   * Задает координаты мыши.<br>
   * При вызове действий из данного набора, часто имеет значение положение курсора мыши.
   *
   * @param aCoords {@link ITsPoint} - координаты мыши
   */
  public void setMouseCoords( ITsPoint aCoords ) {
    mouseCoords = new TsPoint( aCoords );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void doCut() {
    doCopy();

    vedScreen.model().visels().eventer().pauseFiring();
    vedScreen.model().actors().eventer().pauseFiring();

    for( VedItemCfg cfg : visels2paste ) {
      vedScreen.model().visels().remove( cfg.id() );
    }
    for( VedItemCfg cfg : actors2paste ) {
      vedScreen.model().actors().remove( cfg.id() );
    }

    vedScreen.model().visels().eventer().resumeFiring( true );
    vedScreen.model().actors().eventer().resumeFiring( true );

    vedScreen.view().getControl().setMenu( null );
    mouseCoords = null;
    setActiveVisel( null );
  }

  void doCopy() {
    visels2paste.clear();
    actors2paste.clear();
    if( activeVisel != null ) {
      VedItemCfg cfg;
      cfg = VedItemCfg.ofVisel( activeVisel.id(), activeVisel.factoryId(), activeVisel.params(), activeVisel.props() );
      visels2paste.add( cfg );
      IStringList aidList = VedScreenUtils.viselActorIds( activeVisel.id(), vedScreen );
      actors2paste.addAll( VedScreenUtils.listActorConfigs( aidList, vedScreen ) );
    }
    else {
      IStringList vidList = selectionManager.selectedViselIds();
      visels2paste.addAll( VedScreenUtils.listViselConfigs( vidList, vedScreen ) );
      for( String vid : vidList ) {
        IStringList aidList = VedScreenUtils.viselActorIds( vid, vedScreen );
        actors2paste.addAll( VedScreenUtils.listActorConfigs( aidList, vedScreen ) );
      }
    }
    vedScreen.view().getControl().setMenu( null );
    setActiveVisel( null );
  }

  void doPaste() {
    double dx = 0;
    double dy = 0;
    if( visels2paste.size() > 0 ) {
      double minX = visels2paste.first().propValues().getDouble( PROPID_X );
      double minY = visels2paste.first().propValues().getDouble( PROPID_Y );
      for( VedItemCfg cfg : visels2paste ) {
        double x = cfg.propValues().getDouble( PROPID_X );
        double y = cfg.propValues().getDouble( PROPID_Y );
        if( x < minX ) {
          minX = x;
        }
        if( y < minY ) {
          minY = y;
        }
      }

      if( mouseCoords != null ) {
        dx = mouseCoords.x() - minX;
        dy = mouseCoords.y() - minY;
      }
    }

    vedScreen.model().visels().eventer().pauseFiring();
    vedScreen.model().actors().eventer().pauseFiring();
    for( VedItemCfg vCfg : visels2paste ) {
      VedItemCfg viselCfg = vedScreen.model().visels().prepareFromTemplate( vCfg );
      viselCfg.propValues().setDouble( PROPID_X, viselCfg.propValues().getDouble( PROPID_X ) + dx );
      viselCfg.propValues().setDouble( PROPID_Y, viselCfg.propValues().getDouble( PROPID_Y ) + dy );
      vedScreen.model().visels().create( viselCfg );

      IStridablesList<VedItemCfg> actConfs = VedScreenUtils.viselActorsConfigs( vCfg.id(), actors2paste, vedScreen );
      for( VedItemCfg cfg : actConfs ) {
        VedItemCfg newCfg = vedScreen.model().actors().prepareFromTemplate( cfg );
        String str = viselCfg.id();
        newCfg.propValues().setStr( PROPID_VISEL_ID, str );
        vedScreen.model().actors().create( newCfg );
      }
    }
    vedScreen.model().visels().eventer().resumeFiring( true );
    vedScreen.model().actors().eventer().resumeFiring( true );

    mouseCoords = null;
    vedScreen.view().getControl().setMenu( null );
    setActiveVisel( null );
  }

  void onSelectionChanged( @SuppressWarnings( "unused" ) Object aSource ) {
    actionsStateEventer().fireChangeEvent();
  }

}
