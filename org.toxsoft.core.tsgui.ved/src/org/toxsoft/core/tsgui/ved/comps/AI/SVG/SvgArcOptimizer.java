package org.toxsoft.core.tsgui.ved.comps.AI.SVG;

import java.util.*;
import java.util.List;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * SvgArcOptimizer Разбирает строку атрибута 'd' SVG path и строит SWT Path, заменяя последовательные команды 'A'/'a',
 * принадлежащие одной окружности, на единственный вызов {@code path.addArc()}. Условия объединения нескольких SVG-дуг в
 * один addArc: 1. rx == ry (окружность, не эллипс) 2. xRotation == 0 3. Вычисленные центры совпадают с точностью
 * EPSILON 4. Радиусы совпадают с точностью EPSILON 5. Одинаковое направление sweep 6. Конец предыдущей дуги == начало
 * следующей (непрерывность) Все остальные команды (M/L/C/Z и т.д.) обрабатываются стандартно через SWT Path API.
 * Использование: Path p = SvgArcOptimizer.parse(display, svgDAttribute); gc.drawPath(p); p.dispose();
 */
public class SvgArcOptimizer {

  /** Допуск при сравнении вещественных чисел. */
  private static final double EPSILON = 0.5;

  // ════════════════════════════════════════════════════════════════
  // Публичный API
  // ════════════════════════════════════════════════════════════════

  public static Path parse( Display display, String pathData ) {
    Path path = new Path( display );
    if( pathData == null || pathData.isBlank() ) {
      return path;
    }

    // Шаг 1: разобрать строку в список промежуточных команд
    List<Cmd> cmds = new Tokenizer( pathData.trim() ).tokenize();

    // Шаг 2: объединить последовательные дуги одной окружности
    List<Cmd> optimized = optimize( cmds );

    // Шаг 3: построить SWT Path
    new Builder( optimized ).build( path );
    return path;
  }

  // ════════════════════════════════════════════════════════════════
  // Промежуточное представление команд
  // ════════════════════════════════════════════════════════════════

  /** Типы промежуточных команд. */
  enum CmdType {
    MOVE_TO,
    LINE_TO,
    CUBIC_TO,
    ARC_TO,
    ADD_ARC,
    CLOSE
  }

  /** Универсальный контейнер команды. */
  static class Cmd {

    CmdType type;
    float[] args; // смысл зависит от type

    Cmd( CmdType type, float... args ) {
      this.type = type;
      this.args = args;
    }

    // ── Фабричные методы ────────────────────────────────────────

    static Cmd moveTo( float x, float y ) {
      return new Cmd( CmdType.MOVE_TO, x, y );
    }

    static Cmd lineTo( float x, float y ) {
      return new Cmd( CmdType.LINE_TO, x, y );
    }

    static Cmd cubicTo( float x1, float y1, float x2, float y2, float x, float y ) {
      return new Cmd( CmdType.CUBIC_TO, x1, y1, x2, y2, x, y );
    }

    /**
     * Сырая SVG-дуга (endpoint-параметризация). args: [x1, y1, rx, ry, xRot, largeArc(0/1), sweep(0/1), x2, y2, cx, cy,
     * theta1Deg, dThetaDeg] cx, cy, theta1Deg, dThetaDeg — вычисляются при токенизации.
     */
    static Cmd arcTo( float x1, float y1, float rx, float ry, float xRot, float largeArc, float sweep, float x2,
        float y2, float cx, float cy, float theta1Deg, float dThetaDeg ) {
      return new Cmd( CmdType.ARC_TO, x1, y1, rx, ry, xRot, largeArc, sweep, x2, y2, cx, cy, theta1Deg, dThetaDeg );
    }

    /**
     * Оптимизированная дуга окружности для SWT addArc. args: [cx, cy, r, startAngleDeg, arcAngleDeg]
     */
    static Cmd addArc( float cx, float cy, float r, float startAngleDeg, float arcAngleDeg ) {
      return new Cmd( CmdType.ADD_ARC, cx, cy, r, startAngleDeg, arcAngleDeg );
    }

    static Cmd close() {
      return new Cmd( CmdType.CLOSE );
    }

