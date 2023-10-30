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
import org.toxsoft.core.tsgui.utils.layout.*;
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

  /**
   * Конструктор панели, предназаначенной для использования вне диалога.
   * <p>
   *
   * @param aParent {@link Composite} - родительская компонента
   * @param aContext {@link ITsGuiContext} - the context
   * @param aData &lt;T&gt; - initial data record value, may be <code>null</code>
   * @param aFlags int - ORed dialog configuration flags <code>DF_XXX</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelTsImageDescriptorEditor( Composite aParent, ITsGuiContext aContext, TsImageDescriptor aData,
      int aFlags ) {
    super( aParent, aContext, aData, aContext, aFlags );
    init();
    doSetDataRecord( aData );
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
    GridLayout gl = new GridLayout( 3, false );
    gl.marginLeft = 0;
    gl.marginWidth = 0;
    setLayout( gl );

    CLabel l = new CLabel( this, SWT.CENTER );
    l.setText( STR_L_IMG_SOURCE_KIND );

    kindCombo = new ComboViewer( this, SWT.BORDER | SWT.READ_ONLY );

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
            fireContentChangeEvent();
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
