package org.toxsoft.core.tsgui.mws.e4.handlers;

import static org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogConstants.*;
import static org.toxsoft.core.tsgui.mws.e4.handlers.ITsResources.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tsgui.mws.appinf.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;

/**
 * E4 command handler: show "About program" information dialog.
 * <p>
 * Command ID: {@link IMwsCoreConstants#MWSID_CMD_ABOUT}.
 *
 * @author hazard157
 */
public class CmdMwsAbout {

  static final EIconSize ABOUT_ICON_SIZE = EIconSize.IS_128X128;

  /**
   * About dialog content.
   *
   * @author hazard157
   */
  static class AboutContent
      extends AbstractTsDialogPanel<ITsApplicationInfo, Object> {

    final Label label;

    AboutContent( Composite aParent, TsDialog<ITsApplicationInfo, Object> aOwnerDialog ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      //
      IPdwWidget imageWidget = new PdwWidgetSimple( tsContext() );
      imageWidget.createControl( this );
      imageWidget.getControl().setLayoutData( BorderLayout.WEST );
      int sz = ABOUT_ICON_SIZE.pointSize().x() * 2;
      imageWidget.setAreaPreferredSize( new TsPoint( sz, sz ) );
      imageWidget.setFitInfo( RectFitInfo.BEST_FILL );
      imageWidget.setFulcrum( ETsFulcrum.CENTER );
      imageWidget.setPreferredSizeFixed( true );
      Image icon = iconManager().loadStdIcon( ITsStdIconIds.ICONID_TSAPP_WINDOWS_ICON, ABOUT_ICON_SIZE );
      TsImage tsImg = TsImage.create( icon );
      imageWidget.setTsImage( tsImg );
      //
      label = new Label( this, SWT.CENTER | SWT.BORDER | SWT.WRAP );
      label.setLayoutData( BorderLayout.CENTER );
    }

    @Override
    protected void doSetDataRecord( ITsApplicationInfo aData ) {
      if( aData != null ) {
        label.setText( makeMassage( aData ) );
        this.pack();
      }
    }

    @Override
    protected ITsApplicationInfo doGetDataRecord() {
      return null;
    }

  }

  static String makeMassage( ITsApplicationInfo appInfo ) {
    return String.format( "\n" + //$NON-NLS-1$
        "%s - %s\n" + //$NON-NLS-1$
        "ID: %s\n" + //$NON-NLS-1$
        "Ver: %s\n" + //$NON-NLS-1$
        "\n" + //$NON-NLS-1$
        "%s\n", //$NON-NLS-1$
        appInfo.alias(), appInfo.nmName(), //
        appInfo.id(), //
        appInfo.version().toString(), //
        appInfo.description() //
    );

  }

  @Execute
  void execute( IEclipseContext aAppContext, IMwsOsgiService aMws ) {
    IDialogPanelCreator<ITsApplicationInfo, Object> creator = AboutContent::new;
    ITsApplicationInfo appInfo = aMws.appInfo();
    ITsGuiContext ctx = new TsGuiContext( aAppContext );
    TsDialogInfo cdi = new TsDialogInfo( ctx, ctx.get( Shell.class ), DLG_C_ABOUT, DLG_T_ABOUT, DF_NO_APPROVE );
    ITsPoint p = new TsPoint( 4 * ABOUT_ICON_SIZE.size(), 3 * ABOUT_ICON_SIZE.size() );
    cdi.setMaxSize( p );
    TsDialog<ITsApplicationInfo, Object> d = new TsDialog<>( cdi, appInfo, null, creator );
    d.execData();
  }

}
