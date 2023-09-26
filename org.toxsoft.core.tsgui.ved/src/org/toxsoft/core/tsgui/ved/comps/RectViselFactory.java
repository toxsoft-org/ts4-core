package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.tintypes.InspFieldTypeConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
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
 * Фабрика создания визуального элемента - "Прямоугольник" {@link RectVisel}.
 *
 * @author vs
 */
public class RectViselFactory
    extends VedAbstractViselFactory {

  /**
   * ИД фабрики содания прямоугольников
   */
  public static final String FACTORY_ID = "rectViselFactory"; //$NON-NLS-1$

  IDataDef DDEF_X = DataDef.create3( "visel.rect.x", TTI_FLOATING.dataType(), // //$NON-NLS-1$
      TSID_NAME, STR_N_VISEL_X, //
      TSID_DESCRIPTION, STR_D_VISEL_X );

  IDataDef DDEF_Y = DataDef.create3( "visel.rect.y", TTI_FLOATING.dataType(), // //$NON-NLS-1$
      TSID_NAME, STR_N_VISEL_Y, //
      TSID_DESCRIPTION, STR_D_VISEL_Y );

  IDataDef DDEF_WIDTH = DataDef.create3( "visel.rect.width", TTI_FLOATING.dataType(), // //$NON-NLS-1$
      TSID_NAME, STR_N_VISEL_WIDTH, //
      TSID_DESCRIPTION, STR_D_RECT_WIDTH, TSID_DEFAULT_VALUE, avFloat( 100 ) );

  IDataDef DDEF_HEIGHT = DataDef.create3( "visel.rect.height", TTI_FLOATING.dataType(), // //$NON-NLS-1$
      TSID_NAME, STR_N_VISEL_HEIGHT, //
      TSID_DESCRIPTION, STR_D_RECT_HEIGHT, TSID_DEFAULT_VALUE, avFloat( 100 ) );

  /**
   * Конструктор.
   */
  public RectViselFactory() {
    super( FACTORY_ID, TSID_NAME, STR_N_RECT_FACTORY, TSID_DESCRIPTION, STR_D_RECT_FACTORY );
  }

  @Override
  protected VedAbstractVisel doCreate( IVedItemCfg aCfg, IVedEnvironment aEnv ) {
    return new RectVisel( aCfg, propDefs(), aEnv.tsContext() );
  }

  @Override
  protected ITinTypeInfo doCreateTypeInfo() {
    IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
    fields.add( new TinFieldInfo( FID_VISEL_X, TTI_FLOATING, DDEF_X.params() ) );
    fields.add( new TinFieldInfo( FID_VISEL_Y, TTI_FLOATING, DDEF_Y.params() ) );
    fields.add( new TinFieldInfo( FID_VISEL_WIDTH, TTI_FLOATING, DDEF_WIDTH.params() ) );
    fields.add( new TinFieldInfo( FID_VISEL_HEIGHT, TTI_FLOATING, DDEF_HEIGHT.params() ) );
    InspViselTypeInfo typeinfo = new InspViselTypeInfo( fields );
    return typeinfo;
  }

}
