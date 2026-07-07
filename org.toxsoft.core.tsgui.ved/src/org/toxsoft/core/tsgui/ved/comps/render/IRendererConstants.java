package org.toxsoft.core.tsgui.ved.comps.render;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.ved.incub.*;

/**
 * Константы используемые подсистемой рендеринга.
 *
 * @author vs
 */
@SuppressWarnings( "javadoc" )
public interface IRendererConstants {

  // ------------------------------------------------------------------------------------
  // Circular gauge constants
  //

  String PROPID_OWNER_RADIUS   = "ownerRadius";  //$NON-NLS-1$
  String PROPID_OWNER_ANCHOR_X = "ownerAnchorX"; //$NON-NLS-1$
  String PROPID_OWNER_ANCHOR_Y = "ownerAnchorY"; //$NON-NLS-1$
  String PROPID_START_ANGLE    = "startAngle";   //$NON-NLS-1$
  String PROPID_DELTA_ANGLE    = "deltaAngle";   //$NON-NLS-1$
  String PROPID_ARROW_ANGLE    = "arrowAngle";   //$NON-NLS-1$

  ITinFieldInfo TFI_OWNER_RADIUS = TtiUtils.doubleFieldInfo( PROPID_OWNER_RADIUS, //
      "Радиус датчика", "Радиус датчика", 100 );

  ITinFieldInfo TFI_OWNER_ANCHOR_X = TtiUtils.doubleFieldInfo( PROPID_OWNER_ANCHOR_X, //
      "X координата точки крепления стрелки", "X координата точки крепления стрелки", 50 );

  ITinFieldInfo TFI_OWNER_ANCHOR_Y = TtiUtils.doubleFieldInfo( PROPID_OWNER_ANCHOR_Y, //
      "Y координата точки крепления стрелки", "Y координата точки крепления стрелки", 50 );

  ITinFieldInfo TFI_OWNER_START_ANGLE = TtiUtils.doubleFieldInfo( PROPID_START_ANGLE, //
      "Начальный угол", "Начальный угол в градусах", 50 );

  ITinFieldInfo TFI_OWNER_DELTA_ANGLE = TtiUtils.doubleFieldInfo( PROPID_DELTA_ANGLE, //
      "Приращение угла", "Приращение угла в градусах (+ о часовой стрелке)", 50 );

  ITinFieldInfo TFI_ARROW_ANGLE = TtiUtils.doubleFieldInfo( PROPID_ARROW_ANGLE, //
      "Угол стрелки", "Угол стрелки в градусах", 50 );

}
