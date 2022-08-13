package org.toxsoft.core.tsgui.utils.swt;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.*;

/**
 * Adapter for {@link ILabelProvider}.
 *
 * @author hazard157
 */
public abstract class LabelProviderAdapter
    extends LabelProvider {

  @Override
  public Image getImage( Object element ) {
    return null;
  }

  @Override
  abstract public String getText( Object element );

}
