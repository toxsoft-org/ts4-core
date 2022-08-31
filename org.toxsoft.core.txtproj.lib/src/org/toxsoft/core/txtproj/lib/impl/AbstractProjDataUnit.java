package org.toxsoft.core.txtproj.lib.impl;

import org.toxsoft.core.tslib.bricks.events.change.GenericChangeEventer;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeEventer;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.helpers.ECrudOp;
import org.toxsoft.core.tslib.coll.notifier.basis.ITsCollectionChangeListener;
import org.toxsoft.core.tslib.utils.errors.TsIoRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.core.txtproj.lib.IProjDataUnit;

/**
 * Abstract implementation of {@link IProjDataUnit}.
 *
 * @author hazard157
 */
public abstract class AbstractProjDataUnit
    implements IProjDataUnit {

  /**
   * Помощник реализации {@link IGenericChangeEventer} сделан открытым, чтобы в наследниках избежать warning-ы.
   */
  public final GenericChangeEventer genericChangeEventer;

  /**
   * Слушатель-нотификатор для облечения реализации наследников.
   */
  protected final ITsCollectionChangeListener collectionChangeListener = new ITsCollectionChangeListener() {

    @Override
    public void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem ) {
      genericChangeEventer.fireChangeEvent();
    }
  };

  /**
   * Конструктор для наследников.
   */
  protected AbstractProjDataUnit() {
    genericChangeEventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // IKeepableEntity
  //

  @Override
  final public void write( IStrioWriter aDw ) {
    TsNullArgumentRtException.checkNull( aDw );
    doWrite( aDw );
  }

  @Override
  final public void read( IStrioReader aDr ) {
    TsNullArgumentRtException.checkNull( aDr );
    try {
      doRead( aDr );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  final public void clear() {
    try {
      doClear();
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  // ------------------------------------------------------------------------------------
  // IProjDataUnit
  //

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения наследниками
  //

  /**
   * Наслденик должен записать содержимое в поток.
   * <p>
   * <b>Внимание:</b> первым символом (не считая пропуски) должна быть одна из открывающих скобок
   * {@link IStrioHardConstants#CHAR_ARRAY_BEGIN} или {@link IStrioHardConstants#CHAR_SET_BEGIN}. Последним символом (не
   * считая пропусков) должна быть парная к открывающей закрывающая скобка.
   *
   * @param aSw {@link IStrioWriter} - поток записи, не бывает <code>null</code>
   * @throws TsIoRtException при ошибках доступа к потоку
   */
  abstract protected void doWrite( IStrioWriter aSw );

  /**
   * Наследник должен считать содержимое из потока, ранее записанное методом {@link #doWrite(IStrioWriter)}.
   *
   * @param aSr {@link IStrioReader} - поток чтения, не бывает <code>null</code>
   * @throws TsIoRtException при ошибках доступа к потоку
   * @throws StrioRtException при ошибках формата текстового представления
   */
  abstract protected void doRead( IStrioReader aSr );

  /**
   * Implementation must clear the content of the unit as it was immediately after constructor.
   * <p>
   * This method is called from {@link #clear()}.
   */
  abstract protected void doClear();

}
