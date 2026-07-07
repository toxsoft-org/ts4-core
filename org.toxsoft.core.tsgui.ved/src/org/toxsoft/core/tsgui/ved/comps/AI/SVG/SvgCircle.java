package org.toxsoft.core.tsgui.ved.comps.AI.SVG;

/** Окружность: &lt;circle cx cy r&gt; */
public class SvgCircle
    extends SvgShape {

  private double cx, cy, r;

  public SvgCircle() {
    super( "circle" );
  }

  public double getCx() {
    return cx;
  }

  public double getCy() {
    return cy;
  }

  public double getR() {
    return r;
  }

  public void setCx( double cx ) {
    this.cx = cx;
  }

  public void setCy( double cy ) {
    this.cy = cy;
  }

  public void setR( double r ) {
    this.r = r;
  }

  @Override
  public String toString() {
    return super.toString() + String.format( " cx=%.1f cy=%.1f r=%.1f", cx, cy, r );
  }
}
