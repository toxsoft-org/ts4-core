package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель редактирования параметров преобразования координат.
 * <p>
 *
 * @author vs
 */
public class PanelD2ConversionEditor
    extends AbstractTsDialogPanel<ID2Conversion, ITsGuiContext> {

  /**
   * Конструктор.<br>
   *
   * @param aParent Composite - родительская компонента
   * @param aOwnerDialog TsDialog - родительский диалог
   */
  public PanelD2ConversionEditor( Composite aParent, TsDialog<ID2Conversion, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    init();
  }

  /**
   * Конструктор для использовани вне диалога.<br>
   *
   * @param aParent {@link Composite} - родительская компонента
   * @param aContext {@link ITsGuiContext} - соответствующий контекст
   * @param aData {@link ID2Conversion} - параметры преобразования м.б. <b>null</b>
   */
  public PanelD2ConversionEditor( Composite aParent, ITsGuiContext aContext, ID2Conversion aData ) {
    super( aParent, aContext, aData, aContext, 0 );
    this.setLayout( new BorderLayout() );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( ID2Conversion aData ) {
    if( aData != null ) {
      angleEditor.setValue( aData.rotation() );
      scaleEditor.setValue( Double.valueOf( aData.zoomFactor() ) );
      pointEditor.setValue( aData.origin() );
    }
    else {
      scaleEditor.setValue( Double.valueOf( 1 ) );
    }
  }

  @Override
  protected ID2Conversion doGetDataRecord() {
    ID2Angle angle = angleEditor.getValue();
    Double zoomFactor = scaleEditor.getValue();
    ID2Point origin = pointEditor.getValue();
    return new D2Conversion( angle, zoomFactor.doubleValue(), origin );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ValedD2Angle angleEditor;

  private ValedD2Point pointEditor;

  private ValedDoubleSpinner scaleEditor;

  private void init() {

    GridLayout gl = new GridLayout( 4, false );
    setLayout( gl );

    CLabel l;

    l = new CLabel( this, SWT.NONE );
    l.setText( "Угол:" );
    angleEditor = new ValedD2Angle( new TsGuiContext( tsContext() ) );
    angleEditor.createControl( this );

    l = new CLabel( this, SWT.NONE );
    l.setText( "Масштаб:" );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ValedDoubleSpinner.OPDEF_STEP.setValue( ctx.params(), avFloat( 0.1 ) );
    ValedDoubleSpinner.OPDEF_PAGE_STEP.setValue( ctx.params(), avFloat( 1.0 ) );
    scaleEditor = new ValedDoubleSpinner( ctx );
    scaleEditor.setValue( Double.valueOf( 1.0 ) );
    scaleEditor.createControl( this );

    l = new CLabel( this, SWT.NONE );
    l.setText( "Точка опоры поворота и масштаба:" );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false, 2, 1 ) );
    pointEditor = new ValedD2Point( new TsGuiContext( tsContext() ) );
    pointEditor.createControl( this ).setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false, 2, 1 ) );
  }

  // ------------------------------------------------------------------------------------
  // static method to create or edit ID2Conversion
  //

  /**
   * Рдактирует и возвращает значения преобразования координат.
   * <p>
   *
   * @param aInfo ID2Conversion - параметры преобразования координат
   * @param aContext - контекст
   * @return ID2Conversion - параметры преобразования координат или <b>null</b> в случает отказа от редактирования
   */
  public static final ID2Conversion editD2Conversion( ID2Conversion aInfo, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<ID2Conversion, ITsGuiContext> creator = PanelD2ConversionEditor::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, "DLG_T_FILL_INFO", "STR_MSG_FILL_INFO" );
    TsDialog<ID2Conversion, ITsGuiContext> d = new TsDialog<>( dlgInfo, aInfo, aContext, creator );
    return d.execData();
  }

}
