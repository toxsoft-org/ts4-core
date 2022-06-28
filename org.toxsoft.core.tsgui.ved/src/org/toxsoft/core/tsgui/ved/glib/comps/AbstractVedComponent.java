package org.toxsoft.core.tsgui.ved.glib.comps;

import static org.toxsoft.core.tsgui.ved.glib.comps.IVedResoures.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.DataDef.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

public class AbstractVedComponent
    extends StridableParameterized
    implements IVedComponent {

  static IDataDef DDEF_X = create( "coordX", FLOATING, // //$NON-NLS-1$
      TSID_ID, FLOATING.id(), //
      TSID_NAME, NAME_X, //
      TSID_DESCRIPTION, DESCR_X, //
      TSID_DEFAULT_VALUE, FLOATING.defaultValue() //
  );

  static IDataDef DDEF_Y = create( "coordY", FLOATING, // //$NON-NLS-1$
      TSID_ID, FLOATING.id(), //
      TSID_NAME, NAME_Y, //
      TSID_DESCRIPTION, DESCR_Y, //
      TSID_DEFAULT_VALUE, FLOATING.defaultValue() //
  );

  static IDataDef DDEF_WIDTH = create( "width", FLOATING, // //$NON-NLS-1$
      TSID_ID, FLOATING.id(), //
      TSID_NAME, NAME_WIDHT, //
      TSID_DESCRIPTION, DESCR_WIDTH, //
      TSID_DEFAULT_VALUE, FLOATING.defaultValue() //
  );

  static IDataDef DDEF_HEIGHT = create( "height", FLOATING, // //$NON-NLS-1$
      TSID_ID, FLOATING.id(), //
      TSID_NAME, NAME_HEIGHT, //
      TSID_DESCRIPTION, DESCR_HEIGHT, //
      TSID_DEFAULT_VALUE, FLOATING.defaultValue() //
  );

  static IDataDef DDEF_ANGLE = create( "angle", FLOATING, // //$NON-NLS-1$
      TSID_ID, FLOATING.id(), //
      TSID_NAME, NAME_ANGLE, //
      TSID_DESCRIPTION, DESCR_ANGLE, //
      TSID_DEFAULT_VALUE, FLOATING.defaultValue() //
  );

  static IDataDef DDEF_HOR_FLIP = create( "horFlip", FLOATING, // //$NON-NLS-1$
      TSID_ID, BOOLEAN.id(), //
      TSID_NAME, NAME_HOR_FLIP, //
      TSID_DESCRIPTION, DESCR_HOR_FLIP, //
      TSID_DEFAULT_VALUE, BOOLEAN.defaultValue() //
  );

  static IDataDef DDEF_VERT_FLIP = create( "vertFlip", FLOATING, // //$NON-NLS-1$
      TSID_ID, BOOLEAN.id(), //
      TSID_NAME, NAME_VERT_FLIP, //
      TSID_DESCRIPTION, DESCR_VERT_FLIP, //
      TSID_DEFAULT_VALUE, BOOLEAN.defaultValue() //
  );

  IStridablesList<IDataDef> COMMON_DEFS = new StridablesList<>( //
      DDEF_X, //
      DDEF_Y, //
      DDEF_WIDTH, //
      DDEF_HEIGHT, //
      DDEF_ANGLE, //
      DDEF_HOR_FLIP, //
      DDEF_VERT_FLIP //
  );

  /**
   * Конструктор.<br>
   *
   * @param aId String - идентификатор
   */
  protected AbstractVedComponent( String aId ) {
    super( aId );
  }

}
