package org.toxsoft.core.tsgui.ved.comps.AI.SVG;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * SvgPathParser — разбирает строку атрибута 'd' SVG-элемента &lt;path&gt; и строит соответствующий SWT {@link Path}.
 * Поддерживаемые команды SVG path (заглавная = абсолютные координаты, строчная = относительные): M / m — moveTo L / l —
 * lineTo H / h — горизонтальная линия V / v — вертикальная линия C / c — кубическая кривая Безье S / s — сглаженная
 * кубическая кривая Безье Q / q — квадратичная кривая Безье T / t — сглаженная квадратичная кривая Безье A / a — дуга
 * эллипса (приближение кубическими Безье) Z / z — закрыть контур Использование: Path swtPath =
 * SvgPathParser.parse(display, svgDAttribute); // ... использовать path ... swtPath.dispose();
 */
public class SvgPathParser {

  // ════════════════════════════════════════════════════════════════
  // Публичный API
  // ════════════════════════════════════════════════════════════════

  /**
   * Разбирает строку SVG path data и возвращает SWT Path. Вызывающий код обязан вызвать {@code path.dispose()} после
   * использования.
   *
   * @param display SWT Display
   * @param pathData строка атрибута 'd', например "M10,20 L30,40 Z"
   * @return готовый SWT Path
   */
  public static Path parse( Display display, String pathData ) {
    Path path = new Path( display );
    if( pathData == null || pathData.isBlank() ) {
      return path;
    }

    Parser parser = new Parser( pathData.trim() );
    parser.buildPath( path );
    return path;
  }

  // ════════════════════════════════════════════════════════════════
  // Внутренний парсер
  // ════════════════════════════════════════════════════════════════
  private static class Parser {

    private final String data;
    private int          pos;

    // Текущая позиция «пера»
    private float curX, curY;
    // Начало текущего подпути (для Z)
    private float startX, startY;
    // Контрольная точка предыдущей кривой (для S/T)
    private float prevCpX, prevCpY;
    // Предыдущая команда
    private char prevCmd = '\0';

    Parser( String data ) {
      this.data = data;
      this.pos = 0;
    }

    void buildPath( Path path ) {
      while( pos < data.length() ) {
        skipWhitespace();
        if( pos >= data.length() ) {
          break;
        }

        char cmd = data.charAt( pos );

        // Если символ — буква, это команда; иначе повторяем предыдущую
        if( Character.isLetter( cmd ) ) {
          pos++;
        }
        else {
          // Неявное повторение: после M -> L, после m -> l
          if( prevCmd == 'M' ) {
            cmd = 'L';
          }
          else
            if( prevCmd == 'm' ) {
              cmd = 'l';
            }
            else {
              cmd = prevCmd;
            }
        }

        executeCommand( path, cmd );
        prevCmd = cmd;
      }
    }

