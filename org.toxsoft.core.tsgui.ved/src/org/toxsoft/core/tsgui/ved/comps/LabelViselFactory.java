package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.tintypes.IVieselOptionTypeConstants.*;
import static org.toxsoft.core.tsgui.ved.tintypes.InspFieldTypeConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
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

  static final String FID_FILL_INFO = "label.fillInfo"; //$NON-NLS-1$

  static final String FID_BORDER_INFO = "label.borderInfo"; //$NON-NLS-1$

  IDataDef DDEF_X = DataDef.create3( "visel.rect.x", TTI_FLOATING.dataType(), // //$NON-NLS-1$
      TSID_NAME, STR_N_VISEL_X, //
      TSID_DESCRIPTION, STR_D_VISEL_X );

  IDataDef DDEF_Y = DataDef.create3( "visel.rect.y", TTI_FLOATING.dataType(), // //$NON-NLS-1$
      TSID_NAME, STR_N_VISEL_Y, //
      TSID_DESCRIPTION, STR_D_VISEL_Y );

  IDataDef DDEF_TEXT = DataDef.create3( "visel.label.text", TTI_STRING.dataType(), // //$NON-NLS-1$
      TSID_NAME, STR_N_LABEL_TEXT, //
      TSID_DESCRIPTION, STR_D_LABEL_TEXT );

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
    fields.add( new TinFieldInfo( FID_TEXT, TTI_STRING, DDEF_TEXT.params() ) );
    fields.add( new TinFieldInfo( FID_FILL_INFO, InspFillTypeInfo.INSTANCE, DDEF_FILL_INFO.params() ) );
    fields.add( new TinFieldInfo( FID_BORDER_INFO, InspBorderTypeInfo.INSTANCE, DDEF_BORDER_INFO.params() ) );
    fields.add( new TinFieldInfo( FID_D2CONVERSION, InspD2ConversionTypeInfo.INSTANCE, DDEF_D2CONVERSION.params() ) );
    InspVedItemTypeInfo typeinfo = new InspVedItemTypeInfo( fields );
    return typeinfo;
  }

}
