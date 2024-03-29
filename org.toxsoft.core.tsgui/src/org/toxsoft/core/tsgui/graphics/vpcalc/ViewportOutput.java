package org.toxsoft.core.tsgui.graphics.vpcalc;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IViewportOutput} implementation.
 *
 * @author hazard157
 */
public final class ViewportOutput
    implements IViewportOutput {

  private final GenericChangeEventer eventer;
  private final D2Convertor          convertor = new D2Convertor();

  private ID2Conversion     d2Conv         = ID2Conversion.NONE;
  private ITsRectangle      drawArea       = ITsRectangle.MINRECT;
  private ScrollBarSettings horBarSettings = new ScrollBarSettings();
  private ScrollBarSettings verBarSettings = new ScrollBarSettings();

  /**
   * Constructor.
   */
  public ViewportOutput() {
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
   * @param aDrawRect {@link ITsRectangle} - the drawing area rectangle in pixels
   * @param aHor {@link ScrollBarSettings} - the horizontal scroll bar parameters
   * @param aVer {@link ScrollBarSettings} - the vertical scroll bar parameters
   * @return boolean - <code>true</code> when at least one field actually changed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public boolean setParams( ID2Conversion aConv, ITsRectangle aDrawRect, ScrollBarSettings aHor,
      ScrollBarSettings aVer ) {
    TsNullArgumentRtException.checkNulls( aConv, aDrawRect, aHor, aVer );
    boolean wasChange = false;
    if( !d2Conv.equals( aConv ) ) {
      d2Conv = new D2Conversion( aConv );
      convertor.setConversion( d2Conv );
      wasChange = true;
    }
    if( !drawArea.equals( aDrawRect ) ) {
      drawArea = new TsRectangle( aDrawRect );
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
    if( wasChange ) {
      eventer.fireChangeEvent();
    }
    return wasChange;
  }

  // ------------------------------------------------------------------------------------
  // IViewportOutput
  //

  @Override
  public ID2Conversion conversion() {
    return d2Conv;
  }

  // @Override
  // public ID2Convertor converter() {
  // return convertor;
  // }

  @Override
  public ScrollBarSettings horBarSettings() {
    return horBarSettings;
  }

  @Override
  public ScrollBarSettings verBarSettings() {
    return verBarSettings;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return eventer;
  }

}
