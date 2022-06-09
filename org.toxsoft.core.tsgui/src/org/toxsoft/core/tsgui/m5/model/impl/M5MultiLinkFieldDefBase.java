package org.toxsoft.core.tsgui.m5.model.impl;

import static org.toxsoft.core.tsgui.m5.model.impl.ITsResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.helpers.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Базовая реализация мульти-поля, содержащего моделированные сущности.
 *
 * @author goga
 * @param <T> - тип моделированной сущности
 * @param <V> - тип значения поля
 */
public class M5MultiLinkFieldDefBase<T, V>
    extends M5FieldDef<T, IList<V>>
    implements IM5MixinModelledField<V>, IM5MixinMultiLinkField {

  private String      itemModelId;
  private int         maxCount   = 0;
  private boolean     exactCount = false;
  private IM5Model<V> itemModel  = null;

  /**
   * Конструктор.
   *
   * @param aId String - идентификатор поля (ИД-путь)
   * @param aItemModelId String - идентификатор модели {@link #itemModel()}
   * @param aGetter {@link IM5Getter} - field value getter and visualizer or <code>null</code> for default behaviour
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aId не ИД-пут
   * @throws TsIllegalArgumentRtException aItemModelId не ИД-путь
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public M5MultiLinkFieldDefBase( String aId, String aItemModelId, IM5Getter<T, IList<V>> aGetter ) {
    super( aId, (Class)IList.class, aGetter );
    itemModelId = StridUtils.checkValidIdPath( aItemModelId );
  }

  /**
   * Конструктор.
   *
   * @param aId String - идентификатор поля (ИД-путь)
   * @param aItemModelId String - идентификатор модели {@link #itemModel()}
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aId не ИД-пут
   * @throws TsIllegalArgumentRtException aItemModelId не ИД-путь
   */
  public M5MultiLinkFieldDefBase( String aId, String aItemModelId ) {
    this( aId, aItemModelId, null );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  @SuppressWarnings( { "unchecked" } )
  private void initModel() {
    if( itemModel == null ) {
      itemModelId = specifyItemModelId();
      itemModel = (IM5Model<V>)ownerModel().domain().models().findByKey( itemModelId );
    }
    TsInternalErrorRtException.checkNull( itemModel, FMT_ERR_CANT_FIND_LINK_MODEL, id(), itemModelId );
  }

  // ------------------------------------------------------------------------------------
  // Для переопределения
  //

  /**
   * Дает возможность уточнить идентификатор модели связуемых объектов {@link #itemModel()}.
   * <p>
   * Вызывается один раз, при первом обращении к методу {@link #itemModel()}, который и находит модель по уточненному
   * этим методом идентификатору.
   * <p>
   * В базовом классе возворащает идентификатор, зданный в конструкторе.
   *
   * @return String - уточненный идентификатор модели {@link #itemModel()}
   */
  protected String specifyItemModelId() {
    return itemModelId;
  }

  // ------------------------------------------------------------------------------------
  // API редактирования
  //

  /**
   * Задает параметры количества связанных объектов.
   *
   * @param aMaxCount int - максимально допустимое количество связанных объектов 0 (нет ограничения)
   * @param aIsExactCount boolean - признак указания точного количества объектов
   * @throws TsIllegalArgumentRtException aMaxCount < 0
   */
  public void setCountProps( int aMaxCount, boolean aIsExactCount ) {
    TsIllegalArgumentRtException.checkTrue( aMaxCount < 0 );
    maxCount = aMaxCount;
    exactCount = aIsExactCount;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IM5MixinModelledField
  //

  @Override
  public IM5Model<V> itemModel() {
    initModel();
    return itemModel;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IM5MixinMultiLinkField
  //

  @Override
  public int maxCount() {
    return maxCount;
  }

  @Override
  public boolean isExactCount() {
    return exactCount;
  }

}
