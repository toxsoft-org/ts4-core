package org.toxsoft.core.tsgui.m5.model.impl;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.model.impl.ITsResources.*;

import java.util.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.wrappers.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base implementation of {@link IM5FieldDef}.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 * @param <V> - field value type
 */
public class M5FieldDef<T, V>
    extends StridableParameterized
    implements IM5FieldDef<T, V> {

  /**
   * Internal implementation of {@link IM5FieldDef#getter} uses user-specified getter if any.
   *
   * @author hazard157
   */
  private class InternalGetter
      implements IM5Getter<T, V> {

    private IM5Getter<T, V> getter = null;

    @Override
    public String getName( T aEntity ) {
      if( aEntity == null ) {
        return doGetNullEntityFieldValueName();
      }
      if( getter != null ) {
        String s = getter.getName( aEntity );
        if( s != null ) {
          return s;
        }
      }
      return doGetFieldValueName( aEntity );
    }

    @Override
    public String getDescription( T aEntity ) {
      if( aEntity == null ) {
        return doGetNullEntityFieldValueDescription();
      }
      if( getter != null ) {
        String s = getter.getDescription( aEntity );
        if( s != null ) {
          return s;
        }
      }
      return doGetFieldValueDescription( aEntity );
    }

    @Override
    public Image getIcon( T aEntity, EIconSize aIconSize ) {
      if( aEntity == null ) {
        return doGetNullEntityFieldValueIcon( aIconSize );
      }
      if( getter != null ) {
        return getter.getIcon( aEntity, aIconSize );
      }
      return doGetFieldValueIcon( aEntity, aIconSize );
    }

    @Override
    public TsImage getThumb( T aEntity, EThumbSize aThumbSize ) {
      if( aEntity == null ) {
        return doGetNullEntityFieldValueThumb( aThumbSize );
      }
      if( getter != null ) {
        return getter.getThumb( aEntity, aThumbSize );
      }
      return doGetFieldValueThumb( aEntity, aThumbSize );
    }

    @Override
    public V getValue( T aEntity ) {
      if( aEntity == null ) {
        return doGetNullEntityFieldValue();
      }
      if( getter != null ) {
        return getter.getValue( aEntity );
      }
      return doGetFieldValue( aEntity );
    }

    void setUserGetter( IM5Getter<T, V> aUserGetter ) {
      getter = aUserGetter;
    }

  }

  private final InternalGetter          internalGetter;
  private final ITsCompoundValidator<V> validator = TsCompoundValidator.create( true, true );
  private final IStringMapEdit<Object>  valedRefs = new StridMapWrapper<>( new StringMap<>() );

  private Class<V>      valueClass   = null;
  private Comparator<V> comparator   = null;
  private IM5Model<T>   ownerModel   = null;
  private int           flags        = 0;
  private V             defaultValue = null;

  /**
   * Constructor.
   *
   * @param aId String - field ID (IDpath)
   * @param aValueClass {@link Class}&lt;V&gt; - value type
   * @param aGetter {@link IM5Getter} - field value getter and visualizer or <code>null</code> for default behaviour
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public M5FieldDef( String aId, Class<V> aValueClass, IM5Getter<T, V> aGetter ) {
    super( aId );
    internalSetValueClass( aValueClass );
    internalGetter = new InternalGetter();
    internalGetter.setUserGetter( aGetter );
  }

  /**
   * Constructor with default getter.
   *
   * @param aId String - field ID (IDpath)
   * @param aValueClass {@link Class}&lt;V&gt; - value type
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public M5FieldDef( String aId, Class<V> aValueClass ) {
    this( aId, aValueClass, null );
  }

  /**
   * Constructor for descendants not specifying value class.
   *
   * @param aId String - field ID (IDpath)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public M5FieldDef( String aId ) {
    super( aId );
    internalGetter = new InternalGetter();
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  protected final void internalSetValueClass( Class<V> aValueClass ) {
    TsNullArgumentRtException.checkNull( aValueClass );
    TsIllegalStateRtException.checkNoNull( valueClass );
    valueClass = aValueClass;
    comparator = TsLibUtils.makeNaturalComparator( valueClass );
  }

  // ------------------------------------------------------------------------------------
  // Package API
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
    if( flags == 0 ) { // probably flags was specified if not 0
      setFlags( M5_OPDEF_FLAGS.getValue( params() ).asInt() );
    }
    doInit();
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    TsIllegalStateRtException.checkNull( ownerModel );
    return ownerModel.domain().tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IM5FieldDef
  //

  @Override
  public Class<V> valueClass() {
    return valueClass;
  }

  @Override
  final public IM5Model<T> ownerModel() {
    return ownerModel;
  }

  @Override
  final public int flags() {
    return flags;
  }

  @Override
  public IStringMapEdit<Object> valedRefs() {
    return valedRefs;
  }

  @Override
  final public IM5Getter<T, V> getter() {
    return internalGetter;
  }

  @Override
  public V defaultValue() {
    return defaultValue;
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
   * Sets field value getter and visualizer.
   *
   * @param aGetter {@link IM5Getter} - getter or <code>null</code> for default behaviour
   */
  public void setGetter( IM5Getter<T, V> aGetter ) {
    internalGetter.setUserGetter( aGetter );
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
   * Sets name {@link #nmName()} and {@link #description()}.
   *
   * @param aName String - the name
   * @param aDescription String - the description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @Override
  public void setNameAndDescription( String aName, String aDescription ) {
    super.setNameAndDescription( aName, aDescription );
  }

  /**
   * Set the user specified behavior to extract field value and provide visualization.
   * <p>
   * Please not fillofing restrictions on user-specified getter usage:
   * <ul>
   * <li>user-specified getter methods will be called only with non-<code>null</code> arguments;</li>
   * <li>user-specified getter will be used only for non-<code>null</code> entities. Defaults for <code>null</code>
   * entity is defined by this field definition;</li>
   * <li>this method does not changes the internal getter returned by {@link IM5FieldDef#getter()}.</li>
   * </ul>
   *
   * @param aUserGetter {@link IM5Getter}&lt;T,V&gt; - the getter or <code>null</code> for default behaviour
   */
  public void setUserGetter( IM5Getter<T, V> aUserGetter ) {
    internalGetter.setUserGetter( aUserGetter );
  }

  /**
   * Sets {@link #flags()} value.
   *
   * @param aFlags int - new flags
   */
  public void setFlags( int aFlags ) {
    flags = aFlags;
  }

  /**
   * Adds specified flags flags.
   *
   * @param aFlags int - the hits to be added
   */
  public void addFlags( int aFlags ) {
    flags = flags | aFlags;
  }

  /**
   * Removes the specified flags flags.
   *
   * @param aFlags int - the flags to be removed
   */
  public void removeFlags( int aFlags ) {
    flags = flags & (~aFlags);
  }

  /**
   * Sets field editor {@link IValedControl}.
   *
   * @param aFactoryName String - the valed editor factory name
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is a blank string
   */
  public void setValedEditor( String aFactoryName ) {
    TsErrorUtils.checkNonBlank( aFactoryName );
    params().setStr( IValedControlConstants.OPID_EDITOR_FACTORY_NAME, aFactoryName );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return id() + "\nparams = " + OptionSetUtils.humanReadable( params() ); //$NON-NLS-1$
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  // ------------------------------------------------------------------------------------
  // initialization

  /**
   * Subclass may perform additional initialization from {@link #papiInitWithDomain()}.
   * <p>
   * Method does nothing in base class. No need to call the parent method from subclass.
   */
  protected void doInit() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // defaults for null entity visualization

  /**
   * Subclass may change the value returned as {@link IM5Getter#getValue(Object)} for <code>null</code> entity.
   * <p>
   * In base class returns an {@link #defaultValue()}, there is no need to call superclass method when overriding.
   * <p>
   * This method may return <code>null</code> in a case as described in commmens of the method
   * {@link #doGetFieldValue(Object)},
   *
   * @see #doGetFieldValue(Object)
   * @return &lt;V&gt; - value as field value for <code>null</code> entity, may be <code>null</code>
   */
  protected V doGetNullEntityFieldValue() {
    return defaultValue;
  }

  /**
   * Subclass may change the string returned as {@link IM5Getter#getName(Object)} for <code>null</code> entity.
   * <p>
   * In base class returns an empty string, there is no need to call superclass method when overriding.
   *
   * @return String - text to display as field value for <code>null</code> entity, must not be <code>null</code>
   */
  protected String doGetNullEntityFieldValueName() {
    return TsLibUtils.EMPTY_STRING;
  }

  /**
   * Subclass may change the string returned as {@link IM5Getter#getDescription(Object)} for <code>null</code> entity.
   * <p>
   * In base class returns an empty string, there is no need to call superclass method when overriding.
   *
   * @return String - text to display as field description for <code>null</code> entity, must not be <code>null</code>
   */
  protected String doGetNullEntityFieldValueDescription() {
    return TsLibUtils.EMPTY_STRING;
  }

  /**
   * Subclass may change the image returned as {@link IM5Getter#getIcon(Object, EIconSize)} for <code>null</code>
   * entity.
   * <p>
   * In base class returns <code>null</code>, there is no need to call superclass method when overriding.
   *
   * @param aIconSize {@link EIconSize} - requested image size
   * @return {@link Image} - image to display as field icon for <code>null</code> entity, may be <code>null</code>
   */
  protected Image doGetNullEntityFieldValueIcon( EIconSize aIconSize ) {
    return null;
  }

  /**
   * Subclass may change the image returned as {@link IM5Getter#getThumb(Object, EThumbSize)} for <code>null</code>
   * entity.
   * <p>
   * In base class returns <code>null</code>, there is no need to call superclass method when overriding.
   *
   * @param aThumbSize {@link EThumbSize} - requested image size
   * @return {@link TsImage} - image to display as field thumbnail for <code>null</code> entity, may be
   *         <code>null</code>
   */
  protected TsImage doGetNullEntityFieldValueThumb( EThumbSize aThumbSize ) {
    return null;
  }

  // ------------------------------------------------------------------------------------
  // non-null entity visualisation

  /**
   * Subclass must return the string returned as {@link IM5Getter#getName(Object)} for the entity.
   * <p>
   * In base class throws an exception, never to call superclass method when overriding.
   *
   * @param aEntity &lt;T&gt; - the entity, never is <code>null</code>
   * @return String - text to display as field value
   */
  protected String doGetFieldValueName( T aEntity ) {
    throw new TsInternalErrorRtException( FMT_ERR_NO_TEXT_GETTER_CODE, ownerModel.id(), id() );
  }

  /**
   * Subclass may define the string returned as {@link IM5Getter#getDescription(Object)} for the entity.
   * <p>
   * In base class returns an empty string, there is no need to call superclass method when overriding.
   *
   * @param aEntity &lt;T&gt; - the entity, never is <code>null</code>
   * @return String - text to display as description to the field value
   */
  protected String doGetFieldValueDescription( T aEntity ) {
    return TsLibUtils.EMPTY_STRING;
  }

  /**
   * Subclass may define the image returned as {@link IM5Getter#getIcon(Object, EIconSize)} for the entity.
   * <p>
   * In base class returns <code>null</code>, there is no need to call superclass method when overriding.
   *
   * @param aEntity &lt;T&gt; - the entity, never is <code>null</code>
   * @param aIconSize {@link EIconSize} - requested image size
   * @return {@link Image} - image to display as field icon for the entity, may be <code>null</code>
   */
  protected Image doGetFieldValueIcon( T aEntity, EIconSize aIconSize ) {
    return null;
  }

  /**
   * Subclass may define the image returned as {@link IM5Getter#getThumb(Object, EThumbSize)} for the entity.
   * <p>
   * In base class returns <code>null</code>, there is no need to call superclass method when overriding.
   *
   * @param aEntity &lt;T&gt; - the entity, never is <code>null</code>
   * @param aThumbSize {@link EThumbSize} - requested image size
   * @return {@link TsImage} - image to display as field thumbnail for the entity, may be <code>null</code>
   */
  protected TsImage doGetFieldValueThumb( T aEntity, EThumbSize aThumbSize ) {
    return null;
  }

  /**
   * Subclass must return the field value if no getter is specified by user.
   * <p>
   * This method is called if non-<code>null</code> getter was not specified is specified by user in
   * {@link M5FieldDef#M5FieldDef(String, Class, IM5Getter)} or {@link #setUserGetter(IM5Getter)}.
   * <p>
   * In base class throws an exception, never call superclass method when overriding.
   * <p>
   * As defined by M5 modeling specification <code>null</code> may be returned only for simple single reference fields
   * with no value specified. For collection/array fields must return an empty collection/array. For any value types
   * &lt;V&gt; with special-case objects defined must return non-<code>null</code> special-case NULL singleton. Most
   * significant case is atomic value fields - this method must return {@link IAtomicValue#NULL} instead of
   * <code>null</code>.
   *
   * @param aEntity &lt;T&gt; - the entity, never is <code>null</code>
   * @return &lt;V&gt; - the field value, may be <code>null</code> (see comments above)
   */
  protected V doGetFieldValue( T aEntity ) {
    throw new TsInternalErrorRtException( FMT_ERR_NO_VALUE_GETTER_CODE, ownerModel.id(), id() );
  }

}
