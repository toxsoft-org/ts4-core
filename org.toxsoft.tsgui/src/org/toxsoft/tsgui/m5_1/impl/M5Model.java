package org.toxsoft.tsgui.m5_1.impl;

import static org.toxsoft.tsgui.m5_1.impl.ITsResources.*;

import org.toxsoft.tsgui.m5_1.api.*;
import org.toxsoft.tsgui.m5_1.api.helpers.IM5ValuesCache;
import org.toxsoft.tslib.bricks.strid.coll.*;
import org.toxsoft.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.tslib.bricks.strid.coll.impl.StridablesListReorderer;
import org.toxsoft.tslib.bricks.strid.impl.Stridable;
import org.toxsoft.tslib.coll.basis.ITsCollection;
import org.toxsoft.tslib.coll.helpers.IListReorderer;
import org.toxsoft.tslib.utils.errors.*;

/**
 * {@link IM5Model} implementation.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public class M5Model<T>
    extends Stridable
    implements IM5Model<T> {

  private final IStridablesListEdit<IM5FieldDef<T, ?>>      fieldDefs = new StridablesList<>();
  private final IStridablesListReorderer<IM5FieldDef<T, ?>> fieldsReorderer;

  private IM5ValuesCache<T> cache;

  private IM5Domain domain      = null;
  private Class<T>  entityClass = null;

  private IM5LifecycleManager<T>   defaultLifecycleManager = null;
  private M5DefaultPanelCreator<T> panelCreator            = null;

  /**
   * Constructor.
   *
   * @param aId String - model ID
   * @param aEntityClass {@link Class}&lt;T&gt; - modelled entity type
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsItemAlreadyExistsRtException model with specified ID already exists in domain
   */
  public M5Model( String aId, Class<T> aEntityClass ) {
    super( aId );
    entityClass = TsNullArgumentRtException.checkNull( aEntityClass );
    cache = new M5DefaultValuesCache<>( this );
    fieldsReorderer = new StridablesListReorderer<>( fieldDefs );
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  void setDomain( IM5Domain aDomain ) {
    if( domain != null ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_MODEL_ALREADY_INITED, id(), domain.id() );
    }
    domain = aDomain;
    // встроенная инициализация описаний полей
    for( IM5FieldDef<T, ?> fdef : fieldDefs ) {
      ((M5FieldDef<T, ?>)fdef).papiInitWithDomain();
    }
    // дополнительная инициализация модели
    doInit();
  }

  // ------------------------------------------------------------------------------------
  // IM5Model
  //

  @Override
  public Class<T> entityClass() {
    return entityClass;
  }

  @Override
  public IStridablesList<IM5FieldDef<T, ?>> fieldDefs() {
    return fieldDefs;
  }

  @Override
  public boolean isModelledObject( Object aObj ) {
    if( aObj == null ) {
      return false;
    }
    if( !entityClass.isInstance( aObj ) ) {
      return false;
    }
    return doCheckIsModelledClass( entityClass.cast( aObj ) );
  }

  @Override
  public IM5Bunch<T> valuesOf( T aObj ) {
    return cache.getValues( aObj );
  }

  @Override
  public IM5Domain domain() {
    TsIllegalStateRtException.checkNull( domain );
    return domain;
  }

  @Override
  public IM5LifecycleManager<T> createLifecycleManager( Object aMaster ) {
    if( aMaster == null ) {
      IM5LifecycleManager<T> defLm = getDefaultLifecycleManager();
      TsIllegalArgumentRtException.checkTrue( defLm == null, FMT_ERR_NO_DEF_MANAGER_FOR_NULL, id() );
      return defLm;
    }
    IM5LifecycleManager<T> lm = doCreateLifecycleManager( aMaster );
    TsInternalErrorRtException.checkNull( lm );
    return lm;
  }

  @Override
  public IM5LifecycleManager<T> getDefaultLifecycleManager() {
    if( defaultLifecycleManager == null ) {
      defaultLifecycleManager = doCreateDefaultLifecycleManager();
    }
    return defaultLifecycleManager;
  }

  @Override
  public IM5PanelCreator<T> panelCreator() {
    if( panelCreator == null ) {
      TsIllegalStateRtException.checkNull( domain );
      panelCreator = new M5DefaultPanelCreator<>();
      panelCreator.setOwnerModel( this );
    }
    return panelCreator;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Задает имя и описание сущности.
   *
   * @param aName String - имя сущности
   * @param aDescription String - описание сущности
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  @Override
  public void setNameAndDescription( String aName, String aDescription ) {
    super.setNameAndDescription( aName, aDescription );
  }

  /**
   * Добавляет описания полей.
   *
   * @param aFieldDefs {@link IM5FieldDef}[] - описания добавляемых полей
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException какое-либо поле уже привязано к модели
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public void addFieldDefs( IM5FieldDef... aFieldDefs ) {
    TsErrorUtils.checkArrayArg( aFieldDefs );
    for( IM5FieldDef<T, ?> fdef : aFieldDefs ) {
      ((M5FieldDef<T, ?>)fdef).papiSetOwnerModel( this );
    }
    fieldDefs.addAll( aFieldDefs );
    if( domain != null ) {
      for( IM5FieldDef<T, ?> fdef : aFieldDefs ) {
        ((M5FieldDef<T, ?>)fdef).papiInitWithDomain();
      }
    }
  }

  /**
   * Returns the means to change fields oreder.
   *
   * @return {@link IListReorderer}&lt;{@link IM5FieldDef}&gt; - the fields reorderer
   */
  public IStridablesListReorderer<IM5FieldDef<T, ?>> fieldsReorderer() {
    return fieldsReorderer;
  }

  /**
   * Добавляет описания полей.
   *
   * @param aFieldDefs {@link ITsCollection}&lt;{@link IM5FieldDef}&gt; - описания добавляемых полей
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException какое-либо поле уже привязано к модели
   */
  public void addFieldDefs( ITsCollection<IM5FieldDef<T, ?>> aFieldDefs ) {
    TsNullArgumentRtException.checkNull( aFieldDefs );
    for( IM5FieldDef<T, ?> fdef : aFieldDefs ) {
      ((M5FieldDef<T, ?>)fdef).papiSetOwnerModel( this );
    }
    fieldDefs.addAll( aFieldDefs );
    if( domain != null ) {
      for( IM5FieldDef<T, ?> fdef : aFieldDefs ) {
        ((M5FieldDef<T, ?>)fdef).papiInitWithDomain();
      }
    }
  }

  /**
   * Задает создатель предопределенных панелей.
   *
   * @param aCreator {@link M5DefaultPanelCreator} - создатель панелей
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setPanelCreator( M5DefaultPanelCreator<T> aCreator ) {
    TsNullArgumentRtException.checkNull( aCreator );
    panelCreator = aCreator;
    aCreator.setOwnerModel( this );
  }

  /**
   * Задает способ кеширования, используемы методом {@link #valuesOf(Object)}.
   * <p>
   * При создании модели задается способ кеширования по умолчанию.
   *
   * @param aCache {@link IM5ValuesCache} - списо кеширования или <code>null</code> для способа по умолчанию
   */
  public void setCache( IM5ValuesCache<T> aCache ) {
    if( aCache != null ) {
      cache = aCache;
    }
    else {
      cache = new M5DefaultValuesCache<>( this );
    }
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения наследниками
  //

  /**
   * Наследник может провести дополнительную инициализацию модели, когда уже доступен домен.
   * <p>
   * В базовом классе ничего не делает, при переопределении вызывать родительский метод не нужно.
   */
  protected void doInit() {
    // nop
  }

  /**
   * Наследники обязаны переопределить и проверить объект на принадлежность модели, если проверки на Java-тип
   * недостаточно.
   * <p>
   * Этот метод используется в {@link #isModelledObject(Object)} после проверки на {@link Class#isInstance(Object)}.
   *
   * @param aObj &lt;T&gt; - проверяемый объект, не бывет null, и естественно, звестно, что java-типа &lt;T&gt;
   * @return boolean - признак, что этот модель применим при моделировании аргумента<br>
   *         <b>true</b> - aObj моделируется этой моделью;<br>
   *         <b>false</b> - aObj нельзя обрабатывать этой моделью.
   */
  protected boolean doCheckIsModelledClass( T aObj ) {
    return true;
  }

  protected IM5LifecycleManager<T> doCreateDefaultLifecycleManager() {
    return null;
  }

  protected IM5LifecycleManager<T> doCreateLifecycleManager( @SuppressWarnings( "unused" ) Object aMaster ) {
    throw new TsUnsupportedFeatureRtException( FMT_ERR_NO_LM_CREATOR_CODE, id() );
  }

}
