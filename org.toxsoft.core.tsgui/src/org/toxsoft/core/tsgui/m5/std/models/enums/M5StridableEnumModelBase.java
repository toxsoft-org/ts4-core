package org.toxsoft.core.tsgui.m5.std.models.enums;

import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Superclass for all M5-models of the java <code>enum</code> implementing {@link IStridable}.
 *
 * @author hazard157
 * @param <T> - modeled {@link IStridable} <code>enum</code> class
 */
public class M5StridableEnumModelBase<T extends Enum<T> & IStridable>
    extends M5EnumModelBase<T> {

  /**
   * Attribute {@link IStridable#id()}.
   */
  public final M5AttributeFieldDef<T> ID = new M5StdFieldDefId<>();

  /**
   * Attribute {@link IStridable#nmName()}.
   */
  public final M5AttributeFieldDef<T> NAME = new M5StdFieldDefName<>();

  /**
   * Attribute {@link IStridable#description()}.
   */
  public final M5AttributeFieldDef<T> DESCRIPTION = new M5StdFieldDefDescription<>();

  /**
   * Constructor.
   *
   * @param aId String - model ID
   * @param aEntityClass {@link Class}&lt;T&gt; - modeled enum class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsItemAlreadyExistsRtException model with specified ID already exists in domain
   */
  public M5StridableEnumModelBase( String aId, Class<T> aEntityClass ) {
    super( aId, aEntityClass );
    addFieldDefs( ID, NAME, DESCRIPTION );
  }

}
