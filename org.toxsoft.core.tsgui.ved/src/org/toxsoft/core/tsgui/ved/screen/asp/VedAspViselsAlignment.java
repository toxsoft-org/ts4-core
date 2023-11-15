package org.toxsoft.core.tsgui.ved.screen.asp;

import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.asp.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.editor.IVedViselSelectionManager.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
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
public class VedAspViselsAlignment
    extends MethodPerActionTsActionSetProvider
    implements ITsGuiContextable {

  static final String ACTID_ALIGN_LEFT  = "visel.align.left";  //$NON-NLS-1$
  static final String ACTID_ALIGN_RIGHT = "visel.align.right"; //$NON-NLS-1$

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
    defineAction( ACDEF_ALIGN_LEFT, this::doAlignLeft );
    defineAction( ACDEF_ALIGN_RIGHT, this::doAlignRight );
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
  public boolean isActionEnabled( String aActionId ) {
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

  void doAlignLeft() {
    TsIllegalStateRtException.checkTrue( anchorVisel == null );
    IStridablesList<VedAbstractVisel> visels = listSelectedVisels();
    IVedCoorsConverter converter = vedScreen.view().coorsConverter();
    ITsPoint tsp = converter.visel2Swt( anchorVisel.bounds().a(), anchorVisel );
    for( VedAbstractVisel visel : visels ) {
      if( visel != anchorVisel ) {
        ITsPoint swtP = converter.visel2Swt( visel.bounds().a(), visel );
        int dx = tsp.x() - swtP.x();
        ID2Point d2p = converter.swt2Visel( dx, 0, visel );
        double xVal = visel.props().getDouble( PROPID_X ) + d2p.x();
        double yVal = visel.props().getDouble( PROPID_Y ) + d2p.y();
        visel.props().setPropPairs( PROPID_X, avFloat( xVal ), PROPID_Y, avFloat( yVal ) );
      }
    }
    anchorVisel = null;
  }

  void doAlignRight() {
    TsIllegalStateRtException.checkTrue( anchorVisel == null );
    IStridablesList<VedAbstractVisel> visels = listSelectedVisels();
    IVedCoorsConverter converter = vedScreen.view().coorsConverter();
    ITsPoint tsp = converter.visel2Swt( anchorVisel.bounds().b(), anchorVisel );
    for( VedAbstractVisel visel : visels ) {
      if( visel != anchorVisel ) {
        ITsPoint swtP = converter.visel2Swt( visel.bounds().b(), visel );
        int dx = tsp.x() - swtP.x();
        ID2Point d2p = converter.swt2Visel( dx, 0, visel );
        double xVal = visel.props().getDouble( PROPID_X ) + d2p.x();
        double yVal = visel.props().getDouble( PROPID_Y ) + d2p.y();
        visel.props().setPropPairs( PROPID_X, avFloat( xVal ), PROPID_Y, avFloat( yVal ) );
      }
    }
    anchorVisel = null;
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

}