    // ── Геттеры для ARC_TO ──────────────────────────────────────
    float arcX1() {
      return args[0];
    }

    float arcY1() {
      return args[1];
    }

    float arcRx() {
      return args[2];
    }

    float arcRy() {
      return args[3];
    }

    float arcXRot() {
      return args[4];
    }

    float arcSweep() {
      return args[6];
    }

    float arcX2() {
      return args[7];
    }

    float arcY2() {
      return args[8];
    }

    float arcCx() {
      return args[9];
    }

    float arcCy() {
      return args[10];
    }

    float arcTheta1() {
      return args[11];
    }

    float arcDTheta() {
      return args[12];
    }
  }

  // ════════════════════════════════════════════════════════════════
  // Шаг 1: Токенизатор — строка → список Cmd
  // ════════════════════════════════════════════════════════════════

  private static class Tokenizer {

    private final String data;
    private int          pos;
    private float        curX, curY, startX, startY;
    private float        prevCpX, prevCpY;
    private char         prevCmd = '\0';

    Tokenizer( String data ) {
      this.data = data;
    }

    List<Cmd> tokenize() {
      List<Cmd> list = new ArrayList<>();
      while( pos < data.length() ) {
        skipWhitespace();
        if( pos >= data.length() ) {
          break;
        }
        char cmd = data.charAt( pos );
        if( Character.isLetter( cmd ) ) {
          pos++;
        }
        else {
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
        tokenizeCmd( list, cmd );
        prevCmd = cmd;
      }
      return list;
    }

    private void tokenizeCmd( List<Cmd> out, char cmd ) {
      switch( cmd ) {
        case 'M': {
          float x = nextFloat(), y = nextFloat();
          out.add( Cmd.moveTo( x, y ) );
          curX = x;
          curY = y;
          startX = x;
          startY = y;
          while( hasMore() ) {
            x = nextFloat();
            y = nextFloat();
            out.add( Cmd.lineTo( x, y ) );
            curX = x;
            curY = y;
            prevCmd = 'L';
          }
          break;
        }
        case 'm': {
          float x = curX + nextFloat(), y = curY + nextFloat();
          out.add( Cmd.moveTo( x, y ) );
          curX = x;
          curY = y;
          startX = x;
          startY = y;
          while( hasMore() ) {
            x = curX + nextFloat();
            y = curY + nextFloat();
            out.add( Cmd.lineTo( x, y ) );
            curX = x;
            curY = y;
            prevCmd = 'l';
          }
          break;
        }
        case 'L': {
          do {
            float x = nextFloat(), y = nextFloat();
            out.add( Cmd.lineTo( x, y ) );
            curX = x;
            curY = y;
          } while( hasMore() );
          break;
        }
        case 'l': {
          do {
            float x = curX + nextFloat(), y = curY + nextFloat();
            out.add( Cmd.lineTo( x, y ) );
            curX = x;
            curY = y;
          } while( hasMore() );
          break;
        }
        case 'H': {
          do {
            float x = nextFloat();
            out.add( Cmd.lineTo( x, curY ) );
            curX = x;
          } while( hasMore() );
          break;
        }
        case 'h': {
          do {
            float x = curX + nextFloat();
            out.add( Cmd.lineTo( x, curY ) );
            curX = x;
          } while( hasMore() );
          break;
        }
        case 'V': {
          do {
            float y = nextFloat();
            out.add( Cmd.lineTo( curX, y ) );
            curY = y;
          } while( hasMore() );
          break;
        }
        case 'v': {
          do {
            float y = curY + nextFloat();
            out.add( Cmd.lineTo( curX, y ) );
            curY = y;
          } while( hasMore() );
          break;
        }
        case 'C': {
          do {
            float x1 = nextFloat(), y1 = nextFloat();
            float x2 = nextFloat(), y2 = nextFloat();
            float x = nextFloat(), y = nextFloat();
            out.add( Cmd.cubicTo( x1, y1, x2, y2, x, y ) );
            prevCpX = x2;
            prevCpY = y2;
            curX = x;
            curY = y;
          } while( hasMore() );
          break;
        }
        case 'c': {
          do {
            float x1 = curX + nextFloat(), y1 = curY + nextFloat();
            float x2 = curX + nextFloat(), y2 = curY + nextFloat();
            float x = curX + nextFloat(), y = curY + nextFloat();
            out.add( Cmd.cubicTo( x1, y1, x2, y2, x, y ) );
            prevCpX = x2;
            prevCpY = y2;
            curX = x;
            curY = y;
          } while( hasMore() );
          break;
        }
        case 'Q':
        case 'q': {
          // Квадратичная → кубическая
          do {
            float qx1, qy1, x, y;
            if( cmd == 'Q' ) {
              qx1 = nextFloat();
              qy1 = nextFloat();
              x = nextFloat();
              y = nextFloat();
            }
            else {
              qx1 = curX + nextFloat();
              qy1 = curY + nextFloat();
              x = curX + nextFloat();
              y = curY + nextFloat();
            }
            float[] c = quadToCubic( curX, curY, qx1, qy1, x, y );
            out.add( Cmd.cubicTo( c[0], c[1], c[2], c[3], x, y ) );
            prevCpX = qx1;
            prevCpY = qy1;
            curX = x;
            curY = y;
          } while( hasMore() );
          break;
        }
        case 'S':
        case 's':
        case 'T':
        case 't': {
          // Сглаженные кривые
          do {
            boolean isCubic = (cmd == 'S' || cmd == 's');
            boolean isAbs = (cmd == 'S' || cmd == 'T');
            boolean afterC = prevCmd == 'C' || prevCmd == 'c' || prevCmd == 'S' || prevCmd == 's';
            boolean afterQ = prevCmd == 'Q' || prevCmd == 'q' || prevCmd == 'T' || prevCmd == 't';
            if( isCubic ) {
              float x1 = afterC ? 2 * curX - prevCpX : curX;
              float y1 = afterC ? 2 * curY - prevCpY : curY;
              float x2 = isAbs ? nextFloat() : curX + nextFloat();
              float y2 = isAbs ? nextFloat() : curY + nextFloat();
              float x = isAbs ? nextFloat() : curX + nextFloat();
              float y = isAbs ? nextFloat() : curY + nextFloat();
              out.add( Cmd.cubicTo( x1, y1, x2, y2, x, y ) );
              prevCpX = x2;
              prevCpY = y2;
              curX = x;
              curY = y;
            }
            else {
              float qx1 = afterQ ? 2 * curX - prevCpX : curX;
              float qy1 = afterQ ? 2 * curY - prevCpY : curY;
              float x = isAbs ? nextFloat() : curX + nextFloat();
              float y = isAbs ? nextFloat() : curY + nextFloat();
              float[] c = quadToCubic( curX, curY, qx1, qy1, x, y );
              out.add( Cmd.cubicTo( c[0], c[1], c[2], c[3], x, y ) );
              prevCpX = qx1;
              prevCpY = qy1;
              curX = x;
              curY = y;
            }
          } while( hasMore() );
          break;
        }

        // ── A/a: вычисляем center-параметризацию сразу ───────
        case 'A':
        case 'a': {
          do {
            float rx = Math.abs( nextFloat() );
            float ry = Math.abs( nextFloat() );
            float rot = nextFloat();
            int large = nextFlag();
            int sweep = nextFlag();
            float x2 = (cmd == 'A') ? nextFloat() : curX + nextFloat();
            float y2 = (cmd == 'A') ? nextFloat() : curY + nextFloat();

            // Вычисляем center-параметризацию
            double[] cp = endpointToCenter( curX, curY, rx, ry, rot, large != 0, sweep != 0, x2, y2 );

            out.add( Cmd.arcTo( curX, curY, rx, ry, rot, large, sweep, x2, y2, (float)cp[0], (float)cp[1], // cx, cy
                (float)cp[2], (float)cp[3] ) ); // theta1, dTheta (degrees)
            curX = x2;
            curY = y2;
          } while( hasMore() );
          break;
        }

        case 'Z':
        case 'z':
          out.add( Cmd.close() );
          curX = startX;
          curY = startY;
          break;

        default:
          pos++;
      }
    }

    // ── Лексер ──────────────────────────────────────────────────
    float nextFloat() {
      skipSep();
      int start = pos;
      if( pos < data.length() && (data.charAt( pos ) == '-' || data.charAt( pos ) == '+') ) {
        pos++;
      }
      while( pos < data.length() && (Character.isDigit( data.charAt( pos ) ) || data.charAt( pos ) == '.') ) {
        pos++;
      }
      if( pos < data.length() && (data.charAt( pos ) == 'e' || data.charAt( pos ) == 'E') ) {
        pos++;
        if( pos < data.length() && (data.charAt( pos ) == '-' || data.charAt( pos ) == '+') ) {
          pos++;
        }
        while( pos < data.length() && Character.isDigit( data.charAt( pos ) ) ) {
          pos++;
        }
      }
      try {
        return Float.parseFloat( data.substring( start, pos ) );
      }
      catch( NumberFormatException e ) {
        return 0f;
      }
    }

    int nextFlag() {
      skipSep();
      if( pos < data.length() ) {
        char c = data.charAt( pos );
        if( c == '0' || c == '1' ) {
          pos++;
          return c - '0';
        }
      }
      return 0;
    }

    void skipSep() {
      skipWs();
      if( pos < data.length() && data.charAt( pos ) == ',' ) {
        pos++;
        skipWs();
      }
    }

    void skipWhitespace() {
      skipWs();
    }

    void skipWs() {
      while( pos < data.length() && Character.isWhitespace( data.charAt( pos ) ) ) {
        pos++;
      }
    }

    boolean hasMore() {
      skipWs();
      if( pos >= data.length() ) {
        return false;
      }
      char c = data.charAt( pos );
      if( Character.isLetter( c ) ) {
        return false;
      }
      if( c == ',' ) {
        pos++;
        return true;
      }
      return Character.isDigit( c ) || c == '-' || c == '+' || c == '.';
    }
  }

