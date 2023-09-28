package org.toxsoft.core.tsgui.ved.impl;

import static org.toxsoft.core.tsgui.ved.impl.VedAbstractViselFactory.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
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

  private final String factoryId;

  private final IVedViselFactory factory;

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
    factoryId = aConfig.factoryId();
    tsContext = aTsContext;
    // FIXME найти фабрику
    IVedViselFactoriesRegistry fr = tsContext.get( IVedViselFactoriesRegistry.class );
    factory = fr.get( factoryId );
    d2Conv = aConfig.propValues().getValobj( FID_D2CONVERSION );
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
   * Возвращает ИД фабрики создания.
   *
   * @return String - ИД фабрики создания
   */
  public String factoryId() {
    return factoryId;
  }

  /**
   * Возвращает фабрику создания.
   *
   * @return {@link IVedViselFactory} - фабрика создания
   */
  public IVedViselFactory viselFactory() {
    return factory;
  }

  /**
   * Возвращает информацию о типе для инспектора свойств.
   *
   * @return {@link ITinTypeInfo} - информацию о типе для инспектора свойств
   */
  public ITinTypeInfo tinTypeInfo() {
    return factory.typeInfo();
  }

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

  /**
   * Возвращает значение для инспектора свойств.
   *
   * @return {@link ITinValue} - значение для инспектора свойств
   */
  public final ITinValue value() {
    ITinTypeInfo typeInfo = factory.typeInfo();
    return typeInfo.makeValue( this );
  }

  /**
   * Задает новые значения свойств.
   *
   * @param aValue {@link ITinValue} - новые значения свойств
   */
  public final void setValue( ITinValue aValue ) {
    props().propsEventer().pauseFiring();
    for( String vid : aValue.childValues().keys() ) {
      props().setValue( vid, aValue.childValues().getByKey( vid ).atomicValue() );
    }

    originX = props().getDouble( VedAbstractViselFactory.FID_VISEL_X );
    originY = props().getDouble( VedAbstractViselFactory.FID_VISEL_Y );
    width = props().getDouble( VedAbstractViselFactory.FID_VISEL_WIDTH );
    height = props().getDouble( VedAbstractViselFactory.FID_VISEL_HEIGHT );
    d2rect = new D2Rectangle( originX, originY, width, height );
    d2Conv = props().getValobj( FID_D2CONVERSION );
    updateTsRect();
    doOnLocationChanged();
    doOnSizeChanged();
    props().propsEventer().resumeFiring( true );
  }

  /**
   * Возвращает конфигурацию визуального элемента.<br>
   *
   * @return {@link IVedItemCfg} - конфигурация визуального элемента
   */
  public final IVedItemCfg config() {
    IVedItemCfg cfg = new VedItemCfg( id(), factoryId, props() );
    return cfg;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  protected void doOnLocationChanged() {
    // nop
  }

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
