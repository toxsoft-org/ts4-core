package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class PahShadowBlur {

  static final String TEXT      = "Hello";
  static final int    FONT_SIZE = 120;

  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "Path Shadow Blur — SWT" );
    shell.setLayout( new GridLayout( 1, false ) );
    shell.setSize( 750, 560 );

    // ── Панель управления ────────────────────────────────────
    Composite controls = new Composite( shell, SWT.NONE );
    controls.setLayout( new GridLayout( 6, false ) );
    controls.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    // Угол света
    new Label( controls, SWT.NONE ).setText( "Угол:" );
    Slider sAngle = new Slider( controls, SWT.HORIZONTAL );
    sAngle.setMinimum( 0 );
    sAngle.setMaximum( 369 );
    sAngle.setThumb( 9 );
    sAngle.setSelection( 45 );
    sAngle.setLayoutData( new GridData( 150, SWT.DEFAULT ) );
    Label lAngle = new Label( controls, SWT.NONE );
    lAngle.setText( " 45°" );
    lAngle.setLayoutData( new GridData( 40, SWT.DEFAULT ) );

    // Радиус размытия
    new Label( controls, SWT.NONE ).setText( "Blur:" );
    Slider sBlur = new Slider( controls, SWT.HORIZONTAL );
    sBlur.setMinimum( 1 );
    sBlur.setMaximum( 40 );
    sBlur.setThumb( 1 );
    sBlur.setSelection( 12 );
    sBlur.setLayoutData( new GridData( 150, SWT.DEFAULT ) );
    Label lBlur = new Label( controls, SWT.NONE );
    lBlur.setText( "12" );
    lBlur.setLayoutData( new GridData( 30, SWT.DEFAULT ) );

    // Смещение тени
    new Label( controls, SWT.NONE ).setText( "Смещение:" );
    Slider sDist = new Slider( controls, SWT.HORIZONTAL );
    sDist.setMinimum( 0 );
    sDist.setMaximum( 109 );
    sDist.setThumb( 9 );
    sDist.setSelection( 40 );
    sDist.setLayoutData( new GridData( 150, SWT.DEFAULT ) );
    Label lDist = new Label( controls, SWT.NONE );
    lDist.setText( "40" );
    lDist.setLayoutData( new GridData( 30, SWT.DEFAULT ) );

    // Прозрачность тени
    new Label( controls, SWT.NONE ).setText( "Непрозрачность:" );
    Slider sAlpha = new Slider( controls, SWT.HORIZONTAL );
    sAlpha.setMinimum( 10 );
    sAlpha.setMaximum( 265 );
    sAlpha.setThumb( 10 );
    sAlpha.setSelection( 180 );
    sAlpha.setLayoutData( new GridData( 150, SWT.DEFAULT ) );
    Label lAlpha = new Label( controls, SWT.NONE );
    lAlpha.setText( "180" );
    lAlpha.setLayoutData( new GridData( 30, SWT.DEFAULT ) );

    // ── Canvas ───────────────────────────────────────────────
    Canvas canvas = new Canvas( shell, SWT.DOUBLE_BUFFERED );
    canvas.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

    // Состояние параметров
    int[] params = { 45, 12, 40, 180 }; // angle, blur, dist, alpha

    // Слушатели слайдеров
    sAngle.addListener( SWT.Selection, e -> {
      params[0] = sAngle.getSelection();
      lAngle.setText( params[0] + "°" );
      canvas.redraw();
    } );
    sBlur.addListener( SWT.Selection, e -> {
      params[1] = sBlur.getSelection();
      lBlur.setText( String.valueOf( params[1] ) );
      canvas.redraw();
    } );
    sDist.addListener( SWT.Selection, e -> {
      params[2] = sDist.getSelection();
      lDist.setText( String.valueOf( params[2] ) );
      canvas.redraw();
    } );
    sAlpha.addListener( SWT.Selection, e -> {
      params[3] = sAlpha.getSelection();
      lAlpha.setText( String.valueOf( params[3] ) );
      canvas.redraw();
    } );

    canvas.addPaintListener( e -> {
      GC gc = e.gc;
      gc.setAntialias( SWT.ON );
      gc.setTextAntialias( SWT.ON );

      Rectangle area = canvas.getClientArea();
      int W = area.width, H = area.height;

      double angleRad = Math.toRadians( params[0] );
      int blurRadius = params[1];
      int shadowDist = params[2];
      int shadowAlpha = params[3];

      double shadowDx = -Math.cos( angleRad ) * shadowDist;
      double shadowDy = -Math.sin( angleRad ) * shadowDist;

      // ── Шрифт и Path ─────────────────────────────────────
      FontData fd = display.getSystemFont().getFontData()[0];
      fd.setHeight( FONT_SIZE );
      fd.setStyle( SWT.BOLD );
      Font font = new Font( display, fd );

      gc.setFont( font );
      Point textSize = gc.textExtent( TEXT );
      int tx = (W - textSize.x) / 2;
      int ty = (H - textSize.y) / 2;

      // ── 1. Рисуем силуэт тени во временный Image ─────────
      // Добавляем отступ = blurRadius чтобы blur не обрезался по краям
      W = textSize.x + blurRadius * 4;
      H = textSize.y;
      int pad = blurRadius * 2;
      int bW = W + pad * 2;
      int bH = H + pad * 2;

      Image silhouette = new Image( display, bW, bH );
      GC sgc = new GC( silhouette );
      sgc.setAntialias( SWT.ON );
      sgc.setTextAntialias( SWT.ON );

      // Фон буфера — чёрный (0,0,0), силуэт — белый (255,255,255)
      sgc.setBackground( display.getSystemColor( SWT.COLOR_BLACK ) );
      sgc.fillRectangle( 0, 0, bW, bH );

      Path shadowPath = new Path( display );
      shadowPath.addString( TEXT, tx + (int)shadowDx + pad, ty + (int)shadowDy + pad, font );
      // shadowPath.addArc( (int)shadowDx + 20 + pad, (int)shadowDy + 20 + pad, W - 20, W - 20, 0, 360 );
      // shadowPath.addArc( (int)shadowDx + 40 + pad, (int)shadowDy + 40 + pad, W - 60, W - 60, 0, 360 );

      sgc.setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );
      sgc.fillPath( shadowPath );
      sgc.dispose();
      shadowPath.dispose();

      // ── 2. Гауссово размытие силуэта ─────────────────────
      ImageData src = silhouette.getImageData();
      silhouette.dispose();
      ImageData blurred = gaussianBlur( src, blurRadius );

      // ── 3. Собираем финальное изображение ────────────────
      // Фон
      gc.setBackground( new Color( display, 240, 240, 245 ) );
      gc.fillRectangle( area );

      // Накладываем размытую тень попиксельно с заданной непрозрачностью
      // Используем ImageData напрямую — рисуем только тёмные пиксели
      PaletteData palette = new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF );
      ImageData shadow = new ImageData( W, H, 24, palette );

      // Фон тени = цвет фона (240, 240, 245)
      for( int py = 0; py < H; py++ ) {
        for( int px = 0; px < W; px++ ) {
          int bx = px + pad, by = py + pad;
          if( bx < 0 || bx >= bW || by < 0 || by >= bH ) {
            continue;
          }

          int pixel = blurred.getPixel( bx, by );
          RGB rgb = blurred.palette.getRGB( pixel );
          int grayVal = rgb.red; // силуэт был ч/б

          // Смешиваем цвет тени (0,0,0) с фоном (240,240,245)
          // по значению серого с учётом shadowAlpha
          float t = grayVal / 255f * (shadowAlpha / 255f);
          int r = (int)(240 * (1 - t));
          int g = (int)(240 * (1 - t));
          int b = (int)(245 * (1 - t));

          shadow.setPixel( px, py, palette.getPixel( new RGB( Math.min( 255, Math.max( 0, r ) ),
              Math.min( 255, Math.max( 0, g ) ), Math.min( 255, Math.max( 0, b ) ) ) ) );
        }
      }

      Image shadowImg = new Image( display, shadow );
      gc.drawImage( shadowImg, 0, 0 );
      shadowImg.dispose();

      // ── 4. Рисуем сам объект поверх тени ─────────────────
      Path textPath = new Path( display );
      textPath.addString( TEXT, tx, ty, font );

      gc.setBackground( new Color( display, 30, 100, 200 ) );
      gc.fillPath( textPath );
      gc.setForeground( new Color( display, 10, 60, 150 ) );
      gc.setLineWidth( 1 );
      gc.drawPath( textPath );
      // Path path = new Path( display );
      // path.addArc( 20, 20, W - 20, W - 20, 0, 360 );
      // path.addArc( 40, 40, W - 60, W - 60, 0, 360 );
      // gc.fillPath( path );
      // gc.drawPath( path );
      // path.dispose();

      textPath.dispose();
      font.dispose();
    } );

    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  // ── Гауссово размытие ─────────────────────────────────────────
  // Два прохода: сначала по горизонтали, потом по вертикали (separable filter)

  static ImageData gaussianBlur( ImageData src, int radius ) {
    int W = src.width, H = src.height;
    float[] kernel = buildGaussianKernel( radius );
    int kSize = kernel.length;
    int half = kSize / 2;

    // Промежуточный буфер (float для точности)
    float[] tmp = new float[W * H];
    float[] out = new float[W * H];

    // Читаем исходные пиксели в float[]
    float[] srcF = new float[W * H];
    for( int y = 0; y < H; y++ ) {
      for( int x = 0; x < W; x++ ) {
        int pixel = src.getPixel( x, y );
        RGB rgb = src.palette.getRGB( pixel );
        srcF[y * W + x] = rgb.red; // ч/б — берём любой канал
      }
    }

    // Горизонтальный проход
    for( int y = 0; y < H; y++ ) {
      for( int x = 0; x < W; x++ ) {
        float sum = 0;
        for( int k = 0; k < kSize; k++ ) {
          int sx = Math.min( W - 1, Math.max( 0, x + k - half ) );
          sum += srcF[y * W + sx] * kernel[k];
        }
        tmp[y * W + x] = sum;
      }
    }

    // Вертикальный проход
    for( int y = 0; y < H; y++ ) {
      for( int x = 0; x < W; x++ ) {
        float sum = 0;
        for( int k = 0; k < kSize; k++ ) {
          int sy = Math.min( H - 1, Math.max( 0, y + k - half ) );
          sum += tmp[sy * W + x] * kernel[k];
        }
        out[y * W + x] = sum;
      }
    }

    // Записываем результат в новый ImageData
    PaletteData palette = new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF );
    ImageData result = new ImageData( W, H, 24, palette );

    for( int y = 0; y < H; y++ ) {
      for( int x = 0; x < W; x++ ) {
        int v = Math.min( 255, Math.max( 0, (int)out[y * W + x] ) );
        result.setPixel( x, y, palette.getPixel( new RGB( v, v, v ) ) );
      }
    }
    return result;
  }

  // ── Ядро Гаусса ───────────────────────────────────────────────

  static float[] buildGaussianKernel( int radius ) {
    int size = radius * 2 + 1;
    float[] kernel = new float[size];
    float sigma = radius / 3.0f;
    float sum = 0;

    for( int i = 0; i < size; i++ ) {
      int x = i - radius;
      float v = (float)Math.exp( -(x * x) / (2 * sigma * sigma) );
      kernel[i] = v;
      sum += v;
    }
    // Нормализация
    for( int i = 0; i < size; i++ ) {
      kernel[i] /= sum;
    }

    return kernel;
  }
}
