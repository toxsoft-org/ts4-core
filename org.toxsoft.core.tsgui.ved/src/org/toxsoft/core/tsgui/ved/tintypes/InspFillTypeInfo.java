package org.toxsoft.core.tsgui.ved.tintypes;

import static org.toxsoft.core.tsgui.ved.tintypes.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.tintypes.IVieselOptionTypeConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.helpers.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Информация о поле инспектора для {@link TsFillInfo}.
 *
 * @author vs
 */
public class InspFillTypeInfo
    extends AbstractTinTypeInfo<TsFillInfo> {

  private static final String FID_FILL_TYPE     = "fillType";     //$NON-NLS-1$
  private static final String FID_FILL_COLOR    = "fillColor";    //$NON-NLS-1$
  private static final String FID_FILL_GRADIENT = "fillGradient"; //$NON-NLS-1$
  private static final String FID_FILL_IMAGE    = "fillImage";    //$NON-NLS-1$

  /**
   * The type information singleton.
   */
  public static InspFillTypeInfo INSTANCE = new InspFillTypeInfo();

  private InspFillTypeInfo() {
    super( ETinTypeKind.FULL, DT_FILL_INFO, TsFillInfo.class );

    fieldInfos().add( new TinFieldInfo( FID_FILL_TYPE, InspEnumTypeInfo.INSP_TYPE_INFO, //
        TSID_NAME, STR_N_FILL_TYPE, //
        TSID_DESCRIPTION, STR_D_FILL_TYPE, //
        TSID_KEEPER_ID, ETsFillKind.KEEPER_ID //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_FILL_COLOR, RGBATypeInfo.TIN_TYPE_INFO, //
        TSID_NAME, STR_N_FILL_COLOR, //
        TSID_DESCRIPTION, STR_D_FILL_COLOR //
    ) );

    // fieldInfos().add( new TinFieldInfo( FID_FILL_GRADIENT, InspGradientFillTypeInfo.INSTANCE, //
    // TSID_NAME, STR_N_GRADIENT, //
    // TSID_DESCRIPTION, STR_D_GRADIENT, //
    // TSID_KEEPER_ID, TsGradientFillInfo.KEEPER_ID //
    // ) );

    fieldInfos().add( new TinFieldInfo( FID_FILL_IMAGE, InspImageFillTypeInfo.INSTANCE, //
        TSID_NAME, STR_N_IMAGE, //
        TSID_DESCRIPTION, STR_D_IMAGE, //
        TSID_KEEPER_ID, TsImageFillInfo.KEEPER_ID //
    ) );

  }

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  @Override
  protected ITinValue doGetNullTinValue() {
    return doGetTinValue( TsFillInfo.NONE );
  }

  @Override
  protected ITinValue doGetTinValue( TsFillInfo aEntity ) {
    IAtomicValue av = avValobj( aEntity );
    return TinValue.ofFull( av, decompose( av ) );
  }

  @Override
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    ETsFillKind kind = TinTools.getValue( FID_FILL_TYPE, aChildValues, avValobj( ETsFillKind.NONE ) ).asValobj();
    switch( kind ) {
      case NONE:
        return avValobj( TsFillInfo.NONE );
      case SOLID: {
        RGBA rgba;
        rgba = TinTools.getValue( FID_FILL_COLOR, aChildValues, avValobj( new RGBA( 255, 255, 255, 255 ) ) ).asValobj();
        return avValobj( new TsFillInfo( rgba ) );
      }
      case IMAGE: {
        TsImageFillInfo ifi;
        ifi = TinTools.getValue( FID_FILL_IMAGE, aChildValues, avValobj( TsImageFillInfo.DEFAULT ) ).asValobj();
        return avValobj( new TsFillInfo( ifi ) );
      }
      case GRADIENT:
      default:
        throw new IllegalArgumentException( "Unexpected value: " + kind ); //$NON-NLS-1$
    }
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    TsFillInfo fi = TsFillInfo.NONE;
    if( aValue != null && aValue.isAssigned() ) {
      fi = aValue.asValobj();
    }
    aChildValues.put( FID_FILL_TYPE, TinValue.ofAtomic( avValobj( fi.kind() ) ) );
    switch( fi.kind() ) {
      case NONE:
        break;
      case SOLID:
        aChildValues.put( FID_FILL_COLOR, RGBATypeInfo.TIN_TYPE_INFO.makeValue( fi.fillColor() ) );
        break;
      case IMAGE:
        aChildValues.put( FID_FILL_IMAGE, InspImageFillTypeInfo.INSTANCE.makeValue( fi.imageFillInfo() ) );
        break;
      case GRADIENT:
      default:
        throw new IllegalArgumentException( "Unexpected value: " + fi.kind() ); //$NON-NLS-1$
    }
  }

  @Override
  protected IStringList doGetVisibleFieldIds( ITinValue aValue ) {
    TsFillInfo fi = TsFillInfo.NONE;
    if( aValue != null ) {
      IAtomicValue av = aValue.atomicValue();
      if( av != null && av.isAssigned() ) {
        fi = av.asValobj();
      }
    }
    IStringListEdit result = new StringArrayList();
    switch( fi.kind() ) {
      case NONE:
        result.add( FID_FILL_TYPE );
        return result;
      case SOLID:
        result.add( FID_FILL_TYPE );
        result.add( FID_FILL_COLOR );
        return result;
      case IMAGE:
        result.add( FID_FILL_TYPE );
        result.add( FID_FILL_IMAGE );
        return result;
      case GRADIENT:
      default:
        throw new IllegalArgumentException( "Unexpected value: " + fi.kind() ); //$NON-NLS-1$
    }
  }

}
