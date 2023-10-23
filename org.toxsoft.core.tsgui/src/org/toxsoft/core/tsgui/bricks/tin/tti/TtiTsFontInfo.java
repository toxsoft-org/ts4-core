package org.toxsoft.core.tsgui.bricks.tin.tti;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITsResources.*;
import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.graphics.ITsGraphicsConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Информация о поле инспектора для {@link IFontInfo}.
 *
 * @author vs
 */
public class TtiTsFontInfo
    extends AbstractTinTypeInfo<IFontInfo> {

  private static final String FID_FONT_NAME = "name";   //$NON-NLS-1$
  private static final String FID_FONT_SIZE = "size";   //$NON-NLS-1$
  private static final String FID_BOLD      = "bold";   //$NON-NLS-1$
  private static final String FID_ITALIC    = "italic"; //$NON-NLS-1$

  /**
   * The type information singleton.
   */
  public static final TtiTsFontInfo INSTANCE = new TtiTsFontInfo();

  private TtiTsFontInfo() {
    super( ETinTypeKind.FULL, DT_TS_FONT_INFO, IFontInfo.class );
    fieldInfos().add( new TinFieldInfo( FID_FONT_NAME, TTI_AT_STRING, //
        TSID_NAME, STR_FONT_NAME, //
        TSID_DESCRIPTION, STR_FONT_NAME_D //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_FONT_SIZE, TTI_AT_INTEGER, //
        TSID_NAME, STR_FONT_SIZE, //
        TSID_DESCRIPTION, STR_FONT_SIZE_D //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_BOLD, TTI_AT_BOOLEAN, //
        TSID_NAME, STR_FONT_BOLD, //
        TSID_DESCRIPTION, STR_FONT_BOLD_D //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_ITALIC, TTI_AT_BOOLEAN, //
        TSID_NAME, STR_FONT_ITALIC, //
        TSID_DESCRIPTION, STR_FONT_ITALIC_D //
    ) );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  @Override
  protected ITinValue doGetNullTinValue() {
    return doGetTinValue( dataType().params().getValobj( TSID_DEFAULT_VALUE ) );
  }

  @Override
  protected ITinValue doGetTinValue( IFontInfo aEntity ) {
    IStringMapEdit<ITinValue> values = new StringMap<>();
    values.put( FID_FONT_NAME, TinValue.ofAtomic( avStr( aEntity.fontName() ) ) );
    values.put( FID_FONT_SIZE, TinValue.ofAtomic( avInt( aEntity.fontSize() ) ) );
    values.put( FID_BOLD, TinValue.ofAtomic( avBool( aEntity.isBold() ) ) );
    values.put( FID_ITALIC, TinValue.ofAtomic( avBool( aEntity.isItalic() ) ) );
    return TinValue.ofFull( avValobj( aEntity ), values );
  }

  @Override
  public IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    String fName = extractChildAtomic( FID_FONT_NAME, aChildValues, avStr( "Arial" ) ).asString(); //$NON-NLS-1$
    int fSize = extractChildAtomic( FID_FONT_SIZE, aChildValues, avInt( 12 ) ).asInt();
    boolean bold = extractChildAtomic( FID_BOLD, aChildValues, AV_FALSE ).asBool();
    boolean italic = extractChildAtomic( FID_ITALIC, aChildValues, AV_FALSE ).asBool();
    return avValobj( new FontInfo( fName, fSize, bold, italic ) );
  }

  @Override
  public void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    IFontInfo fi = DT_TS_FONT_INFO.defaultValue().asValobj();
    if( aValue != null && aValue.isAssigned() ) {
      fi = aValue.asValobj();
    }
    aChildValues.put( FID_FONT_NAME, TinValue.ofAtomic( avStr( fi.fontName() ) ) );
    aChildValues.put( FID_FONT_SIZE, TinValue.ofAtomic( avInt( fi.fontSize() ) ) );
    aChildValues.put( FID_BOLD, TinValue.ofAtomic( avBool( fi.isBold() ) ) );
    aChildValues.put( FID_ITALIC, TinValue.ofAtomic( avBool( fi.isItalic() ) ) );
  }

}
