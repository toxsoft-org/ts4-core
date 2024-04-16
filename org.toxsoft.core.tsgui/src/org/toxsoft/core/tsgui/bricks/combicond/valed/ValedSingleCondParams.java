package org.toxsoft.core.tsgui.bricks.combicond.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.combicond.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.math.combicond.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VALED to edit {@link ISingleCondParams}.
 *
 * @author hazard157
 */
public class ValedSingleCondParams
    extends AbstractValedControl<ISingleCondParams, Control> {

  /**
   * The factory class.
   *
   * @author hazard157
   */
  public static class Factory
      extends AbstractValedControlFactory {

    /**
     * Constructor.
     */
    public Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<ISingleCondParams> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedSingleCondParams( aContext );
    }

  }

  /**
   * The registered factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".SingleCondParams"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<ISingleCondParams> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedSingleCondParams( aContext );
    }
  };

  /**
   * Reference to the {@link ISingleCondTypesRegistry} to be used for condition type retrieval.
   * <p>
   * May be set both before VALED creation and after VALED creation.
   * <p>
   * If not specified, the reference to {@link DefaultSingleCondTypesRegistry#INSTANCE} will be used.
   */
  @SuppressWarnings( "rawtypes" )
  public static final ITsContextRefDef<ISingleCondTypesRegistry> REFDEF_SCT_REGISTRY = new TsContextRefDef<>(
      TS_FULL_ID + ".tsgui.ValedSingleCondParams.SctRegistry", ISingleCondTypesRegistry.class, IOptionSet.NULL ); //$NON-NLS-1$

  private SingleCondParamsPanel panel;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the VALED context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedSingleCondParams( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    panel = new SingleCondParamsPanel( tsContext(), isCreatedUneditable() );
    panel.createControl( aParent );
    ISingleCondTypesRegistry<ISingleCondType> sctReg =
        REFDEF_SCT_REGISTRY.getRef( tsContext(), DefaultSingleCondTypesRegistry.INSTANCE );
    panel.addRegistry( sctReg );
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
  protected ISingleCondParams doGetUnvalidatedValue() {
    return panel.getEntity();
  }

  @Override
  protected void doSetUnvalidatedValue( ISingleCondParams aValue ) {
    panel.setEntity( aValue );
  }

  @Override
  protected void doClearValue() {
    panel.setEntity( null );
  }

}
