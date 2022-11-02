package org.toxsoft.core.tsgui.ved.zver1.extra.decors;

import org.toxsoft.core.tsgui.ved.zver1.core.view.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * {@link IVedScreen} selected views outline painter decorator.
 * <p>
 * Extends {@link IParameterizedEdit} allowing to tune subclass dependent drawing parameters like outline width, line
 * style, color, etc.
 * <p>
 * As any screen decorator, instances of this interface must be disposed.
 *
 * @author vs
 */
public interface IVedScreenSelectionDecorator
    extends IVedViewDecorator, IParameterizedEdit {

  /**
   * Returns IDs of component views ignored when draing selection.
   * <p>
   * The list may contain identifiers of non-existent objects and it is safe.
   *
   * @return {@link IStringListEdit} - an editable list of ignored views IDs
   */
  IStringListEdit ignoredViewIds();

}
