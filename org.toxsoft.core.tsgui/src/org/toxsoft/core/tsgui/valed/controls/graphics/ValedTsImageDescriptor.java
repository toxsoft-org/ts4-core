package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * VALED for {@link TsImageDescriptor} editing.
 *
 * @author vs
 */
public class ValedTsImageDescriptor
    extends AbstractValedLabelAndButton<TsImageDescriptor> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".TsImageDescriptor"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author vs
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
    protected IValedControl<TsImageDescriptor> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedTsImageDescriptor( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( TsLineInfo.class );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  TsImageDescriptor imageDescriptor = TsImageDescriptor.NONE;

  protected ValedTsImageDescriptor( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  //
  //

  @Override
  protected boolean doProcessButtonPress() {
    TsImageDescriptor imd = PanelTsImageDescriptorEditor.editImageDescriptor( imageDescriptor, tsContext() );
    if( imd == null ) {
      return false;
    }
    imageDescriptor = imd;
    return true;
  }

  @Override
  protected void doUpdateLabelControl() {
    String s = imageDescriptor != null ? imageDescriptor.toString() : TsLibUtils.EMPTY_STRING;
    getLabelControl().setText( s );
  }

  @Override
  protected TsImageDescriptor doGetUnvalidatedValue() {
    return imageDescriptor;
  }

  @Override
  protected void doDoSetUnvalidatedValue( TsImageDescriptor aValue ) {
    imageDescriptor = aValue;
  }

}
