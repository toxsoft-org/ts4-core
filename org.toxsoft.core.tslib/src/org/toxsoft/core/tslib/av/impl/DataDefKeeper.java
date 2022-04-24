package org.toxsoft.core.tslib.av.impl;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

/**
 * {@link IDataDef} and {@link DataDef} keeper.
 * <p>
 * Values returned by <code>read()</code> methods may be safely casted to editable {@link DataDef}.
 *
 * @author hazard157
 */
public final class DataDefKeeper
    extends AbstractEntityKeeper<IDataDef> {

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "DataDef"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<IDataDef> KEEPER = new DataDefKeeper();

  private DataDefKeeper() {
    super( IDataDef.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, IDataDef aEntity ) {
    aSw.writeAsIs( aEntity.id() );
    aSw.writeSeparatorChar();
    EAtomicType.KEEPER.write( aSw, aEntity.atomicType() );
    aSw.writeSeparatorChar();
    OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
  }

  @Override
  protected IDataDef doRead( IStrioReader aSr ) {
    String id = aSr.readIdPath();
    aSr.ensureSeparatorChar();
    EAtomicType atomicType = EAtomicType.KEEPER.read( aSr );
    aSr.ensureSeparatorChar();
    IOptionSetEdit params = (IOptionSetEdit)OptionSetKeeper.KEEPER.read( aSr );
    return new DataDef( 0, id, atomicType, params );
  }

}
