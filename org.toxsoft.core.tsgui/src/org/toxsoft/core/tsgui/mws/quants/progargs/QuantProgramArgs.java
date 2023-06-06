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
 * Quant parses command line and puts resulting {@link ProgramArgs} into the application level context.
 * <p>
 * Designed to parse the command line like<br>
 * <code>program -arg1 [value1] -arg2 [value2] ... -argN [valueN]</code>
 * <p>
 * There is constant {@link #REFDEF_PROGRAM_ARGS} describing the reference to the {@link ProgramArgs} instance in the
 * context.
 *
 * @author hazard157
 */
public class QuantProgramArgs
    extends AbstractQuant {

  /**
   * Reference to the instance of {@link ProgramArgs}.
   * <p>
   * Reference is also accessible simple by class via {@link IEclipseContext#get(Class)}.
   */
  public static final ITsContextRefDef<ProgramArgs> REFDEF_PROGRAM_ARGS = TsContextRefDef.create( ProgramArgs.class, //
      TSID_NAME, STR_N_CTX_REF_PROGRAM_ARGS, //
      TSID_DESCRIPTION, STR_D_CTX_REF_PROGRAM_ARGS //
  );

  /**
   * Constructor.
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
