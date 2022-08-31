package org.toxsoft.core.tslib.av.avtree;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

/**
 * Реализация {@link AbstractEntityKeeper} для {@link IAvTreePathElement}.
 *
 * @author goga
 */
class AvTreePathElementKeeper
    extends AbstractEntityKeeper<IAvTreePathElement> {

  static final AvTreePathElementKeeper KEEPER = new AvTreePathElementKeeper();

  private AvTreePathElementKeeper() {
    super( IAvTreePathElement.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null );
  }

  // ------------------------------------------------------------------------------------
  // AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, IAvTreePathElement aElement ) {
    aSw.writeChar( CHAR_SET_BEGIN );
    aSw.writeBoolean( aElement.isNode() );
    aSw.writeSeparatorChar();
    aSw.writeAsIs( aElement.id() );
    aSw.writeSeparatorChar();
    aSw.writeBoolean( aElement.isArrayElement() );
    aSw.writeSeparatorChar();
    aSw.writeInt( aElement.arrayElementIndex() );
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  protected IAvTreePathElement doRead( IStrioReader aSr ) {
    aSr.ensureChar( CHAR_SET_BEGIN );
    boolean isNode = aSr.readBoolean();
    aSr.ensureSeparatorChar();
    String id = aSr.readIdPath();
    aSr.ensureSeparatorChar();
    boolean isArray = aSr.readBoolean();
    aSr.ensureSeparatorChar();
    int arrayIndex = aSr.readInt();
    aSr.ensureChar( CHAR_SET_END );
    return new AvTreePathElement( isNode, id, isArray, arrayIndex );
  }

}
