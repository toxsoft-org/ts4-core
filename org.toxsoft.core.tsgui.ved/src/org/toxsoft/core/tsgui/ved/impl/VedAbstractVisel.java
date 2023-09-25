package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tsgui.ved.api.helpers.*;
import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedVisel} base implementation.
 *
 * @author hazard157, vs
 */
public abstract class VedAbstractVisel
    extends VedAbstractItem
    implements IVedVisel, ID2Portable, ID2Resizable, ITsGuiContextable {

  private ID2Conversion d2Conv = ID2Conversion.NONE;

  private TsRectangleEdit tsRect = new TsRectangleEdit( 0, 0, 1, 1 );

  private ID2Rectangle d2rect = new D2Rectangle( 0, 0, 1, 1 );

  private double originX = 0;

  private double originY = 0;

  private double width = 1;

  private double height = 1;

  private final ITsGuiContext tsContext;

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aTsContext {@link ITsGuiContext} - the corresponding context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public VedAbstractVisel( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, ITsGuiContext aTsContext ) {
    super( aConfig, aPropDefs );
    tsContext = aTsContext;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IDisplayable
  //

  @Override
  public ITsRectangle bounds() {
    return tsRect;
  }

  @Override
  public boolean isYours( double aX, double aY ) {
    return bounds().contains( (int)aX, (int)aY );
  }

  // ------------------------------------------------------------------------------------
  // ID2Conversionable
  //

  @Override
  public ID2Conversion getConversion() {
    return d2Conv;
  }

  @Override
  public void setConversion( ID2Conversion aConversion ) {
    d2Conv = new D2Conversion( aConversion );
  }

  // ------------------------------------------------------------------------------------
  // ID2Portable
  //

  @Override
  public double originX() {
    return originX;
  }

  @Override
  public double originY() {
    return originY;
  }

  @Override
  public void setLocation( double aX, double aY ) {
    originX = aX;
    originY = aY;
    d2rect = new D2Rectangle( aX, aY, d2rect.width(), d2rect.height() );
    updateTsRect();

    IStringMapEdit<IAtomicValue> values = new StringMap<>();
    values.put( VedAbstractViselFactory.FID_VISEL_X, AvUtils.avFloat( originX ) );
    values.put( VedAbstractViselFactory.FID_VISEL_Y, AvUtils.avFloat( originY ) );
    props().setProps( values );
  }

  // ------------------------------------------------------------------------------------
  // ID2Resisable
  //

  @Override
  public double width() {
    return width;
  }

  @Override
  public double height() {
    return height;
  }

  @Override
  public void setSize( double aWidth, double aHeight ) {
    width = aWidth;
    height = aHeight;
    d2rect = new D2Rectangle( d2rect.x1(), d2rect.y1(), aWidth, aHeight );
    updateTsRect();
    doOnSizeChanged();
    IStringMapEdit<IAtomicValue> values = new StringMap<>();
    values.put( VedAbstractViselFactory.FID_VISEL_WIDTH, AvUtils.avFloat( width ) );
    values.put( VedAbstractViselFactory.FID_VISEL_HEIGHT, AvUtils.avFloat( height ) );
    props().setProps( values );
  }

  // ------------------------------------------------------------------------------------
  // IVisel
  //

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает элемент, который может быть размещен на плоскости или <b>null</b>
   *
   * @return {@link ID2Portable} - элемент, который может быть размещен на плоскости или <b>null</b>
   */
  public ID2Portable asPortable() {
    return this;
  }

  /**
   * Возвращает элемент, у которого можно менять размеры или <b>null</b>
   *
   * @return {@link ID2Portable} - элемент, у которого можно менять размеры или <b>null</b>
   */
  public ID2Resizable asResizable() {
    return this;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  protected void doOnSizeChanged() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void updateTsRect() {
    tsRect.setRect( (int)d2rect.x1(), (int)d2rect.y1(), (int)d2rect.width(), (int)d2rect.height() );
  }

}
