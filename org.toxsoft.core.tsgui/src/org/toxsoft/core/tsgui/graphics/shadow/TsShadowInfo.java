package org.toxsoft.core.tsgui.graphics.shadow;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

/**
 * Параметры тени, создаваемой путем "размытия" (blur).
 *
 * @author vs
 */
public class TsShadowInfo {

  /**
   * Параметры "тени" по-умолчанию
   */
  public static final TsShadowInfo NONE = new TsShadowInfo( 0, 0, 0, new RGBA( 0, 0, 0, 0 ) );

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "TsShadowInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<TsShadowInfo> KEEPER =
      new AbstractEntityKeeper<>( TsShadowInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TsShadowInfo aEntity ) {
          aSw.writeInt( aEntity.blur );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.xOffset );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.yOffset );
          aSw.writeSeparatorChar();
          aSw.writeQuotedString( RGBAKeeper.KEEPER.ent2str( aEntity.rgba ) );
        }

        @Override
        protected TsShadowInfo doRead( IStrioReader aSr ) {
          int blur = aSr.readInt();
          aSr.ensureSeparatorChar();
          int xOffset = aSr.readInt();
          aSr.ensureSeparatorChar();
          int yOffset = aSr.readInt();
          aSr.ensureSeparatorChar();
          String rgbaStr = aSr.readQuotedString();
          RGBA rgba = RGBAKeeper.KEEPER.str2ent( rgbaStr );
          return new TsShadowInfo( blur, xOffset, yOffset, rgba );
        }

      };

  private final int  blur;
  private final int  xOffset;
  private final int  yOffset;
  private final RGBA rgba;

  /**
   * Constructor.
   *
   * @param aBlur int - радиус размытия
   * @param aXOffset - сдвиг по оси x
   * @param aYOffset int - сдвиг по оси y
   * @param aRgba RGBA - цвет тени
   */
  public TsShadowInfo( int aBlur, int aXOffset, int aYOffset, RGBA aRgba ) {
    blur = aBlur;
    xOffset = aXOffset;
    yOffset = aYOffset;
    rgba = aRgba;
  }

  /**
   * Возвращает радиус размытия тени.
   *
   * @return int - радиус размытия тени
   */
  public int blur() {
    return blur;
  }

  /**
   * Возвращает сдвиг по оси x.
   *
   * @return int - сдвиг по оси x
   */
  public int xOffset() {
    return xOffset;
  }

  /**
   * Возвращает сдвиг по оси y.
   *
   * @return int - сдвиг по оси y
   */
  public int yOffset() {
    return yOffset;
  }

  /**
   * Возвращает цвет тени.
   *
   * @return RGBA - цвет тени
   */
  public RGBA rgba() {
    return rgba;
  }

}
