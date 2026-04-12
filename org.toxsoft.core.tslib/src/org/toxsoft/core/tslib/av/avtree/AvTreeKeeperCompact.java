package org.toxsoft.core.tslib.av.avtree;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * {@link IAvTree} keeper.
 * <p>
 * TODO describe storage format
 *
 * @author hazard157
 */
public class AvTreeKeeperCompact
    extends AbstractEntityKeeper<IAvTree> {

  /**
   * The keeper singleton.
   */
  public static final AvTreeKeeperCompact KEEPER = new AvTreeKeeperCompact();

  private AvTreeKeeperCompact() {
    super( IAvTree.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null );
  }

  // ------------------------------------------------------------------------------------
  // AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, IAvTree aAvTree ) {
    if( !aAvTree.isArray() ) {
      writeNodeContent( aSw, aAvTree );
      return;
    }
    aSw.writeChar( CHAR_ARRAY_BEGIN );
    aSw.indIncLine();
    for( IAvTree n : aAvTree.nodes() ) {
      write( aSw, n );
      if( n != aAvTree.nodes().values().last() ) {
        aSw.writeSeparatorChar();
        aSw.indEol();
      }
    }
    aSw.indDecLine();
    aSw.writeChar( CHAR_ARRAY_END );
  }

  @Override
  protected IAvTree doRead( IStrioReader aSr ) {
    boolean isArray = aSr.peekChar( EStrioSkipMode.SKIP_COMMENTS ) == CHAR_ARRAY_BEGIN;
    if( isArray ) {
      return readArray( aSr );
    }
    return readSingle( aSr );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private IAvTree readSingle( IStrioReader aSr ) {
    IAvTreeEdit t = AvTree.createArrayAvTree();
    if( aSr.readArrayBegin() ) {
      do {
        IAvTree elem = doRead( aSr );
        t.addElement( elem );
      } while( aSr.readArrayNext() );
    }
    return t;
  }

  private IAvTree readArray( IStrioReader aSr ) {
    aSr.ensureChar( CHAR_SET_BEGIN );
    // optional structId
    String structId = EMPTY_STRING;
    if( aSr.peekChar( EStrioSkipMode.SKIP_COMMENTS ) != CHAR_SET_BEGIN ) { // first char of fields OptionSet
      structId = aSr.readIdPath();
      aSr.ensureSeparatorChar();
    }
    // fields
    IOptionSet fields = OptionSetKeeper.KEEPER.read( aSr );
    aSr.ensureChar( CHAR_ITEM_SEPARATOR );
    // nodes
    IStringMapEdit<IAvTree> nodes = IStringMap.EMPTY;
    if( aSr.readArrayBegin() ) {
      nodes = new StringMap<>();
      do {
        String nodeId = aSr.readIdPath();
        aSr.ensureChar( CHAR_EQUAL );
        IAvTree node = doRead( aSr );
        nodes.put( nodeId, node );
      } while( aSr.readArrayNext() );
    }
    aSr.ensureChar( CHAR_SET_END );
    return AvTree.createSingleAvTree( structId, fields, nodes );
  }

  private static void writeStructId( IStrioWriter aSw, String aStructId ) {
    if( !aStructId.isEmpty() ) {
      aSw.indSpace();
      aSw.writeAsIs( aStructId );
      aSw.writeSeparatorChar();
    }
    aSw.indEol();
  }

  private static void writeNodeContent( IStrioWriter aSw, IAvTree aAvTree ) {
    aSw.writeChar( CHAR_SET_BEGIN );
    aSw.indIncLine();
    // optional structId
    writeStructId( aSw, aAvTree.structId() );
    // fields
    OptionSetKeeper.KEEPER.write( aSw, aAvTree.fields() );
    aSw.writeSeparatorChar();
    aSw.indEol();
    // nodes
    StrioUtils.writeStringMap( aSw, EMPTY_STRING, aAvTree.nodes(), KEEPER );
    //
    aSw.indDecLine();
    aSw.writeChar( CHAR_SET_BEGIN );
  }

}
