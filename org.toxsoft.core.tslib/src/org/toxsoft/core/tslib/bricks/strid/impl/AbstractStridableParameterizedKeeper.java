package org.toxsoft.core.tslib.bricks.strid.impl;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link StridableParameterized} keeper base implementation.
 *
 * @param <T> - type of kept elements
 */
public abstract class AbstractStridableParameterizedKeeper<T extends IStridable & IParameterized>
    extends AbstractEntityKeeper<T> {

  private final boolean indented;

  /**
   * Constructor.
   *
   * @param aEntityClass Class&lt;E&gt; - type of kept elements
   * @param aNoneObject &lt;E&gt; - none object used to read empty parentheses or <code>null</code>
   * @param aIndented boolean - <code>true</code> to choose indenting keeper
   */
  protected AbstractStridableParameterizedKeeper( Class<T> aEntityClass, T aNoneObject, boolean aIndented ) {
    super( aEntityClass, EEncloseMode.ENCLOSES_BASE_CLASS, aNoneObject );
    indented = aIndented;
  }

  /**
   * Constructor for not indented keeper.
   *
   * @param aEntityClass Class&lt;E&gt; - type of kept elements
   * @param aNoneObject &lt;E&gt; - none object used to read empty parentheses or <code>null</code>
   */
  protected AbstractStridableParameterizedKeeper( Class<T> aEntityClass, T aNoneObject ) {
    this( aEntityClass, aNoneObject, false );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, T aEntity ) {
    aSw.writeAsIs( aEntity.id() );
    aSw.writeSeparatorChar();
    if( indented ) {
      OptionSetKeeper.KEEPER_INDENTED.write( aSw, aEntity.params() );
    }
    else {
      OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
    }
  }

  @Override
  protected T doRead( IStrioReader aSr ) {
    String id = aSr.readIdPath();
    aSr.ensureSeparatorChar();
    IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
    T obj = doCreate( id, params );
    TsInternalErrorRtException.checkNull( obj );
    return obj;
  }

  // ------------------------------------------------------------------------------------
  // To override/indent
  //

  /**
   * Subclass must create instance of the concrete type &lt;T&gt;.
   *
   * @param aId String - the ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link IStridableParameterized#params()} initial values
   * @return &lt;T&gt; - created instance
   */
  protected abstract T doCreate( String aId, IOptionSet aParams );

}
