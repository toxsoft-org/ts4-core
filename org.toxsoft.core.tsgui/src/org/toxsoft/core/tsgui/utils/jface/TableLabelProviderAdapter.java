package org.toxsoft.core.tsgui.utils.jface;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Adapter for easy {@link ITableLabelProvider} implementation.
 *
 * @author hazard157
 */
public abstract class TableLabelProviderAdapter
    extends LabelProvider
    implements ITableLabelProvider {

  @Override
  public Image getColumnImage( Object element, int columnIndex ) {
    return null;
  }

  @Override
  abstract public String getColumnText( Object element, int columnIndex );

}
