package org.toxsoft.core.tsgui.utils;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.eclipse.swt.graphics.Image;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tslib.utils.ITsNameProvider;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Any items visuals provider based on {@link ITsNameProvider}..
 *
 * @author hazard157
 * @param <T> - type of the item
 */
public interface ITsVisualsProvider<T>
    extends ITsNameProvider<T> {

  /**
   * The default provider.
   */
  @SuppressWarnings( "rawtypes" )
  ITsVisualsProvider DEFAULT = new InternalDefaultItemVisualsProvider();

  /**
   * Returns the icon of the item.
   * <p>
   * Implementation must respect argument aIconSize and return the image of specified size.
   *
   * @param aItem &lt;&gt; - the item, may be <code>null</code>
   * @param aIconSize {@link EIconSize} - requested icon size
   * @return {@link Image} - the icon or <code>null</code> if item has no icon
   */
  default Image getIcon( T aItem, EIconSize aIconSize ) {
    TsNullArgumentRtException.checkNull( aIconSize );
    return null;
  }

  /**
   * Returns the thumbnail of the item.
   * <p>
   * Implementation must respect argument aThumbSize and return the image of specified size.
   *
   * @param aItem &lt;&gt; - the item, may be <code>null</code>
   * @param aThumbSize {@link EThumbSize} - requested thumbnail size
   * @return {@link TsImage} - the thumbnail or <code>null</code> if item has no thumbnail
   */
  default TsImage getThumb( T aItem, EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    return null;
  }

}

/**
 * {@link ITsVisualsProvider#DEFAULT} implementation.
 *
 * @author hazard157
 */
class InternalDefaultItemVisualsProvider
    implements ITsVisualsProvider<Object>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Correctly deserializes {@link ITsVisualsProvider#DEFAULT}.
   *
   * @return Object - always {@link ITsVisualsProvider#DEFAULT}
   * @throws ObjectStreamException just declaration is never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsVisualsProvider.DEFAULT;
  }

  @Override
  public String getName( Object aItem ) {
    return ITsNameProvider.DEFAULT.getName( aItem );
  }

  @Override
  public String getDescription( Object aItem ) {
    return ITsNameProvider.DEFAULT.getDescription( aItem );
  }

}
