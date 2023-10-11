package org.toxsoft.core.tsgui.m5.model.impl;

import static org.toxsoft.core.tsgui.m5.model.impl.ITsResources.*;

import java.util.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5Model} implementation.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public class M5Model<T>
    extends Stridable
    implements IM5Model<T>, ITsGuiContextable {

  private final IStridablesListEdit<IM5FieldDef<T, ?>>      fieldDefs = new StridablesList<>();
  private final IStridablesListReorderer<IM5FieldDef<T, ?>> fieldsReorderer;

  private final Class<T> entityClass;

  private Comparator<T> comparator = null;

  private IM5LifecycleManager<T> defaultLifecycleManager = null;

  /**
   * Panel creator is lazy initialized in {@link #panelCreator()} or {@link #setPanelCreator(M5DefaultPanelCreator)}.
   */
  private M5DefaultPanelCreator<T> panelCreator = null;

  private ITsVisualsProvider<T> visualsProvider = ITsVisualsProvider.DEFAULT;
  private IM5ValuesCache<T>     cache;                                       // must not be null
  private IM5Domain             domain          = null;

  /**
   * Constructor.
   *
   * @param aId String - model ID
   * @param aEntityClass {@link Class}&lt;T&gt; - modeled entity type
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   */
  public M5Model( String aId, Class<T> aEntityClass ) {
    super( aId );
    entityClass = TsNullArgumentRtException.checkNull( aEntityClass );
    cache = new M5DefaultValuesCache<>( this );
    fieldsReorderer = new StridablesListReorderer<>( fieldDefs );
    comparator = TsLibUtils.makeNaturalComparator( entityClass );
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  void papiSetDomain( IM5Domain aDomain ) {
    if( domain != null ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_MODEL_ALREADY_INITED, id(), domain.id(), aDomain.id() );
    }
    domain = aDomain;
    // initialize field definititions with domain
    for( IM5FieldDef<T, ?> fdef : fieldDefs ) {
      ((M5FieldDef<T, ?>)fdef).papiInitWithDomain();
    }
    // additional whole model initialization
    doInit();
  }

  // ------------------------------------------------------------------------------------
  // ITsContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    TsIllegalStateRtException.checkNull( domain );
    return domain.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IM5Model
  //

  @Override
  public IM5Domain domain() {
    return domain;
  }

  @Override
  final public Class<T> entityClass() {
    return entityClass;
  }

  @Override
  final public IStridablesList<IM5FieldDef<T, ?>> fieldDefs() {
    return fieldDefs;
  }

  @Override
  public boolean isModelledObject( Object aEntity ) {
    if( aEntity == null ) {
      return false;
    }
    if( !entityClass.isInstance( aEntity ) ) {
      return false;
    }
    return doCheckIsModelledClass( entityClass.cast( aEntity ) );
  }

  @Override
  public IM5Bunch<T> valuesOf( T aEntity ) {
    return cache.getValues( aEntity );
  }

  @Override
  public IM5LifecycleManager<T> findLifecycleManager( Object aMaster ) {
    if( aMaster == null ) {
      if( defaultLifecycleManager == null ) {
        defaultLifecycleManager = doCreateDefaultLifecycleManager();
      }
      return defaultLifecycleManager;
    }
    return doCreateLifecycleManager( aMaster );
  }

  @Override
  public ITsVisualsProvider<T> visualsProvider() {
    return visualsProvider;
  }

  @Override
  public Comparator<T> comparator() {
    return comparator;
  }

  @Override
  public M5DefaultPanelCreator<T> panelCreator() {
    if( panelCreator == null ) {
      TsIllegalStateRtException.checkNull( domain );
      panelCreator = new M5DefaultPanelCreator<>();
      panelCreator.papiSetOwnerModel( this );
    }
    return panelCreator;
  }

  // ------------------------------------------------------------------------------------
  // Class API
  //

  @Override
  public void setNameAndDescription( String aName, String aDescription ) {
    super.setNameAndDescription( aName, aDescription );
  }

  /**
   * Sets the caching strategy.
   *
   * @param aCache {@link IM5ValuesCache} - cahing strategy or <code>null</code> for default strategy
   */
  public void setCache( IM5ValuesCache<T> aCache ) {
    if( aCache != null ) {
      cache = aCache;
    }
    else {
      cache = new M5DefaultValuesCache<>( this );
    }
    cache.clear();
  }

  /**
   * Sets {@link #visualsProvider()}.
   *
   * @param aVisualsProvider {@link ITsVisualsProvider}&lt;&gt; - whole entity visuals provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setVisualsProvider( ITsVisualsProvider<T> aVisualsProvider ) {
    TsNullArgumentRtException.checkNull( aVisualsProvider );
    visualsProvider = aVisualsProvider;
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
   * Adds field definitions to the end of {@link #fieldDefs()}.
   *
   * @param aFieldDefs {@link IM5FieldDef}[] - field definitions to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any field is already added to any model
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
   * Adds field definitions to the end of {@link #fieldDefs()}.
   *
   * @param aFieldDefs {@link ITsCollection}&lt;{@link IM5FieldDef}&gt; - field definitions to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any field is already added to any model
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
   * Changes panel creator.
   *
   * @param aCreator {@link M5DefaultPanelCreator} - panel creator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setPanelCreator( M5DefaultPanelCreator<T> aCreator ) {
    TsNullArgumentRtException.checkNull( aCreator );
    panelCreator = aCreator;
    aCreator.papiSetOwnerModel( this );
  }

  /**
   * Sets the {@link #comparator()}.
   *
   * @param aComparator {@link Comparator}&lt;T&gt; - the comparator may be <code>null</code> for no comparison
   */
  public void setComparator( Comparator<T> aComparator ) {
    comparator = aComparator;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may perform additional initialization immediately after {@link #domain()} was initialized.
   * <p>
   * In base class does nothing, there is no need to call superclass method when overriding.
   */
  protected void doInit() {
    // nop
  }

  /**
   * Subclass may create default lifecycle manager if possible.
   * <p>
   * Default lifecycle manager is that one with <code>null</code> master object.
   * <p>
   * Note: this method creates default LM only once. Created LM is stored and returned by subsequent calls to
   * {@link #getLifecycleManager(Object) getLifecycleManager(null)}.
   *
   * @return {@link IM5LifecycleManager}&lt;T&gt; - default lifecycle manager or <code>null</code>
   */
  protected IM5LifecycleManager<T> doCreateDefaultLifecycleManager() {
    return null;
  }

  /**
   * If non-default LM is supposed to exists then subclass must create and return the instance of LM.
   * <p>
   * For <code>null</code> master object (that is for default LM) the method {@link #doCreateDefaultLifecycleManager()}
   * is used.
   * <p>
   * In base class throws an exception, never call superclass method when overriding.
   *
   * @param aMaster Object - the master object, never is <code>null</code>
   * @return {@link IM5LifecycleManager}&lt;T&gt; - created instance of LM
   * @throws ClassCastException may be thrown if master object is of invalid class
   */
  protected IM5LifecycleManager<T> doCreateLifecycleManager( Object aMaster ) {
    throw new TsUnsupportedFeatureRtException( FMT_ERR_NO_LM_CREATOR_CODE, id() );
  }

  /**
   * If Java type check is not sufficient, subclass must perform additional check on if entity is modeled one.
   * <p>
   * This method is called from {@link #isModelledObject(Object)} after ensuring entity is of Java type
   * {@link #entityClass()}.
   * <p>
   * In base class returns <code>true</code>, there is no need to call superclass method when overriding.
   *
   * @param aEntity &lt;T&gt; - non-null entity of type {@link #entityClass}
   * @return boolean - <code>true</code> if entity is modeled by this model
   */
  protected boolean doCheckIsModelledClass( T aEntity ) {
    return true;
  }

}
