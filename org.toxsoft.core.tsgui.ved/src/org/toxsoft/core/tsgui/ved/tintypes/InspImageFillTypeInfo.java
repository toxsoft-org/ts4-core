package org.toxsoft.core.tsgui.ved.tintypes;

import static org.toxsoft.core.tsgui.ved.tintypes.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.tintypes.IVieselOptionTypeConstants.*;
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
 * Информация о поле инспектора для {@link TsImageFillInfo}.
 *
 * @author vs
 */
public class InspImageFillTypeInfo
    extends AbstractTinTypeInfo<TsImageFillInfo> {

  private static final String FID_FILL_TYPE = "fillType"; //$NON-NLS-1$

  private static final String FID_IMAGE_DESCRIPTOR = "imageDescriptor"; //$NON-NLS-1$

  /**
   * The type information singleton.
   */
  public static InspImageFillTypeInfo INSTANCE = new InspImageFillTypeInfo();

  InspImageFillTypeInfo() {
    super( ETinTypeKind.FULL, DT_IMAGE_FILL_INFO, TsImageFillInfo.class );

    fieldInfos().add( new TinFieldInfo( FID_FILL_TYPE, InspEnumTypeInfo.INSP_TYPE_INFO, //
        TSID_NAME, STR_N_FILL_TYPE, //
        TSID_DESCRIPTION, STR_D_FILL_TYPE, //
        TSID_KEEPER_ID, EImageFillKind.KEEPER_ID //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_IMAGE_DESCRIPTOR, InspImageDescriptorTypeInfo.INSTANCE, //
        TSID_NAME, STR_N_IMAGE_DESCRIPTOR, //
        TSID_DESCRIPTION, STR_D_IMAGE_DESCRIPTOR, //
        TSID_KEEPER_ID, TsImageDescriptor.KEEPER_ID //
    ) );
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
    EImageFillKind fillKind;
    fillKind = TinTools.getValue( FID_FILL_TYPE, aChildValues, avValobj( EImageFillKind.TILE ) ).asValobj();
    TsImageDescriptor imd;
    imd = TinTools.getValue( FID_IMAGE_DESCRIPTOR, aChildValues, avValobj( TsImageDescriptor.NONE ) ).asValobj();
    return avValobj( new TsImageFillInfo( imd, fillKind ) );
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    TsImageFillInfo ifi = aValue.asValobj();
    aChildValues.put( FID_FILL_TYPE, TinValue.ofAtomic( avValobj( ifi.kind() ) ) );
    aChildValues.put( FID_IMAGE_DESCRIPTOR, TinValue.ofAtomic( avValobj( ifi.imageDescriptor() ) ) );
  }
}
