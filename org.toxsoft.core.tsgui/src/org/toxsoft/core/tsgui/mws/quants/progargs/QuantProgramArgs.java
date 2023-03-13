package org.toxsoft.core.tsgui.mws.quants.progargs;

import static org.toxsoft.core.tsgui.mws.quants.progargs.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.equinox.app.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.utils.progargs.*;

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
    IMwsOsgiService mwsService = aAppContext.get( IMwsOsgiService.class );
    aAppContext.set( ProgramArgs.class, pa );
    mwsService.context().put( ProgramArgs.class, pa );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // nop
  }

}
