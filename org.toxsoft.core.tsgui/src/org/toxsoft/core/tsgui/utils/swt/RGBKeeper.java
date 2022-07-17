package org.toxsoft.core.tsgui.utils.swt;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Хранитель объектов типа {@link RGB} в текстовое представление.
 *
 * @author hazard157
 */
public class RGBKeeper
    extends AbstractEntityKeeper<RGB> {

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "RGB"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<RGB> KEEPER = new RGBKeeper();

  /**
   * Default keeped atomic value has black color.
   */
  public static final IAtomicValue AV_COLOR_BLACK = AvUtils.avValobj( ETsColor.BLACK.rgb(), KEEPER, KEEPER_ID );

  private RGBKeeper() {
    super( RGB.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, RGB aEntity ) {
    aSw.writeInt( aEntity.red );
    aSw.writeSeparatorChar();
    aSw.writeInt( aEntity.green );
    aSw.writeSeparatorChar();
    aSw.writeInt( aEntity.blue );
  }

  @Override
  protected RGB doRead( IStrioReader aSr ) {
    int red = aSr.readInt();
    aSr.ensureSeparatorChar();
    int green = aSr.readInt();
    aSr.ensureSeparatorChar();
    int blue = aSr.readInt();
    return new RGB( red, green, blue );
  }

}
