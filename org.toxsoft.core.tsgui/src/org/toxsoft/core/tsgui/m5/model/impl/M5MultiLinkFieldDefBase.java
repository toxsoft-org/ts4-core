package org.toxsoft.core.tsgui.m5.model.impl;

import static org.toxsoft.core.tsgui.m5.model.impl.ITsResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.helpers.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Basic implementation of a multi-field containing other modeled entities.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 * @param <V> - list element type (not collection type!)
 */
public class M5MultiLinkFieldDefBase<T, V>
    extends M5FieldDef<T, IList<V>>
    implements IM5MixinModelledField<V>, IM5MixinMultiLinkField {

  private String      itemModelId;
  private int         maxCount   = 0;
  private boolean     exactCount = false;
  private IM5Model<V> itemModel  = null;

  /**
   * Constructor.
   *
   * @param aId String - field ID (IDpath)
   * @param aItemModelId String - M5-model ID of the {@link #itemModel()}
   * @param aGetter {@link IM5Getter} - field value getter and visualizer or <code>null</code> for default behaviour
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public M5MultiLinkFieldDefBase( String aId, String aItemModelId, IM5Getter<T, IList<V>> aGetter ) {
    super( aId, (Class)IList.class, aGetter );
    itemModelId = StridUtils.checkValidIdPath( aItemModelId );
  }

  /**
   * Constructor.
   *
   * @param aId String - field ID (IDpath)
   * @param aItemModelId String - M5-model ID of the {@link #itemModel()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public M5MultiLinkFieldDefBase( String aId, String aItemModelId ) {
    this( aId, aItemModelId, null );
  }

  // ------------------------------------------------------------------------------------
  // implementation
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
  // to override
  //

  /**
   * Allows to redefine the model identifier of linked objects {@link #itemModel()}.
   * <p>
   * Called once, the first time the {@link #itemModel()} method is called, which finds the model using the identifier
   * specified by this method.
   * <p>
   * In the base class, returns the identifier specified in the constructor.
   *
   * @return String - M5-model ID of the {@link #itemModel()}
   */
  protected String specifyItemModelId() {
    return itemModelId;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets limits on the number of linked objects.
   *
   * @param aMaxCount int - maximum number of linked objects, or 0 for no limit
   * @param aIsExactCount boolean - a sign for specifying the exact number of objects
   * @throws TsIllegalArgumentRtException aMaxCount < 0
   */
  public void setCountProps( int aMaxCount, boolean aIsExactCount ) {
    TsIllegalArgumentRtException.checkTrue( aMaxCount < 0 );
    maxCount = aMaxCount;
    exactCount = aIsExactCount;
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
  // IM5MixinMultiLinkField
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
