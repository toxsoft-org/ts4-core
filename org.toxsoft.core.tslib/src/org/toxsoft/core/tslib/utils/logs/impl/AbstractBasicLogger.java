package org.toxsoft.core.tslib.utils.logs.impl;

import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.ELogSeverity;
import org.toxsoft.core.tslib.utils.logs.ILogger;

/**
 * Very basic implementation of {@link ILogger}.
 * <p>
 * This class implements <code>info()</code>, <code>warning()</code> and <code>error()</code> methods using more general
 * <code>log()</code> method.
 *
 * @author hazard157
 */
public abstract class AbstractBasicLogger
    implements ILogger {

  @Override
  public boolean isSeverityOn( ELogSeverity aSeverity ) {
    TsNullArgumentRtException.checkNull( aSeverity );
    return true;
  }

  @Override
  public void debug( String aMessage, Object... aArgs ) {
    log( ELogSeverity.DEBUG, aMessage, aArgs );
  }

  @Override
  public void debug( Throwable aException, String aMessage, Object... aArgs ) {
    log( ELogSeverity.DEBUG, aException, aMessage, aArgs );
  }

  @Override
  public void error( String aMessage, Object... aArgs ) {
    log( ELogSeverity.ERROR, aMessage, aArgs );
  }

  @Override
  public void error( Throwable aException, String aMessage, Object... aArgs ) {
    log( ELogSeverity.ERROR, aException, aMessage, aArgs );
  }

  @Override
  public void error( Throwable aException ) {
    log( ELogSeverity.ERROR, aException );
  }

  @Override
  public void info( String aMessage, Object... aArgs ) {
    log( ELogSeverity.INFO, aMessage, aArgs );
  }

  @Override
  public void warning( String aMessage, Object... aArgs ) {
    log( ELogSeverity.WARNING, aMessage, aArgs );
  }

  @Override
  public void warning( Throwable aException, String aMessage, Object... aArgs ) {
    log( ELogSeverity.WARNING, aException, aMessage, aArgs );
  }

}
