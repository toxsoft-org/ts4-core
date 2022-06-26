package org.toxsoft.core.tsgui.ved.olds.api;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.olds.incub.*;

public interface IVedShape {

  boolean hasPoint( int aX, int aY );

  double distanceTo( int aX, int aY );

  IShapeOutline outline();

  Rectangle bounds();

  void locate( double aX, double aY );

  void shiftOn( double aDx, double aDy );

  void setSize( double aWidth, double aHeight );

  void setBounds( double aX, double aY, double aWidth, double aHeight );

  void rotate( /* ??? */ );

  void flip( /* ??? */ );

  void zoom( /* ??? */ );

  void shear( /* ??? */ );

  void transform( /* ??? */ );

}
