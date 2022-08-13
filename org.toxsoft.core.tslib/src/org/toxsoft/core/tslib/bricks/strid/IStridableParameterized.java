package org.toxsoft.core.tslib.bricks.strid;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.utils.*;

/**
 * Вспомгательный интерфейс стороко-идентифицируемых сущностей с параметрами.
 * <p>
 * Необязательно, но рекомендуется, чтобы имя {@link #nmName()} и описание {@link #description()} сущности также
 * дублировать в параметрах с идентификаторами {@link IAvMetaConstants#TSID_NAME} и
 * {@link IAvMetaConstants#TSID_DESCRIPTION} соответственно. Вместе с тем, обязательным условием является, что в случае
 * <b>наличия</b> параметров с указанными стандартными идентификаторами в {@link #params()}, их значения <b>обязаны</b>
 * совпадать с {@link #nmName()} и {@link #description()}.
 * <p>
 * Вообще, рекомендуется для подходящих по смысле праматеров использовать идентификаторы из перечня
 * {@link IAvMetaConstants}<code><b>.TSID_XXX</b></code>.
 *
 * @author hazard157
 */
public interface IStridableParameterized
    extends IStridable, IParameterized, IIconIdable {

  /**
   * Returns the icon identifier.
   * <p>
   * Returns the value of the optional parameter with identifier {@link IAvMetaConstants#TSID_ICON_ID}.
   *
   * @return String - icon identifier or <code>null</code> if no parameter specified
   */
  @Override
  default String iconId() {
    return params().getStr( TSID_ICON_ID, null );
  }

}
