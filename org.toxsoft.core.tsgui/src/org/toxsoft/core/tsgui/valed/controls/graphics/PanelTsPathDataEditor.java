package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import java.io.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.path.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель редактирования параметров линии.
 * <p>
 *
 * @author vs
 */
public class PanelTsPathDataEditor
    extends AbstractTsDialogPanel<TsPathData, ITsGuiContext> {

  Path       path = null;
  Canvas     previewPanel;
  ListViewer pathInfoesList;

  IStringMap<String> pathInfoes;

  PanelTsPathDataEditor( Composite aParent, TsDialog<TsPathData, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( TsPathData aData ) {
    if( aData != null ) {
      PathData pd = path.getPathData();
      pd.types = aData.types();
      pd.points = aData.points();
      // widthSpiner.setValue( Integer.valueOf( aData.width() ) );
      // lineTypeCombo.setValue( aData.type() );
      // capStyleCombo.setValue( aData.capStyle() );
      // joinStyleCombo.setValue( aData.joinStyle() );
    }
  }

  @Override
  protected TsPathData doGetDataRecord() {
    // int capStyle = capStyleCombo.getValue().getSwtStyle();
    // int joinStyle = joinStyleCombo.getValue().getSwtStyle();
    // LineAttributes la = new LineAttributes( widthSpiner.getValue().intValue(), capStyle, joinStyle );
    // return TsPathData.fromLineAttributes( la );
    if( path != null && !path.isDisposed() ) {
      PathData pd = path.getPathData();
      return new TsPathData( pd.types, pd.points );
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void init() {
    setLayout( new GridLayout( 1, false ) );

    Composite ctrlPanel = new Composite( this, SWT.NONE );
    ctrlPanel.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    ctrlPanel.setLayout( new GridLayout( 4, false ) );

    Button btnImport = new Button( ctrlPanel, SWT.PUSH );
    btnImport.setText( "Импорт..." );
    btnImport.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        FileDialog fd = new FileDialog( getShell(), SWT.OPEN );
        String filePath = fd.open();
        if( filePath != null && !filePath.isBlank() ) {
          File file = new File( filePath );
          pathInfoes = PathDataUtils.importPathInfoesFromSvg( file );
          // System.out.println( "Paths count: " + pathInfoes.size() );
          pathInfoesList.setInput( pathInfoes.keys().toArray() );
        }
      }
    } );

    SashForm sash = new SashForm( this, SWT.HORIZONTAL );
    sash.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

    pathInfoesList = new ListViewer( sash, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER );
    pathInfoesList.setContentProvider( new ArrayContentProvider() );
    pathInfoesList.setLabelProvider( new LabelProvider() {

      @Override
      public String getText( Object aElement ) {
        return aElement.toString();
      }
    } );
    pathInfoesList.addSelectionChangedListener( aEvent -> {
      IStructuredSelection sel = aEvent.getStructuredSelection();
      if( !sel.isEmpty() ) {
        String pathStr = pathInfoes.getByKey( (String)sel.getFirstElement() );
        // System.out.println( pathStr );
        if( path != null ) {
          path.dispose();
        }
        path = SvgPathParser.svgPathStr2SwtPath( getDisplay(), pathStr );
        previewPanel.redraw();
      }
    } );

    previewPanel = new Canvas( sash, SWT.BORDER );
    previewPanel.addPaintListener( aEvent -> {
      if( path != null && !path.isDisposed() ) {
        aEvent.gc.drawPath( path );
      }
    } );

    sash.setWeights( 1, 4 );
  }

  // ------------------------------------------------------------------------------------
  // static method to create or edit Pattern
  //

  /**
   * Рдактирует и возвращает значение параметров контура.
   * <p>
   *
   * @param aInfo TsPathData - параметры заливки
   * @param aContext - контекст
   * @return TsLineInfo - параметры заливки или <b>null</b> в случает отказа от редактирования
   */
  public static final TsPathData editPathData( TsPathData aInfo, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<TsPathData, ITsGuiContext> creator = PanelTsPathDataEditor::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, DLG_T_LINE_INFO, STR_MSG_LINE_INFO );
    TsDialog<TsPathData, ITsGuiContext> d = new TsDialog<>( dlgInfo, aInfo, aContext, creator );
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
