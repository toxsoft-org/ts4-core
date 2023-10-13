package org.toxsoft.core.tsgui.bricks.tin.tti;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITsResources.*;
import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.graphics.ITsGraphicsConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * {@link ITinTypeInfo} implementation for {@link RGB} entities.
 *
 * @author hazard157
 */
public class RGBTypeInfo
    extends AbstractTinTypeInfo<RGB> {

  private static final String FID_RED   = "red";   //$NON-NLS-1$
  private static final String FID_GREEN = "green"; //$NON-NLS-1$
  private static final String FID_BLUE  = "blue";  //$NON-NLS-1$

  /**
   * The type information singleton.
   */
  public static final ITinTypeInfo INSTANCE = new RGBTypeInfo();

  private RGBTypeInfo() {
    super( ETinTypeKind.FULL, DT_COLOR_RGB, RGB.class );
    fieldInfos().add( new TinFieldInfo( FID_RED, TTI_COLOR_COMPONENT, //
        TSID_NAME, STR_COLOR_FIELD_RED, //
        TSID_DESCRIPTION, STR_COLOR_FIELD_RED_D //
    ) );
    fieldInfos().add( new TinFieldInfo( FID_GREEN, TTI_COLOR_COMPONENT, //
        TSID_NAME, STR_COLOR_FIELD_GREEN, //
        TSID_DESCRIPTION, STR_COLOR_FIELD_GREEN_D //
    ) );
    fieldInfos().add( new TinFieldInfo( FID_BLUE, TTI_COLOR_COMPONENT, //
        TSID_NAME, STR_COLOR_FIELD_BLUE, //
        TSID_DESCRIPTION, STR_COLOR_FIELD_BLUE_D //
    ) );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  @Override
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    int red = extractChildInt( FID_RED, aChildValues, DEFAULT_RGB_VALUE.red );
    int green = extractChildInt( FID_GREEN, aChildValues, DEFAULT_RGB_VALUE.green );
    int blue = extractChildInt( FID_BLUE, aChildValues, DEFAULT_RGB_VALUE.blue );
    RGB rgb = new RGB( red, green, blue );
    return avValobj( rgb );
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    RGB rgb = aValue != null ? aValue.asValobj() : DEFAULT_RGB_VALUE;
    aChildValues.put( FID_RED, TinValue.ofAtomic( avInt( rgb.red ) ) );
    aChildValues.put( FID_GREEN, TinValue.ofAtomic( avInt( rgb.green ) ) );
    aChildValues.put( FID_BLUE, TinValue.ofAtomic( avInt( rgb.blue ) ) );
  }

  @Override
  protected ITinValue doGetNullTinValue() {
    return doGetTinValue( DEFAULT_RGB_VALUE );
  }

  @Override
  protected ITinValue doGetTinValue( RGB aEntity ) {
    IAtomicValue av = avValobj( aEntity );
    IStringMap<ITinValue> cv = INSTANCE.decompose( av );
    return TinValue.ofFull( av, cv );
  }

  @Override
  protected RGB doCreateEntity( ITinValue aValue ) {
    return aValue.atomicValue().asValobj();
  }

}
