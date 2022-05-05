package org.toxsoft.core.tslib.av.opset.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Keeper of the {@link IOptionSet} interface instances.
 * <p>
 * Values returned by <code>read()</code> methods may be safely casted to editable {@link IOptionSetEdit} and evenn more
 * - to the class {@link OptionSet}.
 *
 * @author hazard157
 */
public class OptionSetKeeper
    extends AbstractEntityKeeper<IOptionSet> {

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "OpSet"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<IOptionSet> KEEPER = new OptionSetKeeper( false );

  /**
   * Indented keeper singleton.
   */
  public static final IEntityKeeper<IOptionSet> KEEPER_INDENTED = new OptionSetKeeper( true );

  /**
   * An empty {@link IOptionSet} keeped text representation.
   */
  public static final String STR_EMPTY_OPSET_REPRESENTATION = KEEPER.ent2str( IOptionSet.NULL );

  private final boolean indented;

  private OptionSetKeeper( boolean aIndented ) {
    super( IOptionSet.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null );
    indented = aIndented;
  }

  @Override
  protected void doWrite( IStrioWriter aSw, IOptionSet aEntity ) {
    aSw.writeChar( CHAR_SET_BEGIN );
    // empty option set
    if( aEntity.isEmpty() ) {
      aSw.writeChar( CHAR_SET_END );
      return;
    }
    if( indented ) {
      aSw.incNewLine();
    }
    // write values in form "id = value"
    IStringList ids = aEntity.keys();
    for( int i = 0, n = ids.size(); i < n; i++ ) {
      String name = ids.get( i );
      IAtomicValue value = aEntity.getValue( name );
      aSw.writeAsIs( name );
      aSw.writeChar( CHAR_EQUAL );
      AtomicValueKeeper.KEEPER.write( aSw, value );
      if( i < n - 1 ) {
        aSw.writeChar( CHAR_ITEM_SEPARATOR );
        if( indented ) {
          aSw.writeEol();
        }
      }
    }
    if( indented ) {
      aSw.decNewLine();
    }
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  protected IOptionSet doRead( IStrioReader aSr ) {
    IOptionSetEdit opset = new OptionSet();
    if( aSr.readSetBegin() ) {
      do {
        String id = aSr.readIdPath();
        aSr.ensureChar( CHAR_EQUAL );
        IAtomicValue value = AtomicValueKeeper.KEEPER.read( aSr );
        opset.setValue( id, value );
      } while( aSr.readSetNext() );
    }
    return opset;
  }

}
