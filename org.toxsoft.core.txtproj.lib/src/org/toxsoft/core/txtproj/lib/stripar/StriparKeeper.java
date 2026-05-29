package org.toxsoft.core.txtproj.lib.stripar;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * STRIPAR keeper is defined by {@link IStriparCreator}.
 *
 * @author hazard157
 * @param <E> - stridable parameterized entity type
 */
public class StriparKeeper<E extends IStridable & IParameterized>
    extends AbstractStridableParameterizedKeeper<E> {

  private final IStriparCreator<E> creator;

  /**
   * Constructor for not indented keeper.
   *
   * @param aEntityClass Class&lt;E&gt; - type of kept elements
   * @param aNoneObject &lt;E&gt; - none object used to read empty parentheses or <code>null</code>
   * @param aCreator {@link IStriparCreator} - the creator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StriparKeeper( Class<E> aEntityClass, E aNoneObject, IStriparCreator<E> aCreator ) {
    super( aEntityClass, aNoneObject );
    creator = TsNullArgumentRtException.checkNull( aCreator );
  }

  @Override
  protected E doCreate( String aId, IOptionSet aParams ) {
    return creator.create( aId, aParams );
  }

}
