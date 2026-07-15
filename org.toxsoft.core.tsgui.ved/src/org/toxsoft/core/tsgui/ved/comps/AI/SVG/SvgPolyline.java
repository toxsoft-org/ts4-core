package org.toxsoft.core.tsgui.ved.comps.AI.SVG;

/** Ломаная линия: &lt;polyline points&gt; */
public class SvgPolyline
    extends SvgShape {

  private String points;

  public SvgPolyline() {
    super( "polyline" );
  }

  public String getPoints() {
    return points;
  }

  public void setPoints( String pts ) {
    this.points = pts;
  }

  @Override
  public String toString() {
    return super.toString() + " points=" + points;
  }
}
