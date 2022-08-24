package org.toxsoft.core.tsgui.valed.controls.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель редактирования параметров линии.
 * <p>
 *
 * @author vs
 */
public class PanelTsLinfoEditor
    extends AbstractTsDialogPanel<TsLineInfo, ITsGuiContext> {

  ValedIntegerSpinner              widthSpiner;
  ValedEnumCombo<ETsLineType>      lineTypeCombo;
  ValedEnumCombo<ETsLineCapStyle>  capStyleCombo;
  ValedEnumCombo<ETsLineJoinStyle> joinStyleCombo;

  PanelTsLinfoEditor( Composite aParent, TsDialog<TsLineInfo, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( TsLineInfo aData ) {
    if( aData != null ) {
      widthSpiner.setValue( Integer.valueOf( aData.width() ) );
      lineTypeCombo.setValue( aData.type() );
      capStyleCombo.setValue( aData.capStyle() );
      joinStyleCombo.setValue( aData.joinStyle() );
    }
  }

  @Override
  protected TsLineInfo doGetDataRecord() {
    int capStyle = capStyleCombo.getValue().getSwtStyle();
    int joinStyle = joinStyleCombo.getValue().getSwtStyle();
    LineAttributes la = new LineAttributes( widthSpiner.getValue().intValue(), capStyle, joinStyle );
    return TsLineInfo.fromLineAttributes( la );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  ITsVisualsProvider<? extends IStridable> visualsProvider = IStridable::nmName;

  void init() {
    setLayout( new GridLayout( 2, false ) );

    CLabel l;
    widthSpiner = new ValedIntegerSpinner( tsContext() );
    l = new CLabel( this, SWT.NONE );
    l.setText( "толщина: " );
    // l.setImage( createLabelImage() );
    widthSpiner.createControl( this );
    widthSpiner.setLimits( 1, 1, 1, 100 );

    lineTypeCombo = new ValedEnumCombo( tsContext(), ETsLineType.class, visualsProvider );
    l = new CLabel( this, SWT.NONE );
    l.setText( "тип линии: " );
    lineTypeCombo.createControl( this );
    lineTypeCombo.setValue( ETsLineType.SOLID );

    capStyleCombo = new ValedEnumCombo( tsContext(), ETsLineCapStyle.class, visualsProvider );
    l = new CLabel( this, SWT.NONE );
    l.setText( "тип окончания: " );
    capStyleCombo.createControl( this );
    capStyleCombo.setValue( ETsLineCapStyle.FLAT );

    joinStyleCombo = new ValedEnumCombo( tsContext(), ETsLineJoinStyle.class, visualsProvider );
    l = new CLabel( this, SWT.NONE );
    l.setText( "тип соедиения: " );
    joinStyleCombo.createControl( this );
    joinStyleCombo.setValue( ETsLineJoinStyle.MITER );
  }

  // ------------------------------------------------------------------------------------
  // static method to create or edit Pattern
  //

  /**
   * Рдактирует и возвращает значение параметров заливки.
   * <p>
   *
   * @param aInfo TsLineInfo - параметры заливки
   * @param aContext - контекст
   * @return TsLineInfo - параметры заливки или <b>null</b> в случает отказа от редактирования
   */
  public static final TsLineInfo editLineInfo( TsLineInfo aInfo, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<TsLineInfo, ITsGuiContext> creator = PanelTsLinfoEditor::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, "Caption", "Title" );
    TsDialog<TsLineInfo, ITsGuiContext> d = new TsDialog<>( dlgInfo, aInfo, aContext, creator );
    return d.execData();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  Image createLabelImage() {
    GC gc = null;
    try {
      Image img = new Image( Display.getCurrent(), 32, 22 );
      gc = new GC( img );
      gc.setBackground( colorManager().getColor( ETsColor.WHITE ) );
      gc.fillRectangle( 0, 0, 32, 22 );
      gc.setForeground( colorManager().getColor( ETsColor.BLACK ) );
      gc.setLineStyle( SWT.LINE_SOLID );
      gc.setLineWidth( 5 );
      gc.drawLine( 5, 5, 28, 16 );
      return img;
    }
    finally {
      if( gc != null ) {
        gc.dispose();
      }
    }
  }

}
