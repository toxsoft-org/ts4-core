package org.toxsoft.core.tslib.av.metainfo;

import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The registry of the {@link IDataDef} - atomic data definitions.
 *
 * @author hazard157
 */
public interface IAtomicDataRegistry {

  /**
   * Finds the defniition by it's ID.
   * <p>
   * Method searches in {@link #defs()} and if not found searches in parent registry.
   * <p>
   * Note: data definition in parent with the same ID is hidden by the definition from this list.
   *
   * @param aId String - ID of the data definition
   * @return {@link IDataDef} - found definition or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IDataDef findDef( String aId );

  /**
   * Returns the defniition by it's ID.
   * <p>
   * Method searches in {@link #defs()} and if not found searches in parent registry.
   * <p>
   * Note: data definition in parent with the same ID is hidden by the definition from this list.
   *
   * @param aId String - ID of the data definition
   * @return {@link IDataDef} - found definition, never is <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no data defnition with specified ID found
   */
  IDataDef getDef( String aId );

  /**
   * Returns the definitions in this registry, without parent defs.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - definitions in this registry
   */
  IStridablesList<IDataDef> defs();

  /**
   * The {@link IDataDef#params()} processing mode when registering similar definitions.
   * <p>
   * As in {@link IAtomicDataRegistry#registerDef(IDataDef, EParamsOverwriteMode)}, the similar definition means
   * definitions with the same {@link IDataDef#id()} and {@link IDataDef#atomicType()} but with differing
   * {@link IDataDef#params()}.
   *
   * @author hazard157
   */
  enum EParamsOverwriteMode {

    /**
     * The exception wil be thrown when trying to register similar data definition.
     */
    FAIL,

    /**
     * The existing option will remain unchanged, while non-exsitant options will be added to {@link IDataDef#params()}.
     */
    RETAIN,

    /**
     * The data definition will be replaced by the definition to be registered.
     */
    OVERRIDE

  }

  /**
   * Registers the data definition.
   * <p>
   * <code>aMode</code> arguments determines how to handle similar definitions parameters. The similar definition means
   * definitions with the same {@link IDataDef#id()} and {@link IDataDef#atomicType()} but with differing
   * {@link IDataDef#params()}. Note that only definitions from {@link #defs()} local list is considered. Defs from the
   * parent registries are silently hidden.
   *
   * @param aDef {@link IDataDef} - the definition to be registered
   * @param aMode {@link EParamsOverwriteMode} - how to handle similar definitions parameters
   * @return {@link IDataDef} - the registered definition
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException registering similar defs in {@link EParamsOverwriteMode#FAIL} mode
   * @throws TsIllegalArgumentRtException def with same ID but different atomic type already exists (maybe in parent)
   */
  IDataDef registerDef( IDataDef aDef, EParamsOverwriteMode aMode );

  /**
   * Returns the parent registry.
   *
   * @return {@link IAtomicDataRegistry} - the parent registry or <code>null</code> for root registry
   */
  IAtomicDataRegistry parent();

}
