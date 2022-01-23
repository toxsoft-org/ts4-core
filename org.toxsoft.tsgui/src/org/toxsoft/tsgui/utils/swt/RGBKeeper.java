package org.toxsoft.tsgui.utils.swt;

import org.eclipse.swt.graphics.RGB;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

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