    // ── Выполнение одной команды ─────────────────────────────────
    private void executeCommand( Path path, char cmd ) {
      switch( cmd ) {

        // ── M / m moveTo ────────────────────────────────────
        case 'M': {
          float x = nextFloat(), y = nextFloat();
          path.moveTo( x, y );
          curX = x;
          curY = y;
          startX = x;
          startY = y;
          while( hasMoreCoords() ) {
            x = nextFloat();
            y = nextFloat();
            path.lineTo( x, y );
            curX = x;
            curY = y;
            prevCmd = 'L';
          }
          break;
        }
        case 'm': {
          float x = curX + nextFloat(), y = curY + nextFloat();
          path.moveTo( x, y );
          curX = x;
          curY = y;
          startX = x;
          startY = y;
          while( hasMoreCoords() ) {
            x = curX + nextFloat();
            y = curY + nextFloat();
            path.lineTo( x, y );
            curX = x;
            curY = y;
            prevCmd = 'l';
          }
          break;
        }

        // ── L / l lineTo ────────────────────────────────────
        case 'L': {
          do {
            float x = nextFloat(), y = nextFloat();
            path.lineTo( x, y );
            curX = x;
            curY = y;
          } while( hasMoreCoords() );
          break;
        }
        case 'l': {
          do {
            float x = curX + nextFloat(), y = curY + nextFloat();
            path.lineTo( x, y );
            curX = x;
            curY = y;
          } while( hasMoreCoords() );
          break;
        }

        // ── H / h horizontal lineTo ─────────────────────────
        case 'H': {
          do {
            float x = nextFloat();
            path.lineTo( x, curY );
            curX = x;
          } while( hasMoreCoords() );
          break;
        }
        case 'h': {
          do {
            float x = curX + nextFloat();
            path.lineTo( x, curY );
            curX = x;
          } while( hasMoreCoords() );
          break;
        }

        // ── V / v vertical lineTo ───────────────────────────
        case 'V': {
          do {
            float y = nextFloat();
            path.lineTo( curX, y );
            curY = y;
          } while( hasMoreCoords() );
          break;
        }
        case 'v': {
          do {
            float y = curY + nextFloat();
            path.lineTo( curX, y );
            curY = y;
          } while( hasMoreCoords() );
          break;
        }

        // ── C / c cubic Bezier ──────────────────────────────
        case 'C': {
          do {
            float x1 = nextFloat(), y1 = nextFloat();
            float x2 = nextFloat(), y2 = nextFloat();
            float x = nextFloat(), y = nextFloat();
            path.cubicTo( x1, y1, x2, y2, x, y );
            prevCpX = x2;
            prevCpY = y2;
            curX = x;
            curY = y;
          } while( hasMoreCoords() );
          break;
        }
        case 'c': {
          do {
            float x1 = curX + nextFloat(), y1 = curY + nextFloat();
            float x2 = curX + nextFloat(), y2 = curY + nextFloat();
            float x = curX + nextFloat(), y = curY + nextFloat();
            path.cubicTo( x1, y1, x2, y2, x, y );
            prevCpX = x2;
            prevCpY = y2;
            curX = x;
            curY = y;
          } while( hasMoreCoords() );
          break;
        }

        // ── S / s smooth cubic Bezier ───────────────────────
        case 'S': {
          do {
            float x1 = isAfterCubic() ? 2 * curX - prevCpX : curX;
            float y1 = isAfterCubic() ? 2 * curY - prevCpY : curY;
            float x2 = nextFloat(), y2 = nextFloat();
            float x = nextFloat(), y = nextFloat();
            path.cubicTo( x1, y1, x2, y2, x, y );
            prevCpX = x2;
            prevCpY = y2;
            curX = x;
            curY = y;
          } while( hasMoreCoords() );
          break;
        }
        case 's': {
          do {
            float x1 = isAfterCubic() ? 2 * curX - prevCpX : curX;
            float y1 = isAfterCubic() ? 2 * curY - prevCpY : curY;
            float x2 = curX + nextFloat(), y2 = curY + nextFloat();
            float x = curX + nextFloat(), y = curY + nextFloat();
            path.cubicTo( x1, y1, x2, y2, x, y );
            prevCpX = x2;
            prevCpY = y2;
            curX = x;
            curY = y;
          } while( hasMoreCoords() );
          break;
        }

        // ── Q / q quadratic Bezier → cubic ─────────────────
        case 'Q': {
          do {
            float qx1 = nextFloat(), qy1 = nextFloat();
            float x = nextFloat(), y = nextFloat();
            float[] c = quadToCubic( curX, curY, qx1, qy1, x, y );
            path.cubicTo( c[0], c[1], c[2], c[3], x, y );
            prevCpX = qx1;
            prevCpY = qy1;
            curX = x;
            curY = y;
          } while( hasMoreCoords() );
          break;
        }
        case 'q': {
          do {
            float qx1 = curX + nextFloat(), qy1 = curY + nextFloat();
            float x = curX + nextFloat(), y = curY + nextFloat();
            float[] c = quadToCubic( curX, curY, qx1, qy1, x, y );
            path.cubicTo( c[0], c[1], c[2], c[3], x, y );
            prevCpX = qx1;
            prevCpY = qy1;
            curX = x;
            curY = y;
          } while( hasMoreCoords() );
          break;
        }

        // ── T / t smooth quadratic Bezier ───────────────────
        case 'T': {
          do {
            float qx1 = isAfterQuad() ? 2 * curX - prevCpX : curX;
            float qy1 = isAfterQuad() ? 2 * curY - prevCpY : curY;
            float x = nextFloat(), y = nextFloat();
            float[] c = quadToCubic( curX, curY, qx1, qy1, x, y );
            path.cubicTo( c[0], c[1], c[2], c[3], x, y );
            prevCpX = qx1;
            prevCpY = qy1;
            curX = x;
            curY = y;
          } while( hasMoreCoords() );
          break;
        }
        case 't': {
          do {
            float qx1 = isAfterQuad() ? 2 * curX - prevCpX : curX;
            float qy1 = isAfterQuad() ? 2 * curY - prevCpY : curY;
            float x = curX + nextFloat(), y = curY + nextFloat();
            float[] c = quadToCubic( curX, curY, qx1, qy1, x, y );
            path.cubicTo( c[0], c[1], c[2], c[3], x, y );
            prevCpX = qx1;
            prevCpY = qy1;
            curX = x;
            curY = y;
          } while( hasMoreCoords() );
          break;
        }

        // ── A / a elliptical arc ────────────────────────────
        case 'A': {
          do {
            float rx = Math.abs( nextFloat() );
            float ry = Math.abs( nextFloat() );
            float xRot = nextFloat();
            int largeArc = nextFlag();
            int sweep = nextFlag();
            float x = nextFloat(), y = nextFloat();
            arcTo( path, curX, curY, rx, ry, xRot, largeArc != 0, sweep != 0, x, y );
            curX = x;
            curY = y;
          } while( hasMoreCoords() );
          break;
        }
        case 'a': {
          do {
            float rx = Math.abs( nextFloat() );
            float ry = Math.abs( nextFloat() );
            float xRot = nextFloat();
            int largeArc = nextFlag();
            int sweep = nextFlag();
            float x = curX + nextFloat(), y = curY + nextFloat();
            arcTo( path, curX, curY, rx, ry, xRot, largeArc != 0, sweep != 0, x, y );
            curX = x;
            curY = y;
          } while( hasMoreCoords() );
          break;
        }

        // ── Z / z closePath ─────────────────────────────────
        case 'Z':
        case 'z': {
          path.close();
          curX = startX;
          curY = startY;
          break;
        }

        default:
          pos++; // неизвестная команда — пропускаем
      }
    }

