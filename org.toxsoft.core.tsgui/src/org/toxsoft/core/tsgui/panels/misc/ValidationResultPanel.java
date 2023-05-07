package org.toxsoft.core.tsgui.panels.misc;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Panel to display single {@link ValidationResult}.
 * <p>
 * The panel has a fixed height (determined by the given icon size). On the left - the status icon of the validation
 * type {@link EValidationResultType#iconId()}, the rest will be occupied by the multi-line text of the message
 * {@link ValidationResult#message()}.
 *
 * @author hazard157
 */
public class ValidationResultPanel
    extends TsPanel {

  // ------------------------------------------------------------------------------------
  // Respected options
  //

  /**
   * Status icon size, determines the height of the panel.<br>
   * Type: {@link EAtomicType#VALOBJ} - {@link EIconSize}<br>
   * Usage: determines the size of the displayed icon and the height panel.<br>
   * Default: {@link EIconSize#IS_48X48}
   */
  public static final IDataDef OPDEF_ICON_SIZE = DataDef.create( TS_ID + ".gui.ValresPanel.IconSize", //$NON-NLS-1$
      VALOBJ, //
      TSID_KEEPER_ID, EIconSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EIconSize.IS_48X48 ) //
  );

  /**
   * Flag for using the icon when the validation status is {@link EValidationResultType#OK OK}.<br>
   * Type: {@link EAtomicType#BOOLEAN}<br>
   * Usage: if <code>true</code>, then on status {@link EValidationResultType#OK OK} will be displayed corresponding
   * icon. If <code>false</code> then no icon will be displayed.<br>
   * Default: <code>false</code> - no icon
   */
  public static final IDataDef IS_OK_ICON_USED = DataDef.create( TS_ID + ".gui.ValresPanel.IsOkIconUsed", //$NON-NLS-1$
      BOOLEAN, //
      TSID_DEFAULT_VALUE, AV_FALSE//
  );

  /**
   * Text to display when validation result is the {@link ValidationResult#SUCCESS}.<br>
   * Type: {@link EAtomicType#STRING}<br>
   * Usage: sets the text to be displayed on the status {@link ValidationResult#SUCCESS}. Note: for other instances of
   * type {@link EValidationResultType#OK} the {@link ValidationResult#message()} will be displayed.<br>
   * Default: "" (an empty string)
   */
  public static final IDataDef OPDEF_SUCCESS_MESSAGE = DataDef.create( TS_ID + ".gui.ValresPanel.SuccessMessage", //$NON-NLS-1$
      BOOLEAN, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  // ------------------------------------------------------------------------------------
  //

  private final IMapEdit<EValidationResultType, TsImage> imgMap = new ElemMap<>();

  private final IPdwWidget imageWidget;
  private final Text       text;
  private ValidationResult status = ValidationResult.SUCCESS;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValidationResultPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    BorderLayout borderLayout = new BorderLayout();
    this.setLayout( borderLayout );
    // imageWidget
    imageWidget = new PdwWidgetSimple( tsContext() );
    imageWidget.createControl( this );
    imageWidget.getControl().setLayoutData( BorderLayout.WEST );
    EThumbSize thumbSize = EThumbSize.findIncluding( getIconSize() );
    imageWidget.setAreaPreferredSize( thumbSize.pointSize() );
    imageWidget.setFulcrum( ETsFulcrum.CENTER );
    imageWidget.setPreferredSizeFixed( true );
    // text
    text = new Text( this, SWT.MULTI | SWT.BORDER );
    text.setBackground( this.getBackground() ); // reset the background color to parent for widget to be invisible
    text.setEditable( false );
    text.setLayoutData( BorderLayout.CENTER );
    // init images
    for( EValidationResultType vrt : EValidationResultType.asList() ) {
      Image img = iconManager().loadStdIcon( vrt.iconId(), getIconSize() );
      imgMap.put( vrt, TsImage.create( img ) );
    }
    // setup
    int height = thumbSize.size() + borderLayout.getTopMargin() + borderLayout.getBottomMargin();
    this.setMinimumHeight( height );
    this.setMaximumHeight( height );
    setShownValidationResult( ValidationResult.SUCCESS );
  }

  @Override
  protected void doDispose() {
    while( !imgMap.isEmpty() ) {
      TsImage img = imgMap.removeByKey( imgMap.keys().first() );
      img.dispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private final EIconSize getIconSize() {
    return OPDEF_ICON_SIZE.getValue( tsContext().params() ).asValobj();
  }

  private final String getSuccessMessage() {
    return OPDEF_SUCCESS_MESSAGE.getValue( tsContext().params() ).asString();
  }

  private final boolean isOkIconUsed() {
    return IS_OK_ICON_USED.getValue( tsContext().params() ).asBool();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns currently displayed validation result.
   * <p>
   * Initially returns {@link ValidationResult#SUCCESS}.
   *
   * @return {@link ValidationResult} - the validation result
   */
  public ValidationResult getShownValidationResult() {
    return status;
  }

  /**
   * Sets validation result to be displayed
   *
   * @param aStatus {@link ValidationResult} - the validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setShownValidationResult( ValidationResult aStatus ) {
    TsNullArgumentRtException.checkNull( aStatus );
    status = aStatus;
    TsImage image = imgMap.getByKey( status.type() );
    String msg = status.message();
    if( status.type() == EValidationResultType.OK ) {
      if( !isOkIconUsed() ) {
        image = null;
      }
      if( status == ValidationResult.SUCCESS ) {
        msg = getSuccessMessage();
      }
    }
    imageWidget.setTsImage( image );
    imageWidget.redraw();
    text.setText( msg );
  }

}
