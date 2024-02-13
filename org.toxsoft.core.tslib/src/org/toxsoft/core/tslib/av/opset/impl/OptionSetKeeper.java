package org.toxsoft.core.tslib.av.opset.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.util.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Keeper of the {@link IOptionSet} interface instances.
 * <p>
 * Values returned by <code>read()</code> methods may be safely casted to editable {@link IOptionSetEdit} and even more
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
   * <p>
   * Values returned by <code>read()</code> methods may be safely casted to the {@link IOptionSetEdit}.
   */
  public static final OptionSetKeeper KEEPER = new OptionSetKeeper( false );

  /**
   * Indented keeper singleton.
   * <p>
   * Values returned by <code>read()</code> methods may be safely casted to the {@link IOptionSetEdit}.
   */
  public static final OptionSetKeeper KEEPER_INDENTED = new OptionSetKeeper( true );

  /**
   * An empty {@link IOptionSet} kept text representation.
   */
  public static final String STR_EMPTY_OPSET_REPRESENTATION = KEEPER.ent2str( IOptionSet.NULL );

  /**
   * An empty {@link IOptionSet} kept atomic value representation.
   */
  public static final IAtomicValue AV_EMPTY_OPSET = AvUtils.avValobj( IOptionSet.NULL, KEEPER, KEEPER_ID );

  /**
   * Returns {@link #KEEPER_INDENTED} or {@link #KEEPER} depending on argument.
   *
   * @param aIndented boolean - <code>true</code> to choose indenting keeper
   * @return {@link IEntityKeeper}&lt;{@link IOptionSet}&gt; - the choosen keeper
   */
  public static final IEntityKeeper<IOptionSet> getInstance( boolean aIndented ) {
    if( aIndented ) {
      return KEEPER_INDENTED;
    }
    return KEEPER;
  }

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
      aSw.writeSpace();
      aSw.writeChar( CHAR_EQUAL );
      aSw.writeSpace();
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

  /**
   * Writes option set omitting options with default values.
   * <p>
   * Any option present both in <code>aEntity</code> and <code>aDefaults</code> with the same values are <b>not</b>
   * written to the output stream.
   * <p>
   * This method is kind of optimization, reducing size of output data. It is assumed that at option set reader knows
   * default values of omitted options and resores them.
   *
   * @param aSw {@link IStrioWriter} - output stream
   * @param aEntity {@link IOptionSet} - the entity to write
   * @param aDefaults {@link IStringMap}&lt;{@link IAtomicValue}&gt; - default values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException stream I/O error
   */
  public void writeWithDefaults( IStrioWriter aSw, IOptionSet aEntity, IStringMap<IAtomicValue> aDefaults ) {
    TsNullArgumentRtException.checkNulls( aSw, aEntity, aDefaults );
    IOptionSetEdit opset = new OptionSet();
    for( String opId : aEntity.keys() ) {
      IAtomicValue opVal = aEntity.getByKey( opId );
      IAtomicValue defVal = aDefaults.findByKey( opId );
      if( !Objects.equals( opVal, defVal ) ) {
        opset.put( opId, opVal );
      }
    }
    write( aSw, aEntity );
  }

  /**
   * Writes option set omitting options with default values.
   * <p>
   * See comments for {@link #writeWithDefaults(IStrioWriter, IOptionSet, IStringMap)}.
   *
   * @param aSw {@link IStrioWriter} - output stream
   * @param aEntity {@link IOptionSet} - the entity to write
   * @param aDefaults {@link IStridablesList}&lt;{@link IDataDef}&gt; - defaults are in {@link IDataDef#defaultValue()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException stream I/O error
   */
  public void writeWithDefaults( IStrioWriter aSw, IOptionSet aEntity, IStridablesList<IDataDef> aDefaults ) {
    TsNullArgumentRtException.checkNulls( aSw, aEntity, aDefaults );
    IOptionSetEdit opset = new OptionSet();
    for( String opId : aEntity.keys() ) {
      IAtomicValue opVal = aEntity.getByKey( opId );
      IDataDef opDef = aDefaults.findByKey( opId );
      if( opDef != null ) {
        IAtomicValue defVal = opDef.defaultValue();
        if( !Objects.equals( opVal, defVal ) ) {
          opset.put( opId, opVal );
        }
      }
      else {
        opset.put( opId, opVal );
      }
    }
    write( aSw, aEntity );
  }

}
