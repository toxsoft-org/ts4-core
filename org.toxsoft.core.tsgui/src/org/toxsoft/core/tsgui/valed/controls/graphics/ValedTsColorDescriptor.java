package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * VALED for {@link TsColorDescriptor} editing.
 *
 * @author vs
 */
public class ValedTsColorDescriptor
    extends AbstractValedLabelAndButton<TsColorDescriptor> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".TsColorDescriptor"; //$NON-NLS-1$

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
    protected IValedControl<TsColorDescriptor> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedTsColorDescriptor( aContext );
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

  TsColorDescriptor colorDescriptor = TsColorDescriptor.NONE;

  protected ValedTsColorDescriptor( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  //
  //

  @Override
  protected boolean doProcessButtonPress() {
    TsColorDescriptor color = PanelTsColorDescriptorEditor.editColorDescriptor( colorDescriptor, tsContext() );
    if( color == null ) {
      return false;
    }
    colorDescriptor = color;
    return true;
  }

  @Override
  protected void doUpdateLabelControl() {
    String s = TsLibUtils.EMPTY_STRING;
    if( colorDescriptor != null ) {
      ITsColorSourceKind kind = TsColorDescriptor.getColorSourceKindsMap().findByKey( colorDescriptor.kindId() );
      Color c = kind.createColor( colorDescriptor, getDisplay() );
      s = c.toString();
    }
    getLabelControl().setText( s );
  }

  @Override
  protected TsColorDescriptor doGetUnvalidatedValue() {
    return colorDescriptor;
  }

  @Override
  protected void doDoSetUnvalidatedValue( TsColorDescriptor aValue ) {
    colorDescriptor = aValue;
  }

}
