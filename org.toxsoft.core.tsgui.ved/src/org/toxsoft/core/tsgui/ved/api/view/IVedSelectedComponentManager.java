package org.toxsoft.core.tsgui.ved.api.view;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Manages component view's selection (inclugin multi-selections) on the screen.
 *
 * @author hazard157
 */
public interface IVedSelectedComponentManager
    extends IGenericChangeEventCapable {

  // TODO API ???

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
