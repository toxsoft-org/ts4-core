package org.toxsoft.core.tsgui.graphics.image.impl;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Abstract implementation of {@link IThumbSizeableEx}.
 * <p>
 * Default size is specified in constructor. However, method {@link #defaultThumbSize()} may be overridden.
 *
 * @author hazard157
 */
public abstract class AbstractThumbSizeManager
    implements IThumbSizeableEx {

  private final GenericChangeEventer eventer;

  private EThumbSize defaultSize;
  private EThumbSize currSize;

  /**
   * Constructor.
   *
   * @param aSource Object - the event source
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  protected AbstractThumbSizeManager( Object aSource, EThumbSize aDefaultSize ) {
    eventer = new GenericChangeEventer( aSource );
    defaultSize = TsNullArgumentRtException.checkNull( aDefaultSize );
    currSize = defaultSize;
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeable
  //

  @Override
  final public EThumbSize thumbSize() {
    return currSize;
  }

  @Override
  final public void setThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    if( currSize != aThumbSize ) {
      EThumbSize sz = doChangeThumbSize( aThumbSize );
      TsInternalErrorRtException.checkNull( sz );
      currSize = sz;
      if( currSize != aThumbSize ) {
        eventer.fireChangeEvent();
      }
    }
  }

  @Override
  public EThumbSize defaultThumbSize() {
    return defaultSize;
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeableEx
  //

  @Override
  final public IGenericChangeEventer thumbSizeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // To override/implement
  //

  /**
   * Implementation must actually change the thumbnail size.
   *
   * @param aNewSize {@link EThumbSize} - new size, never is <code>null</code>
   * @return {@link EThumbSize} - actual current thumbnails size, may be different from argument, not <code>null</code>
   */
  protected abstract EThumbSize doChangeThumbSize( EThumbSize aNewSize );

}
