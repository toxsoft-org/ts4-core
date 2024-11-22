package org.toxsoft.core.tsgui.bricks.cond.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.cond.*;
import org.toxsoft.core.tsgui.bricks.cond.impl.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsCombiCondInfo} editor implemented as {@link IPanelCombiCondInfo}.
 * <p>
 * Accepts options defined in {@link PanelCombiCondInfo}.
 *
 * @author hazard157
 */
public class ValedCombiCondInfo
    extends AbstractValedControl<ITsCombiCondInfo, Control> {

  /**
   * The reference in the context to initialize internal topic manager.
   */
  public static final ITsGuiContextRefDef<ITsConditionsTopicManager> REFDEF_TOPIC_MANAGER =
      PanelSingleCondInfo.REFDEF_TOPIC_MANAGER;

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".CombiCondInfo"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<ITsCombiCondInfo> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedCombiCondInfo( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( ITsCombiCondInfo.class );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  private final IPanelCombiCondInfo panel;

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedCombiCondInfo( ITsGuiContext aTsContext ) {
    super( aTsContext );
    OPDEF_IS_HEIGHT_FIXED.setValue( tsContext().params(), AV_FALSE );
    OPDEF_IS_WIDTH_FIXED.setValue( tsContext().params(), AV_FALSE );
    OPDEF_VERTICAL_SPAN.setValue( tsContext().params(), avInt( 12 ) );
    panel = new PanelCombiCondInfo( tsContext(), isCreatedUneditable() );
    ITsConditionsTopicManager tm = REFDEF_TOPIC_MANAGER.getRef( tsContext() );
    panel.setTopicManager( tm );
    panel.genericChangeEventer().addListener( widgetValueChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    panel.createControl( aParent );
    return panel.getControl();
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    panel.setEditable( aEditable );
  }

  @Override
  public ValidationResult canGetValue() {
    return panel.canGetEntity();
  }

  @Override
  protected ITsCombiCondInfo doGetUnvalidatedValue() {
    return panel.getEntity();
  }

  @Override
  protected void doSetUnvalidatedValue( ITsCombiCondInfo aValue ) {
    panel.setEntity( aValue );
  }

  @Override
  protected void doClearValue() {
    panel.setEntity( null );
  }

}
