package org.toxsoft.core.tslib.bricks.ctx.impl;

import static org.toxsoft.core.tslib.bricks.ctx.impl.ITsResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Реализация {@link ITsContext}.
 *
 * @author hazard157
 * @param <P> - the parent context class
 */
public class TsContextBase<P extends ITsContextRo>
    implements ITsContext {

  /**
   * {@link ITsContext#params()} implementation adds event generation and options searching in the parent context.
   *
   * @author hazard157
   */
  class ContextOptions
      extends OptionSet {

    private static final long serialVersionUID = 5922161916792749016L;

    @Override
    protected void doInternalSet( String aId, IAtomicValue aValue ) {
      super.doInternalSet( aId, aValue );
      fireContextOpChanged( aId, aValue );
    }

    @Override
    protected IAtomicValue doInternalFind( String aId ) {
      IAtomicValue val = super.doInternalFind( aId );
      if( val != null ) {
        return val;
      }
      return askParent.findOp( aId );
    }

  }

  private final WeakRefListenersList<ITsContextListener> listeners = new WeakRefListenersList<>();

  private final IOptionSetEdit         ops     = new ContextOptions();
  private final IStringMapEdit<Object> refsMap = new StringMap<>();

  private boolean firingPaused  = false;
  private boolean refWasChanged = false;
  private boolean opWasChanged  = false;

  private final IAskParent askParent;
  private final P          parentTsContext;

  /**
   * Creates an empty context with no parent.
   */
  public TsContextBase() {
    parentTsContext = null;
    askParent = AskParentNone.NONE;
  }

  /**
   * Creates an empty context linked to the parent.
   *
   * @param aParentTsContext &lt;P&gt; - the parent context
   */
  public TsContextBase( P aParentTsContext ) {
    TsNullArgumentRtException.checkNull( aParentTsContext );
    parentTsContext = aParentTsContext;
    askParent = new AskParentTsContext( aParentTsContext );
  }

  /**
   * For descendants.
   *
   * @param aAskParent {@link IAskParent} - parent ops and refs retreival
   */
  protected TsContextBase( IAskParent aAskParent ) {
    TsNullArgumentRtException.checkNull( aAskParent );
    askParent = aAskParent;
    parentTsContext = null;
  }

  // ------------------------------------------------------------------------------------
  // For descendants
  //

  protected IAskParent getAskParent() {
    return askParent;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void fireContextRefChanged( String aName, Object aRef ) {
    if( firingPaused ) {
      refWasChanged = true;
      return;
    }
    for( ITsContextListener l : listeners.list() ) {
      try {
        l.onContextRefChanged( TsContextBase.this, aName, aRef );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  void fireContextOpChanged( String aId, IAtomicValue aValue ) {
    if( firingPaused ) {
      opWasChanged = true;
      return;
    }
    for( ITsContextListener l : listeners.list() ) {
      try {
        l.onContextOpChanged( TsContextBase.this, aId, aValue );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsContextRo
  //

  @Override
  final public IOptionSetEdit params() {
    return ops;
  }

  @Override
  final public P parent() {
    return parentTsContext;
  }

  @Override
  final public <T> T find( Class<T> aClass ) {
    TsNullArgumentRtException.checkNull( aClass );
    String key = aClass.getName();
    Object ref = refsMap.findByKey( key );
    if( ref == null ) {
      ref = askParent.findRef( key );
    }
    return aClass.cast( ref );
  }

  @Override
  final public <T> T get( Class<T> aClass ) {
    T ref = find( aClass );
    TsItemNotFoundRtException.checkNull( ref, FMT_ERR_NO_REF_TO_CLASS_IN_CTX, aClass.getSimpleName() );
    return ref;
  }

  @Override
  public Object find( String aName ) {
    Object ref = refsMap.findByKey( aName );
    if( ref == null ) {
      ref = askParent.findRef( aName );
    }
    return ref;
  }

  @Override
  final public Object get( String aName ) {
    Object ref = find( aName );
    TsItemNotFoundRtException.checkNull( ref, FMT_ERR_NO_REF_OF_KEY_IN_CTX, aName );
    return ref;
  }

  @Override
  final public void addContextListener( ITsContextListener aListener ) {
    listeners.addListener( aListener );
  }

  @Override
  final public void removeContextListener( ITsContextListener aListener ) {
    listeners.removeListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsContext
  //

  @Override
  final public <T> void put( Class<T> aClass, T aRef ) {
    TsNullArgumentRtException.checkNull( aClass );
    String name = aClass.getName();
    refsMap.put( name, aRef );
    fireContextRefChanged( name, aRef );
  }

  @Override
  final public void put( String aName, Object aRef ) {
    refsMap.put( aName, aRef );
    fireContextRefChanged( aName, aRef );
  }

  @Override
  final public void remove( Class<?> aClass ) {
    TsNullArgumentRtException.checkNull( aClass );
    String name = aClass.getName();
    if( refsMap.removeByKey( name ) != null ) {
      fireContextRefChanged( name, null );
    }
  }

  @Override
  final public void remove( String aName ) {
    if( refsMap.removeByKey( aName ) != null ) {
      fireContextRefChanged( aName, null );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsPausabeEventsProducer
  //

  @Override
  final public void pauseFiring() {
    firingPaused = true;
  }

  @Override
  final public void resumeFiring( boolean aFireDelayed ) {
    firingPaused = false;
    if( aFireDelayed && isPendingEvents() ) {
      fireContextRefChanged( null, null );
      fireContextOpChanged( null, null );
    }
    resetPendingEvents();
  }

  @Override
  final public boolean isFiringPaused() {
    return firingPaused;
  }

  @Override
  final public boolean isPendingEvents() {
    return opWasChanged && refWasChanged;
  }

  @Override
  final public void resetPendingEvents() {
    refWasChanged = false;
    opWasChanged = false;
  }

}
