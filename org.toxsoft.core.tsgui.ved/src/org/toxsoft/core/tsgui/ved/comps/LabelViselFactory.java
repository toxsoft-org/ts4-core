package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.tintypes.IVieselOptionTypeConstants.*;
import static org.toxsoft.core.tsgui.ved.tintypes.InspFieldTypeConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.helpers.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tsgui.ved.tintypes.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Фабрика создания визуального элемента - "Текст" {@link LabelVisel}.
 * <p>
 *
 * @author vs
 */
public class LabelViselFactory
    extends VedAbstractViselFactory {

  /**
   * ИД фабрики содания прямоугольников
   */
  public static final String FACTORY_ID = "labelViselFactory"; //$NON-NLS-1$

  static final String FID_TEXT = "label.text"; //$NON-NLS-1$

  static final String FID_TEXT_COLOR = "label.textColor"; //$NON-NLS-1$

  static final String FID_FONT = "label.font"; //$NON-NLS-1$

  static final String FID_FILL_INFO = "label.fillInfo"; //$NON-NLS-1$

  static final String FID_BORDER_INFO = "label.borderInfo"; //$NON-NLS-1$

  static final String FID_TRANSPARENT = "label.transparent"; //$NON-NLS-1$

  static final String FID_HOR_ALIGNMENT = "label.horAlign"; //$NON-NLS-1$

  static final String FID_VER_ALIGNMENT = "label.verAlign"; //$NON-NLS-1$

  IDataDef DDEF_X = DataDef.create3( "visel.label.x", TTI_FLOATING.dataType(), // //$NON-NLS-1$
      TSID_NAME, STR_N_VISEL_X, //
      TSID_DESCRIPTION, STR_D_VISEL_X );

  IDataDef DDEF_Y = DataDef.create3( "visel.label.y", TTI_FLOATING.dataType(), // //$NON-NLS-1$
      TSID_NAME, STR_N_VISEL_Y, //
      TSID_DESCRIPTION, STR_D_VISEL_Y );

  IDataDef DDEF_WIDTH = DataDef.create3( "visel.rect.width", TTI_FLOATING.dataType(), // //$NON-NLS-1$
      TSID_NAME, STR_N_VISEL_WIDTH, //
      TSID_DESCRIPTION, STR_D_RECT_WIDTH, TSID_DEFAULT_VALUE, avFloat( 120 ) );

  IDataDef DDEF_HEIGHT = DataDef.create3( "visel.rect.height", TTI_FLOATING.dataType(), // //$NON-NLS-1$
      TSID_NAME, STR_N_VISEL_HEIGHT, //
      TSID_DESCRIPTION, STR_D_RECT_HEIGHT, TSID_DEFAULT_VALUE, avFloat( 48 ) );

  IDataDef DDEF_TEXT = DataDef.create3( "visel.label.text", TTI_STRING.dataType(), // //$NON-NLS-1$
      TSID_NAME, STR_N_LABEL_TEXT, //
      TSID_DESCRIPTION, STR_D_LABEL_TEXT, TSID_DEFAULT_VALUE, avStr( "Текст" ) ); //$NON-NLS-1$

  IDataDef DDEF_FONT = DataDef.create3( "visel.label.font", DT_FONT_INFO, // //$NON-NLS-1$
      TSID_NAME, STR_N_FONT, //
      TSID_DESCRIPTION, STR_D_FONT, TSID_DEFAULT_VALUE, avValobj( new FontInfo( "Arial", 14, 0 ) ) ); //$NON-NLS-1$

  IDataDef DDEF_TEXT_COLOR = DataDef.create3( "visel.label.textColor", DT_COLOR_RGBA, // //$NON-NLS-1$
      TSID_NAME, STR_N_TEXT_COLOR, //
      TSID_DESCRIPTION, STR_D_TEXT_COLOR, TSID_DEFAULT_VALUE, avValobj( new RGBA( 0, 0, 0, 255 ) ) );

  IDataDef DDEF_TRANSPARENT = DataDef.create3( "visel.label.transparent", TTI_BOOLEAN.dataType(), // //$NON-NLS-1$
      TSID_NAME, STR_N_TRANSPARENT, //
      TSID_DESCRIPTION, STR_D_TRANSPARENT, TSID_DEFAULT_VALUE, avBool( false ) );

  IDataDef DDEF_HOR_ALIGN = DataDef.create3( "visel.label.horAlign", TTI_ENUM_INFO.dataType(), // //$NON-NLS-1$
      TSID_NAME, STR_N_HOR_ALIGN, //
      TSID_DESCRIPTION, STR_D_HOR_ALIGN, //
      TSID_ENUMERATION, avBool( true ), //
      TSID_KEEPER_ID, EHorAlignment.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EHorAlignment.CENTER ) );

  IDataDef DDEF_VER_ALIGN = DataDef.create3( "visel.label.verAlign", TTI_ENUM_INFO.dataType(), // //$NON-NLS-1$
      TSID_NAME, STR_N_VER_ALIGN, //
      TSID_DESCRIPTION, STR_D_VER_ALIGN, //
      TSID_ENUMERATION, avBool( true ), //
      TSID_KEEPER_ID, EVerAlignment.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EVerAlignment.CENTER ) );

  IDataDef DDEF_FILL_INFO = DataDef.create3( "visel.label.fillInfo", DT_FILL_INFO ); //$NON-NLS-1$

  IDataDef DDEF_BORDER_INFO = DataDef.create3( "visel.label.borderInfo", DT_BORDER_INFO ); //$NON-NLS-1$

  IDataDef DDEF_D2CONVERSION = DataDef.create3( "visel.d2conversion", DT_D2CONVERSION ); //$NON-NLS-1$

  /**
   * Конструктор.
   */
  public LabelViselFactory() {
    super( FACTORY_ID, TSID_NAME, STR_N_LABEL_FACTORY, TSID_DESCRIPTION, STR_D_LABEL_FACTORY );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractViselFactory
  //

  @Override
  protected VedAbstractVisel doCreate( IVedItemCfg aCfg, IVedEnvironment aEnv ) {
    return new LabelVisel( aCfg, propDefs(), aEnv.tsContext() );
  }

  @Override
  protected ITinTypeInfo doCreateTypeInfo() {
    IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
    fields.add( new TinFieldInfo( FID_VISEL_X, TTI_FLOATING, DDEF_X.params() ) );
    fields.add( new TinFieldInfo( FID_VISEL_Y, TTI_FLOATING, DDEF_Y.params() ) );
    fields.add( new TinFieldInfo( FID_VISEL_WIDTH, TTI_FLOATING, DDEF_WIDTH.params() ) );
    fields.add( new TinFieldInfo( FID_VISEL_HEIGHT, TTI_FLOATING, DDEF_HEIGHT.params() ) );
    fields.add( new TinFieldInfo( FID_TEXT, TTI_STRING, DDEF_TEXT.params() ) );
    fields.add( new TinFieldInfo( FID_TEXT_COLOR, RGBATypeInfo.INSTANCE, DDEF_TEXT_COLOR.params() ) );
    fields.add( new TinFieldInfo( FID_FONT, InspFontTypeInfo.INSTANCE, DDEF_FONT.params() ) );
    fields.add( new TinFieldInfo( FID_TRANSPARENT, TTI_BOOLEAN, DDEF_TRANSPARENT.params() ) );
    fields.add( new TinFieldInfo( FID_HOR_ALIGNMENT, TTI_ENUM_INFO, DDEF_HOR_ALIGN.params() ) );
    fields.add( new TinFieldInfo( FID_VER_ALIGNMENT, TTI_ENUM_INFO, DDEF_VER_ALIGN.params() ) );
    fields.add( new TinFieldInfo( FID_FILL_INFO, InspFillTypeInfo.INSTANCE, DDEF_FILL_INFO.params() ) );
    fields.add( new TinFieldInfo( FID_BORDER_INFO, InspBorderTypeInfo.INSTANCE, DDEF_BORDER_INFO.params() ) );
    fields.add( new TinFieldInfo( FID_D2CONVERSION, InspD2ConversionTypeInfo.INSTANCE, DDEF_D2CONVERSION.params() ) );
    InspVedItemTypeInfo typeinfo = new InspVedItemTypeInfo( fields );
    return typeinfo;
  }

}
