package org.toxsoft.core.tsgui.graphics.patterns;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Парметры заливки фона изображением.
 * <p>
 *
 * @author vs
 */
public class TsImageFillInfo {

  /**
   * Параметры заливки изображением по-умолчанию.
   */
  public static final TsImageFillInfo DEFAULT = new TsImageFillInfo( EImageFillKind.TILE );

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "TsImageFillInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<TsImageFillInfo> KEEPER =
      new AbstractEntityKeeper<>( TsImageFillInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TsImageFillInfo aEntity ) {
          EImageFillKind.KEEPER.write( aSw, aEntity.kind );
        }

        @Override
        protected TsImageFillInfo doRead( IStrioReader aSr ) {
          EImageFillKind kind = EImageFillKind.KEEPER.read( aSr );
          return new TsImageFillInfo( kind );
        }

      };

  private final EImageFillKind kind;

  /**
   * Конструктор.<br>
   *
   * @param aKind EImageFillKind - тип заливки
   */
  public TsImageFillInfo( EImageFillKind aKind ) {
    kind = aKind;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает тип заполнения.
   *
   * @return EImageFillKind - тип заполнения
   */
  public EImageFillKind kind() {
    return kind;
  }

}