    // ════════════════════════════════════════════════════════════
    // Дуга эллипса → кубические Безье
    // Эталонная реализация: endpoint → center параметризация
    // по спецификации W3C SVG, Appendix F.6
    // ════════════════════════════════════════════════════════════
    private void arcTo( Path path, float x1, float y1, float rx, float ry, float xRotDeg, boolean largeArc,
        boolean sweep, float x2, float y2 ) {

      // Вырожденные случаи — рисуем прямую
      if( rx == 0 || ry == 0 || (x1 == x2 && y1 == y2) ) {
        path.lineTo( x2, y2 );
        return;
      }

      double phi = Math.toRadians( xRotDeg );
      double cosPhi = Math.cos( phi );
      double sinPhi = Math.sin( phi );

      // F.6.5 — преобразование в систему координат эллипса
      double dx = (x1 - x2) / 2.0;
      double dy = (y1 - y2) / 2.0;
      double x1p = cosPhi * dx + sinPhi * dy;
      double y1p = -sinPhi * dx + cosPhi * dy;

      // F.6.6 — корректировка радиусов (если точки вне эллипса)
      double x1pSq = x1p * x1p;
      double y1pSq = y1p * y1p;
      double rxSq = rx * rx;
      double rySq = ry * ry;

      double lambda = x1pSq / rxSq + y1pSq / rySq;
      if( lambda > 1.0 ) {
        double sqrtL = Math.sqrt( lambda );
        rx = (float)(rx * sqrtL);
        ry = (float)(ry * sqrtL);
        rxSq = rx * rx;
        rySq = ry * ry;
      }

      // F.6.5 — вычисление центра в системе эллипса (cxp, cyp)
      double num = rxSq * rySq - rxSq * y1pSq - rySq * x1pSq;
      double den = rxSq * y1pSq + rySq * x1pSq;
      // Знак: если largeArc == sweep → минус, иначе плюс
      double sq = (den == 0.0) ? 0.0 : Math.sqrt( Math.max( 0.0, num / den ) );
      if( largeArc == sweep ) {
        sq = -sq;
      }

      double cxp = sq * (rx * y1p / ry);
      double cyp = -sq * (ry * x1p / rx);

      // F.6.5 — центр в исходной системе координат
      double cx = cosPhi * cxp - sinPhi * cyp + (x1 + x2) / 2.0;
      double cy = sinPhi * cxp + cosPhi * cyp + (y1 + y2) / 2.0;

      // F.6.5 — начальный угол θ₁ и размах дуги Δθ
      double ux = (x1p - cxp) / rx;
      double uy = (y1p - cyp) / ry;
      double vx = -(x1p + cxp) / rx;
      double vy = -(y1p + cyp) / ry;

      double theta1 = vectorAngle( 1.0, 0.0, ux, uy );
      double dTheta = vectorAngle( ux, uy, vx, vy );

      // Корректировка направления обхода
      if( !sweep && dTheta > 0.0 ) {
        dTheta -= 2.0 * Math.PI;
      }
      if( sweep && dTheta < 0.0 ) {
        dTheta += 2.0 * Math.PI;
      }

      // Разбиваем на сегменты не более π/2 (90°) каждый.
      // alpha пересчитывается для каждого сегмента отдельно.
      int segs = Math.max( 1, (int)Math.ceil( Math.abs( dTheta ) / (Math.PI / 2.0) ) );
      double segAngle = dTheta / segs;

      double angStart = theta1;
      for( int i = 0; i < segs; i++ ) {
        double angEnd = angStart + segAngle;
        // Коэффициент Безье-аппроксимации для данного сегмента
        double a = 4.0 / 3.0 * Math.tan( segAngle / 4.0 );
        emitArcSegment( path, cx, cy, rx, ry, cosPhi, sinPhi, angStart, angEnd, a );
        angStart = angEnd;
      }
    }

