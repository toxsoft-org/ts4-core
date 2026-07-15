package org.toxsoft.core.tsgui.ved.comps.AI.SVG;

/** Линия: &lt;line x1 y1 x2 y2&gt; */
public class SvgLine
    extends SvgShape {

  private double x1, y1, x2, y2;

  public SvgLine() {
    super( "line" );
  }

  public double getX1() {
    return x1;
  }

  public double getY1() {
    return y1;
  }

  public double getX2() {
    return x2;
  }

  public double getY2() {
    return y2;
  }

  public void setX1( double x1 ) {
    this.x1 = x1;
  }

  public void setY1( double y1 ) {
    this.y1 = y1;
  }

  public void setX2( double x2 ) {
    this.x2 = x2;
  }

  public void setY2( double y2 ) {
    this.y2 = y2;
  }

  @Override
  public String toString() {
    return super.toString() + String.format( " (%.1f,%.1f)→(%.1f,%.1f)", x1, y1, x2, y2 );
  }
}