  // ════════════════════════════════════════════════════════════════
  // Шаг 2: Оптимизатор — группировка дуг одной окружности
  // ════════════════════════════════════════════════════════════════

  private static List<Cmd> optimize( List<Cmd> cmds ) {
    List<Cmd> out = new ArrayList<>();
    int i = 0;
    while( i < cmds.size() ) {
      Cmd c = cmds.get( i );

      // Пробуем начать группу дуг окружности
      if( c.type == CmdType.ARC_TO && isCircleArc( c ) ) {
        // Собираем все последующие совместимые дуги
        List<Cmd> group = new ArrayList<>();
        group.add( c );
        int j = i + 1;
        while( j < cmds.size() ) {
          Cmd next = cmds.get( j );
          if( next.type == CmdType.ARC_TO && isCircleArc( next ) && canMerge( group.get( group.size() - 1 ), next ) ) {
            group.add( next );
            j++;
          }
          else {
            break;
          }
        }

        if( group.size() > 1 ) {
          // Несколько дуг → один addArc
          out.add( mergeArcs( group ) );
          i = j;
        }
        else {
          // Одиночная дуга — оставляем как ARC_TO (Builder отрисует)
          out.add( c );
          i++;
        }
      }
      else {
        out.add( c );
        i++;
      }
    }
    return out;
  }

