package org.toxsoft.core.tslib.bricks.keeper.std;

import static org.toxsoft.core.tslib.bricks.keeper.std.ITsResources.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * {@link File} keeper.
 *
 * @author hazard157
 */
public class FileKeeper
    extends AbstractEntityKeeper<File> {

  /**
   * Constant read from empty storage.
   */
  public static final File NULL = new File( TsLibUtils.EMPTY_STRING );

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "File"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<File> KEEPER = new FileKeeper();

  /**
   * Registered value-object info.
   */
  public static final ValobjInfo INFO = new ValobjInfo( STR_VALOBJ_FILE, STR_VALOBJ_FILE_D );

  private FileKeeper() {
    super( File.class, EEncloseMode.NOT_IN_PARENTHESES, NULL );
  }

  // ------------------------------------------------------------------------------------
  // AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, File aEntity ) {
    aSw.writeQuotedString( aEntity.getPath() );
  }

  @Override
  protected File doRead( IStrioReader aSr ) {
    String path = aSr.readQuotedString();
    return new File( path );
  }

}
