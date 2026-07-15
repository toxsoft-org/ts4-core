package org.toxsoft.core.tsgui.ved.comps.AI.SVG;

/** Прямоугольник: &lt;rect x y width height rx ry&gt; */
public class SvgRect
    extends SvgShape {

  private double x, y, width, height, rx, ry;

  public SvgRect() {
    super( "rect" );
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  public double getRx() {
    return rx;
  }

  public double getRy() {
    return ry;
  }

  public void setX( double x ) {
    this.x = x;
  }

  public void setY( double y ) {
    this.y = y;
  }

  public void setWidth( double width ) {
    this.width = width;
  }

  public void setHeight( double height ) {
    this.height = height;
  }

  public void setRx( double rx ) {
    this.rx = rx;
  }

  public void setRy( double ry ) {
    this.ry = ry;
  }

  @Override
  public String toString() {
    return super.toString() + String.format( " x=%.1f y=%.1f w=%.1f h=%.1f", x, y, width, height );
  }
}