  /**
   * Дуга является дугой окружности если: rx == ry и xRotation == 0.
   */
  private static boolean isCircleArc( Cmd c ) {
    return Math.abs( c.arcRx() - c.arcRy() ) < EPSILON && Math.abs( c.arcXRot() ) < EPSILON;
  }

  /**
   * Две соседние дуги можно объединить если: - одинаковый центр окружности (с точностью EPSILON) - одинаковый радиус -
   * одинаковое направление sweep - конец первой == начало второй (гарантировано токенизатором, но проверяем на всякий
   * случай)
   */
  private static boolean canMerge( Cmd a, Cmd b ) {
    if( Math.abs( a.arcSweep() - b.arcSweep() ) > EPSILON ) {
      return false;
    }
    if( Math.abs( a.arcRx() - b.arcRx() ) > EPSILON ) {
      return false;
    }
    if( Math.abs( a.arcCx() - b.arcCx() ) > EPSILON ) {
      return false;
    }
    if( Math.abs( a.arcCy() - b.arcCy() ) > EPSILON ) {
      return false;
    }
    // Непрерывность: конец a == начало b
    if( Math.abs( a.arcX2() - b.arcX1() ) > EPSILON ) {
      return false;
    }
    if( Math.abs( a.arcY2() - b.arcY1() ) > EPSILON ) {
      return false;
    }
    return true;
  }

