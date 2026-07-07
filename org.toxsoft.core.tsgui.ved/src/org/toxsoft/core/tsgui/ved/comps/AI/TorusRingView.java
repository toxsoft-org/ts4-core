package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class TorusRingView {

  // ── Параметры тора ──────────────────────────────────────────
  private static final double R       = 140; // большой радиус
  private static final double r       = 50;  // малый радиус (трубка)
  private static final int    STEPS   = 360; // сегментов по v (вдоль кольца)
  private static final int    STEPS_U = 40;  // сегментов по u (поперёк трубки)

  // Часть тора: 0..360 = полное кольцо
  private static final double V_START_DEG = 0;
  private static final double V_END_DEG   = 270;

  // ── Источник света (направление, нормированное) ─────────────
  // Свет сверху-слева-спереди
  private static final double LX = -0.4;
  private static final double LY = -0.6;
  private static final double LZ = 0.7;

  // ── Цвет тора (base color) ──────────────────────────────────
  private static final int BASE_R = 40;
  private static final int BASE_G = 140;
  private static final int BASE_B = 220;

  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "Torus Ring View — SWT Canvas" );
    shell.setLayout( new FillLayout() );
    shell.setSize( 600, 600 );

    Canvas canvas = new Canvas( shell, SWT.DOUBLE_BUFFERED );

    canvas.addPaintListener( e -> render( e.gc, canvas.getClientArea() ) );

    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  // ── Основной рендер ──────────────────────────────────────────

  private static void render( GC gc, Rectangle area ) {
    int cx = area.width / 2;
    int cy = area.height / 2;

    // Фон
    gc.setBackground( gc.getDevice().getSystemColor( SWT.COLOR_BLACK ) );
    gc.fillRectangle( area );

    gc.setAntialias( SWT.ON );

    double vStart = Math.toRadians( V_START_DEG );
    double vEnd = Math.toRadians( V_END_DEG );
    double stepV = (vEnd - vStart) / STEPS;
    double stepU = 2 * Math.PI / STEPS_U;

    // ── Строим и сортируем полигоны по Z (painter's algorithm) ──
    // Каждый полигон — четырёхугольник между (u,v) и (u+1,v+1)
    int quadCount = STEPS * STEPS_U;
    double[] quadZ = new double[quadCount];
    int[] quadIdx = new int[quadCount];

    // Предварительно вычисляем все вершины
    // vertex[j][i] = {x, y, z, nx, ny, nz}
    double[][][] verts = new double[STEPS + 1][STEPS_U + 1][6];

    for( int j = 0; j <= STEPS; j++ ) {
      double v = vStart + j * stepV;
      for( int i = 0; i <= STEPS_U; i++ ) {
        double u = i * stepU;

        double cosU = Math.cos( u ), sinU = Math.sin( u );
        double cosV = Math.cos( v ), sinV = Math.sin( v );

        // Позиция
        double px = (R + r * cosU) * cosV;
        double py = (R + r * cosU) * sinV;
        double pz = r * sinU;

        // Нормаль
        double nx = cosU * cosV;
        double ny = cosU * sinV;
        double nz = sinU;

        verts[j][i][0] = px;
        verts[j][i][1] = py;
        verts[j][i][2] = pz;
        verts[j][i][3] = nx;
        verts[j][i][4] = ny;
        verts[j][i][5] = nz;
      }
    }

    // Вычисляем средний Z каждого квада для сортировки
    for( int j = 0; j < STEPS; j++ ) {
      for( int i = 0; i < STEPS_U; i++ ) {
        int idx = j * STEPS_U + i;
        quadZ[idx] = (verts[j][i][2] + verts[j + 1][i][2] + verts[j][i + 1][2] + verts[j + 1][i + 1][2]) / 4.0;
        quadIdx[idx] = idx;
      }
    }

    // Сортировка от дальнего к ближнему (painter's algorithm)
    // Используем простую сортировку вставками (достаточно для ~14000 квадов)
    for( int k = 1; k < quadCount; k++ ) {
      int key = quadIdx[k];
      double keyZ = quadZ[key];
      int m = k - 1;
      while( m >= 0 && quadZ[quadIdx[m]] > keyZ ) {
        quadIdx[m + 1] = quadIdx[m];
        m--;
      }
      quadIdx[m + 1] = key;
    }

    // ── Рисуем квады от дальнего к ближнему ─────────────────
    int[] polyX = new int[4];
    int[] polyY = new int[4];

    for( int k = 0; k < quadCount; k++ ) {
      int idx = quadIdx[k];
      int j = idx / STEPS_U;
      int i = idx % STEPS_U;

      double[] v00 = verts[j][i];
      double[] v10 = verts[j + 1][i];
      double[] v11 = verts[j + 1][i + 1];
      double[] v01 = verts[j][i + 1];

      // Проекция сверху (вид вдоль Z): x→screenX, y→screenY
      polyX[0] = cx + (int)v00[0];
      polyY[0] = cy - (int)v00[1];
      polyX[1] = cx + (int)v10[0];
      polyY[1] = cy - (int)v10[1];
      polyX[2] = cx + (int)v11[0];
      polyY[2] = cy - (int)v11[1];
      polyX[3] = cx + (int)v01[0];
      polyY[3] = cy - (int)v01[1];

      // Освещение по средней нормали квада
      double nx = (v00[3] + v10[3] + v11[3] + v01[3]) / 4.0;
      double ny = (v00[4] + v10[4] + v11[4] + v01[4]) / 4.0;
      double nz = (v00[5] + v10[5] + v11[5] + v01[5]) / 4.0;

      // dot(normal, lightDir)
      double diff = nx * LX + ny * LY + nz * LZ;

      // Ambient + diffuse
      double ambient = 0.15;
      double diffuse = Math.max( 0, diff );

      // Specular (Blinn-Phong): half-vector между светом и взглядом (0,0,1)
      double hx = LX, hy = LY, hz = LZ + 1.0;
      double hLen = Math.sqrt( hx * hx + hy * hy + hz * hz );
      hx /= hLen;
      hy /= hLen;
      hz /= hLen;
      double spec = Math.pow( Math.max( 0, nx * hx + ny * hy + nz * hz ), 32 ) * 0.6;

      double intensity = Math.min( 1.0, ambient + diffuse + spec );

      int cr = Math.min( 255, (int)(BASE_R * intensity + 255 * spec) );
      int cg = Math.min( 255, (int)(BASE_G * intensity + 255 * spec) );
      int cb = Math.min( 255, (int)(BASE_B * intensity + 255 * spec) );

      Color fill = new Color( gc.getDevice(), cr, cg, cb );
      gc.setBackground( fill );
      gc.fillPolygon( toIntArray( polyX, polyY ) );
      fill.dispose();

      // Тонкая обводка для чёткости граней (опционально)
      Color edge = new Color( gc.getDevice(), Math.max( 0, cr - 20 ), Math.max( 0, cg - 20 ), Math.max( 0, cb - 20 ) );
      gc.setForeground( edge );
      gc.drawPolygon( toIntArray( polyX, polyY ) );
      edge.dispose();
    }
  }

  // ── Вспомогательный метод: int[x0,y0,x1,y1,...] для GC ──────

  private static int[] toIntArray( int[] xs, int[] ys ) {
    int[] arr = new int[xs.length * 2];
    for( int k = 0; k < xs.length; k++ ) {
      arr[k * 2] = xs[k];
      arr[k * 2 + 1] = ys[k];
    }
    return arr;
  }
}
