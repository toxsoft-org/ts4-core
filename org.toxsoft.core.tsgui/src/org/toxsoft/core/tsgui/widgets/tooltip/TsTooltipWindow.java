package org.toxsoft.core.tsgui.widgets.tooltip;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The tooltip window designed to display information provided by {@link ITsVisualsProvider}.
 * <p>
 * Usage:
 * <ul>
 * <li>implement interface {@link ITsTooltipDataProvider} as a standalone class or a mix-in to control;</li>
 * <li>create instance of this class by constructor
 * {@link #TsTooltipWindow(Control, ITsGuiContext, ITsTooltipDataProvider)};</li>
 * <li>optionally, you may hold reference to the created tooltip window to {@link #activate()} or {@link #deactivate()}
 * tooltip display.</li>
 * </ul>
 *
 * @author hazard157
 */
public class TsTooltipWindow
    extends ToolTip
    implements ITsGuiContextable {

  /**
   * Background color as {@link EAtomicType#VALOBJ VALOBJ} {@link RGB}, or {@link IAtomicValue#NULL} for default color.
   * <br>
   * Default value: {@link IAtomicValue#NULL}
   */
  public static final IDataDef BK_COLOR = DataDef.create( "TsTooltipWindow.bkColor", VALOBJ, //$NON-NLS-1$
      TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL //
  );

  /**
   * Text color as {@link EAtomicType#VALOBJ VALOBJ} {@link RGB}, or {@link IAtomicValue#NULL} for default color. <br>
   * Default value: {@link IAtomicValue#NULL}
   */
  public static final IDataDef FG_COLOR = DataDef.create( "TsTooltipWindow.fgColor", VALOBJ, //$NON-NLS-1$
      TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL //
  );

  /**
   * Text font as {@link EAtomicType#VALOBJ VALOBJ} {@link IFontInfo}, or {@link IAtomicValue#NULL} for default font.
   * <br>
   * Default value: {@link IAtomicValue#NULL}
   */
  public static final IDataDef FONT_INFO = DataDef.create( "TsTooltipWindow.fontInfo", VALOBJ, //$NON-NLS-1$
      TSID_KEEPER_ID, FontInfo.KEEPER_ID, //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL //
  );

  private final Control                theControl;
  private final ITsGuiContext          tsContext;
  private final ITsTooltipDataProvider dataProvider;

  /**
   * Creates new instance which add TooltipSupport to the widget
   *
   * @param aControl the control to which the tooltip is bound
   * @param aContext {@link ITsGuiContext} - the context
   * @param aDataProvider {@link ITsTooltipDataProvider} - the data provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsTooltipWindow( Control aControl, ITsGuiContext aContext, ITsTooltipDataProvider aDataProvider ) {
    super( aControl, NO_RECREATE, false );
    TsNullArgumentRtException.checkNulls( aContext, aDataProvider );
    theControl = aControl;
    tsContext = aContext;
    dataProvider = aDataProvider;
  }

  // ------------------------------------------------------------------------------------
  // ToolTip
  //

  /**
   * Creates the content are of the the tooltip.
   *
   * @param aEvent {@link Event} - the event that triggered the activation of the tooltip
   * @param aParent {@link Composite} - the parent of the content area
   * @return {@link Composite} - the content area created
   */
  @Override
  protected Composite createToolTipContentArea( Event aEvent, Composite aParent ) {

    // TODO create panel with CLabel left (for image) and disabled text right for wrapped text display

    // create CLabel with data from provider
    CLabel label = new CLabel( aParent, SWT.SHADOW_NONE );
    ITsTooltipDataProvider.Data data = dataProvider.getTsTooltipData( theControl, aEvent.x, aEvent.y );
    Image image = data != null ? data.image() : null;
    String text = data != null ? data.tooltip() : null;
    if( text != null ) {
      label.setText( text );
    }
    if( image != null ) {
      label.setImage( image );
    }
    // setup CLabel depending on supplied options if any
    IAtomicValue av = FG_COLOR.getValue( tsContext.params() );
    RGB fgRgb = av.isAssigned() ? av.asValobj() : null;
    if( fgRgb != null ) {
      label.setForeground( colorManager().getColor( fgRgb ) );
    }
    av = BK_COLOR.getValue( tsContext.params() );
    RGB bkRgb = av.isAssigned() ? av.asValobj() : null;
    if( bkRgb != null ) {
      label.setBackground( colorManager().getColor( bkRgb ) );
    }
    av = FONT_INFO.getValue( tsContext.params() );
    IFontInfo fontInfo = av.isAssigned() ? av.asValobj() : null;
    if( fontInfo != null ) {
      label.setFont( fontManager().getFont( fontInfo ) );
    }
    return label;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  @Override
  public void activate() {
    super.activate();
  }

  @Override
  public void deactivate() {
    super.deactivate();
  }

}
