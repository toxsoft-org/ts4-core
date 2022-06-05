package org.toxsoft.core.tslib.bricks.keeper.std;

import java.time.LocalTime;

import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * Хранитель объектов типа {@link LocalTime}.
 * <p>
 * Хранит 24-часовом формате, в виде "HH:MM:SS[.mmm]". Если доли секунды равны 0, то миллисекундная часть не пишется.
 * <p>
 * Внимание: время хранится с точностью до миллисекунд, соответственно, после сохранения/загрузки теряеются микро- и
 * нано- секунды. Следовательно, загруженное значение может быть не равно сохраненному.
 *
 * @author hazard157
 */
public class LocalTimeKeeper
    extends AbstractEntityKeeper<LocalTime> {

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "LocalTime"; //$NON-NLS-1$

  /**
   * Синглтон хранителя.
   */
  public static final IEntityKeeper<LocalTime> KEEPER = new LocalTimeKeeper();

  private LocalTimeKeeper() {
    super( LocalTime.class, EEncloseMode.NOT_IN_PARENTHESES, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, LocalTime aEntity ) {
    // OPTIMIZE в случае проблем с производительностью, заменить на методы типа StridWriter.outXxx()
    Integer hh = Integer.valueOf( aEntity.getHour() );
    Integer mm = Integer.valueOf( aEntity.getMinute() );
    Integer ss = Integer.valueOf( aEntity.getSecond() );
    Integer nn = Integer.valueOf( aEntity.getNano() );
    if( nn.intValue() == 0 ) {
      aSw.p( "%02d:%02d:%02d", hh, mm, ss ); //$NON-NLS-1$
    }
    else {
      aSw.p( "%02d:%02d:%02d.%09d", hh, mm, ss, nn ); //$NON-NLS-1$
    }
  }

  @Override
  protected LocalTime doRead( IStrioReader aSr ) {
    int hh = aSr.readInt();
    aSr.ensureSeparatorChar();
    int mm = aSr.readInt();
    aSr.ensureSeparatorChar();
    int ss = aSr.readInt();
    int nn = 0;
    if( aSr.peekChar( EStrioSkipMode.SKIP_NONE ) == '.' ) {
      nn = aSr.readInt();
    }
    return LocalTime.of( hh, mm, ss, nn );
  }

}
