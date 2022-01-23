package org.toxsoft.tsgui.m5_1.impl;

import static org.toxsoft.tsgui.m5_1.IM5Constants.*;

import java.util.Comparator;

import org.eclipse.swt.graphics.Image;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tsgui.graphics.image.EThumbSize;
import org.toxsoft.tsgui.m5_1.api.*;
import org.toxsoft.tsgui.m5_1.api.helpers.IM5Getter;
import org.toxsoft.tsgui.utils.ITsVisualsProvider;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.tslib.av.opset.impl.OptionSet;
import org.toxsoft.tslib.av.utils.IParameterizedEdit;
import org.toxsoft.tslib.bricks.strid.impl.Stridable;
import org.toxsoft.tslib.bricks.validator.ITsCompoundValidator;
import org.toxsoft.tslib.bricks.validator.impl.TsCompoundValidator;
import org.toxsoft.tslib.coll.basis.ITsCollection;
import org.toxsoft.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.*;

/**
 * {@link IM5FieldDef} implementation.
 * <p>
 * Instance initialization consists of 3 steps:
 * <ul>
 * <li>stage 1 - constructor;</li>
 * <li>stage 2 - {@link #papiSetOwnerModel(IM5Model)} is called after FieldDef is added to model;</li>
 * <li>stage 3 - {@link #papiInitWithDomain()} is called after {@link #papiSetOwnerModel(IM5Model)}, when all FiedlDefs
 * are added to model in method {@link M5Model#addFieldDefs(IM5FieldDef...)} or
 * {@link M5Model#addFieldDefs(ITsCollection)}.</li>
 * </ul>
 *
 * @author hazard157
 * @param <T> - modelled entity type
 * @param <V> - field value type
 */
