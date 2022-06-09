package org.toxsoft.core.tsgui.m5.valeds.multimodown;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Просмотрщик ссылочного поля {@link M5MultiModownFieldDef} в виде нередактируемого текстового поля.
 *
 * @author goga
 * @param <V> - тип значения элементов спискочного поля
 */
public class ValedMultiModownTextViewer<V>
    extends AbstractValedMultiModownEditor<V> {

  final IListEdit<V> value = new ElemLinkedBundleList<>();
  Text               text;

  /**
   * Конструктор.
   *
   * @param aContext {@link IEclipseContext} - контекст приложения
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ValedMultiModownTextViewer( ITsGuiContext aContext ) {
    super( aContext );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    text = new Text( aParent, SWT.BORDER );
    text.setEditable( false );
    return text;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // nop
  }

  @Override
  protected IList<V> doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( IList<V> aValue ) {
    if( aValue == null || aValue.isEmpty() ) {
      value.clear();
      text.setText( TsLibUtils.EMPTY_STRING );
      return;
    }
    value.setAll( aValue );
    text.setText( fieldDef().formatFieldText( aValue ) );
  }

  @Override
  protected void doClearValue() {
    doSetUnvalidatedValue( null );
  }

}