    /**
     * Выпускает один сегмент дуги как кубическую кривую Безье. Параметрическое уравнение точки на эллипсе: x(t) = cx +
     * cosPhi*rx*cos(t) - sinPhi*ry*sin(t) y(t) = cy + sinPhi*rx*cos(t) + cosPhi*ry*sin(t) Производная (касательная):
     * x'(t) = -cosPhi*rx*sin(t) - sinPhi*ry*cos(t) y'(t) = -sinPhi*rx*sin(t) + cosPhi*ry*cos(t) Контрольные точки
     * кубической Безье: cp1 = P(t1) + a * P'(t1) cp2 = P(t2) - a * P'(t2)
     */
    private static void emitArcSegment( Path path, double cx, double cy, double rx, double ry, double cosPhi,
        double sinPhi, double t1, double t2, double a ) {

      double cosT1 = Math.cos( t1 ), sinT1 = Math.sin( t1 );
      double cosT2 = Math.cos( t2 ), sinT2 = Math.sin( t2 );

      // Начальная и конечная точки сегмента на эллипсе
      double p1x = cx + cosPhi * rx * cosT1 - sinPhi * ry * sinT1;
      double p1y = cy + sinPhi * rx * cosT1 + cosPhi * ry * sinT1;
      double p4x = cx + cosPhi * rx * cosT2 - sinPhi * ry * sinT2;
      double p4y = cy + sinPhi * rx * cosT2 + cosPhi * ry * sinT2;

      // Касательные в начальной и конечной точках
      double d1x = -cosPhi * rx * sinT1 - sinPhi * ry * cosT1;
      double d1y = -sinPhi * rx * sinT1 + cosPhi * ry * cosT1;
      double d4x = -cosPhi * rx * sinT2 - sinPhi * ry * cosT2;
      double d4y = -sinPhi * rx * sinT2 + cosPhi * ry * cosT2;

      // Контрольные точки
      double cp1x = p1x + a * d1x;
      double cp1y = p1y + a * d1y;
      double cp2x = p4x - a * d4x;
      double cp2y = p4y - a * d4y;

      // Первый сегмент: убеждаемся что перо стоит в p1
      // (для первого сегмента дуги perо уже там после moveTo/lineTo;
      // для последующих — p1 == конец предыдущего сегмента точно)
      path.cubicTo( (float)cp1x, (float)cp1y, (float)cp2x, (float)cp2y, (float)p4x, (float)p4y );
    }

