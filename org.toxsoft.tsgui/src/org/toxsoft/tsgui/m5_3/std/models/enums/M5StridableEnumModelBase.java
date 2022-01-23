package org.toxsoft.tsgui.m5_3.std.models.enums;

import org.toxsoft.tsgui.m5_3.model.impl.M5AttributeFieldDef;
import org.toxsoft.tsgui.m5_3.std.fields.M5StdFieldDefDescription;
import org.toxsoft.tsgui.m5_3.std.fields.M5StdFieldDefId;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Superclass for all M5-models of the java <code>enum</code> implementing {@link IStridable}.
 *
 * @author hazard157
 * @param <T> - modelled {@link IStridable} <code>enum</code> class
 */
public class M5StridableEnumModelBase<T extends Enum<T> & IStridable>
    extends M5EnumModelBase<T> {

  /**
   * Атрибут {@link IStridable#id()}.
   */
  public final M5AttributeFieldDef<T> ID = new M5StdFieldDefId<>();

  /**
   * Атрибут {@link IStridable#description()}.
   */
  public final M5AttributeFieldDef<T> DESCRIPTION = new M5StdFieldDefDescription<>();

  /**
   * Constructor.
   *
   * @param aId String - model ID
   * @param aEntityClass {@link Class}&lt;T&gt; - modelled enum class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsItemAlreadyExistsRtException model with specified ID already exists in domain
   */
  public M5StridableEnumModelBase( String aId, Class<T> aEntityClass ) {
    super( aId, aEntityClass );
    addFieldDefs( ID, DESCRIPTION );
  }

}
