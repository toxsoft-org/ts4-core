package org.toxsoft.core.tslib.bricks.gentask;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IGenericTask} implementation base.
 * <p>
 * Notes for implementors:
 * <ul>
 * <li>method {@link #close()} generates an event;</li>
 * <li>subclass may use {@link #genericChangeEventer()} to fire additional events;</li>
 * <li>this class creates an instance of {@link ITsContext}, returned in editable way by the {@link #out()} method.</li>
 * </ul>
 *
 * @author hazard157
 */
public non-sealed class GenericTask
    implements IGenericTask {

  private final GenericChangeEventer genericChangeEventer;

  private final ITsContextRo in;
  private final ITsContext   out = new TsContext();

  private boolean finished = false;

  /**
   * Constructor for subclasses.
   *
   * @param aIn {@link ITsContextRo} - task input (the same as {@link IGenericTaskRunner#run(ITsContextRo)} argument
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected GenericTask( ITsContextRo aIn ) {
    TsNullArgumentRtException.checkNull( aIn );
    genericChangeEventer = new GenericChangeEventer( this );
    in = aIn;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  final public void close() {
    if( !finished ) {
      try {
        doClose();
      }
      finally {
        finished = true;
        genericChangeEventer.fireChangeEvent();
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // IGenericTask
  //

  @Override
  final public boolean isFinished() {
    return finished;
  }

  @Override
  final public ITsContextRo in() {
    return in;
  }

  @Override
  final public ITsContext out() {
    return out;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may perform closing actions before task becomes marked as finished.
   * <p>
   * Does nothing in the base class so there is no need to call superclass method when overriding.
   */
  protected void doClose() {
    // nop
  }

}
