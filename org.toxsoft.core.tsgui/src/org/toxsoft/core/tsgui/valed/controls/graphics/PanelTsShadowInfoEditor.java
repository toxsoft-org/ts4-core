package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.shadow.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель редактирования параметров линии.
 * <p>
 *
 * @author vs
 */
public class PanelTsShadowInfoEditor
    extends AbstractTsDialogPanel<TsShadowInfo, ITsGuiContext> {

  ValedIntegerSpinner              widthSpiner;
  ValedEnumCombo<ETsLineType>      lineTypeCombo;
  ValedEnumCombo<ETsLineCapStyle>  capStyleCombo;
  ValedEnumCombo<ETsLineJoinStyle> joinStyleCombo;

  Path path;

  int maxBlur   = 100;
  int maxXshift = 100;
  int maxYshift = 100;

  PanelTsShadowInfoEditor( Composite aParent, TsDialog<TsShadowInfo, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    if( tsContext().params().hasKey( ValedTsShadowInfo.PARAMID_MAX_BLUR ) ) {
      maxBlur = tsContext().params().getInt( ValedTsShadowInfo.PARAMID_MAX_BLUR );
    }
    if( tsContext().params().hasKey( ValedTsShadowInfo.PARAMID_MAX_XSHIFT ) ) {
      maxXshift = tsContext().params().getInt( ValedTsShadowInfo.PARAMID_MAX_XSHIFT );
    }
    if( tsContext().params().hasKey( ValedTsShadowInfo.PARAMID_MAX_YSHIFT ) ) {
      maxYshift = tsContext().params().getInt( ValedTsShadowInfo.PARAMID_MAX_YSHIFT );
    }
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( TsShadowInfo aData ) {
    if( aData != null ) {
      spinnerBlur.setSelection( aData.blur() );
      spinnerXshift.setSelection( aData.xOffset() );
      spinnerYshift.setSelection( aData.yOffset() );
      rgbaPanel.setRgba( aData.rgba() );
    }
  }

  @Override
  protected TsShadowInfo doGetDataRecord() {
    int blur = spinnerBlur.getSelection();
    int dx = spinnerXshift.getSelection();
    int dy = spinnerYshift.getSelection();

    RGBA rgba = rgbaPanel.rgba();
    return new TsShadowInfo( blur, dx, dy, rgba );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  Spinner      spinnerBlur;
  Slider       sliderBlur;
  Spinner      spinnerXshift;
  Slider       sliderXshift;
  Spinner      spinnerYshift;
  Slider       sliderYshift;
  RgbaSelector rgbaPanel;
  boolean      isDropShadow = true;

  void init() {
    setLayout( new GridLayout( 1, false ) );

    Canvas previewPanel = new Canvas( this, SWT.BORDER | SWT.DOUBLE_BUFFERED );
    GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true );
    gd.widthHint = 750;
    gd.heightHint = 400;
    previewPanel.setLayoutData( gd );

    previewPanel.addPaintListener( aEvent -> {

      aEvent.gc.setAntialias( SWT.ON );
      aEvent.gc.setTextAntialias( SWT.ON );

      Rectangle r = getClientArea();
      // aEvent.gc.setBackground( colorManager().getColor( ETsColor.RED ) );
      // aEvent.gc.fillRectangle( r );

      path = new Path( getDisplay() );
      FontInfo fi = new FontInfo( "Arial", 72, SWT.NONE );
      aEvent.gc.setFont( fontManager().getFont( fi ) );
      Point p = aEvent.gc.textExtent( "Test string" );
      int tx = (int)((previewPanel.getClientArea().width - p.x) / 2.);
      int ty = (int)((previewPanel.getClientArea().height - p.y) / 2.);
      if( !isDropShadow ) {
        tx = (int)((previewPanel.getClientArea().width) / 2.);
        ty = (int)((previewPanel.getClientArea().height) / 2.);
      }

      TextLayout layout = new TextLayout( aEvent.display );
      layout.setFont( aEvent.gc.getFont() );
      layout.setText( "Test string" );

      // getBounds() — логический прямоугольник
      // Rectangle logical = layout.getBounds();

      // getLineBounds(0) — прямоугольник конкретной строки
      Rectangle lineBounds = layout.getLineBounds( 0 );
      aEvent.gc.drawRectangle( tx, ty, lineBounds.width, lineBounds.height );
      // aEvent.gc.drawRectangle( logical );

      layout.dispose(); // обязательно!

      // aEvent.gc.drawRectangle( tx, ty, p.x, p.y );

      path.addString( "Test string", tx, ty, fontManager().getFont( fi ) );

      // path.addArc( tx - 100, ty - 100, 200, 200, 0, 360 );
      int blur = sliderBlur.getSelection();
      int dx = sliderXshift.getSelection() - maxXshift;
      int dy = sliderYshift.getSelection() - maxYshift;

      RGBA rgba = rgbaPanel.rgba();
      TsShadowInfo shadowInfo = new TsShadowInfo( blur, dx, dy, rgba );
      ImageData shadowData;
      if( isDropShadow ) {
        shadowData = TsShadowUtils.buildDropShadowData( path, shadowInfo, getDisplay(), true );
      }
      else {
        shadowData = TsShadowUtils.buildInnerShadowData( path, shadowInfo, getDisplay() );
      }
      Image img = new Image( getDisplay(), shadowData );
      ImageData imd = img.getImageData();
      // aEvent.gc.drawImage( img, tx - 100 - shadowInfo.blur() + shadowInfo.xOffset(), //
      // ty - 100 - shadowInfo.blur() + shadowInfo.yOffset() );

      // aEvent.gc.drawImage( img, tx - 100 + shadowInfo.blur() - shadowInfo.xOffset(), //
      // ty - 100 + shadowInfo.blur() - shadowInfo.yOffset() );
      // aEvent.gc.drawImage( img, tx - 100 + shadowInfo.xOffset(), ty - 100 + shadowInfo.yOffset() );
      if( isDropShadow ) {
        aEvent.gc.drawImage( img, tx + dx - shadowInfo.blur(), ty + dy - shadowInfo.blur() );
      }
      else {
        aEvent.gc.drawImage( img, tx - 100, ty - 100 );
      }

      img.dispose();

      aEvent.gc.setForeground( colorManager().getColor( ETsColor.BLUE ) );
      aEvent.gc.setBackground( colorManager().getColor( ETsColor.BLUE ) );
      if( isDropShadow ) {
        aEvent.gc.fillPath( path );
      }
      aEvent.gc.drawPath( path );
      path.dispose();

    } );

    Composite bottomPanel = new Composite( this, SWT.NONE );
    bottomPanel.setLayout( new GridLayout( 2, false ) );
    bottomPanel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

    Composite ctrlPanel = new Composite( bottomPanel, SWT.NONE );
    ctrlPanel.setLayout( new GridLayout( 3, false ) );
    CLabel labelBlur = new CLabel( ctrlPanel, SWT.NONE );
    labelBlur.setText( "Размытие: " );

    sliderBlur = new Slider( ctrlPanel, SWT.HORIZONTAL );
    sliderBlur.setValues( 15, 0, maxBlur + 1, 1, 1, 5 );
    sliderBlur.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    sliderBlur.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        spinnerBlur.setSelection( sliderBlur.getSelection() );
        previewPanel.redraw();
      }
    } );

    spinnerBlur = new Spinner( ctrlPanel, SWT.BORDER );
    spinnerBlur.setValues( 15, 0, maxBlur, 0, 1, 5 );
    gd = new GridData( SWT.LEFT, SWT.TOP, false, false );
    gd.widthHint = 40;
    spinnerBlur.setLayoutData( gd );
    spinnerBlur.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        sliderBlur.setSelection( spinnerBlur.getSelection() );
        previewPanel.redraw();
      }
    } );

    CLabel labelXshift = new CLabel( ctrlPanel, SWT.NONE );
    labelXshift.setText( "Сдвиг по X: " );

    sliderXshift = new Slider( ctrlPanel, SWT.HORIZONTAL );
    sliderXshift.setValues( maxXshift, 0, 2 * maxXshift + 1, 1, 1, 5 );
    sliderXshift.setSelection( 0 );
    sliderXshift.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    sliderXshift.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        spinnerXshift.setSelection( sliderXshift.getSelection() - maxXshift );
        previewPanel.redraw();
      }
    } );

    spinnerXshift = new Spinner( ctrlPanel, SWT.BORDER );
    // spinnerXshift.setValues( maxXshift, 0, 2 * maxXshift, 0, 1, 5 );
    spinnerXshift.setMinimum( -maxXshift );
    spinnerXshift.setMaximum( maxXshift );
    spinnerXshift.setSelection( 0 );

    gd = new GridData( SWT.LEFT, SWT.TOP, false, false );
    gd.widthHint = 40;
    spinnerXshift.setLayoutData( gd );
    spinnerXshift.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        sliderXshift.setSelection( spinnerXshift.getSelection() + maxXshift );
        previewPanel.redraw();
      }
    } );

    CLabel labelYshift = new CLabel( ctrlPanel, SWT.NONE );
    labelYshift.setText( "Сдвиг по Y: " );

    sliderYshift = new Slider( ctrlPanel, SWT.HORIZONTAL );
    sliderYshift.setValues( maxYshift, 0, 2 * maxYshift, 1, 1, 5 );
    sliderYshift.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    sliderYshift.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        spinnerYshift.setSelection( sliderYshift.getSelection() - maxYshift );
        previewPanel.redraw();
      }
    } );

    spinnerYshift = new Spinner( ctrlPanel, SWT.BORDER );
    // spinnerYshift.setValues( maxYshift, 0, 2 * maxYshift, 0, 1, 5 );
    spinnerYshift.setMinimum( -maxYshift );
    spinnerYshift.setMaximum( maxYshift );
    spinnerYshift.setSelection( 0 );
    gd = new GridData( SWT.LEFT, SWT.TOP, false, false );
    gd.widthHint = 40;
    spinnerYshift.setLayoutData( gd );
    spinnerYshift.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        sliderYshift.setSelection( spinnerYshift.getSelection() + maxYshift );
        previewPanel.redraw();
      }
    } );

    rgbaPanel = new RgbaSelector( bottomPanel, SWT.NONE, tsContext().eclipseContext() );
    rgbaPanel.setRgba( new RGBA( 0, 0, 0, 200 ) );
    rgbaPanel.genericChangeEventer().addListener( aSource -> {
      previewPanel.redraw();
    } );
  }

  // ------------------------------------------------------------------------------------
  // static method to create or edit Pattern
  //

  /**
   * Рдактирует и возвращает значение параметров контура.
   * <p>
   *
   * @param aInfo TsShadowInfo - параметры заливки
   * @param aContext - контекст
   * @return TsLineInfo - параметры заливки или <b>null</b> в случает отказа от редактирования
   */
  public static final TsShadowInfo editPathData( TsShadowInfo aInfo, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<TsShadowInfo, ITsGuiContext> creator = PanelTsShadowInfoEditor::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, DLG_T_LINE_INFO, STR_MSG_LINE_INFO );
    TsDialog<TsShadowInfo, ITsGuiContext> d = new TsDialog<>( dlgInfo, aInfo, aContext, creator );
    return d.execData();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  // private Image createLabelImage() {
  // GC gc = null;
  // try {
  // Image img = new Image( Display.getCurrent(), 32, 22 );
  // gc = new GC( img );
  // gc.setBackground( colorManager().getColor( ETsColor.WHITE ) );
  // gc.fillRectangle( 0, 0, 32, 22 );
  // gc.setForeground( colorManager().getColor( ETsColor.BLACK ) );
  // gc.setLineStyle( SWT.LINE_SOLID );
  // gc.setLineWidth( 5 );
  // gc.drawLine( 5, 5, 28, 16 );
  // return img;
  // }
  // finally {
  // if( gc != null ) {
  // gc.dispose();
  // }
  // }
  // }

}
