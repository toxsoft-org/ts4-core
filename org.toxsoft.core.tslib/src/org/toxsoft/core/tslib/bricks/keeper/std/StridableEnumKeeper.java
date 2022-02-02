package org.toxsoft.core.tslib.bricks.keeper.std;

import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Basic implementations of the {@link IStridable} {@link Enum} keeper and resolver.
 *
 * @author hazard157
 * @param <E> - keeped {@link Enum} class
 */
public class StridableEnumKeeper<E extends Enum<E> & IStridable>
    extends AbstractEntityKeeper<E> {

  private final Class<E>           enumClass;
  private final IStridablesList<E> enumConsts;

  /**
   * Constructor.
   *
   * @param aEntityClass {@link Class}&ltE&gt; - {@link Enum} class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the specified class is not {@link IStridable}
   * @throws TsIllegalArgumentRtException the specified class is not {@link Enum}
   */
  public StridableEnumKeeper( Class<E> aEntityClass ) {
    super( aEntityClass, EEncloseMode.NOT_IN_PARENTHESES, null );
    TsNullArgumentRtException.checkNull( aEntityClass );
    if( !IStridable.class.isAssignableFrom( aEntityClass ) || !Enum.class.isAssignableFrom( aEntityClass ) ) {
      throw new TsIllegalArgumentRtException();
    }
    enumClass = aEntityClass;
    enumConsts = new StridablesList<>( enumClass.getEnumConstants() );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, E aEntity ) {
    aSw.writeAsIs( aEntity.id() );
  }

  @Override
  protected E doRead( IStrioReader aSr ) {
    String id = aSr.readIdPath();
    return enumConsts.getByKey( id );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the keeped enum class.
   *
   * @return {@link Class}&lt;E&gt; - the keeped enum class
   */
  public Class<E> enumClass() {
    return enumClass;
  }

}
