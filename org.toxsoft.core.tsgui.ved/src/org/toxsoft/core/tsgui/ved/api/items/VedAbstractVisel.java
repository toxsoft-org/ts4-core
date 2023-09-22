package org.toxsoft.core.tsgui.ved.api.items;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedVisel} base implementation.
 *
 * @author hazard157
 */
public class VedAbstractVisel
    extends VedAbstractItem
    implements IVedVisel {

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public VedAbstractVisel( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs ) {
    super( aConfig, aPropDefs );
  }

  // ------------------------------------------------------------------------------------
  // IPointsHost
  //

  @Override
  public boolean isYours( double aX, double aY ) {
    // TODO Auto-generated method stub
    return false;
  }

  // ------------------------------------------------------------------------------------
  // IDisplayable
  //

  @Override
  public void paint( GC aGc ) {
    // TODO Auto-generated method stub

  }

  @Override
  public ITsRectangle bounds() {
    // TODO Auto-generated method stub
    return null;
  }

  // ------------------------------------------------------------------------------------
  // ID2Conversionable
  //

  @Override
  public ID2Conversion getConversion() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setConversion( ID2Conversion aConversion ) {
    // TODO Auto-generated method stub

  }

  // ------------------------------------------------------------------------------------
  // IVisel
  //

}
