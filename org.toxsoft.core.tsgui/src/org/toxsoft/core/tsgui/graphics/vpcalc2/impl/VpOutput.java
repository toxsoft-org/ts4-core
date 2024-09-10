package org.toxsoft.core.tsgui.graphics.vpcalc2.impl;

import org.toxsoft.core.tsgui.graphics.vpcalc2.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVpOutput} implementation.
 *
 * @author hazard157
 */
public final class VpOutput
    implements IVpOutput {

  private final GenericChangeEventer eventer;
  private final D2Convertor          convertor = new D2Convertor();

  private ID2Conversion   d2Conv         = ID2Conversion.NONE;
  private ScrollBarCfg    horBarSettings = new ScrollBarCfg();
  private ScrollBarCfg    verBarSettings = new ScrollBarCfg();
  private TsRectangleEdit contDrawBounds = new TsRectangleEdit();

  /**
   * Constructor.
   */
  public VpOutput() {
    eventer = new GenericChangeEventer( this );
    convertor.setConversion( d2Conv );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the values and fires event if something actually changes.
   *
   * @param aConv {@link ID2Conversion} - transformation to apply to the content before painting
   * @param aHor {@link IScrollBarCfg} - the horizontal scroll bar parameters
   * @param aVer {@link IScrollBarCfg} - the vertical scroll bar parameters
   * @param aBounds {@link ITsRectangle} - content bounding rectangle
   * @return boolean - <code>true</code> when at least one field actually changed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public boolean setParams( ID2Conversion aConv, IScrollBarCfg aHor, IScrollBarCfg aVer, ITsRectangle aBounds ) {
    TsNullArgumentRtException.checkNulls( aConv, aHor, aVer, aBounds );
    boolean wasChange = false;
    if( !d2Conv.equals( aConv ) ) {
      d2Conv = new D2Conversion( aConv );
      convertor.setConversion( d2Conv );
      wasChange = true;
    }
    if( !horBarSettings.equals( aHor ) ) {
      horBarSettings.copyFrom( aHor );
      wasChange = true;
    }
    if( !verBarSettings.equals( aVer ) ) {
      verBarSettings.copyFrom( aVer );
      wasChange = true;
    }
    if( !contDrawBounds.equals( aBounds ) ) {
      contDrawBounds.setRect( aBounds );
      wasChange = true;
    }
    if( wasChange ) {
      eventer.fireChangeEvent();
    }
    return wasChange;
  }

  // ------------------------------------------------------------------------------------
  // IVpOutput
  //

  @Override
  public ID2Conversion d2Conv() {
    return d2Conv;
  }

  @Override
  public IScrollBarCfg horBar() {
    return horBarSettings;
  }

  @Override
  public IScrollBarCfg verBar() {
    return verBarSettings;
  }

  @Override
  public ITsRectangle getContentDrawingBounds() {
    return new TsRectangle( contDrawBounds );
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return eventer;
  }

}
