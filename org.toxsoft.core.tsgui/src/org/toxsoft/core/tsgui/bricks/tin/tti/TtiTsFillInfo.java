package org.toxsoft.core.tsgui.bricks.tin.tti;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITsResources.*;
import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.graphics.ITsGraphicsConstants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * The {@link ITinTypeInfo} implementation for {@link TsFillInfo}.
 *
 * @author vs
 */
public class TtiTsFillInfo
    extends AbstractTinTypeInfo<TsFillInfo> {

  private static final String FID_FILL_TYPE     = "type";     //$NON-NLS-1$
  private static final String FID_FILL_COLOR    = "color";    //$NON-NLS-1$
  private static final String FID_FILL_GRADIENT = "gradient"; //$NON-NLS-1$
  private static final String FID_FILL_IMAGE    = "image";    //$NON-NLS-1$

  private static final ITinFieldInfo TFI_FILL_TYPE = new TinFieldInfo( FID_FILL_TYPE, TtiAvEnum.INSTANCE, //
      TSID_NAME, STR_FILL_TYPE, //
      TSID_DESCRIPTION, STR_FILL_TYPE_D, //
      TSID_KEEPER_ID, ETsFillKind.KEEPER_ID //
  );

  private static final ITinFieldInfo TFI_FILL_COLOR = new TinFieldInfo( FID_FILL_COLOR, TtiRGBA.INSTANCE, //
      TSID_NAME, STR_FILL_COLOR, //
      TSID_DESCRIPTION, STR_FILL_COLOR_D //
  );

  private static final ITinFieldInfo TFI_FILL_GRADIENT = new TinFieldInfo( FID_FILL_GRADIENT, TTI_TS_GRADIENT_FILL_INFO, //
      TSID_NAME, STR_FILL_GRADIENT, //
      TSID_DESCRIPTION, STR_FILL_GRADIENT_D, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsGradientFillInfo.FACTORY_NAME, //
      TSID_KEEPER_ID, TsGradientFillInfo.KEEPER_ID //
  );

  private static final ITinFieldInfo TFI_FILL_IMAGE = new TinFieldInfo( FID_FILL_IMAGE, TtiTsImageFillInfo.INSTANCE, //
      TSID_NAME, STR_FILL_IMAGE, //
      TSID_DESCRIPTION, STR_FILL_IMAGE_D, //
      TSID_KEEPER_ID, TsImageFillInfo.KEEPER_ID //
  );

  /**
   * The type information singleton.
   */
  public static final TtiTsFillInfo INSTANCE = new TtiTsFillInfo();

  private TtiTsFillInfo() {
    super( ETinTypeKind.FULL, DT_TS_FILL_INFO, TsFillInfo.class );
    fieldInfos().add( TFI_FILL_TYPE );
    fieldInfos().add( TFI_FILL_COLOR );
    fieldInfos().add( TFI_FILL_GRADIENT );
    fieldInfos().add( TFI_FILL_IMAGE );
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
    ETsFillKind kind = extractChildValobj( TFI_FILL_TYPE, aChildValues );
    switch( kind ) {
      case NONE:
        return avValobj( TsFillInfo.NONE );
      case SOLID: {
        RGBA rgba = extractChildValobj( TFI_FILL_COLOR, aChildValues );
        return avValobj( new TsFillInfo( rgba ) );
      }
      case IMAGE: {
        TsImageFillInfo ifi = extractChildValobj( TFI_FILL_IMAGE, aChildValues );
        return avValobj( new TsFillInfo( ifi ) );
      }
      case GRADIENT: {
        TsGradientFillInfo gfi = extractChildValobj( TFI_FILL_GRADIENT, aChildValues );
        return avValobj( new TsFillInfo( gfi ) );
      }
      default:
        throw new IllegalArgumentException();
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
        aChildValues.put( FID_FILL_COLOR, TtiRGBA.INSTANCE.makeValue( fi.fillColor() ) );
        break;
      case IMAGE:
        // FIXME !!!
        // aChildValues.put( FID_FILL_IMAGE, TtiImageFill.INSTANCE.makeValue( fi.imageFillInfo() ) );
        break;
      case GRADIENT:
        aChildValues.put( FID_FILL_GRADIENT, TTI_TS_GRADIENT_FILL_INFO.makeValue( fi.gradientFillInfo() ) );
        break;
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
        result.add( FID_FILL_TYPE );
        result.add( FID_FILL_GRADIENT );
        return result;
      default:
        throw new IllegalArgumentException( "Unexpected value: " + fi.kind() ); //$NON-NLS-1$
    }
  }

}
