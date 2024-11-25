package org.toxsoft.core.tsgui.graphics.colors;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

/**
 * The information how to create the {@link Color} from the different sources.
 * <p>
 * This is an immutable class.
 *
 * @author hazard157
 */
public class TsColorDescriptor
    implements IParameterized, Serializable {

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "TsColorDescriptor"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<TsColorDescriptor> KEEPER =
      new AbstractEntityKeeper<>( TsColorDescriptor.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TsColorDescriptor aEntity ) {
          aSw.writeAsIs( aEntity.kindId() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
        }

        @Override
        protected TsColorDescriptor doRead( IStrioReader aSr ) {
          String kindId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
            q qw
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          return new TsColorDescriptor( kindId, params, 0 );
        }
      };

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  @Override
  public IOptionSet params() {
    return params;
  }

}
