package org.toxsoft.tslib.av.opset.impl;

import static org.toxsoft.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.impl.AtomicValueKeeper;
import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.coll.primtypes.IStringList;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

/**
 * Keeper of the {@link IOptionSet} interface instances.
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
   * Keeper singleton reads new instances of {@link IOptionSetEdit}.
   */
  public static final IEntityKeeper<IOptionSet> KEEPER_READ_NEW_INSTANCES = new OptionSetKeeper( 0 );

  /**
   * An empty {@link IOptionSet} keeped text representation.
   */
  public static final String STR_EMPTY_OPSET_REPRESENTATION = KEEPER.ent2str( IOptionSet.NULL );

  private final boolean indented;

  private OptionSetKeeper( boolean aIndented ) {
    super( IOptionSet.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, IOptionSet.NULL );
    indented = aIndented;
  }

  private OptionSetKeeper( @SuppressWarnings( "unused" ) int aFoo ) {
    super( IOptionSet.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null );
    indented = false;
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
