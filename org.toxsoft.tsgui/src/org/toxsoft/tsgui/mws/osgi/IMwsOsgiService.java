package org.toxsoft.tsgui.mws.osgi;

import org.toxsoft.tsgui.bricks.quant.IQuant;
import org.toxsoft.tsgui.mws.appinf.ITsApplicationInfo;
import org.toxsoft.tslib.av.utils.IParameterized;
import org.toxsoft.tslib.bricks.ctx.ITsContext;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

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
   * Unformation must be set previously by {@link #setAppInfo(ITsApplicationInfo)}.
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

  /**
   * Adds quant to be initialized by this plugin's addon.
   * <p>
   * Please note that this plugin (mws.base) is loaded before any other module plugin of the Modular WorkStation.
   * Registered quants will be initialized before any other quants.
   *
   * @param aQuant {@link IQuant} - quant to be added
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addQuant( IQuant aQuant );

  /**
   * Returns quants registered by {@link #addQuant(IQuant)}.
   *
   * @return {@link IList}&lt;{@link IQuant}&gt; - list of the quant in the registration order
   */
  IList<IQuant> listQuants();

}
