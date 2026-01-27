package org.toxsoft.core.tsgui.utils;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Wraps {@link ITsNameProvider} to make {@link ITsVisualsProvider}.
 *
 * @author hazard157
 * @param <T> - type of the item
 */
public class TsVisualsProviderWrapper<T>
    implements ITsVisualsProvider<T> {

  private final ITsNameProvider<T> source;

  /**
   * Constructor.
   *
   * @param aSource {@link ITsNameProvider}&lt;T&gt; - wrapped name provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsVisualsProviderWrapper( ITsNameProvider<T> aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    source = aSource;
  }

  /**
   * Returns wrapped (if needed) visuals provider.
   *
   * @param aSource {@link ITsNameProvider}&lt;T&gt; - wrapped name provider
   * @return {@link ITsVisualsProvider} &lt;T&gt; - the wrapper
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> ITsVisualsProvider<T> wrap( ITsNameProvider<T> aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    if( aSource == ITsNameProvider.DEFAULT ) {
      return ITsVisualsProvider.DEFAULT;
    }
    if( aSource instanceof ITsVisualsProvider<T> vp ) {
      return vp;
    }
    return new TsVisualsProviderWrapper<>( aSource );
  }

  // ------------------------------------------------------------------------------------
  // ITsNameProvider
  //

  @Override
  public String getName( T aItem ) {
    return source.getName( aItem );
  }

  @Override
  public String getDescription( T aItem ) {
    return source.getDescription( aItem );
  }

  // ------------------------------------------------------------------------------------
  // ITsVisualsProvider
  //

  @Override
  public Image getIcon( T aItem, EIconSize aIconSize ) {
    return null;
  }

  @Override
  public TsImage getThumb( T aItem, EThumbSize aThumbSize ) {
    return null;
  }

}
