package org.toxsoft.core.tsgui.ved.glib.inspector;

import java.util.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Displayes properties of the currently selected component and allows to change them.
 * <p>
 * TODO how to get selected component?
 *
 * @author hazard157
 */
public class ComponentInpectorPanel
    extends TsPanel
    implements IVedContextable {

  private final IGenericChangeListener dataModelChangeListener         = aSource -> updateOnDataModelChanged();
  private final IGenericChangeListener activeScreenChangeListener      = aSource -> updateOnScreenChanged();
  private final IGenericChangeListener selectedComponentChangeListener = aSource -> updateOnSelectionChanged();

  private final IOptionSetPanel panel;

  private IVedScreen activeScreen = null;

  private IVedComponent selComponent = null;

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ComponentInpectorPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    panel = new OptionSetPanel( ctx, false );
    panel.createControl( this );
    panel.getControl().setLayoutData( BorderLayout.CENTER );
    panel.optionChangeEventer().addListener( ( s, opId, newVal ) -> whenInspectorChanged( opId, newVal ) );
    vedEnv().dataModel().genericChangeEventer().addListener( dataModelChangeListener );
    vedEnv().screenManager().activeScreenChangeEventer().addListener( activeScreenChangeListener );
    updateOnScreenChanged();
  }

  @Override
  protected void doDispose() {
    vedEnv().dataModel().genericChangeEventer().removeListener( dataModelChangeListener );
    vedEnv().screenManager().activeScreenChangeEventer().removeListener( activeScreenChangeListener );
    if( activeScreen != null ) {
      activeScreen.selectionManager().genericChangeEventer().removeListener( selectedComponentChangeListener );
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void whenInspectorChanged( String aOptionId, IAtomicValue aNewValue ) {
    TsInternalErrorRtException.checkNull( selComponent );
    selComponent.props().setValue( aOptionId, aNewValue );
  }

  void editComponent( IVedComponent aComp ) {
    if( aComp == null ) {
      panel.setOptionDefs( IStridablesList.EMPTY );
      panel.setEditable( false );
      selComponent = null;
      return;
    }
    if( !Objects.equals( aComp, selComponent ) ) {
      panel.setEditable( true );
      panel.setOptionDefs( aComp.provider().propDefs() );
      selComponent = aComp;
    }
    panel.setEntity( aComp.props() );
  }

  void updateOnSelectionChanged() {
    IVedComponent newSel = null;
    if( activeScreen != null ) {
      newSel = activeScreen.selectionManager().selectedComponent();
    }
    editComponent( newSel );
  }

  /**
   * On active screen change switches to listen new active screen and updates the inspector.
   */
  void updateOnScreenChanged() {
    if( activeScreen != null ) {
      activeScreen.selectionManager().genericChangeEventer().removeListener( selectedComponentChangeListener );
    }
    activeScreen = vedEnv().screenManager().activeScreen();
    if( activeScreen != null ) {
      activeScreen.selectionManager().genericChangeEventer().addListener( selectedComponentChangeListener );
    }
    updateOnSelectionChanged();
  }

  /**
   * When data model changes it is interpreted as possible selection change.
   */
  void updateOnDataModelChanged() {
    updateOnSelectionChanged();
  }

}
