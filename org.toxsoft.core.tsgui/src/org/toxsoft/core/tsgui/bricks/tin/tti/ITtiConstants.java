package org.toxsoft.core.tsgui.bricks.tin.tti;

import static org.toxsoft.core.tsgui.graphics.ITsGraphicsConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.av.*;

/**
 * Object inspector helper constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ITtiConstants {

  ITinTypeInfo TTI_AT_BOOLEAN   = new TinAtomicTypeInfo.TtiBoolean( DDEF_BOOLEAN );
  ITinTypeInfo TTI_AT_INTEGER   = new TinAtomicTypeInfo.TtiInteger( DDEF_INTEGER );
  ITinTypeInfo TTI_AT_FLOATING  = new TinAtomicTypeInfo.TtiDouble( DDEF_FLOATING );
  ITinTypeInfo TTI_AT_STRING    = new TinAtomicTypeInfo.TtiString( DDEF_STRING );
  ITinTypeInfo TTI_AT_TIMESTAMP = new TinAtomicTypeInfo.TtiLong( DDEF_TIMESTAMP );
  ITinTypeInfo TTI_IDNAME       = new TinAtomicTypeInfo.TtiString( DDEF_IDNAME );
  ITinTypeInfo TTI_IDPATH       = new TinAtomicTypeInfo.TtiString( DDEF_IDPATH );

  ITinTypeInfo TTI_ENUM_INFO = new TinAtomicTypeInfo<>( DT_AV_ENUM, Enum.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( Enum aEntity ) {
      return avValobj( aEntity );
    }
  };

  ITinTypeInfo TTI_COLOR_COMPONENT = new TinAtomicTypeInfo.TtiInteger( DT_COLOR_COMPONENT );
  ITinTypeInfo TTI_COLOR_RGB       = new TinAtomicTypeInfo.TtiValobj<>( DT_COLOR_RGB, RGB.class );
  ITinTypeInfo TTI_COLOR_RGBA      = new TinAtomicTypeInfo.TtiValobj<>( DT_COLOR_RGBA, RGBA.class );
  // ITinTypeInfo TTI_D2CONVERSION = new TinAtomicTypeInfo.TtiValobj<>( DT_D2CONVERSION, ID2Conversion.class );
  ITinTypeInfo TTI_TS_FILL_INFO   = new TinAtomicTypeInfo.TtiValobj<>( DT_TS_FILL_INFO, TsFillInfo.class );
  ITinTypeInfo TTI_TS_BORDER_INFO = new TinAtomicTypeInfo.TtiValobj<>( DT_TS_BORDER_INFO, TsBorderInfo.class );

}
