package org.toxsoft.core.tsgui.ved.comps.axis;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Разметка шкалы.
 * <p>
 *
 * @author vs
 */
public class AxisMarking {

  /**
   * Значение по умолчанию
   */
  public final static AxisMarking DEFAULT = new AxisMarking( 5, 10, 1, 5 );

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "AxisMarking"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<AxisMarking> KEEPER =
      new AbstractEntityKeeper<>( AxisMarking.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, AxisMarking aEntity ) {
          aSw.writeInt( aEntity.bigTickQtty );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.bigTickNumber );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.litTickNumber );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.midTickNumber );
        }

        @Override
        protected AxisMarking doRead( IStrioReader aSr ) {
          int btQtty = aSr.readInt();
          aSr.ensureSeparatorChar();
          int btNumber = aSr.readInt();
          aSr.ensureSeparatorChar();
          int ltNumber = aSr.readInt();
          aSr.ensureSeparatorChar();
          int mtNumber = aSr.readInt();
          return new AxisMarking( btQtty, btNumber, ltNumber, mtNumber );
        }
      };

  int bigTickQtty;
  int bigTickNumber;
  int litTickNumber;
  int midTickNumber;

  /**
   * Constructor.
   *
   * @param aBigTickQtty - количество больших засечек, не меньше 2-х
   * @param aBigTickNumber - номер большой засечки, напр. 10 означает, что каждая 10-я засечка большая
   * @param aLittTickNumber - 1 или 0, где 0 - означает отсутствие меленьких засечек
   * @param aMidTickNumber - номер средней засечки, напр. 5 означает, что каждая 5-я засечка средняя
   */
  public AxisMarking( int aBigTickQtty, int aBigTickNumber, int aLittTickNumber, int aMidTickNumber ) {
    TsIllegalArgumentRtException.checkTrue( aBigTickQtty < 2 );
    TsIllegalArgumentRtException.checkTrue( aLittTickNumber != 1 && aLittTickNumber != 0 );
    bigTickQtty = aBigTickQtty;
    bigTickNumber = aBigTickNumber;
    litTickNumber = aLittTickNumber;
    midTickNumber = aMidTickNumber;
  }

  /**
   * Возвращает общее количество засечек шкалы.
   *
   * @return int - общее количество засечек шкалы
   */
  public int ticksQtty() {
    if( litTickNumber == 1 ) {
      return (bigTickQtty - 1) * bigTickNumber;
    }
    if( midTickNumber == 1 ) {
      return (bigTickQtty - 1) * bigTickNumber;
    }
    return bigTickQtty;
  }

  /**
   * Возвращает количество больших засечек на шкале (не меньше 2-х).
   *
   * @return
   */
  public int bigTickQtty() {
    return bigTickQtty;
  }

  public int bigTickNumber() {
    return bigTickNumber;
  }

  public int litTickNumber() {
    return litTickNumber;
  }

  public int midTickNumber() {
    return midTickNumber;
  }

}
