package org.toxsoft.core.tsgui.ved.comps.render;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Интерфейс отрисовщика визуального элемента.
 * <p>
 *
 * @author vs
 */
public interface IViselRenderer
    extends IStridableParameterized, ITsPaintable, IPropertable<IViselRenderer> {

  /**
   * Return the kind id (for exmple: button, gauge etc.)
   *
   * @return String - the kind id of the renderer
   */
  String kindId();

  /**
   * Returns the information about RtControl properties.
   * <p>
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   */
  IStridablesList<IDataDef> propDefs();

  /**
   * Returns the type information for item to be viewed and edited in object inspector.
   *
   * @return {@link ITinTypeInfo} - the information for inspecting the VED item instance
   */
  ITinTypeInfo typeInfo();

  /**
   * Returns list of field infoes {@link ITinFieldInfo}.
   *
   * @return IStridablesList&lt;ITinFieldInfo> - field infoes
   */
  IStridablesList<ITinFieldInfo> tinFieldInfoes();

  /**
   * Returns the corresponding VISEL.
   *
   * @return {@link IVedVisel} - the corresponding VISEL
   */
  IVedVisel visel();

  // /**
  // * Sets values needed for painting.
  // *
  // * @param aProps {@link IOptionSet} - values needed for painting
  // */
  // void setPropValues( IOptionSet aProps );

}
