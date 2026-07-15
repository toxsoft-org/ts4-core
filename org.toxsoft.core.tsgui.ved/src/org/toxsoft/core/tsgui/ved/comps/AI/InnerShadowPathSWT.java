package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class InnerShadowPathSWT {

  // --- Параметры тени (управляются через UI) ---
  private static int blurRadius    = 15;
  private static int shadowOffsetX = 6;
  private static int shadowOffsetY = 6;
  private static int shadowAlpha   = 200;
  private static int shadowR       = 0;
  private static int shadowG       = 0;
  private static int shadowB       = 0;

  // --- Параметры контура ---
  private static int strokeWidth = 2;
  private static int strokeR     = 70;
  private static int strokeG     = 130;
  private static int strokeB     = 180;

  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "Inner Shadow — SWT" );
    shell.setLayout( new GridLayout( 1, false ) );

    // ── Холст ──────────────────────────────────────────────
    Canvas canvas = new Canvas( shell, SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND );
    GridData canvasGD = new GridData( SWT.FILL, SWT.FILL, true, true );
    canvasGD.widthHint = 520;
    canvasGD.heightHint = 360;
    canvas.setLayoutData( canvasGD );

    canvas.addPaintListener( e -> paintScene( e.gc, canvas ) );

    // ── Панель управления ──────────────────────────────────
    Group controls = new Group( shell, SWT.NONE );
    controls.setText( "Параметры тени" );
    controls.setLayout( new GridLayout( 6, false ) );
    controls.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    // Вспомогательный метод для создания слайдера
    addSlider( controls, canvas, "Размытие", 1, 40, blurRadius, v -> blurRadius = v );
    addSlider( controls, canvas, "Смещение X", -40, 40, shadowOffsetX, v -> shadowOffsetX = v );
    addSlider( controls, canvas, "Смещение Y", -40, 40, shadowOffsetY, v -> shadowOffsetY = v );
    addSlider( controls, canvas, "Прозрачность", 0, 255, shadowAlpha, v -> shadowAlpha = v );
    addSlider( controls, canvas, "Ширина контура", 1, 10, strokeWidth, v -> strokeWidth = v );

    // Кнопка выбора цвета тени
    Button colorBtn = new Button( controls, SWT.PUSH );
    colorBtn.setText( "Цвет тени…" );
    colorBtn.addListener( SWT.Selection, e -> {
      ColorDialog dlg = new ColorDialog( shell );
      dlg.setRGB( new RGB( shadowR, shadowG, shadowB ) );
      RGB chosen = dlg.open();
      if( chosen != null ) {
        shadowR = chosen.red;
        shadowG = chosen.green;
        shadowB = chosen.blue;
        canvas.redraw();
      }
    } );

    shell.pack();
    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  // ────────────────────────────────────────────────────────────
  // Отрисовка сцены
  // ────────────────────────────────────────────────────────────
  private static void paintScene( GC gc, Canvas canvas ) {
    Display display = canvas.getDisplay();
    Rectangle bounds = canvas.getClientArea();

    // Фон
    gc.setBackground( display.getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ) );
    gc.fillRectangle( bounds );

    // Создаём Path
    Path path = createStarPath( display, bounds.width / 2f, bounds.height / 2f, 120, 50 );

    // ── 1. Маска фигуры (белая фигура на чёрном фоне) ──────
    Image maskImg = new Image( display, bounds.width, bounds.height );
    GC maskGC = new GC( maskImg );
    maskGC.setBackground( display.getSystemColor( SWT.COLOR_BLACK ) );
    maskGC.fillRectangle( bounds );
    maskGC.setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );
    maskGC.setAntialias( SWT.ON );
    maskGC.fillPath( path );
    maskGC.dispose();

    // ── 2. Размытая версия маски ────────────────────────────
    ImageData maskData = maskImg.getImageData();
    maskImg.dispose();
    int[] maskAlpha = extractWhiteChannel( maskData );
    int[] blurredMask = gaussianBlur( maskAlpha, bounds.width, bounds.height, blurRadius );

    // ── 3. Вычисляем inner shadow:
    // innerShadow[i] = mask[i] - blurred(shifted mask)[i]
    // Сдвигаем размытую маску на (-offsetX, -offsetY), чтобы
    // тень «вдавливалась» внутрь со стороны источника света.
    int w = bounds.width, h = bounds.height;
    int[] innerAlpha = new int[w * h];
    for( int y = 0; y < h; y++ ) {
      for( int x = 0; x < w; x++ ) {
        int sx = x - shadowOffsetX;
        int sy = y - shadowOffsetY;
        int blurredVal = 0;
        if( sx >= 0 && sx < w && sy >= 0 && sy < h ) {
          blurredVal = blurredMask[sy * w + sx];
        }
        // Разница: внутри фигуры (mask=255), минус "засветка" от смещения
        int diff = maskAlpha[y * w + x] - blurredVal;
        innerAlpha[y * w + x] = Math.max( 0, diff );
      }
    }

    // ── 4. Строим изображение inner shadow ─────────────────
    Image shadowImg = buildColoredImage( display, w, h, innerAlpha, shadowR, shadowG, shadowB, shadowAlpha );

    // ── 5. Рисуем inner shadow поверх фона ─────────────────
    gc.drawImage( shadowImg, 0, 0 );
    shadowImg.dispose();

    // ── 6. Рисуем только контур фигуры ─────────────────────
    gc.setAntialias( SWT.ON );
    Color strokeColor = new Color( display, strokeR, strokeG, strokeB );
    gc.setForeground( strokeColor );
    gc.setLineWidth( strokeWidth );
    gc.drawPath( path );
    strokeColor.dispose();
    path.dispose();
  }

  // ────────────────────────────────────────────────────────────
  // Вспомогательные методы
  // ────────────────────────────────────────────────────────────

  /** Звезда с N=5 лучами. */
  private static Path createStarPath( Display d, float cx, float cy, float outer, float inner ) {
    Path p = new Path( d );
    // int pts = 5;
    // for( int i = 0; i < pts * 2; i++ ) {
    // double angle = Math.PI / pts * i - Math.PI / 2;
    // float r = (i % 2 == 0) ? outer : inner;
    // float x = cx + (float)(r * Math.cos( angle ));
    // float y = cy + (float)(r * Math.sin( angle ));
    // if( i == 0 ) {
    // p.moveTo( x, y );
    // }
    // else {
    // p.lineTo( x, y );
    // }
    // }
    // p.close();
    p.addArc( cx - 100, cy - 100, 300, 300, 0, 360 );
    return p;
  }

  /** Извлекает яркость белого канала [0..255]. */
  private static int[] extractWhiteChannel( ImageData data ) {
    int w = data.width, h = data.height;
    int[] out = new int[w * h];
    for( int y = 0; y < h; y++ ) {
      for( int x = 0; x < w; x++ ) {
        RGB rgb = data.palette.getRGB( data.getPixel( x, y ) );
        out[y * w + x] = (rgb.red + rgb.green + rgb.blue) / 3;
      }
    }
    return out;
  }

  /** Двухпроходный Gaussian Blur одноканального массива. */
  private static int[] gaussianBlur( int[] src, int w, int h, int radius ) {
    if( radius < 1 ) {
      return src.clone();
    }
    float[] kernel = makeKernel( radius );
    int kLen = kernel.length;
    int[] tmp = new int[w * h];
    int[] dst = new int[w * h];

    for( int y = 0; y < h; y++ ) {
      for( int x = 0; x < w; x++ ) {
        float s = 0;
        for( int k = 0; k < kLen; k++ ) {
          int sx = Math.max( 0, Math.min( w - 1, x + k - radius ) );
          s += src[y * w + sx] * kernel[k];
        }
        tmp[y * w + x] = Math.round( s );
      }
    }

    for( int y = 0; y < h; y++ ) {
      for( int x = 0; x < w; x++ ) {
        float s = 0;
        for( int k = 0; k < kLen; k++ ) {
          int sy = Math.max( 0, Math.min( h - 1, y + k - radius ) );
          s += tmp[sy * w + x] * kernel[k];
        }
        dst[y * w + x] = Math.round( s );
      }
    }
    return dst;
  }

  private static float[] makeKernel( int radius ) {
    int size = radius * 2 + 1;
    float[] k = new float[size];
    float sigma = radius / 3.0f, sum = 0;
    for( int i = 0; i < size; i++ ) {
      int x = i - radius;
      k[i] = (float)Math.exp( -(x * x) / (2 * sigma * sigma) );
      sum += k[i];
    }
    for( int i = 0; i < size; i++ ) {
      k[i] /= sum;
    }
    return k;
  }

  /** Строит RGBA-изображение по альфа-массиву и заданному цвету. */
  private static Image buildColoredImage( Display display, int w, int h, int[] alphaArr, int r, int g, int b,
      int maxAlpha ) {
    ImageData data = new ImageData( w, h, 24, new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF ) );
    byte[] alphaData = new byte[w * h];
    int pixel = data.palette.getPixel( new RGB( r, g, b ) );
    for( int i = 0; i < w * h; i++ ) {
      data.setPixel( i % w, i / w, pixel );
      alphaData[i] = (byte)Math.min( 255, (int)(alphaArr[i] * (maxAlpha / 255.0f)) );
    }
    data.alphaData = alphaData;
    return new Image( display, data );
  }

  // ────────────────────────────────────────────────────────────
  // UI: слайдер с подписью и текущим значением
  // ────────────────────────────────────────────────────────────
  @FunctionalInterface
  interface IntConsumer {

    void accept( int v );
  }

  private static void addSlider( Composite parent, Canvas canvas, String label, int min, int max, int initial,
      IntConsumer onChanged ) {
    Composite col = new Composite( parent, SWT.NONE );
    col.setLayout( new GridLayout( 1, false ) );

    Label lbl = new Label( col, SWT.CENTER );
    lbl.setLayoutData( new GridData( SWT.CENTER, SWT.CENTER, true, false ) );
    lbl.setText( label + ": " + initial );

    Slider slider = new Slider( col, SWT.HORIZONTAL );
    GridData sd = new GridData( SWT.FILL, SWT.CENTER, true, false );
    sd.widthHint = 120;
    slider.setLayoutData( sd );
    slider.setMinimum( min );
    slider.setMaximum( max + slider.getThumb() );
    slider.setSelection( initial );

    slider.addListener( SWT.Selection, e -> {
      int val = slider.getSelection();
      lbl.setText( label + ": " + val );
      onChanged.accept( val );
      canvas.redraw();
    } );
  }
}
