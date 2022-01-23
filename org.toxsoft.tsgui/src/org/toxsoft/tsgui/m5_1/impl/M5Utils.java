package org.toxsoft.tsgui.m5_1.impl;

import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.tsgui.m5_1.IM5Constants;
import org.toxsoft.tsgui.m5_1.api.IM5Domain;
import org.toxsoft.tsgui.m5_1.std.FileM5Model;
import org.toxsoft.tslib.utils.errors.TsInternalErrorRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * M5 utility methods.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class M5Utils {

  /**
   * The root and only root domain ID.
   */
  public static final String ROOT_DOMAIN_ID = IM5Constants.M5_ID + ".root"; //$NON-NLS-1$

  /**
   * One-time initialization of the window level context.
   * <p>
   * Yet does nothing.
   *
   * @param aAppContext {@link IEclipseContext} - application level context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void initAppContext( IEclipseContext aAppContext ) {
    // TODO init ITsFilter registry ???
  }

  /**
   * One-time initialization of the window level context.
   * <p>
   * Creates and puts in the context the new instance of the root M5-domain.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void initWinContext( IEclipseContext aWinContext ) {
    TsNullArgumentRtException.checkNull( aWinContext );
    TsInternalErrorRtException.checkNoNull( aWinContext.get( IM5Domain.class ) );
    ITsGuiContext ctx = new TsGuiContext( aWinContext );
    ctx.params().setStr( TSID_NAME, ROOT_DOMAIN_ID );
    ctx.params().setStr( TSID_DEFAULT_VALUE, "The root M5-domain" ); //$NON-NLS-1$
    M5Domain m5 = new M5Domain( ROOT_DOMAIN_ID, ctx );
    aWinContext.set( IM5Domain.class, m5 );
    initBuiltinModels( m5 );
  }

  public static void initBuiltinModels( IM5Domain aDomain ) {
    aDomain.addModel( new FileM5Model() );
    // TODO aDomain.addModel( new LongM5Model() );
    // TODO aDomain.addModel( new StringM5Model() );
    // TODO aDomain.addModel( new TsVersionM5Model() );
    // TODO aDomain.addModel( new ValidationResultTypeM5Model() );
    // TODO aDomain.addModel( new AtomicTypeM5Model() );
    // TODO aDomain.addModel( new AtomicValueM5Model() );
    // TODO aDomain.addModel( new IdValueM5Model() );
    // TODO aDomain.addModel( new OptionSetM5Model() );
    // TODO aDomain.addModel( new DataTypeM5Model() );
    // TODO aDomain.addModel( new DvInfoM5Model() );
    // TODO aDomain.addModel( new TsWeekDayM5Model() );
    // TODO aDomain.addModel( new LocalTimeM5Model() );
    // TODO aDomain.addModel( new LocalDateM5Model() );
    // TODO aDomain.addModel( new LocalDateTimeM5Model() );
  }

  /**
   * Запрет на создание экземпляров.
   */
  private M5Utils() {
    // nop
  }

}
