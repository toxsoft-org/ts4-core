package org.toxsoft.unit.txtproj.core.impl;

import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.av.opset.impl.OptionSetKeeper;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.bricks.strio.impl.StrioUtils;

/**
 * {@link TsProjectFileFormatInfo} keeper.
 *
 * @author hazard157
 */
public class TsProjectFileFormatInfoKeeper
    extends AbstractEntityKeeper<TsProjectFileFormatInfo> {

  /**
   * Keeper ID.
   */
  public static final String KEEPER_ID = "TsProjectFileFormatInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<TsProjectFileFormatInfo> KEEPER = new TsProjectFileFormatInfoKeeper();

  private static final String KW_SECTION_NAME = "TsProjectFileInfo"; //$NON-NLS-1$

  private TsProjectFileFormatInfoKeeper() {
    super( TsProjectFileFormatInfo.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, TsProjectFileFormatInfo aEntity ) {
    StrioUtils.writeKeywordHeader( aSw, KW_SECTION_NAME, true );
    OptionSetKeeper.KEEPER_INDENTED.write( aSw, aEntity.params );
  }

  @Override
  protected TsProjectFileFormatInfo doRead( IStrioReader aSr ) {
    StrioUtils.ensureKeywordHeader( aSr, KW_SECTION_NAME );
    IOptionSet opset = OptionSetKeeper.KEEPER.read( aSr );
    return new TsProjectFileFormatInfo( opset );
  }
}