  /**
   * Объединяет группу дуг одной окружности в единственный addArc. SWT addArc(x, y, w, h, startAngle, arcAngle): x, y —
   * левый верхний угол описывающего прямоугольника w, h — ширина и высота (= 2*r для окружности) startAngle — начальный
   * угол в градусах (0° = 3 часа, CCW+) arcAngle — размах дуги в градусах (+ = CCW, - = CW) SVG использует Y-ось вниз,
   * SWT — Y-ось вверх для углов addArc. Поэтому инвертируем Y-компоненту угла: swtAngle = -svgAngle
   */
  private static Cmd mergeArcs( List<Cmd> group ) {
    Cmd first = group.get( 0 );
    float cx = first.arcCx();
    float cy = first.arcCy();
    float r = first.arcRx();

    // Суммируем угловые размахи
    float totalDTheta = 0;
    for( Cmd c : group ) {
      totalDTheta += c.arcDTheta();
    }

    // Начальный угол первой дуги (в градусах SVG)
    float theta1Svg = first.arcTheta1();

    // Конвертация SVG → SWT:
    // SVG: 0°=right, CW+ (Y вниз)
    // SWT addArc: 0°=right, CCW+ (Y вверх)
    // Чтобы совпало визуально: инвертируем знак угла
    float swtStart = -theta1Svg;
    float swtExtent = -totalDTheta;

    return Cmd.addArc( cx, cy, r, swtStart, swtExtent );
  }

  // ════════════════════════════════════════════════════════════════
  // Шаг 3: Builder — список Cmd → SWT Path
  // ════════════════════════════════════════════════════════════════

  private static class Builder {

    private final List<Cmd> cmds;

    Builder( List<Cmd> cmds ) {
      this.cmds = cmds;
    }

    void build( Path path ) {
      for( Cmd c : cmds ) {
        switch( c.type ) {
          case MOVE_TO:
            path.moveTo( c.args[0], c.args[1] );
            break;
          case LINE_TO:
            path.lineTo( c.args[0], c.args[1] );
            break;
          case CUBIC_TO:
            path.cubicTo( c.args[0], c.args[1], c.args[2], c.args[3], c.args[4], c.args[5] );
            break;
          case ARC_TO:
            // Одиночная дуга — рисуем через Безье
            buildSingleArc( path, c );
            break;
          case ADD_ARC: {
            // Объединённая дуга окружности — используем addArc
            float cx = c.args[0], cy = c.args[1], r = c.args[2];
            float start = c.args[3], extent = c.args[4];
            // addArc(x, y, width, height, startAngle, arcAngle)
            path.addArc( cx - r, cy - r, 2 * r, 2 * r, start, extent );
            break;
          }
          case CLOSE:
            path.close();
            break;
        }
      }
    }

    /** Одиночная SVG-дуга → кубические Безье (как в SvgPathParser). */
    private static void buildSingleArc( Path path, Cmd c ) {
      float x1 = c.arcX1(), y1 = c.arcY1();
      float rx = c.arcRx(), ry = c.arcRy();
      float x2 = c.arcX2(), y2 = c.arcY2();
      float cx = c.arcCx(), cy = c.arcCy();
      // theta1 и dTheta хранятся в градусах — переводим обратно
      double theta1 = Math.toRadians( c.arcTheta1() );
      double dTheta = Math.toRadians( c.arcDTheta() );
      double cosPhi = 1.0, sinPhi = 0.0; // xRot == 0 для окружностей
      // для общего случая:
      double phi = Math.toRadians( c.arcXRot() );
      cosPhi = Math.cos( phi );
      sinPhi = Math.sin( phi );

      if( rx == 0 || ry == 0 || (x1 == x2 && y1 == y2) ) {
        path.lineTo( x2, y2 );
        return;
      }

      int segs = Math.max( 1, (int)Math.ceil( Math.abs( dTheta ) / (Math.PI / 2) ) );
      double segAngle = dTheta / segs;
      double angStart = theta1;
      for( int i = 0; i < segs; i++ ) {
        double angEnd = angStart + segAngle;
        double a = 4.0 / 3.0 * Math.tan( segAngle / 4.0 );
        emitArcSegment( path, cx, cy, rx, ry, cosPhi, sinPhi, angStart, angEnd, a );
        angStart = angEnd;
      }
    }
  }

