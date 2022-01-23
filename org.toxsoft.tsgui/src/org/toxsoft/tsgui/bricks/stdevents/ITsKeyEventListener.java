package org.toxsoft.tsgui.bricks.stdevents;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

/**
 * Слушатель SWT-событий от клавиатуры общего назначения.
 * <p>
 * Для кажого типа события ({@link SWT#KeyDown}, {@link SWT#KeyUp}) нужно иметь совю подписку (то есть, свой регистратор
 * слушателя addKeyXxxEvent()).
 *
 * @author goga
 */
public interface ITsKeyEventListener {

  /**
   * Вызывается при событий клавиатуры.
   * <p>
   * Общее правило имплементации слушателя рекомендут, когда событие обработано, вернуть <code>true</code>, иначе,
   * вернуть <code>false</code>, чтобы другой слушатель мог обработать событие.
   *
   * @param aSource Object - источни события, приводится к конкретному типу источника
   * @param aEvent {@link Event} - событие клавиатуры
   * @return boolean - признак обработки события<br>
   *         <b>true</b> - событие обработано, дальнейшую обработку следует прекратить;<br>
   *         <b>false</b> - следует продолжить обработку клавиши
   */
  boolean onTsKeyEvent( Object aSource, Event aEvent );

}
