package org.toxsoft.tsgui.utils.swt;

import org.eclipse.swt.graphics.RGBA;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

/**
 * Хранитель объектов типа {@link RGBA} в текстовое представление.
 *
 * @author hazard157
 */
public class RGBAKeeper
    extends AbstractEntityKeeper<RGBA> {

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "RGBA"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<RGBA> KEEPER = new RGBAKeeper();

  private RGBAKeeper() {
    super( RGBA.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, RGBA aEntity ) {
    aSw.writeInt( aEntity.rgb.red );
    aSw.writeSeparatorChar();
    aSw.writeInt( aEntity.rgb.green );
    aSw.writeSeparatorChar();
    aSw.writeInt( aEntity.rgb.blue );
    aSw.writeSeparatorChar();
    aSw.writeInt( aEntity.alpha );
  }

  @Override
  protected RGBA doRead( IStrioReader aSr ) {
    int red = aSr.readInt();
    aSr.ensureSeparatorChar();
    int green = aSr.readInt();
    aSr.ensureSeparatorChar();
    int blue = aSr.readInt();
    aSr.ensureSeparatorChar();
    int alpha = aSr.readInt();
    return new RGBA( red, green, blue, alpha );
  }

}