  // ════════════════════════════════════════════════════════════════
  // Вычисление center-параметризации из endpoint-параметризации
  // W3C SVG spec, Appendix F.6
  // Возвращает: [cx, cy, theta1Degrees, dThetaDegrees]
  // ════════════════════════════════════════════════════════════════

  static double[] endpointToCenter( float x1, float y1, float rx, float ry, float xRotDeg, boolean largeArc,
      boolean sweep, float x2, float y2 ) {

    double phi = Math.toRadians( xRotDeg );
    double cosPhi = Math.cos( phi );
    double sinPhi = Math.sin( phi );

    double dx = (x1 - x2) / 2.0;
    double dy = (y1 - y2) / 2.0;
    double x1p = cosPhi * dx + sinPhi * dy;
    double y1p = -sinPhi * dx + cosPhi * dy;

    double x1pSq = x1p * x1p, y1pSq = y1p * y1p;
    double rxSq = rx * rx, rySq = ry * ry;

    double lambda = x1pSq / rxSq + y1pSq / rySq;
    if( lambda > 1.0 ) {
      double s = Math.sqrt( lambda );
      rx *= s;
      ry *= s;
      rxSq = rx * rx;
      rySq = ry * ry;
    }

    double num = rxSq * rySq - rxSq * y1pSq - rySq * x1pSq;
    double den = rxSq * y1pSq + rySq * x1pSq;
    double sq = (den == 0.0) ? 0.0 : Math.sqrt( Math.max( 0.0, num / den ) );
    if( largeArc == sweep ) {
      sq = -sq;
    }

    double cxp = sq * (rx * y1p / ry);
    double cyp = -sq * (ry * x1p / rx);
    double cx = cosPhi * cxp - sinPhi * cyp + (x1 + x2) / 2.0;
    double cy = sinPhi * cxp + cosPhi * cyp + (y1 + y2) / 2.0;

    double ux = (x1p - cxp) / rx, uy = (y1p - cyp) / ry;
    double vx = -(x1p + cxp) / rx, vy = -(y1p + cyp) / ry;

    double theta1 = vectorAngle( 1.0, 0.0, ux, uy );
    double dTheta = vectorAngle( ux, uy, vx, vy );
    if( !sweep && dTheta > 0.0 ) {
      dTheta -= 2.0 * Math.PI;
    }
    if( sweep && dTheta < 0.0 ) {
      dTheta += 2.0 * Math.PI;
    }

    return new double[] { cx, cy, Math.toDegrees( theta1 ), Math.toDegrees( dTheta ) };
  }

  // ════════════════════════════════════════════════════════════════
  // Общие утилиты
  // ════════════════════════════════════════════════════════════════

  private static void emitArcSegment( Path path, double cx, double cy, double rx, double ry, double cosPhi,
      double sinPhi, double t1, double t2, double a ) {

    double cosT1 = Math.cos( t1 ), sinT1 = Math.sin( t1 );
    double cosT2 = Math.cos( t2 ), sinT2 = Math.sin( t2 );

    double p1x = cx + cosPhi * rx * cosT1 - sinPhi * ry * sinT1;
    double p1y = cy + sinPhi * rx * cosT1 + cosPhi * ry * sinT1;
    double p4x = cx + cosPhi * rx * cosT2 - sinPhi * ry * sinT2;
    double p4y = cy + sinPhi * rx * cosT2 + cosPhi * ry * sinT2;

    double d1x = -cosPhi * rx * sinT1 - sinPhi * ry * cosT1;
    double d1y = -sinPhi * rx * sinT1 + cosPhi * ry * cosT1;
    double d4x = -cosPhi * rx * sinT2 - sinPhi * ry * cosT2;
    double d4y = -sinPhi * rx * sinT2 + cosPhi * ry * cosT2;

    path.cubicTo( (float)(p1x + a * d1x), (float)(p1y + a * d1y), (float)(p4x - a * d4x), (float)(p4y - a * d4y),
        (float)p4x, (float)p4y );
  }