    /**
     * Угол между двумя 2D-векторами с учётом знака (направления). Возвращает значение в радианах в диапазоне (-π, π].
     */
    private static double vectorAngle( double ux, double uy, double vx, double vy ) {
      double dot = ux * vx + uy * vy;
      double lenU = Math.sqrt( ux * ux + uy * uy );
      double lenV = Math.sqrt( vx * vx + vy * vy );
      double cosAng = Math.max( -1.0, Math.min( 1.0, dot / (lenU * lenV) ) );
      double angle = Math.acos( cosAng );
      // Знак определяется знаком cross product
      return (ux * vy - uy * vx < 0.0) ? -angle : angle;
    }

    // ════════════════════════════════════════════════════════════
    // Квадратичная Безье → кубическая
    // P1_cubic = P0 + 2/3*(P1_quad - P0)
    // P2_cubic = P2 + 2/3*(P1_quad - P2)
    // ════════════════════════════════════════════════════════════
    private static float[] quadToCubic( float x0, float y0, float qx, float qy, float x2, float y2 ) {
      return new float[] { x0 + 2f / 3f * (qx - x0), y0 + 2f / 3f * (qy - y0), x2 + 2f / 3f * (qx - x2),
          y2 + 2f / 3f * (qy - y2) };
    }

    // ════════════════════════════════════════════════════════════
    // Лексер
    // ════════════════════════════════════════════════════════════

    /** Читает следующее число (включая знак, точку, экспоненту). */
    private float nextFloat() {
      skipSeparator();
      int start = pos;
      if( pos < data.length() && (data.charAt( pos ) == '-' || data.charAt( pos ) == '+') ) {
        pos++;
      }
      while( pos < data.length() && (Character.isDigit( data.charAt( pos ) ) || data.charAt( pos ) == '.') ) {
        pos++;
      }
      // Научная нотация: 1.5e-3
      if( pos < data.length() && (data.charAt( pos ) == 'e' || data.charAt( pos ) == 'E') ) {
        pos++;
        if( pos < data.length() && (data.charAt( pos ) == '-' || data.charAt( pos ) == '+') ) {
          pos++;
        }
        while( pos < data.length() && Character.isDigit( data.charAt( pos ) ) ) {
          pos++;
        }
      }
      if( start == pos ) {
        return 0f;
      }
      try {
        return Float.parseFloat( data.substring( start, pos ) );
      }
      catch( NumberFormatException e ) {
        return 0f;
      }
    }

    /**
     * Флаги arc (largeArc, sweep) — всегда ровно '0' или '1'. Между двумя флагами запятая не обязательна: "0101" —
     * валидно.
     */
    private int nextFlag() {
      skipSeparator();
      if( pos < data.length() ) {
        char c = data.charAt( pos );
        if( c == '0' || c == '1' ) {
          pos++;
          return c - '0';
        }
      }
      return 0;
    }

    /** Пропускаем пробелы и необязательную запятую. */
    private void skipSeparator() {
      skipWhitespace();
      if( pos < data.length() && data.charAt( pos ) == ',' ) {
        pos++;
        skipWhitespace();
      }
    }

    private void skipWhitespace() {
      while( pos < data.length() && Character.isWhitespace( data.charAt( pos ) ) ) {
        pos++;
      }
    }

