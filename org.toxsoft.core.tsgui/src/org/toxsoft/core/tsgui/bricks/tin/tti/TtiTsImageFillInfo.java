package org.toxsoft.core.tsgui.bricks.tin.tti;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITsResources.*;
import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.graphics.ITsGraphicsConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * {@link ITinTypeInfo} implementation for {@link TsImageFillInfo} entities.
 *
 * @author vs
 */
public class TtiTsImageFillInfo
    extends AbstractTinTypeInfo<TsImageFillInfo> {

  private static final String FID_FILL_TYPE = "fillType"; //$NON-NLS-1$

  private static final String FID_IMAGE_DESCRIPTOR = "gradientInfo"; //$NON-NLS-1$

  private static final ITinFieldInfo TFI_FILL_TYPE = new TinFieldInfo( FID_FILL_TYPE, TtiAvEnum.INSTANCE, //
      TSID_NAME, STR_IMAGE_FILL_TYPE, //
      TSID_DESCRIPTION, STR_IMAGE_FILL_TYPE_D, //
      TSID_KEEPER_ID, EImageFillKind.KEEPER_ID //
  );

  private static final ITinFieldInfo TFI_IMAGE_DESCRIPTOR =
      new TinFieldInfo( FID_IMAGE_DESCRIPTOR, TTI_TS_IMAGE_DECRIPTOR, //
          TSID_NAME, STR_IMAGE_DESCRIPTOR, //
          TSID_DESCRIPTION, STR_IMAGE_DESCRIPTOR_D, //
          TSID_KEEPER_ID, TsImageDescriptor.KEEPER_ID //
      );

  /**
   * The type information singleton.
   */
  public static final TtiTsImageFillInfo INSTANCE = new TtiTsImageFillInfo();

  TtiTsImageFillInfo() {
    super( ETinTypeKind.FULL, DT_TS_IMAGE_FILL_INFO, TsImageFillInfo.class );
    fieldInfos().add( TFI_FILL_TYPE );
    fieldInfos().add( TFI_IMAGE_DESCRIPTOR );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  @Override
  protected ITinValue doGetNullTinValue() {
    return doGetTinValue( TsImageFillInfo.DEFAULT );
  }

  @Override
  protected ITinValue doGetTinValue( TsImageFillInfo aEntity ) {
    IStringMapEdit<ITinValue> childValues = new StringMap<>();
    childValues.put( FID_FILL_TYPE, TinValue.ofAtomic( avValobj( aEntity.kind() ) ) );
    childValues.put( FID_IMAGE_DESCRIPTOR, TinValue.ofAtomic( avValobj( aEntity.imageDescriptor() ) ) );
    return TinValue.ofFull( avValobj( aEntity ), childValues );
  }

  @Override
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    EImageFillKind fillKind = extractChildValobj( TFI_FILL_TYPE, aChildValues );
    TsImageDescriptor imd = extractChildValobj( TFI_IMAGE_DESCRIPTOR, aChildValues );
    return avValobj( new TsImageFillInfo( imd, fillKind ) );
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    TsImageFillInfo ifi = aValue.asValobj();
    aChildValues.put( FID_FILL_TYPE, TinValue.ofAtomic( avValobj( ifi.kind() ) ) );
    aChildValues.put( FID_IMAGE_DESCRIPTOR, TinValue.ofAtomic( avValobj( ifi.imageDescriptor() ) ) );
  }
}
