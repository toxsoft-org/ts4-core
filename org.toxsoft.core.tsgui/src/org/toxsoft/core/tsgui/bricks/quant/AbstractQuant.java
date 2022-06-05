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
   * Имя ссылки в контексте, содержащий {@link Boolean#TRUE} как признак вызова метода
   * {@link #initApp(IEclipseContext)}.
   */
  public final String CTX_REF_NAME_APP_INIT_CONTEXT_FLAG;

  /**
   * Имя ссылки в контексте, содержащий {@link Boolean#TRUE} как признак вызова метода
   * {@link #initWin(IEclipseContext)}.
   */
  public final String CTX_REF_NAME_WIN_INIT_CONTEXT_FLAG;

  /**
   * Контекст приложения, сначала уровня приложения, потом уровня окна.
   */
  private IEclipseContext appContext = null;

  /**
   * Дочерние кванты.
   */
  private final IListEdit<IQuant> quants = new ElemLinkedBundleList<>();

  private final String name; // имя кванта

  /**
   * Constructor.
   *
   * @param aQuantName String - non-blank quant name, must be uinque for all quants in application
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
    if( appContext == null ) {
      LoggerUtils.errorLogger().warning( FMT_WARN_CLOSE_UNOPENED, name );
      return;
    }
    // finalizing child quants
    while( !quants.isEmpty() ) {
      IQuant q = quants.removeByIndex( quants.size() - 1 );
      q.close();
    }
    // finalizing this qunat
    LoggerUtils.defaultLogger().info( FMT_INFO_QUANT_CLOSE, name );
    try {
      doClose();
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex, FMT_ERR_CLOSING_QUANT, name );
    }
    setInitFlag( appContext, CTX_REF_NAME_APP_INIT_CONTEXT_FLAG, false );
    setInitFlag( appContext, CTX_REF_NAME_WIN_INIT_CONTEXT_FLAG, false );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return "Quant '" + name + "'"; //$NON-NLS-1$ //$NON-NLS-2$
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    // quant equality check is just name equality check
    if( aThat instanceof AbstractQuant that ) {
      return this.name.equals( that.name );
    }
    return false;
  }

  @Override
  public int hashCode() {
    return name.hashCode();
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
    // check init state
    if( getInitFlag( aAppContext, CTX_REF_NAME_APP_INIT_CONTEXT_FLAG ) ) {
      LoggerUtils.errorLogger().warning( FMT_WARN_QUANT_DUP_INIT_APP, name );
      return;
    }
    LoggerUtils.defaultLogger().info( FMT_INFO_QUANT_INIT_APP, name );
    TsNullArgumentRtException.checkNull( aAppContext );
    appContext = aAppContext;
    // init child quants
    for( IQuant q : quants ) {
      q.initApp( aAppContext );
    }
    // init this quant
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
    // check init state
    if( getInitFlag( aWinContext, CTX_REF_NAME_WIN_INIT_CONTEXT_FLAG ) ) {
      LoggerUtils.errorLogger().warning( FMT_WARN_QUANT_DUP_INIT_WIN, name );
      return;
    }
    LoggerUtils.defaultLogger().info( FMT_INFO_QUANT_INIT_WIN, name );
    TsNullArgumentRtException.checkNull( aWinContext );
    appContext = aWinContext;
    // init child quants
    for( IQuant q : quants ) {
      q.initWin( appContext );
    }
    // init this quant
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
    return true;
  }

  // ------------------------------------------------------------------------------------
  // IQuantRegistrator
  //

  @Override
  final public void registerQuant( IQuant aQuant ) {
    TsIllegalStateRtException.checkNoNull( appContext );
    if( !quants.hasElem( aQuant ) ) {
      quants.add( aQuant );
    }
  }

  // ------------------------------------------------------------------------------------
  // For subclesses
  //

  /**
   * Returns the context {@link IEclipseContext}.
   * <p>
   * When implementing {@link #doInitApp(IEclipseContext)} returns application level context, in methods
   * {@link #doInitWin(IEclipseContext)} and in {@link #doClose()} return the windows level context.
   *
   * @return {@link IEclipseContext} - application or windows level context
   */
  public IEclipseContext eclipseContext() {
    return appContext;
  }

  // ------------------------------------------------------------------------------------
  // Override
  //

  /**
   * Наследник должен определить действия при инициалиации кванта на уровне приложения.
   * <p>
   * Вызов метода обрамлен конструкцией <code>try-catch</code>, логирующий исключения так, что метод
   * {@link #initApp(IEclipseContext)} не выбрасывает исключение.
   *
   * @param aAppContext {@link IEclipseContext} - application level context
   */
  protected abstract void doInitApp( IEclipseContext aAppContext );

  /**
   * Наследник должен определить действия при инициалиации кванта на уровне окна.
   * <p>
   * Вызов метода обрамлен конструкцией <code>try-catch</code>, логирующий исключения так, что метод
   * {@link #initWin(IEclipseContext)} не выбрасывает исключение.
   *
   * @param aWinContext {@link IEclipseContext} - window level context
   */
  protected abstract void doInitWin( IEclipseContext aWinContext );

  /**
   * Наследник может осуществить действия по завершении работы кванта после дочерних квантов.
   * <p>
   * В базовом классе ничего не делает, при переопределении вызывать родительский метод не надо.
   * <p>
   * Вызов метода обрамлен конструкцией <code>try-catch</code>, логирующий исключения так, что метод {@link #close()} не
   * выбрасывает исключение.
   */
  protected void doClose() {
    // nop
  }

}