    /**
     * Проверяем, есть ли ещё числа для повторения команды. Число начинается с цифры, знака или точки, но не с
     * буквы-команды.
     */
    private boolean hasMoreCoords() {
      skipWhitespace();
      if( pos >= data.length() ) {
        return false;
      }
      char c = data.charAt( pos );
      // Буква-команда SVG — это конец параметров текущей команды
      if( Character.isLetter( c ) ) {
        return false;
      }
      if( c == ',' ) {
        pos++;
        return true;
      }
      return Character.isDigit( c ) || c == '-' || c == '+' || c == '.';
    }

    private boolean isAfterCubic() {
      return prevCmd == 'C' || prevCmd == 'c' || prevCmd == 'S' || prevCmd == 's';
    }

    private boolean isAfterQuad() {
      return prevCmd == 'Q' || prevCmd == 'q' || prevCmd == 'T' || prevCmd == 't';
    }
  }

  // ════════════════════════════════════════════════════════════════
  // Демонстрация
  // ════════════════════════════════════════════════════════════════
  public static void main( String[] args ) {

    String[] examples = {
        // Кольцо — две концентрические окружности (ваш SVG)
        "M 124.72461 77.775391 A 84.744678 84.744678 0 0 0 39.980469 162.51953 "
            + "A 84.744678 84.744678 0 0 0 124.72461 247.26367 " + "A 84.744678 84.744678 0 0 0 209.46875 162.51953 "
            + "A 84.744678 84.744678 0 0 0 124.72461 77.775391 z "
            + "M 124.72461 120.8418 A 41.67771 41.67771 0 0 1 166.40234 162.51953 "
            + "A 41.67771 41.67771 0 0 1 124.72461 204.19727 " + "A 41.67771 41.67771 0 0 1 83.046875 162.51953 "
            + "A 41.67771 41.67771 0 0 1 124.72461 120.8418 z",

        // Скруглённый прямоугольник (дуги A)
        "M 280 70 H 400 A 20 20 0 0 1 420 90 V 160 " + "A 20 20 0 0 1 400 180 H 280 A 20 20 0 0 1 260 160 "
            + "V 90 A 20 20 0 0 1 280 70 Z",

        // Сердечко (кубические Безье)
        "M 100 270 C 100 240 130 210 160 230 " + "C 185 210 210 235 210 260 L 155 320 Z",

        // Звезда (относительные l)
        "M 430 80 l 18 52 l 55 0 l -44 32 l 17 52 " + "l -46 -34 l -46 34 l 17 -52 l -44 -32 l 55 0 Z",

        // Квадратичная Безье (Q)
        "M 250 320 Q 330 260 410 320", };

    int[] fillColors = { 0x2196F3, 0x4CAF50, 0xF44336, 0xFF9800, 0x00BCD4 };

    Display display = new Display();
    org.eclipse.swt.widgets.Shell shell = new org.eclipse.swt.widgets.Shell( display );
    shell.setText( "SvgPathParser — Demo" );
    shell.setSize( 620, 480 );

    shell.addPaintListener( e -> {
      e.gc.setBackground( display.getSystemColor( org.eclipse.swt.SWT.COLOR_WHITE ) );
      e.gc.fillRectangle( shell.getClientArea() );
      e.gc.setAntialias( org.eclipse.swt.SWT.ON );
      e.gc.setLineWidth( 2 );

      for( int i = 0; i < examples.length; i++ ) {
        Path p = SvgPathParser.parse( display, examples[i] );
        int fc = fillColors[i];
        org.eclipse.swt.graphics.Color fill =
            new org.eclipse.swt.graphics.Color( display, (fc >> 16) & 0xFF, (fc >> 8) & 0xFF, fc & 0xFF );
        org.eclipse.swt.graphics.Color stroke = new org.eclipse.swt.graphics.Color( display, (fc >> 16) & 0xFF >> 1,
            (fc >> 8) & 0xFF >> 1, fc & 0xFF >> 1 );
        e.gc.setBackground( fill );
        e.gc.setAlpha( 200 );
        e.gc.fillPath( p );
        e.gc.setAlpha( 255 );
        e.gc.setForeground( stroke );
        e.gc.drawPath( p );
        fill.dispose();
        stroke.dispose();
        p.dispose();
      }
    } );

    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }
}
