package org.toxsoft.tslib.bricks.filter.impl;

import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.av.opset.impl.OptionSetKeeper;
import org.toxsoft.tslib.bricks.filter.ITsSingleFilterParams;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;

/**
 * Keeper of the {@link ITsSingleFilterParams} objects.
 *
 * @author hazard157
 */
public class TsSingleFilterParamsKeeper
    extends AbstractEntityKeeper<ITsSingleFilterParams> {

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ITsSingleFilterParams> KEEPER = new TsSingleFilterParamsKeeper();

  private TsSingleFilterParamsKeeper() {
    super( ITsSingleFilterParams.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, ITsSingleFilterParams aEntity ) {
    aSw.writeAsIs( aEntity.typeId() );
    aSw.writeSeparatorChar();
    OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
  }

  @Override
  protected ITsSingleFilterParams doRead( IStrioReader aSr ) {
    String typeId = aSr.readIdPath();
    aSr.ensureSeparatorChar();
    IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
    return new TsSingleFilterParams( typeId, params );
  }

}
