package org.toxsoft.core.unit.txtproj.lib.sinent;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.coll.helpers.ECrudOp;
import org.toxsoft.core.tslib.coll.notifier.basis.ITsCollectionChangeListener;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Абстрактная базовая реализация наследников {@link ISinentity}.
 *
 * @author hazard157
 * @param <F> - тип (класс) информации о сущности
 */
public abstract class AbstractSinentity<F>
    implements ISinentity<F> {

  /**
   * Слушатель изменений в дочерных коллекциях.
   * <p>
   * Предназначена для использования наследниками.
   * <p>
   * Просто делегирует вызов в {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
   */
  protected final ITsCollectionChangeListener childCollChangeListener = new ITsCollectionChangeListener() {

    @Override
    public void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem ) {
      changeHelper().fireChangeEvent();
    }
  };

  protected final GenericChangeEventer eventer;

  private final String id;
  private F            info;

  protected AbstractSinentity( String aId, F aInfo ) {
    eventer = new GenericChangeEventer( this );
    id = StridUtils.checkValidIdPath( aId );
    info = TsNullArgumentRtException.checkNull( aInfo );
  }

  @Override
  public String id() {
    return id;
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследников
  //

  /**
   * Возвращает внутренный помощник работы с извещениями.
   *
   * @return {@link GenericChangeEventer} - внутренный помощник работы с извещениями
   */
  public GenericChangeEventer changeHelper() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

  @Override
  public int compareTo( ISinentity<F> aThat ) {
    return id.compareTo( aThat.id() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ISinentity
  //

  @Override
  public F info() {
    return info;
  }

  @Override
  public void setInfo( F aInfo ) {
    TsNullArgumentRtException.checkNull( aInfo );
    if( !info.equals( aInfo ) ) {
      F oldInfo = info;
      info = aInfo;
      onInfoChanged( aInfo, oldInfo );
      changeHelper().fireChangeEvent();
    }
  }

  @Override
  public IGenericChangeEventer eventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения наследниками
  //

  /**
   * Вызывается при изменении {@link #info()}.
   *
   * @param aNewInfo F - новая информация, не бывает null
   * @param aOldInfo F - старая информация, не бывает null
   */
  protected void onInfoChanged( F aNewInfo, F aOldInfo ) {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return StridUtils.printf( StridUtils.FORMAT_ID_NAME, this );
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( this.getClass().isInstance( aObj ) ) {
      @SuppressWarnings( "unchecked" )
      ISinentity<F> that = (ISinentity<F>)aObj;
      if( id.equals( that.id() ) ) {
        if( nmName().equals( that.nmName() ) && description().equals( that.description() ) ) {
          return info.equals( that.info() );
        }
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + id.hashCode();
    result = TsLibUtils.PRIME * result + nmName().hashCode();
    result = TsLibUtils.PRIME * result + description().hashCode();
    result = TsLibUtils.PRIME * result + info.hashCode();
    return result;
  }

}
