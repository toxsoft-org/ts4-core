package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * Преобразователь координат основанный на матрице коэффициентов аффиннного преобразования.
 * <p>
 *
 * @author vs
 */

public class D2MatrixConverterNew {

  /**
   * Преобразует параметры преобразования в трансформацию экрана редактирования.
   *
   * @param aD2Conv {@link ID2Conversion}
   * @param aRotX double - координата x центра поворота
   * @param aRotY double - координата y центра поворота
   * @return {@link VedAffineTransform} - трансформацию для экрана редактирования
   */
  public static VedAffineTransform d2convToTransform( ID2Conversion aD2Conv, double aRotX, double aRotY ) {
    VedAffineTransform at = new VedAffineTransform();
    at = transform( at, aD2Conv, aRotX, aRotY );
    return at;
  }

  /**
   * Ghtj,hfpetn
   *
   * @param aTransform
   * @param aD2Conv
   * @param aRotX
   * @param aRotY
   * @return
   */
  public static VedAffineTransform transform( VedAffineTransform aTransform, ID2Conversion aD2Conv, double aRotX,
      double aRotY ) {
    VedAffineTransform at = aTransform;
    at.translate( aD2Conv.origin().x(), aD2Conv.origin().y() );
    at.rotate( -aD2Conv.rotation().radians(), aRotX, aRotY ); // Sol+- изменение знака угла поворота
    at.scale( aD2Conv.zoomFactor(), aD2Conv.zoomFactor() );
    return at;
  }

  public VedAffineTransform getItemTransfrom( Transform aScreenTransform, ID2Conversion aD2Conv, double aRotX,
      double aRotY ) {
    float[] tEl = new float[6];
    aScreenTransform.getElements( tEl );

    float[] koeffs = new float[6];
    aScreenTransform.getElements( koeffs );
    VedAffineTransform at = new VedAffineTransform( koeffs );
    transform( at, aD2Conv, aRotX, aRotY );

    return at;
  }

}
