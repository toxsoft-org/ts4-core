package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.render.IRendererConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Отрисовщик стандартной прямоугольной кнопки с текстом и изображением.
 * <p>
 *
 * @author vs
 */
public class StdButtonRenderer
    extends AbstractViselRenderer {

  /**
   * The renderer factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".stdButtonRendererFactory"; //$NON-NLS-1$

  static final String PROPID_SELECTED_TEXT = "selectedText"; //$NON-NLS-1$
  static final String PROPID_TEXT_GAP      = "textGap";      //$NON-NLS-1$

  static final String PROPID_DISABLED_TEXT_COLOR = "disabledTextColor"; //$NON-NLS-1$
  static final String PROPID_DISABLED_FILL_INFO  = "disabledFillInfo";  //$NON-NLS-1$
  static final String PROPID_SELECTED_TEXT_COLOR = "selectedTextColor"; //$NON-NLS-1$
  static final String PROPID_PRESSED_FILL_INFO   = "pressedFillInfo";   //$NON-NLS-1$

  static final String PROPID_BUTTON_IMAGE   = "buttonImage";   //$NON-NLS-1$
  static final String PROPID_HOVERED_IMAGE  = "hoveredImage";  //$NON-NLS-1$
  static final String PROPID_SELECTED_IMAGE = "selectedImage"; //$NON-NLS-1$
  static final String PROPID_DISABLED_IMAGE = "disabledImage"; //$NON-NLS-1$

  static final ITinFieldInfo TFI_SELECTED_TEXT = TtiUtils.strFieldInfo( PROPID_SELECTED_TEXT, //
      "Выделенный текст", "Текст на выделенной кнопке" );

  static final ITinFieldInfo TFI_TEXT_GAP = TtiUtils.intFieldInfo( PROPID_TEXT_GAP, //
      "Зазор", "Зазор между текстом и изображением", 8 );

  static final ITinFieldInfo TFI_HOVERED_BK_COLOR = TtiUtils.typedFieldInfo( PROPID_HOVERED_BK_COLOR, //
      TtiRGBA.INSTANCE, "Фон под курсором", "Фон кнопки, когда курсор находится над ней" );

  static final ITinFieldInfo TFI_SELECTED_BK_COLOR = TtiUtils.typedFieldInfo( PROPID_SELECTED_BK_COLOR, //
      TtiRGBA.INSTANCE, "Фон выделения", "Фон выделенной кнопки" );

  static final ITinFieldInfo TFI_SELECTED_TEXT_COLOR = TtiUtils.typedFieldInfo( PROPID_SELECTED_FG_COLOR, //
      TtiRGBA.INSTANCE, "Цвет текста выделенной кнопки", "Цвет текста выделенной кнопки" );

  static final ITinFieldInfo TFI_DISABLED_TEXT_COLOR = TtiUtils.typedFieldInfo( PROPID_DISABLED_FG_COLOR, //
      TtiRGBA.INSTANCE, "Цвет текста недоступной кнопки", "Цвет текста недоступной кнопки" );

  static final ITinFieldInfo TFI_DISABLED_FILL_INFO = TtiUtils.typedFieldInfo( PROPID_DISABLED_FILL_INFO, //
      TtiTsFillInfo.INSTANCE, "Фон недоступной кнопки", "Заливка фона недоступной кнопки" );

  static final ITinFieldInfo TFI_PRESSED_FILL_INFO = TtiUtils.typedFieldInfo( PROPID_DISABLED_FILL_INFO, //
      TtiTsFillInfo.INSTANCE, "Фон нажатой кнопки", "Заливка фона нажатой кнопки" );

  static final ITinFieldInfo TFI_BUTTON_IMAGE = TtiUtils.fieldInfo( PROPID_BUTTON_IMAGE, //
      TFI_IMAGE_DESCRIPTOR, "Изображение", "Изображение на кнопке" );

  static final ITinFieldInfo TFI_HOVERED_IMAGE = TtiUtils.fieldInfo( PROPID_HOVERED_IMAGE, //
      TFI_IMAGE_DESCRIPTOR, "Подсвеченное изображение", "Изображение на кнопке, когда курсор находится над ней" );

  static final ITinFieldInfo TFI_DISABLED_IMAGE = TtiUtils.fieldInfo( PROPID_DISABLED_IMAGE, //
      TFI_IMAGE_DESCRIPTOR, "Недоступное изображение", "Изображение на недоступной кнопке" );

  static final ITinFieldInfo TFI_SELECTED_IMAGE = TtiUtils.fieldInfo( PROPID_SELECTED_IMAGE, //
      TFI_IMAGE_DESCRIPTOR, "Выделенное изображение", "Изображение на выделенной кнопке" );

  static final ITinFieldInfo TFI_TEXT_FULCRUM = TtiUtils.fieldInfo( TFI_FULCRUM, //
      "Расположение текста", "Место расположения текста на кнопке" );

  static final IDataType DT_STD_BUTTON_RENDERER = //
      DataType.create( VedAbstractRendererFactory.DT_TS_RENDERER_CFG, //
          TSID_NAME, "Отображение", //
          TSID_DEFAULT_VALUE, avValobj( defaultCfg( "sbrCfg", "none" ) ) );

  /**
   * Возвращает тип данных для инспектора свойств.
   *
   * @return {@link ITinTypeInfo} - тип данных для инспектора свойств.
   */
  public static final ITinTypeInfo tinTypeInfo() {
    IDataType dt = DataType.create( DT_STD_BUTTON_RENDERER );
    return new TinAtomicTypeInfo.TtiValobj<>( dt, ViselRendererCfg.class );
  }

  /**
   * The VISEL factory singleton.
   */
  public static final VedAbstractRendererFactory FACTORY = new AbstractButtonRendererFactory( FACTORY_ID, //
      TSID_NAME, "Standard button renderer", //
      TSID_DESCRIPTION, "Text and image", //
      TSID_ICON_ID, ICONID_VISEL_RECTANGLE ) {

    @Override
    public String kindId() {
      return KIND_ID;
    }

    @Override
    protected void addSpecificTinTypeInfoes( IStridablesListEdit<ITinFieldInfo> aFields ) {
      aFields.add( TFI_TEXT );
      aFields.add( TFI_FONT );
      aFields.add( TFI_TEXT_FULCRUM );
      aFields.add( TFI_BUTTON_IMAGE );
      aFields.add( TFI_TEXT_GAP );

      aFields.add( TFI_FG_COLOR );
      aFields.add( TFI_BK_COLOR );

      aFields.add( TFI_SELECTED_TEXT );
      aFields.add( TFI_SELECTED_TEXT_COLOR );
      aFields.add( TFI_SELECTED_BK_COLOR );
      aFields.add( TFI_SELECTED_IMAGE );

      aFields.add( TFI_HOVERED_BK_COLOR );
      aFields.add( TFI_HOVERED_IMAGE );

      aFields.add( TFI_DISABLED_TEXT_COLOR );
      aFields.add( TFI_DISABLED_FILL_INFO );
      aFields.add( TFI_DISABLED_IMAGE );

      aFields.add( TFI_PRESSED_FILL_INFO );
      aFields.add( TFI_SHADOW_INFO );
    }

    @Override
    public ViselRendererCfg createConfig( String aId, String aViselId ) {
      return defaultCfg( aId, aViselId );
    }

    @Override
    protected AbstractViselRenderer doCreate( ViselRendererCfg aCfg, IVedVisel aVisel, VedScreen aVedScreen ) {
      return new StdButtonRenderer( aCfg.id(), propDefs(), aCfg, aVisel, aVedScreen.tsContext() );
    }

  };

  static final ViselRendererCfg defaultCfg( String aId, String aViselId ) {
    IOptionSetEdit opSet = new OptionSet();

    // IListEdit<Pair<Double, RGBA>> fractions = new ElemArrayList<>();
    // fractions.add( new Pair<>( Double.valueOf( 0.0 ), new RGBA( 229, 229, 229, 255 ) ) );
    // fractions.add( new Pair<>( Double.valueOf( 100.0 ), new RGBA( 242, 242, 242, 255 ) ) );
    // LinearGradientInfo lgi = new LinearGradientInfo( fractions, 90 );
    // TsFillInfo fi = new TsFillInfo( new TsGradientFillInfo( lgi ) );
    // opSet.setValobj( PROPID_BK_FILL, fi );
    //
    // fractions = new ElemArrayList<>();
    // fractions.add( new Pair<>( Double.valueOf( 0.0 ), new RGBA( 204, 204, 204, 255 ) ) );
    // fractions.add( new Pair<>( Double.valueOf( 100.0 ), new RGBA( 229, 229, 229, 255 ) ) );
    // lgi = new LinearGradientInfo( fractions, 90 );
    // fi = new TsFillInfo( new TsGradientFillInfo( lgi ) );
    // opSet.setValobj( PROPID_FRAME_FILL, fi );

    RGBA hovRgba = new RGBA( 229, 241, 251, 255 );
    opSet.setValobj( PROPID_HOVERED_BK_COLOR, hovRgba );

    opSet.setValobj( PROPID_BK_COLOR, new RGBA( 30, 230, 230, 255 ) );

    return new ViselRendererCfg( aId, AbstractButtonRendererFactory.KIND_ID, FACTORY_ID, opSet, aViselId );
  }

  // TsFillInfo frameFillInfo = TsFillInfo.NONE;
  // TsFillInfo innerFillInfo = TsFillInfo.NONE;
  // TsFillInfo textFillInfo = TsFillInfo.NONE;

  String text = TsLibUtils.EMPTY_STRING;
  Font   font = null;

  double borderThick = 8;

  TsFillInfo fillInfo         = null;
  TsFillInfo pressedFillInfo  = null;
  TsFillInfo hoveredFillInfo  = null;
  TsFillInfo selectedFillInfo = null;
  TsFillInfo disableFillInfo  = new TsFillInfo( new RGBA( 164, 164, 164, 255 ) );

  protected RGBA bkRgba  = new RGBA( 255, 255, 255, 255 );
  protected RGBA fgRgba  = new RGBA( 0, 0, 0, 255 );
  protected RGBA hvRgba  = new RGBA( 229, 241, 251, 255 );
  protected RGBA selRgba = new RGBA( 0, 0, 0, 255 );
  protected RGBA disRgba = new RGBA( 96, 96, 96, 255 );

  protected TsLineInfo lineInfo = TsLineInfo.ofWidth( 1 );

  protected boolean hovered = false;

  TsImage image   = null;
  TsImage imgNorm = null;
  TsImage imgDis  = null;
  TsImage imgSel  = null;
  TsImage imgHov  = null;

  /**
   * Constructor.
   *
   * @param aId String - idnetifier
   * @param aPropDefs IStridablesList&lt;IDataDef> - list of data definitions
   * @param aCfg {@link ViselRendererCfg} - visel configeration
   * @param aVisel {@link IVedVisel} - the corresponding visel
   * @param aTsContext {@link ITsGuiContext} - corresponding context
   */
  public StdButtonRenderer( String aId, IStridablesList<IDataDef> aPropDefs, ViselRendererCfg aCfg, IVedVisel aVisel,
      ITsGuiContext aTsContext ) {
    super( aId, aPropDefs, aVisel, aTsContext );
    ITinValue tv = TtiRGBA.INSTANCE.makeValue( new RGBA( 0, 0, 0, 255 ) );
    hoveredFillInfo = new TsFillInfo( hvRgba );
  }

  @Override
  public String kindId() {
    return AbstractButtonRendererFactory.KIND_ID;
  }

  @Override
  public IStridablesList<ITinFieldInfo> tinFieldInfoes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected ITinTypeInfo doCreateTypeInfo() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    super.doUpdateCachesAfterPropsChange( aChangedValues );
    // if( aChangedValues.hasKey( PROPID_BK_FILL ) ) {
    // innerFillInfo = aChangedValues.getValobj( PROPID_BK_FILL );
    // }
    if( aChangedValues.hasKey( PROPID_TEXT ) ) {
      text = aChangedValues.getStr( PROPID_TEXT );
    }
    if( aChangedValues.hasKey( PROPID_FONT ) ) {
      FontInfo fi = aChangedValues.getValobj( PROPID_FONT );
      font = fontManager().getFont( fi );
    }
    if( aChangedValues.hasKey( PROPID_BK_COLOR ) ) {
      bkRgba = aChangedValues.getValobj( PROPID_BK_COLOR );
      fillInfo = normalFillInfo( bkRgba );
      pressedFillInfo = pressedFillInfo( bkRgba );
    }
    if( aChangedValues.hasKey( PROPID_FG_COLOR ) ) {
      fgRgba = aChangedValues.getValobj( PROPID_FG_COLOR );
    }
    if( aChangedValues.hasKey( PROPID_HOVERED_BK_COLOR ) ) {
      hvRgba = aChangedValues.getValobj( PROPID_HOVERED_BK_COLOR );
      hoveredFillInfo = normalFillInfo( hvRgba );
    }
    if( aChangedValues.hasKey( PROPID_SELECTED_BK_COLOR ) ) {
      selRgba = aChangedValues.getValobj( PROPID_SELECTED_BK_COLOR );
    }

    if( aChangedValues.hasKey( PROPID_BUTTON_IMAGE ) ) {
      TsImageDescriptor imd = aChangedValues.getValobj( PROPID_BUTTON_IMAGE );
      if( imd != TsImageDescriptor.NONE ) {
        imgNorm = imageManager().getImage( imd );
      }
      else {
        imgNorm = null;
      }
    }

    if( aChangedValues.hasKey( PROPID_SELECTED_IMAGE ) ) {
      TsImageDescriptor imd = aChangedValues.getValobj( PROPID_SELECTED_IMAGE );
      if( imd != TsImageDescriptor.NONE ) {
        imgSel = imageManager().getImage( imd );
      }
      else {
        imgSel = null;
      }
    }

    if( aChangedValues.hasKey( PROPID_HOVERED_IMAGE ) ) {
      TsImageDescriptor imd = aChangedValues.getValobj( PROPID_HOVERED_IMAGE );
      if( imd != TsImageDescriptor.NONE ) {
        imgHov = imageManager().getImage( imd );
      }
      else {
        imgHov = null;
      }
    }

    if( aChangedValues.hasKey( PROPID_DISABLED_IMAGE ) ) {
      TsImageDescriptor imd = aChangedValues.getValobj( PROPID_DISABLED_IMAGE );
      if( imd != TsImageDescriptor.NONE ) {
        imgDis = imageManager().getImage( imd );
      }
      else {
        imgDis = null;
      }
    }

    if( aChangedValues.hasKey( PROPID_BUTTON_HOVERED ) ) {
      hovered = aChangedValues.getBool( PROPID_BUTTON_HOVERED );
    }

  }

  @Override
  protected void doPaint( ITsGraphicsContext aPaintContext ) {
    // double w = props().getDouble( PROPID_WIDTH );
    // double h = props().getDouble( PROPID_HEIGHT );
    // double doubleR = Math.min( w, h );
    //
    // aPaintContext.setFillInfo( frameFillInfo );
    // aPaintContext.fillOval( 0, 0, (int)doubleR, (int)doubleR );
    //
    // aPaintContext.setFillInfo( innerFillInfo );
    // aPaintContext.fillOval( (int)borderThick, (int)borderThick, (int)(doubleR - 2 * borderThick),
    // (int)(doubleR - 2 * borderThick) );
    //
    // aPaintContext.setForegroundRgba( new RGBA( 128, 128, 128, 255 ) );
    // aPaintContext.drawOval( 0, 0, (int)doubleR, (int)doubleR );
    //
    // drawText( aPaintContext, doubleR );

    EButtonViselState state = props().getValobj( PROPID_BUTTON_STATE );

    int arcW = 8;
    int arcH = 8;
    switch( state ) {
      case NORMAL -> {
        if( hovered ) {
          aPaintContext.setFillInfo( hoveredFillInfo );
        }
        else {
          aPaintContext.setFillInfo( fillInfo );
        }
      }
      case DISABLED, SELECTED, WORKING -> aPaintContext.setFillInfo( fillInfo );
      case PRESSED -> aPaintContext.setFillInfo( pressedFillInfo );
      default -> throw new TsNotAllEnumsUsedRtException();
    }

    aPaintContext.fillRoundRect( 0, 0, (int)viselWidth(), (int)viselHeight(), arcW, arcH );
    aPaintContext.drawRoundRect( 0, 0, (int)viselWidth(), (int)viselHeight(), arcW, arcH );

    if( hovered && imgHov != null ) {
      aPaintContext.gc().drawImage( imgHov.image(), imageX( imgHov ), imageY( imgHov ) );
      return;
    }
    if( imgNorm != null ) {
      aPaintContext.gc().drawImage( imgNorm.image(), imageX( imgNorm ), imageY( imgNorm ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private int imageX( TsImage aImage ) {
    if( text.isBlank() ) { // если текста нет, то всегда по центру
      ITsPoint p = aImage.imageSize();
      return (int)((viselWidth() - p.x()) / 2.);
    }
    return 0;
  }

  private int imageY( TsImage aImage ) {
    if( text.isBlank() ) { // если текста нет, то всегда по центру
      ITsPoint p = aImage.imageSize();
      return (int)((viselHeight() - p.x()) / 2.);
    }
    return 0;
  }

  RGBA disbaleTextRgba = new RGBA( 196, 196, 196, 255 );

  void drawText( ITsGraphicsContext aPaintContext, double aDoubleR ) {
    if( text != null && !text.isBlank() ) {
      Path path = new Path( aPaintContext.gc().getDevice() );
      path.addString( text, 0, 0, font );
      float[] bounds = new float[4];
      path.getBounds( bounds );

      double x = (aDoubleR - bounds[2]) / 2. - bounds[0];
      double y = (aDoubleR - bounds[3]) / 2. - bounds[1];
      aPaintContext.gc().setForeground( colorManager().getColor( ETsColor.BLACK ) );
      aPaintContext.gc().setBackground( colorManager().getColor( ETsColor.BLACK ) );

      // aPaintContext.setFillInfo( textFillInfo );
      aPaintContext.setForegroundRgba( new RGBA( 0, 0, 0, 255 ) );
      EButtonViselState buttonState = visel().props().getValobj( ViselButton.PROPID_STATE );
      if( buttonState == EButtonViselState.DISABLED ) {
        aPaintContext.setFillInfo( new TsFillInfo( disbaleTextRgba ) );
        aPaintContext.setForegroundRgba( disbaleTextRgba );
      }

      aPaintContext.fillPath( path, (int)(x), (int)(y), (int)bounds[2] + 1, (int)bounds[3] + 1 );
      // aPaintContext.drawPath( path, (int)(x), (int)(y) );
      path.dispose();
    }
  }

  private static TsFillInfo normalFillInfo( RGBA aRgba ) {
    RGBA sc = new RGBA( 220, 220, 220, 255 );
    RGBA ec = new RGBA( 190, 190, 190, 255 );

    RGB rgb = GradientUtils.tuneBrightness( aRgba.rgb, 0.2 );
    sc = new RGBA( rgb.red, rgb.green, rgb.blue, 255 );
    rgb = GradientUtils.tuneBrightness( aRgba.rgb, -0.2 );
    ec = new RGBA( rgb.red, rgb.green, rgb.blue, 255 );

    Pair<Double, RGBA> p1 = new Pair<>( Double.valueOf( 0 ), sc );
    Pair<Double, RGBA> p2 = new Pair<>( Double.valueOf( 100 ), ec );
    IListEdit<Pair<Double, RGBA>> fractions = new ElemArrayList<>();
    fractions.add( p1 );
    fractions.add( p2 );
    LinearGradientInfo lgi = new LinearGradientInfo( fractions, 90 );
    return new TsFillInfo( new TsGradientFillInfo( lgi ) );
  }

  private static TsFillInfo pressedFillInfo( RGBA aRgba ) {
    RGBA sc = new RGBA( 220, 220, 220, 255 );
    RGBA ec = new RGBA( 190, 190, 190, 255 );

    RGB rgb = GradientUtils.tuneBrightness( aRgba.rgb, 0.2 );
    sc = new RGBA( rgb.red, rgb.green, rgb.blue, 255 );
    rgb = GradientUtils.tuneBrightness( aRgba.rgb, -0.2 );
    ec = new RGBA( rgb.red, rgb.green, rgb.blue, 255 );

    Pair<Double, RGBA> p1 = new Pair<>( Double.valueOf( 0 ), ec );
    Pair<Double, RGBA> p2 = new Pair<>( Double.valueOf( 100 ), sc );
    IListEdit<Pair<Double, RGBA>> fractions = new ElemArrayList<>();
    fractions = new ElemArrayList<>();
    fractions.add( p1 );
    fractions.add( p2 );
    LinearGradientInfo lgi = new LinearGradientInfo( fractions, 90 );
    return new TsFillInfo( new TsGradientFillInfo( lgi ) );
  }

}
