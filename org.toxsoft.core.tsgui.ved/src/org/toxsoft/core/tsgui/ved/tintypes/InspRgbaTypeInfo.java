package org.toxsoft.core.tsgui.ved.tintypes;

import static org.toxsoft.core.tsgui.ved.tintypes.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.tintypes.IVieselOptionTypeConstants.*;
import static org.toxsoft.core.tsgui.ved.tintypes.InspFieldTypeConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Информация о поле типа {@link RGB}.
 *
 * @author vs
 */
public class InspRgbaTypeInfo
    extends AbstractTinTypeInfo<RGBA> {

  private static final String FID_RED   = "red";   //$NON-NLS-1$
  private static final String FID_GREEN = "green"; //$NON-NLS-1$
  private static final String FID_BLUE  = "blue";  //$NON-NLS-1$
  private static final String FID_ALPHA = "alpha"; //$NON-NLS-1$

  /**
   * The type information singleton.
   */
  public static final InspRgbaTypeInfo INSP_TYPE_INFO = new InspRgbaTypeInfo();

  private InspRgbaTypeInfo() {
    super( ETinTypeKind.FULL, DT_COLOR_RGBA, RGBA.class );
    fieldInfos().add( new TinFieldInfo( FID_RED, TTI_COLOR_COMPONENT, //
        TSID_NAME, TSID_COLOR_FIELD_RED, //
        TSID_DESCRIPTION, TSID_COLOR_FIELD_RED_D //
    ) );
    fieldInfos().add( new TinFieldInfo( FID_GREEN, TTI_COLOR_COMPONENT, //
        TSID_NAME, TSID_COLOR_FIELD_GREEN, //
        TSID_DESCRIPTION, TSID_COLOR_FIELD_GREEN_D //
    ) );
    fieldInfos().add( new TinFieldInfo( FID_BLUE, TTI_COLOR_COMPONENT, //
        TSID_NAME, TSID_COLOR_FIELD_BLUE, //
        TSID_DESCRIPTION, TSID_COLOR_FIELD_BLUE_D //
    ) );
    fieldInfos().add( new TinFieldInfo( FID_ALPHA, TTI_COLOR_COMPONENT, //
        TSID_NAME, TSID_COLOR_FIELD_APLHA, //
        TSID_DESCRIPTION, TSID_COLOR_FIELD_APLHA_D //
    ) );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  @Override
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    int red = extractChildInt( FID_RED, aChildValues, DEFAULT_RGBA_VALUE.rgb.red );
    int green = extractChildInt( FID_GREEN, aChildValues, DEFAULT_RGBA_VALUE.rgb.green );
    int blue = extractChildInt( FID_BLUE, aChildValues, DEFAULT_RGBA_VALUE.rgb.blue );
    int alpha = extractChildInt( FID_ALPHA, aChildValues, DEFAULT_RGBA_VALUE.alpha );
    RGBA rgb = new RGBA( red, green, blue, alpha );
    return avValobj( rgb );
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    RGBA rgba = aValue != null ? aValue.asValobj() : DEFAULT_RGBA_VALUE;
    aChildValues.put( FID_RED, TinValue.ofAtomic( avInt( rgba.rgb.red ) ) );
    aChildValues.put( FID_GREEN, TinValue.ofAtomic( avInt( rgba.rgb.green ) ) );
    aChildValues.put( FID_BLUE, TinValue.ofAtomic( avInt( rgba.rgb.blue ) ) );
  }

  @Override
  protected ITinValue doGetNullTinValue() {
    return doGetTinValue( DEFAULT_RGBA_VALUE );
  }

  @Override
  protected ITinValue doGetTinValue( RGBA aEntity ) {
    IAtomicValue av = avValobj( aEntity );
    IStringMap<ITinValue> cv = INSP_TYPE_INFO.decompose( av );
    return TinValue.ofFull( av, cv );
  }

}