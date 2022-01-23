package org.toxsoft.tsgui.mws.quants.progargs;

import static org.toxsoft.tsgui.mws.quants.progargs.ITsResources.*;
import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.equinox.app.IApplicationContext;
import org.toxsoft.tsgui.bricks.quant.AbstractQuant;
import org.toxsoft.tsgui.mws.osgi.IMwsOsgiService;
import org.toxsoft.tslib.bricks.ctx.ITsContextRefDef;
import org.toxsoft.tslib.bricks.ctx.impl.TsContextRefDef;
import org.toxsoft.tslib.utils.progargs.ProgramArgs;

/**
 * Квант разборщика командной строки.
 * <p>
 * Предназначен для разбора командной строки вида<br>
 * <code>program -arg1 [value1] -arg2 [value2] ... -argN [valueN]</code>
 * <p>
 * Результаты {@link ProgramArgs} размещаются в контексте приложения в ссылке {@link #REFDEF_PROGRAM_ARGS}.
 *
 * @author hazard157
 */
public class QuantProgramArgs
    extends AbstractQuant {

  /**
   * Ссылка на аргументы команной строки в контексте приложения.
   */
  public static final ITsContextRefDef<ProgramArgs> REFDEF_PROGRAM_ARGS = TsContextRefDef.create( ProgramArgs.class, //
      TSID_NAME, STR_N_CTX_REF_PROGRAM_ARGS, //
      TSID_DESCRIPTION, STR_D_CTX_REF_PROGRAM_ARGS //
  );

  /**
   * Конструктор.
   */
  public QuantProgramArgs() {
    super( QuantProgramArgs.class.getSimpleName() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    IApplicationContext applicationContext = aAppContext.get( IApplicationContext.class );
    String cmdLineArgs[] = (String[])applicationContext.getArguments().get( IApplicationContext.APPLICATION_ARGS );
    ProgramArgs pa = new ProgramArgs( cmdLineArgs );
    IMwsOsgiService mwsService = eclipseContext().get( IMwsOsgiService.class );
    aAppContext.set( ProgramArgs.class, pa );
    mwsService.context().put( ProgramArgs.class, pa );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // nop
  }

}
