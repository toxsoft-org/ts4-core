package org.toxsoft.core.tsgui.mws.osgi;

import org.toxsoft.core.tsgui.mws.appinf.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Modular WorkStation (MWS) supporting ISGI service.
 * <p>
 * {@link IParameterized#params()} method returns the parameters from {@link #context()}.
 *
 * @author hazard157
 */
public interface IMwsOsgiService {

  /**
   * Returns the context - common references and parameters shared between all modules.
   *
   * @return {@link ITsContext} - the common context
   */
  ITsContext context();

  /**
   * Returns the information about application.
   * <p>
   * Information must be set previously by {@link #setAppInfo(ITsApplicationInfo)}.
   *
   * @return {@link ITsApplicationInfo} - the information about application
   */
  ITsApplicationInfo appInfo();

  /**
   * Sets the information about application.
   *
   * @param aAppInfo {@link ITsApplicationInfo} - the information about application
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setAppInfo( ITsApplicationInfo aAppInfo );

}
