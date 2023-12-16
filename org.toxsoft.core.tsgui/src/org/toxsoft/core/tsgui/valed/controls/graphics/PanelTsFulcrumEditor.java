package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель редактирования параметров опорной точки.
 * <p>
 *
 * @author vs
 */
public class PanelTsFulcrumEditor
    extends AbstractTsDialogPanel<TsFulcrum, ITsGuiContext> {

  ValedEnumCombo<ETsFulcrum> fulcrumCombo;
  ValedAvFloatSpinner        xSpinner;
  ValedAvFloatSpinner        ySpinner;

  PanelTsFulcrumEditor( Composite aParent, TsDialog<TsFulcrum, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( TsFulcrum aData ) {
    if( aData != null ) {
      if( aData.fulcrum() != null ) {
        fulcrumCombo.setValue( aData.fulcrum() );
      }
      xSpinner.setValue( avFloat( aData.xPerc() ) );
      ySpinner.setValue( avFloat( aData.yPerc() ) );
    }
    else {
      TsFulcrum tf = TsFulcrum.of( ETsFulcrum.LEFT_TOP );
      xSpinner.setValue( avFloat( tf.xPerc() ) );
      ySpinner.setValue( avFloat( tf.yPerc() ) );
    }
  }

  @Override
  protected TsFulcrum doGetDataRecord() {
    if( fulcrumCombo.canGetValue() == ValidationResult.SUCCESS ) {
      ETsFulcrum f = fulcrumCombo.getValue();
      if( f != null ) {
        return TsFulcrum.of( f );
      }
    }
    return TsFulcrum.of( xSpinner.getValue().asDouble(), ySpinner.getValue().asDouble() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void init() {
    setLayout( new GridLayout( 2, false ) );

    CLabel l;
    l = new CLabel( this, SWT.NONE );
    l.setText( STR_L_FULCRUM_TYPE );

    fulcrumCombo = new ValedEnumCombo<>( tsContext(), ETsFulcrum.class, IStridable::nmName );
    fulcrumCombo.createControl( this );
    fulcrumCombo.eventer().addListener( ( aSource, aEditFinished ) -> {
      if( aSource.canGetValue() == ValidationResult.SUCCESS ) {
        ETsFulcrum f = fulcrumCombo.getValue();
        if( f != null ) {
          TsFulcrum tf = TsFulcrum.of( f );
          xSpinner.setValue( avFloat( tf.xPerc() ) );
          ySpinner.setValue( avFloat( tf.yPerc() ) );
        }
      }
    } );

    l = new CLabel( this, SWT.NONE );
    l.setText( STR_L_X_SHIFT );
    xSpinner = new ValedAvFloatSpinner( environ() );
    xSpinner.createControl( this );

    l = new CLabel( this, SWT.NONE );
    l.setText( STR_L_Y_SHIFT );
    ySpinner = new ValedAvFloatSpinner( environ() );
    ySpinner.createControl( this );
  }

  // ------------------------------------------------------------------------------------
  // static method to create or edit fulcrum
  //

  /**
   * Рдактирует и возвращает значение параметров опорной точки.
   * <p>
   *
   * @param aInfo TsFulcrum - параметры опорной точки
   * @param aContext - контекст
   * @return TsFulcrum - параметры опорной точки или <b>null</b> в случает отказа от редактирования
   */
  public static final TsFulcrum edit( TsFulcrum aInfo, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<TsFulcrum, ITsGuiContext> creator = PanelTsFulcrumEditor::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, DLG_T_TS_FULCRUM, STR_MSG_TS_FULCRUM );
    TsDialog<TsFulcrum, ITsGuiContext> d = new TsDialog<>( dlgInfo, aInfo, aContext, creator );
    return d.execData();
  }

}
