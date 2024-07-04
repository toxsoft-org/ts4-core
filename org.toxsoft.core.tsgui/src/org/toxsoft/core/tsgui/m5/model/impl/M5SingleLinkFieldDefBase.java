package org.toxsoft.core.tsgui.m5.model.impl;

import static org.toxsoft.core.tsgui.m5.model.impl.ITsResources.*;

import java.util.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.helpers.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base implementation for all single lookup field defs.
 * <p>
 * Note: {@link #valueClass} from base class is not used in this implementation. Rather {@link #valueClass()} returns
 * class from lookup items M5-model.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 * @param <V> - field value type
 */
public class M5SingleLinkFieldDefBase<T, V>
    extends M5FieldDef<T, V>
    implements IM5MixinModelledField<V>, IM5MixinSingleLinkField {

  private String      itemModelId;
  private IM5Model<V> itemModel         = null;
  private boolean     userCanSelectNull = false;

  /**
   * Construcor.
   * <p>
   * {@link #canUserSelectNull()} defaults to <code>false</code>.
   *
   * @param aId String - field ID
   * @param aItemModelId String - ID of lookup items model {@link #itemModel()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument is not an IDpath
   */
  public M5SingleLinkFieldDefBase( String aId, String aItemModelId ) {
    super( aId );
    itemModelId = StridUtils.checkValidIdPath( aItemModelId );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  @SuppressWarnings( { "unchecked" } )
  private void initModel() {
    if( itemModel == null ) {
      itemModelId = specifyItemModelId();
      itemModel = (IM5Model<V>)ownerModel().domain().models().findByKey( itemModelId );
      TsInternalErrorRtException.checkNull( itemModel, FMT_ERR_CANT_FIND_LINK_MODEL, id(), itemModelId );
      internalSetValueClass( itemModel.entityClass() );
    }
  }

  // ------------------------------------------------------------------------------------
  // Методы базового класса
  //

  @Override
  protected String doGetFieldValueName( T aEntity ) {
    return itemModel().visualsProvider().getName( getFieldValue( aEntity ) );
  }

  @Override
  protected String doGetFieldValueDescription( T aEntity ) {
    return itemModel().visualsProvider().getDescription( getFieldValue( aEntity ) );
  }

  @Override
  protected TsImage doGetFieldValueThumb( T aEntity, EThumbSize aThumbSize ) {
    return itemModel().visualsProvider().getThumb( getFieldValue( aEntity ), aThumbSize );
  }

  @Override
  protected Image doGetFieldValueIcon( T aEntity, EIconSize aIconSize ) {
    return itemModel().visualsProvider().getIcon( getFieldValue( aEntity ), aIconSize );
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
   * В базовом классе возворащает идентификатор, зданный в конструкторе
   * {@link M5SingleLinkFieldDefBase#M5SingleLinkFieldDefBase(String, String)}, при переопределении вызвывать метод
   * родительского класса не обязательно.
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
   * Задает признак {@link #canUserSelectNull()}.
   *
   * @param aFlag boolean - признак выбираемости отсутствия объекта (допустимости выбора значения <code>null</code>)
   */
  public void setCanUserSelectNull( boolean aFlag ) {
    userCanSelectNull = aFlag;
  }

  /**
   * Задает идентификатор модели {@link #itemModel()}.
   *
   * @param aItemModelId String - идентификатор модели {@link #itemModel()}
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException аргумент не ИД-путь
   */
  public void setItemModelId( String aItemModelId ) {
    itemModelId = StridUtils.checkValidIdPath( aItemModelId );
  }

  // ------------------------------------------------------------------------------------
  // IM5FieldDef
  //

  @Override
  public Class<V> valueClass() {
    initModel();
    return itemModel().entityClass();
  }

  @Override
  public Comparator<V> comparator() {
    return itemModel().comparator();
  }

  // ------------------------------------------------------------------------------------
  // IM5MixinModelledField
  //

  @Override
  public IM5Model<V> itemModel() {
    initModel();
    return itemModel;
  }

  // ------------------------------------------------------------------------------------
  // IM5MixinSingleLinkField
  //

  @Override
  public boolean canUserSelectNull() {
    return userCanSelectNull;
  }

}
