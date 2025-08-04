package org.toxsoft.core.tsgui.bricks.quant;

import static org.toxsoft.core.tsgui.bricks.quant.ITsResources.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Abstract implementation of the {@link IQuant}.
 *
 * @author hazard157
 */
public abstract class AbstractQuant
    implements IQuant {

  /**
   * Name of the reference in the context containing {@link Boolean#TRUE} as the flag that
   * {@link #initApp(IEclipseContext)} was called.
   */
  public final String CTX_REF_NAME_APP_INIT_CONTEXT_FLAG;

  /**
   * Name of the reference in the context containing {@link Boolean#TRUE} as the flag that
   * {@link #initWin(IEclipseContext)} was called.
   */
  public final String CTX_REF_NAME_WIN_INIT_CONTEXT_FLAG;

  /**
   * Child quants.
   */
  private final IListEdit<IQuant> quants = new ElemLinkedBundleList<>();

  /**
   * The quant name.
   */
  private final String name;

  /**
   * Constructor.
   *
   * @param aQuantName String - non-blank quant name, must be unique for all quants in application
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is blank string
   */
  protected AbstractQuant( String aQuantName ) {
    TsErrorUtils.checkNonBlank( aQuantName );
    name = aQuantName;
    CTX_REF_NAME_APP_INIT_CONTEXT_FLAG = "AppContextQuantInitFlag - " + name; //$NON-NLS-1$
    CTX_REF_NAME_WIN_INIT_CONTEXT_FLAG = "WinContextQuantInitFlag - " + name; //$NON-NLS-1$
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static boolean getInitFlag( IEclipseContext aContext, String aFlagName ) {
    TsNullArgumentRtException.checkNulls( aContext, aFlagName );
    Object ref = aContext.get( aFlagName );
    if( ref instanceof Boolean b ) {
      return b == Boolean.TRUE;
    }
    return false;
  }

  private static boolean setInitFlag( IEclipseContext aContext, String aFlagName, boolean aFlag ) {
    boolean oldFlag = getInitFlag( aContext, aFlagName );
    if( aFlag != oldFlag ) {
      if( aFlag ) {
        aContext.set( aFlagName, Boolean.TRUE );
      }
      else {
        aContext.remove( aFlagName );
      }
    }
    return oldFlag;
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    // finalizing child quants
    while( !quants.isEmpty() ) {
      IQuant q = quants.removeByIndex( quants.size() - 1 );
      q.close();
    }
    // finalizing this quant
    LoggerUtils.defaultLogger().info( FMT_INFO_QUANT_CLOSE, name );
    try {
      doClose();
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex, FMT_ERR_CLOSING_QUANT, name );
    }
  }

  // ------------------------------------------------------------------------------------
  // IQuant
  //

  @Override
  final public String name() {
    return name;
  }

  @Override
  final public void initApp( IEclipseContext aAppContext ) {
    // check context initialization state
    if( getInitFlag( aAppContext, CTX_REF_NAME_APP_INIT_CONTEXT_FLAG ) ) {
      LoggerUtils.errorLogger().warning( FMT_WARN_QUANT_DUP_INIT_APP, name );
      return;
    }
    LoggerUtils.defaultLogger().info( FMT_INFO_QUANT_INIT_APP, name );
    TsNullArgumentRtException.checkNull( aAppContext );
    // this quant, before children
    try {
      doInitAppBeforeChildQuants( aAppContext );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex, FMT_ERR_APP_INIT_QUANT, name );
    }
    // child quants
    for( IQuant q : quants ) {
      q.initApp( aAppContext );
    }
    // this quant, after children
    try {
      doInitApp( aAppContext );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex, FMT_ERR_APP_INIT_QUANT, name );
    }
    setInitFlag( aAppContext, CTX_REF_NAME_APP_INIT_CONTEXT_FLAG, true );
  }

  @Override
  final public void initWin( IEclipseContext aWinContext ) {
    TsNullArgumentRtException.checkNull( aWinContext );
    // check init state
    if( getInitFlag( aWinContext, CTX_REF_NAME_WIN_INIT_CONTEXT_FLAG ) ) {
      LoggerUtils.errorLogger().warning( FMT_WARN_QUANT_DUP_INIT_WIN, name );
      return;
    }
    LoggerUtils.defaultLogger().info( FMT_INFO_QUANT_INIT_WIN, name );
    // init this quant, beforechildren
    try {
      doInitWinBeforeChildQuants( aWinContext );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex, FMT_ERR_WIN_INIT_QUANT, name );
    }
    // init child quants
    for( IQuant q : quants ) {
      q.initWin( aWinContext );
    }
    // init this quant, after children
    try {
      doInitWin( aWinContext );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex, FMT_ERR_WIN_INIT_QUANT, name );
    }
    setInitFlag( aWinContext, CTX_REF_NAME_WIN_INIT_CONTEXT_FLAG, true );
  }

  @Override
  public boolean canCloseMainWindow( IEclipseContext aWinContext, MWindow aWindow ) {
    for( IQuant q : quants ) {
      try {
        if( !q.canCloseMainWindow( aWinContext, aWindow ) ) {
          return false;
        }
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
    return doCanCloseMainWindow( aWinContext, aWindow );
  }

  @Override
  public void whenCloseMainWindow( IEclipseContext aWinContext, MWindow aWindow ) {
    for( IQuant q : quants ) {
      try {
        q.whenCloseMainWindow( aWinContext, aWindow );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
    try {
      doCloseWin( aWindow );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
    setInitFlag( aWinContext, CTX_REF_NAME_WIN_INIT_CONTEXT_FLAG, false );
  }

  // ------------------------------------------------------------------------------------
  // IQuantRegistrator
  //

  @Override
  final public void registerQuant( IQuant aQuant ) {
    if( !quants.hasElem( aQuant ) ) {
      quants.add( aQuant );
    }
  }

  // ------------------------------------------------------------------------------------
  // Override
  //

  /**
   * Subclass may perform application level initialization (once when application starts).
   * <p>
   * This method is called before child quants {@link IQuant#initApp(IEclipseContext)}.
   * <p>
   * Does nothing in the base class. No need to call parent method when overriding.
   *
   * @param aAppContext {@link IEclipseContext} - application level context
   */
  protected void doInitAppBeforeChildQuants( IEclipseContext aAppContext ) {
    // nop
  }

  /**
   * Subclass may perform window level initialization (once per window).
   * <p>
   * This method is called before child quants {@link IQuant#initWin(IEclipseContext)}.
   * <p>
   * Does nothing in the base class. No need to call parent method when overriding.
   *
   * @param aWinContext {@link IEclipseContext} - window level context
   */
  protected void doInitWinBeforeChildQuants( IEclipseContext aWinContext ) {
    // nop
  }

  /**
   * Subclass may perform application level initialization (once when application starts).
   * <p>
   * This method is called after child quants {@link IQuant#initApp(IEclipseContext)}.
   *
   * @param aAppContext {@link IEclipseContext} - application level context
   */
  protected abstract void doInitApp( IEclipseContext aAppContext );

  /**
   * Subclass may perform window level initialization (once per window).
   * <p>
   * This method is called after child quants {@link IQuant#initWin(IEclipseContext)}.
   *
   * @param aWinContext {@link IEclipseContext} - window level context
   */
  protected abstract void doInitWin( IEclipseContext aWinContext );

  /**
   * The implementation may intercept and, if necessary, prevent the attempt to close the window.
   * <p>
   * Returns <code>true</code> in the base class there is need to call parent method when overriding.
   *
   * @param aWinContext {@link IEclipseContext} - the window context
   * @param aWindow {@link MWindow} - the window to be closed
   * @return boolean - permission to close the window<br>
   *         <b>true</b> - the window will be closed;<br>
   *         <b>false</b> - the window will remain open.
   */
  protected boolean doCanCloseMainWindow( IEclipseContext aWinContext, MWindow aWindow ) {
    return true;
  }

  /**
   * Subclass may process window closing, eg release resources allocated in {@link #doInitWin(IEclipseContext)}.
   * <p>
   * Does nothing in the base class there is need to call parent method when overriding.
   *
   * @param aWindow {@link MWindow} - the window to be closed
   */
  protected void doCloseWin( MWindow aWindow ) {
    // nop
  }

  /**
   * Subclass may perform clean-up before application quits.
   * <p>
   * Does nothing in the base class there is need to call parent method when overriding.
   */
  protected void doClose() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return "Quant '" + name + "'"; //$NON-NLS-1$ //$NON-NLS-2$
  }

}
