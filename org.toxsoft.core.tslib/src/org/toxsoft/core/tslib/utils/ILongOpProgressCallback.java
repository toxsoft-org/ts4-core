package org.toxsoft.core.tslib.utils;

import org.toxsoft.core.tslib.bricks.validator.*;

/**
 * Methods for notifying the user about the progress of a lengthy operation.
 *
 * @author hazard157
 */
public interface ILongOpProgressCallback {

  /**
   * Singleton doing nothing.
   */
  ILongOpProgressCallback NONE = new InternalNoneLongOpProgressCallback();

  /**
   * Singleton outputs into the console.
   */
  ILongOpProgressCallback CONSOLE = new InternalConsoleLongOpProgressCallback();

  /**
   * Called before work starts.
   * <p>
   * The progress dialog displays the name of the running jobs <code>aName</code>. The <code>aUndefined</code> argument
   * tells the dialog whether the degree of completion will be known as the work progresses.
   * <p>
   * The name of the work being performed can be changed as the work progresses (with the <code>aName</code> argument of
   * the {@link #updateWorkProgress(String, double)} method).
   * <p>
   * The behavior of the progress dialog differs depending on whether progress can be determined. In particular:
   * <ul>
   * <li>aUndefined = <code>true</code> - it is not known how long the process will take (for example, connecting to a
   * network resource). The dialog in this mode simply shows that the process is in progress, for example, the slider
   * runs back-and-forth;</li>
   * <li>aUndefined = <code>false</code> - if not the time, then the number of some units of work is known (for example,
   * the number of files when copying). In this case, it is assumed that all the work is 100%, and as the units of work
   * are completed, the box is filled in proportion to the degree of work completion. The process notifies the progress
   * dialog by periodically calling the {@link #updateWorkProgress(String, double)} method.</li>
   * </ul>
   * In fact, what value the user should set to the <code>aUndefined</code> argument is determined by whether the
   * executable process can be programmatically broken into parts. If yes, then the value will be <code>true</code>.
   *
   * @param aName String - displayed name of the work in progress
   * @param aUndefined boolean - the flag determines if all work will be done as one part
   * @return boolean - <code>true</code> to cancel the operation (if possible)
   */
  boolean startWork( String aName, boolean aUndefined );

  /**
   * Updates the status of the work progress.
   * <p>
   * This method should be called after any part of the implementation's work has been done. The aWorkedPercents
   * argument specifies how much of the work (as a percentage) has been completed. For example, if one third of the work
   * is completed, then the value should be set to 33.3333.
   * <p>
   * To clarify which jobs are running, you can change the display name of the job in progress. For example, if files A,
   * B, and C are copied, then after copying file A, you can display the message "File B is being copied ...", or "File
   * A has been copied."
   *
   * @param aName String - displayed name of the work in progress
   * @param aWorkedPercents - what percentage of work has been completed since the last call (0.0 ... 100.0)
   * @return boolean - <code>true</code> to cancel the operation (if possible)
   */
  boolean updateWorkProgress( String aName, double aWorkedPercents );

  /**
   * Called after all work is done.
   * <p>
   * The completion status can be used to display a separate dialog.
   *
   * @param aStatus {@link ValidationResult} - the completion status
   */
  void finished( ValidationResult aStatus );

}

class InternalNoneLongOpProgressCallback
    implements ILongOpProgressCallback {

  @Override
  public boolean startWork( String aName, boolean aUndefined ) {
    return false;
  }

  @Override
  public boolean updateWorkProgress( String aName, double aWorkedPercents ) {
    return false;
  }

  @Override
  public void finished( ValidationResult aStatus ) {
    // nop
  }

}

class InternalConsoleLongOpProgressCallback
    implements ILongOpProgressCallback {

  private static final String PREFIX = "===LongOp=== "; //$NON-NLS-1$

  @Override
  public boolean startWork( String aName, boolean aUndefined ) {
    TsTestUtils.pl( PREFIX + aName );
    return false;
  }

  @Override
  public boolean updateWorkProgress( String aName, double aWorkedPercents ) {
    TsTestUtils.pl( PREFIX + "%s, %.1f%%", aName, Double.valueOf( aWorkedPercents ) ); //$NON-NLS-1$
    return false;
  }

  @Override
  public void finished( ValidationResult aStatus ) {
    TsTestUtils.pl( PREFIX + "%s - %s", aStatus.type().nmName(), aStatus.message() ); //$NON-NLS-1$
  }
}
