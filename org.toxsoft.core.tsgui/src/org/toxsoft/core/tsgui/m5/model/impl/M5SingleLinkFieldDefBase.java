package org.toxsoft.core.tsgui.m5.model.impl;

import static org.toxsoft.core.tsgui.m5.model.impl.ITsResources.*;

import java.util.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.helpers.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base implementation for all single lookup field defs.
 * <p>
 * Note: {@link #valueClass} from base class is not used in this implementation. Rather {@link #valueClass()} returns
 * class from lookup items M5-model.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 * @param <V> - field value type
 */
public class M5SingleLinkFieldDefBase<T, V>
    extends M5FieldDef<T, V>
    implements IM5MixinModelledField<V>, IM5MixinSingleLinkField {

  private String      itemModelId;
  private IM5Model<V> itemModel         = null;
  private boolean     userCanSelectNull = false;

  /**
   * Constructor.
   * <p>
   * {@link #canUserSelectNull()} defaults to <code>false</code>.
   *
   * @param aId String - field ID
   * @param aItemModelId String - ID of lookup items model {@link #itemModel()}
   * @param aIdsAndValues Object[] - identifier / value pairs of {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument is not an IDpath
   */
  public M5SingleLinkFieldDefBase( String aId, String aItemModelId, Object... aIdsAndValues ) {
    super( aId );
    params().addAll( OptionSetUtils.createOpSet( aIdsAndValues ) );
    itemModelId = StridUtils.checkValidIdPath( aItemModelId );
  }

  // ------------------------------------------------------------------------------------
  // implementation
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
  // M5FieldDef
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

  // ------------------------------------------------------------------------------------
  // To override
  //

  // TODO TRANSLATE

  /**
   * Дает возможность уточнить идентификатор модели связуемых объектов {@link #itemModel()}.
   * <p>
   * Вызывается один раз, при первом обращении к методу {@link #itemModel()}, который и находит модель по уточненному
   * этим методом идентификатору.
   * <p>
   * В базовом классе возворащает идентификатор, зданный в конструкторе
   * {@link M5SingleLinkFieldDefBase#M5SingleLinkFieldDefBase(String, String, Object...)}, при переопределении вызвывать
   * метод родительского класса не обязательно.
   *
   * @return String - уточненный идентификатор модели {@link #itemModel()}
   */
  protected String specifyItemModelId() {
    return itemModelId;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the value of {@link #canUserSelectNull()}.
   *
   * @param aFlag boolean - signs that user can specify the absence of an object, that is to specify <code>null</code>
   */
  public void setCanUserSelectNull( boolean aFlag ) {
    userCanSelectNull = aFlag;
  }

  /**
   * Sets the ID of lookup items model {@link #itemModel()}
   *
   * @param aItemModelId String - {@link #itemModel()} nodel ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an ID-path
   */
  public void setItemModelId( String aItemModelId ) {
    itemModelId = StridUtils.checkValidIdPath( aItemModelId );
  }

}
