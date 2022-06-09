package org.toxsoft.core.tsgui.m5.valeds.multimodown;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактор поля связи {@link M5MultiLookupFieldDef} в виде списка с панелью управления.
 *
 * @author goga
 * @param <V> - тип значения поля
 */
public class ValedMultiModownTableViewer<V>
    extends AbstractValedMultiModownEditor<V> {

  IM5CollectionPanel<V>             panel         = null;
  private M5DefaultItemsProvider<V> itemsProvider = new M5DefaultItemsProvider<>();

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ValedMultiModownTableViewer( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, avInt( 15 ) );
    itemsProvider.genericChangeEventer().addListener( aSource -> {
      if( panel != null ) {
        panel.refresh();
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedMultiModownEditor
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    IM5Model<V> model = fieldDef().itemModel();
    panel = model.panelCreator().createCollViewerPanel( tsContext(), itemsProvider );
    panel.createControl( aParent );
    return panel.getControl();
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    panel.setEditable( aEditable );
  }

  @Override
  public ValidationResult canGetValue() {
    ValidationResult vr = validateItemsCount( panel.items().size(), fieldDef().maxCount(), fieldDef().isExactCount() );
    if( vr.isError() ) {
      return vr;
    }
    return ValidationResult.firstNonOk( vr, super.canGetValue() );
  }

  @Override
  protected IList<V> doGetUnvalidatedValue() {
    return panel.items();
  }

  @Override
  protected void doSetUnvalidatedValue( IList<V> aValue ) {
    if( aValue == null ) {
      itemsProvider.items().clear();
    }
    else {
      itemsProvider.items().setAll( aValue );
    }
  }

  @Override
  protected void doClearValue() {
    itemsProvider.items().clear();
  }

}
