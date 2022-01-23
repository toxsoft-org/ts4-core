package org.toxsoft.tslib.bricks.keeper.std;

import java.io.File;

import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

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
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "File"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<File> KEEPER = new FileKeeper();

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
