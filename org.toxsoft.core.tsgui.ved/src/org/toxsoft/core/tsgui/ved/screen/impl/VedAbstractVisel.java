package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.helpers.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
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
    doOnLocationChanged();

    IStringMapEdit<IAtomicValue> values = new StringMap<>();
    values.put( PROP_X.id(), AvUtils.avFloat( originX ) );
    values.put( PROP_Y.id(), AvUtils.avFloat( originY ) );
    props().setProps( values );
  }

  // ------------------------------------------------------------------------------------
  // ID2Resizable
  //

  @Override
  public double width() {
    return props().getDouble( PROP_WIDTH );
  }

  @Override
  public double height() {
    return props().getDouble( PROP_HEIGHT );
  }

  @Override
  public void setSize( double aWidth, double aHeight ) {
    props().propsEventer().props().setDouble( PROP_WIDTH, aWidth );
    props().setDouble( PROP_HEIGHT, aHeight );
    d2rect = new D2Rectangle( d2rect.x1(), d2rect.y1(), aWidth, aHeight );
    updateTsRect();
    doOnSizeChanged();
    IStringMapEdit<IAtomicValue> values = new StringMap<>();
    values.put( PROP_WIDTH.id(), AvUtils.avFloat( width ) );
    values.put( PROP_HEIGHT.id(), AvUtils.avFloat( height ) );
    props().setProps( values );
  }

  // ------------------------------------------------------------------------------------
  // IVisel
  //

  // ------------------------------------------------------------------------------------
  // To override
  //

  protected void doOnLocationChanged() {
    // nop
  }

  protected void doOnSizeChanged() {
    // nop
  }

  protected void doOnPropsChanged() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void updateTsRect() {
    tsRect.setRect( (int)d2rect.x1(), (int)d2rect.y1(), (int)d2rect.width(), (int)d2rect.height() );
  }

}
