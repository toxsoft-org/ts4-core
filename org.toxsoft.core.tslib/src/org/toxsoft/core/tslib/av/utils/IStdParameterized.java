package org.toxsoft.core.tslib.av.utils;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * {@link IParameterized} extension with tslib standard options from {@link IAvMetaConstants}.
 *
 * @author hazard157
 */
public interface IStdParameterized
    extends IParameterized, IIconIdable {

  /**
   * Returns the short human-readable visual name.
   * <p>
   * Returns the value of the optional parameter {@link IAvMetaConstants#DDEF_NAME}.
   *
   * @return String - short human-readable visual name or an empty string if no parameter specified
   */
  default String nmName() {
    return DDEF_NAME.getValue( params() ).asString();
  }

  /**
   * Returns the human-readable descriptive text.
   * <p>
   * Returns the value of the optional parameter {@link IAvMetaConstants#DDEF_DESCRIPTION}.
   *
   * @return String - human-readable descriptive text or an empty string if no parameter specified
   */
  default String description() {
    return DDEF_DESCRIPTION.getValue( params() ).asString();
  }

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
