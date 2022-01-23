package org.toxsoft.tsgui.utils.swt;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * Адаптер для удобства над {@link org.eclipse.swt.events.SelectionListener}.
 *
 * @author hazard157
 */
abstract public class SelectionListenerAdapter
    implements SelectionListener {

  @Override
  public void widgetDefaultSelected( SelectionEvent e ) {
    // nop
  }

  @Override
  public abstract void widgetSelected( SelectionEvent e );
}
