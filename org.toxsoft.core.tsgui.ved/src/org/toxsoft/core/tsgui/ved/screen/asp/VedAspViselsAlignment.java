package org.toxsoft.core.tsgui.ved.screen.asp;

import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.asp.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.editor.IVedViselSelectionManager.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Set of the actions to align group of selected visels.
 * <p>
 *
 * @author vs
 */
// TODO разобраться с ситуацией когда visel fullcrum != ETsFulcrum.LEFT_TOP
// Нужно ли в методе visleToScreen вместо 0, 0 передавать координаты опорной точки
public class VedAspViselsAlignment
    extends MethodPerActionTsActionSetProvider
    implements ITsGuiContextable {

  static final String ACTID_ALIGN_LEFT       = "visel.align.left";       //$NON-NLS-1$
  static final String ACTID_ALIGN_RIGHT      = "visel.align.right";      //$NON-NLS-1$
  static final String ACTID_ALIGN_TOP        = "visel.align.top";        //$NON-NLS-1$
  static final String ACTID_ALIGN_BOTTOM     = "visel.align.bottom";     //$NON-NLS-1$
  static final String ACTID_ALIGN_HOR_CENTER = "visel.align.hor.center"; //$NON-NLS-1$
  static final String ACTID_ALIGN_VER_CENTER = "visel.align.ver.center"; //$NON-NLS-1$

  /**
   * Action: align group of selected visels to left edge.
   */
  public static final ITsActionDef ACDEF_ALIGN_LEFT = ofPush2( ACTID_ALIGN_LEFT, //
      STR_ALIGN_LEFT, STR_ALIGN_LEFT_D, ICONID_ALIGN_LEFT );

  /**
   * Action: align group of selected visels to right edge.
   */
  public static final ITsActionDef ACDEF_ALIGN_RIGHT = ofPush2( ACTID_ALIGN_RIGHT, //
      STR_ALIGN_RIGHT, STR_ALIGN_RIGHT_D, ICONID_ALIGN_RIGHT );

  /**
   * Action: align group of selected visels to top edge.
   */
  public static final ITsActionDef ACDEF_ALIGN_TOP = ofPush2( ACTID_ALIGN_TOP, //
      STR_ALIGN_TOP, STR_ALIGN_TOP_D, ICONID_ALIGN_TOP );

  /**
   * Action: align group of selected visels to bottom edge.
   */
  public static final ITsActionDef ACDEF_ALIGN_BOTTOM = ofPush2( ACTID_ALIGN_BOTTOM, //
      STR_ALIGN_BOTTOM, STR_ALIGN_BOTTOM_D, ICONID_ALIGN_BOTTOM );

  /**
   * Action: align group of selected visels to bottom edge.
   */
  public static final ITsActionDef ACDEF_ALIGN_HOR_CENTER = ofPush2( ACTID_ALIGN_HOR_CENTER, //
      STR_ALIGN_HOR_CENTER, STR_ALIGN_HOR_CENTER_D, ICONID_ALIGN_HOR_CENTER );

  /**
   * Action: align group of selected visels to bottom edge.
   */
  public static final ITsActionDef ACDEF_ALIGN_VER_CENTER = ofPush2( ACTID_ALIGN_VER_CENTER, //
      STR_ALIGN_VER_CENTER, STR_ALIGN_VER_CENTER_D, ICONID_ALIGN_VER_CENTER );

  private final IVedScreen vedScreen;

  private final IVedViselSelectionManager selectionManager;

  private VedAbstractVisel anchorVisel = null;

  /**
   * Constructor.
   *
   * @param aVedScreen {@link IVedScreen} - the screen to handle
   * @param aSelectionManager IVedViselSelectionManager - manager of the selected visels
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAspViselsAlignment( IVedScreen aVedScreen, IVedViselSelectionManager aSelectionManager ) {
    vedScreen = TsNullArgumentRtException.checkNull( aVedScreen );
    selectionManager = aSelectionManager;
    selectionManager.genericChangeEventer().addListener( this::onSelectionChanged );
    defineAction( ACDEF_ALIGN_LEFT, this::doAlignLeft );
    defineAction( ACDEF_ALIGN_RIGHT, this::doAlignRight );
    defineAction( ACDEF_ALIGN_TOP, this::doAlignTop );
    defineAction( ACDEF_ALIGN_BOTTOM, this::doAlignBottom );
    defineAction( ACDEF_ALIGN_HOR_CENTER, this::doAlignHorCenter );
    defineAction( ACDEF_ALIGN_VER_CENTER, this::doAlignVerCenter );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiConextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // MethodPerActionTsActionSetProvider
  //

  @Override
  protected boolean doIsActionEnabled( ITsActionDef aActionDef ) {
    return selectionManager.selectionKind() == ESelectionKind.MULTI;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Устанавливает элемент, по которому будет осуществляться выравнивание.
   *
   * @param aVisel {@link VedAbstractVisel} - визуальный элемент
   */
  public void setAnchorVisel( VedAbstractVisel aVisel ) {
    anchorVisel = aVisel;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  double fulcrum2x( ETsFulcrum aFulcrum, IVedVisel aVisel ) {
    double anchorX = 0;
    if( aFulcrum != null ) {
      if( aFulcrum.isRight() ) {
        anchorX = aVisel.bounds().width();
      }
      if( aFulcrum.isHorizontalCenter() ) {
        anchorX = aVisel.bounds().width() / 2.;
      }
    }
    return anchorX;
  }

  double fulcrum2y( ETsFulcrum aFulcrum, IVedVisel aVisel ) {
    double anchorY = 0;
    if( aFulcrum != null ) {
      if( aFulcrum.isBottom() ) {
        anchorY = aVisel.bounds().height();
      }
      if( aFulcrum.isVerticalCenter() ) {
        anchorY = aVisel.bounds().height() / 2.;
      }
    }
    return anchorY;
  }

  void doAlign( ETsFulcrum aFulcrumX, ETsFulcrum aFulcrumY ) {
    TsIllegalStateRtException.checkTrue( anchorVisel == null );
    IVedCoorsConverter converter = vedScreen.view().coorsConverter();
    double anchorX = fulcrum2x( aFulcrumX, anchorVisel );
    double anchorY = fulcrum2y( aFulcrumY, anchorVisel );
    ID2Point ap = converter.visel2Screen( anchorX, anchorY, anchorVisel );

    IStridablesList<VedAbstractVisel> visels = listSelectedVisels();
    for( VedAbstractVisel visel : visels ) {
      if( visel != anchorVisel ) {
        double viselX = fulcrum2x( aFulcrumX, visel );
        double viselY = fulcrum2y( aFulcrumY, visel );
        ID2Point vp = converter.visel2Screen( viselX, viselY, visel );
        double dx = 0;
        if( aFulcrumX != null ) {
          dx = ap.x() - vp.x();
        }
        double dy = 0;
        if( aFulcrumY != null ) {
          dy = ap.y() - vp.y();
        }
        double xVal = visel.props().getDouble( PROPID_X ) + dx;
        double yVal = visel.props().getDouble( PROPID_Y ) + dy;
        visel.props().setPropPairs( PROPID_X, avFloat( xVal ), PROPID_Y, avFloat( yVal ) );
      }
    }
    anchorVisel = null;
  }

  void doAlignLeft() {
    doAlign( ETsFulcrum.LEFT_TOP, null );
  }

  void doAlignRight() {
    doAlign( ETsFulcrum.RIGHT_BOTTOM, null );
  }

  void doAlignTop() {
    doAlign( null, ETsFulcrum.RIGHT_TOP );
  }

  void doAlignBottom() {
    doAlign( null, ETsFulcrum.RIGHT_BOTTOM );
  }

  void doAlignHorCenter() {
    doAlign( ETsFulcrum.TOP_CENTER, null );
  }

  void doAlignVerCenter() {
    doAlign( null, ETsFulcrum.LEFT_CENTER );
  }

  IStridablesList<VedAbstractVisel> listSelectedVisels() {
    IStridablesListEdit<VedAbstractVisel> result = new StridablesList<>();
    IStringList idsList = selectionManager.selectedViselIds();
    IStridablesList<VedAbstractVisel> visels = vedScreen.model().visels().list();
    for( String id : idsList ) {
      result.add( visels.getByKey( id ) );
    }
    return result;
  }

  void onSelectionChanged( @SuppressWarnings( "unused" ) Object aSource ) {
    actionsStateEventer().fireChangeEvent();
  }

}
