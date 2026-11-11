package org.toxsoft.core.tslib.bricks.coopcomp;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The only base implementation of {@link ITsStridableComponent}.
 *
 * @author mvk
 */
abstract class AbstractTsStridableComponent
    extends AbstractTsCooperativeComponent
    implements ITsStridableComponent {

  private final Stridable stridable;

  /**
   * Constructor.
   *
   * @param aStridable {@link Stridable} unmuttable id implementation
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected AbstractTsStridableComponent( Stridable aStridable ) {
    stridable = TsNullArgumentRtException.checkNull( aStridable );
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  final public String id() {
    return stridable.id();
  }

  @Override
  final public String nmName() {
    return stridable.nmName();
  }

  @Override
  final public String description() {
    return stridable.description();
  }
}
