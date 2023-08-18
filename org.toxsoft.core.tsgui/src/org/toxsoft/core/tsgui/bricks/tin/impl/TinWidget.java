package org.toxsoft.core.tsgui.bricks.tin.impl;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITinWidget} implementation.
 *
 * @author hazard157
 */
public class TinWidget
    extends AbstractTsStdEventsProducerLazyPanel<Object, Control>
    implements ITinWidget {

  private ITinTypeInfo typeInfo = null;

  private TinTree   tinTree;
  private TinTopRow topRow = null;

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
    tinTree = new TinTree( aParent, tsContext() );
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
    if( topRow != null ) {
      topRow.genericChangeEventer().removeListener( genericChangeEventer );
    }
    if( aEntityInfo != null ) {
      topRow = tinTree.papiCreateTopRow( aEntityInfo );
      // topRow.setTinValue( typeInfo.makeValue( null ) );
      topRow.genericChangeEventer().addListener( genericChangeEventer );
    }
    else {
      topRow = null;
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

}
