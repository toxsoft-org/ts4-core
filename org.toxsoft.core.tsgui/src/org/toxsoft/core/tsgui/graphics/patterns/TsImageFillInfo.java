package org.toxsoft.core.tsgui.graphics.patterns;

import org.toxsoft.core.tsgui.graphics.image.*;
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
  public static final TsImageFillInfo DEFAULT = new TsImageFillInfo( TsImageDescriptor.NONE, EImageFillKind.TILE );

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
          TsImageDescriptor.KEEPER.write( aSw, aEntity.imageDescriptor );
          aSw.writeSeparatorChar();
          EImageFillKind.KEEPER.write( aSw, aEntity.kind );
        }

        @Override
        protected TsImageFillInfo doRead( IStrioReader aSr ) {
          TsImageDescriptor imgDescr = TsImageDescriptor.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          EImageFillKind kind = EImageFillKind.KEEPER.read( aSr );
          return new TsImageFillInfo( imgDescr, kind );
        }

      };

  private final EImageFillKind kind;

  private final TsImageDescriptor imageDescriptor;

  /**
   * Конструктор.<br>
   *
   * @param aImgDescr {@link TsImageDescriptor} - the information how to create the TsImage from the different sources
   * @param aKind EImageFillKind - тип заливки
   */
  public TsImageFillInfo( TsImageDescriptor aImgDescr, EImageFillKind aKind ) {
    imageDescriptor = aImgDescr;
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

  /**
   * Возвращает дескриптор изображения.
   *
   * @return {@link TsImageDescriptor} - дескриптор изображения
   */
  public TsImageDescriptor imageDescriptor() {
    return imageDescriptor;
  }

}
