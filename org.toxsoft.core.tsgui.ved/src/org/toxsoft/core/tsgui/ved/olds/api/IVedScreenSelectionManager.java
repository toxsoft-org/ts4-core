package org.toxsoft.core.tsgui.ved.olds.api;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

public interface IVedScreenSelectionManager
    extends IGenericChangeEventCapable {

  enum ESelectionKind {
    NONE,
    ONE,
    MULTI
  }

  ESelectionKind selectionKind();

  IVedComponent selectedComponent();

  IStridablesList<IVedComponent> selectedComponents();

  void deselectAll();

  void deselectOne( String aCompId );

  void deselectMulti( IStringList aCompId );

  void selectOne( String aCompId );

  void selectMulti( IStringList aCompIds );

}
