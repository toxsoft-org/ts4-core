package org.toxsoft.core.tsgui.bricks.tin.impl;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITinWidget} implementation.
 *
 * @author hazard157
 */
public class TinWidget
    extends AbstractTsStdEventsProducerLazyPanel<Object, Control>
    implements ITinWidget {

  private final IListEdit<ITinWidgetPropertyChangeListener> propListeners = new ElemArrayList<>();

  private ITinTypeInfo typeInfo = null;

  private TinTree tinTree;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TinWidget( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  final void papiFireProperyChangeEvent( String aPropId ) {
    if( !propListeners.isEmpty() ) {
      for( ITinWidgetPropertyChangeListener l : new ElemArrayList<>( propListeners ) ) {
        l.onPropertyChange( this, aPropId );
      }
    }
    genericChangeEventer().fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsStdEventsProducerLazyPanel
  //

  @Override
  public Object selectedItem() {
    return tinTree.getSelctedRow();
  }

  @Override
  public void setSelectedItem( Object aItem ) {
    if( aItem instanceof ITinRow tinRow ) {
      tinTree.setSelectedRow( tinRow );
    }
    else {
      tinTree.setSelectedRow( null );
    }
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    tinTree = new TinTree( aParent, this );
    return tinTree;
  }

  // ------------------------------------------------------------------------------------
  // IGenericContentPanel
  //

  @Override
  public boolean isViewer() {
    return false;
  }

  // ------------------------------------------------------------------------------------
  // ITinWidget
  //

  @Override
  public void setEntityInfo( ITinTypeInfo aEntityInfo ) {
    typeInfo = aEntityInfo;
    TinTopRow topRow = null;
    if( aEntityInfo != null ) {
      topRow = tinTree.papiCreateTopRow( aEntityInfo );
    }
    tinTree.papiSetRoot( topRow );
  }

  @Override
  public ITinTypeInfo getTypeInfo() {
    return typeInfo;
  }

  @Override
  public void setValue( ITinValue aValue ) {
    if( typeInfo != null ) {
      ITinValue tv = aValue != null ? aValue : ITinValue.NULL;
      tinTree.papiGetRoot().setTinValue( tv );
    }
  }

  @Override
  public ValidationResult canGetValue() {
    // TODO how to perform value check ?
    return ValidationResult.SUCCESS;
  }

  @Override
  public ITinValue getValue() {
    ITinValue tv = ITinValue.NULL;
    if( tinTree.papiGetRoot() != null ) {
      tv = tinTree.papiGetRoot().getTinValue();
    }
    return tv;
  }

  @Override
  public void addPropertyChangeListener( ITinWidgetPropertyChangeListener aListener ) {
    if( !propListeners.hasElem( aListener ) ) {
      propListeners.add( aListener );
    }
  }

  @Override
  public void removePropertyChangeListener( ITinWidgetPropertyChangeListener aListener ) {
    propListeners.remove( aListener );
  }

}
