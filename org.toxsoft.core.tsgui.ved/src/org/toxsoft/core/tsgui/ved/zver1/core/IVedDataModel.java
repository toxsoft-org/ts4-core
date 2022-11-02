package org.toxsoft.core.tsgui.ved.zver1.core;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Data model edited by the VED framework.
 *
 * @author hazard157
 */
public interface IVedDataModel
    extends ITsClearable, // to implement "New" command
    IGenericChangeEventCapable // inform about any user edits
{

  /**
   * Return canvas and background configuration options.
   *
   * @return {@link INotifierOptionSetEdit} - editable canvas config data model
   */
  INotifierOptionSetEdit canvasConfig();

  /**
   * Returns the components.
   *
   * @return {@link INotifierStridablesListEdit}&lt;{@link IVedComponent}&gt; - components data model
   */
  INotifierStridablesList<IVedComponent> listComponents();

  /**
   * Adds new component to the end of list {@link #listComponents()}.
   *
   * @param aComponent {@link IVedComponent} - component to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException component with such ID already exists
   */
  void addComponent( IVedComponent aComponent );

  /**
   * Adds the component to the specified position in list {@link #listComponents()}.
   *
   * @param aIndex int - index of the element to insert (in range 0..list size)
   * @param aComponent {@link IVedComponent} - component to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException component with such ID already exists
   */
  void insertComponent( int aIndex, IVedComponent aComponent );

  /**
   * Removes the specified component.
   * <p>
   * Unexistant ID is ignored
   *
   * @param aComponentId String - component ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeComponent( String aComponentId );

  /**
   * Returns components list {@link #listComponents()} reorderer.
   *
   * @return {@link IListReorderer}&lt;{@link IVedComponent}&gt; - compnents list reorderer
   */
  IListReorderer<IVedComponent> compsReorderer();

}
