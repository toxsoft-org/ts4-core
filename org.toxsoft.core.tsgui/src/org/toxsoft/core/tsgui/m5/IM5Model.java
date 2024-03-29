package org.toxsoft.core.tsgui.m5;

import java.util.*;

import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * M5-model of entities of specified &lt;T&gt; type.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public interface IM5Model<T>
    extends IStridable {

  /**
   * Returns the owning domain of the model.
   *
   * @return {@link IM5Domain} - the owning domain of the model
   * @throws TsIllegalStateRtException method was called before model was added to the domain
   */
  IM5Domain domain();

  /**
   * Returns the class of the modeled entity.
   *
   * @return {@link Class}&lt;T&gt; - the class of the modeled entity
   */
  Class<T> entityClass();

  /**
   * Returns the definitions of all fields.
   *
   * @return {@link IStridablesList}&lt;{@link IM5FieldDef}&lt;T,V&gt;&gt; - the list of the field definitions
   */
  IStridablesList<IM5FieldDef<T, ?>> fieldDefs();

  /**
   * Determines if the specified object is modeled by this model.
   * <p>
   * Generally it is not enough to check if an argument is {@link Class#isInstance(Object)}. For example, all
   * <code>SkObject</code> entities of uSkat framework are based on the same class but must be M5-modlled by the
   * different models.
   * <p>
   * For <code>null</code> argument always returns <code>false</code>.
   *
   * @param aObj {@link Object} - the object to be tested
   * @return boolean - <code>true</code> if argument is modeled by this model
   */
  boolean isModelledObject( Object aObj );

  /**
   * Returns the bunch of the specified modeled entity field values.
   * <p>
   * For <code>null</code> argument returns the bunch where each field has {@link IM5FieldDef#defaultValue()}.
   * <p>
   * In general, a bunch is returned from the {@link IM5Bunch} internal cache. The caching strategy is specified by the
   * M5 subsystems user. By default, the cache is disabled, and on each call of this method the new instance of the
   * {@link IM5Bunch} is created.
   *
   * @param aObj &lt;T&gt; - the modeled entity, may be <code>null</code>
   * @return {@link IM5Bunch} - field values of the specified entity or default values for <code>null</code> argument
   */
  IM5Bunch<T> valuesOf( T aObj );

  /**
   * Creates and returns the new instance of the LM with specified master object if possible.
   *
   * @param aMaster {@link Object} - the master object or <code>null</code> for default LM
   * @return {@link IM5LifecycleManager}&lt;T&gt; - created instance of LM or <code>null</code>
   * @throws ClassCastException may be thrown if master object is of invalid class
   * @throws TsIllegalStateRtException lifecycle manager cann't be created (usually no code for LM creation)
   */
  IM5LifecycleManager<T> findLifecycleManager( Object aMaster );

  /**
   * Creates and returns the new instance of the LM with specified master object.
   *
   * @param aMaster {@link Object} - the master object or <code>null</code> for default LM
   * @return {@link IM5LifecycleManager}&lt;T&gt; - created instance of LM, never is <code>null</code>
   * @throws ClassCastException may be thrown if master object is of invalid class
   * @throws TsIllegalStateRtException no lifecycle manager can be created (usually no code for LM creation)
   * @throws TsItemNotFoundRtException lifecycle manager not created (is <code>null</code>)
   */
  default IM5LifecycleManager<T> getLifecycleManager( Object aMaster ) {
    IM5LifecycleManager<T> lm = findLifecycleManager( aMaster );
    TsItemNotFoundRtException.checkNull( lm );
    return lm;
  }

  /**
   * Return the visualization provider for modeled entity as whole.
   *
   * @return {@link ITsVisualsProvider}&lt;T&gt; - modeled entity visuals provider
   */
  ITsVisualsProvider<T> visualsProvider();

  /**
   * Returns the comparator of the entities if any.
   *
   * @return {@link Comparator}&lt;T&gt; - the comparator or <code>null</code> if entities are not comparable
   */
  Comparator<T> comparator();

  /**
   * Returns the GUI panels creator.
   *
   * @return {@link IM5PanelCreator} - panels creator
   */
  IM5PanelCreator<T> panelCreator();

}
