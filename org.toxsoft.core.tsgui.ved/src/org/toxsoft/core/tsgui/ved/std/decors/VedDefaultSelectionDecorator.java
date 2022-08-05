package org.toxsoft.core.tsgui.ved.std.decors;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * Simple selection decorator.
 * <p>
 *
 * @author vs
 */
public class VedDefaultSelectionDecorator
    extends VedAbstractScreenSelectionDecorator {

  /**
   * Parameter: outline line width in pixels.
   */
  public static final IDataDef OPDEF_LINE_WIDTH = DataDef.create( "LineWidth", INTEGER, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_1 //
  );

  /**
   * Parameter: outline line color as {@link RGB}.
   */
  public static final IDataDef OPDEF_COLOR = DataDef.create( "Color", VALOBJ, //$NON-NLS-1$
      TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.BLUE.rgb() ) //
  );

  /**
   * Parameter: outline line type as {@link ETsLineType}.
   */
  public static final IDataDef OPDEF_LINE_TYPE = DataDef.create( "LineType", VALOBJ, //$NON-NLS-1$
      TSID_KEEPER_ID, ETsLineType.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETsLineType.DASH ) //
  );

  private int         lineWidth;
  private Color       lineColor;
  private ETsLineType lineType;

  /**
   * Constructor for subclasses.
   *
   * @param aScreen {@link IVedScreen} - the screen
   * @param aEnv {@link IVedEnvironment} - the VED nevironment
   */
  public VedDefaultSelectionDecorator( IVedScreen aScreen, IVedEnvironment aEnv ) {
    super( aScreen, aEnv );
    doUpdateOnParamsChange();
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractScreenSelectionDecorator
  //

  @Override
  protected void doUpdateOnParamsChange() {
    lineWidth = OPDEF_LINE_WIDTH.getValue( params() ).asInt();
    if( lineWidth < 1 ) {
      lineWidth = 1;
    }
    RGB rgb = OPDEF_COLOR.getValue( params() ).asValobj();
    lineColor = colorManager().getColor( rgb );
    lineType = OPDEF_LINE_TYPE.getValue( params() ).asValobj();
  }

  // ------------------------------------------------------------------------------------
  // IVedScreenSelectionDecorator
  //

  @Override
  protected void paintSelection( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    aGc.setAdvanced( true );
    aGc.setAntialias( SWT.ON );
    // save old and apply new line drawing params
    int oldStyle = aGc.getLineStyle();
    int oldWidth = aGc.getLineWidth();
    Color oldColor = aGc.getForeground();
    aGc.setLineStyle( lineType.getSwtStyle() );
    aGc.setLineWidth( lineWidth );
    aGc.setForeground( lineColor );
    // apply transform if needed
    Transform oldTransfrom = null;
    Transform t = null;
    ID2Conversion d2conv = vedScreen().getConversion();
    if( d2conv.isConversion() ) {
      oldTransfrom = new Transform( aGc.getDevice() );
      aGc.getTransform( oldTransfrom );
      t = TsGraphicsUtils.conv2transform( d2conv, aGc );
      aGc.setTransform( t );
      t.dispose();
    }
    // draw surrounding rectangle
    ID2Rectangle d2r = aView.outline().bounds();
    int x = (int)d2r.a().x();
    int y = (int)d2r.a().y();
    int w = (int)d2r.width();
    int h = (int)d2r.height();
    aGc.drawRectangle( x - 2, y - 2, w + 4, h + 4 );
    // restore transform
    if( oldTransfrom != null ) { // восстановим старый transform
      aGc.setTransform( oldTransfrom );
      oldTransfrom.dispose();
    }
    // restore line drawing params
    aGc.setLineStyle( oldStyle );
    aGc.setLineWidth( oldWidth );
    aGc.setForeground( oldColor );
  }

}
