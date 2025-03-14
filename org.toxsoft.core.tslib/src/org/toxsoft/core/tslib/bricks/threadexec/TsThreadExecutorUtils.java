package org.toxsoft.core.tslib.bricks.threadexec;

import static java.lang.String.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Helper methods for {@link TsThreadExecutor}.
 *
 * @author mvk
 */
public class TsThreadExecutorUtils {

  /**
   * Return thread stack trace in textual view.
   *
   * @param aThread {@link Thread} thread
   * @return String stack trace
   * @throws TsNullArgumentRtException aThread = <b>null</b>.
   */
  public static String threadStackToString( Thread aThread ) {
    TsNullArgumentRtException.checkNull( aThread );
    StringBuilder sb = new StringBuilder();
    StackTraceElement[] stack = aThread.getStackTrace();
    for( int index = 2, n = stack.length; index < n; index++ ) {
      sb.append( format( "   %s\n", stack[index] ) ); //$NON-NLS-1$
    }
    return sb.toString();
  }

}
