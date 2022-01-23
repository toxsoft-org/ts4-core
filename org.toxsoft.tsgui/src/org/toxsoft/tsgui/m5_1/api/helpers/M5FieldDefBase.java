package org.toxsoft.tsgui.m5_1.api.helpers;

import java.util.Comparator;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5_1.api.IM5FieldDef;
import org.toxsoft.tsgui.m5_1.api.IM5Model;
import org.toxsoft.tsgui.utils.ITsVisualsProvider;
import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.bricks.strid.impl.Stridable;
import org.toxsoft.tslib.bricks.validator.ITsValidator;
import org.toxsoft.tslib.coll.primtypes.IStringMap;

public class M5FieldDefBase<T, V>
    extends Stridable
    implements IM5FieldDef<T, V> {

  public M5FieldDefBase( String aId ) {
    super( aId );
    // TODO Auto-generated constructor stub
  }

  @Override
  public IOptionSet params() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ITsVisualsProvider<T> visualsProvider() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Class<V> valueClass() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ITsGuiContext tsContext() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IM5Model<T> owninigModel() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int hints() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public IStringMap<Object> valedRefs() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public V getFieldValue( T aEntity ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public V defaultValue() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ITsValidator<V> validator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Comparator<V> comparator() {
    // TODO Auto-generated method stub
    return null;
  }

}
