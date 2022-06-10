package org.toxsoft.core.tsgui.m5.valeds.multimodown;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактор поля связи {@link M5MultiLookupFieldDef} в виде списка с панелью управления.
 *
 * @author goga
 * @param <V> - тип значения поля
 */
public class ValedMultiModownTableEditor<V>
    extends AbstractValedMultiModownEditor<V> {

  // FIXME есть проблема - про вызове не "руками", нужно указывать правильный мастер-объект
  // правильно все работает, только если мастер = null

  /**
   * Original items lifecycle manager wrapper edits items in {@link ValedMultiModownTableEditor#itemsContainer}.
   *
   * @author hazard157
   */
  class LifecycleManagerWrapper
      implements IM5LifecycleManager<V> {

    IM5LifecycleManager<V> lm;

    LifecycleManagerWrapper( IM5LifecycleManager<V> aLifecycleManager ) {
      lm = TsNullArgumentRtException.checkNull( aLifecycleManager );
    }

    @Override
    public boolean isCrudOpAllowed( ECrudOp aOp ) {
      return lm.isCrudOpAllowed( aOp );
    }

    @Override
    public IM5Model<V> model() {
      return lm.model();
    }

    @Override
    public IM5ItemsProvider<V> itemsProvider() {
      return lm.itemsProvider();
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public Object master() {
      return lm.master();
    }

    @Override
    public ValidationResult canCreate( IM5Bunch<V> aValues ) {
      return lm.canCreate( aValues );
    }

    @Override
    public V create( IM5Bunch<V> aValues ) {
      V v = lm.create( aValues );
      if( v != null ) {
        itemsContainer.items().add( v );
      }
      return v;
    }

    @Override
    public ValidationResult canEdit( IM5Bunch<V> aValues ) {
      return lm.canEdit( aValues );
    }

    @Override
    public V edit( IM5Bunch<V> aValues ) {
      V v = lm.edit( aValues );
      if( v != null ) {
        int index = itemsContainer.items().indexOf( aValues.originalEntity() );
        itemsContainer.items().set( index, v );
      }
      return v;
    }

    @Override
    public ValidationResult canRemove( V aEntity ) {
      return lm.canRemove( aEntity );
    }

    @Override
    public void remove( V aEntity ) {
      lm.remove( aEntity );
      itemsContainer.items().remove( aEntity );
    }

  }

  final M5DefaultItemsProvider<V> itemsContainer = new M5DefaultItemsProvider<>();

  IM5CollectionPanel<V> panel = null;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ValedMultiModownTableEditor( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, avInt( 15 ) );
    itemsContainer.genericChangeEventer().addListener( aSource -> {
      if( panel != null ) {
        panel.refresh();
      }
    } );
    itemsContainer.genericChangeEventer().addListener( widgetValueChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //
  void updateOnMasterObject() {
    Object mo = findMasterObject();
    IM5LifecycleManager<V> lm = new LifecycleManagerWrapper( fieldDef().itemModel().findLifecycleManager( mo ) );
    panel.setLifecycleManager( lm );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedM5FieldEditor
  //

  @Override
  protected void onMasterObjectChanged( Object aNewMaster, Object aOldMaster ) {
    updateOnMasterObject();
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedMultiModownEditor
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    IM5Model<V> model = fieldDef().itemModel();
    IM5LifecycleManager<V> lm =
        new LifecycleManagerWrapper( fieldDef().itemModel().getLifecycleManager( findMasterObject() ) );
    panel = model.panelCreator().createCollEditPanel( tsContext(), itemsContainer, lm );
    updateOnMasterObject();
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
      itemsContainer.items().clear();
    }
    else {
      itemsContainer.items().setAll( aValue );
    }
  }

  @Override
  protected void doClearValue() {
    itemsContainer.items().clear();
  }

}
