package org.toxsoft.tsgui.utils.jface;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Адаптер для удобства над {@link IStructuredContentProvider} с типизацией.
 *
 * @author goga
 * @param <T> - тип элементов поставляемого содержимого
 */
public abstract class StructuredContentProviderAdapter<T>
    implements IStructuredContentProvider {

  @Override
  public void dispose() {
    // nop
  }

  @Override
  public void inputChanged( Viewer viewer, Object oldInput, Object newInput ) {
    // nop
  }

  @Override
  abstract public T[] getElements( Object aInputElement );

}
