package org.toxsoft.core.tsgui.ved.comps.AI.SVG;

/** Эллипс: &lt;ellipse cx cy rx ry&gt; */
public class SvgEllipse
    extends SvgShape {

  private double cx, cy, rx, ry;

  public SvgEllipse() {
    super( "ellipse" );
  }

  public double getCx() {
    return cx;
  }

  public double getCy() {
    return cy;
  }

  public double getRx() {
    return rx;
  }

  public double getRy() {
    return ry;
  }

  public void setCx( double cx ) {
    this.cx = cx;
  }

  public void setCy( double cy ) {
    this.cy = cy;
  }

  public void setRx( double rx ) {
    this.rx = rx;
  }

  public void setRy( double ry ) {
    this.ry = ry;
  }

  @Override
  public String toString() {
    return super.toString() + String.format( " cx=%.1f cy=%.1f rx=%.1f ry=%.1f", cx, cy, rx, ry );
  }
}
