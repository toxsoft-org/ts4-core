package org.toxsoft.core.tsgui.valed.controls.graphics;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;

/**
 * VALED to view {@link TsImageDescriptor} as a thumbnail.
 *
 * @author vs
 */
public class ValedTsImageDescriptorViewer
    extends AbstractValedControl<TsImageDescriptor, Control> {

  private IPdwWidget imageWidget;

  private TsImageDescriptor value = TsImageDescriptor.NONE;

  ValedTsImageDescriptorViewer( ITsGuiContext aContext ) {
    super( aContext );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    imageWidget = new PdwWidgetSimple( ctx );
    imageWidget.setFitInfo( RectFitInfo.BEST );
    imageWidget.setFulcrum( ETsFulcrum.CENTER );
    imageWidget.setAreaPreferredSize( EThumbSize.SZ256.pointSize() ); // TODO size as option
    imageWidget.setPreferredSizeFixed( true );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedLabelAndButton
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    imageWidget.createControl( aParent );
    return imageWidget.getControl();
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // nop
  }

  @Override
  protected TsImageDescriptor doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( TsImageDescriptor aValue ) {
    value = aValue;
    TsImage image = imageManager().getImage( value );
    imageWidget.setTsImage( image );
    imageWidget.redraw();
  }

  @Override
  protected void doClearValue() {
    value = TsImageDescriptor.NONE;
    imageWidget.setTsImage( null );
  }

}
