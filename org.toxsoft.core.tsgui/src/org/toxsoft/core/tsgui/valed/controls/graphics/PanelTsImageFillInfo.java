package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

// C:\works\git-repos\ci\ru.toxsoft.ci.ws.mnemos\icons\ci-compressor.png

/**
 * Панель редактирования параметров заливки изображением.
 * <p>
 *
 * @author vs
 */
public class PanelTsImageFillInfo
    extends AbstractTsDialogPanel<TsImageFillInfo, ITsGuiContext> {

  /**
   * Панель предпросмотра выбранного изображения.
   *
   * @author vs
   */
  static class PreviewPanel
      extends Canvas {

    int width  = 8;
    int height = 8;

    private final Color lightColor;
    private final Color darkColor;

    Color color = null;
    int   alpha = 255;

    Color colorBlack;
    Color colorWhite;

    private TsImage tsImage = null;

    private final ITsImageManager imageManager;

    PreviewPanel( Composite aParent, ITsImageManager aImageManager ) {
      super( aParent, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED );

      imageManager = aImageManager;

      lightColor = new Color( ETsColor.DARK_GRAY.rgb() );
      darkColor = new Color( ETsColor.GRAY.rgb() );

      colorBlack = new Color( ETsColor.BLACK.rgb() );
      colorWhite = new Color( ETsColor.WHITE.rgb() );

      addPaintListener( aEvent -> {
        Point p = getSize();
        int rows = p.y / 8 + 1;
        int columns = p.x / 8 + 1;
        for( int i = 0; i < columns; i++ ) {
          for( int j = 0; j < rows; j++ ) {
            if( (i + j) % 2 == 1 ) {
              aEvent.gc.setBackground( darkColor );
            }
            else {
              aEvent.gc.setBackground( lightColor );
            }
            aEvent.gc.fillRectangle( i * 8, j * 8, width, height );
          }
        }
        if( color != null ) {
          aEvent.gc.setAlpha( alpha );
          aEvent.gc.setBackground( color );
          aEvent.gc.fillRectangle( 0, 0, p.x, p.y );
        }

        if( tsImage != null ) {
          Point panelSize = getSize();
          ITsPoint imgSize = tsImage.imageSize();

          double kWidth = (double)panelSize.x / imgSize.x();
          double kHeight = (double)panelSize.y / imgSize.y();

          double scaleFactor = 1.0;

          if( kWidth < kHeight && kWidth < 1 ) {
            scaleFactor = kWidth;
          }
          if( kHeight < kWidth && kHeight < 1 ) {
            scaleFactor = kHeight;
          }

          ITsPoint newSize = new TsPoint( (int)(imgSize.x() * scaleFactor), (int)(imgSize.y() * scaleFactor) );

          int imgX = (panelSize.x - newSize.x()) / 2;
          int imgY = (panelSize.y - newSize.y()) / 2;
          aEvent.gc.drawImage( tsImage.image(), 0, 0, imgSize.x(), imgSize.y(), imgX, imgY, newSize.x(), newSize.y() );
        }

        aEvent.gc.setForeground( colorBlack );
        aEvent.gc.drawRectangle( 0, 0, p.x - 1, p.y - 1 );
        aEvent.gc.setForeground( colorWhite );
        aEvent.gc.drawRectangle( 1, 1, p.x - 3, p.y - 3 );
      } );

    }

    void setColor( Color aColor, int aAlpha ) {
      color = aColor;
      alpha = aAlpha;
    }

    void setImageDescriptor( TsImageDescriptor aImageDescriptor ) {
      if( aImageDescriptor != null ) {
        tsImage = imageManager.getImage( aImageDescriptor );
      }
      else {
        tsImage = null;
      }
      redraw();
    }

    String imageSizeStr() {
      if( tsImage != null ) {
        ITsPoint size = tsImage.imageSize();
        return "" + size.x() + "x" + size.y(); //$NON-NLS-1$ //$NON-NLS-2$
      }
      return TsLibUtils.EMPTY_STRING;
    }
  }

  // private PanelTsImageDescriptorEditor imdPanel;
  private PanelTsImageSourceEditor       imdPanel;
  private Composite                      imagePanel;
  private PreviewPanel                   previewPanel;
  private ValedEnumCombo<EImageFillKind> imgFillTypeCombo;

  CLabel labelSize;
  Text   nameField;

  PanelTsImageFillInfo( Composite aParent, TsDialog<TsImageFillInfo, ITsGuiContext> aOwnerDialog ) {
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
  public PanelTsImageFillInfo( Composite aParent, ITsGuiContext aContext, TsImageFillInfo aData, int aFlags ) {
    super( aParent, aContext, aData, aContext, aFlags );
    init();
    doSetDataRecord( aData );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( TsImageFillInfo aData ) {
    if( aData != null ) {
      imgFillTypeCombo.setValue( aData.kind() );
      TsImageDescriptor imd = aData.imageDescriptor();
      imdPanel.setDataRecord( imd );
      previewPanel.setImageDescriptor( imd );
      labelSize.setText( previewPanel.imageSizeStr() );
      if( imd != null ) {
        nameField.setText( imd.toString() );
      }
      else {
        nameField.setText( TsLibUtils.EMPTY_STRING );
      }
    }
  }

  @Override
  protected TsImageFillInfo doGetDataRecord() {
    TsImageDescriptor imd = imdPanel.getDataRecord();
    if( imd == null ) {
      return TsImageFillInfo.DEFAULT;
    }
    return new TsImageFillInfo( imd, imgFillTypeCombo.getValue() );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает значение параметров заливки изображением.
   *
   * @param aFillInfo {@link TsImageFillInfo} - параметры заливки изображением
   */
  public void setImageFillInfo( TsImageFillInfo aFillInfo ) {
    doSetDataRecord( aFillInfo );
  }

  // ------------------------------------------------------------------------------------
  // Статический метод вызова диалога редактирования
  //

  /**
   * Рдактирует и возвращает значение параметров заливки.
   * <p>
   *
   * @param aInfo TsFillInfo - параметры заливки
   * @param aContext - контекст
   * @return TsImageFillInfo - параметры заливки или <b>null</b> в случает отказа от редактирования
   */
  public static final TsImageFillInfo edit( TsImageFillInfo aInfo, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<TsImageFillInfo, ITsGuiContext> creator = PanelTsImageFillInfo::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, DLG_T_FILL_INFO, STR_MSG_FILL_INFO );
    TsDialog<TsImageFillInfo, ITsGuiContext> d = new TsDialog<>( dlgInfo, aInfo, aContext, creator );
    return d.execData();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void init() {
    setLayout( new BorderLayout() );

    Composite topPanel = new Composite( this, SWT.NONE );
    topPanel.setLayout( new GridLayout( 3, false ) );
    topPanel.setLayoutData( BorderLayout.NORTH );

    // imdPanel = new PanelTsImageDescriptorEditor( topPanel, tsContext(), null, 0 );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    imdPanel = new PanelTsImageSourceEditor( topPanel, ctx, null, 0 );

    CLabel l = new CLabel( topPanel, SWT.CENTER );
    l.setText( STR_L_FILL_IMAGE_KIND );

    imgFillTypeCombo = new ValedEnumCombo<>( ctx, EImageFillKind.class, IStridable::nmName );
    imgFillTypeCombo.createControl( topPanel );
    imgFillTypeCombo.setValue( EImageFillKind.CENTER );

    imagePanel = new Composite( this, SWT.NONE );
    imagePanel.setLayoutData( BorderLayout.CENTER );
    imagePanel.setLayout( new GridLayout( 4, false ) );

    l = new CLabel( imagePanel, SWT.CENTER );
    l.setText( STR_L_IMAGE_DESCRIPTION );
    nameField = new Text( imagePanel, SWT.BORDER | SWT.READ_ONLY );
    nameField.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) ); // , 3, 1 ) );

    l = new CLabel( imagePanel, SWT.CENTER );
    l.setText( STR_L_IMAGE_SIZE );
    labelSize = new CLabel( imagePanel, SWT.BORDER );
    labelSize.setText( "                           " ); //$NON-NLS-1$
    labelSize.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false ) ); // , 3, 1 ) );

    Group previewGroup = new Group( imagePanel, SWT.NONE );
    previewGroup.setText( STR_G_PREVIEW );
    previewGroup.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 4, 1 ) );
    BorderLayout bl = new BorderLayout();
    bl.setMargins( 8, 8, 8, 8 );
    previewGroup.setLayout( bl );

    previewPanel = new PreviewPanel( previewGroup, imageManager() );
    previewPanel.setLayoutData( BorderLayout.CENTER );

    imdPanel.genericChangeEventer().addListener( aSource -> {
      TsImageDescriptor imd = imdPanel.getDataRecord();
      previewPanel.setImageDescriptor( imd );
      labelSize.setText( previewPanel.imageSizeStr() );
      if( imd != null ) {
        nameField.setText( imd.toString() );
      }
      else {
        nameField.setText( TsLibUtils.EMPTY_STRING );
      }
    } );
  }

}
