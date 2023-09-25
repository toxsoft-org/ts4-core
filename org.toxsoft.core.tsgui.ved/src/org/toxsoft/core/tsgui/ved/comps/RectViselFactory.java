package org.toxsoft.core.tsgui.ved.comps;

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
 * Фабрика создания визуального элемента - "Прямоугольник" {@link RectVisel}.
 *
 * @author vs
 */
public class RectViselFactory
    extends VedAbstractViselFactory {

  IDataDef DDEF_X = DataDef.create3( "visel.rect.x", TTI_FLOATING.dataType(), // //$NON-NLS-1$
      TSID_DESCRIPTION, "X координата левого верхнего угла прямоугольника" );

  IDataDef DDEF_Y = DataDef.create3( "visel.rect.y", TTI_FLOATING.dataType(), // //$NON-NLS-1$
      TSID_DESCRIPTION, "Y координата левого верхнего угла прямоугольника" );

  IDataDef DDEF_WIDTH = DataDef.create3( "visel.rect.width", TTI_FLOATING.dataType(), // //$NON-NLS-1$
      TSID_DESCRIPTION, "Ширина прямоугольника" );

  IDataDef DDEF_HEIGHT = DataDef.create3( "visel.rect.height", TTI_FLOATING.dataType(), // //$NON-NLS-1$
      TSID_DESCRIPTION, "Высота прямоугольника" );

  public RectViselFactory( String aId, Object[] aIdsAndValues ) {
    super( aId, aIdsAndValues );
    // TODO Auto-generated constructor stub
  }

  @Override
  protected VedAbstractVisel doCreate( IVedItemCfg aCfg, IVedEnvironment aEnv ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected ITinTypeInfo doCreateTypeInfo() {
    IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
    fields.add( new TinFieldInfo( FID_VISEL_X, TTI_FLOATING, DDEF_X.params() ) );
    fields.add( new TinFieldInfo( FID_VISEL_Y, TTI_FLOATING, DDEF_Y.params() ) );
    fields.add( new TinFieldInfo( FID_VISEL_WIDTH, TTI_FLOATING, DDEF_WIDTH.params() ) );
    fields.add( new TinFieldInfo( FID_VISEL_HEIGHT, TTI_FLOATING, DDEF_HEIGHT.params() ) );
    VedViselTypeInfo typeinfo = new VedViselTypeInfo( fields );
    return typeinfo;
  }

}