public abstract class M5FieldDef<T, V>
    extends Stridable
    implements IM5FieldDef<T, V>, IParameterizedEdit {

  class InternalDefaultGetter
      implements IM5Getter<T, V>, ITsVisualsProvider<T> {

    @Override
    public V getFieldValue( T aEntity ) {
      if( aEntity == null ) {
        return defaultValue();
      }
      return doGetFieldValue( aEntity );
    }

    @Override
    public String getName( T aEntity ) {
      // TODO Auto-generated method stub
    }

    @Override
    public String getDescription( T aEntity ) {
      // TODO Auto-generated method stub
    }

    @Override
    public Image getThumb( T aEntity, EThumbSize aThumbSize ) {
      // TODO Auto-generated method stub
    }

    @Override
    public Image getIcon( T aEntity, EIconSize aIconSize ) {
      // TODO Auto-generated method stub
    }

    @Override
    public ITsVisualsProvider<T> visualsProvider() {
      return this;
    }

  }

  private final IOptionSetEdit          params    = new OptionSet();
  private final ITsCompoundValidator<V> validator = TsCompoundValidator.create( true, true );
  private final IStringMapEdit<Object>  valedRefs = new StringMap<>();
  private final IM5Getter<T, V>         getter;

  private Class<V>      valueClass   = null;
  private V             defaultValue = null;
  private IM5Model<T>   ownerModel   = null;
  private Comparator<V> comparator   = null;

  /**
   * Constructor.
   *
   * @param aId String - field ID (IDpath)
   * @param aValueClass {@link Class}&lt;V&gt; - value type
   * @param aGetter {@link IM5Getter} - field value getter and visualizer
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public M5FieldDef( String aId, Class<V> aValueClass, IM5Getter<T, V> aGetter ) {
    super( aId );
    internalSetValueClass( aValueClass );
    if( aGetter == null ) {
      getter = new InternalDefaultGetter();
    }
    else {
      getter = aGetter;
    }
  }

  /**
   * Constructor.
   *
   * @param aId String - field ID (IDpath)
   * @param aValueClass {@link Class}&lt;V&gt; - value type
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public M5FieldDef( String aId, Class<V> aValueClass ) {
    this( aId, aValueClass, null );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void internalSetValueClass( Class<V> aValueClass ) {
    TsNullArgumentRtException.checkNull( aValueClass );
    TsIllegalStateRtException.checkNoNull( valueClass );
    valueClass = aValueClass;
    // if comparator is not set from outside, initialize it with natural comparison
    if( comparator != null && Comparable.class.isAssignableFrom( valueClass ) ) {
      comparator = ( aO1, aO2 ) -> Comparable.class.cast( aO1 ).compareTo( aO2 );
    }
  }

  // ------------------------------------------------------------------------------------
  // PAPI - the package-internal API
  //

  /**
   * Initializes reference when this field definition is added to owner model.
   * <p>
   * This is stage 2 of the field definition initialization process.
   *
   * @param aModel {@link IM5Model} - the owner model
   */
  void papiSetOwnerModel( IM5Model<T> aModel ) {
    TsInternalErrorRtException.checkNoNull( ownerModel );
    ownerModel = aModel;
  }

  /**
   * Наследник может определить дополнительные действия по инициализации описания поля после того, как задан домен, и
   * соответственно, доступен контекст приложения.
   * <p>
   * Внимание: этот метод предназначен только для переопределения внутри библиотеки m5! при переопределеннии нужно
   * вызвать родительский метод или напрямую {@link #doInit()}.
   * <p>
   * This is stage 3 of the field definition initialization process.
   */
  void papiInitWithDomain() {
    doInit();
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IM5FieldDef
  //

  @Override
  public ITsGuiContext tsContext() {
    TsIllegalStateRtException.checkNoNull( ownerModel );
    return ownerModel.domain().tsContext();
  }

  @Override
  public Class<V> valueClass() {
    TsIllegalStateRtException.checkNoNull( valueClass );
    return valueClass;
  }

  @Override
  public IM5Model<T> owninigModel() {
    return ownerModel;
  }

  @Override
  public V getFieldValue( T aEntity ) {
  }

  @Override
  public int hints() {
    return M5_OPDEF_HINTS.getValue( params ).asInt();
  }

  @Override
  public IStringMapEdit<Object> valedRefs() {
    return valedRefs;
  }

  @Override
  public V getFieldValue( IM5Bunch<T> aBunch ) {
    TsNullArgumentRtException.checkNull( aBunch );
    return aBunch.get( this );
  }

  @Override
  public void setFieldValue( IM5BunchEdit<T> aBunch, V aValue ) {
    TsNullArgumentRtException.checkNull( aBunch );
    aBunch.set( this, aValue );
  }

  @Override
  public V defaultValue() {
    return defaultValue;
  }

  @Override
  public ITsVisualsProvider<T> visualsProvider() {
    return getter.visualsProvider();
  }

  @Override
  public ITsCompoundValidator<V> validator() {
    return validator;
  }

  @Override
  public Comparator<V> comparator() {
    return comparator;
  }

  // ------------------------------------------------------------------------------------
  // Class API
  //

  /**
   * Sets field value comparator.
   *
   * @param aComparator {@link Comparator}&lt;V&gt; - the comparator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setComparator( Comparator<V> aComparator ) {
    TsNullArgumentRtException.checkNull( aComparator );
    comparator = aComparator;
  }

  /**
   * Sets {@link #defaultValue()}.
   *
   * @param aValue &lt;V&gt; - the defaul value, may be <code>null</code>
   */
  public void setDefaultValue( V aValue ) {
    defaultValue = aValue;
  }

  /**
   * Sets {@link #hints()} value.
   *
   * @param aHints int - new hints
   */
  public void setHints( int aHints ) {
    params.setInt( M5_OPDEF_HINTS, aHints );
  }

  /**
   * Adds specified hints flags.
   *
   * @param aHints int - the hits to be added
   */
  public void addHints( int aHints ) {
    int hints = hints() | aHints;
    setHints( hints );
  }

  /**
   * Removes the specified hints flags.
   *
   * @param aHints int - the hints to be removed
   */
  public void removeHints( int aHints ) {
    int hints = hints() & (~aHints);
    setHints( hints );
  }

  // ------------------------------------------------------------------------------------
  // Subclass overrides
  //

  /**
   * Subclass may perform additional initialization from {@link #papiInitWithDomain()}.
   * <p>
   * Method does nothing in base class. No need to call the parent method from subclass.
   */
  protected void doInit() {
    // nop
  }

  /**
   * Subclass may override text returned as {@link ITsVisualsProvider#getName(Object)} for <code>null</code> entity.
   * <p>
   * Method returns an empty string in base class. No need to call the parent method from subclass.
   * <p>
   * Note: this method is called only for default visualiser.
   *
   * @return String - текст, отображаемый для null объекта, не должен быть null
   */
  protected String doGetNullEntityFieldValueName() {
    return TsLibUtils.EMPTY_STRING;
  }

  /**
   * Наследник должен вернуть значение поля.
   * <p>
   * Метод может возвращать <code>null</code>, но этого надо всячески избегать. По большому счету, <code>null</code>
   * возвращается тогда, когда поле является единичной ссылкой на другой объект, но ссылка не задана. Для полей же типа
   * {@link IAtomicValue} нужно возвращаеть {@link IAtomicValue#NULL}, а для полей-коллекции - пустые коллекции.
   *
   * @param aEntity &lt;T&gt; - экземпляр моделированого объекта, не бывает null
   * @return &lt;V&gt; - значение поля, может быть null
   */
  protected abstract V doGetFieldValue( T aEntity );

}
