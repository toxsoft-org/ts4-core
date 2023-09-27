package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель редактирования параметров дескриптора изображения.
 * <p>
 *
 * @author vs
 */
public class PanelTsImageDescriptorEditor
    extends AbstractTsDialogPanel<TsImageDescriptor, ITsGuiContext> {

  ComboViewer kindCombo;
  Button      btnEdit;

  TsImageDescriptor imageDescriptor = TsImageDescriptor.NONE;

  PanelTsImageDescriptorEditor( Composite aParent, TsDialog<TsImageDescriptor, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( TsImageDescriptor aData ) {
    if( aData != null ) {
      imageDescriptor = aData;
    }
    IStringMap<ITsImageSourceKind> kindsMap = TsImageDescriptor.getImageSourceKindsMap();
    kindCombo.setSelection( new StructuredSelection( kindsMap.getByKey( imageDescriptor.kindId() ) ) );
  }

  @Override
  protected TsImageDescriptor doGetDataRecord() {
    return imageDescriptor;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void init() {
    setLayout( new GridLayout( 3, false ) );

    CLabel l = new CLabel( this, SWT.CENTER );
    l.setText( STR_L_IMG_FILL_KIND );

    kindCombo = new ComboViewer( this, SWT.BORDER );

    kindCombo.setContentProvider( new ArrayContentProvider() );
    kindCombo.setLabelProvider( new LabelProvider() {

      @Override
      public String getText( Object aElement ) {
        ITsImageSourceKind kind = (ITsImageSourceKind)aElement;
        return kind.nmName();
      }
    } );

    IStringMap<ITsImageSourceKind> kindsMap = TsImageDescriptor.getImageSourceKindsMap();
    kindCombo.setInput( kindsMap.values().toArray() );

    btnEdit = new Button( this, SWT.PUSH );
    btnEdit.setText( STR_B_IMG_DESCR_EDIT );
    btnEdit.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        ISelection sel = kindCombo.getSelection();
        if( !sel.isEmpty() ) {
          AbstractTsImageSourceKind kind = (AbstractTsImageSourceKind)((IStructuredSelection)sel).getFirstElement();
          TsImageDescriptor imd = kind.editDescription( kind.params(), environ() );
          if( imd != null ) {
            imageDescriptor = imd;
          }
        }
      }

    } );

  }

  // ------------------------------------------------------------------------------------
  // static method to create or edit TsImageDescriptor
  //

  /**
   * Редактирует и возвращает значение параметров дескриптора изображения.
   * <p>
   *
   * @param aImgDescr TsImageDescriptor - дескриптора изображения
   * @param aContext {@link ITsGuiContext} - соответствующий контекст
   * @return TsImageDescriptor - параметры дескриптора изображения или <b>null</b> в случает отказа от редактирования
   */
  public static final TsImageDescriptor editImageDescriptor( TsImageDescriptor aImgDescr, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<TsImageDescriptor, ITsGuiContext> creator = PanelTsImageDescriptorEditor::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, DLG_T_IMAGE_DESCRIPTOR, STR_MSG_IMAGE_DESCRIPTOR );
    TsDialog<TsImageDescriptor, ITsGuiContext> d = new TsDialog<>( dlgInfo, aImgDescr, aContext, creator );
    return d.execData();
  }

}
