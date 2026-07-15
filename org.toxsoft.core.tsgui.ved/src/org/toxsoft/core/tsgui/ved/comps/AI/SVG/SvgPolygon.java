package org.toxsoft.core.tsgui.ved.comps.AI.SVG;

/** Многоугольник: &lt;polygon points&gt; */
public class SvgPolygon
    extends SvgShape {

  private String points;

  public SvgPolygon() {
    super( "polygon" );
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
