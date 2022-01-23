package org.toxsoft.unit.txtproj.gui.m5.stripar;

import org.toxsoft.tsgui.m5.model.impl.M5AttributeFieldDef;
import org.toxsoft.tsgui.m5.model.impl.M5Model;
import org.toxsoft.tsgui.m5.std.fields.*;
import org.toxsoft.tslib.av.utils.IParameterized;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Base class for STRIPAR entities M5-model.
 *
 * @author hazard157
 * @param <E> - STRIPAR type
 */
public class StriparM5Model<E extends IStridable & IParameterized>
    extends M5Model<E> {

  /**
   * Attribute {@link IStridable#id()}
   */
  public final M5AttributeFieldDef<E> ID = new M5StdFieldDefId<>();

  /**
   * Attribute {@link IStridable#nmName()}
   */
  public final M5AttributeFieldDef<E> NAME = new M5StdFieldDefName<>();

  /**
   * Attribute {@link IStridable#description()}
   */
  public final M5AttributeFieldDef<E> DESCRIPTION = new M5StdFieldDefDescription<>();

  /**
   * Constructor.
   *
   * @param aId String - model ID
   * @param aEntityClass {@link Class}&lt;E&gt; - STRIPAR entity type
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsItemAlreadyExistsRtException model with specified ID already exists in domain
   */
  public StriparM5Model( String aId, Class<E> aEntityClass ) {
    super( aId, aEntityClass );
    addFieldDefs( ID, NAME, DESCRIPTION );
  }

}