  private static double vectorAngle( double ux, double uy, double vx, double vy ) {
    double dot = ux * vx + uy * vy;
    double lenU = Math.sqrt( ux * ux + uy * uy );
    double lenV = Math.sqrt( vx * vx + vy * vy );
    double cos = Math.max( -1.0, Math.min( 1.0, dot / (lenU * lenV) ) );
    double a = Math.acos( cos );
    return (ux * vy - uy * vx < 0.0) ? -a : a;
  }

  private static float[] quadToCubic( float x0, float y0, float qx, float qy, float x2, float y2 ) {
    return new float[] { x0 + 2f / 3f * (qx - x0), y0 + 2f / 3f * (qy - y0), x2 + 2f / 3f * (qx - x2),
        y2 + 2f / 3f * (qy - y2) };
  }

  // ════════════════════════════════════════════════════════════════
  // Демонстрация
  // ════════════════════════════════════════════════════════════════
  public static void main( String[] args ) {

    // Кольцо из вашего SVG — 4+4 дуги → должны стать 2 addArc
    String ring = "M 124.72461 77.775391 " + "A 84.744678 84.744678 0 0 0 39.980469 162.51953 "
        + "A 84.744678 84.744678 0 0 0 124.72461 247.26367 " + "A 84.744678 84.744678 0 0 0 209.46875 162.51953 "
        + "A 84.744678 84.744678 0 0 0 124.72461 77.775391 z " + "M 124.72461 120.8418 "
        + "A 41.67771 41.67771 0 0 1 166.40234 162.51953 " + "A 41.67771 41.67771 0 0 1 124.72461 204.19727 "
        + "A 41.67771 41.67771 0 0 1 83.046875 162.51953 " + "A 41.67771 41.67771 0 0 1 124.72461 120.8418 z";

    // Диагностика: показываем что получилось после оптимизации
    List<Cmd> cmds = new Tokenizer( ring ).tokenize();
    List<Cmd> optimized = optimize( cmds );

    System.out.println( "=== До оптимизации: " + cmds.size() + " команд ===" );
    for( Cmd c : cmds ) {
      System.out.println( "  " + c.type );
    }

    System.out.println( "\n=== После оптимизации: " + optimized.size() + " команд ===" );
    for( Cmd c : optimized ) {
      if( c.type == CmdType.ADD_ARC ) {
        System.out.printf( "  ADD_ARC  cx=%.2f cy=%.2f r=%.2f " + "start=%.1f° extent=%.1f°%n", c.args[0], c.args[1],
            c.args[2], c.args[3], c.args[4] );
      }
      else {
        System.out.println( "  " + c.type );
      }
    }

    Display display = new Display();
    org.eclipse.swt.widgets.Shell shell = new org.eclipse.swt.widgets.Shell( display );
    shell.setText( "SvgArcOptimizer — Demo" );
    shell.setSize( 400, 380 );

    final String pathData = ring;
    shell.addPaintListener( e -> {
      e.gc.setBackground( display.getSystemColor( org.eclipse.swt.SWT.COLOR_WHITE ) );
      e.gc.fillRectangle( shell.getClientArea() );
      e.gc.setAntialias( org.eclipse.swt.SWT.ON );
      e.gc.setLineWidth( 2 );

      Path p = SvgArcOptimizer.parse( display, pathData );
      e.gc.setForeground( display.getSystemColor( org.eclipse.swt.SWT.COLOR_DARK_BLUE ) );
      e.gc.drawPath( p );
      p.dispose();
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
