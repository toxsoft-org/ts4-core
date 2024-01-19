package org.toxsoft.core.tslib.av.opset;

/**
 * Helper interface to be used to handle options values change attempt for {@link IOpsBatchEdit}.
 *
 * @author hazard157
 */
public interface IOptionsBatchEditInterceptor {

  /**
   * Singleton of the interceptor doing nothing.
   */
  IOptionsBatchEditInterceptor NONE = ( aCurrValues, aValuesToSet ) -> true;

  /**
   * Called when options values change is requested.
   * <p>
   * Editable argument <code>aValuesToSet</code> is the values, that will be set to options. It initially contains the
   * the new values of the change request. Interceptor may freely add, edit or remove options from
   * <code>aValuesToSet</code>.
   * <p>
   * Warning: <code>aCurrValues</code> the argument may be <code>null</code>, which means a request to replace the
   * existing set with a new one.
   *
   * @param aCurrValues {@link IOptionSet} - current values of the options or <code>null</code> when replacing all
   * @param aValuesToSet {@link IOptionSet} - the values to be set after interception
   * @return boolean - indicates whether the request should be executed<br>
   *         <b>true</b> - request will be excuted and <code>aValuesToSet</code> will be applied to the set;<br>
   *         <b>false</b> - request will be ignored.
   */
  boolean interceptPropsChange( IOptionSet aCurrValues, IOptionSetEdit aValuesToSet );

}
