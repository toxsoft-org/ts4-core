package org.toxsoft.core.tslib.bricks.qnodes.helpers;

import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link IQNodeVisitor} the abstract base implements only {@link #visitSubtree(IQNode)}.
 *
 * @author hazard157
 */
public abstract non-sealed class AbstractQNodeVisitor
    implements IQNodeVisitor {

  private IQNode stopperNode = null;

  /**
   * Constructor.
   */
  public AbstractQNodeVisitor() {
    // nop
  }

  private boolean internalVisitSubtree( IQNode aNode ) {
    stopperNode = aNode;
    if( visitNode( aNode ) ) {
      return true;
    }
    for( IQNode n : aNode.childs() ) {
      if( internalVisitSubtree( n ) ) {
        return true;
      }
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // IQNodeVisitor
  //

  @Override
  final public boolean visitSubtree( IQNode aNode ) {
    TsNullArgumentRtException.checkNull( aNode );
    stopperNode = null;
    if( beforeStartSubtree( aNode ) ) {
      return true;
    }
    boolean retval;
    Exception exception = null;
    try {
      retval = internalVisitSubtree( aNode );
    }
    catch( Exception ex ) {
      exception = ex;
      retval = true;
      LoggerUtils.errorLogger().error( exception );
    }
    afterEndSubtree( aNode, retval, exception, stopperNode );
    return retval;
  }

  @Override
  final public boolean visitNode( IQNode aNode ) {
    TsNullArgumentRtException.checkNull( aNode );
    return doVisitNode( aNode );
  }

  // ------------------------------------------------------------------------------------
  // To implement & override
  //

  /**
   * Real node visiting job must be done here.
   *
   * @param aNode {@link IQNode} - the visited node, never is <code>null</code>
   * @return boolean - visiting cancellation flag<br>
   *         <b>true</b> - stop visiting process immediately;<br>
   *         <b>false</b> - continue visiting.
   */
  protected abstract boolean doVisitNode( IQNode aNode );

  /**
   * Called from {@link #visitSubtree(IQNode)} before anynode is visited.
   *
   * @param aSubtreeRoot {@link IQNode} - root of subtree to be visited, necver is <code>null</code>
   * @return boolean - visiting cancellation flag<br>
   *         <b>true</b> - stop visiting process immediately;<br>
   *         <b>false</b> - continue visiting.
   */
  protected boolean beforeStartSubtree( IQNode aSubtreeRoot ) {
    return false;
  }

  /**
   * Called from {@link #visitSubtree(IQNode)} after nodes are visited.
   * <p>
   * If iteration was cancelled either by user returned value or by exception, argument <code>aWasCancelled</code> is
   * true. In case of exception <code>aException</code> is not <code>null</code>.
   * <p>
   * In base class throws {@link TsRuntimeException} wrapped over <code>aException</code> if exception was catched.
   * Otherwise does nothig. It's up to subclass to call superclass method when overriding.
   *
   * @param aSubtreeRoot {@link IQNode} - root of subtree to be visited, never is <code>null</code>
   * @param aWasCancelled boolean - <code>true</code> if iteration was cancelled
   * @param aException {@link Throwable} - an excepation ctched during visiting or <code>null</code>
   * @param aStopperNode {@link IQNode} - node that cancelled iteration
   */
  protected void afterEndSubtree( IQNode aSubtreeRoot, boolean aWasCancelled, Throwable aException,
      IQNode aStopperNode ) {
    if( aException != null ) {
      throw new TsRemoteIoRtException( aException );
    }
  }

}
